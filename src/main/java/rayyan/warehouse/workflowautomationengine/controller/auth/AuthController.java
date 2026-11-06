package rayyan.warehouse.workflowautomationengine.controller.auth;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import rayyan.warehouse.workflowautomationengine.dto.auth.LoginRequestDTO;
import rayyan.warehouse.workflowautomationengine.dto.auth.LoginResponseDTO;
import rayyan.warehouse.workflowautomationengine.dto.auth.RegistrationRequestDTO;
import rayyan.warehouse.workflowautomationengine.service.AuthService;

@Controller
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping({"/register", "/signup"})
    public ResponseEntity<String> register(RegistrationRequestDTO request){

        authService.register(request);

        return ResponseEntity.
                status(HttpStatus.CREATED).
                body("user registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO request){

        LoginResponseDTO response = authService.login(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
