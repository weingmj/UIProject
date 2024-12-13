package com.example.uiproject;

import static android.content.Context.MODE_PRIVATE;
import static android.view.Gravity.TOP;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.uiproject.databinding.TimetableDialogAddplanBinding;
import com.example.uiproject.databinding.TimetableMainBinding;


import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicInteger;


public class TimetableMainFragment extends Fragment {
    private TimetableMainBinding binding;
    private final int startTime = 9;
    private final int endTime = 21;
    static int[] colorList = new int[15];
    List<EachLecture> list;
    List<PairedLecture> pairedLectureList;
    boolean[][][] timetableManager;
    int success;

    public void recreate() {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.detach(this).commitNow();
        transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.attach(this).commitNow();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = TimetableMainBinding.inflate(getLayoutInflater());
        initializeTimetable();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String userInput;
        LayoutInflater inflater = getLayoutInflater();
        for (int i = 19 + 1; i <= endTime; i++) {
            TableRow tableRow = (TableRow) inflater.inflate(R.layout.timetable_main_eachrow, null);
            TableLayout.LayoutParams params = new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT, // 너비
                    0,                                     // 높이
                    1                                      // Weight
            );
            tableRow.setLayoutParams(params);
            binding.timetableTable.addView(tableRow);
            TextView t = (TextView) tableRow.getChildAt(0);
            t.setText(String.valueOf(i));
        }
        binding.getRoot().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                binding.getRoot().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                initializeTimetable();
            }
        });
        binding.timetableImageButtonAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), TimetableAlarmMain.class);
                startActivity(intent);
            }
        });
        binding.timetableImageButtonAddPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean[] dayClicked = {false, false, false, false, false};
                TimetableDialogAddplanBinding timetableDialogAddplanBinding = TimetableDialogAddplanBinding.inflate(getLayoutInflater());
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(timetableDialogAddplanBinding.getRoot());
                timetableDialogAddplanBinding.buttonAddplanMon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!dayClicked[0]) {
                            setFalse(dayClicked);
                            dayClicked[0] = true;
                            timetableDialogAddplanBinding.buttonAddplanMon.setTextColor(requireActivity().getColor(R.color.red));
                            timetableDialogAddplanBinding.buttonAddplanTue.setTextColor(requireActivity().getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanWed.setTextColor(requireActivity().getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanThu.setTextColor(requireActivity().getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanFri.setTextColor(requireActivity().getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanAdd.setTextColor(requireActivity().getColor(R.color.black));
                        } else {
                            dayClicked[0] = false;
                            timetableDialogAddplanBinding.buttonAddplanMon.setTextColor(requireActivity().getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanAdd.setTextColor(requireActivity().getColor(R.color.gray));
                        }
                    }
                });
                timetableDialogAddplanBinding.buttonAddplanTue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!dayClicked[1]) {
                            setFalse(dayClicked);
                            dayClicked[1] = true;
                            timetableDialogAddplanBinding.buttonAddplanMon.setTextColor(requireActivity().getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanTue.setTextColor(requireActivity().getColor(R.color.red));
                            timetableDialogAddplanBinding.buttonAddplanWed.setTextColor(requireActivity().getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanThu.setTextColor(requireActivity().getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanFri.setTextColor(requireActivity().getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanAdd.setTextColor(requireActivity().getColor(R.color.black));
                        } else {
                            dayClicked[1] = false;
                            timetableDialogAddplanBinding.buttonAddplanTue.setTextColor(requireActivity().getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanAdd.setTextColor(requireActivity().getColor(R.color.gray));
                        }
                    }
                });
                timetableDialogAddplanBinding.buttonAddplanWed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!dayClicked[2]) {
                            setFalse(dayClicked);
                            dayClicked[2] = true;
                            timetableDialogAddplanBinding.buttonAddplanMon.setTextColor(requireActivity().getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanTue.setTextColor(requireActivity().getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanWed.setTextColor(requireActivity().getColor(R.color.red));
                            timetableDialogAddplanBinding.buttonAddplanThu.setTextColor(requireActivity().getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanFri.setTextColor(requireActivity().getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanAdd.setTextColor(requireActivity().getColor(R.color.black));
                        } else {
                            dayClicked[2] = false;
                            timetableDialogAddplanBinding.buttonAddplanWed.setTextColor(requireActivity().getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanAdd.setTextColor(requireActivity().getColor(R.color.gray));
                        }
                    }
                });
                timetableDialogAddplanBinding.buttonAddplanThu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!dayClicked[3]) {
                            setFalse(dayClicked);
                            dayClicked[3] = true;
                            timetableDialogAddplanBinding.buttonAddplanMon.setTextColor(requireActivity().getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanTue.setTextColor(requireActivity().getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanWed.setTextColor(requireActivity().getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanThu.setTextColor(requireActivity().getColor(R.color.red));
                            timetableDialogAddplanBinding.buttonAddplanFri.setTextColor(requireActivity().getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanAdd.setTextColor(requireActivity().getColor(R.color.black));
                        } else {
                            dayClicked[3] = false;
                            timetableDialogAddplanBinding.buttonAddplanThu.setTextColor(requireActivity().getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanAdd.setTextColor(requireActivity().getColor(R.color.gray));
                        }
                    }
                });
                timetableDialogAddplanBinding.buttonAddplanFri.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!dayClicked[4]) {
                            setFalse(dayClicked);
                            dayClicked[4] = true;
                            timetableDialogAddplanBinding.buttonAddplanMon.setTextColor(requireActivity().getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanTue.setTextColor(requireActivity().getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanWed.setTextColor(requireActivity().getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanThu.setTextColor(requireActivity().getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanFri.setTextColor(requireActivity().getColor(R.color.red));
                            timetableDialogAddplanBinding.buttonAddplanAdd.setTextColor(requireActivity().getColor(R.color.black));
                        } else {
                            dayClicked[4] = false;
                            timetableDialogAddplanBinding.buttonAddplanFri.setTextColor(requireActivity().getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanAdd.setTextColor(requireActivity().getColor(R.color.gray));
                        }
                    }
                });
                timetableDialogAddplanBinding.numberPickerAddplanStartHour.setMinValue(startTime);
                timetableDialogAddplanBinding.numberPickerAddplanStartHour.setMaxValue(endTime);
                timetableDialogAddplanBinding.numberPickerAddplanStartMin.setMinValue(0);
                timetableDialogAddplanBinding.numberPickerAddplanStartMin.setMaxValue(59);
                timetableDialogAddplanBinding.numberPickerAddplanEndHour.setMinValue(startTime);
                timetableDialogAddplanBinding.numberPickerAddplanEndHour.setMaxValue(endTime);
                timetableDialogAddplanBinding.numberPickerAddplanEndMin.setMinValue(0);
                timetableDialogAddplanBinding.numberPickerAddplanEndMin.setMaxValue(59);
                timetableDialogAddplanBinding.buttonAddplanAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int day = -1;
                        for (int i = 0; i < 5; i++) {
                            if (dayClicked[i]) day = i;
                        }
                        int startHour = timetableDialogAddplanBinding.numberPickerAddplanStartHour.getValue();
                        int startMin = timetableDialogAddplanBinding.numberPickerAddplanStartMin.getValue();
                        int endHour = timetableDialogAddplanBinding.numberPickerAddplanEndHour.getValue();
                        int endMin = timetableDialogAddplanBinding.numberPickerAddplanEndMin.getValue();
                        boolean breaker = false;
                        if (timetableDialogAddplanBinding.editTextAddplanInputPlan.getText().toString().isBlank()) {
                            Toast.makeText(getContext(),"이름을 입력해주세요!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (day != -1) {
                            if (startHour == endHour) {
                                if (startMin < endMin) {
                                    for (int i = startMin; i < endMin && !breaker; i++) {
                                        if (timetableManager[day][startHour - startTime][i]) {
                                            Toast.makeText(getContext(), "다른 시간대를 선택해주세요.", Toast.LENGTH_SHORT).show();
                                            breaker = true;
                                        }
                                    }
                                } else {
                                    Toast.makeText(getContext(), "시작 시간이 종료 시간보다 늦습니다.", Toast.LENGTH_SHORT).show();
                                    breaker = true;
                                }
                            } else if (startHour < endHour) {
                                for (int i = startMin; i < 60 && !breaker; i++) {
                                    if (timetableManager[day][startHour - startTime][i]) {
                                        Toast.makeText(getContext(), "다른 시간대를 선택해주세요.", Toast.LENGTH_SHORT).show();
                                        breaker = true;
                                    }
                                }
                                for (int i = startHour + 1; i < endHour - 1 && !breaker; i++) {
                                    for (int j = 0; j < 60 && !breaker; j++) {
                                        if (timetableManager[day][i - startTime][j]) {
                                            Toast.makeText(getContext(), "다른 시간대를 선택해주세요.", Toast.LENGTH_SHORT).show();
                                            breaker = true;
                                        }
                                    }
                                }
                                for (int i = 0; i < endMin && !breaker; i++) {
                                    if (timetableManager[day][endHour - startTime][i]) {
                                        Toast.makeText(getContext(), "다른 시간대를 선택해주세요.", Toast.LENGTH_SHORT).show();
                                        breaker = true;
                                    }
                                }
                            } else {
                                Toast.makeText(getContext(), "시작 시간이 종료 시간보다 늦습니다.", Toast.LENGTH_SHORT).show();
                                breaker = true;
                            }
                            if (!breaker) {
                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("app_data", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                String origin = sharedPreferences.getString("saved_lecturelist", "Oops!");
                                if (!origin.equals("Oops!")) {
                                    origin = origin.trim();
                                    String adder = "\n" +
                                            timetableDialogAddplanBinding.editTextAddplanInputPlan.getText() + " "
                                            + intToDay(day) + " "
                                            + startHour + " "
                                            + startMin + " "
                                            + endHour + " "
                                            + endMin + " "
                                            + "false" + "\n";
                                    origin = origin.concat(adder);
                                    editor.putString("saved_lecturelist", origin);
                                    editor.apply();
                                    recreate();
                                }
                            }
                        } else {
                            Toast.makeText(getContext(), "요일을 선택해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        });
    }

    private String intToDay(int day) {
        switch (day) {
            case 0:
                return "월";
            case 1:
                return "화";
            case 2:
                return "수";
            case 3:
                return "목";
            case 4:
                return "금";
        }
        return "error";
    }

    protected void setFalse(boolean[] arr) {
        int i = 0;
        for (boolean b : arr) {
            arr[i] = false;
            i++;
        }
    }

    private void initializeTimetable() {
        ResultReceiver resultReceiver = new ResultReceiver(new Handler()) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                super.onReceiveResult(resultCode, resultData);
                // Service에서 돌아온 데이터를 받음
                if (resultData != null) {
                    list = resultData.getParcelableArrayList("eachLectureList");
                    pairedLectureList = resultData.getParcelableArrayList("pairedLectureList");
                    timetableManager = (boolean[][][]) resultData.getSerializable("timetableManager");
                    success = resultData.getInt("isSuccess");
                    setTimeTable();
                }
            }
        };
        Intent serviceIntent = new Intent(getContext(), ManageLectureList.class);
        serviceIntent.putExtra("receiver", resultReceiver);
        getActivity().startService(serviceIntent);
    }

    private void deleteLecture(String lectureName) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("app_data", MODE_PRIVATE);
        String lectureMass = sharedPreferences.getString(getString(R.string.saved_lecturelist), "Oops!");
        StringTokenizer tokenizer = new StringTokenizer(lectureMass, "\n");
        String result = "";
        while (tokenizer.hasMoreTokens()) {
            String temp = tokenizer.nextToken();
            if (temp.isBlank()) break;
            StringTokenizer eachLectureName = new StringTokenizer(temp, " ");
            if (!eachLectureName.nextToken().equals(lectureName)) {
                result = result.concat(temp + "\n");
            }
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("saved_lecturelist", result);
        editor.apply();
    }

    private void setTimeTable() {
        if (success == 1) {
            int k = 0;
            int[][] pos = new int[5][2]; // pos[n][x or y] : n요일의 x, y좌표
            binding.textViewTimetableMon.getLocationOnScreen(pos[0]);
            binding.textViewTimetableTue.getLocationOnScreen(pos[1]);
            binding.textViewTimetableWed.getLocationOnScreen(pos[2]);
            binding.textViewTimetableThr.getLocationOnScreen(pos[3]);
            binding.textViewTimetableFri.getLocationOnScreen(pos[4]);
            int[] eachBlockSize = {binding.timetableEachBlockSample.getWidth(), binding.timetableEachBlockSample.getHeight()};
            AtomicInteger cnt = new AtomicInteger();
            for (PairedLecture p : pairedLectureList) {
                int x = 0, y;
                if (!p.paired) {
                    switch (p.getWeekDay(0)) {
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
                    y = p.getStartHour(0) - startTime;
                    y = pos[0][1] + y * eachBlockSize[1] - 8;
                    y += eachBlockSize[1] * p.getStartMin(0) / 60;
                    y += ((binding.timetableTable.getHeight() * 0.05) / 100);
                    TextView button = new TextView(getContext());
                    button.setText(p.getLectureName());
                    button.setMinWidth(30);
                    button.setWidth(eachBlockSize[0]);
                    button.setOnClickListener(view -> {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                        LayoutInflater inflater = getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.timetable_eachbutton_dialog_single, null);

                        TextView LectureName = dialogView.findViewById(R.id.textView_timetable_eachButton_LectureName);
                        LectureName.setText(p.getLectureName());

                        TextView FirstLectureTime = dialogView.findViewById(R.id.textView_timetable_eachButton_firstLectureTime);
                        String temp = intToDay(p.getWeekDay(0)) +  "요일 " +
                                p.getStartHour(0) + " : "
                                + (p.getStartMin(0) < 10 ? "0" + p.getStartMin(0) : p.getStartMin(0)) + " ~ "
                                + p.getEndHour(0) + " : "
                                + (p.getEndMin(0) < 10 ? "0" + p.getEndMin(0) : p.getEndMin(0));
                        FirstLectureTime.setText(temp);

                        builder.setView(dialogView);

                        AlertDialog dialog = builder.create();
                        dialog.show();
                        Window window = dialog.getWindow();
                        if (window != null) {
                            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                            layoutParams.copyFrom(window.getAttributes());
                            layoutParams.width = 600; // 너비를 256dp로 고정
                            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT; // 높이는 내용물에 맞춤
                            window.setAttributes(layoutParams);
                        }
                        AppCompatImageButton buttonClose = dialogView.findViewById(R.id.button_timetable_eachButton_close);
                        buttonClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                        AppCompatImageButton buttonDelete = dialogView.findViewById(R.id.button_timetable_eachButton_delete);
                        buttonDelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                deleteLecture(p.getLectureName());
                                recreate();
                                dialog.dismiss();
                            }
                        });
                    });
                    button.setX(x);
                    button.setY(y);
                    button.setGravity(TOP);
                    button.setPadding(4, 4, 0, 0);
                    button.setTextSize(12);
                    button.setHeight(eachBlockSize[1] * ((p.getEndHour(0) * 60 + p.getEndMin(0)) - (p.getStartHour(0) * 60 + p.getStartMin(0))) / 60);
                    button.setBackground(ContextCompat.getDrawable(getContext(), p.getColor()));
                    binding.getRoot().addView(button);
                } else {
                    for (int i = 0; i < 2; i++) {
                        switch (p.getWeekDay(i)) {
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
                        y = p.getStartHour(i) - startTime;
                        y = pos[0][1] + y * eachBlockSize[1] - 8;
                        y += eachBlockSize[1] * p.getStartMin(0) / 60;
                        y += ((binding.timetableTable.getHeight() * 0.05) / 100);
                        TextView button = new TextView(getContext());
                        button.setText(p.getLectureName());
                        button.setMinWidth(30);
                        button.setWidth(eachBlockSize[0]);
                        button.setOnClickListener(view -> {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            int fastLecture = 0;
                            int slowLecture = 1;
                            if (p.getWeekDay(0) > p.getWeekDay(1)) {
                                fastLecture = 1;
                                slowLecture = 0;
                            } else if (p.getWeekDay(0) == p.getWeekDay(1)) {
                                if (p.getStartHour(0) > p.getStartHour(1)) {
                                    fastLecture = 1;
                                    slowLecture = 0;
                                }
                            }
                            LayoutInflater inflater = getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.timetable_eachbutton_dialog, null);

                            TextView LectureName = dialogView.findViewById(R.id.textView_timetable_eachButton_LectureName);
                            LectureName.setText(p.getLectureName());

                            TextView FirstLectureTime = dialogView.findViewById(R.id.textView_timetable_eachButton_firstLectureTime);
                            String temp = intToDay(p.getWeekDay(fastLecture)) +  "요일 "
                                    + p.getStartHour(fastLecture) + " : "
                                    + (p.getStartMin(fastLecture) < 10 ? "0" + p.getStartMin(fastLecture) : p.getStartMin(fastLecture)) + " ~ "
                                    + p.getEndHour(fastLecture) + " : "
                                    + (p.getEndMin(fastLecture) < 10 ? "0" + p.getEndMin(fastLecture) : p.getEndMin(fastLecture));
                            FirstLectureTime.setText(temp);

                            TextView SecondLectureTime = dialogView.findViewById(R.id.textView_timetable_eachButton_secondLectureTime);
                            String temp2 = intToDay(p.getWeekDay(slowLecture)) +  "요일 "
                                    + p.getStartHour(slowLecture) + " : "
                                    + (p.getStartMin(slowLecture) < 10 ? "0" + p.getStartMin(slowLecture) : p.getStartMin(slowLecture)) + " ~ "
                                    + p.getEndHour(slowLecture) + " : "
                                    + (p.getEndMin(slowLecture) < 10 ? "0" + p.getEndMin(slowLecture) : p.getEndMin(slowLecture));
                            SecondLectureTime.setText(temp2);
                            AppCompatImageButton buttonClose = dialogView.findViewById(R.id.button_timetable_eachButton_close);
                            builder.setView(dialogView);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                            Window window = dialog.getWindow();
                            if (window != null) {
                                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                                layoutParams.copyFrom(window.getAttributes());
                                layoutParams.width = 600;
                                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                                window.setAttributes(layoutParams);
                            }
                            buttonClose.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });
                            AppCompatImageButton buttonDelete = dialogView.findViewById(R.id.button_timetable_eachButton_delete);
                            buttonDelete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    deleteLecture(p.getLectureName());
                                    dialog.dismiss();
                                    recreate();
                                }
                            });
                        });
                        button.setX(x);
                        button.setY(y);
                        button.setGravity(TOP);
                        button.setPadding(4, 4, 0, 0);
                        button.setTextSize(12);
                        button.setHeight(eachBlockSize[1] * ((p.getEndHour(i) * 60 + p.getEndMin(i)) - (p.getStartHour(i) * 60 + p.getStartMin(i))) / 60);
                        button.setBackground(ContextCompat.getDrawable(getContext(), p.getColor()));
                        binding.getRoot().addView(button);
                    }
                }
            }
        } else {
            binding.timetableTable.setVisibility(View.GONE);
            binding.timetableTextViewTableName.setVisibility(View.GONE);
            TextView textView = new TextView(getContext());
            textView.setText("에브리타임에서 시간표를 추가해주세요!");
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            params.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
            params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
            params.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
            params.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
            textView.setTextSize(24);
            textView.setLayoutParams(params);
            binding.getRoot().addView(textView);

        }
    }
}

