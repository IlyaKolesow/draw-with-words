package com.game.draw.repositories;

import com.game.draw.models.Player;
import com.game.draw.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {
    List<Player> findByRoom(Room room);
}
