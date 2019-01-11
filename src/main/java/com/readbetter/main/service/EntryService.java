package com.readbetter.main.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.readbetter.main.exceptions.ElementNotFound;
import com.readbetter.main.model.*;
import com.readbetter.main.repository.AppUserRepository;
import com.readbetter.main.repository.EntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;


@Service
public class EntryService implements IEntryService {

    AppUserRepository appUserRepository;
    EntryRepository entryRepository;


    @Autowired
    public void setAppUserRepository(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Autowired
    public void setEntryRepository(EntryRepository entryRepository) {
        this.entryRepository = entryRepository;
    }

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
    public JsonNode getDictionaryPronunciation(String wordToLookUp) throws IOException {
        URL url = new URL("https://od-api.oxforddictionaries.com:443/api/v1/entries/en/" + wordToLookUp + "/pronunciations");
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
            try {
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
            } catch (NullPointerException npe) {

            }
        }
        return stringDefintions;
    }

    @Override
    public List<Pronunciation> getPronunciationBrE(JsonNode jsonResponse) {
        List<Pronunciation> audioFilesLinks = new ArrayList<>();
        if (jsonResponse.isArray()) {
            for (final JsonNode objNode : jsonResponse) {
                JsonNode lexicalEntries = objNode.get("lexicalEntries");
                for (final JsonNode obj1Node : lexicalEntries) {
                    JsonNode pronunciations = obj1Node.get("pronunciations");
                    for (final JsonNode obj2Node : pronunciations) {
                        System.out.println("this is lexicalCategory: " + obj1Node.get("lexicalCategory").asText());
                        System.out.println("this is lexicalCategory: " + obj2Node.get("audioFile").asText());
                        audioFilesLinks.add(new Pronunciation(obj1Node.get("lexicalCategory").asText(), obj2Node.get("audioFile").asText()));

                    }
                }
            }
        }
        return audioFilesLinks;
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
        Entry entry = new Entry(word, definitions);
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

    @Override
    public LinkedHashMap<String, LocalDateTime> returnNextInterval(Entry entry, boolean correct) {
        String acviveRep = "";
        String nextRep = "";
        LinkedHashMap<String, LocalDateTime> tempRepMap = entry.getRepMap();
        Iterator<Map.Entry<String, LocalDateTime>> it = tempRepMap.entrySet().iterator();
        LocalDateTime now = LocalDateTime.now();
        if (correct) {
            while (it.hasNext()) {
                Map.Entry<String, LocalDateTime> e = it.next();
                if (e.getValue().isBefore(LocalDateTime.now())) {
                    acviveRep = e.getKey();
                    nextRep = it.next().getKey();
                }
            }
            tempRepMap.put(acviveRep, LocalDateTime.now().plusYears(20));
            switch (nextRep) {
                case "TWO_DAY":
                    tempRepMap.put(nextRep, LocalDateTime.now().plusDays(2));
                    break;
                case "WEEK":
                    tempRepMap.put(nextRep, LocalDateTime.now().plusWeeks(1));
                    break;
                case "TWO_WEEK":
                    tempRepMap.put(nextRep, LocalDateTime.now().plusWeeks(2));
                    break;
                case "MONTH":
                    tempRepMap.put(nextRep, LocalDateTime.now().plusMonths(1));
                    break;
                case "TWO_MONTH":
                    tempRepMap.put(nextRep, LocalDateTime.now().plusMonths(2));
                    break;
                case "HALF_YEAR":
                    tempRepMap.put(nextRep, LocalDateTime.now().plusMonths(6));
                    break;
                case "YEAR":
                    tempRepMap.put(nextRep, LocalDateTime.now().plusYears(1));
                    break;
            }
        } if (!correct) {
            tempRepMap.put(acviveRep, LocalDateTime.now().plusYears(20));
            tempRepMap.put("DAY", LocalDateTime.now());
        }
        return tempRepMap;
    }
}



