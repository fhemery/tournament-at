package org.hemit.infra.storage.mongo

import org.hemit.domain.model.*
import org.hemit.domain.ports.output.GetTournamentResult
import org.hemit.domain.ports.output.TournamentStorage
import org.hemit.infra.storage.mongo.dao.ParticipantDao
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
            insertTournament(tournament)
        }
    }

    private fun insertTournament(tournament: Tournament) {
        val dao = TournamentDao(
            tournament.id,
            tournament.name,
            tournament.phases.map { toPhaseDao(it) },
            tournament.participants.map { toParticipantDao(it) },
            tournament.maxParticipants,
            tournament.status.name
        )
        dao.persist()
    }

    private fun toParticipantDao(it: Participant): ParticipantDao {
        return ParticipantDao(it.name, it.elo)
    }

    private fun updateTournament(tournamentDao: TournamentDao, tournament: Tournament) {
        tournamentDao.name = tournament.name
        tournamentDao.phases = tournament.phases.map { toPhaseDao(it) }
        tournamentDao.participants = tournament.participants.map { toParticipantDao(it) }
        tournamentDao.status = tournament.status.name
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
        return Tournament(
            dao.identifier,
            dao.name,
            dao.participants.map { toParticipant(it) },
            dao.phases.map { toPhase(it) },
            dao.maxParticipants
        )
    }

    private fun toParticipant(it: ParticipantDao): Participant {
        return IndividualParticipant(it.name, it.elo)
    }

    private fun toPhase(it: TournamentPhaseDao): TournamentPhase {
        return when (it.type) {
            TournamentPhaseType.SingleBracketElimination.name -> SingleEliminationBracketTournamentPhase()
            TournamentPhaseType.RoundRobin.name -> RoundRobinTournamentPhase()
            else -> throw Exception("Unknown phase type ${it.type} coming from DB")
        }

    }
}