package com.campusconnect.facility.repository;

import com.campusconnect.facility.model.AssessmentAttempt;
import com.campusconnect.facility.model.UserAccount;
import com.campusconnect.facility.model.SkillAssessment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AssessmentAttemptRepository extends JpaRepository<AssessmentAttempt, Long> {
    
    List<AssessmentAttempt> findByStudentUserOrderByStartTimeDesc(UserAccount studentUser);
    
    List<AssessmentAttempt> findBySkillAssessmentOrderByStartTimeDesc(SkillAssessment skillAssessment);
    
    Optional<AssessmentAttempt> findByStudentUserAndSkillAssessmentAndAttemptStatus(
        UserAccount studentUser, SkillAssessment skillAssessment, String attemptStatus);
    
    @Query("SELECT a FROM AssessmentAttempt a WHERE a.studentUser = :user AND a.attemptStatus = 'COMPLETED' ORDER BY a.completionTime DESC")
    List<AssessmentAttempt> findCompletedAttemptsByUser(@Param("user") UserAccount user);
    
    @Query("SELECT a FROM AssessmentAttempt a WHERE a.studentUser = :user AND a.isPassed = true ORDER BY a.completionTime DESC")
    List<AssessmentAttempt> findPassedAttemptsByUser(@Param("user") UserAccount user);
    
    @Query("SELECT COUNT(a) FROM AssessmentAttempt a WHERE a.studentUser = :user AND a.attemptStatus = 'COMPLETED'")
    Long countCompletedAttemptsByUser(@Param("user") UserAccount user);
    
    @Query("SELECT AVG(a.percentageScore) FROM AssessmentAttempt a WHERE a.studentUser = :user AND a.attemptStatus = 'COMPLETED'")
    Double findAverageScoreByUser(@Param("user") UserAccount user);
    
    @Query("SELECT a FROM AssessmentAttempt a WHERE a.startTime < :cutoffTime AND a.attemptStatus = 'IN_PROGRESS'")
    List<AssessmentAttempt> findExpiredAttempts(@Param("cutoffTime") LocalDateTime cutoffTime);
    
    Page<AssessmentAttempt> findByAttemptStatusOrderByStartTimeDesc(String attemptStatus, Pageable pageable);
    
    @Query("SELECT a FROM AssessmentAttempt a WHERE a.skillAssessment.skillCategory = :category AND a.attemptStatus = 'COMPLETED' ORDER BY a.percentageScore DESC")
    List<AssessmentAttempt> findTopPerformersByCategory(@Param("category") String category, Pageable pageable);
}