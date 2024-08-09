package com.game.draw.services;

import com.game.draw.models.Player;
import com.game.draw.models.Room;
import com.game.draw.repositories.PlayerRepository;
import com.game.draw.repositories.RoomRepository;
import com.game.draw.util.PlayerNotFoundException;
import com.game.draw.util.RoomIsFullException;
import com.game.draw.util.RoomNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GameService {
    private final PlayerRepository playerRepository;
    private final RoomRepository roomRepository;

    public GameService(PlayerRepository playerRepository, RoomRepository roomRepository) {
        this.playerRepository = playerRepository;
        this.roomRepository = roomRepository;
    }

    public Room createRoom(String name) {
        return roomRepository.save(new Room(name));
    }

    public Room updateRoom(int id, String name) {
        Room room = findRoom(id);
        room.setName(name);
        return roomRepository.save(room);
    }

    public void deleteRoom(int id) {
        roomRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Room> findRooms() {
        return roomRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Room findRoom(int id) {
        return roomRepository.findById(id).orElseThrow(() -> new RoomNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public Player findPlayer(int id) {
        return playerRepository.findById(id).orElseThrow(() -> new PlayerNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<Player> findPlayersInRoom(int roomId) {
        Room room = findRoom(roomId);
        return playerRepository.findByRoom(room);
    }

    public Player createPlayer(String name) {
        return playerRepository.save(new Player(name));
    }

    public Player updatePlayer(int id, String name) {
        Player player = findPlayer(id);
        player.setName(name);
        return playerRepository.save(player);
    }

    public void joinTheRoom(int playerId, int roomId) {
        Room room = findRoom(roomId);

        if (room.getPlayers().size() >= 5)
            throw new RoomIsFullException(roomId);

        Player player = findPlayer(playerId);
        player.setRoom(room);
        room.getPlayers().add(player);
        playerRepository.save(player);
        roomRepository.save(room);
    }

    public void leaveTheRoom(int playerId) {
        Player player = findPlayer(playerId);
        Room room = player.getRoom();
        player.setRoom(null);
        room.getPlayers().remove(player);

        if (room.getPlayers().isEmpty())
            deleteRoom(room.getId());
        else
            roomRepository.save(room);

        playerRepository.save(player);
    }

    public void deletePlayer(int id) {
        playerRepository.deleteById(id);
    }
}
