package rayyan.warehouse.workflowautomationengine.dto.auth;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponseDTO(
        LocalDateTime timestamp,
        int status,
        String message,
        String error,
        String path,
        Map<String, String> validationErrors
) {
}
