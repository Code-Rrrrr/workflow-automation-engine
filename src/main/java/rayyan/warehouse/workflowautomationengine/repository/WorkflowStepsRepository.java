package rayyan.warehouse.workflowautomationengine.repository;

import rayyan.warehouse.workflowautomationengine.model.WorkflowSteps;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkflowStepsRepository extends JpaRepository<WorkflowSteps, UUID> {

    List<WorkflowSteps> findAllByWorkflow_IdOrderByStepOrderAsc(UUID workflowId);

    Optional<WorkflowSteps> findByIdAndWorkflow_Id(
            UUID stepId,
            UUID workflowId
    );

    boolean existsByWorkflow_IdAndStepOrder(
            UUID workflowId,
            Integer stepOrder
    );

    void deleteAllByWorkflow_Id(UUID workflowId);
}