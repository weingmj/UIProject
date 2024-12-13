package com.example.uiproject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SubjectEnterRoomDialog extends DialogFragment {
    String roomname;
    String subject;
    onDismissListener listener;
    EditText nickname;
    interface onDismissListener {
        void onDismiss(int fromWhere);
    }

    public SubjectEnterRoomDialog(String roomname, String subject) {
        this.roomname = roomname;
        this.subject = subject;
    }

    public void setOnDismissListener(onDismissListener listener) { this.listener = listener; }

    @SuppressLint("DialogFragmentCallbacksDetector")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext());
        View view = getLayoutInflater().inflate(R.layout.subject_enterroom, null, false);
        nickname = view.findViewById(R.id.subject_enterRoom_editText_nickname);
        view.findViewById(R.id.subject_enterRoom_button_enter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getActivity().getSharedPreferences("roomRouteSet", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                Set<String> roomSet =  new HashSet<>(pref.getStringSet("set", new HashSet<>()));
                String myRoute = "subjectRoomList/" + subject + "/" + roomname;
                if (roomSet.contains(myRoute)) {
                    Toast.makeText(getContext(), "방 이름이 이미 존재합니다.", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference(myRoute);
                    DatabaseReference childRef = reference.child("참여자");
                    childRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            List<String> participants = new ArrayList<>();
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String curNickname = (String) snapshot.getValue();
                                    Log.d("WMJ", nickname.getText().toString() + " " + curNickname);
                                    if (nickname.getText().toString().equals(curNickname)) {
                                        Toast.makeText(getContext(), "해당 닉네임은 이미 존재합니다.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    participants.add(curNickname);
                                }
                            }
                            participants.add(nickname.getText().toString());
                            childRef.setValue(participants);
                            roomSet.add(myRoute);
                            editor.putStringSet("set", roomSet);
                            editor.apply();
                            SharedPreferences nickPref = getContext().getSharedPreferences("eachRoomsNickname", Context.MODE_PRIVATE);
                            SharedPreferences.Editor nickEditor = nickPref.edit();
                            nickEditor.putString("subjectRoomList/" + subject + "/" + roomname, nickname.getText().toString());
                            nickEditor.apply();
                            //액티비티 실행하고
                            dialog.dismiss();
                            listener.onDismiss(1);
                            startActivity(new Intent(getContext(), SubjectChatRoom.class).putExtra("roomRoute", myRoute).putExtra("nickname", nickname.getText().toString()));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("FirebaseError", error.getMessage());
                        }
                    });
                }
            }
        });

        dialog.setContentView(view);
        return dialog;
    }
}
