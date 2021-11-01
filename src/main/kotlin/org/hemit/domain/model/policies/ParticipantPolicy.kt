package org.hemit.domain.model.policies

import org.hemit.domain.model.Participant
import org.hemit.domain.model.tournament.RegisteredTournament
import org.hemit.domain.model.exceptions.MaximumNumberOfParticipantsReachedException
import org.hemit.domain.model.exceptions.ParticipantAlreadyExistsForTournamentError

class ParticipantPolicy {
    fun checkParticipantCanBeAdded(registeredTournament: RegisteredTournament, participant: Participant) {
        with(registeredTournament) {
            if (participants.find { it.name == participant.name } != null) {
                throw ParticipantAlreadyExistsForTournamentError()
            }
            if (participants.size == maxParticipants) {
                throw MaximumNumberOfParticipantsReachedException()
            }
        }
    }

}
