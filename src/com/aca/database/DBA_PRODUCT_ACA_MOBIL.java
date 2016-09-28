package com.aca.database;


import com.aca.amm.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBA_PRODUCT_ACA_MOBIL {
	public static final String PRODUCT_NAME = "PRODUCT_NAME"; 
	public static final String CUSTOMER_NAME = "CUSTOMER_NAME"; 
	public static final String PRODUCT_MAIN_ID = "PRODUCT_MAIN_ID"; 
	public static final String _id = "_id"; 
	public static final String PRODUCT_MAKE = "PRODUCT_MAKE"; 
	public static final String PRODUCT_TYPE = "PRODUCT_TYPE"; 
	public static final String YEAR = "YEAR"; 
	public static final String NOPOL_1 = "NOPOL_1"; 
	public static final String NOPOL_2 = "NOPOL_2"; 
	public static final String NOPOL_3 = "NOPOL_3"; 
	public static final String COLOR = "COLOR"; 
	public static final String CHASSIS_NO = "CHASSIS_NO"; 
	public static final String MACHINE_NO = "MACHINE_NO"; 
	public static final String PERLENGKAPAN = "PERLENGKAPAN"; 
	public static final String SEAT_NUMBER = "SEAT_NUMBER"; 
	public static final String JANGKA_WAKTU_EFF = "JANGKA_WAKTU_EFF"; 
	public static final String JANGKA_WAKTU_EXP = "JANGKA_WAKTU_EXP"; 
	public static final String NILAI_PERTANGGUNGAN = "NILAI_PERTANGGUNGAN"; 
	public static final String NILAI_PERTANGGUNGAN_PERLENGKAPAN = "NILAI_PERTANGGUNGAN_PERLENGKAPAN"; 
	public static final String ACT_OF_GOD = "ACT_OF_GOD"; 
	public static final String TJH_PIHAK_KETIGA = "TJH_PIHAK_KETIGA"; 
	public static final String RATE = "RATE"; 
	public static final String PREMI = "PREMI"; 
	public static final String POLIS = "POLIS"; 
	public static final String MATERAI = "MATERAI"; 
	public static final String TOTAL = "TOTAL";
	
	public static final String PRODUCT_MODEL = "PRODUCT_MODEL"; 
	public static final String PERLENGKAPAN_TYPE = "PERLENGKAPAN_TYPE"; 
	public static final String PRODUCT_MAKE_DESC = "PRODUCT_MAKE_DESC"; 
	public static final String PRODUCT_TYPE_DESC = "PRODUCT_TYPE_DESC";
	
    private static final String TAG = "DBA_PRODUCT_ACA_MOBIL";
    
    private static final String DATABASE_NAME = "AMM_VERSION_2";
    private static final String DATABASE_TABLE = "PRODUCT_ACA_MOBIL";

     static final String DATABASE_CREATE =
        "CREATE TABLE PRODUCT_ACA_MOBIL (PRODUCT_MAIN_ID NUMERIC, _id INTEGER PRIMARY KEY, PRODUCT_MAKE NUMERIC, PRODUCT_TYPE NUMERIC, " +
        "YEAR NUMERIC, NOPOL_1 TEXT, NOPOL_2 TEXT, NOPOL_3 TEXT, COLOR TEXT, CHASSIS_NO TEXT, MACHINE_NO TEXT, PERLENGKAPAN TEXT, " +
        "SEAT_NUMBER NUMERIC, JANGKA_WAKTU_EFF TEXT, JANGKA_WAKTU_EXP TEXT, NILAI_PERTANGGUNGAN NUMERIC, NILAI_PERTANGGUNGAN_PERLENGKAPAN NUMERIC, " +
        "ACT_OF_GOD TEXT, TJH_PIHAK_KETIGA NUMERIC, RATE NUMERIC, PREMI NUMERIC, POLIS NUMERIC, MATERAI NUMERIC, TOTAL NUMERIC, " +
        "CUSTOMER_NAME TEXT, PRODUCT_NAME TEXT, PRODUCT_MODEL TEXT, PERLENGKAPAN_TYPE TEXT, PRODUCT_MAKE_DESC TEXT, PRODUCT_TYPE_DESC TEXT);";
        
    private final Context context;    

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBA_PRODUCT_ACA_MOBIL(Context ctx) 
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
            db.execSQL("DROP TABLE IF EXISTS PRODUCT_ACA_MOBIL");
            onCreate(db);
            */
        }
    }    

    //---opens the database---
    public DBA_PRODUCT_ACA_MOBIL open() throws SQLException 
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
    public long initialInsert(long product_main_id,  String make, String type, String model, String year, String nopol1, String nopol2, String nopol3, String color
    		, String chassis_no, String machine_no, String perlengkapan_type, String perlengkapan, int seat, String jangka_waktu_eff, String jangka_waktu_exp, double nilai_pertanggungan
    		, double nilai_perlengkapan, String act_of_god, double tjh, double rate, double premi, double materai, double polis, double total, 
    		 String make_desc, String type_desc, String product_name, String customer_name) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(PRODUCT_MAIN_ID, product_main_id); 
        initialValues.put(PRODUCT_MAKE, make); 
        initialValues.put(PRODUCT_TYPE, type); 
        initialValues.put(YEAR, year); 
        initialValues.put(NOPOL_1, nopol1); 
        initialValues.put(NOPOL_2, nopol2); 
        initialValues.put(NOPOL_3, nopol3); 
        initialValues.put(COLOR, color); 
        initialValues.put(CHASSIS_NO, chassis_no); 
        initialValues.put(MACHINE_NO, machine_no); 
        initialValues.put(PERLENGKAPAN, perlengkapan); 
        initialValues.put(SEAT_NUMBER, seat); 
        initialValues.put(JANGKA_WAKTU_EFF, jangka_waktu_eff); 
        initialValues.put(JANGKA_WAKTU_EXP, jangka_waktu_exp);
        initialValues.put(NILAI_PERTANGGUNGAN, nilai_pertanggungan); 
        initialValues.put(NILAI_PERTANGGUNGAN_PERLENGKAPAN, nilai_perlengkapan); 
        initialValues.put(ACT_OF_GOD, act_of_god); 
        initialValues.put(TJH_PIHAK_KETIGA, tjh); 
        initialValues.put(RATE, rate);
        initialValues.put(PREMI, premi);
        initialValues.put(POLIS, polis);
        initialValues.put(MATERAI, materai);
        initialValues.put(TOTAL, total);
        initialValues.put(PRODUCT_NAME, "ACAMOBIL");
        initialValues.put(CUSTOMER_NAME, customer_name);
        
        initialValues.put(PRODUCT_MODEL, model); 
        initialValues.put(PERLENGKAPAN_TYPE, perlengkapan_type); 
        initialValues.put(PRODUCT_MAKE_DESC, make_desc); 
        initialValues.put(PRODUCT_TYPE_DESC, type_desc); 
        
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    public boolean nextInsert(long product_main_id, String make, String type, String model, String year, String nopol1, String nopol2, String nopol3, String color
    		, String chassis_no, String machine_no, String perlengkapan_type, String perlengkapan, int seat, String jangka_waktu_eff, String jangka_waktu_exp, double nilai_pertanggungan
    		, double nilai_perlengkapan, String act_of_god, double tjh, double rate, double premi, double materai
    		, double polis, double total, 
    		String make_desc, String type_desc,String product_name, String customer_name )  
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(PRODUCT_MAKE, make); 
        initialValues.put(PRODUCT_TYPE, type); 
        initialValues.put(YEAR, year); 
        initialValues.put(NOPOL_1, nopol1); 
        initialValues.put(NOPOL_2, nopol2); 
        initialValues.put(NOPOL_3, nopol3); 
        initialValues.put(COLOR, color); 
        initialValues.put(CHASSIS_NO, chassis_no); 
        initialValues.put(MACHINE_NO, machine_no); 
        initialValues.put(PERLENGKAPAN, perlengkapan); 
        initialValues.put(SEAT_NUMBER, seat); 
        initialValues.put(JANGKA_WAKTU_EFF, jangka_waktu_eff); 
        initialValues.put(JANGKA_WAKTU_EXP, jangka_waktu_exp);
        initialValues.put(NILAI_PERTANGGUNGAN, nilai_pertanggungan); 
        initialValues.put(NILAI_PERTANGGUNGAN_PERLENGKAPAN, nilai_perlengkapan); 
        initialValues.put(ACT_OF_GOD, act_of_god); 
        initialValues.put(TJH_PIHAK_KETIGA, tjh); 
        initialValues.put(RATE, rate);
        initialValues.put(PREMI, premi);
        initialValues.put(POLIS, polis);
        initialValues.put(MATERAI, materai);
        initialValues.put(TOTAL, total);
        initialValues.put(PRODUCT_NAME, product_name);
        initialValues.put(CUSTOMER_NAME, customer_name);
        
        initialValues.put(PRODUCT_MODEL, model); 
        initialValues.put(PERLENGKAPAN_TYPE, perlengkapan_type);
        initialValues.put(PRODUCT_MAKE_DESC, make_desc); 
        initialValues.put(PRODUCT_TYPE_DESC, type_desc); 
        
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
        		PRODUCT_MAIN_ID , 
        		_id , 
        		PRODUCT_MAKE , 
        		PRODUCT_TYPE , 
        		YEAR , 
        		NOPOL_1 , 
        		NOPOL_2 , 
        		NOPOL_3 , 
        		COLOR , 
        		CHASSIS_NO , 
        		MACHINE_NO , 
        		PERLENGKAPAN ,
        		SEAT_NUMBER , 
        		JANGKA_WAKTU_EFF ,
        		JANGKA_WAKTU_EXP ,
        		NILAI_PERTANGGUNGAN ,
        		NILAI_PERTANGGUNGAN_PERLENGKAPAN ,
        		ACT_OF_GOD ,
        		TJH_PIHAK_KETIGA , 
        		RATE , 
        		PREMI ,
        		POLIS ,
        		MATERAI ,
        		TOTAL,
        		PRODUCT_NAME,
        		CUSTOMER_NAME,
        		PRODUCT_MODEL,
        		PERLENGKAPAN_TYPE,
        		PRODUCT_MAKE_DESC, 
        		PRODUCT_TYPE_DESC}, null, null, null, null, null);
    }
    
    public Cursor getRow(long product_main_id) 
    {
    	return db.query(DATABASE_TABLE, new String[] {
    			PRODUCT_MAIN_ID , 
        		_id , 
        		PRODUCT_MAKE , 
        		PRODUCT_TYPE , 
        		YEAR , 
        		NOPOL_1 , 
        		NOPOL_2 , 
        		NOPOL_3 , 
        		COLOR , 
        		CHASSIS_NO , 
        		MACHINE_NO , 
        		PERLENGKAPAN ,
        		SEAT_NUMBER , 
        		JANGKA_WAKTU_EFF ,
        		JANGKA_WAKTU_EXP ,
        		NILAI_PERTANGGUNGAN ,
        		NILAI_PERTANGGUNGAN_PERLENGKAPAN ,
        		ACT_OF_GOD ,
        		TJH_PIHAK_KETIGA , 
        		RATE , 
        		PREMI ,
        		POLIS ,
        		MATERAI ,
        		TOTAL,
        		PRODUCT_NAME,
        		CUSTOMER_NAME,
        		PRODUCT_MODEL,
        		PERLENGKAPAN_TYPE,
        		PRODUCT_MAKE_DESC , 
        		PRODUCT_TYPE_DESC}, 
        		PRODUCT_MAIN_ID + "=" + product_main_id, null, null, null, null);
    }
}