class EachLecture implements Parcelable {
    String[] lectureInfo; // 강의명 / 요일 / 시작 시간/분 / 끝 시간/분 / (에타에서 불러온건지:true or false) 순서로 저장
    int color;

    public EachLecture(String input) {
        StringTokenizer tokenizer = new StringTokenizer(input, " ");
        lectureInfo = new String[7];
        int i = 0;
        for (String info : lectureInfo) {
            info = tokenizer.nextToken();
            lectureInfo[i++] = info;
        }
        Random random = new Random();
        color = TimetableMainFragment.colorList[random.nextInt(15)];
    }

    protected EachLecture(Parcel in) {
        lectureInfo = in.createStringArray();
        color = in.readInt();
    }

    public static final Creator<EachLecture> CREATOR = new Creator<EachLecture>() {
        @Override
        public EachLecture createFromParcel(Parcel in) {
            return new EachLecture(in);
        }

        @Override
        public EachLecture[] newArray(int size) {
            return new EachLecture[size];
        }
    };

    @NonNull
    @Override
    public String toString() {
        return lectureInfo[0] + " " + lectureInfo[1];
    }

    String getLectureName() {
        return lectureInfo[0];
    }

    String getWeekDay() {
        return lectureInfo[1];
    }

    int getStartHour() {
        return Integer.parseInt(lectureInfo[2]);
    }

    int getStartMin() {
        return Integer.parseInt(lectureInfo[3]);
    }

    int getEndHour() {
        return Integer.parseInt(lectureInfo[4]);
    }

    int getEndMin() {
        return Integer.parseInt(lectureInfo[5]);
    }

    int getColor() {
        return color;
    }

    boolean getFromEveryTime() {
        return lectureInfo[6].equals("true");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeStringArray(lectureInfo);
        parcel.writeInt(color);
    }
}