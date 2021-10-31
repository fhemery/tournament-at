package org.hemit.integration

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.config.EncoderConfig
import io.restassured.config.JsonConfig
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hemit.infra.api.dto.TournamentDto
import org.junit.jupiter.api.Test

@QuarkusTest
class TournamentTests {
    init {
        RestAssured.config = RestAssured.config()
            .jsonConfig(JsonConfig.jsonConfig())
            .encoderConfig(EncoderConfig.encoderConfig().defaultContentCharset("utf-8"))
    }

    @Test
    fun `should return 404 when tournament does not exist`() {
        RestAssured.given()
            .`when`().get("/tournaments/unknown")
            .then()
            .statusCode(404)
    }

    @Test
    fun `should return the tournament with the id of creation`() {
        val response = Given {
            contentType("application/json")
            body(Json.encodeToString(TournamentDto("Unreal Tournament")))
        } When {
            post("/tournaments")
        } Then {
            statusCode(201)
        }

        val id = response.extract().header("location").split("/").last()

        When {
            get("/tournaments/$id")
        } Then {
            statusCode(200)
        }
    }
}