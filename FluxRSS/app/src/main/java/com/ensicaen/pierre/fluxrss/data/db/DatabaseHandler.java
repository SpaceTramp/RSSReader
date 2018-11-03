package com.ensicaen.pierre.fluxrss.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ensicaen.pierre.fluxrss.data.network.MyRSSsaxHandler;
import com.ensicaen.pierre.fluxrss.data.db.model.Item;

import java.util.LinkedList;
import java.util.List;

import static com.ensicaen.pierre.fluxrss.utils.AppConstants.DATABASE_NAME;
import static com.ensicaen.pierre.fluxrss.utils.AppConstants.TABLE_NAME;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                    + " (title VARCHAR PRIMARY KEY, pubDate VARCHAR, description VARCHAR, image BLOB);";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void loadIndatabase(MyRSSsaxHandler handler) {
        onUpgrade(getWritableDatabase(), 0, 1);
        for (int i = 0; i < 5; i++) {
            Item it = handler.getItemByNumber(i);
            addItem(it.getTitle(), it.getPubDate(), it.getDescription(), it.getImage());
        }
    }

    public List<Item> loadFromDatabase() {
        List<Item> items = new LinkedList<>();

        String query = "SELECT  * FROM " + "entry";

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Item item;
        if (cursor.moveToFirst()) {
            do {
                item = new Item();
                item.setTitle(cursor.getString(0));
                item.setPubDate(cursor.getString(1));
                item.setDescription(cursor.getString(2));

                items.add(item);
            } while (cursor.moveToNext());
        }

        return items;
    }


    private Boolean addItem(String title, String pubDate, String description, byte[] image) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("pubDate", pubDate);
        values.put("description", description);
        values.put("image", image);

        long newRowId = db.insert(TABLE_NAME, null, values);

        return newRowId != -1;
    }
}