package com.game.draw.util;

import com.game.draw.dto.PlayerDTO;
import com.game.draw.dto.RoomDTO;
import com.game.draw.models.Player;
import com.game.draw.models.Room;

import java.util.ArrayList;
import java.util.List;

public class DtoMapper {
    public static RoomDTO mapToRoomDTO(Room room) {
        RoomDTO dto = new RoomDTO();
        dto.setId(room.getId());
        dto.setName(room.getName());
        return dto;
    }

    public static List<RoomDTO> mapToRoomDTO(List<Room> rooms) {
        List<RoomDTO> dtoList = new ArrayList<>();
        for (Room room : rooms) {
            RoomDTO dto = mapToRoomDTO(room);
            dtoList.add(dto);
        }
        return dtoList;
    }

    public static PlayerDTO mapToPlayerDTO(Player player) {
        PlayerDTO dto = new PlayerDTO();
        dto.setName(player.getName());
        dto.setPlayerId(player.getId());
        if (player.getRoom() == null)
            dto.setRoomId(null);
        else
            dto.setRoomId(player.getRoom().getId());
        return dto;
    }

    public static List<PlayerDTO> mapToPlayerDTO(List<Player> players) {
        List<PlayerDTO> dtoList = new ArrayList<>();
        for (Player player : players) {
            PlayerDTO dto = mapToPlayerDTO(player);
            dtoList.add(dto);
        }
        return dtoList;
    }
}
