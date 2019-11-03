package com.example.hackduke_honeypot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class PostMessageActivity extends AppCompatActivity {
    Button bCancel, bPost;
    EditText getMessageData;

    ImageView ivBack;
    ImageView ivPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_message);

        //bCancel = findViewById(R.id.bCancel);
        //bPost = findViewById(R.id.bPost);
        getMessageData = findViewById(R.id.getMessageData);
        ivBack = findViewById(R.id.ivBack);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ivPost = findViewById(R.id.ivPost);

        ivPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message("3", getMessageData.getText().toString(), "9");
                Intent intent = new Intent(PostMessageActivity.this, MainActivity.class);
                intent.putExtra(MainActivity.MESSAGE_DATA, message);
                setResult(MainActivity.REQ_CODE, intent);
                //startActivity(intent);
                finish();
            }
        });
//        Date date = new Date();
//        CharSequence time  = DateFormat.format("EEEE, MMMM d, yyyy ", date.getTime());



    }

}
