package edu.temple.cis4350.bc.sia;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import java.util.ArrayList;


public class MainActivity extends Activity implements
        StockListItemAdapter.OnItemClickListener,
        StockDetailsFragment.OnFragmentInteractionListener {

    private static final String TAG = "MainActivity";

    private DrawerLayout drawerLayout;
    private RecyclerView drawerStockList;
    private ActionBarDrawerToggle drawerToggle;

    private String[] stockSymbols;
    private String[] stockNames;
    private int[] stockColors;

    private ArrayList<Stock> stocks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerStockList = (RecyclerView) findViewById(R.id.drawer_list);

        stockSymbols = getResources().getStringArray(R.array.stock_symbols);
        stockNames = getResources().getStringArray(R.array.stock_names);
        stockColors = getResources().getIntArray(R.array.stock_colors);

        stocks = new ArrayList<Stock>();
        for (int i = 0; i < stockSymbols.length; i++) {
            stocks.add(i, new Stock(stockSymbols[i], stockNames[i], stockColors[i]));
        }

        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        drawerStockList.setLayoutManager(new LinearLayoutManager(this));
        drawerStockList.setAdapter(new StockListItemAdapter(stocks, this));

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                Log.d(TAG, "Stock list drawer closed");
                invalidateOptionsMenu();
                syncState();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerClosed(drawerView);
                Log.d(TAG, "Stock list drawer opened");
                invalidateOptionsMenu();
                syncState();
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);

        drawerToggle.syncState();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_news_feed:
                Log.d(TAG, "News Feed selected");
                return true;
            case R.id.action_settings:
                Log.d(TAG, "Settings selected");
                return true;
            case R.id.action_help:
                Log.d(TAG, "Help selected");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view, int position) {
        Log.d(TAG, "Item " + stocks.get(position).stockName + " selected");
        drawerLayout.closeDrawer(drawerStockList);

        StockDetailsFragment sdf = StockDetailsFragment.newInstance(
                stocks.get(position).stockName,
                stocks.get(position).stockSymbol,
                String.valueOf(stocks.get(position).stockPrice),
                String.valueOf(stocks.get(position).stockChange)

        );

        getFragmentManager().beginTransaction()
                .replace(R.id.main_content_fragment_container, sdf)
                .commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
