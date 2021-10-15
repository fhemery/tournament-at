package org.hemit.utils.builders

import org.hemit.domain.model.TournamentToCreate
import org.hemit.domain.model.TournamentType

class TournamentToCreateBuilder {
    var type = TournamentType.SingleBracketElimination
    var name = "Default"

    fun withType(type: TournamentType): TournamentToCreateBuilder {
        this.type = type
        return this
    }

    fun withName(name: String): TournamentToCreateBuilder {
        this.name = name
        return this
    }

    fun build(): TournamentToCreate {
        return TournamentToCreate(name, type)
    }
}