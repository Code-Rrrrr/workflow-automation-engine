package rayyan.warehouse.workflowautomationengine.core.executor.executoridentity.config;

import rayyan.warehouse.workflowautomationengine.core.executor.executorexceptions.InvalidConfigurationException;

public class DelayConfig {

    public static final int MIN_DELAY_MILLIS = 1;
    public static final int MAX_DELAY_MILLIS = 120_000;

    // Integer (not int) so a missing value stays null instead of silently becoming 0.
    private Integer milliseconds;

    public Integer getMilliseconds() {
        return milliseconds;
    }

    public void setMilliseconds(Integer milliseconds) {
        this.milliseconds = milliseconds;
    }

    public void validate() {
        if (milliseconds == null) {
            throw new InvalidConfigurationException("Delay config must contain a 'milliseconds' value");
        }
        if (milliseconds < MIN_DELAY_MILLIS || milliseconds > MAX_DELAY_MILLIS) {
            throw new InvalidConfigurationException(
                    "Delay must be between " + MIN_DELAY_MILLIS + " and " + MAX_DELAY_MILLIS
                            + " ms, but was " + milliseconds);
        }
    }
}