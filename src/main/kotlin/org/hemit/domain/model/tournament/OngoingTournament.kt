package org.hemit.domain.model.tournament

import org.hemit.domain.model.Participant
import org.hemit.domain.model.TournamentPhase

class OngoingTournament(
    id: String,
    name: String,
    participants: List<Participant> = emptyList(),
    phases: List<TournamentPhase> = emptyList(),
) : Tournament(id, name, phases, participants) {
    override val status: TournamentStatus = TournamentStatus.Started

    var currentPhase: TournamentPhase? = null
        private set

    init {
        currentPhase = phases.first()
    }
}