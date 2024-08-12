package com.game.draw.controllers;

import com.game.draw.dto.PlayerIdDTO;
import com.game.draw.dto.RoomDTO;
import com.game.draw.dto.RoomNameDTO;
import com.game.draw.dto.RoomPlayerIdsDTO;
import com.game.draw.services.GameService;
import com.game.draw.util.DtoMapper;
import com.game.draw.util.ErrorResponse;
import com.game.draw.util.RoomIsFullException;
import com.game.draw.util.RoomNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(RoomNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(RoomIsFullException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }
}
