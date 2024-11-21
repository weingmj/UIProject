package com.example.uiproject;

import static android.view.Gravity.TOP;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.uiproject.databinding.TimetableDialogAddplanBinding;
import com.example.uiproject.databinding.TimetableMainBinding;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicInteger;


public class TimetableMain extends AppCompatActivity {
    private TimetableMainBinding binding;
    private final int startTime = 9;
    private final int endTime = 21;
    static int [] colorList = new int[15];
    List<EachLecture> list;
    List<PairedLecture> pairedLectureList;
    boolean [][][] timetableManager;
    int success;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String userInput;
        super.onCreate(savedInstanceState);
        binding = TimetableMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
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
        binding.timetableImageButtonAddPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean [] dayClicked = {false, false, false, false, false};
                TimetableDialogAddplanBinding timetableDialogAddplanBinding = TimetableDialogAddplanBinding.inflate(getLayoutInflater());
                Dialog dialog = new Dialog(TimetableMain.this);
                dialog.setContentView(timetableDialogAddplanBinding.getRoot());
                timetableDialogAddplanBinding.buttonAddplanMon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!dayClicked[0]) {
                            setFalse(dayClicked);
                            dayClicked[0] = true;
                            timetableDialogAddplanBinding.buttonAddplanMon.setTextColor(getColor(R.color.red));
                            timetableDialogAddplanBinding.buttonAddplanTue.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanWed.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanThu.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanFri.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanAdd.setTextColor(getColor(R.color.black));
                        } else {
                            dayClicked[0] = false;
                            timetableDialogAddplanBinding.buttonAddplanMon.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanAdd.setTextColor(getColor(R.color.gray));
                        }
                    }
                });
                timetableDialogAddplanBinding.buttonAddplanTue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!dayClicked[1]) {
                            setFalse(dayClicked);
                            dayClicked[1] = true;
                            timetableDialogAddplanBinding.buttonAddplanMon.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanTue.setTextColor(getColor(R.color.red));
                            timetableDialogAddplanBinding.buttonAddplanWed.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanThu.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanFri.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanAdd.setTextColor(getColor(R.color.black));
                        } else {
                            dayClicked[1] = false;
                            timetableDialogAddplanBinding.buttonAddplanTue.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanAdd.setTextColor(getColor(R.color.gray));
                        }
                    }
                });
                timetableDialogAddplanBinding.buttonAddplanWed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!dayClicked[2]) {
                            setFalse(dayClicked);
                            dayClicked[2] = true;
                            timetableDialogAddplanBinding.buttonAddplanMon.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanTue.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanWed.setTextColor(getColor(R.color.red));
                            timetableDialogAddplanBinding.buttonAddplanThu.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanFri.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanAdd.setTextColor(getColor(R.color.black));
                        } else {
                            dayClicked[2] = false;
                            timetableDialogAddplanBinding.buttonAddplanWed.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanAdd.setTextColor(getColor(R.color.gray));
                        }
                    }
                });
                timetableDialogAddplanBinding.buttonAddplanThu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!dayClicked[3]) {
                            setFalse(dayClicked);
                            dayClicked[3] = true;
                            timetableDialogAddplanBinding.buttonAddplanMon.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanTue.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanWed.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanThu.setTextColor(getColor(R.color.red));
                            timetableDialogAddplanBinding.buttonAddplanFri.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanAdd.setTextColor(getColor(R.color.black));
                        } else {
                            dayClicked[3] = false;
                            timetableDialogAddplanBinding.buttonAddplanThu.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanAdd.setTextColor(getColor(R.color.gray));
                        }
                    }
                });
                timetableDialogAddplanBinding.buttonAddplanFri.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!dayClicked[4]) {
                            setFalse(dayClicked);
                            dayClicked[4] = true;
                            timetableDialogAddplanBinding.buttonAddplanMon.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanTue.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanWed.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanThu.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanFri.setTextColor(getColor(R.color.red));
                            timetableDialogAddplanBinding.buttonAddplanAdd.setTextColor(getColor(R.color.black));
                        } else {
                            dayClicked[4] = false;
                            timetableDialogAddplanBinding.buttonAddplanFri.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanAdd.setTextColor(getColor(R.color.gray));
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
                        //dayClicked[0]~[4]까지 true있으면 해당 일정이 가능한지 검사, 아무것도 true가 아니면 날짜선택해달라 하면 됨
                    }
                });
                dialog.show();
            }
        });
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
        Intent serviceIntent = new Intent(this, ManageLectureList.class);
        serviceIntent.putExtra("receiver", resultReceiver);
        startService(serviceIntent);
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
                    TextView button = new TextView(this);
                    button.setText(p.getLectureName());
                    button.setMinWidth(30);
                    button.setWidth(eachBlockSize[0]);
                    button.setOnClickListener(view -> {
                        Log.d("WMJ", "hi " + cnt.getAndIncrement());
                    });
                    button.setX(x);
                    button.setY(y);
                    button.setGravity(TOP);
                    button.setPadding(4, 4, 0, 0);
                    button.setTextSize(12);
                    button.setHeight(eachBlockSize[1] * ((p.getEndHour(0) * 60 + p.getEndMin(0)) - (p.getStartHour(0) * 60 + p.getStartMin(0))) / 60);
                    button.setBackground(ContextCompat.getDrawable(this, p.getColor()));
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
                        TextView button = new TextView(this);
                        button.setText(p.getLectureName());
                        button.setMinWidth(30);
                        button.setWidth(eachBlockSize[0]);
                        button.setOnClickListener(view -> {
                            Log.d("WMJ", "hi " + cnt.getAndIncrement());
                        });
                        button.setX(x);
                        button.setY(y);
                        button.setGravity(TOP);
                        button.setPadding(4, 4, 0, 0);
                        button.setTextSize(12);
                        button.setHeight(eachBlockSize[1] * ((p.getEndHour(i) * 60 + p.getEndMin(i)) - (p.getStartHour(i) * 60 + p.getStartMin(i))) / 60);
                        button.setBackground(ContextCompat.getDrawable(this, p.getColor()));
                        binding.getRoot().addView(button);
                    }
                }
            }
        } else {
            binding.timetableTable.setVisibility(View.GONE);
            binding.timetableTextViewTableName.setVisibility(View.GONE);
            TextView textView = new TextView(this);
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

class EachLecture implements Parcelable{
    String [] lectureInfo; // 강의명 / 요일 / 시작 시간/분 / 끝 시간/분 순서로 저장
    int color;
    public EachLecture(String input) {
        StringTokenizer tokenizer = new StringTokenizer(input, " ");
        lectureInfo = new String[6];
        int i = 0;
        for (String info : lectureInfo) {
            info = tokenizer.nextToken();
            lectureInfo[i++] = info;
        }
        Random random = new Random();
        color = TimetableMain.colorList[random.nextInt(15)];
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