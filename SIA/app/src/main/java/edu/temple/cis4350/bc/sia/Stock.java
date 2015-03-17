package edu.temple.cis4350.bc.sia;

import org.json.JSONException;
import org.json.JSONObject;

public class Stock {

    private String stockSymbol;
    private String stockName;
    private int stockColorCode;
    private String stockPrice;
    private String stockChange;
    private String stockPrevClosePrice;
    private String stockOpenPrice;
    private String stockMarketCap;

    public Stock(String stockSymbol, int stockColorCode) {
        this.stockSymbol = stockSymbol;
        this.stockName = "null";
        this.stockColorCode = stockColorCode;
        this.stockPrice = "0";
        this.stockChange = "0";
        this.stockPrevClosePrice = "0";
        this.stockOpenPrice = "0";
        this.stockMarketCap = "0";
    }

    public Stock(String stockSymbol, String stockName, int stockColorCode) {
        this.stockSymbol = stockSymbol;
        this.stockName = stockName;
        this.stockColorCode = stockColorCode;
        this.stockPrice = "0";
        this.stockChange = "0";
        this.stockPrevClosePrice = "0";
        this.stockOpenPrice = "0";
        this.stockMarketCap = "0";
    }

    public Stock(JSONObject stockJSONObject) {
        try {
            // These will be necessary
            this.stockSymbol = stockJSONObject.getString("symbol");
            this.stockName = stockJSONObject.getString("name");
            this.stockColorCode = stockJSONObject.getInt("colorCode");

            // These may not be necessary, as they should be populated during run-time
            this.stockPrice = stockJSONObject.getString("price");
            this.stockChange = stockJSONObject.getString("change");
            this.stockPrevClosePrice = stockJSONObject.getString("prevClosePrice");
            this.stockOpenPrice = stockJSONObject.getString("openPrice");
            this.stockMarketCap = stockJSONObject.getString("marketCap");
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

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public int getStockColorCode() {
        return stockColorCode;
    }

    public void setStockColorCode(int stockColorCode) {
        this.stockColorCode = stockColorCode;
    }

    public String getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(String stockPrice) {
        this.stockPrice = stockPrice;
    }

    public String getStockChange() {
        return stockChange;
    }

    public void setStockChange(String stockChange) {
        this.stockChange = stockChange;
    }

    public String getStockPrevClosePrice() {
        return stockPrevClosePrice;
    }

    public void setStockPrevClosePrice(String stockPrevClosePrice) {
        this.stockPrevClosePrice = stockPrevClosePrice;
    }

    public String getStockOpenPrice() {
        return stockOpenPrice;
    }

    public void setStockOpenPrice(String stockOpenPrice) {
        this.stockOpenPrice = stockOpenPrice;
    }

    public String getStockMarketCap() {
        return stockMarketCap;
    }

    public void setStockMarketCap(String stockMarketCap) {
        this.stockMarketCap = stockMarketCap;
    }
}
