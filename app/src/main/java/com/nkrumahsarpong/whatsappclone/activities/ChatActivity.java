package com.nkrumahsarpong.whatsappclone.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nkrumahsarpong.whatsappclone.R;
import com.nkrumahsarpong.whatsappclone.adapters.ChatAdapter;
import com.nkrumahsarpong.whatsappclone.models.Chat;
import com.nkrumahsarpong.whatsappclone.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    TextView tvUsername;
    ImageView ivUserImage;
    RecyclerView recyclerView;
    EditText etMessage;
    Button btSend;
    ProgressBar progressBar;

    FirebaseUser currentUser;
    DatabaseReference userReference;
    Intent intent;
    User user;

    String userImageUrl;
    String userId;

    List<Chat> chats;
    ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = findViewById(R.id.toolBar);
        tvUsername = findViewById(R.id.tvUsername);
        ivUserImage = findViewById(R.id.ivUserImage);
        recyclerView = findViewById(R.id.recyclerView);
        etMessage = findViewById(R.id.etMessage);
        btSend = findViewById(R.id.btSend);
        progressBar = findViewById(R.id.progressBar);

        // Toolbar
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        intent = getIntent();
        setUserId(intent.getStringExtra("userid"));
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userReference = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                tvUsername.setText(user.getUsername());
                if (!user.getImageUrl().equals("default")) {
                    Glide.with(ChatActivity.this)
                            .load(user.getImageUrl())
                            .into(ivUserImage);
                }
                setUserImageUrl(user.getImageUrl());
                readMessages();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = etMessage.getText().toString();
                if (!message.equals("")) {
                    sendMessage(currentUser.getUid(), userId, message);
                    etMessage.setText("");
                }
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        chats = new ArrayList<>();
        chatAdapter = new ChatAdapter(this, chats, "default");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(chatAdapter);

        readMessages();
    }

    private void sendMessage(String sender, String receiver, String message) {
        DatabaseReference chatsReference = FirebaseDatabase.getInstance().getReference().child("chats");
        HashMap<String, Object> map = new HashMap<>();
        map.put("sender", sender);
        map.put("receiver", receiver);
        map.put("message", message);
        map.put("timestamp", System.currentTimeMillis());
        chatsReference.push().setValue(map);
    }

    private void readMessages() {
        showProgressBar();

        final String myId = currentUser.getUid();
        DatabaseReference chatReference = FirebaseDatabase.getInstance().getReference().child("chats");
        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chats.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Chat chat = data.getValue(Chat.class);
                    assert chat!=null;
                    if (chat.getReceiver().equals(myId) && chat.getSender().equals(userId)
                            || chat.getSender().equals(myId) && chat.getReceiver().equals(userId)) {

                        chats.add(chat);
                        chatAdapter.notifyDataSetChanged();
                    }
                }
                hideProgressBar();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                hideProgressBar();
            }
        });
    }

    void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    void setUserImageUrl(String url){
        this.userImageUrl = url;
    }

    void setUserId(String id){
        this.userId = id;
    }
}