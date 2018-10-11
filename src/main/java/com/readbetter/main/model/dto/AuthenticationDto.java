package com.readbetter.main.model.dto;


import com.readbetter.main.model.AppUser;
import lombok.Data;


@Data
public class AuthenticationDto {
    private String token;
    private AppUser user;

    public AuthenticationDto(String token, AppUser user) {
        this.token = token;
        this.user = user;
    }
}
