package com.example.uiproject;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uiproject.databinding.CommunitySelectingGroupBinding;

public class CommunitySelectingGroup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommunitySelectingGroupBinding binding = CommunitySelectingGroupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}