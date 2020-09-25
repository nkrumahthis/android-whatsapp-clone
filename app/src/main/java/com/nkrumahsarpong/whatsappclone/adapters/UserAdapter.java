package com.nkrumahsarpong.whatsappclone.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nkrumahsarpong.whatsappclone.R;
import com.nkrumahsarpong.whatsappclone.activities.ChatActivity;
import com.nkrumahsarpong.whatsappclone.adapters.viewholders.UserViewHolder;
import com.nkrumahsarpong.whatsappclone.models.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {
    private List<User> users;
    private Context context;

    public UserAdapter(List<User> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_user,
                parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        final User user = users.get(position);
        holder.tvUsername.setText(user.getUsername());
        if(user.getImageUrl().equals("default"))
            holder.ivUserImage.setImageResource(R.drawable.ic_baseline_account_circle_24);
        else {
            Glide.with(context)
                    .load(user.getImageUrl())
                    .into(holder.ivUserImage);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ChatActivity.class);
                i.putExtra("userid", user.getId());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(users == null) return 0;
        else return users.size();
    }

}
