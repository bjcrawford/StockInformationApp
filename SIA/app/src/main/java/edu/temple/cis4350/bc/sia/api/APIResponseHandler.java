/*
  Brett Crawford
  Stock Information App
  CIS 4350
  Spring 2015
 */

package edu.temple.cis4350.bc.sia.api;

import android.os.Handler;
import android.os.Message;

import org.json.JSONObject;

import edu.temple.cis4350.bc.sia.MainActivity;

/**
 * A handler class for communicating API request responses to the main activity.
 */
public class APIResponseHandler extends Handler {

    private static final String TAG = "APIResponseHandler";

    private OnAPIResponseHandlerInteractionListener listener;

    public APIResponseHandler(OnAPIResponseHandlerInteractionListener listener) {
        this.listener = listener;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.arg1) {

            case MainActivity.STOCK_QUERY_ID:
                JSONObject stockQueryJSONObject = (JSONObject) msg.obj;
                onResponse(stockQueryJSONObject, MainActivity.STOCK_QUERY_ID);
                break;

            case MainActivity.NEWS_QUERY_ID:
                JSONObject newsQueryJSONObject = (JSONObject) msg.obj;
                onResponse(newsQueryJSONObject, MainActivity.NEWS_QUERY_ID);
                break;

            case MainActivity.COMPANY_QUERY_ID:
                JSONObject companySearchJSONObject = (JSONObject) msg.obj;
                onResponse(companySearchJSONObject, MainActivity.COMPANY_QUERY_ID);

            default:
                onError(TAG + " Error: Invalid handler message format");
                break;
        }
    }


    public void onResponse(JSONObject jsonObject, int taskId) {
        if (listener != null) {
            listener.onAPIResponseHandlerInteraction(jsonObject, taskId);
        }
    }

    public void onError(String errorMsg) {
        if (listener != null) {
            listener.onAPIResponseHandlerError(errorMsg);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * handler to allow an interaction in this handler to be communicated
     * to the activity.
     */
    public interface OnAPIResponseHandlerInteractionListener {
        public void onAPIResponseHandlerInteraction(JSONObject jsonObject, int taskId);
        public void onAPIResponseHandlerError(String errorMsg);
    }
}
