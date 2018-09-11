package com.readbetter.main.model.dto;

public class Response {
    private Long timestamp;

    public Response() {
        this.timestamp = System.currentTimeMillis();
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}