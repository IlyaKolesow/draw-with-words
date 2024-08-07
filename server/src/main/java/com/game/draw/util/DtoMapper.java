package com.game.draw.util;

import com.game.draw.dto.RoomDTO;
import com.game.draw.models.Room;

import java.util.ArrayList;
import java.util.List;

public class DtoMapper {
    public static RoomDTO map(Room room) {
        RoomDTO dto = new RoomDTO();
        dto.setId(room.getId());
        dto.setName(room.getName());
        return dto;
    }

    public static List<RoomDTO> map(List<Room> rooms) {
        List<RoomDTO> dtoList = new ArrayList<>();
        for (Room room : rooms) {
            RoomDTO dto = map(room);
            dtoList.add(dto);
        }
        return dtoList;
    }

}
