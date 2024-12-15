package com.example.uiproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uiproject.databinding.ActivityRegisterMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.univcert.api.UnivCert;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class RegisterMain extends AppCompatActivity {

    String value = BuildConfig.API_KEY;

    private static final String TAG = "cjw";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    boolean isNickExist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRegisterMainBinding binding = ActivityRegisterMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initFirebaseAuth();
        initializeCloudFirestore();

        binding.buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.register_signup_dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterMain.this, R.style.CustomDialogTheme);
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();

                EditText emailEditText = dialogView.findViewById(R.id.editText_register_email);

                EditText nicknameEditText = dialogView.findViewById(R.id.editText_register_nickname);
                EditText univnameEditText = dialogView.findViewById(R.id.editText_register_univ);

                EditText passwordEditText = dialogView.findViewById(R.id.editText_register_pw);

                Button signUpButton = dialogView.findViewById(R.id.button_register_regiButton);
                signUpButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String email = emailEditText.getText().toString();
                        String nickname = nicknameEditText.getText().toString();
                        String univname = univnameEditText.getText().toString();
                        String password = passwordEditText.getText().toString();

                        signUp(email, nickname, univname, password);
                    }
                });
/// 구별하기 쉽게 나누는 선

// 구별하기 쉽게 나누는 선

                Button getcertify = dialogView.findViewById(R.id.button_getCertificationMail);
                getcertify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String email = emailEditText.getText().toString().trim();
                        String univName = univnameEditText.getText().toString().trim();
                        String nickname = nicknameEditText.getText().toString().trim();
                        String pw = passwordEditText.getText().toString().trim();
                        if (email.isBlank() || univName.isBlank() || nickname.isBlank() || pw.isBlank()) {
                            Toast.makeText(RegisterMain.this, "모두 입력해주세요.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(RegisterMain.this, "1분넘게 인증번호가 안온" + "다면 재인증", Toast.LENGTH_SHORT).show();



                        Thread t = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Map<String, Object> response = UnivCert.check(univName);
                                    boolean success = (boolean) response.get("success");

                                    if (success) {
                                        UnivCert.clear(value);
                                        UnivCert.certify(value, email, univName, true);
                                    }
                                    else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
//                                        Toast.makeText(RegisterMain.this, "대학교이름 불일치", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
                        t.start();

                        LayoutInflater inflater = getLayoutInflater();
                        View codedialogView = inflater.inflate(R.layout.code_certify_dialog, null);
                        AlertDialog.Builder codedialogbuilder = new AlertDialog.Builder(RegisterMain.this, R.style.CustomDialogTheme);
                        codedialogbuilder.setView(codedialogView);
                        AlertDialog codedialog = codedialogbuilder.create();

                        EditText codeEditText = codedialogView.findViewById(R.id.editText_email_code);
                        Button certifyCode = codedialogView.findViewById(R.id.button_certify_code);

                        certifyCode.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                int code = Integer.parseInt(codeEditText.getText().toString());

                                Thread t = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Map<String, Object> response = UnivCert.certifyCode(value, email, univName, code);
                                            boolean success = (boolean) response.get("success");

                                            if (success) {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        signUpButton.setEnabled(true);
                                                        signUpButton.setTextColor(getColor(R.color.black));
                                                        Toast.makeText(RegisterMain.this, "인증 성공", Toast.LENGTH_SHORT).show();
                                                        codedialog.dismiss();
                                                    }
                                                });
                                            }
                                            else {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(RegisterMain.this, "인증코드 불일치", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                });
                                t.start();
                            }
                        });
                        codedialog.show();
                    }
                });
                dialog.show();
            }
        });

        binding.buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.register_signin_dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterMain.this, R.style.CustomDialogTheme);
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();

                EditText emailEditText = dialogView.findViewById(R.id.editText_login_email);
                EditText passwordEditText = dialogView.findViewById(R.id.editText_login_pw);

                Button signInButton = dialogView.findViewById(R.id.button_signin);
                signInButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String email = emailEditText.getText().toString();
                        String password = passwordEditText.getText().toString();
                        if (email.isBlank() || password.isBlank()) {
                            Toast.makeText(RegisterMain.this, "입력을 완료해주세요.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        signIn(email, password);
                    }
                });
                dialog.show();
            }
        });
    }



    private void initFirebaseAuth() {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
//            updateUI(currentUser);
        }
    }

    private void signUp(String email, String nickname, String univname, String password) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");
        Query query = usersRef.whereEqualTo("nickname", nickname);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        Toast.makeText(RegisterMain.this, "이미 존재하는 닉네임입니다!", Toast.LENGTH_SHORT).show();
                    } else {
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(RegisterMain.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d(TAG, "createUserWithEmail:success");
                                            FirebaseUser user = mAuth.getCurrentUser();

                                            // Firestore에 사용자 정보 저장 (시간표는 null로 설정)
                                            saveUserToFirestore(user, nickname, univname, null, null); // 시간표를 null로 전달
                                            SharedPreferences pref = getSharedPreferences("userInfo", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = pref.edit();
                                            editor.putString("uid", user.getUid());
                                            editor.commit();
                                            Log.d(TAG, pref.getString("uid", "error occurred"));
                                            startActivity(new Intent(RegisterMain.this, BottomMain.class));
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                            Log.d(TAG, "createUserWithEmail:failure");
                                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();
//                                            updateUI(null);
                                        }
                                    }
                                });
                    }
                }
            }
        });
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            SharedPreferences pref = getSharedPreferences("userInfo", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("uid", user.getUid());
                            editor.commit();
                            Log.d(TAG, pref.getString("uid", "error occurred"));
                            startActivity(new Intent(RegisterMain.this, BottomMain.class));
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Log.d(TAG, "signInWithEmail:failure");
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                    }
                });
    }

    private void initializeCloudFirestore() {
        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance();
    }

    private void saveUserToFirestore(FirebaseUser user, String nickname, String univname, List<Boolean> schedule, List<Boolean> sharedSchedule) {
        User newUser = new User(nickname, univname, schedule, sharedSchedule); // 닉네임과 시간표를 포함하여 User 객체 생성

        // Firestore에 사용자 정보 저장, uid를 문서 ID로 사용
        db.collection("users").document(user.getUid())
                .set(newUser) // User 객체를 Firestore에 저장
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "User data successfully written to Firestore");
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error writing user data to Firestore", e);
                });
    }
}