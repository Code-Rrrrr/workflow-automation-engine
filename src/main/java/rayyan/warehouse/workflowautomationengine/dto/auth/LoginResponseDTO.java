package rayyan.warehouse.workflowautomationengine.dto.auth;

public record LoginResponseDTO(
        String accessToken,
        String tokenType
) {}
