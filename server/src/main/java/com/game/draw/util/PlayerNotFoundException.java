package com.game.draw.util;

public class PlayerNotFoundException extends RuntimeException {
    public PlayerNotFoundException(int id) {
        super("Player with id " + id + " does not exist");
    }
}
