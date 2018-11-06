package com.readbetter.main.repository;

import com.readbetter.main.model.AppUser;
import com.readbetter.main.model.BrowseHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrowseHistoryRepository extends JpaRepository<BrowseHistory, Long> {

    Optional<BrowseHistory> findBrowseHistoryByUrl (String url);
    List<BrowseHistory> findBrowseHistoryByAppUserId (Long appUser);
    void deleteBrowseHistoryByUrlAndAppUserId (String url, Long appUserId);
}
