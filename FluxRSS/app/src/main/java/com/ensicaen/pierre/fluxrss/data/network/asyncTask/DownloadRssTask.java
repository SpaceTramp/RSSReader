package com.ensicaen.pierre.fluxrss.data.network.asyncTask;

import android.os.AsyncTask;
import com.ensicaen.pierre.fluxrss.data.network.MyRSSsaxHandler;
import com.ensicaen.pierre.fluxrss.ui.home.HomePresenter;

public class DownloadRssTask extends AsyncTask<MyRSSsaxHandler, Void, MyRSSsaxHandler> {
    private HomePresenter homePresenter;

    public DownloadRssTask(HomePresenter homePresenter) {
        this.homePresenter = homePresenter;
    }

    protected MyRSSsaxHandler doInBackground(MyRSSsaxHandler... handler) {
        handler[0].processFeed();
        return handler[0];
    }

    protected void onPostExecute(MyRSSsaxHandler handler) {
        homePresenter.setMaxNbImage(handler.getNumItemMax());
        homePresenter.reset(handler.getItemByNumber(0));
        homePresenter.getDataBase().loadIndatabase(handler);
    }
}