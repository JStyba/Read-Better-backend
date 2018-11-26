package com.readbetter.main.service;

import com.readbetter.main.model.AppUser;
import com.readbetter.main.model.Role;
import com.readbetter.main.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Arrays;

@Service
@Transactional
public class SignupService {
    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public AppUser addUser(AppUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return appUserRepository.save(user);
    }

    /**
     *
     * set up a default customer with two roles USER and ADMIN
     *
     */
//    @PostConstruct
//    private void setupDefaultUser() {
//        //-- just to make sure there is an ADMIN user exist in the database for testing purpose
//        if (appUserRepository.count() == 0) {
//            appUserRepository.save(new AppUser("admin",
//                    passwordEncoder.encode("adminpass"),
//                    Arrays.asList(new Role("USER"), new Role("ADMIN"))));
//        }
//    }
}
