package com.example.hackduke_honeypot;

import android.os.Parcel;
import android.os.Parcelable;

public class Message implements Parcelable {
    String messageId, messageData, messageTime;

    public Message(String messageId, String messageData, String messageTime) {
        this.messageId = messageId;
        this.messageData = messageData;
        this.messageTime = messageTime;
    }

    protected Message(Parcel in) {
        messageId = in.readString();
        messageData = in.readString();
        messageTime = in.readString();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(messageId);
        parcel.writeString(messageData);
        parcel.writeString(messageTime);
    }
}
