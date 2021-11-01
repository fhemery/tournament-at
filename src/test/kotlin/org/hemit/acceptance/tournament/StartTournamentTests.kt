package org.hemit.acceptance.tournament

import org.hemit.BaseAcceptanceTest
import org.hemit.domain.model.RoundRobinTournamentPhase
import org.hemit.domain.model.tournament.OngoingTournament
import org.hemit.domain.model.tournament.TournamentStatus
import org.hemit.domain.ports.input.commands.StartTournamentCommand
import org.hemit.domain.ports.input.commands.StartTournamentResult
import org.hemit.domain.ports.output.GetTournamentResult
import org.hemit.utils.builders.TournamentTestBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.hasSize
import strikt.assertions.isA
import strikt.assertions.isEqualTo

class StartTournamentTests : BaseAcceptanceTest() {

    lateinit var startTournamentCommand: StartTournamentCommand

    @BeforeEach
    fun setup() {
        startTournamentCommand = StartTournamentCommand(tournamentStoragePort)
    }

    @Test
    fun `should launch the tournament`() {
        val initialTournament =
            TournamentTestBuilder().withRandomParticipants(8).withPhase(RoundRobinTournamentPhase())
                .build()
        tournamentStoragePort.saveTournament(initialTournament)

        val result = startTournamentCommand.execute(initialTournament.id)

        expectThat(result).isA<StartTournamentResult.Success>()

        val tournamentInStorage = tournamentStoragePort.getTournament(initialTournament.id)
        expectThat(tournamentInStorage).isA<GetTournamentResult.Success>().and {
            get { tournament.status }.isEqualTo(TournamentStatus.Started)
        }
    }

    @Test
    fun `should return an error if there are not enough participants`() {
        val tournament =
            TournamentTestBuilder().withRandomParticipants(1).withPhase(RoundRobinTournamentPhase())
                .build()
        tournamentStoragePort.saveTournament(tournament)

        val result = startTournamentCommand.execute(tournament.id)

        expectThat(result).isA<StartTournamentResult.NotEnoughParticipants>()
    }

    @Test
    fun `should return an error if tournament does not exist`() {
        val result = startTournamentCommand.execute("Unknown")

        expectThat(result).isA<StartTournamentResult.TournamentDoesNotExist>()
    }

    @Test
    fun `should return an error if tournament is already started (or finished)`() {
        val tournament =
            TournamentTestBuilder().withRandomParticipants(8).withPhase(RoundRobinTournamentPhase())
                .build()
        val startedTournament = tournament.start()
        tournamentStoragePort.saveTournament(startedTournament)

        val result = startTournamentCommand.execute(tournament.id)

        expectThat(result).isA<StartTournamentResult.TournamentAlreadyStarted>()
    }

    @Test
    fun `should return an error if there are no phases configured`() {
        val tournament = TournamentTestBuilder().withRandomParticipants(8).build()
        tournamentStoragePort.saveTournament(tournament)

        val result = startTournamentCommand.execute(tournament.id)

        expectThat(result).isA<StartTournamentResult.NoPhaseDefined>()
    }

    @Test
    fun `should set the nextPhase to the first one`() {
        val initialTournament = TournamentTestBuilder().withRandomParticipants(8).withPhase(RoundRobinTournamentPhase())
            .withPhase(RoundRobinTournamentPhase())
            .build()

        tournamentStoragePort.saveTournament(initialTournament)

        val result = startTournamentCommand.execute(initialTournament.id)

        expectThat(result).isA<StartTournamentResult.Success>()

        val tournamentInStorage = tournamentStoragePort.getTournament(initialTournament.id)
        expectThat(tournamentInStorage).isA<GetTournamentResult.Success>().and {
            get { tournament }.isA<OngoingTournament>().and {
                get { currentPhaseIndex }.isEqualTo(0)
            }
        }
    }

    @Test
    fun `should init the matches of the first phase (round robin)`() {
        val initialTournament = TournamentTestBuilder().withRandomParticipants(4).withPhase(RoundRobinTournamentPhase())
            .withPhase(RoundRobinTournamentPhase())
            .build()

        tournamentStoragePort.saveTournament(initialTournament)

        val result = startTournamentCommand.execute(initialTournament.id)

        expectThat(result).isA<StartTournamentResult.Success>()

        val tournamentInStorage = tournamentStoragePort.getTournament(initialTournament.id)
        expectThat(tournamentInStorage).isA<GetTournamentResult.Success>().and {
            get { tournament }.isA<OngoingTournament>().and {
                get { phases.first() }.isA<RoundRobinTournamentPhase>()
                    .get { matches }.hasSize(6)
                    .get { filter { it.id == 0 } }.hasSize(0)
            }
        }
    }
}