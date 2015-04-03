/*
  Brett Crawford
  Stock Information App
  CIS 4350
  Spring 2015
 */

package edu.temple.cis4350.bc.sia.apirequest;

/**
 * A simple class for building API URLs with given parameters
 */
public class APIURLBuilder {

    public static String getStockQueryURL(String... stockSymbols) {

        // Build the yql url from the stock symbols
        String yqlUrl = "http://query.yahooapis.com/v1/public/yql?q=" +
                "select%20*%20from%20yahoo.finance.quotes%20" +
                "where%20symbol%20in%20(";

        for (int i = 0; i < stockSymbols.length; i++) {
            yqlUrl += (i > 0 ? "%2C" : "") + "%22" + stockSymbols[i] + "%22";
        }
        yqlUrl += ")%0A%09%09&env=http%3A%2F%2Fdatatables.org%2Falltables.env&format=json";

        return yqlUrl;
    }

    public static String getNewsQueryURL(String... stockSymbols) {

        // Build the yql url from the stock symbols
        String yqlUrl = "https://query.yahooapis.com/v1/public/yql?q=" +
                "select%20*%20from%20xml%20" +
                "where%20url%3D%27http%3A%2F%2Ffinance.yahoo.com%2Frss%2Fheadline%3Fs%3D";

        for (int i = 0; i < stockSymbols.length; i++) {
            yqlUrl += (i > 0 ? "%2B" : "") + stockSymbols[i];
        }
        yqlUrl += "%27&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&format=json";

        return yqlUrl;
    }

    public static String getCompanySearchURL(String searchString) {

        // Add the search string to the url
        return "http://d.yimg.com/autoc.finance.yahoo.com/autoc?callback=YAHOO.Finance." +
                "SymbolSuggest.ssCallback&query=" + searchString;
    }

}
