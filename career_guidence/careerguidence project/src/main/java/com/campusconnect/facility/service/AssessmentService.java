package com.campusconnect.facility.service;

import com.campusconnect.facility.dto.AssessmentSubmissionDto;
import com.campusconnect.facility.model.*;
import com.campusconnect.facility.repository.*;
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
public class AssessmentService {
    
    private static final Logger logger = LoggerFactory.getLogger(AssessmentService.class);
    
    @Autowired
    private SkillAssessmentRepository assessmentRepository;
    
    @Autowired
    private AssessmentAttemptRepository attemptRepository;
    
    @Autowired
    private AssessmentQuestionRepository questionRepository;
    
    @Autowired
    private StudentAnswerRepository answerRepository;
    
    @Autowired
    private QuestionOptionRepository optionRepository;
    
    @Autowired
    private ProgressRecordService progressService;
    
    public List<SkillAssessment> getAllActiveAssessments() {
        return assessmentRepository.findByIsActiveTrueOrderByCreatedDateDesc();
    }
    
    public Page<SkillAssessment> getActiveAssessments(Pageable pageable) {
        return assessmentRepository.findByIsActiveTrue(pageable);
    }
    
    public Optional<SkillAssessment> findAssessmentById(Long assessmentId) {
        return assessmentRepository.findById(assessmentId);
    }
    
    public List<SkillAssessment> getAssessmentsByCategory(String category) {
        return assessmentRepository.findBySkillCategoryAndIsActiveTrue(category);
    }
    
    public List<String> getAllSkillCategories() {
        return assessmentRepository.findDistinctSkillCategories();
    }
    
    public AssessmentAttempt startAssessment(Long assessmentId, UserAccount student) {
        SkillAssessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new RuntimeException("Assessment not found"));
        
        // Check if there's already an in-progress attempt
        Optional<AssessmentAttempt> existingAttempt = attemptRepository
                .findByStudentUserAndSkillAssessmentAndAttemptStatus(student, assessment, "IN_PROGRESS");
        
        if (existingAttempt.isPresent()) {
            return existingAttempt.get();
        }
        
        // Calculate total possible score
        Double totalScore = assessment.getAssessmentQuestions().stream()
                .mapToDouble(q -> q.getPointsValue().doubleValue())
                .sum();
        
        AssessmentAttempt newAttempt = new AssessmentAttempt(student, assessment, totalScore);
        AssessmentAttempt savedAttempt = attemptRepository.save(newAttempt);
        
        logger.info("Assessment attempt started: User {} - Assessment {}", 
                   student.getUsername(), assessment.getAssessmentTitle());
        
        return savedAttempt;
    }
    
    public AssessmentAttempt submitAssessment(AssessmentSubmissionDto submissionDto, UserAccount student) {
        AssessmentAttempt attempt = attemptRepository.findById(submissionDto.getAttemptId())
                .orElseThrow(() -> new RuntimeException("Assessment attempt not found"));
        
        if (!attempt.getStudentUser().getUserId().equals(student.getUserId())) {
            throw new RuntimeException("Unauthorized access to assessment attempt");
        }
        
        if (!"IN_PROGRESS".equals(attempt.getAttemptStatus().name())) {
            throw new RuntimeException("Assessment attempt is not in progress");
        }
        
        double totalScore = 0.0;
        
        // Process each submitted answer
        for (AssessmentSubmissionDto.AnswerSubmissionDto answerDto : submissionDto.getSubmittedAnswers()) {
            AssessmentQuestion question = questionRepository.findById(answerDto.getQuestionId())
                    .orElseThrow(() -> new RuntimeException("Question not found"));
            
            StudentAnswer studentAnswer = new StudentAnswer(attempt, question);
            
            if (answerDto.getSelectedOptionId() != null) {
                QuestionOption selectedOption = optionRepository.findById(answerDto.getSelectedOptionId())
                        .orElseThrow(() -> new RuntimeException("Option not found"));
                studentAnswer.setSelectedOption(selectedOption);
            }
            
            if (answerDto.getTextAnswer() != null) {
                studentAnswer.setTextAnswer(answerDto.getTextAnswer());
            }
            
            studentAnswer.evaluateAnswer();
            totalScore += studentAnswer.getPointsEarned();
            
            answerRepository.save(studentAnswer);
        }
        
        // Complete the attempt
        attempt.setAchievedScore(totalScore);
        attempt.completeAttempt();
        
        AssessmentAttempt completedAttempt = attemptRepository.save(attempt);
        
        // Record progress if passed
        if (completedAttempt.getIsPassed()) {
            progressService.recordAssessmentCompletion(student, completedAttempt);
        }
        
        logger.info("Assessment completed: User {} - Score: {}/{}", 
                   student.getUsername(), totalScore, attempt.getTotalPossibleScore());
        
        return completedAttempt;
    }
    
    public List<AssessmentAttempt> getUserAttempts(UserAccount user) {
        return attemptRepository.findByStudentUserOrderByStartTimeDesc(user);
    }
    
    public List<AssessmentAttempt> getCompletedAttempts(UserAccount user) {
        return attemptRepository.findCompletedAttemptsByUser(user);
    }
    
    public List<AssessmentAttempt> getPassedAttempts(UserAccount user) {
        return attemptRepository.findPassedAttemptsByUser(user);
    }
    
    public Double getUserAverageScore(UserAccount user) {
        Double avgScore = attemptRepository.findAverageScoreByUser(user);
        return avgScore != null ? avgScore : 0.0;
    }
    
    public Long getUserCompletedCount(UserAccount user) {
        return attemptRepository.countCompletedAttemptsByUser(user);
    }
    
    public void expireOldAttempts() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusHours(2); // 2 hours timeout
        List<AssessmentAttempt> expiredAttempts = attemptRepository.findExpiredAttempts(cutoffTime);
        
        for (AssessmentAttempt attempt : expiredAttempts) {
            attempt.setAttemptStatus(com.campusconnect.facility.model.AttemptStatus.EXPIRED);
            attemptRepository.save(attempt);
        }
        
        if (!expiredAttempts.isEmpty()) {
            logger.info("Expired {} assessment attempts", expiredAttempts.size());
        }
    }
    
    public Page<SkillAssessment> searchAssessments(String searchTerm, Pageable pageable) {
        return assessmentRepository.searchAssessments(searchTerm, pageable);
    }
}