package rayyan.warehouse.workflowautomationengine.dto.workflowstep;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import rayyan.warehouse.workflowautomationengine.model.StepType;

public record UpdateWorkflowStepRequestDTO(

        @NotBlank(message = "Step name is required")
        @Size(max = 100, message = "Step name cannot exceed 100 characters")
        String name,

        @NotNull(message = "Step type is required")
        StepType stepType,

        @NotNull(message = "Step order is required")
        @Positive(message = "Step order must be greater than zero")
        Integer stepOrder,

        @NotBlank(message = "Configuration is required")
        String configuration

) {}