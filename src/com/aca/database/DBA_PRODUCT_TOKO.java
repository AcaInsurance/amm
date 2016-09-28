package com.aca.database;

import com.aca.amm.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBA_PRODUCT_TOKO {
	public static final String PRODUCT_NAME = "PRODUCT_NAME"; 
	public static final String CUSTOMER_NAME = "CUSTOMER_NAME"; 	
	public static final String ALAMAT_SAMA  = "ALAMAT_SAMA";
	public static final String _id = "_id";
	public static final String PRODUCT_MAIN_ID  = "PRODUCT_MAIN_ID";
	public static final String KODE_POS_LOKASI_USAHA  = "KODE_POS_LOKASI_USAHA";
	public static final String SHOPPING_CENTRE  = "SHOPPING_CENTRE";
	public static final String KEPEMILIKAN_BANGUNAN  = "KEPEMILIKAN_BANGUNAN";
	public static final String JENIS_USAHA  = "JENIS_USAHA";
	public static final String GROUP_FRANCHISE  = "GROUP_FRANCHISE";
	public static final String LUAS_BANGUNAN  = "LUAS_BANGUNAN";
	public static final String HARGA_BANGUNAN  = "HARGA_BANGUNAN";
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
	public static final String KEBAKARAN_BANJIR  = "KEBAKARAN";
	public static final String PENCURIAN  = "PENCURIAN";
	public static final String UMUR_BANGUNAN  = "UMUR_BANGUNAN";
	public static final String TANGGAL_MULAI  = "TGL_MULAI";
	public static final String TANGGAL_AKHIR  = "TGL_AKHIR";
	public static final String RATE  = "RATE";
	public static final String PREMI  = "PREMI";
	public static final String POLIS  = "POLIS";
	public static final String MATERAI  = "MATERAI";
	public static final String TOTAL = "TOTAL";
	
	public static final String JENIS_USAHA_DESC = "JENIS_USAHA_DESC";
	public static final String HARGA_STOCK  = "HARGA_STOCK";
	public static final String ADA_SPRINKLER = "ADA_SPRINKLER";
	
    private static final String TAG = "DBA_PRODUCT_TOKO";
    
    private static final String DATABASE_NAME = "AMM_VERSION_2";
    private static final String DATABASE_TABLE = "PRODUCT_TOKO";

     static final String DATABASE_CREATE =
        "CREATE TABLE PRODUCT_TOKO (_id INTEGER PRIMARY KEY, PRODUCT_MAIN_ID NUMERIC, KODE_POS_LOKASI_USAHA TEXT, " +
        "SHOPPING_CENTRE NUMERIC, KEPEMILIKAN_BANGUNAN NUMERIC, JENIS_USAHA TEXT, GROUP_FRANCHISE NUMERIC, " +
        "LUAS_BANGUNAN NUMERIC, HARGA_BANGUNAN NUMERIC, HARGA_PERABOTAN NUMERIC, HARGA_BARANG_DAGANGAN NUMERIC, " +
        "TOTAL_HARGA NUMERIC, ALAMAT_PERTANGGUNGAN TEXT, ALAMAT_SAMA NUMERIC, KOTA_DESC TEXT, RW TEXT, KELURAHAN TEXT, " +
        "KECAMATAN TEXT, KOTA_PROPINSI TEXT, KODE_POS TEXT, DINDING TEXT, LANTAI TEXT, ATAP TEXT, KEBAKARAN TEXT, " +
        "PENCURIAN TEXT, UMUR_BANGUNAN NUMERIC, TGL_MULAI TEXT, TGL_AKHIR TEXT, RATE NUMERIC, PREMI NUMERIC, " +
        "POLIS NUMERIC, MATERAI NUMERIC, TOTAL NUMERIC, CUSTOMER_NAME TEXT, PRODUCT_NAME TEXT, JENIS_USAHA_DESC TEXT, HARGA_STOCK NUMERIC, ADA_SPRINKLER TEXT );";
        
    private final Context context;    

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBA_PRODUCT_TOKO(Context ctx) 
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
            db.execSQL("DROP TABLE IF EXISTS PRODUCT_TOKO");
            onCreate(db);*/
        }
    }    

    //---opens the database---
    public DBA_PRODUCT_TOKO open() throws SQLException 
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
    		String kodeposlokasiusaha, int shoppingcenter,
    		int kepemilikkan, String jenisusaha, int grupfranchise,
    		int alamat_sama, int luas, 
    		double harga_bangunan, double harga_perabot, 
    		double total_harga, String alamat, 
    		String rt, String rw,
    		String kel, String kec, 
    		String kota, String kode_pos, 
    		String dinding, String lantai, 
    		String atap, String kebakaran,
    		String pencurian, int umur, 
    		String tgl_mulai, String tgl_akhir, double rate, double premi, double materai, double polis, double total, 
    		String product_name, String customer_name, String jenis_usaha_desc,
    		double harga_stock, int ada_sprinkler, String kota_desc) 
    {
        ContentValues initialValues = new ContentValues();

        initialValues.put(KODE_POS_LOKASI_USAHA, kodeposlokasiusaha);
    	initialValues.put(SHOPPING_CENTRE, shoppingcenter);
    	initialValues.put(KEPEMILIKAN_BANGUNAN, kepemilikkan);
    	initialValues.put(JENIS_USAHA, jenisusaha);
    	initialValues.put(GROUP_FRANCHISE, grupfranchise);
    	initialValues.put(UMUR_BANGUNAN, umur);
        initialValues.put(ALAMAT_SAMA, alamat_sama);
        initialValues.put(PRODUCT_MAIN_ID, product_main_id);
        initialValues.put(LUAS_BANGUNAN, luas);
        initialValues.put(HARGA_BANGUNAN, harga_bangunan);
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
        initialValues.put(JENIS_USAHA_DESC, jenis_usaha_desc);
        initialValues.put(HARGA_STOCK, harga_stock);
        initialValues.put(ADA_SPRINKLER, ada_sprinkler);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }
    
    public boolean nextInsert(long product_main_id,
    		String kodeposlokasiusaha, int shoppingcenter,
    		int kepemilikkan, String jenisusaha, int grupfranchise,
    		int alamat_sama, int luas, 
    		double harga_bangunan, double harga_perabot, 
    		double total_harga, String alamat, 
    		String rt, String rw,
    		String kel, String kec, 
    		String kota, String kode_pos, 
    		String dinding, String lantai, 
    		String atap, String kebakaran,
    		String pencurian, int umur, 
    		String tgl_mulai, String tgl_akhir, double rate, double premi, double materai, double polis, double total, 
    		String product_name, String customer_name, String jenis_usaha_desc, 
    		double harga_stock, int ada_sprinkler, String kota_desc) 
    {
        ContentValues initialValues = new ContentValues();
        
        initialValues.put(KODE_POS_LOKASI_USAHA, kodeposlokasiusaha);
    	initialValues.put(SHOPPING_CENTRE, shoppingcenter);
    	initialValues.put(KEPEMILIKAN_BANGUNAN, kepemilikkan);
    	initialValues.put(JENIS_USAHA, jenisusaha);
    	initialValues.put(GROUP_FRANCHISE, grupfranchise);
    	initialValues.put(UMUR_BANGUNAN, umur);
        initialValues.put(ALAMAT_SAMA, alamat_sama);
        initialValues.put(PRODUCT_MAIN_ID, product_main_id);
        initialValues.put(LUAS_BANGUNAN, luas);
        initialValues.put(HARGA_BANGUNAN, harga_bangunan);
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
        initialValues.put(JENIS_USAHA_DESC, jenis_usaha_desc);
        initialValues.put(HARGA_STOCK, harga_stock);
        initialValues.put(ADA_SPRINKLER, ada_sprinkler);
        
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
        		LUAS_BANGUNAN,
        		HARGA_BANGUNAN,
        		HARGA_PERABOTAN,
        		HARGA_STOCK,
        		TOTAL_HARGA,
        		ALAMAT_SAMA,
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
        		KODE_POS_LOKASI_USAHA,
        		SHOPPING_CENTRE,
        		KEPEMILIKAN_BANGUNAN,
        		JENIS_USAHA,
        		GROUP_FRANCHISE,
        		UMUR_BANGUNAN, 
        		PRODUCT_NAME, 
        		CUSTOMER_NAME,
        		JENIS_USAHA_DESC,
        		ADA_SPRINKLER}, null, null, null, null, null);
    }
    
    public Cursor getRow(long product_main_id) 
    {
    	return db.query(DATABASE_TABLE, new String[] {
        		_id,
        		PRODUCT_MAIN_ID,
        		LUAS_BANGUNAN,
        		HARGA_BANGUNAN,
        		HARGA_PERABOTAN,
        		HARGA_STOCK,
        		TOTAL_HARGA,
        		ALAMAT_SAMA,
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
    			KODE_POS_LOKASI_USAHA,
        		SHOPPING_CENTRE,
        		KEPEMILIKAN_BANGUNAN,
        		JENIS_USAHA,
        		GROUP_FRANCHISE,
        		UMUR_BANGUNAN, 
        		PRODUCT_NAME, 
        		CUSTOMER_NAME,
        		JENIS_USAHA_DESC,
        		ADA_SPRINKLER}, 
        		PRODUCT_MAIN_ID + "=" + product_main_id, null, null, null, null);
    }
}

