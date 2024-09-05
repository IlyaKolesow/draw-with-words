package com.game.draw.exceptions;

public class RoomIsFullException extends RuntimeException {
    public RoomIsFullException(int id) {
        super("Room with id " + id + " is full");
    }
}
