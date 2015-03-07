package edu.temple.cis4350.bc.sia;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;


public class MainActivity extends Activity implements StockListItemAdapter.OnItemClickListener {

    private static final String TAG = "MainActivity";

    private DrawerLayout drawerLayout;
    private RecyclerView drawerStockList;

    private String[] stockSymbols;
    private String[] stockNames;
    private int[] stockColors;

    private ArrayList<StockListItem> stockListItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerStockList = (RecyclerView) findViewById(R.id.drawer_list);

        stockSymbols = getResources().getStringArray(R.array.stock_symbols);
        stockNames = getResources().getStringArray(R.array.stock_names);
        stockColors = getResources().getIntArray(R.array.stock_colors);

        stockListItems = new ArrayList<StockListItem>();
        for (int i = 0; i < stockSymbols.length; i++) {
            stockListItems.add(i, new StockListItem(stockSymbols[i], stockNames[i], stockColors[i]));
        }

        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        drawerStockList.setLayoutManager(new LinearLayoutManager(this));
        drawerStockList.setAdapter(new StockListItemAdapter(stockListItems, this));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view, int position) {
        Log.d(TAG, "Item " + stockListItems.get(position).stockName + " clicked");
    }

}
