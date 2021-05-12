package com.credo.database;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.credo.database");

        noClasses()
            .that()
            .resideInAnyPackage("com.credo.database.service..")
            .or()
            .resideInAnyPackage("com.credo.database.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..com.credo.database.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
