package com.campusconnect.facility.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "career_recommendations")
public class CareerRecommendation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recommendationId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccount studentUser;
    
    @NotBlank(message = "Career path is required")
    @Column(nullable = false)
    private String careerPath;
    
    @Column(columnDefinition = "TEXT")
    private String careerDescription;
    
    @NotNull(message = "Match percentage is required")
    @Column(nullable = false)
    private Double matchPercentage;
    
    @Column(columnDefinition = "TEXT")
    private String requiredSkills;
    
    @Column(columnDefinition = "TEXT")
    private String recommendedCourses;
    
    @Column(columnDefinition = "TEXT")
    private String industryOutlook;
    
    @Column(columnDefinition = "TEXT")
    private String salaryRange;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecommendationStatus recommendationStatus = RecommendationStatus.ACTIVE;
    
    @Column(nullable = false)
    private LocalDateTime generatedDate = LocalDateTime.now();
    
    private LocalDateTime lastUpdatedDate;
    
    @Column(columnDefinition = "TEXT")
    private String recommendationReason;
    
    // Constructors
    public CareerRecommendation() {}
    
    public CareerRecommendation(UserAccount studentUser, String careerPath, Double matchPercentage) {
        this.studentUser = studentUser;
        this.careerPath = careerPath;
        this.matchPercentage = matchPercentage;
    }
    
    // Getters and Setters
    public Long getRecommendationId() { return recommendationId; }
    public void setRecommendationId(Long recommendationId) { this.recommendationId = recommendationId; }
    
    public UserAccount getStudentUser() { return studentUser; }
    public void setStudentUser(UserAccount studentUser) { this.studentUser = studentUser; }
    
    public String getCareerPath() { return careerPath; }
    public void setCareerPath(String careerPath) { this.careerPath = careerPath; }
    
    public String getCareerDescription() { return careerDescription; }
    public void setCareerDescription(String careerDescription) { this.careerDescription = careerDescription; }
    
    public Double getMatchPercentage() { return matchPercentage; }
    public void setMatchPercentage(Double matchPercentage) { this.matchPercentage = matchPercentage; }
    
    public String getRequiredSkills() { return requiredSkills; }
    public void setRequiredSkills(String requiredSkills) { this.requiredSkills = requiredSkills; }
    
    public String getRecommendedCourses() { return recommendedCourses; }
    public void setRecommendedCourses(String recommendedCourses) { this.recommendedCourses = recommendedCourses; }
    
    public String getIndustryOutlook() { return industryOutlook; }
    public void setIndustryOutlook(String industryOutlook) { this.industryOutlook = industryOutlook; }
    
    public String getSalaryRange() { return salaryRange; }
    public void setSalaryRange(String salaryRange) { this.salaryRange = salaryRange; }
    
    public RecommendationStatus getRecommendationStatus() { return recommendationStatus; }
    public void setRecommendationStatus(RecommendationStatus recommendationStatus) { this.recommendationStatus = recommendationStatus; }
    
    public LocalDateTime getGeneratedDate() { return generatedDate; }
    public void setGeneratedDate(LocalDateTime generatedDate) { this.generatedDate = generatedDate; }
    
    public LocalDateTime getLastUpdatedDate() { return lastUpdatedDate; }
    public void setLastUpdatedDate(LocalDateTime lastUpdatedDate) { this.lastUpdatedDate = lastUpdatedDate; }
    
    public String getRecommendationReason() { return recommendationReason; }
    public void setRecommendationReason(String recommendationReason) { this.recommendationReason = recommendationReason; }
}
