package org.hemit.integration

import io.quarkus.test.junit.QuarkusTest
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hemit.infra.api.dto.ParticipantDto
import org.hemit.infra.api.dto.TournamentToCreateDto
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.hasSize
import strikt.assertions.isEqualTo

@QuarkusTest
class ParticipantTests {

    @Test
    fun `should return 404 when tournament does not exist`() {
        Given {
            contentType("application/json")
            body(Json.encodeToString(ParticipantDto("Alice", 123)))
        } When {
            post("tournaments/unknown/participants")
        } Then {
            statusCode(404)
        }
    }

    @Test
    fun `should return 204 when participant gets added`() {
        val id = createTournament()

        addParticipantToTournament(id, ParticipantDto("Alice", 123))

        val tournament = getTournament(id)
        expectThat(tournament.participants).hasSize(1).and {
            get { first().name }.isEqualTo("Alice")
        }
    }


    @Test
    fun `should return 400 if participant was already added`() {
        val id = createTournament()

        addParticipantToTournament(id, ParticipantDto("Alice", 123))

        Given {
            contentType("application/json")
            body(Json.encodeToString(ParticipantDto("Alice", 123)))
        } When {
            post("tournaments/$id/participants")
        } Then {
            statusCode(400)
        }
    }

    @Test
    fun `should return 400 if max participant has been reached`() {
        val id = createTournament(TournamentToCreateDto("Tour", 2))

        addParticipantToTournament(id, ParticipantDto("Alice", 123))
        addParticipantToTournament(id, ParticipantDto("Bob", 456))

        Given {
            contentType("application/json")
            body(Json.encodeToString(ParticipantDto("Carol", 789)))
        } When {
            post("tournaments/$id/participants")
        } Then {
            statusCode(400)
        }
    }
}