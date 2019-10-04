package com.dcinspirations.medassistant;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dcinspirations.medassistant.adapters.contacts_adapter;
import com.dcinspirations.medassistant.adapters.med_adapter;
import com.dcinspirations.medassistant.models.contacts;
import com.dcinspirations.medassistant.models.med;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class EmergencyContacts extends AppCompatActivity {

    RecyclerView rv;
    ArrayList<contacts> datalist;
    contacts_adapter ma;
    DatabaseClass db;
    FloatingActionButton fab;
    Dialog dialog;
    Toolbar tb;
    LinearLayout empty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contacts);
        dialog = new Dialog(this);
        db = new DatabaseClass(this);
        tb = findViewById(R.id.toolbar);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        empty = findViewById(R.id.empty);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDialog();
            }
        });
        datalist = new ArrayList<>();
        ma = new contacts_adapter(this,datalist);
        rv =  findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(RecyclerView.VERTICAL);
        rv.setLayoutManager(llm);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(ma);
        populate();
    }

    private void setDialog() {
        dialog.setContentView(R.layout.addcontact);
        String[] Datalist = {"Medical Personnel","Family","Friend"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.spinner_layout,Datalist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner sp = dialog.findViewById(R.id.status);
        sp.setAdapter(adapter);
        final EditText name = dialog.findViewById(R.id.name);
        final EditText email = dialog.findViewById(R.id.email);
        final EditText number =dialog.findViewById(R.id.number);
        RelativeLayout rl = dialog.findViewById(R.id.action);
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n = name.getText().toString();
                String e = email.getText().toString();
                String nu = number.getText().toString();
                String s = sp.getSelectedItem().toString();
                if(!(n.isEmpty()||e.isEmpty()||nu.isEmpty()||nu.length()<11||nu.length()>11)){
                   Boolean b = db.insertIntoContacts(n,s,e,nu);
                   if(!b){
                       dialog.cancel();
                       populate();
                   }
                }else if(n.isEmpty()||e.isEmpty()||nu.isEmpty()){
                    Toast.makeText(EmergencyContacts.this, "Fill all fields", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(EmergencyContacts.this, "11 digits phone number", Toast.LENGTH_LONG).show();
                }
            }
        });
        dialog.show();
    }

    private void populate(){
        datalist.clear();
        Cursor cs = db.getContactData();
        while(cs.moveToNext()){
            contacts m = new contacts(cs.getInt(0),cs.getString(1),cs.getString(2),cs.getString(3),cs.getString(4));
            datalist.add(m);
            ma.notifyDataSetChanged();
        }
        empty.setVisibility(datalist.isEmpty()?View.VISIBLE:View.GONE);

    }
}
