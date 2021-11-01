package org.hemit.infra.api.dto

import kotlinx.serialization.Serializable

@Serializable
class TournamentToUpdateDto(var status: TournamentStatusDto? = null)