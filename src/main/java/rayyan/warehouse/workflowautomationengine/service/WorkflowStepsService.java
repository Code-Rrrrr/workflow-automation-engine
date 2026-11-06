package rayyan.warehouse.workflowautomationengine.service;

import rayyan.warehouse.workflowautomationengine.model.StepType;
import rayyan.warehouse.workflowautomationengine.model.Users;
import rayyan.warehouse.workflowautomationengine.model.Workflows;
import rayyan.warehouse.workflowautomationengine.model.WorkflowSteps;
import rayyan.warehouse.workflowautomationengine.repository.UserRepository;
import rayyan.warehouse.workflowautomationengine.repository.WorkflowRepository;
import rayyan.warehouse.workflowautomationengine.repository.WorkflowStepsRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class WorkflowStepsService {

    private final WorkflowStepsRepository stepRepository;
    private final WorkflowRepository workflowRepository;
    private final UserRepository userRepository;

    public WorkflowStepsService(
            WorkflowStepsRepository stepRepository,
            WorkflowRepository workflowRepository,
            UserRepository userRepository
    ) {
        this.stepRepository = stepRepository;
        this.workflowRepository = workflowRepository;
        this.userRepository = userRepository;
    }

    public WorkflowSteps createStep(
            UUID workflowId,
            String name,
            StepType stepType,
            Integer stepOrder,
            String configuration
    ) {
        UUID userId = getAuthenticatedUserId();

        Workflows workflow = findOwnedWorkflow(workflowId, userId);

        if (stepRepository.existsByWorkflow_IdAndStepOrder(workflowId, stepOrder)) {
            throw new RuntimeException("Step order already exists");
        }

        WorkflowSteps step = new WorkflowSteps();
        step.setWorkflow(workflow);
        step.setName(name);
        step.setStepType(stepType);
        step.setStepOrder(stepOrder);
        step.setConfiguration(configuration);

        return stepRepository.save(step);
    }

    @Transactional(readOnly = true)
    public List<WorkflowSteps> getAllSteps(UUID workflowId) {

        return stepRepository
                .findAllByWorkflow_IdOrderByStepOrderAsc(workflowId);
    }

    @Transactional(readOnly = true)
    public WorkflowSteps getStep(UUID workflowId, UUID stepId) {

        UUID userId = getAuthenticatedUserId();

        findOwnedWorkflow(workflowId, userId);

        return stepRepository.findByIdAndWorkflow_Id(stepId, workflowId)
                .orElseThrow(() -> new RuntimeException("Step not found"));
    }

    public WorkflowSteps updateStep(
            UUID workflowId,
            UUID stepId,
            String name,
            StepType stepType,
            Integer stepOrder,
            String configuration
    ) {
        UUID userId = getAuthenticatedUserId();

        findOwnedWorkflow(workflowId, userId);

        WorkflowSteps step = stepRepository
                .findByIdAndWorkflow_Id(stepId, workflowId)
                .orElseThrow(() -> new RuntimeException("Step not found"));

        boolean orderOccupied =
                stepRepository.existsByWorkflow_IdAndStepOrder(workflowId, stepOrder);

        if (orderOccupied && !step.getStepOrder().equals(stepOrder)) {
            throw new RuntimeException("Step order already exists");
        }

        step.setName(name);
        step.setStepType(stepType);
        step.setStepOrder(stepOrder);
        step.setConfiguration(configuration);

        return stepRepository.save(step);
    }

    public void deleteStep(UUID workflowId, UUID stepId) {

        UUID userId = getAuthenticatedUserId();

        findOwnedWorkflow(workflowId, userId);

        WorkflowSteps step = stepRepository
                .findByIdAndWorkflow_Id(stepId, workflowId)
                .orElseThrow(() -> new RuntimeException("Step not found"));

        stepRepository.delete(step);
    }

    private Workflows findOwnedWorkflow(UUID workflowId, UUID userId) {

        return workflowRepository.findByIdAndUser_Id(workflowId, userId)
                .orElseThrow(() -> new RuntimeException("Workflow not found"));
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