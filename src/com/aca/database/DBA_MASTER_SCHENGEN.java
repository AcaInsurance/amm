package com.aca.database;

import com.aca.amm.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBA_MASTER_SCHENGEN {

	public static final String CODE = "CODE";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String FLAG = "FLAG";
    
    private static final String TAG = "DBA_MASTER_SCHENGEN";
    
    private static final String DATABASE_NAME = "AMM_VERSION_BIG";
    private static final String DATABASE_TABLE = "MASTER_SCHENGEN";

     static final String DATABASE_CREATE =
        "CREATE TABLE MASTER_SCHENGEN (CODE TEXT, DESCRIPTION TEXT, FLAG TEXT);";
        
    private final Context context;    

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBA_MASTER_SCHENGEN(Context ctx) 
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
            db.execSQL("DROP TABLE IF EXISTS MASTER_SCHENGEN");
            onCreate(db);
            */
        }
    }    

    //---opens the database---
    public DBA_MASTER_SCHENGEN open() throws SQLException 
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
    public long insert(String code, String description, String Flag) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(CODE, code);
        initialValues.put(DESCRIPTION, description);
        initialValues.put(FLAG, Flag);
        
        return db.insert(DATABASE_TABLE, null, initialValues);
    }
    
    public void insertSchengen () {
    	db.execSQL("INSERT INTO MASTER_SCHENGEN (CODE, DESCRIPTION, FLAG) SELECT CODE, DESCRIPTION, FLAG FROM MASTER_SCHENGEN");
    }

    //---deletes a particular contact---
    public boolean deleteAll() 
    {
        return db.delete(DATABASE_TABLE, null, null) > 0;
    }
    
    public Cursor getAll() 
    {
    	return db.query(DATABASE_TABLE,  new String[] {CODE, DESCRIPTION, FLAG}, null, null, null, null, DESCRIPTION + " asc");    			
    }
    
    public Cursor getAllByFlag(String Flag) 
    {
    	return db.query(DATABASE_TABLE,  new String[] {CODE, DESCRIPTION, FLAG}, FLAG + "=" + Flag, null, null, null, DESCRIPTION + " asc");    			
    }
}
