/*
  Brett Crawford
  Stock Information App
  CIS 4350
  Spring 2015
 */

package edu.temple.cis4350.bc.sia.newslistitem;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import edu.temple.cis4350.bc.sia.R;
import edu.temple.cis4350.bc.sia.newsarticle.NewsArticle;
import edu.temple.cis4350.bc.sia.stock.Stock;

/**
 * This class is a ViewHolder for populating the item within the
 * RecyclerView used in the news feed fragment.
 */
public class NewsListItemViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "NewsListItemViewHolder";

    private View newsListItemView;
    private TextView newsTitle;
    private TextView newsDesc;

    /**
     * A constructor for the NewsListItemViewHolder.
     *
     * @param v the View of the list item
     */
    public NewsListItemViewHolder(View v) {
        super(v);

        newsListItemView = v;

        newsTitle = (TextView) newsListItemView.findViewById(R.id.news_title);
        newsDesc = (TextView) newsListItemView.findViewById(R.id.news_desc);
    }

    /**
     * Binds a given NewsArticle object to the view.
     *
     * @param na the NewsArticle object to bind
     */
    public void bindStockListItem(NewsArticle na) {

        newsTitle.setText(na.getTitle());
        newsDesc.setText(na.getDesc());
    }

    /**
     * Returns the View associated with this list item.
     *
     * @return the list item's View
     */
    public View getView() {
        return newsListItemView;
    }
}
