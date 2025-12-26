package com.campusconnect.facility.service;

import com.campusconnect.facility.dto.UserRegistrationDto;
import com.campusconnect.facility.model.UserAccount;
import com.campusconnect.facility.repository.UserAccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAccountServiceTest {
    
    @Mock
    private UserAccountRepository userRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @InjectMocks
    private UserAccountService userService;
    
    @Test
    void testRegisterNewUser_Success() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("testuser");
        dto.setEmailAddress("test@example.com");
        dto.setPassword("password123");
        dto.setConfirmPassword("password123");
        dto.setFullName("Test User");
        dto.setUserRole("STUDENT");
        
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmailAddress("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(UserAccount.class))).thenReturn(new UserAccount());
        
        UserAccount result = userService.registerNewUser(dto);
        
        assertNotNull(result);
        verify(userRepository).save(any(UserAccount.class));
    }
    
    @Test
    void testRegisterNewUser_UsernameExists() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("existinguser");
        dto.setEmailAddress("test@example.com");
        
        when(userRepository.existsByUsername("existinguser")).thenReturn(true);
        
        assertThrows(RuntimeException.class, () -> userService.registerNewUser(dto));
    }
}