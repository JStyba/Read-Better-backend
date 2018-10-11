package com.readbetter.main.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    private String login;
    private String password;

    public CharSequence getPassword() {
        return password;
    }

    public String getLogin() {
        return login;
    }
}
