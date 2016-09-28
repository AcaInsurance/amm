package com.aca.database;

import com.aca.amm.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBA_PRODUCT_ASRI_SYARIAH {
	public static final String PRODUCT_NAME = "PRODUCT_NAME"; 
	public static final String CUSTOMER_NAME = "CUSTOMER_NAME"; 
	public static final String ALAMAT_SAMA  = "ALAMAT_SAMA";
	public static final String _id = "_id";
	public static final String PRODUCT_MAIN_ID  = "PRODUCT_MAIN_ID";
	public static final String LUAS_BANGUNAN  = "LUAS_BANGUNAN";
	public static final String HARGA_BANGUNGAN  = "HARGA_BANGUNGAN";
	public static final String HARGA_PERABOTAN  = "HARGA_PERABOTAN";
	public static final String TOTAL_HARGA  = "TOTAL_HARGA";
	public static final String ALAMAT_PERTANGGUNGAN  = "ALAMAT_PERTANGGUNGAN";
	public static final String KOTA_DESC  = "KOTA_DESC";
	public static final String RW  = "RW";
	public static final String KELURAHAN  = "KELURAHAN";
	public static final String KECAMATAN  = "KECAMATAN";
	public static final String KOTA_PROPINSI  = "KOTA_PROPINSI";
	public static final String KODE_POS  = "KODE_POS";
	public static final String DINDING  = "DINDING";
	public static final String LANTAI  = "LANTAI";
	public static final String ATAP  = "ATAP";
	public static final String KEBAKARAN_BANJIR  = "KEBAKARAN_BANJIR";
	public static final String PENCURIAN  = "PENCURIAN";
	public static final String TANGGAL_MULAI  = "TANGGAL_MULAI";
	public static final String TANGGAL_AKHIR  = "TANGGAL_AKHIR";
	public static final String RATE  = "RATE";
	public static final String PREMI  = "PREMI";
	public static final String POLIS  = "POLIS";
	public static final String MATERAI  = "MATERAI";
	public static final String TOTAL = "TOTAL";
    private static final String TAG = "DBA_PRODUCT_ASRI";
    
    private static final String DATABASE_NAME = "AMM_VERSION_2";
    private static final String DATABASE_TABLE = "PRODUCT_ASRI_SYARIAH";

     static final String DATABASE_CREATE =
        "CREATE TABLE PRODUCT_ASRI_SYARIAH (ALAMAT_SAMA NUMERIC, _id INTEGER PRIMARY KEY, PRODUCT_MAIN_ID NUMERIC, LUAS_BANGUNAN NUMERIC, " +
        "HARGA_BANGUNGAN NUMERIC, HARGA_PERABOTAN NUMERIC, TOTAL_HARGA NUMERIC, ALAMAT_PERTANGGUNGAN TEXT, KOTA_DESC TEXT, RW TEXT, " +
        "KELURAHAN TEXT, KECAMATAN TEXT, KOTA_PROPINSI NUMERIC, KODE_POS TEXT, DINDING TEXT, LANTAI TEXT, ATAP TEXT, " +
        "KEBAKARAN_BANJIR TEXT, PENCURIAN TEXT, TANGGAL_MULAI TEXT, TANGGAL_AKHIR TEXT, RATE NUMERIC, PREMI NUMERIC, POLIS NUMERIC, " +
        "MATERAI NUMERIC, TOTAL NUMERIC, CUSTOMER_NAME TEXT, PRODUCT_NAME TEXT);";
        
    private final Context context;    

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBA_PRODUCT_ASRI_SYARIAH(Context ctx) 
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
            db.execSQL("DROP TABLE IF EXISTS PRODUCT_ASRI");
            onCreate(db);*/
        }
    }    

    //---opens the database---
    public DBA_PRODUCT_ASRI_SYARIAH open() throws SQLException 
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
    public long initialInsert(long product_main_id, String alamat_sama, int luas, double harga_bangunan, double harga_perabot, double total_harga, String alamat, String rt, String rw,
    		String kel, String kec, String kota, String kode_pos, String dinding, String lantai, String atap, String kebakaran, String pencurian,
    		String tgl_mulai, String tgl_akhir, double rate, double premi, double materai, double polis, double total, 
    		String product_name, String customer_name, String kota_desc) 
    {
    	 ContentValues initialValues = new ContentValues();
         initialValues.put(ALAMAT_SAMA, alamat_sama);
         initialValues.put(PRODUCT_MAIN_ID, product_main_id);
         initialValues.put(LUAS_BANGUNAN, luas);
         initialValues.put(HARGA_BANGUNGAN, harga_bangunan);
         initialValues.put(HARGA_PERABOTAN, harga_perabot);
         initialValues.put(TOTAL_HARGA, total_harga);
         initialValues.put(ALAMAT_PERTANGGUNGAN, alamat);
         initialValues.put(KOTA_DESC, kota_desc);
         initialValues.put(RW, rw);
         initialValues.put(KELURAHAN, kel);
         initialValues.put(KECAMATAN, kec);
         initialValues.put(KOTA_PROPINSI, kota);
         initialValues.put(KODE_POS, kode_pos);
         initialValues.put(DINDING, dinding);
         initialValues.put(LANTAI, lantai);
         initialValues.put(ATAP, atap);
         initialValues.put(KEBAKARAN_BANJIR, kebakaran);
         initialValues.put(PENCURIAN, pencurian);
         initialValues.put(TANGGAL_MULAI, tgl_mulai);
         initialValues.put(TANGGAL_AKHIR, tgl_akhir);
         initialValues.put(RATE, rate);
         initialValues.put(PREMI, premi);
         initialValues.put(POLIS, polis);
         initialValues.put(MATERAI, materai);
         initialValues.put(TOTAL, total);
         initialValues.put(PRODUCT_NAME, product_name);
         initialValues.put(CUSTOMER_NAME, customer_name);
         return db.insert(DATABASE_TABLE, null, initialValues);
    }
    
    public boolean nextInsert(long  product_main_id, String alamat_sama, int luas, double harga_bangunan, double harga_perabot, double total_harga, String alamat, String rt, String rw,
    		String kel, String kec, String kota, String kode_pos, String dinding, String lantai, String atap, String kebakaran, String pencurian,
    		String tgl_mulai, String tgl_akhir, double rate, double premi, 
    		double materai, double polis, double total,
    		String product_name, String customer_name, String kota_desc)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(ALAMAT_SAMA, alamat_sama);
        initialValues.put(LUAS_BANGUNAN, luas);
        initialValues.put(HARGA_BANGUNGAN, harga_bangunan);
        initialValues.put(HARGA_PERABOTAN, harga_perabot);
        initialValues.put(TOTAL_HARGA, total_harga);
        initialValues.put(ALAMAT_PERTANGGUNGAN, alamat);
        initialValues.put(KOTA_DESC, kota_desc);
        initialValues.put(RW, rw);
        initialValues.put(KELURAHAN, kel);
        initialValues.put(KECAMATAN, kec);
        initialValues.put(KOTA_PROPINSI, kota);
        initialValues.put(KODE_POS, kode_pos);
        initialValues.put(DINDING, dinding);
        initialValues.put(LANTAI, lantai);
        initialValues.put(ATAP, atap);
        initialValues.put(KEBAKARAN_BANJIR, kebakaran);
        initialValues.put(PENCURIAN, pencurian);
        initialValues.put(TANGGAL_MULAI, tgl_mulai);
        initialValues.put(TANGGAL_AKHIR, tgl_akhir);
        initialValues.put(RATE, rate);
        initialValues.put(PREMI, premi);
        initialValues.put(POLIS, polis);
        initialValues.put(MATERAI, materai);
        initialValues.put(TOTAL, total);
        initialValues.put(PRODUCT_NAME, product_name);
        initialValues.put(CUSTOMER_NAME, customer_name);
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
        		ALAMAT_SAMA,
        		LUAS_BANGUNAN,
        		HARGA_BANGUNGAN,
        		HARGA_PERABOTAN,
        		TOTAL_HARGA,
        		ALAMAT_PERTANGGUNGAN,
        		KOTA_DESC,
        		RW,
        		KELURAHAN,
        		KECAMATAN,
        		KOTA_PROPINSI,
        		KODE_POS,
        		DINDING,
        		LANTAI,
        		ATAP,
        		KEBAKARAN_BANJIR,
        		PENCURIAN,
        		TANGGAL_MULAI,
        		TANGGAL_AKHIR,
        		RATE,
        		PREMI,
        		POLIS,
        		MATERAI,
        		TOTAL,
        		PRODUCT_NAME,
        		CUSTOMER_NAME
        		}, null, null, null, null, null);
    }
    
    public Cursor getRow(long product_main_id) 
    {
    	return db.query(DATABASE_TABLE, new String[] {
    			_id,
    			PRODUCT_MAIN_ID,
    			ALAMAT_SAMA,
    			LUAS_BANGUNAN,
    			HARGA_BANGUNGAN,
    			HARGA_PERABOTAN,
    			TOTAL_HARGA,
    			ALAMAT_PERTANGGUNGAN,
    			KOTA_DESC,
    			RW,
    			KELURAHAN,
    			KECAMATAN,
    			KOTA_PROPINSI,
    			KODE_POS,
    			DINDING,
    			LANTAI,
    			ATAP,
    			KEBAKARAN_BANJIR,
    			PENCURIAN,
    			TANGGAL_MULAI,
    			TANGGAL_AKHIR,
    			RATE,
    			PREMI,
    			POLIS,
    			MATERAI,
    			TOTAL,
    			PRODUCT_NAME,
        		CUSTOMER_NAME
    			}, 
    			PRODUCT_MAIN_ID + "=" + product_main_id, null, null, null, null);
    }
}

