package com.ensicaen.pierre.fluxrss.data.network.asyncTask;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.ensicaen.pierre.fluxrss.ui.home.HomeView;
import com.ensicaen.pierre.fluxrss.utils.NetworkUtils;

import java.io.InputStream;

import static android.graphics.BitmapFactory.decodeStream;


public class UpdateImageTask extends AsyncTask<String, Void, Void> {
    private static final String TAG = UpdateImageTask.class.getSimpleName();
    private HomeView mActivity;
    private Bitmap image;

    public UpdateImageTask(HomeView mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    protected void onPostExecute(Void handler) {
        mActivity.updateImage(image);
    }

    @Override
    protected Void doInBackground(String... param) {
        InputStream in = null;
        try {
            in = NetworkUtils.openLink(param[0]);
        } catch (Exception e) {
            Log.e(TAG, "doInBackground exception :" + e.getMessage());
        }
        image = decodeStream(in);
        return null;
    }


}