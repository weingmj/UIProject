package com.example.uiproject;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uiproject.databinding.SchedulerMainBinding;
import com.example.uiproject.databinding.SchedulerItemBinding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SchedulerMain extends Fragment {
    SchedulerMainBinding binding;
    List<String> friendList;
    RecyclerView.Adapter<FriendViewHolder> adapter;
    DrawerLayout drawerLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SchedulerMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        friendList = new ArrayList<>();
        FriendAdapter adapter = new FriendAdapter(friendList);
        binding.schedulerMainRecyclerView.setAdapter(adapter);
        binding.schedulerMainRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                if (o.getResultCode() == RESULT_OK) {
                    Log.d("WMJ", "hi");
                    String selectedNickname = o.getData().getStringExtra("selectedNickname");
                    if (!friendList.contains(selectedNickname)) {
                        friendList.add(selectedNickname);
                        adapter.notifyItemInserted(friendList.size() - 1);
                        updateRecyclerViewVisibility(friendList);
                    }
                }
            }
        });

        binding.schedulerMainAddFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launcher.launch(new Intent(getContext(), SchedulerFriendSearcher.class));
            }
        });

        binding.schedulerMainTuneScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> nickNameList = adapter.getFriendList();
                startActivity(new Intent(getContext(), SchedulerTimeTuner.class)
                        .putExtra("nickNameList", (Serializable) nickNameList));
            }
        });



        binding.schedulerMainSlideButton.setOnClickListener(v -> {
            drawerLayout.openDrawer(Gravity.LEFT);
        });

        drawerLayout.findViewById(R.id.drawer_button_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), InputURL.class));
            }
        });

        setDrawerCloseClick(view);
    }

    private void setDrawerCloseClick(View view) {
        view.findViewById(R.id.close_drawer).setOnClickListener(v -> {
            drawerLayout.closeDrawer(Gravity.LEFT);
        });
    }

    private void updateRecyclerViewVisibility(List<String> friendList) {
        if (friendList.isEmpty()) {
            binding.schedulerMainRecyclerView.setVisibility(View.INVISIBLE);
            binding.schedulerMainIfRecyclerEmpty.setVisibility(View.VISIBLE);

            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(binding.getRoot());

            constraintSet.connect(binding.schedulerMainAddFriendButton.getId(), ConstraintSet.TOP, binding.schedulerMainIfRecyclerEmpty.getId(), ConstraintSet.BOTTOM);
            constraintSet.connect(binding.schedulerMainAddFriendButton.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
            constraintSet.connect(binding.schedulerMainAddFriendButton.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
            constraintSet.applyTo(binding.getRoot());
        } else {
            binding.schedulerMainRecyclerView.setVisibility(View.VISIBLE);
            binding.schedulerMainIfRecyclerEmpty.setVisibility(View.INVISIBLE);

            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(binding.getRoot());

            constraintSet.connect(binding.schedulerMainAddFriendButton.getId(), ConstraintSet.TOP, binding.schedulerMainRecyclerView.getId(), ConstraintSet.BOTTOM);
            constraintSet.connect(binding.schedulerMainAddFriendButton.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
            constraintSet.connect(binding.schedulerMainAddFriendButton.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
            constraintSet.applyTo(binding.getRoot());
        }
    }

    private class FriendViewHolder extends RecyclerView.ViewHolder {
        SchedulerItemBinding binding;
        public FriendViewHolder(@NonNull SchedulerItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

    private class FriendAdapter extends RecyclerView.Adapter<FriendViewHolder> {
        List<String> friendList;
        FriendAdapter(List<String> friendList) { this.friendList = friendList; }
        @NonNull
        @Override
        public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            SchedulerItemBinding binding = SchedulerItemBinding.inflate(getLayoutInflater(), parent, false);
            return new FriendViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
            int p = position;
            holder.binding.schedulerItemDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    friendList.remove(p);
                    notifyItemRemoved(p);
                    notifyItemRangeChanged(p, friendList.size());
                    updateRecyclerViewVisibility(friendList);
                }
            });
            holder.binding.schedulerItemNickname.setText(friendList.get(position));
        }

        @Override
        public int getItemCount() {
            return friendList.size();
        }

        public List<String> getFriendList() {
            return friendList;
        }
    }
}