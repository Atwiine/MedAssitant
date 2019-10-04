package com.dcinspirations.medassistant;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.SystemClock;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.dcinspirations.medassistant.BaseApp.CHANNEL_1_ID;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Calendar calendar = Calendar.getInstance();
        DatabaseClass db = new DatabaseClass(context);
        String medinfo[] = intent.getExtras().getStringArray("medinfo");
        String basic[] = new Sp().getBasics();
        new MailAsync().execute(basic[1],"MedAssitant Reminder","Time for your " + medinfo[1] + " medication, " + medinfo[3] + "(" + medinfo[4] + " per day)");

//        String[] startdate = medinfo[5].split("/");
//        String[] today = getDate().split("/");
//        String[] tom = Tomorrow();
//        Calendar sd = getCalendar(startdate);
//        Calendar td = getCalendar(today);
//        Calendar tom1 = getCalendar(tom);
//        calendar.setTime(tom1.getTime());
//        if(isBefore(startdate,today)){
//            Toast.makeText(context, "works", Toast.LENGTH_LONG).show();
//            Cursor c = db.getDur(Integer.parseInt(medinfo[0]));
//            while (c.moveToNext()){
//                int duration = c.getInt(c.getColumnIndex("duration"));
//                Toast.makeText(context, String.valueOf(duration), Toast.LENGTH_SHORT).show();
//                if(duration>0) {
                    AddNotification(context, "Time for your " + medinfo[1] + " medication", medinfo[3] + "(" + medinfo[4] + " per day)", medinfo);
//                    duration-=1;
//                    db.updateDur(Integer.parseInt(medinfo[0]),duration);
//                setAlarm(context,medinfo);
//                }
//            }


//        }




    }
    Boolean isBefore(String sd[], String td[]){
        if(Integer.parseInt(sd[2])<=Integer.parseInt(td[2])){
            if(Integer.parseInt(sd[1])<=Integer.parseInt(td[1])) {
                if (Integer.parseInt(sd[0]) <= Integer.parseInt(td[0])) {
                    return true;
                }
            }
        }
        return false;
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
        Intent notifyIntent = new Intent(ctx, Dash.class);

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
    public void setAlarm(Context context,String med2[]){
        Intent intent =new Intent(context, NotificationReceiver.class);
        intent.putExtra("medinfo",med2);
        PendingIntent pi = PendingIntent.getBroadcast(context,Integer.parseInt(med2[0]),intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = Calendar.getInstance();
        String[] tom = Tomorrow();
        String[] dtime = med2[6].split(":");
//        calendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(tom[0]));
//        calendar.set(Calendar.MONTH,Integer.parseInt(tom[1]));
//        calendar.set(Calendar.YEAR,Integer.parseInt(tom[2]));
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(dtime[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(dtime[1]));
        calendar.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC, calendar.getTimeInMillis(),pi);
        }else{
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+calendar.getTimeInMillis(),pi);
        }
//
    }
    private String[] Tomorrow(){
        String date[] = getDate().split("/");
        if((date[1].equalsIgnoreCase("4")||date[1].equalsIgnoreCase("6")||date[1].equalsIgnoreCase("9")||date[1].equalsIgnoreCase("10"))
                && date[0].equalsIgnoreCase("30")){
            date[0] = "1";
            date[1] = String.valueOf(Integer.parseInt(date[1])+1);
        }else if(date[1].equalsIgnoreCase("12")&&date[0].equalsIgnoreCase("31")){
            date[0] = "1";
            date[1] = "1";
            date[2] = String.valueOf(Integer.parseInt(date[2])+1);
        }else if(date[0].equalsIgnoreCase("31")){
            date[0] = "1";
            date[1] = String.valueOf(Integer.parseInt(date[1])+1);
        }else{
            date[0] = String.valueOf(Integer.parseInt(date[0])+1);
        }

        return date;
    }
    class MailAsync extends AsyncTask<String,String,Void> {


        @Override
        protected Void doInBackground(String... strings) {
            try {
                GMailSender sender = new GMailSender("dchiaha@gmail.com", "08990108");
                sender.sendMail(strings[1],
                        strings[2],
                        "dchiaha@gmail.com",
                        strings[0]);
            } catch (Exception e) {
            }
            return null;
        }
    }
}
