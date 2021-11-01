package org.hemit.infra.storage.mongo.dao

import org.hemit.domain.model.Match

class MatchDao(var opponent1: ParticipantDao? = null, var opponent2: ParticipantDao? = null, var id: Int = -1) {

    companion object {
        fun from(match: Match): MatchDao {
            return MatchDao(
                if (match.opponent1 == null) null else ParticipantDao.from(match.opponent1),
                if (match.opponent2 == null) null else ParticipantDao.from(match.opponent2),
                match.id
            )
        }
    }

    fun toMatch(): Match {
        return Match(this.opponent1?.toParticipant(), this.opponent2?.toParticipant(), this.id)
    }
}
