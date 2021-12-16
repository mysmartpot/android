package com.example.smartpot;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.slider.Slider;

public class WaterDialogBuilder {
    private AlertDialog.Builder dialog;

    private View content;
    private Slider slider;
    private EditText input;
    private View waterLevelWarning;

    private ConfirmCallback confirmCallback;

    public WaterDialogBuilder(Pot pot, Context context) {
        dialog = new AlertDialog.Builder(context);

        // The pot cannot be watered when there is a task still pending.
        Pot.PotStatus potStatus = pot.getPotStatus();
        Pot.PotStatus.Watered watered = potStatus == null ? null : potStatus.getWatered();
        if (watered != null && !watered.isCompleted()) {
            StringBuilder messageBuilder = new StringBuilder();
            messageBuilder.append("There is a pending task to water ");
            messageBuilder.append(pot.getName());
            messageBuilder.append(" since ");
            messageBuilder.append(watered.getTimestamp());
            messageBuilder.append(". ");

            if (potStatus.isOnline()) {
                messageBuilder.append("The task will be executed as soon as possible.");
            } else {
                messageBuilder.append("The task will be executed as soon as the pot comes back online.");
            }

            dialog.setTitle("Pending task for " + pot.getName());
            dialog.setMessage(messageBuilder.toString());
            dialog.setPositiveButton("Okay", null);
            return;
        }

        // The pot cannot be watered when the water level is low.
        Pot.PotStatus.Measurement measurement = potStatus == null ? null : potStatus.getMeasurement();
        WaterLevelClassification waterLevelClassification = measurement == null ? null : WaterLevelClassification.classify(measurement.getWaterLevel());
        if (waterLevelClassification == WaterLevelClassification.EMPTY) {
            StringBuilder messageBuilder = new StringBuilder();
            messageBuilder.append("The water tank of ");
            messageBuilder.append(pot.getName());
            messageBuilder.append(" is empty or almost empty. ");
            messageBuilder.append("You have to refill the tank before the plant can be watered.");

            dialog.setTitle("Empty tank");
            dialog.setMessage(messageBuilder.toString());
            dialog.setPositiveButton("Cancel", null);
            return;
        }

        // Create regular dialog when there is no pending task.
        LayoutInflater inflater = LayoutInflater.from(context);
        dialog.setTitle("Water " + pot.getName());
        dialog.setMessage("How much water should the plant be watered with?");

        content = inflater.inflate(R.layout.water_dialog, null);
        dialog.setView(content);
        dialog.setPositiveButton("Water Plant", this::onConfirm);
        dialog.setNegativeButton("Cancel", null);

        slider = (Slider) content.findViewById(R.id.amount_slider);
        input = (EditText) content.findViewById(R.id.amount_input);
        waterLevelWarning = content.findViewById(R.id.water_level_warning);

        // Use last amount as default value for slider and input.
        if (watered != null) {
            int amount = pot.getPotStatus().getWatered().getAmount();
            slider.setValue(amount);
            input.setText(Integer.toString(amount));
        }
        slider.addOnChangeListener(this::onSliderChanged);
        input.addTextChangedListener(new InputTextWatcher());

        // Show a warning when the water tank is running low.
        if (waterLevelClassification == WaterLevelClassification.LOW) {
            waterLevelWarning.setVisibility(View.VISIBLE);
        } else {
            waterLevelWarning.setVisibility(View.INVISIBLE);
        }
    }

    public void show() {
        dialog.show();
    }

    public void setConfirmCallback(ConfirmCallback confirmCallback) {
        this.confirmCallback = confirmCallback;
    }

    private void onConfirm(DialogInterface dialog, int id) {
        if (confirmCallback != null) {
            confirmCallback.onConfirm((int) slider.getValue());
        }
    }

    private void onSliderChanged(Slider slider, float amount, boolean fromUser) {
        if (fromUser) input.setText(Integer.toString((int) amount));
    }

    private class InputTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            try {
                int amount = Integer.parseInt(s.toString());
                amount = Math.max(amount, (int) slider.getValueFrom());
                amount = Math.min(amount, (int) slider.getValueTo());
                slider.setValue(amount);
            } catch (NumberFormatException e) {
                slider.setValue(0);
            }
        }
    }

    public interface ConfirmCallback {
        void onConfirm(int amount);
    }
}
