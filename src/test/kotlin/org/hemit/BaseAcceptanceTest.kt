package org.hemit

import org.hemit.ports.IdGeneratorStub
import org.hemit.ports.TournamentStorageStub
import org.junit.jupiter.api.BeforeEach

open class BaseAcceptanceTest {
    lateinit var tournamentStoragePort: TournamentStorageStub
    lateinit var idGeneratorPort: IdGeneratorStub

    @BeforeEach
    fun baseSetup() {
        tournamentStoragePort = TournamentStorageStub()
        idGeneratorPort = IdGeneratorStub()
    }
}