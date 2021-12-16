package com.example.smartpot;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ConnectPotDialogBuilder {
    private AlertDialog.Builder dialog;

    private View content;
    private EditText nameInput;
    private Button positiveButton;

    private ConfirmCallback confirmCallback;

    public ConnectPotDialogBuilder(AvailablePot pot, Context context) {
        dialog = new AlertDialog.Builder(context);

        // Create regular dialog when there is no pending task.
        LayoutInflater inflater = LayoutInflater.from(context);
        dialog.setTitle("Add new pot");
        dialog.setMessage("Enter the name for the new pot.");

        content = inflater.inflate(R.layout.connect_pot_dialog, null);
        dialog.setView(content);
        dialog.setPositiveButton("Add Pot", this::onConfirm);
        dialog.setNegativeButton("Cancel", null);

        nameInput = (EditText) content.findViewById(R.id.name_input);
        nameInput.setText(pot.getAddr());

        nameInput.addTextChangedListener(new InputTextWatcher());
    }

    public void show() {
        AlertDialog actualDialog = dialog.create();
        actualDialog.show();
        positiveButton = actualDialog.getButton(AlertDialog.BUTTON_POSITIVE);
    }

    public void setConfirmCallback(ConfirmCallback confirmCallback) {
        this.confirmCallback = confirmCallback;
    }

    private void onConfirm(DialogInterface dialog, int id) {
        if (confirmCallback != null) {
            confirmCallback.onConfirm(nameInput.getText().toString().trim());
        }
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
            positiveButton.setEnabled(!s.toString().trim().isEmpty());
        }
    }

    public interface ConfirmCallback {
        void onConfirm(String newName);
    }
}
