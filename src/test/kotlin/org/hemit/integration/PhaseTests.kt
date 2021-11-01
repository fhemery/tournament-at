package org.hemit.integration

import io.quarkus.test.junit.QuarkusTest
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hemit.infra.api.dto.TournamentPhaseDto
import org.hemit.infra.api.dto.TournamentPhaseTypeDto
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.hasSize
import strikt.assertions.isEqualTo

@QuarkusTest
@DisplayName("Phase Integration tests")
class PhaseTests : BaseIntegrationTest() {

    @Test
    fun `should return 404 when adding phase to unknown tournament`() {
        Given {
            contentType("application/json")
            body(Json.encodeToString(TournamentPhaseDto(TournamentPhaseTypeDto.RoundRobin)))
        } When {
            post("/tournaments/unknown/phases")
        } Then {
            statusCode(404)
        }
    }

    @Test
    fun `should return the phase in tournament details when tournament exists`() {
        val id = createTournament()

        addPhase(id, TournamentPhaseDto(TournamentPhaseTypeDto.RoundRobin))

        val tournament = getTournament(id)
        expectThat(tournament.phases).hasSize(1).and {
            get { first().type }.isEqualTo(TournamentPhaseTypeDto.RoundRobin)
        }
    }

}