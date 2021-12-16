package com.example.smartpot;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LoadingErrorFragment extends Fragment {
    public LoadingErrorFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loading_error, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.retry_button).setOnClickListener(_view -> showLoadingPotsFragment());
        view.findViewById(R.id.vpn_button).setOnClickListener(_view -> openVpnSettings());
    }

    private void showLoadingPotsFragment() {
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_LoadingErrorFragment_to_LoadingPotsFragment);
    }

    private void openVpnSettings() {
        startActivity(new Intent(Settings.ACTION_VPN_SETTINGS));
    }
}