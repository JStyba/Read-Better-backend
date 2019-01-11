package com.readbetter.main.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMap;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;


@NoArgsConstructor
@Entity
public class Entry {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    private String word;
    private String timestamp;
    private String entryUrl;
    private String filename;
    private String language;

    private LinkedHashMap<String, LocalDateTime> repMap = new LinkedHashMap<>(ImmutableMap.<String, LocalDateTime>builder()
            .put("DAY", LocalDateTime.now())
            .put("TWO_DAY", LocalDateTime.now().plusYears(20))
            .put("WEEK", LocalDateTime.now().plusYears(20))
            .put("TWO_WEEK", LocalDateTime.now().plusYears(20))
            .put("MONTH", LocalDateTime.now().plusYears(20))
            .put("TWO_MONTH", LocalDateTime.now().plusYears(20))
            .put("HALF_YEAR", LocalDateTime.now().plusYears(20))
            .put("YEAR", LocalDateTime.now().plusYears(20))
            .put("RETIRED", LocalDateTime.now().plusYears(20))
            .build());
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "entry")
    @JsonManagedReference
    private List<Definition> definitions;
    @ManyToOne
    @JsonIgnore
    private AppUser appUser;

    public Entry(List<Definition> definitions) {
        this.definitions = definitions;
    }

    public Entry(String word, String timestamp, String entryUrl, String filename, String language/*, BiMap<String, Boolean> repMap*/) {
        this.word = word;
        this.timestamp = timestamp;
        this.entryUrl = entryUrl;
        this.filename = filename;
        this.language = language;
//        this.repMap = repMap;
    }

    public Entry(String word, List<Definition> definitions) {
        this.word = word;
        this.definitions = definitions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getEntryUrl() {
        return entryUrl;
    }

    public void setEntryUrl(String entryUrl) {
        this.entryUrl = entryUrl;
    }

    public List<Definition> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<Definition> definitions) {
        this.definitions = definitions;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public LinkedHashMap<String, LocalDateTime> getRepMap() {
        return repMap;
    }

    public void setRepMap(LinkedHashMap<String, LocalDateTime> repMap) {
        this.repMap = repMap;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "id=" + id +
                ", word='" + word + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", entryUrl='" + entryUrl + '\'' +
                ", definitions=" + definitions +
                ", appUser=" + appUser +
                '}';
    }
}
