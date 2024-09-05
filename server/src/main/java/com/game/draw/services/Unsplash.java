package com.game.draw.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.draw.exceptions.UnsplashException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class Unsplash {
    private final String URL = "https://api.unsplash.com/photos/random";
    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    @Value("${unsplash.key}")
    private String key;

    public Unsplash(RestTemplate restTemplate, ObjectMapper mapper) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
    }

    public String getImageUrl() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", key);
        ResponseEntity<String> response = restTemplate.exchange(
                URL,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        );

        JsonNode jsonNode;
        try {
            jsonNode = mapper.readTree(response.getBody());
        } catch (JsonProcessingException e) {
            throw new UnsplashException("Error parsing JSON response", e);
        }

        return jsonNode.get("urls").get("raw").asText();
    }
}
