package com.example.uiproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class PWAuthActivity extends AppCompatActivity {

    private static final String TAG = "cjw";

    private FirebaseAuth mAuth;

    private FirebaseFirestore db;

    boolean isNickExist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwauth);

        initFirebaseAuth();
        initializeCloudFirestore();

        EditText emailEditText = findViewById(R.id.email);

        EditText nicknameEditText = findViewById(R.id.nickname);
        EditText univnameEditText = findViewById(R.id.univname);

        EditText passwordEditText = findViewById(R.id.password);

        Button signUpButton = findViewById(R.id.sign_up);
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

        Button signInButton = findViewById(R.id.sign_in);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                signIn(email, password);
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
                        Toast.makeText(PWAuthActivity.this, "이미 존재하는 닉네임입니다!", Toast.LENGTH_SHORT).show();
                    } else {
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(PWAuthActivity.this, new OnCompleteListener<AuthResult>() {
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
                                            startActivity(new Intent(PWAuthActivity.this, BottomMain.class)
                                                    .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
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
//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "createUserWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//
//                            // Firestore에 사용자 정보 저장 (시간표는 null로 설정)
//                            saveUserToFirestore(user, nickname, univname, null); // 시간표를 null로 전달
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                            Log.d(TAG, "createUserWithEmail:failure");
//                            Toast.makeText(getApplicationContext(), "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
//                        }
//                    }
//                });
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
                            startActivity(new Intent(PWAuthActivity.this, BottomMain.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
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

//    private void updateUI(FirebaseUser user) {
//        if (user != null) {
//            Intent intent = new Intent(this, SignOutActivity.class);
//            intent.putExtra("USER_PROFILE", "email: " + user.getEmail() + "\n" + "uid: " + user.getUid());
//
//            startActivity(intent);
//        }
//    }


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