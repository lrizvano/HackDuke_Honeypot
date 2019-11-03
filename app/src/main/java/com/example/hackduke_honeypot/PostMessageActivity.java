package com.example.hackduke_honeypot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;

public class PostMessageActivity extends AppCompatActivity {
    Button bCancel, bPost;
    EditText etMessageData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_message);

        bCancel = findViewById(R.id.bCancel);
        bPost = findViewById(R.id.bPost);
        etMessageData = findViewById(R.id.etMessageData);

//        Date date = new Date();
//        CharSequence time  = DateFormat.format("EEEE, MMMM d, yyyy ", date.getTime());
        Message message = new Message("3", etMessageData.getText().toString(), "9");

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.MESSAGE_DATA, message);
        finish();
    }
}
