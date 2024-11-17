package com.example.uiproject;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uiproject.databinding.CommunityReplyBinding;

public class CommunityReply extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommunityReplyBinding binding = CommunityReplyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}