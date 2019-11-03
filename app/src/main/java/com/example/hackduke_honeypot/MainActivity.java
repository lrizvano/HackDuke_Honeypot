package com.example.hackduke_honeypot;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import static android.widget.GridLayout.HORIZONTAL;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    Button bMessage;

    ArrayList<Message> messages = new ArrayList<>();
    static final int REQ_CODE = 100;
    static final String MESSAGE_DATA = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("honeypot");

        bMessage = findViewById(R.id.bMessage);

        messages.add(new Message("1", "The police are attacking rioters", "1:00"));
        messages.add(new Message("2", "Free Hong Kong", "2:00"));
        messages.add(new Message("3", "Screw Blizzard", "3:00"));

        mRecyclerView = findViewById(R.id.my_recycler_view);
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(this, HORIZONTAL));

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MessageAdapter(messages);
        mRecyclerView.setAdapter(mAdapter);

        bMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PostMessageActivity.class);
                startActivity(intent);
            }
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
////        if (resultCode == RESULT_OK) {
////            Message message = data.getParcelableExtra(MESSAGE_DATA);
////            messages.add(message);
////        }
//    }
}
