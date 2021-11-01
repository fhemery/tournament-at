package org.hemit.infra.api.dto

import kotlinx.serialization.Serializable

@Serializable
class MatchToUpdateDto(val winner: String, val score: String)