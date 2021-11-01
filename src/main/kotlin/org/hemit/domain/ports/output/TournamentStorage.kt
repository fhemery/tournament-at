package org.hemit.domain.ports.output

import org.hemit.domain.model.tournament.Tournament

interface TournamentStorage {
    fun saveTournament(tournament: Tournament)
    fun getTournament(tournamentId: String): GetTournamentResult
}

sealed class GetTournamentResult {
    class Success(val tournament: Tournament) : GetTournamentResult()
    object TournamentDoesNotExist : GetTournamentResult()
}