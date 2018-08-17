package com.readbetter.main.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.util.List;

@Data


public class Entry {
    @JsonManagedReference
    String word;
    @JsonManagedReference
    private List<String> definitions;

    @Autowired
    public Entry(String word, List<String> definitions) {
        this.word = word;
        this.definitions = definitions;
    }
}
