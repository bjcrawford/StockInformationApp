package edu.temple.cis4350.bc.sia;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;


public class StockListItemViewHolder extends RecyclerView.ViewHolder {

    private View stockListItemView;
    private FrameLayout stockColorBox;
    private TextView stockSymbol;
    //private TextView stockName;
    //private TextView stockPrice;
    //private ImageView stockChangeImg;
    //private TextView stockChange;

    public StockListItemViewHolder(View v) {
        super(v);

        stockListItemView = v;
        stockColorBox = (FrameLayout) stockListItemView.findViewById(R.id.sli_stock_color_box);
        stockSymbol = (TextView) stockListItemView.findViewById(R.id.sli_stock_symbol);
        //stockName = (TextView) stockListItemView.findViewById(R.id.sli_stock_name);
        //stockPrice = (TextView) convertView.findViewById(R.id.sli_stock_price);
        //stockChangeImg = (ImageView) convertView.findViewById(R.id.sli_stock_change_img);
        //stockChange = (TextView) convertView.findViewById(R.id.sli_stock_change);
    }

    public void bindStockListItem(Stock sli) {

        stockColorBox.setBackgroundColor(sli.stockColorCode);
        stockSymbol.setText(sli.stockSymbol);
        //stockName.setText(sli.stockName);
        //stockPrice.setText(sli.stockPrice);
        //stockChangeImg.setImageDrawable(sli.stockChangeImg);
        //stockChange.setText(sli.stockChange);

    }
    
    public View getView() {
        return stockListItemView;
    }
}
