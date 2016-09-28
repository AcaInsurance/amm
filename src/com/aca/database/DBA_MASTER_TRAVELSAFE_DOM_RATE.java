package com.aca.database;

import com.aca.amm.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBA_MASTER_TRAVELSAFE_DOM_RATE {

	public static final String AREA = "AREA";
    public static final String COVERAGE = "COVERAGE";
    public static final String DURATION_CODE = "DURATION_CODE";
    public static final String PREMI = "PREMI";
    public static final String MAX_BENEFIT = "MAX_BENEFIT";
    
    private static final String TAG = "DBA_MASTER_TRAVELSAFE_DOM_RATE";
    
    private static final String DATABASE_NAME = "AMM_VERSION_BIG";
    private static final String DATABASE_TABLE = "MASTER_TRAVELSAFE_DOM_RATE";
     static final String DATABASE_CREATE =
        "CREATE TABLE MASTER_TRAVELSAFE_DOM_RATE (AREA TEXT, COVERAGE TEXT, DURATION_CODE TEXT, PREMI NUMERIC, MAX_BENEFIT NUMERIC);";
        
    private final Context context;    

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBA_MASTER_TRAVELSAFE_DOM_RATE(Context ctx) 
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
            db.execSQL("DROP TABLE IF EXISTS MASTER_JENIS_TOKO");
            onCreate(db);
            */
        }
    }    

    //---opens the database---
    public DBA_MASTER_TRAVELSAFE_DOM_RATE open() throws SQLException 
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
    public long insert(String area, String coverage, String duration_code, double premi, double max_benefit) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(AREA, area);
        initialValues.put(COVERAGE, coverage);
        initialValues.put(DURATION_CODE, duration_code);
        initialValues.put(PREMI, premi);
        initialValues.put(MAX_BENEFIT, max_benefit);
        
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //---deletes a particular contact---
    public boolean deleteAll() 
    {
        return db.delete(DATABASE_TABLE, null, null) > 0;
    }
    
    public Cursor getAll() 
    {
    	return db.query(DATABASE_TABLE,  new String[] {AREA, COVERAGE, DURATION_CODE, PREMI, MAX_BENEFIT}, null, null, null, null, null);    			
    }
    public Cursor getRate(String area, String coverage, String durationCode) 
    {
    	return db.rawQuery("SELECT AREA, COVERAGE, DURATION_CODE, PREMI, MAX_BENEFIT FROM MASTER_TRAVELSAFE_DOM_RATE" +
    			" WHERE AREA = ? AND" +
    			"		COVERAGE = ? AND " +
    			"		DURATION_CODE  = ? " +
    			"	LIMIT 1", new String[]{area, coverage, durationCode});
    }
}
