package org.hemit.acceptance.participants

import org.hemit.BaseAcceptanceTest
import org.hemit.domain.model.IndividualParticipant
import org.hemit.domain.ports.input.commands.AddParticipantCommand
import org.hemit.domain.ports.input.commands.AddParticipantCommandResult
import org.hemit.domain.ports.output.GetTournamentResult
import org.hemit.utils.builders.TournamentTestBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.contains
import strikt.assertions.hasSize
import strikt.assertions.isA

class ParticipantsTests : BaseAcceptanceTest() {

    lateinit var addParticipantCommand: AddParticipantCommand

    @BeforeEach
    fun setup() {
        addParticipantCommand = AddParticipantCommand(tournamentStoragePort)
    }

    @Test
    fun `should return success if participant was correctly added`() {
        val tournament = TournamentTestBuilder().withRandomParticipants(5).build()
        tournamentStoragePort.saveTournament(tournament)

        val newParticipant = IndividualParticipant("Bob", 1500)
        val result = addParticipantCommand.execute(tournament.id, newParticipant)

        expectThat(result).isA<AddParticipantCommandResult.Success>()
        expectThat(tournamentStoragePort.getTournament(tournament.id))
            .isA<GetTournamentResult.Success>().and {
                get { tournament.participants }.contains(newParticipant)
            }
    }

    @Test
    fun `should return error if tournament does not exist`() {
        val result = addParticipantCommand.execute("UnknownId", IndividualParticipant("Alice", 1600))
        expectThat(result).isA<AddParticipantCommandResult.TournamentDoesNotExist>()
    }

    @Test
    fun `should return error if participant is already participating`() {
        val tournament = TournamentTestBuilder().withRandomParticipants(5).build()
        tournamentStoragePort.saveTournament(tournament)

        val result = addParticipantCommand.execute(tournament.id, tournament.participants.first())

        expectThat(result).isA<AddParticipantCommandResult.AlreadyParticipating>()
        expectThat(tournamentStoragePort.getTournament(tournament.id)).isA<GetTournamentResult.Success>().and {
            get { tournament.participants }.hasSize(5)
        }
    }

    @Test
    fun `should return error if max participants is reached`() {
        val tournament = TournamentTestBuilder().withMaxParticipants(10).withRandomParticipants(10).build()
        tournamentStoragePort.saveTournament(tournament)

        val result = addParticipantCommand.execute(tournament.id, IndividualParticipant("Carol", 1200))
        expectThat(result).isA<AddParticipantCommandResult.MaximumParticipantsReached>()
        expectThat(tournamentStoragePort.getTournament(tournament.id)).isA<GetTournamentResult.Success>().and {
            get { tournament.participants }.hasSize(10)
        }
    }
}