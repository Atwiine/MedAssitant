package com.dcinspirations.medassistant;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.MenuItem;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dcinspirations.medassistant.adapters.med_adapter;
import com.dcinspirations.medassistant.models.med;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.dcinspirations.medassistant.BaseApp.ctx;

public class Dash extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    ImageView ni;
    boolean medopen = false;
    TextView home,ud, med,smed,vmed,ec,lg,title,name,email;
    DrawerLayout drawer;
    private ViewPager vp;
    private LinearLayout dl;
    private HomeSliderAdapter sa;
    //    private TextView[] dotArray;
    private CircleImageView[] dotArray;
    LinearLayout empty;

    RecyclerView rv;
    ArrayList<med> datalist;
    med_adapter ma;
    DatabaseClass db;
    FloatingActionButton fab;
    ArrayList<String> filearray;
    AutoCompleteTextView act;
    Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String[] arr = new Sp().getBasics();

        db = new DatabaseClass(this);
        datalist = new ArrayList<>();
        filearray = new ArrayList<>();
        ma = new med_adapter(this,datalist);
        rv =  findViewById(R.id.rv);
        rv.requestFocus();
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(RecyclerView.VERTICAL);
        rv.setLayoutManager(llm);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(ma);

        empty = findViewById(R.id.empty);
        act = findViewById(R.id.search);



//        vp = findViewById(R.id.vp);
//        dl = findViewById(R.id.dotlayout);
        sa = new HomeSliderAdapter(this);
//        addDotIndicator(0);
//        vp.addOnPageChangeListener(viewListener);
//
//        vp.setAdapter(sa);
//        home = findViewById(R.id.home);
        ud = findViewById(R.id.ud);
        med = findViewById(R.id.med);

//        smed = findViewById(R.id.smed);
//        vmed = findViewById(R.id.vmed);
        ec = findViewById(R.id.ec);
        lg = findViewById(R.id.logout);
        title = findViewById(R.id.title);
        ni = findViewById(R.id.imageView);
        name = findViewById(R.id.ntext);
        email = findViewById(R.id.etext);
        fab = findViewById(R.id.fab);

        fab.setOnClickListener(this);
//        home.setOnClickListener(this);
        med.setOnClickListener(this);
        ud.setOnClickListener(this);
//        smed.setOnClickListener(this);
//        vmed.setOnClickListener(this);
        ec.setOnClickListener(this);
        lg.setOnClickListener(this);

//        name.setText(arr[0]);
//        email.setText(arr[1]);
        name.setText(arr[0]);
        email.setText(arr[1]);
        ni.setImageResource(Integer.parseInt(arr[3])==0?R.drawable.contact2:R.drawable.female);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        toggle.setDrawerIndicatorEnabled(false);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        toggle.setHomeAsUpIndicator(R.drawable.menu);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }
    private void populate(){
        filearray.clear();
        datalist.clear();
        Cursor cs = db.getMedData();
        while(cs.moveToNext()){
            med m = new med(cs.getInt(0),cs.getString(1),cs.getString(2),cs.getString(3),cs.getString(4),cs.getString(5),cs.getInt(6),cs.getString(7),cs.getInt(8));
            datalist.add(0,m);
            filearray.add(0,m.drugname);
            ma.notifyDataSetChanged();
        }
        empty.setVisibility(datalist.isEmpty()?View.VISIBLE:View.GONE);
        setAutoComplete();

    }

    public void setAutoComplete(){
//        Collections.sort(filearray);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx,android.R.layout.simple_list_item_1 ,filearray.toArray(new String[filearray.size()]));
        act.setAdapter(adapter);
        act.setDropDownBackgroundResource(R.color.aux1);
        act.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               rv.smoothScrollToPosition(filearray.size()-position);
               rv.requestFocus();
               timer.schedule(new MyTimerTask(rv,filearray.size()-position),3000);
            }
        });
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        populate();
        unselect();
        med.setBackground(getDrawable(R.drawable.bgshape2));
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.dash, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
//            case R.id.med:
//                if(medopen){
//                    smed.setVisibility(View.GONE);
//                    vmed.setVisibility(View.GONE);
//                    medopen = false;
//                    tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.med,0,R.drawable.down,0);
//                }else{
//                    smed.setVisibility(View.VISIBLE);
//                    vmed.setVisibility(View.VISIBLE);
//                    medopen = true;
//                    tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.med,0,R.drawable.up,0);
//                }
//                break;
                case R.id.fab:
                    startActivity(new Intent(this,SetMed.class));
                    break;
                default:
                    TextView tv = (TextView) v;
                    unselect();
                    tv.setBackground(getDrawable(R.drawable.bgshape2));
                    drawer.closeDrawer(GravityCompat.START);
                    if(tv.getText().toString().equals("User Details")){
                        startActivity(new Intent(this,UserDetails.class));
                    }else if(tv.getText().equals("Medications")){
//                        startActivity(new Intent(this,SetMed.class));

                    }else if(tv.getText().equals("Emergency Contact")){
                        startActivity(new Intent(this,EmergencyContacts.class));
                    }else if(tv.getText().equals("Logout")){
                        FirebaseAuth.getInstance().signOut();
                        new DatabaseClass(this).initDelete();
                        new Sp().setLoggedIn(false,"","","",0,"");
                        startActivity(new Intent(this, Validation.class));
                        finish();
                    }

                    break;

        }
    }

    private void unselect(){
        med.setBackgroundColor(Color.TRANSPARENT);
        ud.setBackgroundColor(Color.TRANSPARENT);
        ec.setBackgroundColor(Color.TRANSPARENT);
        lg.setBackgroundColor(Color.TRANSPARENT);
    }

    class MyTimerTask extends TimerTask {
        private Timer tim;
        private RecyclerView rv;
        private int position;

        MyTimerTask(RecyclerView rv, int position){
            this.rv = rv;
            this.position=position;
            rv.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.aux2));
        }

        @Override
        public void run() {

            runOnUiThread(new Runnable(){

                @Override
                public void run() {
                    rv.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.aux1));
                }});
        }

    }



}
