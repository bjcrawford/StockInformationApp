package edu.temple.cis4350.bc.sia;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import org.json.JSONObject;

public class StockQueryJSONHandler extends Handler {

    private static final String TAG = "StockJSONQuoteHandler";

    private Context context;

    public StockQueryJSONHandler(Context context) {
        this.context = context;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.arg1) {
            case 1: // Successful JSON response received
                JSONObject stockQueryJSONObject = (JSONObject) msg.obj;
                // Pass the response to the main activity for processing
                ((MainActivity) context).parseStockQueryJSONObject(stockQueryJSONObject);
                break;
            default:
                ((MainActivity) context).makeToast("Error: Invalid handler message format");
                break;
        }
    }
}
