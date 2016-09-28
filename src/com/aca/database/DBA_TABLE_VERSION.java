package com.aca.database;

import com.aca.amm.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBA_TABLE_VERSION {

	public static final String VERSION = "VERSION";
    public static final String FUNCTION_NAME = "FUNCTION_NAME";
    public static final String IS_SUCCESS_SYNC = "IS_SUCCESS_SYNC";
    
    
    private static final String TAG = "DBA_TABLE_VERSION";
    
    private static final String DATABASE_NAME = "AMM_VERSION_BIG";
    private static final String DATABASE_TABLE = "TABLE_VERSION";

     static final String DATABASE_CREATE =
        "CREATE TABLE TABLE_VERSION (VERSION NUMERIC, FUNCTION_NAME TEXT, IS_SUCCESS_SYNC TEXT);";
        
    private final Context context;    

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBA_TABLE_VERSION(Context ctx) 
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
            //Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
            //        + newVersion + ", which will destroy all old data");
            //db.execSQL("DROP TABLE IF EXISTS MASTER_CAR_BRAND");
            //onCreate(db);
        }
    }    

    
    //---opens the database---
    public DBA_TABLE_VERSION open() throws SQLException 
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
    public long insert(int version, String function_name) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(VERSION, version);
        initialValues.put(FUNCTION_NAME, function_name);
        initialValues.put(IS_SUCCESS_SYNC, "NO");
        
        
        return db.insert(DATABASE_TABLE, null, initialValues);
    }
    
    public boolean update(int version, String function_name) 
    {
    	ContentValues initialValues = new ContentValues();
        initialValues.put(VERSION, version);
        initialValues.put(FUNCTION_NAME, function_name);
        initialValues.put(IS_SUCCESS_SYNC, "NO");
        
    	return db.update(DATABASE_TABLE, initialValues, FUNCTION_NAME + " = '" + function_name + "'", null) > 0;
    }
    public boolean setSuccessSync (String functionName) {
    	ContentValues initialValues = new ContentValues();
        initialValues.put(IS_SUCCESS_SYNC, "YES");

    	return db.update(DATABASE_TABLE, initialValues, FUNCTION_NAME + " = '" + functionName + "'", null) > 0;
    }
   
    public boolean setNeedSync (String functionName) {
    	ContentValues initialValues = new ContentValues();
        initialValues.put(IS_SUCCESS_SYNC, "NO");

    	return db.update(DATABASE_TABLE, initialValues, FUNCTION_NAME + " = '" + functionName + "'", null) > 0;
    }
    
    public boolean deleteAll() 
    {
        return db.delete(DATABASE_TABLE, null, null) > 0;
    }
    public boolean deleteByFunctionName(String wsFunctionName) 
    {
        return db.delete(DATABASE_TABLE, FUNCTION_NAME + "'='" + wsFunctionName, null) > 0;
    }
    
    public Cursor getAll() 
    {
    	return db.query(DATABASE_TABLE,  new String[] {VERSION, FUNCTION_NAME, IS_SUCCESS_SYNC}, null, null, null, null, FUNCTION_NAME + " asc");    			
    }
}
