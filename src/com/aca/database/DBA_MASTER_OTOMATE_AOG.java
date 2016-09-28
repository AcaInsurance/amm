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

public class DBA_MASTER_OTOMATE_AOG{

	public static final String WILAYAH = "WILAYAH";
    public static final String RATE = "RATE"; 
     
    private static final String DATABASE_NAME = "AMM_VERSION_BIG";
    private static final String DATABASE_TABLE = "MASTER_OTOMATE_AOG";

     static final String DATABASE_CREATE =
        "CREATE TABLE MASTER_OTOMATE_AOG (WILAYAH TEXT, RATE TEXT);";
     
        
    private final Context context;    

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBA_MASTER_OTOMATE_AOG(Context ctx) 
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
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
        { 
        }
    }    

    //---opens the database---
    public DBA_MASTER_OTOMATE_AOG open() throws SQLException 
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
    public long insert(String wilayah, String rate) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(WILAYAH, wilayah);
        initialValues.put(RATE, rate); 
        
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //---deletes a particular contact---
    public boolean deleteAll() 
    {
        return db.delete(DATABASE_TABLE, null, null) > 0;
    }
    
    public Cursor getByWilayah(String wilayah) 
    {
    	return db.query(DATABASE_TABLE,  new String[] {
    			WILAYAH, 
    			RATE }, "WILAYAH = '" + wilayah + "'", null, null, null, null);    			
    } 
    
    public Cursor getAll() 
    {
    	return db.query(DATABASE_TABLE,  new String[] {
    			WILAYAH, 
    			RATE }, null, null, null, null, null);    			
    } 
}