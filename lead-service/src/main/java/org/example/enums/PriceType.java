package org.example.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PriceType {
    FIXED("fixed"),
    FROM_PRICE("from_price"),
    NEGOTIABLE("negotiable");

    private final String value;

    PriceType(String value) {
        this.value = value;
    }

    @JsonCreator
    public static PriceType fromValue(String value) {
        for (PriceType type : values()) {
            if (type.value.equalsIgnoreCase(value) || type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown price type: " + value);
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
