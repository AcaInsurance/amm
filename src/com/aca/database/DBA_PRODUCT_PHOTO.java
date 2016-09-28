package com.aca.database;

import com.aca.amm.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBA_PRODUCT_PHOTO {

	public static final String PHOTO_ROWID = "PHOTO_ROWID";
	public static final String PRODUCT_MAIN_ID  = "PRODUCT_MAIN_ID";
	public static final String PHOTO_FILENAME = "PHOTO_FILENAME";
    public static final String PHOTO_DESCRIPTION = "PHOTO_DESCRIPTION";
    public static final String PHOTO_DATE_TAKEN = "PHOTO_DATE_TAKEN";
    
    private static final String TAG = "DBA_PRODUCT_PHOTO";
    
    private static final String DATABASE_NAME = "AMM_VERSION_2";
    private static final String DATABASE_TABLE = "PRODUCT_PHOTO";

     static final String DATABASE_CREATE =
        "CREATE TABLE PRODUCT_PHOTO (PHOTO_ROWID NUMERIC, PRODUCT_MAIN_ID NUMERIC, PHOTO_FILENAME TEXT, PHOTO_DESCRIPTION TEXT, PHOTO_DATE_TAKEN TEXT);";
        
    private final Context context;    

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBA_PRODUCT_PHOTO(Context ctx) 
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }
        
    private static class DatabaseHelper extends SQLiteOpenHelper 
    {
        DatabaseHelper(Context context) 
        {
            super(context, DATABASE_NAME, null, Utility.getDatabaseVersion());
        }

        @Override
        public void onCreate(SQLiteDatabase db) 
        {
        	/*
        	try {
        		db.execSQL(DATABASE_CREATE);	
        	} catch (SQLException e) {
        		e.printStackTrace();
        	}
        	*/
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
        {
            //Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
            //        + newVersion + ", which will destroy all old data");
            //db.execSQL("DROP TABLE IF EXISTS MASTER_CAR_BRAND");
            //onCreate(db);
        }
    }    

    //---opens the database---
    public DBA_PRODUCT_PHOTO open() throws SQLException 
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---    
    public void close() 
    {
        DBHelper.close();
    }
    
    //---insert a contact into the database---
    public long insert(long product_main_id, String photo_filename, String photo_description, String photo_date_take) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(PRODUCT_MAIN_ID, product_main_id);
        initialValues.put(PHOTO_FILENAME, photo_filename);
        initialValues.put(PHOTO_DESCRIPTION, photo_description);
        initialValues.put(PHOTO_DATE_TAKEN, photo_date_take);
       
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //---deletes a particular contact---
    public boolean deleteAll() 
    {
        return db.delete(DATABASE_TABLE, null, null) > 0;
    }
    
    public Cursor getRowsByProductMainID(long product_main_id) 
    {
    	return db.query(DATABASE_TABLE,  new String[] {PHOTO_ROWID, PRODUCT_MAIN_ID, PHOTO_FILENAME, PHOTO_DESCRIPTION, PHOTO_DATE_TAKEN}, PRODUCT_MAIN_ID + "=" + product_main_id, null, null, null, PHOTO_DESCRIPTION + " asc");    			
    }
    
    public Cursor getRow(long photo_row_id) 
    {
    	return db.query(DATABASE_TABLE, new String[] {
    			PHOTO_ROWID, PRODUCT_MAIN_ID, PHOTO_FILENAME, PHOTO_DESCRIPTION, PHOTO_DATE_TAKEN
    			}, 
    			PHOTO_ROWID + "=" + photo_row_id, null, null, null, null);
    }
}
