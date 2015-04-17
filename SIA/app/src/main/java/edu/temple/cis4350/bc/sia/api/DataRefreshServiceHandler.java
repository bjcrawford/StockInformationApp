package edu.temple.cis4350.bc.sia.api;

import android.net.http.AndroidHttpClient;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import edu.temple.cis4350.bc.sia.MainActivity;
import edu.temple.cis4350.bc.sia.util.Utils;

public class DataRefreshServiceHandler extends Handler {

    private static final String TAG = "DataRefreshServiceHandl";

    private boolean run;
    private boolean destroy;

    private DataRefreshService dataRefreshService;

    private APIResponseHandler apiResponseHandler;

    public DataRefreshServiceHandler(DataRefreshService dataRefreshService, Looper looper) {
        super(looper);
        this.dataRefreshService = dataRefreshService;
        setDestroy(false);
    }

    public synchronized void setRun(boolean run) {
        this.run = run;
        if (!run) {
            this.notify();
        }
    }

    public synchronized boolean getRun() {
        return run;
    }

    public synchronized void setDestroy(boolean destroy) {
        this.destroy = destroy;
        if (destroy) {
            this.notify();
        }
    }

    public synchronized boolean getDestroy() {
        return destroy;
    }

    public synchronized APIResponseHandler getApiResponseHandler() {
        return apiResponseHandler;
    }

    public synchronized void setApiResponseHandler(APIResponseHandler apiResponseHandler) {
        this.apiResponseHandler = apiResponseHandler;
    }

    @Override
    public void handleMessage(Message msg) {
        Log.d(TAG, "Begin message " + msg.arg1 + " handling");
        setRun(true);
        int interval = msg.arg2 * 1000;
        String[] apiUrls = (String[]) msg.obj;

        if (interval != 0) {
            while (getRun() && !getDestroy()) {

                // Make the api requests and return the json using the api response handler
                makeApiRequest(getApiResponseHandler(), apiUrls);

                synchronized (this) {
                    try {
                        wait(interval);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        Log.d(TAG, "End message " + msg.arg1 + " handling");
    }

    protected void makeApiRequest(final APIResponseHandler apiResponseHandler, final String... apiUrls) {

        Thread thread = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < apiUrls.length; i++) {

                    // Attempt to make API call for JSON quote response
                    if (Utils.hasConnection(dataRefreshService)) {
                        try {
                            URL url = new URL(apiUrls[i]);
                            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                            String response = reader.readLine();
                            String totalResponse = "";

                            // Read the entire response
                            while (response != null) {
                                totalResponse += response;
                                response = reader.readLine();
                            }

                            // Pass json response to the handler
                            JSONObject jsonObject = new JSONObject(totalResponse);
                            Message msg = Message.obtain();
                            if (apiUrls[i].contains("quote")) {
                                msg.arg1 = MainActivity.STOCK_QUERY_ID;
                            } else if (apiUrls[i].contains("headline")) {
                                msg.arg1 = MainActivity.NEWS_QUERY_ID;
                            } else {
                                msg.arg1 = MainActivity.COMPANY_QUERY_ID;
                            }

                            msg.obj = jsonObject;
                            apiResponseHandler.sendMessage(msg);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        Toast.makeText(dataRefreshService, "No network connection", Toast.LENGTH_LONG).show();
                    }
                }
            }
        };
        thread.start();
    }
}
