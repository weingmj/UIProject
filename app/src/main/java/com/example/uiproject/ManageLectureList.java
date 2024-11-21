package com.example.uiproject;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class ManageLectureList extends Service {
    List<EachLecture> list = null;
    List<PairedLecture> pairedLectureList;
    boolean [][][] timetableManager;
    protected HashSet<Integer> colorList;
    String originList;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = new Bundle();
        originToList();
        if (originList.equals("No data found")) {
            bundle.putInt("isSuccess", -1);
        } else {
            bundle.putInt("isSuccess", 1);
        }
        setColorList();
        translateList();
        setTimetableManager();
        bundle.putParcelableArrayList("pairedLectureList", (ArrayList<? extends Parcelable>) pairedLectureList);
        bundle.putParcelableArrayList("eachLectureList", (ArrayList<? extends Parcelable>) list);
        bundle.putSerializable("timetableManager", timetableManager);
        ResultReceiver receiver = intent.getParcelableExtra("receiver");
        receiver.send(0, bundle);
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public boolean deleteLecture(String lectureName) {
        return true;
    }

    public boolean addLecture(String lectureName) {
        return true;
    }

    public boolean isNoLectureInfo() { return list == null; }

    private void originToList() {
        SharedPreferences sharedPreferences = getSharedPreferences("app_data", MODE_PRIVATE);
        originList = sharedPreferences.getString(getString(R.string.saved_lecturelist), "No data found");
        if (originList.equals("No data found")) return;
        StringTokenizer tokenizer = new StringTokenizer(originList, "\n");
        list = new ArrayList<>(); // 원본 String 덩어리를 list에 분할해 저장
        int k = 0;
        while (tokenizer.hasMoreTokens()) {
            String temp = tokenizer.nextToken();
            if (temp.isBlank()) break;
            list.add(new EachLecture(temp));
        }
    }

    private void translateList() {
        HashSet<String> lectureSet = new HashSet<String>();
        List<String> pairLecture = new ArrayList<String>();
        for (EachLecture e : list) {
            int beforeSize = lectureSet.size();
            lectureSet.add(e.getLectureName());
            if (beforeSize == lectureSet.size()) { // 넣기 전과 넣은 후의 lectureSet의 크기가 변하지 않았으면 중복이라는 소리임
                pairLecture.add(e.getLectureName()); // 중복되는 녀석의 강의 이름 추가
            }
        }
        pairedLectureList = new ArrayList<>();
        List<String> alreadyAdd = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            PairedLecture p = new PairedLecture();
            p.timeList = new int[5][2];
            if (alreadyAdd.contains(list.get(i).getLectureName())) continue;
            if (pairLecture.contains(list.get(i).getLectureName())) {
                p.paired = true;
                int temp = 0;
                for (EachLecture e : list) {
                    if (e.getLectureName().compareTo(list.get(i).getLectureName()) == 0) {
                        p.timeList[0][temp] = dayToInt(e.lectureInfo[1]);
                        for (int j = 1; j < 5; j++) {
                            p.timeList[j][temp] = Integer.parseInt(e.lectureInfo[j + 1]);
                        }
                        temp++;
                        p.lectureName = e.getLectureName();
                        alreadyAdd.add(p.lectureName);
                    }
                }
                int color = colorList.iterator().next();
                p.color = color;
                colorList.remove(color);
            } else {
                for (EachLecture e : list) {
                    if (e.getLectureName().compareTo(list.get(i).getLectureName()) == 0) {
                        p.timeList[0][0] = dayToInt(e.lectureInfo[1]);
                        for (int j = 1; j < 5; j++) {
                            p.timeList[j][0] = Integer.parseInt(e.lectureInfo[j + 1]);
                        }
                        p.lectureName = e.getLectureName();
                    }
                }
                int color = colorList.iterator().next();
                p.color = color;
                colorList.remove(color);
            }
            pairedLectureList.add(p);
        }
        Log.d("WMJ", "hihi");
        Iterator<PairedLecture> iter = pairedLectureList.iterator();
        for (;iter.hasNext();) {
            Log.d("WMJ", iter.next().getLectureName());
        }
    }

    private void setTimetableManager() {
        timetableManager = new boolean[5][13][60]; // 13은 endHour(21) - startHour(9) + 1; 시간표 옵션 따라 수정 필요
        int startHour = 9;
        for (int k = 0; k < list.size(); k++) {
            EachLecture temp = list.get(k);
            int day = dayToInt(temp.getWeekDay());
            if (temp.getStartHour() != temp.getEndHour()) { // 시작 시간 != 종료 시간
                for (int i = temp.getStartMin(); i < 60; i++) {
                    timetableManager[day][temp.getStartHour() - startHour][i] = true;
                }
                for (int i = temp.getStartHour() + 1; i < temp.getEndHour(); i++) {
                    for (int j = 0; j < 60; j++) {
                        timetableManager[day][i - startHour][j] = true;
                    }
                }
                for (int i = 0; i < temp.getEndMin(); i++) {
                    timetableManager[day][temp.getEndHour() - startHour][i] = true;
                }
            } else {
                for (int i = temp.getStartMin(); i < temp.getEndMin(); i++) {
                    timetableManager[day][temp.getStartHour() - startHour][i] = true;
                }
            }
        }
    }

    private int dayToInt(String day) {
        if (day.compareTo("월") == 0) {
            return 0;
        } else if (day.compareTo("화") == 0) {
            return 1;
        } else if (day.compareTo("수") == 0) {
            return 2;
        } else if (day.compareTo("목") == 0) {
            return 3;
        } else if (day.compareTo("금") == 0) {
            return 4;
        } else {
            return -1;
        }
    }
    protected void setColorList() {
        colorList = new HashSet<>();
        colorList.add(R.color.medium_slate_blue);
        colorList.add(R.color.cold);
        colorList.add(R.color.pink);
        colorList.add(R.color.coral);
        colorList.add(R.color.dark_sea_green);
        colorList.add(R.color.dark_olive_green);
        colorList.add(R.color.light_sea_green);
        colorList.add(R.color._light_green);
        colorList.add(R.color.aquamarine);
        colorList.add(R.color.dark_orange);
        colorList.add(R.color.dodger_blue);
        colorList.add(R.color.light_coral);
        colorList.add(R.color.medium_orchid);
        colorList.add(R.color.light_sky_blue);
        colorList.add(R.color.rosy_brown);
    }
}

