package org.hemit

import org.hemit.utils.ports.IdGeneratorStub
import org.hemit.utils.ports.TournamentStorageStub
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