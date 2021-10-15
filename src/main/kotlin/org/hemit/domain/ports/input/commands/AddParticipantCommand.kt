package org.hemit.domain.ports.input.commands

import org.hemit.domain.model.Participant
import org.hemit.domain.model.exceptions.ParticipantAlreadyExistsForTournamentError
import org.hemit.domain.ports.output.TournamentStorage

class AddParticipantCommand(private val tournamentStorage: TournamentStorage) {

    fun execute(tournamentId: String, participant: Participant): AddParticipantCommandResult {
        val tournament =
            tournamentStorage.getTournament(tournamentId) ?: return AddParticipantCommandResult.TournamentDoesNotExist

        try {
            tournament.addParticipant(participant)

            tournamentStorage.saveTournament(tournament)
        } catch (e: ParticipantAlreadyExistsForTournamentError) {
            return AddParticipantCommandResult.AlreadyParticipating
        }

        return AddParticipantCommandResult.Success
    }
}

sealed class AddParticipantCommandResult {
    object Success : AddParticipantCommandResult()
    object MaximumParticipantReached : AddParticipantCommandResult()
    object AlreadyParticipating : AddParticipantCommandResult()
    object TournamentDoesNotExist : AddParticipantCommandResult()
}
