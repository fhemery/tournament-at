package org.hemit.infra.api

import org.hemit.domain.ports.input.commands.AddTournamentPhaseCommand
import org.hemit.domain.ports.input.commands.AddTournamentPhaseResult
import org.hemit.infra.api.dto.TournamentPhaseDto
import org.hemit.infra.api.dto.toTournamentPhase
import javax.inject.Inject
import javax.ws.rs.*

@Path("tournaments/{tournamentId}/phases")
class PhaseResource {

    @Inject
    lateinit var addTournamentPhaseCommand: AddTournamentPhaseCommand

    @POST()
    @Consumes("application/json")
    @Produces("application/json")
    fun addPhaseToTournament(@PathParam("tournamentId") tournamentId: String, phase: TournamentPhaseDto) {
        when (addTournamentPhaseCommand.execute(tournamentId, toTournamentPhase(phase))) {
            AddTournamentPhaseResult.Success -> Unit
            AddTournamentPhaseResult.TournamentDoesNotExist -> throw NotFoundException()
        }
    }
}