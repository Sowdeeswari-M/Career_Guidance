package com.campusconnect.facility.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

@Entity
@Table(name = "assessment_questions")
public class AssessmentQuestion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;
    
    @NotBlank(message = "Question text is required")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String questionText;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionType questionType;
    
    @NotNull(message = "Points value is required")
    @Positive(message = "Points must be positive")
    @Column(nullable = false)
    private Integer pointsValue;
    
    @Column(nullable = false)
    private Integer questionOrder;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_id", nullable = false)
    private SkillAssessment skillAssessment;
    
    @OneToMany(mappedBy = "assessmentQuestion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<QuestionOption> questionOptions;
    
    @OneToMany(mappedBy = "assessmentQuestion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StudentAnswer> studentAnswers;
    
    // Constructors
    public AssessmentQuestion() {}
    
    public AssessmentQuestion(String questionText, QuestionType questionType, 
                             Integer pointsValue, Integer questionOrder) {
        this.questionText = questionText;
        this.questionType = questionType;
        this.pointsValue = pointsValue;
        this.questionOrder = questionOrder;
    }
    
    // Getters and Setters
    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }
    
    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    
    public QuestionType getQuestionType() { return questionType; }
    public void setQuestionType(QuestionType questionType) { this.questionType = questionType; }
    
    public Integer getPointsValue() { return pointsValue; }
    public void setPointsValue(Integer pointsValue) { this.pointsValue = pointsValue; }
    
    public Integer getQuestionOrder() { return questionOrder; }
    public void setQuestionOrder(Integer questionOrder) { this.questionOrder = questionOrder; }
    
    public SkillAssessment getSkillAssessment() { return skillAssessment; }
    public void setSkillAssessment(SkillAssessment skillAssessment) { this.skillAssessment = skillAssessment; }
    
    public List<QuestionOption> getQuestionOptions() { return questionOptions; }
    public void setQuestionOptions(List<QuestionOption> questionOptions) { this.questionOptions = questionOptions; }
    
    public List<StudentAnswer> getStudentAnswers() { return studentAnswers; }
    public void setStudentAnswers(List<StudentAnswer> studentAnswers) { this.studentAnswers = studentAnswers; }
}

enum QuestionType {
    MULTIPLE_CHOICE, TRUE_FALSE, SHORT_ANSWER, ESSAY
}