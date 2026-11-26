package rayyan.warehouse.workflowautomationengine.core.executor.delay;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import rayyan.warehouse.workflowautomationengine.core.executor.executorexceptions.InvalidConfigurationException;
import rayyan.warehouse.workflowautomationengine.core.executor.executoridentity.config.DelayConfig;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DelayConfigTest {

    @ParameterizedTest
    @NullSource
    @ValueSource(ints = {
            Integer.MIN_VALUE,
            -1,
            DelayConfig.MIN_DELAY_MILLIS - 1,   // zero is rejected on purpose
            DelayConfig.MAX_DELAY_MILLIS + 1,
            Integer.MAX_VALUE
    })
    void invalidValuesAreRejected(Integer milliseconds) {
        DelayConfig config = new DelayConfig();
        config.setMilliseconds(milliseconds);

        assertThrows(InvalidConfigurationException.class, () -> config.validate());
    }

    @ParameterizedTest
    @ValueSource(ints = {
            DelayConfig.MIN_DELAY_MILLIS,
            500,
            DelayConfig.MAX_DELAY_MILLIS
    })
    void validValuesAreAccepted(Integer milliseconds) {
        DelayConfig config = new DelayConfig();
        config.setMilliseconds(milliseconds);

        assertDoesNotThrow(() -> config.validate());
    }

    @Test
    void missingValueIsRejected() {
        DelayConfig config = new DelayConfig();   // never set, so it is null

        assertThrows(InvalidConfigurationException.class, () -> config.validate());
    }
}