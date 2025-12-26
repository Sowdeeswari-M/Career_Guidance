package com.campusconnect.facility.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "assessment_attempts")
public class AssessmentAttempt {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attemptId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccount studentUser;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_id", nullable = false)
    private SkillAssessment skillAssessment;
    
    @Column(nullable = false)
    private LocalDateTime startTime = LocalDateTime.now();
    
    private LocalDateTime completionTime;
    
    @NotNull(message = "Score is required")
    @Column(nullable = false)
    private Double achievedScore = 0.0;
    
    @NotNull(message = "Total possible score is required")
    @Column(nullable = false)
    private Double totalPossibleScore;
    
    @Column(nullable = false)
    private Double percentageScore = 0.0;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttemptStatus attemptStatus = AttemptStatus.IN_PROGRESS;
    
    @Column(nullable = false)
    private Boolean isPassed = false;
    
    private Integer timeSpentMinutes;
    
    @Column(columnDefinition = "TEXT")
    private String feedbackComments;
    
    @OneToMany(mappedBy = "assessmentAttempt", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StudentAnswer> studentAnswers;
    
    // Constructors
    public AssessmentAttempt() {}
    
    public AssessmentAttempt(UserAccount studentUser, SkillAssessment skillAssessment, Double totalPossibleScore) {
        this.studentUser = studentUser;
        this.skillAssessment = skillAssessment;
        this.totalPossibleScore = totalPossibleScore;
    }
    
    // Business methods
    public void completeAttempt() {
        this.completionTime = LocalDateTime.now();
        this.attemptStatus = AttemptStatus.COMPLETED;
        this.timeSpentMinutes = (int) java.time.Duration.between(startTime, completionTime).toMinutes();
        this.percentageScore = (achievedScore / totalPossibleScore) * 100;
        this.isPassed = percentageScore >= skillAssessment.getPassingScore();
    }
    
    // Getters and Setters
    public Long getAttemptId() { return attemptId; }
    public void setAttemptId(Long attemptId) { this.attemptId = attemptId; }
    
    public UserAccount getStudentUser() { return studentUser; }
    public void setStudentUser(UserAccount studentUser) { this.studentUser = studentUser; }
    
    public SkillAssessment getSkillAssessment() { return skillAssessment; }
    public void setSkillAssessment(SkillAssessment skillAssessment) { this.skillAssessment = skillAssessment; }
    
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    
    public LocalDateTime getCompletionTime() { return completionTime; }
    public void setCompletionTime(LocalDateTime completionTime) { this.completionTime = completionTime; }
    
    public Double getAchievedScore() { return achievedScore; }
    public void setAchievedScore(Double achievedScore) { this.achievedScore = achievedScore; }
    
    public Double getTotalPossibleScore() { return totalPossibleScore; }
    public void setTotalPossibleScore(Double totalPossibleScore) { this.totalPossibleScore = totalPossibleScore; }
    
    public Double getPercentageScore() { return percentageScore; }
    public void setPercentageScore(Double percentageScore) { this.percentageScore = percentageScore; }
    
    public AttemptStatus getAttemptStatus() { return attemptStatus; }
    public void setAttemptStatus(AttemptStatus attemptStatus) { this.attemptStatus = attemptStatus; }
    
    public Boolean getIsPassed() { return isPassed; }
    public void setIsPassed(Boolean isPassed) { this.isPassed = isPassed; }
    
    public Integer getTimeSpentMinutes() { return timeSpentMinutes; }
    public void setTimeSpentMinutes(Integer timeSpentMinutes) { this.timeSpentMinutes = timeSpentMinutes; }
    
    public String getFeedbackComments() { return feedbackComments; }
    public void setFeedbackComments(String feedbackComments) { this.feedbackComments = feedbackComments; }
    
    public List<StudentAnswer> getStudentAnswers() { return studentAnswers; }
    public void setStudentAnswers(List<StudentAnswer> studentAnswers) { this.studentAnswers = studentAnswers; }
}
