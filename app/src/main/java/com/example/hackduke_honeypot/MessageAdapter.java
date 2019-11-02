package com.example.hackduke_honeypot;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    ArrayList<Message> mData;

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessageId, tvMessageTime, tvMessageData;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessageId = itemView.findViewById(R.id.tvMessageId);
            tvMessageTime = itemView.findViewById(R.id.tvMessageTime);
            tvMessageData = itemView.findViewById(R.id.tvMessageData);
        }
    }
}
