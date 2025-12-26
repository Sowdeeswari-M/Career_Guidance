package com.campusconnect.facility.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "student_answers")
public class StudentAnswer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long answerId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attempt_id", nullable = false)
    private AssessmentAttempt assessmentAttempt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private AssessmentQuestion assessmentQuestion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_option_id")
    private QuestionOption selectedOption;
    
    @Column(columnDefinition = "TEXT")
    private String textAnswer;
    
    @Column(nullable = false)
    private Boolean isCorrect = false;
    
    @Column(nullable = false)
    private Double pointsEarned = 0.0;
    
    @Column(nullable = false)
    private LocalDateTime answeredAt = LocalDateTime.now();
    
    // Constructors
    public StudentAnswer() {}
    
    public StudentAnswer(AssessmentAttempt assessmentAttempt, AssessmentQuestion assessmentQuestion) {
        this.assessmentAttempt = assessmentAttempt;
        this.assessmentQuestion = assessmentQuestion;
    }
    
    // Business methods
    public void evaluateAnswer() {
        if (selectedOption != null) {
            this.isCorrect = selectedOption.getIsCorrectAnswer();
            this.pointsEarned = isCorrect ? assessmentQuestion.getPointsValue().doubleValue() : 0.0;
        }
    }
    
    // Getters and Setters
    public Long getAnswerId() { return answerId; }
    public void setAnswerId(Long answerId) { this.answerId = answerId; }
    
    public AssessmentAttempt getAssessmentAttempt() { return assessmentAttempt; }
    public void setAssessmentAttempt(AssessmentAttempt assessmentAttempt) { this.assessmentAttempt = assessmentAttempt; }
    
    public AssessmentQuestion getAssessmentQuestion() { return assessmentQuestion; }
    public void setAssessmentQuestion(AssessmentQuestion assessmentQuestion) { this.assessmentQuestion = assessmentQuestion; }
    
    public QuestionOption getSelectedOption() { return selectedOption; }
    public void setSelectedOption(QuestionOption selectedOption) { this.selectedOption = selectedOption; }
    
    public String getTextAnswer() { return textAnswer; }
    public void setTextAnswer(String textAnswer) { this.textAnswer = textAnswer; }
    
    public Boolean getIsCorrect() { return isCorrect; }
    public void setIsCorrect(Boolean isCorrect) { this.isCorrect = isCorrect; }
    
    public Double getPointsEarned() { return pointsEarned; }
    public void setPointsEarned(Double pointsEarned) { this.pointsEarned = pointsEarned; }
    
    public LocalDateTime getAnsweredAt() { return answeredAt; }
    public void setAnsweredAt(LocalDateTime answeredAt) { this.answeredAt = answeredAt; }
}