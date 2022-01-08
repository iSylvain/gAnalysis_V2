package com.givaudan.galaxy.model.enums;

public enum ProductTypeEnum {

    INGREDIENT("ingredient"),
    INTERMEDIATEMATERIAL("intermediatematerial"),
    RECYCLABLEMATERIAL("recyclablematerial"),
    SOLUTION("solution"),
    WASTEMATERIAL("wastematerial"),
    RAWMATERIAL("rawmaterial"),
    REAGENT("reagent"),
    CATALYST("catalyst"),
    STABILIZER("stabiliser"),
    INORGANIC("inorganic"),
    METAL("metal"),
    MINERALACID("mineralacid"),
    MINERALBASE("mineralbase"),
    SOLVENT("solvent"),
    NATURAL("natural"),
    NOTDEFINED("notDefined");

    private final String label;

    ProductTypeEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}
