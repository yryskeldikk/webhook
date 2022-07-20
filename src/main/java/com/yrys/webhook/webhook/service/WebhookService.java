package com.yrys.webhook.webhook.service;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.yrys.webhook.webhook.listener.WebhookListenerService;
import com.yrys.webhook.webhook.listener.model.WebhookListener;
import com.yrys.webhook.webhook.service.notification.Notification;

@Service
public class WebhookService {

    @Autowired
    private RestTemplate restTemplate;

    @Resource
    WebhookListenerService webhookListenerService;
    
    private static final Logger logger = LoggerFactory.getLogger(WebhookService.class);

    public void sendNotification(String event) {
        logger.info("Starting Notification Send for " + event);
        Notification notification = new Notification("New " + event + " data available");
        for (WebhookListener listener: webhookListenerService.getAllListeners()) {
            if (listener.getEvent().equals(event) && listener.getIsActive()) {
                HttpEntity<Notification> request = new HttpEntity<>(notification);
		        String resp =  restTemplate.postForObject(listener.getDestination(), request, String.class);
            }  
        }
        logger.info("Notification Send Completed for " + event);
    }
}