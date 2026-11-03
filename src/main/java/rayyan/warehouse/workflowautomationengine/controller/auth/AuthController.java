package rayyan.warehouse.workflowautomationengine.controller.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import rayyan.warehouse.workflowautomationengine.dto.RegistrationRequestDTO;

@Controller
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping({"/register", "/signup"})
    public ResponseEntity<String> register(RegistrationRequestDTO user){
        // TODO: call the service layer

        return ResponseEntity.
                status(HttpStatus.CREATED).
                body("user registered successfully");
    }
}
