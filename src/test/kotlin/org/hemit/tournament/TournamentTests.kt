package org.hemit.tournament

import org.hemit.BaseAcceptanceTest
import org.hemit.domain.model.TournamentToCreate
import org.hemit.domain.model.TournamentType
import org.hemit.domain.ports.input.commands.CreateTournamentCommand
import org.hemit.domain.ports.input.commands.CreateTournamentResult
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo

class TournamentTests : BaseAcceptanceTest() {

    @Test
    fun `should assign an Id to the tournament for later reuse`() {
        val createTournamentCommand = CreateTournamentCommand(tournamentStoragePort, idGeneratorPort)

        val result = createTournamentCommand.execute(
            TournamentToCreate(
                "Unreal Tournament",
                TournamentType.SingleBracketElimination
            )
        )

        expectThat(result).isA<CreateTournamentResult.Success>().get { tournamentId }.isEqualTo("1")
    }
}