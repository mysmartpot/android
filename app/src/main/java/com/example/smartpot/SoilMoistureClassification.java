package com.example.smartpot;

public enum SoilMoistureClassification {
    DRY,
    MOIST,
    DAMP,
    WET;

    static SoilMoistureClassification classify(int soilMoisture) {
        if (soilMoisture >= 600) return DRY;
        if (soilMoisture >= 500) return MOIST;
        if (soilMoisture >= 400) return DAMP;
        return WET;
    }
}
