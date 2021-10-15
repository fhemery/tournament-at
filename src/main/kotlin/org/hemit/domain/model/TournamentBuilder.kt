package org.hemit.domain.model

class TournamentBuilder {
    companion object {
        fun from(tournamentToCreate: TournamentToCreate, id: String): Tournament {
            return when (tournamentToCreate.type) {
                TournamentType.SingleBracketElimination -> SingleBracketTournament(id, tournamentToCreate.name)
                TournamentType.SwissRound -> SwissRoundTournament(id, tournamentToCreate.name)
            }
        }
    }
}
