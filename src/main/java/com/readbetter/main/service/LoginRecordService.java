package com.readbetter.main.service;

import com.readbetter.main.model.AppUser;
import com.readbetter.main.model.BrowseHistory;
import com.readbetter.main.model.LoginRecord;
import com.readbetter.main.repository.AppUserRepository;
import com.readbetter.main.repository.LoginRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class LoginRecordService implements ILoginRecordService {
    private AppUserRepository appUserRepository;
    private LoginRecordRepository loginRecordRepository;

    @Autowired
    public void setLoginRecordRepository(LoginRecordRepository loginRecordRepository) {
        this.loginRecordRepository = loginRecordRepository;
    }

    @Autowired
    public void setAppUserRepository(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public void recordLogin(LocalDateTime loggedInAt, LocalDateTime loggedOutAt, String username) {
        Optional<AppUser> user = appUserRepository.findByUsername(username);
        if (user.isPresent()) {
            Long minutes = ChronoUnit.MINUTES.between(loggedInAt, loggedOutAt);
            LoginRecord loginRecord = new LoginRecord();
            loginRecord.setLoggedInAt(loggedInAt);
            loginRecord.setLoggedOutAt(loggedOutAt);
            loginRecord.setTimeSpentOnline(minutes);
            loginRecord.setAppUser(user.get());
            loginRecordRepository.saveAndFlush(loginRecord);
            user.get().setLoginCounter(user.get().getLoginCounter()+1);
            appUserRepository.saveAndFlush(user.get());

        }
    }
}
