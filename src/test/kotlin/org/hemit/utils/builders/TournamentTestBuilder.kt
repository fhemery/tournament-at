package org.hemit.utils.builders

import org.hemit.domain.model.IndividualParticipant
import org.hemit.domain.model.Participant
import org.hemit.domain.model.Tournament

class TournamentTestBuilder {

    var name = "Tournament"
    var participants: List<Participant> = emptyList()

    fun withName(name: String): TournamentTestBuilder {
        this.name = name
        return this
    }

    fun withRandomParticipants(nbParticipants: Int): TournamentTestBuilder {
        val participants = mutableListOf<Participant>()
        for (nb in 0 until nbParticipants) {
            participants.add(IndividualParticipant("Player$nb", (0..1000).random()))
        }
        this.participants = participants
        return this
    }

    fun build(): Tournament {
        return Tournament("default", name, participants)
    }
}