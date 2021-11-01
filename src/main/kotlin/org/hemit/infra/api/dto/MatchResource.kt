package org.hemit.infra.api.dto

import org.hemit.domain.model.MatchToUpdate
import org.hemit.domain.ports.input.commands.UpdateMatchScoreCommand
import org.hemit.domain.ports.input.commands.UpdateMatchScoreResult
import javax.inject.Inject
import javax.ws.rs.Consumes
import javax.ws.rs.PATCH
import javax.ws.rs.Path
import javax.ws.rs.PathParam

@Path("/tournaments/{tournamentId}/matches")
class MatchResource {

    @Inject
    lateinit var updateMatchScoreCommand: UpdateMatchScoreCommand

    @Path("{matchId}")
    @PATCH
    @Consumes("application/json")
    fun updateMatchScore(
        @PathParam("tournamentId") tournamentId: String,
        @PathParam("matchId") matchId: String,
        matchToUpdate: MatchToUpdateDto
    ) {
        when (updateMatchScoreCommand.execute(
            tournamentId,
            MatchToUpdate(matchId.toInt(), matchToUpdate.winner, matchToUpdate.score)
        )) {
            UpdateMatchScoreResult.Success -> Unit
        }
    }
}