package com.campusconnect.facility.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class MentorshipRequestDto {
    
    private Long mentorId;
    
    @NotBlank(message = "Request message is required")
    @Size(min = 10, max = 1000, message = "Request message must be between 10 and 1000 characters")
    private String requestMessage;
    
    @NotBlank(message = "Mentorship goals are required")
    @Size(min = 10, max = 500, message = "Mentorship goals must be between 10 and 500 characters")
    private String mentorshipGoals;
    
    private String preferredMeetingSchedule;
    private String skillAreasOfInterest;
    private String currentChallenges;
    
    // Constructors
    public MentorshipRequestDto() {}
    
    public MentorshipRequestDto(String requestMessage, String mentorshipGoals) {
        this.requestMessage = requestMessage;
        this.mentorshipGoals = mentorshipGoals;
    }
    
    // Getters and Setters
    public Long getMentorId() { return mentorId; }
    public void setMentorId(Long mentorId) { this.mentorId = mentorId; }
    
    public String getRequestMessage() { return requestMessage; }
    public void setRequestMessage(String requestMessage) { this.requestMessage = requestMessage; }
    
    public String getMentorshipGoals() { return mentorshipGoals; }
    public void setMentorshipGoals(String mentorshipGoals) { this.mentorshipGoals = mentorshipGoals; }
    
    public String getPreferredMeetingSchedule() { return preferredMeetingSchedule; }
    public void setPreferredMeetingSchedule(String preferredMeetingSchedule) { this.preferredMeetingSchedule = preferredMeetingSchedule; }
    
    public String getSkillAreasOfInterest() { return skillAreasOfInterest; }
    public void setSkillAreasOfInterest(String skillAreasOfInterest) { this.skillAreasOfInterest = skillAreasOfInterest; }
    
    public String getCurrentChallenges() { return currentChallenges; }
    public void setCurrentChallenges(String currentChallenges) { this.currentChallenges = currentChallenges; }
}