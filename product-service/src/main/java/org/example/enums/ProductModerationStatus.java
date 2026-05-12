package org.example.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ProductModerationStatus {
    PENDING,    // Moderatsiyaga yuborildi, kutilmoqda
    APPROVED,   // Moderator tasdiqladi → katalogda ko'rinadi
    REJECTED,   // Moderator rad etdi → seller to'g'rilab qayta yuboradi
    ARCHIVED;   // Faol emas, o'chirilmagan → katalogda ko'rinmaydi
}
