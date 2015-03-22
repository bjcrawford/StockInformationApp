/*
  Brett Crawford
  Stock Information App
  CIS 4350
  Spring 2015
 */

package edu.temple.cis4350.bc.sia;

import android.util.Log;

import java.util.ArrayList;

/**
 * A class that holds an ArrayList of Stock objects.
 */
public class Stocks {

    private static final String TAG = "Stocks";

    /* The list of stocks */
    private ArrayList<Stock> stocks;

    /**
     * Creates a stocks object.
     */
    public Stocks() {

        stocks = new ArrayList<Stock>();
    }

    /**
     * Adds a stock to the list of stocks. Checks the list of stocks
     * for the stock symbol being added. If the stock symbol already
     * exists the stock is not added and false is returned. Otherwise,
     * the stock is added to the list and true is returned.
     *
     * @param stock the stock to add
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
     * Returns the stock with the given list position. If the stock is
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
     * Returns the ArrayList of stock objects.
     *
     * @return the ArrayList of stocks
     */
    public ArrayList<Stock> getArrayList() {

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
