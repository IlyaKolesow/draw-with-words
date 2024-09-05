package com.game.draw.exceptions;

public class UnsplashException extends RuntimeException {
    public UnsplashException(String message, Exception e) {
        super(message, e);
    }
}
