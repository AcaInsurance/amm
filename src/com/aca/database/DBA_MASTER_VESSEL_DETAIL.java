package com.aca.database;

import com.aca.amm.Utility;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.content.Loader.ForceLoadContentObserver;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;
import android.util.Log;


public class DBA_MASTER_VESSEL_DETAIL {
//	private String[] columnName = new String[] {
//										"_id"
//										,"Status" 
//										,"VesselCode"
//										,"VesselType" 
//										,"VesselName"
//										,"VesselType2" 
//										,"Weight" 
//										,"Weight2" 
//										,"Year" 
//										,"Month"
//										,"Length"
//										,"Width"
//										,"Height"
//										,"TotalEngine" 
//										,"Engine" 
//										,"Brand"
//										,"ExName" 
//										,"YOH" 
//										,"Registration" 
//										,"Material" 
//										,"Flag" 
//										,"BLNo"
//										,"Filler"
//	};
//	
	
	public static final String _id = "_id";
    public static final String STATUS = "STATUS";
    public static final String VESSELCODE = "VESSELCODE";
    public static final String VESSELTYPE = "VESSELTYPE";
    public static final String VESSELNAME = "VESSELNAME";
    public static final String VESSELTYPE2 = "VESSELTYPE2";
    public static final String WEIGHT = "WEIGHT";    
    public static final String WEIGHT2 = "WEIGHT2";
    public static final String YEAR = "YEAR";
    public static final String MONTH = "MONTH";
    public static final String LENGTH = "LENGTH";
    public static final String WIDTH = "WIDTH";	
    public static final String HEIGHT = "HEIGHT";
    public static final String TOTALENGINE = "TOTALENGINE";
    public static final String ENGINE = "ENGINE";
    public static final String BRAND = "BRAND";
    public static final String EXNAME = "EXNAME";
    public static final String YOH = "YOH";
    public static final String REGISTRATION = "REGISTRATION";
    public static final String MATERIAL = "MATERIAL";
    public static final String FLAG = "FLAG";
    public static final String BLNO = "BLNO";
    public static final String FILLER = "FILLER";
    
    
    private static final String TAG = "DBA_MASTER_VESSEL_DETAIL";
    
    private static final String DATABASE_NAME = "AMM_VERSION_BIG";
    private static final String DATABASE_TABLE = "MASTER_VESSEL_DETAIL";

    private String createTable = "";    
    
    static final String DATABASE_CREATE = 
    		"CREATE TABLE MASTER_VESSEL_DETAIL (" +
	    					"_id INTEGER PRIMARY KEY" +
							",Status TEXT" + 
							",VesselCode TEXT" +
							",VesselType TEXT" +
							",VesselName TEXT" +
							",VesselType2 TEXT" +
							",Weight TEXT" +
							",Weight2 TEXT" +
							",Year TEXT" +
							",Month TEXT" +
							",Length TEXT" +
							",Width TEXT" +
							",Height TEXT" +
							",TotalEngine TEXT" + 
							",Engine TEXT" +
							",Brand TEXT" +
							",ExName TEXT" +
							",YOH TEXT" +
							",Registration TEXT" +
							",Material TEXT" +
							",Flag TEXT" +
							",BLNo TEXT" +
							",Filler TEXT )";
			    		
    private final Context context;    

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;
    
//    private void createSQLString () {
//    	createTable =  "CREATE TABLE " + DATABASE_TABLE + " ( ";
//    	createTable  =  createTable +  columnName[0] + " INTEGER PRIMARY KEY ";
//    	
//    	for (int i = 0; i < columnName.length; i++) {
//    		createTable = createTable + " , ";
//    		createTable = createTable + columnName[i] + " TEXT";
//    	}
//    	
//    	createTable = createTable + " ) ";
//    	
//    	DATABASE_CREATE = createTable;
//    	
//    	Log.i(TAG, "::enclosing_method:" + "" + DATABASE_CREATE);
//    }

