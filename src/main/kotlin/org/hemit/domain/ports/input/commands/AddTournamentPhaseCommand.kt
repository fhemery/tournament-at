package org.hemit.domain.ports.input.commands

import org.hemit.domain.model.TournamentPhase
import org.hemit.domain.ports.output.TournamentStorage

class AddTournamentPhaseCommand(private val tournamentStorage: TournamentStorage) {
    fun execute(tournamentId: String, phase: TournamentPhase): AddTournamentPhaseResult {
        val tournament =
            tournamentStorage.getTournament(tournamentId) ?: return AddTournamentPhaseResult.TournamentDoesNotExist

        tournament.addPhase(phase)

        tournamentStorage.saveTournament(tournament)

        return AddTournamentPhaseResult.Success
    }
}

sealed class AddTournamentPhaseResult {
    object Success : AddTournamentPhaseResult()
    object TournamentDoesNotExist : AddTournamentPhaseResult()
}
