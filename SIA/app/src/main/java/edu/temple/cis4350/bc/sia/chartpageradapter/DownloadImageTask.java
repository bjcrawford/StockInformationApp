package edu.temple.cis4350.bc.sia.chartpageradapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.net.URL;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    private static final String TAG = "DownloadImageTask";

    private ImageView imageView;

    public DownloadImageTask(ImageView imageView) {
        this.imageView = imageView;
    }

    protected Bitmap doInBackground(String... imageUrls) {
        String imageUrl = imageUrls[0];
        Bitmap result = null;
        try {
            result = BitmapFactory.decodeStream(new URL(imageUrl).openStream());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    protected void onPostExecute(Bitmap result) {
        imageView.setImageBitmap(result);
    }
}
