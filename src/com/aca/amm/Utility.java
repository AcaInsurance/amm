package com.aca.amm;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.security.auth.PrivateCredentialPermission;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import com.aca.amm.R.id;
import com.aca.database.DBA_MASTER_AGENT;
import com.aca.database.DBA_MASTER_BUSINESS_TYPE;
import com.aca.database.DBA_MASTER_CAR_BRAND;
import com.aca.database.DBA_MASTER_CAR_BRAND_SYARIAH;
import com.aca.database.DBA_MASTER_CAR_BRAND_TRUK;
import com.aca.database.DBA_MASTER_CAR_TYPE;
import com.aca.database.DBA_MASTER_CAR_TYPE_SYARIAH;
import com.aca.database.DBA_MASTER_CITY_PROVINCE;
import com.aca.database.DBA_MASTER_COUNTRY;
import com.aca.database.DBA_MASTER_DISC_COMM;
import com.aca.database.DBA_MASTER_JENIS_TOKO;
import com.aca.database.DBA_MASTER_LOB;
import com.aca.database.DBA_MASTER_OCCUPATION;
import com.aca.database.DBA_MASTER_OTOMATE_PA;
import com.aca.database.DBA_MASTER_OTOMATE_TPL;
import com.aca.database.DBA_MASTER_PRODUCT_SETTING;
import com.aca.database.DBA_MASTER_VESSEL_TYPE;
import com.aca.database.DBA_PRODUCT_MAIN;


