package org.hemit.infra.api

import org.hemit.domain.model.IndividualParticipant
import org.hemit.domain.model.Participant
import org.hemit.domain.ports.input.commands.AddParticipantCommand
import org.hemit.domain.ports.input.commands.AddParticipantCommandResult
import org.hemit.infra.api.dto.ParticipantDto
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("tournaments/{tournamentId}/participants")
class ParticipantResource {

    @Inject
    lateinit var addParticipantCommand: AddParticipantCommand

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun addParticipant(@PathParam("tournamentId") tournamentId: String, participant: ParticipantDto) {
        when (addParticipantCommand.execute(tournamentId, toParticipant(participant))) {
            AddParticipantCommandResult.AlreadyParticipating -> throw BadRequestException("Participant was already added")
            AddParticipantCommandResult.MaximumParticipantsReached -> throw BadRequestException("Max participants has been reached")
            AddParticipantCommandResult.Success -> Unit
            AddParticipantCommandResult.TournamentDoesNotExist -> throw NotFoundException("Tournament does not exist")
        }
    }

    private fun toParticipant(participant: ParticipantDto): Participant {
        return IndividualParticipant(participant.name, participant.elo)
    }
}