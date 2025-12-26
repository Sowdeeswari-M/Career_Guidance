package com.campusconnect.facility.controller;

import com.campusconnect.facility.dto.MentorshipRequestDto;
import com.campusconnect.facility.model.UserAccount;
import com.campusconnect.facility.service.MentorshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/mentorship")
public class MentorshipController {
    
    @Autowired
    private MentorshipService mentorshipService;
    
    @PostMapping("/request")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> submitRequest(@Valid @RequestBody MentorshipRequestDto requestDto, Authentication auth) {
        try {
            UserAccount student = (UserAccount) auth.getPrincipal();
            var request = mentorshipService.submitMentorshipRequest(requestDto, student);
            return ResponseEntity.ok(Map.of("requestId", request.getRequestId(), "message", "Request submitted"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/{requestId}/accept")
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseEntity<?> acceptRequest(@PathVariable Long requestId, @RequestBody Map<String, String> response, Authentication auth) {
        try {
            UserAccount mentor = (UserAccount) auth.getPrincipal();
            var request = mentorshipService.acceptMentorshipRequest(requestId, mentor, response.get("response"));
            return ResponseEntity.ok(Map.of("message", "Request accepted"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/my-requests")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getMyRequests(Authentication auth) {
        UserAccount student = (UserAccount) auth.getPrincipal();
        return ResponseEntity.ok(mentorshipService.getStudentRequests(student));
    }
    
    @GetMapping("/pending")
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseEntity<?> getPendingRequests(Authentication auth) {
        UserAccount mentor = (UserAccount) auth.getPrincipal();
        return ResponseEntity.ok(mentorshipService.getPendingRequestsForMentor(mentor));
    }
}