package com.game.draw.services;

import com.game.draw.models.Room;
import com.game.draw.repositories.RoomRepository;
import com.game.draw.util.RoomNotFoundException;
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

    public Room create(String name) {
        return repository.save(new Room(name, unsplash.getImageUrl()));
    }

    public Room updateUrl(int id) {
        Room room = findById(id);
        room.setImageUrl(unsplash.getImageUrl());
        return repository.save(room);
    }

    public void deleteById(int id) {
        repository.deleteById(id);
    }

    public List<Room> findAll() {
        return repository.findAll();
    }

    public Room findById(int id) {
        return repository.findById(id).orElseThrow(() -> new RoomNotFoundException(id));
    }

    private String getImageByRoomId(int roomId) {
        return findById(roomId).getImageUrl();
    }
}
