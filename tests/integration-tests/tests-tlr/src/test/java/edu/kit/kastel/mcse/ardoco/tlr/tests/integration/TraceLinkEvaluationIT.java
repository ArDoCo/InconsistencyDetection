/* Licensed under MIT 2023-2024. */
package edu.kit.kastel.mcse.ardoco.tlr.tests.integration;

import static edu.kit.kastel.mcse.ardoco.core.tests.eval.ProjectHelper.ANALYZE_CODE_DIRECTLY;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import edu.kit.kastel.mcse.ardoco.core.api.output.ArDoCoResult;
import edu.kit.kastel.mcse.ardoco.core.common.RepositoryHandler;
import edu.kit.kastel.mcse.ardoco.core.tests.eval.CodeProject;
import edu.kit.kastel.mcse.ardoco.core.tests.eval.GoldStandardProject;
import edu.kit.kastel.mcse.ardoco.core.tests.eval.Project;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TraceLinkEvaluationIT<T extends GoldStandardProject> {

    protected static final String OUTPUT = "target/testout-tlr-it";

    protected static final String LOGGING_ARDOCO_CORE = "org.slf4j.simpleLogger.log.edu.kit.kastel.mcse.ardoco.core";
    protected static AtomicBoolean analyzeCodeDirectly = ANALYZE_CODE_DIRECTLY;

    @BeforeAll
    static void beforeAll() {
        System.setProperty(LOGGING_ARDOCO_CORE, "info");
    }

    @AfterAll
    static void afterAll() {
        System.setProperty(LOGGING_ARDOCO_CORE, "error");
    }

    private static void cleanUpCodeRepository(CodeProject codeProject) {
        RepositoryHandler.removeRepository(codeProject.getCodeLocation());
    }

    @EnabledIfEnvironmentVariable(named = "testCodeFull", matches = ".*")
    @DisplayName("Evaluate SAD-SAM-Code TLR (Full)")
    @ParameterizedTest(name = "{0}")
    @EnumSource(CodeProject.class)
    @Order(1)
    void evaluateSadSamCodeTlrFullIT(CodeProject project) {
        analyzeCodeDirectly.set(true);
        if (analyzeCodeDirectly.get())
            cleanUpCodeRepository(project);

        var evaluation = new SadSamCodeTraceabilityLinkRecoveryEvaluation();
        ArDoCoResult results = evaluation.runTraceLinkEvaluation(project);
        Assertions.assertNotNull(results);
    }

    @EnabledIfEnvironmentVariable(named = "testCodeFull", matches = ".*")
    @DisplayName("Evaluate SAM-Code TLR (Full)")
    @ParameterizedTest(name = "{0}")
    @EnumSource(value = CodeProject.class)
    @Order(2)
    void evaluateSamCodeTlrFullIT(CodeProject project) {
        analyzeCodeDirectly.set(true);
        if (analyzeCodeDirectly.get())
            cleanUpCodeRepository(project);

        var evaluation = new SamCodeTraceabilityLinkRecoveryEvaluation();
        var results = evaluation.runTraceLinkEvaluation(project);
        Assertions.assertNotNull(results);
    }

    @DisplayName("Evaluate SAD-SAM-Code TLR")
    @ParameterizedTest(name = "{0}")
    @EnumSource(CodeProject.class)
    @Order(9)
    void evaluateSadSamCodeTlrIT(CodeProject codeProject) {
        analyzeCodeDirectly.set(false);
        if (analyzeCodeDirectly.get())
            cleanUpCodeRepository(codeProject);

        var evaluation = new SadSamCodeTraceabilityLinkRecoveryEvaluation();
        var results = evaluation.runTraceLinkEvaluation(codeProject);
        Assertions.assertNotNull(results);

        TraceabilityLinkRecoveryEvaluation.resultMap.put(codeProject, results);
    }

    @DisplayName("Evaluate SAM-Code TLR")
    @ParameterizedTest(name = "{0}")
    @EnumSource(CodeProject.class)
    @Order(10)
    void evaluateSamCodeTlrIT(CodeProject project) {
        analyzeCodeDirectly.set(false);
        if (analyzeCodeDirectly.get())
            cleanUpCodeRepository(project);

        var evaluation = new SamCodeTraceabilityLinkRecoveryEvaluation();
        ArDoCoResult results = evaluation.runTraceLinkEvaluation(project);
        Assertions.assertNotNull(results);
    }

    @DisplayName("Evaluate SAD-SAM TLR")
    @ParameterizedTest(name = "{0}")
    @MethodSource("getProjects")
    @Order(20)
    void evaluateSadSamTlrIT(T project) {
        var evaluation = new SadSamTraceabilityLinkRecoveryEvaluation<>();
        var arDoCoResult = evaluation.runTraceLinkEvaluation(project);
        Assertions.assertNotNull(arDoCoResult);
    }

    private static List<? extends GoldStandardProject> getProjects() {
        return Arrays.asList(Project.values());
    }
}
