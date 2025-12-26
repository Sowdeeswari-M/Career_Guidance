package com.campusconnect.facility.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "question_options")
public class QuestionOption {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long optionId;
    
    @NotBlank(message = "Option text is required")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String optionText;
    
    @Column(nullable = false)
    private Boolean isCorrectAnswer = false;
    
    @Column(nullable = false)
    private Integer optionOrder;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private AssessmentQuestion assessmentQuestion;
    
    // Constructors
    public QuestionOption() {}
    
    public QuestionOption(String optionText, Boolean isCorrectAnswer, Integer optionOrder) {
        this.optionText = optionText;
        this.isCorrectAnswer = isCorrectAnswer;
        this.optionOrder = optionOrder;
    }
    
    // Getters and Setters
    public Long getOptionId() { return optionId; }
    public void setOptionId(Long optionId) { this.optionId = optionId; }
    
    public String getOptionText() { return optionText; }
    public void setOptionText(String optionText) { this.optionText = optionText; }
    
    public Boolean getIsCorrectAnswer() { return isCorrectAnswer; }
    public void setIsCorrectAnswer(Boolean isCorrectAnswer) { this.isCorrectAnswer = isCorrectAnswer; }
    
    public Integer getOptionOrder() { return optionOrder; }
    public void setOptionOrder(Integer optionOrder) { this.optionOrder = optionOrder; }
    
    public AssessmentQuestion getAssessmentQuestion() { return assessmentQuestion; }
    public void setAssessmentQuestion(AssessmentQuestion assessmentQuestion) { this.assessmentQuestion = assessmentQuestion; }
}