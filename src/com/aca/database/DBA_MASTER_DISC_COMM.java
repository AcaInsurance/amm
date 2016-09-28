package com.aca.database;

import com.aca.amm.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBA_MASTER_DISC_COMM {
	public static final String AGENT_CODE = "AGENT_CODE"; 
	public static final String LOB = "LOB"; 
	public static final String BRANCH_CODE  = "BRANCH_CODE";
	public static final String DISC_COMM  = "DISC_COMM";
	public static final String DISCOUNT  = "DISCOUNT";
	public static final String COMMISSION = "COMMISSION";
	
	private static final String DATABASE_NAME = "AMM_VERSION_2";
	private static final String DATABASE_TABLE = "MASTER_DISC_COMM";

     static final String DATABASE_CREATE =
        "CREATE TABLE MASTER_DISC_COMM (AGENT_CODE TEXT, LOB TEXT, BRANCH_CODE TEXT, DISC_COMM NUMERIC, DISCOUNT NUMERIC, COMMISSION NUMERIC);";
        
    private final Context context;    

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBA_MASTER_DISC_COMM(Context ctx) 
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
        		db.execSQL(DATABASE_CREATE);	
        	} catch (SQLException e) {
        		e.printStackTrace();
        	}
        }

		@Override
		public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}
    }    

    //---opens the database---
    public DBA_MASTER_DISC_COMM open() throws SQLException 
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
    public long insert(String agentCode, String lob, String branchCode, double DiscComm, double Discount, double Commission) 
    {
    	 ContentValues initialValues = new ContentValues();
         initialValues.put(AGENT_CODE, agentCode);
         initialValues.put(LOB, lob);
         initialValues.put(BRANCH_CODE, branchCode);
         initialValues.put(DISC_COMM, DiscComm);
         initialValues.put(DISCOUNT, Discount);
         initialValues.put(COMMISSION, Commission);
         
         return db.insert(DATABASE_TABLE, null, initialValues);
    }
    
    public boolean deleteAll() 
    {
        return db.delete(DATABASE_TABLE, null, null) > 0;
    }

    //---retrieves all the contacts---
   
    public Cursor getRow(String agentCode, String lob, String branchCode) 
    {
    	return db.query(DATABASE_TABLE, new String[] {
    			AGENT_CODE,
    			LOB,
    			BRANCH_CODE,
    			DISC_COMM,
    			DISCOUNT,
    			COMMISSION
    			}, 
    			AGENT_CODE + "= '" + agentCode + "' AND " + LOB + "= '" + lob + "' AND " + BRANCH_CODE + "= '" + branchCode + "'", null, null, null, null);
    }

    public Cursor getAll() 
    {
    	return db.query(DATABASE_TABLE, new String[] {
    			AGENT_CODE,
    			LOB,
    			BRANCH_CODE,
    			DISC_COMM,
    			DISCOUNT,
    			COMMISSION
    			},null, null, null, null, null);
    }
}

