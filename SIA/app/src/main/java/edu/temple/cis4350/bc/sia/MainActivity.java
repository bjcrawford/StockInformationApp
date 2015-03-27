/*
  Brett Crawford
  Stock Information App
  CIS 4350
  Spring 2015
 */

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
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.temple.cis4350.bc.sia.apirequest.APIURLBuilder;
import edu.temple.cis4350.bc.sia.stock.Stock;
import edu.temple.cis4350.bc.sia.stock.Stocks;
import edu.temple.cis4350.bc.sia.stocklistitem.StockListItemAdapter;
import edu.temple.cis4350.bc.sia.apirequest.APIResponseHandler;
import edu.temple.cis4350.bc.sia.apirequest.APIRequestTask;


public class MainActivity extends Activity implements
        StockListItemAdapter.OnItemClickListener,
        StockDetailsFragment.OnFragmentInteractionListener,
        FloatingActionButton.OnClickListener,
        AddStockDialogFragment.OnAddStockListener {

    private static final String TAG = "MainActivity";

    private DrawerLayout drawerLayout;
    private RelativeLayout drawerView;
    private RecyclerView drawerStockList;
    private ActionBarDrawerToggle drawerToggle;
    private APIResponseHandler APIResponseHandler;
    private StockDetailsFragment currentStockDetailsFragment;

    private FloatingActionButton fab;

    private String[] stockSymbols;
    private int[] stockColors;

    private Stocks stocks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fab = (FloatingActionButton) findViewById(R.id.drawer_fab);
        fab.setOnClickListener(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (RelativeLayout) findViewById(R.id.drawer);
        drawerStockList = (RecyclerView) findViewById(R.id.drawer_recyclerview);

        APIResponseHandler = new APIResponseHandler(this);

        stockSymbols = getResources().getStringArray(R.array.stock_symbols);
        stockColors = getResources().getIntArray(R.array.stock_colors);

        stocks = new Stocks();
        for (int i = 0; i < stockSymbols.length; i++) {
            stocks.add(new Stock(stockSymbols[i], stockColors[i], i));
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

                // This is probably not the best place to make this call. Just testing for now
                for (int i = 0; i < stocks.size(); i++) {
                    drawerStockList.getAdapter().notifyItemChanged(i);
                }

                Log.d(TAG, "Stock list drawer opened");
                invalidateOptionsMenu();
                syncState();
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);

        drawerToggle.syncState();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateStocks(stocks);
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
                makeToast("News Feed selected");
                Log.d(TAG, "News Feed selected");
                return true;
            case R.id.action_settings:
                makeToast("Settings selected");
                Log.d(TAG, "Settings selected");
                return true;
            case R.id.action_help:
                makeToast("Help selected");
                Log.d(TAG, "Help selected");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view, int position) {
        Log.d(TAG, "Item " + stocks.get(position).getStockName() + " selected");
        drawerLayout.closeDrawer(drawerView);

        currentStockDetailsFragment = StockDetailsFragment.newInstance(stocks.get(position));

        getFragmentManager().beginTransaction()
                .replace(R.id.main_content_fragment_container, currentStockDetailsFragment)
                .commit();
    }

    private boolean hasConnection() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }

    protected void updateStocks(Stocks stocks) {

        if (hasConnection()) {
            try {
                String apiUrl = APIURLBuilder.getStockQueryURL(stocks.getStockSymbolStringArray());
                new APIRequestTask(APIResponseHandler, apiUrl).execute().get();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            // Notify user of lack of network connection and un-updated stock details
            makeToast("No network connection");
        }
    }

    public void parseStockQueryJSONObject(JSONObject stockQueryJSONObject) {
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
                    drawerStockList.getAdapter().notifyItemChanged(stock.getListPosition());
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void parseNewsQueryJSONObject(JSONObject newsQueryJSONObject) {
        // Stub
    }

    public void parseCompanySearchJSONObject(JSONObject companySearchJSONObject) {
        // Stub
        Log.d(TAG, companySearchJSONObject.toString());
    }

    public void makeToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFABClicked(FloatingActionButton fabView) {
        new AddStockDialogFragment().show(getFragmentManager(), null);
    }

    @Override
    public void onAddStock(String msg) {
        if (!msg.equals("")) {
            if (msg.startsWith("[")) { // Input from auto complete, parse out stock symbol
                msg = msg.replace("[", "");
                msg = msg.substring(0, msg.indexOf("]"));
                makeToast("Selected: " + msg);

            }
            else {
                makeToast("Please make selection from the auto complete suggestions.");
            }
        }
    }
}
