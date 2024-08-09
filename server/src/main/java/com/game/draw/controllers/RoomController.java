package com.game.draw.controllers;

import com.game.draw.dto.PlayerIdDTO;
import com.game.draw.dto.RoomDTO;
import com.game.draw.dto.RoomNameDTO;
import com.game.draw.dto.RoomPlayerIdsDTO;
import com.game.draw.services.GameService;
import com.game.draw.util.DtoMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {
    private final GameService service;

    public RoomController(GameService service) {
        this.service = service;
    }

    @GetMapping
    public List<RoomDTO> getRooms() {
        return DtoMapper.mapToRoomDTO(service.findRooms());
    }

    @GetMapping("/{id}")
    public RoomDTO getRoom(@PathVariable int id) {
        return DtoMapper.mapToRoomDTO(service.findRoom(id));
    }

    @PostMapping
    public RoomDTO createRoom(@RequestBody RoomNameDTO dto) {
        return DtoMapper.mapToRoomDTO(service.createRoom(dto.getName()));
    }

    @PatchMapping("/{id}")
    public RoomDTO update(@PathVariable int id, @RequestBody RoomNameDTO dto) {
        return DtoMapper.mapToRoomDTO(service.updateRoom(id, dto.getName()));
    }

    @PostMapping("/join")
    public void join(@RequestBody RoomPlayerIdsDTO dto) {
        service.joinTheRoom(dto.getPlayerId(), dto.getRoomId());
    }

    @PostMapping("/leave")
    public void leave(@RequestBody PlayerIdDTO dto) {
        service.leaveTheRoom(dto.getId());
    }
}
