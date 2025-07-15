package com.FreshandClean.backend.Controler;

import com.FreshandClean.backend.DTOs.BookPickupRequest;
import com.FreshandClean.backend.Entity.Booking;
import com.FreshandClean.backend.Repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "https://fresh-and-clean-git-main-prathameshs-projects-4f0b241d.vercel.app", allowCredentials = "true")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepo;

    @PostMapping("/book-pickup")
    public ResponseEntity<?> bookPickup(@RequestBody BookPickupRequest request) {

        if (request.getUserEmail() == null || request.getItems() == null || request.getItems().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Invalid request: Email or items missing"
            ));
        }

        List<Booking> bookings = request.getItems().stream().map(item -> {
            Booking b = new Booking();
            b.setUserEmail(request.getUserEmail());
            b.setItemName(item.getItemName());
            b.setPrice(item.getPrice());
            b.setQuantity(item.getQuantity());
            return b;
        }).collect(Collectors.toList());

        bookingRepo.saveAll(bookings);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Booking stored successfully"
        ));
    }
    @GetMapping("/book-pickup")
    public ResponseEntity<?> getBookings() {
        try {
            // Fetch all bookings ordered by creation date in descending order
            List<Booking> bookings = bookingRepo.findAllByOrderByCreatedAtDesc();

            // Return the bookings in the response body
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "bookings", bookings
            ));
        } catch (Exception e) {
            // Handle any potential errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Failed to retrieve bookings",
                    "error", e.getMessage()
            ));
        }
    }

}
