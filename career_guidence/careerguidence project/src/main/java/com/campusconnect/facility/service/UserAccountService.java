package com.campusconnect.facility.service;

import com.campusconnect.facility.dto.UserRegistrationDto;
import com.campusconnect.facility.model.UserAccount;
import com.campusconnect.facility.model.UserRole;
import com.campusconnect.facility.repository.UserAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserAccountService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserAccountService.class);
    
    @Autowired
    private UserAccountRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public UserAccount registerNewUser(UserRegistrationDto registrationDto) {
        if (userRepository.existsByUsername(registrationDto.getUsername())) {
            throw new RuntimeException("Username already exists!");
        }
        
        if (userRepository.existsByEmailAddress(registrationDto.getEmailAddress())) {
            throw new RuntimeException("Email already exists!");
        }
        
        if (!registrationDto.isPasswordMatching()) {
            throw new RuntimeException("Passwords do not match!");
        }
        
        UserAccount newUser = new UserAccount();
        newUser.setUsername(registrationDto.getUsername());
        newUser.setEmailAddress(registrationDto.getEmailAddress());
        String encodedPwd = passwordEncoder.encode(registrationDto.getPassword());
        newUser.setPasswordHash(encodedPwd);
        newUser.setFullName(registrationDto.getFullName());
        newUser.setUserRole(com.campusconnect.facility.model.UserRole.valueOf(registrationDto.getUserRole().toUpperCase()));
        newUser.setPhoneNumber(registrationDto.getPhoneNumber());
        newUser.setProfileBio(registrationDto.getProfileBio());
        newUser.setIsEmailVerified(true); // Enable account for immediate login
        
        // Set role-specific fields
        if ("STUDENT".equalsIgnoreCase(registrationDto.getUserRole())) {
            newUser.setCurrentEducationLevel(registrationDto.getCurrentEducationLevel());
            newUser.setFieldOfStudy(registrationDto.getFieldOfStudy());
            newUser.setCareerInterests(registrationDto.getCareerInterests());
        } else if ("MENTOR".equalsIgnoreCase(registrationDto.getUserRole())) {
            newUser.setProfessionalTitle(registrationDto.getProfessionalTitle());
            newUser.setCompanyName(registrationDto.getCompanyName());
            newUser.setYearsOfExperience(registrationDto.getYearsOfExperience());
            newUser.setExpertiseAreas(registrationDto.getExpertiseAreas());
        }
        
        UserAccount savedUser = userRepository.save(newUser);
        logger.info("New user registered: {}", savedUser.getUsername());
        
        return savedUser;
    }
    
    public Optional<UserAccount> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    public Optional<UserAccount> findByEmail(String email) {
        return userRepository.findByEmailAddress(email);
    }
    
    public Optional<UserAccount> findById(Long userId) {
        return userRepository.findById(userId);
    }
    
    public UserAccount updateUserProfile(Long userId, UserRegistrationDto updateDto) {
        UserAccount existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        existingUser.setFullName(updateDto.getFullName());
        existingUser.setPhoneNumber(updateDto.getPhoneNumber());
        existingUser.setProfileBio(updateDto.getProfileBio());
        
        // Update role-specific fields
        if (existingUser.getUserRole().name().equals("STUDENT")) {
            existingUser.setCurrentEducationLevel(updateDto.getCurrentEducationLevel());
            existingUser.setFieldOfStudy(updateDto.getFieldOfStudy());
            existingUser.setCareerInterests(updateDto.getCareerInterests());
        } else if (existingUser.getUserRole().name().equals("MENTOR")) {
            existingUser.setProfessionalTitle(updateDto.getProfessionalTitle());
            existingUser.setCompanyName(updateDto.getCompanyName());
            existingUser.setYearsOfExperience(updateDto.getYearsOfExperience());
            existingUser.setExpertiseAreas(updateDto.getExpertiseAreas());
        }
        
        return userRepository.save(existingUser);
    }
    
    public void updateLastLoginTime(String username) {
        Optional<UserAccount> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            UserAccount user = userOpt.get();
            user.setLastLoginTime(LocalDateTime.now());
            userRepository.save(user);
        }
    }
    
    public Page<UserAccount> findAvailableMentors(Pageable pageable) {
        return userRepository.findAvailableMentors(pageable);
    }
    
    public List<UserAccount> findMentorsByExpertise(String skillArea) {
        return userRepository.findMentorsByExpertiseArea(skillArea);
    }
    
    public Page<UserAccount> searchUsers(String searchTerm, Pageable pageable) {
        return userRepository.searchUsers(searchTerm, pageable);
    }
    
    public Long countActiveUsersByRole(String role) {
        return userRepository.countActiveUsersByRole(role);
    }
    
    public void deactivateUser(Long userId) {
        UserAccount user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setIsAccountActive(false);
        userRepository.save(user);
        logger.info("User deactivated: {}", user.getUsername());
    }
    
    public void activateUser(Long userId) {
        UserAccount user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setIsAccountActive(true);
        userRepository.save(user);
        logger.info("User activated: {}", user.getUsername());
    }
    
    public void verifyEmail(Long userId) {
        UserAccount user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setIsEmailVerified(true);
        userRepository.save(user);
        logger.info("Email verified for user: {}", user.getUsername());
    }
}