package com.dcinspirations.medassistant.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.dcinspirations.medassistant.DatabaseClass;
import com.dcinspirations.medassistant.R;
import com.dcinspirations.medassistant.models.contacts;
import com.dcinspirations.medassistant.models.med;

import java.util.List;

/**
 * Created by pc on 2/18/2018.
 */

public class contacts_adapter extends RecyclerView.Adapter<contacts_adapter.viewHolder>{

    private List<contacts> objectlist;
    private LayoutInflater inflater;
    private Context context;

    public contacts_adapter(Context context, List<contacts> objectlist) {
        inflater = LayoutInflater.from(context);
        this.objectlist = objectlist;
        this.context=context;
    }


    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.contactlayout,parent,false);
        viewHolder vholder = new viewHolder(view);
        return vholder;
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        contacts current = objectlist.get(position);
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
        private TextView name,status,sms,mail,call;
        private ImageView cimg;
        private int position;
        private contacts currentObject;

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
                                    new DatabaseClass(context).deleteContactData(objectlist.get(position).id);
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
            name = itemView.findViewById(R.id.name);
            status = itemView.findViewById(R.id.status);
            cimg = itemView.findViewById(R.id.cimg);
            call = itemView.findViewById(R.id.call);
            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendCall(currentObject.number);
                }
            });
            sms = itemView.findViewById(R.id.sms);
            sms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendSms(currentObject.number);
                }
            });
            mail = itemView.findViewById(R.id.mail);
            mail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendMail(currentObject.email);
                }
            });
        }



        public void setData(contacts current, int position) {

            this.name.setText(current.name);
            this.status.setText(current.status);
            this.cimg.setImageResource(current.status.equalsIgnoreCase("friend")?R.drawable.friend:
                    current.status.equalsIgnoreCase("family")?R.drawable.fam:R.drawable.doc2 );

            this.position = position;
            this.currentObject=current;
        }
        public void sendCall(String num){
            Intent dialIntent = new Intent(Intent.ACTION_DIAL);
            dialIntent.setData(Uri.parse("tel:" +num));
            context.startActivity(dialIntent);
        }
        public void sendSms(String num){
            Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.putExtra("address",num);
            smsIntent.putExtra("sms_body","");
            context.startActivity(smsIntent);
        }
        public void sendMail(String email){
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:"+email));
            context.startActivity(Intent.createChooser(emailIntent, "Send using"));
        }



    }
}
