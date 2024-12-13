package com.example.uiproject;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uiproject.databinding.SubjectMainItemBinding;
import com.example.uiproject.databinding.SubjectMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SubjectMainFragment extends Fragment implements SubjectSubSelectorDialog.OnDismissListener {
    private SubjectMainBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SubjectMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.buttonSubjectAddRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubjectSubSelectorDialog dialog = new SubjectSubSelectorDialog();
                dialog.setOnDismissListener(SubjectMainFragment.this);
                dialog.show(getParentFragmentManager(), null);
            }
        });
        SharedPreferences pref = getActivity().getSharedPreferences("roomRouteSet", MODE_PRIVATE);
        Set<String> set = pref.getStringSet("set", new HashSet<>());
        List<String> list = new ArrayList<>(set);
        RecyclerView recyclerView = binding.subjectMainJoinedRoom;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new RoomAdapter(list));
        if (list.isEmpty()) {
            binding.subjectMainIfListEmpty.setVisibility(View.VISIBLE);
        } else {
            binding.subjectMainIfListEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDismiss() {
        recreate();
    }

    public void recreate() {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.detach(this).commitNow();
        transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.attach(this).commitNow();
    }

    class RoomViewHolder extends RecyclerView.ViewHolder {
        SubjectMainItemBinding binding;
        public RoomViewHolder(SubjectMainItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    class RoomAdapter extends RecyclerView.Adapter<RoomViewHolder> {
        List<String> roomList; // 각 방의 경로를 갖는 리스트
        RoomAdapter(List<String> roomList) { this.roomList = roomList; }
        @NonNull
        @Override
        public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            SubjectMainItemBinding binding = SubjectMainItemBinding.inflate(getLayoutInflater(), parent, false);
            return new RoomViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
            int tempPos = position;
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference(roomList.get(tempPos));
            String roomName = reference.getKey();
            String subjectName = reference.getParent().getKey();
            final int[] peopleNum = new int[1];
            reference.child("참여자").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    peopleNum[0] = (int) snapshot.getChildrenCount();  // "참여자" 아래 원소의 개수
                    holder.binding.subjectMainItemPeopleNum.setText(Integer.toString(peopleNum[0]));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("WMJ", "참여자 개수 조회 실패", error.toException());
                }
            });
            holder.binding.subjectMainItemRoomName.setText(roomName);
            holder.binding.subjectMainItemSubjectName.setText(subjectName);
            holder.binding.subjectMainItemDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences pref = getActivity().getSharedPreferences("roomRouteSet", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    Set<String> set = pref.getStringSet("set", new HashSet<>());
                    Set<String> updatedSet = new HashSet<>(set);
                    updatedSet.remove("subjectRoomList/" + subjectName + "/" + roomName);
                    editor.putStringSet("set", updatedSet);
                    editor.apply();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("subjectRoomList/" + subjectName + "/" + roomName + "/참여자");
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            SharedPreferences preferences = getActivity().getSharedPreferences("eachRoomsNickname", MODE_PRIVATE);
                            String nickName = preferences.getString("subjectRoomList/" + subjectName + "/" + roomName, null);
                            if (snapshot.getChildrenCount() == 1) {
                                ref.getParent().removeValue();
                                SharedPreferences.Editor editor1 = preferences.edit();
                                editor1.remove("subjectRoomList/" + subjectName + "/" + roomName);
                                editor1.apply();
                                return;
                            }
                            for (DataSnapshot child : snapshot.getChildren()) {
                                if (nickName.equals((String) child.getValue())) {
                                    snapshot.getRef().removeValue();
                                    SharedPreferences.Editor editor1 = preferences.edit();
                                    editor1.remove("subjectRoomList/" + subjectName + "/" + roomName);
                                    editor1.apply();
                                    return;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    recreate();
                }
            });
            holder.binding.subjectMainItemEachRoom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getContext(), SubjectChatRoom.class)
                            .putExtra("roomRoute", roomList.get(tempPos)));
                }
            });
        }

        @Override
        public int getItemCount() {
            return roomList.size();
        }
    }
}