package rayyan.warehouse.workflowautomationengine.core.executor.executormodels;


import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class StepExecutionInput {
    private final Map<String, Object> data;

    public StepExecutionInput(Map<String, Object> data){
        Objects.requireNonNull(data, "Data cannot be null");
        this.data = Collections.unmodifiableMap(new LinkedHashMap<>(data));
    }

    // used for first steps, where the input is empty.
    public static StepExecutionInput empty(){
        return new StepExecutionInput(Map.of());
    }

    public Map<String, Object> getData(){
        return data;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof StepExecutionInput other)) return false;
        return data.equals(other.data);
    }

    @Override
    public int hashCode() {
        return data.hashCode();
    }

    @Override
    public String toString() {
        return "Input for this step is"+data;
    }
}
