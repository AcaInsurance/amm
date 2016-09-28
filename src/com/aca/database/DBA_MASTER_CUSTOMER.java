package com.aca.database;

import com.aca.amm.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBA_MASTER_CUSTOMER {
    public static final String KEY_ROWID = "_id";
    
    public static final String KEY_FULLNAME = "FULLNAME";
    public static final String KEY_ID_NO = "ID_NO";
    public static final String KEY_DOB = "DOB";
    public static final String KEY_POB = "POB";
    public static final String KEY_ADDRESS = "ADDRESS";
    public static final String KEY_EMAIL = "EMAIL";
    public static final String KEY_PHONE_NO = "PHONE_NO";
    public static final String KEY_MOBILE_NO = "MOBILE_NO";
    public static final String KEY_RT_NO = "RT_NO";
    public static final String KEY_RW_NO = "RW_NO";
    public static final String KEY_KELURAHAN = "KELURAHAN";
    public static final String KEY_KECAMATAN = "KECAMATAN";
    public static final String KEY_POSCODE = "POSTCODE";

    public static final String KEY_OCCUPATION = "OCCUPATION";
    public static final String KEY_LOB = "LOB";
    public static final String KEY_CITY_PROVINCE = "CITY_PROVINCE";
    
    private static final String TAG = "DBA_MASTER_CUSTOMER";
    
    private static final String DATABASE_NAME = "AMM_VERSION_2";
    private static final String DATABASE_TABLE = "MASTER_CUSTOMER";

     static final String DATABASE_CREATE =
        "CREATE TABLE MASTER_CUSTOMER (_id INTEGER PRIMARY KEY, FULLNAME TEXT, MOBILE_NO TEXT, EMAIL TEXT, DOB TEXT, " +
        "POB TEXT, ID_NO TEXT, ADDRESS TEXT, RT_NO TEXT, RW_NO TEXT, KELURAHAN TEXT, KECAMATAN TEXT, POSTCODE TEXT, " +
        "PHONE_NO TEXT, CITY_PROVINCE NUMERIC, LOB NUMERIC, OCCUPATION NUMERIC);";
        
    private final Context context;    

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBA_MASTER_CUSTOMER(Context ctx) 
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }
        
    private static class DatabaseHelper extends SQLiteOpenHelper 
    {
        DatabaseHelper(Context context) 
        {
            super(context, DATABASE_NAME, null, Utility.getDatabaseVersion());
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
            db.execSQL("DROP TABLE IF EXISTS MASTER_CUSTOMER");
            onCreate(db);
            */
        }
    }    

    //---opens the database---
    public DBA_MASTER_CUSTOMER open() throws SQLException 
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
    public long insert(String custname, String id, String pob, String dob, String address, String email, 
    		String phoneno1, String mobileno, String rw, String rt, String kel, String kec, String poscode, 
    		long occupation, long lob, long city) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_FULLNAME, custname);
        initialValues.put(KEY_ID_NO, id);
        initialValues.put(KEY_DOB, dob);
        initialValues.put(KEY_POB, pob);
        initialValues.put(KEY_ADDRESS, address);
        initialValues.put(KEY_EMAIL, email);
        initialValues.put(KEY_PHONE_NO, phoneno1);
        initialValues.put(KEY_MOBILE_NO, mobileno);
        initialValues.put(KEY_RW_NO, rw);
        initialValues.put(KEY_RT_NO, rt);
        initialValues.put(KEY_KELURAHAN, kel);
        initialValues.put(KEY_KECAMATAN, kec);
        initialValues.put(KEY_POSCODE, poscode);

        initialValues.put(KEY_OCCUPATION, occupation);
        initialValues.put(KEY_LOB, lob);
        initialValues.put(KEY_CITY_PROVINCE, city);
        
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //---deletes a particular contact---
    public boolean delete(String rowId) 
    {
        return db.delete(DATABASE_TABLE, KEY_ROWID + "='" + rowId + "'", null) > 0;
    }
    
    //---Untuk Menampilkan Di List
    public Cursor getRows(String key) 
    {
        return db.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_FULLNAME, KEY_ID_NO, 
        		KEY_ADDRESS, KEY_PHONE_NO, KEY_MOBILE_NO}, KEY_FULLNAME + " like '%" + key + "%'", null, null, null, KEY_FULLNAME + " asc");
    }
    
    public Cursor getRowsBySearch(String key) 
    {
        return db.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_FULLNAME, KEY_ID_NO, KEY_ADDRESS, KEY_PHONE_NO, KEY_MOBILE_NO}, KEY_FULLNAME + " like '%" + key + "%'", null, null, null, KEY_FULLNAME + " Desc");
    }
    
    //untuk Synchronize
    public Cursor getRowsAll() 
    {
    	Cursor mCursor =
                db.query(DATABASE_TABLE, new String[] 
                		{KEY_ROWID, KEY_ID_NO, KEY_FULLNAME, KEY_ADDRESS, KEY_POB, KEY_DOB, KEY_PHONE_NO, KEY_MOBILE_NO, KEY_OCCUPATION, 
                		KEY_EMAIL, KEY_RT_NO, KEY_RW_NO, KEY_KELURAHAN, KEY_KECAMATAN, KEY_LOB, KEY_CITY_PROVINCE, KEY_ID_NO, KEY_POSCODE
                		}, null, null, null, null, KEY_ROWID);
        
        return mCursor;
    }

    //---retrieves a particular contact---
    public Cursor getRow(String rowId) throws SQLException 
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] 
                		{KEY_ROWID, KEY_ID_NO, KEY_FULLNAME, KEY_ADDRESS, KEY_POB, KEY_DOB, KEY_PHONE_NO, KEY_MOBILE_NO, KEY_OCCUPATION, 
                		KEY_EMAIL, KEY_RT_NO, KEY_RW_NO, KEY_KELURAHAN, KEY_KECAMATAN, KEY_LOB, KEY_CITY_PROVINCE, KEY_ID_NO, KEY_POSCODE
                		}, KEY_ROWID + "='" + rowId + "'", null,
                null, null, null, null);
        
        return mCursor;
    }
    
    
    //---updates a contact---
    public boolean update(String rowId, String custname, String id, String pob, String dob, String address, String email, 
    		String phoneno1, String mobileno, String rw, String rt, String kel, String kec, String poscode, 
    		long occupation, long lob, long city) 
    {
    	ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_FULLNAME, custname);
        initialValues.put(KEY_ID_NO, id);
        initialValues.put(KEY_DOB, dob);
        initialValues.put(KEY_POB, pob);
        initialValues.put(KEY_ADDRESS, address);
        initialValues.put(KEY_EMAIL, email);
        initialValues.put(KEY_PHONE_NO, phoneno1);
        initialValues.put(KEY_MOBILE_NO, mobileno);
        initialValues.put(KEY_RW_NO, rw);
        initialValues.put(KEY_RT_NO, rt);
        initialValues.put(KEY_KELURAHAN, kel);
        initialValues.put(KEY_KECAMATAN, kec);
        initialValues.put(KEY_POSCODE, poscode);

        initialValues.put(KEY_OCCUPATION, occupation);
        initialValues.put(KEY_LOB, lob);
        initialValues.put(KEY_CITY_PROVINCE, city);
        
        return db.update(DATABASE_TABLE, initialValues, KEY_ROWID + "='" + rowId + "'", null) > 0;
    }
    
   
}
