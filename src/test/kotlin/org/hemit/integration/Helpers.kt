package org.hemit.integration

import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hemit.infra.api.dto.ParticipantDto
import org.hemit.infra.api.dto.TournamentDto
import org.hemit.infra.api.dto.TournamentPhaseDto
import org.hemit.infra.api.dto.TournamentToCreateDto

fun createTournament(tournament: TournamentToCreateDto = TournamentToCreateDto("Unreal tournament", 1024)): String {
    val response = Given {
        contentType("application/json")
        body(Json.encodeToString(tournament))
    } When {
        post("/tournaments")
    } Then {
        statusCode(201)
    }

    return response.extract().header("location").split("/").last()
}

fun getTournament(id: String): TournamentDto {
    val response = When {
        get("/tournaments/$id")
    } Then {
        statusCode(200)
    }

    return Json.decodeFromString(response.extract().asString())
}

fun addParticipantToTournament(id: String, participantDto: ParticipantDto) {
    Given {
        contentType("application/json")
        body(Json.encodeToString(participantDto))
    } When {
        post("tournaments/${id}/participants")
    } Then {
        statusCode(204)
    }
}

fun addPhase(id: String, tournamentPhaseDto: TournamentPhaseDto) {
    Given {
        contentType("application/json")
        body(Json.encodeToString(tournamentPhaseDto))
    } When {
        post("/tournaments/$id/phases")
    } Then {
        statusCode(204)
    }
}