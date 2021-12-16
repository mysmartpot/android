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

public class RenameDialogBuilder {
    private AlertDialog.Builder dialog;

    private View content;
    private EditText newNameInput;
    private Button positiveButton;

    private ConfirmCallback confirmCallback;

    public RenameDialogBuilder(Pot pot, Context context) {
        dialog = new AlertDialog.Builder(context);

        // Create regular dialog when there is no pending task.
        LayoutInflater inflater = LayoutInflater.from(context);
        dialog.setTitle("Rename " + pot.getName());
        dialog.setMessage("Enter the new name for the pot.");

        content = inflater.inflate(R.layout.rename_dialog, null);
        dialog.setView(content);
        dialog.setPositiveButton("Rename Pot", this::onConfirm);
        dialog.setNegativeButton("Cancel", null);

        newNameInput = (EditText) content.findViewById(R.id.name_input);
        newNameInput.setText(pot.getName());

        newNameInput.addTextChangedListener(new InputTextWatcher());
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
            confirmCallback.onConfirm(newNameInput.getText().toString().trim());
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
