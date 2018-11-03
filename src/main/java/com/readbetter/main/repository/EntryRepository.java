package com.readbetter.main.repository;


import com.fasterxml.jackson.databind.JsonNode;
import com.readbetter.main.model.AppUser;
import com.readbetter.main.model.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EntryRepository extends JpaRepository<Entry, Long> {

    Optional<Entry> findByWord(String word);
    List<Entry> findAllByAppUser(AppUser appUser);
    void deleteEntryByWordAndAppUser (String word, AppUser appUser);


}
