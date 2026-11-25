package rayyan.warehouse.workflowautomationengine.core.executor.executormodels;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class StepExecutionOutput {

    private final Map<String, Object> data;

    public StepExecutionOutput(Map<String, Object> data) {
        Objects.requireNonNull(data, "data must not be null");
        this.data = Collections.unmodifiableMap(new LinkedHashMap<>(data));
    }

    public Map<String, Object> getData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StepExecutionOutput other)) return false;
        return data.equals(other.data);
    }

    @Override
    public int hashCode() {
        return data.hashCode();
    }

    @Override
    public String toString() {
        return "StepExecutionOutput" + data;
    }
}