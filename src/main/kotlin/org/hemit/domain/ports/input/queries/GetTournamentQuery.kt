package org.hemit.domain.ports.input.queries

import org.hemit.domain.model.Tournament
import org.hemit.domain.ports.output.TournamentStorage

class GetTournamentQuery(private val tournamentStoragePort: TournamentStorage) {
    fun execute(id: String): GetTournamentQueryResult {
        val tournament =
            tournamentStoragePort.getTournament(id) ?: return GetTournamentQueryResult.TournamentDoesNotExist
        return GetTournamentQueryResult.Success(tournament)
    }
}

sealed class GetTournamentQueryResult {
    class Success(val tournament: Tournament) : GetTournamentQueryResult()
    object TournamentDoesNotExist : GetTournamentQueryResult()
}