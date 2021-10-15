package org.hemit.domain.ports.input.commands

import org.hemit.domain.model.TournamentBuilder
import org.hemit.domain.model.TournamentToCreate
import org.hemit.domain.ports.output.IdGeneration
import org.hemit.domain.ports.output.TournamentStorage

class CreateTournamentCommand(val tournamentStoragePort: TournamentStorage, private val idGeneratorPort: IdGeneration) {

    fun execute(tournamentToCreate: TournamentToCreate): CreateTournamentResult {
        val tournament = TournamentBuilder.from(tournamentToCreate, idGeneratorPort.generateId())
        return CreateTournamentResult.Success(tournament.id)
    }
}

sealed class CreateTournamentResult{
    class Success(val tournamentId: String): CreateTournamentResult()
}