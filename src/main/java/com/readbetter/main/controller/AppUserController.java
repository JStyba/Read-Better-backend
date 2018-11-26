package com.readbetter.main.controller;


import com.readbetter.main.exceptions.RegistrationException;
import com.readbetter.main.model.AppUser;
import com.readbetter.main.model.BrowseHistory;
import com.readbetter.main.model.LoginRecord;
import com.readbetter.main.model.dto.RespFactory;
import com.readbetter.main.model.dto.Response;
import com.readbetter.main.repository.AppUserRepository;
import com.readbetter.main.repository.BrowseHistoryRepository;
import com.readbetter.main.service.AppUserService;
import com.readbetter.main.service.BrowseHistoryService;
import com.readbetter.main.service.LoginRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/users/")
public class AppUserController {

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
//    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    public ResponseEntity<?> deleteAppUser(@RequestParam (name = "username") String username) {
//        AppUser appUser = appUserService.getCustomer(customerId);
//        customerService.deleteCustomer(customer);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }



    @RequestMapping(path = "/save-url", method = RequestMethod.GET)
    public ResponseEntity<Response> saveUrl(@RequestParam(name = "username") String username, @RequestParam(name = "url") String url) {
        Optional<AppUser> user = appUserRepository.findByUsername(username);
        if (!url.isEmpty() && url != "") {
            if (user.isPresent()) {
                BrowseHistory link = new BrowseHistory();
                link.setUrl(url);
                link.setTimestamp(LocalDateTime.now());
                link.setAppUser(user.get());
                if (!browseHistoryRepository.findBrowseHistoryByUrl(url).isPresent()) {
                    browseHistoryService.addUrlToDatabase(link);
                }
            }
        }
        return RespFactory.created();
    }

    @RequestMapping(path = "/get-browsing-history", method = RequestMethod.GET)
    public List<BrowseHistory> getAllUrlsByUser(@RequestParam(name = "username") String username) {
        Optional<AppUser> user = appUserRepository.findByUsername(username);
        if (user.isPresent()) {
            List<BrowseHistory> browseHistoryList = browseHistoryService.getAllUrls(user.get().getId());
            return browseHistoryList;
        }
        return null;
    }

    @RequestMapping(path = "/remove-link", method = RequestMethod.DELETE)
    @Transactional
    public ResponseEntity<Response> removeUrl(@RequestParam(name = "url") String url, @RequestParam(name = "username") String username) {
        Optional<AppUser> user = appUserRepository.findByUsername(username);
        Optional<BrowseHistory> urlToDelete = browseHistoryRepository.findBrowseHistoryByUrl(url);
        if (user.isPresent() && urlToDelete.isPresent()) {
            browseHistoryService.deleteUrl(url, user.get().getId());
        }
        return RespFactory.ok("Url deleted");
    }

    @RequestMapping(path = "/record-session", method = RequestMethod.GET)
    public void recordSession(@RequestParam(name = "loggedIn") String loggedIn, @RequestParam(name = "loggedOut") String loggedOut, @RequestParam (name = "username") String username) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime loggedInAt = LocalDateTime.parse(loggedIn, formatter);
        LocalDateTime loggedOutAt = LocalDateTime.parse(loggedOut, formatter);
            loginRecordService.recordLogin(loggedInAt, loggedOutAt, username);
    }


}