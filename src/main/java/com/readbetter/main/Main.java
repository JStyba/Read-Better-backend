package com.readbetter.main;


import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
import com.readbetter.main.model.Entry;
import org.apache.commons.httpclient.HttpURL;
import org.apache.commons.httpclient.util.HttpURLConnection;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {


    public static void main(String[] args) throws IOException {

//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Wprowadź adres strony");
//        String url1 = scanner.nextLine().trim();
//
//
//        Document doc = Jsoup.connect(url1)
//                .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
//                .referrer("http://www.google.com")
//                .get();
//
//        Elements title = doc.select("p");
//        String newTitle = title.toString();
//
//        String webContent = newTitle.replaceAll("<(.*?)>", "").replaceAll("[.,!?]", "");
//        String[] webContentTable = webContent.split(" ");
//        Set<String> webContentSet = new HashSet<String>(Arrays.asList(webContentTable));
//        System.out.println(webContent);
//        System.out.println("---------------------------------------------------------------------------------------------------------------");
//
//        for (String s :
//                webContentSet) {
//            System.out.println(s);
//        }

//**************************************************************************************
        String searchedWord = "stop";
        URL url = new URL("https://od-api.oxforddictionaries.com:443/api/v1/entries/en/" + searchedWord + "/definitions");
//        URL url = new URL("https://od-api.oxforddictionaries.com:443/api/v1/search/en?q=" + searchedWord + "prefix=false");
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
        final JsonNode arrNode = mapper.readTree(wordEntry).get("results");
//        System.out.println(arrNode.toString());
        List <String> stringDefinitions = new ArrayList<>();
        if (arrNode.isArray()) {
            for (final JsonNode objNode : arrNode) {
                JsonNode lexicalEntries = objNode.get("lexicalEntries");
                for (final JsonNode obj1Node : lexicalEntries) {
                    JsonNode entries = obj1Node.get("entries");
                    for (final JsonNode obj2Node : entries) {
                        JsonNode senses = obj2Node.get("senses");
                        for (final JsonNode obj3Node : senses) {
                            stringDefinitions.add(obj3Node.get("definitions").toString());

                            }
                    }
                }
            }

        }
        int i = 1;
        for (String def :
                stringDefinitions) {

            def = def.replaceAll("[^,&&\\W&&\\S]", "");
            System.out.println(i++ + ". " + def);
        }

        stringDefinitions.toString();
        System.out.println(stringDefinitions);
        String entryString = mapper.writeValueAsString(arrNode);
//        System.out.println(entryString);

//        String splittedEntryString = entryString.substring(entryString.indexOf("definitions"), entryString.indexOf("\"]",entryString.indexOf("definitions"))).replaceFirst("definitions","");
//        String splittedEntryString2 = entryString.substring(entryString.indexOf("etymologies"), entryString.indexOf("\"]",entryString.indexOf("etymologies"))).replaceFirst("etymologies","");
//
//        System.out.println("DEFINITION OF " + searchedWord.toUpperCase() + ": " + splittedEntryString.replaceAll("[^,&&\\W&&\\S]", "") + ".");
//        System.out.println("ETYMOLOGY OF " + searchedWord.toUpperCase() + ": " + splittedEntryString2.replaceAll("[^,&&\\W&&\\S]", "") + ".");
    }


}