package com.nkrumahsarpong.whatsappclone.adapters.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nkrumahsarpong.whatsappclone.R;

public class UserViewHolder extends RecyclerView.ViewHolder {

    public TextView tvUsername;
    public ImageView ivUserImage;

    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        tvUsername = itemView.findViewById(R.id.tvUsername);
        ivUserImage = itemView.findViewById(R.id.ivUserImage);
    }
}
