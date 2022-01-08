package com.givaudan.galaxy.model.enums;

public enum MethodGCTypeEnum {

    QC("qc"),
    PROD("prod"),
    MONITORING("monitoring"),
    ISDT("isdt"),
    USEFUL("useful");

    private final String label;

    MethodGCTypeEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}
