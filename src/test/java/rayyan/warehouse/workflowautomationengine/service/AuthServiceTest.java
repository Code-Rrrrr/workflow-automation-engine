package rayyan.warehouse.workflowautomationengine.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import rayyan.warehouse.workflowautomationengine.dto.auth.RegistrationRequestDTO;
import rayyan.warehouse.workflowautomationengine.model.Users;
import rayyan.warehouse.workflowautomationengine.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

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

    // TEST 2: Test
    @Test
    void testDuplicateEmail(){

        // arrange
        RegistrationRequestDTO request = new RegistrationRequestDTO();
        request.setName("TestBot1");
        request.setEmail("test123");
        request.setPassword("test123");

        when(userRepository.existsByEmail("test123"))
                .thenReturn(true);

        // act
        assertThrows(RuntimeException.class,
                () -> authService.register(request));

        // assert : verify user was never saved
        verify(userRepository, never()).save(any(Users.class));
        verify(passwordEncoder, never()).encode("test123");
    }

    // TEST 3: Password must be encoded
    @Test
    void testShouldEncodePasswordBeforeSaving() {

        // Arrange
        RegistrationRequestDTO request = new RegistrationRequestDTO();
        request.setEmail("test2@gmail.com");
        request.setPassword("myPassword");

        when(userRepository.existsByEmail("test2@gmail.com"))
                .thenReturn(false);

        when(passwordEncoder.encode("myPassword"))
                .thenReturn("encodedPassword");

        // Act
        authService.register(request);

        // Assert
        verify(passwordEncoder).encode("myPassword");

        verify(userRepository).save(
                argThat(user ->
                        user.getPasswordHash().equals("encodedPassword")
                )
        );
    }

    // TEST 4: Repository should not save more than once
    @Test
    void testShouldSaveUserExactlyOnce() {

        // Arrange
        RegistrationRequestDTO request = new RegistrationRequestDTO();
        request.setEmail("test3@gmail.com");
        request.setPassword("password123");

        when(userRepository.existsByEmail("test3@gmail.com"))
                .thenReturn(false);

        when(passwordEncoder.encode("password123"))
                .thenReturn("encodedPassword");

        // Act
        authService.register(request);

        // Assert
        verify(userRepository, times(1))
                .save(any(Users.class));
    }
}
