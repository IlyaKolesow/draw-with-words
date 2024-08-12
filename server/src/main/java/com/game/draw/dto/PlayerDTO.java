package com.game.draw.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PlayerDTO {

    @JsonProperty("player_id")
    private Integer playerId;

    private String name;

    @JsonProperty("room_id")
    private Integer roomId;

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }
}
