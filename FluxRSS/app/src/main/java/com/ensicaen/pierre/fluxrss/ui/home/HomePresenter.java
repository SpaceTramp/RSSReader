package com.ensicaen.pierre.fluxrss.ui.home;

import com.ensicaen.pierre.fluxrss.data.db.DatabaseHandler;
import com.ensicaen.pierre.fluxrss.data.network.MyRSSsaxHandler;
import com.ensicaen.pierre.fluxrss.R;
import com.ensicaen.pierre.fluxrss.data.db.model.Item;
import com.ensicaen.pierre.fluxrss.data.network.asyncTask.DownloadRssTask;
import com.ensicaen.pierre.fluxrss.utils.NetworkUtils;

import java.util.List;

import static com.ensicaen.pierre.fluxrss.utils.AppConstants.CURRENT_RSS;
import static com.ensicaen.pierre.fluxrss.utils.AppConstants.PARISIEN_RSS;

public class HomePresenter {

    public static final int CURRENT_IMAGE = 0;
    public static final int MAX_NB_IMAGE = 4;
    private HomeView mActivity;

    private int currentImage = 0;
    private int maxNbImage;
    private MyRSSsaxHandler handler;
    private DatabaseHandler dataBase;
    private Boolean modeOffline;
    private List<Item> items;


    public HomePresenter(HomeView mActivity) {
        this.mActivity = mActivity;
        dataBase = new DatabaseHandler(mActivity);
        handler = new MyRSSsaxHandler();
        handler.setUrl(CURRENT_RSS);
    }

    public void init() {
        if (NetworkUtils.isOnline()) {
            modeOffline = false;
            new DownloadRssTask(this).execute(handler);
        } else {
            sync();
        }
    }

    public void reset(Item currentItem){
        mActivity.resetDisplay(currentItem);
        mActivity.resetOnline(currentItem);
    }

    private void nextImage() {

        if (currentImage != maxNbImage) {
            currentImage++;
            if (modeOffline) {
                mActivity.resetDisplay(items.get(currentImage));
                mActivity.resetOffline();
            } else {
                mActivity.resetDisplay(handler.getItemByNumber(currentImage));
                mActivity.resetOnline(handler.getItemByNumber(currentImage));
            }
        }
    }

    private void previousImage() {
        if (currentImage != 0) {
            currentImage--;
            if (modeOffline) {
                mActivity.resetDisplay(items.get(currentImage));
                mActivity.resetOffline();

            } else {
                mActivity.resetDisplay(handler.getItemByNumber(currentImage));
                mActivity.resetOnline(handler.getItemByNumber(currentImage));
            }
        }
    }


    private void sync() {
        if (NetworkUtils.isOnline()) {
            new DownloadRssTask(this).execute(handler);
            dataBase.loadIndatabase(handler);
            mActivity.displayToast(mActivity.getString(R.string.sync_success));
            modeOffline = false;

        } else {
            setOfflineMode();
            items = dataBase.loadFromDatabase();
            mActivity.resetDisplay(items.get(currentImage));
            mActivity.resetOffline();

        }
    }

    private void setOfflineMode() {
        modeOffline = true;
        mActivity.displayToast(mActivity.getString(R.string.sync_failed));
        currentImage = CURRENT_IMAGE;
        maxNbImage = MAX_NB_IMAGE;
        mActivity.hideProgressBar();
    }

    public DatabaseHandler getDataBase() {
        return dataBase;
    }


    public void onPreviousClick() {
        previousImage();
    }

    public void onNextClick() {
        nextImage();
    }

    public void onSyncClick() {
        sync();
    }

    public void onSwipeRight() {
        previousImage();
    }

    public void onSwipeLeft() {
        nextImage();
    }

    public void onSwipeBottom() {
        sync();
    }

    public void onDestroy() {
        dataBase.close();
    }

    public void setMaxNbImage(int maxNbImage) {
        this.maxNbImage = maxNbImage;
    }
}
