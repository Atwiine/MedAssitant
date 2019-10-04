package com.dcinspirations.medassistant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;


public class HomeSliderAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;

    public HomeSliderAdapter(Context context) {
        this.context = context;
    }

//    public int[][]  imageId= {
//            {R.drawable.c3,R.drawable.c9},
//            {R.drawable.c5,R.drawable.c4},
//            {R.drawable.c7,R.drawable.c8}
//    };
    public int[] imageId= {
            R.drawable.c9,
            R.drawable.c4,
            R.drawable.c8
    };
    public String[] text = {
            "Save Medication ailments and the doctor's prescription for further use, you never can tell!",
            "Guess what? You also get to be notified on when to take any medication of your choice",
            "A doctor is always one call or one message away. Save contacts you can contact in times of emergency"
    };
    public String[] text2 = {
            "View Medications","Set Medication Reminder","Emergency Contacts"
    };
    @Override
    public int getCount() {
        return imageId.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.homepage,container,false);

        ImageView img1 = view.findViewById(R.id.img1);
        img1.setImageResource(imageId[position]);
//        ImageView img2 = view.findViewById(R.id.img2);
//        img2.setImageResource(imageId[position][1]);
        TextView des = view.findViewById(R.id.description);
        des.setText(text[position]);
        TextView title = view.findViewById(R.id.title);
        title.setText(text2[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }


}
