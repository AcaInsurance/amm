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

public class DBA_MASTER_KONVE_PA{

	public static final String PA = "PA";
    public static final String PREMI = "PREMI"; 
     
    private static final String DATABASE_NAME = "AMM_VERSION_BIG";
    private static final String DATABASE_TABLE = "MASTER_KONVE_PA";

     static final String DATABASE_CREATE =
        "CREATE TABLE MASTER_KONVE_PA (PA TEXT, PREMI TEXT);";
     
        
    private final Context context;    

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBA_MASTER_KONVE_PA(Context ctx) 
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
    public DBA_MASTER_KONVE_PA open() throws SQLException 
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
    public long insert(String pa, String premi) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(PA, pa);
        initialValues.put(PREMI, premi); 
        
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //---deletes a particular contact---
    public boolean deleteAll() 
    {
        return db.delete(DATABASE_TABLE, null, null) > 0;
    }
    
    public Cursor getByPA(String pa) 
    {
    	return db.query(DATABASE_TABLE,  new String[] {
    			PA, 
    			PREMI }, "PA = " + pa + "'", null, null, null, null);    			
    } 
    
    public Cursor getAll() 
    {
    	return db.query(DATABASE_TABLE,  new String[] {
    			PA, 
    			PREMI }, null, null, null, null, null);    			
    } 
}
