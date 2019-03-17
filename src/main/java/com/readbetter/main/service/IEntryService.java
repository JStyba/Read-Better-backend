package com.readbetter.main.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.readbetter.main.exceptions.ElementNotFound;
import com.readbetter.main.model.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

public interface IEntryService {

    JsonNode getDictionaryJson(String wordToLookUp) throws IOException;
    List<Definition> getDefinitions(JsonNode jsonResponse);
    List<Definition> cleanedDefinitionsFromJson(List<Definition> rawDefinitions);
    Optional<Entry> createEntryToSend (String word, List<Definition> defintions);
    JsonNode findInflection (String word) throws IOException;
    String getInflection (JsonNode jsonResponse);
    void addEntryToDatabse(Entry entry);
    List<Entry> getAllEntries(Long appUserId);
    void deleteEntry(String word, AppUser appUser) throws ElementNotFound;
    JsonNode getDictionaryJsonPl (String wordToLookUp) throws IOException;
    List<Definition> getTranslationsPl(JsonNode jsonResponse);
    List<Pronunciation> getPronunciationBrE(JsonNode jsonResponse);
    JsonNode getDictionaryPronunciation (String wordToLookUp) throws IOException;
    LinkedHashMap<String, LocalDateTime> returnNextInterval (Entry entry, boolean correct);
    List<Entry> getEntriesToLearn (List<Entry> entryList);

}
