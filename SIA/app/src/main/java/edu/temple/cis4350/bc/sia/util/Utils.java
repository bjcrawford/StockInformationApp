package edu.temple.cis4350.bc.sia.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Utils {

    /**
     * A static method for converting a file into a string of text
     *
     * @param file The file to read from
     * @return The text from the file
     */
    public static String fileToString(File file) {
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return text.toString();
    }

    /**
     * A static method for writing text to a file. Overwrites the file.
     *
     * @param text The text to write
     * @param file The file to write to
     */
    public static void stringToFile(String text, File file) {
        try {
            FileWriter fw = new FileWriter(file);
            fw.write(text);
            fw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * Checks for an internet connection.
     *
     * @return true if connection is found, otherwise false
     */
    public static boolean hasConnection(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }
}
