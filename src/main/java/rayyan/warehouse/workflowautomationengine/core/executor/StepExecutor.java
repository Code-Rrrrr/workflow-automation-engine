package rayyan.warehouse.workflowautomationengine.core.executor;

import rayyan.warehouse.workflowautomationengine.core.executor.executorexceptions.InvalidConfigurationException;
import rayyan.warehouse.workflowautomationengine.core.executor.executorexceptions.StepFailedException;
import rayyan.warehouse.workflowautomationengine.core.executor.executormodels.StepExecutionInput;
import rayyan.warehouse.workflowautomationengine.core.executor.executormodels.StepExecutionOutput;
import rayyan.warehouse.workflowautomationengine.model.StepType;

public interface StepExecutor {
     StepType stepType();

     /**
      * Runs this step's work and returns the data to pass to the next step.
      *
      * <p>Contract for every implementation:
      * <ul>
      *   <li>Keeps no state between calls. Per-call data (config, input) stays in
      *       parameters and local variables, never in fields.</li>
      *   <li>Never returns null.</li>
      *   <li>Does not touch the database or the workflow status. It only reports
      *       the result; the engine decides what happens next.</li>
      *   <li>Does not swallow interruption. If the thread is interrupted, the
      *       exception is passed up to the caller.</li>
      * </ul>
      *
      * @param config the step's raw JSON settings text, as stored on the step
      * @param input  the previous step's data; empty for the first step
      * @return the data to pass to the next step
      * @throws InvalidConfigurationException if the config is missing, malformed or has invalid values
      * @throws StepFailedException           if the step ran and failed
      * @throws InterruptedException          if the thread was interrupted while the step was running
      */
     StepExecutionOutput execute(String config, StepExecutionInput input)
             throws InterruptedException;
}
