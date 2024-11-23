package com.example.chatgram;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatgram.model.ChatRoomModel;
import com.example.chatgram.model.UserModel;
import com.example.chatgram.utils.AndroidUtil;
import com.example.chatgram.utils.FirebaseUtil;
import com.google.firebase.Timestamp;

import java.util.Arrays;

public class ChatActivity extends AppCompatActivity {
    UserModel otherUser;
    EditText messgae_input;
    TextView otherUsername;
    RecyclerView recyclerView;
    ChatRoomModel chatRoomModel;
    String chatroomId;
    ImageView sendButton,backButton,profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        otherUser= AndroidUtil.getUserModelFromIntent(getIntent());
        chatroomId= FirebaseUtil.getChatroomId(FirebaseUtil.currentUserId(),otherUser.getUserId());
        messgae_input=findViewById(R.id.chat_message_input);
        otherUsername=findViewById(R.id.other_username);
        sendButton=findViewById(R.id.message_send_btn);
        recyclerView=findViewById(R.id.chat_recyclerview);
        backButton=findViewById(R.id.back_btn);
        backButton.setOnClickListener(v -> {
            onBackPressed();
        });
        otherUsername.setText(otherUser.getUsername());
        getorCreateChatroomModel();
        sendButton.setOnClickListener(v -> {
            String message =messgae_input.getText().toString().trim();
            if (message.isEmpty()){
                return;
            }
            sendMessageToUser(message);
        });

    }
    
    private void sendMessageToUser(String message) {

    }

    private void getorCreateChatroomModel() {
        FirebaseUtil.getChatroomReference(chatroomId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                chatRoomModel=task.getResult().toObject(ChatRoomModel.class);
                if (chatRoomModel==null){
                    chatRoomModel= new ChatRoomModel(chatroomId,
                            Arrays.asList(FirebaseUtil.currentUserId(),otherUser.getUserId()),
                            Timestamp.now(),""
                            );
                    FirebaseUtil.getChatroomReference(chatroomId).set(chatRoomModel);
                }
            }
        });
    }

}