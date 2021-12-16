package com.example.smartpot;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoadAvailableFragment extends Fragment {

    AvailableListViewModel availableListViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_load_available, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        availableListViewModel = new ViewModelProvider(requireActivity()).get(AvailableListViewModel.class);
        loadAvailablePots();
    }

    private void loadAvailablePots() {
        HubApi.loadAvailablePots(this::onAvailablePotsLoaded, this::onLoadingFailed);
    }

    private void onAvailablePotsLoaded(List<AvailablePot> availablePots) {
        availableListViewModel.setAvailablePots(availablePots);
        NavHostFragment.findNavController(LoadAvailableFragment.this)
                .navigate(R.id.action_LoadAvailableFragment_to_ListAvailableFragment);
    }

    private void onLoadingFailed() {
        getActivity().onBackPressed();
    }
}