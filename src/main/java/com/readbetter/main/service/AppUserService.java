package com.readbetter.main.service;

import com.readbetter.main.exceptions.RegistrationException;
import com.readbetter.main.exceptions.UserDoesNotExistException;
import com.readbetter.main.exceptions.UserEmailAlreadyExistsException;
import com.readbetter.main.exceptions.UserLoginAlreadyExistsException;
import com.readbetter.main.model.AppUser;
import com.readbetter.main.model.Role;
import com.readbetter.main.model.dto.LoginDto;
import com.readbetter.main.model.dto.PageResponse;
import com.readbetter.main.repository.AppUserRepository;
import com.readbetter.main.repository.RoleRepository;
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
    private RoleRepository roleRepository;

    @Autowired
    public void setAppUserRepository(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Autowired
    public void setEncoder(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    //    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<AppUser> OptionalUser = appUserRepository.findByUsername(username);
//        if (!OptionalUser.isPresent()) {
//            throw new UsernameNotFoundException("Invalid username or password.");
//        }
//        AppUser user = OptionalUser.get();
//        LoginDto userDTO = new LoginDto(user);
//        //        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthority());
//        return userDTO;
//    }
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
//    	if (userRepository.count() == 0) {
//    		System.out.println("There is no user exist in the database. So, adding two users");
//    		userRepository.save(new User("crmadmin", passwordEncoder.encode("adminpass"), Arrays.asList(new UserRole("USER"), new UserRole("ADMIN"))));
//    		userRepository.save(new User("crmuser", passwordEncoder.encode("crmpass"), Arrays.asList(new UserRole("USER"))));
//    	}

        Optional<AppUser> user = appUserRepository.findByUsername(userName);
        if (user == null) {
            throw new UsernameNotFoundException("UserName " + userName + " not found");
        }
        return new LoginDto(user.get());
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
        Role userRole = roleRepository.findByName("USER");
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
        appUser.setRoles(Arrays.asList(userRole));
        appUser.setEnabled(true);
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

//    @Override
//    public Optional<AppUser> getUserWithUsernameAndPassword(LoginDto dto) throws UserDoesNotExistException {
//        Optional<AppUser> foundUser = appUserRepository.findByUsername(dto.getLogin());
//
//        if (!foundUser.isPresent()) {
//            throw new UserDoesNotExistException();
//        } else {
//            AppUser user = foundUser.get();
//            if (!encoder.matches(dto.getPassword(), user.getPassword())) {
//                throw new UserDoesNotExistException();
//            }
//        }
//        return foundUser;
//    }

//    @Override
//    public PageResponse<AppUser> getUsers(int page) {
//        Page<AppUser> users = appUserRepository.findAllBy(PageRequest.of(page, DEFAULT_PAGE_SIZE));
//
//        return new PageResponse<>(users);
//    }


}