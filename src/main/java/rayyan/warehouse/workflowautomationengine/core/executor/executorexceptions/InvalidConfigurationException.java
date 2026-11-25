package rayyan.warehouse.workflowautomationengine.core.executor.executorexceptions;

public class InvalidConfigurationException extends StepFailedException{
    public InvalidConfigurationException(String message) {
        super(message);
    }

    public InvalidConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
