package com.yrys.webhook;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.time.Duration;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableAutoConfiguration
public class AppConfig {

	private static final int TIMEOUT = 5000;
	
	@Bean
	public RestTemplate restTemplate()
			throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		
		TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

		SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy)
				.build();

		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();

		RestTemplate restTemplate = new RestTemplateBuilder().setConnectTimeout(Duration.ofMillis(TIMEOUT))
				.setReadTimeout(Duration.ofMillis(TIMEOUT)).additionalMessageConverters(
						new Jaxb2RootElementHttpMessageConverter(), new MappingJackson2HttpMessageConverter(), new StringHttpMessageConverter())
				.build();

		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setHttpClient(httpClient);
		factory.setConnectTimeout(TIMEOUT);
		//factory.setReadTimeout(TIMEOUT);
		restTemplate.setRequestFactory(factory);
		
		return restTemplate;
	}
}
