package com.example.employeemanagment.controllers;

import com.example.employeemanagment.model.LeaveRequestDTO;
import com.example.employeemanagment.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/leave")
public class LeaveController {

    @Autowired
    private LeaveService service;

    @PreAuthorize("hasAnyRole('EMPLOYEE')")
    @PostMapping("/apply")
    public ResponseEntity<?> applyLeave(@RequestBody LeaveRequestDTO DTO){
        return ResponseEntity.ok(service.applyLeave(DTO));
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/employee/{EmployeeId}")
    public ResponseEntity<?> getLeavesByEmployee(@PathVariable long EmployeeId){
        return ResponseEntity.ok(service.getLeavesByEmployee(EmployeeId));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approveLeave(@PathVariable Long id){
          return ResponseEntity.ok(service.approveLeave(id));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/{id}/reject")
    public ResponseEntity<?> rejectLeave(@PathVariable Long id){
        return ResponseEntity.ok(service.rejectLeave(id));
    }
}
