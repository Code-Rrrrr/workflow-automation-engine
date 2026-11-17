package rayyan.warehouse.workflowautomationengine.core;

import rayyan.warehouse.workflowautomationengine.model.WorkflowExecutionStatus;
import rayyan.warehouse.workflowautomationengine.model.WorkflowExecutionStepStatus;

import java.util.*;

public class StateMachine {
    private static final Map<WorkflowExecutionStatus, List<WorkflowExecutionStatus>> ALLOWED_WORKFLOW_EXECUTION_TRANSITIONS =
            Map.of(
                    WorkflowExecutionStatus.PENDING, List.of(WorkflowExecutionStatus.RUNNING),
                    WorkflowExecutionStatus.RUNNING, List.of(WorkflowExecutionStatus.COMPLETED, WorkflowExecutionStatus.FAILED),
                    WorkflowExecutionStatus.COMPLETED, List.of(),
                    WorkflowExecutionStatus.FAILED, List.of(WorkflowExecutionStatus.PENDING)
            );

    private static final Map<WorkflowExecutionStepStatus, List<WorkflowExecutionStepStatus>>  ALLOWED_WORKFLOW_STEPS_TRANSITIONS =
            Map.of(
                    WorkflowExecutionStepStatus.PENDING, List.of(WorkflowExecutionStepStatus.RUNNING, WorkflowExecutionStepStatus.SKIPPED),
                    WorkflowExecutionStepStatus.RUNNING, List.of(WorkflowExecutionStepStatus.COMPLETED, WorkflowExecutionStepStatus.FAILED),
                    WorkflowExecutionStepStatus.COMPLETED, List.of(),
                    WorkflowExecutionStepStatus.FAILED, List.of(WorkflowExecutionStepStatus.PENDING),
                    WorkflowExecutionStepStatus.SKIPPED, List.of()
            );

    // both the transitioning will be checked by a single method, via method overloading

    // check for workflow transition being valid
    public boolean checkTransition(WorkflowExecutionStatus from, WorkflowExecutionStatus to) {

        if(from == null || to == null) return false;

        List<WorkflowExecutionStatus> allowed_to = ALLOWED_WORKFLOW_EXECUTION_TRANSITIONS.get(from);
        if(allowed_to.contains(to)){
            return true;
        }
        return false;
    }

    // check for workflow step transition being valid
    public boolean checkTransition(WorkflowExecutionStepStatus from, WorkflowExecutionStepStatus to) {

        if(from == null || to == null) return false;

        List<WorkflowExecutionStepStatus> allowed_to = ALLOWED_WORKFLOW_STEPS_TRANSITIONS.get(from);
        if(allowed_to.contains(to)){
            return true;
        }
        return false;
    }
}
