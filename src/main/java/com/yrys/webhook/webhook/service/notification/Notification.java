package com.yrys.webhook.webhook.service.notification;

public class Notification {
    private String text;

    public Notification(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }
}
