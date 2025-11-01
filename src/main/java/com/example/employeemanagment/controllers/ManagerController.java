package com.example.employeemanagment.controllers;

import com.example.employeemanagment.model.managerWrapper;
import com.example.employeemanagment.service.managerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Manager")
public class ManagerController {

    @Autowired
    private managerService service;

    @PreAuthorize("hasRole('HR')")
    @GetMapping
    public ResponseEntity<List<managerWrapper>> getAllManager(){
     List<managerWrapper> res = service.getAllManager();
     return new ResponseEntity<>(res, HttpStatus.OK);
    }
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<managerWrapper> getManager(@PathVariable long id){
        managerWrapper wrapper = service.getManager(id);
        return new ResponseEntity<>(wrapper,HttpStatus.OK);
    }
    @PreAuthorize("hasRole('HR')")
    @PostMapping
    public ResponseEntity<String> createManger(@RequestBody managerWrapper manager){
        service.createManager(manager);
        return new ResponseEntity<>("Success",HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('HR')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateManager(@PathVariable Long id, @RequestBody managerWrapper wrapper) {
        service.updateManager(id, wrapper);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('HR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteManager(@PathVariable long id){
        service.deleteManager(id);
        return new ResponseEntity<>("Deleted",HttpStatus.OK);
    }
}
