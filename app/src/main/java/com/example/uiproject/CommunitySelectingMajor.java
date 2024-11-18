package com.example.uiproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.example.uiproject.databinding.CommunitySelectingMajorBinding;

public class CommunitySelectingMajor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        CommunitySelectingMajorBinding binding = CommunitySelectingMajorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}