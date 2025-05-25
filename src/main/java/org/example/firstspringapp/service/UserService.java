package org.example.firstspringapp.service;

import org.example.firstspringapp.model.User;
import org.example.firstspringapp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public void registerUser(User user) {
        logger.info("Registering user: {}", user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(false); // Аккаунт неактивен до подтверждения
        user.setConfirmationToken(UUID.randomUUID().toString());
        userRepository.save(user);
        emailService.sendConfirmationEmail(user.getEmail(), user.getConfirmationToken());
        logger.info("User registered, confirmation.html email sent to: {}", user.getEmail());
    }

    public boolean confirmUser(String token) {
        logger.info("Confirming user with token: {}", token);
        return userRepository.findByConfirmationToken(token)
                .map(user -> {
                    user.setEnabled(true);
                    user.setConfirmationToken(null);
                    userRepository.save(user);
                    logger.info("User {} confirmed successfully", user.getUsername());
                    return true;
                })
                .orElseGet(() -> {
                    logger.warn("Invalid confirmation.html token: {}", token);
                    return false;
                });
    }

    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                true, true, true,
                Collections.singletonList(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}