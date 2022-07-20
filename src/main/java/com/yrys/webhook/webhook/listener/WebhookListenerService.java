package com.yrys.webhook.webhook.listener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yrys.webhook.common.util.DateUtil;
import com.yrys.webhook.repository.WebhookRepository;
import com.yrys.webhook.webhook.listener.model.WebhookListener;

@Service
public class WebhookListenerService {

    @Resource 
    WebhookRepository webhookRepository;

    public List<WebhookListener> getAllListeners() {
        List<WebhookListener> allListeners = new ArrayList<>();
        webhookRepository.findAll().forEach(allListeners::add);
        return allListeners;
    } 
    
    public WebhookListener subscribe(Map<String, Object> body) throws ParseException {
        if (!isValidEndpoint(body.get("destination")) || !isValidEvent(body.get("event"))) 
            return null;
        WebhookListener newListener = new WebhookListener();
        newListener.setDestination(String.valueOf(body.get("destination")));
        newListener.setEvent(String.valueOf(body.get("event")));
        newListener.setIsActive(true);
        newListener.setCreatedTime(DateUtil.toString(DateUtil.getCurrentDate()));
        newListener.setUpdatedTime(DateUtil.toString(DateUtil.getCurrentDate()));
        webhookRepository.save(newListener);
        return newListener;
    }

    public WebhookListener unsubscribe(String id) {
        try {
            WebhookListener listener =  webhookRepository.findById(id).get();
            webhookRepository.deleteById(id);
            return listener;
        } catch (Exception ex) {
            return null;
        } 
    }

    public WebhookListener update(String id, Map<String, Object> body) {
        if (!isValidEndpoint(body.get("destination")) || !isValidEvent(body.get("event")) || !isValidActive(body.get("isActive"))) 
            return null;
        try {
            WebhookListener listener =  webhookRepository.findById(id).get();
            listener.setDestination(String.valueOf(body.get("destination")));
            listener.setEvent(String.valueOf(body.get("event")));
            listener.setIsActive((Boolean)body.get("isActive"));
            listener.setUpdatedTime(DateUtil.toString(DateUtil.getCurrentDate()));
            webhookRepository.save(listener);
            return listener;
        } catch (Exception ex) {
            return null;
        }
    }

    protected Boolean isValidEvent(Object event) {
        if (!(event instanceof String))
            return false;
        String[] allEvents = {"traffic_incident", "open_status_incident", "bus_journey_time",
            "historical_route", "predicted_probability", "prediction_speed", "weather_impact"};
        return Arrays.asList(allEvents).contains(event);
    }

    protected Boolean isValidEndpoint(Object url) {
        if (!(url instanceof String))
            return false;
        String regex = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(String.valueOf(url));
        return matcher.matches();
    }

    protected Boolean isValidActive(Object isActive) {
        return isActive instanceof Boolean;
    }
}