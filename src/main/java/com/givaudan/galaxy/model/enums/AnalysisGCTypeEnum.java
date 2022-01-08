package com.givaudan.galaxy.model.enums;

public enum AnalysisGCTypeEnum {

    ISDT("isdt"),
    RECTIFIED("rectified"),
    CRUDE("crude"),
    MONITORING("monitoring");

    private final String label;

    AnalysisGCTypeEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}
