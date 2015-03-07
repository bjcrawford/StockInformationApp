package edu.temple.cis4350.bc.sia;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class StockListItemAdapter extends RecyclerView.Adapter<StockListItemViewHolder> {

    private ArrayList<StockListItem> stockListItems;
    private OnItemClickListener listener;

    public StockListItemAdapter(ArrayList<StockListItem> stockListItems, OnItemClickListener listener) {
        this.stockListItems = stockListItems;
        this.listener = listener;
    }

    @Override
    public StockListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_list_item, parent, false);
        return new StockListItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(StockListItemViewHolder viewHolder, final int position) {
        viewHolder.bindStockListItem(stockListItems.get(position));
        viewHolder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stockListItems.size();
    }

    public interface OnItemClickListener {
        public void onClick(View view, int position);
    }

}
