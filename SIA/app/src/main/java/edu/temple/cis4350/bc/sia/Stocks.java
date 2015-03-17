package edu.temple.cis4350.bc.sia;

import android.util.Log;

import java.util.ArrayList;

public class Stocks {

    private static final String TAG = "Stocks";

    private ArrayList<Stock> stocks;

    public Stocks() {
        stocks = new ArrayList<Stock>();
    }

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

    public Stock get(int index) {
        return stocks.get(index);
    }

    public Stock get(String stockSymbol) {
        for (Stock s : stocks) {
            if (s.getStockSymbol().equals(stockSymbol)) {
                return s;
            }
        }
        Log.d(TAG, "Could not locate " + stockSymbol + " in list");
        return null;
    }

    public ArrayList<Stock> getArrayList() {
        return stocks;
    }

    public int size() {
        return stocks.size();
    }


}
