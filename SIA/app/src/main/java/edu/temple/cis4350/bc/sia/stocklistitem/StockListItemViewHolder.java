/*
  Brett Crawford
  Stock Information App
  CIS 4350
  Spring 2015
 */

package edu.temple.cis4350.bc.sia.stocklistitem;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import edu.temple.cis4350.bc.sia.R;
import edu.temple.cis4350.bc.sia.stock.Stock;

/**
 * This class is a ViewHolder for populating the item within the
 * RecyclerView used in the navigation drawer of the main activity.
 */
public class StockListItemViewHolder extends RecyclerView.ViewHolder {

    private View stockListItemView;
    private FrameLayout stockColorBox;
    private TextView stockSymbol;
    private TextView stockPrice;
    private ImageView stockChangeImg;
    private TextView stockChange;

    /**
     * A constructor for the StockListItemViewHolder.
     *
     * @param v the View of the list item
     */
    public StockListItemViewHolder(View v) {
        super(v);

        stockListItemView = v;
        stockColorBox = (FrameLayout) stockListItemView.findViewById(R.id.sli_stock_color_box);
        stockSymbol = (TextView) stockListItemView.findViewById(R.id.sli_stock_symbol);
        stockPrice = (TextView) stockListItemView.findViewById(R.id.sli_stock_price);
        stockChangeImg = (ImageView) stockListItemView.findViewById(R.id.sli_stock_change_img);
        stockChange = (TextView) stockListItemView.findViewById(R.id.sli_stock_change);
    }

    /**
     * Binds a given Stock object to the view.
     *
     * @param sli the Stock object to bind
     */
    public void bindStockListItem(Stock sli) {

        stockColorBox.setBackgroundColor(sli.getStockColorCode());
        stockSymbol.setText(sli.getStockSymbol());
        stockPrice.setText("$" + sli.getStockPrice());
        int resID;
        if (sli.getStockChange().startsWith("+")) {
            stockChange.setTextColor(Color.parseColor("#99CC00"));
            resID = R.drawable.plus_change;
        }
        else if (sli.getStockChange().startsWith("-")) {
            stockChange.setTextColor(Color.parseColor("#FF4444"));
            resID = R.drawable.minus_change;
        }
        else {
            stockChange.setTextColor(Color.parseColor("#AAAAAA"));
            resID = R.drawable.no_change;
        }
        stockChange.setText(sli.getStockChange());
        stockChangeImg.setImageResource(resID);
    }

    /**
     * Returns the View associated with this list item.
     *
     * @return the list item's View
     */
    public View getView() {
        return stockListItemView;
    }
}
