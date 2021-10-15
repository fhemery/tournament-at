package org.hemit.domain.model.policies

import org.hemit.domain.model.Tournament
import org.hemit.domain.model.TournamentStatus
import org.hemit.domain.model.exceptions.NotEnoughParticipantsException
import org.hemit.domain.model.exceptions.TournamentAlreadyStartedException
import org.hemit.domain.model.exceptions.TournamentHasNoPhaseException

class StartTournamentPolicy {
    fun check(tournament: Tournament) {
        with(tournament) {
            if (participants.size < 2) {
                throw NotEnoughParticipantsException()
            }
            if (status != TournamentStatus.NotStarted) {
                throw TournamentAlreadyStartedException()
            }
            if (phases.isEmpty()) {
                throw TournamentHasNoPhaseException()
            }
        }
    }

}
