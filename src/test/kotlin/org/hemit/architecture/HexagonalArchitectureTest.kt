package org.hemit.architecture

import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.library.Architectures.onionArchitecture

@AnalyzeClasses(packages = ["org.hemit"], importOptions = [ImportOption.DoNotIncludeTests::class])
class HexagonalArchitectureTest {
    @ArchTest
    val `Architecture should be a matching hexagonal architecture`: ArchRule =
        onionArchitecture()
            .withOptionalLayers(true)
            .domainModels("org.hemit.domain..")
            .adapter("api", "org.hemit.infra.api..")
            .adapter("idgeneration", "org.hemit.infra.idgeneration..")
            .adapter("storage", "org.hemit.infra.storage..")
}