import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.Settings;
import android.sax.StartElementListener;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class Utility  {
	
//	acAsqlsvr_88
	
	public static int year;
	public static int month;
	public static int day;
	public static int next_year;
	public static int next_month;
	public static int next_day;
	
	public static int popup = 0;

	public static String prevPolisLOB;	
	public static String prevPolisBranch;	
	public static String prevPolisYear;
	public static String prevPolisNo;
	public static String OldDate;
	
	private static int durationSyncImage = 1000 * 60 * 10;
	private static int durationLogoutSession = 1000 * 60 * 20 ;
	
	private static String specialChars = "/*!@#$%^&*()\"{}_[]|\\?/<>,.";
	private static String anytypeStr = "anytype{}";
	
	private static final int DATABASE_VERSION = 8;
	private static final int DATABASE_VERSION_BIG = 5;

	private static final String namespace = "http://tempuri.org/";
	private static final String soapAction = "http://tempuri.org/";

	//	private static final String URL = "http://110.35.81.227/wsgetdata/service.asmx";
//	private static final String URL = "http://182.23.65.68/wsgetdata/service.asmx";
	private static final String URL = "http://172.16.88.31/wsgetdatatest/service.asmx";
//    private static final String URL_IMAGE = "http://www.aca-mobile.com/wssaveimage.asmx";
    private static final String URL_IMAGE = "http://172.16.88.31/mobiletest/wssaveimage.asmx";

    
	@SuppressWarnings("unused")
	private static final String TAG = "com.aca.amm.Utility";
	
	public static String getURL () {
		return URL;
	}

    public static String getUrlImage() {
        return URL_IMAGE;
    }
	public static String getNamespace() {
		return namespace;
	}

	public static String getSoapAction() {
		return soapAction;
	}


	public static void setAutoDateTime(Context context) {
	        Settings.System.putInt(context.getContentResolver(),
	                Settings.System.AUTO_TIME, 1);

	    }
	  
	
	public static void CopyDB(InputStream inputStream, OutputStream outputStream) throws IOException {
		//---copy 1K bytes at a time---
			byte[] buffer = new byte[1024];
			int length;
			while ((length = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, length);
			}
			outputStream.flush();
			inputStream.close();
			outputStream.close();
	}
	
	public static void CreateDatabaseDir(Context c)
	{
		File mydir = new File("data/data/"+ c.getPackageName() + "/databases");
		if(!mydir.exists()){
			mydir.mkdir();
		}
	}
	
	
	
	public static void BindCityProv(Spinner s, Context ctx, Activity act) {
	
		List<SpinnerGenericItem> spinnerGenericList = new LinkedList<SpinnerGenericItem>();
		SpinnerGenericAdapter spinnerGenericAdapter = new SpinnerGenericAdapter(spinnerGenericList, act);
		
		// database handler 
		DBA_MASTER_CITY_PROVINCE db = new DBA_MASTER_CITY_PROVINCE(ctx);
		db.open();
  
        // Spinner Drop down elements 
        Cursor c = db.getAll(); 
  
        if (c.moveToFirst()) { 
            do { 
            	spinnerGenericList.add(new SpinnerGenericItem(c.getString(0), c.getString(1))); 
            } while (c.moveToNext()); 
        } 
          
        // closing connection 
        c.close(); 
        db.close(); 
        
        s.setAdapter(spinnerGenericAdapter);
	}
	
	public static void BindCityProv(List<City> cities, Context ctx) {
				
		// database handler 
		DBA_MASTER_CITY_PROVINCE db = new DBA_MASTER_CITY_PROVINCE(ctx);
		db.open();
  
        // Spinner Drop down elements 
        Cursor c = db.getAll(); 
  
        if (c.moveToFirst()) { 
            do { 
            	City city = new City(c.getString(0), c.getString(1));
            	cities.add(city); 
            } while (c.moveToNext()); 
        } 
          
        // closing connection 
        c.close(); 
        db.close(); 

	}
	
	public static void BindOccupation(Spinner s, Context ctx, Activity act) {

		List<SpinnerGenericItem> spinnerGenericList = new LinkedList<SpinnerGenericItem>();
		SpinnerGenericAdapter spinnerGenericAdapter = new SpinnerGenericAdapter(spinnerGenericList, act);
		
		// database handler 
		DBA_MASTER_OCCUPATION db = new DBA_MASTER_OCCUPATION(ctx);
		db.open();
  
        // Spinner Drop down elements 
        Cursor c = db.getAll(); 
  
        if (c.moveToFirst()) { 
            do { 
            	spinnerGenericList.add(new SpinnerGenericItem(c.getString(0), c.getString(1))); 
            } while (c.moveToNext()); 
        } 
          
        // closing connection 
        c.close(); 
        db.close(); 
        
        s.setAdapter(spinnerGenericAdapter);
	}
	
	public static void BindBusiness(Spinner s, Context ctx, Activity act) {

		List<SpinnerGenericItem> spinnerGenericList = new LinkedList<SpinnerGenericItem>();
		SpinnerGenericAdapter spinnerGenericAdapter = new SpinnerGenericAdapter(spinnerGenericList, act);
		
		// database handler 
		DBA_MASTER_LOB db = new DBA_MASTER_LOB(ctx);
		db.open();
  
        // Spinner Drop down elements 
        Cursor c = db.getAll(); 
  
        if (c.moveToFirst()) { 
            do { 
            	spinnerGenericList.add(new SpinnerGenericItem(c.getString(0), c.getString(1))); 
            } while (c.moveToNext()); 
        } 
          
        // closing connection 
        c.close(); 
        db.close(); 
        
        s.setAdapter(spinnerGenericAdapter);
	}
	
	public static void BindNegaraTujuan(Spinner s, Context ctx, Activity act, int flag) {

		List<SpinnerGenericItem> spinnerGenericList = new LinkedList<SpinnerGenericItem>();
		SpinnerGenericAdapter spinnerGenericAdapter = new SpinnerGenericAdapter(spinnerGenericList, act);
		
		// database handler 
		DBA_MASTER_COUNTRY db = new DBA_MASTER_COUNTRY(ctx);
		db.open();
  
        // Spinner Drop down elements 
        Cursor c = db.getAllByFlag(String.valueOf(flag)); 
  
        if (c.moveToFirst()) { 
            do { 
            	spinnerGenericList.add(new SpinnerGenericItem(c.getString(0), c.getString(1))); 
            } while (c.moveToNext()); 
        } 
          
        // closing connection 
        c.close(); 
        db.close(); 
        
        s.setAdapter(spinnerGenericAdapter);
	}
	
	
	
	public static void BindNegaraTujuan(List<Country> countries, Context ctx, int flag) {

		// database handler 
		DBA_MASTER_COUNTRY db = new DBA_MASTER_COUNTRY(ctx);
		db.open();
  
        // Spinner Drop down elements 
		Cursor c = db.getAllByFlag(String.valueOf(flag)); 
  
        if (c.moveToFirst()) { 
            do { 
            	Country country = new Country(c.getString(0), c.getString(1));
            	countries.add(country); 
            } while (c.moveToNext()); 
        } 
          
        // closing connection 
        c.close(); 
        db.close(); 
	}
	
	public static void BindJenisUsaha(Spinner s, Context ctx, Activity act) {

		List<SpinnerGenericItem> spinnerGenericList = new LinkedList<SpinnerGenericItem>();
		SpinnerGenericAdapter spinnerGenericAdapter = new SpinnerGenericAdapter(spinnerGenericList, act);
		
		// database handler 
		DBA_MASTER_JENIS_TOKO db = new DBA_MASTER_JENIS_TOKO(ctx);
		db.open();
  
        // Spinner Drop down elements 
        Cursor c = db.getAll(); 
  
        if (c.moveToFirst()) { 
            do { 
            	spinnerGenericList.add(new SpinnerGenericItem(c.getString(0), c.getString(1))); 
            } while (c.moveToNext()); 
        } 
          
        // closing connection 
        c.close(); 
        db.close(); 
        
        s.setAdapter(spinnerGenericAdapter);
	}
	
	public static void BindMerk(Spinner s, Context ctx, Activity act) {

		List<SpinnerGenericItem> spinnerGenericList = new LinkedList<SpinnerGenericItem>();
		SpinnerGenericAdapter spinnerGenericAdapter = new SpinnerGenericAdapter(spinnerGenericList, act);
		
		// database handler 
		DBA_MASTER_CAR_BRAND db = new DBA_MASTER_CAR_BRAND(ctx);
		db.open();
  
        // Spinner Drop down elements 
        Cursor c = db.getAll(); 
  
        if (c.moveToFirst()) { 
            do { 
            	spinnerGenericList.add(new SpinnerGenericItem(c.getString(0), c.getString(1))); 
            } while (c.moveToNext()); 
        } 
          
        // closing connection 
        c.close(); 
        db.close(); 
        
        s.setAdapter(spinnerGenericAdapter);
	}
	
	public static void BindMerk(List<CarBrand> carbrands, Context ctx) {

		// database handler 
		DBA_MASTER_CAR_BRAND db = new DBA_MASTER_CAR_BRAND(ctx);
		db.open();
  
		// Spinner Drop down elements 
        Cursor c = db.getAll(); 
  
        if (c.moveToFirst()) { 
            do { 
            	CarBrand carbrand = new CarBrand(c.getString(0), c.getString(1));
            	carbrands.add(carbrand); 
            } while (c.moveToNext()); 
        } 
          
        // closing connection 
        c.close(); 
        db.close(); 
	}
	

	public static void BindMerkSyariah(List<CarBrand> carbrands, Context ctx) {

		// database handler 
		DBA_MASTER_CAR_BRAND_SYARIAH db = new DBA_MASTER_CAR_BRAND_SYARIAH(ctx);
		db.open();
  
		// Spinner Drop down elements 
        Cursor c = db.getAll(); 
  
        if (c.moveToFirst()) { 
            do { 
            	CarBrand carbrand = new CarBrand(c.getString(0), c.getString(1));
            	carbrands.add(carbrand); 
            } while (c.moveToNext()); 
        } 
          
        // closing connection 
        c.close(); 
        db.close(); 
	}
	
	public static void BindMerkTruk(List<CarBrand> carbrands, Context ctx) {

		// database handler 
		DBA_MASTER_CAR_BRAND_TRUK db = new DBA_MASTER_CAR_BRAND_TRUK(ctx);
		db.open();
  
		// Spinner Drop down elements 
        Cursor c = db.getAll(); 
  
        if (c.moveToFirst()) { 
            do { 
            	CarBrand carbrand = new CarBrand(c.getString(0), c.getString(1));
            	carbrands.add(carbrand); 
            } while (c.moveToNext()); 
        } 
          
        // closing connection 
        c.close(); 
        db.close(); 
	}
	
	public static void BindType(Spinner s, Context ctx, Activity act, String BrandCode) {

		List<SpinnerGenericItem> spinnerGenericList = new LinkedList<SpinnerGenericItem>();
		SpinnerGenericAdapter spinnerGenericAdapter = new SpinnerGenericAdapter(spinnerGenericList, act);
		
		// database handler 
		DBA_MASTER_CAR_TYPE db = new DBA_MASTER_CAR_TYPE(ctx);
		db.open();
  
        // Spinner Drop down elements 
        Cursor c = db.getAllByCarBrand(BrandCode); 
  
        if (c.moveToFirst()) { 
            do { 
            	spinnerGenericList.add(new SpinnerGenericItem(c.getString(0), c.getString(1))); 
            } while (c.moveToNext()); 
        } 
          
        // closing connection 
        c.close(); 
        db.close(); 
        
        s.setAdapter(spinnerGenericAdapter);
	}
	
	
	public static void BindType(List<CarType> cartypes, Context ctx, String BrandCode) {

		// database handler 
		DBA_MASTER_CAR_TYPE db = new DBA_MASTER_CAR_TYPE(ctx);
		db.open();
  
		// Spinner Drop down elements 
        Cursor c = db.getAllByCarBrand(BrandCode); 
  
        if (c.moveToFirst()) { 
            do { 
            	CarType cartype = new CarType(c.getString(0), c.getString(1));
            	cartypes.add(cartype); 
            } while (c.moveToNext()); 
        } 
          
        // closing connection 
        c.close(); 
        db.close(); 
	}
	

	public static void BindTypeSyariah(List<CarType> cartypes, Context ctx, String BrandCode) {

		// database handler 
		DBA_MASTER_CAR_TYPE_SYARIAH db = new DBA_MASTER_CAR_TYPE_SYARIAH(ctx);
		db.open();
  
		// Spinner Drop down elements 
        Cursor c = db.getAllByCarBrand(BrandCode); 
  
        if (c.moveToFirst()) { 
            do { 
            	CarType cartype = new CarType(c.getString(0), c.getString(1));
            	cartypes.add(cartype); 
            } while (c.moveToNext()); 
        } 
          
        // closing connection 
        c.close(); 
        db.close(); 
	}
	
	public static void BindVesselType(List<CarType> cartypes, Context ctx, String BrandCode) {

		// database handler 
		DBA_MASTER_CAR_TYPE db = new DBA_MASTER_CAR_TYPE(ctx);
		db.open();
  
		// Spinner Drop down elements 
        Cursor c = db.getAllByCarBrand(BrandCode); 
  
        if (c.moveToFirst()) { 
            do { 
            	CarType cartype = new CarType(c.getString(0), c.getString(1));
            	cartypes.add(cartype); 
            } while (c.moveToNext()); 
        } 
          
        // closing connection 
        c.close(); 
        db.close(); 
	}
	
	
	
	public static void BindTujuanPerjalanan(Spinner s, Context ctx) {

		List<String> list = new ArrayList<String>();
		list.add("LIBURAN");
		list.add("PEKERJAAN");
		list.add("BEROBAT");
		list.add("LAINNYA");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ctx,
			android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		s.setAdapter(dataAdapter);
	}
	
	public static String getcurrency (int kode) {
		switch (kode) {
		case 1:
			return "IDR";			
		case 86:
			return "USD";			
		case 84:
			return "SGD";			
		case 43:
			return "JPY";			
		case 87:
			return "EUR";			
			

		default:
			return "";
		}
	}
	public static void BindCargoCurrency(Spinner s, Context ctx, Activity act) {

		List<SpinnerGenericItem> spinnerGenericList = new LinkedList<SpinnerGenericItem>();
		SpinnerGenericAdapter spinnerGenericAdapter = new SpinnerGenericAdapter(spinnerGenericList, act);
		
		spinnerGenericList.add(new SpinnerGenericItem("1","IDR"));
		spinnerGenericList.add(new SpinnerGenericItem("86","USD"));
		spinnerGenericList.add(new SpinnerGenericItem("84","SGD"));
		spinnerGenericList.add(new SpinnerGenericItem("43","JPY"));
		spinnerGenericList.add(new SpinnerGenericItem("87","EUR"));
		
		s.setAdapter(spinnerGenericAdapter);
	}
	
	public static void BindCargoPolicyType(Spinner s, Context ctx, Activity act) {

		List<SpinnerGenericItem> spinnerGenericList = new LinkedList<SpinnerGenericItem>();
		SpinnerGenericAdapter spinnerGenericAdapter = new SpinnerGenericAdapter(spinnerGenericList, act);
		
		spinnerGenericList.add(new SpinnerGenericItem("EXP","EXPORT"));
		spinnerGenericList.add(new SpinnerGenericItem("IMP","IMPORT"));
		spinnerGenericList.add(new SpinnerGenericItem("INT","INTER ISLAND"));
		spinnerGenericList.add(new SpinnerGenericItem("LAN","LAND IN TRANSIT"));
		
		s.setAdapter(spinnerGenericAdapter);
		
		
	}
	
	public static void BindPhoneCode(Spinner s, Context ctx, Activity act) {

		List<SpinnerGenericItem> spinnerGenericList = new LinkedList<SpinnerGenericItem>();
		SpinnerGenericAdapter spinnerGenericAdapter = new SpinnerGenericAdapter(spinnerGenericList, act);
		
		spinnerGenericList.add(new SpinnerGenericItem("1","62"));
		spinnerGenericList.add(new SpinnerGenericItem("2","65"));
		
		s.setAdapter(spinnerGenericAdapter);
	}
	
	
	
	public static void BindBankName(Spinner s, Context ctx, Activity act) {

		List<SpinnerGenericItem> spinnerGenericList = new LinkedList<SpinnerGenericItem>();
		SpinnerGenericAdapter spinnerGenericAdapter = new SpinnerGenericAdapter(spinnerGenericList, act);
		
		spinnerGenericList.add(new SpinnerGenericItem("1","BCA"));
		
		s.setAdapter(spinnerGenericAdapter);
	}
	
	public static void BindInsuredCount(Spinner s, Context ctx, Activity act) {


		List<SpinnerGenericItem> spinnerGenericList = new LinkedList<SpinnerGenericItem>();
		SpinnerGenericAdapter spinnerGenericAdapter = new SpinnerGenericAdapter(spinnerGenericList, act);
		
		for (int i = 1; i <= 15; i++) {
			spinnerGenericList.add(new SpinnerGenericItem(i + "", i + ""));			
		}
		
		s.setAdapter(spinnerGenericAdapter);
	}
	
	
	
	public static void BindTpl(Spinner s, Context ctx, Activity act) {

    	List<SpinnerGenericItem> spinnerGenericList = new LinkedList<SpinnerGenericItem>();
		SpinnerGenericAdapter spinnerGenericAdapter = new SpinnerGenericAdapter(spinnerGenericList, act);
		
		DBA_MASTER_OTOMATE_TPL dba = new DBA_MASTER_OTOMATE_TPL(ctx);
		Cursor c = null;
		
		try {
			dba.open();
			c = dba.getAll();
			
			if (c.moveToFirst()) {
				spinnerGenericList.add(new SpinnerGenericItem (c.getString(1), "STANDARD", c.getString(0)));
				c.moveToNext();
				do {
					spinnerGenericList.add(new SpinnerGenericItem (c.getString(1), FormatNumber(c.getString(0)), c.getString(0)));			
				}while (c.moveToNext());
			}
			s.setAdapter(spinnerGenericAdapter);
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		finally {
			if (dba != null)
				dba.close();
			
			if (c != null)
				c.close();
			
		}
	}
	
	
	public static void BindPA(Spinner s, Context ctx, Activity act) {


    	List<SpinnerGenericItem> spinnerGenericList = new LinkedList<SpinnerGenericItem>();
		SpinnerGenericAdapter spinnerGenericAdapter = new SpinnerGenericAdapter(spinnerGenericList, act);
		
		DBA_MASTER_OTOMATE_PA dba = new DBA_MASTER_OTOMATE_PA(ctx);
		Cursor c = null;
		
		try {
			dba.open();
			c = dba.getAll();
			
			if (c.moveToFirst()) {
				spinnerGenericList.add(new SpinnerGenericItem (c.getString(1), "STANDARD", c.getString(0)));
				c.moveToNext();
				do {
					spinnerGenericList.add(new SpinnerGenericItem (c.getString(1), FormatNumber(c.getString(0)), c.getString(0)));			
				}while (c.moveToNext());
			}
			s.setAdapter(spinnerGenericAdapter);
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		finally {
			if (dba != null)
				dba.close();
			
			if (c != null)
				c.close();
			
		}
	}
	
	public static void BindPenggunaan(Spinner s, Context ctx, Activity act) {

		List<SpinnerGenericItem> spinnerGenericList = new LinkedList<SpinnerGenericItem>();
		SpinnerGenericAdapter spinnerGenericAdapter = new SpinnerGenericAdapter(spinnerGenericList, act);

		spinnerGenericList.add(new SpinnerGenericItem("N","PRIBADI"));
        spinnerGenericList.add(new SpinnerGenericItem("D","DINAS"));

		
		s.setAdapter(spinnerGenericAdapter);
	}
	
	
	
	
	public static void BindTplKonve(Spinner s, Context ctx, Activity act) {

		List<SpinnerGenericItem> spinnerGenericList = new LinkedList<SpinnerGenericItem>();
		SpinnerGenericAdapter spinnerGenericAdapter = new SpinnerGenericAdapter(spinnerGenericList, act);
		

		spinnerGenericList.add(new SpinnerGenericItem("0","STANDARD"));
		spinnerGenericList.add(new SpinnerGenericItem("5000000","5,000,000"));
		spinnerGenericList.add(new SpinnerGenericItem("10000000","10,000,000"));
		spinnerGenericList.add(new SpinnerGenericItem("15000000","15,000,000"));
		spinnerGenericList.add(new SpinnerGenericItem("20000000","20,000,000"));
		spinnerGenericList.add(new SpinnerGenericItem("25000000","25,000,000"));

		
		s.setAdapter(spinnerGenericAdapter);
	}
	
	
	public static void BindPAKonve(Spinner s, Context ctx, Activity act) {

		List<SpinnerGenericItem> spinnerGenericList = new LinkedList<SpinnerGenericItem>();
		SpinnerGenericAdapter spinnerGenericAdapter = new SpinnerGenericAdapter(spinnerGenericList, act);

		spinnerGenericList.add(new SpinnerGenericItem("0","STANDARD"));
		spinnerGenericList.add(new SpinnerGenericItem("5000000","5,000,000"));
		spinnerGenericList.add(new SpinnerGenericItem("10000000","10,000,000"));
		spinnerGenericList.add(new SpinnerGenericItem("15000000","15,000,000"));
		spinnerGenericList.add(new SpinnerGenericItem("20000000","20,000,000"));
		spinnerGenericList.add(new SpinnerGenericItem("25000000","25,000,000"));

		
		s.setAdapter(spinnerGenericAdapter);
	}

	public static void BindDNC(Spinner s, Context ctx, Activity act) {

		List<SpinnerGenericItem> spinnerGenericList = new LinkedList<SpinnerGenericItem>();
		SpinnerGenericAdapter spinnerGenericAdapter = new SpinnerGenericAdapter(spinnerGenericList, act);

		spinnerGenericList.add(new SpinnerGenericItem("N","PRIBADI"));
        spinnerGenericList.add(new SpinnerGenericItem("D","DINAS"));
//		spinnerGenericList.add(new SpinnerGenericItem("C","KOMERSIL"));

		
		s.setAdapter(spinnerGenericAdapter);
	}
	
	

	
	public static Date GetToday()
	{
		return Calendar.getInstance().getTime(); 
	}
	
	@SuppressLint("SimpleDateFormat")
	public static String GetTodayString()
	{   
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		dateFormat.setTimeZone(TimeZone.getDefault());
		
		String myDate = dateFormat.format(Calendar.getInstance().getTime()).toString();

		Log.i(TAG, "::GetTodayString:" + myDate);
		return myDate;
	}
	
	@SuppressLint("SimpleDateFormat")
	public static String GetTomorrowString (String theDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(dateFormat.parse(theDate));
			cal.add(Calendar.DATE, 1);
		} 
		catch (ParseException e) {
			e.printStackTrace();
		}
		
		return dateFormat.format(cal.getTime()).toString();
	}
	
	 
    @SuppressLint("SimpleDateFormat")
	public static String getYesterdayString (String theDate)   {
         SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy"); // "dd/MM/yyyy HH:mm:ss");
         Calendar cal = Calendar.getInstance();
         
         try {
			cal.setTime(format.parse(theDate));
			cal.add(Calendar.DATE, -1);
         }  
         catch (ParseException e) {
			e.printStackTrace();
		 }
    	
    	return format.format(cal.getTime()).toString();
    	  	
    }
    
	public static String GetTodayNextYearString(String theDate)
	{
		Calendar c = Calendar.getInstance();
		
		try {
			c.setTime(new SimpleDateFormat("dd/MM/yyyy").parse(theDate));
			c.add(Calendar.YEAR, 1);
		} 
		catch (ParseException e) {
			e.printStackTrace();
		}
		
		return new SimpleDateFormat("dd/MM/yyyy").format(c.getTime());
	}
	
	public static String DateToString(Date dt, String Format)
	{
		// Create an instance of SimpleDateFormat used for formatting 
    	// the string representation of date (month/day/year)
    	SimpleDateFormat df = new SimpleDateFormat(Format);
    	return df.format(dt);
	}
	
	public static String FormatNumber(String num)
	{
		return NumberFormat.getInstance().format(Double.parseDouble(num));
	}

	
	public static double countPremi (double rate, double TSI){
		return rate * TSI / 100;
	}
	
	public static double countPremi (double rate, double TSI, double minimumPremi){
		return Math.max(rate * TSI / 100, minimumPremi);
	}
	
	public static int countMaterai (double premi){
		
		return 0;
				
		/*
		if (premi < 250000)
			return 0;
		else if (premi <= 1000000)
			return 3000;
		else 
			return 6000;
		*/
	}
	
	public static double countTotal (double premi, double polis, double materai, double disc){
		return premi - disc + polis + materai;
	}
	
	public static PropertyInfo GetPropertyInfo(String sName, String sValue, Object type)
	{
		PropertyInfo pi = new PropertyInfo();
		
		pi.setName(sName);         
		pi.setValue(sValue);         
		pi.setType(type); 
		
		return pi;
	}
	
	
	public static int calculateAge (String dob, String effDate){
		return 1;
	}

	public static String removeComma (String v){		
		return v.replaceAll(",", "");
		
	}
	
	public static String getProductNameBySPPAno (String SPPA_NO ){
		SPPA_NO = SPPA_NO.substring(6,8);
		if(SPPA_NO.equals("01")) return "ASRI";
				else if (SPPA_NO.equals("03")) return "Otomate";
				else if (SPPA_NO.equals("04")) return "MediSafe";
				else if (SPPA_NO.equals("05")) return "ExecutiveSafe";
				else if (SPPA_NO.equals("06")) return "TravelDom";
				else if (SPPA_NO.equals("07")) return "TravelSafe";
				else if (SPPA_NO.equals("08")) return "AsriSyariah";
				else if (SPPA_NO.equals("09")) return "OtomateSyariah";
				else if (SPPA_NO.equals("10")) return "PAAmanah";
				else if (SPPA_NO.equals("11")) return "Toko";
				else if (SPPA_NO.equals("12")) return "ACAMobil";
				else if (SPPA_NO.equals("13")) return "Cargo";
		
			
		return "";
	}
	
	public static String getFormatDate (String dateinp){
		return dateinp.substring(8,10) + "/" + dateinp.substring(5,7) + "/" + dateinp.substring(0,4);
	}
	
	public static String getFormatDateForSPPA (String dateinp){
		//return dateinp.substring(3,5) + "/" + dateinp.substring(0,2) + "/" + dateinp.substring(6,10);
		
		Log.d("formate date for sppa ", dateinp);
		
		if (dateinp.toLowerCase().contains("anytype"))
			return "";
		else {
			String[] parts = dateinp.split("/");
			String m = parts[0]; // 004
			String d = parts[1]; // 034556
			String y = parts[2].substring(0,4); // 034556
			
			return d + "/" + m + "/" + y;
		}
	}
	
	public static String AddExpiredDate (String EffDate){
		if (EffDate.substring(2).equals("-") || EffDate.substring(2).equals("/"))
			return EffDate.substring(0, 6) + String.valueOf(Integer.parseInt(EffDate.substring(6,10))+1) ;
		else 
			return String.valueOf(Integer.parseInt(EffDate.substring(0,4)) + 1) + EffDate.substring(4,10);
	}
	
	public static void PolisNumber (String noPolis){
		prevPolisLOB = noPolis.substring(0, 2);
		prevPolisBranch= noPolis.substring(3, 5);
		prevPolisYear = noPolis.substring(6, 8);
		prevPolisNo = noPolis.substring(9, 15);
	}
	
	public static int getAge(String birthDate, String now) {
		if (birthDate.isEmpty() || now.isEmpty())
			return 0;
		
	    int bYear, bMonth, bDay;
	    int nowYear, nowMonth, nowDay;
	    
	    bYear = Integer.parseInt(birthDate.substring(6,10));
	    bMonth = Integer.parseInt(birthDate.substring(3,5));
	    bDay = Integer.parseInt(birthDate.substring(0,2));
	    
	    nowYear = Integer.parseInt(now.substring(6,10));
	    nowMonth = Integer.parseInt(now.substring(3,5));
	    nowDay = Integer.parseInt(now.substring(0,2));
	    
	    int age = nowYear - bYear;
	    
	    if(bMonth > nowMonth)
	    	age -- ;
	    else if (bMonth == nowMonth && bDay > nowDay)
	    	age -- ;
	    
	    return age;
	}
	
//	public static ArrayList<HashMap<String, String>> getCustData (FillWellWomanActivity act, String custNo) throws InterruptedException{
//	
//		RetrieveCustomer customer = new RetrieveCustomer(act, custNo);
//		boolean error = false;
//		try {
//			customer.customerInterface = act;
//			customer.execute();
////			customer.get(10000, TimeUnit.MILLISECONDS);
//		} 
//		catch (Exception e) {
//			e.printStackTrace();;
//			error = true;
//		}
//	    finally {
//	    }
//
//		if (error)
//			return null;
//		else
//			return customer.getCustomerData();
//	}

	public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
    
    public static class Item{
        public final String text;
        public final int icon;
        public Item(String text, Integer icon) {
            this.text = text;
            this.icon = icon;
        }
        @Override
        public String toString() {
            return text;
        }
    }
    
    public static Bitmap GetImage(String imageUrl){
    	
    	Bitmap b = null;
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            File f = new  File(imageUrl);
            FileInputStream fis = new FileInputStream(f);
            o.inJustDecodeBounds = true;

            try {
                BitmapFactory.decodeStream (fis, null, o);
            } finally {
                fis.close ();
            }

            int scale = 1;
            int IMAGE_MAX_SIZE = 600;
			for (int size = Math.max (o.outHeight, o.outWidth); 
                (size>>(scale-1)) > IMAGE_MAX_SIZE ; ++scale);

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options ();
            o2.inSampleSize = scale;
            fis = new FileInputStream (f);
            try {
                b = BitmapFactory.decodeStream (fis, null, o2);
            } finally {
                fis.close ();
            }
        } catch (IOException e) {
        }
        return b;
    }
    
	public static void CopyIMGFromGallery(File sourceLocation , File targetLocation)
	throws IOException {
	
	    if (sourceLocation.isDirectory()) {
	        if (!targetLocation.exists() && !targetLocation.mkdirs()) {
	            throw new IOException("Cannot create dir " + targetLocation.getAbsolutePath());
	        }
	
	        String[] children = sourceLocation.list();
	        for (int i=0; i<children.length; i++) {
	        	CopyIMGFromGallery(new File(sourceLocation, children[i]),
	                    new File(targetLocation, children[i]));
	        }
	    } else {
	
	        // make sure the directory we plan to store the recording in exists
	        File directory = targetLocation.getParentFile();
	        if (directory != null && !directory.exists() && !directory.mkdirs()) {
	            throw new IOException("Cannot create dir " + directory.getAbsolutePath());
	        }
	
	        InputStream in = new FileInputStream(sourceLocation);
	        OutputStream out = new FileOutputStream(targetLocation);
	
	        // Copy the bits from instream to outstream
	        byte[] buf = new byte[1024];
	        int len;
	        while ((len = in.read(buf)) > 0) {
	            out.write(buf, 0, len);
	        }
	        in.close();
	        out.close();
	    }
	}
	
	public static void ConfirmDialog(final Activity activity, String sTitle, String sMessage, String sYes, String sNo){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
		
		alertDialog.setTitle(sTitle);
        alertDialog.setMessage(sMessage);
        
      
        alertDialog.setPositiveButton(sYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
//            	Intent startMain = new Intent(Intent.ACTION_MAIN);
//        	    startMain.addCategory(Intent.CATEGORY_HOME);
//        	    activity.startActivity(startMain);
//            	activity.finish();
            	
            	
            	DBA_MASTER_AGENT dba = new DBA_MASTER_AGENT(activity.getBaseContext());
	            try {
		            dba.open();
		            if (dba.getRow().getCount() != 0)
		            	dba.updateStatusLogout();
		        	        			
				} catch (Exception e) {
					e.printStackTrace();

				} finally {
					if (dba != null)
						dba.close();
				}
	            
//            	Intent intent = new Intent(Intent.ACTION_MAIN);            	
//            	intent.addCategory(intent.CATEGORY_HOME);
//            	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				
//            	activity.startActivity(intent);
            	activity.finish();
            }
        });
 
        alertDialog.setNegativeButton(sNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	dialog.cancel();
            }
        });
        
		Dialog dialog = alertDialog.show();
		  
        int textViewId = dialog.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
		TextView tv = (TextView) dialog.findViewById(textViewId);
		tv.setTextColor(activity.getResources().getColor(R.color.Black));
	}
	
	public static void sessionTimeoutLogout (final Activity activity) {
		Intent startMain = new Intent(activity.getBaseContext(), LoginActivity.class);
	    activity.startActivity(startMain);
    	activity.finish();
	}
	
	
	public static int DATEDIFF(String departureDate, String ArrivalDate) {		
		if (departureDate.isEmpty() || ArrivalDate.isEmpty())
			return 0;
		
        long MILLISECS_PER_DAY = 24 * 60 * 60 * 1000;
        int days = 1;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy"); // "dd/MM/yyyy HH:mm:ss");

        Date dateIni = null;
        Date dateFin = null;        
        try {       
            dateIni = (Date) format.parse(departureDate);
            dateFin = (Date) format.parse(ArrivalDate);
            days = (int) ((dateFin.getTime() - dateIni.getTime())/MILLISECS_PER_DAY);                        
        } catch (Exception e) {  e.printStackTrace();  }   

        return days; 
     }
	
	public static String getAnnualMonth (String departureDate) throws ParseException{
		SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy"); 
		Date annualDate = (Date) fmt.parse(departureDate);
		
		Calendar cal = Calendar.getInstance();
        cal.setTime(annualDate);
        cal.add(Calendar.DAY_OF_YEAR, 29);
	        
		return fmt.format(cal.getTime());
	}
	
	public static String getAnnualDate (String departureDate, String arrivalDate) throws ParseException{
		SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy"); 
		Date annualDate = (Date) fmt.parse(departureDate);
		
		Calendar cal = Calendar.getInstance();
        cal.setTime(annualDate);
        cal.add(Calendar.YEAR, 1);
        cal.add(Calendar.DAY_OF_YEAR, -1);
	        
		OldDate = arrivalDate;
		
		String annualDate1 = fmt.format(cal.getTime());
		return annualDate1;
	}
	
	
	public static String getOldDate (){
		return OldDate;
	}

