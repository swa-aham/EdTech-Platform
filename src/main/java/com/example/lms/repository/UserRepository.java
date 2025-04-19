// src/main/java/com/example/lms/repository/UserRepository.java
package com.example.lms.repository;

import com.example.lms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findByRole(User.Role role);
}
