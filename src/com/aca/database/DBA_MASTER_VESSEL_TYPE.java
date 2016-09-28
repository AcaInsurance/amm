package com.aca.database;

import com.aca.amm.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBA_MASTER_VESSEL_TYPE {

	public static final String _id = "_id";
    public static final String STATUS = "STATUS";
    public static final String CODE = "CODE";
    public static final String TYPE = "TYPE";
    public static final String ABBRV = "ABBRV";
    public static final String DESC1 = "DESC1";
    public static final String DESC2 = "DESC2";
    
    private static final String TAG = "DBA_MASTER_VESSEL_TYPE";
    
    private static final String DATABASE_NAME = "AMM_VERSION_BIG";
    private static final String DATABASE_TABLE = "MASTER_VESSEL_TYPE";

     static final String DATABASE_CREATE = 
    		"CREATE TABLE MASTER_VESSEL_TYPE (" +
    					"_id INTEGER PRIMARY KEY," +
			    		" STATUS TEXT," +
			    		" CODE TEXT," +
			    		" TYPE TEXT," +
			    		" ABBRV TEXT," +
			    		" DESC1 TEXT," +
			    		" DESC2 TEXT);";
        
    private final Context context;    

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBA_MASTER_VESSEL_TYPE(Context ctx) 
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
            db.execSQL("DROP TABLE IF EXISTS MASTER_CITY_PROVINCE");
            onCreate(db);
            */
        }
    }    

    //---opens the database---
    public DBA_MASTER_VESSEL_TYPE open() throws SQLException 
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
    public long insert(String status,
			    		String code,
			    		String Type,
			    		String Abbrv,
			    		String Desc1,
			    		String Desc2   		
			    		) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(STATUS, status);
        initialValues.put(CODE, code);
        initialValues.put(TYPE, Type);
        initialValues.put(ABBRV, Abbrv);
        initialValues.put(DESC1, Desc1);
        initialValues.put(DESC2, Desc2);
        
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //---deletes a particular contact---
    public boolean deleteAll() 
    {
        return db.delete(DATABASE_TABLE, null, null) > 0;
    }
    
    public Cursor getByCode(String code) 
    {
    	return db.query(DATABASE_TABLE,  new String[] {
    			STATUS,	//0
	    		CODE,	//1
	    		TYPE,	//2
	    		ABBRV,	//3
	    		DESC1,	//4
	    		DESC2  	//5
    		}, CODE + " = '" + code + "'", null, null, null, null);    			
    }
    
    public Cursor getAll() 
    {
    	return db.query(DATABASE_TABLE,  new String[] {
    			STATUS,	//0
	    		CODE,	//1
	    		TYPE,	//2
	    		ABBRV,	//3
	    		DESC1,	//4
	    		DESC2  	//5
    		}, null, null, null, null, null);    			
    }
}
