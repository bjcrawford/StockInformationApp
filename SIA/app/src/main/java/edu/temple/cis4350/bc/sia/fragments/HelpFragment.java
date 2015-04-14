package edu.temple.cis4350.bc.sia.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.temple.cis4350.bc.sia.R;

public class HelpFragment extends Fragment {

    private OnHelpFragmentInteractionListener listener;

    private View view;

    private TextView helpStockListDrawerOpenTextView;
    private TextView helpStockListDrawerAddTextView;
    private TextView helpStockListDrawerRemoveTextView;
    private TextView helpStockListDrawerCloseTextView;

    private TextView helpStockDetailPageOpenTextView;

    public HelpFragment() {

    }

    public static HelpFragment newInstance() {
        HelpFragment fragment = new HelpFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_help, container, false);

        helpStockListDrawerOpenTextView = (TextView) view.findViewById(R.id.help_stock_list_drawer_open);
        helpStockListDrawerAddTextView = (TextView) view.findViewById(R.id.help_stock_list_drawer_add);
        helpStockListDrawerRemoveTextView = (TextView) view.findViewById(R.id.help_stock_list_drawer_remove);
        helpStockListDrawerCloseTextView = (TextView) view.findViewById(R.id.help_stock_list_drawer_close);

        helpStockDetailPageOpenTextView = (TextView) view.findViewById(R.id.help_stock_detail_page_open);

        helpStockListDrawerOpenTextView.setText(Html.fromHtml(getString(R.string.help_stock_list_drawer_open)));
        helpStockListDrawerAddTextView.setText(Html.fromHtml(getString(R.string.help_stock_list_drawer_add)));
        helpStockListDrawerRemoveTextView.setText(Html.fromHtml(getString(R.string.help_stock_list_drawer_remove)));
        helpStockListDrawerCloseTextView.setText(Html.fromHtml(getString(R.string.help_stock_list_drawer_close)));

        helpStockDetailPageOpenTextView.setText(Html.fromHtml(getString(R.string.help_stock_detail_page_open)));

        return view;
    }

    public void onSomeAction(Uri uri) {
        if (listener != null) {
            listener.onHelpFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnHelpFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHelpFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnHelpFragmentInteractionListener {
        public void onHelpFragmentInteraction(Uri uri);
    }

}
