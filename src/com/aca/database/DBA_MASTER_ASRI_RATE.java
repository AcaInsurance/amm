package com.aca.database;

import com.aca.amm.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBA_MASTER_ASRI_RATE {

	public static final String ZONE = "ZONE";
    public static final String RATE = "RATE";
    public static final String KETERANGAN = "KETERANGAN";
    
    private static final String TAG = "DBA_MASTER_ASRI_RATE";
    
    private static final String DATABASE_NAME = "AMM_VERSION_BIG";
    private static final String DATABASE_TABLE = "MASTER_ASRI_RATE";

     static final String DATABASE_CREATE =
        "CREATE TABLE MASTER_ASRI_RATE (ZONE TEXT, RATE NUMERIC, KETERANGAN TEXT);";
        
    private final Context context;    

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBA_MASTER_ASRI_RATE(Context ctx) 
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
    public DBA_MASTER_ASRI_RATE open() throws SQLException 
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
    public long insert(String zone, double rate, String keterangan) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(ZONE, zone);
        initialValues.put(RATE, rate);
        initialValues.put(KETERANGAN, keterangan);
        
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //---deletes a particular contact---
    public boolean deleteAll() 
    {
        return db.delete(DATABASE_TABLE, null, null) > 0;
    }
    
    public Cursor getAll() 
    {
    	return db.query(DATABASE_TABLE,  new String[] {ZONE, RATE, KETERANGAN}, null, null, null, null, null);    			
    }
    
    public Cursor getRate(double TSI) 
    {
    	//return db.query(DATABASE_TABLE,  new String[] {KATEGORI, JNP, RATE}, JNP + ">=" + TSI , null, null, null, null);    			
    
    	return db.rawQuery("SELECT ZONE, RATE, KETERANGAN FROM MASTER_ASRI_RATE LIMIT 1",null);
    	
    }
}
