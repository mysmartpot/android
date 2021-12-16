package com.example.smartpot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import java.util.List;

public class LoadingPotsFragment extends Fragment {

    private PotListViewModel potListViewModel;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loading_pots, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        potListViewModel = new ViewModelProvider(requireActivity()).get(PotListViewModel.class);

        HubApi.loadPots(this::onPotListLoaded, this::showLoadingErrorFragment);
    }

    private void onPotListLoaded(List<Pot> pots) {
        potListViewModel.setPots(pots);
        showPotListFragment();
    }

    private void showPotListFragment() {
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_LoadingPotsFragment_to_PotListFragment);
    }

    private void showLoadingErrorFragment() {
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_LoadingPotsFragment_to_LoadingErrorFragment);
    }
}