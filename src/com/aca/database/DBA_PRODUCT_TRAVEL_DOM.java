package com.aca.database;

import com.aca.amm.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBA_PRODUCT_TRAVEL_DOM {
	public static final String PRODUCT_NAME = "PRODUCT_NAME"; 
	public static final String CUSTOMER_NAME = "CUSTOMER_NAME"; 
	public static final String _id = "_id"; 
	public static final String PRODUCT_MAIN_ID = "PRODUCT_MAIN_ID";
	public static final String TUJUAN_PERJALANAN = "TUJUAN_PERJALANAN";
	public static final String KOTA_TUJUAN = "KOTA_TUJUAN";
	public static final String AHLI_WARIS = "AHLI_WARIS";
	public static final String HUBUNGAN = "HUBUNGAN";
	public static final String TGL_KEBERANGKATAN = "TGL_KEBERANGKATAN";
	public static final String TGL_KEMBALI = "TGL_KEMBALI";
	public static final String PLAN = "PLAN";
	public static final String JUMLAH_HARI = "JUMLAH_HARI";
	public static final String TAMBAHAN_PER_MINGGU = "TAMBAHAN_PER_MINGGU";
	public static final String PREMI = "PREMI";
	public static final String BIAYA_POLIS = "BIAYA_POLIS";
	public static final String TOTAL = "TOTAL";
    
	public static final String CCOD 					= "CCOD";
	public static final String DCOD 					= "DCOD";
	public static final String PREMIDAYS 				= "PREMIDAYS";
	public static final String PREMIWEEKS 				= "PREMIWEEKS";
	public static final String MAXBENEFIT 				= "MAXBENEFIT";
	public static final String TOTALDAYS 				= "TOTALDAYS";
	public static final String TOTALWEEKS 				= "TOTALWEEKS";
	
	private static final String TAG = "DBA_PRODUCT_TRAVEL_DOM";
    
    
    
    private static final String DATABASE_NAME = "AMM_VERSION_2";
    private static final String DATABASE_TABLE = "PRODUCT_TRAVEL_DOM";

     static final String DATABASE_CREATE =
        "CREATE TABLE PRODUCT_TRAVEL_DOM (_id INTEGER PRIMARY KEY, PRODUCT_MAIN_ID NUMERIC, TUJUAN_PERJALANAN NUMERIC, " +
        "KOTA_TUJUAN TEXT, AHLI_WARIS TEXT, HUBUNGAN TEXT, TGL_KEBERANGKATAN TEXT, TGL_KEMBALI TEXT, PLAN NUMERIC, " +
        "JUMLAH_HARI NUMERIC, TAMBAHAN_PER_MINGGU NUMERIC, PREMI NUMERIC," +
        " BIAYA_POLIS NUMERIC, TOTAL NUMERIC, CUSTOMER_NAME TEXT, PRODUCT_NAME TEXT," +
        "CCOD NUMERIC," +
        "DCOD NUMERIC," +
        "PREMIDAYS NUMERIC," +
        "PREMIWEEKS NUMERIC," +
        "MAXBENEFIT NUMERIC," +
        "TOTALDAYS NUMERIC," +
        "TOTALWEEKS NUMERIC);";
        
    private final Context context;    

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBA_PRODUCT_TRAVEL_DOM(Context ctx) 
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
            db.execSQL("DROP TABLE IF EXISTS PRODUCT_TRAVEL_DOM");
            onCreate(db);*/
        }
    }    

    //---opens the database---
    public DBA_PRODUCT_TRAVEL_DOM open() throws SQLException 
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
    		String tujuan, String kotatujuan, int plan,
    		String ahliwaris, String hubungan, String tglberangkat, String tglkembali,
    		double jumlahhari, double tambahan,
    		double premi, double polis, double total, String product_name, String customer_name,
    		double ccod,
    		double dcod,
    		double premidays,
    		double premiweeks,
    		double maxbenefit,
    		int totaldays,
    		int totalweeks) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(PRODUCT_MAIN_ID, product_main_id);
        initialValues.put(TUJUAN_PERJALANAN, tujuan);
        initialValues.put(AHLI_WARIS, ahliwaris);
        initialValues.put(HUBUNGAN, hubungan);
        initialValues.put(TGL_KEBERANGKATAN, tglberangkat);
        initialValues.put(TGL_KEMBALI, tglkembali);
        initialValues.put(PLAN, plan);
        initialValues.put(JUMLAH_HARI, jumlahhari);
        initialValues.put(TAMBAHAN_PER_MINGGU, tambahan);
        initialValues.put(KOTA_TUJUAN, kotatujuan);
        initialValues.put(PREMI, premi);
        initialValues.put(BIAYA_POLIS, polis);
        initialValues.put(TOTAL, total);
        initialValues.put(PRODUCT_NAME, product_name);
        initialValues.put(CUSTOMER_NAME, customer_name);
        initialValues.put(CCOD, ccod);
        initialValues.put(DCOD, dcod);
        initialValues.put(PREMIDAYS, premidays);
        
        initialValues.put(PREMIWEEKS, premiweeks);
        initialValues.put(MAXBENEFIT, maxbenefit);
        initialValues.put(TOTALDAYS, totaldays);
        initialValues.put(TOTALWEEKS, totalweeks);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }
    
    public boolean nextInsert(long product_main_id,
    		String tujuan, String kotatujuan, int plan,
    		String ahliwaris, String hubungan, String tglberangkat, String tglkembali,
    		double jumlahhari, double tambahan,
    		double premi, double polis, double total, String product_name, String customer_name,
    		double ccod,
    		double dcod,
    		double premidays,
    		double premiweeks,
    		double maxbenefit,
    		int totaldays,
    		int totalweeks)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(PRODUCT_MAIN_ID, product_main_id);
        initialValues.put(TUJUAN_PERJALANAN, tujuan);
        initialValues.put(AHLI_WARIS, ahliwaris);
        initialValues.put(HUBUNGAN, hubungan);
        initialValues.put(TGL_KEBERANGKATAN, tglberangkat);
        initialValues.put(TGL_KEMBALI, tglkembali);
        initialValues.put(PLAN, plan);
        initialValues.put(JUMLAH_HARI, jumlahhari);
        initialValues.put(TAMBAHAN_PER_MINGGU, tambahan);
        initialValues.put(KOTA_TUJUAN, kotatujuan);
        initialValues.put(PREMI, premi);
        initialValues.put(BIAYA_POLIS, polis);
        initialValues.put(TOTAL, total);
        initialValues.put(PRODUCT_NAME, product_name);
        initialValues.put(CUSTOMER_NAME, customer_name);
        initialValues.put(CCOD, ccod);
        initialValues.put(DCOD, dcod);
        initialValues.put(PREMIDAYS, premidays);
        
        initialValues.put(PREMIWEEKS, premiweeks);
        initialValues.put(MAXBENEFIT, maxbenefit);
        initialValues.put(TOTALDAYS, totaldays);
        initialValues.put(TOTALWEEKS, totalweeks);
        
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
        		TUJUAN_PERJALANAN,
        		KOTA_TUJUAN,
        		AHLI_WARIS,
        		HUBUNGAN,
        		TGL_KEBERANGKATAN,
        		TGL_KEMBALI,
        		PLAN,
        		JUMLAH_HARI,
        		TAMBAHAN_PER_MINGGU,
        		PREMI,
        		BIAYA_POLIS,
        		TOTAL, PRODUCT_NAME, CUSTOMER_NAME,
        		CCOD ,
        		DCOD ,
        		PREMIDAYS ,
        		PREMIWEEKS,
        		MAXBENEFIT,
        		TOTALDAYS ,
        		TOTALWEEKS}, null, null, null, null, null);
    }
    
    public Cursor getRow(long product_main_id) 
    {
    	return db.query(DATABASE_TABLE, new String[] {
    			_id, 
    			PRODUCT_MAIN_ID,
    			TUJUAN_PERJALANAN,
    			KOTA_TUJUAN,
    			AHLI_WARIS,
    			HUBUNGAN,
    			TGL_KEBERANGKATAN,
    			TGL_KEMBALI,
    			PLAN,
    			JUMLAH_HARI,
    			TAMBAHAN_PER_MINGGU,
    			PREMI,
    			BIAYA_POLIS,
    			TOTAL, 
    			PRODUCT_NAME, 
    			CUSTOMER_NAME,
    			CCOD ,
        		DCOD ,
        		PREMIDAYS ,
        		PREMIWEEKS,
        		MAXBENEFIT,
        		TOTALDAYS ,
        		TOTALWEEKS}, 
    			PRODUCT_MAIN_ID + "=" + product_main_id, null, null, null, null);
    }
}

