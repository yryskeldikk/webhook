package com.yrys.webhook.webhook.webhookrest;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.yrys.webhook.webhook.listener.WebhookListenerService;
import com.yrys.webhook.webhook.listener.model.WebhookListener;



@RestController
public class WebhookRestController {

    private static final String API_KEY = "";
     
    @Resource
    WebhookListenerService webhookListenerService;

    @GetMapping("/webhooks/all")
    public ResponseEntity<List<WebhookListener>> allListeners(String apiKey) {
        if (!API_KEY.equals(apiKey)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
        List<WebhookListener> allListeners = webhookListenerService.getAllListeners();
        return ResponseEntity.status(HttpStatus.OK).body(allListeners);
    }

    @PostMapping(path="/webhooks/subscribe", consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> subscribeListener(String apiKey, @RequestBody Map<String, Object> body) {
        if (!API_KEY.equals(apiKey)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
        try {
            WebhookListener newListener = webhookListenerService.subscribe(body);
            if (newListener == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Wrong destination or event");
            }
            return ResponseEntity.status(HttpStatus.OK).body(newListener);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    } 
    
    @PostMapping("/webhooks/unsubscribe/{id}")
    public ResponseEntity<Object> unsubscribeListener(String apiKey, @PathVariable("id") String id) {
        if (!API_KEY.equals(apiKey)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
        try {
            WebhookListener listener = webhookListenerService.unsubscribe(id);
            if (listener == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No such listener");
            }
            return ResponseEntity.status(HttpStatus.OK).body(listener);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/webhooks/update/{id}") 
    public ResponseEntity<Object> updateListener(String apiKey, @PathVariable("id") String id, @RequestBody Map<String, Object> body) {
        if (!API_KEY.equals(apiKey)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
        try {
            WebhookListener listener = webhookListenerService.update(id, body);
            if (listener == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Wrong input or no such listener");
            }
            return ResponseEntity.status(HttpStatus.OK).body(listener);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
