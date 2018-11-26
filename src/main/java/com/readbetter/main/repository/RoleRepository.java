package com.readbetter.main.repository;


import com.readbetter.main.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String role_admin);
}