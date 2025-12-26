package com.campusconnect.facility.controller;

import com.campusconnect.facility.model.UserAccount;
import com.campusconnect.facility.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/recommendation")
@PreAuthorize("hasRole('STUDENT')")
public class RecommendationController {
    
    @Autowired
    private RecommendationService recommendationService;
    
    @GetMapping
    public ResponseEntity<?> getRecommendations(Authentication auth) {
        UserAccount user = (UserAccount) auth.getPrincipal();
        return ResponseEntity.ok(recommendationService.getActiveRecommendationsForUser(user));
    }
    
    @PostMapping("/generate")
    public ResponseEntity<?> generateRecommendations(Authentication auth) {
        try {
            UserAccount user = (UserAccount) auth.getPrincipal();
            var recommendations = recommendationService.generateRecommendationsForUser(user);
            return ResponseEntity.ok(Map.of("count", recommendations.size(), "recommendations", recommendations));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/{recommendationId}/dismiss")
    public ResponseEntity<?> dismissRecommendation(@PathVariable Long recommendationId, Authentication auth) {
        try {
            UserAccount user = (UserAccount) auth.getPrincipal();
            recommendationService.dismissRecommendation(recommendationId, user);
            return ResponseEntity.ok(Map.of("message", "Recommendation dismissed"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}