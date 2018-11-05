package com.readbetter.main.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.List;


@NoArgsConstructor
@Entity
public class Entry {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;

    private String word;
    private String timestamp;
    private String entryUrl;
    private String pronunciationLinkBrE;
    @OneToMany (cascade = CascadeType.PERSIST, mappedBy = "entry")
    @JsonManagedReference
    private List<Definition> definitions;
    @ManyToOne
    @JsonIgnore
    private AppUser appUser;
    public Entry(List<Definition> definitions) {
        this.definitions = definitions;
    }

    public Entry(String word, String timestamp, String entryUrl, List<Definition> definitions) {
        this.word = word;
        this.timestamp = timestamp;
        this.entryUrl = entryUrl;
        this.definitions = definitions;
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
