package com.game.draw.util;

public class UnsplashException extends RuntimeException {
    public UnsplashException(String message, Exception e) {
        super(message, e);
    }
}