class PairedLecture implements Parcelable {
    PairedLecture(boolean paired, String lectureName, int color, int [][] timeList) {
        this.paired = paired;
        this.lectureName = lectureName;
        this.color = color;
        this.timeList = new int[5][2];
    }
    PairedLecture() {}
    boolean paired = false;
    String lectureName;
    int color;
    int [][] timeList; // row : 0 ~ 4, 0 : 요일, 1 : 시작 시, 2 : 시작 분, 3 : 종료 시, 4 : 종료 분; column : 0(default), 1(paired lecture)

    protected PairedLecture(Parcel in) {
        paired = in.readByte() != 0;
        lectureName = in.readString();
        color = in.readInt();
        timeList = new int[5][2];
        for (int i = 0; i < timeList.length; i++) {
            in.readIntArray(timeList[i]);
        }
    }
    public int getWeekDay(int index) {
        return timeList[0][index];
    }
    public boolean isPaired() {
        return paired;
    }

    public String getLectureName() {
        return lectureName;
    }

    public int getColor() {
        return color;
    }

    public int[][] getTimeList() {
        return timeList;
    }

    public int getStartHour(int index) {
        return timeList[1][index];
    }

    public int getStartMin(int index) {
        return timeList[2][index];
    }

    public int getEndHour(int index) {
        return timeList[3][index];
    }

    public int getEndMin(int index) {
        return timeList[4][index];
    }

    public static final Creator<PairedLecture> CREATOR = new Creator<PairedLecture>() {
        @Override
        public PairedLecture createFromParcel(Parcel in) {
            return new PairedLecture(in);
        }

        @Override
        public PairedLecture[] newArray(int size) {
            return new PairedLecture[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeByte((byte) (paired ? 1 : 0));
        parcel.writeString(lectureName);
        parcel.writeInt(color);
        for (int k = 0; k < timeList.length; k++) {
            parcel.writeIntArray(timeList[k]);
        }
    }
}