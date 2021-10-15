package org.hemit.architecture

import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.library.Architectures.onionArchitecture

@AnalyzeClasses(packages = ["org.hemit"])
class HexagonalArchitectureTest {
    //@ArchTest
    val `Architecture should be a matching hexagonal architecture`: ArchRule =
        onionArchitecture()
            .domainModels("org.hemit.domain.model..")
            .domainServices("org.hemit.domain.service..")
            .applicationServices("org.hemit.utils.ports.input..")

}