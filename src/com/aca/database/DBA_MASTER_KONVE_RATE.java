package com.aca.database;

import java.text.NumberFormat;
import java.text.ParseException;

import com.aca.amm.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBA_MASTER_KONVE_RATE{

	public static final String JNP = "JNP";
    public static final String WILAYAH = "WILAYAH"; 
    public static final String VEHICLE_CATEGORY = "VEHICLE_CATEGORY"; 
    public static final String EXCO = "EXCO"; 
    public static final String RATE_BAWAH = "RATE_BAWAH"; 
    public static final String RATE_TENGAH = "RATE_TENGAH"; 
    public static final String RATE_ATAS = "RATE_ATAS"; 
    public static final String JAMINAN = "JAMINAN"; 
     
    private static final String DATABASE_NAME = "AMM_VERSION_BIG";
    private static final String DATABASE_TABLE = "MASTER_KONVE_RATE";

     static final String DATABASE_CREATE =
        "CREATE TABLE MASTER_KONVE_RATE (" +
		        "JNP TEXT, " +
		        "WILAYAH TEXT, " +
		        "VEHICLE_CATEGORY TEXT," +
		        " EXCO TEXT, " +
		        "RATE_BAWAH TEXT, " +
		        "RATE_TENGAH TEXT, " +
		        "RATE_ATAS TEXT, " +
		        "JAMINAN TEXT);";
     
        
    private final Context context;    

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBA_MASTER_KONVE_RATE(Context ctx) 
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
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
        { 
        }
    }    

    //---opens the database---
    public DBA_MASTER_KONVE_RATE open() throws SQLException 
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
    public long insert(
			    		String jnp, 
			    		String wilayah,
			    		String vehicle_category,
			    		String exco,
			    		String rate_bawah,
			    		String rate_tengah,
			    		String rate_atas,
			    		String jaminan
			    		) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(JNP, jnp);
        initialValues.put(WILAYAH, wilayah); 
        initialValues.put(VEHICLE_CATEGORY, vehicle_category); 
        initialValues.put(EXCO, exco); 
        initialValues.put(RATE_BAWAH, rate_bawah); 
        initialValues.put(RATE_TENGAH, rate_tengah); 
        initialValues.put(RATE_ATAS, rate_atas); 
        initialValues.put(JAMINAN, jaminan); 
        
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //---deletes a particular contact---
    public boolean deleteAll() 
    {
        return db.delete(DATABASE_TABLE, null, null) > 0;
    }
    
    public Cursor getRate(String wilayah, String vehicle_category, String exco, String jaminan, String jnp) throws ParseException 
    {
    	NumberFormat nf =NumberFormat.getInstance();
    	
    	String sql = "SELECT RATE_BAWAH, RATE_TENGAH, RATE_ATAS FROM MASTER_KONVE_RATE WHERE " +
    					"WILAYAH = '" + wilayah + "' AND " +
		    			"VEHICLE_CATEGORY = '" + vehicle_category+"' AND " +
    					"EXCO = '" + exco + "' AND " + 
		    			"JAMINAN = '" +  jaminan +  "' AND " +
    					"CAST(JNP AS INTEGER) >= " + String.valueOf(nf.parse(jnp).doubleValue()) + " LIMIT 1";
		    			
    	return db.rawQuery(sql, null);
    } 
    
    public Cursor getAll() 
    {
    	return db.query(DATABASE_TABLE,  new String[] {
    			JNP, 			//0
	    		WILAYAH,		//1
	    		VEHICLE_CATEGORY,//2
	    		EXCO,			//3
	    		RATE_BAWAH,		//4
	    		RATE_TENGAH,	//5
	    		RATE_ATAS,		//6
	    		JAMINAN			//7
	    		}, null, null, null, null, null);    			
    } 
}
