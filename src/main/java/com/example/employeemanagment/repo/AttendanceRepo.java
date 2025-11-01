package com.example.employeemanagment.repo;

import com.example.employeemanagment.model.AttendanceRecord;
import com.example.employeemanagment.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AttendanceRepo extends JpaRepository<AttendanceRecord,Long> {
Optional<AttendanceRecord> findByEmployeeAndDate(Employee employee, LocalDate date);
}
