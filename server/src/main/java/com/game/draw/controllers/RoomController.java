package com.game.draw.controllers;

import com.game.draw.dto.RoomDTO;
import com.game.draw.dto.RoomNameDTO;
import com.game.draw.services.RoomService;
import com.game.draw.util.DtoMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {
    private final RoomService service;

    public RoomController(RoomService service) {
        this.service = service;
    }

    @GetMapping
    public List<RoomDTO> getRooms() {
        return DtoMapper.map(service.findAll());
    }

    @GetMapping("/{id}")
    public RoomDTO getRoom(@PathVariable int id) {
        return DtoMapper.map(service.findById(id));
    }

    @PostMapping
    public RoomDTO createRoom(@RequestBody RoomNameDTO dto) {
        return DtoMapper.map(service.create(dto.getName()));
    }

    @PatchMapping("/{id}")
    public RoomDTO update(@PathVariable int id, @RequestBody RoomNameDTO dto) {
        return DtoMapper.map(service.update(id, dto.getName()));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        service.deleteById(id);
    }
}
