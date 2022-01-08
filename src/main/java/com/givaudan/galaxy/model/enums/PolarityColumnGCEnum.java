package com.givaudan.galaxy.model.enums;

public enum PolarityColumnGCEnum {

    NONE("none"),
    NEUTRAL("neutral"),
    POLAR("polar"),
    NONPOLAR("nonPolar");

    private final String label;

    PolarityColumnGCEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}
