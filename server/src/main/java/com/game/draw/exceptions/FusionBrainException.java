package com.game.draw.exceptions;

public class FusionBrainException extends RuntimeException {
    public FusionBrainException(String message, Exception e) {
        super(message, e);
    }
}
