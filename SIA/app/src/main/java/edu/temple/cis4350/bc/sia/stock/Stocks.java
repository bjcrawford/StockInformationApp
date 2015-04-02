/*
  Brett Crawford
  Stock Information App
  CIS 4350
  Spring 2015
 */

package edu.temple.cis4350.bc.sia.stock;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.temple.cis4350.bc.sia.stock.Stock;

/**
 * A class that holds an ArrayList of Stock objects.
 */
public class Stocks {

    private static final String TAG = "Stocks";

    /* The list of stocks */
    private List<Stock> stocks;

    /**
     * Creates a stocks object.
     */
    public Stocks() {

        stocks = new ArrayList<Stock>();
    }

    /**
     * Creates a stocks object from a JSON representation.
     * @param stockJSONArray An array of stock JSONObjects
     */
    public Stocks(JSONArray stockJSONArray) {

        this();
        try {
            for (int i = 0; i < stockJSONArray.length(); i++) {
                JSONObject stockJSONObject = stockJSONArray.getJSONObject(i);
                stocks.add(new Stock(
                        stockJSONObject.getString("symbol"),
                        stockJSONObject.getInt("colorCode"),
                        stockJSONObject.getInt("listPosition")
                ));
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a JSON representation of the list of stocks.
     * @return A JSONArray of stock JSONObjects
     */
    public JSONArray getStockJSONArray() {
        JSONArray stockJSONArray = new JSONArray();
        try {
            for (int i = 0; i < stocks.size(); i++) {
                stockJSONArray.put(i, stocks.get(i).getStockJSONObject());
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return stockJSONArray;
    }

    /**
     * Adds a Stock to the list of stocks. Checks the list of stocks
     * for the stock symbol being added. If the stock symbol already
     * exists the stock is not added and false is returned. Otherwise,
     * the stock is added to the list and true is returned.
     *
     * @param stock the Stock object  to add
     * @return true on success, otherwise false
     */
    public boolean add(Stock stock) {
        for (Stock s : stocks) {
            if (s.getStockSymbol().equals(stock.getStockSymbol())) {
                Log.d(TAG, "Stock " + stock.getStockSymbol() + " already exists in collection");

                return false;
            }
        }
        stocks.add(stock);
        Log.d(TAG, "Successfully added " + stock.getStockSymbol() + " to list");

        return true;
    }

    /**
     * Returns the Stock with the given list position. If the stock is
     * not found, null is returned.
     *
     * @param listPosition the list position of the stock
     * @return the stock with the given list position or null if not found
     */
    public Stock get(int listPosition) {
        for (Stock s : stocks) {
            if (s.getListPosition() == listPosition) {
                return s;
            }
        }
        Log.d(TAG, "Could not locate stock with list position " + listPosition + " in list");

        return null;
    }

    /**
     * Returns the stock with the given stock symbol. If the stock is not
     * found, null is returned.
     *
     * @param stockSymbol the stock symbol of the stock
     * @return the stock with the given symbol or null if not found
     */
    public Stock get(String stockSymbol) {
        for (Stock s : stocks) {
            if (s.getStockSymbol().equals(stockSymbol)) {
                return s;
            }
        }
        Log.d(TAG, "Could not locate stock with stock symbol " + stockSymbol + " in list");

        return null;
    }

    /**
     * Returns the List of stock objects.
     *
     * @return the List of stocks
     */
    public List<Stock> getList() {

        return stocks;
    }

    /**
     * Returns the size of the list of stocks.
     *
     * @return the size of the stock list
     */
    public int size() {

        return stocks.size();
    }

    /**
     * Returns an array of strings containing all of the stock symbols
     * in the stock list.
     *
     * @return an array of stock symbol strings
     */
    public String[] getStockSymbolStringArray() {
        String[] stockSymbols = new String[size()];
        for (int i = 0; i < size(); i++) {
            stockSymbols[i] = stocks.get(i).getStockSymbol();
        }

        return stockSymbols;
    }
}
