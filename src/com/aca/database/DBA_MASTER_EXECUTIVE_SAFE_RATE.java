package com.aca.database;

import com.aca.amm.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBA_MASTER_EXECUTIVE_SAFE_RATE {

	public static final String TYPE = "TYPE";
    public static final String PLAN = "PLAN";
    public static final String AGE = "AGE";
    public static final String PREMI = "PREMI";
    
    private static final String TAG = "DBA_MASTER_EXECUTIVE_SAFE_RATE";
    
    private static final String DATABASE_NAME = "AMM_VERSION_BIG";
    private static final String DATABASE_TABLE = "MASTER_EXECUTIVE_SAFE_RATE";

     static final String DATABASE_CREATE =
        "CREATE TABLE MASTER_EXECUTIVE_SAFE_RATE (TYPE NUMERIC, PLAN NUMERIC, AGE NUMERIC, PREMI NUMERIC);";
        
    private final Context context;    

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBA_MASTER_EXECUTIVE_SAFE_RATE(Context ctx) 
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
    public DBA_MASTER_EXECUTIVE_SAFE_RATE open() throws SQLException 
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
    public long insert(int type, int plan, int age, double premi) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(TYPE, type);
        initialValues.put(PLAN, plan);
        initialValues.put(AGE, age);
        initialValues.put(PREMI, premi);
        
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //---deletes a particular contact---
    public boolean deleteAll() 
    {
        return db.delete(DATABASE_TABLE, null, null) > 0;
    }
    
    public Cursor getAll() 
    {
    	return db.query(DATABASE_TABLE,  new String[] {TYPE, PLAN, AGE, PREMI}, null, null, null, null, null);    			
    }
    public Cursor getRate(String type, String plan, String age) 
    {
    	return db.rawQuery("SELECT TYPE, PLAN, AGE, PREMI FROM MASTER_EXECUTIVE_SAFE_RATE " +
    			" WHERE TYPE = ? AND" +
    			"		PLAN = ? AND " +
    			"		AGE = ? " +
    			"		LIMIT 1", new String[]{type, plan, age});
    }
}
