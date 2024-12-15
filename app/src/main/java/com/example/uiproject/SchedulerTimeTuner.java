package com.example.uiproject;

import static android.view.Gravity.TOP;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.uiproject.databinding.SchedulerTimeTunerBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class SchedulerTimeTuner extends AppCompatActivity {
    SchedulerTimeTunerBinding binding;
    boolean[][][] myTimeTable;
    boolean[][][] tunedSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SchedulerTimeTunerBinding.inflate(getLayoutInflater());

        for (int i = 20; i <= 21; i++) {
            TableRow tableRow = (TableRow) getLayoutInflater().inflate(R.layout.timetable_main_eachrow, null);
            TableLayout.LayoutParams params = new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    0,
                    1
            );
            tableRow.setLayoutParams(params);
            binding.timetableTable.addView(tableRow);
            TextView t = (TextView) tableRow.getChildAt(0);
            t.setText(String.valueOf(i));
        }

        setContentView(binding.getRoot());

        List<String> nicknameList = (List<String>) getIntent().getSerializableExtra("nickNameList");
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        binding.schedulerTimetunerShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (String nickname : nicknameList) {
                    db.collection("users")
                            .whereEqualTo("nickname", nickname)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            // 문서의 ID를 사용하여 해당 문서를 참조
                                            DocumentReference docRef = db.collection("users").document(document.getId());
                                            List<Boolean> flat = new ArrayList<>();
                                            for (int i = 0; i < 5; i++) {
                                                for (int j = 0; j < 13; j++) {
                                                    for (int k = 0; k < 60; k++) {
                                                        flat.add(tunedSchedule[i][j][k]);
                                                    }
                                                }
                                            }
                                            // "shared_schedule" 필드 업데이트
                                            docRef.update("sharedSchedule", flat) // 원하는 값으로 변경
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d("Firestore", "Document successfully updated!");
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.e("Firestore", "Error updating document", e);
                                                        }
                                                    });
                                        }
                                    } else {
                                        Log.e("Firestore", "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                }
            }
        });
        tunedSchedule = new boolean[5][13][60];


        for (String nickname : nicknameList) {
            Task<QuerySnapshot> task = db.collection("users")
                    .whereEqualTo("nickname", nickname)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            try {
                                if (task.isSuccessful()) {
                                    QuerySnapshot querySnapshot = task.getResult();
                                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                        for (QueryDocumentSnapshot document : querySnapshot) {
                                            boolean[][][] eachSchedule = new boolean[5][13][60];
                                            int index = 0;
                                            List<Boolean> flatList = (List<Boolean>) document.get("schedule");
                                            if (flatList == null) throw new Exception();
                                            for (int i = 0; i < 5; i++) {
                                                for (int j = 0; j < 13; j++) {
                                                    for (int k = 0; k < 60; k++) {
                                                        eachSchedule[i][j][k] = flatList.get(index++);
                                                    }
                                                }
                                            }
                                            for (int i = 0; i < 5; i++) {
                                                for (int j = 0; j < 13; j++) {
                                                    for (int k = 0; k < 60; k++) {
                                                        if (eachSchedule[i][j][k]) {
                                                            tunedSchedule[i][j][k] = true;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        Log.d("Firestore", "No documents found for nickname: " + nickname);
                                    }
                                } else {
                                    Log.e("Firestore", "Error getting documents: ", task.getException());
                                }
                            } catch (Exception e) {
                                Log.d("WMJ", "someone do not set timetable");
                            }
                        }
                    });
        }


        ResultReceiver resultReceiver = new ResultReceiver(new Handler()) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                super.onReceiveResult(resultCode, resultData);
                myTimeTable = (boolean[][][]) resultData.getSerializable("timetableManager");
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 13; j++) {
                        for (int k = 0; k < 60; k++) {
                            if (myTimeTable[i][j][k]) {
                                tunedSchedule[i][j][k] = true;
                            }
                        }
                    }
                }

                boolean[][] flattedSchedule = new boolean[5][780];
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 13; j++) {
                        for (int k = 0; k < 60; k++) {
                            flattedSchedule[i][j * 60 + k] = tunedSchedule[i][j][k];
                        }
                    }
                }
                List<Pair<Integer, Pair<Integer, Integer>>> startEndPairedList = new ArrayList<>();

                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 780; j++) {
                        if (!flattedSchedule[i][j]) {
                            int startTime = j;
                            while (!flattedSchedule[i][j]) {
                                j++;
                                if (j == 780) {
                                    if (j - startTime > 60)
                                        startEndPairedList.add(new Pair<>(i, new Pair<>(startTime, j)));
                                    break;
                                }
                                if (flattedSchedule[i][j]) {
                                    if (j - startTime > 60) {
                                        startEndPairedList.add(new Pair<>(i, new Pair<>(startTime, j)));
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
                int[][] pos = new int[5][2];
                binding.textViewTimetableMon.getLocationOnScreen(pos[0]);
                binding.textViewTimetableTue.getLocationOnScreen(pos[1]);
                binding.textViewTimetableWed.getLocationOnScreen(pos[2]);
                binding.textViewTimetableThr.getLocationOnScreen(pos[3]);
                binding.textViewTimetableFri.getLocationOnScreen(pos[4]);
                for (Pair<Integer, Pair<Integer, Integer>> p : startEndPairedList) {
                    Log.d("WMJ", p.first + " " + p.second.first + " " + p.second.second);
                    int[] eachBlockSize = {binding.timetableEachBlockSample.getWidth(), binding.timetableEachBlockSample.getHeight()};
                    Log.d("WMJ", "eachBS:" + eachBlockSize[0] + " wid," + eachBlockSize[1] + " heig");
                    int x = 0, y = 0;
                    int hour, min;
                    int endH, endM;
                    hour = p.second.first / 60; // 0 / 2
                    min = p.second.first % 60; // 0 / 20
                    endH = p.second.second / 60; // 1 / 4
                    endM = p.second.second % 60; // 30 / 0
                    switch (p.first) {
                        case (0):
                            x = pos[0][0];
                            break;
                        case (1):
                            x = pos[1][0];
                            break;
                        case (2):
                            x = pos[2][0];
                            break;
                        case (3):
                            x = pos[3][0];
                            break;
                        case (4):
                            x = pos[4][0];
                            break;
                    }
                    y = hour;
                    y = pos[0][1] + y * eachBlockSize[1] - 8;
                    y += eachBlockSize[1] * min / 60;
                    y += ((binding.timetableTable.getHeight() * 0.05) / 100);
                    TextView button = new TextView(SchedulerTimeTuner.this);
                    button.setMinWidth(30);
                    button.setWidth(eachBlockSize[0]);
                    button.setOnClickListener(view -> {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SchedulerTimeTuner.this);
                        LayoutInflater inflater = getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.scheduler_availabletime_dialog, null);
                        builder.setView(dialogView);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        TextView from = dialogView.findViewById(R.id.scheduler_availTime_from);
                        if (min < 10) {
                            from.setText((hour + 9) + ":0" + min);
                        } else {
                            from.setText((hour + 9) + ":" + min);
                        }
                        TextView to = dialogView.findViewById(R.id.scheduler_availTime_To);
                        if (endM < 10) {
                            to.setText((endH + 9) + ":0" + endM);
                        } else {
                            to.setText((endH + 9) + ":" + endM);
                        }
                        Window window = dialog.getWindow();
                        if (window != null) {
                            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                            layoutParams.copyFrom(window.getAttributes());
                            layoutParams.width = 600; // 너비를 256dp로 고정
                            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT; // 높이는 내용물에 맞춤
                            window.setAttributes(layoutParams);
                        }
                    });
                    button.setX(x);
                    button.setY(y);
                    button.setGravity(TOP);
                    button.setPadding(4, 4, 0, 0);
                    button.setHeight((eachBlockSize[1] * ((endH * 60 + endM) - (hour * 60 + min))) / 60);
                    button.setBackground(ContextCompat.getDrawable(SchedulerTimeTuner.this, R.color.cornflower_blue));
                    binding.getRoot().addView(button);
                }
                Log.d("WMJ", String.valueOf(startEndPairedList.size()));
                for (Pair<Integer, Pair<Integer, Integer>> p : startEndPairedList) {
                    Log.d("WMJ", p.first + " " + p.second.first + " " + p.second.second);
                }
            }
        };
        // 서비스 실행
        Intent serviceIntent = new Intent(SchedulerTimeTuner.this, ManageLectureList.class);
        serviceIntent.putExtra("receiver", resultReceiver);
        startService(serviceIntent);


    }
}