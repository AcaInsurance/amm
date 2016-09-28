package com.aca.database;

import java.text.NumberFormat;

import com.aca.amm.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBA_MASTER_WELLWOMAN_RATE {
	public static final String PLAN = "PLAN";
    public static final String BENEFIT = "BENEFIT";
    public static final String AGE = "AGE";
    public static final String PREMI = "PREMI";
    public static final String PREMI_REAS = "PREMI_REAS";
    
    private static final String TAG = "DBA_MASTER_WELLWOMAN_RATE";
    
    private static final String DATABASE_NAME = "AMM_VERSION_BIG";
    private static final String DATABASE_TABLE = "MASTER_WELLWOMAN_RATE";

     static final String DATABASE_CREATE =
        "CREATE TABLE MASTER_WELLWOMAN_RATE (PLAN TEXT, BENEFIT TEXT, AGE TEXT, PREMI NUMERIC, PREMI_REAS NUMERIC);";
        
    private final Context context;    

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBA_MASTER_WELLWOMAN_RATE(Context ctx) 
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
    public DBA_MASTER_WELLWOMAN_RATE open() throws SQLException 
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
    public long insert(String plan, String benefit, String age, double premi, double premi_reas) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(PLAN, plan);
        initialValues.put(BENEFIT, benefit);
        initialValues.put(AGE, age);
        initialValues.put(PREMI, premi);
        initialValues.put(PREMI_REAS, premi_reas);
        
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //---deletes a particular contact---
    public boolean deleteAll() 
    {
        return db.delete(DATABASE_TABLE, null, null) > 0;
    }
    
    public Cursor getAll() 
    {
    	return db.query(DATABASE_TABLE,  new String[] {PLAN, BENEFIT, AGE, PREMI, PREMI_REAS}, null, null, null, null, null);    			
    }
    public Cursor getRate(String plan, String age) 
    {
    	NumberFormat nf = NumberFormat.getInstance();
    	nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		
    	return db.rawQuery("SELECT PLAN, BENEFIT, AGE, PREMI, PREMI_REAS FROM MASTER_WELLWOMAN_RATE WHERE PLAN = '"+plan+"' AND CAST(AGE as integer) >= "+ String.valueOf(age)  +" LIMIT 1", null);
    }
}
