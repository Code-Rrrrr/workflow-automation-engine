package rayyan.warehouse.workflowautomationengine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rayyan.warehouse.workflowautomationengine.dto.RegistrationRequestDTO;
import rayyan.warehouse.workflowautomationengine.model.Users;
import rayyan.warehouse.workflowautomationengine.repository.UserRepository;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

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
}
