package com.example.uiproject;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.uiproject.databinding.CommunityCalendarBinding;

import java.util.ArrayList;
import java.util.List;

public class no2Fragment extends Fragment {
    private DrawerLayout drawerLayout;

    private List<String> friendList;
    private CommunityCalendarBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = CommunityCalendarBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        drawerLayout = view.findViewById(R.id.calender_main_content);

        binding.buttonCalendarPanel.setOnClickListener(v -> {
            drawerLayout.openDrawer(Gravity.LEFT);
        });

        drawerLayout.findViewById(R.id.drawer_button_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), InputURL.class));
            }
        });

        setDrawerCloseClick(view);
    }

    private void setDrawerCloseClick(View view) {
        view.findViewById(R.id.close_drawer).setOnClickListener(v -> {
            drawerLayout.closeDrawer(Gravity.LEFT);
        });
    }
}