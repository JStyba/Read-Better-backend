package com.readbetter.main.repository;


import com.fasterxml.jackson.databind.JsonNode;
import com.readbetter.main.model.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntryRepository extends JpaRepository<Entry, Long> {



    Entry findByWord(String word);
}
