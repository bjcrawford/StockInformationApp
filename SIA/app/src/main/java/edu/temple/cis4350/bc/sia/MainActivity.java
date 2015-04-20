/*
  Brett Crawford
  Stock Information App
  CIS 4350
  Spring 2015
 */

package edu.temple.cis4350.bc.sia;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
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

import java.io.File;
import java.io.IOException;

import edu.temple.cis4350.bc.sia.api.APIURLBuilder;
import edu.temple.cis4350.bc.sia.api.DataRefreshService;
import edu.temple.cis4350.bc.sia.floatingactionbutton.FloatingActionButton;
import edu.temple.cis4350.bc.sia.fragment.AddStockDialogFragment;
import edu.temple.cis4350.bc.sia.fragment.HelpFragment;
import edu.temple.cis4350.bc.sia.fragment.NewsFeedFragment;
import edu.temple.cis4350.bc.sia.fragment.SettingsDialogFragment;
import edu.temple.cis4350.bc.sia.fragment.StockDetailsFragment;
import edu.temple.cis4350.bc.sia.newsarticle.NewsArticles;
import edu.temple.cis4350.bc.sia.stock.Stock;
import edu.temple.cis4350.bc.sia.stock.Stocks;
import edu.temple.cis4350.bc.sia.stock.StockListItemAdapter;
import edu.temple.cis4350.bc.sia.api.APIResponseHandler;
import edu.temple.cis4350.bc.sia.util.Utils;

public class MainActivity extends Activity implements
        StockListItemAdapter.OnStockListItemAdapterInteractionListener,
        FloatingActionButton.OnFloatingActionButtonInteractionListener,
        AddStockDialogFragment.OnAddStockDialogFragmentInteractionListener,
        NewsFeedFragment.OnNewsFeedFragmentInteractionListener,
        APIResponseHandler.OnAPIResponseHandlerInteractionListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "MainActivity";
    private static final String STORAGE_FILE = "jsonFileArray.json";

    public static final int STOCK_QUERY_ID = 1;
    public static final int NEWS_QUERY_ID = 2;
    public static final int COMPANY_QUERY_ID = 3;

    private static final String FRAG_BUNDLE_KEY = "FragBundleKey";
    private static final String STOCK_BUNDLE_KEY = "StockBundleKey";

    private static final int NEWS_FEED_FRAG = 1;
    private static final int STOCK_DETAILS_FRAG = 2;
    private static final int SETTINGS_FRAG = 3;
    private static final int HELP_FRAG = 4;

    public static final String NOTIFICATION_CHECKS_STOCKS = "edu.temple.cis4350.bc.sia.NOTIFICATION_CHECKS_STOCKS";

    public static final String KEY_PREF_REFRESH_RATE = "pref_refresh_rate";
    public static final String VALUE_PREF_REFRESH_5 = "5";
    public static final String VALUE_PREF_REFRESH_15 = "15";
    public static final String VALUE_PREF_REFRESH_30 = "30";
    public static final String VALUE_PREF_REFRESH_45 = "45";
    public static final String VALUE_PREF_REFRESH_60 = "60";
    public static final String VALUE_PREF_REFRESH_0 = "0";

    private DrawerLayout drawerLayout;
    private RelativeLayout drawer;
    private RecyclerView drawerStockList;
    private FloatingActionButton drawerFab;
    private StockListDrawerToggle drawerToggle;

    private APIResponseHandler apiResponseHandler;

    private int currentFrag;
    private StockDetailsFragment currentStockDetailsFragment;
    private NewsFeedFragment newsFeedFragment;
    private HelpFragment helpFragment;

    private Stocks stocks;
    private NewsArticles newsArticles;

    private boolean isBound;
    private DataRefreshService boundDataRefreshService;

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            boundDataRefreshService = ((DataRefreshService.DataRefreshBinder)service).getService();
            Log.d(TAG, "Service is bound");
            isBound = true;
            refresh(true);
            if (currentFrag == STOCK_DETAILS_FRAG) {
                currentStockDetailsFragment.setDataRefreshService(boundDataRefreshService);
                currentStockDetailsFragment.updateNews();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            boundDataRefreshService = null;
            isBound = false;
        }
    };

