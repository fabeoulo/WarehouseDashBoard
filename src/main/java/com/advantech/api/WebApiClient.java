/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.advantech.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

/**
 *
 * @author Justin.Yeh
 */
@Component
public class WebApiClient {
    
    private static final Logger log = LoggerFactory.getLogger(WebApiClient.class);
    
    private String baseUrl;
    private WebClient webClient;
    
    public WebApiClient() {
    }
    
    public String getBaseUrl() {
        return baseUrl;
    }
    
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
    
    public WebClient getWebClient() {
        return webClient;
    }
    
    @Autowired
    public void setWebClient() {
        //set timeout 5s
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(5));

        //set cache 10MB and init
        this.webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(config -> {
                            config.defaultCodecs().maxInMemorySize(10 * 1024 * 1024);
                        })
                        .build())
                .build();
    }
    
    public WebApiUser getUserInAtmc(String jobnumber) {
        Mono<Object[]> body = webClient
                .get()
                .uri(baseUrl + jobnumber)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Object[].class);
        try {
            Object[] bodyObject = body.block();
            List<WebApiUser> users = objectToUser(bodyObject);
            return users.isEmpty() ? null : users.get(0);
        } catch (Exception e) { //WebClientException
            log.error(e.toString());
            return null;
        }
    }
    
    private List<WebApiUser> objectToUser(Object[] bodyObject) {
        if (bodyObject == null) {
            return new ArrayList<>();
        }
        
        ObjectMapper mapper = new ObjectMapper();
        List<WebApiUser> l = mapper.convertValue(bodyObject, new TypeReference<List<WebApiUser>>() {
        });
        
        return l;
    }
}
