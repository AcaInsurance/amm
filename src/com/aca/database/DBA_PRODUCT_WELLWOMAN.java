package com.aca.database;

import com.aca.amm.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBA_PRODUCT_WELLWOMAN {
	
	public static final String _id 						= "_id"; // 0
	public static final String PRODUCT_NAME 			= "PRODUCT_NAME"; // 1
	public static final String CUSTOMER_NAME 			= "CUSTOMER_NAME"; // 2 
	public static final String PRODUCT_MAIN_ID 			= "PRODUCT_MAIN_ID"; // 3
	
	public static final String PREMI 					= "PREMI"; // 4
	public static final String BIAYA_POLIS 				= "BIAYA_POLIS"; // 5
	public static final String TOTAL 					= "TOTAL"; // 6
	
	public static final String AHLI_WARIS 				= "AHLI_WARIS"; // 7
	public static final String HUBUNGAN 				= "HUBUNGAN"; // 8
	public static final String PLAN 					= "PLAN"; // 9

	public static final String Q1FLAG 					= "Q1FLAG"; // 10
	public static final String Q2FLAG 					= "Q2FLAG"; // 11
	public static final String Q3FLAG 					= "Q3FLAG"; // 12
	public static final String Q4FLAG 					= "Q4FLAG"; // 13
	
	public static final String Q1NOTE 					= "Q1NOTE"; // 14
	public static final String Q2NOTE 					= "Q2NOTE"; // 15
	public static final String Q3NOTE 					= "Q3NOTE"; // 16
	public static final String Q4NOTE 					= "Q4NOTE"; // 17
	
	public static final String TGL_MULAI 				= "TGL_MULAI"; // 18
	public static final String TGL_AKHIR 				= "TGL_AKHIR"; // 19
	
	public static final String PREMI_REAS 				= "PREMI_REAS"; // 20
	public static final String BENEFIT 					= "BENEFIT"; // 21
	
	
	
    private static final String TAG = "DBA_PRODUCT_WELLWOMAN";
    
    private static final String DATABASE_NAME = "AMM_VERSION_2";
    private static final String DATABASE_TABLE = "PRODUCT_WELLWOMAN";

     static final String DATABASE_CREATE =
        "CREATE TABLE PRODUCT_WELLWOMAN (" +
        "_id INTEGER PRIMARY KEY," +
        " PRODUCT_MAIN_ID NUMERIC," +
        " AHLI_WARIS TEXT," +
        " HUBUNGAN TEXT," +
        " TGL_MULAI TEXT, " +
        "TGL_AKHIR TEXT," +
        " PLAN TEXT, " +
        "PREMI NUMERIC," +
        " PREMI_REAS NUMERIC," +
        "  BENEFIT TEXT, " +
        "BIAYA_POLIS NUMERIC, " +
        "TOTAL NUMERIC," +
        "CUSTOMER_NAME TEXT, " +
        "PRODUCT_NAME TEXT, " + 
        "Q1FLAG TEXT, Q2FLAG TEXT, Q3FLAG TEXT, Q4FLAG TEXT, " +
        "Q1NOTE TEXT, Q2NOTE TEXT, Q3NOTE TEXT, Q4NOTE TEXT)";

        
    private final Context context;    

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBA_PRODUCT_WELLWOMAN(Context ctx) 
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
            /*Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS PRODUCT_TRAVEL_SAFE");
            onCreate(db);*/
        }
    }    

    //---opens the database---
    public DBA_PRODUCT_WELLWOMAN open() throws SQLException 
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
    		String plan,
    		String ahliwaris, String hubungan, String tglmulai, String tglakhir,
    		double premi, double premireas, double polis, double total, String product_name, String customer_name,
    		String q1flag, String q2flag, String q3flag, String q4flag,
    		String q1note, String q2note, String q3note, String q4note,
    		String benefit)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(PRODUCT_MAIN_ID, product_main_id);
        initialValues.put(PLAN, plan);
        initialValues.put(AHLI_WARIS, ahliwaris);
        initialValues.put(HUBUNGAN, hubungan);
        
        initialValues.put(TGL_MULAI, tglmulai);
        initialValues.put(TGL_AKHIR, tglakhir);
        
        initialValues.put(Q1FLAG, q1flag);
        initialValues.put(Q2FLAG, q2flag);
        initialValues.put(Q3FLAG, q3flag);
        initialValues.put(Q4FLAG, q4flag);
        
        initialValues.put(Q1NOTE, q1note);
        initialValues.put(Q2NOTE, q2note);
        initialValues.put(Q3NOTE, q3note);
        initialValues.put(Q4NOTE, q4note);
        
        initialValues.put(PREMI, premi);
        initialValues.put(PREMI_REAS, premireas);
        initialValues.put(BIAYA_POLIS, polis);
        initialValues.put(TOTAL, total);
        initialValues.put(PRODUCT_NAME, product_name);
        initialValues.put(CUSTOMER_NAME, customer_name);
        
        initialValues.put(BENEFIT, benefit);
        
        
        return db.insert(DATABASE_TABLE, null, initialValues);
    }
    
    public boolean nextInsert(long product_main_id,
    		String plan,
    		String ahliwaris, String hubungan, String tglmulai, String tglakhir,
    		double premi, double premireas, double polis, double total, String product_name, String customer_name,
    		String q1flag, String q2flag, String q3flag, String q4flag,
    		String q1note, String q2note, String q3note, String q4note,
    		String benefit
    		)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(PRODUCT_MAIN_ID, product_main_id);
        initialValues.put(PLAN, plan);
        initialValues.put(AHLI_WARIS, ahliwaris);
        initialValues.put(HUBUNGAN, hubungan);
        
        
        initialValues.put(TGL_MULAI, tglmulai);
        initialValues.put(TGL_AKHIR, tglakhir);
        
        initialValues.put(Q1FLAG, q1flag);
        initialValues.put(Q2FLAG, q2flag);
        initialValues.put(Q3FLAG, q3flag);
        initialValues.put(Q4FLAG, q4flag);
        
        initialValues.put(Q1NOTE, q1note);
        initialValues.put(Q2NOTE, q2note);
        initialValues.put(Q3NOTE, q3note);
        initialValues.put(Q4NOTE, q4note);
        
        initialValues.put(PLAN, plan);
        initialValues.put(PREMI, premi);
        initialValues.put(PREMI_REAS, premireas);
        initialValues.put(BIAYA_POLIS, polis);
        initialValues.put(TOTAL, total);
        initialValues.put(PRODUCT_NAME, product_name);
        initialValues.put(CUSTOMER_NAME, customer_name);
        
        initialValues.put(BENEFIT, benefit);
        
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
        		PLAN,
        		AHLI_WARIS,
        		HUBUNGAN,
        		TGL_MULAI,
        		TGL_AKHIR,
        		Q1FLAG,
        		Q2FLAG,
        		Q3FLAG,
        		Q4FLAG,
        		Q1NOTE,
        		Q2NOTE,
        		Q3NOTE,
        		Q4NOTE,
        		PREMI,
        		PREMI_REAS,
        		BIAYA_POLIS,
        		TOTAL, 
        		PRODUCT_NAME, 
        		CUSTOMER_NAME,
        		BENEFIT}, null, null, null, null, null);
        
    }
    
    public Cursor getRow(long product_main_id) 
    {
    	return db.query(DATABASE_TABLE, new String[] {
    			_id, 				//0
        		PRODUCT_MAIN_ID,	//1
        		PLAN,				//2
        		AHLI_WARIS,			//3
        		HUBUNGAN,			//4
        		TGL_MULAI,			//5
        		TGL_AKHIR,			//6
        		Q1FLAG,				//7
        		Q2FLAG,				//8			
        		Q3FLAG,				//9
        		Q4FLAG,				//10
        		Q1NOTE,				//11
        		Q2NOTE,				//12
        		Q3NOTE,				//13
        		Q4NOTE,				//14
        		PREMI,				//15
        		PREMI_REAS,			//16
        		BIAYA_POLIS,		//17
        		TOTAL, 				//18
        		PRODUCT_NAME, 		//19
        		CUSTOMER_NAME,		//20
        		BENEFIT				//21
    	}, PRODUCT_MAIN_ID + "=" + product_main_id, null, null, null, null);
    }

}
