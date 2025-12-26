package com.campusconnect.facility.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Entity
@Table(name = "progress_records")
public class ProgressRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long progressId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccount userAccount;
    
    @NotBlank(message = "Achievement type is required")
    @Column(nullable = false)
    private String achievementType;
    
    @NotBlank(message = "Achievement title is required")
    @Column(nullable = false)
    private String achievementTitle;
    
    @Column(columnDefinition = "TEXT")
    private String achievementDescription;
    
    @Column(nullable = false)
    private LocalDateTime achievedDate = LocalDateTime.now();
    
    @Column(nullable = false)
    private Integer pointsEarned = 0;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AchievementCategory achievementCategory;

    @Column(columnDefinition = "TEXT")
    private String skillsImproved;

    @Column(columnDefinition = "TEXT")
    private String certificateUrl;

    @Column(nullable = false)
    private Boolean isVerified = false;

    private LocalDateTime verificationDate;

    @Column(columnDefinition = "TEXT")
    private String verificationNotes;

    // Constructors
    public ProgressRecord() {}

    public ProgressRecord(UserAccount userAccount, String achievementType, String achievementTitle,
                         AchievementCategory achievementCategory, Integer pointsEarned) {
        this.userAccount = userAccount;
        this.achievementType = achievementType;
        this.achievementTitle = achievementTitle;
        this.achievementCategory = achievementCategory;
        this.pointsEarned = pointsEarned;
    }

    // Business methods
    public void verifyAchievement(String notes) {
        this.isVerified = true;
        this.verificationDate = LocalDateTime.now();
        this.verificationNotes = notes;
    }

    // Getters and Setters
    public Long getProgressId() { return progressId; }
    public void setProgressId(Long progressId) { this.progressId = progressId; }

    public UserAccount getUserAccount() { return userAccount; }
    public void setUserAccount(UserAccount userAccount) { this.userAccount = userAccount; }

    public String getAchievementType() { return achievementType; }
    public void setAchievementType(String achievementType) { this.achievementType = achievementType; }

    public String getAchievementTitle() { return achievementTitle; }
    public void setAchievementTitle(String achievementTitle) { this.achievementTitle = achievementTitle; }

    public String getAchievementDescription() { return achievementDescription; }
    public void setAchievementDescription(String achievementDescription) { this.achievementDescription = achievementDescription; }

    public LocalDateTime getAchievedDate() { return achievedDate; }
    public void setAchievedDate(LocalDateTime achievedDate) { this.achievedDate = achievedDate; }

    public Integer getPointsEarned() { return pointsEarned; }
    public void setPointsEarned(Integer pointsEarned) { this.pointsEarned = pointsEarned; }

    public AchievementCategory getAchievementCategory() { return achievementCategory; }
    public void setAchievementCategory(AchievementCategory achievementCategory) { this.achievementCategory = achievementCategory; }

    public String getSkillsImproved() { return skillsImproved; }
    public void setSkillsImproved(String skillsImproved) { this.skillsImproved = skillsImproved; }

    public String getCertificateUrl() { return certificateUrl; }
    public void setCertificateUrl(String certificateUrl) { this.certificateUrl = certificateUrl; }

    public Boolean getIsVerified() { return isVerified; }
    public void setIsVerified(Boolean isVerified) { this.isVerified = isVerified; }

    public LocalDateTime getVerificationDate() { return verificationDate; }
    public void setVerificationDate(LocalDateTime verificationDate) { this.verificationDate = verificationDate; }

    public String getVerificationNotes() { return verificationNotes; }
    public void setVerificationNotes(String verificationNotes) { this.verificationNotes = verificationNotes; }

}
