/*
  Brett Crawford
  Stock Information App
  CIS 4350
  Spring 2015
 */

package edu.temple.cis4350.bc.sia.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.temple.cis4350.bc.sia.R;
import edu.temple.cis4350.bc.sia.chartpageradapter.ChartPagerAdapter;
import edu.temple.cis4350.bc.sia.chartpageradapter.ChartViewPager;
import edu.temple.cis4350.bc.sia.chartpageradapter.DownloadImageTask;
import edu.temple.cis4350.bc.sia.stock.Stock;

public class StockDetailsFragment extends Fragment {

    private static final String TAG = "StockDetailsFragment";

    private static final String ARG_STOCK_SYMBOL = "stockSymbol";
    private static final String ARG_STOCK_NAME = "stockName";
    private static final String ARG_STOCK_PRICE = "stockPrice";
    private static final String ARG_STOCK_CHANGE = "stockChange";
    private static final String ARG_STOCK_PREV_CLOSE = "stockPrevClose";
    private static final String ARG_STOCK_OPEN = "stockOpen";
    private static final String ARG_STOCK_MARKET_CAP = "stockMarketCap";

    private Stock stock;

    private ChartViewPager chartViewPager;
    private ChartPagerAdapter chartPagerAdapter;

    private ImageView chart1d;
    private ImageView chart5d;
    private ImageView chart1m;
    private ImageView chart6m;
    private ImageView chart1y;

    private ArrayList<TextView> controls;

    private TextView control1d;
    private TextView control5d;
    private TextView control1m;
    private TextView control6m;
    private TextView control1y;

    private String stockSymbol;
    private String stockName;
    private String stockPrice;
    private String stockChange;
    private String stockPrevClose;
    private String stockOpen;
    private String stockMarketCap;

    private View view;

    private TextView stockSymbolTextView;
    private TextView stockNameTextView;
    private TextView stockPriceTextView;
    private TextView stockChangeTextView;
    private TextView stockPrevCloseTextView;
    private TextView stockOpenTextView;
    private TextView stockMarketCapTextView;

    private OnStockDetailsFragmentInteractionListener listener;

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
        args.putString(ARG_STOCK_NAME, stock.getStockName());
        args.putString(ARG_STOCK_PRICE, stock.getStockPrice());
        args.putString(ARG_STOCK_CHANGE, stock.getStockChange());
        args.putString(ARG_STOCK_PREV_CLOSE, stock.getStockPrevClosePrice());
        args.putString(ARG_STOCK_OPEN, stock.getStockOpenPrice());
        args.putString(ARG_STOCK_MARKET_CAP, stock.getStockMarketCap());
        fragment.setArguments(args);
        return fragment;
    }

    public StockDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() fired");
        if (getArguments() != null) {
            stockSymbol = getArguments().getString(ARG_STOCK_SYMBOL);
            stockName = getArguments().getString(ARG_STOCK_NAME);
            stockPrice = getArguments().getString(ARG_STOCK_PRICE);
            stockChange = getArguments().getString(ARG_STOCK_CHANGE);
            stockPrevClose = getArguments().getString(ARG_STOCK_PREV_CLOSE);
            stockOpen = getArguments().getString(ARG_STOCK_OPEN);
            stockMarketCap = getArguments().getString(ARG_STOCK_MARKET_CAP);
        }
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

        controls = new ArrayList<TextView>();

        controls.add(0, (TextView) view.findViewById(R.id.chart_viewpager_controls_1d));
        controls.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chartViewPager.setCurrentItem(0);
            }
        });
        controls.add(1, (TextView) view.findViewById(R.id.chart_viewpager_controls_5d));
        controls.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chartViewPager.setCurrentItem(1);
            }
        });
        controls.add(2, (TextView) view.findViewById(R.id.chart_viewpager_controls_1m));
        controls.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chartViewPager.setCurrentItem(2);
            }
        });
        controls.add(3, (TextView) view.findViewById(R.id.chart_viewpager_controls_6m));
        controls.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chartViewPager.setCurrentItem(3);
            }
        });
        controls.add(4, (TextView) view.findViewById(R.id.chart_viewpager_controls_1y));
        controls.get(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chartViewPager.setCurrentItem(4);
            }
        });

        controls.get(0).setTextColor(0xFF000000);

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
                for (int i = 0; i < controls.size(); i++) {
                    controls.get(i).setTextColor(i == position ? 0xFF000000 : 0xFF999999);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        stockSymbolTextView = (TextView) view.findViewById(R.id.stock_symbol);
        stockNameTextView = (TextView) view.findViewById(R.id.stock_name);
        stockPriceTextView = (TextView) view.findViewById(R.id.stock_price);
        stockChangeTextView = (TextView) view.findViewById(R.id.stock_change);
        stockPrevCloseTextView = (TextView) view.findViewById(R.id.stock_prev_close);
        stockOpenTextView = (TextView) view.findViewById(R.id.stock_open);
        stockMarketCapTextView = (TextView) view.findViewById(R.id.stock_market_cap);

        updateView();

        return view;
    }

    public void update() {

        Log.d(TAG, "update() fired");
        stockSymbol = stock.getStockSymbol();
        stockName = stock.getStockName();
        stockPrice = stock.getStockPrice();
        stockChange = stock.getStockChange();
        stockPrevClose = stock.getStockPrevClosePrice();
        stockOpen = stock.getStockOpenPrice();
        stockMarketCap = stock.getStockMarketCap();

        updateView();
    }

    public void updateView() {

        Log.d(TAG, "updateView() fired");
        stockSymbolTextView.setText(stockSymbol);
        stockNameTextView.setText(stockName);
        stockPriceTextView.setText(stockPrice);
        stockChangeTextView.setText(stockChange);
        stockPrevCloseTextView.setText(stockPrevClose);
        stockOpenTextView.setText(stockOpen);
        stockMarketCapTextView.setText(stockMarketCap);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (listener != null) {
            listener.onStockDetailsFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG, "OnAttach() fired");
        try {
            listener = (OnStockDetailsFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnStockDetailsFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "OnDetach() fired");
        listener = null;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnStockDetailsFragmentInteractionListener {
        public void onStockDetailsFragmentInteraction(Uri uri);
    }

}
