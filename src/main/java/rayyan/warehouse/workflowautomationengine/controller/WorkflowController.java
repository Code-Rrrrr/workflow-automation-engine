package rayyan.warehouse.workflowautomationengine.controller;


import rayyan.warehouse.workflowautomationengine.dto.workflow.CreateWorkflowRequestDTO;
import rayyan.warehouse.workflowautomationengine.dto.workflow.UpdateWorkflowRequestDTO;
import rayyan.warehouse.workflowautomationengine.dto.workflow.WorkflowResponseDTO;
import rayyan.warehouse.workflowautomationengine.service.WorkflowService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/workflows")
public class WorkflowController {

    private final WorkflowService workflowService;

    public WorkflowController(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @PostMapping
    public ResponseEntity<WorkflowResponseDTO> createWorkflow(
            @Valid @RequestBody CreateWorkflowRequestDTO request
    ) {
        WorkflowResponseDTO response = workflowService.createWorkflow(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<WorkflowResponseDTO>> getAllWorkflows() {

        return ResponseEntity.ok(workflowService.getAllWorkflows());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkflowResponseDTO> getWorkflow(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(workflowService.getWorkflow(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkflowResponseDTO> updateWorkflow(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateWorkflowRequestDTO request
    ) {
        return ResponseEntity.ok(
                workflowService.updateWorkflow(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkflow(
            @PathVariable UUID id
    ) {
        workflowService.deleteWorkflow(id);

        return ResponseEntity.noContent().build();
    }
}