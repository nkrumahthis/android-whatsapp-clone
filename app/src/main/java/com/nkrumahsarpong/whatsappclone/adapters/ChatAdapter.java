package com.nkrumahsarpong.whatsappclone.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nkrumahsarpong.whatsappclone.R;
import com.nkrumahsarpong.whatsappclone.activities.ChatActivity;
import com.nkrumahsarpong.whatsappclone.adapters.viewholders.ChatViewHolder;
import com.nkrumahsarpong.whatsappclone.models.Chat;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder> {
    private List<Chat> chats;
    private Context context;
    private String imgUrl;

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    public ChatAdapter(Context context, List<Chat> chats, String imgUrl){
        this.chats = chats;
        this.context = context;
        this.imgUrl = imgUrl;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == MSG_TYPE_LEFT){
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
        }
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        final Chat chat = chats.get(position);
        holder.textView.setText(chat.getMessage());
        if(imgUrl.equals("default"))
            holder.imageView.setImageResource(R.drawable.ic_baseline_account_circle_24);
        else {
            Glide.with(context).load(imgUrl).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        if (chats == null) return 0;
        else return chats.size();
    }

    @Override
    public int getItemViewType(int position) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(chats.get(position).getSender().equals(currentUser.getUid())) return MSG_TYPE_RIGHT;
        else return MSG_TYPE_LEFT;
    }
}
