package com.example.hackduke_honeypot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    ArrayList<Message> mData;

    public MessageAdapter(ArrayList<Message> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Message message = mData.get(position);
        holder.tvMessageId.setText(message.messageId);
        holder.tvMessageTime.setText(message.messageTime);
        holder.tvMessageData.setText(message.messageData);
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
