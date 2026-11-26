package rayyan.warehouse.workflowautomationengine.core.executor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import rayyan.warehouse.workflowautomationengine.core.executor.executorexceptions.InvalidConfigurationException;
import rayyan.warehouse.workflowautomationengine.core.executor.executoridentity.config.DelayConfig;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class DelayStepExecutorTests {

    // =================================================================================
    // ||
    // || Tests for the delay configuration.
    // ||
    // =================================================================================

    private DelayConfig delayConfig;

    @BeforeEach
    void setUp(){
         delayConfig = new DelayConfig();
    }

    @ParameterizedTest
    @MethodSource("invalidMillisecondsValues")
    void invalidMillisecondsValuesAreRejected(
            Integer milliseconds
    ){
        delayConfig.setMilliseconds(milliseconds);
        assertThrows(InvalidConfigurationException.class, () -> delayConfig.validate());
    }

    static Stream<Arguments> invalidMillisecondsValues(){

        return Stream.of(
                Arguments.of(-1),
                Arguments.of(DelayConfig.MIN_DELAY_MILLIS - 1),
                Arguments.of(DelayConfig.MAX_DELAY_MILLIS + 1)
        );
    }
}
