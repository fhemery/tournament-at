package org.hemit.tournament

import org.hemit.BaseAcceptanceTest
import org.hemit.domain.model.SingleBracketTournament
import org.hemit.domain.model.SwissRoundTournament
import org.hemit.domain.model.TournamentToCreate
import org.hemit.domain.model.TournamentType
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
                "Unreal Tournament",
                TournamentType.SingleBracketElimination
            )
        )

        expectThat(result).isA<CreateTournamentResult.Success>().get { tournamentId }.isEqualTo("1")
    }

    @Test
    fun `should return a single bracket tournament when it was the created type`() {
        val tournamentId = createTournament(createTournamentCommand, "Bracket", TournamentType.SingleBracketElimination)

        val getResult = getTournamentQuery.execute(tournamentId)

        expectThat(getResult).isA<GetTournamentQueryResult.Success>().and {
            get { tournament }.isA<SingleBracketTournament>()
            get { tournament.id }.isEqualTo(tournamentId)
        }
    }

    @Test
    fun `should return a swiss round tournament when it was the requested type`() {
        val tournamentId = createTournament(createTournamentCommand, "Swiss", TournamentType.SwissRound)

        val getResult = getTournamentQuery.execute(tournamentId)

        expectThat(getResult).isA<GetTournamentQueryResult.Success>().and {
            get { tournament }.isA<SwissRoundTournament>()
            get { tournament.id }.isEqualTo(tournamentId)
        }
    }

    private fun createTournament(
        createTournamentCommand: CreateTournamentCommand,
        name: String,
        type: TournamentType
    ): String {
        val result = createTournamentCommand.execute(
            TournamentToCreateBuilder().withName(name).withType(type).build()
        )
        expectThat(result).isA<CreateTournamentResult.Success>().get { tournamentId }.isNotEqualTo("")
        return (result as CreateTournamentResult.Success).tournamentId
    }

}