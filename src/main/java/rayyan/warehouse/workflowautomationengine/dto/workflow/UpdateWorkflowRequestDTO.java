package rayyan.warehouse.workflowautomationengine.dto.workflow;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateWorkflowRequestDTO(
        @NotBlank(message = "workflow is name is required")
        @Size(max = 100, message = "name cannot exceed 100 characters")
        String name,
        String description
) {
}
