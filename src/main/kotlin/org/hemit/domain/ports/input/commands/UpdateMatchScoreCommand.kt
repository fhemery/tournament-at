package org.hemit.domain.ports.input.commands

import org.hemit.domain.model.MatchToUpdate
import org.hemit.domain.model.tournament.OngoingTournament
import org.hemit.domain.ports.output.GetTournamentResult
import org.hemit.domain.ports.output.TournamentStorage
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class UpdateMatchScoreCommand {

    @Inject
    lateinit var storagePort: TournamentStorage

    fun execute(tournamentId: String, matchToUpdate: MatchToUpdate): UpdateMatchScoreResult {
        val tournament = when (val result = storagePort.getTournament(tournamentId)) {
            is GetTournamentResult.Success -> result.tournament
            GetTournamentResult.TournamentDoesNotExist -> TODO()
        }

        (tournament as OngoingTournament).setMatchScore(matchToUpdate)
        storagePort.saveTournament(tournament)
        
        return UpdateMatchScoreResult.Success
    }
}

sealed class UpdateMatchScoreResult {
    object Success : UpdateMatchScoreResult()
}