package com.givaudan.galaxy.model.enums;

public enum GivaudanLocationEnum {
    
    NONE("none"),
    VERNIER("vernier"),
    SANCELONI("sanCeloni");

    private final String label;

    GivaudanLocationEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}
