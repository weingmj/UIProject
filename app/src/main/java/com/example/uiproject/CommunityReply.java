package com.example.uiproject;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.uiproject.databinding.CommunityReplyBinding;

public class CommunityReply extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        CommunityReplyBinding binding = CommunityReplyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}