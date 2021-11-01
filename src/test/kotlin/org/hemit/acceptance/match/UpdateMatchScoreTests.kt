package org.hemit.acceptance.match

import org.hemit.BaseAcceptanceTest
import org.hemit.domain.model.IndividualParticipant
import org.hemit.domain.model.Match
import org.hemit.domain.model.MatchToUpdate
import org.hemit.domain.model.RoundRobinTournamentPhase
import org.hemit.domain.model.tournament.OngoingTournament
import org.hemit.domain.ports.input.commands.UpdateMatchScoreCommand
import org.hemit.domain.ports.input.commands.UpdateMatchScoreResult
import org.hemit.domain.ports.output.GetTournamentResult
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo

class UpdateMatchScoreTests : BaseAcceptanceTest() {

    lateinit var updateMatchScoreCommand: UpdateMatchScoreCommand

    @BeforeEach
    fun setup() {
        updateMatchScoreCommand = UpdateMatchScoreCommand()
        updateMatchScoreCommand.storagePort = tournamentStoragePort
    }

    @Test
    fun `should return success when everything is right`() {
        val alice = IndividualParticipant("Alice", 10)
        val bob = IndividualParticipant("Bob", 9)
        val ongoingTournament = OngoingTournament(
            "123", "tournament", listOf(alice, bob), listOf(
                RoundRobinTournamentPhase(
                    listOf(
                        Match(1, alice, bob)
                    )
                )
            )
        )
        tournamentStoragePort.saveTournament(ongoingTournament)

        val result = updateMatchScoreCommand.execute("123", MatchToUpdate(1, "Alice", "1-0"))

        expectThat(result).isA<UpdateMatchScoreResult.Success>()

        val storedTournament = tournamentStoragePort.getTournament(ongoingTournament.id)
        expectThat(storedTournament).isA<GetTournamentResult.Success>()
            .get { tournament.phases.first().matches.first() }.and {
                get { score }.isEqualTo("1-0")
                get { winner }.isEqualTo("Alice")
            }
    }
}