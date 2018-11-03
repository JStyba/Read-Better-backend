package com.readbetter.main.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.readbetter.main.exceptions.ElementNotFound;
import com.readbetter.main.model.AppUser;
import com.readbetter.main.model.Definition;
import com.readbetter.main.model.Entry;
import com.readbetter.main.repository.AppUserRepository;
import com.readbetter.main.repository.EntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class EntryService implements IEntryService {
    @Autowired
    AppUserRepository appUserRepository;
    @Autowired
    EntryRepository entryRepository;

    @Override
    public JsonNode getDictionaryJson(String wordToLookUp) throws IOException {
        URL url = new URL("https://od-api.oxforddictionaries.com:443/api/v1/entries/en/" + wordToLookUp + "/definitions");
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("app_id", "3c699787");
        conn.setRequestProperty("app_key", "4a30da605e25de91cf692d808bdb069d");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + conn.getResponseCode());
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();

        String line = null;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line + "\n");
        }
        String wordEntry = stringBuilder.toString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode arrNode = mapper.readTree(wordEntry).get("results");
        return arrNode;
    }

    @Override
    public JsonNode findInflection(String word) throws IOException {
        URL url = new URL("https://od-api.oxforddictionaries.com:443/api/v1/inflections/en/" + word);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("app_id", "3c699787");
        conn.setRequestProperty("app_key", "4a30da605e25de91cf692d808bdb069d");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + conn.getResponseCode());
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();

        String line = null;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line + "\n");
        }
        String wordEntry = stringBuilder.toString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode arrNode = mapper.readTree(wordEntry).get("results");
        return arrNode;
    }

    @Override
    public String getInflection(JsonNode jsonResponse) {

        if (jsonResponse.isArray()) {
            for (final JsonNode objNode : jsonResponse) {
                JsonNode lexicalEntries = objNode.get("lexicalEntries");
                for (final JsonNode obj1Node : lexicalEntries) {
                    JsonNode inflectionOf = obj1Node.get("inflectionOf");
                    for (final JsonNode obj2Node : inflectionOf) {
                        return new String(obj2Node.get("id").toString().replaceAll("\"", ""));
                    }
                }
            }
        }
        return "";
    }

    @Override
    public List<Definition> getDefinitions(JsonNode jsonResponse) {
        List<Definition> stringDefintions = new ArrayList<>();
        if (jsonResponse.isArray()) {
            for (final JsonNode objNode : jsonResponse) {
                JsonNode lexicalEntries = objNode.get("lexicalEntries");
                for (final JsonNode obj1Node : lexicalEntries) {
                    JsonNode entries = obj1Node.get("entries");
                    for (final JsonNode obj2Node : entries) {
                        JsonNode senses = obj2Node.get("senses");
                        for (final JsonNode obj3Node : senses) {
                            JsonNode definitions = obj3Node.get("definitions");
                            stringDefintions.add(new Definition(obj3Node.get("definitions").toString()));
                        }
                    }
                }
            }
        }
        return stringDefintions;
    }


    @Override
    public List<Definition> cleanedDefinitionsFromJson(List<Definition> rawDefinitions) {
        int i = 1;
        List<Definition> cleanedDefinitions = new ArrayList<>();
        for (Definition def : rawDefinitions) {
            def.setDefinition(def.getDefinition().replaceAll("[^,&&\\W&&\\S]", ""));
            cleanedDefinitions.add(def);
        }
        return cleanedDefinitions;
    }

    @Override
    public Optional<Entry> createEntryToSend(String word, List<Definition> definitions) {
        Entry entry = new Entry("test", definitions);
        Optional<Entry> optionalEntry = Optional.ofNullable(entry);
        return optionalEntry;
    }

    @Override
    public void addEntryToDatabse(Entry entry) {
        entryRepository.saveAndFlush(entry);
    }

    @Override
    public List<Entry> getAllEntries(Long appUserId) {
        Optional<AppUser> appUser = appUserRepository.findById(appUserId);
        if (appUser.isPresent()) {
            return entryRepository.findAllByAppUser(appUser.get());
        }
        return new ArrayList<>();
    }

    @Override
    public void deleteEntry(String word, AppUser appUser) throws ElementNotFound {
        entryRepository.deleteEntryByWordAndAppUser(word, appUser);
    }

    @Override
    public JsonNode getDictionaryJsonPl(String wordToLookUp) throws IOException {
        URL url = new URL("https://glosbe.com/gapi/translate?from=eng&dest=pol&format=json&phrase=" + wordToLookUp + "&pretty=true");
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + conn.getResponseCode());
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();

        String line = null;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line + "\n");
        }
        String wordEntry = stringBuilder.toString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode arrNode = mapper.readTree(wordEntry);
        return arrNode;
    }

    @Override
    public List<Definition> getTranslationsPl(JsonNode jsonResponse) {
        List<Definition> stringDefintions = new ArrayList<>();
        System.out.println("this is jsonResponse" + jsonResponse.toString());
        JsonNode root = jsonResponse.get("tuc");
        for (final JsonNode outerArray : root) {
            for (final JsonNode phrase : outerArray) {
                if (phrase.get("text") != null) {
                    System.out.println("this is it: " + phrase.get("text").asText());
                    stringDefintions.add(new Definition(phrase.get("text").asText()));
                }
            }
        }
        System.out.println(stringDefintions);
        return stringDefintions;
    }
}


