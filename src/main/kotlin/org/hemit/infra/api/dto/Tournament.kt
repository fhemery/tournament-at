package org.hemit.infra.api.dto

import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.serialization.Serializable
import org.hemit.domain.model.*

@Serializable
data class TournamentDto(
    val id: String,
    val name: String,
    val status: TournamentStatusDto,
    val phases: List<TournamentPhaseDto>
)

@Serializable
enum class TournamentStatusDto { NotStarted, Started, Finished }

@Serializable
class TournamentPhaseDto(@JsonProperty("type") val type: TournamentPhaseTypeDto)

@Serializable
enum class TournamentPhaseTypeDto { SingleBracketElimination, RoundRobin }


private fun toTournamentStatusDto(status: TournamentStatus): TournamentStatusDto {
    return when (status) {
        TournamentStatus.NotStarted -> TournamentStatusDto.NotStarted
        TournamentStatus.Started -> TournamentStatusDto.Started
        TournamentStatus.Finished -> TournamentStatusDto.Finished
    }
}

private fun toTournamentPhaseTypeDto(type: TournamentPhaseType): TournamentPhaseTypeDto {
    return when (type) {
        TournamentPhaseType.SingleBracketElimination -> TournamentPhaseTypeDto.SingleBracketElimination
        TournamentPhaseType.RoundRobin -> TournamentPhaseTypeDto.RoundRobin
    }
}

private fun toTournamentPhases(phases: List<TournamentPhase>): List<TournamentPhaseDto> {
    return phases.map { toTournamentPhaseDto(it) }
}

fun toTournamentPhaseDto(it: TournamentPhase) =
    TournamentPhaseDto(toTournamentPhaseTypeDto(it.type))

fun toTournamentPhase(phaseDto: TournamentPhaseDto): TournamentPhase {
    return when (phaseDto.type) {
        TournamentPhaseTypeDto.SingleBracketElimination -> SingleEliminationBracketTournamentPhase()
        TournamentPhaseTypeDto.RoundRobin -> RoundRobinTournamentPhase()
    }
}

fun toDto(tournament: Tournament): TournamentDto {
    return TournamentDto(
        tournament.id,
        tournament.name,
        toTournamentStatusDto(tournament.status),
        toTournamentPhases(tournament.phases)
    )
}