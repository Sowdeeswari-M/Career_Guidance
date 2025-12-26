package com.campusconnect.facility.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "mentorship_requests")
public class MentorshipRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private UserAccount studentUser;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_id")
    private UserAccount mentorUser;
    
    @NotBlank(message = "Request message is required")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String requestMessage;
    
    @Column(columnDefinition = "TEXT")
    private String mentorshipGoals;
    
    @Column(columnDefinition = "TEXT")
    private String preferredMeetingSchedule;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus requestStatus = RequestStatus.PENDING;
    
    @Column(nullable = false)
    private LocalDateTime requestDate = LocalDateTime.now();
    
    private LocalDateTime responseDate;
    
    @Column(columnDefinition = "TEXT")
    private String mentorResponse;
    
    private LocalDateTime mentorshipStartDate;
    private LocalDateTime mentorshipEndDate;
    
    @Column(nullable = false)
    private Boolean isActive = true;
    
    @OneToMany(mappedBy = "mentorshipRequest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MentorshipSession> mentorshipSessions;
    
    // Constructors
    public MentorshipRequest() {}
    
    public MentorshipRequest(UserAccount studentUser, String requestMessage, String mentorshipGoals) {
        this.studentUser = studentUser;
        this.requestMessage = requestMessage;
        this.mentorshipGoals = mentorshipGoals;
    }
    
    // Business methods
    public void acceptRequest(UserAccount mentor, String response) {
        this.mentorUser = mentor;
        this.requestStatus = RequestStatus.ACCEPTED;
        this.responseDate = LocalDateTime.now();
        this.mentorResponse = response;
        this.mentorshipStartDate = LocalDateTime.now();
    }
    
    public void rejectRequest(String response) {
        this.requestStatus = RequestStatus.REJECTED;
        this.responseDate = LocalDateTime.now();
        this.mentorResponse = response;
    }
    
    // Getters and Setters
    public Long getRequestId() { return requestId; }
    public void setRequestId(Long requestId) { this.requestId = requestId; }
    
    public UserAccount getStudentUser() { return studentUser; }
    public void setStudentUser(UserAccount studentUser) { this.studentUser = studentUser; }
    
    public UserAccount getMentorUser() { return mentorUser; }
    public void setMentorUser(UserAccount mentorUser) { this.mentorUser = mentorUser; }
    
    public String getRequestMessage() { return requestMessage; }
    public void setRequestMessage(String requestMessage) { this.requestMessage = requestMessage; }
    
    public String getMentorshipGoals() { return mentorshipGoals; }
    public void setMentorshipGoals(String mentorshipGoals) { this.mentorshipGoals = mentorshipGoals; }
    
    public String getPreferredMeetingSchedule() { return preferredMeetingSchedule; }
    public void setPreferredMeetingSchedule(String preferredMeetingSchedule) { this.preferredMeetingSchedule = preferredMeetingSchedule; }
    
    public RequestStatus getRequestStatus() { return requestStatus; }
    public void setRequestStatus(RequestStatus requestStatus) { this.requestStatus = requestStatus; }
    
    public LocalDateTime getRequestDate() { return requestDate; }
    public void setRequestDate(LocalDateTime requestDate) { this.requestDate = requestDate; }
    
    public LocalDateTime getResponseDate() { return responseDate; }
    public void setResponseDate(LocalDateTime responseDate) { this.responseDate = responseDate; }
    
    public String getMentorResponse() { return mentorResponse; }
    public void setMentorResponse(String mentorResponse) { this.mentorResponse = mentorResponse; }
    
    public LocalDateTime getMentorshipStartDate() { return mentorshipStartDate; }
    public void setMentorshipStartDate(LocalDateTime mentorshipStartDate) { this.mentorshipStartDate = mentorshipStartDate; }
    
    public LocalDateTime getMentorshipEndDate() { return mentorshipEndDate; }
    public void setMentorshipEndDate(LocalDateTime mentorshipEndDate) { this.mentorshipEndDate = mentorshipEndDate; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public List<MentorshipSession> getMentorshipSessions() { return mentorshipSessions; }
    public void setMentorshipSessions(List<MentorshipSession> mentorshipSessions) { this.mentorshipSessions = mentorshipSessions; }
}
