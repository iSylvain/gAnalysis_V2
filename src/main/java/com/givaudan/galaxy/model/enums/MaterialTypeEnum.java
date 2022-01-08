package com.givaudan.galaxy.model.enums;

public enum MaterialTypeEnum {
    
    PRODUCT("product"),
    MIXTURE("mixture"),
    COMPO("compo"),
    NOTDEFINED("notDefined");

    private final String label;

    MaterialTypeEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}
