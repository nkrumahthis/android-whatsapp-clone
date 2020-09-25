package com.nkrumahsarpong.whatsappclone.adapters.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nkrumahsarpong.whatsappclone.R;

public class ChatViewHolder extends RecyclerView.ViewHolder {
    public TextView textView;
    public ImageView imageView;


    public ChatViewHolder(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.tvChatText);
        imageView = itemView.findViewById(R.id.ivChatImg);
    }
}
