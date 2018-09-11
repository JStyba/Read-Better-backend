package com.readbetter.main.model.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public abstract class RespFactory {
    public static ResponseEntity<Response> ok(String message) {
        return ResponseEntity.ok(new MessageResponse(message));
    }

    public static ResponseEntity created() {
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public static ResponseEntity badRequest() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    public static <T> ResponseEntity<Response> result(PageResponse<T> response) {
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public static <T> ResponseEntity<T> result(T response) {
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}