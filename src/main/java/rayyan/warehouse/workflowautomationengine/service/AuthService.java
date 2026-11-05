package rayyan.warehouse.workflowautomationengine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rayyan.warehouse.workflowautomationengine.dto.LoginRequestDTO;
import rayyan.warehouse.workflowautomationengine.dto.LoginResponseDTO;
import rayyan.warehouse.workflowautomationengine.dto.RegistrationRequestDTO;
import rayyan.warehouse.workflowautomationengine.exception.ConflictException;
import rayyan.warehouse.workflowautomationengine.exception.UnauthorizedException;
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
            throw new ConflictException("Email is already registered");
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
                        new UnauthorizedException("Email is not registered")
                );

        if (!passwordEncoder.matches(
                request.password(),
                user.getPasswordHash())) {

            throw new UnauthorizedException("Password doesn't match with what you gave earlier");
        }

        String token = jwtService.generateToken(user.getId());

        return new LoginResponseDTO(token, "Bearer");
    }

}
