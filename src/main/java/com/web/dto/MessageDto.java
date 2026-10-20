package com.web.dto;

import java.sql.Timestamp;

public class MessageDto {
    private String fromUser;
    private String toUser;
    private String message;
    private Timestamp createdAt;

    public MessageDto(String fromUser, String toUser, String message) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.message = message;
    }

    // Getters and setters
    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
