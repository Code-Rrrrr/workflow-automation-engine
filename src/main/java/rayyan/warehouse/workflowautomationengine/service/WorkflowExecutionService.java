package rayyan.warehouse.workflowautomationengine.service;

import rayyan.warehouse.workflowautomationengine.model.WorkflowExecutionStatus;
import rayyan.warehouse.workflowautomationengine.model.WorkflowExecutionStepStatus;
import rayyan.warehouse.workflowautomationengine.model.WorkflowExecutions;
import rayyan.warehouse.workflowautomationengine.model.WorkflowExecutionSteps;
import rayyan.warehouse.workflowautomationengine.model.WorkflowSteps;
import rayyan.warehouse.workflowautomationengine.model.Workflows;

import rayyan.warehouse.workflowautomationengine.repository.WorkflowRepository;
import rayyan.warehouse.workflowautomationengine.repository.WorkflowStepsRepository;
import rayyan.warehouse.workflowautomationengine.repository.WorkflowExecutionsRepository;
import rayyan.warehouse.workflowautomationengine.repository.WorkflowExecutionStepsRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class WorkflowExecutionService {

    private final WorkflowRepository workflowRepository;
    private final WorkflowStepsRepository workflowStepsRepository;
    private final WorkflowExecutionsRepository executionsRepository;
    private final WorkflowExecutionStepsRepository executionStepsRepository;

    public WorkflowExecutionService(
            WorkflowRepository workflowRepository,
            WorkflowStepsRepository workflowStepsRepository,
            WorkflowExecutionsRepository executionsRepository,
            WorkflowExecutionStepsRepository executionStepsRepository
    ) {
        this.workflowRepository = workflowRepository;
        this.workflowStepsRepository = workflowStepsRepository;
        this.executionsRepository = executionsRepository;
        this.executionStepsRepository = executionStepsRepository;
    }

    public WorkflowExecutions createExecution(UUID workflowId) {

        UUID userId = getAuthenticatedUserId();

        // Verify that the workflow belongs to the authenticated user.
        Workflows workflow = workflowRepository
                .findByIdAndUser_Id(workflowId, userId)
                .orElseThrow(() -> new RuntimeException("Workflow not found"));

        // Retrieve workflow steps in execution order.
        List<WorkflowSteps> workflowSteps =
                workflowStepsRepository
                        .findAllByWorkflow_IdOrderByStepOrderAsc(workflowId);

        if (workflowSteps.isEmpty()) {
            throw new RuntimeException("Cannot execute a workflow with no steps");
        }

        // Create the parent execution.
        WorkflowExecutions execution = new WorkflowExecutions();
        execution.setWorkflow(workflow);
        execution.setStatus(WorkflowExecutionStatus.PENDING);

        WorkflowExecutions savedExecution =
                executionsRepository.save(execution);

        // Initialize an execution record for every workflow step.
        List<WorkflowExecutionSteps> executionSteps =
                workflowSteps.stream().map(step -> {

                    WorkflowExecutionSteps executionStep =
                            new WorkflowExecutionSteps();

                    executionStep.setExecution(savedExecution);
                    executionStep.setWorkflowStep(step);
                    executionStep.setStatus(
                            WorkflowExecutionStepStatus.PENDING
                    );

                    return executionStep;

                }).toList();

        executionStepsRepository.saveAll(executionSteps);

        return savedExecution;
    }

    private UUID getAuthenticatedUserId() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !(authentication.getPrincipal() instanceof UUID userId)) {
            throw new RuntimeException("Unauthenticated user");
        }

        return userId;
    }
}