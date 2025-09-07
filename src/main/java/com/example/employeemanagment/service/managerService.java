package com.example.employeemanagment.service;

import com.example.employeemanagment.model.Employee;
import com.example.employeemanagment.model.Manager;
import com.example.employeemanagment.model.managerWrapper;
import com.example.employeemanagment.repo.managerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class managerService {

    @Autowired
    private managerRepo repo;
    public List<managerWrapper> getAllManager() {
        List<Manager> mang = repo.findAll();
        List<managerWrapper> wrappers = new ArrayList<>();
        for(Manager manager:mang){
            managerWrapper mangerWarp = new managerWrapper();
            mangerWarp.setId(manager.getId());
            mangerWarp.setName(manager.getName());
            mangerWarp.setEmail(manager.getEmail());
            mangerWarp.setPhone(manager.getPhone());
            List<String> employeeNames = new ArrayList<>();
            if (manager.getTeamMembers() != null) {
                for (Employee emp : manager.getTeamMembers()) {
                    employeeNames.add(emp.getName());
                }
            }
            mangerWarp.setName(String.valueOf(employeeNames));
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
        List<String> employeeNames = new ArrayList<>();
        if (manager.getTeamMembers() != null) {
            for (Employee emp : manager.getTeamMembers()) {
                employeeNames.add(emp.getName());
            }
        }
        mangerWarp.setName(String.valueOf(employeeNames));
        return  mangerWarp;
    }

    public void createManager(Manager manager) {
        repo.save(manager);
    }

    public void deleteManager(long id) {
        repo.deleteById(id);
    }
}
