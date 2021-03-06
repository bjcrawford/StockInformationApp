/*
  Brett Crawford
  Stock Information App
  CIS 4350
  Spring 2015
 */

package edu.temple.cis4350.bc.sia.stock;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A class for representing a stock
 */
public class Stock {

    // Data to persist
    private String stockSymbol;
    private int stockColorCode;
    private int listPosition;

    // Data that can be retrieved
    private String stockName;
    private String stockPrice;
    private String stockChange;
    private String stockPrevClosePrice;
    private String stockOpenPrice;
    private String stockMarketCap;
    private String stockVolume;

    // State variables
    private boolean isChecked;

    /**
     * Creates a Stock object.
     *
     * @param stockSymbol the symbol
     * @param stockColorCode the RGB color code
     * @param listPosition the list position
     */
    public Stock(String stockSymbol, int stockColorCode, int listPosition) {

        this.stockSymbol = stockSymbol.toUpperCase();
        this.stockColorCode = stockColorCode;
        this.listPosition = listPosition;
        this.stockName = "null";
        this.stockPrice = "null";
        this.stockChange = "null";
        this.stockPrevClosePrice = "null";
        this.stockOpenPrice = "null";
        this.stockMarketCap = "null";
        this.stockVolume = "null";
        this.isChecked = false;
    }

    /**
     * Creates a Stock object from a JSON representation.
     *
     * @param stockJSONObject the JSON representation
     */
    public Stock(JSONObject stockJSONObject) {
        try {
            // These will be necessary to populate
            this.stockSymbol = stockJSONObject.getString("symbol");
            this.stockColorCode = stockJSONObject.getInt("colorCode");
            this.listPosition = stockJSONObject.getInt("listPosition");
            this.isChecked = false;
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a JSON representation of the Stock object.
     *
     * @return the JSON representation
     */
    public JSONObject getStockJSONObject() {
        JSONObject stockJSONObject = new JSONObject();
        try {
            // These will be necessary to persist
            stockJSONObject.put("symbol", stockSymbol);
            stockJSONObject.put("colorCode", stockColorCode);
            stockJSONObject.put("listPosition", listPosition);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return stockJSONObject;
    }

    /**
     * Returns the stock symbol.
     *
     * @return the stock symbol
     */
    public String getStockSymbol() {
        return stockSymbol;
    }

    /**
     * Sets the stock symbol.
     *
     * @param stockSymbol the stock symbol
     */
    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    /**
     * Returns the stock color code.
     *
     * @return the stock color code
     */
    public int getStockColorCode() {
        return stockColorCode;
    }

    /**
     * Sets the stock color code.
     *
     * @param stockColorCode the stock color code
     */
    public void setStockColorCode(int stockColorCode) {
        this.stockColorCode = stockColorCode;
    }

    /**
     * Returns the list position.
     *
     * @return the list position
     */
    public int getListPosition() {
        return listPosition;
    }

    /**
     * Sets the list position.
     *
     * @param listPosition the list position
     */
    public void setListPosition(int listPosition) {
        this.listPosition = listPosition;
    }

    /**
     * Returns the stock name.
     *
     * @return the stock name
     */
    public String getStockName() {
        return stockName;
    }

    /**
     * Sets the stock name.
     *
     * @param stockName the stock name
     */
    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    /**
     * Returns the stock price.
     *
     * @return the stock price
     */
    public String getStockPrice() {
        return "$" + stockPrice;
    }

    /**
     * Sets the stock price.
     *
     * @param stockPrice the stock price
     */
    public void setStockPrice(String stockPrice) {
        this.stockPrice = stockPrice;
    }

    /**
     * Returns the stock change.
     *
     * @return the stock change
     */
    public String getStockChange() {
        return stockChange;
    }

    /**
     * Sets the stock change.
     *
     * @param stockChange the stock change
     */
    public void setStockChange(String stockChange) {
        this.stockChange = stockChange;
    }

    /**
     * Returns the stock previous close price.
     *
     * @return the stock previous close price
     */
    public String getStockPrevClosePrice() {
        return "$" + stockPrevClosePrice;
    }

    /**
     * Sets the stock previous close price.
     *
     * @param stockPrevClosePrice the stock previous close price
     */
    public void setStockPrevClosePrice(String stockPrevClosePrice) {
        this.stockPrevClosePrice = stockPrevClosePrice;
    }

    /**
     * Returns the stock open price.
     *
     * @return the stock open price
     */
    public String getStockOpenPrice() {
        return "$" + stockOpenPrice;
    }

    /**
     * Sets the stock open price.
     *
     * @param stockOpenPrice the stock open price
     */
    public void setStockOpenPrice(String stockOpenPrice) {
        this.stockOpenPrice = stockOpenPrice;
    }

    /**
     * Returns the stock market capitalization.
     *
     * @return the stock market capitalization
     */
    public String getStockMarketCap() {
        return "$" + stockMarketCap;
    }

    /**
     * Sets the stock market capitalization.
     *
     * @param stockMarketCap the stock market capitalization
     */
    public void setStockMarketCap(String stockMarketCap) {
        this.stockMarketCap = stockMarketCap;
    }


    /**
     * Returns the stock volume
     *
     * @return the stock volume
     */
    public String getStockVolume() {
        return stockVolume;
    }

    /**
     * Sets the stock volume
     *
     * @param stockVolume the stock volume
     */
    public void setStockVolume(String stockVolume) {
        this.stockVolume = stockVolume;
    }

    /**
     * Returns the state of the stocks checked property
     *
     * @return true if checked, false otherwise
     */
    public boolean isItemChecked() {
        return this.isChecked;
    }

    /**
     * Sets the state of the stocks checked property
     *
     * @param isChecked the state(true or false)
     */
    public void setItemChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

}
