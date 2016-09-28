package com.aca.database;

import com.aca.amm.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBA_PRODUCT_CARGO {
	public static final String PRODUCT_NAME = "PRODUCT_NAME"; 
	public static final String CUSTOMER_NAME = "CUSTOMER_NAME"; 
	public static final String _id = "_id";
	public static final String PRODUCT_MAIN_ID = "PRODUCT_MAIN_ID";
	
	public static final String TSI = "TSI";
	public static final String INTEREST_DETAIL = "INTEREST_DETAIL";
	public static final String CONDITION_WARRANTIES = "CONDITION_WARRANTIES";
	public static final String BL_NO = "BL_NO";
	public static final String LC_NO = "LC_NO";
	
	public static final String CONVEYANCE_TYPE = "CONVEYANCE_TYPE";
	public static final String CONVEYANCE_CODE = "CONVEYANCE_CODE";
	
	public static final String CONVEYANCE_NAME = "CONVEYANCE_NAME";
	public static final String PRINTED_TYPE = "PRINTED_TYPE";
	public static final String PRINTED_NAME = "PRINTED_NAME";
	
	public static final String CURRENCY = "CURRENCY";
	public static final String EXCHANGE_RATE = "EXCHANGE_RATE";	
	
	public static final String VESSEL_WEIGHT = "VESSEL_WEIGHT";
	public static final String VESSEL_YEAR = "VESSEL_YEAR";
	public static final String PRINTED_WEIGHT = "PRINTED_WEIGHT";
	public static final String PRINTED_YEAR = "PRINTED_YEAR";
	
	public static final String VOYAGE_FR = "VOYAGE_FR";
	public static final String VOYAGE_TO = "VOYAGE_TO";
	public static final String VOYAGE_TRANS = "VOYAGE_TRANS";
	public static final String POLICY_TYPE = "POLICY_TYPE";
	public static final String VOYAGE_NO = "VOYAGE_NO";
	
	public static final String TGL_MULAI = "TGL_MULAI";
	public static final String TGL_AKHIR = "TGL_AKHIR";
	public static final String RATE  = "RATE";
	public static final String PREMI = "PREMI";
	public static final String BIAYA_POLIS = "BIAYA_POLIS";
	public static final String TOTAL = "TOTAL";
	
	
    private static final String TAG = "DBA_PRODUCT_CARGO";
    
    private static final String DATABASE_NAME = "AMM_VERSION_2";
    private static final String DATABASE_TABLE = "PRODUCT_CARGO";

     static final String DATABASE_CREATE =
        "CREATE TABLE PRODUCT_CARGO (_id INTEGER PRIMARY KEY, PRODUCT_MAIN_ID NUMERIC, TSI NUMERIC, " +
        "INTEREST_DETAIL TEXT, CONDITION_WARRANTIES TEXT, BL_NO TEXT, LC_NO TEXT," +
        "CONVEYANCE_TYPE TEXT, CONVEYANCE_CODE TEXT," +
        "CONVEYANCE_NAME TEXT, PRINTED_TYPE TEXT, PRINTED_NAME TEXT, CURRENCY TEXT, EXCHANGE_RATE NUMERIC, " +
        "VESSEL_WEIGHT TEXT, VESSEL_YEAR TEXT, PRINTED_WEIGHT TEXT, PRINTED_YEAR TEXT, VOYAGE_FR TEXT, " +
        "VOYAGE_TO TEXT, VOYAGE_TRANS TEXT, POLICY_TYPE TEXT, VOYAGE_NO TEXT, " +
        "TGL_MULAI TEXT, TGL_AKHIR TEXT,  RATE NUMERIC, PREMI NUMERIC, BIAYA_POLIS NUMERIC, TOTAL NUMERIC" +
        ", CUSTOMER_NAME TEXT, PRODUCT_NAME TEXT);";
        
    private final Context context;    

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBA_PRODUCT_CARGO(Context ctx) 
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
//        	try {
//        		db.execSQL(DATABASE_CREATE);	
//        	} catch (SQLException e) {
//        		e.printStackTrace();
//        	}
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
        {
//            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
//                    + newVersion + ", which will destroy all old data");
//            db.execSQL("DROP TABLE IF EXISTS PRODUCT_CARGO");
//            onCreate(db);
        }
    }    

    //---opens the database---
    public DBA_PRODUCT_CARGO open() throws SQLException 
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
    public long initialInsert(
    		long product_main_id,
    		double tsi, 
    		
    		String interestdetail, 
    		String conditionwarranties, 
    		String blno, 
    		String lcno, 
    		
    		String conveyancetype, 
    		String conveyanceCode,
    		String conveyancename,
    		String printedtype, 
    		String printedname, 
    		
    		String currency, 
    		double exchangerate, 
    		
    		String vesselweight,
    		String vesselyear, 
    		String printedweight,
    		String printedyear,
    		
    		String voyagefr, 
    		String voyageto, 
    		String voyagetrans, 
    		String policytype, 
    		String voyageno, 
    		
    		double rate,
    		String tglmulai, 
    		String tglakhir,
    		double premi, 
    		double polis, 
    		double total, 
    		String product_name, 
    		String customer_name)
    {
    	
    	
        ContentValues initialValues = new ContentValues();
        initialValues.put(PRODUCT_MAIN_ID, product_main_id);
        initialValues.put(TSI, tsi);
        initialValues.put(INTEREST_DETAIL, interestdetail);
        initialValues.put(CONDITION_WARRANTIES, conditionwarranties);
        initialValues.put(BL_NO, blno);
        initialValues.put(LC_NO, lcno);
        
        initialValues.put(CONVEYANCE_TYPE, conveyancetype);
        initialValues.put(CONVEYANCE_CODE, conveyanceCode);
        
        initialValues.put(CONVEYANCE_NAME, conveyancename);
        initialValues.put(VESSEL_WEIGHT, vesselweight);
        initialValues.put(VESSEL_YEAR, vesselyear);
        
        initialValues.put(PRINTED_TYPE, printedtype);
        initialValues.put(PRINTED_NAME, printedname);
        initialValues.put(PRINTED_WEIGHT, printedweight);
        initialValues.put(PRINTED_YEAR, printedyear);
        
        initialValues.put(CURRENCY, currency);
        initialValues.put(EXCHANGE_RATE, exchangerate);
       
        initialValues.put(VOYAGE_FR, voyagefr);
        initialValues.put(VOYAGE_TO, voyageto);
        initialValues.put(VOYAGE_TRANS, voyagetrans);
        initialValues.put(POLICY_TYPE, policytype);
        initialValues.put(VOYAGE_NO, voyageno);
        initialValues.put(TGL_MULAI, tglmulai);
        initialValues.put(TGL_AKHIR, tglakhir);
        initialValues.put(RATE, rate);
        initialValues.put(PREMI, premi);
        initialValues.put(BIAYA_POLIS, polis);
        initialValues.put(TOTAL, total);
        initialValues.put(PRODUCT_NAME, product_name);
        initialValues.put(CUSTOMER_NAME, customer_name);

        
        return db.insert(DATABASE_TABLE, null, initialValues);
    }
    
    public boolean nextInsert(long product_main_id,
    		double tsi, String interestdetail, String conditionwarranties, 
    		String blno, String lcno, 
    		String conveyancetype, 
    		String conveyanceCode,     		
    		String conveyancename, 
    		String printedtype,
    		String printedname, 
    		String currency, double exchangerate, String vesselweight,
    		String vesselyear, 
    		String printedweight,
    		String printedyear,
    		String voyagefr, String voyageto, 
    		String voyagetrans, String policytype, String voyageno, 
    		double rate,
    		String tglmulai, String tglakhir,
    		double premi, double polis, double total, String product_name, String customer_name)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(PRODUCT_MAIN_ID, product_main_id);
        initialValues.put(TSI, tsi);
        initialValues.put(INTEREST_DETAIL, interestdetail);
        initialValues.put(CONDITION_WARRANTIES, conditionwarranties);
        initialValues.put(BL_NO, blno);
        initialValues.put(LC_NO, lcno);
        initialValues.put(CONVEYANCE_TYPE, conveyancetype);
        initialValues.put(CONVEYANCE_CODE, conveyanceCode);
        initialValues.put(CONVEYANCE_NAME, conveyancename);
        initialValues.put(PRINTED_TYPE, printedtype);
        initialValues.put(PRINTED_NAME, printedname);
        initialValues.put(CURRENCY, currency);
        initialValues.put(EXCHANGE_RATE, exchangerate);
        initialValues.put(VESSEL_WEIGHT, vesselweight);
        initialValues.put(VESSEL_YEAR, vesselyear);
        initialValues.put(PRINTED_WEIGHT, printedweight);
        initialValues.put(PRINTED_YEAR, printedyear);
        initialValues.put(VOYAGE_FR, voyagefr);
        initialValues.put(VOYAGE_TO, voyageto);
        initialValues.put(VOYAGE_TRANS, voyagetrans);
        initialValues.put(POLICY_TYPE, policytype);
        initialValues.put(VOYAGE_NO, voyageno);
        initialValues.put(TGL_MULAI, tglmulai);
        initialValues.put(TGL_AKHIR, tglakhir);
        initialValues.put(RATE, rate);
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
    	try {
    		db.execSQL(DATABASE_CREATE);	
    	} catch (SQLException e) {
    		//e.printStackTrace();
    	}
    	return db.delete(DATABASE_TABLE, PRODUCT_MAIN_ID + "=" + product_main_id, null) > 0;
    }
    
    public boolean deleteAll() 
    {
    	try {
    		db.execSQL(DATABASE_CREATE);	
    	} catch (SQLException e) {
    		//e.printStackTrace();
    	}
    	
        return db.delete(DATABASE_TABLE, null, null) > 0;
    }

    //---retrieves all the contacts---
    public Cursor getRows() 
    { 
        return db.query(DATABASE_TABLE, new String[] {
        		_id, 					// 00
    			PRODUCT_MAIN_ID, 		// 01
        		TSI, 					// 02
        		INTEREST_DETAIL, 		// 03
        		CONDITION_WARRANTIES, 	// 04
        		BL_NO,					// 05
        		LC_NO,					// 06
        		CONVEYANCE_TYPE,		// 07
        		CONVEYANCE_NAME,		// 08
        		PRINTED_TYPE,			// 09
        		PRINTED_NAME,			// 10
        		CURRENCY,				// 11
        		EXCHANGE_RATE,			// 12
        		VESSEL_WEIGHT,			// 13
        		VESSEL_YEAR,			// 14
        		PRINTED_WEIGHT,			// 15
        		PRINTED_YEAR,			// 16
        		VOYAGE_FR,				// 17
        		VOYAGE_TO,				// 18
        		VOYAGE_TRANS,			// 19
        		POLICY_TYPE,			// 20	
        		VOYAGE_NO,				// 21
        		TGL_MULAI,				// 22
        		TGL_AKHIR,				// 23
        		RATE,					// 24
        		PREMI,					// 25
        		BIAYA_POLIS,			// 26
        		TOTAL, 					// 27
        		PRODUCT_NAME, 			// 28	
        		CUSTOMER_NAME,			// 29
        		CONVEYANCE_CODE			// 30
        		}, null, null, null, null, null);
    }
    
    public Cursor getRow(long product_main_id) 
    {
    	return db.query(DATABASE_TABLE, new String[] {
    			_id, 					// 00
    			PRODUCT_MAIN_ID, 		// 01
        		TSI, 					// 02
        		INTEREST_DETAIL, 		// 03
        		CONDITION_WARRANTIES, 	// 04
        		BL_NO,					// 05
        		LC_NO,					// 06
        		CONVEYANCE_TYPE,		// 07
        		CONVEYANCE_NAME,		// 08
        		PRINTED_TYPE,			// 09
        		PRINTED_NAME,			// 10
        		CURRENCY,				// 11
        		EXCHANGE_RATE,			// 12
        		VESSEL_WEIGHT,			// 13
        		VESSEL_YEAR,			// 14
        		PRINTED_WEIGHT,			// 15
        		PRINTED_YEAR,			// 16
        		VOYAGE_FR,				// 17
        		VOYAGE_TO,				// 18
        		VOYAGE_TRANS,			// 19
        		POLICY_TYPE,			// 20	
        		VOYAGE_NO,				// 21
        		TGL_MULAI,				// 22
        		TGL_AKHIR,				// 23
        		RATE,					// 24
        		PREMI,					// 25
        		BIAYA_POLIS,			// 26
        		TOTAL, 					// 27
        		PRODUCT_NAME, 			// 28	
        		CUSTOMER_NAME,			// 29
        		CONVEYANCE_CODE			// 30
        		
    			}, 		
    			PRODUCT_MAIN_ID + "=" + product_main_id, null, null, null, null);
    }
}
