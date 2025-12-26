package com.campusconnect.facility.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "user_accounts")
public class UserAccount implements UserDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50)
    @Column(unique = true, nullable = false)
    private String username;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(unique = true, nullable = false)
    private String emailAddress;
    
    @NotBlank(message = "Password is required")
    @Size(min = 6)
    @Column(nullable = false)
    private String passwordHash;
    
    @NotBlank(message = "Full name is required")
    @Column(nullable = false)
    private String fullName;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole;
    
    @Column(nullable = false)
    private Boolean isAccountActive = true;
    
    @Column(nullable = false)
    private Boolean isEmailVerified = false;
    
    private String phoneNumber;
    
    @Column(columnDefinition = "TEXT")
    private String profileBio;
    
    private String profileImageUrl;
    
    @Column(nullable = false)
    private LocalDateTime registrationDate = LocalDateTime.now();
    
    private LocalDateTime lastLoginTime;
    
    // Student specific fields
    private String currentEducationLevel;
    private String fieldOfStudy;
    private String careerInterests;
    
    // Mentor specific fields
    private String professionalTitle;
    private String companyName;
    private Integer yearsOfExperience;
    private String expertiseAreas;
    
    @OneToMany(mappedBy = "studentUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AssessmentAttempt> assessmentAttempts;
    
    @OneToMany(mappedBy = "studentUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MentorshipRequest> mentorshipRequests;
    
    @OneToMany(mappedBy = "mentorUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MentorshipRequest> mentorshipAssignments;
    
    @OneToMany(mappedBy = "userAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProgressRecord> progressRecords;
    
    // Constructors
    public UserAccount() {}
    
    public UserAccount(String username, String emailAddress, String passwordHash, 
                      String fullName, UserRole userRole) {
        this.username = username;
        this.emailAddress = emailAddress;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.userRole = userRole;
    }
    
    // UserDetails implementation
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + userRole.name()));
    }
    
    @Override
    public String getPassword() {
        return passwordHash;
    }
    
    @Override
    public String getUsername() {
        return username;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return isAccountActive;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return isAccountActive;
    }
    
    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public void setUsername(String username) { this.username = username; }
    
    public String getEmailAddress() { return emailAddress; }
    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }
    
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public UserRole getUserRole() { return userRole; }
    public void setUserRole(UserRole userRole) { this.userRole = userRole; }
    
    public Boolean getIsAccountActive() { return isAccountActive; }
    public void setIsAccountActive(Boolean isAccountActive) { this.isAccountActive = isAccountActive; }
    
    public Boolean getIsEmailVerified() { return isEmailVerified; }
    public void setIsEmailVerified(Boolean isEmailVerified) { this.isEmailVerified = isEmailVerified; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getProfileBio() { return profileBio; }
    public void setProfileBio(String profileBio) { this.profileBio = profileBio; }
    
    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }
    
    public LocalDateTime getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDateTime registrationDate) { this.registrationDate = registrationDate; }
    
    public LocalDateTime getLastLoginTime() { return lastLoginTime; }
    public void setLastLoginTime(LocalDateTime lastLoginTime) { this.lastLoginTime = lastLoginTime; }
    
    public String getCurrentEducationLevel() { return currentEducationLevel; }
    public void setCurrentEducationLevel(String currentEducationLevel) { this.currentEducationLevel = currentEducationLevel; }
    
    public String getFieldOfStudy() { return fieldOfStudy; }
    public void setFieldOfStudy(String fieldOfStudy) { this.fieldOfStudy = fieldOfStudy; }
    
    public String getCareerInterests() { return careerInterests; }
    public void setCareerInterests(String careerInterests) { this.careerInterests = careerInterests; }
    
    public String getProfessionalTitle() { return professionalTitle; }
    public void setProfessionalTitle(String professionalTitle) { this.professionalTitle = professionalTitle; }
    
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    
    public Integer getYearsOfExperience() { return yearsOfExperience; }
    public void setYearsOfExperience(Integer yearsOfExperience) { this.yearsOfExperience = yearsOfExperience; }
    
    public String getExpertiseAreas() { return expertiseAreas; }
    public void setExpertiseAreas(String expertiseAreas) { this.expertiseAreas = expertiseAreas; }
    
    public List<AssessmentAttempt> getAssessmentAttempts() { return assessmentAttempts; }
    public void setAssessmentAttempts(List<AssessmentAttempt> assessmentAttempts) { this.assessmentAttempts = assessmentAttempts; }
    
    public List<MentorshipRequest> getMentorshipRequests() { return mentorshipRequests; }
    public void setMentorshipRequests(List<MentorshipRequest> mentorshipRequests) { this.mentorshipRequests = mentorshipRequests; }
    
    public List<MentorshipRequest> getMentorshipAssignments() { return mentorshipAssignments; }
    public void setMentorshipAssignments(List<MentorshipRequest> mentorshipAssignments) { this.mentorshipAssignments = mentorshipAssignments; }
    
    public List<ProgressRecord> getProgressRecords() { return progressRecords; }
    public void setProgressRecords(List<ProgressRecord> progressRecords) { this.progressRecords = progressRecords; }
}
