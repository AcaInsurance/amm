package com.aca.database;

import com.aca.amm.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBA_MASTER_KONVE_TPL{

	public static final String TPL = "TPL";
    public static final String PREMI = "PREMI"; 
     
    private static final String DATABASE_NAME = "AMM_VERSION_BIG";
    private static final String DATABASE_TABLE = "MASTER_KONVE_TPL";

     static final String DATABASE_CREATE =
        "CREATE TABLE MASTER_KONVE_TPL (TPL TEXT, PREMI TEXT);";
        
    private final Context context;    

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBA_MASTER_KONVE_TPL(Context ctx) 
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
    public DBA_MASTER_KONVE_TPL open() throws SQLException 
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
    public long insert(String tpl, String premi) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(TPL, tpl);
        initialValues.put(PREMI, premi); 
        
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //---deletes a particular contact---
    public boolean deleteAll() 
    {
        return db.delete(DATABASE_TABLE, null, null) > 0;
    }
    
    public Cursor getByTPL(String tpl) 
    {
    	return db.query(DATABASE_TABLE,  new String[] {
    			TPL, 
    			PREMI }, "TPL = '" + tpl + "'", null, null, null, null);    			
    } 
    
    public Cursor getAll() 
    {
    	return db.query(DATABASE_TABLE,  new String[] {
    			TPL, //0
    			PREMI //1
			}, null, null, null, null, null);    			
    } 
}
