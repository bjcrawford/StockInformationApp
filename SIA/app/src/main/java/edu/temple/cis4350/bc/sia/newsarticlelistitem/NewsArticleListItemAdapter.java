/*
  Brett Crawford
  Stock Information App
  CIS 4350
  Spring 2015
 */

package edu.temple.cis4350.bc.sia.newsarticlelistitem;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.temple.cis4350.bc.sia.R;
import edu.temple.cis4350.bc.sia.newsarticle.NewsArticles;

/**
 * This class is an Adapter for populating the RecyclerView used in
 * the news feed fragment.
 */
public class NewsArticleListItemAdapter extends RecyclerView.Adapter<NewsArticleListItemViewHolder> {

    /* A reference to the NewsArticles object */
    private NewsArticles newsArticles;

    /* A listener for click events */
    private OnNewsItemClickListener listener;

    /**
     * A constructor for the NewsListItemAdapter.
     *
     * @param newsArticles the NewsArticles object
     * @param listener the listener for click events
     */
    public NewsArticleListItemAdapter(NewsArticles newsArticles, OnNewsItemClickListener listener) {
        this.newsArticles = newsArticles;
        this.listener = listener;
    }

    @Override
    public NewsArticleListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_item, parent, false);
        return new NewsArticleListItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NewsArticleListItemViewHolder viewHolder, final int position) {
        viewHolder.bindStockListItem(newsArticles.get(position));
        viewHolder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onNewsItemClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsArticles.size();
    }

    /**
     * This interface must be implemented by activities using this adapter.
     * This allows for interaction between the adapter and the activity.
     */
    public interface OnNewsItemClickListener {
        public void onNewsItemClick(View view, int position);
    }
}
