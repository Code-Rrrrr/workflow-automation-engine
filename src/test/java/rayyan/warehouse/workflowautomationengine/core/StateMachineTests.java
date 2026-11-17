package rayyan.warehouse.workflowautomationengine.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import rayyan.warehouse.workflowautomationengine.model.WorkflowExecutionStatus;
import rayyan.warehouse.workflowautomationengine.model.WorkflowExecutionStepStatus;

import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class StateMachineTest {

    private StateMachine stateMachine;

    @BeforeEach
    void setUp() {
        // Arrange (shared system under test)
        stateMachine = new StateMachine();
    }

    // --- Workflow Execution Tests ---

    @ParameterizedTest
    @MethodSource("validExecutionTransitions")
    void checkTransition_shouldReturnTrue_forValidExecutionTransitions(
            WorkflowExecutionStatus from,
            WorkflowExecutionStatus to
    ) {
        // Arrange (inputs provided by MethodSource)

        // Act
        boolean result = stateMachine.checkTransition(from, to);

        // Assert
        assertTrue(result, () -> "Expected transition " + from + " -> " + to + " to be valid");
    }

    @ParameterizedTest
    @MethodSource("invalidExecutionTransitions")
    void checkTransition_shouldReturnFalse_forInvalidExecutionTransitions(
            WorkflowExecutionStatus from,
            WorkflowExecutionStatus to
    ) {
        // Arrange (inputs provided by MethodSource)

        // Act
        boolean result = stateMachine.checkTransition(from, to);

        // Assert
        assertFalse(result, () -> "Expected transition " + from + " -> " + to + " to be invalid");
    }

    @Test
    void checkTransition_shouldReturnFalse_whenExecutionStatusIsNull() {
        // Arrange
        WorkflowExecutionStatus validStatus = WorkflowExecutionStatus.PENDING;

        // Act
        boolean fromNull = stateMachine.checkTransition(null, validStatus);
        boolean toNull = stateMachine.checkTransition(validStatus, null);
        boolean bothNull = stateMachine.checkTransition((WorkflowExecutionStatus) null, null);

        // Assert
        assertFalse(fromNull);
        assertFalse(toNull);
        assertFalse(bothNull);
    }

    static Stream<Arguments> validExecutionTransitions() {
        return Stream.of(
                Arguments.of(WorkflowExecutionStatus.PENDING, WorkflowExecutionStatus.RUNNING),
                Arguments.of(WorkflowExecutionStatus.RUNNING, WorkflowExecutionStatus.COMPLETED),
                Arguments.of(WorkflowExecutionStatus.RUNNING, WorkflowExecutionStatus.FAILED),
                Arguments.of(WorkflowExecutionStatus.FAILED, WorkflowExecutionStatus.PENDING)
        );
    }

    static Stream<Arguments> invalidExecutionTransitions() {
        Set<Arguments> valid = Set.of(
                Arguments.of(WorkflowExecutionStatus.PENDING, WorkflowExecutionStatus.RUNNING),
                Arguments.of(WorkflowExecutionStatus.RUNNING, WorkflowExecutionStatus.COMPLETED),
                Arguments.of(WorkflowExecutionStatus.RUNNING, WorkflowExecutionStatus.FAILED),
                Arguments.of(WorkflowExecutionStatus.FAILED, WorkflowExecutionStatus.PENDING)
        );

        return Stream.of(WorkflowExecutionStatus.values())
                .flatMap(from -> Stream.of(WorkflowExecutionStatus.values())
                        .map(to -> Arguments.of(from, to)))
                .filter(pair -> !valid.contains(pair));
    }
}