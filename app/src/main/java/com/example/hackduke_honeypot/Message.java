package com.example.hackduke_honeypot;

import android.os.Parcel;
import android.os.Parcelable;

public class Message {
    String messageId, messageTime, messageData;

    public Message(String messageId, String messageTime, String messageData) {
        this.messageId = messageId;
        this.messageTime = messageTime;
        this.messageData = messageData;
    }
}
