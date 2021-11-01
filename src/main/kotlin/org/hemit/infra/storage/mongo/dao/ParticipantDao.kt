package org.hemit.infra.storage.mongo.dao

import org.hemit.domain.model.IndividualParticipant
import org.hemit.domain.model.Participant

class ParticipantDao() {
    lateinit var name: String
    var elo: Int = 0

    companion object {
        fun from(participant: Participant): ParticipantDao {
            return ParticipantDao(participant.name, participant.elo)
        }
    }

    constructor(name: String, elo: Int) : this() {
        this.name = name
        this.elo = elo
    }

    fun toParticipant(): Participant {
        return IndividualParticipant(this.name, this.elo)
    }
}
