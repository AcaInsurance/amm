package com.aca.database;

import com.aca.amm.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBA_RATE_ASRI {

	public static final String CODE = "CODE";
    public static final String RATE = "RATE";
    
    private static final String TAG = "DBA_RATE_ASRI";
    
    private static final String DATABASE_NAME = "AMM_VERSION_BIG";
    private static final String DATABASE_TABLE = "RATE_ASRI";

     static final String DATABASE_CREATE =
        "CREATE TABLE RATE_ASRI (CODE TEXT, RATE NUMERIC);";
        
    private final Context context;    

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBA_RATE_ASRI(Context ctx) 
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
        	}*/
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
        {
        	/*
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS RATE_ASRI");
            onCreate(db);*/
        }
    }    

    //---opens the database---
    public DBA_RATE_ASRI open() throws SQLException 
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
    public long insert(String code, double rate) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(CODE, code);
        initialValues.put(RATE, rate);
        
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //---deletes a particular contact---
    public boolean deleteAll() 
    {
        return db.delete(DATABASE_TABLE, null, null) > 0;
    }
    
    public Cursor getAll() 
    {
    	return db.query(DATABASE_TABLE,  new String[] {CODE, RATE}, null, null, null, null, CODE + " asc");    			
    }
    
    public Cursor getByCode(String code) 
    {
    	return db.query(DATABASE_TABLE,  new String[] {CODE, RATE}, CODE + "='" + code + "'", null, null, null, CODE + " asc");    			
    }
}
