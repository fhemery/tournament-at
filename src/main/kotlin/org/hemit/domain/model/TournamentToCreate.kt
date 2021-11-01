package org.hemit.domain.model

data class TournamentToCreate(val name: String, val maxParticipants: Int = Int.MAX_VALUE)