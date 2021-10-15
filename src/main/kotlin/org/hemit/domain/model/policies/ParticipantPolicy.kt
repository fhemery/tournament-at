package org.hemit.domain.model.policies

import org.hemit.domain.model.Participant
import org.hemit.domain.model.Tournament
import org.hemit.domain.model.exceptions.MaximumNumberOfParticipantsReachedException
import org.hemit.domain.model.exceptions.ParticipantAlreadyExistsForTournamentError

class ParticipantPolicy {
    fun checkParticipantCanBeAdded(tournament: Tournament, participant: Participant) {
        with(tournament) {
            if (participants.find { it.name == participant.name } != null) {
                throw ParticipantAlreadyExistsForTournamentError()
            }
            if (participants.size == maxParticipants) {
                throw MaximumNumberOfParticipantsReachedException()
            }
        }
    }

}
