package org.hemit.acceptance.tournament

import org.hemit.BaseAcceptanceTest
import org.hemit.domain.model.SingleEliminationBracketTournamentPhase
import org.hemit.domain.model.SwissRoundTournamentPhase
import org.hemit.domain.model.TournamentStatus
import org.hemit.domain.ports.input.commands.StartTournamentCommand
import org.hemit.domain.ports.input.commands.StartTournamentResult
import org.hemit.domain.ports.input.queries.GetTournamentQueryResult
import org.hemit.utils.builders.TournamentTestBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull

class StartTournamentTests : BaseAcceptanceTest() {

    lateinit var startTournamentCommand: StartTournamentCommand

    @BeforeEach
    fun setup() {
        startTournamentCommand = StartTournamentCommand(tournamentStoragePort)
    }

    @Test
    fun `should launch the tournament`() {
        val tournament =
            TournamentTestBuilder().withRandomParticipants(8).withPhase(SingleEliminationBracketTournamentPhase())
                .build()
        tournamentStoragePort.saveTournament(tournament)

        val result = startTournamentCommand.execute(tournament.id)

        expectThat(result).isA<StartTournamentResult.Success>()

        val tournamentInStorage = tournamentStoragePort.getTournament(tournament.id)
        expectThat(tournamentInStorage).isA<GetTournamentQueryResult.Success>().and {
            get { tournament.status }.isEqualTo(TournamentStatus.Started)
        }
    }

    @Test
    fun `should return an error if there are not enough participants`() {
        val tournament =
            TournamentTestBuilder().withRandomParticipants(1).withPhase(SingleEliminationBracketTournamentPhase())
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
            TournamentTestBuilder().withRandomParticipants(8).withPhase(SingleEliminationBracketTournamentPhase())
                .build()
        tournament.start()
        tournamentStoragePort.saveTournament(tournament)

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
        val tournament = TournamentTestBuilder().withRandomParticipants(8).withPhase(SwissRoundTournamentPhase())
            .withPhase(SingleEliminationBracketTournamentPhase())
            .build()

        tournamentStoragePort.saveTournament(tournament)

        val result = startTournamentCommand.execute(tournament.id)

        expectThat(result).isA<StartTournamentResult.Success>()

        val tournamentInStorage = tournamentStoragePort.getTournament(tournament.id)
        expectThat(tournamentInStorage).isA<GetTournamentQueryResult.Success>().and {
            get { tournament.currentPhase }.isNotNull().isA<SwissRoundTournamentPhase>()
        }
    }
}