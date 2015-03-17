package edu.temple.cis4350.bc.sia;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class StockQueryJSONTask extends AsyncTask<String, Void, Void> {

    private static final String TAG = "StockJSONQuoteTask";

    private Handler responseHandler;

    public StockQueryJSONTask(Handler responseHandler) {
        this.responseHandler = responseHandler;
    }

    @Override
    protected Void doInBackground(String... stockSymbols) {

        // Build the yql url from the stock symbols
        String yqlUrl = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quotes%20where%20symbol%20in%20(";
        yqlUrl += "%22" + stockSymbols[0] + "%22";
        for (int i = 1; i < stockSymbols.length; i++) {
            yqlUrl += "%2C%22" + stockSymbols[i] + "%22";
        }
        yqlUrl += ")%0A%09%09&env=http%3A%2F%2Fdatatables.org%2Falltables.env&format=json";

        try {
            // Attempt to make API call for JSON quote response
            URL url = new URL(yqlUrl);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String response = reader.readLine();
            String totalResponse = "";

            while (response != null) {
                totalResponse += response;
                response = reader.readLine();
            }

            // Pass response to the handler
            JSONObject stockQueryJSONObject = new JSONObject(totalResponse);
            Message msg = Message.obtain();
            msg.arg1 = 1;
            msg.obj = stockQueryJSONObject;
            responseHandler.sendMessage(msg);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
