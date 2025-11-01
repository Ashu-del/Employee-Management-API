package com.example.employeemanagment.service;

import com.example.employeemanagment.model.AttendanceRecord;
import com.example.employeemanagment.model.CheckInResponseDTO;
import com.example.employeemanagment.model.CheckOutResponseDTO;
import com.example.employeemanagment.model.Employee;
import com.example.employeemanagment.repo.AttendanceRepo;
import com.example.employeemanagment.repo.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
public class AttendenceService {

    @Autowired
    private AttendanceRepo attendencerepo;

    @Autowired
    private EmployeeRepo employeeRepo;
    public CheckInResponseDTO checkIn(long id) {
        Employee employee = employeeRepo.findById(id).orElseThrow(()->
                new RuntimeException("Employee not found"));
        LocalDate today = LocalDate.now();

        attendencerepo.findByEmployeeAndDate(employee,today).
                ifPresent(record -> {
                    if(record.getCheckInTime() != null){
                        throw new RuntimeException("Already check in");
                    }
                });

        AttendanceRecord record = attendencerepo.findByEmployeeAndDate(employee,today).
                orElseGet(()->{
                    AttendanceRecord newRecord = new AttendanceRecord();
                    newRecord.setEmployee(employee);
                    newRecord.setDate(today);
                    newRecord.setStatus(AttendanceRecord.Status.PRESENT);
                    return newRecord;
                });
        record.setCheckInTime(LocalTime.now());
        attendencerepo.save(record);
        return new CheckInResponseDTO(
                " Check-in successful",
                employee.getId(),
                employee.getName(),
                record.getDate(),
                record.getCheckInTime()
        );
    }

    public CheckOutResponseDTO checkOut(long employeeId) {
        Employee employee  = employeeRepo.findById(employeeId).orElseThrow(() ->
                new RuntimeException("Employee not found"));

        LocalDate today = LocalDate.now();

        AttendanceRecord record = attendencerepo.findByEmployeeAndDate(employee,today).
                orElseThrow(() -> new RuntimeException("No check-in record found for today."));

        if(record.getCheckOutTime() != null){
            throw new RuntimeException("Already check in today");
        }

        LocalTime checkOut = LocalTime.now();
        record.setCheckOutTime(checkOut);

        Duration duration = Duration.between(record.getCheckInTime(),checkOut);
        double hoursWorked = duration.toMinutes() / 60.0;

        if (hoursWorked >= 8) {
            record.setStatus(AttendanceRecord.Status.PRESENT);
        } else if (hoursWorked >= 4) {
            record.setStatus(AttendanceRecord.Status.HALF_DAY);
        } else {
            record.setStatus(AttendanceRecord.Status.ABSENT);
        }

        attendencerepo.save(record);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        String formattedOut = record.getCheckOutTime().format(formatter);

        return new CheckOutResponseDTO(
                "Checked out successfully at " + formattedOut,
                employee.getId(),
                employee.getName(),
                record.getDate(),
                record.getCheckInTime(),
                record.getCheckOutTime(),
                Math.round(hoursWorked * 100.0) / 100.0,
                record.getStatus().toString()
        );
    }
}
