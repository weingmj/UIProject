package com.example.uiproject;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uiproject.databinding.CommunityForumBinding;

public class ForumActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommunityForumBinding binding = CommunityForumBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}