/*====================================== Lifecycle Methods =======================================*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() fired");
        setContentView(R.layout.activity_main);

        isBound = false;

        // Set up the stock drawer and toggle
        drawer = (RelativeLayout) findViewById(R.id.drawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        drawerToggle = new StockListDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        restoreStocks();
        newsArticles = new NewsArticles();

        // Set up the recycler view in the stock drawer
        drawerStockList = (RecyclerView) findViewById(R.id.drawer_recyclerview);
        drawerStockList.setLayoutManager(new LinearLayoutManager(this));
        drawerStockList.setAdapter(new StockListItemAdapter(stocks, this));

        // Set up floating action button in stock drawer
        drawerFab = (FloatingActionButton) findViewById(R.id.drawer_fab);
        drawerFab.setListener(this);

        apiResponseHandler = new APIResponseHandler(this);

        newsFeedFragment = NewsFeedFragment.newInstance(newsArticles);
        helpFragment = HelpFragment.newInstance();

        doBindService();

        // Take appropriate action on launch
        if (stocks.size() == 0) { // No stocks? Show help page
            launchFragment(HELP_FRAG, null);
        }
        else if (savedInstanceState != null) { // Saved instance state? Check it

            // Check for the fragment to launch
            int fragId = savedInstanceState.getInt(FRAG_BUNDLE_KEY);
            String stockSymbol = "";
            if (fragId == STOCK_DETAILS_FRAG) {
                stockSymbol = savedInstanceState.getString(STOCK_BUNDLE_KEY);
            }
            launchFragment(fragId, stockSymbol);

        }
        else { // Otherwise, just load the news feed
            launchFragment(NEWS_FEED_FRAG, null);
        }

        // If the user tapped the notification, open the stocks drawer
        if (getIntent().getAction().equals(NOTIFICATION_CHECKS_STOCKS)) {
            drawerLayout.openDrawer(drawer);
        }
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() fired");

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.d(TAG, "onSaveInstanceState() fired");

        // Save current frag and possibly stock detail symbol
        savedInstanceState.putInt(FRAG_BUNDLE_KEY, currentFrag);
        if (currentStockDetailsFragment != null) {
            savedInstanceState.putString(STOCK_BUNDLE_KEY, currentStockDetailsFragment.getStockSymbol());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() fired");

        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
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
        doUnbindService();
    }

/*==================================== Options Menu Methods ======================================*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        // Toggle the refresh and discard buttons on and off
        // based on the state of the stock list items
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
                refresh(false);
                return true;
            case R.id.action_discard:
                if (currentFrag == STOCK_DETAILS_FRAG) {
                    launchFragment(NEWS_FEED_FRAG, null);
                }
                for (Stock s : stocks.getCheckedItems()) {
                    stocks.remove(s);
                }
                saveStocks();
                refresh(true);
                drawerStockList.getAdapter().notifyDataSetChanged();
                invalidateOptionsMenu();
                return true;
            case R.id.action_news_feed:
                launchFragment(NEWS_FEED_FRAG, null);
                return true;
            case R.id.action_settings:
                launchFragment(SETTINGS_FRAG, null);
                return true;
            case R.id.action_help:
                launchFragment(HELP_FRAG, null);
                return true;
            case R.id.action_exit:
                finish();
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
            drawerLayout.closeDrawer(drawer);
            launchFragment(STOCK_DETAILS_FRAG, stocks.get(position).getStockSymbol());
        }
    }

    @Override
    public boolean onStockListItemLongClick(View view, int position) {

        boolean isChecked = stocks.get(position).isItemChecked();
        stocks.get(position).setItemChecked(!isChecked);
        drawerStockList.getAdapter().notifyItemChanged(position);
        stocks.setSelectable(stocks.areAnyChecked());
        invalidateOptionsMenu();

        return true;
    }

    @Override
    public void onFloatingActionButtonClick(FloatingActionButton fabView) {
        AddStockDialogFragment.newInstance(this).show(getFragmentManager(), null);
    }

    @Override
    public void onStockAdded(String stockName, int stockColor) {
        if (!stockName.equals("")) {
            if (stockName.endsWith("*")) { // Input from auto complete, parse out stock symbol
                String words[] = stockName.split(" ");
                stocks.add(new Stock(words[0], stockColor, stocks.size()));
                saveStocks();
                refresh(true);
                drawerStockList.getAdapter().notifyItemInserted(stocks.size() - 1);
            }
            else {
                makeToast("Please make selection from the auto complete suggestions.");
            }
        }
    }

    @Override
    public void onNewsFeedFragmentNewsItemClick(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    @Override
    public void onAPIResponseHandlerInteraction(JSONObject jsonObject, int taskId) {

        switch (taskId) {
            case STOCK_QUERY_ID:
                stocks.parseStockQueryJSONObject(jsonObject);
                drawerStockList.getAdapter().notifyDataSetChanged();
                if (currentStockDetailsFragment != null) {
                    currentStockDetailsFragment.update();
                }
                break;
            case NEWS_QUERY_ID:
                newsArticles.parseNewsQueryJSONObject(jsonObject);
                newsFeedFragment.updateRecyclerView();
                break;
            case COMPANY_QUERY_ID:
                //parseCompanySearchJSONObject(jsonObject);
                break;
        }
    }

    @Override
    public void onAPIResponseHandlerError(String errorMsg) {
        makeToast(errorMsg);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case KEY_PREF_REFRESH_RATE:
                refresh(true);
                break;
            default:
                break;
        }
    }

/*====================================== General Methods =========================================*/


    /**
     * Restores the stock list. Pulls and uses a json representation from an internal
     * file if found.
     */
    private void restoreStocks() {

        String strStocksJSONArray = Utils.fileToString(openStorageFile());

        // Construct the Stocks object
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
    }

    /**
     * Saves the stock list. The stock list is persisted into an internal file.
     */
    private void saveStocks() {

        Utils.stringToFile(stocks.getStockJSONArray().toString(), openStorageFile());
    }

    /**
     * Open or creates and returns the storage file.
     *
     * @return The internal storage file
     */
    private File openStorageFile() {

        File file = new File(getFilesDir(), STORAGE_FILE);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }

    /**
     * Updates the stock list and the news list using the bound data refresh service.
     */
    protected void refresh(boolean auto) {

        if (stocks.size() > 0 && isBound) {

            String[] apiUrls = new String[]{
                    APIURLBuilder.getStockQueryURL(stocks.getStockSymbolStringArray()),
                    APIURLBuilder.getNewsQueryURL(stocks.getStockSymbolStringArray())
            };

            if (auto) {
                String strRate = PreferenceManager.getDefaultSharedPreferences(this).getString(KEY_PREF_REFRESH_RATE, "15");
                int refreshRate = Integer.decode(strRate);

                boundDataRefreshService.startAutoRefresh(apiResponseHandler, refreshRate, apiUrls);
            }
            else {
                boundDataRefreshService.manualRefresh(apiResponseHandler, apiUrls);
            }
        }
        else {
            newsArticles.clear();
        }
        drawerStockList.getAdapter().notifyDataSetChanged();
        newsFeedFragment.updateRecyclerView();
    }

    /**
     * Launches a specified fragment. If the stock details fragment is launched, a string
     * specifying the stock symbol of the stock to load must be given. For other fragments,
     * the string can be passed in as null.
     *
     * @param fragId The id of the fragment to launch
     * @param stockSymbol The stock symbol if launching a stock detail fragment, otherwise null
     */
    private void launchFragment(int fragId, String stockSymbol) {

        currentStockDetailsFragment = null;
        switch (fragId) {
            case STOCK_DETAILS_FRAG:
                Stock s = stocks.get(stockSymbol);
                currentStockDetailsFragment = StockDetailsFragment.newInstance(s, boundDataRefreshService);
                getFragmentManager().beginTransaction()
                        .replace(R.id.main_content_fragment_container, currentStockDetailsFragment)
                        .commit();
                currentFrag = STOCK_DETAILS_FRAG;
                getActionBar().setTitle(currentStockDetailsFragment.getStockSymbol());
                break;
            case NEWS_FEED_FRAG:
                getFragmentManager().beginTransaction()
                        .replace(R.id.main_content_fragment_container, newsFeedFragment)
                        .commit();
                currentFrag = NEWS_FEED_FRAG;
                getActionBar().setTitle(R.string.news_feed_ab_title);
                break;
            case SETTINGS_FRAG:
                new SettingsDialogFragment().show(getFragmentManager(), null);
                break;
            case HELP_FRAG:
                getFragmentManager().beginTransaction()
                        .replace(R.id.main_content_fragment_container, helpFragment)
                        .commit();
                currentFrag = HELP_FRAG;
                getActionBar().setTitle(R.string.help_ab_title);
                break;
        }
    }

    /**
     * Binds the refresh data service to this activity
     */
    void doBindService() {
        Log.d(TAG, "Binding to service");
        bindService(new Intent(MainActivity.this, DataRefreshService.class), serviceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * Unbinds the data refresh service from this activity
     */
    void doUnbindService() {
        if (isBound) {
            unbindService(serviceConnection);
        }
    }

    /**
     * A helper method for easily creating a toast
     *
     * @param text The text to display
     */
    public void makeToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }


/*====================================== Inner Classes =========================================*/


    private class StockListDrawerToggle extends ActionBarDrawerToggle {

        public StockListDrawerToggle(Activity activity, DrawerLayout drawerLayout,
                int openDrawerContentDescRes, int closeDrawerContentDescRes) {
            super(activity, drawerLayout, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        @Override
        public void onDrawerClosed(View view) {
            super.onDrawerClosed(view);

            // Restore the correct action bar title
            switch (currentFrag) {
                case NEWS_FEED_FRAG:
                    getActionBar().setTitle(R.string.news_feed_ab_title);
                    break;
                case STOCK_DETAILS_FRAG:
                    getActionBar().setTitle(currentStockDetailsFragment.getStockSymbol());
                    break;
                case HELP_FRAG:
                    getActionBar().setTitle(R.string.help_ab_title);
                    break;
            }

            // Clear any checked states on the stock list
            stocks.setSelectable(false);
            stocks.setAllChecked(false);

            invalidateOptionsMenu();
            syncState();
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);

            getActionBar().setTitle(R.string.app_name);
            syncState();
        }
    }
}
