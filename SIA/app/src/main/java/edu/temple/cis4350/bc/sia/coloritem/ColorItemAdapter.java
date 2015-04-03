package edu.temple.cis4350.bc.sia.coloritem;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

import edu.temple.cis4350.bc.sia.R;

public class ColorItemAdapter extends ArrayAdapter<ColorItem> {

    private static final String TAG = "ColorItemAdapter";

    public ColorItemAdapter(Context context, ArrayList<ColorItem> colorItems) {
        super(context, 0, colorItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(TAG, "getView() fired");

        ColorItem ci = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.color_item, parent, false);
        }

        CardView cv = (CardView) convertView.findViewById(R.id.itemColorBox);
        TextView tv = (TextView) convertView.findViewById(R.id.itemText);

        cv.setCardBackgroundColor(ci.getItemColorCode());
        tv.setText(ci.getItemText());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}

