package org.hemit.domain.model

class SwissRoundTournament(
    override val id: String,
    override val name: String,
    override var participants: List<Participant> = emptyList()
) : Tournament {
    override val type: TournamentType = TournamentType.SwissRound

}
