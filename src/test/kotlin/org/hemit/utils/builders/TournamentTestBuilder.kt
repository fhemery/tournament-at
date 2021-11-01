package org.hemit.utils.builders

import org.hemit.domain.model.IndividualParticipant
import org.hemit.domain.model.Participant
import org.hemit.domain.model.tournament.RegisteredTournament
import org.hemit.domain.model.TournamentPhase

class TournamentTestBuilder {

    var name = "Tournament"
    var maxParticipants = 256
    var participants: List<Participant> = emptyList()
    var phases: List<TournamentPhase> = emptyList()

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

    fun build(): RegisteredTournament {
        return RegisteredTournament("default", name, participants, phases, maxParticipants)
    }

    fun withMaxParticipants(max: Int): TournamentTestBuilder {
        maxParticipants = max
        return this
    }

    fun withPhase(phase: TournamentPhase): TournamentTestBuilder {
        this.phases += phase
        return this
    }
}