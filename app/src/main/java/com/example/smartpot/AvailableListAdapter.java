package com.example.smartpot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

public class AvailableListAdapter extends RecyclerView.Adapter<AvailableListAdapter.ViewHolder>{

    private AvailablePot[] pots;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private AvailablePot pot;
        private final TextView addr;
        private final CardView card;

        public ViewHolder(View view) {
            super(view);

            addr = (TextView) view.findViewById(R.id.addr);
            card = (CardView) view.findViewById(R.id.card);

            card.setOnClickListener(this::onCardClicked);
        }

        public void bind(AvailablePot pot) {
            this.pot = pot;
            addr.setText(pot.getAddr());
        }

        public void onCardClicked(View _card) {
            Context context = FragmentManager.findFragment(card).getContext();
            ConnectPotDialogBuilder dialog = new ConnectPotDialogBuilder(pot, context);
            dialog.setConfirmCallback(this::onDialogConfirmed);
            dialog.show();
        }

        public void onDialogConfirmed(String name) {
            HubApi.connectPot(pot.getAddr(), name, this::onPotConnected, this::onConnectPotError);
        }

        public void onPotConnected(Void _x) {
            FragmentManager.findFragment(card).getActivity().onBackPressed();
        }

        private void onConnectPotError() {
            Snackbar snackbar = Snackbar.make(
                    card,
                    "Failed to connect " + pot.getAddr() + ".",
                    Snackbar.LENGTH_LONG
            );
            snackbar.show();
        }
    }

    public AvailableListAdapter(AvailablePot[] pots) {
        this.pots = pots;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.available_list_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(AvailableListAdapter.ViewHolder viewHolder, final int position) {
        viewHolder.bind(pots[position]);
    }

    @Override
    public int getItemCount() {
        return pots.length;
    }

}
