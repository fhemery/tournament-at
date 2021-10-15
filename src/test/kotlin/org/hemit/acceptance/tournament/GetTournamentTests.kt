package org.hemit.acceptance.tournament

import org.hemit.BaseAcceptanceTest
import org.hemit.domain.model.Tournament
import org.hemit.domain.ports.input.queries.GetTournamentQuery
import org.hemit.domain.ports.input.queries.GetTournamentQueryResult
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo

class GetTournamentTests : BaseAcceptanceTest() {

    lateinit var tournamentQuery: GetTournamentQuery

    @BeforeEach
    fun setup() {
        tournamentQuery = GetTournamentQuery(tournamentStoragePort)
    }

    @Test
    fun `should return the tournament if it exists in storage`() {
        tournamentStoragePort.saveTournament(Tournament("1", "Tournament"))

        val result = tournamentQuery.execute("1")

        expectThat(result).isA<GetTournamentQueryResult.Success>().and {
            get { tournament.name }.isEqualTo("Tournament")
        }
    }

    @Test
    fun `should return tournament not found if it does not exist in storage`() {
        val result = tournamentQuery.execute("NonExisting")

        expectThat(result).isA<GetTournamentQueryResult.TournamentDoesNotExist>()
    }
}