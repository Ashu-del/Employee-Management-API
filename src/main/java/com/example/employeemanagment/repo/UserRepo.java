package com.example.employeemanagment.repo;


import com.example.employeemanagment.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<Users,Long> {
    Users findByUsername(String username);

    @Query("select u.id  from Users u where u.role = 'USER'")
    List<Integer> findByUser();
}
