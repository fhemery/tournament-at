package org.hemit.domain.ports.input.queries

import org.hemit.domain.model.Tournament
import org.hemit.domain.model.TournamentType

class GetTournamentQuery {
    fun execute(id: String): GetTournamentQueryResult {
        return GetTournamentQueryResult.TournamentDoesNotExist
    }
}

sealed class GetTournamentQueryResult {
    class Success(val tournament: Tournament): GetTournamentQueryResult()
    object TournamentDoesNotExist: GetTournamentQueryResult()
}