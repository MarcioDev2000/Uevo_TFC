package com.monografia.EvoluTCC.dto;

import java.util.UUID;

public class EmailDto {

    private UUID userId;
    private String emailFrom; // Novo campo
    private String emailTo;
    private String subject;
    private String text;
    private boolean isHtml;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getEmailFrom() { // Novo método
        return emailFrom;
    }

    public void setEmailFrom(String emailFrom) { // Novo método
        this.emailFrom = emailFrom;
    }

    public String getEmailTo() {
        return emailTo;
    }

    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isHtml() { 
        return isHtml;
    }

    public void setHtml(boolean isHtml) { 
        this.isHtml = isHtml;
    }
}
