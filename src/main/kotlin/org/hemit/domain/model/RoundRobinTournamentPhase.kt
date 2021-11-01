package org.hemit.domain.model

class RoundRobinTournamentPhase() : TournamentPhase {
    override fun computeMatches(participants: List<Participant>) {
        if (participants.size == 2) {
            matches = listOf(Match(1, participants.last(), participants.first()))
            return
        }

        var idCounter = 1

        var evenParticipants =
            if (participants.size % 2 == 0) participants else participants + IndividualParticipant("--- FAKE ---", 0)
        val nbParticipants = evenParticipants.size

        val matches = mutableListOf<Match>()
        for (round in 0 until nbParticipants - 1) {
            for (partIndex in 0 until nbParticipants / 2) {
                matches.add(
                    Match(
                        idCounter++,
                        evenParticipants[nbParticipants / 2 + partIndex],
                        evenParticipants[partIndex]
                    )
                )
            }

            evenParticipants = emptyList<Participant>() +
                    evenParticipants.first() +
                    evenParticipants[nbParticipants / 2] +
                    evenParticipants.subList(1, nbParticipants / 2 - 1) +
                    evenParticipants.subList(nbParticipants / 2 + 1, nbParticipants) +
                    evenParticipants[nbParticipants / 2 - 1]
        }

        this.matches = matches.filter { it.opponent1?.name != "--- FAKE ---" && it.opponent2?.name != "--- FAKE ---" }
    }

    override val type = TournamentPhaseType.RoundRobin
    override var matches: List<Match> = emptyList()

    constructor(matches: List<Match>) : this() {
        this.matches = matches
    }
}