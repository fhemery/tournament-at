package org.hemit.infra.storage.mongo.dao

import org.hemit.domain.model.RoundRobinTournamentPhase
import org.hemit.domain.model.TournamentPhase
import org.hemit.domain.model.TournamentPhaseType

class TournamentPhaseDao() {
    lateinit var type: String

    companion object {
        fun from(phase: TournamentPhase): TournamentPhaseDao {
            return TournamentPhaseDao(phase.type.name)
        }
    }

    constructor(type: String) : this() {
        this.type = type
    }

    fun toPhase(): TournamentPhase {
        return when (this.type) {
            TournamentPhaseType.RoundRobin.name -> RoundRobinTournamentPhase()
            else -> throw Exception("Unknown phase type ${this.type} coming from DB")
        }
    }
}
