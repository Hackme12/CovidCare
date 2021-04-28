package com.example.covidcare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class slide_page_adaptar extends PagerAdapter {
    Context context;
    LayoutInflater inflater;

    public slide_page_adaptar(Context context) {
        this.context = context;

    }

    public int[] slide_images = {R.drawable.coronavirussafety, R.drawable.covidsymptoms};


    @Override
    public int getCount() {
        return slide_images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.slide_layout, container, false);
        ImageView slideImage = (ImageView) view.findViewById(R.id.imageView);

        slideImage.setImageResource(slide_images[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }


}
