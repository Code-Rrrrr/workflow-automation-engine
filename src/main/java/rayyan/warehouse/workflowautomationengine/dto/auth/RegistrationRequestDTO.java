package rayyan.warehouse.workflowautomationengine.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegistrationRequestDTO {
    @NotBlank(message = "name cannot be empty")
    @Size(max = 100, message = "too long for a name")
    private String name;

    @NotBlank(message = "please enter an email")
    @Email(message = "enter a valid email")
    @Size(max = 255, message = "too long for an email")
    private String email;

    @NotBlank(message = "You wanna be protected, right?!")
    @Size(min = 8, max = 72, message = "password needs to be 8 to 72 characters long")
    private String password;

    // Getters and Setters


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
