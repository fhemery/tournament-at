package org.hemit.domain.model

import org.hemit.domain.model.policies.ParticipantPolicy

class Tournament(
    val id: String,
    val name: String,
    var participants: List<Participant> = emptyList(),
    var phases: List<TournamentPhase> = emptyList(),
    val maxParticipants: Int = Int.MAX_VALUE
) {
    fun addParticipant(participant: Participant) {
        ParticipantPolicy().checkParticipantCanBeAdded(this, participant)
        participants += participant
    }

    fun addPhase(phase: TournamentPhase) {
        phases += phase
    }
}
