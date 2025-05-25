package org.example.firstspringapp.controller;

import org.example.firstspringapp.model.User;
import org.example.firstspringapp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;

@Controller
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginForm() {
        logger.info("Displaying login page");
        return "login";
    }

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        logger.info("Displaying signup page");
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        logger.info("Processing signup for user: {}", user.getUsername());
        if (result.hasErrors()) {
            logger.warn("Validation errors during signup: {}", result.getAllErrors());
            return "signup";
        }
        if (userService.usernameExists(user.getUsername())) {
            model.addAttribute("usernameError", "Username already exists");
            return "signup";
        }
        if (userService.emailExists(user.getEmail())) {
            model.addAttribute("emailError", "Email already exists");
            return "signup";
        }
        userService.registerUser(user);
        model.addAttribute("message", "Please check your email to confirm your registration.");
        return "confirmation.html";
    }

    @GetMapping("/confirm")
    public String confirmUser(@RequestParam("token") String token, Model model) {
        logger.info("Processing confirmation.html with token: {}", token);
        boolean confirmed = userService.confirmUser(token);
        if (confirmed) {
            model.addAttribute("message", "Your account has been confirmed. You can now log in.");
        } else {
            model.addAttribute("message", "Invalid or expired confirmation.html token.");
        }
        return "confirmation.html";
    }

    @GetMapping("/home")
    public String home() {
        logger.info("Displaying home page after login");
        return "home";
    }
}