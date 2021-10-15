package org.hemit.domain.ports.input.commands

import org.hemit.domain.model.Participant
import org.hemit.domain.ports.output.TournamentStorage

class AddParticipantCommand(private val tournamentStorage: TournamentStorage) {

    fun execute(tournamentId: String, participant: Participant): AddParticipantCommandResult {
        val tournament = tournamentStorage.getTournament(tournamentId)!!

        tournament.participants = tournament.participants + participant

        tournamentStorage.saveTournament(tournament)

        return AddParticipantCommandResult.Success
    }
}

sealed class AddParticipantCommandResult {
    object Success : AddParticipantCommandResult()
    object MaximumParticipantReached : AddParticipantCommandResult()
    object ParticipantAlreadyAdded : AddParticipantCommandResult()
    object TournamentDoesNotExist : AddParticipantCommandResult()
}
