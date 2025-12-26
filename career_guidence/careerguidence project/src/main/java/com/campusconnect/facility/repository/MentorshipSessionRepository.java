package com.campusconnect.facility.repository;

import com.campusconnect.facility.model.MentorshipSession;
import com.campusconnect.facility.model.MentorshipRequest;
import com.campusconnect.facility.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MentorshipSessionRepository extends JpaRepository<MentorshipSession, Long> {
    
    List<MentorshipSession> findByMentorshipRequestOrderByScheduledDateTimeDesc(MentorshipRequest mentorshipRequest);
    
    @Query("SELECT s FROM MentorshipSession s WHERE s.mentorshipRequest.studentUser = :student AND s.scheduledDateTime > :currentTime ORDER BY s.scheduledDateTime ASC")
    List<MentorshipSession> findUpcomingSessionsForStudent(@Param("student") UserAccount student, @Param("currentTime") LocalDateTime currentTime);
    
    @Query("SELECT s FROM MentorshipSession s WHERE s.mentorshipRequest.mentorUser = :mentor AND s.scheduledDateTime > :currentTime ORDER BY s.scheduledDateTime ASC")
    List<MentorshipSession> findUpcomingSessionsForMentor(@Param("mentor") UserAccount mentor, @Param("currentTime") LocalDateTime currentTime);
    
    @Query("SELECT s FROM MentorshipSession s WHERE s.sessionStatus = :status ORDER BY s.scheduledDateTime DESC")
    List<MentorshipSession> findBySessionStatus(@Param("status") String status);
    
    @Query("SELECT COUNT(s) FROM MentorshipSession s WHERE s.mentorshipRequest.mentorUser = :mentor AND s.sessionStatus = 'COMPLETED'")
    Long countCompletedSessionsByMentor(@Param("mentor") UserAccount mentor);
    
    @Query("SELECT COUNT(s) FROM MentorshipSession s WHERE s.mentorshipRequest.studentUser = :student AND s.sessionStatus = 'COMPLETED'")
    Long countCompletedSessionsByStudent(@Param("student") UserAccount student);
}