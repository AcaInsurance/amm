package com.aca.database;

import java.text.SimpleDateFormat;

import com.aca.amm.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

public class DBA_PRODUCT_MAIN {
	
	public static final String _id = "_id";
	public static final String CUSTOMER_CODE = "CUSTOMER_CODE";
	public static final String CUSTOMER_NAME = "CUSTOMER_NAME";
	public static final String PRODUCT_NAME = "PRODUCT_NAME";
	public static final String I_SPPA_NO = "I_SPPA_NO";
	public static final String E_SPPA_NO = "E_SPPA_NO";
	public static final String PREMI = "PREMI";
	public static final String STAMP = "STAMP";
	public static final String COST = "COST";
	public static final String TOTAL = "TOTAL";
	public static final String SIGNATURE = "SIGNATURE";
	public static final String IS_PAID = "IS_PAID";
	public static final String START_DATE = "START_DATE";
	public static final String END_DATE = "END_DATE";
	public static final String ENTRY_DATE = "ENTRY_DATE";
	public static final String SYNC_DATE = "SYNC_DATE";
	public static final String PAID_DATE = "PAID_DATE";
	
	public static final String PREV_POLIS_BRANCH = "PREV_POLIS_BRANCH";
	public static final String PREV_POLIS_YEAR = "PREV_POLIS_YEAR";
	public static final String PREV_POLIS_NO = "PREV_POLIS_NO";
	public static final String PREV_POLIS_LOB = "PREV_POLIS_LOB";

	public static final String SPPA_TYPE = "SPPA_TYPE";
	public static final String COMPLETE_DATE = "COMPLETE_DATE";
	
	public static final String DISCOUNT_PCT = "DISCOUNT_PCT";
	public static final String DISCOUNT = "DISCOUNT";
	
	public static final String IS_COMPLETE_PHOTO = "IS_COMPLETE_PHOTO";
	    
    private static final String TAG = "DBA_PRODUCT_MAIN";
    
    private static final String DATABASE_NAME = "AMM_VERSION_2";
    private static final String DATABASE_TABLE = "PRODUCT_MAIN";

     static final String DATABASE_CREATE =
        "CREATE TABLE PRODUCT_MAIN (_id INTEGER PRIMARY KEY, CUSTOMER_CODE TEXT, CUSTOMER_NAME TEXT, PRODUCT_NAME TEXT," +
        "I_SPPA_NO TEXT, E_SPPA_NO TEXT, PREMI NUMERIC, STAMP NUMERIC, COST NUMERIC, TOTAL NUMERIC, " +
        "SIGNATURE TEXT, IS_PAID TEXT, START_DATE TEXT, END_DATE TEXT, ENTRY_DATE TEXT, " +
        "SYNC_DATE TEXT, PAID_DATE TEXT, " +
        "PREV_POLIS_BRANCH TEXT, PREV_POLIS_YEAR TEXT, PREV_POLIS_NO TEXT," +
        "SPPA_TYPE TEXT, COMPLETE_DATE TEXT," +
        "PREV_POLIS_LOB TEXT, DISCOUNT_PCT NUMERIC, DISCOUNT NUMERIC," +
        "IS_COMPLETE_PHOTO TEXT );";
        
