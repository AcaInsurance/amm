package com.aca.database;

import com.aca.amm.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBA_PRODUCT_TRAVEL_SAFE {
	public static final String PRODUCT_NAME 			= "PRODUCT_NAME"; 
	public static final String CUSTOMER_NAME 			= "CUSTOMER_NAME"; 
	public static final String _id 						= "_id";
	public static final String PRODUCT_MAIN_ID 			= "PRODUCT_MAIN_ID";
	public static final String NAMA_PASANGAN 			= "NAMA_PASANGAN";
	public static final String TGL_LAHIR_PASANGAN 		= "TGL_LAHIR_PASANGAN";
	public static final String NO_PASSPORT_PASANGAN 	= "NO_PASSPORT_PASANGAN";
	public static final String NAMA_ANAK_1 				= "NAMA_ANAK_1";
	public static final String TGL_LAHIR_ANAK_1 		= "TGL_LAHIR_ANAK_1";
	public static final String NO_PASSPORT_ANAK_1 		= "NO_PASSPORT_ANAK_1";
	public static final String NAMA_ANAK_2 				= "NAMA_ANAK_2";
	public static final String TGL_LAHIR_ANAK_2 		= "TGL_LAHIR_ANAK_2";
	public static final String NO_PASSPORT_ANAK_2 		= "NO_PASSPORT_ANAK_2";
	public static final String NAMA_ANAK_3 				= "NAMA_ANAK_3";
	public static final String TGL_LAHIR_ANAK_3 		= "TGL_LAHIR_ANAK_3";
	public static final String NO_PASSPORT_ANAK_3 		= "NO_PASSPORT_ANAK_3";
	public static final String TUJUAN_PERJALANAN 		= "TUJUAN_PERJALANAN";
	public static final String NEGARA_TUJUAN 			= "NEGARA_TUJUAN";
	public static final String NAMA_NEGARA 				= "NAMA_NEGARA";
	public static final String AHLI_WARIS 				= "AHLI_WARIS";
	public static final String HUBUNGAN 				= "HUBUNGAN";
	public static final String TGL_KEBERANGKATAN 		= "TGL_KEBERANGKATAN";
	public static final String TGL_KEMBALI 				= "TGL_KEMBALI";
	public static final String PLAN 					= "PLAN";
	public static final String JUMLAH_HARI_DIPERTANGGUNGKAN = "JUMLAH_HARI_DIPERTANGGUNGKAN";
	public static final String TAMBAHAN_PER_MINGGU 		= "TAMBAHAN_PER_MINGGU";
	public static final String LOADING_PREMI 			= "LOADING_PREMI";
	public static final String PREMI 					= "PREMI";
	public static final String BIAYA_POLIS 				= "BIAYA_POLIS";
	public static final String TOTAL 					= "TOTAL";
	public static final String FAMILY 					= "FAMILY";

	public static final String PREMI_PASANGAN = "PREMI_PASANGAN";
	public static final String PREMI_ANAK_1 = "PREMI_ANAK_1";
	public static final String PREMI_ANAK_2 = "PREMI_ANAK_2";
	public static final String PREMI_ANAK_3 = "PREMI_ANAK_3";
	
	public static final String IS_PASANGAN = "IS_PASANGAN";
	public static final String IS_ANAK_1 = "IS_ANAK_1";
	public static final String IS_ANAK_2 = "IS_ANAK_2";
	public static final String IS_ANAK_3= "IS_ANAK_3";
	
	public static final String KODE_NEGARA = "KODE_NEGARA";
	public static final String IS_ANNUAL = "IS_ANNUAL";
	public static final String EXCH_RATE= "EXCH_RATE";
	
	public static final String ACOD 					= "ACOD";
	public static final String CCOD 					= "CCOD";
	public static final String DCOD 					= "DCOD";
	public static final String PREMIDAYS 				= "PREMIDAYS";
	public static final String PREMIWEEKS 				= "PREMIWEEKS";
	public static final String MAXBENEFIT 				= "MAXBENEFIT";
	public static final String TOTALDAYS 				= "TOTALDAYS";
	public static final String TOTALWEEKS 				= "TOTALWEEKS";
	
	public static final String PREMI_ALOKASI 	    	= "PREMI_ALOKASI"; 
	public static final String NO_PASSPORT 	    		= "NO_PASSPORT";
	
	 

	public static final String NAMA_TERTANGGUNG_5 = "NAMA_TERTANGGUNG_5";
	public static final String TGL_LAHIR_5 		  = "TGL_LAHIR_5";
	public static final String NO_PASSPORT_5 	  = "NO_PASSPORT_5";
	

	public static final String NAMA_TERTANGGUNG_6 = "NAMA_TERTANGGUNG_6";
	public static final String TGL_LAHIR_6 		  = "TGL_LAHIR_6";
	public static final String NO_PASSPORT_6 	  = "NO_PASSPORT_6";
	

	public static final String NAMA_TERTANGGUNG_7 = "NAMA_TERTANGGUNG_7";
	public static final String TGL_LAHIR_7 		  = "TGL_LAHIR_7";
	public static final String NO_PASSPORT_7 	  = "NO_PASSPORT_7";
	

	public static final String NAMA_TERTANGGUNG_8 = "NAMA_TERTANGGUNG_8";
	public static final String TGL_LAHIR_8 		  = "TGL_LAHIR_8";
	public static final String NO_PASSPORT_8 	  = "NO_PASSPORT_8";
	

	public static final String NAMA_TERTANGGUNG_9 = "NAMA_TERTANGGUNG_9";
	public static final String TGL_LAHIR_9 		  = "TGL_LAHIR_9";
	public static final String NO_PASSPORT_9 	  = "NO_PASSPORT_9";
	

	public static final String NAMA_TERTANGGUNG_10 = "NAMA_TERTANGGUNG_10";
	public static final String TGL_LAHIR_10 	  = "TGL_LAHIR_10";
	public static final String NO_PASSPORT_10 	  = "NO_PASSPORT_10";
	

	public static final String PREMI_4				= "PREMI_4";
	public static final String PREMI_5				= "PREMI_5";
	public static final String PREMI_6				= "PREMI_6";
	public static final String PREMI_7				= "PREMI_7";
	public static final String PREMI_8				= "PREMI_8";
	public static final String PREMI_9				= "PREMI_9";
	public static final String PREMI_10			    = "PREMI_10";
	 
	public static final String IS_INSURED_4				= "IS_INSURED_4";
	public static final String IS_INSURED_5				= "IS_INSURED_5";
	public static final String IS_INSURED_6				= "IS_INSURED_6";
	public static final String IS_INSURED_7				= "IS_INSURED_7";
	public static final String IS_INSURED_8				= "IS_INSURED_8";
	public static final String IS_INSURED_9				= "IS_INSURED_9";
	public static final String IS_INSURED_10			= "IS_INSURED_10";
	
	public static final String NAMA_NEGARA_2		    = "NAMA_NEGARA_2";
	public static final String NAMA_NEGARA_3		    = "NAMA_NEGARA_3";
	public static final String NAMA_NEGARA_4		    = "NAMA_NEGARA_4";
	public static final String NAMA_NEGARA_5		    = "NAMA_NEGARA_5";
	public static final String NAMA_NEGARA_6		    = "NAMA_NEGARA_6";
	
	public static final String KODE_NEGARA_2		    = "KODE_NEGARA_2";
	public static final String KODE_NEGARA_3		    = "KODE_NEGARA_3";
	public static final String KODE_NEGARA_4		    = "KODE_NEGARA_4";
	public static final String KODE_NEGARA_5		    = "KODE_NEGARA_5";
	public static final String KODE_NEGARA_6		    = "KODE_NEGARA_6";

	public static final String INSURED_GROUP_ID		    = "INSURED_GROUP_ID";
	public static final String GROUP_NAME			    = "GROUP_NAME";
	public static final String INSURED_GROUP_COUNT		= "INSURED_GROUP_COUNT";
	
	
	public static final String TAG = "DBA_PRODUCT_TRAVEL_SAFE";
    
	public static final String DATABASE_NAME = "AMM_VERSION_2";
	public static final String DATABASE_TABLE = "PRODUCT_TRAVEL_SAFE";

     static final String DATABASE_CREATE =
        "CREATE TABLE PRODUCT_TRAVEL_SAFE (_id INTEGER PRIMARY KEY, PRODUCT_MAIN_ID NUMERIC, NAMA_PASANGAN TEXT, " +
        "TGL_LAHIR_PASANGAN TEXT, NO_PASSPORT_PASANGAN TEXT, NAMA_ANAK_1 TEXT, TGL_LAHIR_ANAK_1 TEXT, " +
        "NO_PASSPORT_ANAK_1 TEXT, NAMA_ANAK_2 TEXT, TGL_LAHIR_ANAK_2 TEXT, NO_PASSPORT_ANAK_2 TEXT, " +
        "NAMA_ANAK_3 TEXT, TGL_LAHIR_ANAK_3 TEXT, NO_PASSPORT_ANAK_3 TEXT, TUJUAN_PERJALANAN TEXT, " +
        "NEGARA_TUJUAN TEXT, NAMA_NEGARA TEXT, AHLI_WARIS TEXT, HUBUNGAN TEXT, TGL_KEBERANGKATAN TEXT, " +
        "TGL_KEMBALI TEXT, PLAN NUMERIC, JUMLAH_HARI_DIPERTANGGUNGKAN NUMERIC, TAMBAHAN_PER_MINGGU NUMERIC, " +
        "LOADING_PREMI NUMERIC, PREMI NUMERIC, BIAYA_POLIS NUMERIC, TOTAL NUMERIC, FAMILY TEXT, CUSTOMER_NAME TEXT, PRODUCT_NAME TEXT, " + 
        "PREMI_PASANGAN NUMERIC, PREMI_ANAK_1 NUMERIC, PREMI_ANAK_2 NUMERIC, PREMI_ANAK_3 NUMERIC, " +
        "IS_PASANGAN TEXT, IS_ANAK_1 TEXT, IS_ANAK_2 TEXT, IS_ANAK_3 TEXT,KODE_NEGARA TEXT, IS_ANNUAL TEXT" +
        ", EXCH_RATE NUMERIC, " +
        "ACOD NUMBERIC, " +
        "CCOD NUMERIC," +
        "DCOD NUMERIC," +
        "PREMIDAYS NUMERIC," +
        "PREMIWEEKS NUMERIC," +
        "MAXBENEFIT NUMERIC," +
        "TOTALDAYS NUMERIC," +
        "TOTALWEEKS NUMERIC, " +
        "PREMI_ALOKASI NUMERIC, " +
        "NO_PASSPORT TEXT," + 
        "NAMA_TERTANGGUNG_5 TEXT, TGL_LAHIR_5 TEXT, NO_PASSPORT_5 TEXT, IS_INSURED_5 TEXT, PREMI_5 TEXT," +
        "NAMA_TERTANGGUNG_6 TEXT, TGL_LAHIR_6 TEXT, NO_PASSPORT_6 TEXT, IS_INSURED_6 TEXT, PREMI_6 TEXT," +
        "NAMA_TERTANGGUNG_7 TEXT, TGL_LAHIR_7 TEXT, NO_PASSPORT_7 TEXT, IS_INSURED_7 TEXT, PREMI_7 TEXT," +
        "NAMA_TERTANGGUNG_8 TEXT, TGL_LAHIR_8 TEXT, NO_PASSPORT_8 TEXT, IS_INSURED_8 TEXT, PREMI_8 TEXT," +
        "NAMA_TERTANGGUNG_9 TEXT, TGL_LAHIR_9 TEXT, NO_PASSPORT_9 TEXT, IS_INSURED_9 TEXT, PREMI_9 TEXT," +
        "NAMA_TERTANGGUNG_10 TEXT, TGL_LAHIR_10 TEXT, NO_PASSPORT_10 TEXT, IS_INSURED_10 TEXT, PREMI_10 TEXT," +
        "NAMA_NEGARA_2 TEXT, KODE_NEGARA_2 TEXT," +
        "NAMA_NEGARA_3 TEXT, KODE_NEGARA_3 TEXT," +
        "NAMA_NEGARA_4 TEXT, KODE_NEGARA_4 TEXT," +
        "NAMA_NEGARA_5 TEXT, KODE_NEGARA_5 TEXT," +
        "NAMA_NEGARA_6 TEXT, KODE_NEGARA_6 TEXT," +
        "INSURED_GROUP_ID NUMERIC," +
        "GROUP_NAME		 TEXT," +
        "INSURED_GROUP_COUNT NUMERIC" + 
        
        ");";

        
    private final Context context;    

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBA_PRODUCT_TRAVEL_SAFE(Context ctx) 
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
    public DBA_PRODUCT_TRAVEL_SAFE open() throws SQLException 
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
    		String namapasangan, String tgllahirpasangan, String nopasspasangan, 
    		String namaanak1, String tgllahiranak1, String nopassanak1, 
    		String namaanak2, String tgllahiranak2, String nopassanak2, 
    		String namaanak3, String tgllahiranak3, String nopassanak3, 
    		String tujuan, String negaratujuan, String namaNegara, String plan,
    		String ahliwaris, String hubungan, String tglberangkat, String tglkembali,
    		double jumlahhari, double tambahan, double loadingpremi, String family,
    		double premi, double polis, double total, String product_name, String customer_name,
    		double premi_pasangan, double premi_anak_1, double premi_anak_2, double premi_anak_3,
    		String is_pasangan, String is_anak_1, String is_anak_2, String is_anak_3,
    		String kode_negara, String is_annual, double ex_rate,
    		String acod,
    		String ccod,
    		String dcod,
    		double premidays,
    		double premiweeks,
    		double maxbenefit,
    		int totaldays,
    		int totalweeks,
    		double premiAlokasi,
    		String noPassport, 
    		 
			String nama5, String dob5, String passport5,
			String nama6, String dob6, String passport6,
			String nama7, String dob7, String passport7,
			String nama8, String dob8, String passport8,
			String nama9, String dob9, String passport9,
			String nama10, String dob10, String passport10, 
			int isInsured5, int isInsured6,
			int isInsured7, int isInsured8,
			int isInsured9, int isInsured10,
			double premiGrup5,
			double premiGrup6, double premiGrup7,
			double premiGrup8, double premiGrup9,
			double premiGrup10,
			
			String namaNegara2, String kodeNegara2,
			String namaNegara3, String kodeNegara3,
			String namaNegara4, String kodeNegara4,
			String namaNegara5, String kodeNegara5,
			String namaNegara6, String kodeNegara6,
			long insured_grup_id,
			String namaGrup,
			int insuredCount
    		)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(PRODUCT_MAIN_ID, product_main_id);
        initialValues.put(NAMA_PASANGAN, namapasangan);
        initialValues.put(TGL_LAHIR_PASANGAN, tgllahirpasangan);
        initialValues.put(NO_PASSPORT_PASANGAN, nopasspasangan);
        initialValues.put(NAMA_ANAK_1, namaanak1);
        initialValues.put(TGL_LAHIR_ANAK_1, tgllahiranak1);
        initialValues.put(NO_PASSPORT_ANAK_1, nopassanak1);
        initialValues.put(NAMA_ANAK_2, namaanak2);
        initialValues.put(TGL_LAHIR_ANAK_2, tgllahiranak2);
        initialValues.put(NO_PASSPORT_ANAK_2, nopassanak2);
        initialValues.put(NAMA_ANAK_3, namaanak3);
        initialValues.put(TGL_LAHIR_ANAK_3, tgllahiranak3);
        initialValues.put(NO_PASSPORT_ANAK_3, nopassanak3);
        initialValues.put(TUJUAN_PERJALANAN, tujuan);
        initialValues.put(NEGARA_TUJUAN, negaratujuan);
        initialValues.put(NAMA_NEGARA, namaNegara);
        initialValues.put(AHLI_WARIS, ahliwaris);
        initialValues.put(HUBUNGAN, hubungan);
        initialValues.put(TGL_KEBERANGKATAN, tglberangkat);
        initialValues.put(TGL_KEMBALI, tglkembali);
        initialValues.put(PLAN, plan);
        initialValues.put(JUMLAH_HARI_DIPERTANGGUNGKAN, jumlahhari);
        initialValues.put(TAMBAHAN_PER_MINGGU, tambahan);
        initialValues.put(LOADING_PREMI, loadingpremi);
        initialValues.put(PREMI, premi);
        initialValues.put(BIAYA_POLIS, polis);
        initialValues.put(TOTAL, total);
        initialValues.put(FAMILY, family);
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
        
        initialValues.put(KODE_NEGARA, kode_negara);
        initialValues.put(IS_ANNUAL, is_annual);
        initialValues.put(EXCH_RATE, ex_rate);

        initialValues.put(ACOD, acod);
        initialValues.put(CCOD, ccod);
        initialValues.put(DCOD, dcod);
        initialValues.put(PREMIDAYS, premidays);
        
        initialValues.put(PREMIWEEKS, premiweeks);
        initialValues.put(MAXBENEFIT, maxbenefit);
        initialValues.put(TOTALDAYS, totaldays);
        initialValues.put(TOTALWEEKS, totalweeks);
        
        initialValues.put(PREMI_ALOKASI, premiAlokasi);
        initialValues.put(NO_PASSPORT, noPassport);
        
         
        initialValues.put(NAMA_TERTANGGUNG_5, nama5);
        initialValues.put(NAMA_TERTANGGUNG_6, nama6);
        initialValues.put(NAMA_TERTANGGUNG_7, nama7);
        initialValues.put(NAMA_TERTANGGUNG_8, nama8);
        initialValues.put(NAMA_TERTANGGUNG_9, nama9);
        initialValues.put(NAMA_TERTANGGUNG_10, nama10); 
    
        initialValues.put(TGL_LAHIR_5, dob5);
        initialValues.put(TGL_LAHIR_6, dob6);
        initialValues.put(TGL_LAHIR_7, dob7);
        initialValues.put(TGL_LAHIR_8, dob8);
        initialValues.put(TGL_LAHIR_9, dob9);
        initialValues.put(TGL_LAHIR_10, dob10); 
           
        initialValues.put(NO_PASSPORT_5, passport5);
        initialValues.put(NO_PASSPORT_6, passport6);
        initialValues.put(NO_PASSPORT_7, passport7);
        initialValues.put(NO_PASSPORT_8, passport8);
        initialValues.put(NO_PASSPORT_9, passport9);
        initialValues.put(NO_PASSPORT_10, passport10); 
        
  
        initialValues.put(IS_INSURED_5, isInsured5);
        initialValues.put(IS_INSURED_6, isInsured6);
        initialValues.put(IS_INSURED_7, isInsured7);
        initialValues.put(IS_INSURED_8, isInsured8);
        initialValues.put(IS_INSURED_9, isInsured9);
        initialValues.put(IS_INSURED_10, isInsured10); 

        initialValues.put(NAMA_NEGARA_2, namaNegara2);
        initialValues.put(NAMA_NEGARA_3, namaNegara3);
        initialValues.put(NAMA_NEGARA_4, namaNegara4);
        initialValues.put(NAMA_NEGARA_5, namaNegara5);
        initialValues.put(NAMA_NEGARA_6, namaNegara6);
        
        initialValues.put(KODE_NEGARA_2, kodeNegara2);
        initialValues.put(KODE_NEGARA_3, kodeNegara3);
        initialValues.put(KODE_NEGARA_4, kodeNegara4);
        initialValues.put(KODE_NEGARA_5, kodeNegara5);
        initialValues.put(KODE_NEGARA_6, kodeNegara6); 
        
        initialValues.put(INSURED_GROUP_ID, insured_grup_id); 
        initialValues.put(GROUP_NAME, namaGrup); 
        initialValues.put(INSURED_GROUP_COUNT, insuredCount); 
        
        
        
        
        
        return db.insert(DATABASE_TABLE, null, initialValues);
    }
    
    public boolean nextInsert(long product_main_id,
    		String namapasangan, String tgllahirpasangan, String nopasspasangan, 
    		String namaanak1, String tgllahiranak1, String nopassanak1, 
    		String namaanak2, String tgllahiranak2, String nopassanak2, 
    		String namaanak3, String tgllahiranak3, String nopassanak3, 
    		String tujuan, String negaratujuan, String namaNegara, String plan,
    		String ahliwaris, String hubungan, String tglberangkat, String tglkembali,
    		double jumlahhari, double tambahan, double loadingpremi, String family,
    		double premi, double polis, double total, String product_name, String customer_name,
    		double premi_pasangan, double premi_anak_1, double premi_anak_2, double premi_anak_3,
    		String is_pasangan, String is_anak_1, String is_anak_2, String is_anak_3,
    		String kode_negara, String is_annual, double ex_rate,
    		String acod,
    		String ccod,
    		String dcod,
    		double premidays,
    		double premiweeks,
    		double maxbenefit,
    		int totaldays,
    		int totalweeks,
    		double premiAlokasi,
    		String no_passPort,  
			String nama5, String dob5, String passport5,
			String nama6, String dob6, String passport6,
			String nama7, String dob7, String passport7,
			String nama8, String dob8, String passport8,
			String nama9, String dob9, String passport9,
			String nama10, String dob10, String passport10, 
			int isInsured5, int isInsured6,
			int isInsured7, int isInsured8,
			int isInsured9, int isInsured10, 
			double premiGrup5,
			double premiGrup6, double premiGrup7,
			double premiGrup8, double premiGrup9,
			double premiGrup10 ,
			String namaNegara2, String kodeNegara2,
			String namaNegara3, String kodeNegara3,
			String namaNegara4, String kodeNegara4,
			String namaNegara5, String kodeNegara5,
			String namaNegara6, String kodeNegara6,
			long insured_grup_id, 
			String namaGrup,
			int insuredCount)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(PRODUCT_MAIN_ID, product_main_id);
        initialValues.put(NAMA_PASANGAN, namapasangan);
        initialValues.put(TGL_LAHIR_PASANGAN, tgllahirpasangan);
        initialValues.put(NO_PASSPORT_PASANGAN, nopasspasangan);
        initialValues.put(NAMA_ANAK_1, namaanak1);
        initialValues.put(TGL_LAHIR_ANAK_1, tgllahiranak1);
        initialValues.put(NO_PASSPORT_ANAK_1, nopassanak1);
        initialValues.put(NAMA_ANAK_2, namaanak2);
        initialValues.put(TGL_LAHIR_ANAK_2, tgllahiranak2);
        initialValues.put(NO_PASSPORT_ANAK_2, nopassanak2);
        initialValues.put(NAMA_ANAK_3, namaanak3);
        initialValues.put(TGL_LAHIR_ANAK_3, tgllahiranak3);
        initialValues.put(NO_PASSPORT_ANAK_3, nopassanak3);
        initialValues.put(TUJUAN_PERJALANAN, tujuan);
        initialValues.put(NEGARA_TUJUAN, negaratujuan);
        initialValues.put(NAMA_NEGARA, namaNegara);
        initialValues.put(AHLI_WARIS, ahliwaris);
        initialValues.put(HUBUNGAN, hubungan);
        initialValues.put(TGL_KEBERANGKATAN, tglberangkat);
        initialValues.put(TGL_KEMBALI, tglkembali);
        initialValues.put(PLAN, plan);
        initialValues.put(JUMLAH_HARI_DIPERTANGGUNGKAN, jumlahhari);
        initialValues.put(TAMBAHAN_PER_MINGGU, tambahan);
        initialValues.put(LOADING_PREMI, loadingpremi);
        initialValues.put(PREMI, premi);
        initialValues.put(BIAYA_POLIS, polis);
        initialValues.put(TOTAL, total);
        initialValues.put(FAMILY, family);
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
        
        initialValues.put(KODE_NEGARA, kode_negara);
        initialValues.put(IS_ANNUAL, is_annual);
        initialValues.put(EXCH_RATE, ex_rate);
        
        initialValues.put(ACOD, acod);
        initialValues.put(CCOD, ccod);
        initialValues.put(DCOD, dcod);
        initialValues.put(PREMIDAYS, premidays);
        
        initialValues.put(PREMIWEEKS, premiweeks);
        initialValues.put(MAXBENEFIT, maxbenefit);
        initialValues.put(TOTALDAYS, totaldays);
        initialValues.put(TOTALWEEKS, totalweeks);
        
        initialValues.put(PREMI_ALOKASI, premiAlokasi);
        initialValues.put(NO_PASSPORT, no_passPort);
        
  
         
        initialValues.put(NAMA_TERTANGGUNG_5, nama5);
        initialValues.put(NAMA_TERTANGGUNG_6, nama6);
        initialValues.put(NAMA_TERTANGGUNG_7, nama7);
        initialValues.put(NAMA_TERTANGGUNG_8, nama8);
        initialValues.put(NAMA_TERTANGGUNG_9, nama9);
        initialValues.put(NAMA_TERTANGGUNG_10, nama10); 
    
        initialValues.put(TGL_LAHIR_5, dob5);
        initialValues.put(TGL_LAHIR_6, dob6);
        initialValues.put(TGL_LAHIR_7, dob7);
        initialValues.put(TGL_LAHIR_8, dob8);
        initialValues.put(TGL_LAHIR_9, dob9);
        initialValues.put(TGL_LAHIR_10, dob10); 
           
        initialValues.put(NO_PASSPORT_5, passport5);
        initialValues.put(NO_PASSPORT_6, passport6);
        initialValues.put(NO_PASSPORT_7, passport7);
        initialValues.put(NO_PASSPORT_8, passport8);
        initialValues.put(NO_PASSPORT_9, passport9);
        initialValues.put(NO_PASSPORT_10, passport10); 
        
  
        initialValues.put(IS_INSURED_5, isInsured5);
        initialValues.put(IS_INSURED_6, isInsured6);
        initialValues.put(IS_INSURED_7, isInsured7);
        initialValues.put(IS_INSURED_8, isInsured8);
        initialValues.put(IS_INSURED_9, isInsured9);
        initialValues.put(IS_INSURED_10, isInsured10); 
        

        initialValues.put(NAMA_NEGARA_2, namaNegara2);
        initialValues.put(NAMA_NEGARA_3, namaNegara3);
        initialValues.put(NAMA_NEGARA_4, namaNegara4);
        initialValues.put(NAMA_NEGARA_5, namaNegara5);
        initialValues.put(NAMA_NEGARA_6, namaNegara6);
        
        initialValues.put(KODE_NEGARA_2, kodeNegara2);
        initialValues.put(KODE_NEGARA_3, kodeNegara3);
        initialValues.put(KODE_NEGARA_4, kodeNegara4);
        initialValues.put(KODE_NEGARA_5, kodeNegara5);
        initialValues.put(KODE_NEGARA_6, kodeNegara6); 
        

        initialValues.put(INSURED_GROUP_ID, insured_grup_id); 
        initialValues.put(GROUP_NAME, namaGrup); 
        initialValues.put(INSURED_GROUP_COUNT, insuredCount); 
        
        
        
        
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
        		_id, //0
    			FAMILY, //1
    			PRODUCT_MAIN_ID, //2
    			NAMA_PASANGAN, //3
    			TGL_LAHIR_PASANGAN, //4
    			NO_PASSPORT_PASANGAN, //5
    			NAMA_ANAK_1, //6
    			TGL_LAHIR_ANAK_1,//7
    			NO_PASSPORT_ANAK_1,//8
    			NAMA_ANAK_2,//9
    			TGL_LAHIR_ANAK_2,//10
    			NO_PASSPORT_ANAK_2,//11
    			NAMA_ANAK_3,//12
    			TGL_LAHIR_ANAK_3,//13
    			NO_PASSPORT_ANAK_3,//14
    			TUJUAN_PERJALANAN,//15
    			NEGARA_TUJUAN,//16
    			NAMA_NEGARA,//17
    			AHLI_WARIS,//18
    			HUBUNGAN,//19
    			TGL_KEBERANGKATAN,//20
    			TGL_KEMBALI,//21
    			PLAN,//22
    			JUMLAH_HARI_DIPERTANGGUNGKAN,//23
    			TAMBAHAN_PER_MINGGU,//24
    			LOADING_PREMI,//25
    			PREMI,//26
    			BIAYA_POLIS,//27
    			TOTAL, //28
    			PRODUCT_NAME, //29 
    			CUSTOMER_NAME, //30
        		PREMI_PASANGAN, //31
        		PREMI_ANAK_1, //32
        		PREMI_ANAK_2, //33
        		PREMI_ANAK_3, //34
        		IS_PASANGAN, //35
        		IS_ANAK_1, //36
        		IS_ANAK_2, //37
        		IS_ANAK_3, //38
        		KODE_NEGARA, //39
        		IS_ANNUAL, //40
        		EXCH_RATE, //41
        		ACOD, //42
        		CCOD, //43
        		DCOD, //44
        		PREMIDAYS, //45
        		PREMIWEEKS, //46
        		MAXBENEFIT, //47
        		TOTALDAYS, //48 
        		TOTALWEEKS, //49
        		PREMI_ALOKASI, //50
        		NO_PASSPORT //51
        		 
     	        ,NAMA_TERTANGGUNG_5 , TGL_LAHIR_5 , NO_PASSPORT_5   						//52
     	        ,NAMA_TERTANGGUNG_6 , TGL_LAHIR_6 , NO_PASSPORT_6   						//55
     	        ,NAMA_TERTANGGUNG_7 , TGL_LAHIR_7 , NO_PASSPORT_7   						//58
     	        ,NAMA_TERTANGGUNG_8 , TGL_LAHIR_8 , NO_PASSPORT_8   						//61
     	        ,NAMA_TERTANGGUNG_9 , TGL_LAHIR_9 , NO_PASSPORT_9   						//64
     	        ,NAMA_TERTANGGUNG_10 , TGL_LAHIR_10 , NO_PASSPORT_10   					//67 
     	        ,IS_INSURED_5		//70
	   	        ,IS_INSURED_6		//71
	   	        ,IS_INSURED_7		//72
	   	        ,IS_INSURED_8		//73
	   	        ,IS_INSURED_9		//74
	   	        ,IS_INSURED_10		//75
	   	        ,PREMI_5			//76
	   	        ,PREMI_6			//77
	   	        ,PREMI_7			//78
	   	        ,PREMI_8			//79
	   	        ,PREMI_9			//80
	   	        ,PREMI_10			//81
	   	        ,PREMI_10			//82

	   	        ,NAMA_NEGARA_2		//83
	   	        ,NAMA_NEGARA_3		//84
	   	        ,NAMA_NEGARA_4		//85
	   	        ,NAMA_NEGARA_5		//86
	   	        ,NAMA_NEGARA_6	   	//87
	   	        
	   	        ,KODE_NEGARA_2		//88
	   	        ,KODE_NEGARA_3		//89
	   	        ,KODE_NEGARA_4		//90
	   	        ,KODE_NEGARA_5		//91
	   	        ,KODE_NEGARA_6		//92
	   	        
	   	        ,INSURED_GROUP_ID	//93
	   	        ,GROUP_NAME			//94
	   	        ,INSURED_GROUP_COUNT//95
 	        


 
        }, null, null, null, null, null);
        
    }
    
    public Cursor getRow(long product_main_id) 
    {
    	return db.query(DATABASE_TABLE, new String[] {
    			_id, //0
    			FAMILY, //1
    			PRODUCT_MAIN_ID, //2
    			NAMA_PASANGAN, //3
    			TGL_LAHIR_PASANGAN, //4
    			NO_PASSPORT_PASANGAN, //5
    			NAMA_ANAK_1, //6
    			TGL_LAHIR_ANAK_1,//7
    			NO_PASSPORT_ANAK_1,//8
    			NAMA_ANAK_2,//9
    			TGL_LAHIR_ANAK_2,//10
    			NO_PASSPORT_ANAK_2,//11
    			NAMA_ANAK_3,//12
    			TGL_LAHIR_ANAK_3,//13
    			NO_PASSPORT_ANAK_3,//14
    			TUJUAN_PERJALANAN,//15
    			NEGARA_TUJUAN,//16
    			NAMA_NEGARA,//17
    			AHLI_WARIS,//18
    			HUBUNGAN,//19
    			TGL_KEBERANGKATAN,//20
    			TGL_KEMBALI,//21
    			PLAN,//22
    			JUMLAH_HARI_DIPERTANGGUNGKAN,//23
    			TAMBAHAN_PER_MINGGU,//24
    			LOADING_PREMI,//25
    			PREMI,//26
    			BIAYA_POLIS,//27
    			TOTAL, //28
    			PRODUCT_NAME, //29 
    			CUSTOMER_NAME, //30
        		PREMI_PASANGAN, //31
        		PREMI_ANAK_1, //32
        		PREMI_ANAK_2, //33
        		PREMI_ANAK_3, //34
        		IS_PASANGAN, //35
        		IS_ANAK_1, //36
        		IS_ANAK_2, //37
        		IS_ANAK_3, //38
        		KODE_NEGARA, //39
        		IS_ANNUAL, //40
        		EXCH_RATE, //41
        		ACOD, //42
        		CCOD, //43
        		DCOD, //44
        		PREMIDAYS, //45
        		PREMIWEEKS, //46
        		MAXBENEFIT, //47
        		TOTALDAYS, //48 
        		TOTALWEEKS, //49
        		PREMI_ALOKASI, //50
        		NO_PASSPORT //51

     	        ,NAMA_TERTANGGUNG_5 , TGL_LAHIR_5 , NO_PASSPORT_5   						//52
     	        ,NAMA_TERTANGGUNG_6 , TGL_LAHIR_6 , NO_PASSPORT_6   						//55
     	        ,NAMA_TERTANGGUNG_7 , TGL_LAHIR_7 , NO_PASSPORT_7   						//58
     	        ,NAMA_TERTANGGUNG_8 , TGL_LAHIR_8 , NO_PASSPORT_8   						//61
     	        ,NAMA_TERTANGGUNG_9 , TGL_LAHIR_9 , NO_PASSPORT_9   						//64
     	        ,NAMA_TERTANGGUNG_10 , TGL_LAHIR_10 , NO_PASSPORT_10   					//67 
     	        ,IS_INSURED_5		//70
	   	        ,IS_INSURED_6		//71
	   	        ,IS_INSURED_7		//72
	   	        ,IS_INSURED_8		//73
	   	        ,IS_INSURED_9		//74
	   	        ,IS_INSURED_10		//75
	   	        ,PREMI_5			//76
	   	        ,PREMI_6			//77
	   	        ,PREMI_7			//78
	   	        ,PREMI_8			//79
	   	        ,PREMI_9			//80
	   	        ,PREMI_10			//81
	   	        ,PREMI_10			//82

	   	        ,NAMA_NEGARA_2		//83
	   	        ,NAMA_NEGARA_3		//84
	   	        ,NAMA_NEGARA_4		//85
	   	        ,NAMA_NEGARA_5		//86
	   	        ,NAMA_NEGARA_6	   	//87
	   	        
	   	        ,KODE_NEGARA_2		//88
	   	        ,KODE_NEGARA_3		//89
	   	        ,KODE_NEGARA_4		//90
	   	        ,KODE_NEGARA_5		//91
	   	        ,KODE_NEGARA_6		//92
	   	        

	   	        ,INSURED_GROUP_ID	//93
	   	        ,GROUP_NAME			//94
	   	        ,INSURED_GROUP_COUNT//95

 
        		
    	}, 
    			PRODUCT_MAIN_ID + "=" + product_main_id, null, null, null, null);
    }
}

