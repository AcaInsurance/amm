package com.aca.database;

import com.aca.amm.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBA_MASTER_TOKO_RATE {

	public static final String ID = "ID";
    public static final String OKUPASI_DESCRIPTION = "OKUPASI_DESCRIPTION";
    public static final String RATE = "RATE";
    
    private static final String TAG = "DBA_MASTER_TOKO_RATE";
    
    private static final String DATABASE_NAME = "AMM_VERSION_BIG";
    private static final String DATABASE_TABLE = "MASTER_TOKO_RATE";

     static final String DATABASE_CREATE =
        "CREATE TABLE MASTER_TOKO_RATE (ID NUMERIC, OKUPASI_DESCRIPTION TEXT, RATE NUMERIC);";
        
    private final Context context;    

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBA_MASTER_TOKO_RATE(Context ctx) 
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
    public DBA_MASTER_TOKO_RATE open() throws SQLException 
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
    public long insert(int id, String okupasi_description, double rate) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(ID, id);
        initialValues.put(OKUPASI_DESCRIPTION, okupasi_description);
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
    	return db.query(DATABASE_TABLE,  new String[] {ID, OKUPASI_DESCRIPTION, RATE}, null, null, null, null, null);    			
    }
    public Cursor getRate(String tokoType) 
    {
    	return db.rawQuery("SELECT ID, OKUPASI_DESCRIPTION, RATE FROM MASTER_TOKO_RATE" +
    			" WHERE id = ? LIMIT 1", new String[]{tokoType});
    	 
    }
}
