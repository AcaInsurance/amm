package com.aca.amm;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.aca.amm.R;
import com.aca.amm.FillMedisafe.DeleteSPPA;
import com.aca.amm.R.id;
import com.aca.amm.R.layout;
import com.aca.amm.R.menu;
import com.aca.database.DBA_MASTER_TOKO_RATE;
import com.aca.database.DBA_PRODUCT_MAIN;
import com.aca.database.DBA_PRODUCT_TOKO;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class FillTokoActivity extends ControlNormalActivity {

	private ArrayList<HashMap<String, String>> custList ;
	
	private Context context = null;
	
	private Bundle b;
	private NumberFormat nf;
	private String PRODUCT_ACTION;
	private long SPPA_ID;
	private int flag = 0;
	private Calendar c;
    
    private double v_rate = 0, v_tsi = 0, v_premi = 0, v_polis = 0, v_materai = 0, v_total = 0, v_discpct = 0, v_disc = 0;
	private EditText etFromDate, etToDate;
	private Spinner spJenisUsaha, spCity;
	private static final int DATE_FROM_ID = 99;
	private static final int DATE_TO_ID = 100;
	private EditText kodepos, luas, IDRluas, IDRperabotan, IDRisibarang, IDRtotal;
	private EditText txtAlamatLain, txtKodePosLain, umurBangunan, rate;
	private EditText premi, polis, materai, total, customerno, customername, discpct, disc;	
	private Switch shoppingcenter, miliksendiri, franchise, firesprinkler, alamatsama;
	
	private String UIErrorMessage = "Lengkapi semua data";
	private double MAX_DISCOUNT = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_fill_toko);

		context = FillTokoActivity.this;
		
		nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		
		spJenisUsaha = (Spinner)findViewById(R.id.spinnerJenisUsaha);
		Utility.BindJenisUsaha(spJenisUsaha, getBaseContext(), this);
		
		spCity = (Spinner)findViewById(R.id.spinnerPropinsi);
		Utility.BindCityProv(spCity, getBaseContext(), this);
		
		InitControls();
		RegisterListener();
		
		try{
			
			Intent i = getIntent();
			b = i.getExtras();
			
			PRODUCT_ACTION = b.getString("PRODUCT_ACTION");
			
			if (PRODUCT_ACTION.equals("EDIT") || PRODUCT_ACTION.equals("VIEW")) {
				SPPA_ID = b.getLong("SPPA_ID");
				LoadDB();
			}
			
			if (b.getString("CUSTOMER_NO") != null) {
				customerno.setText(b.getString("CUSTOMER_NO"));
				customername.setText(b.getString("CUSTOMER_NAME"));
			}
			
			MAX_DISCOUNT = Utility.getMaxDiscount(getBaseContext(), "11");
			
			if (Utility.getIsDiscountable(getBaseContext(), "11").equals("0"))
			{
				disc.setEnabled(false);
				discpct.setEnabled(false);
			}
		}
		catch(Exception e){	
			e.printStackTrace();
		}		
	}
	
	private void InitControls() {
		kodepos = (EditText)findViewById(R.id.txtKodePos);
		luas = (EditText)findViewById(R.id.txtLuasBangunan);
		IDRluas = (EditText)findViewById(R.id.txtHargaBangunan);
		IDRperabotan = (EditText)findViewById(R.id.txtPerabotan);
		
		IDRisibarang = (EditText)findViewById(R.id.txtDagangan);
		IDRtotal = (EditText)findViewById(R.id.txtTotalNilaiHarta);
		
		IDRtotal.setEnabled(false);
		
		txtAlamatLain = (EditText)findViewById(R.id.txtalamat);
		txtKodePosLain = (EditText)findViewById(R.id.txtKodePos2);
		
		umurBangunan = (EditText)findViewById(R.id.txtUmurBangunan);
		rate = (EditText)findViewById(R.id.txtRate);
	
		materai = (EditText)findViewById(R.id.txtMaterai);
		premi = (EditText)findViewById(R.id.txtPremi);
		polis = (EditText)findViewById(R.id.txtBiayaPolis);
		total = (EditText)findViewById(R.id.txtTotal);
		
		discpct = (EditText)findViewById(R.id.txtDiscountPct);
		disc = (EditText)findViewById(R.id.txtDiscount);
		
		shoppingcenter = (Switch)findViewById(R.id.swiShoppingCenter);
		miliksendiri = (Switch)findViewById(R.id.swiMilikSendiri);
		franchise = (Switch)findViewById(R.id.swiGroupFranchise);
		firesprinkler = (Switch)findViewById(R.id.swiSprinkler); 
		alamatsama  = (Switch)findViewById(R.id.swiAlamatSama);
		
		customerno = (EditText)findViewById(R.id.txtCustomerNo);
		customername = (EditText)findViewById(R.id.txtCustomerName);
		customerno.setEnabled(true);
	}
	
	private void RegisterListener(){
		etToDate  = (EditText)findViewById(R.id.txttodate);
		etToDate.setKeyListener(null);

		etFromDate = (EditText)findViewById(R.id.txtfromdate);
		etFromDate.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View arg0, boolean hasFocus) {
				if(hasFocus)
					showDialog(DATE_FROM_ID);
				else { 
					((Button)findViewById(R.id.btnNext)).setEnabled(true);
					etFromDate.setTextColor(Color.BLACK);
					boolean flag = Utility.validasiEffDate(etFromDate.getText().toString(), 
										Utility.GetTomorrowString(Utility.GetTodayString()), getBaseContext());
					
					if (!flag) {
						etFromDate.setTextColor(Color.RED);
						Utility.showCustomDialogInformation(FillTokoActivity.this, "Warning", "Tanggal mulai minimal besok hari");
						((Button)findViewById(R.id.btnNext)).setEnabled(false);
					}
				}
			}
		});
		
		etFromDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showDialog(DATE_FROM_ID);
			}
		});