//	@SuppressWarnings("finally")
//	public static String getFlagApproval(Context ctx, String sppa_no) throws InterruptedException{
//		
//		GetFlagApprovalDokumen app = null;
//		
//        try {
//        	app = new GetFlagApprovalDokumen(ctx, sppa_no);
//        	app.execute();
//        	app.get(10000, TimeUnit.MILLISECONDS);
//		} catch (ExecutionException e) {
//			e.printStackTrace();
//		} catch (TimeoutException e) {
//			e.printStackTrace();
//		}
//    	finally
//    	{
//        	return app.getFlagAppDokumen();    		
//    	}
//	}
//	
//	public static String getFlagPaid(Context ctx, String sppa_no) throws InterruptedException{
//		
//		GetFlagPaidDokumen app = null;
//		
//        try {
//        	app = new GetFlagPaidDokumen(ctx, sppa_no);
//        	app.execute();
//        	app.get(10000, TimeUnit.MILLISECONDS);
//        	
//		} catch (ExecutionException e) {
//			e.printStackTrace();
//		} catch (TimeoutException e) {
//			e.printStackTrace();
//		}
//    	
//    	return app.getFlagPaidDokumen();
//	}
//	
//	public static boolean validasiEffDate (String effDate){
//		Calendar c = Calendar.getInstance();
//		int hari = c.get(Calendar.DAY_OF_MONTH);
//		int bulan = c.get(Calendar.MONTH) + 1;
//		int tahun = c.get(Calendar.YEAR);
//		
//		int harief = Integer.parseInt(effDate.substring(0,2));
//		int bulanef = Integer.parseInt(effDate.substring(3,5));
//		int tahunef = Integer.parseInt(effDate.substring(6,10));
//		
//		int flag = 0;
//		
//		
//		if (tahunef < tahun) flag ++ ;
//		else if (tahunef == tahun && bulanef < bulan) flag ++;
//		else if (tahunef == tahun && bulanef == bulan && harief < hari) flag ++;
//
//		if (flag > 0)
//			return false;
//		else 
//			return true;
//	}
	
	public static boolean validasiEffDate(String effDate, String minDate, Context ctx) {
 
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date deffdate = dateFormat.parse(effDate);
			Date dmindate = dateFormat.parse(minDate);
			
			if (deffdate.before(dmindate)) {
				return false;
			}
			else { 
				return true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
		
		
	}
	
	public static boolean validasiToDate (String effDate, String toDate, Context context) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		try {
			Date deff = dateFormat.parse(effDate);
			Date dto = dateFormat.parse(toDate);
			
			if (dto.before(deff))
				return false;
			else {
				return true;
			}
		} 
		catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
				
	}
	
	public static int isExpired (String dateExp, String today) {
		String formatString = "dd/MM/yyyy"; // for example
		SimpleDateFormat df = new SimpleDateFormat(formatString);
		Date Exp = null;
		Date todays = null;
		try {
			Exp = df.parse(dateExp);
			todays = df.parse(today);
			return Exp.compareTo(todays);
		} catch (ParseException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public static String ImageToString(String path)
	{
		String sourceFileUri = path;
		File file = new File(sourceFileUri); 							
		String retValue = null;
				
        if (file.isFile())
        {
        	FileInputStream objFileIS = null;
        	ByteArrayOutputStream objByteArrayOS = null;
        	try 
            {                   
        		
        		//resizeImageFile(file, 500);
        	
        		file = new File(sourceFileUri);
        		
        		objFileIS = new FileInputStream(file);    
        		
        		objByteArrayOS = new ByteArrayOutputStream();
          	   	byte[] byteBufferString = new byte[1024];
          	   
          	   	for (int readNum; (readNum = objFileIS.read(byteBufferString)) != -1;) 
          	   		objByteArrayOS.write(byteBufferString, 0, readNum);            	   
          	                       
          	   	byte[] byteBinaryData = Base64.encode((objByteArrayOS.toByteArray()), Base64.DEFAULT);
          	   	String strAttachmentCoded = new String(byteBinaryData);
          	   	
          	   	retValue = strAttachmentCoded; 
          	   	
            } catch (Exception ex) {
          	  	ex.printStackTrace();
            }
        	finally
        	{
        		if (objByteArrayOS != null)
					try {
						objByteArrayOS.close();
						objFileIS.close();
					} catch (IOException e) {
						e.printStackTrace();
					}   

        	}
        }
        
        return retValue;

	}
	
	public static CharSequence getExpDate() {
		String month = "", day = "";
		
		if (Utility.next_month + 1 < 10) 
			month = "0" + (Utility.next_month + 1);
		else 
			month = String.valueOf(Utility.next_month + 1);
		
	    if (Utility.next_day < 10)
	    	day  = "0" + Utility.next_day ;
	    else
	    	day = String.valueOf(Utility.next_day);
	
	   return new StringBuilder().append(day).append("/").append(month).append("/").append(Utility.next_year).append("");
	}
	
	public static CharSequence getEffDate() {
		String month = "", day = "";
		
		if (Utility.next_month + 1 < 10) 
			month = "0" + (Utility.next_month + 1);
		else 
			month = String.valueOf(Utility.next_month + 1);
		
	    if (Utility.next_day < 10)
	    	day  = "0" + Utility.next_day ;
	    else
	    	day = String.valueOf(Utility.next_day);
	
	   return new StringBuilder().append(day).append("/").append(month).append("/").append(Utility.next_year).append("");
	}
//	
//	public static CharSequence setUIDate(int selectedYear, int selectedMonth, int selectedDay)
//	{
//		String year = "", month = "", day = "";
//		
//		year = String.valueOf(selectedYear);
//		
//		if (selectedMonth + 1 < 10) 
//			month = "0" + (selectedMonth + 1);
//		else 
//			month = String.valueOf(selectedMonth + 1);
//		
//	    if (selectedDay < 10)
//	    	day  = "0" + selectedDay ;
//	    else
//	    	day = String.valueOf(selectedDay);
//	
//	   return new StringBuilder().append(day).append("/").append(month).append("/").append(year).append("");
//
//	}
	/**************
	*/
	@SuppressLint("SimpleDateFormat")
	@SuppressWarnings("deprecation")
	public static String setUIDate (int year, int month, int day) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date dateIni = new Date(year - 1900, month, day);
		String dateString = dateFormat.format(dateIni);
		return dateString;
	}
	public static double countDiscount(double premi, double discpct)
	{
		return premi * discpct / 100;
	}
	
	public static double parseDouble(EditText v) throws ParseException {

		double r;
		
		NumberFormat nf = NumberFormat.getInstance();
		
		if(v.getText().toString().isEmpty())
			r = 0;
		else 
		  r =  nf.parse(v.getText().toString()).doubleValue();

		return r;
	}
	
	private static String getTodaysDate() { 
		 
        final Calendar c = Calendar.getInstance();
        int todaysDate =     (c.get(Calendar.YEAR) * 10000) + 
        ((c.get(Calendar.MONTH) + 1) * 100) + 
        (c.get(Calendar.DAY_OF_MONTH));

        return(String.valueOf(todaysDate));
 
    }
 
    private static String getCurrentTime() {
 
        final Calendar c = Calendar.getInstance();
        int currentTime =     (c.get(Calendar.HOUR_OF_DAY) * 10000) + 
        (c.get(Calendar.MINUTE) * 100) + 
        (c.get(Calendar.SECOND));
        return(String.valueOf(currentTime));
 
    }
    
    public static Double getMaxDiscount(Context ctx, String lobCode) {
    	
    	DBA_MASTER_AGENT dbAgent = null;
    	DBA_MASTER_DISC_COMM dbDiscComm = null;
    	
    	Cursor cAgent = null;
    	Cursor cDiscComm = null;
    	
    	
    	double dValue = 0;
    	try
    	{
	    	dbAgent = new DBA_MASTER_AGENT(ctx);
			dbAgent.open();
			
			cAgent = dbAgent.getRow();
			cAgent.moveToFirst();
    	
			dbDiscComm = new DBA_MASTER_DISC_COMM(ctx);
			dbDiscComm.open();
			cDiscComm = dbDiscComm.getRow(cAgent.getString(1), lobCode, cAgent.getString(0));
			
			cDiscComm.moveToFirst();

			
			dValue = cDiscComm.getDouble(4);
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		dValue = 0;
    	}
    	finally
    	{
    		if (cAgent != null)
    			cAgent.close();
    		
    		if (cDiscComm != null)
    			cDiscComm.close();
    		
    		if (dbAgent != null)
    			dbAgent.close();
    		
    		if (dbDiscComm != null)
    			dbDiscComm.close();
    	}
 
    	return dValue;
    }
    
    public static String getIsDiscountable(Context ctx, String lobCode) {
    	
    	DBA_MASTER_PRODUCT_SETTING dbProductSetting = null;
    	Cursor cProductSetting = null;
  
    	String dValue = "0";
    	try
    	{
    		dbProductSetting = new DBA_MASTER_PRODUCT_SETTING(ctx);
    		dbProductSetting.open();
			
    		cProductSetting = dbProductSetting.getRow(lobCode);
    		cProductSetting.moveToFirst();

    		Log.d("dvalue", cProductSetting.getString(7));

			
			dValue = cProductSetting.getString(7);
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		dValue = "0";
    	}
    	finally
    	{
    		if (cProductSetting != null)
    			cProductSetting.close();
    
    		if (dbProductSetting != null)
    			dbProductSetting.close();
    	}
 
    	return dValue;
    }
	
    public static void mapData(String fieldName,  HashMap<String, String> map, SoapObject tableRow)
    {
    	try
    	{
    		map.put(fieldName, tableRow.getPropertySafelyAsString(fieldName).toString());
    	}
    	catch(Exception e)
    	{
    		map.put(fieldName, "1900-01-01");
    	}
    	
    }
    
    
    public static void mapDataString(String fieldName,  HashMap<String, String> map, SoapObject tableRow)
    {
    	try
    	{
    		map.put(fieldName, tableRow.getPropertySafelyAsString(fieldName).toString());
    	}
    	catch(Exception e)
    	{
    		map.put(fieldName, "");
    	}
    	
    }
    
    public static boolean IsAllowAddSPPA(Context ctx)
    {
    	int iMaxRow = 0;
    	int iTotalSPPAInDevice = 0;
    	
    	
    	iTotalSPPAInDevice = GetTotalSPPAInDevice(ctx);
    	iMaxRow = GetMaxRow(ctx);
    
    	
    	if (iMaxRow >= iTotalSPPAInDevice)
    		return true;
    	else
    		return false;
    }
    
    
    private static int GetTotalSPPAInDevice(Context ctx)
	{
		DBA_PRODUCT_MAIN dba = null;
		Cursor c = null;
		int iTotalSPPAInDevice = 0;
		
		try
		{
			 dba = new DBA_PRODUCT_MAIN(ctx);
			 dba.open();
			 
			 c = dba.getRowsAll();
			 while(c.moveToNext())
			 {
				 iTotalSPPAInDevice++; 
			 }
		}
		catch(Exception ex)
		{
			iTotalSPPAInDevice = 0;
		}
		finally
		{
			if (c != null)
				c.close();
			
			if (dba != null)
				dba.close();
		}
		
		return iTotalSPPAInDevice;
	}
	
	private static int GetMaxRow(Context ctx)
	{
		DBA_MASTER_AGENT dba = null;
		Cursor c = null;
		int iMaxRow = 0;
		try
		{
			 dba = new DBA_MASTER_AGENT(ctx);
			 dba.open();
			 
			 c = dba.getRow();
			 
			 if (c.moveToFirst())	
			 {
				 iMaxRow = Integer.parseInt(c.getString(6));
			 }
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			iMaxRow = 0;
		}
		finally
		{
			if (c != null)
				c.close();
			
			if (dba != null)
				dba.close();
		}
		
		return iMaxRow;
	}
	
	public static void DeleteDirectory(long RowID){
		File directory = new File(Environment.getExternalStorageDirectory() + "/LoadImg/" + String.valueOf(RowID));
		if(directory.exists()){
			try {
				DeleteDirectoryFile(directory);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void DeleteDirectoryFile(File file) throws IOException
	{
		if(file.isDirectory()){
			 
    		//directory is empty, then delete it
    		if(file.list().length==0){
    		   file.delete();
    		}else{
 
    		   //list all the directory contents
        	   String files[] = file.list();
 
        	   for (String temp : files) {
        	      //construct the file structure
        	      File fileDelete = new File(file, temp);
 
        	      //recursive delete
        	      DeleteDirectoryFile(fileDelete);
        	   }
 
        	   //check the directory again, if empty then delete it
        	   if(file.list().length==0){
           	     file.delete();
        	   }
    		}
 
    	}else{
    		//if file, then delete it
    		file.delete();
    	}
	}
	
	public static void resizeImageFile(File file, int requiredSize)
	{
		
		if ((((float)file.length() / (float)1024) / (float)1024)  > (float)1)
		{
			
			Log.d("start", String.valueOf(file.length()));
			
			try {

		        // Decode image size
		        BitmapFactory.Options o = new BitmapFactory.Options();
		        o.inJustDecodeBounds = true;
		        BitmapFactory.decodeStream(new FileInputStream(file), null, o);

		        // The new size we want to scale to

		        // Find the correct scale value. It should be the power of 2.
		        int width_tmp = o.outWidth, height_tmp = o.outHeight;
		        int scale = 1;
		        while (true) {
		            if (width_tmp < requiredSize || height_tmp  < requiredSize)
		                break;
		            width_tmp /= 2;
		            height_tmp /= 2;
		            scale *= 2;
		        }

		        // Decode with inSampleSize
		        BitmapFactory.Options o2 = new BitmapFactory.Options();
		        o2.inSampleSize = scale;
		        Bitmap r =  BitmapFactory.decodeStream(new FileInputStream(file), null, o2);
		        
		        
		        file.delete ();
		        FileOutputStream out = new FileOutputStream(file);
		        r.compress(Bitmap.CompressFormat.PNG, 90, out);
		        out.flush();
		        out.close();
		        
		        Log.d("done", "done");
		        
		    } catch (Exception e) {
		    	Log.d("fail", String.valueOf(file.length()));
		    	e.printStackTrace();
		    }
			
			/*
			try {
			       FileOutputStream out = new FileOutputStream(file);
			       finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
			       out.flush();
			       out.close();

			} catch (Exception e) {
			       e.printStackTrace();
			}
			*/
		}
		
		/*
		try {

	        // Decode image size
	        BitmapFactory.Options o = new BitmapFactory.Options();
	        o.inJustDecodeBounds = true;
	        BitmapFactory.decodeStream(new FileInputStream(file), null, o);

	        // The new size we want to scale to

	        // Find the correct scale value. It should be the power of 2.
	        int width_tmp = o.outWidth, height_tmp = o.outHeight;
	        int scale = 1;
	        while (true) {
	            if (width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize)
	                break;
	            width_tmp /= 2;
	            height_tmp /= 2;
	            scale *= 2;
	        }

	        // Decode with inSampleSize
	        BitmapFactory.Options o2 = new BitmapFactory.Options();
	        o2.inSampleSize = scale;
	        return BitmapFactory.decodeStream(new FileInputStream(file), null,
	                o2);
	    } catch (Exception e) {
	    }
	    return null;
	    */
	}
    
    public static String ERROR_OVER_CAPACITY_SPPA = "Jumlah SPPA yang belum dilengkapi, disubmit dan dibayar melebih kapasitas.";

    public static final class City implements Comparable<City>
    {
    	public final String text;
    	public final String id;

        public City(String id, String text) {
            this.text = text;
            this.id = id;
        }
        
        @Override
        public int compareTo(City other){
            // compareTo should return < 0 if this is supposed to be
            // less than other, > 0 if this is supposed to be greater than 
            // other and 0 if they are supposed to be equal
            int last = this.text.compareTo(other.text);
            return last;
        }
    }
    
    public static final class Country implements Comparable<Country>
    {
    	public final String text;
    	public final String id;

        public Country(String id, String text) {
            this.text = text;
            this.id = id;
        }
        
        @Override
        public int compareTo(Country other){
            // compareTo should return < 0 if this is supposed to be
            // less than other, > 0 if this is supposed to be greater than 
            // other and 0 if they are supposed to be equal
            int last = this.text.compareTo(other.text);
            return last;
        }
    }
    
    
    public static final class CarType implements Comparable<CarType>
    {
    	public final String text;
    	public final String id;

        public CarType(String id, String text) {
            this.text = text;
            this.id = id;
        }
        
        @Override
        public int compareTo(CarType other){
            // compareTo should return < 0 if this is supposed to be
            // less than other, > 0 if this is supposed to be greater than 
            // other and 0 if they are supposed to be equal
            int last = this.text.compareTo(other.text);
            return last;
        }
    }
    
    public static final class CarBrand implements Comparable<CarBrand>
    {
    	public final String text;
    	public final String id;

        public CarBrand(String id, String text) {
            this.text = text;
            this.id = id;
        }
        
        @Override
        public int compareTo(CarBrand other){
            // compareTo should return < 0 if this is supposed to be
            // less than other, > 0 if this is supposed to be greater than 
            // other and 0 if they are supposed to be equal
            int last = this.text.compareTo(other.text);
            return last;
        }
    }
    
    public static boolean cekLogin (Context ctxContext){
		
    	DBA_MASTER_AGENT dba = null;
		Cursor c = null;
		boolean login = false;
		
		
		try{
			dba = new DBA_MASTER_AGENT(ctxContext);
			dba.open();
			c = dba.getRow();
			if (c.getCount() == 0)
			{
				login = false;
				return login ;
			}
				
			c.moveToFirst();
			if (c == null)
				return false;
			
			if(c.getString(7).equals("LOGIN")) {
				login = true;
			}
			else 
				login = false;
				
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		finally {
			if (c != null)
				c.close();
			
			if (dba!= null)
				dba.close();
		}
		return login;
		
	}
    
    public static Dialog showCustomDialogInformation(Context ctx, String title, String content)
	{
		final Dialog dialog = new Dialog(ctx);
		dialog.setContentView(R.layout.dialog_information_all);
		dialog.setTitle(title);
		
		int textViewId = dialog.getContext().getResources().getIdentifier("android:id/title", null, null);
		TextView tv = (TextView) dialog.findViewById(textViewId);
		tv.setTextColor(ctx.getResources().getColor(R.color.Black)); 
		 tv.setSingleLine(false);
		
		((TextView)dialog.findViewById(R.id.tvWellWomanInfo))
		.setText(content);
		
		((Button)dialog.findViewById(R.id.dlgOK))
		.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();			
			}
		});
		dialog.show();
		
		return dialog;
		
	}
    
    /******** with finish activity ***************/
    
    public static void showCustomDialogInformation(Context ctx, String title, String content, final Activity act)
   	{
   		final Dialog dialog = new Dialog(ctx);
   		dialog.setContentView(R.layout.dialog_information_all);
   		dialog.setTitle(title);
   		
   		((TextView)dialog.findViewById(R.id.tvWellWomanInfo))
   		.setText(content);
   		
   		int textViewId = dialog.getContext().getResources().getIdentifier("android:id/title", null, null);
		TextView tv = (TextView) dialog.findViewById(textViewId);
		tv.setTextColor(ctx.getResources().getColor(R.color.Black));
   		
   		
   		((Button)dialog.findViewById(R.id.dlgOK))
   		.setOnClickListener(new OnClickListener() {
   			
   			@Override
   			public void onClick(View v) {
   				dialog.dismiss();	
   				act.finish();
   				
   			}
   		});
   		dialog.show();
   		
   	}

    public static void BindJenisUsahaLiability(Spinner s, Context ctx, Activity act) {

		/*
    	List<SpinnerGenericItem> spinnerGenericList = new LinkedList<SpinnerGenericItem>();
		SpinnerGenericAdapter spinnerGenericAdapter = new SpinnerGenericAdapter(spinnerGenericList, act);
		
		// database handler 
		DBA_MASTER_JENIS_TOKO db = new DBA_MASTER_JENIS_TOKO(ctx);
		db.open();
  
        // Spinner Drop down elements 
        Cursor c = db.getAll(); 
  
        if (c.moveToFirst()) { 
            do { 
            	spinnerGenericList.add(new SpinnerGenericItem(c.getString(0), c.getString(1))); 
            } while (c.moveToNext()); 
        } 
          
        // closing connection 
        c.close(); 
        db.close(); 
        
        s.setAdapter(spinnerGenericAdapter);
        */
    	
    	List<SpinnerGenericItem> spinnerGenericList = new LinkedList<SpinnerGenericItem>();
		SpinnerGenericAdapter spinnerGenericAdapter = new SpinnerGenericAdapter(spinnerGenericList, act);
		DBA_MASTER_BUSINESS_TYPE dba_MASTER_BUSINESS_TYPE = new DBA_MASTER_BUSINESS_TYPE(ctx);
		Cursor c = null;
		int counter = 1;
		
		try {
			dba_MASTER_BUSINESS_TYPE.open();
			c = dba_MASTER_BUSINESS_TYPE.getAll();
			
			spinnerGenericList.add(new SpinnerGenericItem("0", "-- Jenis Perusahaan --")); 
			
			if (c.moveToFirst()) {
				do {
					spinnerGenericList.add(new SpinnerGenericItem(counter+"", c.getString(0)));
					counter++ ;				
				}while (c.moveToNext());
			}
			s.setAdapter(spinnerGenericAdapter);
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		finally {
			if (dba_MASTER_BUSINESS_TYPE != null)
				dba_MASTER_BUSINESS_TYPE.close();
			
			if (c != null)
				c.close();
			
		}
	}

    
    public static void BindVesselType(Spinner s, Context ctx, Activity act) {

    	List<SpinnerGenericItem> spinnerGenericList = new LinkedList<SpinnerGenericItem>();
		SpinnerGenericAdapter spinnerGenericAdapter = new SpinnerGenericAdapter(spinnerGenericList, act);
		
		DBA_MASTER_VESSEL_TYPE dba = new DBA_MASTER_VESSEL_TYPE(ctx);
		Cursor c = null;
		
		try {
			dba.open();
			c = dba.getAll();
			
			if (c.moveToFirst()) {
				do {
					spinnerGenericList.add(new SpinnerGenericItem(c.getString(1), c.getString(4)));			
				}while (c.moveToNext());
			}
			s.setAdapter(spinnerGenericAdapter);
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		finally {
			if (dba != null)
				dba.close();
			
			if (c != null)
				c.close();
			
		}
	}
    
    
	public static int getDatabaseVersion() {
		return DATABASE_VERSION;
	}
    
	public static void DeleteRecursive(File fileOrDirectory) {
	    if (fileOrDirectory.isDirectory())
	        for (File child : fileOrDirectory.listFiles())
	            DeleteRecursive(child);

	    fileOrDirectory.delete();
	}
	
	public static TextWatcher registerTextChangedListener (final EditText fieldCurrency) {
		return new TextWatcher() {
			private String current = "";
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			
				   if (!s.toString().equals(current)) {
					   fieldCurrency.removeTextChangedListener(this);     
		                
		                String replaceable = String.format("[%s,.\\s]", "Rp. ");
		                String cleanString = s.toString().replaceAll(replaceable, "");  
		            		                
		                double parsed;
		                try {
		                    parsed = Double.parseDouble(cleanString);
		                } catch (NumberFormatException e) {
		                    parsed = 0.00;
		                }
		                String formatted = NumberFormat.getInstance().format((parsed));                 
		                
		                current = formatted;
		                fieldCurrency.setText(formatted);
		                fieldCurrency.setSelection(formatted.length());
		                fieldCurrency.addTextChangedListener(this);
		            }                           
			}
		};
	}
	
	public static boolean cekInternetConnection (Context context) {
	
		ConnectionDetector cDetector = new ConnectionDetector(context);
		boolean isConnect =  cDetector.isConnectingToInternet();
		
		if (!isConnect) {
			Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
			return false;
		}
		else return true;
		
	}

	
	public static String getPropertyTable (SoapObject s, String objectKey) {
		if (( s.getPropertySafelyAsString(objectKey)) == null ) {
			Log.d("getpropery table", s.getPropertySafelyAsString(objectKey));
			return "";
		}
		else 
			return s.getPropertySafelyAsString(objectKey);
	}

	public static int getDurationSyncImage() {
		return durationSyncImage;
	}

	public static void setDurationSyncImage(int durationSyncImage) {
		Utility.durationSyncImage = durationSyncImage;
	}

	public static int getDurationLogoutSession() {
		return durationLogoutSession;
	}

	public static void setDurationLogoutSession(int durationLogoutSession) {
		Utility.durationLogoutSession = durationLogoutSession;
	}

	public static String getSpecialChars() {
		return specialChars;
	}

	public static void setSpecialChars(String specialChars) {
		Utility.specialChars = specialChars;
	}
	
	public static void toastMakeTextShort  (Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}
	
	public static void toastMakeTextLong  (Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}
	
	@SuppressWarnings("deprecation")
	public static boolean hasSpecialChar (String message) {	
		boolean specialChar = false;
		
		for (Character c : message.toCharArray()) {
			if (!Character.isLetterOrDigit(c) && !Character.isSpace(c)) {
				specialChar = true;
				break;
			}	
		}
		return specialChar;
	}
	
	public static boolean isEmptyField (EditText yourField) {
		if (TextUtils.isEmpty(yourField.getText())) {
			yourField.requestFocus();
			yourField.setError("Please Fill the Blank");
			return false;
		}
		else {
			return true;
		}
	} 
	
	public static String getUserID (Context ctx) {
		DBA_MASTER_AGENT dbAgent = null;
	 	Cursor c = null;
	 	
		try {
			dbAgent = new DBA_MASTER_AGENT(ctx);				
			
			c = dbAgent.open().getRow();
			c.moveToFirst();			
			
			return c.getString(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
			
			return "";
		}
		finally {
			if (dbAgent != null )
				dbAgent.close();
			if (c != null) 
				c.close();
		}
		
		
	}
	
	public static final String md5(final String s) {
	    final String MD5 = "MD5";
	    try {
	        // Create MD5 Hash
	        MessageDigest digest = java.security.MessageDigest
	                .getInstance(MD5);
	        digest.update(s.getBytes());
	        byte messageDigest[] = digest.digest();

	        // Create Hex String
	        StringBuilder hexString = new StringBuilder();
	        for (byte aMessageDigest : messageDigest) {
	            String h = Integer.toHexString(0xFF & aMessageDigest);
	            while (h.length() < 2)
	                h = "0" + h;
	            hexString.append(h);
	        }
	        return hexString.toString();

	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
	    return "";
	}

	public static String getAnytypeStr() {
		return anytypeStr;
	}

	public static void setAnytypeStr(String anytypeStr) {
		Utility.anytypeStr = anytypeStr;
	}

	public static int getDatabaseVersionBig() {
		return DATABASE_VERSION_BIG;
	}
	
	public static String ifAnytype (String value) {
		if (value.toLowerCase().contains("anytype"))
			return "";
		else
			return value;
	}
	
	public static boolean isAddedPhoto (long SPPA_ID) {
		String folder = Environment.getExternalStorageDirectory() +"/LoadImg/" + String.valueOf(SPPA_ID);
        File f = new File(folder);
        File[] files=f.listFiles();
        
        
		try {
			if (files != null)
			{
				Log.i(TAG, "::isAddedPhoto:" + "length=" +  files.length + "");
				
				if (files.length <= 1) 
					return false;			
				else 
					return true;
			}
			else 
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	


    public static LocalDateTime parseUTC(String tanggal) {
    	//DateTime dt = new DateTime(tanggal, DateTimeZone.UTC);
        DateTime dt = new DateTime(tanggal);
        LocalDateTime localDateTime = dt.toLocalDateTime();
        return localDateTime;
    }
    

    public static LocalDateTime getDateTime() {
        LocalDateTime localDateTime = LocalDateTime.now(DateTimeZone.forTimeZone(TimeZone.getTimeZone("GMT+7")));
        return localDateTime;
    }

    
	
}



