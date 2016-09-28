package com.aca.database;

import com.aca.amm.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBA_INSURED_GROUP {

	public static final String _id 						= "_id";
	public static final String PRODUCT_MAIN_ID			= "product_main_id";
  
	public static final String NAMA_TERTANGGUNG_1 		= "NAMA_TERTANGGUNG_1";  
	public static final String DOB_1 					= "DOB_1";  
	public static final String PASSPORT_1 				= "PASSPORT_1";  
	  
	public static final String NAMA_TERTANGGUNG_2 		= "NAMA_TERTANGGUNG_2";  
	public static final String DOB_2 					= "DOB_2";  
	public static final String PASSPORT_2 				= "PASSPORT_2";  
	  
	public static final String NAMA_TERTANGGUNG_3 		= "NAMA_TERTANGGUNG_3";  
	public static final String DOB_3 					= "DOB_3";  
	public static final String PASSPORT_3 				= "PASSPORT_3";  
	  
	public static final String NAMA_TERTANGGUNG_4 		= "NAMA_TERTANGGUNG_4";  
	public static final String DOB_4 					= "DOB_4";  
	public static final String PASSPORT_4 				= "PASSPORT_4";  
	  
	public static final String NAMA_TERTANGGUNG_5 		= "NAMA_TERTANGGUNG_5";  
	public static final String DOB_5 					= "DOB_5";  
	public static final String PASSPORT_5 				= "PASSPORT_5";  
	  
	public static final String NAMA_TERTANGGUNG_6 		= "NAMA_TERTANGGUNG_6";  
	public static final String DOB_6 					= "DOB_6";  
	public static final String PASSPORT_6 				= "PASSPORT_6";  
	  
	public static final String NAMA_TERTANGGUNG_7 		= "NAMA_TERTANGGUNG_7";  
	public static final String DOB_7 					= "DOB_7";  
	public static final String PASSPORT_7 				= "PASSPORT_7";  
	  
	public static final String NAMA_TERTANGGUNG_8 		= "NAMA_TERTANGGUNG_8";  
	public static final String DOB_8					= "DOB_8";  
	public static final String PASSPORT_8 				= "PASSPORT_8";  
	  
	public static final String NAMA_TERTANGGUNG_9 		= "NAMA_TERTANGGUNG_9";  
	public static final String DOB_9 					= "DOB_9";  
	public static final String PASSPORT_9 				= "PASSPORT_9";  
	  
	public static final String NAMA_TERTANGGUNG_10 		= "NAMA_TERTANGGUNG_10";  
	public static final String DOB_10 					= "DOB_10";  
	public static final String PASSPORT_10 				= "PASSPORT_10";  

	public static final String IS_INSURED_1				= "IS_INSURED_1";
	public static final String IS_INSURED_2				= "IS_INSURED_2";
	public static final String IS_INSURED_3				= "IS_INSURED_3";
	public static final String IS_INSURED_4				= "IS_INSURED_4";
	public static final String IS_INSURED_5				= "IS_INSURED_5";
	public static final String IS_INSURED_6				= "IS_INSURED_6";
	public static final String IS_INSURED_7				= "IS_INSURED_7";
	public static final String IS_INSURED_8				= "IS_INSURED_8";
	public static final String IS_INSURED_9				= "IS_INSURED_9";
	public static final String IS_INSURED_10			= "IS_INSURED_10";
	
	public static final String INSURED_COUNT			= "INSURED_COUNT";
			
	public static final String CRE_DATE 				= "CRE_DATE";  
	public static final String MOD_DATE 				= "MOD_DATE";
	
	 
		
    private static final String TAG = "DBA_INSURED_GROUP";
    
    private static final String DATABASE_NAME = "AMM_VERSION_2";
    private static final String DATABASE_TABLE = "INSURED_GROUP";

     static final String DATABASE_CREATE =
        "CREATE TABLE "+ DATABASE_TABLE + "(" +
	        "_id INTEGER PRIMARY KEY" + 
	        ",product_main_id TEXT" +  
	        ",NAMA_TERTANGGUNG_1 TEXT, DOB_1 TEXT, PASSPORT_1 TEXT " + 
	        ",NAMA_TERTANGGUNG_2 TEXT, DOB_2 TEXT, PASSPORT_2 TEXT " + 
	        ",NAMA_TERTANGGUNG_3 TEXT, DOB_3 TEXT, PASSPORT_3 TEXT " + 
	        ",NAMA_TERTANGGUNG_4 TEXT, DOB_4 TEXT, PASSPORT_4 TEXT " + 
	        ",NAMA_TERTANGGUNG_5 TEXT, DOB_5 TEXT, PASSPORT_5 TEXT " + 
	        ",NAMA_TERTANGGUNG_6 TEXT, DOB_6 TEXT, PASSPORT_6 TEXT " + 
	        ",NAMA_TERTANGGUNG_7 TEXT, DOB_7 TEXT, PASSPORT_7 TEXT " + 
	        ",NAMA_TERTANGGUNG_8 TEXT, DOB_8 TEXT, PASSPORT_8 TEXT " + 
	        ",NAMA_TERTANGGUNG_9 TEXT, DOB_9 TEXT, PASSPORT_9 TEXT " + 
	        ",NAMA_TERTANGGUNG_10 TEXT, DOB_10 TEXT, PASSPORT_10 TEXT " + 
	        ",IS_INSURED_1 NUMERIC" +
	        ",IS_INSURED_2 NUMERIC" +
	        ",IS_INSURED_3 NUMERIC" +
	        ",IS_INSURED_4 NUMERIC" +
	        ",IS_INSURED_5 NUMERIC" +
	        ",IS_INSURED_6 NUMERIC" +
	        ",IS_INSURED_7 NUMERIC" +
	        ",IS_INSURED_8 NUMERIC" +
	        ",IS_INSURED_9 NUMERIC" +
	        ",IS_INSURED_10 NUMERIC" +
	        ",INSURED_COUNT NUMERIC" +
	        ",CRE_DATE TEXT" +
	        ",MOD_DATE TEXT" +
	        ")";

        
    private final Context context;    

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBA_INSURED_GROUP(Context ctx) 
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
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
        { 
        	
        }
    }    

    //---opens the database---
    public DBA_INSURED_GROUP open() throws SQLException 
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
    public long initialInsert(  
    			String nama1, String dob1, String passport1,
    			String nama2, String dob2, String passport2,
    			String nama3, String dob3, String passport3,
    			String nama4, String dob4, String passport4,
    			String nama5, String dob5, String passport5,
    			String nama6, String dob6, String passport6,
    			String nama7, String dob7, String passport7,
    			String nama8, String dob8, String passport8,
    			String nama9, String dob9, String passport9,
    			String nama10, String dob10, String passport10,
    			int isInsured1, int isInsured2,
    			int isInsured3, int isInsured4,
    			int isInsured5, int isInsured6,
    			int isInsured7, int isInsured8,
    			int isInsured9, int isInsured10,
    			int insuredCount
    		)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(PRODUCT_MAIN_ID, "");
        
        initialValues.put(NAMA_TERTANGGUNG_1, nama1);
        initialValues.put(NAMA_TERTANGGUNG_2, nama2);
        initialValues.put(NAMA_TERTANGGUNG_3, nama3);
        initialValues.put(NAMA_TERTANGGUNG_4, nama4);
        initialValues.put(NAMA_TERTANGGUNG_5, nama5);
        initialValues.put(NAMA_TERTANGGUNG_6, nama6);
        initialValues.put(NAMA_TERTANGGUNG_7, nama7);
        initialValues.put(NAMA_TERTANGGUNG_8, nama8);
        initialValues.put(NAMA_TERTANGGUNG_9, nama9);
        initialValues.put(NAMA_TERTANGGUNG_10, nama10); 
          
        initialValues.put(DOB_1, dob1);
        initialValues.put(DOB_2, dob2);
        initialValues.put(DOB_3, dob3);
        initialValues.put(DOB_4, dob4);
        initialValues.put(DOB_5, dob5);
        initialValues.put(DOB_6, dob6);
        initialValues.put(DOB_7, dob7);
        initialValues.put(DOB_8, dob8);
        initialValues.put(DOB_9, dob9);
        initialValues.put(DOB_10, dob10); 
         
        initialValues.put(PASSPORT_1, passport1); 
        initialValues.put(PASSPORT_2, passport2);
        initialValues.put(PASSPORT_3, passport3);
        initialValues.put(PASSPORT_4, passport4);
        initialValues.put(PASSPORT_5, passport5);
        initialValues.put(PASSPORT_6, passport6);
        initialValues.put(PASSPORT_7, passport7);
        initialValues.put(PASSPORT_8, passport8);
        initialValues.put(PASSPORT_9, passport9);
        initialValues.put(PASSPORT_10, passport10); 
        

        initialValues.put(IS_INSURED_1, isInsured1); 
        initialValues.put(IS_INSURED_2, isInsured2);
        initialValues.put(IS_INSURED_3, isInsured3);
        initialValues.put(IS_INSURED_4, isInsured4);
        initialValues.put(IS_INSURED_5, isInsured5);
        initialValues.put(IS_INSURED_6, isInsured6);
        initialValues.put(IS_INSURED_7, isInsured7);
        initialValues.put(IS_INSURED_8, isInsured8);
        initialValues.put(IS_INSURED_9, isInsured9);
        initialValues.put(IS_INSURED_10, isInsured10); 
        
        initialValues.put(INSURED_COUNT, insuredCount); 
        
        initialValues.put(CRE_DATE, Utility.GetTodayString()); 
        initialValues.put(MOD_DATE, Utility.GetTodayString()); 
        
        return db.insert(DATABASE_TABLE, null, initialValues);
    }
    public boolean nextInsert(Long product_main_id,
			String nama1, String dob1, String passport1,
			String nama2, String dob2, String passport2,
			String nama3, String dob3, String passport3,
			String nama4, String dob4, String passport4,
			String nama5, String dob5, String passport5,
			String nama6, String dob6, String passport6,
			String nama7, String dob7, String passport7,
			String nama8, String dob8, String passport8,
			String nama9, String dob9, String passport9,
			String nama10, String dob10, String passport10,
			int isInsured1, int isInsured2,
			int isInsured3, int isInsured4,
			int isInsured5, int isInsured6,
			int isInsured7, int isInsured8,
			int isInsured9, int isInsured10,
			int insuredCount )
	{
	    ContentValues initialValues = new ContentValues();
	    initialValues.put(PRODUCT_MAIN_ID, "");
	    
	    initialValues.put(NAMA_TERTANGGUNG_1, nama1);
	    initialValues.put(NAMA_TERTANGGUNG_2, nama2);
	    initialValues.put(NAMA_TERTANGGUNG_3, nama3);
	    initialValues.put(NAMA_TERTANGGUNG_4, nama4);
	    initialValues.put(NAMA_TERTANGGUNG_5, nama5);
	    initialValues.put(NAMA_TERTANGGUNG_6, nama6);
	    initialValues.put(NAMA_TERTANGGUNG_7, nama7);
	    initialValues.put(NAMA_TERTANGGUNG_8, nama8);
	    initialValues.put(NAMA_TERTANGGUNG_9, nama9);
	    initialValues.put(NAMA_TERTANGGUNG_10, nama10); 
	      
	    initialValues.put(DOB_1, dob1);
	    initialValues.put(DOB_2, dob2);
	    initialValues.put(DOB_3, dob3);
	    initialValues.put(DOB_4, dob4);
	    initialValues.put(DOB_5, dob5);
	    initialValues.put(DOB_6, dob6);
	    initialValues.put(DOB_7, dob7);
	    initialValues.put(DOB_8, dob8);
	    initialValues.put(DOB_9, dob9);
	    initialValues.put(DOB_10, dob10); 
	     
	    initialValues.put(PASSPORT_1, passport1); 
	    initialValues.put(PASSPORT_2, passport2);
	    initialValues.put(PASSPORT_3, passport3);
	    initialValues.put(PASSPORT_4, passport4);
	    initialValues.put(PASSPORT_5, passport5);
	    initialValues.put(PASSPORT_6, passport6);
	    initialValues.put(PASSPORT_7, passport7);
	    initialValues.put(PASSPORT_8, passport8);
	    initialValues.put(PASSPORT_9, passport9);
	    initialValues.put(PASSPORT_10, passport10); 
	    
        initialValues.put(IS_INSURED_1, isInsured1); 
        initialValues.put(IS_INSURED_2, isInsured2);
        initialValues.put(IS_INSURED_3, isInsured3);
        initialValues.put(IS_INSURED_4, isInsured4);
        initialValues.put(IS_INSURED_5, isInsured5);
        initialValues.put(IS_INSURED_6, isInsured6);
        initialValues.put(IS_INSURED_7, isInsured7);
        initialValues.put(IS_INSURED_8, isInsured8);
        initialValues.put(IS_INSURED_9, isInsured9);
        initialValues.put(IS_INSURED_10, isInsured10); 

        initialValues.put(INSURED_COUNT, insuredCount);
        
	    initialValues.put(MOD_DATE, Utility.GetTodayString()); 

        return db.update(DATABASE_TABLE, initialValues, _id + "=" + product_main_id, null) > 0;
	}

    //---deletes a particular contact---
    public boolean delete(long product_main_id) 
    {
        return db.delete(DATABASE_TABLE, _id + "=" + product_main_id, null) > 0;
    }
    
    public boolean deleteAll() 
    {
        return db.delete(DATABASE_TABLE, null, null) > 0;
    }

    //---retrieves all the contacts---
    public Cursor getRows() 
    {
        return db.query(DATABASE_TABLE, new String[] {
        		 _id,														//0
        		 PRODUCT_MAIN_ID, 											//1
        		 NAMA_TERTANGGUNG_1 , DOB_1 , PASSPORT_1   					//2
    	        ,NAMA_TERTANGGUNG_2 , DOB_2 , PASSPORT_2   						//3
    	        ,NAMA_TERTANGGUNG_3 , DOB_3 , PASSPORT_3   						//4
    	        ,NAMA_TERTANGGUNG_4 , DOB_4 , PASSPORT_4   						//5
    	        ,NAMA_TERTANGGUNG_5 , DOB_5 , PASSPORT_5   						//6
    	        ,NAMA_TERTANGGUNG_6 , DOB_6 , PASSPORT_6   						//7
    	        ,NAMA_TERTANGGUNG_7 , DOB_7 , PASSPORT_7   						//8
    	        ,NAMA_TERTANGGUNG_8 , DOB_8 , PASSPORT_8   						//9
    	        ,NAMA_TERTANGGUNG_9 , DOB_9 , PASSPORT_9   						//10
    	        ,NAMA_TERTANGGUNG_10 , DOB_10 , PASSPORT_10   					//11
    	        ,CRE_DATE 			//32
    	        ,MOD_DATE 			//33
    	        ,IS_INSURED_1		//34
    	        ,IS_INSURED_2		//35
    	        ,IS_INSURED_3		//36
    	        ,IS_INSURED_4		//37
    	        ,IS_INSURED_5		//38
    	        ,IS_INSURED_6		//39
    	        ,IS_INSURED_7		//40
    	        ,IS_INSURED_8		//41
    	        ,IS_INSURED_9		//42
    	        ,IS_INSURED_10		//43
    	        ,INSURED_COUNT		//44


    	        

        			
        }, null, null, null, null, null);
        
        
        
    }
    
    public Cursor getRow(Long id) 
    {
    	return db.query(DATABASE_TABLE, new String[] {
    			 _id,														//0
        		 PRODUCT_MAIN_ID, 											//1
        		 NAMA_TERTANGGUNG_1 , DOB_1 , PASSPORT_1   					//2
    	        ,NAMA_TERTANGGUNG_2 , DOB_2 , PASSPORT_2   						//3
    	        ,NAMA_TERTANGGUNG_3 , DOB_3 , PASSPORT_3   						//4
    	        ,NAMA_TERTANGGUNG_4 , DOB_4 , PASSPORT_4   						//5
    	        ,NAMA_TERTANGGUNG_5 , DOB_5 , PASSPORT_5   						//6
    	        ,NAMA_TERTANGGUNG_6 , DOB_6 , PASSPORT_6   						//7
    	        ,NAMA_TERTANGGUNG_7 , DOB_7 , PASSPORT_7   						//8
    	        ,NAMA_TERTANGGUNG_8 , DOB_8 , PASSPORT_8   						//9
    	        ,NAMA_TERTANGGUNG_9 , DOB_9 , PASSPORT_9   						//10
    	        ,NAMA_TERTANGGUNG_10 , DOB_10 , PASSPORT_10   					//11
    	        ,CRE_DATE 			//32
    	        ,MOD_DATE 			//33
    	        ,IS_INSURED_1		//34
    	        ,IS_INSURED_2		//35
    	        ,IS_INSURED_3		//36
    	        ,IS_INSURED_4		//37
    	        ,IS_INSURED_5		//38
    	        ,IS_INSURED_6		//39
    	        ,IS_INSURED_7		//40
    	        ,IS_INSURED_8		//41
    	        ,IS_INSURED_9		//42
    	        ,IS_INSURED_10		//43
    	        ,INSURED_COUNT		//44

    	}, _id + "=" + id, null, null, null, null);
    }
}
