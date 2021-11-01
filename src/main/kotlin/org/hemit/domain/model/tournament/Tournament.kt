package org.hemit.domain.model.tournament

import org.hemit.domain.model.Participant
import org.hemit.domain.model.TournamentPhase

abstract class Tournament(
    val id: String,
    val name: String,
    var phases: List<TournamentPhase> = emptyList(),
    var participants: List<Participant> = emptyList()
) {
    abstract val status: TournamentStatus
}