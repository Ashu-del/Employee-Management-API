package com.example.employeemanagment.model;

import java.util.List;

public class managerWrapper {
    private Long id;

    private String name;

    private String email;

    private String phone;

    private List<Long> EmployeeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Long> getEmployeeId() {
        return EmployeeId;
    }

    public void setEmployeeId(List<Long> employeeId) {
        EmployeeId = employeeId;
    }

    @Override
    public String toString() {
        return "managerWrapper{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", EmployeeId=" + EmployeeId +
                '}';
    }
}
