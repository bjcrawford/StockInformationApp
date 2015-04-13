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

    /* Designates the selectable state of the stocks list */
    private boolean isSelectable;

    /**
     * Creates a stocks object.
     */
    public Stocks() {

        this.stocks = new ArrayList<Stock>();
        this.isSelectable = false;
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
                Log.d(TAG, "Stock " + stock.getStockSymbol() + " already exists in list");

                return false;
            }
        }
        stocks.add(stock);
        Log.d(TAG, "Successfully added " + stock.getStockSymbol() + " to list");

        return true;
    }

    /**
     * Removes a stock from the list
     *
     * @param stock The stock to remove
     * @return True if removed, otherwise false
     */
    public boolean remove(Stock stock) {
        for (Stock s : stocks) {
            if (stock.getStockSymbol().equals(s.getStockSymbol())) {
                stocks.remove(stock.getListPosition());
                updateStockListPositions();

                return true;
            }
        }
        Log.d(TAG, "Stock " + stock.getStockSymbol() + " was not found in list");

        return false;
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
     * Updates the list position associated with each stock object to match
     * its position in the stock list
     */
    public void updateStockListPositions() {
        for (int i = 0; i < stocks.size(); i++) {
            stocks.get(i).setListPosition(i);
        }
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

    /**
     * Returns the selectable state of the stock list
     *
     * @return True is selectable, otherwise false
     */
    public boolean isSelectable() {
        return isSelectable;
    }

    /**
     * Sets the selectable state of the stock list
     *
     * @param selectable True if selectable, false otherwise
     */
    public void setSelectable(boolean selectable) {
        isSelectable = selectable;
    }

    /**
     * Returns true if any stocks are checked
     *
     * @return True if any checked, otherwise false
     */
    public boolean areAnyChecked() {
        for (int i = 0; i < stocks.size(); i++) {
            if (stocks.get(i).isItemChecked()) {

                return true;
            }
        }

        return false;
    }

    /**
     * Set the checked state of all stocks
     *
     * @param isChecked The checked state
     */
    public void setAllChecked(boolean isChecked) {
        for (int i = 0; i < stocks.size(); i++) {
            stocks.get(i).setItemChecked(isChecked);
        }
    }

    /**
     * Returns a list of all the checked stocks
     *
     * @return A list of checked stocks
     */
    public List<Stock> getCheckedItems() {
        ArrayList<Stock> checked = new ArrayList<Stock>();
        for (int i = 0; i < stocks.size(); i++) {
            if (stocks.get(i).isItemChecked())
            checked.add(stocks.get(i));
        }

        return checked;
    }



    /**
     * Parses a JSON stock query response. Individual JSON stock quotes are passed to the
     * parseStockQuoteJSONObject() method. On finish, the current stock details fragment is
     * updated.
     * @param stockQueryJSONObject
     */
    public void parseStockQueryJSONObject(JSONObject stockQueryJSONObject) {
        try {
            JSONObject query = stockQueryJSONObject.getJSONObject("query");
            int count = query.getInt("count");
            JSONObject results = query.getJSONObject("results");

            if (count > 1) {
                JSONArray quotes = results.getJSONArray("quote");
                for (int i = 0; i < count; i++) {
                    parseStockQuoteJSONObject(quotes.getJSONObject(i));
                }
            }
            else {
                parseStockQuoteJSONObject(results.getJSONObject("quote"));
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /**
     * Parses a JSON stock quote response.
     * @param stockQuoteJSONObject
     */
    private void parseStockQuoteJSONObject(JSONObject stockQuoteJSONObject) {
        try {
            String symbol = stockQuoteJSONObject.getString("symbol");
            String name = stockQuoteJSONObject.getString("Name");
            String price = stockQuoteJSONObject.getString("LastTradePriceOnly");
            String change = stockQuoteJSONObject.getString("PercentChange");
            String prevClosePrice = stockQuoteJSONObject.getString("PreviousClose");
            String openPrice = stockQuoteJSONObject.getString("Open");
            String marketCap = stockQuoteJSONObject.getString("MarketCapitalization");
            String volume = stockQuoteJSONObject.getString("Volume");

            Stock stock = this.get(symbol);
            if (stock != null) {
                stock.setStockSymbol(symbol);
                stock.setStockName(name);
                stock.setStockPrice(price);
                stock.setStockChange(change);
                stock.setStockPrevClosePrice(prevClosePrice);
                stock.setStockOpenPrice(openPrice);
                stock.setStockMarketCap(marketCap);
                stock.setStockVolume(volume);
            }
            else {
                Log.d(TAG, "Stock " + symbol + " was not found in the stock list");
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
