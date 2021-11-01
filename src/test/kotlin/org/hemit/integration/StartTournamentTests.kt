package org.hemit.integration

import io.quarkus.test.junit.QuarkusTest
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hemit.infra.api.dto.*
import org.junit.jupiter.api.Test

@QuarkusTest
class StartTournamentTests {

    @Test
    fun `should return 400 on non existing tournament`() {
        startTournamentReturnsStatusCode("unknown", 404)
    }

    @Test
    fun `should return 400 if there are not enough participants`() {
        val id = createTournament()
        addPhase(id, TournamentPhaseDto(TournamentPhaseTypeDto.RoundRobin))

        startTournamentReturnsStatusCode(id, 400)
    }

    @Test
    fun `should return 400 if there is no phase defined`() {
        val id = createTournament()
        addParticipantToTournament(id, ParticipantDto("Alice", 123))
        addParticipantToTournament(id, ParticipantDto("Bob", 456))

        startTournamentReturnsStatusCode(id, 400)
    }

    @Test
    fun `should return 204 if tournament has been successfully started`() {
        val id = createTournament()
        addParticipantToTournament(id, ParticipantDto("Alice", 123))
        addParticipantToTournament(id, ParticipantDto("Bob", 456))

        addPhase(id, TournamentPhaseDto(TournamentPhaseTypeDto.RoundRobin))

        startTournamentReturnsStatusCode(id, 204)
    }

    @Test
    fun `should return 400 if tournament is already started`() {
        val id = createTournament()
        addParticipantToTournament(id, ParticipantDto("Alice", 123))
        addParticipantToTournament(id, ParticipantDto("Bob", 456))

        addPhase(id, TournamentPhaseDto(TournamentPhaseTypeDto.RoundRobin))

        startTournamentReturnsStatusCode(id, 204)
        startTournamentReturnsStatusCode(id, 400)
    }

    private fun startTournamentReturnsStatusCode(id: String, errorCode: Int) {
        Given {
            contentType("application/json")
            body(Json.encodeToString(TournamentToUpdateDto(TournamentStatusDto.Started)))
        } When {
            patch("tournaments/$id")
        } Then {
            statusCode(errorCode)
        }
    }

}