//		
//		alamatsama.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			@Override
//			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
//				if(alamatsama.isChecked() && !customerno.getText().toString().isEmpty()){
//					try {
//						custList = Utility.getCustData(FillTokoActivity.this, customerno.getText().toString());
//						if(custList != null) {
//							txtAlamatLain.setText(custList.get(0).get("CUSTOMER_ADDRESS").toString());
//							txtKodePosLain.setText(custList.get(0).get("CUSTOMER_KODE_POS").toString());
//							SpinnerGenericAdapter a = (SpinnerGenericAdapter)spCity.getAdapter();
//							spCity.setSelection(a.getItemId(custList.get(0).get("CUSTOMER_CITY_CODE").toString()));
//
//						}		
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//				else {
//					txtAlamatLain.setText("");
//					txtKodePosLain.setText("");
//					spCity.setSelection(0);
//				}
//			}
//		});

		spJenisUsaha.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				try {
					if(!customerno.getText().toString().isEmpty() && !etFromDate.getText().toString().isEmpty() && !etToDate.getText().toString().isEmpty()){
						calculateAll();
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}	
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		
		luas.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus) {
					//
				}
				else {	
					double bangunan = 2500000 * Integer.parseInt(luas.getText().toString());
					IDRluas.setText(nf.format(bangunan));
				}
		}});
		
		IDRluas.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus) {
					IDRluas.setText(Utility.removeComma(IDRluas.getText().toString()));
				}
				else {
					try {
						if (IDRluas.getText().toString().isEmpty())
							return ;
					
						IDRluas.setText(nf.format(nf.parse(IDRluas.getText().toString())));	
						calculateAll();
						
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}});
		
		IDRperabotan.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus)
					IDRperabotan.setText(Utility.removeComma(IDRperabotan.getText().toString()));
				else {
					try {
						if (IDRperabotan.getText().toString().isEmpty())
							return ;
						
						IDRperabotan.setText(nf.format(nf.parse(IDRperabotan.getText().toString())));
						calculateAll();
						
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}});
		
		IDRisibarang.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus) {
					IDRisibarang.setText(Utility.removeComma(IDRisibarang.getText().toString()));
				}
				else {
					try {
						if (IDRisibarang.getText().toString().isEmpty())
							return ;
						
						IDRisibarang.setText(nf.format(nf.parse(IDRisibarang.getText().toString())));
						calculateAll();
						
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}});
		
		shoppingcenter.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if(shoppingcenter.isChecked()){
					Utility.showCustomDialogInformation(FillTokoActivity.this, "Informasi", 
							"Toko tidak boleh berada di shopping centre)");	
					shoppingcenter.setChecked(false);
				}
			}
		});
		
		
		discpct.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus) {
					discpct.setText(Utility.removeComma(discpct.getText().toString()));
				}
				else {
					try {
						if (discpct.getText().toString().isEmpty())
							return ;
						
						discpct.setText(nf.format(nf.parse(discpct.getText().toString())));
						calculateAll();
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		disc.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus) {
					disc.setText(Utility.removeComma(disc.getText().toString()));
				}
				else {
					try {
						if (disc.getText().toString().isEmpty())
							return ;
						
						disc.setText(nf.format(nf.parse(disc.getText().toString())));
						calculateAll();
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	private void calculateAll() throws ParseException {
		v_tsi = getTSI();
		v_rate = getRate();
		v_premi = Utility.countPremi(v_rate, v_tsi);
		v_discpct = Utility.parseDouble(discpct);
		v_disc = Utility.countDiscount(v_premi, v_discpct);
		v_polis = 0;
		v_materai = Utility.countMaterai(v_premi);
		v_total = Utility.countTotal(v_premi, v_polis, v_materai, v_disc);

		IDRtotal.setText(nf.format(v_tsi));				    
		rate.setText(nf.format(v_rate));
		premi.setText(nf.format(v_premi));
		disc.setText(nf.format(v_disc));
		polis.setText(nf.format(v_polis));
		materai.setText(nf.format(v_materai));
		total.setText(nf.format(v_total));
	}
	
	private void LoadDB() {

		DBA_PRODUCT_MAIN dbm = null;
		DBA_PRODUCT_TOKO dba = null;
		
		Cursor cm  = null;
		Cursor c = null;
		
		try{
			
			dbm = new DBA_PRODUCT_MAIN(getBaseContext());
			dba = new DBA_PRODUCT_TOKO(getBaseContext());
			
			dbm.open();
			cm = dbm.getRow(SPPA_ID);
			cm.moveToFirst();
			
			dba.open();
			c = dba.getRow(SPPA_ID);
			c.moveToFirst();
			
			customerno.setText(cm.getString(1));
			customername.setText(cm.getString(2));
			
			discpct.setText(nf.format(cm.getDouble(23)));
			disc.setText(nf.format(cm.getDouble(24)));
			
			if (cm.getString(20).equals("R")){
				etFromDate.setEnabled(false);
				etToDate.setEnabled(false);
				
				spCity.setEnabled(false);
				kodepos.setEnabled(false);
				spJenisUsaha.setEnabled(false);
				alamatsama.setEnabled(false);
				txtAlamatLain.setEnabled(false);
				txtKodePosLain.setEnabled(false);
				
				customerno.setEnabled(false);
				((Button)findViewById(R.id.btnChooseCustomer)).setEnabled(false);
			}
			if(c.getLong(28) == 1)
				shoppingcenter.setChecked(true);
			
			if(c.getLong(29) == 1)
				miliksendiri.setChecked(true);

			SpinnerGenericAdapter a = (SpinnerGenericAdapter)spJenisUsaha.getAdapter();
			spJenisUsaha.setSelection(a.getItemId(c.getString(30)));

			
			if(c.getLong(31) == 1)
				franchise.setChecked(true);

			a = (SpinnerGenericAdapter)spCity.getAdapter();
			spCity.setSelection(a.getItemId(c.getString(13)));

			kodepos.setText(c.getString(14));
			
			luas.setText(String.valueOf(c.getInt(2)));
			IDRluas.setText(nf.format(c.getDouble(3)));
			IDRperabotan.setText(nf.format(c.getDouble(4)));
			IDRisibarang.setText(nf.format(c.getDouble(5)));
			
			double total = c.getDouble(3) + c.getDouble(4) + c.getDouble(5);
			IDRtotal.setText(nf.format(total));
			
			txtAlamatLain.setText(c.getString(8));
			
			if(c.getLong(7) == 1)
				alamatsama.setChecked(true);
				
			if(c.getLong(36) == 1)
				firesprinkler.setChecked(true);
			
			txtKodePosLain.setText(c.getString(27));
			
			umurBangunan.setText(c.getString(32));
			
			etFromDate.setText(c.getString(20));
			etToDate.setText(c.getString(21));
			
			rate.setText(nf.format(c.getDouble(22)));
			premi.setText(nf.format(c.getDouble(23)));
			polis.setText(nf.format(c.getDouble(24)));
			materai.setText(nf.format(c.getDouble(25)));
			this.total.setText(nf.format(c.getDouble(26)));
			
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			if (cm != null)
				cm.close();
			
			if (c != null)
				c.close();
			
			if (dbm != null)
				dbm.close();
			
			if (dba != null)
				dba.close();
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putAll(b);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		
		b = savedInstanceState;
		
		try{
			SPPA_ID = b.getLong("SPPA_ID");
			PRODUCT_ACTION = b.getString("PRODUCT_ACTION");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.fill_toko, menu);
		return true;
	}
	
	public void btnHomeClick(View v) {
		Intent i = new Intent(getBaseContext(),  FirstActivity.class);
		startActivity(i);
		this.finish();
	}
	
	public void btnBackClick(View v)
	{
		try{
			Intent i = null;
			
			if (PRODUCT_ACTION.equals("NEW"))
				i = new Intent(getBaseContext(),  ChooseProductActivity.class);
			else
				i = new Intent(getBaseContext(),  SyncActivity.class);
			
			startActivity(i);
			this.finish();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
public void btnDeleteClick(View v){
		
		showConfirmDelete(getBaseContext(), SPPA_ID, "").show();
	}
	
    private AlertDialog showConfirmDelete(final Context ctx, final long rowId, final String noPolis) {
	    AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
	        .setTitle("Hapus") 
	        .setMessage("Hapus data ini sekarang?") 
	        .setIcon(R.drawable.delete)
	        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

        		public void onClick(DialogInterface dialog, int whichButton) { 
	            	
        			new DeleteSPPA().execute();
	                dialog.dismiss();
	            }      

	        })
	        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	                dialog.dismiss();
	            }
	        })
	        .create();
	        return myQuittingDialogBox;	
	}
	
	public void showDisc(View v) {
		TableRow trDiscPct = (TableRow)findViewById(R.id.trDiscPct);
		TableRow trDisc = (TableRow)findViewById(R.id.trDisc);
		trDiscPct.setVisibility(View.VISIBLE);
		trDisc.setVisibility(View.VISIBLE);
	}
	
	private void validasiNext () throws ParseException{
		flag = 0;
		if( customername.getText().toString().isEmpty()){
			flag++ ;
			customername.setHintTextColor(Color.RED);
			UIErrorMessage = "Pilih customer";
		}
		if( customerno.getText().toString().isEmpty()){
			flag++ ;
			customerno.setHintTextColor(Color.RED);
			UIErrorMessage = "Pilih customer";
		}
		if (luas.getText().toString().isEmpty() ){
			flag++ ;
			luas.setHintTextColor(Color.RED);
			UIErrorMessage = "Luas toko harus diisi";
		}
		if (kodepos.getText().toString().isEmpty() ){
			flag++ ;
			kodepos.setHintTextColor(Color.RED);
			UIErrorMessage = "Kode Pos lokasi toko harus diisi";
		}
		if (IDRluas.getText().toString().isEmpty() ){
			flag++ ;
			IDRluas.setHintTextColor(Color.RED);
			UIErrorMessage = "Harga bangunan harus diisi, jika tidak ada masukkan angka 0";
		}
		if (IDRperabotan.getText().toString().isEmpty() ){
			flag++ ;
			IDRperabotan.setHintTextColor(Color.RED);
			UIErrorMessage = "Harga perabotan harus diisi, jika tidak ada masukkan angka 0";
		}
		if (IDRisibarang.getText().toString().isEmpty() ){
			flag++ ;
			IDRisibarang.setHintTextColor(Color.RED);
			UIErrorMessage = "Harga isi barang dagangan harus diisi, jika tidak ada masukkan angka 0";
		}
		if (IDRtotal.getText().toString().isEmpty()){
			flag++ ;
			IDRtotal.setHintTextColor(Color.RED);
			UIErrorMessage = "Total harga tidak bisa dihitung. Hubungi IT ACA";
		}
		if (umurBangunan.getText().toString().isEmpty() ){
			flag++ ;
			umurBangunan.setHintTextColor(Color.RED);
			UIErrorMessage = "Umur bangunan harus diisi";
		}
		if (etFromDate.getText().toString().isEmpty() ){
			flag++ ;
			etFromDate.setHintTextColor(Color.RED);
			UIErrorMessage = "Tanggal mulai harus diisi";
		}
		if( etToDate.getText().toString().isEmpty() ){
			flag++ ;
			etToDate.setHintTextColor(Color.RED);
			UIErrorMessage = "Tanggal berakhir harus diisi";
		}
		if (txtAlamatLain.getText().toString().isEmpty() ){
			flag++ ;
			txtAlamatLain.setHintTextColor(Color.RED);
			UIErrorMessage = "Alamat toko harus diisi";
		}
		if( txtKodePosLain.getText().toString().isEmpty() ){
			flag++ ;
			txtKodePosLain.setHintTextColor(Color.RED);
			UIErrorMessage = "Kode pos toko harus diisi";
		}
		if(rate.getText().toString().isEmpty()){
			flag++ ;
			rate.setHintTextColor(Color.RED);
			UIErrorMessage = "Rate tidak bisa dihitung. Hubungi IT ACA";
		}
		if(premi.getText().toString().isEmpty()){
			flag++ ;
			premi.setHintTextColor(Color.RED);
			UIErrorMessage = "Premi tidak bisa dihitung. Hubungi IT ACA";
		}
		if(discpct.getText().toString().isEmpty()){
			flag++ ;
			discpct.setHintTextColor(Color.RED);
			UIErrorMessage = "Diskon % harus diisi, jika tidak ada masukkan angka 0";
		}
		if(disc.getText().toString().isEmpty()){
			flag++ ;
			disc.setHintTextColor(Color.RED);
			UIErrorMessage = "Diskon harus diisi, jika tidak ada masukkan angka 0";
		}
		if(shoppingcenter.isChecked()) {
			flag ++;
			UIErrorMessage = "Toko tidak boleh berada di shopping centre";
		}
		if(getTSI() > 2500000000.00){
			flag++;
			IDRtotal.setTextColor(Color.RED);
			UIErrorMessage = "Maksimum nilai pertanggungan Rp. 2,500,000,000";
		}
		if (Utility.parseDouble(discpct) > MAX_DISCOUNT){
			flag++;
			discpct.setTextColor(Color.RED);
			UIErrorMessage = "% diskon terlalu besar, maksimum adalah " + String.valueOf(MAX_DISCOUNT) + "%";
		}
	}

	public void btnNextClick(View v)
	{
		try{			
			validasiNext();
			
			if(flag != 0)	
				Utility.showCustomDialogInformation(FillTokoActivity.this, "Warning", 
						UIErrorMessage);	
			else{
				calculateAll();
				insertDB();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void btnChooseCustomerClick(View v) {
		Intent i = new Intent(getBaseContext(),  ChooseCustomerForBuyActivity.class);
		i.putExtras(b);
		startActivity(i);
		this.finish();
//		
//		if (customerno.getText().length() == 0) {
//			Intent i = new Intent(getBaseContext(),  ChooseCustomerForBuyActivity.class);
//			i.putExtras(b);
//			startActivity(i);
//			this.finish();
//		}
//		else {
//			try {
//				custList = Utility.getCustData(getBaseContext(), customerno.getText().toString());
//				if(custList != null)
//					customername.setText(custList.get(0).get("CUSTOMER_NAME"));
//				else {
//					customerno.setTextColor(Color.RED);
//					Toast.makeText(getBaseContext(), "Nasabah tidak ditemukan", Toast.LENGTH_SHORT).show();
//					customername.setText("");
//				}
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
	}
	
	public void insertDB()
	{
		DBA_PRODUCT_TOKO dba = null;
		DBA_PRODUCT_MAIN dba2 = null;
		
		try{
			
			if (PRODUCT_ACTION.equals("VIEW"))
			{
				Intent i = new Intent(getBaseContext(),  ConfirmActivity.class);
				b.putLong("SPPA_ID", SPPA_ID);
				i.putExtras(b);
				startActivity(i);
				this.finish();
			}
			else
			{
			
				dba = new DBA_PRODUCT_TOKO(getBaseContext());
				dba2 = new DBA_PRODUCT_MAIN(getBaseContext());
				
				dba.open();
				dba2.open();
			
				int scenter, owner, franch, alamat_sama, fsprinkler;
	
				if (shoppingcenter.isChecked()) scenter = 1; else scenter = 0;
				
				if (miliksendiri.isChecked()) owner = 0; else owner = 1;
			
				if (franchise.isChecked()) franch = 1; else franch = 0;
				
				if (alamatsama.isChecked()) alamat_sama = 1; else alamat_sama = 0;
				
				if(firesprinkler.isChecked()) fsprinkler = 1; else fsprinkler = 0;
				
				if (PRODUCT_ACTION.equals("NEW")) {	
					SPPA_ID = dba2.initialInsert(customerno.getText().toString(), 
							customername.getText().toString(), "TOKO",
							nf.parse(premi.getText().toString()).doubleValue(),
							nf.parse(materai.getText().toString()).doubleValue(),
							nf.parse(polis.getText().toString()).doubleValue(),
							nf.parse(total.getText().toString()).doubleValue(),
							etFromDate.getText().toString(), 
							etToDate.getText().toString(),
							Utility.DateToString(Utility.GetToday(), "dd/MM/yyyy"),
							"","","","N","",
							nf.parse((discpct.getText().toString())).doubleValue(),
							nf.parse((disc.getText().toString())).doubleValue());
					
					dba.initialInsert(SPPA_ID, kodepos.getText().toString().toUpperCase(), scenter,
						owner, 
						((SpinnerGenericItem)spJenisUsaha.getSelectedItem()).getCode(), 
						franch,
						alamat_sama, 
						Integer.parseInt(luas.getText().toString()), 
						nf.parse(IDRluas.getText().toString()).doubleValue(),
						nf.parse(IDRperabotan.getText().toString()).doubleValue(),
						nf.parse(IDRtotal.getText().toString()).doubleValue(), 
						txtAlamatLain.getText().toString(), "","", "", "", 
						((SpinnerGenericItem)spCity.getSelectedItem()).getCode(),
						txtKodePosLain.getText().toString(),"0", "0","0","0","0", 
						Integer.parseInt(umurBangunan.getText().toString()), 
						etFromDate.getText().toString(), 
						etToDate.getText().toString(),
						nf.parse(rate.getText().toString()).doubleValue(),
						nf.parse(premi.getText().toString()).doubleValue(),
						nf.parse(materai.getText().toString()).doubleValue(),
						nf.parse(polis.getText().toString()).doubleValue(),
						nf.parse(total.getText().toString()).doubleValue(),
						"TOKO", customername.getText().toString(), 
						((SpinnerGenericItem)spJenisUsaha.getSelectedItem()).getDesc(),
						nf.parse(IDRisibarang.getText().toString()).doubleValue(), fsprinkler,
						((SpinnerGenericItem)spCity.getSelectedItem()).getDesc());
				}
				else {
					dba2.nextInsert(SPPA_ID, customerno.getText().toString(), 
							customername.getText().toString(),  
							nf.parse(premi.getText().toString()).doubleValue(),
							nf.parse(materai.getText().toString()).doubleValue(),
							nf.parse(polis.getText().toString()).doubleValue(),
							nf.parse(total.getText().toString()).doubleValue(),
							etFromDate.getText().toString(), 
							etToDate.getText().toString(),
							Utility.DateToString(Utility.GetToday(), "dd/MM/yyyy"),
							nf.parse((discpct.getText().toString())).doubleValue(),
							nf.parse((disc.getText().toString())).doubleValue());
					
					dba.nextInsert(SPPA_ID, kodepos.getText().toString().toUpperCase(), scenter,
							owner, ((SpinnerGenericItem)spJenisUsaha.getSelectedItem()).getCode(), franch,
							alamat_sama, 
							Integer.parseInt(luas.getText().toString()), 
							nf.parse(IDRluas.getText().toString()).doubleValue(),
							nf.parse(IDRperabotan.getText().toString()).doubleValue(),
							nf.parse(IDRtotal.getText().toString()).doubleValue() ,
							txtAlamatLain.getText().toString(), "","", "", "", 
							((SpinnerGenericItem)spCity.getSelectedItem()).getCode(), 
							txtKodePosLain.getText().toString(),"0", "0", "0", "0", "0", 
							Integer.parseInt(umurBangunan.getText().toString()), etFromDate.getText().toString(), etToDate.getText().toString(),
							nf.parse(rate.getText().toString()).doubleValue(),
							nf.parse(this.premi.getText().toString()).doubleValue(),
							nf.parse(this.materai.getText().toString()).doubleValue(),
							nf.parse(this.polis.getText().toString()).doubleValue(),
							nf.parse(this.total.getText().toString()).doubleValue(),
							"TOKO", customername.getText().toString(), 
							((SpinnerGenericItem)spJenisUsaha.getSelectedItem()).getDesc(),
							nf.parse(IDRisibarang.getText().toString()).doubleValue(), 
							fsprinkler,
							((SpinnerGenericItem)spCity.getSelectedItem()).getDesc());
				}
	
				Intent i = new Intent(getBaseContext(),  ConfirmActivity.class);
				b.putLong("SPPA_ID",  SPPA_ID);
				i.putExtras(b);
				startActivity(i);
				this.finish();
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			if (dba != null)
				dba.close();
			
			if (dba2 != null)
				dba2.close();
		}
	}
	
	private double getIsiBarang () throws ParseException {
		double perlengkapan ;
		
		if(IDRisibarang.getText().toString().isEmpty())
			perlengkapan = 0;
		else 
			perlengkapan =  nf.parse(IDRisibarang.getText().toString()).doubleValue();
		
		return perlengkapan;
	}
	
	private double getIsiLuasBangunan () throws ParseException {
		double perlengkapan ;
		
		if(IDRluas.getText().toString().isEmpty())
			perlengkapan = 0;
		else 
			perlengkapan =  nf.parse(IDRluas.getText().toString()).doubleValue();
		
		return perlengkapan;
	}
	
	private double getIsiPerabotan () throws ParseException {
		double perlengkapan ;
		
		if(IDRperabotan.getText().toString().isEmpty())
			perlengkapan = 0;
		else 
			perlengkapan =  nf.parse(IDRperabotan.getText().toString()).doubleValue();
		
		return perlengkapan;
	}
	
	private double getTSI () throws ParseException {
		double TSI =  getIsiBarang() + getIsiLuasBangunan() + getIsiPerabotan();
		return TSI;
	}
	
	private double getRate (){
		DBA_MASTER_TOKO_RATE dba = null;
		Cursor c = null;
		double v = 0.0;
		try {
			dba = new DBA_MASTER_TOKO_RATE(getBaseContext());
			dba.open();
			
			c = dba.getRate(((SpinnerGenericItem)spJenisUsaha.getSelectedItem()).getCode());
			
			if (c.moveToFirst())
				v = c.getDouble(2);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		finally {
			if (c != null)
				c.close();
			
			if (dba != null)
				dba.close();
		}
		return v;
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		
		c = Calendar.getInstance();
		
		int Y = c.get(Calendar.YEAR);
		int M = c.get(Calendar.MONTH);
		int D =  c.get(Calendar.DAY_OF_MONTH);
	
		switch (id) {
			case DATE_FROM_ID:
				return new DatePickerDialog(this, datePickerListener, Y, M, D + 1);
			case DATE_TO_ID:
				return new DatePickerDialog(this, datePickerListenerNext, Y + 1, M, D);
		}
		return null;
	}
	
	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
			String theDate = Utility.setUIDate(selectedYear, selectedMonth, selectedDay);
			etFromDate.setText(theDate);
			etToDate.setText(Utility.GetTodayNextYearString(theDate));
			validasiTanggal();
		}
	};
	
	private DatePickerDialog.OnDateSetListener datePickerListenerNext = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
			etToDate.setText(Utility.setUIDate(selectedYear, selectedMonth, selectedDay));
		}
	};
	
	private void validasiTanggal () {
		((Button)findViewById(R.id.btnNext)).setEnabled(true);
		etFromDate.setTextColor(Color.BLACK);
		boolean flag = Utility.validasiEffDate(etFromDate.getText().toString(), 
							Utility.GetTomorrowString(Utility.GetTodayString()), getBaseContext());
		
		if (!flag) {
			etFromDate.setTextColor(Color.RED);
			Utility.showCustomDialogInformation(FillTokoActivity.this, "Warning", "Tanggal mulai minimal besok hari");
			((Button)findViewById(R.id.btnNext)).setEnabled(false);
		}
	}
	
	
	public class DeleteSPPA extends AsyncTask<Void, Void, Void> 
    {
		private ProgressDialog progDialog = null;
		private boolean error = false;
		private String flag = "";
		
		protected void onPreExecute()  {
            progDialog = ProgressDialog.show(context, "", "Processing...",false);
        }
		
		 @Override
		 protected Void doInBackground(Void... params) {
			 
			 
				DBA_PRODUCT_MAIN dba = null;
				DBA_PRODUCT_TOKO dba2 = null;
				
				Cursor c = null;
				String E_SPPA_NO = "";
				
				HttpTransportSE androidHttpTransport = null;
				String NAMESPACE = "http://tempuri.org/";  
				    
				SoapObject request = null;
			    SoapSerializationEnvelope envelope = null;
			    String URL = Utility.getURL();  
			    String SOAP_ACTION = "http://tempuri.org/DeleteSPPA";     
			    String METHOD_NAME = "DeleteSPPA";
			    
			   
			    
				try 
				{
				    dba = new DBA_PRODUCT_MAIN(context);
					dba.open();
					
					dba2 = new DBA_PRODUCT_TOKO(context);
					dba2.open();

					//dapatin no SPPA
					c = dba.getRow(SPPA_ID);
					c.moveToNext();
					E_SPPA_NO = c.getString(5);
			            
					Log.d("-->", E_SPPA_NO);
					
					if (E_SPPA_NO == null || E_SPPA_NO.length() == 0 || !TextUtils.isDigitsOnly(E_SPPA_NO))
					{
						Log.d("-->", "EMPTY");
						
						//hapus data di local db
						dba.delete(SPPA_ID);
						dba2.delete(SPPA_ID);
						
						//hapus directory
						Utility.DeleteDirectory(SPPA_ID);
					}
					else
					{
						//coba hapus SPPA di server
						envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); 
						envelope.implicitTypes = true;
						envelope.dotNet = true;	//used only if we use the webservice from a dot net file (asmx)
			    		androidHttpTransport = new HttpTransportSE(URL);
			    		request = new SoapObject(NAMESPACE, METHOD_NAME);
			    	
			    		request.addProperty(Utility.GetPropertyInfo("SPPANo", E_SPPA_NO, String.class));
	    				
			    		envelope.setOutputSoapObject(request);
	    	    		androidHttpTransport.call(SOAP_ACTION, envelope);  
	    	    		
	    	    		SoapObject result = (SoapObject) envelope.bodyIn;					
						String response = "";
		
						if (result.getPropertyCount() == 0) {
							flag = "1";
						}
						else
						{
							response = result.getPropertySafelyAsString("DeleteSPPAResult");						
							flag = response.toString();
						}


						if (flag.equals("1")) {
							// hapus data di local db
							dba.delete(SPPA_ID);
							dba2.delete(SPPA_ID);

							// hapus directory
							Utility.DeleteDirectory(SPPA_ID);
						}
					}
             	
				}
				catch (Exception e) {
	        		error = true;
	        		e.printStackTrace();	        		
//					errorMessage = new MasterExceptionClass(e).getException();
				}
				finally {
					
					if (c != null)
						c.close();
					
					if (dba != null)
						dba.close();
					
					if (dba2 != null)
						dba2.close();
				}
				return null;
		 }
		 

		 protected void onPostExecute(Void result) 
		 {
			 try {
				 progDialog.dismiss();
				 progDialog = null;
				 
				 if(error)
					Toast.makeText(getBaseContext(), "Data gagal dihapus", Toast.LENGTH_SHORT).show();
				 else{	
					Toast.makeText(getBaseContext(), "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
					 
					 Intent i = null;
					
					i = new Intent(getBaseContext(),  SyncActivity.class);
					
					startActivity(i);
				}
			 }	
			 catch(Exception ex) {
				 //
			 }
		 }
    }
}
