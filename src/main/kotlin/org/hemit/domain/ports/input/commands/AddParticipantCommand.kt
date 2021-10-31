package org.hemit.domain.ports.input.commands

import org.hemit.domain.model.Participant
import org.hemit.domain.model.exceptions.MaximumNumberOfParticipantsReachedException
import org.hemit.domain.model.exceptions.ParticipantAlreadyExistsForTournamentError
import org.hemit.domain.ports.output.GetTournamentResult
import org.hemit.domain.ports.output.TournamentStorage
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class AddParticipantCommand() {

    constructor(tournamentStorage: TournamentStorage) : this() {
        this.tournamentStorage = tournamentStorage
    }

    @Inject
    lateinit var tournamentStorage: TournamentStorage

    fun execute(tournamentId: String, participant: Participant): AddParticipantCommandResult {
        val tournament = when (val result = tournamentStorage.getTournament(tournamentId)) {
            is GetTournamentResult.Success -> result.tournament
            GetTournamentResult.TournamentDoesNotExist -> return AddParticipantCommandResult.TournamentDoesNotExist
        }

        try {
            tournament.addParticipant(participant)

            tournamentStorage.saveTournament(tournament)
        } catch (e: ParticipantAlreadyExistsForTournamentError) {
            return AddParticipantCommandResult.AlreadyParticipating
        } catch (e: MaximumNumberOfParticipantsReachedException) {
            return AddParticipantCommandResult.MaximumParticipantsReached
        }

        return AddParticipantCommandResult.Success
    }
}

sealed class AddParticipantCommandResult {
    object Success : AddParticipantCommandResult()
    object MaximumParticipantsReached : AddParticipantCommandResult()
    object AlreadyParticipating : AddParticipantCommandResult()
    object TournamentDoesNotExist : AddParticipantCommandResult()
}
