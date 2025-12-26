package com.campusconnect.facility.service;

import com.campusconnect.facility.model.UserAccount;
import com.campusconnect.facility.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LoginHandler {
    
    @Autowired
    private UserAccountRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public boolean validateCredentials(String usernameOrEmail, String rawPassword) {
        System.out.println("🔐 LoginHandler validating: " + usernameOrEmail);
        
        Optional<UserAccount> userOpt = userRepository.findByUsernameOrEmailAddress(usernameOrEmail, usernameOrEmail);
        
        if (userOpt.isPresent()) {
            UserAccount userAccount = userOpt.get();
            boolean passwordMatches = passwordEncoder.matches(rawPassword, userAccount.getPasswordHash());
            
            System.out.println("🔍 Password validation for " + userAccount.getEmailAddress() + 
                             " | Role: " + userAccount.getUserRole() + 
                             " | Matches: " + passwordMatches);
            
            return passwordMatches;
        }
        
        System.out.println("❌ User not found for validation: " + usernameOrEmail);
        return false;
    }
    
    public void updateLastLoginTime(String username) {
        Optional<UserAccount> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            UserAccount userAccount = userOpt.get();
            userAccount.setLastLoginTime(LocalDateTime.now());
            userRepository.save(userAccount);
            System.out.println("⏰ Updated last login for: " + userAccount.getEmailAddress());
        }
    }
}