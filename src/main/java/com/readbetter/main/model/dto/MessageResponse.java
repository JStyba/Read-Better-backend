package com.readbetter.main.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;



@Data
public class MessageResponse extends  Response{
    private String message;

    public MessageResponse(String message) {
        this.message = message;
    }
}