package org.hemit.acceptance.participants

import org.hemit.BaseAcceptanceTest
import org.hemit.domain.model.IndividualParticipant
import org.hemit.domain.ports.input.commands.AddParticipantCommand
import org.hemit.domain.ports.input.commands.AddParticipantCommandResult
import org.hemit.utils.builders.SwissRoundTournamentBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.contains
import strikt.assertions.isA
import strikt.assertions.isNotNull

class ParticipantsTests : BaseAcceptanceTest() {

    lateinit var addParticipantCommand: AddParticipantCommand

    @BeforeEach
    fun setup() {
        addParticipantCommand = AddParticipantCommand(tournamentStoragePort)
    }

    @Test
    fun `should return success if participant was correctly added`() {
        val tournament = SwissRoundTournamentBuilder().withRandomParticipants(5).build()
        tournamentStoragePort.saveTournament(tournament)

        val newParticipant = IndividualParticipant("Bob", 1500)
        val result = addParticipantCommand.execute(tournament.id, newParticipant)

        expectThat(result).isA<AddParticipantCommandResult.Success>()
        expectThat(tournamentStoragePort.getTournament(tournament.id))
            .isNotNull().and {
                get { participants }.contains(newParticipant)
            }
    }
}