package com.readbetter.main.repository;


import com.readbetter.main.exceptions.UserDoesNotExistException;
import com.readbetter.main.model.AppUser;

import com.readbetter.main.model.dto.LoginDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {


    Optional<AppUser> findByUsername (String login);
    Optional<AppUser> findByEmail(String email);
}