package com.game.draw.controllers;

import com.game.draw.dto.PlayerDTO;
import com.game.draw.dto.PlayerNameDTO;
import com.game.draw.services.GameService;
import com.game.draw.util.DtoMapper;
import com.game.draw.util.ErrorResponse;
import com.game.draw.util.PlayerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/players")
public class PlayerController {
    private final GameService service;

    public PlayerController(GameService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public PlayerDTO getPlayer(@PathVariable int id) {
        return DtoMapper.mapToPlayerDTO(service.findPlayer(id));
    }

    @GetMapping("/in/{roomId}")
    public List<PlayerDTO> getPlayersInRoom(@PathVariable int roomId) {
        return DtoMapper.mapToPlayerDTO(service.findPlayersInRoom(roomId));
    }

    @PostMapping
    public PlayerDTO create(@RequestBody PlayerNameDTO dto) {
        return DtoMapper.mapToPlayerDTO(service.createPlayer(dto.getName()));
    }

    @PatchMapping("/{id}")
    public PlayerDTO update(@PathVariable int id, @RequestBody PlayerNameDTO dto) {
        return DtoMapper.mapToPlayerDTO(service.updatePlayer(id, dto.getName()));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        service.deletePlayer(id);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(PlayerNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
