package com.readbetter.main.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.readbetter.main.model.Entry;

import java.io.IOException;
import java.util.List;

public interface IEntryService {

    JsonNode getDictionaryJson(String wordToLookUp) throws IOException;
    List<String> getDefinitions(JsonNode jsonResponse);
    List<String> cleanedDefinitionsFromJson(List<String> rawDefinitions);
    Entry createEntryToSend (String word, List<String> defintions);

}
