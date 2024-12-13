package com.example.uiproject;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uiproject.databinding.SubjectEachroomItemBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SubjectRoomListDialog extends DialogFragment implements SubjectRoomMakerDialog.OnDialogDismissListener, SubjectEnterRoomDialog.onDismissListener {
    private String subjectName;
    private List<RoomInfo> list;
    private RecyclerView recyclerView;
    private TextView textView;
    private SubjectRoomListDialog() {}
    SubjectRoomListDialog(String subjectName) {this.subjectName = subjectName;}

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext());
        View view = View.inflate(requireContext(), R.layout.subject_roomlist, null);
        view.findViewById(R.id.subject_item_addRoomButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubjectRoomMakerDialog dialog = new SubjectRoomMakerDialog(subjectName);
                dialog.setOnDialogDismissListener(SubjectRoomListDialog.this);
                dialog.show(getParentFragmentManager(), null);
            }
        });
        list = new ArrayList<>();
        getRoomListFromFirebase();
        recyclerView = view.findViewById(R.id.roomRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        textView = view.findViewById(R.id.subject_textView_ifEmptyRoomList);
        dialog.setContentView(view);
        dialog.setTitle(subjectName);
        return dialog;
    }
    private void getRoomListFromFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference root = database.getReference("subjectRoomList");
        DatabaseReference reference = root.child(subjectName);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                if (!snapshot.exists()) textView.setVisibility(View.VISIBLE);
                else textView.setVisibility(View.GONE);
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String childKey = childSnapshot.getKey();
                    int participatingNum = (int) childSnapshot.child("참여자").getChildrenCount();
                    list.add(new RoomInfo(participatingNum, childKey));
                }
                recyclerView.setAdapter(new MyAdapter(list));
                recyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("WMJ", "Error: " + error.getMessage());
            }
        });

    }

    @Override
    public void onDismiss(int fromWhere) {
        if (fromWhere == 0) {
            getRoomListFromFirebase();
        }
        else if (fromWhere == 1) {
            getRoomListFromFirebase();
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        SubjectEachroomItemBinding binding;
        public MyViewHolder(SubjectEachroomItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private List<RoomInfo> list;
        private MyAdapter(List<RoomInfo> list) { this.list = list; }
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyViewHolder(SubjectEachroomItemBinding.inflate(getLayoutInflater(), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.binding.subjectItemSubjectName.setText(list.get(position).getRoomName());
            holder.binding.subjectItemPeopleNum.setText(Integer.toString(list.get(position).getParticipatingNum()));
            int i = position;
            holder.binding.subjectItemRoomButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences pref = getActivity().getSharedPreferences("roomRouteSet", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    Set<String> roomSet =  new HashSet<>(pref.getStringSet("set", new HashSet<>()));
                    String myRoute = "subjectRoomList/" + subjectName + "/" + list.get(i).getRoomName();
                    if (roomSet.contains(myRoute)) {
                        Toast.makeText(getContext(), "방 이름이 이미 존재합니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    SubjectEnterRoomDialog dialog = new SubjectEnterRoomDialog(list.get(i).getRoomName(), subjectName);
                    dialog.setOnDismissListener(SubjectRoomListDialog.this);
                    dialog.show(getParentFragmentManager(), null);
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    private class RoomInfo {
        private String roomName;
        private int participatingNum;
        RoomInfo(int participatingNum, String roomName) {
            this.participatingNum = participatingNum;
            this.roomName = roomName;
        }
        public int getParticipatingNum() {
            return participatingNum;
        }

        public String getRoomName() {
            return roomName;
        }
    }
}