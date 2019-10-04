package com.dcinspirations.medassistant.introPlace;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.dcinspirations.medassistant.R;


public class sliderAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;

    public sliderAdapter(Context context) {
        this.context = context;
    }

    public int[] imageId = {
            R.drawable.c9,
            R.drawable.c5,
            R.drawable.c8
    };
    public String[] text = {
            "Add your medications and keep track of them",
            "Set reminders for your medications to be on the safe side",
            "Contact the doctor and other medical personnel if you need"
    };
    @Override
    public int getCount() {
        return imageId.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout,container,false);

        ImageView img = view.findViewById(R.id.img);
        img.setImageResource(imageId[position]);
        TextView des = view.findViewById(R.id.des);
        des.setText(text[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }


}
