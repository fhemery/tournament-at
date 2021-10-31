package org.hemit.infra.api.dto

import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.serialization.Serializable
import javax.validation.constraints.NotBlank

@Serializable
data class TournamentDto(@field:NotBlank @JsonProperty("name") val name: String)