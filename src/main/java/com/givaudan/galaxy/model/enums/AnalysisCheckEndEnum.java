package com.givaudan.galaxy.model.enums;

public enum AnalysisCheckEndEnum {
     
    CRUDE("crude"),
    FINISHED("finished"),
    INTERMEDIATE("intermediate"),
    LOTPROP("lotProp"),
    DIVERS("divers");

    private final String label;

    AnalysisCheckEndEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}
