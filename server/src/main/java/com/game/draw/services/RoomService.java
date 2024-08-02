package com.game.draw.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.game.draw.models.Room;
import com.game.draw.repositories.RoomRepository;
import com.game.draw.util.RoomNotFoundException;
import com.game.draw.util.Unsplash;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {
    private final RoomRepository repository;
    private final Unsplash unsplash;

    public RoomService(RoomRepository repository, Unsplash unsplash) {
        this.repository = repository;
        this.unsplash = unsplash;
    }

    public Room save(String name) throws JsonProcessingException {
        return repository.save(new Room(name, unsplash.getImageUrl()));
    }

    public void deleteById(int id) {
        repository.deleteById(id);
    }

    public List<Room> findAll() {
        return repository.findAll();
    }

    public Room findById(int id) {
        return repository.findById(id).orElseThrow(() -> new RoomNotFoundException("Room with id " + id + "does not exist"));
    }

    private String getImage(int roomId) {
        return findById(roomId).getImageUrl();
    }
}
