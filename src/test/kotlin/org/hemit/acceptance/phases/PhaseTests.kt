package org.hemit.acceptance.phases

import org.hemit.BaseAcceptanceTest
import org.hemit.domain.model.SingleEliminationBracketTournamentPhase
import org.hemit.domain.model.TournamentPhaseType
import org.hemit.domain.ports.input.commands.AddTournamentPhaseCommand
import org.hemit.domain.ports.input.commands.AddTournamentPhaseResult
import org.hemit.domain.ports.output.GetTournamentResult
import org.hemit.utils.builders.TournamentTestBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.hasSize
import strikt.assertions.isA
import strikt.assertions.isEqualTo

class PhaseTests : BaseAcceptanceTest() {
    lateinit var addPhaseCommand: AddTournamentPhaseCommand

    @BeforeEach
    fun setup() {
        addPhaseCommand = AddTournamentPhaseCommand(tournamentStoragePort)
    }

    @Test
    fun `should return success if phase can be added`() {
        val tournament = TournamentTestBuilder().build()
        tournamentStoragePort.saveTournament(tournament)

        val result = addPhaseCommand.execute(tournament.id, SingleEliminationBracketTournamentPhase())

        expectThat(result).isA<AddTournamentPhaseResult.Success>()
        expectThat(tournamentStoragePort.getTournament(tournament.id)).isA<GetTournamentResult.Success>().and {
            get { tournament.phases }.hasSize(1).and {
                get { first().type }.isEqualTo(TournamentPhaseType.SingleBracketElimination)
            }
        }
    }

    @Test
    fun `should return failure if tournament does not exist`() {
        val result = addPhaseCommand.execute("Unknown", SingleEliminationBracketTournamentPhase())

        expectThat(result).isA<AddTournamentPhaseResult.TournamentDoesNotExist>()
    }
}