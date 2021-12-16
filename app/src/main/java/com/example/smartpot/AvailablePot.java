package com.example.smartpot;

public class AvailablePot {

    private String addr;
    private double rssi;

    public AvailablePot() {

    }

    public AvailablePot(String addr, double rssi) {
        this.addr = addr;
        this.rssi = rssi;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public double getRssi() {
        return rssi;
    }

    public void setRssi(double rssi) {
        this.rssi = rssi;
    }
}
