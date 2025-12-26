package com.campusconnect.facility.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class AssessmentSubmissionDto {
    
    @NotNull(message = "Assessment ID is required")
    private Long assessmentId;
    
    @NotNull(message = "Attempt ID is required")
    private Long attemptId;
    
    private List<AnswerSubmissionDto> submittedAnswers;
    
    // Constructors
    public AssessmentSubmissionDto() {}
    
    public AssessmentSubmissionDto(Long assessmentId, Long attemptId, List<AnswerSubmissionDto> submittedAnswers) {
        this.assessmentId = assessmentId;
        this.attemptId = attemptId;
        this.submittedAnswers = submittedAnswers;
    }
    
    // Getters and Setters
    public Long getAssessmentId() { return assessmentId; }
    public void setAssessmentId(Long assessmentId) { this.assessmentId = assessmentId; }
    
    public Long getAttemptId() { return attemptId; }
    public void setAttemptId(Long attemptId) { this.attemptId = attemptId; }
    
    public List<AnswerSubmissionDto> getSubmittedAnswers() { return submittedAnswers; }
    public void setSubmittedAnswers(List<AnswerSubmissionDto> submittedAnswers) { this.submittedAnswers = submittedAnswers; }
    
    // Inner class for answer submission
    public static class AnswerSubmissionDto {
        @NotNull(message = "Question ID is required")
        private Long questionId;
        
        private Long selectedOptionId;
        private String textAnswer;
        
        // Constructors
        public AnswerSubmissionDto() {}
        
        public AnswerSubmissionDto(Long questionId, Long selectedOptionId, String textAnswer) {
            this.questionId = questionId;
            this.selectedOptionId = selectedOptionId;
            this.textAnswer = textAnswer;
        }
        
        // Getters and Setters
        public Long getQuestionId() { return questionId; }
        public void setQuestionId(Long questionId) { this.questionId = questionId; }
        
        public Long getSelectedOptionId() { return selectedOptionId; }
        public void setSelectedOptionId(Long selectedOptionId) { this.selectedOptionId = selectedOptionId; }
        
        public String getTextAnswer() { return textAnswer; }
        public void setTextAnswer(String textAnswer) { this.textAnswer = textAnswer; }
    }
}