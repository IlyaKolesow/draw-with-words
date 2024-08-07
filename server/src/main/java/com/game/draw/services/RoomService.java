package com.game.draw.services;

import com.game.draw.models.Room;
import com.game.draw.repositories.RoomRepository;
import com.game.draw.util.RoomNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RoomService {
    private final RoomRepository repository;

    public RoomService(RoomRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Room create(String name) {
        return repository.save(new Room(name));
    }

    @Transactional
    public Room update(int id, String name) {
        Room room = findById(id);
        room.setName(name);
        return repository.save(room);
    }

    @Transactional
    public void deleteById(int id) {
        repository.deleteById(id);
    }

    public List<Room> findAll() {
        return repository.findAll();
    }

    public Room findById(int id) {
        return repository.findById(id).orElseThrow(() -> new RoomNotFoundException(id));
    }
}
