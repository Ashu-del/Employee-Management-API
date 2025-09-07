package com.example.employeemanagment.service;

import com.example.employeemanagment.model.Employee;
import com.example.employeemanagment.model.EmployeeWrapper;
import com.example.employeemanagment.repo.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepo repo;
    public EmployeeWrapper getEmployee(Long id) {
        Employee employee = repo.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));
        EmployeeWrapper employeeWrapper = new EmployeeWrapper();
        employeeWrapper.setId(employee.getId());
        employeeWrapper.setName(employee.getName());
        employeeWrapper.setDepartment(employee.getDepartment());
        employeeWrapper.setDesignation(employee.getDesignation());
        employeeWrapper.setEmail(employee.getEmail());
        employeeWrapper.setPhone(employee.getPhone());
        employeeWrapper.setJoiningDate(employee.getJoiningDate());
        employeeWrapper.setStatus(employee.getStatus());
        if (employee.getReportingManager() != null) {
            employeeWrapper.setManagerName(employee.getReportingManager().getName());
        } else {
            employeeWrapper.setManagerName(null);
        }
        return employeeWrapper;
    }

    public void createEmployee(Employee employee) {
        repo.save(employee);
    }

    public List<EmployeeWrapper> getAllEmployee() {
        List<Employee> employee = repo.findAll();
        List<EmployeeWrapper> wrapperList = new ArrayList<>();
        for (Employee emp : employee) {
            EmployeeWrapper wrapper = new EmployeeWrapper();
            wrapper.setId(emp.getId());
            wrapper.setName(emp.getName());
            wrapper.setDepartment(emp.getDepartment());
            wrapper.setDesignation(emp.getDesignation());
            wrapper.setEmail(emp.getEmail());
            wrapper.setPhone(emp.getPhone());
            wrapper.setJoiningDate(emp.getJoiningDate());
            wrapper.setStatus(emp.getStatus());

            if (emp.getReportingManager() != null) {
                wrapper.setManagerName(emp.getReportingManager().getName());
            } else {
                wrapper.setManagerName(null);
            }

            wrapperList.add(wrapper);
        }
        return wrapperList;
    }

    public void deleteEmployee(long id) {
        repo.deleteById(id);
    }
}
