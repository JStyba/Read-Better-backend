package com.readbetter.main.controller;

import com.readbetter.main.exceptions.RegistrationException;
import com.readbetter.main.model.AppUser;
import com.readbetter.main.model.dto.RespFactory;
import com.readbetter.main.model.dto.Response;
import com.readbetter.main.repository.AppUserRepository;
import com.readbetter.main.repository.BrowseHistoryRepository;
import com.readbetter.main.service.AppUserService;
import com.readbetter.main.service.BrowseHistoryService;
import com.readbetter.main.service.LoginRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/admin/")
public class AdminController {
    private AppUserRepository appUserRepository;
    private AppUserService appUserService;
    private BrowseHistoryRepository browseHistoryRepository;
    private BrowseHistoryService browseHistoryService;
    private LoginRecordService loginRecordService;

    @Autowired
    public void setLoginRecordService(LoginRecordService loginRecordService) {
        this.loginRecordService = loginRecordService;
    }
    @Autowired
    public void setBrowseHistoryService(BrowseHistoryService browseHistoryService) {
        this.browseHistoryService = browseHistoryService;
    }

    @Autowired
    public void setBrowseHistoryRepository(BrowseHistoryRepository browseHistoryRepository) {
        this.browseHistoryRepository = browseHistoryRepository;
    }

    @Autowired
    public void setAppUserService(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @Autowired
    public void setAppUserRepository(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public ResponseEntity<Response> register(@RequestBody AppUser appUser) {
        try {
            appUserService.register(appUser);
        } catch (RegistrationException e) {
            return RespFactory.badRequest();
        }
        return RespFactory.created();
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<AppUser> listUser() {
        return appUserService.findAll();
    }

//    @RequestMapping(path = "/page", method = RequestMethod.GET)
//    public ResponseEntity<Response> page(@RequestParam(name = "page") int page) {
//        PageResponse<AppUser> list = appUserService.getUsers(page);
//
//        return RespFactory.result(list);
//    }

}
