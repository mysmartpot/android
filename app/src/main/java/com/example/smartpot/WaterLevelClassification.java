package com.example.smartpot;

public enum WaterLevelClassification {
    EMPTY,
    LOW,
    HIGH,
    FULL;

    static WaterLevelClassification classify(int waterLevel) {
        if (waterLevel >= 600) return EMPTY;
        if (waterLevel >= 500) return LOW;
        if (waterLevel >= 400) return HIGH;
        return FULL;
    }
}
