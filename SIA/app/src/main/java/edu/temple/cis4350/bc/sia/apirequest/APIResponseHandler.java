/*
  Brett Crawford
  Stock Information App
  CIS 4350
  Spring 2015
 */

package edu.temple.cis4350.bc.sia.apirequest;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import org.json.JSONObject;

import edu.temple.cis4350.bc.sia.MainActivity;

/**
 * A handler class for communicating API request responses to the main activity.
 */
public class APIResponseHandler extends Handler {

    private static final String TAG = "APIResponseHandler";

    private Context context;

    public APIResponseHandler(Context context) {
        this.context = context;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.arg1) {

            case 1: // Successful stock query JSON response received
                JSONObject stockQueryJSONObject = (JSONObject) msg.obj;
                // Pass the response to the main activity for processing
                ((MainActivity) context).parseStockQueryJSONObject(stockQueryJSONObject);
                break;

            case 2: // Successful news query JSON response received
                JSONObject newsQueryJSONObject = (JSONObject) msg.obj;
                // Pass the response to the main activity for processing
                ((MainActivity) context).parseNewsQueryJSONObject(newsQueryJSONObject);
                break;

            case 4: // Successful company search query response received
                JSONObject companySearchJSONObject = (JSONObject) msg.obj;
                // Pass the response to the main activity for processing
                ((MainActivity) context).parseCompanySearchJSONObject(companySearchJSONObject);

            default: // Invalid handler message format
                ((MainActivity) context).makeToast("Error: Invalid handler message format");
                break;
        }
    }
}
