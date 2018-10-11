package com.readbetter.main.controller;


import com.readbetter.main.exceptions.RegistrationException;
import com.readbetter.main.model.AppUser;
import com.readbetter.main.model.dto.PageResponse;
import com.readbetter.main.model.dto.RespFactory;
import com.readbetter.main.model.dto.Response;
import com.readbetter.main.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/users/")
public class AppUserController {


    @Autowired
    AppUserService appUserService;


    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public ResponseEntity<Response> register(@RequestBody AppUser appUser) {
        try {
            appUserService.register(appUser);
        } catch (RegistrationException e) {
            return RespFactory.badRequest();
        }
        return RespFactory.created();
    }

    @CrossOrigin(value = "*", maxAge = 3600)
    @RequestMapping(path = "/list", method = RequestMethod.GET)
    public String list() {
        return "this is a message";
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public List<AppUser> listUser() {
        return appUserService.findAll();
    }

//    @RequestMapping(path = "/page", method = RequestMethod.GET)
//    public ResponseEntity<Response> page(@RequestParam(name = "page") int page) {
//        PageResponse<AppUser> list = appUserService.getUsers(page);
//
//        return RespFactory.result(list);
//    }

//    @RequestMapping(path = "/get", method = RequestMethod.GET)
//    public AppUser login (@RequestParam (name ="login") String login) {
//        AppUser user = appUserService.findByLogin(login).get();
//        return user;
//    }
}