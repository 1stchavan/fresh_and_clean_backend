package com.FreshandClean.backend.Controler;

import com.FreshandClean.backend.DTOs.ContactRequest;
import com.FreshandClean.backend.Entity.ContactMessage;
import com.FreshandClean.backend.Repository.ContactMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Stream;

@RestController
@CrossOrigin(origins = "https://fresh-and-clean-git-main-prathameshs-projects-4f0b241d.vercel.app", allowCredentials = "true")
public class ContactController {

    @Autowired
    private ContactMessageRepository contactRepo;

    @PostMapping("/api/contact")
    public ResponseEntity<?> contact(@RequestBody ContactRequest request) {
        if (Stream.of(request.name, request.email, request.message).anyMatch(f -> f == null || f.isBlank())) {
            return ResponseEntity.badRequest().body("All fields are required");
        }

        ContactMessage message = new ContactMessage();
        message.setName(request.name);
        message.setEmail(request.email);
        message.setMessage(request.message);
        contactRepo.save(message);

        return ResponseEntity.status(HttpStatus.CREATED).body("Message sent successfully!");
    }
}

