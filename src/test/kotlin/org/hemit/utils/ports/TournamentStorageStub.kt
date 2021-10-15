package org.hemit.utils.ports

import org.hemit.domain.model.Tournament
import org.hemit.domain.ports.output.TournamentStorage

class TournamentStorageStub : TournamentStorage {
    private val savedTournaments = HashMap<String, Tournament>()

    override fun saveTournament(tournament: Tournament) {
        savedTournaments[tournament.id] = tournament
    }

    override fun getTournament(tournamentId: String): Tournament? {
        return savedTournaments[tournamentId]
    }
}