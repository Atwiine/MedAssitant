package com.dcinspirations.medassistant.introPlace;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.dcinspirations.medassistant.Dash;
import com.dcinspirations.medassistant.R;
import com.dcinspirations.medassistant.Sp;
import com.dcinspirations.medassistant.Validation;
import com.google.firebase.database.FirebaseDatabase;
import com.wajahatkarim3.easyflipviewpager.CardFlipPageTransformer;

import de.hdodenhof.circleimageview.CircleImageView;

public class intro extends AppCompatActivity {

    private ViewPager vp;
    private LinearLayout dl;
    private sliderAdapter sa;
//    private TextView[] dotArray;
    private CircleImageView[] dotArray;
    private TextView skip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

            if(new Sp().getLoggedIn()){
                startActivity(new Intent(this,Dash.class));
                finish();
            }


        skip = findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Validation.class));
                finish();

            }
        });
        vp = findViewById(R.id.vp);
        dl = findViewById(R.id.dotlayout);

        sa = new sliderAdapter(this);
        vp.setAdapter(sa);

        addDotIndicator(0);
        vp.addOnPageChangeListener(viewListener);
        // Create an object of page transformer
        CardFlipPageTransformer cardFlipPageTransformer = new CardFlipPageTransformer();

// Enable / Disable scaling while flipping. If false, then card will only flip as in Poker card example.
// Otherwise card will also scale like in Gallery demo. By default, its true.
        cardFlipPageTransformer.setScalable(true);

// Set orientation. Either horizontal or vertical. By default, its vertical.
        cardFlipPageTransformer.setFlipOrientation(CardFlipPageTransformer.VERTICAL);

// Assign the page transformer to the ViewPager.
        vp.setPageTransformer(true, cardFlipPageTransformer);

    }


    public void addDotIndicator(int position){
        dotArray = new CircleImageView[3];
        dl.removeAllViews();

        for(int i =0;i<dotArray.length;i++){
            LayoutInflater li = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
               View sample = li.inflate(R.layout.cimg,null);
               RelativeLayout cl = (RelativeLayout) sample;
               CircleImageView cimg = (CircleImageView) cl.getChildAt(0);
              dotArray[i] = cimg;
//            dotArray[i].setText(Html.fromHtml("&#8226;"));

            cl.removeAllViews();
            dl.addView(dotArray[i]);
        }

        dotArray[position].setImageResource(R.color.colorPrimary);
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotIndicator(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
