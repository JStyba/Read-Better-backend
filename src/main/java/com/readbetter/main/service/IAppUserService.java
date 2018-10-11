package com.readbetter.main.service;


import com.readbetter.main.exceptions.RegistrationException;
import com.readbetter.main.model.AppUser;
import com.readbetter.main.model.dto.LoginDto;
import com.readbetter.main.model.dto.PageResponse;

import java.util.List;
import java.util.Optional;

public interface IAppUserService {
        List<AppUser> findAll();
        void register(AppUser appUser) throws RegistrationException;

        PageResponse<AppUser> getUsers(int page);

//        PageResponse<AppUser> getAllUsers();

        Optional<AppUser> getUserWithId(long ownerId);
//
//        Optional<AppUser> getUserWithLoginAndPassword(LoginDto dto);

        Optional<AppUser> findByLogin (String login);
}