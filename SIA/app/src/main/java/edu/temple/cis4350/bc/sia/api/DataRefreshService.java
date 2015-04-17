package edu.temple.cis4350.bc.sia.api;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.*;
import android.util.Log;

import edu.temple.cis4350.bc.sia.MainActivity;
import edu.temple.cis4350.bc.sia.R;

public class DataRefreshService extends Service {

    private static final String TAG = "DataRefreshService";
    private static final int RUNNING_NOTIFICATION = 1;

    private int idCount;

    private NotificationManager notificationManager;
    private Looper serviceLooper;
    private DataRefreshServiceHandler dataRefreshServiceHandler;

    private final IBinder dataRefreshBinder = new DataRefreshBinder();

    public class DataRefreshBinder extends Binder {
        public DataRefreshService getService() {
            return DataRefreshService.this;
        }
    }

    public DataRefreshService() {

    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate() fired");

        idCount = 1;

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        showNotification();

        HandlerThread thread = new HandlerThread(TAG, android.os.Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        serviceLooper = thread.getLooper();
        dataRefreshServiceHandler = new DataRefreshServiceHandler(this, serviceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() fired");
        return START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return dataRefreshBinder;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy() fired");
        dataRefreshServiceHandler.setRun(false);
        dataRefreshServiceHandler.setDestroy(true);
    }

    public void startAutoRefresh(APIResponseHandler apiResponseHandler, int refreshRate, String... apiUrls) {

        // Shut down the currently running loop inside the handler
        dataRefreshServiceHandler.setRun(false);

        // Start a new loop running in the handler
        dataRefreshServiceHandler.setApiResponseHandler(apiResponseHandler);
        Message msg = dataRefreshServiceHandler.obtainMessage();
        msg.arg1 = idCount++;
        msg.arg2 = refreshRate;
        msg.obj = apiUrls;
        dataRefreshServiceHandler.sendMessage(msg);
    }

    public void manualRefresh(APIResponseHandler apiResponseHandler, String... apiUrls) {

        dataRefreshServiceHandler.makeApiRequest(apiResponseHandler, apiUrls);
    }

    private void showNotification() {

        Notification.Builder n;
        Intent stopIntent = new Intent(DataRefreshService.this, MainActivity.class);
        stopIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        stopIntent.setAction(MainActivity.NOTIFICATION_CHECKS_STOCKS);
        PendingIntent stopPIntent = PendingIntent.getActivity(DataRefreshService.this, 0, stopIntent, 0);
        n = new Notification.Builder(DataRefreshService.this)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText("Check stocks")
                .setSmallIcon(R.drawable.sia_notification_icon)
                .setContentIntent(stopPIntent)
                .setAutoCancel(false);
        DataRefreshService.this.startForeground(RUNNING_NOTIFICATION, n.build());
    }


}
