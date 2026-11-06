package rayyan.warehouse.workflowautomationengine.dto.workflowstep;

import rayyan.warehouse.workflowautomationengine.model.StepType;

import java.time.OffsetDateTime;
import java.util.UUID;

public record WorkflowStepResponseDTO(

        UUID id,
        UUID workflowId,
        String name,
        StepType stepType,
        Integer stepOrder,
        String configuration,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt

) {}