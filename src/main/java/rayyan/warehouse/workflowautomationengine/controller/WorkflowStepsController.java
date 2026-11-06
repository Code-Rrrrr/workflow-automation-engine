package rayyan.warehouse.workflowautomationengine.controller;

import jakarta.validation.Valid;

import rayyan.warehouse.workflowautomationengine.dto.workflowstep.CreateWorkflowStepRequestDTO;
import rayyan.warehouse.workflowautomationengine.dto.workflowstep.UpdateWorkflowStepRequestDTO;
import rayyan.warehouse.workflowautomationengine.dto.workflowstep.WorkflowStepResponseDTO;
import rayyan.warehouse.workflowautomationengine.model.WorkflowSteps;
import rayyan.warehouse.workflowautomationengine.service.WorkflowStepsService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/workflows/{workflowId}/steps")
public class WorkflowStepsController {

    private final WorkflowStepsService workflowStepsService;

    public WorkflowStepsController(WorkflowStepsService workflowStepsService) {
        this.workflowStepsService = workflowStepsService;
    }

    @PostMapping
    public ResponseEntity<WorkflowStepResponseDTO> createStep(
            @PathVariable UUID workflowId,
            @Valid @RequestBody CreateWorkflowStepRequestDTO request
    ) {
        WorkflowSteps step = workflowStepsService.createStep(
                workflowId,
                request.name(),
                request.stepType(),
                request.stepOrder(),
                request.configuration()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapToResponse(step));
    }

    @GetMapping
    public ResponseEntity<List<WorkflowStepResponseDTO>> getAllSteps(
            @PathVariable UUID workflowId
    ) {
        List<WorkflowStepResponseDTO> steps = workflowStepsService
                .getAllSteps(workflowId)
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ResponseEntity.ok(steps);
    }

    @GetMapping("/{stepId}")
    public ResponseEntity<WorkflowStepResponseDTO> getStep(
            @PathVariable UUID workflowId,
            @PathVariable UUID stepId
    ) {
        WorkflowSteps step = workflowStepsService.getStep(workflowId, stepId);

        return ResponseEntity.ok(mapToResponse(step));
    }

    @PutMapping("/{stepId}")
    public ResponseEntity<WorkflowStepResponseDTO> updateStep(
            @PathVariable UUID workflowId,
            @PathVariable UUID stepId,
            @Valid @RequestBody UpdateWorkflowStepRequestDTO request
    ) {
        WorkflowSteps step = workflowStepsService.updateStep(
                workflowId,
                stepId,
                request.name(),
                request.stepType(),
                request.stepOrder(),
                request.configuration()
        );

        return ResponseEntity.ok(mapToResponse(step));
    }

    @DeleteMapping("/{stepId}")
    public ResponseEntity<Void> deleteStep(
            @PathVariable UUID workflowId,
            @PathVariable UUID stepId
    ) {
        workflowStepsService.deleteStep(workflowId, stepId);

        return ResponseEntity.noContent().build();
    }

    private WorkflowStepResponseDTO mapToResponse(WorkflowSteps step) {
        return new WorkflowStepResponseDTO(
                step.getId(),
                step.getWorkflow().getId(),
                step.getName(),
                step.getStepType(),
                step.getStepOrder(),
                step.getConfiguration(),
                step.getCreatedAt(),
                step.getUpdatedAt()
        );
    }
}