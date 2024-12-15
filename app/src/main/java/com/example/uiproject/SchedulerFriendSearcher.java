package com.example.uiproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uiproject.databinding.FirestoreSearchFriendBinding;
import com.example.uiproject.databinding.FirestoreFriendItemBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SchedulerFriendSearcher extends AppCompatActivity {

    private static final String TAG = "cjw";

    private FirebaseFirestore db;
    private MyAdapter adapter;
    private List<User> userList;
    private List<User> filteredList;// 필터링된 리스트
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirestoreSearchFriendBinding binding = FirestoreSearchFriendBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        searchView = findViewById(R.id.search_view);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userList = new ArrayList<>();
        filteredList = new ArrayList<>(); // 필터링 된 리스트 초기화
        adapter = new MyAdapter(filteredList); // 필터링 된 리스트를 사용하는 Adapter
        binding.recyclerView.setAdapter(adapter);

        initializeCloudFirestore();
        getUserFromFirestore();

        // SearchView의 텍스트 변경 리스너 설정
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }

    private void initializeCloudFirestore() {
        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance();
    }

    private void getUserFromFirestore() {
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                User user = document.toObject(User.class);
                                userList.add(user);
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            filteredList.addAll(userList); // 필터링 된 리스트에 전체 리스트 추가
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void filter(String text) {
        filteredList.clear(); // 필터링 된 리스트 초기화
        if (text.isEmpty()) {
            filteredList.addAll(userList); // 검색어가 없으면 전체 리스트 복원
        } else {
            String query = text.toLowerCase();
            for (User user : userList) {
                if (user.getNickname().toLowerCase().contains(query)) {
                    filteredList.add(user); // 검색어에 맞는 사용자 추가
                }
            }
        }
        adapter.notifyDataSetChanged(); // 데이터 변경 알림
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private FirestoreFriendItemBinding binding;

        private MyViewHolder(FirestoreFriendItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private List<User> list;

        private MyAdapter(List<User> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            FirestoreFriendItemBinding binding = FirestoreFriendItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new MyViewHolder(binding);                                                  //^부모view가 될 것을 알려줘야 함
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            User user = list.get(position);
            int p = position;
            holder.binding.friendName.setText(user.getNickname());
            holder.binding.univName.setText(user.getUnivname());
            holder.binding.friendItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                     //클릭 시 배경 색을 잠시 유지
                    view.setPressed(true); // 강제로 눌린 상태로 설정
                    view.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            view.setPressed(false); // 일정 시간 후 눌림 해제
                        }
                    }, 33); // 200ms 후 눌림 해제
                    Intent intent = new Intent();
                    intent.putExtra("selectedNickname", list.get(p).getNickname());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }
}