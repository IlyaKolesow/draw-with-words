package com.game.draw.repositories;

import com.game.draw.models.Player;
import com.game.draw.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {
    Long countAllByRoomIs(Room room);
}
