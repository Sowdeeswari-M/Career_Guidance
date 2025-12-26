package com.campusconnect.facility.controller;

import com.campusconnect.facility.dto.AssessmentSubmissionDto;
import com.campusconnect.facility.model.UserAccount;
import com.campusconnect.facility.service.AssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/assessment")
@PreAuthorize("hasRole('STUDENT')")
public class AssessmentController {
    
    @Autowired
    private AssessmentService assessmentService;
    
    @GetMapping
    public ResponseEntity<?> getAllAssessments() {
        return ResponseEntity.ok(assessmentService.getAllActiveAssessments());
    }
    
    @PostMapping("/{assessmentId}/start")
    public ResponseEntity<?> startAssessment(@PathVariable Long assessmentId, Authentication auth) {
        try {
            UserAccount user = (UserAccount) auth.getPrincipal();
            var attempt = assessmentService.startAssessment(assessmentId, user);
            return ResponseEntity.ok(Map.of("attemptId", attempt.getAttemptId(), "message", "Assessment started"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/submit")
    public ResponseEntity<?> submitAssessment(@Valid @RequestBody AssessmentSubmissionDto submission, Authentication auth) {
        try {
            UserAccount user = (UserAccount) auth.getPrincipal();
            var result = assessmentService.submitAssessment(submission, user);
            return ResponseEntity.ok(Map.of("score", result.getPercentageScore(), "passed", result.getIsPassed()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/my-attempts")
    public ResponseEntity<?> getMyAttempts(Authentication auth) {
        UserAccount user = (UserAccount) auth.getPrincipal();
        return ResponseEntity.ok(assessmentService.getUserAttempts(user));
    }
}