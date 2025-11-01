package com.example.employeemanagment.service;

import com.example.employeemanagment.model.Role;
import com.example.employeemanagment.model.Users;
import com.example.employeemanagment.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo repo;

    @Autowired
    private Jwtservice jwtservice;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public void saveUsers(Users user) {
        // Prevent duplicate registration
        Optional<Users> existing = Optional.ofNullable(repo.findByUsername(user.getUsername()));
        if (existing.isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        // Default role to EMPLOYEE if not set
        if (user.getRole() == null) {
            user.setRole(Role.EMPLOYEE);
        }


        user.setPassword(encoder.encode(user.getPassword()));

         repo.save(user);
    }


    public String generateTokenForUser(String username) {
        return jwtservice.generateToken(username);
    }
}
