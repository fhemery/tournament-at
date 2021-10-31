package org.hemit.domain.ports.input.commands

import org.hemit.domain.model.TournamentPhase
import org.hemit.domain.ports.output.GetTournamentResult
import org.hemit.domain.ports.output.TournamentStorage
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class AddTournamentPhaseCommand() {

    constructor(tournamentStorage: TournamentStorage) : this() {
        this.tournamentStorage = tournamentStorage
    }

    @Inject
    lateinit var tournamentStorage: TournamentStorage

    fun execute(tournamentId: String, phase: TournamentPhase): AddTournamentPhaseResult {
        val tournament = when (val result = tournamentStorage.getTournament(tournamentId)) {
            is GetTournamentResult.Success -> result.tournament
            GetTournamentResult.TournamentDoesNotExist -> return AddTournamentPhaseResult.TournamentDoesNotExist
        }

        tournament.addPhase(phase)

        tournamentStorage.saveTournament(tournament)

        return AddTournamentPhaseResult.Success
    }
}

sealed class AddTournamentPhaseResult {
    object Success : AddTournamentPhaseResult()
    object TournamentDoesNotExist : AddTournamentPhaseResult()
}
