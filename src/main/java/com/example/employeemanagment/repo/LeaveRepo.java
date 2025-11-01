package com.example.employeemanagment.repo;

import com.example.employeemanagment.model.Employee;
import com.example.employeemanagment.model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRepo extends JpaRepository<LeaveRequest,Long> {

    List<LeaveRequest> findByEmployee(Employee employee);
}
