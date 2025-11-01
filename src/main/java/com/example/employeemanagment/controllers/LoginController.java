package com.example.employeemanagment.controllers;

import com.example.employeemanagment.model.EmployeeWrapper;
import com.example.employeemanagment.model.Role;
import com.example.employeemanagment.model.Users;
import com.example.employeemanagment.service.EmployeeService;
import com.example.employeemanagment.service.Jwtservice;
import com.example.employeemanagment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private UserService service;

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private Jwtservice jwtservice;

    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Users user) {

        if (user.getRole() == null) {
            user.setRole(Role.EMPLOYEE);
        }

        if (user.getRole() != Role.HR && user.getRole() != Role.MANAGER && user.getRole() != Role.EMPLOYEE) {
            return ResponseEntity.badRequest().body("Invalid role. Allowed roles: HR, MANAGER, EMPLOYEE.");
        }

        service.saveUsers(user);

        return ResponseEntity.ok(user.getRole() + " registration successful!");
    }



    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Users user) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );

        if (authentication.isAuthenticated()) {
            String token = jwtservice.generateToken(user.getUsername());
            return ResponseEntity.ok("Bearer " + token);
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
}
