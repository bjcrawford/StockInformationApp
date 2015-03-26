/*
  Brett Crawford
  Stock Information App
  CIS 4350
  Spring 2015
 */

package edu.temple.cis4350.bc.sia.apirequest;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * An AsyncTask that handles requesting  data from various APIs.
 */
public class APIRequestTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "APIRequestTask";

    private Handler handler;
    private String apiUrl;
    private int arg1id;

    public APIRequestTask(Handler handler, String apiUrl) {
        this.handler = handler;
        this.apiUrl = apiUrl;
        if (this.apiUrl.contains("quotes")) {
            arg1id = 1;
        }
        else if (this.apiUrl.contains("headline")) {
            arg1id = 2;
        }
        else if (this.apiUrl.contains("SymbolSuggest")) {
            arg1id = 4;
        }
    }

    @Override
    protected Void doInBackground(Void... params) {

        try {
            // Attempt to make API call for JSON quote response
            URL url = new URL(apiUrl);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String response = reader.readLine();
            String totalResponse = "";

            // Read the entire response
            while (response != null) {
                totalResponse += response;
                response = reader.readLine();
            }

            // If the response is from the company search, we have to strip off the crap
            if (arg1id == 4) {
                totalResponse = totalResponse.replace("YAHOO.Finance.SymbolSuggest.ssCallback(", "");
                totalResponse = totalResponse.replace(")", "");
            }

            // Pass response to the handler
            JSONObject jsonObject = new JSONObject(totalResponse);
            Message msg = Message.obtain();
            msg.arg1 = arg1id;
            msg.obj = jsonObject;
            handler.sendMessage(msg);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
