package org.hemit.integration

import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hemit.infra.api.dto.TournamentDto
import org.hemit.infra.api.dto.TournamentToCreateDto

fun createTournament(name: String = "Unreal Tournament"): String {
    val response = Given {
        contentType("application/json")
        body(Json.encodeToString(TournamentToCreateDto(name)))
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