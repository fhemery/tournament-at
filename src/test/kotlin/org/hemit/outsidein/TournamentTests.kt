package org.hemit.outsidein

import org.hemit.domain.ports.input.commands.CreateTournamentCommand
import org.hemit.domain.ports.input.queries.GetTournamentQuery
import org.hemit.infra.api.TournamentResource
import org.hemit.infra.api.dto.TournamentToCreateDto
import org.hemit.utils.ports.IdGeneratorStub
import org.hemit.utils.ports.TournamentStorageStub
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo
import javax.ws.rs.NotFoundException

class TournamentTests {

    private lateinit var tournamentResource: TournamentResource

    @BeforeEach
    fun setup() {
        tournamentResource = TournamentResource()
        val tournamentStorageStub = TournamentStorageStub()
        tournamentResource.getTournamentQuery = GetTournamentQuery(tournamentStorageStub)
        tournamentResource.createTournamentCommand = CreateTournamentCommand(tournamentStorageStub, IdGeneratorStub())

    }

    @Test
    fun `should return 404 when tournament does not exist`() {
        expectThrows<NotFoundException> { tournamentResource.getTournament("unknown") }
    }

    @Test
    fun `should enable to retrieve tournament after creation`() {
        val creationResponse = tournamentResource.createTournament(TournamentToCreateDto("Unreal tournament"))
        expectThat(creationResponse.status).isEqualTo(201)

        val id = creationResponse.headers["location"]!!.first().toString().split("/").last()
        val tournament = tournamentResource.getTournament(id)
        expectThat(tournament.name).isEqualTo("Unreal tournament")

    }
}