package com.example.ranggarizky.bukakayakgini.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.ranggarizky.bukakayakgini.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by RanggaRizky on 5/25/2017.
 */
public class CustomPagerAdapter extends PagerAdapter {

    Context mContext;
    ArrayList<String> images;
    LayoutInflater mLayoutInflater;

    public CustomPagerAdapter(Context context,ArrayList<String> images) {
        mContext = context;
        this.images = images;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.item_slider, container, false);

        ImageView img = (ImageView) itemView.findViewById(R.id.image);
        //txt.setText(dataHeader.get(position).getContent());
        Picasso.with(mContext)
                .load(images.get(position))
                .placeholder(R.drawable.dummy)
                .into(img);
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
