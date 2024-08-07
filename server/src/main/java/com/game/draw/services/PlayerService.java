package com.game.draw.services;

import com.game.draw.models.Player;
import com.game.draw.models.Room;
import com.game.draw.repositories.PlayerRepository;
import com.game.draw.repositories.RoomRepository;
import com.game.draw.util.PlayerNotFoundException;
import com.game.draw.util.RoomIsFullException;
import com.game.draw.util.RoomNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final RoomRepository roomRepository;

    public PlayerService(PlayerRepository playerRepository, RoomRepository roomRepository) {
        this.playerRepository = playerRepository;
        this.roomRepository = roomRepository;
    }

    public Player findById(int id) {
        return playerRepository.findById(id).orElseThrow(() -> new PlayerNotFoundException(id));
    }

    public Player create(String name) {
        return playerRepository.save(new Player(name));
    }

    public void joinTheRoom(int playerId, int roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RoomNotFoundException(roomId));
        long playersInRoom = playerRepository.countAllByRoomIs(room);

        if (playersInRoom < 5) {
            Player player = findById(playerId);
            player.setRoom(room);
            playerRepository.save(player);
        } else {
            throw new RoomIsFullException(roomId);
        }
    }

    public void leaveTheRoom(int id) {
        Player player = findById(id);
        player.setRoom(null);
        playerRepository.save(player);
    }

}
