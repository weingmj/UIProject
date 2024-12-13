package com.example.uiproject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import java.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SubjectChatRoom extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText messageEditText;
    private ImageButton sendButton;
    private String roomRoute;
    private ChatAdapter chatAdapter;
    String nickname;
    private List<ChatMessage> messageList = new ArrayList<>();

    private DatabaseReference chatRoomRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subject_chatroom);
        roomRoute = getIntent().getStringExtra("roomRoute");
        SharedPreferences pref = getSharedPreferences("eachRoomsNickname", MODE_PRIVATE);
        nickname = pref.getString(roomRoute, null);
        chatRoomRef = FirebaseDatabase.getInstance().getReference(roomRoute + "/메세지들");
        Log.d("WMJ", chatRoomRef.getKey());
        recyclerView = findViewById(R.id.subject_chatroom_recycler);
        messageEditText = findViewById(R.id.subject_chatroom_inputText);
        sendButton = findViewById(R.id.subject_chatroom_sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = messageEditText.getText().toString().trim();
                if (!message.isEmpty()) {
                    String messageId = chatRoomRef.push().getKey();
                    Date date = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String timeString = formatter.format(date);
                    ChatMessage chatMessage = new ChatMessage(nickname, message, timeString);
                    chatRoomRef.child(messageId).setValue(chatMessage)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    messageEditText.setText("");
                                } else {
                                    Toast.makeText(SubjectChatRoom.this, "메시지 전송 실패", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(SubjectChatRoom.this, "메시지를 입력하세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.subject_chatroom_backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        chatAdapter = new ChatAdapter(messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatAdapter);

        chatRoomRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ChatMessage message = snapshot.getValue(ChatMessage.class);
                if (message != null) {
                    messageList.add(message);
                    chatAdapter.notifyItemInserted(messageList.size() - 1);
                    recyclerView.scrollToPosition(messageList.size() - 1);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
        private List<ChatMessage> messageList;

        public ChatAdapter(List<ChatMessage> messageList) {
            this.messageList = messageList;
        }

        @NonNull
        @Override
        public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_chat_item, parent, false);
            return new ChatViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
            ChatMessage message = messageList.get(position);
            holder.sender.setText(message.sender);
            holder.content.setText(message.message);
            holder.timestamp.setText(message.timeString);
        }

        @Override
        public int getItemCount() {
            return messageList.size();
        }

        class ChatViewHolder extends RecyclerView.ViewHolder {
            TextView sender, content, timestamp;

            public ChatViewHolder(@NonNull View itemView) {
                super(itemView);
                sender = itemView.findViewById(R.id.subject_chat_name);
                content = itemView.findViewById(R.id.subject_chat_content);
                timestamp = itemView.findViewById(R.id.subject_chat_time);
            }
        }
    }
}
class ChatMessage {
    public String sender;
    public String message;
    public String timeString;

    public ChatMessage() {}

    ChatMessage(String sender, String message, String timeString) {
        this.sender = sender;
        this.message = message;
        this.timeString = timeString;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timeString;
    }
}