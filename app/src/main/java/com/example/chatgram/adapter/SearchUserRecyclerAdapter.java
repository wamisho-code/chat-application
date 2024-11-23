package com.example.chatgram.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatgram.ChatActivity;
import com.example.chatgram.R;
import com.example.chatgram.model.UserModel;
import com.example.chatgram.utils.AndroidUtil;
import com.example.chatgram.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class SearchUserRecyclerAdapter extends FirestoreRecyclerAdapter<UserModel, SearchUserRecyclerAdapter.UserModalViewHolder> {
    Context context;
    public SearchUserRecyclerAdapter(@NonNull FirestoreRecyclerOptions<UserModel> options,Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserModalViewHolder holder, int i, @NonNull UserModel userModel) {
        holder.usernameText.setText(userModel.getUsername());
        holder.phoneText.setText(userModel.getPhone());
        if (userModel.getUserId() != null && FirebaseUtil.currentUserId() != null && userModel.getUserId().equals(FirebaseUtil.currentUserId())) {
            holder.usernameText.setText(userModel.getUsername() + " (me)");
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, ChatActivity.class);
                AndroidUtil.passUserModelAsIntent(intent,userModel);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);
            }
        });

    }

    @NonNull
    @Override
    public UserModalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.search_user_recycler_row,parent,false);
        return  new UserModalViewHolder(view);
    }

    class UserModalViewHolder extends RecyclerView.ViewHolder{
        TextView usernameText,phoneText;
        ImageView profilePic;
        public UserModalViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameText=itemView.findViewById(R.id.usernametxt);
            phoneText=itemView.findViewById(R.id.phonetxt);
            profilePic=itemView.findViewById(R.id.profile_pic_image_view);
        }
    }
}
