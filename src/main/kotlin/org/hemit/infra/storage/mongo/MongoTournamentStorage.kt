package org.hemit.infra.storage.mongo

import org.hemit.domain.model.*
import org.hemit.domain.ports.output.GetTournamentResult
import org.hemit.domain.ports.output.TournamentStorage
import org.hemit.infra.storage.mongo.dao.TournamentDao
import org.hemit.infra.storage.mongo.dao.TournamentPhaseDao
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class MongoTournamentStorage : TournamentStorage {
    override fun saveTournament(tournament: Tournament) {
        val tournamentDao = TournamentDao.findById(tournament.id)
        if (tournamentDao != null) {
            updateTournament(tournamentDao, tournament)
        } else {
            val dao = TournamentDao(tournament.id, tournament.name, tournament.phases.map { toPhaseDao(it) })
            dao.persist()
        }
    }

    private fun updateTournament(tournamentDao: TournamentDao, tournament: Tournament) {
        tournamentDao.name = tournament.name
        tournamentDao.phases = tournament.phases.map { toPhaseDao(it) }
        tournamentDao.update()
    }

    private fun toPhaseDao(it: TournamentPhase): TournamentPhaseDao {
        return TournamentPhaseDao(it.type.name)
    }

    override fun getTournament(tournamentId: String): GetTournamentResult {
        return GetTournamentResult.Success(
            toTournament(TournamentDao.findById(tournamentId) ?: return GetTournamentResult.TournamentDoesNotExist)
        )
    }

    private fun toTournament(dao: TournamentDao): Tournament {
        return Tournament(dao.identifier, dao.name, phases = dao.phases.map { toPhase(it) })
    }

    private fun toPhase(it: TournamentPhaseDao): TournamentPhase {
        return when (it.type) {
            TournamentPhaseType.SingleBracketElimination.name -> SingleEliminationBracketTournamentPhase()
            TournamentPhaseType.RoundRobin.name -> RoundRobinTournamentPhase()
            else -> throw Exception("Unknown phase type ${it.type} coming from DB")
        }

    }
}