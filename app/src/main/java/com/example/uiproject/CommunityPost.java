package com.example.uiproject;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uiproject.databinding.CommunityPostBinding;

public class CommunityPost extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommunityPostBinding binding = CommunityPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}