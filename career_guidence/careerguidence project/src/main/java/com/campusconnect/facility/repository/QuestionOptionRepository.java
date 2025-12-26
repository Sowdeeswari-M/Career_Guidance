package com.campusconnect.facility.repository;

import com.campusconnect.facility.model.QuestionOption;
import com.campusconnect.facility.model.AssessmentQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionOptionRepository extends JpaRepository<QuestionOption, Long> {
    
    List<QuestionOption> findByAssessmentQuestionOrderByOptionOrder(AssessmentQuestion assessmentQuestion);
    
    @Query("SELECT o FROM QuestionOption o WHERE o.assessmentQuestion.questionId = :questionId ORDER BY o.optionOrder")
    List<QuestionOption> findByQuestionIdOrderByOptionOrder(@Param("questionId") Long questionId);
    
    @Query("SELECT o FROM QuestionOption o WHERE o.assessmentQuestion = :question AND o.isCorrectAnswer = true")
    Optional<QuestionOption> findCorrectAnswerByQuestion(@Param("question") AssessmentQuestion question);
    
    @Query("SELECT COUNT(o) FROM QuestionOption o WHERE o.assessmentQuestion = :question")
    Long countOptionsByQuestion(@Param("question") AssessmentQuestion question);
}