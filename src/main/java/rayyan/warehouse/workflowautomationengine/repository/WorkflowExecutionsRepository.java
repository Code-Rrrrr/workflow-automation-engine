package rayyan.warehouse.workflowautomationengine.repository;

import rayyan.warehouse.workflowautomationengine.model.WorkflowExecutionStatus;
import rayyan.warehouse.workflowautomationengine.model.WorkflowExecutions;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkflowExecutionsRepository
        extends JpaRepository<WorkflowExecutions, UUID> {

    List<WorkflowExecutions> findAllByWorkflow_IdOrderByCreatedAtDesc(
            UUID workflowId
    );

    Optional<WorkflowExecutions> findByIdAndWorkflow_Id(
            UUID executionId,
            UUID workflowId
    );

    List<WorkflowExecutions> findAllByStatus(WorkflowExecutionStatus status);

    boolean existsByWorkflow_IdAndStatus(
            UUID workflowId,
            WorkflowExecutionStatus status
    );
}