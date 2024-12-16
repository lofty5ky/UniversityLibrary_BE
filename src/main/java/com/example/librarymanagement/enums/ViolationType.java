package com.example.librarymanagement.enums;

import lombok.Getter;

@Getter
public enum ViolationType {
    LATE_RETURN("Late Return"),  // Trả sách trễ
    DAMAGED_BOOK("Damaged Book"),  // Sách bị hư hỏng
    LOST_BOOK("Lost Book");  // Sách bị mất

    private final String displayName;

    ViolationType(String displayName) {
        this.displayName = displayName;
    }
}
