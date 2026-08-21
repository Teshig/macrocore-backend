package org.grimjo.macrocore.infrastructure.api.websocket.dto;

import lombok.Data;

@Data
public class MoveCommandRequest {
    private Long actorId;
    private String direction;
}
