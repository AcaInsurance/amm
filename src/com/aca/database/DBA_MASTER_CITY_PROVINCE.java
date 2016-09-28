package com.aca.database;

import java.util.LinkedList;
import java.util.List;

import com.aca.amm.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBA_MASTER_CITY_PROVINCE {

	public static final String NO = "NO";
    public static final String KOTA = "KOTA";
    public static final String PROPINSI = "PROPINSI";
    public static final String KABUPATEN = "KABUPATEN";
    public static final String ZONE = "ZONE";
    
    private static final String TAG = "DBA_MASTER_CITY_PROVINCE";
    
    private static final String DATABASE_NAME = "AMM_VERSION_BIG";
    private static final String DATABASE_TABLE = "MASTER_CITY_PROVINCE";

     static final String DATABASE_CREATE = "CREATE TABLE MASTER_CITY_PROVINCE (" +
     		"NO TEXT, KOTA TEXT, PROPINSI TEXT, KABUPATEN TEXT, ZONE TEXT);";
        
    private final Context context;    

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBA_MASTER_CITY_PROVINCE(Context ctx) 
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }
        
    private static class DatabaseHelper extends SQLiteOpenHelper 
    {
        DatabaseHelper(Context context) 
        {
            super(context, DATABASE_NAME, null, Utility.getDatabaseVersionBig());
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
        	/*
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS MASTER_CITY_PROVINCE");
            onCreate(db);
            */
        }
    }    

    //---opens the database---
    public DBA_MASTER_CITY_PROVINCE open() throws SQLException 
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
    public long insert(String no, String kota, String propinsi, String kabupaten, String zone) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(NO, no);
        initialValues.put(KOTA, kota);
        initialValues.put(PROPINSI, propinsi);
        initialValues.put(KABUPATEN, kabupaten);
        initialValues.put(ZONE, zone);
        
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //---deletes a particular contact---
    public boolean deleteAll() 
    {
        return db.delete(DATABASE_TABLE, null, null) > 0;
    }
    
    public Cursor getAll() 
    {
    	return db.query(DATABASE_TABLE,  new String[] {NO, KOTA, PROPINSI, KABUPATEN, ZONE}, null, null, null, null, KOTA + " asc");    			
    }
}
