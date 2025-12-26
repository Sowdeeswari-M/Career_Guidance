package com.campusconnect.facility.controller;

import com.campusconnect.facility.model.UserAccount;
import com.campusconnect.facility.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class AuthController {
    
    @Autowired
    private UserAccountRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid email or password");
        }
        return "login";
    }
    
    @PostMapping("/login")
    public String processLogin(@RequestParam String username, 
                              @RequestParam String password, 
                              HttpSession session, 
                              Model model) {
        
        System.out.println("🔐 Login attempt: " + username);
        
        Optional<UserAccount> userOpt = userRepository.findByEmailAddress(username);
        
        if (userOpt.isEmpty()) {
            System.out.println("❌ User not found: " + username);
            model.addAttribute("errorMessage", "Invalid email or password");
            return "login";
        }
        
        UserAccount user = userOpt.get();
        boolean passwordMatches = passwordEncoder.matches(password, user.getPasswordHash());
        
        System.out.println("🔍 Password check for " + user.getEmailAddress() + 
                          " | Role: " + user.getUserRole() + 
                          " | Matches: " + passwordMatches);
        
        if (!passwordMatches) {
            model.addAttribute("errorMessage", "Invalid email or password");
            return "login";
        }
        
        // Store user in session
        session.setAttribute("user", user);
        
        // Redirect to dashboard (role-based logic handled there)
        return "redirect:/dashboard";
    }
    
    @GetMapping("/student/dashboard")
    public String studentDashboard(HttpSession session, Model model) {
        UserAccount user = (UserAccount) session.getAttribute("user");
        if (user == null || !user.getUserRole().name().equals("STUDENT")) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "dashboard";
    }
    
    @GetMapping("/mentor/dashboard")
    public String mentorDashboard(HttpSession session, Model model) {
        UserAccount user = (UserAccount) session.getAttribute("user");
        if (user == null || !user.getUserRole().name().equals("MENTOR")) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "dashboard";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout=true";
    }
}