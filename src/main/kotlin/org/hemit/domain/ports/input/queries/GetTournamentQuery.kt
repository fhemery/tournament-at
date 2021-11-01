package org.hemit.domain.ports.input.queries

import org.hemit.domain.model.tournament.Tournament
import org.hemit.domain.ports.output.GetTournamentResult
import org.hemit.domain.ports.output.TournamentStorage
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class GetTournamentQuery() {

    constructor(tournamentStorage: TournamentStorage) : this() {
        this.tournamentStorage = tournamentStorage
    }

    @Inject
    lateinit var tournamentStorage: TournamentStorage

    fun execute(id: String): GetTournamentQueryResult {
        val tournament = when (val result = tournamentStorage.getTournament(id)) {
            is GetTournamentResult.Success -> result.tournament
            GetTournamentResult.TournamentDoesNotExist -> return GetTournamentQueryResult.TournamentDoesNotExist
        }
        return GetTournamentQueryResult.Success(tournament)
    }
}

sealed class GetTournamentQueryResult {
    class Success(val tournament: Tournament) : GetTournamentQueryResult()
    object TournamentDoesNotExist : GetTournamentQueryResult()
}