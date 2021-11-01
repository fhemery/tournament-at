package org.hemit.integration

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import org.hemit.infra.api.dto.TournamentToCreateDto
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@QuarkusTest
@DisplayName("Tournament Integration tests")
class TournamentTests : BaseIntegrationTest() {
    @Test
    fun `should return 404 when tournament does not exist`() {
        RestAssured.given()
            .`when`().get("/tournaments/unknown")
            .then()
            .statusCode(404)
    }

    @Test
    fun `should return the tournament with the id of creation`() {
        val id = createTournament(TournamentToCreateDto(name = "Tour"))

        expectThat(getTournament(id).name).isEqualTo("Tour")
    }

}