package org.hemit.domain.ports.input.commands

import org.hemit.domain.model.MatchToUpdate
import org.hemit.domain.ports.output.TournamentStorage
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class UpdateMatchScoreCommand {

    @Inject
    lateinit var storagePort: TournamentStorage

    fun execute(tournamentId: String, matchToUpdate: MatchToUpdate): UpdateMatchScoreResult {
        TODO()
    }
}

sealed class UpdateMatchScoreResult {
    object Success : UpdateMatchScoreResult()
}