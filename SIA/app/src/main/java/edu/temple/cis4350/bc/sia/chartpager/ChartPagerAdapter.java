package edu.temple.cis4350.bc.sia.chartpager;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

public class ChartPagerAdapter extends PagerAdapter {

    private ArrayList<ImageView> imageViewList;

    public ChartPagerAdapter() {
        super();
        imageViewList = new ArrayList<ImageView>();
    }

    public void addChart(ImageView imageView) {
        imageViewList.add(imageView);
    }

    @Override
    public Object instantiateItem (ViewGroup container, int position) {
        ImageView iv = imageViewList.get(position);
        container.addView(iv);

        return iv;
    }

    @Override
    public void destroyItem (ViewGroup container, int position, Object object) {
        container.removeView(imageViewList.get(position));
    }

    @Override
    public int getCount() {
        return imageViewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = "1d";
                break;
            case 1:
                title = "5d";
                break;
            case 2:
                title = "1m";
                break;
            case 3:
                title = "6m";
                break;
            case 4:
                title = "1y";
                break;
            default:
                break;
        }

        return title;
    }
}
