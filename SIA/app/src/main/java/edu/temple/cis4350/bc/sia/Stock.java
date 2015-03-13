package edu.temple.cis4350.bc.sia;

import org.json.JSONException;
import org.json.JSONObject;

public class Stock {

    public String stockSymbol;
    public String stockName;
    public int stockColorCode;
    public double stockPrice;
    public double stockChange;
    public double stockPrevClosePrice;
    public double stockOpenPrice;
    public double stockMarketCap;

    public Stock(String stockSymbol, int stockColorCode) {
        this.stockSymbol = stockSymbol;
        this.stockName = "null";
        this.stockColorCode = stockColorCode;
        this.stockPrice = 0;
        this.stockChange = 0;
        this.stockPrevClosePrice = 0;
        this.stockOpenPrice = 0;
        this.stockMarketCap = 0;
    }

    public Stock(String stockSymbol, String stockName, int stockColorCode) {
        this.stockSymbol = stockSymbol;
        this.stockName = stockName;
        this.stockColorCode = stockColorCode;
        this.stockPrice = 0;
        this.stockChange = 0;
        this.stockPrevClosePrice = 0;
        this.stockOpenPrice = 0;
        this.stockMarketCap = 0;
    }

    public Stock(JSONObject stockJSONObject) {
        try {
            // These will be necessary
            this.stockSymbol = stockJSONObject.getString("symbol");
            this.stockName = stockJSONObject.getString("name");
            this.stockColorCode = stockJSONObject.getInt("colorCode");

            // These may not be necessary, as they should be populated during run-time
            this.stockPrice = stockJSONObject.getDouble("price");
            this.stockChange = stockJSONObject.getDouble("change");
            this.stockPrevClosePrice = stockJSONObject.getDouble("prevClosePrice");
            this.stockOpenPrice = stockJSONObject.getDouble("openPrice");
            this.stockMarketCap = stockJSONObject.getDouble("marketCap");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getStockJSONObject() {
        JSONObject stockJSONObject = new JSONObject();
        try {
            // These should be necessary
            stockJSONObject.put("symbol", stockSymbol);
            stockJSONObject.put("name", stockName);
            stockJSONObject.put("colorCode", stockColorCode);

            // These change to often to warrant persisting the data
            stockJSONObject.put("price", stockPrice);
            stockJSONObject.put("change", stockChange);
            stockJSONObject.put("prevClosePrice", stockPrevClosePrice);
            stockJSONObject.put("openPrice", stockOpenPrice);
            stockJSONObject.put("marketCap", stockMarketCap);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return stockJSONObject;
    }

    public int getStockColorCode() {
        return stockColorCode;
    }

    public void setStockColorCode(int stockColorCode) {
        this.stockColorCode = stockColorCode;
    }

    public double getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(double stockPrice) {
        this.stockPrice = stockPrice;
    }

    public double getStockChange() {
        return stockChange;
    }

    public void setStockChange(double stockChange) {
        this.stockChange = stockChange;
    }

    public double getStockPrevClosePrice() {
        return stockPrevClosePrice;
    }

    public void setStockPrevClosePrice(double stockPrevClosePrice) {
        this.stockPrevClosePrice = stockPrevClosePrice;
    }

    public double getStockOpenPrice() {
        return stockOpenPrice;
    }

    public void setStockOpenPrice(double stockOpenPrice) {
        this.stockOpenPrice = stockOpenPrice;
    }

    public double getStockMarketCap() {
        return stockMarketCap;
    }

    public void setStockMarketCap(double stockMarketCap) {
        this.stockMarketCap = stockMarketCap;
    }
}
