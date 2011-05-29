/**
 *  Copyright (c) 2011 Scott Truger
 *  See the file COPYING.txt for copying permissions
 **/

package com.trugertech.quickbart;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Simple favorites database access helper class. Defines the basic CRUD operations
 * for the QuickBART, and gives the ability to list all favorites as well as
 * retrieve or modify a specific favorite.
 */
public class QuickBartDbAdapter {

    public static final String KEY_FAVORITE_NAME = "favorite_name";
	public static final String KEY_DESTINATION_SHORT = "destination_short";
    public static final String KEY_DESTINATION_LONG = "destination_long";
    public static final String KEY_DEPARTURE_SHORT = "departure_short";
    public static final String KEY_DEPARTURE_LONG = "departure_long";
    public static final String KEY_ROWID = "_id";

    private static final String TAG = "QuickBartDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "quickbart";
    private static final String DATABASE_TABLE = "favorites";
    private static final int DATABASE_VERSION = 4;

    /**
     * Database creation SQL statement
     */
    private static final String DATABASE_CREATE =
        "create table " + DATABASE_TABLE + " ("+ KEY_ROWID +" integer primary key autoincrement, "
        + KEY_FAVORITE_NAME + " text not null,"
        + KEY_DEPARTURE_SHORT + " text not null, "
        + KEY_DEPARTURE_LONG + " text not null," 
        + KEY_DESTINATION_SHORT + " text not null," 
        + KEY_DESTINATION_LONG + " text not null" 
        + ");";
    
    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public QuickBartDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the favorites database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public QuickBartDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
//        mDb.execSQL("DROP TABLE IF EXISTS favorites");
//        mDb.execSQL(DATABASE_CREATE);
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    /**
     * Create a new favorite using the departure and destination short and long 
     * names provided. If the favorite is successfully created return the new
     * rowId for that favorite, otherwise return a -1 to indicate failure.
     * 
     * @param favorite name
     * @param departure station short name
     * @param departure station long name
     * @param destination station short name
     * @param destination station long name
     * @return rowId or -1 if failed
     */
    public long createFavorite(String favorite_name, String dept_short, String dept_long,
    		String dest_short, String dest_long) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_FAVORITE_NAME, favorite_name);
        initialValues.put(KEY_DEPARTURE_SHORT, dept_short);
        initialValues.put(KEY_DEPARTURE_LONG, dept_long);
        initialValues.put(KEY_DESTINATION_SHORT, dest_short);
        initialValues.put(KEY_DESTINATION_LONG, dest_long);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Delete the favorite with the given rowId
     * 
     * @param rowId id of favorite to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteFavorite(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Return a Cursor over the list of all favorites in the database
     * 
     * @return Cursor over all favorites
     */
    public Cursor fetchAllFavorites() {

    	return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_FAVORITE_NAME, KEY_DEPARTURE_SHORT,
        		KEY_DEPARTURE_LONG, KEY_DESTINATION_SHORT, KEY_DESTINATION_LONG}, 
        		null, null, null, null, null);

    }

    /**
     * Return a Cursor positioned at the favorite that matches the given rowId
     * 
     * @param rowId id of favorite to retrieve
     * @return Cursor positioned to matching favorite, if found
     * @throws SQLException if favorite could not be found/retrieved
     */
    public Cursor fetchFavorite(long rowId) throws SQLException {

        Cursor mCursor =

            mDb.query(true, 
            		DATABASE_TABLE, 
            		new String[] {
            			KEY_ROWID,
            			KEY_FAVORITE_NAME,
            			KEY_DEPARTURE_SHORT, 
            			KEY_DEPARTURE_LONG, 
            			KEY_DESTINATION_SHORT,
            			KEY_DESTINATION_LONG}, 
        			KEY_ROWID + "=" + rowId, 
        			null,
                    null, 
                    null, 
                    null, 
                    null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /**
     * Update the favorite using the details provided. The favorite to be updated is
     * specified using the rowId, and it is altered to use the departure and 
     * destination short and long names passed in
     * 
     * @param rowId id of favorite to update
     * @param favorite name
     * @param departure station short name
     * @param departure station long name
     * @param destination station short name
     * @param destination station long name
     * @return true if the favorite was successfully updated, false otherwise
     */
    public boolean updateFavorite(long rowId, String favorite_name, String dept_short, String dept_long,
    		String dest_short, String dest_long) {
        ContentValues args = new ContentValues();
        args.put(KEY_FAVORITE_NAME, favorite_name);
        args.put(KEY_DEPARTURE_SHORT, dept_short);
        args.put(KEY_DEPARTURE_LONG, dept_long);
        args.put(KEY_DESTINATION_SHORT, dest_short);
        args.put(KEY_DESTINATION_LONG, dest_long);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}
