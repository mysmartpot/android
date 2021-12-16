package com.example.smartpot;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashSet;
import java.util.Set;

public class Pot {
    private int id;
    private String name;
    @JsonIgnore
    private PotStatus potStatus;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PotStatus getPotStatus() {
        return potStatus;
    }

    public void setPotStatus(PotStatus potStatus) { this.potStatus = potStatus; }

    public static class PotStatus {
        private boolean online;
        private Measurement measurement;
        private Watered watered;

        public boolean isOnline() {
            return online;
        }

        public void setOnline(boolean online) {
            this.online = online;
        }

        public Measurement getMeasurement() {
            return measurement;
        }

        public void setMeasurement(Measurement measurement) {
            this.measurement = measurement;
        }

        public Watered getWatered() {
            return watered;
        }

        public void setWatered(Watered watered) {
            this.watered = watered;
        }

        public static class Measurement {

            @JsonProperty("soil-moisture")
            private int soilMoisture;
            @JsonProperty("water-level")
            private int waterLevel;
            private String timestamp;

            public int getSoilMoisture() { return soilMoisture; }

            public void setSoilMoisture(int soilMoisture) {
                this.soilMoisture = soilMoisture;
            }

            public int getWaterLevel() {
                return waterLevel;
            }

            public void setWaterLevel(int waterLevel) {
                this.waterLevel = waterLevel;
            }

            public String getTimestamp() {
                return timestamp;
            }

            public void setTimestamp(String timestamp) {
                this.timestamp = timestamp;
            }
        }

        public static class Watered {
            private int amount;
            private String timestamp;
            private boolean completed;

            public int getAmount() {
                return amount;
            }

            public void setAmount(int amount) {
                this.amount = amount;
            }

            public String getTimestamp() {
                return timestamp;
            }

            public void setTimestamp(String timestamp) {
                this.timestamp = timestamp;
            }

            public boolean isCompleted() {
                return completed;
            }

            public void setCompleted(boolean completed) {
                this.completed = completed;
            }
        }
    }
}
