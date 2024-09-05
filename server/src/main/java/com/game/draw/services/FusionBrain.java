package com.game.draw.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.draw.exceptions.FusionBrainException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class FusionBrain {
    private final String URL = "https://api-key.fusionbrain.ai/";
    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    @Value("${fusion-brain.key}")
    private String key;

    @Value("${fusion-brain.secret}")
    private String secret;

    public FusionBrain(RestTemplate restTemplate, ObjectMapper mapper) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
    }

    private JsonNode parseResponse(ResponseEntity<String> response) {
        JsonNode jsonNode;
        try {
            jsonNode = mapper.readTree(response.getBody());
        } catch (JsonProcessingException e) {
            throw new FusionBrainException("Error parsing JSON response", e);
        }
        return jsonNode;
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Key", key);
        headers.add("X-Secret", secret);
        return headers;
    }

    private HttpHeaders createHeaders(MediaType mediaType) {
        HttpHeaders headers = createHeaders();
        headers.setContentType(mediaType);
        return headers;
    }

    private String getModelID() {
        ResponseEntity<String> response = restTemplate.exchange(
                URL + "key/api/v1/models",
                HttpMethod.GET,
                new HttpEntity<>(createHeaders()),
                String.class
        );

        JsonNode jsonNode = parseResponse(response);
        return jsonNode.get(0).get("id").asText();
    }

    private MultiValueMap<String, Object> buildRequestBody(String query) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", "GENERATE");
        params.put("numImages", 1);
        params.put("width", 1024);
        params.put("height", 1024);
        params.put("style", "UHD");

        Map<String, String> generateParams = new HashMap<>();
        generateParams.put("query", query);

        params.put("generateParams", generateParams);

        String jsonParams;
        try {
            jsonParams = mapper.writeValueAsString(params);
        } catch (JsonProcessingException e) {
            throw new FusionBrainException("Error converting to JSON", e);
        }

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("params", new HttpEntity<>(jsonParams, createHeaders(MediaType.APPLICATION_JSON)));
        body.add("model_id", new HttpEntity<>(getModelID(), createHeaders(MediaType.TEXT_PLAIN)));

        return body;
    }

    public String generate(String query) {
        MultiValueMap<String, Object> body = buildRequestBody(query);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, createHeaders(MediaType.MULTIPART_FORM_DATA));

        ResponseEntity<String> response = restTemplate.exchange(
                URL + "key/api/v1/text2image/run",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        JsonNode jsonNode = parseResponse(response);
        return jsonNode.get("uuid").asText();
    }

    public String checkGeneration(String uuid) {
        ResponseEntity<String> response = restTemplate.exchange(
                URL + "/key/api/v1/text2image/status/" + uuid,
                HttpMethod.GET,
                new HttpEntity<>(createHeaders()),
                String.class
        );

        JsonNode jsonNode = parseResponse(response);

        if (jsonNode.get("status").asText().equals("DONE"))
            return jsonNode.get("images").get(0).asText();
        else
            return "PROCESSING";
    }
}
