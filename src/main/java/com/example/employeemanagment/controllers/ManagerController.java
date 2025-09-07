package com.example.employeemanagment.controllers;

import com.example.employeemanagment.model.Manager;
import com.example.employeemanagment.model.managerWrapper;
import com.example.employeemanagment.service.managerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ManagerController {

    @Autowired
    private managerService service;

    @GetMapping("manager")
    public ResponseEntity<List<managerWrapper>> getAllManager(){
     List<managerWrapper> res = service.getAllManager();
     return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("manager/{id}")
    public ResponseEntity<managerWrapper> getManager(@PathVariable long id){
        managerWrapper wrapper = service.getManager(id);
        return new ResponseEntity<>(wrapper,HttpStatus.OK);
    }

    @PostMapping("manager")
    public ResponseEntity<String> createManger(@RequestBody Manager manager){
        service.createManager(manager);
        return new ResponseEntity<>("Success",HttpStatus.CREATED);
    }

    @PutMapping("manager")
    public ResponseEntity<String> updateManger(@RequestBody Manager manager){
        service.createManager(manager);
        return new ResponseEntity<>("Success",HttpStatus.CREATED);
    }

    @DeleteMapping("manager/{id}")
    public ResponseEntity<String> deleteManager(@PathVariable long id){
        service.deleteManager(id);
        return new ResponseEntity<>("Deleted",HttpStatus.OK);
    }
}
