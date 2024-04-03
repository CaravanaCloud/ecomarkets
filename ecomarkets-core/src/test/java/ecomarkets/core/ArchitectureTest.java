package ecomarkets.core;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;
import static com.tngtech.archunit.base.DescribedPredicate.alwaysTrue;


@AnalyzeClasses(packages = "ecomarkets", importOptions = ImportOption.DoNotIncludeTests.class)
public class ArchitectureTest {

    @ArchTest
    static final ArchRule layer_dependencies_are_respected = layeredArchitecture().consideringOnlyDependenciesInLayers()

            .layer("Domain").definedBy("ecomarkets.core.domain..")
            .layer("Infra").definedBy("ecomarkets.core.infra..")

            .whereLayer("Domain").mayNotAccessAnyLayer();


    @ArchTest
    static final ArchRule no_cycles =
            slices().matching("ecomarkets.(**)..").should().beFreeOfCycles()
                    .ignoreDependency(resideInAPackage("..event.."), alwaysTrue());

}
