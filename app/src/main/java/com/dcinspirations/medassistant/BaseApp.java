package com.dcinspirations.medassistant;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class BaseApp extends Application {

    public static final String CHANNEL_1_ID= "channel1";
    public static final String CHANNEL_2_ID= "channel2";
    static TextToSpeech tts;
    static int result;
    static Context ctx;
    static Voice voice;
    static File appDir ;
    public static boolean done = true;


    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        ctx = getApplicationContext();
        createNotificationChannels();

    }
    private void createNotificationChannels() {
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){
            NotificationChannel gc = new NotificationChannel(
                    CHANNEL_1_ID,
                    "mm channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            gc.setDescription("MedMan channel");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(gc);
        }
    }



}
