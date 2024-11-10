package com.example.uiproject;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.uiproject.databinding.CommunitySelectingGroupBinding;

public class CommunitySelectingGroup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        CommunitySelectingGroupBinding binding = CommunitySelectingGroupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}