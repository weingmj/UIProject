package com.example.uiproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uiproject.databinding.TimetableMainBinding;

public class TimetableMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TimetableMainBinding binding = TimetableMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}