package org.hemit.infra.api

import org.hemit.domain.model.TournamentToCreate
import org.hemit.domain.ports.input.commands.CreateTournamentCommand
import org.hemit.domain.ports.input.commands.CreateTournamentResult
import org.hemit.domain.ports.input.queries.GetTournamentQuery
import org.hemit.domain.ports.input.queries.GetTournamentQueryResult
import org.hemit.infra.api.dto.TournamentDto
import org.hemit.infra.api.dto.TournamentToCreateDto
import org.hemit.infra.api.dto.toDto
import org.jboss.resteasy.annotations.ResponseObject
import java.net.URI
import javax.inject.Inject
import javax.validation.Valid
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.core.Response.created

@Path("tournaments")
class TournamentResource {

    @Inject
    lateinit var getTournamentQuery: GetTournamentQuery

    @Inject
    lateinit var createTournamentCommand: CreateTournamentCommand

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getTournament(@PathParam("id") id: String): TournamentDto {
        println("Fetching tournament with id $id")
        when (val result = getTournamentQuery.execute(id)) {
            is GetTournamentQueryResult.Success -> return toDto(result.tournament)
            GetTournamentQueryResult.TournamentDoesNotExist -> throw NotFoundException()
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ResponseObject()
    fun createTournament(@Valid tournament: TournamentToCreateDto): Response {
        when (val result = createTournamentCommand.execute(TournamentToCreate(tournament.name))) {
            is CreateTournamentResult.Success -> return created(URI.create("tournaments/${result.tournamentId}")).build()
        }
    }
}


