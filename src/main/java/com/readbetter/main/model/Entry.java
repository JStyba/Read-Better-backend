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
    private List<String> definitions;

    @Autowired
    public Entry(List<String> definitions) {

        this.definitions = definitions;
    }





    public List<String> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<String> definitions) {
        this.definitions = definitions;
    }
}
