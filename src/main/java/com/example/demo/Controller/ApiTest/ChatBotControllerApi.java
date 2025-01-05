package com.example.demo.Controller.ApiTest;


import com.example.demo.Service.ChatBotService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/chatbot")
public class ChatBotControllerApi {
    @Autowired
    private ChatBotService chatBotService;

    @PostMapping("/message")
    public CompletableFuture<ResponseEntity<String>> chat(@RequestBody String input) {
        System.out.println(input);
        ObjectMapper objectMapper = new ObjectMapper();
        String messageContent = "";
        try {
            JsonNode jsonNode = objectMapper.readTree(input);
            messageContent = jsonNode.get("message").asText();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return chatBotService.getResponse(messageContent)
                .thenApply(ResponseEntity::ok);
    }
}