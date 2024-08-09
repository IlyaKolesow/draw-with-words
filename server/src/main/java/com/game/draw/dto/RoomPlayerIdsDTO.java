package com.game.draw.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RoomPlayerIdsDTO {

    @JsonProperty("player_id")
    private int playerId;

    @JsonProperty("room_id")
    private int roomId;

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
}
