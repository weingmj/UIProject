package com.example.uiproject;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
                String nickName = nicknameInput.getText().toString();
                String roomName = roomnameInput.getText().toString();
                String subjectName = SubjectRoomMakerDialog.this.subjectName;
                DatabaseReference database = FirebaseDatabase.getInstance().getReference();

                Map<String, Object> roomData = new HashMap<>();
                roomData.put("참여자", Arrays.asList(nickName));

                Map<String, Object> messages = new HashMap<>();
                messages.put("message1", "hi");
                messages.put("message2", "hihi");
                roomData.put("메세지들", Arrays.asList(messages));

                database.child("subjectRoomList").child(subjectName).child(roomName).setValue(roomData)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d("Firebase", "Data added successfully.");
                            } else {
                                Log.e("Firebase", "Data addition failed.", task.getException());
                            }
                        });
            }
        });
        dialog.setContentView(view);
        return dialog;
    }
}