package com.pledgeapp.pledge.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class RecentQueriesHelper extends SQLiteOpenHelper {
    private static final String TAG = RecentQueriesHelper.class.getSimpleName();

    // Database Info
    private static final String DATABASE_NAME = "recentQueriesDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_SEARCH_QUERIES = "search_queries";

    // Search Queries Table Columns
    private static final String KEY_QUERY_ID = "_id";
    private static final String KEY_QUERY_STRING = "query_string";
    private static final int RECENT_QUERIES_LIMIT = 50;


    private static RecentQueriesHelper sInstance;

    // ...

    public static synchronized RecentQueriesHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new RecentQueriesHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    public RecentQueriesHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // These is where we need to write create table statements.
    // This is called when database is created.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SEARCH_QUERIES_TABLE = "CREATE TABLE " + TABLE_SEARCH_QUERIES +
                "(" +
                KEY_QUERY_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_QUERY_STRING + " TEXT" +
                ")";
        db.execSQL(CREATE_SEARCH_QUERIES_TABLE);
    }

    // This method is called when database is upgraded like
    // modifying the table structure,
    // adding constraints to database, etc
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        // SQL for upgrading the tables
        if (oldVersion != newVersion) {
            throw new RuntimeException("YOU NEED TO IMPLEMENT A MIGRATION!");
        }
    }

    public void addOrUpdateQuery(String queryString) {
        deleteQuery(queryString);
        ContentValues values = new ContentValues();
        values.put(KEY_QUERY_STRING, queryString);
        SQLiteDatabase db = getWritableDatabase();
        long newRowId = db.insert(TABLE_SEARCH_QUERIES, null, values);
        db.close();
        if (newRowId < 0) {
            throw new RuntimeException("Failed to insert search query");
        }
    }

    public void deleteQuery(String queryString) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_SEARCH_QUERIES, KEY_QUERY_STRING + " = ?", new String[]{queryString});
    }

    public List<SearchSuggestion> getAllRecentQueries() {
        List<SearchSuggestion> result = new ArrayList<>();

        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM %s ORDER BY %s DESC LIMIT %s",
                              TABLE_SEARCH_QUERIES,
                              KEY_QUERY_ID,
                              RECENT_QUERIES_LIMIT);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    String queryText = cursor.getString(cursor.getColumnIndex(KEY_QUERY_STRING));
                    SearchSuggestion recentQuery = new SearchSuggestion(queryText);
                    result.add(recentQuery);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get recent queries from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return result;
    }
}
