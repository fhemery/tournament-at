package org.hemit.domain.model.policies

import org.hemit.domain.model.exceptions.NotEnoughParticipantsException
import org.hemit.domain.model.exceptions.TournamentAlreadyStartedException
import org.hemit.domain.model.exceptions.TournamentHasNoPhaseException
import org.hemit.domain.model.tournament.RegisteredTournament
import org.hemit.domain.model.tournament.TournamentStatus

class StartTournamentPolicy {
    fun check(registeredTournament: RegisteredTournament) {
        with(registeredTournament) {
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
