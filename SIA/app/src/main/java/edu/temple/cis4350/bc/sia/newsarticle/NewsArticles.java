/*
  Brett Crawford
  Stock Information App
  CIS 4350
  Spring 2015
 */

package edu.temple.cis4350.bc.sia.newsarticle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class NewsArticles {

    private List<NewsArticle> newsArticles;

    public NewsArticles() {

        newsArticles = new ArrayList<NewsArticle>();
    }

    public boolean add(NewsArticle newsArticle) {

        newsArticle.setPosition(size());
        newsArticles.add(size(), newsArticle);

        return true;
    }

    /**
     * Returns the Stock with the given list position. If the stock is
     * not found, null is returned.
     *
     * @param listPosition the list position of the stock
     * @return the stock with the given list position or null if not found
     */
    public NewsArticle get(int listPosition) {

        return newsArticles.get(listPosition);
    }

    /**
     * Returns the List of NewsArticle objects.
     *
     * @return the List of NewArticle objects.
     */
    public List<NewsArticle> getArrayList() {

        return newsArticles;
    }

    public int size() {
        return newsArticles.size();
    }

    public void clear() {
        newsArticles.clear();
    }

    public void parseNewsQueryJSONObject(JSONObject newsQueryJSONObject) {
        try {
            JSONArray item = newsQueryJSONObject.getJSONObject("query")
                    .getJSONObject("results")
                    .getJSONObject("rss")
                    .getJSONObject("channel")
                    .getJSONArray("item");

            newsArticles.clear();
            for (int i = 0; i < item.length(); i++) {
                newsArticles.add(new NewsArticle(item.getJSONObject(i)));
            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
