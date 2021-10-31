package org.hemit.infra.storage.mongo

import org.hemit.domain.model.Tournament
import org.hemit.domain.ports.output.GetTournamentResult
import org.hemit.domain.ports.output.TournamentStorage
import org.hemit.infra.storage.mongo.dao.TournamentDao
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class MongoTournamentStorage : TournamentStorage {
    override fun saveTournament(tournament: Tournament) {
        val dao = TournamentDao(tournament.id, tournament.name)
        dao.persist()
    }

    override fun getTournament(tournamentId: String): GetTournamentResult {
        return GetTournamentResult.Success(
            toDao(TournamentDao.findById(tournamentId) ?: return GetTournamentResult.TournamentDoesNotExist)
        )
    }

    private fun toDao(tournament: TournamentDao): Tournament {
        return Tournament(tournament.identifier, tournament.name)
    }
}