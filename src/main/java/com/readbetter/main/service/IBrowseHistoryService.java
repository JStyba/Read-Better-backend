package com.readbetter.main.service;

import com.readbetter.main.model.AppUser;
import com.readbetter.main.model.BrowseHistory;

import java.util.List;

public interface IBrowseHistoryService {

    void addUrlToDatabase (BrowseHistory url);
    List<BrowseHistory> getAllUrls (Long appUserId);
    void deleteUrl(String url, Long appUserId);
}
