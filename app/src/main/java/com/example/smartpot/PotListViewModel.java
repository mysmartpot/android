package com.example.smartpot;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class PotListViewModel extends ViewModel {

    private List<Pot> pots = new ArrayList<>();

    public List<Pot> getPots() {
        return pots;
    }

    public void setPots(List<Pot> pots) { this.pots = pots; }
}
