package rayyan.warehouse.workflowautomationengine.core.executor.delay;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import rayyan.warehouse.workflowautomationengine.core.executor.executorexceptions.InvalidConfigurationException;
import rayyan.warehouse.workflowautomationengine.core.executor.executoridentity.config.DelayConfig;
import rayyan.warehouse.workflowautomationengine.core.executor.executoridentity.types.DelayStepExecutor;
import rayyan.warehouse.workflowautomationengine.core.executor.executormodels.StepExecutionInput;
import rayyan.warehouse.workflowautomationengine.core.executor.executormodels.StepExecutionOutput;
import rayyan.warehouse.workflowautomationengine.model.StepType;
import tools.jackson.databind.json.JsonMapper;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DelayStepExecutorTest {

    private static final String VALID_CONFIG = "{\"milliseconds\": 10}";

    private final DelayStepExecutor executor =
            new DelayStepExecutor(JsonMapper.builder().build());

    // Safety net: if a test leaves the thread's interrupt flag set, clear it
    // so the next test is not affected.
    @AfterEach
    void clearInterruptFlag() {
        Thread.interrupted();
    }

    @Test
    void reportsDelayAsItsStepType() {
        assertEquals(StepType.DELAY, executor.stepType());
    }

    @Test
    void passesInputDataThroughUnchanged() throws InterruptedException {
        StepExecutionInput input = new StepExecutionInput(Map.of("status", 200, "name", "order"));

        StepExecutionOutput output = executor.execute(VALID_CONFIG, input);

        assertEquals(input.getData(), output.getData());
    }

    @Test
    void emptyInputGivesEmptyOutput() throws InterruptedException {
        StepExecutionOutput output = executor.execute(VALID_CONFIG, StepExecutionInput.empty());

        assertTrue(output.getData().isEmpty());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {
            "",                                  // empty text
            "   ",                               // blank text
            "null",                              // valid JSON, but no object
            "{not json",                         // broken JSON
            "{}",                                // milliseconds missing
            "{\"milliseconds\": \"abc\"}",       // wrong type
            "{\"milliseconds\": 99999999999}"    // too big for an Integer
    })
    void badConfigIsRejected(String config) {
        assertThrows(InvalidConfigurationException.class,
                () -> executor.execute(config, StepExecutionInput.empty()));
    }

    @Test
    void brokenJsonKeepsTheOriginalCause() {
        InvalidConfigurationException ex = assertThrows(InvalidConfigurationException.class,
                () -> executor.execute("{not json", StepExecutionInput.empty()));

        assertNotNull(ex.getCause());
    }

    // If validation ran after the sleep, this would take two minutes and the timeout would fail the test.
    @Test
    @Timeout(2)
    void outOfRangeDelayIsRejectedBeforeSleeping() {
        String config = "{\"milliseconds\": " + (DelayConfig.MAX_DELAY_MILLIS + 1) + "}";

        assertThrows(InvalidConfigurationException.class,
                () -> executor.execute(config, StepExecutionInput.empty()));
    }

    @Test
    void interruptionIsPassedUp() {
        Thread.currentThread().interrupt();   // flag already set, so sleep throws immediately

        assertThrows(InterruptedException.class,
                () -> executor.execute(VALID_CONFIG, StepExecutionInput.empty()));
    }
}