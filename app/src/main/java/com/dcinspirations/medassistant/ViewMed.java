package com.dcinspirations.medassistant;

import android.database.Cursor;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dcinspirations.medassistant.adapters.med_adapter;
import com.dcinspirations.medassistant.models.med;

import java.util.ArrayList;

public class ViewMed extends AppCompatActivity {

    RecyclerView rv;
    ArrayList<med> datalist;
    med_adapter ma;
    DatabaseClass db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_med);
        db = new DatabaseClass(this);

        datalist = new ArrayList<>();
        ma = new med_adapter(this,datalist);
        rv =  findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(RecyclerView.VERTICAL);
        rv.setLayoutManager(llm);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(ma);
        populate();
    }

    private void populate(){
        Cursor cs = db.getMedData();
        while(cs.moveToNext()){
            med m = new med(cs.getInt(0),cs.getString(1),cs.getString(2),cs.getString(3),cs.getString(4),cs.getString(5),cs.getInt(6),cs.getString(7),cs.getInt(8));
            datalist.add(m);
            ma.notifyDataSetChanged();
        }

    }
}
