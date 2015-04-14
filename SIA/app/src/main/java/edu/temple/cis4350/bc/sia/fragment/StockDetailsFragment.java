/*
  Brett Crawford
  Stock Information App
  CIS 4350
  Spring 2015
 */

package edu.temple.cis4350.bc.sia.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import edu.temple.cis4350.bc.sia.MainActivity;
import edu.temple.cis4350.bc.sia.R;
import edu.temple.cis4350.bc.sia.api.APIRequestTask;
import edu.temple.cis4350.bc.sia.api.APIResponseHandler;
import edu.temple.cis4350.bc.sia.api.APIURLBuilder;
import edu.temple.cis4350.bc.sia.chartpager.ChartPagerAdapter;
import edu.temple.cis4350.bc.sia.chartpager.ChartViewPager;
import edu.temple.cis4350.bc.sia.chartpager.DownloadImageTask;
import edu.temple.cis4350.bc.sia.newsarticle.NewsArticles;
import edu.temple.cis4350.bc.sia.stock.Stock;
import edu.temple.cis4350.bc.sia.util.Utils;

public class StockDetailsFragment extends Fragment implements
        APIResponseHandler.OnAPIResponseHandlerInteractionListener {

    private static final String TAG = "StockDetailsFragment";

    private static final String ARG_STOCK_SYMBOL = "stockSymbol";
    private static final String ARG_STOCK_COLOR = "stockColor";
    private static final String ARG_STOCK_NAME = "stockName";
    private static final String ARG_STOCK_PRICE = "stockPrice";
    private static final String ARG_STOCK_CHANGE = "stockChange";
    private static final String ARG_STOCK_PREV_CLOSE = "stockPrevClose";
    private static final String ARG_STOCK_OPEN = "stockOpen";
    private static final String ARG_STOCK_MARKET_CAP = "stockMarketCap";
    private static final String ARG_STOCK_VOLUME = "stockVolume";

    private Stock stock;

    private ChartViewPager chartViewPager;
    private ChartPagerAdapter chartPagerAdapter;

    private ImageView chart1d;
    private ImageView chart5d;
    private ImageView chart1m;
    private ImageView chart6m;
    private ImageView chart1y;

    private ArrayList<TextView> chartControls;

    private String stockSymbol;
    private int stockColor;
    private String stockName;
    private String stockPrice;
    private String stockChange;
    private String stockPrevClose;
    private String stockOpen;
    private String stockMarketCap;
    private String stockVolume;

    private View view;

    private CardView stockNameCardView;
    private TextView stockSymbolTextView;
    private TextView stockNameTextView;
    private TextView stockPriceTextView;
    private TextView stockChangeTextView;
    private TextView stockPrevCloseTextView;
    private TextView stockOpenTextView;
    private TextView stockMarketCapTextView;
    private TextView stockVolumeTextView;
    private View stockDivider1;
    private View stockDivider2;
    private ScrollView stockScrollView;
    private RelativeLayout stockNewsFeed;
    private ImageButton newsFeedExpandButton;

    private boolean expanded;

    private NewsArticles newsArticles;
    private NewsFeedFragment newsFeedFragment;

    private APIResponseHandler APIResponseHandler;

    public StockDetailsFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param stock the stock object
     * @return A new instance of fragment StockDetailsFragment.
     */
    public static StockDetailsFragment newInstance(Stock stock) {
        StockDetailsFragment fragment = new StockDetailsFragment();
        fragment.stock = stock;
        Bundle args = new Bundle();
        args.putString(ARG_STOCK_SYMBOL, stock.getStockSymbol());
        args.putInt(ARG_STOCK_COLOR, stock.getStockColorCode());
        args.putString(ARG_STOCK_NAME, stock.getStockName());
        args.putString(ARG_STOCK_PRICE, stock.getStockPrice());
        args.putString(ARG_STOCK_CHANGE, stock.getStockChange());
        args.putString(ARG_STOCK_PREV_CLOSE, stock.getStockPrevClosePrice());
        args.putString(ARG_STOCK_OPEN, stock.getStockOpenPrice());
        args.putString(ARG_STOCK_MARKET_CAP, stock.getStockMarketCap());
        args.putString(ARG_STOCK_VOLUME, stock.getStockVolume());
        fragment.setArguments(args);
        return fragment;
    }


/*====================================== Lifecycle Methods =======================================*/


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG, "OnAttach() fired");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() fired");
        if (getArguments() != null) {
            stockSymbol = getArguments().getString(ARG_STOCK_SYMBOL);
            stockColor = getArguments().getInt(ARG_STOCK_COLOR);
            stockName = getArguments().getString(ARG_STOCK_NAME);
            stockPrice = getArguments().getString(ARG_STOCK_PRICE);
            stockChange = getArguments().getString(ARG_STOCK_CHANGE);
            stockPrevClose = getArguments().getString(ARG_STOCK_PREV_CLOSE);
            stockOpen = getArguments().getString(ARG_STOCK_OPEN);
            stockMarketCap = getArguments().getString(ARG_STOCK_MARKET_CAP);
            stockVolume = getArguments().getString(ARG_STOCK_VOLUME);
        }

        APIResponseHandler = new APIResponseHandler(this);
        newsArticles = new NewsArticles();
        newsFeedFragment = NewsFeedFragment.newInstance(newsArticles);

        expanded = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView() fired");
        view = inflater.inflate(R.layout.fragment_stock_details, container, false);



        chartViewPager = (ChartViewPager) view.findViewById(R.id.chart_viewpager);

        chartPagerAdapter = new ChartPagerAdapter();

        chart1d = new ImageView(getActivity());
        chart5d = new ImageView(getActivity());
        chart1m = new ImageView(getActivity());
        chart6m = new ImageView(getActivity());
        chart1y = new ImageView(getActivity());

        chartControls = new ArrayList<TextView>();

        chartControls.add(0, (TextView) view.findViewById(R.id.chart_viewpager_controls_1d));
        chartControls.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chartViewPager.setCurrentItem(0);
            }
        });
        chartControls.add(1, (TextView) view.findViewById(R.id.chart_viewpager_controls_5d));
        chartControls.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chartViewPager.setCurrentItem(1);
            }
        });
        chartControls.add(2, (TextView) view.findViewById(R.id.chart_viewpager_controls_1m));
        chartControls.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chartViewPager.setCurrentItem(2);
            }
        });
        chartControls.add(3, (TextView) view.findViewById(R.id.chart_viewpager_controls_6m));
        chartControls.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chartViewPager.setCurrentItem(3);
            }
        });
        chartControls.add(4, (TextView) view.findViewById(R.id.chart_viewpager_controls_1y));
        chartControls.get(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chartViewPager.setCurrentItem(4);
            }
        });

        chartControls.get(0).setTextColor(stockColor);

        new DownloadImageTask(chart1d).execute("https://chart.yahoo.com/z?t=1d&s=" + stockSymbol);
        new DownloadImageTask(chart5d).execute("https://chart.yahoo.com/z?t=5d&s=" + stockSymbol);
        new DownloadImageTask(chart1m).execute("https://chart.yahoo.com/z?t=1m&s=" + stockSymbol);
        new DownloadImageTask(chart6m).execute("https://chart.yahoo.com/z?t=6m&s=" + stockSymbol);
        new DownloadImageTask(chart1y).execute("https://chart.yahoo.com/z?t=1y&s=" + stockSymbol);

        chartPagerAdapter.addChart(chart1d);
        chartPagerAdapter.addChart(chart5d);
        chartPagerAdapter.addChart(chart1m);
        chartPagerAdapter.addChart(chart6m);
        chartPagerAdapter.addChart(chart1y);

        chartViewPager.setAdapter(chartPagerAdapter);

        chartViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "Page " + position + " selected");
                for (int i = 0; i < chartControls.size(); i++) {
                    chartControls.get(i).setTextColor(i == position ? stockColor : 0xFF999999);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        stockNameCardView = (CardView) view.findViewById(R.id.stock_name_color_box);
        stockSymbolTextView = (TextView) view.findViewById(R.id.stock_symbol);
        stockNameTextView = (TextView) view.findViewById(R.id.stock_name);
        stockPriceTextView = (TextView) view.findViewById(R.id.stock_price);
        stockChangeTextView = (TextView) view.findViewById(R.id.stock_change);
        stockPrevCloseTextView = (TextView) view.findViewById(R.id.stock_prev_close);
        stockOpenTextView = (TextView) view.findViewById(R.id.stock_open);
        stockMarketCapTextView = (TextView) view.findViewById(R.id.stock_market_cap);
        stockVolumeTextView = (TextView) view.findViewById(R.id.stock_volume);
        stockDivider1 = view.findViewById(R.id.stock_detail_divider1);
        stockDivider2 = view.findViewById(R.id.stock_detail_divider2);

        if (view.findViewById(R.id.stock_scrollview) != null) {
            stockScrollView = (ScrollView) view.findViewById(R.id.stock_scrollview);
            stockNewsFeed = (RelativeLayout) view.findViewById(R.id.stock_news_feed);
            newsFeedExpandButton = (ImageButton) view.findViewById(R.id.stock_news_feed_expand_button);

            newsFeedExpandButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    LinearLayout.LayoutParams svParams = (LinearLayout.LayoutParams) stockScrollView.getLayoutParams();
                    LinearLayout.LayoutParams nfParams = (LinearLayout.LayoutParams) stockNewsFeed.getLayoutParams();
                    if (!expanded) {
                        svParams.weight = 0.0f;
                        nfParams.weight = 1.0f;
                        newsFeedExpandButton.setImageResource(R.drawable.ic_action_collapse);
                    } else {
                        svParams.weight = 0.6f;
                        nfParams.weight = 0.4f;
                        newsFeedExpandButton.setImageResource(R.drawable.ic_action_expand);
                    }
                    expanded = !expanded;
                    stockScrollView.setLayoutParams(svParams);
                    stockNewsFeed.setLayoutParams(nfParams);
                }
            });
        }


        getChildFragmentManager().beginTransaction()
                .add(R.id.news_feed_frag_container, newsFeedFragment)
                .commit();

        updateView();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated() fired");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() fired");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() fired");
        updateNews();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() fired");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() fired");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView() fired");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() fired");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "OnDetach() fired");
    }


