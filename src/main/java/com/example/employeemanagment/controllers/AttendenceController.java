package com.example.employeemanagment.controllers;

import com.example.employeemanagment.service.AttendenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Attendence")
public class AttendenceController {

    @Autowired
    private AttendenceService service;
    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping("/check-In/{employeeId}")
    public ResponseEntity<?> checkIn(@PathVariable long employeeId){
        return ResponseEntity.ok(service.checkIn(employeeId));
    }
    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping("/check-Out/{employeeId}")
    public ResponseEntity<?> checkOut(@PathVariable long employeeId){
        return ResponseEntity.ok(service.checkOut(employeeId));
    }



}
