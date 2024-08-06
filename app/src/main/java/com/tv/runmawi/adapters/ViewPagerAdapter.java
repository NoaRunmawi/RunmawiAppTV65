package com.tv.runmawi.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.tv.runmawi.BannerDetailActivity;
import com.tv.runmawi.R;
import com.tv.runmawi.model.Banneritem;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<Banneritem> sliderList;
    private LayoutInflater layoutInflater;
 //   private Integer [] images = {R.drawable.logo,R.drawable.logo,R.drawable.logo};

    public ViewPagerAdapter(Context context) {
        this.context = context;
    }

    public ViewPagerAdapter(Context context, ArrayList<Banneritem> sliderList) {
        this.context = context;
        this.sliderList = sliderList;
    }

    @Override
    public int getCount() {
        return sliderList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_item, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        //    imageView.setImageResource(images[position]);
        Glide.with(context).load(sliderList.get(position).getImg()).error(context.getResources().getDrawable(R.drawable.splash)).placeholder(context.getResources().getDrawable(R.drawable.splash)).into(imageView);
        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                imageView.setBackground(context.getResources().getDrawable(R.drawable.recycle_focus));

            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BannerDetailActivity.class);
              //  intent.putExtra(DetailActivity.MOVIE, item);
                intent.putExtra("banner_item",sliderList.get(position));


                context.startActivity(intent);

            }
        });
        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}