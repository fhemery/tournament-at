package org.hemit.domain.model.tournament

class TournamentBuilder {
    companion object {
        fun from(tournamentToCreate: TournamentToCreate, id: String): RegisteredTournament {
            return RegisteredTournament(
                id,
                tournamentToCreate.name,
                maxParticipants = tournamentToCreate.maxParticipants
            )
        }
    }
}
