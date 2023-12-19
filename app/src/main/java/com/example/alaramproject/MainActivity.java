package com.example.alaramproject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout firstPage;
    private TextView timeText;
    private TextView dateText;
    private Handler handler;
    private float startY;
    private GestureDetector gestureDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstPage = findViewById(R.id.firstPage);
        timeText = findViewById(R.id.timeText);
        dateText = findViewById(R.id.timeText2);
        updateTime();

        handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(timeUpdateRunnable, 1000); // Update every 1 second

        gestureDetector = new GestureDetector(this, new GestureListener());

        firstPage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        float deltaY = event.getRawY() - startY;
                        if (deltaY < -200) {
                            // Swipe up detected, navigate to the second page
                            Log.d("SwipeGesture", "onFling: Swipe detected");
                            setContentView(R.layout.activity_second);
                            handler.removeCallbacks(timeUpdateRunnable); // Stop time updates
                        }
                        return true;
                    default:
                        return false;
                }
            }
        });

        // Adjust text size based on screen density
        adjustTextSize();

        // Use Display
        useDisplay();
    }

    private void adjustTextSize() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        float screenDensity = displayMetrics.density;

        if (screenDensity > 2) {
            // High-density screen
            timeText.setTextSize(48);
            dateText.setTextSize(48); // Set the size for dateText
        } else {
            // Medium or low-density screen
            timeText.setTextSize(32);
            dateText.setTextSize(32); // Set the size for dateText
        }
    }

    private void updateTime() {
        // Get the current date and time
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH : mm", Locale.getDefault());

        String currentDate = dateFormat.format(calendar.getTime());
        String currentTime = timeFormat.format(calendar.getTime());

        // Set the date and time to the respective TextViews
        timeText.setText(currentTime);
        dateText.setText(currentDate);
    }

    private final Runnable timeUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            updateTime();
            handler.postDelayed(this, 1000); // Update every 1 second
        }
    };

    private void useDisplay() {
        // Get the default display
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();

        // Get display metrics
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);

        int screenWidthPixels = displayMetrics.widthPixels;
        int screenHeightPixels = displayMetrics.heightPixels;
        float density = displayMetrics.density;

        Log.d("DisplayInfo", "Screen width: " + screenWidthPixels + " pixels");
        Log.d("DisplayInfo", "Screen height: " + screenHeightPixels + " pixels");
        Log.d("DisplayInfo", "Screen density: " + density);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(timeUpdateRunnable); // Stop time updates when the activity is destroyed
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 200;
        private static final int SWIPE_VELOCITY_THRESHOLD = 200;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float deltaY = e2.getY() - e1.getY();
            if (Math.abs(deltaY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                // Swipe up detected, navigate to the second page or perform desired action
                setContentView(R.layout.activity_second);
                handler.removeCallbacks(timeUpdateRunnable); // Stop time updates
                return true;
            }
            return false;
        }
    }
}
