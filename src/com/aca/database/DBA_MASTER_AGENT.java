package com.aca.database;

import com.aca.amm.Utility;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBA_MASTER_AGENT {
	public static final String BRANCH_ID = "BRANCH_ID"; 
	public static final String USER_ID = "USER_ID"; 
	public static final String OFFICE_ID  = "OFFICE_ID";
	public static final String MKT_CODE  = "MKT_CODE";
	public static final String SIGN_PLACE  = "SIGN_PLACE";
	public static final String POPUP = "POPUP";
	public static final String MAX_ROW = "MAX_ROW";
	public static final String STATUS = "STATUS";
	
	public static final String STATUS_USER = "STATUS_USER";
	public static final String USER_EXP_DATE = "USER_EXP_DATE";
	public static final String EMAIL_ADDRESS = "EMAIL_ADDRESS";
	public static final String PHONE_NO = "PHONE_NO";
	public static final String USER_NAME = "USER_NAME";
	public static final String U_NAME = "U_NAME";
	public static final String U_PASS = "U_PASS";
	
	
	
	
	private static final String DATABASE_NAME = "AMM_VERSION_2";
	private static final String DATABASE_TABLE = "MASTER_AGENT";

    static final String DATABASE_CREATE =
        "CREATE TABLE MASTER_AGENT (BRANCH_ID TEXT," +
							        "USER_ID TEXT," +
							        "OFFICE_ID TEXT," +
							        "MKT_CODE TEXT," +
							        "SIGN_PLACE TEXT, " +
							        "POPUP TEXT, " +
							        "MAX_ROW NUMERIC," +
							        "STATUS TEXT, " +
							        "STATUS_USER TEXT, " +
							        "USER_EXP_DATE TEXT, " +
							        "EMAIL_ADDRESS TEXT, " +
							        "PHONE_NO TEXT, " +
							        "USER_NAME TEXT, " +
							        "U_NAME TEXT, " +
							        "U_PASS TEXT " +
							        
							        ");";
        
    private final Context context;    

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBA_MASTER_AGENT(Context ctx) 
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
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
//			
			
		}
    }    

    //---opens the database---
    public DBA_MASTER_AGENT open() throws SQLException 
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
    		String userId, 
    		String signPlace, 
    		String mktCode, 
    		String officeId, 
    		String MaxRow,
    		String statusUser,
    		String userExpDate,
    		String email,
    		String phoneNo,
    		String userName,
    		String uName, 
    		String uPass
    		) 
    {
    	 ContentValues initialValues = new ContentValues();
         initialValues.put(BRANCH_ID, branchId);
         initialValues.put(USER_ID, userId);
         initialValues.put(SIGN_PLACE, signPlace);
         initialValues.put(MKT_CODE, mktCode);
         initialValues.put(OFFICE_ID, officeId);
         initialValues.put(POPUP, "0");
         initialValues.put(MAX_ROW, MaxRow);
         initialValues.put(STATUS, "LOGIN");
         
         initialValues.put(STATUS_USER, statusUser );
         initialValues.put(USER_EXP_DATE, userExpDate );
         initialValues.put(EMAIL_ADDRESS, email);
         initialValues.put(PHONE_NO, phoneNo);
         initialValues.put(USER_NAME, userName);
         initialValues.put(U_NAME, uName);
         initialValues.put(U_PASS, uPass);
         
         
         
         
         return db.insert(DATABASE_TABLE, null, initialValues);
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
        		STATUS_USER, 	//8
        		USER_EXP_DATE, 	//9
        		EMAIL_ADDRESS, 	//10
        		PHONE_NO, 	//11
        		USER_NAME, 	//12
        		U_NAME 		,//13
                U_PASS
        		
        		
        		}, 	
    			null, null, null, null, null);
    }
    public int getExistingUser (String loginID) {
    	return db.query(DATABASE_TABLE, 
    			new String[] {USER_ID}, 
    			USER_ID 		+ "='" + loginID  + "' OR " +
    			EMAIL_ADDRESS 	+ "='" + loginID  + "' OR " +
    			USER_NAME 		+ "='" + loginID  + "'"
    			,	null, null, null, null, null).getCount();
    }
    

    public Cursor getRowByAgent(String userID) 
    {
    	return db.query(DATABASE_TABLE, new String[] {
        		USER_ID,	//0
        		STATUS}, 	//1
    			USER_ID + "='" + userID + "'" , null, null, null, null);
    }
    
    public int login (String userID, String hashPassword)  
    {
    	Cursor cursor = db.query(
			    			 DATABASE_TABLE
			    			,new String[] {USER_ID}
			    			,USER_ID + "='" + userID + "' AND " +
			    			 U_PASS  + "='" + hashPassword + "'"
			    			,null, null, null, null, null);
    	
    	return cursor.getCount();
    }
    
    public boolean updateUsername(String newUsername) {
    	ContentValues initialValues = new ContentValues();
    	initialValues.put(USER_NAME, newUsername);
    	
    	return db.update(DATABASE_TABLE, initialValues, null, null) > 0 ;
	}
    
    public boolean updatePassword (String newPassword) {
    	ContentValues initialValues = new ContentValues();
    	initialValues.put(U_PASS, newPassword);
    	
    	return db.update(DATABASE_TABLE, initialValues, null, null) > 0 ;
	}
    
    
    public int getCount (){
    	Cursor cursor = db.rawQuery("select count(USER_ID) FROM MASTER_AGENT", null);
    	Log.d("curosr count", String.valueOf(cursor.getInt(0)));
    	return cursor.getInt(0);
    }
}

