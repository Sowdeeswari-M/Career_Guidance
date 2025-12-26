package com.campusconnect.facility.service;

import com.campusconnect.facility.model.UserAccount;
import com.campusconnect.facility.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UserAccountRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        System.out.println("Loading user: " + usernameOrEmail);
        
        UserAccount user = userRepository.findByUsernameOrEmailAddress(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> {
                    System.out.println("User not found: " + usernameOrEmail);
                    return new UsernameNotFoundException("User not found: " + usernameOrEmail);
                });
        
        System.out.println("User found: " + user.getEmailAddress() + " | Role: " + user.getUserRole());
        return user;
    }
}