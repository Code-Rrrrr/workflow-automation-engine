package rayyan.warehouse.workflowautomationengine.dto.workflow;

import java.time.OffsetDateTime;
import java.util.UUID;

public record WorkflowResponseDTO(
        UUID id,
        String name,
        String description,
        boolean enabled,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
