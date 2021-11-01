package org.hemit.domain.model.tournament

import org.hemit.domain.model.Participant
import org.hemit.domain.model.TournamentPhase
import org.hemit.domain.model.policies.ParticipantPolicy
import org.hemit.domain.model.policies.StartTournamentPolicy

class RegisteredTournament(
    id: String,
    name: String,
    participants: List<Participant> = emptyList(),
    phases: List<TournamentPhase> = emptyList(),
    val maxParticipants: Int = Int.MAX_VALUE
) : Tournament(id, name, phases, participants) {

    override val status: TournamentStatus = TournamentStatus.NotStarted


    fun addParticipant(participant: Participant) {
        ParticipantPolicy().checkParticipantCanBeAdded(this, participant)
        participants += participant
    }

    fun addPhase(phase: TournamentPhase) {
        phases += phase
    }

    fun start(): OngoingTournament {
        StartTournamentPolicy().check(this)

        return OngoingTournament(this.id, this.name, this.participants, this.phases)
    }
}
