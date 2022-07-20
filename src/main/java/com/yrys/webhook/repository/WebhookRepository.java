package com.yrys.webhook.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.yrys.webhook.webhook.listener.model.WebhookListener;


@Repository
public interface WebhookRepository extends CrudRepository<WebhookListener, String> {

}

