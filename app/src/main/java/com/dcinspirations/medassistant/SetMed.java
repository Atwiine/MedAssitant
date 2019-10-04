package com.dcinspirations.medassistant;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.StrictMode;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dcinspirations.medassistant.models.med;

import java.util.Calendar;

public class SetMed extends AppCompatActivity implements View.OnClickListener {
    String[] Datalist = {"Select Drug Type","Pills","Vaccine"};
    ArrayAdapter<String> adapter;
    Spinner mt;
    EditText ail,mn,dos,dpd;
    TextView error,back,action,sd,nottime;
    LinearLayout ma,na;
    Dialog cd;
    DatabaseClass db;
    Toolbar tb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_med);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        db = new DatabaseClass(this);
        cd = new Dialog(this);
        tb = findViewById(R.id.toolbar);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        dpd = findViewById(R.id.dpd);
        sd = findViewById(R.id.sd);
        nottime = findViewById(R.id.nottime);
        nottime.setOnClickListener(this);
        sd.setOnClickListener(this);
        nottime.setOnClickListener(this);
        back = findViewById(R.id.back);
        action = findViewById(R.id.action);
        back.setOnClickListener(this);
        action.setOnClickListener(this);
        ma = findViewById(R.id.medarea);
        na = findViewById(R.id.notarea);
        ail = findViewById(R.id.ail);
        mn = findViewById(R.id.mn);
        dos = findViewById(R.id.dos);
        error = findViewById(R.id.error);
        mt = findViewById(R.id.mt);
        adapter = new ArrayAdapter<>(this,R.layout.spinner_layout,Datalist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mt.setAdapter(adapter);
    }

    private void next(){
        String ailt = ail.getText().toString().trim();
        String mnt = mn.getText().toString().trim();
        String dost = dos.getText().toString().trim();
        String mtt = mt.getSelectedItem().toString();
        if(!(ailt.isEmpty()||mnt.isEmpty()||dost.isEmpty()||mtt.equalsIgnoreCase("Select Drug Type"))){
            error.setVisibility(View.GONE);
            ma.setVisibility(View.GONE);
            na.setVisibility(View.VISIBLE);
            back.setVisibility(View.VISIBLE);
            action.setText("Save");
        }else{
            error.setVisibility(View.VISIBLE);
        }

    }

    private void back(){
        ma.setVisibility(View.VISIBLE);
        na.setVisibility(View.GONE);
        back.setVisibility(View.GONE);
        action.setText("Next");
    }

    private void save(){
        String dpdt = dpd.getText().toString().trim();
        String sdt = sd.getText().toString().trim();
        String ntt = nottime.getText().toString().trim();
        if(!(dpdt.isEmpty()||sdt.isEmpty()||ntt.isEmpty())){
            error.setVisibility(View.GONE);
            saveJob();
        }else{
            error.setVisibility(View.VISIBLE);
        }

    }
    private void saveJob(){
        String ailt = ail.getText().toString().trim();
        String mnt = mn.getText().toString().trim();
        String dost = dos.getText().toString().trim();
        String mtt = mt.getSelectedItem().toString();
        String dpdt = dpd.getText().toString().trim();
        String sdt = sd.getText().toString().trim();
        String ntt = nottime.getText().toString().trim();

        Boolean insert = db.insertIntoMeds(ailt,mtt,mnt,dpdt,sdt,getEndDate(sdt,dost,dpdt),ntt,0);
        med m = new med(new Sp().getLast(),ailt,mtt,mnt,dpdt,sdt,getEndDate(sdt,dost,dpdt),ntt,0);
        String[] minfo = {String.valueOf(new Sp().getLast()),ailt,mtt,mnt,dpdt,sdt};
        if(insert){
            Toast.makeText(this, "Medication not saved", Toast.LENGTH_LONG).show();
        }else{
            setAlarm(m);
            finish();
        }
    }
    private int getEndDate(String date, String tot, String dos){
        int a = Integer.parseInt(tot)/Integer.parseInt(dos);
        return a;
    }

    public void setAlarm(med m){
        Intent intent =new Intent(getApplicationContext(), NotificationReceiver.class);
        String[] med2 = {String.valueOf(m.id),m.ailment,m.drugtype,m.drugname,m.dosage,m.startd,m.nottime};
        intent.putExtra("medinfo",med2);
        PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(),m.id,intent,PendingIntent.FLAG_UPDATE_CURRENT);
//            boolean alarmUp = (PendingIntent.getBroadcast(context,m.id,intent,PendingIntent.FLAG_UPDATE_CURRENT)!= null);
//            if(!alarmUp) {

        Calendar calendar = Calendar.getInstance();
        String[] tom = m.startd.split("/");
        String[] dtime = m.nottime.split(":");
//        calendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(tom[0]));
//        calendar.set(Calendar.MONTH,Integer.parseInt(tom[1]));
//        calendar.set(Calendar.YEAR,Integer.parseInt(tom[2]));
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(dtime[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(dtime[1]));
        calendar.set(Calendar.SECOND, 0);



        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), 1000*60*60*24, pi);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC, calendar.getTimeInMillis(),pi);
//        }else{
//            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+calendar.getTimeInMillis(),pi);
//        }
//        boolean alarmUp = (PendingIntent.getBroadcast(getApplicationContext(), m.id,
//                new Intent(getApplicationContext(), NotificationReceiver.class),
//                PendingIntent.FLAG_NO_CREATE) != null);
//
//        if (alarmUp)
//        {
//        }else{
//            Toast.makeText(this, "nothing ooooo", Toast.LENGTH_LONG).show();
//        }

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.action:
                TextView b = (TextView) v;
                if(b.getText().toString().equalsIgnoreCase("next")){
                    next();
                }else{
                    save();
                }
                break;
            case R.id.back:
                back();
                break;
            case R.id.sd:
                calJob();
                break;
                case R.id.nottime:
                    clockjob();
                    break;

        }
    }

    private void clockjob() {
        cd.setContentView(R.layout.clock);
        final TimePicker tp = cd.findViewById(R.id.tp);
        tp.setIs24HourView(true);
        cd.show();
        Button done = cd.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cd.cancel();
                int hour, minute;
                String am_pm;
                if (Build.VERSION.SDK_INT >= 23 ){
                    hour = tp.getHour();
                    minute = tp.getMinute();
                }
                else{
                    hour = tp.getCurrentHour();
                    minute = tp.getCurrentMinute();
                }
                if(hour > 12) {
                    am_pm = "PM";
//                    hour = hour - 12;
                }
                else
                {
                    am_pm="AM";
                }
                nottime.setText(hour +":"+ minute);

            }
        });
    }

    private void calJob(){
        cd.setContentView(R.layout.calendar);
        cd.show();
        CalendarView cv = cd.findViewById(R.id.calendar);
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                cd.cancel();
                sd.setText(String.valueOf(dayOfMonth)+"/"+String.valueOf(month+1)+"/"+String.valueOf(year));
            }
        });
    }





}
