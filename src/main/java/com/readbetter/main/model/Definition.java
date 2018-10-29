package com.readbetter.main.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@NoArgsConstructor
public class Definition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    private String definition;

    @OneToOne
    @JsonBackReference
    private Entry entry;

    public Definition(String definition, Entry entry) {
        this.definition = definition;
        this.entry = entry;
    }

    public Definition(String definition) {
        this.definition = definition;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    @Override
    public String toString() {
        return "Definition{" +
                "definition='" + definition + '\'' +
                '}';
    }
}
