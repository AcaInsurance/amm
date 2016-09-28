package com.aca.database;

import com.aca.amm.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBA_RATE_OTOMATE {

	public static final String TSI_FROM = "TSI_FROM";
	public static final String TSI_TO = "TSI_TO";
	public static final String RATE = "RATE";
    
    private static final String TAG = "DBA_RATE_OTOMATE";
    
    private static final String DATABASE_NAME = "AMM_VERSION_BIG";
    private static final String DATABASE_TABLE = "RATE_OTOMATE";

     static final String DATABASE_CREATE =
        "CREATE TABLE RATE_OTOMATE (TSI_FROM NUMERIC, TSI_TO NUMERIC, RATE NUMERIC);";
        
    private final Context context;    

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBA_RATE_OTOMATE(Context ctx) 
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
            /*Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS RATE_OTOMATE");
            onCreate(db);*/
        }
    }    

    //---opens the database---
    public DBA_RATE_OTOMATE open() throws SQLException 
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
    public long insert(double tsifrom, double tsito, double rate) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(TSI_FROM, tsifrom);
        initialValues.put(TSI_TO, tsito);
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
    	return db.query(DATABASE_TABLE,  new String[] {TSI_FROM, TSI_TO, RATE}, null, null, null, null, TSI_FROM + " asc");    			
    }
    
    public Cursor getByCode(double tsi) 
    {
    	return db.query(DATABASE_TABLE,  new String[] {TSI_FROM, TSI_TO, RATE}, TSI_FROM + " <= " + String.valueOf(tsi) + " AND " + TSI_TO + " >= " + String.valueOf(tsi) , null, null, null, TSI_FROM + " asc");    			
    }
}