    public DBA_MASTER_VESSEL_DETAIL(Context ctx) 
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
        
//        createSQLString();
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
        	/*
        	try {
        		db.execSQL(DATABASE_CREATE);	
        	} catch (SQLException e) {
        		e.printStackTrace();
        	}
        	*/
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
        {
        	/*
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS MASTER_CITY_PROVINCE");
            onCreate(db);
            */
        }
    }    

    //---opens the database---
    public DBA_MASTER_VESSEL_DETAIL open() throws SQLException 
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
				 String Status 
				,String VesselCode
				,String VesselType 
				,String VesselName
				,String VesselType2 
				,String Weight 
				,String Weight2 
				,String Year 
				,String Month
				,String Length
				,String Width
				,String Height
				,String TotalEngine 
				,String Engine 
				,String Brand
				,String ExName 
				,String YOH 
				,String Registration 
				,String Material 
				,String Flag 
				,String BLNo
				,String Filler	   		) 
    {
        ContentValues initialValues = new ContentValues();
        
        initialValues.put(STATUS, Status);
        initialValues.put(VESSELCODE, VesselCode);
        initialValues.put(VESSELTYPE, VesselType);
        initialValues.put(VESSELNAME, VesselName);
        initialValues.put(VESSELTYPE2, VesselType2);
        initialValues.put(WEIGHT, Weight);        
        initialValues.put(WEIGHT2, Weight2);
        initialValues.put(YEAR, Year);
        initialValues.put(MONTH, Month);
        initialValues.put(LENGTH, Length);
        initialValues.put(WIDTH, Width);      
        initialValues.put(HEIGHT, Height);
        initialValues.put(TOTALENGINE, TotalEngine);
        initialValues.put(ENGINE, Engine);
        initialValues.put(BRAND, Brand);
        initialValues.put(EXNAME, ExName);     
        initialValues.put(this.YOH, YOH);
        initialValues.put(REGISTRATION, Registration);
        initialValues.put(MATERIAL, Material);
        initialValues.put(FLAG, Flag);
        initialValues.put(BLNO, BLNo);
        initialValues.put(FILLER, Filler);
        
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //---deletes a particular contact---
    public boolean deleteAll() 
    {
        return db.delete(DATABASE_TABLE, null, null) > 0;
    }
    
    public Cursor getByVesselName(String name) 
    {
    	return db.query(DATABASE_TABLE,  new String[] {
    			_id			//0
    			,STATUS 	//1
 				,VESSELCODE	//2
 				,VESSELTYPE //3
 				,VESSELNAME	//4
 				,VESSELTYPE2 //5
 				,WEIGHT 	//6
 				,WEIGHT2 	//7
 				,YEAR 		//8
 				,MONTH		//9
 				,LENGTH		//10
 				,WIDTH		//11
 				,HEIGHT		//12
 				,TOTALENGINE//13 
 				,ENGINE 	//14
 				,BRAND		//15
 				,EXNAME	 	//16
 				,YOH		 //17
 				,REGISTRATION //18
 				,MATERIAL 	//19
 				,FLAG 		//20
 				,BLNO		//21
 				,FILLER		//22
    			
    		}, VESSELNAME + " = '" + name + "'", null, null, null, null);    			
    }
    
    public Cursor getByVesselCode(String code) 
    {
    	return db.query(DATABASE_TABLE,  new String[] {
    			_id			//0
    			,STATUS 	//1
 				,VESSELCODE	//2
 				,VESSELTYPE //3
 				,VESSELNAME	//4
 				,VESSELTYPE2 //5
 				,WEIGHT 	//6
 				,WEIGHT2 	//7
 				,YEAR 		//8
 				,MONTH		//9
 				,LENGTH		//10
 				,WIDTH		//11
 				,HEIGHT		//12
 				,TOTALENGINE//13 
 				,ENGINE 	//14
 				,BRAND		//15
 				,EXNAME	 	//16
 				,YOH		 //17
 				,REGISTRATION //18
 				,MATERIAL 	//19
 				,FLAG 		//20
 				,BLNO		//21
 				,FILLER		//22
    			
    		}, VESSELCODE + " = '" + code + "'", null, null, null, null);    			
    }
    
    public Cursor getByID(Long id) 
    {
    	return db.query(DATABASE_TABLE,  new String[] {
    			_id			//0
    			,STATUS 	//1
 				,VESSELCODE	//2
 				,VESSELTYPE //3
 				,VESSELNAME	//4
 				,VESSELTYPE2 //5
 				,WEIGHT 	//6
 				,WEIGHT2 	//7
 				,YEAR 		//8
 				,MONTH		//9
 				,LENGTH		//10
 				,WIDTH		//11
 				,HEIGHT		//12
 				,TOTALENGINE//13 
 				,ENGINE 	//14
 				,BRAND		//15
 				,EXNAME	 	//16
 				,YOH		 //17
 				,REGISTRATION //18
 				,MATERIAL 	//19
 				,FLAG 		//20
 				,BLNO		//21
 				,FILLER		//22
    			
    		}, _id + " = '" + id + "'", null, null, null, null);    			
    }
    
    public Cursor getAll() 
    {
    	return db.query(DATABASE_TABLE,  new String[] {
    			_id			//0
    			,STATUS 	//1
 				,VESSELCODE	//2
 				,VESSELTYPE //3
 				,VESSELNAME	//4
 				,VESSELTYPE2 //5
 				,WEIGHT 	//6
 				,WEIGHT2 	//7
 				,YEAR 		//8
 				,MONTH		//9
 				,LENGTH		//10
 				,WIDTH		//11
 				,HEIGHT		//12
 				,TOTALENGINE//13 
 				,ENGINE 	//14
 				,BRAND		//15
 				,EXNAME	 	//16
 				,YOH		 //17
 				,REGISTRATION //18
 				,MATERIAL 	//19
 				,FLAG 		//20
 				,BLNO		//21
 				,FILLER		//22
    		}, null, null, null, null, null);    			
    }
}
