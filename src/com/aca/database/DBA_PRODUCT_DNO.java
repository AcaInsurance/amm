package com.aca.database;

import com.aca.amm.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBA_PRODUCT_DNO {

	public static final String _id 						= "_id"; // 0
	public static final String PRODUCT_NAME 			= "PRODUCT_NAME"; // 1
	public static final String CUSTOMER_NAME 			= "CUSTOMER_NAME"; // 2 
	public static final String PRODUCT_MAIN_ID 			= "PRODUCT_MAIN_ID"; // 3
	
	public static final String PREMI 					= "PREMI"; // 4
	public static final String BIAYA_POLIS 				= "BIAYA_POLIS"; // 5
	public static final String TOTAL 					= "TOTAL"; // 6
	
	public static final String PLAN 					= "PLAN"; // 7

	public static final String Q1FLAG 					= "Q1FLAG"; // 8
	public static final String Q1NOTE 					= "Q1NOTE"; // 9
	public static final String Q1DATE 					= "Q1DATE"; // 10
	
	public static final String START_DATE 				= "START_DATE"; // 11
	public static final String END_DATE 				= "END_DATE"; // 12
	
	public static final String COMP_NAME_1 				= "COMP_NAME_1"; // 13
	public static final String COMP_BUS_TYPE_1 			= "COMP_BUS_TYPE_1"; // 14
	
	public static final String COMP_NAME_2 				= "COMP_NAME_2"; // 15
	public static final String COMP_BUS_TYPE_2 			= "COMP_BUS_TYPE_2"; // 16
	
	public static final String COMP_NAME_3 				= "COMP_NAME_3"; // 17
	public static final String COMP_BUS_TYPE_3 			= "COMP_BUS_TYPE_3"; // 18
	
	public static final String COMP_NAME_4 				= "COMP_NAME_4"; // 19
	public static final String COMP_BUS_TYPE_4 			= "COMP_BUS_TYPE_4"; // 20
	
	public static final String COMP_NAME_5 				= "COMP_NAME_5"; // 21
	public static final String COMP_BUS_TYPE_5 			= "COMP_BUS_TYPE_5"; // 22
	
	public static final String COMP_NAME_6 				= "COMP_NAME_6"; // 23
	public static final String COMP_BUS_TYPE_6 			= "COMP_BUS_TYPE_6"; // 24
	
	public static final String COMP_NAME_7				= "COMP_NAME_7"; // 25
	public static final String COMP_BUS_TYPE_7 			= "COMP_BUS_TYPE_7"; // 26
	
	public static final String COMP_NAME_8 				= "COMP_NAME_8"; // 27
	public static final String COMP_BUS_TYPE_8 			= "COMP_BUS_TYPE_8"; // 28
	
	public static final String COMP_NAME_9 				= "COMP_NAME_9"; // 29
	public static final String COMP_BUS_TYPE_9 			= "COMP_BUS_TYPE_9"; // 30
	
	public static final String COMP_NAME_10 			= "COMP_NAME_10"; // 31
	public static final String COMP_BUS_TYPE_10 		= "COMP_BUS_TYPE_10"; // 32
		
    private static final String TAG = "DBA_PRODUCT_DNO";
    
    private static final String DATABASE_NAME = "AMM_VERSION_2";
    private static final String DATABASE_TABLE = "PRODUCT_DNO";

     static final String DATABASE_CREATE =
        "CREATE TABLE PRODUCT_DNO (" +
	        "_id INTEGER PRIMARY KEY," +
	        " PRODUCT_MAIN_ID NUMERIC, " +
	        "START_DATE TEXT, " +
	        "END_DATE TEXT, " +
	        "PLAN TEXT," +
	        " PREMI NUMERIC," +
	        " BIAYA_POLIS NUMERIC," +
	        " TOTAL NUMERIC," +
	        "CUSTOMER_NAME TEXT, " +
	        "PRODUCT_NAME TEXT, " + 
	        "Q1FLAG TEXT," +
	        " Q1NOTE TEXT," +
	        " Q1DATE TEXT, " +
	        "COMP_NAME_1 TEXT, COMP_BUS_TYPE_1 TEXT, " +
	        "COMP_NAME_2 TEXT, COMP_BUS_TYPE_2 TEXT, " +
	        "COMP_NAME_3 TEXT, COMP_BUS_TYPE_3 TEXT, " +
	        "COMP_NAME_4 TEXT, COMP_BUS_TYPE_4 TEXT, " +
	        "COMP_NAME_5 TEXT, COMP_BUS_TYPE_5 TEXT, " +
	        "COMP_NAME_6 TEXT, COMP_BUS_TYPE_6 TEXT, " +
	        "COMP_NAME_7 TEXT, COMP_BUS_TYPE_7 TEXT, " +
	        "COMP_NAME_8 TEXT, COMP_BUS_TYPE_8 TEXT, " +
	        "COMP_NAME_9 TEXT, COMP_BUS_TYPE_9 TEXT, " +
	        "COMP_NAME_10 TEXT, COMP_BUS_TYPE_10 TEXT)";

        
    private final Context context;    

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBA_PRODUCT_DNO(Context ctx) 
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
//        	
//        	try {
//        		db.execSQL(DATABASE_CREATE);	
//        	} catch (SQLException e) {
//        		e.printStackTrace();
//        	}
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
        {
            /*Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS PRODUCT_TRAVEL_SAFE");
            onCreate(db);*/
//        	String sqlQuery = "ALTER TABLE PRODUCT_DNO ADD COLUMN EMAIL TEXT";
//			db.execSQL(sqlQuery);
			
        	
        }
    }    

    //---opens the database---
    public DBA_PRODUCT_DNO open() throws SQLException 
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void clearData () {
    	this.open(); 

    	String sql = "_id not in ( SELECT MAX(_id) FROM " + DATABASE_TABLE + " GROUP BY product_main_id)";
    	db.delete(DATABASE_TABLE, sql, null );
    	this.close();
    }
    //---closes the database---    
    public void close() 
    {
        DBHelper.close();
    }
    
    //---insert a contact into the database---
    public long initialInsert(long product_main_id,
    		String plan,String startDate, String endDate,
    		double premi, double polis, double total, String product_name, String customer_name,
    		String q1flag, String q1note, String q1date,
    		String comp_name_1, String comp_bus_type_1,
    		String comp_name_2, String comp_bus_type_2,
    		String comp_name_3, String comp_bus_type_3,
    		String comp_name_4, String comp_bus_type_4,
    		String comp_name_5, String comp_bus_type_5,
    		String comp_name_6, String comp_bus_type_6,
    		String comp_name_7, String comp_bus_type_7,
    		String comp_name_8, String comp_bus_type_8,
    		String comp_name_9, String comp_bus_type_9,
    		String comp_name_10, String comp_bus_type_10)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(PRODUCT_MAIN_ID, product_main_id);
        initialValues.put(PLAN, plan);
        
        initialValues.put(START_DATE, startDate);
        initialValues.put(END_DATE, endDate);
        
        initialValues.put(Q1FLAG, q1flag);
        initialValues.put(Q1NOTE, q1note);
        initialValues.put(Q1DATE, q1date);

        initialValues.put(PREMI, premi);
        initialValues.put(BIAYA_POLIS, polis);
        initialValues.put(TOTAL, total);
        initialValues.put(PRODUCT_NAME, product_name);
        initialValues.put(CUSTOMER_NAME, customer_name);
        
        initialValues.put(COMP_NAME_1, comp_name_1);
        initialValues.put(COMP_BUS_TYPE_1, comp_bus_type_1);
        
        initialValues.put(COMP_NAME_2, comp_name_2);
        initialValues.put(COMP_BUS_TYPE_2, comp_bus_type_2);
        
        initialValues.put(COMP_NAME_3, comp_name_3);
        initialValues.put(COMP_BUS_TYPE_3, comp_bus_type_3);
        
        initialValues.put(COMP_NAME_4, comp_name_4);
        initialValues.put(COMP_BUS_TYPE_4, comp_bus_type_4);
        
        initialValues.put(COMP_NAME_5, comp_name_5);
        initialValues.put(COMP_BUS_TYPE_5, comp_bus_type_5);
        
        initialValues.put(COMP_NAME_6, comp_name_6);
        initialValues.put(COMP_BUS_TYPE_6, comp_bus_type_6);
        
        initialValues.put(COMP_NAME_7, comp_name_7);
        initialValues.put(COMP_BUS_TYPE_7, comp_bus_type_7);
        
        initialValues.put(COMP_NAME_8, comp_name_8);
        initialValues.put(COMP_BUS_TYPE_8, comp_bus_type_8);
        
        initialValues.put(COMP_NAME_9, comp_name_9);
        initialValues.put(COMP_BUS_TYPE_9, comp_bus_type_9);
        
        initialValues.put(COMP_NAME_10, comp_name_10);
        initialValues.put(COMP_BUS_TYPE_10, comp_bus_type_10);
        
        
        return db.insert(DATABASE_TABLE, null, initialValues);
    }
    
    public boolean nextInsert(long product_main_id,
    		String plan, String startDate, String endDate,
    		double premi, double polis, double total, String product_name, String customer_name,
    		String q1flag, String q1note, String q1date,
    		String comp_name_1, String comp_bus_type_1,
    		String comp_name_2, String comp_bus_type_2,
    		String comp_name_3, String comp_bus_type_3,
    		String comp_name_4, String comp_bus_type_4,
    		String comp_name_5, String comp_bus_type_5,
    		String comp_name_6, String comp_bus_type_6,
    		String comp_name_7, String comp_bus_type_7,
    		String comp_name_8, String comp_bus_type_8,
    		String comp_name_9, String comp_bus_type_9,
    		String comp_name_10, String comp_bus_type_10)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(PRODUCT_MAIN_ID, product_main_id);
        initialValues.put(PLAN, plan);
        
        initialValues.put(START_DATE, startDate);
        initialValues.put(END_DATE, endDate);
        
        initialValues.put(Q1FLAG, q1flag);
        initialValues.put(Q1NOTE, q1note);
        initialValues.put(Q1DATE, q1date);

        initialValues.put(PREMI, premi);
        initialValues.put(BIAYA_POLIS, polis);
        initialValues.put(TOTAL, total);
        initialValues.put(PRODUCT_NAME, product_name);
        initialValues.put(CUSTOMER_NAME, customer_name);
        
        initialValues.put(COMP_NAME_1, comp_name_1);
        initialValues.put(COMP_BUS_TYPE_1, comp_bus_type_1);
        
        initialValues.put(COMP_NAME_2, comp_name_2);
        initialValues.put(COMP_BUS_TYPE_2, comp_bus_type_2);
        
        initialValues.put(COMP_NAME_3, comp_name_3);
        initialValues.put(COMP_BUS_TYPE_3, comp_bus_type_3);
        
        initialValues.put(COMP_NAME_4, comp_name_4);
        initialValues.put(COMP_BUS_TYPE_4, comp_bus_type_4);
        
        initialValues.put(COMP_NAME_5, comp_name_5);
        initialValues.put(COMP_BUS_TYPE_5, comp_bus_type_5);
        
        initialValues.put(COMP_NAME_6, comp_name_6);
        initialValues.put(COMP_BUS_TYPE_6, comp_bus_type_6);
        
        initialValues.put(COMP_NAME_7, comp_name_7);
        initialValues.put(COMP_BUS_TYPE_7, comp_bus_type_7);
        
        initialValues.put(COMP_NAME_8, comp_name_8);
        initialValues.put(COMP_BUS_TYPE_8, comp_bus_type_8);
        
        initialValues.put(COMP_NAME_9, comp_name_9);
        initialValues.put(COMP_BUS_TYPE_9, comp_bus_type_9);
        
        initialValues.put(COMP_NAME_10, comp_name_10);
        initialValues.put(COMP_BUS_TYPE_10, comp_bus_type_10);
        
        
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
        		START_DATE,
        		END_DATE,
        		Q1FLAG,
        		Q1NOTE,
        		Q1DATE,
        		PREMI,
        		BIAYA_POLIS,
        		TOTAL, 
        		PRODUCT_NAME, 
        		CUSTOMER_NAME,
        		COMP_NAME_1,
        		COMP_BUS_TYPE_1,
        		COMP_NAME_2,
        		COMP_BUS_TYPE_2,
        		COMP_NAME_3,
        		COMP_BUS_TYPE_3,
        		COMP_NAME_4,
        		COMP_BUS_TYPE_4,
        		COMP_NAME_5,
        		COMP_BUS_TYPE_5,
        		COMP_NAME_6,
        		COMP_BUS_TYPE_6,
        		COMP_NAME_7,
        		COMP_BUS_TYPE_7,
        		COMP_NAME_8,
        		COMP_BUS_TYPE_8,
        		COMP_NAME_9,
        		COMP_BUS_TYPE_9,
        		COMP_NAME_10,
        		COMP_BUS_TYPE_10}, null, null, null, null, null);
        
        
        
    }
    
    public Cursor getRow(long product_main_id) 
    {
    	return db.query(DATABASE_TABLE, new String[] {
    			_id, 				// 0
        		PRODUCT_MAIN_ID,	// 1
        		PLAN,				// 2
        		START_DATE,			// 3
        		END_DATE,			// 4
        		Q1FLAG,				// 5
        		Q1NOTE,				// 6
        		Q1DATE,				// 7
        		PREMI,				// 8
        		BIAYA_POLIS,		// 9
        		TOTAL, 				// 10
        		PRODUCT_NAME, 		// 11
        		CUSTOMER_NAME,		// 12
        		COMP_NAME_1,		// 13
        		COMP_BUS_TYPE_1,	// 14
        		COMP_NAME_2,		// 15
        		COMP_BUS_TYPE_2,	// 16
        		COMP_NAME_3,		// 17 
        		COMP_BUS_TYPE_3,	// 18
        		COMP_NAME_4,		// 19
        		COMP_BUS_TYPE_4,	// 20
        		COMP_NAME_5,		// 21	 
        		COMP_BUS_TYPE_5,	// 22
        		COMP_NAME_6,		// 23 
        		COMP_BUS_TYPE_6,	// 24
        		COMP_NAME_7,		// 25
        		COMP_BUS_TYPE_7,	// 26
         		COMP_NAME_8,		// 27
        		COMP_BUS_TYPE_8,	// 28
        		COMP_NAME_9,		// 29
        		COMP_BUS_TYPE_9,	// 30
        		COMP_NAME_10,		// 31
        		COMP_BUS_TYPE_10	// 32
    	}, PRODUCT_MAIN_ID + "=" + product_main_id, null, null, null, null);
    }
}
