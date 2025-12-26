package com.campusconnect.facility.controller;

import com.campusconnect.facility.dto.LoginRequestDto;
import com.campusconnect.facility.dto.UserRegistrationDto;
import com.campusconnect.facility.model.UserAccount;
import com.campusconnect.facility.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
public class WebController {
    
    @Autowired
    private UserAccountService userService;
    
    @GetMapping("/welcome")
    public String welcome() {
        return "welcome";
    }
    
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("userRegistration", new UserRegistrationDto());
        return "register";
    }
    
    @PostMapping("/register")
    public String processRegistration(@Valid @ModelAttribute("userRegistration") UserRegistrationDto registrationDto,
                                    BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "register";
        }
        
        try {
            userService.registerNewUser(registrationDto);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Registration successful! Please login with your credentials.");
            return "redirect:/login";
            
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        UserAccount currentUser = (UserAccount) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }
        
        if (currentUser.getUserRole().name().equals("STUDENT")) {
            return studentDashboard(model, currentUser);
        } else if (currentUser.getUserRole().name().equals("MENTOR")) {
            return mentorDashboard(model, currentUser);
        } else if (currentUser.getUserRole().name().equals("ADMIN")) {
            return adminDashboard(model, currentUser);
        }
        
        return "redirect:/login";
    }
    
    private String studentDashboard(Model model, UserAccount student) {
        model.addAttribute("user", student);
        model.addAttribute("recentAssessments", java.util.Collections.emptyList());
        model.addAttribute("recommendations", java.util.Collections.emptyList());
        model.addAttribute("activeMentorships", java.util.Collections.emptyList());
        model.addAttribute("upcomingSessions", java.util.Collections.emptyList());
        return "student-dashboard";
    }
    
    private String mentorDashboard(Model model, UserAccount mentor) {
        model.addAttribute("user", mentor);
        model.addAttribute("pendingRequests", java.util.Collections.emptyList());
        model.addAttribute("activeMentorships", java.util.Collections.emptyList());
        model.addAttribute("upcomingSessions", java.util.Collections.emptyList());
        return "mentor-dashboard";
    }
    
    private String adminDashboard(Model model, UserAccount admin) {
        model.addAttribute("user", admin);
        model.addAttribute("totalStudents", 0L);
        model.addAttribute("totalMentors", 0L);
        return "admin-dashboard";
    }
    

    

}