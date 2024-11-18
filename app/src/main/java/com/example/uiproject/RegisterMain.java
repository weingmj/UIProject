package com.example.uiproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.uiproject.databinding.RegisterMainBinding;

public class RegisterMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RegisterMainBinding binding = RegisterMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.register_dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterMain.this, R.style.CustomDialogTheme);
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}