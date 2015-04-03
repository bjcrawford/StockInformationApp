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

import java.util.List;

import edu.temple.cis4350.bc.sia.apirequest.APIURLBuilder;
import edu.temple.cis4350.bc.sia.floatingactionbutton.FloatingActionButton;
import edu.temple.cis4350.bc.sia.fragments.AddStockDialogFragment;
import edu.temple.cis4350.bc.sia.fragments.NewsFeedFragment;
import edu.temple.cis4350.bc.sia.fragments.StockDetailsFragment;
import edu.temple.cis4350.bc.sia.newsarticle.NewsArticle;
import edu.temple.cis4350.bc.sia.newsarticle.NewsArticles;
import edu.temple.cis4350.bc.sia.stock.Stock;
import edu.temple.cis4350.bc.sia.stock.Stocks;
import edu.temple.cis4350.bc.sia.stocklistitem.StockListItemAdapter;
import edu.temple.cis4350.bc.sia.apirequest.APIResponseHandler;
import edu.temple.cis4350.bc.sia.apirequest.APIRequestTask;


public class MainActivity extends Activity implements
        StockListItemAdapter.OnStockListItemClickListener,
        FloatingActionButton.OnFABClickListener,
        AddStockDialogFragment.OnAddStockListener,
        StockDetailsFragment.OnStockDetailsFragmentInteractionListener,
        NewsFeedFragment.OnNewsFeedFragmentInteractionListener {

    private static final String TAG = "MainActivity";
    private static final String PREF_STOCKS_JSON = "PrefStocksJson";

    private static final int NEWS_FEED_FRAG = 1;
    private static final int STOCK_DETAILS_FRAG = 2;

    private DrawerLayout drawerLayout;
    private RelativeLayout drawerView;
    private ActionBarDrawerToggle drawerToggle;
    private FloatingActionButton drawerFab;


    private APIResponseHandler APIResponseHandler;

    private int currentFrag;
    private StockDetailsFragment currentStockDetailsFragment;
    private NewsFeedFragment newsFeedFragment;


    private RecyclerView drawerStockList;
    private Stocks stocks;


    private NewsArticles newsArticles;

/*====================================== Lifecycle Methods =======================================*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() fired");
        setContentView(R.layout.activity_main);

        // Set up floating action button in stock drawer
        drawerFab = (FloatingActionButton) findViewById(R.id.drawer_fab);
        drawerFab.setOnFABClickListener(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (RelativeLayout) findViewById(R.id.drawer);
        drawerStockList = (RecyclerView) findViewById(R.id.drawer_recyclerview);

        APIResponseHandler = new APIResponseHandler(this);

        String strStocksJSONArray = this.getPreferences(Context.MODE_PRIVATE).getString(PREF_STOCKS_JSON, "");
        if (strStocksJSONArray.length() == 0) {
            stocks = new Stocks();
        }
        else {
            try {
                stocks = new Stocks(new JSONArray(strStocksJSONArray));
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }

        newsArticles = new NewsArticles();
        newsFeedFragment = NewsFeedFragment.newInstance(newsArticles);

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

                switch (currentFrag) {
                    case NEWS_FEED_FRAG:
                        getActionBar().setTitle(R.string.news_feed_ab_title);
                        break;
                    case STOCK_DETAILS_FRAG:
                        getActionBar().setTitle(currentStockDetailsFragment.getStockSymbol());
                        break;
                }

                stocks.setSelectable(false);
                stocks.setAllChecked(false);
                // This is probably not the best place to make this call. Just testing for now
                for (int i = 0; i < stocks.size(); i++) {
                    drawerStockList.getAdapter().notifyItemChanged(i);
                }

                invalidateOptionsMenu();
                syncState();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Log.d(TAG, "Stock list drawer opened");

                getActionBar().setTitle(R.string.app_name);

                // This is probably not the best place to make this call. Just testing for now
                for (int i = 0; i < stocks.size(); i++) {
                    drawerStockList.getAdapter().notifyItemChanged(i);
                }

                //invalidateOptionsMenu();
                syncState();
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart() fired");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() fired");
        updateStocks();
        updateNews();
        getActionBar().setTitle(R.string.news_feed_ab_title);
        getFragmentManager().beginTransaction()
                .replace(R.id.main_content_fragment_container, newsFeedFragment)
                .commit();
        currentFrag = NEWS_FEED_FRAG;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() fired");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() fired");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() fired");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() fired");
        this.getPreferences(Context.MODE_PRIVATE).edit()
                .putString(PREF_STOCKS_JSON, stocks.getStockJSONArray().toString())
                .commit();
    }

/*==================================== Options Menu Methods ======================================*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        boolean anyChecked = stocks.areAnyChecked();
        menu.findItem(R.id.action_refresh).setVisible(!anyChecked);
        menu.findItem(R.id.action_discard).setVisible(anyChecked);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_refresh:
                Log.d(TAG, "Refresh selected");
                updateStocks();
                updateNews();
                return true;
            case R.id.action_discard:
                Log.d(TAG, "Discard selected");
                List<Stock> checked = stocks.getCheckedItems();
                for (Stock s : checked) {
                    int listPosition = s.getListPosition();
                    stocks.remove(s);
                    //drawerStockList.getAdapter().notifyItemChanged(listPosition);
                }
                updateNews();
                updateStocks();
                invalidateOptionsMenu();
                return true;
            case R.id.action_news_feed:
                makeToast("News Feed selected");
                Log.d(TAG, "News Feed selected");
                getFragmentManager().beginTransaction()
                        .replace(R.id.main_content_fragment_container, newsFeedFragment)
                        .commit();
                currentFrag = NEWS_FEED_FRAG;
                getActionBar().setTitle(R.string.news_feed_ab_title);
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

/*====================================== Listener Methods ========================================*/

    @Override
    public void onStockListItemClick(View view, int position) {

        if (stocks.isSelectable()) {
            onStockListItemLongClick(view, position);
        }
        else {
            Log.d(TAG, "Item " + stocks.get(position).getStockName() + " clicked");
            drawerLayout.closeDrawer(drawerView);

            currentStockDetailsFragment = StockDetailsFragment.newInstance(stocks.get(position));

            getFragmentManager().beginTransaction()
                    .replace(R.id.main_content_fragment_container, currentStockDetailsFragment)
                    .commit();
            currentFrag = STOCK_DETAILS_FRAG;
            getActionBar().setTitle(currentStockDetailsFragment.getStockSymbol());
        }
    }

    @Override
    public boolean onStockListItemLongClick(View view, int position) {
        Log.d(TAG, "Item " + stocks.get(position).getStockName() + " long clicked");

        boolean isChecked = stocks.get(position).isItemChecked();
        stocks.get(position).setItemChecked(!isChecked);
        drawerStockList.getAdapter().notifyItemChanged(position);
        stocks.setSelectable(stocks.areAnyChecked());

        invalidateOptionsMenu();
        return true;
    }

    @Override
    public void onFABClick(FloatingActionButton fabView) {
        new AddStockDialogFragment().show(getFragmentManager(), null);
    }

    @Override
    public void onAddStock(String stockName, int stockColor) {
        if (!stockName.equals("")) {
            if (stockName.endsWith("*")) { // Input from auto complete, parse out stock symbol
                String words[] = stockName.split(" ");
                //makeToast("Selected: " + words[0]);
                stocks.add(new Stock(words[0], stockColor, stocks.size()));
                updateStocks();
                updateNews();
            }
            else {
                makeToast("Please make selection from the auto complete suggestions.");
            }
        }
    }

    @Override
    public void onStockDetailsFragmentInteraction(Uri uri) {

    }

    @Override
    public void onNewsFeedFragmentInteraction(Uri uri) {

    }

/*====================================== General Methods =========================================*/

    /**
     * Checks for an internet connection.
     *
     * @return true if connection is found, otherwise false
     */
    private boolean hasConnection() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Updates the stock list information. Launches an AsyncTask to retrieve a JSON stock query.
     * The response is returned to the parseStockQueryJSONObject() method.
     */
    protected void updateStocks() {

        if (stocks.size() > 0) {
            if (hasConnection()) {
                try {
                    String apiUrl = APIURLBuilder.getStockQueryURL(stocks.getStockSymbolStringArray());
                    new APIRequestTask(APIResponseHandler, apiUrl).execute().get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                // Notify user of lack of network connection and un-updated stock details
                makeToast("No network connection");
            }
        }
        else {
            drawerStockList.getAdapter().notifyDataSetChanged();
        }
    }

    /**
     * Updates the news list information. Launches an AsyncTask to retrieve a JSON news query.
     * The response is returned to the parseNewsQueryJSONObject() method.
     */
    protected void updateNews() {

        if (stocks.size() > 0) {
            if (hasConnection()) {
                try {
                    String apiUrl = APIURLBuilder.getNewsQueryURL(stocks.getStockSymbolStringArray());
                    new APIRequestTask(APIResponseHandler, apiUrl).execute().get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                // Notify user of lack of network connection and un-updated stock details
                makeToast("No network connection");
            }
        }
        else {
            newsArticles.clear();
            newsFeedFragment.updateRecyclerView();
        }
    }

    /**
     * Parses a JSON stock query response. Individual JSON stock quotes are passed to the
     * parseStockQuoteJSONObject() method. On finish, the current stock details fragment is
     * updated.
     * @param stockQueryJSONObject
     */
    public void parseStockQueryJSONObject(JSONObject stockQueryJSONObject) {
        try {
            JSONObject query = stockQueryJSONObject.getJSONObject("query");
            int count = query.getInt("count");
            JSONObject results = query.getJSONObject("results");

            if (count > 1) {
                JSONArray quotes = results.getJSONArray("quote");
                for (int i = 0; i < count; i++) {
                    parseStockQuoteJSONObject(quotes.getJSONObject(i));
                }
            }
            else {
                parseStockQuoteJSONObject(results.getJSONObject("quote"));
            }

            if (currentStockDetailsFragment != null) {
                currentStockDetailsFragment.update();
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Parses a JSON stock quote response.
     * @param stockQuoteJSONObject
     */
    protected void parseStockQuoteJSONObject(JSONObject stockQuoteJSONObject) {
        try {
            String symbol = stockQuoteJSONObject.getString("symbol");
            String name = stockQuoteJSONObject.getString("Name");
            String price = stockQuoteJSONObject.getString("LastTradePriceOnly");
            String change = stockQuoteJSONObject.getString("PercentChange");
            String prevClosePrice = stockQuoteJSONObject.getString("PreviousClose");
            String openPrice = stockQuoteJSONObject.getString("Open");
            String marketCap = stockQuoteJSONObject.getString("MarketCapitalization");
            String volume = stockQuoteJSONObject.getString("Volume");

            Stock stock = stocks.get(symbol);
            if (stock != null) {
                stock.setStockSymbol(symbol);
                stock.setStockName(name);
                stock.setStockPrice(price);
                stock.setStockChange(change);
                stock.setStockPrevClosePrice(prevClosePrice);
                stock.setStockOpenPrice(openPrice);
                stock.setStockMarketCap(marketCap);
                stock.setStockVolume(volume);
                drawerStockList.getAdapter().notifyItemChanged(stock.getListPosition());
            }
            else {
                Log.d(TAG, "Stock " + symbol + " was not found in the stock list");
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void parseNewsQueryJSONObject(JSONObject newsQueryJSONObject) {
        try {
            JSONArray item = newsQueryJSONObject.getJSONObject("query")
                    .getJSONObject("results")
                    .getJSONObject("rss")
                    .getJSONObject("channel")
                    .getJSONArray("item");

            newsArticles.clear();
            for (int i = 0; i < item.length(); i++) {
                newsArticles.add(new NewsArticle(item.getJSONObject(i)));
            }
            newsFeedFragment.updateRecyclerView();

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void parseCompanySearchJSONObject(JSONObject companySearchJSONObject) {
        // Stub
        Log.d(TAG, companySearchJSONObject.toString());
    }

    public void makeToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

}
