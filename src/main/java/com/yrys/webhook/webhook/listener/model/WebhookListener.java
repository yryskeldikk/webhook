package com.yrys.webhook.webhook.listener.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.redis.core.RedisHash;

import lombok.Data;

@Data
@RedisHash("webhook_listener_data")
@TypeAlias("WebhookListenerData")
public class WebhookListener {
    @Id
    private String id;
    private String destination;
    private String event;
    private Boolean isActive;
    private String createdTime;
    private String updatedTime;
}
