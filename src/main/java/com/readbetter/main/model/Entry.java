package com.readbetter.main.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class Entry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    private String word;
    @OneToMany (cascade = CascadeType.ALL, mappedBy = "entry")
    @JsonManagedReference
    private List<Definition> definitions;
    @ManyToOne
    @JsonIgnore
    private AppUser appUser;

    public Entry(String word, List<Definition> definitions) {
        this.definitions = definitions;
    }
}
