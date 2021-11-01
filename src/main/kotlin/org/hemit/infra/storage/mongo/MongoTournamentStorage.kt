package org.hemit.infra.storage.mongo

import org.hemit.domain.model.*
import org.hemit.domain.model.tournament.RegisteredTournament
import org.hemit.domain.model.tournament.Tournament
import org.hemit.domain.model.tournament.TournamentStatus
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
        } else if (tournament.status == TournamentStatus.NotStarted) {
            insertTournament(tournament as RegisteredTournament)
        }
    }

    private fun insertTournament(registeredTournament: RegisteredTournament) {
        val dao = TournamentDao(
            registeredTournament.id,
            registeredTournament.name,
            registeredTournament.phases.map { toPhaseDao(it) },
            registeredTournament.participants.map { toParticipantDao(it) },
            registeredTournament.maxParticipants,
            registeredTournament.status.name
        )
        dao.persist()
    }

    private fun toParticipantDao(it: Participant): ParticipantDao {
        return ParticipantDao(it.name, it.elo)
    }

    private fun updateTournament(tournamentDao: TournamentDao, tournamentSetup: Tournament) {
        tournamentDao.name = tournamentSetup.name
        tournamentDao.phases = tournamentSetup.phases.map { toPhaseDao(it) }
        tournamentDao.participants = tournamentSetup.participants.map { toParticipantDao(it) }
        tournamentDao.status = tournamentSetup.status.name
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

    private fun toTournament(dao: TournamentDao): RegisteredTournament {
        return RegisteredTournament(
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