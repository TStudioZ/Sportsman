package com.tstudioz.sportsman.app.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Tomáš Zahálka on 10. 5. 2014.
 */
public class AbstractDAO {
    private final DatabaseHelper dbHelper;
    private boolean writable;
    protected SQLiteDatabase db;

    public AbstractDAO(Context context, boolean writable) {
        this.dbHelper = new DatabaseHelper(context);
        this.writable = writable;
        open();
    }

    private void open() {
        if (writable)
            db = dbHelper.getWritableDatabase();
        else
            db = dbHelper.getReadableDatabase();
    }

    private void close() {
        db.close();
    }
}
