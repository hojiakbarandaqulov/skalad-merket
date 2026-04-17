package org.example.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ProductModerationStatus {
    PENDING,
    APPROVED,
    REJECTED,
    ARCHIVED
}
