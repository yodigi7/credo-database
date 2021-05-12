package com.credo.database.domain.enumeration;

/**
 * The YesNoEmpty enumeration.
 */
public enum YesNoEmpty {
    YES("Y"),
    NO("N"),
    EMPTY;

    private String value;

    YesNoEmpty() {}

    YesNoEmpty(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
