package com.campusconnect.facility.controller;

import com.campusconnect.facility.model.UserAccount;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class SimpleWebController {
    
    @GetMapping("/error")
    public String handleGenericError(Model model) {
        model.addAttribute("errorMessage", "Oops! Something went wrong. Please try again.");
        return "error_page";
    }
    
    @GetMapping("/assessment-form")
    public String assessmentForm(Model model, HttpSession session) {
        UserAccount currentUser = (UserAccount) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", currentUser);
        model.addAttribute("message", "Assessment feature coming soon!");
        return "simple-page";
    }
    
    @GetMapping("/career-paths")
    public String careerPaths(Model model, HttpSession session) {
        UserAccount currentUser = (UserAccount) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", currentUser);
        model.addAttribute("message", "Career paths feature coming soon!");
        return "simple-page";
    }
    
    @GetMapping("/mentor-request")
    public String mentorRequest(Model model, HttpSession session) {
        UserAccount currentUser = (UserAccount) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", currentUser);
        model.addAttribute("message", "Mentor request feature coming soon!");
        return "simple-page";
    }
    
    @GetMapping("/progress-summary")
    public String progressSummary(Model model, HttpSession session) {
        UserAccount currentUser = (UserAccount) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", currentUser);
        model.addAttribute("message", "Progress summary feature coming soon!");
        return "simple-page";
    }
    
    @GetMapping("/profile")
    public String profile(Model model, HttpSession session) {
        UserAccount currentUser = (UserAccount) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", currentUser);
        model.addAttribute("message", "Profile management feature coming soon!");
        return "simple-page";
    }
}