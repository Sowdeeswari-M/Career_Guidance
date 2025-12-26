package com.campusconnect.facility.repository;

import com.campusconnect.facility.model.StudentAnswer;
import com.campusconnect.facility.model.AssessmentAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentAnswerRepository extends JpaRepository<StudentAnswer, Long> {
    
    List<StudentAnswer> findByAssessmentAttemptOrderByAssessmentQuestionQuestionOrder(AssessmentAttempt assessmentAttempt);
    
    @Query("SELECT a FROM StudentAnswer a WHERE a.assessmentAttempt.attemptId = :attemptId ORDER BY a.assessmentQuestion.questionOrder")
    List<StudentAnswer> findByAttemptIdOrderByQuestionOrder(@Param("attemptId") Long attemptId);
    
    @Query("SELECT COUNT(a) FROM StudentAnswer a WHERE a.assessmentAttempt = :attempt AND a.isCorrect = true")
    Long countCorrectAnswersByAttempt(@Param("attempt") AssessmentAttempt attempt);
    
    @Query("SELECT SUM(a.pointsEarned) FROM StudentAnswer a WHERE a.assessmentAttempt = :attempt")
    Double getTotalPointsByAttempt(@Param("attempt") AssessmentAttempt attempt);
}