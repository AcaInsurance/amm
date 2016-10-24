package com.aca.database;

import java.util.ArrayList;

import com.aca.amm.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBA_TABLE_CREATE_ALL   {
		
	private static final String DATABASE_NAME = "AMM_VERSION_2";

    private final Context context;    

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBA_TABLE_CREATE_ALL(Context ctx) 
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
        	try {
//        	 

        		db.execSQL(DBA_MASTER_AGENT.DATABASE_CREATE);	
        		db.execSQL(DBA_MASTER_CUSTOMER.DATABASE_CREATE);	
        		db.execSQL(DBA_MASTER_DISC_COMM.DATABASE_CREATE);	 
        		
        		db.execSQL(DBA_PRODUCT_ACA_MOBIL.DATABASE_CREATE);	
        		db.execSQL(DBA_PRODUCT_ASRI_SYARIAH.DATABASE_CREATE);	
        		db.execSQL(DBA_PRODUCT_ASRI.DATABASE_CREATE);	
        		db.execSQL(DBA_PRODUCT_CARGO.DATABASE_CREATE);	
        		db.execSQL(DBA_PRODUCT_CONVENSIONAL.DATABASE_CREATE);	
        		db.execSQL(DBA_PRODUCT_DNO.DATABASE_CREATE);	
        		db.execSQL(DBA_PRODUCT_EXECUTIVE_SAFE.DATABASE_CREATE);	
        		db.execSQL(DBA_PRODUCT_MAIN.DATABASE_CREATE);	
        		db.execSQL(DBA_PRODUCT_MEDISAFE.DATABASE_CREATE);	
        		db.execSQL(DBA_PRODUCT_OTOMATE_SYARIAH.DATABASE_CREATE);	
        		db.execSQL(DBA_PRODUCT_OTOMATE.DATABASE_CREATE);	
        		db.execSQL(DBA_PRODUCT_PA_AMANAH.DATABASE_CREATE);	
        		db.execSQL(DBA_PRODUCT_PHOTO.DATABASE_CREATE);	
        		db.execSQL(DBA_PRODUCT_TOKO.DATABASE_CREATE);	
        		db.execSQL(DBA_PRODUCT_TRAVEL_DOM.DATABASE_CREATE);	
        		db.execSQL(DBA_PRODUCT_TRAVEL_SAFE.DATABASE_CREATE);	
        		db.execSQL(DBA_PRODUCT_WELLWOMAN.DATABASE_CREATE);	
        	
        		db.execSQL(DBA_INSURED_GROUP.DATABASE_CREATE);
        		

    			Log.d("DBA_TEST", "ON CREATE sukses all");
        	} catch (SQLException e) {
        		e.printStackTrace();
        	}
        }
        

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub

			if (oldVersion < 2) { 
				String sqlString = "ALTER TABLE PRODUCT_MAIN ADD COLUMN IS_COMPLETE_PHOTO TEXT";
				db.execSQL(sqlString);
				Log.d("DBA UPGRADE", "ON UPGRADE PRODUCT MAIN ADD IS COMPLETE PHOTO CLEAR");
			}
			if (oldVersion < 3) {
				String sqlString = "ALTER TABLE TABLE_VERSION ADD COLUMN IS_SUCCESS_SYNC TEXT";
				db.execSQL(sqlString);
				Log.d("DBA UPGRADE", "ON UPGRADE TABLE_VERSION ADD IS_SUCCESS_SYNC");
				
			}
			if (oldVersion < 4) {
				String sqlString = "ALTER TABLE MASTER_AGENT ADD COLUMN STATUS_USER TEXT";
				db.execSQL(sqlString);
				
				sqlString = "ALTER TABLE MASTER_AGENT ADD COLUMN USER_EXP_DATE TEXT";
				db.execSQL(sqlString);
				
				
				sqlString = "ALTER TABLE MASTER_AGENT ADD COLUMN EMAIL_ADDRESS TEXT";
				db.execSQL(sqlString);
				
				
				sqlString = "ALTER TABLE MASTER_AGENT ADD COLUMN PHONE_NO TEXT";
				db.execSQL(sqlString);
				
				sqlString = "ALTER TABLE MASTER_AGENT ADD COLUMN USER_NAME TEXT";
				db.execSQL(sqlString);

				sqlString = "ALTER TABLE MASTER_AGENT ADD COLUMN U_NAME TEXT";
				db.execSQL(sqlString);
				
				sqlString = "ALTER TABLE MASTER_AGENT ADD COLUMN U_PASS TEXT";
				db.execSQL(sqlString);
				
				
				Log.d("DBA UPGRADE", "ON UPGRADE MASTER_AGENT ADD 7 COLUMNS IS SUCCESS");		
			}
			if (oldVersion < 6) {
        		db.execSQL(DBA_PRODUCT_CONVENSIONAL.DATABASE_CREATE);	
        		db.execSQL(DBA_INSURED_GROUP.DATABASE_CREATE);	
        		
        		db.execSQL("ALTER TABLE PRODUCT_OTOMATE ADD COLUMN PA TEXT");
        		db.execSQL("ALTER TABLE PRODUCT_OTOMATE ADD COLUMN RISIKO_SENDIRI TEXT");
        		db.execSQL("ALTER TABLE PRODUCT_OTOMATE ADD COLUMN PENGGUNAAN TEXT");
        		db.execSQL("ALTER TABLE PRODUCT_OTOMATE ADD COLUMN KERUSAKAN TEXT");
        		db.execSQL("ALTER TABLE PRODUCT_OTOMATE ADD COLUMN KETERANGAN_KERUSAKAN TEXT");
        		db.execSQL("ALTER TABLE PRODUCT_OTOMATE ADD COLUMN PILIHAN_LOADING TEXT");
        		
        		
        		db.execSQL("ALTER TABLE PRODUCT_OTOMATE_SYARIAH ADD COLUMN PA TEXT");
        		db.execSQL("ALTER TABLE PRODUCT_OTOMATE_SYARIAH ADD COLUMN RISIKO_SENDIRI TEXT");
        		db.execSQL("ALTER TABLE PRODUCT_OTOMATE_SYARIAH ADD COLUMN PENGGUNAAN TEXT");
        		db.execSQL("ALTER TABLE PRODUCT_OTOMATE_SYARIAH ADD COLUMN KERUSAKAN TEXT");
        		db.execSQL("ALTER TABLE PRODUCT_OTOMATE_SYARIAH ADD COLUMN KETERANGAN_KERUSAKAN TEXT");
        		db.execSQL("ALTER TABLE PRODUCT_OTOMATE_SYARIAH ADD COLUMN PILIHAN_LOADING TEXT");

        		ArrayList<String> arrList = new ArrayList<String>();
        		arrList.add(DBA_PRODUCT_TRAVEL_SAFE.NAMA_TERTANGGUNG_5);
        		arrList.add(DBA_PRODUCT_TRAVEL_SAFE.NAMA_TERTANGGUNG_6);
        		arrList.add(DBA_PRODUCT_TRAVEL_SAFE.NAMA_TERTANGGUNG_7);
        		arrList.add(DBA_PRODUCT_TRAVEL_SAFE.NAMA_TERTANGGUNG_8);
        		arrList.add(DBA_PRODUCT_TRAVEL_SAFE.NAMA_TERTANGGUNG_9);
        		arrList.add(DBA_PRODUCT_TRAVEL_SAFE.NAMA_TERTANGGUNG_10);
        		
        		arrList.add(DBA_PRODUCT_TRAVEL_SAFE.TGL_LAHIR_5);
        		arrList.add(DBA_PRODUCT_TRAVEL_SAFE.TGL_LAHIR_6);
        		arrList.add(DBA_PRODUCT_TRAVEL_SAFE.TGL_LAHIR_7);
        		arrList.add(DBA_PRODUCT_TRAVEL_SAFE.TGL_LAHIR_8);
        		arrList.add(DBA_PRODUCT_TRAVEL_SAFE.TGL_LAHIR_9);
        		arrList.add(DBA_PRODUCT_TRAVEL_SAFE.TGL_LAHIR_10);
        		
        		arrList.add(DBA_PRODUCT_TRAVEL_SAFE.NO_PASSPORT_5);
        		arrList.add(DBA_PRODUCT_TRAVEL_SAFE.NO_PASSPORT_6);
        		arrList.add(DBA_PRODUCT_TRAVEL_SAFE.NO_PASSPORT_7);
        		arrList.add(DBA_PRODUCT_TRAVEL_SAFE.NO_PASSPORT_8);
        		arrList.add(DBA_PRODUCT_TRAVEL_SAFE.NO_PASSPORT_9);
        		arrList.add(DBA_PRODUCT_TRAVEL_SAFE.NO_PASSPORT_10);
        		
        		arrList.add(DBA_PRODUCT_TRAVEL_SAFE.PREMI_5);
        		arrList.add(DBA_PRODUCT_TRAVEL_SAFE.PREMI_6);
        		arrList.add(DBA_PRODUCT_TRAVEL_SAFE.PREMI_7);
        		arrList.add(DBA_PRODUCT_TRAVEL_SAFE.PREMI_8);
        		arrList.add(DBA_PRODUCT_TRAVEL_SAFE.PREMI_9);
        		arrList.add(DBA_PRODUCT_TRAVEL_SAFE.PREMI_10);

        		arrList.add(DBA_PRODUCT_TRAVEL_SAFE.IS_INSURED_5);
        		arrList.add(DBA_PRODUCT_TRAVEL_SAFE.IS_INSURED_6);
        		arrList.add(DBA_PRODUCT_TRAVEL_SAFE.IS_INSURED_7);
        		arrList.add(DBA_PRODUCT_TRAVEL_SAFE.IS_INSURED_8);
        		arrList.add(DBA_PRODUCT_TRAVEL_SAFE.IS_INSURED_9);
        		arrList.add(DBA_PRODUCT_TRAVEL_SAFE.IS_INSURED_10);
        		
        		arrList.add(DBA_PRODUCT_TRAVEL_SAFE.NAMA_NEGARA_2);
        		arrList.add(DBA_PRODUCT_TRAVEL_SAFE.NAMA_NEGARA_3);
        		arrList.add(DBA_PRODUCT_TRAVEL_SAFE.NAMA_NEGARA_4);
        		arrList.add(DBA_PRODUCT_TRAVEL_SAFE.NAMA_NEGARA_5);
        		arrList.add(DBA_PRODUCT_TRAVEL_SAFE.NAMA_NEGARA_6);

        		arrList.add(DBA_PRODUCT_TRAVEL_SAFE.KODE_NEGARA_2);
        		arrList.add(DBA_PRODUCT_TRAVEL_SAFE.KODE_NEGARA_3);
        		arrList.add(DBA_PRODUCT_TRAVEL_SAFE.KODE_NEGARA_4);
        		arrList.add(DBA_PRODUCT_TRAVEL_SAFE.KODE_NEGARA_5);
        		arrList.add(DBA_PRODUCT_TRAVEL_SAFE.KODE_NEGARA_6);

        		arrList.add(DBA_PRODUCT_TRAVEL_SAFE.GROUP_NAME);
        		
        		for (String st : arrList) {
					db.execSQL("ALTER TABLE PRODUCT_TRAVEL_SAFE ADD COLUMN "+ st +" TEXT");
				}  
        		 

        		db.execSQL("ALTER TABLE PRODUCT_TRAVEL_SAFE ADD COLUMN INSURED_GROUP_COUNT NUMERIC");
        		db.execSQL("ALTER TABLE PRODUCT_TRAVEL_SAFE ADD COLUMN INSURED_GROUP_ID NUMERIC");
        		
				Log.d("DBA UPGRADE", "ON UPGRADE version 6 success");		
			}
		 
			if (oldVersion < 7) {
        		db.execSQL("ALTER TABLE PRODUCT_CONVENSIONAL ADD COLUMN IS_TRUCK TEXT");
        		db.execSQL("ALTER TABLE PRODUCT_CONVENSIONAL ADD COLUMN VEHICLE_CATEGORY TEXT");
			}
			if (oldVersion < 8) {
                db.execSQL("ALTER TABLE PRODUCT_OTOMATE ADD COLUMN FLOOD TEXT");
                db.execSQL("ALTER TABLE PRODUCT_OTOMATE ADD COLUMN EQ TEXT");
                db.execSQL("ALTER TABLE PRODUCT_OTOMATE ADD COLUMN SRCC TEXT");
                db.execSQL("ALTER TABLE PRODUCT_OTOMATE ADD COLUMN TS TEXT");
                db.execSQL("ALTER TABLE PRODUCT_OTOMATE ADD COLUMN KODE_PRODUK TEXT");
                db.execSQL("ALTER TABLE PRODUCT_OTOMATE ADD COLUMN BENGKEL TEXT");
                db.execSQL("ALTER TABLE PRODUCT_OTOMATE ADD COLUMN IS_NEW_SPPA TEXT");


                db.execSQL("ALTER TABLE PRODUCT_OTOMATE_SYARIAH ADD COLUMN FLOOD TEXT");
                db.execSQL("ALTER TABLE PRODUCT_OTOMATE_SYARIAH ADD COLUMN EQ TEXT");
                db.execSQL("ALTER TABLE PRODUCT_OTOMATE_SYARIAH ADD COLUMN SRCC TEXT");
                db.execSQL("ALTER TABLE PRODUCT_OTOMATE_SYARIAH ADD COLUMN TS TEXT");
                db.execSQL("ALTER TABLE PRODUCT_OTOMATE_SYARIAH ADD COLUMN KODE_PRODUK TEXT");
                db.execSQL("ALTER TABLE PRODUCT_OTOMATE_SYARIAH ADD COLUMN BENGKEL TEXT");
                db.execSQL("ALTER TABLE PRODUCT_OTOMATE_SYARIAH ADD COLUMN IS_NEW_SPPA TEXT");
			}
		}		
    }    

    //---opens the database---
    public DBA_TABLE_CREATE_ALL open() throws SQLException 
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }
    
    
    public void drop() throws SQLException {
//    	String sql = "DROP DATABASE " + DATABASE_NAME ;
//    	db.execSQL(sql);
//    	
//    	Log.d("DBA DROP", "ON DROP SUCCESS");
    	db.close();
    	context.deleteDatabase(DATABASE_NAME);
    }

    //---closes the database---    
    public void close() 
    {
        DBHelper.close();
    }
    
   
}
