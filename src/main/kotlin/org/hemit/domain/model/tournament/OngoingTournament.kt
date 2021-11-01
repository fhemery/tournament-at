package org.hemit.domain.model.tournament

import org.hemit.domain.model.MatchToUpdate
import org.hemit.domain.model.Participant
import org.hemit.domain.model.TournamentPhase

class OngoingTournament(
    id: String,
    name: String,
    participants: List<Participant> = emptyList(),
    phases: List<TournamentPhase> = emptyList(),
    val currentPhaseIndex: Int = 0
) : Tournament(id, name, phases, participants) {
    private val currentPhase: TournamentPhase

    fun setMatchScore(matchToUpdate: MatchToUpdate) {
        currentPhase.setMatchScore(matchToUpdate)
    }

    override val status: TournamentStatus = TournamentStatus.Started

    init {
        currentPhase = phases[currentPhaseIndex]
        if (currentPhase.matches.isEmpty()) {
            currentPhase.computeMatches(participants)
        }
    }
}