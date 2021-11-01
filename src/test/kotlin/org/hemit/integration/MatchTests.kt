package org.hemit.integration

import io.quarkus.test.junit.QuarkusTest
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hemit.infra.api.dto.MatchToUpdateDto
import org.hemit.infra.api.dto.ParticipantDto
import org.hemit.infra.api.dto.TournamentPhaseDto
import org.hemit.infra.api.dto.TournamentPhaseTypeDto
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@QuarkusTest
class MatchTests {

    @Test
    fun `should set match score`() {
        val id = createTournament()
        addParticipantToTournament(id, ParticipantDto("Alice", 10))
        addParticipantToTournament(id, ParticipantDto("Bob", 9))
        addPhase(id, TournamentPhaseDto(TournamentPhaseTypeDto.RoundRobin))
        startTournamentReturnsStatusCode(id, 204)

        Given {
            contentType("application/json")
            body(Json.encodeToString(MatchToUpdateDto("Alice", "1-0")))
        } When {
            patch("/tournaments/${id}/matches/1")
        } Then {
            statusCode(204)
        }

        val tournament = getTournament(id)
        val match = tournament.phases.first().matches!!.first()
        expectThat(match.score).isEqualTo("1-0")
        expectThat(match.winner).isEqualTo("Alice")
    }
}