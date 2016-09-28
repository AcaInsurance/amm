package com.aca.database;

import com.aca.amm.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBA_TEST_2   {
	public static final String BRANCH_ID = "BRANCH_ID"; 
	public static final String USER_ID = "USER_ID"; 
	public static final String OFFICE_ID  = "OFFICE_ID";
	public static final String MKT_CODE  = "MKT_CODE";
	public static final String SIGN_PLACE  = "SIGN_PLACE";
	public static final String POPUP = "POPUP";
	public static final String MAX_ROW = "MAX_ROW";
	public static final String STATUS = "STATUS";
	public static final String EMAIL = "EMAIL";
	
	
	private static final String DATABASE_NAME = "AMM_VERSION_2";
	private static final String DATABASE_TABLE = "MASTER_TEST_2";

     static final String DATABASE_CREATE =
        "CREATE TABLE MASTER_TEST_2 (BRANCH_ID TEXT," +
							        "USER_ID TEXT," +
							        " EMAIL TEXT," +
							        " CALENDAR TEXT);";
        
    private final Context context;    

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBA_TEST_2(Context ctx) 
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
        	
        }
        

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
		

			
		}
    }    

    //---opens the database---
    public DBA_TEST_2 open() throws SQLException 
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
    		String branchId, 
    		String userId) 
    {
    	 ContentValues initialValues = new ContentValues();
         initialValues.put(BRANCH_ID, branchId);
         initialValues.put(USER_ID, userId);
         
         
         
         return db.insert(DATABASE_TABLE, null, initialValues);
    }
    
    public boolean updateEmail (String emailAgent) {
    	ContentValues initialValues = new ContentValues();
    	initialValues.put("EMAIL", emailAgent);
    	
    	return db.update(DATABASE_TABLE, initialValues, null,  null) > 0;
    }
    
    
    
    public boolean updatePopup ()
    {
    	 ContentValues initialValues = new ContentValues();
         initialValues.put(POPUP, "1");
         
         return db.update(DATABASE_TABLE, initialValues, null, null) > 0;
    }

    public boolean updateStatusLogin () 
    {
    	ContentValues initialValues = new ContentValues();
    	initialValues.put("STATUS", "LOGIN");
    	
    	return db.update(DATABASE_TABLE, initialValues, null, null) > 0 ;
    }
    
    public boolean updateStatusLogout () 
    {
    	ContentValues initialValues = new ContentValues();
    	initialValues.put("STATUS", "LOGOUT");
    	
    	return db.update(DATABASE_TABLE, initialValues, null, null) > 0 ;
    }
    
    public Cursor getStatusLogin () {
    	return db.query(DATABASE_TABLE, new String[] {STATUS}, 
    			null, null, null, null, null);
    }
    
    public boolean delete(String userId) 
    {
        return db.delete(DATABASE_TABLE, USER_ID + "=" + userId, null) > 0;
    }
    
    public boolean deleteAll() 
    {
        return db.delete(DATABASE_TABLE, null, null) > 0;
    }

    //---retrieves all the contacts---
   
    public Cursor getRow() 
    {
    	return db.query(DATABASE_TABLE, new String[] {
    			BRANCH_ID,  //0
        		USER_ID,	//1
        		SIGN_PLACE,	//2
        		MKT_CODE,	//3
        		OFFICE_ID,	//4
        		POPUP,		//5
        		MAX_ROW,	//6
        		STATUS, 	//7
        		EMAIL}, 	//8
    			null, null, null, null, null);
    }
    
    public Cursor getEmailAgent() {
    	return db.query(DATABASE_TABLE, new String[] {EMAIL}, null, null, null, null, null);
    }
    public Cursor getRowByAgent(String userID) 
    {
    	return db.query(DATABASE_TABLE, new String[] {
        		USER_ID,	//0
        		STATUS}, 	//1
    			USER_ID + "='" + userID + "'" , null, null, null, null);
    }
    
    
    
    public int getCount (){
    	Cursor cursor = db.rawQuery("select count(USER_ID) FROM MASTER_AGENT", null);
    	Log.d("curosr count", String.valueOf(cursor.getInt(0)));
    	return cursor.getInt(0);
    }
}
