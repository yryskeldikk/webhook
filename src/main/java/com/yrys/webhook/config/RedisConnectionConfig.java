package com.yrys.webhook.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
import lombok.NoArgsConstructor;

@ConfigurationProperties(prefix = "spring.redis", ignoreUnknownFields = false)
@NoArgsConstructor
@Data
public class RedisConnectionConfig {

	private int database;

	private String host;

	private String port;
	
	private double timeout;
	
	private String clusterMode;
	
	private String clusterNodeHosts;

}
