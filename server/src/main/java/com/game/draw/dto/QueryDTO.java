package com.game.draw.dto;

import jakarta.validation.constraints.Pattern;

public class QueryDTO {

    @Pattern(regexp = "[\\w\\s.,а-яА-ЯёЁ-]+")
    private String query;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
