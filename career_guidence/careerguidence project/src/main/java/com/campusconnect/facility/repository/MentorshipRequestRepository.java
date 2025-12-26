package com.campusconnect.facility.repository;

import com.campusconnect.facility.model.MentorshipRequest;
import com.campusconnect.facility.model.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MentorshipRequestRepository extends JpaRepository<MentorshipRequest, Long> {
    
    List<MentorshipRequest> findByStudentUserOrderByRequestDateDesc(UserAccount studentUser);
    
    List<MentorshipRequest> findByMentorUserOrderByRequestDateDesc(UserAccount mentorUser);
    
    List<MentorshipRequest> findByRequestStatusOrderByRequestDateDesc(String requestStatus);
    
    @Query("SELECT m FROM MentorshipRequest m WHERE m.mentorUser = :mentor AND m.requestStatus = 'PENDING' ORDER BY m.requestDate ASC")
    List<MentorshipRequest> findPendingRequestsForMentor(@Param("mentor") UserAccount mentor);
    
    @Query("SELECT m FROM MentorshipRequest m WHERE m.studentUser = :student AND m.requestStatus = 'ACCEPTED' AND m.isActive = true")
    List<MentorshipRequest> findActiveMentorshipsForStudent(@Param("student") UserAccount student);
    
    @Query("SELECT m FROM MentorshipRequest m WHERE m.mentorUser = :mentor AND m.requestStatus = 'ACCEPTED' AND m.isActive = true")
    List<MentorshipRequest> findActiveMentorshipsForMentor(@Param("mentor") UserAccount mentor);
    
    @Query("SELECT COUNT(m) FROM MentorshipRequest m WHERE m.mentorUser = :mentor AND m.requestStatus = 'ACCEPTED'")
    Long countAcceptedRequestsByMentor(@Param("mentor") UserAccount mentor);
    
    @Query("SELECT COUNT(m) FROM MentorshipRequest m WHERE m.studentUser = :student AND m.requestStatus = 'PENDING'")
    Long countPendingRequestsByStudent(@Param("student") UserAccount student);
    
    Page<MentorshipRequest> findByIsActiveTrueOrderByRequestDateDesc(Pageable pageable);
    
    @Query("SELECT m FROM MentorshipRequest m WHERE m.requestStatus = :status AND m.isActive = true ORDER BY m.requestDate DESC")
    Page<MentorshipRequest> findByStatusAndActive(@Param("status") String status, Pageable pageable);
}