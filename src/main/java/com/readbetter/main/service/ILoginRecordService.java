package com.readbetter.main.service;

import java.time.LocalDateTime;

public interface ILoginRecordService {

    void recordLogin(LocalDateTime loggedInAt, LocalDateTime loggedOutAt, String username);
    void recordLoginCount(String username);
}
