package com.dcinspirations.medassistant.adapters;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.dcinspirations.medassistant.DatabaseClass;
import com.dcinspirations.medassistant.NotificationReceiver;
import com.dcinspirations.medassistant.R;
import com.dcinspirations.medassistant.models.med;

import java.util.Calendar;
import java.util.List;

/**
 * Created by pc on 2/18/2018.
 */

public class med_adapter extends RecyclerView.Adapter<med_adapter.viewHolder>{

    private List<med> objectlist;
    private LayoutInflater inflater;
    private Context context;

    public med_adapter(Context context, List<med> objectlist) {
        inflater = LayoutInflater.from(context);
        this.objectlist = objectlist;
        this.context=context;
    }


    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.med_layout,parent,false);
        viewHolder vholder = new viewHolder(view);
        return vholder;
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        med current = objectlist.get(position);
//        holder.setAlarm(current);
        holder.setData(current,position);

    }

    @Override
    public int getItemCount() {
        return objectlist.size();
    }


    public void refreshEvents() {
        notifyDataSetChanged();
    }


    public class viewHolder extends RecyclerView.ViewHolder{
        private TextView ail,dname,dose,status;
        private ImageView aimg;
        private int position;
        private med currentObject;

        public void setPosition(int position) {
            this.position = position;
        }

        public viewHolder(final View itemView){
            super(itemView);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
//                    Calendar calendar = Calendar.getInstance();
//                    calendar.set(Calendar.HOUR_OF_DAY,15);
//                    calendar.set(Calendar.MINUTE,43);
//                    calendar.set(Calendar.SECOND,10);
//                    Intent intent =new Intent(context, NotificationReceiver.class);
//                    PendingIntent pi = PendingIntent.getActivity(context,100,intent,PendingIntent.FLAG_UPDATE_CURRENT);
//                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_FIFTEEN_MINUTES,pi);
//
//                }
//            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    PopupMenu pm = new PopupMenu(v.getContext(),itemView, Gravity.END);
                    pm.getMenuInflater().inflate(R.menu.main3,pm.getMenu());
                    pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.remove:
                                    new DatabaseClass(context).deleteData(objectlist.get(position).id);
                                    objectlist.remove(position);
                                    refreshEvents();
                                    return true;
                            }
                            return false;
                        }
                    });
                    pm.show();
                    return false;
                }
            });
            ail = itemView.findViewById(R.id.ail);
            dname = itemView.findViewById(R.id.drug);
            dose = itemView.findViewById(R.id.dos);
            aimg = itemView.findViewById(R.id.icon);
            status = itemView.findViewById(R.id.status);
            status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!status.getText().toString().equalsIgnoreCase("completed")) {
                        changeStatus(status);
                    }
                }
            });
        }



        public void setData(med current, int position) {
            if(current.completed==1){
                this.status.setText("completed");
                this.status.getBackground().setTint(context.getResources().getColor(R.color.aux8));
            }else{
                this.status.setText("still active");
                this.status.getBackground().setTint(context.getResources().getColor(R.color.colorAccent));
            }
            if(current.drugtype.equalsIgnoreCase("pills")){
                this.aimg.setImageDrawable(context.getDrawable(R.drawable.pill));
                this.dose.setText(current.dosage+ " pill(s) daily");
            }else{
                this.aimg.setImageDrawable(context.getDrawable(R.drawable.vac));
                this.dose.setText(current.dosage+ " vaccine(s) daily");
            }
            this.ail.setText("for " + current.ailment);
            this.dname.setText(current.drugname);


            this.position = position;
            this.currentObject=current;
        }
        public void changeStatus(final TextView t){
            final Dialog d = new Dialog(context);
            d.setContentView(R.layout.status_layout);
            TextView yes= d.findViewById(R.id.yes);
            TextView cancel = d.findViewById(R.id.cancel);
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Boolean b = new DatabaseClass(context).updateStatus(currentObject.id,1);
                    if(b){
                        t.setText("completed");
                        t.getBackground().setTint(context.getResources().getColor(R.color.aux8));
                        d.cancel();

                    }
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    d.cancel();
                }
            });
            d.show();
        }
//        public void setAlarm(med m){
//            Intent intent =new Intent(context, NotificationReceiver.class);
//            String[] med2 = {String.valueOf(m.id),m.ailment,m.drugtype,m.drugname,m.dosage,m.startd,m.nottime};
//            intent.putExtra("medinfo",med2);
//            PendingIntent pi = PendingIntent.getBroadcast(context,m.id,intent,PendingIntent.FLAG_UPDATE_CURRENT);
////            boolean alarmUp = (PendingIntent.getBroadcast(context,m.id,intent,PendingIntent.FLAG_UPDATE_CURRENT)!= null);
////            if(!alarmUp) {
//                Calendar calendar = Calendar.getInstance();
//                String[] dtime = m.nottime.split(":");
//                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(dtime[0]));
//                calendar.set(Calendar.MINUTE, Integer.parseInt(dtime[1]));
//                calendar.set(Calendar.SECOND, 0);
//
//                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
////                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);
//                alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pi);
////            }
//        }


    }
}
