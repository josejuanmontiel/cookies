package com.example.demo;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class DemoApplication {

    @Bean
    public RestTemplate restTemplate() {
    	// A parte de usar HTTPClient, deshabilitamos el uso de cookies...
    	HttpClientBuilder client = HttpClientBuilder.create();
    	client.disableCookieManagement();
    	// TODO Con los certificados actuales apra desarrollo...
    	// 2017-02-01 13:06:37,110 -  - [http-nio-8090-exec-1] -  -  - DEBUG o.a.h.c.ssl.DefaultHostnameVerifier - Certificate for <local.dev.mango.com> doesn't match common name of the certificate subject: test_shop
    	// javax.net.ssl.SSLException: Certificate for <local.dev.mango.com> doesn't match common name of the certificate subject: test_shop
    	client.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE);
    	
    	
    	// Deshabilitamos la compresion gzip
    	// client.disableContentCompression();
    	
    	HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(client.build());
    	
    	RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
//    	restTemplate.setErrorHandler(new MyResponseErrorHandler(messageConverter));
        return restTemplate;
    }
	
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}

