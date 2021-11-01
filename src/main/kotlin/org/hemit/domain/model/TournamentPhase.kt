package org.hemit.domain.model

import org.hemit.domain.model.exceptions.MatchDoesNotExistException

interface TournamentPhase {
    fun computeMatches(participants: List<Participant>)
    fun setMatchScore(matchToUpdate: MatchToUpdate) {
        val match = matches.find { it.id == matchToUpdate.id } ?: throw MatchDoesNotExistException()
        match.winner = matchToUpdate.winnerName
        match.score = matchToUpdate.score
    }

    val type: TournamentPhaseType
    var matches: List<Match>
}