package edu.temple.cis4350.bc.sia;

/**
 * Created by bcrawford on 3/4/15.
 */
public class StockListItem {

    public String stockSymbol;
    public String stockName;
    public int stockColorCode;
    public float stockPrice;
    public float stockChange;


    public StockListItem(String stockSymbol, String stockName, int stockColorCode) {
        this.stockSymbol = stockSymbol;
        this.stockName = stockName;
        this.stockColorCode = stockColorCode;
    }
}
