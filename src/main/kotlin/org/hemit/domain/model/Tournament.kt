package org.hemit.domain.model

import org.hemit.domain.model.policies.ParticipantPolicy
import org.hemit.domain.model.policies.StartTournamentPolicy

class Tournament(
    val id: String,
    val name: String,
    var participants: List<Participant> = emptyList(),
    var phases: List<TournamentPhase> = emptyList(),
    val maxParticipants: Int = Int.MAX_VALUE
) {
    var currentPhase: TournamentPhase? = null
        private set

    var status: TournamentStatus = TournamentStatus.NotStarted
        private set

    fun addParticipant(participant: Participant) {
        ParticipantPolicy().checkParticipantCanBeAdded(this, participant)
        participants += participant
    }

    fun addPhase(phase: TournamentPhase) {
        phases += phase
    }

    fun start() {
        StartTournamentPolicy().check(this)

        status = TournamentStatus.Started
        currentPhase = phases.first()
    }
}