    private final Context context;    	

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBA_PRODUCT_MAIN(Context ctx) 
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
            db.execSQL("DROP TABLE IF EXISTS PRODUCT_MAIN");
            onCreate(db);*/
        }
    }    

    public DBA_PRODUCT_MAIN open() throws SQLException 
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close() 
    {
        DBHelper.close();
    }
    
    public long initialInsert(String cust_code, String cust_name, String prod_name, 
    		double premi, 
    		double stamp, 
    		double cost,
    		double total, 
    		String start_date, 
    		String end_date, 
    		String entry_date,
    		String prev_polis_branch,
    		String prev_polis_year,
    		String prev_polis_no,
    		String sppa_type,
    		String prev_polis_lob,
    		double discount_pct,
    		double discount) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(CUSTOMER_CODE, cust_code);
        initialValues.put(CUSTOMER_NAME, cust_name);
        initialValues.put(PRODUCT_NAME, prod_name);
        initialValues.put(I_SPPA_NO, "");
        initialValues.put(E_SPPA_NO, "");
        initialValues.put(PREMI, premi);
        initialValues.put(STAMP, stamp);
        initialValues.put(COST, cost);
        initialValues.put(TOTAL, total);
        initialValues.put(SIGNATURE, "");
        initialValues.put(IS_PAID, "");
        initialValues.put(START_DATE, start_date);
        initialValues.put(END_DATE, end_date);
        initialValues.put(ENTRY_DATE, entry_date);
        initialValues.put(SYNC_DATE, "");
        initialValues.put(PAID_DATE, "");

        initialValues.put(PREV_POLIS_LOB, prev_polis_lob);
        initialValues.put(PREV_POLIS_BRANCH, prev_polis_branch);
        initialValues.put(PREV_POLIS_YEAR, prev_polis_year);
        initialValues.put(PREV_POLIS_NO, prev_polis_no);
        
        initialValues.put(SPPA_TYPE, sppa_type);
        initialValues.put(COMPLETE_DATE, "");
        
        initialValues.put(DISCOUNT_PCT, discount_pct);
        initialValues.put(DISCOUNT, discount);
        
        initialValues.put(IS_COMPLETE_PHOTO, "");
        
        return db.insert(DATABASE_TABLE, null, initialValues);
    }
    
    public boolean nextInsert(long rowId, String cust_code, String cust_name, 
    		double premi, double stamp, double cost, double total,
    		String start_date, String end_date, String entry_date,
    		double discount_pct,
    		double discount) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(CUSTOMER_CODE, cust_code);
        initialValues.put(CUSTOMER_NAME, cust_name);
        initialValues.put(PREMI, premi);
        initialValues.put(STAMP, stamp);
        initialValues.put(COST, cost);
        initialValues.put(TOTAL, total);
        initialValues.put(START_DATE, start_date);
        initialValues.put(END_DATE, end_date);
        initialValues.put(ENTRY_DATE, entry_date);
        
        initialValues.put(DISCOUNT_PCT, discount_pct);
        initialValues.put(DISCOUNT, discount);
        
        return db.update(DATABASE_TABLE, initialValues, _id + "=" + rowId, null) > 0;
    }
    
    public boolean updateCompletePhoto (long rowId, String completePhoto) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(IS_COMPLETE_PHOTO, completePhoto);
        
        return db.update(DATABASE_TABLE, initialValues, _id + "=" + rowId, null) > 0;
    }
    
    public boolean updateCustomer(long rowId, String cust_code, String cust_name) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(CUSTOMER_CODE, cust_code);
        initialValues.put(CUSTOMER_CODE, cust_name);
        
        return db.update(DATABASE_TABLE, initialValues, _id + "=" + rowId, null) > 0;
    }
    
    public boolean updateSyncDate(long rowId, String sync_date) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(SYNC_DATE, sync_date);
        
        return db.update(DATABASE_TABLE, initialValues, _id + "=" + rowId, null) > 0;
    }
    
    
    public boolean updatePaidDate(long rowId, String paid_date) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(PAID_DATE, paid_date);
        
        return db.update(DATABASE_TABLE, initialValues, _id + "=" + rowId, null) > 0;
    }
    
    public boolean updateCompleteDate(long rowId, String complete_date) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(COMPLETE_DATE, complete_date);
        
        return db.update(DATABASE_TABLE, initialValues, _id + "=" + rowId, null) > 0;
    }
    
    public boolean updateSignature(long rowId, String signature_path) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(SIGNATURE, signature_path);
        
        return db.update(DATABASE_TABLE, initialValues, _id + "=" + rowId, null) > 0;
    }
    
    public boolean updateISPPA(long rowId) 
    {
    	String lastSppa;
    	String[] sppaPart;
    	StringBuilder sb = new StringBuilder();
    	
    	SimpleDateFormat sdfmonth = new SimpleDateFormat("MM");
    	SimpleDateFormat sdfyear = new SimpleDateFormat("yy");
    	
        String curryear = sdfyear.format(new java.util.Date());
        String currmonth = sdfmonth.format(new java.util.Date());
        String currnumber = "";
        String currSPPA = "";
    	
    	try{
    		lastSppa = MaxISPPANo();
    		
    		if(!lastSppa.isEmpty()){
	    		sppaPart = lastSppa.split("[@.]");
	    		
	    		String year = sppaPart[1];
	    		String month = sppaPart[2];
	    		String rnumber = sppaPart[3];
	    		
	    		if(year.equals(curryear)){
	    			
	    			if(month.equals(currmonth)){
	    				int nextnumber = Integer.parseInt(rnumber) + 1;
	    	    		
	    	    		currnumber = String.format("%06d", nextnumber);
	    			}
	    			else{
	    				currnumber = String.format("%06d", 1);
	    			}
	    		}else{
	    			currnumber = String.format("%06d", 1);
	    		}
	    		
    		}else{
    			
    			currnumber = String.format("%06d", 1);
    		}
    		

			sb.append("AMM.");
			sb.append(curryear);
			sb.append(".");
			sb.append(currmonth);
			sb.append(".");
			sb.append(currnumber);
			
			currSPPA = sb.toString();
    		
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    	
        ContentValues initialValues = new ContentValues();
        initialValues.put(I_SPPA_NO, currSPPA);
        
        return db.update(DATABASE_TABLE, initialValues, _id + "=" + rowId, null) > 0;
    }
    
    public boolean updateESPPA(long rowId, String e_sppa) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(E_SPPA_NO, e_sppa);
        
        return db.update(DATABASE_TABLE, initialValues, _id + "=" + rowId, null) > 0;
    }
    
    public boolean updatePaid(long rowId) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(IS_PAID, "1");
        
        return db.update(DATABASE_TABLE, initialValues, _id + "=" + rowId, null) > 0;
    }

    //---deletes a particular contact---
    public boolean delete(long rowId) 
    {
        return db.delete(DATABASE_TABLE, _id + "=" + rowId, null) > 0;
    }
    
    //---Untuk Menampilkan Di List
    public Cursor getRowsIncomplete() 
    {
        return db.query(DATABASE_TABLE, new String[] {
        		_id,
        		CUSTOMER_CODE,
        		CUSTOMER_NAME,
        		PRODUCT_NAME,
        		I_SPPA_NO,
        		E_SPPA_NO,
        		PREMI,
        		STAMP,
        		COST,
        		TOTAL,
        		SIGNATURE,
        		IS_PAID,
        		START_DATE,
        		END_DATE,
        		PREV_POLIS_BRANCH,
        		PREV_POLIS_YEAR,
        		PREV_POLIS_NO,
        		SPPA_TYPE, 								
        		ENTRY_DATE,
        		PREV_POLIS_LOB,
        		DISCOUNT_PCT,
        		DISCOUNT
        		}, I_SPPA_NO  + " = '' ", null, null, null,  "substr(" + ENTRY_DATE + ", 7, 4) Desc, substr(" + ENTRY_DATE +",4,2)Desc, substr(" +ENTRY_DATE + ",1,2) Desc, " + _id + " Desc ");
        
        //
//        _id + " Desc"
    }
    
    public Cursor getRowsUnSynch() 
    {
        return db.query(DATABASE_TABLE, new String[] {
        		_id,
        		CUSTOMER_CODE,
        		CUSTOMER_NAME,
        		PRODUCT_NAME,
        		I_SPPA_NO,
        		E_SPPA_NO,
        		PREMI,
        		STAMP,
        		COST,
        		TOTAL,
        		SIGNATURE,
        		IS_PAID,
        		START_DATE,
        		END_DATE,
        		PREV_POLIS_BRANCH,
        		PREV_POLIS_YEAR,
        		PREV_POLIS_NO,
        		SPPA_TYPE, 
        		COMPLETE_DATE,
        		PREV_POLIS_LOB,
        		DISCOUNT_PCT,
        		DISCOUNT
        		}, E_SPPA_NO  + " = '' and " + I_SPPA_NO + " != ''", null, null, null, "substr(" + COMPLETE_DATE + ", 7, 4) Desc, substr(" + COMPLETE_DATE +",4,2) Desc, substr(" +COMPLETE_DATE + ",1,2) Desc, " + I_SPPA_NO + " Desc ");
//        /,"strftime('%d/%m/%y', " + COMPLETE_DATE + ")Desc"
    }
    
    public Cursor getRowsUnPaid() 
    {
        return db.query(DATABASE_TABLE, new String[] {
        		_id,				//1
        		CUSTOMER_CODE,	//2
        		CUSTOMER_NAME,	//3
        		PRODUCT_NAME,	//4
        		I_SPPA_NO,	//5
        		E_SPPA_NO,	//6
        		PREMI,		//7
        		STAMP,		//8
        		COST,		//9
        		TOTAL,		//10
        		SIGNATURE,	//11
        		IS_PAID,	//12
        		START_DATE,//13
        		END_DATE,	//14
        		SYNC_DATE,	//15
        		PREV_POLIS_BRANCH,	//16
        		PREV_POLIS_YEAR,	//17
        		PREV_POLIS_NO,		//18
        		SPPA_TYPE,			 //19
        		SYNC_DATE,			//20
        		PREV_POLIS_LOB,		//21
        		DISCOUNT_PCT,		//22
        		DISCOUNT,			//23
        		IS_COMPLETE_PHOTO	//24
        		}, IS_PAID  + " = '' and " + E_SPPA_NO  + " != '' and " + I_SPPA_NO + " != ''", null, null, null, 
        		 "substr(" + SYNC_DATE + ", 7, 4) Desc, substr(" + SYNC_DATE +",4,2) Desc, substr(" +SYNC_DATE + ",1,2) Desc, substr(" + E_SPPA_NO + ",-5) Desc ");
    }
    											
    public Cursor getRowsBySearch(String key) 
    {
        return db.query(DATABASE_TABLE, new String[] {
        		_id,
        		CUSTOMER_CODE,
        		CUSTOMER_NAME,
        		PRODUCT_NAME,
        		I_SPPA_NO,
        		E_SPPA_NO,
        		PREMI,
        		STAMP,
        		COST,
        		TOTAL,
        		SIGNATURE,
        		IS_PAID,
        		START_DATE,
        		END_DATE,
        		PREV_POLIS_BRANCH,
        		PREV_POLIS_YEAR,
        		PREV_POLIS_NO,
        		SPPA_TYPE, 
        		COMPLETE_DATE,
        		PREV_POLIS_LOB,
        		DISCOUNT_PCT,
        		DISCOUNT
        		}, PRODUCT_NAME + " like '%" + key + "%'", null, null, null, _id + " Desc");
    }
    

    public Cursor getRowsAll() 
    {
    	Cursor mCursor =
                db.query(DATABASE_TABLE, new String[] {
            		_id,
            		CUSTOMER_CODE,
            		CUSTOMER_NAME,
            		PRODUCT_NAME,
            		I_SPPA_NO,
            		E_SPPA_NO,
            		PREMI,
            		STAMP,
            		COST,
            		TOTAL,
            		SIGNATURE,
            		IS_PAID,
            		START_DATE,
            		END_DATE,
            		PREV_POLIS_BRANCH,
            		PREV_POLIS_YEAR,
            		PREV_POLIS_NO,
            		SPPA_TYPE, 
            		COMPLETE_DATE,
            		PREV_POLIS_LOB,
            		DISCOUNT_PCT,
            		DISCOUNT}, null, null, null, null, _id);
        
        return mCursor;
    }

    public Cursor getRow(long rowId) throws SQLException 
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {
            		_id,
            		CUSTOMER_CODE,
            		CUSTOMER_NAME,
            		PRODUCT_NAME,
            		I_SPPA_NO,
            		E_SPPA_NO,
            		PREMI,
            		STAMP,
            		COST,
            		TOTAL,
            		SIGNATURE,
            		IS_PAID,
            		START_DATE,
            		END_DATE,
            		ENTRY_DATE,
            		SYNC_DATE, 
            		PAID_DATE,
            		PREV_POLIS_BRANCH,
            		PREV_POLIS_YEAR,
            		PREV_POLIS_NO,
            		SPPA_TYPE, 
            		COMPLETE_DATE,
            		PREV_POLIS_LOB,
            		DISCOUNT_PCT,
            		DISCOUNT}, _id + "=" + rowId, null, null, null, null, null);
        
        return mCursor;
    }
    public Cursor getRowByPolicyNo(String polisNo) throws SQLException 
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {
            		_id,
            		PREV_POLIS_NO,
            		}, PREV_POLIS_NO + " like '%" + polisNo + "%'", null, null, null, null, null);
        
        return mCursor;
    }
    
    public String MaxISPPANo(){
    	
    	Cursor c = db.rawQuery("SELECT MAX(" + I_SPPA_NO + ") FROM " + DATABASE_TABLE, null);
    	c.moveToFirst();
    	
    	return c.getString(0);
    	
    }
}
