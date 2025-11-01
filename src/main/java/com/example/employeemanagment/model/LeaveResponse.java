package com.example.employeemanagment.model;

public class LeaveResponse {
    private LeaveRequestDTO leaveDetails;
    private String status;

    public LeaveResponse(LeaveRequestDTO leaveDetails, String status) {
        this.leaveDetails = leaveDetails;
        this.status = status;
    }

    public LeaveRequestDTO getLeaveDetails() {
        return leaveDetails;
    }

    public void setLeaveDetails(LeaveRequestDTO leaveDetails) {
        this.leaveDetails = leaveDetails;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "LeaveResponse{" +
                "leaveDetails=" + leaveDetails +
                ", status='" + status + '\'' +
                '}';
    }
}
