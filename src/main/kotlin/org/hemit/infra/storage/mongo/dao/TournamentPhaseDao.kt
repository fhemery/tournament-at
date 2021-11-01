package org.hemit.infra.storage.mongo.dao

import org.hemit.domain.model.RoundRobinTournamentPhase
import org.hemit.domain.model.TournamentPhase
import org.hemit.domain.model.TournamentPhaseType

class TournamentPhaseDao() {
    lateinit var type: String
    var matches: List<MatchDao>? = null

    companion object {
        fun from(phase: TournamentPhase): TournamentPhaseDao {
            return TournamentPhaseDao(
                phase.type.name,
                if (phase.matches.isEmpty()) null else phase.matches.map { MatchDao.from(it) })
        }
    }

    constructor(type: String, matches: List<MatchDao>? = null) : this() {
        this.type = type
        this.matches = matches
    }

    fun toPhase(): TournamentPhase {
        return when (this.type) {
            TournamentPhaseType.RoundRobin.name -> RoundRobinTournamentPhase(if (this.matches == null) emptyList() else this.matches!!.map { it.toMatch() })
            else -> throw Exception("Unknown phase type ${this.type} coming from DB")
        }
    }
}
