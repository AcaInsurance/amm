package com.aca.database;

import com.aca.amm.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBA_TABLE_CREATE_BIG   {
		
	public static final String DATABASE_NAME = "AMM_VERSION_BIG";

    private final Context context;    

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBA_TABLE_CREATE_BIG(Context ctx) 
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
        	try {
//        		db.execSQL(DBA_MASTER_ACA_MOBIL_RATE.DATABASE_CREATE);	
//        		db.execSQL(DBA_MASTER_ASRI_RATE.DATABASE_CREATE);	
//        		db.execSQL(DBA_MASTER_BUSINESS_TYPE.DATABASE_CREATE);	
//        		db.execSQL(DBA_MASTER_CAR_BRAND.DATABASE_CREATE);	
//        		db.execSQL(DBA_MASTER_CAR_TYPE.DATABASE_CREATE);	
//        		db.execSQL(DBA_MASTER_CITY_PROVINCE.DATABASE_CREATE);	
//        		db.execSQL(DBA_MASTER_COUNTRY.DATABASE_CREATE);	
//        		db.execSQL(DBA_MASTER_EXECUTIVE_SAFE_RATE.DATABASE_CREATE);	
//        		db.execSQL(DBA_MASTER_JENIS_TOKO.DATABASE_CREATE);	
//        		db.execSQL(DBA_MASTER_LOB.DATABASE_CREATE);	
//        		db.execSQL(DBA_MASTER_MEDISAFE_RATE.DATABASE_CREATE);	
//        		db.execSQL(DBA_MASTER_OCCUPATION.DATABASE_CREATE);	
//        		db.execSQL(DBA_MASTER_OTOMATE_RATE.DATABASE_CREATE);	
//        		db.execSQL(DBA_MASTER_PA_AMANAH_RATE.DATABASE_CREATE);	
//        		db.execSQL(DBA_MASTER_PRODUCT_SETTING.DATABASE_CREATE);	
//        		db.execSQL(DBA_MASTER_TOKO_RATE.DATABASE_CREATE);	
//        		db.execSQL(DBA_MASTER_TRAVELSAFE_DOM_RATE.DATABASE_CREATE);	
//        		db.execSQL(DBA_MASTER_TRAVELSAFE_INT_RATE.DATABASE_CREATE);	
//        		
//        		db.execSQL(DBA_MASTER_VESSEL_TYPE.DATABASE_CREATE);	
//        		db.execSQL(DBA_MASTER_VESSEL_DETAIL.DATABASE_CREATE);	        		
//        		db.execSQL(DBA_MASTER_WELLWOMAN_RATE.DATABASE_CREATE);	
//        		
//        		db.execSQL(DBA_RATE_ASRI.DATABASE_CREATE);	
//        		db.execSQL(DBA_RATE_OTOMATE.DATABASE_CREATE);	
//        		db.execSQL(DBA_TABLE_VERSION.DATABASE_CREATE);	
//
//        		db.execSQL(DBA_MASTER_OTOMATE_TPL.DATABASE_CREATE);	
//        		db.execSQL(DBA_MASTER_OTOMATE_PA.DATABASE_CREATE);	

//        		db.execSQL(DBA_MASTER_OTOMATE_AOG.DATABASE_CREATE);	
        		
        		
        		
        

    			Log.d("DBA_BIG", "ON CREATE sukses all");
        	} catch (SQLException e) {
        		e.printStackTrace();
        	}
        }
        

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			if (oldVersion < 3 ) {
//        		db.execSQL(DBA_MASTER_CAR_BRAND_SYARIAH.DATABASE_CREATE);
//    			Log.d("DBA_BIG", "ON CREATE dba car brand syariah sukses");
//    			
//        		db.execSQL(DBA_MASTER_CAR_TYPE_SYARIAH.DATABASE_CREATE);
//    			Log.d("DBA_BIG", "ON CREATE dba car type syariah sukses");
			}
			
		}		
    }    

    //---opens the database---
    public DBA_TABLE_CREATE_BIG open() throws SQLException 
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }
    
    //---closes the database---    
    public void close() 
    {
        DBHelper.close();
    }
    
   
}
