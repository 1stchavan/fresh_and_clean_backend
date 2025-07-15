package com.FreshandClean.backend.Controler;

import com.FreshandClean.backend.DTOs.LoginRequest;
import com.FreshandClean.backend.DTOs.RegisterRequest;
import com.FreshandClean.backend.Entity.User;
import com.FreshandClean.backend.Repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "https://fresh-and-clean-git-main-prathameshs-projects-4f0b241d.vercel.app", allowCredentials = "true")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        if (Stream.of(request.getFullName(), request.getEmail(), request.getPassword(), request.getContactNumber(), request.getAddress())
                .anyMatch(field -> field == null || field.isBlank())) {
            return ResponseEntity.badRequest().body("All fields are required");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already registered");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); // Note: Use password hashing in production!
        user.setContactNumber(request.getContactNumber());
        user.setAddress(request.getAddress());

        userRepository.save(user);
        return ResponseEntity.ok("Account created Successfully");
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request, HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        if ("admin@dryclean.com".equals(request.email)) {
            session.setAttribute("user", Map.of("email", request.email, "role", "admin"));
            response.put("message", "Admin login successful");
            response.put("user", session.getAttribute("user"));
            return ResponseEntity.ok(response);
        }

        return userRepository.findByEmail(request.email)
                .filter(u -> u.getPassword().equals(request.password))
                .map(user -> {
                    session.setAttribute("user", Map.of("id", user.getId(), "email", user.getEmail(), "role", "user"));
                    response.put("message", "Login successful");
                    response.put("user", session.getAttribute("user"));
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    response.put("message", "Invalid email or password");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
                });
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logged out successfully");
    }

    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> check(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        Object user = session.getAttribute("user");

        if (user != null) {
            response.put("loggedIn", true);
            response.put("user", user); // Return the user info if logged in
        } else {
            response.put("loggedIn", false);
            response.put("user", null);
            response.put("message", "No user logged in"); // Optionally provide a message if no user
        }

        return ResponseEntity.ok(response);
    }

}

