package com.example.lms.service;

import com.example.lms.entity.User;
import com.example.lms.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisService redisService;

    public Optional<User> getUserById(Integer userId) {
        return userRepository.findById(userId);
    }

    @Transactional
    public User updateUser(Integer userId, User userDetails) {
        return userRepository.findById(userId)
                .map(existingUser -> {
                    // Update user fields
                    if (userDetails.getFirstName() != null) {
                        existingUser.setFirstName(userDetails.getFirstName());
                    }
                    if (userDetails.getLastName() != null) {
                        existingUser.setLastName(userDetails.getLastName());
                    }
                    if (userDetails.getRole() != null) {
                        existingUser.setRole(userDetails.getRole());
                    }

                    // Save updated user
                    User savedUser = userRepository.save(existingUser);

                    // Invalidate related caches
                    invalidateUserRelatedCaches(userId);

                    return savedUser;
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }

    private void invalidateUserRelatedCaches(Integer userId) {
        logger.info("Invalidating caches for user ID: {}", userId);

        // Invalidate weekly scores caches that might contain this user
        redisService.deleteByPattern("weekly_scores:*");

        // Invalidate monthly scores caches
        redisService.deleteByPattern("monthly_scores:*");

        // Invalidate attendance caches
        redisService.deleteByPattern("monthly_attendance:*");

        // Invalidate overall progress caches
        redisService.deleteByPattern("overall_progress:*");

        logger.info("Cache invalidation completed for user ID: {}", userId);
    }
}