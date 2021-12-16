package com.example.smartpot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class PotListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private PotListViewModel potListViewModel;
    private RecyclerView potListView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View emptyContentView;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pot_list, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        potListViewModel = new ViewModelProvider(requireActivity()).get(PotListViewModel.class);

        emptyContentView = (View) getView().findViewById(R.id.empty_content);

        potListView = (RecyclerView) getView().findViewById(R.id.potList);
        potListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updatePotListAdapter();

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        HubApi.loadPots(this::onPotListLoaded, this::showLoadingErrorFragment);
    }

    @Override
    public void onResume() {
        super.onResume();
        swipeRefreshLayout.setRefreshing(true);
        HubApi.loadPots(this::onPotListLoaded, this::showLoadingErrorFragment);
    }

    private void onPotListLoaded(List<Pot> pots) {
        potListViewModel.setPots(pots);
        updatePotListAdapter();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void updatePotListAdapter() {
        List<Pot> pots = potListViewModel.getPots();
        PotListAdapter adapter = new PotListAdapter(pots);
        potListView.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                updateEmptyContentVisibility();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                updateEmptyContentVisibility();
            }
        });
        updateEmptyContentVisibility();
    }

    private void updateEmptyContentVisibility() {
        List<Pot> pots = potListViewModel.getPots();
        emptyContentView.setVisibility(pots.isEmpty() ? View.VISIBLE : View.INVISIBLE);
    }

    private void showLoadingErrorFragment() {
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_PotListFragment_to_LoadingErrorFragment);
    }
}