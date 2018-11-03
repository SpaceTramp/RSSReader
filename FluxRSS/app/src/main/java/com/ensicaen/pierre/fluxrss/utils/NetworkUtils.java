package com.ensicaen.pierre.fluxrss.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.ensicaen.pierre.fluxrss.utils.AppConstants.MOVED_PERMANENTLY;
import static com.ensicaen.pierre.fluxrss.utils.AppConstants.MOVED_TEMPORARILY;
import static com.ensicaen.pierre.fluxrss.utils.AppConstants.PING_GOOGLE;

public class NetworkUtils {

    // TODO Log.e
    public static boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec(PING_GOOGLE);
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static InputStream openLink(String url) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.connect();
        int responseCode = connection.getResponseCode();
        while (responseCode == MOVED_PERMANENTLY || responseCode == MOVED_TEMPORARILY) {
            url = connection.getHeaderField("Location");
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.connect();
            responseCode = connection.getResponseCode();
        }
        return connection.getInputStream();
    }
}
