package com.example.employeemanagment.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class CheckInResponseDTO {

    private String message;
    private Long employeeId;
    private String employeeName;
    private LocalDate Date;
    private LocalTime checkInTime;

    public CheckInResponseDTO(String message, Long employeeId, String employeeName, LocalDate date, LocalTime checkInTime) {
        this.message = message;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        Date = date;
        this.checkInTime = checkInTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public LocalDate getDate() {
        return Date;
    }

    public void setDate(LocalDate date) {
        Date = date;
    }

    public LocalTime getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(LocalTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    @Override
    public String toString() {
        return "CheckInResponseDTO{" +
                "message='" + message + '\'' +
                ", employeeId=" + employeeId +
                ", employeeName='" + employeeName + '\'' +
                ", Date=" + Date +
                ", checkInTime=" + checkInTime +
                '}';
    }
}
