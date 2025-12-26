package com.campusconnect.facility.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "skill_assessments")
public class SkillAssessment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assessmentId;
    
    @NotBlank(message = "Assessment title is required")
    @Column(nullable = false)
    private String assessmentTitle;
    
    @Column(columnDefinition = "TEXT")
    private String assessmentDescription;
    
    @NotBlank(message = "Skill category is required")
    @Column(nullable = false)
    private String skillCategory;
    
    @NotNull(message = "Duration is required")
    @Positive(message = "Duration must be positive")
    @Column(nullable = false)
    private Integer durationMinutes;
    
    @NotNull(message = "Passing score is required")
    @Column(nullable = false)
    private Double passingScore;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DifficultyLevel difficultyLevel;
    
    @Column(nullable = false)
    private Boolean isActive = true;
    
    @Column(nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();
    
    private LocalDateTime lastModifiedDate;
    
    @OneToMany(mappedBy = "skillAssessment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AssessmentQuestion> assessmentQuestions;
    
    @OneToMany(mappedBy = "skillAssessment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AssessmentAttempt> assessmentAttempts;
    
    // Constructors
    public SkillAssessment() {}
    
    public SkillAssessment(String assessmentTitle, String skillCategory, 
                          Integer durationMinutes, Double passingScore, DifficultyLevel difficultyLevel) {
        this.assessmentTitle = assessmentTitle;
        this.skillCategory = skillCategory;
        this.durationMinutes = durationMinutes;
        this.passingScore = passingScore;
        this.difficultyLevel = difficultyLevel;
    }
    
    // Getters and Setters
    public Long getAssessmentId() { return assessmentId; }
    public void setAssessmentId(Long assessmentId) { this.assessmentId = assessmentId; }
    
    public String getAssessmentTitle() { return assessmentTitle; }
    public void setAssessmentTitle(String assessmentTitle) { this.assessmentTitle = assessmentTitle; }
    
    public String getAssessmentDescription() { return assessmentDescription; }
    public void setAssessmentDescription(String assessmentDescription) { this.assessmentDescription = assessmentDescription; }
    
    public String getSkillCategory() { return skillCategory; }
    public void setSkillCategory(String skillCategory) { this.skillCategory = skillCategory; }
    
    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }
    
    public Double getPassingScore() { return passingScore; }
    public void setPassingScore(Double passingScore) { this.passingScore = passingScore; }
    
    public DifficultyLevel getDifficultyLevel() { return difficultyLevel; }
    public void setDifficultyLevel(DifficultyLevel difficultyLevel) { this.difficultyLevel = difficultyLevel; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
    
    public LocalDateTime getLastModifiedDate() { return lastModifiedDate; }
    public void setLastModifiedDate(LocalDateTime lastModifiedDate) { this.lastModifiedDate = lastModifiedDate; }
    
    public List<AssessmentQuestion> getAssessmentQuestions() { return assessmentQuestions; }
    public void setAssessmentQuestions(List<AssessmentQuestion> assessmentQuestions) { this.assessmentQuestions = assessmentQuestions; }
    
    public List<AssessmentAttempt> getAssessmentAttempts() { return assessmentAttempts; }
    public void setAssessmentAttempts(List<AssessmentAttempt> assessmentAttempts) { this.assessmentAttempts = assessmentAttempts; }
}