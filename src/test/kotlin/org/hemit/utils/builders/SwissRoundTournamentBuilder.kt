package org.hemit.utils.builders

import org.hemit.domain.model.IndividualParticipant
import org.hemit.domain.model.Participant
import org.hemit.domain.model.SwissRoundTournament

class SwissRoundTournamentBuilder {

    var name = "Tournament"
    var participants: List<Participant> = emptyList()

    fun withName(name: String): SwissRoundTournamentBuilder {
        this.name = name
        return this
    }

    fun withRandomParticipants(nbParticipants: Int): SwissRoundTournamentBuilder {
        val participants = mutableListOf<Participant>()
        for (nb in 0 until nbParticipants) {
            participants.add(IndividualParticipant("Player$nb", (0..1000).random()))
        }
        this.participants = participants
        return this
    }

    fun build(): SwissRoundTournament {
        return SwissRoundTournament("default", name, participants)
    }
}