package org.hemit.domain.ports.output

import org.hemit.domain.model.Tournament
import org.hemit.domain.model.TournamentToCreate

interface TournamentStorage {
    fun saveTournament(tournament: Tournament)
    fun getTournament(tournamentId: String): Tournament?
}

sealed class GetTournamentResult {
    class Success(tournament: Tournament): GetTournamentResult()
    object TournamentDoesNotExist: GetTournamentResult()
}