package org.hemit.infra.api.dto

import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.serialization.Serializable
import org.hemit.domain.model.*
import org.hemit.domain.model.tournament.Tournament
import org.hemit.domain.model.tournament.TournamentStatus

@Serializable
data class TournamentDto(
    val id: String,
    val name: String,
    val status: TournamentStatusDto,
    val phases: List<TournamentPhaseDto>,
    val participants: List<ParticipantDto>
)

@Serializable
data class ParticipantDto(val name: String, val elo: Int)

@Serializable
enum class TournamentStatusDto { NotStarted, Started, Finished }

@Serializable
class TournamentPhaseDto(@JsonProperty("type") val type: TournamentPhaseTypeDto, val matches: List<MatchDto>? = null)

@Serializable
class MatchDto(val opponent1: ParticipantDto?, val opponent2: ParticipantDto?, val id: String)

@Serializable
enum class TournamentPhaseTypeDto { RoundRobin }


private fun toTournamentStatusDto(status: TournamentStatus): TournamentStatusDto {
    return when (status) {
        TournamentStatus.NotStarted -> TournamentStatusDto.NotStarted
        TournamentStatus.Started -> TournamentStatusDto.Started
        TournamentStatus.Finished -> TournamentStatusDto.Finished
    }
}

private fun toTournamentPhaseTypeDto(type: TournamentPhaseType): TournamentPhaseTypeDto {
    return when (type) {
        TournamentPhaseType.RoundRobin -> TournamentPhaseTypeDto.RoundRobin
    }
}

private fun toTournamentPhases(phases: List<TournamentPhase>): List<TournamentPhaseDto> {
    return phases.map { toTournamentPhaseDto(it) }
}

fun toTournamentPhaseDto(it: TournamentPhase) =
    TournamentPhaseDto(toTournamentPhaseTypeDto(it.type), toMatches(it.matches))

fun toMatches(matches: List<Match>): List<MatchDto>? {
    if (matches.isEmpty()) {
        return null
    }
    return matches.map { toMatch(it) }
}

fun toMatch(match: Match): MatchDto {
    return MatchDto(
        if (match.opponent1 != null) toParticipantDto(match.opponent1) else null,
        if (match.opponent2 != null) toParticipantDto(match.opponent2) else null,
        ""
    )
}

fun toTournamentPhase(phaseDto: TournamentPhaseDto): TournamentPhase {
    return when (phaseDto.type) {
        TournamentPhaseTypeDto.RoundRobin -> RoundRobinTournamentPhase()
    }
}

fun toDto(tournamentSetup: Tournament): TournamentDto {
    return TournamentDto(
        tournamentSetup.id,
        tournamentSetup.name,
        toTournamentStatusDto(tournamentSetup.status),
        toTournamentPhases(tournamentSetup.phases),
        tournamentSetup.participants.map { toParticipantDto(it) }
    )
}

fun toParticipantDto(participant: Participant): ParticipantDto {
    return ParticipantDto(participant.name, participant.elo)
}
