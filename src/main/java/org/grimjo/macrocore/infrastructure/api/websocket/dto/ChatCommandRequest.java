package org.grimjo.macrocore.infrastructure.api.websocket.dto;

import lombok.Data;

@Data
public class ChatCommandRequest {
    private Long speakerId;
    private String message;
}
