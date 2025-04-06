package com.example.tailgate.tailgate_backend.controller;

import com.example.tailgate.tailgate_backend.model.Tailgate;
import com.example.tailgate.tailgate_backend.repository.TailgateRepository;
import com.example.tailgate.tailgate_backend.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;
import java.time.LocalDate;
import java.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.web.util.UriUtils;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/tailgates")
public class TailgateController {

    private static final Logger logger = LoggerFactory.getLogger(TailgateController.class);

    @Autowired
    private TailgateRepository tailgateRepository;

    @Autowired
    private NotificationService notificationService;

    // POST request to create a new tailgate
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createTailgatePost(@RequestBody Tailgate tailgate) {
        try {
            if (tailgate.getTailgateName() == null || tailgate.getTailgateName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("{\"error\":\"Tailgate name is required\"}");
            }

            if (tailgate.getDate() == null) {
                tailgate.setDate(LocalDate.now());
            }
            if (tailgate.getStartTime() == null) {
                tailgate.setStartTime(LocalTime.of(10, 0));
            }
            if (tailgate.getEndTime() == null) {
                tailgate.setEndTime(LocalTime.of(16, 0));
            }
            if (tailgate.getMaxAttendees() == null) {
                tailgate.setMaxAttendees(50);
            }
            if (tailgate.getCurrentAttendees() == null) {
                tailgate.setCurrentAttendees(0);
            }

            Tailgate savedTailgate = tailgateRepository.save(tailgate);

            // 🔔 Send notification
//            Notification createNotif = new Notification(
//                    savedTailgate.getOwnerId(), // recipientId
//                    "user",
//                    savedTailgate.getOwnerId(), // senderId (could be system or owner)
//                    "tailgate_created",
//                    "Tailgate created: " + savedTailgate.getTailgateName(),
//                    savedTailgate.getId()
//            );
//            notificationService.send(createNotif);

            return ResponseEntity.ok(savedTailgate);

        } catch (Exception e) {
            logger.error("Error creating tailgate", e);
            return ResponseEntity.internalServerError().body("{\"error\":\"Failed to create tailgate: " + e.getMessage() + "\"}");
        }
    }

    // GET all tailgates
    @GetMapping
    public ResponseEntity<List<Tailgate>> getAllTailgates() {
        List<Tailgate> tailgates = tailgateRepository.findAll();
        return ResponseEntity.ok(tailgates);
    }

    // GET tailgate by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getTailgateById(@PathVariable int id) {
        Optional<Tailgate> tailgate = tailgateRepository.findById(id);
        return tailgate.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body((Tailgate) Map.of("error", "Tailgate not found")));
    }

    // GET tailgate by tailgateName
    @GetMapping("/name/{tailgateName}")
    public ResponseEntity<?> getTailgateByName(@PathVariable String tailgateName) {
        // Decode URL-encoded tailgate names (handling spaces)
        String decodedName = UriUtils.decode(tailgateName, StandardCharsets.UTF_8);

        // Fetch tailgate by name
        Optional<Tailgate> tailgate = tailgateRepository.findByTailgateNameIgnoreCase(decodedName);

        return tailgate.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body((Tailgate) Map.of("error", "Tailgate not found")));
    }

    // 4. DELETE tailgate by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTailgateById(@PathVariable int id) {
        Optional<Tailgate> tailgate = tailgateRepository.findById(id);

        if (tailgate.isPresent()) {
            tailgateRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Tailgate deleted successfully"));
        } else {
            return ResponseEntity.status(404).body(Map.of("error", "Tailgate not found"));
        }
    }

    // 5. UPDATE (Edit) an existing tailgate by ID
    // 5. UPDATE (Edit) an existing tailgate by ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTailgate(@PathVariable int id, @RequestBody Tailgate updatedTailgate) {
        Optional<Tailgate> existingTailgate = tailgateRepository.findById(id);

        if (existingTailgate.isPresent()) {
            Tailgate tailgate = existingTailgate.get();

            if (updatedTailgate.getTailgateName() != null) {
                tailgate.setTailgateName(updatedTailgate.getTailgateName());
            }
            if (updatedTailgate.getLocation() != null) {
                tailgate.setLocation(updatedTailgate.getLocation());
            }
            if (updatedTailgate.getDate() != null) {
                tailgate.setDate(updatedTailgate.getDate());
            }
            if (updatedTailgate.getDescription() != null) {
                tailgate.setDescription(updatedTailgate.getDescription());
            }
            if (updatedTailgate.getStartTime() != null) {
                tailgate.setStartTime(updatedTailgate.getStartTime());
            }
            if (updatedTailgate.getEndTime() != null) {
                tailgate.setEndTime(updatedTailgate.getEndTime());
            }
            if (updatedTailgate.getMaxAttendees() != null) {
                tailgate.setMaxAttendees(updatedTailgate.getMaxAttendees());
            }
            if (updatedTailgate.getCurrentAttendees() != null) {
                tailgate.setCurrentAttendees(updatedTailgate.getCurrentAttendees());
            }
            tailgate.setFoodProvided(updatedTailgate.isFoodProvided());
            tailgate.setCanEdit(updatedTailgate.isCanEdit());
            tailgate.setPublic(updatedTailgate.isPublic());

            Tailgate savedTailgate = tailgateRepository.save(tailgate);

            // 🔔 Send update notification
//            Notification updateNotif = new Notification(
//                    savedTailgate.getOwnerId(),
//                    "user",
//                    savedTailgate.getOwnerId(),
//                    "tailgate_updated",
//                    "Tailgate updated: " + savedTailgate.getTailgateName(),
//                    savedTailgate.getId()
//            );
//            notificationService.send(updateNotif); // 🔔

            return ResponseEntity.ok(savedTailgate);
        } else {
            return ResponseEntity.status(404).body(Map.of("error", "Tailgate not found"));
        }

    }

}


