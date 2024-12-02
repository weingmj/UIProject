package com.example.uiproject;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uiproject.databinding.SubjectEachroomItemBinding;

import java.util.ArrayList;
import java.util.List;

public class SubjectRoomListDialog extends DialogFragment {
    private String subjectName;
    private List<RoomInfo> list;
    SubjectRoomListDialog() {}
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
                dialog.show(getParentFragmentManager(), null);
            }
        });
        list = new ArrayList<>();
        list.add(new RoomInfo(5, "Test 1"));
        list.add(new RoomInfo(4, "Test 2"));
        list.add(new RoomInfo(6, "Test 3"));
        list.add(new RoomInfo(6, "Test 4"));
        list.add(new RoomInfo(6, "Test 5"));
        list.add(new RoomInfo(6, "Test 6"));
        list.add(new RoomInfo(6, "Test 7"));
        list.add(new RoomInfo(6, "Test 8"));

        RecyclerView recyclerView = view.findViewById(R.id.roomRecyclerView);
        recyclerView.setAdapter(new MyAdapter(list));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dialog.setContentView(view);
        dialog.setTitle(subjectName);
        return dialog;
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
                    SubjectEnterRoomDialog dialog = new SubjectEnterRoomDialog(list.get(i).getRoomName());
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