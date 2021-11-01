package org.hemit.infra.api.dto

import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.serialization.Serializable
import org.hemit.domain.model.Participant
import org.hemit.domain.model.RoundRobinTournamentPhase
import org.hemit.domain.model.TournamentPhase
import org.hemit.domain.model.TournamentPhaseType
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
class TournamentPhaseDto(@JsonProperty("type") val type: TournamentPhaseTypeDto)

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
    TournamentPhaseDto(toTournamentPhaseTypeDto(it.type))

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
        toTournamentParticipants(tournamentSetup.participants)
    )
}

fun toTournamentParticipants(participants: List<Participant>): List<ParticipantDto> {
    return participants.map { ParticipantDto(it.name, it.elo) }
}
