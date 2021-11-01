package org.hemit.utils.builders

import org.hemit.domain.model.TournamentPhaseType
import org.hemit.domain.model.tournament.TournamentToCreate

class TournamentToCreateBuilder {
    var type = TournamentPhaseType.SingleBracketElimination
    var name = "Default"

    fun withName(name: String): TournamentToCreateBuilder {
        this.name = name
        return this
    }

    fun build(): TournamentToCreate {
        return TournamentToCreate(name)
    }
}