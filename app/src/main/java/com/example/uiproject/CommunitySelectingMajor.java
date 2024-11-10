package com.example.uiproject;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.uiproject.databinding.CommunitySelectingGroupBinding;
import com.example.uiproject.databinding.CommunitySelectingMajorBinding;

public class CommunitySelectingMajor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        CommunitySelectingMajorBinding binding = CommunitySelectingMajorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}