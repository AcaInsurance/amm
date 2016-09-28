package com.aca.database;

import com.aca.amm.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBA_MASTER_PRODUCT_SETTING {

	public static final String LOB = "LOB";
    public static final String PRODUCT_NAME = "PRODUCT_NAME";
    public static final String IS_RENEWABLE = "IS_RENEWABLE";
    public static final String MIN_TSI = "MIN_TSI";
    public static final String MAX_TSI = "MAX_TSI";
    public static final String MIN_PREMI = "MIN_PREMI";
    public static final String MAX_PREMI = "MAX_PREMI";
    public static final String IS_DISCOUNTABLE = "IS_DISCOUNTABLE";
    
    
    private static final String TAG = "DBA_MASTER_PRODUCT_SETTING";
    
    private static final String DATABASE_NAME = "AMM_VERSION_BIG";
    private static final String DATABASE_TABLE = "MASTER_PRODUCT_SETTING";

     static final String DATABASE_CREATE = "CREATE TABLE MASTER_PRODUCT_SETTING (" +
     		"LOB TEXT, " +
     		"PRODUCT_NAME TEXT," +
     		" IS_RENEWABLE TEXT, " +
     		"MIN_TSI NUMERIC," +
     		" MAX_TSI NUMERIC," +
     		" MIN_PREMI NUMERIC," +
     		" MAX_PREMI NUMERIC," +
     		" IS_DISCOUNTABLE TEXT);";
        
     
    private final Context context;    

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBA_MASTER_PRODUCT_SETTING(Context ctx) 
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
            db.execSQL("DROP TABLE IF EXISTS MASTER_LOB");
            onCreate(db);
            */
        }
    }    

    //---opens the database---
    public DBA_MASTER_PRODUCT_SETTING open() throws SQLException 
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
    public long insert(String lob, String productName, String isRenewable, double minTSI, double maxTSI, double minPremi, double maxPremi, String isDiscountable) 
    {
        ContentValues initialValues = new ContentValues();

        initialValues.put(LOB, lob);
        initialValues.put(PRODUCT_NAME, productName);
        initialValues.put(IS_RENEWABLE, isRenewable);
        initialValues.put(MIN_TSI, minTSI);
        initialValues.put(MAX_TSI, maxTSI);
        initialValues.put(MIN_PREMI, minPremi);
        initialValues.put(MAX_PREMI, maxPremi);
        initialValues.put(IS_DISCOUNTABLE, isDiscountable);
        
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //---deletes a particular contact---
    public boolean deleteAll() 
    {
        return db.delete(DATABASE_TABLE, null, null) > 0;
    }
    
    public Cursor getRow(String lob) 
    {
    	return db.query(DATABASE_TABLE,  new String[] {
    			LOB,		
    			PRODUCT_NAME,
    			IS_RENEWABLE,
    			MIN_TSI,	//3
    			MAX_TSI,	//4
    			MIN_PREMI,
    			MAX_PREMI,
    			IS_DISCOUNTABLE}, LOB + "='" + lob + "'", null, null, null, null);    			
    }
    
    public Cursor getAll() 
    {
    	return db.query(DATABASE_TABLE,  new String[] {
    			LOB,
    			PRODUCT_NAME,
    			IS_RENEWABLE,
    			MIN_TSI,
    			MAX_TSI,
    			MIN_PREMI,
    			MAX_PREMI,
    			IS_DISCOUNTABLE}, null, null, null, null, null);    			
    }
}
