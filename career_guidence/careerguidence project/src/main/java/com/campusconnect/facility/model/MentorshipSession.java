package com.campusconnect.facility.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Entity
@Table(name = "mentorship_sessions")
public class MentorshipSession {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private MentorshipRequest mentorshipRequest;
    
    @NotBlank(message = "Session title is required")
    @Column(nullable = false)
    private String sessionTitle;
    
    @Column(columnDefinition = "TEXT")
    private String sessionDescription;
    
    @Column(nullable = false)
    private LocalDateTime scheduledDateTime;
    
    private Integer durationMinutes;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SessionStatus sessionStatus = SessionStatus.SCHEDULED;
    
    @Column(columnDefinition = "TEXT")
    private String sessionNotes;
    
    @Column(columnDefinition = "TEXT")
    private String studentFeedback;
    
    @Column(columnDefinition = "TEXT")
    private String mentorFeedback;
    
    private LocalDateTime actualStartTime;
    private LocalDateTime actualEndTime;
    
    @Column(nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();
    
    // Constructors
    public MentorshipSession() {}
    
    public MentorshipSession(MentorshipRequest mentorshipRequest, String sessionTitle, 
                           LocalDateTime scheduledDateTime, Integer durationMinutes) {
        this.mentorshipRequest = mentorshipRequest;
        this.sessionTitle = sessionTitle;
        this.scheduledDateTime = scheduledDateTime;
        this.durationMinutes = durationMinutes;
    }
    
    // Business methods
    public void startSession() {
        this.actualStartTime = LocalDateTime.now();
        this.sessionStatus = SessionStatus.IN_PROGRESS;
    }
    
    public void completeSession(String notes) {
        this.actualEndTime = LocalDateTime.now();
        this.sessionStatus = SessionStatus.COMPLETED;
        this.sessionNotes = notes;
    }
    
    // Getters and Setters
    public Long getSessionId() { return sessionId; }
    public void setSessionId(Long sessionId) { this.sessionId = sessionId; }
    
    public MentorshipRequest getMentorshipRequest() { return mentorshipRequest; }
    public void setMentorshipRequest(MentorshipRequest mentorshipRequest) { this.mentorshipRequest = mentorshipRequest; }
    
    public String getSessionTitle() { return sessionTitle; }
    public void setSessionTitle(String sessionTitle) { this.sessionTitle = sessionTitle; }
    
    public String getSessionDescription() { return sessionDescription; }
    public void setSessionDescription(String sessionDescription) { this.sessionDescription = sessionDescription; }
    
    public LocalDateTime getScheduledDateTime() { return scheduledDateTime; }
    public void setScheduledDateTime(LocalDateTime scheduledDateTime) { this.scheduledDateTime = scheduledDateTime; }
    
    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }
    
    public SessionStatus getSessionStatus() { return sessionStatus; }
    public void setSessionStatus(SessionStatus sessionStatus) { this.sessionStatus = sessionStatus; }
    
    public String getSessionNotes() { return sessionNotes; }
    public void setSessionNotes(String sessionNotes) { this.sessionNotes = sessionNotes; }
    
    public String getStudentFeedback() { return studentFeedback; }
    public void setStudentFeedback(String studentFeedback) { this.studentFeedback = studentFeedback; }
    
    public String getMentorFeedback() { return mentorFeedback; }
    public void setMentorFeedback(String mentorFeedback) { this.mentorFeedback = mentorFeedback; }
    
    public LocalDateTime getActualStartTime() { return actualStartTime; }
    public void setActualStartTime(LocalDateTime actualStartTime) { this.actualStartTime = actualStartTime; }
    
    public LocalDateTime getActualEndTime() { return actualEndTime; }
    public void setActualEndTime(LocalDateTime actualEndTime) { this.actualEndTime = actualEndTime; }
    
    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
}

enum SessionStatus {
    SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED, NO_SHOW
}