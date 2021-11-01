package org.hemit.infra.storage.mongo.dao

import io.quarkus.mongodb.panache.common.MongoEntity
import io.quarkus.mongodb.panache.kotlin.PanacheMongoCompanion
import io.quarkus.mongodb.panache.kotlin.PanacheMongoEntity
import org.hemit.domain.model.tournament.OngoingTournament
import org.hemit.domain.model.tournament.RegisteredTournament
import org.hemit.domain.model.tournament.Tournament
import org.hemit.domain.model.tournament.TournamentStatus

@MongoEntity(collection = "tournament")
class TournamentDao() : PanacheMongoEntity() {
    companion object : PanacheMongoCompanion<TournamentDao> {
        fun findById(id: String) = find("identifier", id).firstResult()
        fun from(tournament: Tournament): TournamentDao {
            return when (tournament.status) {
                TournamentStatus.NotStarted -> fromRegisteredTournament(tournament as RegisteredTournament)
                TournamentStatus.Started -> fromOngoingTournament(tournament as OngoingTournament)
                else -> TODO("Implement finished tournament")
            }

        }

        private fun fromOngoingTournament(ongoingTournament: OngoingTournament): TournamentDao {
            return TournamentDao(
                ongoingTournament.id,
                ongoingTournament.name,
                ongoingTournament.phases.map { TournamentPhaseDao.from(it) },
                ongoingTournament.participants.map { ParticipantDao.from(it) },
                null,
                ongoingTournament.status.name
            )
        }

        private fun fromRegisteredTournament(registeredTournament: RegisteredTournament): TournamentDao {
            return TournamentDao(
                registeredTournament.id,
                registeredTournament.name,
                registeredTournament.phases.map { TournamentPhaseDao.from(it) },
                registeredTournament.participants.map { ParticipantDao.from(it) },
                registeredTournament.maxParticipants,
                registeredTournament.status.name
            )
        }
    }

    lateinit var identifier: String
    lateinit var name: String
    lateinit var phases: List<TournamentPhaseDao>
    lateinit var participants: List<ParticipantDao>
    var maxParticipants: Int? = null
    lateinit var status: String

    constructor(
        id: String,
        name: String,
        phases: List<TournamentPhaseDao> = emptyList(),
        participants: List<ParticipantDao> = emptyList(),
        maxParticipants: Int? = null,
        status: String
    ) : this() {
        this.identifier = id
        this.name = name
        this.phases = phases
        this.participants = participants
        this.maxParticipants = maxParticipants
        this.status = status
    }

    fun toTournament(): Tournament {
        return when (this.status) {
            TournamentStatus.NotStarted.name ->
                RegisteredTournament(
                    this.identifier,
                    this.name,
                    this.participants.map { it.toParticipant() },
                    this.phases.map { it.toPhase() },
                    this.maxParticipants ?: Int.MAX_VALUE
                )
            TournamentStatus.Started.name ->
                OngoingTournament(
                    this.identifier,
                    this.name,
                    this.participants.map { it.toParticipant() },
                    this.phases.map { it.toPhase() },
                )
            else -> throw Exception("Unexpected tournament state ${this.status}")
        }
    }
}