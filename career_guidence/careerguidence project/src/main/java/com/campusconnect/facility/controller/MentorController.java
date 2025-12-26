package com.campusconnect.facility.controller;

import com.campusconnect.facility.exception.ResourceNotFoundException;
import com.campusconnect.facility.model.*;
import com.campusconnect.facility.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/mentor")
public class MentorController {
    
    @Autowired
    private SkillAssessmentRepository skillAssessmentRepository;
    
    @Autowired
    private MentorshipRequestRepository mentorshipRequestRepository;
    
    @Autowired
    private UserAccountRepository userAccountRepository;
    
    @GetMapping("/requests")
    public String viewRequests(Model model, HttpSession session) {
        try {
            UserAccount currentUser = (UserAccount) session.getAttribute("user");
            if (currentUser == null) {
                return "redirect:/login";
            }
            
            if (!"MENTOR".equals(currentUser.getUserRole().name())) {
                throw new ResourceNotFoundException("Access denied. Only mentors can view requests.");
            }
            
            model.addAttribute("user", currentUser);
            model.addAttribute("requests", java.util.Collections.emptyList());
            model.addAttribute("message", "No pending requests at the moment");
            
            return "mentor-requests";
        } catch (Exception e) {
            throw e;
        }
    }
    
    @GetMapping("/sessions")
    public String viewSessions(Model model, HttpSession session) {
        try {
            UserAccount currentUser = (UserAccount) session.getAttribute("user");
            if (currentUser == null) {
                return "redirect:/login";
            }
            
            if (!"MENTOR".equals(currentUser.getUserRole().name())) {
                throw new ResourceNotFoundException("Access denied. Only mentors can view sessions.");
            }
            
            model.addAttribute("user", currentUser);
            model.addAttribute("sessions", java.util.Collections.emptyList());
            model.addAttribute("message", "No sessions scheduled");
            
            return "mentor-sessions";
        } catch (Exception e) {
            throw e;
        }
    }
    
    @GetMapping("/students")
    public String viewStudents(Model model, HttpSession session) {
        try {
            UserAccount currentUser = (UserAccount) session.getAttribute("user");
            if (currentUser == null) {
                return "redirect:/login";
            }
            
            if (!"MENTOR".equals(currentUser.getUserRole().name())) {
                throw new ResourceNotFoundException("Access denied. Only mentors can view students.");
            }
            
            model.addAttribute("user", currentUser);
            model.addAttribute("students", java.util.Collections.emptyList());
            model.addAttribute("message", "No students assigned yet");
            
            return "mentor-students";
        } catch (Exception e) {
            throw e;
        }
    }
    
    @GetMapping("/assessment/new")
    public String showCreateAssessmentForm(Model model, HttpSession session) {
        try {
            UserAccount currentUser = (UserAccount) session.getAttribute("user");
            if (currentUser == null) {
                return "redirect:/login";
            }
            
            if (!"MENTOR".equals(currentUser.getUserRole().name())) {
                throw new ResourceNotFoundException("Access denied. Only mentors can create assessments.");
            }
            
            model.addAttribute("user", currentUser);
            return "create_assessment";
        } catch (Exception e) {
            throw e;
        }
    }
    
    @PostMapping("/assessment/save")
    public String saveAssessment(@RequestParam String title, @RequestParam String description, 
                               @RequestParam String skillType, @RequestParam Integer maxScore,
                               HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            UserAccount currentUser = (UserAccount) session.getAttribute("user");
            if (currentUser == null) {
                return "redirect:/login";
            }
            
            SkillAssessment assessment = new SkillAssessment();
            assessment.setAssessmentTitle(title);
            assessment.setAssessmentDescription(description);
            assessment.setSkillCategory(skillType);
            assessment.setPassingScore(maxScore.doubleValue());
            assessment.setDurationMinutes(60); // Default duration
            assessment.setDifficultyLevel(DifficultyLevel.INTERMEDIATE); // Default level
            
            skillAssessmentRepository.save(assessment);
            
            redirectAttributes.addFlashAttribute("successMessage", "✅ Assessment created successfully.");
            return "redirect:/dashboard";
        } catch (Exception e) {
            throw e;
        }
    }
    
    @GetMapping("/mentorship/new")
    public String showAssignMentorshipForm(Model model, HttpSession session) {
        try {
            UserAccount currentUser = (UserAccount) session.getAttribute("user");
            if (currentUser == null) {
                return "redirect:/login";
            }
            
            if (!"MENTOR".equals(currentUser.getUserRole().name())) {
                throw new ResourceNotFoundException("Access denied. Only mentors can assign mentorships.");
            }
            
            List<UserAccount> students = userAccountRepository.findByUserRole(UserRole.STUDENT);
            model.addAttribute("user", currentUser);
            model.addAttribute("students", students);
            return "assign_mentorship";
        } catch (Exception e) {
            throw e;
        }
    }
    
    @PostMapping("/mentorship/save")
    public String saveMentorship(@RequestParam Long studentId, @RequestParam String topic,
                               @RequestParam String startDate, @RequestParam String endDate,
                               @RequestParam String description, HttpSession session, 
                               RedirectAttributes redirectAttributes) {
        try {
            UserAccount currentUser = (UserAccount) session.getAttribute("user");
            if (currentUser == null) {
                return "redirect:/login";
            }
            
            UserAccount student = userAccountRepository.findById(studentId).orElse(null);
            if (student == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Student not found.");
                return "redirect:/mentor/mentorship/new";
            }
            
            MentorshipRequest mentorship = new MentorshipRequest();
            mentorship.setStudentUser(student);
            mentorship.setMentorUser(currentUser);
            mentorship.setRequestMessage(topic);
            mentorship.setMentorshipGoals(description);
            mentorship.setRequestStatus(RequestStatus.ACCEPTED);
            mentorship.setMentorshipStartDate(LocalDate.parse(startDate).atStartOfDay());
            mentorship.setMentorshipEndDate(LocalDate.parse(endDate).atStartOfDay());
            mentorship.setResponseDate(LocalDateTime.now());
            
            mentorshipRequestRepository.save(mentorship);
            
            redirectAttributes.addFlashAttribute("successMessage", "✅ Mentorship assigned successfully.");
            return "redirect:/dashboard";
        } catch (Exception e) {
            throw e;
        }
    }
}