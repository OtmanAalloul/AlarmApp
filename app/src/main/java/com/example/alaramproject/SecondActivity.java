package com.example.alaramproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        ImageButton addAlarmButton = findViewById(R.id.addAlarm);
        addAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddAlarmClick(v);
            }
        });
    }
    public void onAddAlarmClick(View view) {
        // Handle the click event for the ImageButton
        Intent intent = new Intent(this, activityAddAlarm.class);
        startActivity(intent);
    }


}
