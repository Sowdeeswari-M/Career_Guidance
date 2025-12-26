package com.campusconnect.facility.repository;

import com.campusconnect.facility.model.AssessmentQuestion;
import com.campusconnect.facility.model.SkillAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentQuestionRepository extends JpaRepository<AssessmentQuestion, Long> {
    
    List<AssessmentQuestion> findBySkillAssessmentOrderByQuestionOrder(SkillAssessment skillAssessment);
    
    @Query("SELECT q FROM AssessmentQuestion q WHERE q.skillAssessment.assessmentId = :assessmentId ORDER BY q.questionOrder")
    List<AssessmentQuestion> findByAssessmentIdOrderByQuestionOrder(@Param("assessmentId") Long assessmentId);
    
    @Query("SELECT COUNT(q) FROM AssessmentQuestion q WHERE q.skillAssessment = :assessment")
    Long countQuestionsByAssessment(@Param("assessment") SkillAssessment assessment);
    
    @Query("SELECT SUM(q.pointsValue) FROM AssessmentQuestion q WHERE q.skillAssessment = :assessment")
    Integer getTotalPointsByAssessment(@Param("assessment") SkillAssessment assessment);
}