package rayyan.warehouse.workflowautomationengine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rayyan.warehouse.workflowautomationengine.dto.LoginRequestDTO;
import rayyan.warehouse.workflowautomationengine.dto.LoginResponseDTO;
import rayyan.warehouse.workflowautomationengine.dto.RegistrationRequestDTO;
import rayyan.warehouse.workflowautomationengine.model.Users;
import rayyan.warehouse.workflowautomationengine.repository.UserRepository;
import rayyan.warehouse.workflowautomationengine.security.JWTService;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTService jwtService;

    public void register(RegistrationRequestDTO requests){
        if(userRepository.existsByEmail(requests.getEmail())){
            throw new RuntimeException("Email is already registered");
        }

        Users user = new Users();
        user.setName(requests.getName());
        user.setEmail(requests.getEmail());
        user.setPasswordHash(passwordEncoder.encode(requests.getPassword()));

        userRepository.save(user);
    }

    public LoginResponseDTO login(LoginRequestDTO request) {

        Users user = userRepository.getByEmail(request.email())
                .orElseThrow(() ->
                        new RuntimeException("Invalid email or password")
                );

        if (!passwordEncoder.matches(
                request.password(),
                user.getPasswordHash())) {

            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getId());

        return new LoginResponseDTO(token, "Bearer");
    }

}
