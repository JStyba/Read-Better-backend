package com.readbetter.main.service;

import com.readbetter.main.exceptions.RegistrationException;
import com.readbetter.main.exceptions.UserDoesNotExistException;
import com.readbetter.main.exceptions.UserEmailAlreadyExistsException;
import com.readbetter.main.exceptions.UserLoginAlreadyExistsException;
import com.readbetter.main.model.AppUser;
import com.readbetter.main.model.dto.LoginDto;
import com.readbetter.main.model.dto.PageResponse;
import com.readbetter.main.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.jws.soap.SOAPBinding;
import javax.transaction.Transactional;
import java.util.*;


@Transactional
@Service(value = "appUserService")
public class AppUserService implements IAppUserService, UserDetailsService {
    private static final int DEFAULT_PAGE_SIZE = 10;


    private AppUserRepository appUserRepository;
    private PasswordEncoder encoder;

    @Autowired
    public void setAppUserRepository(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Autowired
    public void setEncoder(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> OptionalUser = appUserRepository.findByUsername(username);
        if (!OptionalUser.isPresent()) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        AppUser user = OptionalUser.get();
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthority());
    }

    private List<SimpleGrantedAuthority> getAuthority() {
        return Arrays.asList(new SimpleGrantedAuthority("ADMIN"));
    }

    public List<AppUser> findAll() {
        List<AppUser> list = new ArrayList<>();
        appUserRepository.findAll().iterator().forEachRemaining(list::add);
        return list;
    }

//    public AppUserService(AppUserRepository appUserRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
//        this.appUserRepository = appUserRepository;
//        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
//    }

    @Override
    public void register(AppUser appUser) throws RegistrationException {
        appUser.setEmail(appUser.getEmail().toLowerCase());
        appUser.setUsername(appUser.getUsername().toLowerCase());
        Optional<AppUser> emailUser = appUserRepository.findByEmail(appUser.getEmail());
        if (emailUser.isPresent()) {
            throw new UserEmailAlreadyExistsException();
        }
        Optional<AppUser> loginUser = appUserRepository.findByUsername(appUser.getUsername());
        if (loginUser.isPresent()) {
            throw new UserLoginAlreadyExistsException();
        }
        appUser.setPassword(encoder.encode(appUser.getPassword()));
        appUser.setLoginCounter(0L);
        appUserRepository.save(appUser);
    }

    @Override
    public PageResponse<AppUser> getUsers(int page) {
        return null;
    }

    @Override
    public Optional<AppUser> getUserWithId(long ownerId) {
        return Optional.empty();
    }

    public PageResponse<AppUser> getAllUsers() {
        return getUsers(0);
    }

    @Override
    public Optional<AppUser> findByUsername(String username) throws UserDoesNotExistException {
        return appUserRepository.findByUsername(username);
    }

    @Override
    public Optional<AppUser> getUserWithUsernameAndPassword(LoginDto dto) throws UserDoesNotExistException {
        Optional<AppUser> foundUser = appUserRepository.findByUsername(dto.getLogin());

        if (!foundUser.isPresent()) {
            throw new UserDoesNotExistException();
        } else {
            AppUser user = foundUser.get();
            if (!encoder.matches(dto.getPassword(), user.getPassword())) {
                throw new UserDoesNotExistException();
            }
        }
        return foundUser;
    }

//    @Override
//    public PageResponse<AppUser> getUsers(int page) {
//        Page<AppUser> users = appUserRepository.findAllBy(PageRequest.of(page, DEFAULT_PAGE_SIZE));
//
//        return new PageResponse<>(users);
//    }


}