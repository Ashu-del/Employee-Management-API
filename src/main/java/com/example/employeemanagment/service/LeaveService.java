package com.example.employeemanagment.service;

import com.example.employeemanagment.model.Employee;
import com.example.employeemanagment.model.LeaveRequest;
import com.example.employeemanagment.model.LeaveRequestDTO;
import com.example.employeemanagment.model.LeaveResponse;
import com.example.employeemanagment.repo.EmployeeRepo;
import com.example.employeemanagment.repo.LeaveRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LeaveService {


    @Autowired
    private LeaveRepo leaveRepo;

    @Autowired
    private EmployeeRepo employeeRepo;
    @Autowired
    private EmailService emailService;
    public String applyLeave(LeaveRequestDTO DTO) {

        Employee employee = employeeRepo.findById(DTO.getEmployeeId()).orElseThrow(
                ()->new RuntimeException("Employee not found")
        );

        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setEmployee(employee);
        leaveRequest.setStartDate(DTO.getStartDate());
        leaveRequest.setEndDate(DTO.getEndDate());
        leaveRequest.setLeaveStatus(LeaveRequest.LeaveStatus.PENDING);
        leaveRequest.setLeaveType(DTO.getLeaveType());
        leaveRequest.setReason(DTO.getReason());
        leaveRepo.save(leaveRequest);
        String managerEmail = employee.getReportingManager().getEmail();
        emailService.sendEmail(
                managerEmail,
                "Leave Application from " + employee.getName(),
                "Employee " + employee.getName() + " has applied for leave from "
                        + leaveRequest.getStartDate() + " to " + leaveRequest.getEndDate() +
                        " for reason: " + leaveRequest.getReason()
        );
        return "Leave Request raised successfully";
    }

    public String  approveLeave(Long id) {
        LeaveRequest leaveRequest = leaveRepo.findById(id).orElseThrow(
                ()->new RuntimeException("Leave not found")
        );
        if(leaveRequest.getLeaveStatus() == LeaveRequest.LeaveStatus.APPROVED){
            throw new RuntimeException("Already approved");
        }
        leaveRequest.setLeaveStatus(LeaveRequest.LeaveStatus.APPROVED);
        leaveRepo.save(leaveRequest);
        emailService.sendEmail(
                leaveRequest.getEmployee().getEmail(),
                "Leave Approved ",
                "Your leave from " + leaveRequest.getStartDate() + " to " + leaveRequest.getEndDate() + " has been approved."
        );
        return "Leave is Approved";
    }
    public String  rejectLeave(Long id) {
        LeaveRequest leaveRequest = leaveRepo.findById(id).orElseThrow(
                ()->new RuntimeException("Leave not found")
        );
        if(leaveRequest.getLeaveStatus() == LeaveRequest.LeaveStatus.REJECTED){
            throw new RuntimeException("Already rejected");
        }
        if(leaveRequest.getLeaveStatus() == LeaveRequest.LeaveStatus.APPROVED){
            throw new RuntimeException("Already approved cannot rejected");
        }
        leaveRequest.setLeaveStatus(LeaveRequest.LeaveStatus.REJECTED);
        leaveRepo.save(leaveRequest);
        emailService.sendEmail(
                leaveRequest.getEmployee().getEmail(),
                "Leave Rejected",
                "Your leave from " + leaveRequest.getStartDate() + " to " + leaveRequest.getEndDate() + " has been rejected."
        );
        return "Leave is Rejected";
    }

    public List<LeaveResponse> getLeavesByEmployee(Long EmployeeId){
        Employee employee = employeeRepo.findById(EmployeeId).orElseThrow(
                ()->new RuntimeException("Employee not found")
        );
        List<LeaveRequest> leaveRequest= leaveRepo.findByEmployee(employee);
        List<LeaveResponse> responses = new ArrayList<>();
        for (LeaveRequest leave : leaveRequest) {
            LeaveRequestDTO dto = new LeaveRequestDTO();
            dto.setEmployeeId(leave.getEmployee().getId());
            dto.setEmployeeName(leave.getEmployee().getName());
            dto.setStartDate(leave.getStartDate());
            dto.setEndDate(leave.getEndDate());
            dto.setLeaveType(leave.getLeaveType());
            dto.setReason(leave.getReason());
            responses.add(new LeaveResponse(dto,leave.getLeaveStatus().toString()));
        }
        return responses;
    }
}
