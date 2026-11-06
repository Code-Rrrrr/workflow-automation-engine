package rayyan.warehouse.workflowautomationengine.service;

import rayyan.warehouse.workflowautomationengine.dto.workflow.CreateWorkflowRequestDTO;
import rayyan.warehouse.workflowautomationengine.dto.workflow.WorkflowResponseDTO;
import rayyan.warehouse.workflowautomationengine.dto.workflow.UpdateWorkflowRequestDTO;
import rayyan.warehouse.workflowautomationengine.model.Users;
import rayyan.warehouse.workflowautomationengine.model.Workflows;
import rayyan.warehouse.workflowautomationengine.repository.UserRepository;
import rayyan.warehouse.workflowautomationengine.repository.WorkflowRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class WorkflowService {

    private final WorkflowRepository workflowRepository;
    private final UserRepository userRepository;

    public WorkflowService(
            WorkflowRepository workflowRepository,
            UserRepository userRepository
    ) {
        this.workflowRepository = workflowRepository;
        this.userRepository = userRepository;
    }

    public WorkflowResponseDTO createWorkflow(CreateWorkflowRequestDTO request) {

        UUID userId = getAuthenticatedUserId();

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Workflows workflow = new Workflows();
        workflow.setUser(user);
        workflow.setName(request.name());
        workflow.setDescription(request.description());
        workflow.setEnabled(false);

        Workflows savedWorkflow = workflowRepository.save(workflow);

        return mapToResponse(savedWorkflow);
    }

    @Transactional(readOnly = true)
    public WorkflowResponseDTO getWorkflow(UUID workflowId) {

        UUID userId = getAuthenticatedUserId();

        Workflows workflow = findOwnedWorkflow(workflowId, userId);

        return mapToResponse(workflow);
    }

    @Transactional(readOnly = true)
    public List<WorkflowResponseDTO> getAllWorkflows() {

        UUID userId = getAuthenticatedUserId();

        return workflowRepository.findAllByUser_Id(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public WorkflowResponseDTO updateWorkflow(
            UUID workflowId,
            UpdateWorkflowRequestDTO request
    ) {

        UUID userId = getAuthenticatedUserId();

        Workflows workflow = findOwnedWorkflow(workflowId, userId);

        workflow.setName(request.name());
        workflow.setDescription(request.description());

        Workflows updatedWorkflow = workflowRepository.save(workflow);

        return mapToResponse(updatedWorkflow);
    }

    public void deleteWorkflow(UUID workflowId) {

        UUID userId = getAuthenticatedUserId();

        Workflows workflow = findOwnedWorkflow(workflowId, userId);

        workflowRepository.delete(workflow);
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

    private WorkflowResponseDTO mapToResponse(Workflows workflow) {

        return new WorkflowResponseDTO(
                workflow.getId(),
                workflow.getName(),
                workflow.getDescription(),
                workflow.isEnabled(),
                workflow.getCreatedAt(),
                workflow.getUpdatedAt()
        );
    }
}
