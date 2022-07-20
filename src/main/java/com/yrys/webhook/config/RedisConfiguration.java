package com.yrys.webhook.config;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfiguration {

	@Autowired
	private RedisConnectionConfig redisConnectionConfig;

	@Bean
	public JedisPoolConfig jedisPoolConfig() {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxIdle(100000);
		config.setMinIdle(5);
		config.setMaxTotal(200000);
		config.setMaxWaitMillis(100000000);
		return config;
	}

	@Bean
	public JedisConnectionFactory jedisConnectionFactory(JedisPoolConfig config) {

		Duration connectTimeout = Duration.ofMillis(300000 * 1000);

		JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jpcb = JedisClientConfiguration.builder()
				.readTimeout(connectTimeout).connectTimeout(connectTimeout).usePooling();

		jpcb.poolConfig(config);
		JedisClientConfiguration jedisClientConfiguration = jpcb.build();

		RedisStandaloneConfiguration redisConfiguration;
		
		if("true".equals(redisConnectionConfig.getClusterMode())) {
			List<String> clusterNodes = Arrays.asList(redisConnectionConfig.getClusterNodeHosts().split(","));
			RedisClusterConfiguration redisStandaloneConfiguration = new RedisClusterConfiguration(clusterNodes);

			return new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration);
		} else {
			redisConfiguration = new RedisStandaloneConfiguration();
			redisConfiguration.setDatabase(redisConnectionConfig.getDatabase());
			redisConfiguration.setHostName(redisConnectionConfig.getHost());
			redisConfiguration.setPort(6379);
			
			return new JedisConnectionFactory(redisConfiguration, jedisClientConfiguration);
		}
		
	}

	@Bean
	public RedisTemplate<Object, Object> redisTemplate(JedisConnectionFactory connectionFactory) {
		RedisTemplate<Object, Object> template = new RedisTemplate<>();

		template.setConnectionFactory(connectionFactory);
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new StringRedisSerializer());

		return template;
	}

}
