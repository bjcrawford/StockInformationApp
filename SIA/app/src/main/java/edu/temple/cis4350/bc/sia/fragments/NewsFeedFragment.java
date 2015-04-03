/*
  Brett Crawford
  Stock Information App
  CIS 4350
  Spring 2015
 */

package edu.temple.cis4350.bc.sia.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.temple.cis4350.bc.sia.MainActivity;
import edu.temple.cis4350.bc.sia.R;
import edu.temple.cis4350.bc.sia.newsarticle.NewsArticles;
import edu.temple.cis4350.bc.sia.newsarticlelistitem.NewsArticleListItemAdapter;

/**
 * A fragment to hold the news feed page.
 */
public class NewsFeedFragment extends Fragment implements
        NewsArticleListItemAdapter.OnNewsItemClickListener {

    private static final String TAG = "NewsFeedFragment";

    private View view;
    private OnNewsFeedFragmentInteractionListener mListener;

    private RecyclerView newsList;
    private NewsArticles newsArticles;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment NewsFeedFragment.
     */
    public static NewsFeedFragment newInstance(NewsArticles newsArticles) {
        NewsFeedFragment nff = new NewsFeedFragment();
        nff.setNewsArticles(newsArticles);
        return nff;
    }

    /**
     * Required empty public constructor
     */
    public NewsFeedFragment() {
    }

    /**
     * Sets the list of news articles.
     * @param newsArticles A list of NewsArticle objects
     */
    public void setNewsArticles(NewsArticles newsArticles) {
        this.newsArticles = newsArticles;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news_feed, container, false);

        newsList = (RecyclerView) view.findViewById(R.id.news_feed_recyclerview);


        newsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        newsList.setAdapter(new NewsArticleListItemAdapter(newsArticles, this));

        return view;
    }

    /**
     * Handles calls to the parent activity.
     * @param uri
     */
    public void onSomeAction(Uri uri) {
        if (mListener != null) {
            mListener.onNewsFeedFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnNewsFeedFragmentInteractionListener) activity;
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
     * Handles the click events for news item within the news list
     * @param view The view associated with the item clicked
     * @param position The position of the item in the news list
     */
    @Override
    public void onNewsItemClick(View view, int position) {
        ((MainActivity) getActivity()).makeToast(newsArticles.get(position).getLink());
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnNewsFeedFragmentInteractionListener {
        public void onNewsFeedFragmentInteraction(Uri uri);
    }

    /**
     * Notifies the RecyclerView's adapter that the data set has been changed.
     */
    public void updateRecyclerView() {
        if (newsList != null) {
            newsList.getAdapter().notifyDataSetChanged();
        }
    }

}
