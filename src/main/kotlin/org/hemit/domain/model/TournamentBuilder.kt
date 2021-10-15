package org.hemit.domain.model

class TournamentBuilder {
    companion object {
        fun from(tournamentToCreate: TournamentToCreate, id: String): Tournament {
            return SingleBracketTournament(id,  tournamentToCreate.name)
        }
    }
}
