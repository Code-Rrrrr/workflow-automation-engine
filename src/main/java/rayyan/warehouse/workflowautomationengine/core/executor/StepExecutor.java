package rayyan.warehouse.workflowautomationengine.core.executor;

import rayyan.warehouse.workflowautomationengine.core.executor.executor_models.StepExecutionInput;
import rayyan.warehouse.workflowautomationengine.core.executor.executor_models.StepExecutionOutput;
import rayyan.warehouse.workflowautomationengine.model.StepType;

public interface StepExecutor {
     StepType stepType();

     /**
      * It keeps no state between calls, so don't store the input or config in fields.
      * Bad config throws your invalid-config exception.
      * A failed step throws your step-failure exception.
      * It never returns null.
      * It doesn't touch the database or the workflow status.
      * Interruption is passed up, not swallowed*/
     StepExecutionOutput execute(String config, StepExecutionInput input)
             throws InterruptedException;
}
