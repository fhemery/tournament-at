package org.hemit.domain.model

import org.hemit.domain.model.exceptions.ParticipantAlreadyExistsForTournamentError

class Tournament(
    val id: String,
    val name: String,
    var participants: List<Participant> = emptyList(),
    var phases: List<TournamentPhase> = emptyList()
) {
    fun addParticipant(participant: Participant) {
        if (participants.find { it.name == participant.name } != null) {
            throw ParticipantAlreadyExistsForTournamentError()
        }
        participants += participant
    }
}
