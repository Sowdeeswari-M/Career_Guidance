package com.campusconnect.facility.service;

import com.campusconnect.facility.model.*;
import com.campusconnect.facility.repository.CareerRecommendationRepository;
import com.campusconnect.facility.repository.AssessmentAttemptRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class RecommendationService {
    
    private static final Logger logger = LoggerFactory.getLogger(RecommendationService.class);
    
    @Autowired
    private CareerRecommendationRepository recommendationRepository;
    
    @Autowired
    private AssessmentAttemptRepository attemptRepository;
    
    // Career path mapping based on skill categories and scores
    private static final Map<String, List<CareerPathInfo>> CAREER_MAPPINGS = new HashMap<>();
    
    static {
        CAREER_MAPPINGS.put("Programming", Arrays.asList(
            new CareerPathInfo("Software Developer", "Design and develop software applications", 
                             "Java, Python, JavaScript, Problem Solving", "$70,000 - $120,000", "High Growth"),
            new CareerPathInfo("Full Stack Developer", "Work on both frontend and backend development", 
                             "React, Node.js, Databases, APIs", "$75,000 - $130,000", "Very High Growth"),
            new CareerPathInfo("DevOps Engineer", "Manage deployment and infrastructure", 
                             "Docker, Kubernetes, AWS, CI/CD", "$80,000 - $140,000", "High Growth")
        ));
        
        CAREER_MAPPINGS.put("Data Science", Arrays.asList(
            new CareerPathInfo("Data Scientist", "Analyze data to extract business insights", 
                             "Python, R, Machine Learning, Statistics", "$85,000 - $150,000", "Very High Growth"),
            new CareerPathInfo("Data Analyst", "Interpret data and create reports", 
                             "SQL, Excel, Tableau, Statistics", "$60,000 - $100,000", "High Growth"),
            new CareerPathInfo("Machine Learning Engineer", "Build and deploy ML models", 
                             "Python, TensorFlow, AWS, MLOps", "$90,000 - $160,000", "Extremely High Growth")
        ));
        
        CAREER_MAPPINGS.put("Business", Arrays.asList(
            new CareerPathInfo("Business Analyst", "Analyze business processes and requirements", 
                             "Requirements Analysis, Process Modeling, Communication", "$65,000 - $110,000", "Steady Growth"),
            new CareerPathInfo("Project Manager", "Lead and coordinate project teams", 
                             "Project Management, Leadership, Agile", "$70,000 - $120,000", "High Growth"),
            new CareerPathInfo("Product Manager", "Define product strategy and roadmap", 
                             "Product Strategy, Market Research, Analytics", "$80,000 - $140,000", "High Growth")
        ));
        
        CAREER_MAPPINGS.put("Design", Arrays.asList(
            new CareerPathInfo("UX Designer", "Design user experiences for digital products", 
                             "User Research, Prototyping, Figma, Usability", "$65,000 - $115,000", "High Growth"),
            new CareerPathInfo("UI Designer", "Create visual interfaces for applications", 
                             "Visual Design, Adobe Creative Suite, CSS", "$60,000 - $105,000", "Moderate Growth"),
            new CareerPathInfo("Product Designer", "End-to-end product design and strategy", 
                             "Design Thinking, Prototyping, User Research", "$75,000 - $130,000", "High Growth")
        ));
    }
    
    @Cacheable(value = "recommendations", key = "#user.userId")
    public List<CareerRecommendation> generateRecommendationsForUser(UserAccount user) {
        logger.info("Generating career recommendations for user: {}", user.getUsername());
        
        // Get user's completed assessments
        List<AssessmentAttempt> completedAttempts = attemptRepository.findCompletedAttemptsByUser(user);
        
        if (completedAttempts.isEmpty()) {
            return generateDefaultRecommendations(user);
        }
        
        // Calculate skill scores by category
        Map<String, Double> skillScores = calculateSkillScores(completedAttempts);
        
        // Generate recommendations based on scores
        List<CareerRecommendation> recommendations = new ArrayList<>();
        
        for (Map.Entry<String, Double> entry : skillScores.entrySet()) {
            String skillCategory = entry.getKey();
            Double score = entry.getValue();
            
            if (CAREER_MAPPINGS.containsKey(skillCategory)) {
                List<CareerPathInfo> careerPaths = CAREER_MAPPINGS.get(skillCategory);
                
                for (CareerPathInfo careerInfo : careerPaths) {
                    double matchPercentage = calculateMatchPercentage(score, user, careerInfo);
                    
                    if (matchPercentage >= 60.0) { // Only recommend if match is above 60%
                        CareerRecommendation recommendation = createRecommendation(user, careerInfo, matchPercentage, skillCategory);
                        recommendations.add(recommendation);
                    }
                }
            }
        }
        
        // Sort by match percentage and limit to top 5
        recommendations.sort((r1, r2) -> Double.compare(r2.getMatchPercentage(), r1.getMatchPercentage()));
        List<CareerRecommendation> topRecommendations = recommendations.subList(0, Math.min(5, recommendations.size()));
        
        // Save recommendations
        List<CareerRecommendation> savedRecommendations = new ArrayList<>();
        for (CareerRecommendation rec : topRecommendations) {
            savedRecommendations.add(recommendationRepository.save(rec));
        }
        
        logger.info("Generated {} recommendations for user: {}", savedRecommendations.size(), user.getUsername());
        return savedRecommendations;
    }
    
    private Map<String, Double> calculateSkillScores(List<AssessmentAttempt> attempts) {
        Map<String, List<Double>> categoryScores = new HashMap<>();
        
        for (AssessmentAttempt attempt : attempts) {
            String category = attempt.getSkillAssessment().getSkillCategory();
            Double score = attempt.getPercentageScore();
            
            categoryScores.computeIfAbsent(category, k -> new ArrayList<>()).add(score);
        }
        
        // Calculate average scores for each category
        Map<String, Double> avgScores = new HashMap<>();
        for (Map.Entry<String, List<Double>> entry : categoryScores.entrySet()) {
            double average = entry.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            avgScores.put(entry.getKey(), average);
        }
        
        return avgScores;
    }
    
    private double calculateMatchPercentage(Double skillScore, UserAccount user, CareerPathInfo careerInfo) {
        double baseMatch = Math.min(skillScore, 100.0);
        
        // Adjust based on user interests
        if (user.getCareerInterests() != null && 
            user.getCareerInterests().toLowerCase().contains(careerInfo.getCareerPath().toLowerCase())) {
            baseMatch += 10.0; // Boost if matches interests
        }
        
        // Adjust based on education level
        if (user.getCurrentEducationLevel() != null) {
            if (user.getCurrentEducationLevel().contains("Bachelor") || 
                user.getCurrentEducationLevel().contains("Master")) {
                baseMatch += 5.0; // Slight boost for higher education
            }
        }
        
        return Math.min(baseMatch, 100.0);
    }
    
    private CareerRecommendation createRecommendation(UserAccount user, CareerPathInfo careerInfo, 
                                                    double matchPercentage, String skillCategory) {
        CareerRecommendation recommendation = new CareerRecommendation(user, careerInfo.getCareerPath(), matchPercentage);
        recommendation.setCareerDescription(careerInfo.getDescription());
        recommendation.setRequiredSkills(careerInfo.getRequiredSkills());
        recommendation.setSalaryRange(careerInfo.getSalaryRange());
        recommendation.setIndustryOutlook(careerInfo.getIndustryOutlook());
        recommendation.setRecommendedCourses(generateRecommendedCourses(skillCategory));
        recommendation.setRecommendationReason(generateRecommendationReason(matchPercentage, skillCategory));
        
        return recommendation;
    }
    
    private String generateRecommendedCourses(String skillCategory) {
        Map<String, String> courseMappings = Map.of(
            "Programming", "Advanced Java Programming, Spring Boot Masterclass, React Development",
            "Data Science", "Python for Data Science, Machine Learning Fundamentals, Statistics for Data Analysis",
            "Business", "Business Analysis Certification, Agile Project Management, Strategic Planning",
            "Design", "UX Design Principles, Advanced Figma, User Research Methods"
        );
        
        return courseMappings.getOrDefault(skillCategory, "Professional Development Courses");
    }
    
    private String generateRecommendationReason(double matchPercentage, String skillCategory) {
        if (matchPercentage >= 90) {
            return "Excellent match based on your outstanding performance in " + skillCategory + " assessments.";
        } else if (matchPercentage >= 80) {
            return "Strong match based on your solid skills in " + skillCategory + ".";
        } else if (matchPercentage >= 70) {
            return "Good match with potential for growth in " + skillCategory + ".";
        } else {
            return "Moderate match - consider additional training in " + skillCategory + ".";
        }
    }
    
    private List<CareerRecommendation> generateDefaultRecommendations(UserAccount user) {
        List<CareerRecommendation> defaultRecs = new ArrayList<>();
        
        // Generate basic recommendations based on user interests or field of study
        String interests = user.getCareerInterests();
        String fieldOfStudy = user.getFieldOfStudy();
        
        if (interests != null || fieldOfStudy != null) {
            String searchTerm = interests != null ? interests : fieldOfStudy;
            
            if (searchTerm.toLowerCase().contains("computer") || searchTerm.toLowerCase().contains("software")) {
                CareerRecommendation rec = new CareerRecommendation(user, "Software Developer", 75.0);
                rec.setCareerDescription("Entry-level software development position");
                rec.setRecommendationReason("Based on your field of study and interests");
                defaultRecs.add(recommendationRepository.save(rec));
            }
        }
        
        return defaultRecs;
    }
    
    public List<CareerRecommendation> getActiveRecommendationsForUser(UserAccount user) {
        return recommendationRepository.findActiveRecommendationsForUser(user);
    }
    
    public void dismissRecommendation(Long recommendationId, UserAccount user) {
        CareerRecommendation recommendation = recommendationRepository.findById(recommendationId)
                .orElseThrow(() -> new RuntimeException("Recommendation not found"));
        
        if (!recommendation.getStudentUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("Unauthorized access to recommendation");
        }
        
        recommendation.setRecommendationStatus(RecommendationStatus.DISMISSED);
        recommendation.setLastUpdatedDate(LocalDateTime.now());
        recommendationRepository.save(recommendation);
    }
    
    public List<String> getAllCareerPaths() {
        return recommendationRepository.findDistinctCareerPaths();
    }
    
    // Inner class for career path information
    private static class CareerPathInfo {
        private final String careerPath;
        private final String description;
        private final String requiredSkills;
        private final String salaryRange;
        private final String industryOutlook;
        
        public CareerPathInfo(String careerPath, String description, String requiredSkills, 
                            String salaryRange, String industryOutlook) {
            this.careerPath = careerPath;
            this.description = description;
            this.requiredSkills = requiredSkills;
            this.salaryRange = salaryRange;
            this.industryOutlook = industryOutlook;
        }
        
        // Getters
        public String getCareerPath() { return careerPath; }
        public String getDescription() { return description; }
        public String getRequiredSkills() { return requiredSkills; }
        public String getSalaryRange() { return salaryRange; }
        public String getIndustryOutlook() { return industryOutlook; }
    }
}