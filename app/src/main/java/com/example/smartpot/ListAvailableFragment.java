package com.example.smartpot;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ListAvailableFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private AvailableListViewModel availableListViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;

    private TextView noAvailableLabel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_available, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        availableListViewModel = new ViewModelProvider(requireActivity()).get(AvailableListViewModel.class);
        noAvailableLabel = view.findViewById(R.id.no_available);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        updatePotListAdapter();
    }

    @Override
    public void onRefresh() {
        HubApi.loadAvailablePots(this::onPotListLoaded, this::onLoadingFailed);
    }

    private void onPotListLoaded(List<AvailablePot> pots) {
        availableListViewModel.setAvailablePots(pots);
        updatePotListAdapter();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void onLoadingFailed() {
        getActivity().onBackPressed();
    }

    public void updatePotListAdapter() {
        List<AvailablePot> availablePots = availableListViewModel.getAvailablePots();

        RecyclerView availableListView = (RecyclerView) getView().findViewById(R.id.availableList);
        AvailablePot[] availablePotArr = availablePots.toArray(new AvailablePot[availablePots.size()]);
        AvailableListAdapter adapter = new AvailableListAdapter(availablePotArr);
        availableListView.setAdapter(adapter);
        availableListView.setLayoutManager(new LinearLayoutManager(getActivity()));

        noAvailableLabel.setVisibility(availablePots.isEmpty() ? View.VISIBLE : View.GONE);
    }
}