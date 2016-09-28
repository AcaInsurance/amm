package com.aca.database;

import com.aca.amm.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBA_PRODUCT_PA_AMANAH {
	public static final String PRODUCT_NAME = "PRODUCT_NAME"; 
	public static final String CUSTOMER_NAME = "CUSTOMER_NAME"; 
	public static final String _id = "_id";
	public static final String PRODUCT_MAIN_ID = "PRODUCT_MAIN_ID";
	public static final String NAMA_AHLIWARIS_1 = "NAMA_AHLIWARIS_1";
	public static final String HUB_AHLIWARIS_1 = "HUB_AHLIWARIS_1";
	public static final String ALAMAT_AHLIWARIS_1 = "ALAMAT_AHLIWARIS_1";
	public static final String NAMA_AHLIWARIS_2 = "NAMA_AHLIWARIS_2";
	public static final String HUB_AHLIWARIS_2 = "HUB_AHLIWARIS_2";
	public static final String ALAMAT_AHLIWARIS_2 = "ALAMAT_AHLIWARIS_2";
	public static final String NAMA_AHLIWARIS_3 = "NAMA_AHLIWARIS_3";
	public static final String HUB_AHLIWARIS_3 = "HUB_AHLIWARIS_3";
	public static final String ALAMAT_AHLIWARIS_3 = "ALAMAT_AHLIWARIS_3";
	public static final String NILAI_PERTANGGUNGAN = "NILAI_PERTANGGUNGAN";
	public static final String TGL_MULAI = "TGL_MULAI";
	public static final String TGL_AKHIR = "TGL_AKHIR";
	public static final String PLAN = "PLAN";
	public static final String PREMI = "PREMI";
	public static final String BIAYA_POLIS = "BIAYA_POLIS";
	public static final String TOTAL = "TOTAL";
    private static final String TAG = "DBA_PRODUCT_PA_AMANAH";
    
    private static final String DATABASE_NAME = "AMM_VERSION_2";
    private static final String DATABASE_TABLE = "PRODUCT_PA_AMANAH";

     static final String DATABASE_CREATE =
        "CREATE TABLE PRODUCT_PA_AMANAH (_id INTEGER PRIMARY KEY, PRODUCT_MAIN_ID NUMERIC, " +
        "NAMA_AHLIWARIS_1 TEXT, HUB_AHLIWARIS_1 TEXT, ALAMAT_AHLIWARIS_1 TEXT, " +
        "NAMA_AHLIWARIS_2 TEXT, HUB_AHLIWARIS_2 TEXT, ALAMAT_AHLIWARIS_2 TEXT, " +
        "NAMA_AHLIWARIS_3 TEXT, HUB_AHLIWARIS_3 TEXT, ALAMAT_AHLIWARIS_3 TEXT, " +
        "TGL_MULAI TEXT, TGL_AKHIR TEXT, NILAI_PERTANGGUNGAN NUMERIC, " +
        "PLAN NUMERIC, PREMI NUMERIC, BIAYA_POLIS NUMERIC, TOTAL NUMERIC, CUSTOMER_NAME TEXT, PRODUCT_NAME TEXT);";
        
    private final Context context;    

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBA_PRODUCT_PA_AMANAH(Context ctx) 
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
        	}*/
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
        {
        	/*
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS PRODUCT_PA_AMANAH");
            onCreate(db);*/
        }
    }    

    //---opens the database---
    public DBA_PRODUCT_PA_AMANAH open() throws SQLException 
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---    
    public void close() 
    {
        DBHelper.close();
    }
    
    public void clearData () {
    	this.open(); 

    	String sql = "_id not in ( SELECT MAX(_id) FROM " + DATABASE_TABLE + " GROUP BY product_main_id)";
    	db.delete(DATABASE_TABLE, sql, null );
    	this.close();
    }
    
    //---insert a contact into the database---
    public long initialInsert(long product_main_id, 
    		double nilaipertanggungan, 
    		String namaahliwaris1, String hubahliwaris1, String alamatahliwaris1, 
    		String namaahliwaris2, String hubahliwaris2, String alamatahliwaris2, 
    		String namaahliwaris3, String hubahliwaris3, String alamatahliwaris3, 
    		int plan, String tglmulai, String tglakhir,
    		double premi, double polis, double total, String product_name, String customer_name) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(PRODUCT_MAIN_ID, product_main_id);
        initialValues.put(NILAI_PERTANGGUNGAN, nilaipertanggungan);
        initialValues.put(NAMA_AHLIWARIS_1, namaahliwaris1);
        initialValues.put(HUB_AHLIWARIS_1, hubahliwaris1);
        initialValues.put(ALAMAT_AHLIWARIS_1, alamatahliwaris1);
        initialValues.put(NAMA_AHLIWARIS_2, namaahliwaris2);
        initialValues.put(HUB_AHLIWARIS_2, hubahliwaris2);
        initialValues.put(ALAMAT_AHLIWARIS_2, alamatahliwaris2);
        initialValues.put(NAMA_AHLIWARIS_3, namaahliwaris3);
        initialValues.put(HUB_AHLIWARIS_3, hubahliwaris3);
        initialValues.put(ALAMAT_AHLIWARIS_3, alamatahliwaris3);
        initialValues.put(TGL_MULAI, tglmulai);
        initialValues.put(TGL_AKHIR, tglakhir);
        initialValues.put(PLAN, plan);
        initialValues.put(PREMI, premi);
        initialValues.put(BIAYA_POLIS, polis);
        initialValues.put(TOTAL, total);
        initialValues.put(PRODUCT_NAME, product_name);
        initialValues.put(CUSTOMER_NAME, customer_name);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }
    
    public boolean nextInsert(long product_main_id,
    		double nilaipertanggungan, 
    		String namaahliwaris1, String hubahliwaris1, String alamatahliwaris1, 
    		String namaahliwaris2, String hubahliwaris2, String alamatahliwaris2, 
    		String namaahliwaris3, String hubahliwaris3, String alamatahliwaris3, 
    		int plan, String tglmulai, String tglakhir,
    		double premi, double polis, double total, String product_name, String customer_name) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(PRODUCT_MAIN_ID, product_main_id);
        initialValues.put(NILAI_PERTANGGUNGAN, nilaipertanggungan);
        initialValues.put(NAMA_AHLIWARIS_1, namaahliwaris1);
        initialValues.put(HUB_AHLIWARIS_1, hubahliwaris1);
        initialValues.put(ALAMAT_AHLIWARIS_1, alamatahliwaris1);
        initialValues.put(NAMA_AHLIWARIS_2, namaahliwaris2);
        initialValues.put(HUB_AHLIWARIS_2, hubahliwaris2);
        initialValues.put(ALAMAT_AHLIWARIS_2, alamatahliwaris2);
        initialValues.put(NAMA_AHLIWARIS_3, namaahliwaris3);
        initialValues.put(HUB_AHLIWARIS_3, hubahliwaris3);
        initialValues.put(ALAMAT_AHLIWARIS_3, alamatahliwaris3);
        initialValues.put(TGL_MULAI, tglmulai);
        initialValues.put(TGL_AKHIR, tglakhir);
        initialValues.put(PLAN, plan);
        initialValues.put(PREMI, premi);
        initialValues.put(BIAYA_POLIS, polis);
        initialValues.put(TOTAL, total);
        initialValues.put(PRODUCT_NAME, product_name);
        initialValues.put(CUSTOMER_NAME, customer_name);
        return db.update(DATABASE_TABLE, initialValues, PRODUCT_MAIN_ID + "=" + product_main_id, null) > 0;
    }

    //---deletes a particular contact---
    public boolean delete(long product_main_id) 
    {
        return db.delete(DATABASE_TABLE, PRODUCT_MAIN_ID + "=" + product_main_id, null) > 0;
    }
    
    public boolean deleteAll() 
    {
        return db.delete(DATABASE_TABLE, null, null) > 0;
    }

    //---retrieves all the contacts---
    public Cursor getRows() 
    {
        return db.query(DATABASE_TABLE, new String[] {
        		_id,
        		PRODUCT_MAIN_ID,
        		NILAI_PERTANGGUNGAN,
        		NAMA_AHLIWARIS_1,
        		HUB_AHLIWARIS_1,
        		ALAMAT_AHLIWARIS_1,
        		NAMA_AHLIWARIS_2,
        		HUB_AHLIWARIS_2,
        		ALAMAT_AHLIWARIS_2,
        		NAMA_AHLIWARIS_3,
        		HUB_AHLIWARIS_3,
        		ALAMAT_AHLIWARIS_3,
        		TGL_MULAI,
        		TGL_AKHIR,
        		PLAN,
        		PREMI,
        		BIAYA_POLIS,
        		TOTAL, PRODUCT_NAME, CUSTOMER_NAME}, null, null, null, null, null);
    }
    
    public Cursor getRow(long product_main_id) 
    {
    	return db.query(DATABASE_TABLE, new String[] {
    			_id,
    			PRODUCT_MAIN_ID,
    			NILAI_PERTANGGUNGAN,
    			NAMA_AHLIWARIS_1,
    			HUB_AHLIWARIS_1,
    			ALAMAT_AHLIWARIS_1,
    			NAMA_AHLIWARIS_2,
    			HUB_AHLIWARIS_2,
    			ALAMAT_AHLIWARIS_2,
    			NAMA_AHLIWARIS_3,
    			HUB_AHLIWARIS_3,
    			ALAMAT_AHLIWARIS_3,
    			TGL_MULAI,
    			TGL_AKHIR,
    			PLAN,
    			PREMI,
    			BIAYA_POLIS,
    			TOTAL,
    			PRODUCT_NAME, 
    			CUSTOMER_NAME}, 
    			PRODUCT_MAIN_ID + "=" + product_main_id, null, null, null, null);
    }
}

