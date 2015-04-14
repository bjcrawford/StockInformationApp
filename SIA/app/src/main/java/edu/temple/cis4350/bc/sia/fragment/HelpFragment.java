package edu.temple.cis4350.bc.sia.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.temple.cis4350.bc.sia.R;

public class HelpFragment extends Fragment {

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

}
