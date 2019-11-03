package com.example.hackduke_honeypot;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static android.widget.GridLayout.HORIZONTAL;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    FloatingActionButton fab;
    FloatingActionButton fab2;
    String macBT;


    ArrayList<Message> messages = new ArrayList<>();
    static final int REQ_CODE = 100;
    static final String MESSAGE_DATA = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("honeypot");



        //SpannableString s = new SpannableString("honeypot");
        // typeface = Typeface.createFromAsset(getAssets(), "fonts/baloo_regular.ttf");
        //s.setSpan(new TypefaceSpan(typeface), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        //setTitle(s);

        messages.add(new Message("1", "The police are attacking rioters", "1:00"));
        messages.add(new Message("2", "Free Hong Kong", "2:00"));
        messages.add(new Message("3", "Screw Blizzard", "3:00"));

        mRecyclerView = findViewById(R.id.my_recycler_view);
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(this, HORIZONTAL));

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MessageAdapter(messages);
        mRecyclerView.setAdapter(mAdapter);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, PostMessageActivity.class), REQ_CODE);
            }
        });



        FloatingActionButton fabBT = findViewById(R.id.fabBT);
        fabBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Bluetooth Mac Address");
                builder.setMessage("The hardware Bluetooth address is needed to send and receive messages over Bluetooth. Copy this from Android Settings -> System -> About Phone -> Status -> Bluetooth address.");
                final EditText input = new EditText(MainActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(new EditText(MainActivity.this));

                // Set up the buttons
                builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        macBT = input.getText().toString();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Message message = data.getParcelableExtra(MESSAGE_DATA);
            messages.add(message);
        }

    }
}
