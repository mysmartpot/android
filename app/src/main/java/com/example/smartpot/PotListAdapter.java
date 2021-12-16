package com.example.smartpot;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.slider.Slider;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class PotListAdapter extends RecyclerView.Adapter<PotListAdapter.PotListItemViewHolder> {

    private final List<Pot> pots;

    public PotListAdapter(List<Pot> pots) { this.pots = pots; }

    @Override
    public PotListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.pot_list_item, viewGroup, false);

        return new PotListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PotListItemViewHolder viewHolder, final int position) {
        viewHolder.bind(position);
    }

    @Override
    public int getItemCount() {
        return pots.size();
    }

    public class PotListItemViewHolder extends RecyclerView.ViewHolder {
        private Pot pot;

        private final View view;
        private final TextView name;
        private final TextView subtitle;
        private final Chip offlineChip;
        private final Chip soilMoistureChip;
        private final Chip waterLevelChip;
        private final ImageButton menuButton;
        private final MaterialCardView card;
        private final ProgressBar progressBar;
        private final ChipGroup chipGroup;

        public PotListItemViewHolder(View view) {
            super(view);
            this.view = view;

            name = (TextView) view.findViewById(R.id.name);
            subtitle = (TextView) view.findViewById(R.id.subtitle);
            progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
            chipGroup = (ChipGroup) view.findViewById(R.id.chip_group);
            soilMoistureChip = (Chip) view.findViewById(R.id.soil_moisture_chip);
            waterLevelChip = (Chip) view.findViewById(R.id.water_level_chip);

            offlineChip = (Chip) view.findViewById(R.id.offline_chip);
            offlineChip.setOnClickListener(this::onOfflineChipClick);

            menuButton = (ImageButton) view.findViewById(R.id.menuButton);
            menuButton.setOnClickListener(this::onMenuButtonClick);

            card = (MaterialCardView) view.findViewById(R.id.pot_card);
            card.setOnClickListener(this::onCardClick);
        }

        public void bind(int position) {
            // Show the name of the newly selected pot.
            pot = pots.get(position);
            name.setText(pot.getName());

            // Load the pot status if it has not been loaded before. Otherwise, use the cached
            // pot status.
            if (pot.getPotStatus() == null) {
                progressBar.setVisibility(View.VISIBLE);
                chipGroup.setVisibility(View.INVISIBLE);
                subtitle.setVisibility(View.GONE);
                HubApi.loadPotStatus(pot.getId(), this::onPotStatusLoaded, this::showLoadingErrorFragment);
            } else {
                updateStatusChips(pot.getPotStatus());
            }
        }

        private void onPotStatusLoaded(Pot.PotStatus potStatus) {
            pot.setPotStatus(potStatus);
            updateStatusChips(potStatus);
        }

        private void setPotName(String newName) {
            pot.setName(newName);
            name.setText(newName);
        }

        private void updateStatusChips(Pot.PotStatus potStatus) {
            progressBar.setVisibility(View.GONE);
            chipGroup.setVisibility(View.VISIBLE);
            subtitle.setVisibility(View.VISIBLE);
            offlineChip.setVisibility(potStatus.isOnline() ? View.GONE : View.VISIBLE);

            Pot.PotStatus.Measurement measurement = potStatus.getMeasurement();
            if (measurement == null) {
                soilMoistureChip.setVisibility(View.GONE);
                waterLevelChip.setVisibility(View.GONE);
            } else {
                soilMoistureChip.setVisibility(View.VISIBLE);
                waterLevelChip.setVisibility(View.VISIBLE);
                soilMoistureChip.setText(soilMoistureToString(measurement.getSoilMoisture()));
                waterLevelChip.setText(waterLevelToString(measurement.getWaterLevel()));
            }

            Pot.PotStatus.Watered watered = potStatus.getWatered();
            if (watered == null) {
                subtitle.setText("Never watered");
            } else if (watered.isCompleted()) {
                subtitle.setText("Last watered " + watered.getTimestamp());
            } else {
                subtitle.setText("Pending since " + watered.getTimestamp());
            }
        }

        private String soilMoistureToString(int soilMoisture) {
            switch (SoilMoistureClassification.classify(soilMoisture)) {
                case DRY: return "Dry";
                case MOIST: return "Moist";
                case DAMP: return "Damp";
                case WET: return "Wet";
                default: return "Unknown";
            }
        }

        private String waterLevelToString(int waterLevel) {
            switch (WaterLevelClassification.classify(waterLevel)) {
                case EMPTY: return "Empty";
                case LOW: return "Low";
                case HIGH: return "High";
                case FULL: return "Full";
                default: return "Unknown";
            }
        }

        private void showLoadingErrorFragment() {
            NavHostFragment.findNavController(FragmentManager.findFragment(view))
                    .navigate(R.id.action_LoadingPotsFragment_to_LoadingErrorFragment);
        }

        private void onCardClick(View view) {
            final WaterDialogBuilder dialog = new WaterDialogBuilder(pot, view.getContext());
            dialog.setConfirmCallback(this::onConfirmWaterPlant);
            dialog.show();
        }

        private void onConfirmWaterPlant(int amount) {
            progressBar.setVisibility(View.VISIBLE);
            subtitle.setVisibility(View.GONE);

            Pot wateredPot = pot;
            HubApi.waterPot(wateredPot.getId(), amount, _x -> {
                progressBar.setVisibility(View.GONE);
                subtitle.setVisibility(View.VISIBLE);
                subtitle.setText("Watering task scheduled just now");
            }, () -> {
                progressBar.setVisibility(View.GONE);
                subtitle.setVisibility(View.VISIBLE);
                subtitle.setText("Error: Failed to schedule task!");
            });
        }

        private void onConfirmRenamePot(String newName) {
            progressBar.setVisibility(View.VISIBLE);
            subtitle.setVisibility(View.GONE);

            Pot renamedPot = pot;
            String oldName = renamedPot.getName();
            setPotName(newName);
            HubApi.renamePot(renamedPot.getId(), newName, _x -> {
                progressBar.setVisibility(View.GONE);
                subtitle.setVisibility(View.VISIBLE);
            }, () -> {
                progressBar.setVisibility(View.GONE);
                subtitle.setVisibility(View.VISIBLE);
                subtitle.setText("Error: Failed to rename pot!");
                setPotName(oldName);
            });
        }

        private void onMenuButtonClick(View view) {
            PopupMenu menu = new PopupMenu(view.getContext(), view);
            menu.inflate(R.menu.pot_menu);
            menu.setOnMenuItemClickListener(this::onMenuItemClick);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                menu.setForceShowIcon(true);
            }
            menu.show();
        }

        private boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.rename_pot:
                    onRenameMenuItemClick();
                    return true;
                case R.id.remove_pot:
                    onRemovceMenuItemClick();
                    return true;
                default:
                    return false;
            }
        }

        private void onRenameMenuItemClick() {
            final RenameDialogBuilder dialog = new RenameDialogBuilder(pot, view.getContext());
            dialog.setConfirmCallback(this::onConfirmRenamePot);
            dialog.show();
        }

        private void onRemovceMenuItemClick() {
            // Remove the pot from the list locally.
            int position = getAdapterPosition();
            Pot removedPot = pots.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, pots.size());

            // Remove the pot remotely and roll back the local change when the remote operation
            // failed.
            HubApi.removePot(removedPot.getId(), _x -> {}, () -> {
                int newPosition = Math.min(position, pots.size());
                pots.add(newPosition, removedPot);
                notifyItemInserted(newPosition);
                notifyItemRangeChanged(newPosition, pots.size());
            });
        }

        private void onOfflineChipClick(View chip) {
            Snackbar snackbar = Snackbar.make(
                    chip,
                    pot.getName() + " is out of range or not plugged in!",
                    Snackbar.LENGTH_LONG
            );
            snackbar.show();
        }
    }
}
