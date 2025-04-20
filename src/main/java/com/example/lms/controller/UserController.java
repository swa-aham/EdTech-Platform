package com.example.lms.controller;

import com.example.lms.entity.User;
import com.example.lms.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Integer userId) {
        return userService.getUserById(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Integer userId, @RequestBody User userDetails) {
        long startTime = System.nanoTime();
        logger.info("Received request to update user with ID: {}", userId);

        User updatedUser = userService.updateUser(userId, userDetails);

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;
        logger.info("User update for ID: {} processed in {} ms", userId, durationMs);

        return ResponseEntity.ok(updatedUser);
    }
}