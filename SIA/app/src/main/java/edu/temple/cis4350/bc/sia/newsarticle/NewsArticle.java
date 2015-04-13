/*
  Brett Crawford
  Stock Information App
  CIS 4350
  Spring 2015
 */

package edu.temple.cis4350.bc.sia.newsarticle;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewsArticle {

    private String title;
    private String link;
    private String desc;
    private String pubDate;

    private int position;

    public NewsArticle(JSONObject newsArticleJSONObject) {
        try {
            title = newsArticleJSONObject.getString("title");
            link = newsArticleJSONObject.getString("link");
            link = link.substring(link.indexOf("*") + 1);
            desc = newsArticleJSONObject.getString("description");
            if (desc.equals("null")) {
                desc = "";
            }
            else if (desc.length() > 220) {
                desc = desc.substring(0, desc.substring(160, 220).indexOf(" ") + 160);
                desc = desc + "...";
            }
            pubDate = newsArticleJSONObject.getString("pubDate");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getLink() {
        return link;
    }

    public String getPubDate() {
        return pubDate;
    }
    public String getPubDateFormatted() {

        Date date = null;

        SimpleDateFormat format = new SimpleDateFormat("ccc, d MMM yyyy H:m:s zzz");
        try {
            date = format.parse(pubDate);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        return DateFormat.getDateTimeInstance().format(date);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}
