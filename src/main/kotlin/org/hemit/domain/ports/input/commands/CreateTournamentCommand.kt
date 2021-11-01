package org.hemit.domain.ports.input.commands

import org.hemit.domain.model.tournament.TournamentBuilder
import org.hemit.domain.model.tournament.TournamentToCreate
import org.hemit.domain.ports.output.IdGeneration
import org.hemit.domain.ports.output.TournamentStorage
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class CreateTournamentCommand() {
    constructor(
        tournamentStoragePort: TournamentStorage,
        idGeneratorPort: IdGeneration
    ) : this() {
        this.tournamentStoragePort = tournamentStoragePort
        this.idGeneratorPort = idGeneratorPort
    }

    @Inject
    lateinit var tournamentStoragePort: TournamentStorage

    @Inject
    lateinit var idGeneratorPort: IdGeneration


    fun execute(tournamentToCreate: TournamentToCreate): CreateTournamentResult {
        val tournament = TournamentBuilder.from(tournamentToCreate, idGeneratorPort.generateId())
        tournamentStoragePort.saveTournament(tournament)
        return CreateTournamentResult.Success(tournament.id)
    }
}

sealed class CreateTournamentResult {
    class Success(val tournamentId: String) : CreateTournamentResult()
}