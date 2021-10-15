package org.hemit.domain.model

interface Tournament {
    val id: String
    val name: String
    val type: TournamentType
    var participants: List<Participant>
}
