/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.advantech.webservice;

import com.google.gson.Gson;
import javax.annotation.PostConstruct;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Justin.Yeh
 */
@Component
public class WebAccess {
    private String username;

    private String password;

    protected HttpHeaders headers;

    //Authorization setting
    @PostConstruct
    private void setHeaders() {
        String logonCredentials = username + ":" + password;// id:password
        String base64Credentials = new String(Base64.encodeBase64(logonCredentials.getBytes()));

        HttpHeaders myHeaders = new HttpHeaders();
        myHeaders.add("Authorization", "Basic " + base64Credentials);
        myHeaders.setContentType(MediaType.APPLICATION_JSON);// necessary 
        this.headers = myHeaders;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getJsonString(Object obj) {
        return new Gson().toJson(obj);
    }

    public <C> C jsonToObj(String st, Class<C> clazz) {
        return new Gson().fromJson(st, clazz);
    }

    // POST method
    protected String postJson(String url, String json) {
        HttpEntity<String> request = new HttpEntity<>(json, this.headers);
        ResponseEntity<String> responseEntity
                = new RestTemplate().postForEntity(url, request, String.class);
        return responseEntity.getBody();
    }
}

