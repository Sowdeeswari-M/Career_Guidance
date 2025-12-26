package com.campusconnect.facility.service;

import com.campusconnect.facility.model.*;
import com.campusconnect.facility.repository.ProgressRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProgressRecordService {
    
    private static final Logger logger = LoggerFactory.getLogger(ProgressRecordService.class);
    
    @Autowired
    private ProgressRecordRepository progressRepository;
    
    public ProgressRecord recordAssessmentCompletion(UserAccount user, AssessmentAttempt attempt) {
        String achievementTitle = "Completed " + attempt.getSkillAssessment().getAssessmentTitle();
        String description = String.format("Scored %.1f%% on %s assessment", 
                                          attempt.getPercentageScore(), 
                                          attempt.getSkillAssessment().getSkillCategory());
        
        int points = calculateAssessmentPoints(attempt);
        
        ProgressRecord record = new ProgressRecord(user, "Assessment Completion", achievementTitle, 
                                                 AchievementCategory.ASSESSMENT_COMPLETION, points);
        record.setAchievementDescription(description);
        record.setSkillsImproved(attempt.getSkillAssessment().getSkillCategory());
        record.setIsVerified(true); // Auto-verify assessment completions
        record.setVerificationDate(LocalDateTime.now());
        
        ProgressRecord savedRecord = progressRepository.save(record);
        logger.info("Assessment completion recorded: User {} - Points {}", user.getUsername(), points);
        
        return savedRecord;
    }
    
    public ProgressRecord recordSkillImprovement(UserAccount user, String skillName, String improvementDetails) {
        String achievementTitle = "Skill Improvement: " + skillName;
        
        ProgressRecord record = new ProgressRecord(user, "Skill Development", achievementTitle, 
                                                 AchievementCategory.SKILL_IMPROVEMENT, 25);
        record.setAchievementDescription(improvementDetails);
        record.setSkillsImproved(skillName);
        
        ProgressRecord savedRecord = progressRepository.save(record);
        logger.info("Skill improvement recorded: User {} - Skill {}", user.getUsername(), skillName);
        
        return savedRecord;
    }
    
    public ProgressRecord recordCourseCompletion(UserAccount user, String courseName, String certificateUrl) {
        String achievementTitle = "Completed Course: " + courseName;
        String description = "Successfully completed the " + courseName + " course";
        
        ProgressRecord record = new ProgressRecord(user, "Course Completion", achievementTitle, 
                                                 AchievementCategory.COURSE_COMPLETION, 50);
        record.setAchievementDescription(description);
        record.setCertificateUrl(certificateUrl);
        
        ProgressRecord savedRecord = progressRepository.save(record);
        logger.info("Course completion recorded: User {} - Course {}", user.getUsername(), courseName);
        
        return savedRecord;
    }
    
    public ProgressRecord recordCertification(UserAccount user, String certificationName, String certificateUrl) {
        String achievementTitle = "Earned Certification: " + certificationName;
        String description = "Successfully earned " + certificationName + " certification";
        
        ProgressRecord record = new ProgressRecord(user, "Certification", achievementTitle, 
                                                 AchievementCategory.CERTIFICATION, 100);
        record.setAchievementDescription(description);
        record.setCertificateUrl(certificateUrl);
        
        ProgressRecord savedRecord = progressRepository.save(record);
        logger.info("Certification recorded: User {} - Certification {}", user.getUsername(), certificationName);
        
        return savedRecord;
    }
    
    public ProgressRecord recordMentorshipMilestone(UserAccount user, String milestoneType, String description) {
        String achievementTitle = "Mentorship Milestone: " + milestoneType;
        
        int points = calculateMentorshipPoints(milestoneType);
        
        ProgressRecord record = new ProgressRecord(user, "Mentorship", achievementTitle, 
                                                 AchievementCategory.MENTORSHIP_MILESTONE, points);
        record.setAchievementDescription(description);
        record.setIsVerified(true); // Auto-verify mentorship milestones
        record.setVerificationDate(LocalDateTime.now());
        
        ProgressRecord savedRecord = progressRepository.save(record);
        logger.info("Mentorship milestone recorded: User {} - Milestone {}", user.getUsername(), milestoneType);
        
        return savedRecord;
    }
    
    public ProgressRecord recordCareerMilestone(UserAccount user, String milestoneType, String description) {
        String achievementTitle = "Career Milestone: " + milestoneType;
        
        ProgressRecord record = new ProgressRecord(user, "Career Achievement", achievementTitle, 
                                                 AchievementCategory.CAREER_MILESTONE, 75);
        record.setAchievementDescription(description);
        
        ProgressRecord savedRecord = progressRepository.save(record);
        logger.info("Career milestone recorded: User {} - Milestone {}", user.getUsername(), milestoneType);
        
        return savedRecord;
    }
    
    public List<ProgressRecord> getUserProgress(UserAccount user) {
        return progressRepository.findByUserAccountOrderByAchievedDateDesc(user);
    }
    
    public List<ProgressRecord> getUserProgressByCategory(UserAccount user, AchievementCategory category) {
        return progressRepository.findByUserAccountAndAchievementCategoryOrderByAchievedDateDesc(user, category.name());
    }
    
    public List<ProgressRecord> getVerifiedAchievements(UserAccount user) {
        return progressRepository.findVerifiedAchievementsByUser(user);
    }
    
    public Integer getTotalPointsForUser(UserAccount user) {
        Integer totalPoints = progressRepository.getTotalPointsByUser(user);
        return totalPoints != null ? totalPoints : 0;
    }
    
    public Map<String, Long> getAchievementCountsByCategory(UserAccount user) {
        List<ProgressRecord> records = progressRepository.findByUserAccountOrderByAchievedDateDesc(user);
        
        return records.stream()
                .collect(Collectors.groupingBy(
                    record -> record.getAchievementCategory().name(),
                    Collectors.counting()
                ));
    }
    
    public List<String> getUserAchievementTypes(UserAccount user) {
        return progressRepository.findDistinctAchievementTypesByUser(user);
    }
    
    public ProgressSummary generateProgressSummary(UserAccount user) {
        List<ProgressRecord> allRecords = getUserProgress(user);
        Integer totalPoints = getTotalPointsForUser(user);
        Map<String, Long> categoryCounts = getAchievementCountsByCategory(user);
        
        ProgressSummary summary = new ProgressSummary();
        summary.setTotalAchievements(allRecords.size());
        summary.setTotalPoints(totalPoints);
        summary.setCategoryCounts(categoryCounts);
        summary.setRecentAchievements(allRecords.stream().limit(5).collect(Collectors.toList()));
        summary.setUserLevel(calculateUserLevel(totalPoints));
        summary.setNextLevelPoints(calculateNextLevelPoints(totalPoints));
        
        return summary;
    }
    
    public void verifyAchievement(Long progressId, String verificationNotes) {
        ProgressRecord record = progressRepository.findById(progressId)
                .orElseThrow(() -> new RuntimeException("Progress record not found"));
        
        record.verifyAchievement(verificationNotes);
        progressRepository.save(record);
        
        logger.info("Achievement verified: Progress ID {} - User {}", progressId, record.getUserAccount().getUsername());
    }
    
    private int calculateAssessmentPoints(AssessmentAttempt attempt) {
        double percentage = attempt.getPercentageScore();
        
        if (percentage >= 95) return 50;
        if (percentage >= 85) return 40;
        if (percentage >= 75) return 30;
        if (percentage >= 65) return 20;
        return 10;
    }
    
    private int calculateMentorshipPoints(String milestoneType) {
        return switch (milestoneType.toLowerCase()) {
            case "mentorship started" -> 25;
            case "session completed" -> 15;
            case "mentorship completed" -> 50;
            default -> 10;
        };
    }
    
    private String calculateUserLevel(Integer totalPoints) {
        if (totalPoints >= 1000) return "Expert";
        if (totalPoints >= 500) return "Advanced";
        if (totalPoints >= 250) return "Intermediate";
        if (totalPoints >= 100) return "Beginner";
        return "Newcomer";
    }
    
    private Integer calculateNextLevelPoints(Integer totalPoints) {
        if (totalPoints < 100) return 100 - totalPoints;
        if (totalPoints < 250) return 250 - totalPoints;
        if (totalPoints < 500) return 500 - totalPoints;
        if (totalPoints < 1000) return 1000 - totalPoints;
        return 0; // Already at max level
    }
    
    // Inner class for progress summary
    public static class ProgressSummary {
        private Integer totalAchievements;
        private Integer totalPoints;
        private Map<String, Long> categoryCounts;
        private List<ProgressRecord> recentAchievements;
        private String userLevel;
        private Integer nextLevelPoints;
        
        // Getters and Setters
        public Integer getTotalAchievements() { return totalAchievements; }
        public void setTotalAchievements(Integer totalAchievements) { this.totalAchievements = totalAchievements; }
        
        public Integer getTotalPoints() { return totalPoints; }
        public void setTotalPoints(Integer totalPoints) { this.totalPoints = totalPoints; }
        
        public Map<String, Long> getCategoryCounts() { return categoryCounts; }
        public void setCategoryCounts(Map<String, Long> categoryCounts) { this.categoryCounts = categoryCounts; }
        
        public List<ProgressRecord> getRecentAchievements() { return recentAchievements; }
        public void setRecentAchievements(List<ProgressRecord> recentAchievements) { this.recentAchievements = recentAchievements; }
        
        public String getUserLevel() { return userLevel; }
        public void setUserLevel(String userLevel) { this.userLevel = userLevel; }
        
        public Integer getNextLevelPoints() { return nextLevelPoints; }
        public void setNextLevelPoints(Integer nextLevelPoints) { this.nextLevelPoints = nextLevelPoints; }
    }
}