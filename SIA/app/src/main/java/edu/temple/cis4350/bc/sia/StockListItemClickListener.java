package edu.temple.cis4350.bc.sia;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class StockListItemClickListener implements ListView.OnItemClickListener {

    private static final String TAG = "DrawerItemClickListener";

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        
        Log.d(TAG, "Item " + position + " clicked");
    }
}
