package com.example.uiproject;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SubjectRoomMakerDialog extends DialogFragment {
    String subjectName;
    SubjectRoomMakerDialog(String subjectName) { this.subjectName = subjectName; }
    private OnDialogDismissListener onDialogDismissListener;

    public void setOnDialogDismissListener(OnDialogDismissListener listener) {
        this.onDialogDismissListener = listener;
    }

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
                String roomName = roomnameInput.getText().toString();
                if (roomName.isBlank()) {
                    Toast.makeText(getContext(), "방 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String nickName = nicknameInput.getText().toString();
                if (nickName.isBlank()) {
                    Toast.makeText(getContext(), "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String subjectName = SubjectRoomMakerDialog.this.subjectName;
                DatabaseReference database = FirebaseDatabase.getInstance().getReference();

                Map<String, Object> roomData = new HashMap<>();
                roomData.put("참여자", Arrays.asList(nickName));

                Map<String, Object> messages = new HashMap<>();
                roomData.put("메세지들", Arrays.asList(messages));
                SharedPreferences pref = getContext().getSharedPreferences("roomRouteSet", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                Set<String> set = pref.getStringSet("set", new HashSet<>());
                Set<String> updatedSet = new HashSet<>(set);
                updatedSet.add("subjectRoomList/" + subjectName + "/" + roomName);
                editor.putStringSet("set", updatedSet);
                editor.apply();
                SharedPreferences nickPref = getContext().getSharedPreferences("eachRoomsNickname", MODE_PRIVATE);
                SharedPreferences.Editor nickEditor = nickPref.edit();
                nickEditor.putString("subjectRoomList/" + subjectName + "/" + roomName, nickName);
                nickEditor.apply();
                database.child("subjectRoomList").child(subjectName).child(roomName).setValue(roomData)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d("Firebase", "Data added successfully.");
                            } else {
                                Log.e("Firebase", "Data addition failed.", task.getException());
                            }
                        });
                dismiss();
                onDialogDismissListener.onDismiss(0);
            }
        });
        dialog.setContentView(view);
        return dialog;
    }

    interface OnDialogDismissListener {
        void onDismiss(int fromWhere);
    }
}