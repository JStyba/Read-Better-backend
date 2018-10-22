package com.readbetter.main.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.readbetter.main.model.Definition;
import com.readbetter.main.model.Entry;

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

}
