package rayyan.warehouse.workflowautomationengine.core.executor.executoridentity.types;

import org.springframework.stereotype.Component;
import rayyan.warehouse.workflowautomationengine.core.executor.StepExecutor;
import rayyan.warehouse.workflowautomationengine.core.executor.executorexceptions.InvalidConfigurationException;
import rayyan.warehouse.workflowautomationengine.core.executor.executoridentity.config.DelayConfig;
import rayyan.warehouse.workflowautomationengine.core.executor.executormodels.StepExecutionInput;
import rayyan.warehouse.workflowautomationengine.core.executor.executormodels.StepExecutionOutput;
import rayyan.warehouse.workflowautomationengine.model.StepType;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Component
public class DelayStepExecutor implements StepExecutor {

    private final ObjectMapper objectMapper;

    public DelayStepExecutor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public StepType stepType() {
        return StepType.DELAY;
    }

    @Override
    public StepExecutionOutput execute(String config, StepExecutionInput input)
            throws InterruptedException {
        DelayConfig delayConfig = parse(config);
        delayConfig.validate();
        Thread.sleep(delayConfig.getMilliseconds());
        return new StepExecutionOutput(input.getData());
    }

    private DelayConfig parse(String config) {
        if (config == null || config.isBlank()) {
            throw new InvalidConfigurationException("Delay config is missing");
        }
        try {
            DelayConfig parsed = objectMapper.readValue(config, DelayConfig.class);
            if (parsed == null) {
                throw new InvalidConfigurationException("Delay config must be a JSON object");
            }
            return parsed;
        } catch (JacksonException e) {
            throw new InvalidConfigurationException("Delay config could not be read: " + e.getMessage(), e);
        }
    }
}