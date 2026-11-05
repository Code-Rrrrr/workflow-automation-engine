package rayyan.warehouse.workflowautomationengine.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import rayyan.warehouse.workflowautomationengine.dto.RegistrationRequestDTO;
import rayyan.warehouse.workflowautomationengine.model.Users;
import rayyan.warehouse.workflowautomationengine.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    AuthService authService;

    // the TESTS follows the AAA pattern

    // TEST 1: Test successful registration
    @Test
    void testRegisteringUser(){

        // arrange
        RegistrationRequestDTO request = new RegistrationRequestDTO();
        request.setName("TestBot1");
        request.setEmail("test123@gmail.com");
        request.setPassword("test123");

        when(userRepository.existsByEmail("test123@gmail.com"))
                  .thenReturn(false);

        when(passwordEncoder.encode("test123"))
                .thenReturn("hashedtest123");

        // act
        authService.register(request);

        // assert
        ArgumentCaptor<Users> captor = ArgumentCaptor
                .forClass(Users.class);

        verify(userRepository).save(captor.capture());

        Users savedUser = captor.getValue();

        assertEquals("TestBot1", savedUser.getName());
        assertEquals("test123@gmail.com", savedUser.getEmail());
        assertEquals("hashedtest123", savedUser.getPasswordHash());

        verify(passwordEncoder).encode("test123");
    }
}
