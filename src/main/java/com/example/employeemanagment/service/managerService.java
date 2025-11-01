package com.example.employeemanagment.service;

import com.example.employeemanagment.model.Employee;
import com.example.employeemanagment.model.Manager;
import com.example.employeemanagment.model.managerWrapper;
import com.example.employeemanagment.repo.EmployeeRepo;
import com.example.employeemanagment.repo.managerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class managerService {

    @Autowired
    private managerRepo repo ;

    @Autowired
    private EmployeeRepo employeeRepo;

    public void updateManager(Long id, managerWrapper wrapper) {
        Manager existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Manager not found with ID: " + id));


        existing.setName(wrapper.getName());
        existing.setEmail(wrapper.getEmail());
        existing.setPhone(wrapper.getPhone());


        Manager updatedManager = repo.save(existing);

        if (wrapper.getEmployeeId() != null) {
            //  Unlink old employees
            List<Employee> oldTeam = employeeRepo.findByReportingManager(existing);
            for (Employee emp : oldTeam) {
                emp.setReportingManager(null);
            }
            employeeRepo.saveAll(oldTeam);

            // Link new employees
            List<Employee> newTeam = employeeRepo.findByIdIn(wrapper.getEmployeeId());
            for (Employee emp : newTeam) {
                emp.setReportingManager(updatedManager);
            }
            employeeRepo.saveAll(newTeam);
        }
    }

    public List<managerWrapper> getAllManager() {
        List<Manager> mang = repo.findAll();
        List<managerWrapper> wrappers = new ArrayList<>();
        for(Manager manager:mang){
            managerWrapper mangerWarp = new managerWrapper();
            mangerWarp.setId(manager.getId());
            mangerWarp.setName(manager.getName());
            mangerWarp.setEmail(manager.getEmail());
            mangerWarp.setPhone(manager.getPhone());
            List<Long> employeeId = new ArrayList<>();
            if (manager.getTeamMembers() != null) {
                for (Employee emp : manager.getTeamMembers()) {
                    employeeId.add(emp.getId());
                }
            }
            mangerWarp.setEmployeeId(employeeId);
            wrappers.add(mangerWarp);
        }
        return wrappers;
    }

    public managerWrapper getManager(long id) {
        Manager manager = repo.findById(id).orElseThrow(() -> new RuntimeException("Manager not found"));
        managerWrapper mangerWarp = new managerWrapper();
        mangerWarp.setId(manager.getId());
        mangerWarp.setName(manager.getName());
        mangerWarp.setEmail(manager.getEmail());
        mangerWarp.setPhone(manager.getPhone());
        List<Long> employeeId = new ArrayList<>();
        if (manager.getTeamMembers() != null) {
            for (Employee emp : manager.getTeamMembers()) {
                employeeId.add(emp.getId());
            }
        }
        mangerWarp.setEmployeeId(employeeId);
        return  mangerWarp;
    }

    public void createManager(managerWrapper manager) {
        Manager manager1 = new Manager();
        manager1.setName(manager.getName());
        manager1.setEmail(manager.getEmail());
        manager1.setPhone(manager.getPhone());
        Manager savedManager = repo.save(manager1);
        if (manager.getEmployeeId() != null) {
            List<Employee> employees = employeeRepo.findByIdIn(manager.getEmployeeId());
            for (Employee emp : employees) {
                emp.setReportingManager(savedManager);
            }
            employeeRepo.saveAll(employees);
        }

    }

    public void deleteManager(long id) {
        repo.deleteById(id);
    }
}
