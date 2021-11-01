package org.hemit.domain.model.tournament

data class TournamentToCreate(val name: String, val maxParticipants: Int = Int.MAX_VALUE)