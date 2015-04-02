package edu.temple.cis4350.bc.sia.apirequest;

/**
 * Created by bcrawford on 3/23/15.
 */
public class APIURLBuilder {

    public static String getStockQueryURL(String... stockSymbols) {

        // Build the yql url from the stock symbols
        String yqlUrl = "http://query.yahooapis.com/v1/public/yql?q=" +
                "select%20*%20from%20yahoo.finance.quotes%20" +
                "where%20symbol%20in%20(";

        yqlUrl += "%22" + stockSymbols[0] + "%22";

        for (int i = 1; i < stockSymbols.length; i++) {
            yqlUrl += "%2C%22" + stockSymbols[i] + "%22";
        }
        yqlUrl += ")%0A%09%09&env=http%3A%2F%2Fdatatables.org%2Falltables.env&format=json";

        return yqlUrl;
    }

    public static String getNewsQueryURL(String... stockSymbols) {

        // Build the yql url from the stock symbols

        //https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20xml%20where%20url%3D%27http%3A%2F%2Ffinance.yahoo.com%2Frss%2Fheadline%3Fs%3D%27&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&format=json

       String yqlUrl = "https://query.yahooapis.com/v1/public/yql?q=" +
               "select%20*%20from%20xml%20" +
               "where%20url%3D%27http%3A%2F%2Ffinance.yahoo.com%2Frss%2Fheadline%3Fs%3D";

        for (int i = 0; i < stockSymbols.length; i++) {
            if (i != 0) {
                yqlUrl += "%2B";
            }
            yqlUrl += stockSymbols[i];
        }
        yqlUrl += "%27&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&format=json";

        return yqlUrl;
    }

    public static String getCompanySearchURL(String searchString) {

        String apiUrl = "http://d.yimg.com/autoc.finance.yahoo.com/autoc?callback=YAHOO.Finance.SymbolSuggest.ssCallback&query=";
        apiUrl += searchString;

        return apiUrl;
    }

}
