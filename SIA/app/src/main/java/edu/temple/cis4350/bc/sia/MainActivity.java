package edu.temple.cis4350.bc.sia;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends Activity implements
        StockListItemAdapter.OnItemClickListener,
        StockDetailsFragment.OnFragmentInteractionListener {

    private static final String TAG = "MainActivity";

    private DrawerLayout drawerLayout;
    private RecyclerView drawerStockList;
    private ActionBarDrawerToggle drawerToggle;
    private StockQueryJSONHandler stockQueryJSONHandler;
    private StockDetailsFragment currentStockDetailsFragment;

    private String[] stockSymbols;
    private String[] stockNames;
    private int[] stockColors;

    private Stocks stocks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerStockList = (RecyclerView) findViewById(R.id.drawer_list);

        stockQueryJSONHandler = new StockQueryJSONHandler(this);

        stockSymbols = getResources().getStringArray(R.array.stock_symbols);
        stockNames = getResources().getStringArray(R.array.stock_names);
        stockColors = getResources().getIntArray(R.array.stock_colors);

        stocks = new Stocks();
        for (int i = 0; i < stockSymbols.length; i++) {
            stocks.add(new Stock(stockSymbols[i], stockNames[i], stockColors[i]));
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
        Log.d(TAG, "Item " + stocks.get(position).getStockName() + " selected");
        drawerLayout.closeDrawer(drawerStockList);

        if (hasConnection()) {
            new StockQueryJSONTask(stockQueryJSONHandler).execute(stocks.get(position).getStockSymbol());
        }
        else {
            // Notify user of lack of network connection and un-updated stock details
            makeToast("No network connection");
        }

        currentStockDetailsFragment = StockDetailsFragment.newInstance(
                stocks.get(position).getStockSymbol(),
                stocks.get(position).getStockName(),
                stocks.get(position).getStockPrice(),
                stocks.get(position).getStockChange(),
                stocks.get(position).getStockPrevClosePrice(),
                stocks.get(position).getStockOpenPrice(),
                stocks.get(position).getStockMarketCap()

        );

        getFragmentManager().beginTransaction()
                .replace(R.id.main_content_fragment_container, currentStockDetailsFragment)
                .commit();
    }

    private boolean hasConnection() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }

    protected void parseStockQueryJSONObject(JSONObject stockQueryJSONObject) {
        try {
            JSONObject query = stockQueryJSONObject.getJSONObject("query");
            int count = query.getInt("count");
            JSONObject results = query.getJSONObject("results");

            if (count > 1) {
                JSONArray quotes = results.getJSONArray("quote");
                for (int i = 0; i < count; i++) {
                    parseStockQuoteJSONObject(quotes.getJSONObject(i));
                    JSONObject quote = quotes.getJSONObject(i);
                }
            }
            else {
                parseStockQuoteJSONObject(results.getJSONObject("quote"));
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }

    protected void parseStockQuoteJSONObject(JSONObject stockQuoteJSONObject) {
        try {
            String symbol = stockQuoteJSONObject.getString("symbol");
            String name = stockQuoteJSONObject.getString("Name");
            String price = stockQuoteJSONObject.getString("LastTradePriceOnly");
            String change = stockQuoteJSONObject.getString("PercentChange");
            String prevClosePrice = stockQuoteJSONObject.getString("PreviousClose");
            String openPrice = stockQuoteJSONObject.getString("Open");
            String marketCap = stockQuoteJSONObject.getString("MarketCapitalization");

            for (Stock stock : stocks.getArrayList()) {
                if (stock.getStockSymbol().equals(symbol)) {
                    stock.setStockSymbol(symbol);
                    stock.setStockName(name);
                    stock.setStockPrice(price);
                    stock.setStockChange(change);
                    stock.setStockPrevClosePrice(prevClosePrice);
                    stock.setStockOpenPrice(openPrice);
                    stock.setStockMarketCap(marketCap);

                    currentStockDetailsFragment.updateView(stock);
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void makeToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
