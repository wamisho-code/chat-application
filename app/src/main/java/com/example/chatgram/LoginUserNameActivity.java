package com.example.chatgram;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chatgram.model.UserModel;
import com.example.chatgram.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

public class LoginUserNameActivity extends AppCompatActivity {
    EditText usernameinput;
    Button letmeinbtn;

    UserModel userModel;
    ProgressBar progressBar;
    String phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_user_name);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        phoneNumber=getIntent().getStringExtra("phone");
        usernameinput=findViewById(R.id.login_username);
        letmeinbtn=findViewById(R.id.login_let_me_in_btn);
        letmeinbtn.setOnClickListener(v -> {
            setUsername();
        });
        progressBar=findViewById(R.id.login_progressbar);
        getUsername();
    }
    void setUsername(){

        String username=usernameinput.getText().toString();
        String currentUserid=FirebaseUtil.currentUserId();
        if(username.isEmpty()|| username.length()<3){
            usernameinput.setError("Username should be at least 3 character");
            return;
        }
        setInProgress(true);
        if (userModel!=null){
                userModel.setUsername(username);
        }else {
            userModel=new UserModel(phoneNumber,username, Timestamp.now(),currentUserid);
        }
        FirebaseUtil.currentUserDetials().set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                setInProgress(false);
                if(task.isSuccessful()){
                    Intent intent= new Intent(LoginUserNameActivity.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });

    }

    private void getUsername() {
        setInProgress(true);
        FirebaseUtil.currentUserDetials().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                setInProgress(false);
                if(task.isSuccessful()){
                   userModel=task.getResult().toObject(UserModel.class);
                    if(userModel!=null){
                        usernameinput.setText(userModel.getUsername());
                    }
                }
            }
        });
    }

    void setInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            letmeinbtn.setEnabled(false);
        }else {
            progressBar.setVisibility(View.GONE);
            letmeinbtn.setEnabled(true);
        }
    }
}