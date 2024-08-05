package com.game.draw.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class Unsplash {
    private final String url;
    private final RestTemplate restTemplate;
    private final HttpHeaders headers;
    private final ObjectMapper mapper;

    @Value("${unsplash.key}")
    private String key;

    public Unsplash(RestTemplate restTemplate, HttpHeaders headers, ObjectMapper mapper) {
        this.restTemplate = restTemplate;
        this.headers = headers;
        this.mapper = mapper;
        url = "https://api.unsplash.com/photos/random";
    }

    public String getImageUrl() {
        headers.add("Authorization", key);
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        );

        JsonNode jsonNode;
        try {
            jsonNode = mapper.readTree(response.getBody());
        } catch (JsonProcessingException e) {
            throw new UnsplashException(e);
        }

        return jsonNode.get("urls").get("raw").asText();
    }
}
