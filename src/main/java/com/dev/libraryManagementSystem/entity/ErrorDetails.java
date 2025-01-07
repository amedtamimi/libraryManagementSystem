package com.dev.libraryManagementSystem.entity;

import org.springframework.http.HttpStatus;

import java.util.Date;

public class ErrorDetails {
    private HttpStatus status;
    private String message;
    private String details;
    private Date timestamp;

    public ErrorDetails(HttpStatus status, String message, String details, Date timestamp) {
        this.status = status;
        this.message = message;
        this.details = details;
        this.timestamp = timestamp;
    }

    // Getters and setters

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
