package rayyan.warehouse.workflowautomationengine.repository;

import rayyan.warehouse.workflowautomationengine.model.WorkflowExecutionSteps;
import rayyan.warehouse.workflowautomationengine.model.WorkflowExecutionStepStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkflowExecutionStepsRepository
        extends JpaRepository<WorkflowExecutionSteps, UUID> {

    List<WorkflowExecutionSteps> findAllByExecution_IdOrderByWorkflowStep_StepOrderAsc(
            UUID executionId
    );

    Optional<WorkflowExecutionSteps> findByIdAndExecution_Id(
            UUID stepExecutionId,
            UUID executionId
    );

    List<WorkflowExecutionSteps> findAllByStatus(
            WorkflowExecutionStepStatus status
    );

    boolean existsByExecution_IdAndStatus(
            UUID executionId,
            WorkflowExecutionStepStatus status
    );
}