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

    private static final String ARG_STOCK_NAME = "stockName";
    private static final String ARG_STOCK_SYMBOL = "stockSymbol";
    private static final String ARG_STOCK_PRICE = "stockPrice";
    private static final String ARG_STOCK_CHANGE = "stockChange";

    private String stockName;
    private String stockSymbol;
    private String stockPrice;
    private String stockChange;

    private TextView stockNameTextView;
    private TextView stockSymbolTextView;
    private TextView stockPriceTextView;
    private TextView stockChangeTextView;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param stockName The name of the stock
     * @param stockSymbol The symbol of the stock
     * @param stockPrice The price of the stock
     * @param stockChange The change of the stock
     * @return A new instance of fragment StockDetailsFragment.
     */
    public static StockDetailsFragment newInstance(
            String stockName,
            String stockSymbol,
            String stockPrice,
            String stockChange) {
        StockDetailsFragment fragment = new StockDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_STOCK_NAME, stockName);
        args.putString(ARG_STOCK_SYMBOL, stockSymbol);
        args.putString(ARG_STOCK_PRICE, stockPrice);
        args.putString(ARG_STOCK_CHANGE, stockChange);
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
            stockName = getArguments().getString(ARG_STOCK_NAME);
            stockSymbol = getArguments().getString(ARG_STOCK_SYMBOL);
            stockPrice = getArguments().getString(ARG_STOCK_PRICE);
            stockChange = getArguments().getString(ARG_STOCK_CHANGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stock_details, container, false);

        stockNameTextView = (TextView) v.findViewById(R.id.stock_name);
        stockSymbolTextView = (TextView) v.findViewById(R.id.stock_symbol);
        stockPriceTextView = (TextView) v.findViewById(R.id.stock_price);
        stockChangeTextView = (TextView) v.findViewById(R.id.stock_change);

        stockNameTextView.setText(stockName);
        stockSymbolTextView.setText(stockSymbol);
        stockPriceTextView.setText(stockPrice);
        stockChangeTextView.setText(stockChange);

        return v;
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
