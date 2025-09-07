package com.example.employeemanagment.repo;

import com.example.employeemanagment.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface managerRepo extends JpaRepository<Manager,Long> {
}
