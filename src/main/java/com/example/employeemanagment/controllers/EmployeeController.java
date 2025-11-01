package com.example.employeemanagment.controllers;

import com.example.employeemanagment.model.EmployeeWrapper;
import com.example.employeemanagment.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Employee")
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeWrapper> getEmployee(@PathVariable long id){

         return new ResponseEntity<>(service.getEmployee(id),HttpStatus.OK);

    }
    @PreAuthorize("hasAnyRole('HR', 'MANAGER')")
    @GetMapping
    public ResponseEntity<List<EmployeeWrapper>> getAllEmployee(){
        List<EmployeeWrapper> wrappers = service.getAllEmployee();
        return new ResponseEntity<>(wrappers,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('HR')")
    @PostMapping
    public ResponseEntity<String> createEmployee(@RequestBody EmployeeWrapper employee){
        service.createEmployee(employee);
        return new ResponseEntity<>("Success",HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('HR')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable Long id){
        service.updateEmployee(id);
        return new ResponseEntity<>("Success",HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('HR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable long id){
        service.deleteEmployee(id);
        return new ResponseEntity<>("Deleted",HttpStatus.OK);
    }
}
