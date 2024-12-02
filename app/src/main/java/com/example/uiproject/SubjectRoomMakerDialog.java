package com.example.uiproject;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class SubjectRoomMakerDialog extends DialogFragment {
    String subjectName;
    SubjectRoomMakerDialog(String subjectName) { this.subjectName = subjectName; }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext());
        View view = getLayoutInflater().inflate(R.layout.subject_createroom, null, false);
        view.findViewById(R.id.subject_createRoom_button_addRoom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nicknameInput = view.findViewById(R.id.subject_editText_inputNickName);
                EditText roomnameInput = view.findViewById(R.id.subject_editText_inputRoomName);
                String nickname = nicknameInput.getText().toString();
                String roomname = roomnameInput.getText().toString();

            }
        });
        dialog.setContentView(view);
        return dialog;
    }
}