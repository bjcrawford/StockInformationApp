/*
  Brett Crawford
  Stock Information App
  CIS 4350
  Spring 2015
 */

package edu.temple.cis4350.bc.sia.newsarticlelistitem;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import edu.temple.cis4350.bc.sia.R;
import edu.temple.cis4350.bc.sia.newsarticle.NewsArticle;

/**
 * This class is a ViewHolder for populating the item within the
 * RecyclerView used in the news feed fragment.
 */
public class NewsArticleListItemViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "NewsListItemViewHolder";

    private View newsListItemView;
    private TextView newsTitle;
    private TextView newsPubDate;

    /**
     * A constructor for the NewsListItemViewHolder.
     *
     * @param v the View of the list item
     */
    public NewsArticleListItemViewHolder(View v) {
        super(v);

        newsListItemView = v;

        newsTitle = (TextView) newsListItemView.findViewById(R.id.news_title);
        newsPubDate = (TextView) newsListItemView.findViewById(R.id.news_pub_date);
    }

    /**
     * Binds a given NewsArticle object to the view.
     *
     * @param na the NewsArticle object to bind
     */
    public void bindStockListItem(NewsArticle na) {

        newsTitle.setText(na.getTitle());
        newsPubDate.setText(na.getPubDateFormatted());
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
