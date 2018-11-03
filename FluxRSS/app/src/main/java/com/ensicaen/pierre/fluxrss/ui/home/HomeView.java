package com.ensicaen.pierre.fluxrss.ui.home;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ActionMenuItemView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ensicaen.pierre.fluxrss.utils.OnSwipeTouchListener;
import com.ensicaen.pierre.fluxrss.R;
import com.ensicaen.pierre.fluxrss.data.db.model.Item;
import com.ensicaen.pierre.fluxrss.data.network.asyncTask.UpdateImageTask;

public class HomeView extends AppCompatActivity {

    private HomePresenter homePresenter;

    private ImageView imgView;
    private ProgressBar progressBar;
    private ActionMenuItemView itemNext, itemPrevious;
    private TextView viewTitle, viewDate, viewDesc, offline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setWidgets();
        initialize();
    }

    private void initialize() {
        homePresenter = new HomePresenter(this);
        homePresenter.init();

        LinearLayout layout = findViewById(R.id.linearLayout);
        layout.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeRight() {
                homePresenter.onSwipeRight();
            }

            public void onSwipeLeft() {
                homePresenter.onSwipeLeft();
            }

            public void onSwipeBottom() {
                homePresenter.onSwipeBottom();
            }
        });
    }

    private void setWidgets() {
        imgView = findViewById(R.id.imageDisplay);
        progressBar = findViewById(R.id.progressBar);
        viewTitle = findViewById(R.id.imageTitle);
        viewDate = findViewById(R.id.imageDate);
        viewDesc = findViewById(R.id.imageDescription);
        offline = findViewById(R.id.offline);

    }

    @Override
    protected void onDestroy() {
        homePresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_display_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_previous:
                homePresenter.onPreviousClick();
                return true;
            case R.id.action_next:
                homePresenter.onNextClick();
                return true;
            case R.id.action_sync:
                homePresenter.onSyncClick();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void resetDisplay(Item currentItem) {
        itemNext = findViewById(R.id.action_next);
        itemPrevious = findViewById(R.id.action_previous);

        viewTitle.setText(currentItem.getTitle());
        viewDate.setText(currentItem.getPubDate());
        viewDesc.setText(currentItem.getDescription());
        imgView.setImageResource(0);
    }

    public void resetOffline() {
        offline.setVisibility(android.view.View.VISIBLE);
    }

    public void resetOnline(Item currentItem) {
        progressBar.setVisibility(android.view.View.VISIBLE);
        offline.setVisibility(android.view.View.INVISIBLE);
        itemNext.setEnabled(false);
        itemPrevious.setEnabled(false);
        new UpdateImageTask(this).execute(currentItem.getUrl());
    }

    public void updateImage(Bitmap image) {
        imgView.setImageBitmap(image);
        progressBar.setVisibility(android.view.View.INVISIBLE);
        itemNext.setEnabled(true);
        itemPrevious.setEnabled(true);
    }

    public void displayToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    public void hideProgressBar() {
        progressBar.setVisibility(android.view.View.INVISIBLE);
    }

}
