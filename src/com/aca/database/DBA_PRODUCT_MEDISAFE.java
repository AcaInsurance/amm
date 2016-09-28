package com.aca.database;

import com.aca.amm.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBA_PRODUCT_MEDISAFE {
	public static final String PRODUCT_NAME = "PRODUCT_NAME"; 
	public static final String CUSTOMER_NAME = "CUSTOMER_NAME"; 
	public static final String _id = "_id";
	public static final String PRODUCT_MAIN_ID = "PRODUCT_MAIN_ID";
	public static final String NAMA_PASANGAN = "NAMA_PASANGAN";
	public static final String TGL_LAHIR_PASANGAN = "TGL_LAHIR_PASANGAN";
	public static final String NO_KTP_PASANGAN = "NO_KTP_PASANGAN";
	public static final String NAMA_ANAK_1 = "NAMA_ANAK_1";
	public static final String TGL_LAHIR_ANAK_1 = "TGL_LAHIR_ANAK_1";
	public static final String NO_KTP_ANAK_1 = "NO_KTP_ANAK_1";
	public static final String NAMA_ANAK_2 = "NAMA_ANAK_2";
	public static final String TGL_LAHIR_ANAK_2 = "TGL_LAHIR_ANAK_2";
	public static final String NO_KTP_ANAK_2 = "NO_KTP_ANAK_2";
	public static final String NAMA_ANAK_3 = "NAMA_ANAK_3";
	public static final String TGL_LAHIR_ANAK_3 = "TGL_LAHIR_ANAK_3";
	public static final String NO_KTP_ANAK_3 = "NO_KTP_ANAK_3";
	public static final String TGL_MULAI = "TGL_MULAI";
	public static final String TGL_AKHIR = "TGL_AKHIR";
	public static final String PLAN = "PLAN";
	public static final String PREMI = "PREMI";
	public static final String BIAYA_POLIS = "BIAYA_POLIS";
	public static final String TOTAL = "TOTAL";
	public static final String NOREK = "NOREK";
	public static final String BANK = "BANK";
	public static final String PEMEGANG_REKENING = "PEMEGANG_REKENING";
	
	public static final String PREMI_PASANGAN = "PREMI_PASANGAN";
	public static final String PREMI_ANAK_1 = "PREMI_ANAK_1";
	public static final String PREMI_ANAK_2 = "PREMI_ANAK_2";
	public static final String PREMI_ANAK_3 = "PREMI_ANAK_3";
	
	public static final String IS_PASANGAN = "IS_PASANGAN";
	public static final String IS_ANAK_1 = "IS_ANAK_1";
	public static final String IS_ANAK_2 = "IS_ANAK_2";
	public static final String IS_ANAK_3= "IS_ANAK_3";
	
	public static final String IS_PERAWATAN = "IS_PERAWATAN";
	public static final String IS_PENYAKIT = "IS_PENYAKIT";
	
	private static final String PERAWATAN_DESC = "PERAWATAN_DESC";
	private static final String PENYAKIT_DESC = "PENYAKIT_DESC";
        
	
    private static final String TAG = "DBA_PRODUCT_MEDIASAFE";
    
    private static final String DATABASE_NAME = "AMM_VERSION_2";
    private static final String DATABASE_TABLE = "PRODUCT_MEDISAFE";
     static final String DATABASE_CREATE =
        "CREATE TABLE PRODUCT_MEDISAFE (_id INTEGER PRIMARY KEY, PRODUCT_MAIN_ID NUMERIC, " +
        "NAMA_PASANGAN TEXT, TGL_LAHIR_PASANGAN TEXT, NO_KTP_PASANGAN TEXT, " +
        "NAMA_ANAK_1 TEXT, TGL_LAHIR_ANAK_1 TEXT, NO_KTP_ANAK_1 TEXT, " +
        "NAMA_ANAK_2 TEXT, TGL_LAHIR_ANAK_2 TEXT, NO_KTP_ANAK_2 TEXT, " +
        "NAMA_ANAK_3 TEXT, TGL_LAHIR_ANAK_3 TEXT, NO_KTP_ANAK_3 TEXT, " +
        "TGL_MULAI TEXT, TGL_AKHIR TEXT, " +
        "PLAN NUMERIC, PREMI NUMERIC, BIAYA_POLIS NUMERIC, TOTAL NUMERIC, " +
        "NOREK TEXT, BANK TEXT, PEMEGANG_REKENING TEXT, CUSTOMER_NAME TEXT, PRODUCT_NAME TEXT, " + 
        "PREMI_PASANGAN NUMERIC, PREMI_ANAK_1 NUMERIC, PREMI_ANAK_2 NUMERIC, PREMI_ANAK_3 NUMERIC, " +
        "IS_PASANGAN TEXT, IS_ANAK_1 TEXT, IS_ANAK_2 TEXT, IS_ANAK_3 TEXT," +
        "IS_PERAWATAN NUMERIC, IS_PENYAKIT NUMERIC," +
        "PERAWATAN_DESC TEXT, PENYAKIT_DESC TEXT );";
	
    private final Context context;    

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBA_PRODUCT_MEDISAFE(Context ctx) 
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
            db.execSQL("DROP TABLE IF EXISTS DBA_PRODUCT_MEDISAFE");
            onCreate(db);*/
        }
    }    

    //---opens the database---
    public DBA_PRODUCT_MEDISAFE open() throws SQLException 
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
    public long initialInsert( long product_main_id,
    		String namapasangan, String tgllahirpasangan, String noktppasangan, 
    		String namaanak1, String tgllahiranak1, String noktpanak1, 
    		String namaanak2, String tgllahiranak2, String noktpanak2, 
    		String namaanak3, String tgllahiranak3, String noktpanak3, 
    		String norek, String bank, String pemegangrek, 
    		int plan, String tglmulai, String tglakhir,
    		double premi, double polis, double total, String product_name, String customer_name,
    		double premi_pasangan, double premi_anak_1, double premi_anak_2, double premi_anak_3,
    		String is_pasangan, String is_anak_1, String is_anak_2, String is_anak_3,
    		int is_perawatan, int is_penyakit, String perawatan_desc, String penyakit_desc)  
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(PRODUCT_MAIN_ID, product_main_id);
        initialValues.put(NAMA_PASANGAN, namapasangan);
        initialValues.put(TGL_LAHIR_PASANGAN, tgllahirpasangan);
        initialValues.put(NO_KTP_PASANGAN, noktppasangan);
        initialValues.put(NAMA_ANAK_1, namaanak1);
        initialValues.put(TGL_LAHIR_ANAK_1, tgllahiranak1);
        initialValues.put(NO_KTP_ANAK_1, noktpanak1);
        initialValues.put(NAMA_ANAK_2, namaanak2);
        initialValues.put(TGL_LAHIR_ANAK_2, tgllahiranak2);
        initialValues.put(NO_KTP_ANAK_2, noktpanak2);
        initialValues.put(NAMA_ANAK_3, namaanak3);
        initialValues.put(TGL_LAHIR_ANAK_3, tgllahiranak3);
        initialValues.put(NO_KTP_ANAK_3, noktpanak3);
        initialValues.put(NOREK, norek);
        initialValues.put(BANK, bank);
        initialValues.put(PEMEGANG_REKENING, pemegangrek);
        initialValues.put(TGL_MULAI, tglmulai);
        initialValues.put(TGL_AKHIR, tglakhir);
        initialValues.put(PLAN, plan);
        initialValues.put(PREMI, premi);
        initialValues.put(BIAYA_POLIS, polis);
        initialValues.put(TOTAL, total);
        initialValues.put(PRODUCT_NAME, product_name);
        initialValues.put(CUSTOMER_NAME, customer_name);
        
        initialValues.put(PREMI_PASANGAN, premi_pasangan);
        initialValues.put(PREMI_ANAK_1, premi_anak_1);
        initialValues.put(PREMI_ANAK_2, premi_anak_2);
        initialValues.put(PREMI_ANAK_3, premi_anak_3);
        
        initialValues.put(IS_PASANGAN, is_pasangan);
        initialValues.put(IS_ANAK_1, is_anak_1);
        initialValues.put(IS_ANAK_2, is_anak_2);
        initialValues.put(IS_ANAK_3, is_anak_3);
        
        initialValues.put(IS_PERAWATAN, is_perawatan);
        initialValues.put(IS_PENYAKIT, is_penyakit);
        
        initialValues.put(PERAWATAN_DESC, perawatan_desc);
        initialValues.put(PENYAKIT_DESC, penyakit_desc);
        
        return db.insert(DATABASE_TABLE, null, initialValues);
    }
    
    public boolean nextInsert(long product_main_id,
    		String namapasangan, String tgllahirpasangan, String noktppasangan, 
    		String namaanak1, String tgllahiranak1, String noktpanak1, 
    		String namaanak2, String tgllahiranak2, String noktpanak2, 
    		String namaanak3, String tgllahiranak3, String noktpanak3, 
    		String norek, String bank, String pemegangrek, 
    		int plan, String tglmulai, String tglakhir,
    		double premi, double polis, double total, String product_name, String customer_name,
    		double premi_pasangan, double premi_anak_1, double premi_anak_2, double premi_anak_3,
    		String is_pasangan, String is_anak_1, String is_anak_2, String is_anak_3,
    		int is_perawatan, int is_penyakit, String perawatan_desc, String penyakit_desc) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(PRODUCT_MAIN_ID, product_main_id);
        initialValues.put(NAMA_PASANGAN, namapasangan);
        initialValues.put(TGL_LAHIR_PASANGAN, tgllahirpasangan);
        initialValues.put(NO_KTP_PASANGAN, noktppasangan);
        initialValues.put(NAMA_ANAK_1, namaanak1);
        initialValues.put(TGL_LAHIR_ANAK_1, tgllahiranak1);
        initialValues.put(NO_KTP_ANAK_1, noktpanak1);
        initialValues.put(NAMA_ANAK_2, namaanak2);
        initialValues.put(TGL_LAHIR_ANAK_2, tgllahiranak2);
        initialValues.put(NO_KTP_ANAK_2, noktpanak2);
        initialValues.put(NAMA_ANAK_3, namaanak3);
        initialValues.put(TGL_LAHIR_ANAK_3, tgllahiranak3);
        initialValues.put(NO_KTP_ANAK_3, noktpanak3);
        initialValues.put(NOREK, norek);
        initialValues.put(BANK, bank);
        initialValues.put(PEMEGANG_REKENING, pemegangrek);
        initialValues.put(TGL_MULAI, tglmulai);
        initialValues.put(TGL_AKHIR, tglakhir);
        initialValues.put(PLAN, plan);
        initialValues.put(PREMI, premi);
        initialValues.put(BIAYA_POLIS, polis);
        initialValues.put(TOTAL, total);
        initialValues.put(PRODUCT_NAME, product_name);
        initialValues.put(CUSTOMER_NAME, customer_name);
        
        initialValues.put(PREMI_PASANGAN, premi_pasangan);
        initialValues.put(PREMI_ANAK_1, premi_anak_1);
        initialValues.put(PREMI_ANAK_2, premi_anak_2);
        initialValues.put(PREMI_ANAK_3, premi_anak_3);
        
        initialValues.put(IS_PASANGAN, is_pasangan);
        initialValues.put(IS_ANAK_1, is_anak_1);
        initialValues.put(IS_ANAK_2, is_anak_2);
        initialValues.put(IS_ANAK_3, is_anak_3);
        
        initialValues.put(IS_PERAWATAN, is_perawatan);
        initialValues.put(IS_PENYAKIT, is_penyakit);

        initialValues.put(PERAWATAN_DESC, perawatan_desc);
        initialValues.put(PENYAKIT_DESC, penyakit_desc);
        
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
        		NAMA_PASANGAN,
        		TGL_LAHIR_PASANGAN,
        		NO_KTP_PASANGAN,
        		NAMA_ANAK_1,
        		TGL_LAHIR_ANAK_1,
        		NO_KTP_ANAK_1,
        		NAMA_ANAK_2,
        		TGL_LAHIR_ANAK_2,
        		NO_KTP_ANAK_2,
        		NAMA_ANAK_3,
        		TGL_LAHIR_ANAK_3,
        		NO_KTP_ANAK_3,
        		NOREK,
        		BANK,
        		PEMEGANG_REKENING,
        		TGL_MULAI,
        		TGL_AKHIR,
        		PLAN,
        		PREMI,
        		BIAYA_POLIS,
        		TOTAL,
        		PRODUCT_NAME,
        		CUSTOMER_NAME,
        		PREMI_PASANGAN,
        		PREMI_ANAK_1,
        		PREMI_ANAK_2,
        		PREMI_ANAK_3,
        		IS_PASANGAN,
        		IS_ANAK_1,
        		IS_ANAK_2,
        		IS_ANAK_3,
        		IS_PERAWATAN,
        		IS_PENYAKIT,
        		PERAWATAN_DESC,
        		PENYAKIT_DESC
        		}, null, null, null, null, null);
    }
    
    public Cursor getRow(long product_main_id) 
    {
    	return db.query(DATABASE_TABLE, new String[] {
    			_id,
    			PRODUCT_MAIN_ID,
    			NAMA_PASANGAN,
    			TGL_LAHIR_PASANGAN,
    			NO_KTP_PASANGAN,
    			NAMA_ANAK_1,
    			TGL_LAHIR_ANAK_1,
    			NO_KTP_ANAK_1,
    			NAMA_ANAK_2,
    			TGL_LAHIR_ANAK_2,
    			NO_KTP_ANAK_2,
    			NAMA_ANAK_3,
    			TGL_LAHIR_ANAK_3,
    			NO_KTP_ANAK_3,
        		NOREK,
        		BANK,
        		PEMEGANG_REKENING,
    			TGL_MULAI,
    			TGL_AKHIR,
    			PLAN,
    			PREMI,
    			BIAYA_POLIS,
    			TOTAL,
    			PRODUCT_NAME,
        		CUSTOMER_NAME,
        		PREMI_PASANGAN,
        		PREMI_ANAK_1,
        		PREMI_ANAK_2,
        		PREMI_ANAK_3,
        		IS_PASANGAN,
        		IS_ANAK_1,
        		IS_ANAK_2,
        		IS_ANAK_3,
        		IS_PERAWATAN,
        		IS_PENYAKIT,
        		PERAWATAN_DESC,
        		PENYAKIT_DESC
        		}, 
    			PRODUCT_MAIN_ID + "=" + product_main_id, null, null, null, null);
    }
}
