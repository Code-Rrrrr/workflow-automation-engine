package rayyan.warehouse.workflowautomationengine.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequestDTO(
        @Email(message = "Enter your registered email please")
        @Size(max = 255, message = "too long for an email")
        String email,

        @NotBlank(message = "Enter your password")
        @Size(min = 8, max = 72, message = "password was 8 to 72 characters long")
        String password
) {
}
