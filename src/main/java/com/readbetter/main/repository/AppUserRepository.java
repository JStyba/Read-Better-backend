package com.readbetter.main.repository;


import com.readbetter.main.model.AppUser;

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
//    Page<AppUser> findAllBy(Pageable pageable);
//    List<Entry> getAllById(Long id);
//    Optional<AppUser> findByLoginAndPassword(String username, String password);



}