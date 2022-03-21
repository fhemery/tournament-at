package org.hemit.acceptance.tournament

import org.hemit.BaseAcceptanceTest
import org.hemit.domain.model.tournament.TournamentToCreate
import org.hemit.domain.ports.input.commands.CreateTournamentCommand
import org.hemit.domain.ports.input.commands.CreateTournamentResult
import org.hemit.domain.ports.input.queries.GetTournamentQuery
import org.hemit.domain.ports.input.queries.GetTournamentQueryResult
import org.hemit.utils.builders.TournamentToCreateBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.isNotEqualTo

class CreateTournamentTests : BaseAcceptanceTest() {

    private lateinit var createTournamentCommand: CreateTournamentCommand
    lateinit var getTournamentQuery: GetTournamentQuery

    @BeforeEach
    fun setup() {
        createTournamentCommand = CreateTournamentCommand(tournamentStoragePort, idGeneratorPort)
        getTournamentQuery = GetTournamentQuery(tournamentStoragePort)
    }

    @Test
    fun `should assign an Id to the tournament for later reuse`() {
        val result = createTournamentCommand.execute(
            TournamentToCreate(
                "Unreal Tournament"
            )
        )

        expectThat(result).isA<CreateTournamentResult.Success>().get { tournamentId }.isEqualTo("1")
    }

    @Test
    fun `should return a tournament`() {
        val tournamentId =
            createTournament(createTournamentCommand, "Bracket")

        val getResult = getTournamentQuery.execute(tournamentId)

        expectThat(getResult).isA<GetTournamentQueryResult.Success>().and {
            get { tournament.id }.isEqualTo(tournamentId)
        }
    }

    private fun createTournament(
        createTournamentCommand: CreateTournamentCommand,
        name: String
    ): String {
        val result = createTournamentCommand.execute(
            TournamentToCreateBuilder().withName(name).build()
        )
        expectThat(result).isA<CreateTournamentResult.Success>().get { tournamentId }.isNotEqualTo("")
        return (result as CreateTournamentResult.Success).tournamentId
    }
}
