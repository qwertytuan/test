package com.example.demo.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Service
public class ChatBotService {

    private final RestTemplate restTemplate;
    @Value("${groq.ai.endpoint}")
    private String groqAiEndpoint;
    @Value("${groq.ai.api.key}")
    private String groqAiApiKey;
    @Value("${groq.ai.model}")
    private String groqAiModel;

    public ChatBotService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CompletableFuture<String> getResponse(String input) {
        return CompletableFuture.supplyAsync(() -> {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + groqAiApiKey);
            String query = "{\"model\": \"" + groqAiModel + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + input + "\"}]}";
            HttpEntity<String> entity = new HttpEntity<>(query, headers);
            String response =restTemplate.exchange(groqAiEndpoint, HttpMethod.POST, entity, String.class).getBody();

            ObjectMapper objectMapper = new ObjectMapper();
            String content = "";
            try {
                JsonNode jsonNode = objectMapper.readTree(response);
                content = jsonNode.path("choices").get(0).path("message").path("content").asText();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return content;
        });
    }
}