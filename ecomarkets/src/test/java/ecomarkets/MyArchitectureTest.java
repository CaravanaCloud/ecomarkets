package ecomarkets;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

@AnalyzeClasses(packages = "ecomarkets", importOptions = ImportOption.DoNotIncludeTests.class)
public class MyArchitectureTest {

    @ArchTest
    static final ArchRule layer_dependencies_are_respected = layeredArchitecture().consideringOnlyDependenciesInLayers()

            .layer("Domain").definedBy("ecomarkets.domain..")
            .layer("Infra").definedBy("ecomarkets.infra..")
            .layer("RestServices").definedBy("ecomarkets.rs..")

            .whereLayer("RestServices").mayNotBeAccessedByAnyLayer()
            .whereLayer("Domain").mayNotAccessAnyLayer();


    @ArchTest
    static final ArchRule no_cycles =
            slices().matching("ecomarkets.(**)..").should().beFreeOfCycles();
}
