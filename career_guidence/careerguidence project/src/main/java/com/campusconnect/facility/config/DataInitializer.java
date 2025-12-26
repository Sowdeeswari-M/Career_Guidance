package com.campusconnect.facility.config;

import com.campusconnect.facility.model.UserAccount;
import com.campusconnect.facility.model.UserRole;
import com.campusconnect.facility.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UserAccountRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        System.out.println("🚀 DataInitializer starting...");
        
        try {
            // Check if mentor user already exists
            if (userRepository.findByEmailAddress("mentor@example.com").isPresent()) {
                System.out.println("📊 Users already exist, skipping initialization");
                return;
            }
        } catch (Exception e) {
            System.out.println("📊 Proceeding with user creation...");
        }
        
        // Create student
        UserAccount student = new UserAccount();
        student.setUsername("testuser");
        student.setEmailAddress("testuser@example.com");
        student.setPasswordHash(passwordEncoder.encode("password123"));
        student.setFullName("Test Student");
        student.setUserRole(UserRole.STUDENT);
        student.setIsAccountActive(true);
        student.setIsEmailVerified(true);
        userRepository.save(student);
        System.out.println("✅ Student created: testuser@example.com");

        // Create mentor
        UserAccount mentor = new UserAccount();
        mentor.setUsername("mentor");
        mentor.setEmailAddress("mentor@example.com");
        mentor.setPasswordHash(passwordEncoder.encode("password123"));
        mentor.setFullName("Test Mentor");
        mentor.setUserRole(UserRole.MENTOR);
        mentor.setIsAccountActive(true);
        mentor.setIsEmailVerified(true);
        userRepository.save(mentor);
        System.out.println("✅ Mentor created: mentor@example.com");
        
        System.out.println("📊 Total users created: " + userRepository.count());
        System.out.println("🎉 DataInitializer completed successfully!");
    }
}