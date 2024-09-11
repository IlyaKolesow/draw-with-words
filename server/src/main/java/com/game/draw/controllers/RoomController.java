package com.game.draw.controllers;

import com.game.draw.dto.*;
import com.game.draw.services.GameService;
import com.game.draw.util.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
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
    public RoomDTO createRoom(@RequestBody @Valid RoomNameDTO dto) {
        return DtoMapper.mapToRoomDTO(service.createRoom(dto.getName()));
    }

    @PatchMapping("/{id}")
    public RoomDTO update(@PathVariable int id, @RequestBody @Valid RoomNameDTO dto) {
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

    @PostMapping("/status")
    public RoomDTO setRoomStatus(@RequestBody RoomStatusDTO dto) {
        return DtoMapper.mapToRoomDTO(service.setRoomStatus(dto.getRoomId(), dto.getStatus()));
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<Map<String, String>> checkStatus(@PathVariable int id) {
        Map<String, String> response = new HashMap<>();
        response.put("status", service.checkRoomStatus(id));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
