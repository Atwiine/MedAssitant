package com.dcinspirations.medassistant;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.dcinspirations.medassistant.BaseApp.CHANNEL_1_ID;

public class MyJobService extends JobService {
    String[] medinfo;
    Context context = BaseApp.ctx;
    Boolean jc = false;
//    public MyJobService(String[] m){
//        medinfo = m;
//        context = BaseApp.ctx;
//    }
    @Override
    public boolean onStartJob(JobParameters params) {
        medinfo = params.getExtras().getStringArray("m");
        doInBg(params);
        return true;
    }

    private void doInBg(final JobParameters params) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DatabaseClass db = new DatabaseClass(context);
                String[] startdate = medinfo[5].split("/");
                String[] today = getDate().split("/");
                Calendar sd = getCalendar(startdate);
                Calendar td = getCalendar(today);
                if(jc) return;
                if(!td.before(sd)){
                    Cursor c = db.getDur(Integer.parseInt(medinfo[0]));
                    while (c.moveToNext()){
                        int duration = c.getInt(c.getColumnIndex("duration"));
//                        Toast.makeText(context, String.valueOf(duration), Toast.LENGTH_SHORT).show();
                        if(duration>0) {
                            AddNotification(context, "Time for your " + medinfo[1] + " medication", medinfo[3] + "(" + medinfo[4] + " per day)", medinfo);
                            duration-=1;
                            db.updateDur(Integer.parseInt(medinfo[0]),duration);
                        }
                    }

                }
                jobFinished(params,false);
            }
        }).start();
    }



    public static Calendar getCalendar(String[] date){
        Calendar a = Calendar.getInstance();
        a.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[0]));
        a.set(Calendar.MONTH, Integer.parseInt(date[1]));
        a.set(Calendar.YEAR, Integer.parseInt(date[2]));
        return a;
    }
    public static String getDate(){
        SimpleDateFormat tf = new SimpleDateFormat("MM/YYYY");
        String date = tf.format( Calendar.getInstance().getTime());
        return String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH))+"/"+date;
    }
    private void AddNotification(Context ctx, String Title, String message, String med[]){
        NotificationManagerCompat notificationManagerCompat;
        notificationManagerCompat = NotificationManagerCompat.from(ctx);
        Intent notifyIntent = new Intent(ctx, ViewMed.class);

        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                ctx, Integer.parseInt(med[0]), notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
        );
        Notification notification;
        if(med[2].equalsIgnoreCase("pills")){
            notification = new NotificationCompat.Builder(ctx,CHANNEL_1_ID)
                    .setSmallIcon(R.drawable.med)
                    .setContentTitle(Title)
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setContentIntent(notifyPendingIntent)
                    .setAutoCancel(true)
                    .build();
        }else{
            notification = new NotificationCompat.Builder(ctx,CHANNEL_1_ID)
                    .setSmallIcon(R.drawable.vac)
                    .setContentTitle(Title)
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setContentIntent(notifyPendingIntent)
                    .setAutoCancel(true)
                    .build();
        }


        notificationManagerCompat.notify(100,notification);
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        jc = true;
        return false;
    }
}