/*====================================== Listener Methods ========================================*/


    @Override
    public void onAPIResponseHandlerInteraction(JSONObject jsonObject, int taskId) {

        switch (taskId) {
            case MainActivity.NEWS_QUERY_ID:
                newsArticles.parseNewsQueryJSONObject(jsonObject);
                newsFeedFragment.updateRecyclerView();
                break;
            default:
                break;
        }
    }

    @Override
    public void onAPIResponseHandlerError(String errorMsg) {
        makeToast(errorMsg);
    }


/*====================================== General Methods =========================================*/


    public String getStockSymbol() {
        return stockSymbol;
    }

    public void update() {

        stockSymbol = stock.getStockSymbol();
        stockColor = stock.getStockColorCode();
        stockName = stock.getStockName();
        stockPrice = stock.getStockPrice();
        stockChange = stock.getStockChange();
        stockPrevClose = stock.getStockPrevClosePrice();
        stockOpen = stock.getStockOpenPrice();
        stockMarketCap = stock.getStockMarketCap();
        stockVolume = stock.getStockVolume();

        updateNews();
        updateView();
    }

    /**
     * Updates the news list. Launches an AsyncTask to retrieve a JSON news query.
     */
    protected void updateNews() {

        if (Utils.hasConnection(getActivity())) {
            try {
                String apiUrl = APIURLBuilder.getNewsQueryURL(getStockSymbol());
                new APIRequestTask(APIResponseHandler, apiUrl, MainActivity.NEWS_QUERY_ID).execute().get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Notify user of lack of network connection
            makeToast("No network connection");
        }
    }

    /**
     * Updates the view.
     */
    public void updateView() {

        stockNameCardView.setCardBackgroundColor(stockColor);
        stockNameCardView.setCardBackgroundColor(stockColor);
        stockSymbolTextView.setText(stockSymbol);
        stockNameTextView.setText(stockName);
        stockNameTextView.setTextColor(0xFFFFFFFF);
        stockPriceTextView.setText(stockPrice);
        stockChangeTextView.setText(stockChange);
        if (stockChange.startsWith("+")) {
            stockChangeTextView.setTextColor(0xFF99CC00);
        }
        else if (stockChange.startsWith("-")) {
            stockChangeTextView.setTextColor(0xFFFF4444);
        }
        stockPrevCloseTextView.setText(stockPrevClose);
        stockOpenTextView.setText(stockOpen);
        stockMarketCapTextView.setText(stockMarketCap);
        stockVolumeTextView.setText(stockVolume);
        if (stockDivider1 != null) {
            stockDivider1.setBackgroundColor(stockColor);
        }
        stockDivider2.setBackgroundColor(stockColor);
    }

    public void makeToast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
    }
}
