package org.hemit.infra.api

import org.hemit.domain.model.tournament.TournamentToCreate
import org.hemit.domain.ports.input.commands.CreateTournamentCommand
import org.hemit.domain.ports.input.commands.CreateTournamentResult
import org.hemit.domain.ports.input.commands.StartTournamentCommand
import org.hemit.domain.ports.input.commands.StartTournamentResult
import org.hemit.domain.ports.input.queries.GetTournamentQuery
import org.hemit.domain.ports.input.queries.GetTournamentQueryResult
import org.hemit.infra.api.dto.TournamentDto
import org.hemit.infra.api.dto.TournamentToCreateDto
import org.hemit.infra.api.dto.TournamentToUpdateDto
import org.hemit.infra.api.dto.toDto
import java.net.URI
import javax.inject.Inject
import javax.validation.Valid
import javax.ws.rs.BadRequestException
import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.NotFoundException
import javax.ws.rs.PATCH
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.core.Response.created

@Path("tournaments")
class TournamentResource {

    @Inject
    lateinit var getTournamentQuery: GetTournamentQuery

    @Inject
    lateinit var createTournamentCommand: CreateTournamentCommand

    @Inject
    lateinit var startTournamentCommand: StartTournamentCommand

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getTournament(@PathParam("id") id: String): TournamentDto {
        when (val result = getTournamentQuery.execute(id)) {
            is GetTournamentQueryResult.Success -> return toDto(result.tournament)
            GetTournamentQueryResult.TournamentDoesNotExist -> throw NotFoundException()
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun createTournament(@Valid tournament: TournamentToCreateDto): Response {
        when (
            val result =
                createTournamentCommand.execute(TournamentToCreate(tournament.name, tournament.maxParticipants))
        ) {
            is CreateTournamentResult.Success -> return created(URI.create("tournaments/${result.tournamentId}")).build()
        }
    }

    @PATCH
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun updateTournament(@PathParam("id") id: String, @Valid tournamentToUpdateDto: TournamentToUpdateDto) {
        if (tournamentToUpdateDto.status != null) {
            when (startTournamentCommand.execute(id)) {
                StartTournamentResult.NoPhaseDefined -> throw BadRequestException("No phase defined")
                StartTournamentResult.NotEnoughParticipants -> throw BadRequestException("Not enough participants")
                StartTournamentResult.Success -> Unit
                StartTournamentResult.TournamentAlreadyStarted -> throw BadRequestException("Tournament already started")
                StartTournamentResult.TournamentDoesNotExist -> throw NotFoundException("Tournament does not exist")
            }
        }
    }
}
