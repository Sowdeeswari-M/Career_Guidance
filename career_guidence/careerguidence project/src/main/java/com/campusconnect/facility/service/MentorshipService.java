package com.campusconnect.facility.service;

import com.campusconnect.facility.dto.MentorshipRequestDto;
import com.campusconnect.facility.model.*;
import com.campusconnect.facility.repository.MentorshipRequestRepository;
import com.campusconnect.facility.repository.MentorshipSessionRepository;
import com.campusconnect.facility.repository.UserAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MentorshipService {
    
    private static final Logger logger = LoggerFactory.getLogger(MentorshipService.class);
    
    @Autowired
    private MentorshipRequestRepository requestRepository;
    
    @Autowired
    private MentorshipSessionRepository sessionRepository;
    
    @Autowired
    private UserAccountRepository userRepository;
    
    @Autowired
    private ProgressRecordService progressService;
    
    public MentorshipRequest submitMentorshipRequest(MentorshipRequestDto requestDto, UserAccount student) {
        // Check if student has too many pending requests
        Long pendingCount = requestRepository.countPendingRequestsByStudent(student);
        if (pendingCount >= 3) {
            throw new RuntimeException("You have too many pending mentorship requests. Please wait for responses.");
        }
        
        MentorshipRequest newRequest = new MentorshipRequest(student, requestDto.getRequestMessage(), requestDto.getMentorshipGoals());
        newRequest.setPreferredMeetingSchedule(requestDto.getPreferredMeetingSchedule());
        
        // If specific mentor is requested
        if (requestDto.getMentorId() != null) {
            UserAccount mentor = userRepository.findById(requestDto.getMentorId())
                    .orElseThrow(() -> new RuntimeException("Mentor not found"));
            
            if (!mentor.getUserRole().name().equals("MENTOR")) {
                throw new RuntimeException("Selected user is not a mentor");
            }
            
            newRequest.setMentorUser(mentor);
        }
        
        MentorshipRequest savedRequest = requestRepository.save(newRequest);
        logger.info("Mentorship request submitted: Student {} - Request ID {}", 
                   student.getUsername(), savedRequest.getRequestId());
        
        return savedRequest;
    }
    
    public List<MentorshipRequest> getPendingRequestsForMentor(UserAccount mentor) {
        return requestRepository.findPendingRequestsForMentor(mentor);
    }
    
    public List<MentorshipRequest> getStudentRequests(UserAccount student) {
        return requestRepository.findByStudentUserOrderByRequestDateDesc(student);
    }
    
    public List<MentorshipRequest> getActiveMentorshipsForStudent(UserAccount student) {
        return requestRepository.findActiveMentorshipsForStudent(student);
    }
    
    public List<MentorshipRequest> getActiveMentorshipsForMentor(UserAccount mentor) {
        return requestRepository.findActiveMentorshipsForMentor(mentor);
    }
    
    public MentorshipRequest acceptMentorshipRequest(Long requestId, UserAccount mentor, String response) {
        MentorshipRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Mentorship request not found"));
        
        if (request.getRequestStatus() != RequestStatus.PENDING) {
            throw new RuntimeException("Request is no longer pending");
        }
        
        // Check mentor's current mentorship load
        Long currentMentorships = requestRepository.countAcceptedRequestsByMentor(mentor);
        if (currentMentorships >= 5) { // Limit mentors to 5 active mentorships
            throw new RuntimeException("You have reached the maximum number of active mentorships");
        }
        
        request.acceptRequest(mentor, response);
        MentorshipRequest acceptedRequest = requestRepository.save(request);
        
        // Record progress for student
        progressService.recordMentorshipMilestone(request.getStudentUser(), "Mentorship Started", 
                                                "Started mentorship with " + mentor.getFullName());
        
        logger.info("Mentorship request accepted: Mentor {} - Student {} - Request ID {}", 
                   mentor.getUsername(), request.getStudentUser().getUsername(), requestId);
        
        return acceptedRequest;
    }
    
    public MentorshipRequest rejectMentorshipRequest(Long requestId, UserAccount mentor, String response) {
        MentorshipRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Mentorship request not found"));
        
        if (request.getRequestStatus() != RequestStatus.PENDING) {
            throw new RuntimeException("Request is no longer pending");
        }
        
        request.rejectRequest(response);
        MentorshipRequest rejectedRequest = requestRepository.save(request);
        
        logger.info("Mentorship request rejected: Mentor {} - Request ID {}", 
                   mentor.getUsername(), requestId);
        
        return rejectedRequest;
    }
    
    public MentorshipSession scheduleSession(Long requestId, String sessionTitle, String description, 
                                           LocalDateTime scheduledTime, Integer durationMinutes, UserAccount mentor) {
        MentorshipRequest mentorship = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Mentorship not found"));
        
        if (!mentorship.getMentorUser().getUserId().equals(mentor.getUserId())) {
            throw new RuntimeException("Unauthorized access to mentorship");
        }
        
        if (mentorship.getRequestStatus() != RequestStatus.ACCEPTED) {
            throw new RuntimeException("Mentorship is not active");
        }
        
        MentorshipSession session = new MentorshipSession(mentorship, sessionTitle, scheduledTime, durationMinutes);
        session.setSessionDescription(description);
        
        MentorshipSession savedSession = sessionRepository.save(session);
        logger.info("Mentorship session scheduled: {} - {}", sessionTitle, scheduledTime);
        
        return savedSession;
    }
    
    public MentorshipSession startSession(Long sessionId, UserAccount mentor) {
        MentorshipSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        
        if (!session.getMentorshipRequest().getMentorUser().getUserId().equals(mentor.getUserId())) {
            throw new RuntimeException("Unauthorized access to session");
        }
        
        session.startSession();
        return sessionRepository.save(session);
    }
    
    public MentorshipSession completeSession(Long sessionId, String sessionNotes, UserAccount mentor) {
        MentorshipSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        
        if (!session.getMentorshipRequest().getMentorUser().getUserId().equals(mentor.getUserId())) {
            throw new RuntimeException("Unauthorized access to session");
        }
        
        session.completeSession(sessionNotes);
        MentorshipSession completedSession = sessionRepository.save(session);
        
        // Record progress for student
        UserAccount student = session.getMentorshipRequest().getStudentUser();
        progressService.recordMentorshipMilestone(student, "Session Completed", 
                                                "Completed mentorship session: " + session.getSessionTitle());
        
        logger.info("Mentorship session completed: {}", session.getSessionTitle());
        return completedSession;
    }
    
    public List<MentorshipSession> getSessionsForMentorship(Long requestId) {
        MentorshipRequest mentorship = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Mentorship not found"));
        
        return sessionRepository.findByMentorshipRequestOrderByScheduledDateTimeDesc(mentorship);
    }
    
    public List<MentorshipSession> getUpcomingSessionsForUser(UserAccount user) {
        LocalDateTime now = LocalDateTime.now();
        
        if (user.getUserRole().name().equals("STUDENT")) {
            return sessionRepository.findUpcomingSessionsForStudent(user, now);
        } else if (user.getUserRole().name().equals("MENTOR")) {
            return sessionRepository.findUpcomingSessionsForMentor(user, now);
        }
        
        return List.of();
    }
    
    public void addStudentFeedback(Long sessionId, String feedback, UserAccount student) {
        MentorshipSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        
        if (!session.getMentorshipRequest().getStudentUser().getUserId().equals(student.getUserId())) {
            throw new RuntimeException("Unauthorized access to session");
        }
        
        session.setStudentFeedback(feedback);
        sessionRepository.save(session);
        
        logger.info("Student feedback added for session: {}", session.getSessionTitle());
    }
    
    public void addMentorFeedback(Long sessionId, String feedback, UserAccount mentor) {
        MentorshipSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        
        if (!session.getMentorshipRequest().getMentorUser().getUserId().equals(mentor.getUserId())) {
            throw new RuntimeException("Unauthorized access to session");
        }
        
        session.setMentorFeedback(feedback);
        sessionRepository.save(session);
        
        logger.info("Mentor feedback added for session: {}", session.getSessionTitle());
    }
    
    public Page<UserAccount> findAvailableMentors(Pageable pageable) {
        return userRepository.findAvailableMentors(pageable);
    }
    
    public List<UserAccount> findMentorsByExpertise(String skillArea) {
        return userRepository.findMentorsByExpertiseArea(skillArea);
    }
    
    public void endMentorship(Long requestId, UserAccount user, String reason) {
        MentorshipRequest mentorship = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Mentorship not found"));
        
        // Check if user is either the student or mentor
        boolean isAuthorized = mentorship.getStudentUser().getUserId().equals(user.getUserId()) ||
                              (mentorship.getMentorUser() != null && mentorship.getMentorUser().getUserId().equals(user.getUserId()));
        
        if (!isAuthorized) {
            throw new RuntimeException("Unauthorized access to mentorship");
        }
        
        mentorship.setIsActive(false);
        mentorship.setMentorshipEndDate(LocalDateTime.now());
        requestRepository.save(mentorship);
        
        logger.info("Mentorship ended: Request ID {} - Reason: {}", requestId, reason);
    }
}