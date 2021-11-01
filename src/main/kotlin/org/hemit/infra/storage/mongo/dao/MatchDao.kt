package org.hemit.infra.storage.mongo.dao

import org.hemit.domain.model.Match

class MatchDao(
    var id: Int = -1,
    var opponent2: ParticipantDao? = null,
    var opponent1: ParticipantDao? = null,
    var winner: String? = null,
    var score: String? = null
) {

    companion object {
        fun from(match: Match): MatchDao {
            return MatchDao(
                match.id,
                if (match.opponent2 == null) null else ParticipantDao.from(match.opponent2),
                if (match.opponent1 == null) null else ParticipantDao.from(match.opponent1),
                match.winner,
                match.score
            )
        }
    }

    fun toMatch(): Match {
        return Match(this.id, this.opponent2?.toParticipant(), this.opponent1?.toParticipant(), this.winner, this.score)
    }
}
