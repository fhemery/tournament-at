package org.hemit.infra.storage.mongo

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

    override fun getTournament(tournamentId: String): GetTournamentResult {
        return GetTournamentResult.Success(
            TournamentDao.findById(tournamentId)?.toTournament() ?: return GetTournamentResult.TournamentDoesNotExist
        )
    }

    private fun insertTournament(registeredTournament: RegisteredTournament) {
        val dao = TournamentDao.from(registeredTournament)
        dao.persist()
    }

    private fun updateTournament(tournamentDao: TournamentDao, tournamentSetup: Tournament) {
        tournamentDao.name = tournamentSetup.name
        tournamentDao.phases = tournamentSetup.phases.map { TournamentPhaseDao.from(it) }
        tournamentDao.participants = tournamentSetup.participants.map { ParticipantDao.from(it) }
        tournamentDao.status = tournamentSetup.status.name
        tournamentDao.update()
    }
}