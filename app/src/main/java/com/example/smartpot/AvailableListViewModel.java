package com.example.smartpot;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class AvailableListViewModel extends ViewModel {

    private List<AvailablePot> availablePots = new ArrayList<>();

    public List<AvailablePot> getAvailablePots() {
        return availablePots;
    }

    public void setAvailablePots(List<AvailablePot> availablePots) {
        this.availablePots = availablePots;
    }
}
