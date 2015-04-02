/*
  Brett Crawford
  Stock Information App
  CIS 4350
  Spring 2015
 */

package edu.temple.cis4350.bc.sia.stocklistitem;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.temple.cis4350.bc.sia.R;
import edu.temple.cis4350.bc.sia.stock.Stocks;

/**
 * This class is an Adapter for populating the RecyclerView used in
 * the navigation drawer of the main activity.
 */
public class StockListItemAdapter extends RecyclerView.Adapter<StockListItemViewHolder> {

    /* A reference to the Stocks object */
    private Stocks stocks;

    /* A listener for click events */
    private OnStockListItemClickListener listener;

    /**
     * A constructor for the StockListItemAdapter.
     *
     * @param stocks the Stocks object
     * @param listener the listener for click events
     */
    public StockListItemAdapter(Stocks stocks, OnStockListItemClickListener listener) {
        this.stocks = stocks;
        this.listener = listener;
    }

    @Override
    public StockListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_list_item, parent, false);
        return new StockListItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(StockListItemViewHolder viewHolder, final int position) {
        viewHolder.bindStockListItem(stocks.get(position));
        viewHolder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onStockListItemClick(view, position);
            }
        });
        viewHolder.getView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return listener.onStockListItemLongClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stocks.size();
    }

    /**
     * This interface must be implemented by activities using this adapter.
     * This allows for interaction between the adapter and the activity.
     */
    public interface OnStockListItemClickListener {
        public void onStockListItemClick(View view, int position);
        public boolean onStockListItemLongClick(View view, int position);
    }
}
