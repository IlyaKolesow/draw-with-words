package com.game.draw.exceptions;

public class RoomNotFoundException extends RuntimeException {
    public RoomNotFoundException(int id) {
        super("Room with id " + id + " does not exist");
    }
}
