package com.readbetter.main.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class LoginRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @JsonIgnore
    private Long id;
    private LocalDateTime loggedInAt;
    private LocalDateTime loggedOutAt;
    private Long timeSpentOnline;
    @ManyToOne
    @JsonIgnore
    private AppUser appUser;



}
