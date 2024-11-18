package com.example.uiproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.example.uiproject.databinding.CommunityMainBinding;

public class CommunityMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommunityMainBinding binding = CommunityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}