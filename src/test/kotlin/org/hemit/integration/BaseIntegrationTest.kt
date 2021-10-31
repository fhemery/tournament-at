package org.hemit.integration

import io.restassured.RestAssured
import io.restassured.config.EncoderConfig
import io.restassured.config.JsonConfig

open class BaseIntegrationTest {
    init {
        RestAssured.config = RestAssured.config()
            .jsonConfig(JsonConfig.jsonConfig())
            .encoderConfig(EncoderConfig.encoderConfig().defaultContentCharset("utf-8"))
    }
}