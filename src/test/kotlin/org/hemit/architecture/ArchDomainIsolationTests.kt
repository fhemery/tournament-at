package org.hemit.architecture

import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition

@AnalyzeClasses(packages = ["org.hemit"])
class ArchDomainIsolationTests {

    @ArchTest
    val `Domain should know nothing of infrastructure`: ArchRule =
        ArchRuleDefinition.noClasses().that().resideInAPackage("..domain..")
            .should().accessClassesThat().resideInAPackage("..infra..")
}