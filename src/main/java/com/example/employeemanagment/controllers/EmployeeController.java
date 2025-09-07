package com.example.employeemanagment.controllers;

import com.example.employeemanagment.model.Employee;
import com.example.employeemanagment.model.EmployeeWrapper;
import com.example.employeemanagment.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    @GetMapping("Employee/{id}")
    public ResponseEntity<EmployeeWrapper> getEmployee(@PathVariable long id){

         return new ResponseEntity<>(service.getEmployee(id),HttpStatus.OK);

    }

    @GetMapping("Employee")
    public ResponseEntity<List<EmployeeWrapper>> getAllEmployee(){
        List<EmployeeWrapper> wrappers = service.getAllEmployee();
        return new ResponseEntity<>(wrappers,HttpStatus.OK);
    }


    @PostMapping("Employee")
    public ResponseEntity<String> createEmployee(@RequestBody Employee employee){
        service.createEmployee(employee);
        return new ResponseEntity<>("Success",HttpStatus.CREATED);
    }

    @PutMapping("Employee")
    public ResponseEntity<String> updateEmployee(@RequestBody Employee employee){
        service.createEmployee(employee);
        return new ResponseEntity<>("Success",HttpStatus.CREATED);
    }

    @DeleteMapping("Employee/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable long id){
        service.deleteEmployee(id);
        return new ResponseEntity<>("Deleted",HttpStatus.OK);
    }
}
