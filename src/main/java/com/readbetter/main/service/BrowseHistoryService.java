package com.readbetter.main.service;

import com.readbetter.main.model.AppUser;
import com.readbetter.main.model.BrowseHistory;
import com.readbetter.main.repository.BrowseHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrowseHistoryService implements IBrowseHistoryService {

    private BrowseHistoryRepository browseHistoryRepository;

    @Autowired
    public void setBrowseHistoryRepository(BrowseHistoryRepository browseHistoryRepository) {
        this.browseHistoryRepository = browseHistoryRepository;
    }
    @Override
    public void addUrlToDatabase(BrowseHistory url) {
        browseHistoryRepository.saveAndFlush(url);
    }

    @Override
    public List<BrowseHistory> getAllUrls(Long appUserId) {
        return browseHistoryRepository.findBrowseHistoryByAppUserId(appUserId);
    }

    public void deleteUrl(String url, Long appUserId) {
        browseHistoryRepository.deleteBrowseHistoryByUrlAndAppUserId(url, appUserId);

    }
}
