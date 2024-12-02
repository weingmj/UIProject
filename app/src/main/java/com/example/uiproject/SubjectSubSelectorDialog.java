package com.example.uiproject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uiproject.databinding.SubjectEachsubnameItemBinding;
import com.example.uiproject.databinding.SubjectSelectsubBinding;

import java.util.ArrayList;
import java.util.List;

public class SubjectSubSelectorDialog extends DialogFragment {
    SubjectSelectsubBinding binding;
    List<PairedLecture> pairedLectureList;
    List<RoomInfo> list;
    RecyclerView recyclerView;
    SubjectAdapter adapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.subject_selectsub, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        recyclerView = dialogView.findViewById(R.id.subjectRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        list = new ArrayList<>();
        adapter = new SubjectAdapter(list);
        recyclerView.setAdapter(adapter);

        getLectureList();

        return dialog;
    }

    private void getLectureList() {
        ResultReceiver resultReceiver = new ResultReceiver(new Handler()) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                super.onReceiveResult(resultCode, resultData);
                if (resultData != null) {
                    pairedLectureList = resultData.getParcelableArrayList("pairedLectureList");
                }
                for (int i = 0; i < pairedLectureList.size(); i++) {
                    RoomInfo temp = new RoomInfo();
                    temp.participatingNum = 0;
                    temp.roomName = pairedLectureList.get(i).getLectureName();
                    list.add(temp);
                }
                updateRecyclerView();
            }
        };
        Intent serviceIntent = new Intent(getContext(), ManageLectureList.class);
        serviceIntent.putExtra("receiver", resultReceiver);
        requireContext().startService(serviceIntent);
    }

    private void updateRecyclerView() {
        adapter.updateData(list);
    }

    private class SubjectViewHolder extends RecyclerView.ViewHolder {
        SubjectEachsubnameItemBinding binding;

        public SubjectViewHolder(SubjectEachsubnameItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private class SubjectAdapter extends RecyclerView.Adapter<SubjectViewHolder> {
        private List<RoomInfo> list;

        public SubjectAdapter(List<RoomInfo> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new SubjectViewHolder(SubjectEachsubnameItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }

        public void updateData(List<RoomInfo> newList) {
            this.list = newList;
            notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
            holder.binding.subjectItemSubjectName.setText(list.get(position).getRoomName());
            int i = position;
            holder.binding.subjectItemRoomButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SubjectRoomListDialog dialog = new SubjectRoomListDialog(list.get(i).getRoomName());
                    dialog.show(getParentFragmentManager(), "roomList");
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

        public int getParticipatingNum() {
            return participatingNum;
        }

        public String getRoomName() {
            return roomName;
        }
    }
}
