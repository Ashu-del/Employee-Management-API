package com.example.employeemanagment.service;

import com.example.employeemanagment.model.Employee;
import com.example.employeemanagment.model.EmployeeWrapper;
import com.example.employeemanagment.model.Manager;
import com.example.employeemanagment.repo.EmployeeRepo;
import com.example.employeemanagment.repo.managerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepo repo;

    @Autowired
    private managerRepo managerRepo;
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
            employeeWrapper.setManagerId(employee.getReportingManager().getId());
        } else {
            employeeWrapper.setManagerName(null);
        }
        return employeeWrapper;
    }

    public void createEmployee(EmployeeWrapper employee) {
        Manager manager = managerRepo.findById(employee.getManagerId()).orElseThrow(
                ()-> new RuntimeException("Manager not found")
        );

        Employee employee1 = new Employee();
        employee1.setName(employee.getName());
        employee1.setEmail(employee.getEmail());
        employee1.setPhone(employee.getPhone());
        employee1.setDepartment(employee.getDepartment());
        employee1.setDesignation(employee.getDesignation());
        employee1.setJoiningDate(employee.getJoiningDate());
        employee1.setStatus(employee.getStatus());
        employee1.setReportingManager(manager);

        repo.save(employee1);

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

    public void updateEmployee(Long id) {
        Employee employee = repo.findById(id).orElseThrow(
                () -> new RuntimeException("Employee Not Found")
        );
        employee.setStatus(Employee.Status.INACTIVE);

    }
}
