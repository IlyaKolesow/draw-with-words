package com.game.draw.dto;

import jakarta.validation.constraints.Pattern;

public class RoomNameDTO {

    @Pattern(regexp = "^[\\wа-яА-ЯёЁ]{2,}$", message = "Only letters, numbers, underscores, at least 2 characters")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
