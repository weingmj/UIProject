package com.example.uiproject;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class SubjectEnterRoomDialog extends DialogFragment {
    String roomname;
    SubjectEnterRoomDialog(String roomname) {this.roomname = roomname;}
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext());
        View view = getLayoutInflater().inflate(R.layout.subject_enterroom, null, false);
        view.findViewById(R.id.subject_enterRoom_button_enter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //기존 방에 들어갈 수 있게 코드 작성하면 됨
            }
        });
        dialog.setContentView(view);
        return dialog;
    }
}