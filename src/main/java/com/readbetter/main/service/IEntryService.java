package com.readbetter.main.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.readbetter.main.model.Entry;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface IEntryService {

    JsonNode getDictionaryJson(String wordToLookUp) throws IOException;
    List<String> getDefinitions(JsonNode jsonResponse);
    List<String> cleanedDefinitionsFromJson(List<String> rawDefinitions);
    Optional<Entry> createEntryToSend (String word, List<String> defintions);

}
