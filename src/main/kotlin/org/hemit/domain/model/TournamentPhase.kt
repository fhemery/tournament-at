package org.hemit.domain.model

interface TournamentPhase {
    fun computeMatches(participants: List<Participant>)

    val type: TournamentPhaseType
    var matches: List<Match>
}