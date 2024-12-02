package com.example.uiproject;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uiproject.databinding.SubjectMainBinding;

public class SubjectMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SubjectMainBinding binding = SubjectMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonSubjectAddRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubjectSubSelectorDialog dialog = new SubjectSubSelectorDialog();
                dialog.show(getSupportFragmentManager(), null);
            }
        });
    }
}