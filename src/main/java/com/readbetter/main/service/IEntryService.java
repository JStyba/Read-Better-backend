package com.readbetter.main.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.readbetter.main.exceptions.ElementNotFound;
import com.readbetter.main.model.AppUser;
import com.readbetter.main.model.Definition;
import com.readbetter.main.model.Entry;
import com.readbetter.main.model.Pronunciation;

import java.io.IOException;
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

}
