package edu.temple.cis4350.bc.sia;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

public class StockDetailsFragment extends Fragment {

    private static final String ARG_STOCK_SYMBOL = "stockSymbol";
    private static final String ARG_STOCK_NAME = "stockName";
    private static final String ARG_STOCK_PRICE = "stockPrice";
    private static final String ARG_STOCK_CHANGE = "stockChange";
    private static final String ARG_STOCK_PREV_CLOSE = "stockPrevClose";
    private static final String ARG_STOCK_OPEN = "stockOpen";
    private static final String ARG_STOCK_MARKET_CAP = "stockMarketCap";

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

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param stockSymbol The symbol of the stock
     * @param stockName The name of the stock
     * @param stockPrice The price of the stock
     * @param stockChange The change of the stock
     * @param stockPrevClose The previous close of the stock
     * @param stockOpen The open of the stock
     * @param stockMarketCap The market cap of the stock
     * @return A new instance of fragment StockDetailsFragment.
     */
    public static StockDetailsFragment newInstance(
            String stockSymbol,
            String stockName,
            String stockPrice,
            String stockChange,
            String stockPrevClose,
            String stockOpen,
            String stockMarketCap) {
        StockDetailsFragment fragment = new StockDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_STOCK_SYMBOL, stockSymbol);
        args.putString(ARG_STOCK_NAME, stockName);
        args.putString(ARG_STOCK_PRICE, stockPrice);
        args.putString(ARG_STOCK_CHANGE, stockChange);
        args.putString(ARG_STOCK_PREV_CLOSE, stockPrevClose);
        args.putString(ARG_STOCK_OPEN, stockOpen);
        args.putString(ARG_STOCK_MARKET_CAP, stockMarketCap);
        fragment.setArguments(args);
        return fragment;
    }

    public StockDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        view = inflater.inflate(R.layout.fragment_stock_details, container, false);

        stockSymbolTextView = (TextView) view.findViewById(R.id.stock_symbol);
        stockNameTextView = (TextView) view.findViewById(R.id.stock_name);
        stockPriceTextView = (TextView) view.findViewById(R.id.stock_price);
        stockChangeTextView = (TextView) view.findViewById(R.id.stock_change);
        stockPrevCloseTextView = (TextView) view.findViewById(R.id.stock_prev_close);
        stockOpenTextView = (TextView) view.findViewById(R.id.stock_open);
        stockMarketCapTextView = (TextView) view.findViewById(R.id.stock_market_cap);

        stockSymbolTextView.setText(stockSymbol);
        stockNameTextView.setText(stockName);
        stockPriceTextView.setText(stockPrice);
        stockChangeTextView.setText(stockChange);
        stockPrevCloseTextView.setText(stockPrevClose);
        stockOpenTextView.setText(stockOpen);
        stockMarketCapTextView.setText(stockMarketCap);

        return view;
    }

    public void updateView(Stock stock) {

        stockSymbolTextView.setText(stock.getStockSymbol());
        stockNameTextView.setText(stock.getStockName());
        stockPriceTextView.setText(stock.getStockPrice());
        stockChangeTextView.setText(stock.getStockChange());
        stockPrevCloseTextView.setText(stock.getStockPrevClosePrice());
        stockOpenTextView.setText(stock.getStockOpenPrice());
        stockMarketCapTextView.setText(stock.getStockMarketCap());
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
