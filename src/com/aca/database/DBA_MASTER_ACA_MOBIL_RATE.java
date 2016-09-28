package com.aca.database;

import java.text.NumberFormat;
import java.text.ParseException;

import com.aca.amm.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBA_MASTER_ACA_MOBIL_RATE {

	public static final String KATEGORI = "KATEGORI";
    public static final String JNP = "JNP";
    public static final String RATE = "RATE";
    
    private static final String TAG = "DBA_MASTER_ACA_MOBIL_RATE";
    
    private static final String DATABASE_NAME = "AMM_VERSION_BIG";
    private static final String DATABASE_TABLE = "MASTER_ACA_MOBIL_RATE";

    static final String DATABASE_CREATE =
        "CREATE TABLE MASTER_ACA_MOBIL_RATE (KATEGORI TEXT, JNP TEXT, RATE NUMERIC);";
        
    private final Context context;    

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBA_MASTER_ACA_MOBIL_RATE(Context ctx) 
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
    public DBA_MASTER_ACA_MOBIL_RATE open() throws SQLException 
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
    public long insert(String kategori, String jnp, double rate) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KATEGORI, kategori);
        initialValues.put(JNP, jnp);
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
    	return db.query(DATABASE_TABLE,  new String[] {KATEGORI, JNP, RATE}, null, null, null, null, null);    			
    }
    
    public Cursor getRate(String TSI) throws ParseException 
    {
    	NumberFormat nf = NumberFormat.getInstance();
    	nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		
    	
    	String sql = "SELECT KATEGORI, JNP, RATE FROM MASTER_ACA_MOBIL_RATE WHERE CAST(JNP as integer) >= " + String.valueOf(nf.parse(TSI).doubleValue()) + " LIMIT 1;";
    	return db.rawQuery(sql, null);
   
    }
}
