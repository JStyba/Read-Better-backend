package com.readbetter.main.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.readbetter.main.model.Definition;
import com.readbetter.main.model.Entry;
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


//        @Override
//    public Entry findWordDefinition(String searchedWord) throws IOException {
//
//        URL url = new URL("https://od-api.oxforddictionaries.com:443/api/v1/inflections/en/" + searchedWord);
//        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
//        conn.setRequestMethod("GET");
//        conn.setRequestProperty("Accept", "application/json");
//        conn.setRequestProperty("app_id", "3c699787");
//        conn.setRequestProperty("app_key", "4a30da605e25de91cf692d808bdb069d");
//
//        if (conn.getResponseCode() != 200) {
//            throw new RuntimeException("Failed : HTTP error code : "
//                    + conn.getResponseCode());
//        }
//
//
//        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//        StringBuilder stringBuilder = new StringBuilder();
//
//        String line = null;
//        while ((line = reader.readLine()) != null) {
//            stringBuilder.append(line + "\n");
//        }
//
//        String wordEntry = stringBuilder.toString();
//        ObjectMapper mapper = new ObjectMapper();
//        final JsonNode arrNode = mapper.readTree(wordEntry).path("results");
//        System.out.println(arrNode.toString());
//        if (arrNode.isArray()) {
//            for (final JsonNode objNode : arrNode) {
////                System.out.println(objNode.get("id"));
//            }
//        }
//
//        String entryString = mapper.writeValueAsString(arrNode);
////        System.out.println(entryString);
//
//        String splittedEntryString = entryString.substring(entryString.indexOf("definitions"), entryString.indexOf("\"]", entryString.indexOf("definitions"))).replaceFirst("definitions", "");
//        String splittedEntryString2 = entryString.substring(entryString.indexOf("etymologies"), entryString.indexOf("\"]", entryString.indexOf("etymologies"))).replaceFirst("etymologies", "");
//        return new Entry(splittedEntryString.replaceAll("[^,&&\\W&&\\S]", ""));
////            return "DEFINITION OF " + searchedWord.toUpperCase() + ": " + splittedEntryString.replaceAll("[^,&&\\W&&\\S]", "") + ".";
////            System.out.println("ETYMOLOGY OF " + searchedWord.toUpperCase() + ": " + splittedEntryString2.replaceAll("[^,&&\\W&&\\S]", "") + ".");
//
//    }
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
        for (Definition def :
                rawDefinitions) {

            def.setDefinition( def.getDefinition().replaceAll("[^,&&\\W&&\\S]", ""));
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


}
