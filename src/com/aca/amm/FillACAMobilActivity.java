package com.aca.amm;

import java.io.File;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.aca.database.DBA_MASTER_ACA_MOBIL_RATE;
import com.aca.database.DBA_PRODUCT_ACA_MOBIL;
import com.aca.database.DBA_PRODUCT_MAIN;


import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

@SuppressLint("DefaultLocale")
public class FillACAMobilActivity extends ControlNormalActivity implements InterfaceCustomer {

	ArrayList<HashMap<String, String>> custList ;
	
	private Context context = null;
	
	private Bundle b;
	private NumberFormat nf;
	private String PRODUCT_ACTION;
	private long SPPA_ID;
	private int flag = 0;
	private Calendar c;
    
    private int maxTahun = 5;
    
	private double v_rate = 0,  v_tsi = 0, v_premi = 0, v_polis = 0, v_materai = 0, v_total = 0, v_discpct = 0, v_disc = 0;
    
    private int flagLoad = 0;
    private String carType = "";
    
	private Spinner spMake, spType;
	private static final int DATE_FROM_ID = 99;
	private static final int DATE_TO_ID = 100;
	
	private EditText nopol1, nopol2, nopol3, model;
	private EditText tahun, chassisNo, machineNo, color, perlengkapan, seat;
	private EditText jangkaWaktuEff, jangkaWaktuExp, nilaiPertanggungan, nilaiPerlengkapan, tjh;
	private EditText rate, premi, polis, materai, total, customerno, customername, discpct, disc;
	private Switch AOG,  AccType;

	private String UIErrorMessage = "Lengkapi semua data";
	private double MAX_DISCOUNT = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_fill_acamobil);
		
		context = FillACAMobilActivity.this;
		 
		nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		
		spMake = (Spinner)findViewById(R.id.spinnerMake);
		Utility.BindMerk(spMake, getBaseContext(), this);
		
		spMake.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				Utility.BindType(spType, getBaseContext(), ((SpinnerGenericAdapter)spMake.getAdapter()).getActivity(), ((SpinnerGenericItem)spMake.getSelectedItem()).getCode());
		
				if (flagLoad == 1) {
					SpinnerGenericAdapter a = (SpinnerGenericAdapter)spType.getAdapter();
					spType.setSelection(a.getItemId(carType));
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		
		spType = (Spinner)findViewById(R.id.spinnerType);
		Utility.BindType(spType, getBaseContext(), this, ((SpinnerGenericItem)spMake.getSelectedItem()).getCode());
		
		InitEditText();
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
			
			MAX_DISCOUNT = Utility.getMaxDiscount(getBaseContext(), "12");
			
			if (Utility.getIsDiscountable(getBaseContext(), "12").equals("0"))
			{
				disc.setEnabled(false);
				discpct.setEnabled(false);
			}
		}
		catch(Exception e){	
			e.printStackTrace();
		}
	}

	
	private void calculateAll() throws ParseException, InterruptedException {
		v_tsi = getTSI();
		v_rate = getRate();
		v_premi = getPremi();
		v_discpct = Utility.parseDouble(discpct);
		v_disc = Utility.countDiscount(v_premi, v_discpct);
		v_polis = 0;
		v_materai = Utility.countMaterai(v_premi);
		v_total = Utility.countTotal(v_premi, v_polis, v_materai, v_disc);
	
		rate.setText(nf.format(v_rate));
		premi.setText(nf.format(v_premi));
		disc.setText(nf.format(v_disc));
		polis.setText(nf.format(v_polis));
		materai.setText(nf.format(v_materai));
		total.setText(nf.format(v_total));
	}
	
	private void RegisterListener(){

		nilaiPertanggungan.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
	
				if(hasFocus)
					nilaiPertanggungan.setText(Utility.removeComma(nilaiPertanggungan.getText().toString()));
				else {
					try {
						if (nilaiPertanggungan.getText().toString().isEmpty())
							return ;
						
						nilaiPertanggungan.setText(nf.format(nf.parse(nilaiPertanggungan.getText().toString())));
						
						calculateAll();
						
					} catch (ParseException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}
		});
		
		nilaiPerlengkapan.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				if(hasFocus)
					nilaiPerlengkapan.setText(Utility.removeComma(nilaiPerlengkapan.getText().toString()));
				else {
					try {
						if (nilaiPerlengkapan.getText().toString().isEmpty())
							return ;
						
						nilaiPerlengkapan.setText(nf.format(nf.parse(nilaiPerlengkapan.getText().toString())));
						
						calculateAll();
						
					} catch (ParseException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}
		});
		
		tjh.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				
				if(hasFocus)
					tjh.setText(Utility.removeComma(tjh.getText().toString()));
				else {
					try {
						if (tjh.getText().toString().isEmpty())
							return ;
						
						tjh.setText(nf.format(nf.parse(tjh.getText().toString())));
						
						calculateAll();
						
					} catch (ParseException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
	   tahun.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				
				if(hasFocus)
					tahun.setTextColor(Color.BLACK);
				else
					validasiUmur();
			}			
		});
		
		jangkaWaktuEff.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View arg0, boolean hasFocus) {
				if(hasFocus)
					showDialog(DATE_FROM_ID);
				else { 
				}
			}
		});
		jangkaWaktuEff.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showDialog(DATE_FROM_ID);
			}
		});
		
		AOG.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				try {
					calculateAll();
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
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
	
	private void validasiTanggal () {
		((Button)findViewById(R.id.btnNext)).setEnabled(true);
		jangkaWaktuEff.setTextColor(Color.BLACK);
		boolean flag = Utility.validasiEffDate(jangkaWaktuEff.getText().toString(), 
				Utility.GetTodayString(), getBaseContext());
		
		if (!flag) {
			jangkaWaktuEff.setTextColor(Color.RED);
			Toast.makeText(getBaseContext(), "Tanggal minimal hari ini", Toast.LENGTH_SHORT).show();
			((Button)findViewById(R.id.btnNext)).setEnabled(false);
		}
	}
	
	private void validasiUmur() {
		Calendar c = Calendar.getInstance();
		int todayYear = c.get(Calendar.YEAR);
		
		if (tahun.getText().toString().isEmpty())
			return ;
		
		int vtahun = Integer.parseInt(tahun.getText().toString());
		
		if (vtahun+ maxTahun < todayYear){
			tahun.setTextColor(Color.RED);
			flag ++ ;
			Toast.makeText(getBaseContext(), "Umur kendaran maksimal 5 tahun (7 tahun Renewal)", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void InitEditText(){
		
		customerno = (EditText)findViewById(R.id.txtCustomerNo);
		customername = (EditText)findViewById(R.id.txtCustomerName);
		
		model = (EditText)findViewById(R.id.txtModel);
		tahun = (EditText)findViewById(R.id.txtYear);
		
		nopol1 = (EditText)findViewById(R.id.txtNopol1);
		nopol2 = (EditText)findViewById(R.id.txtNopol2);
		nopol3 = (EditText)findViewById(R.id.txtNopol3);
		
		chassisNo = (EditText)findViewById(R.id.txtChassisNo);
		machineNo = (EditText)findViewById(R.id.txtMachineNo);
		color = (EditText)findViewById(R.id.txtColor);
		perlengkapan = (EditText)findViewById(R.id.txtPerlengkapan);
		seat = (EditText)findViewById(R.id.txtSeat);
		
		AOG = (Switch)findViewById(R.id.swiAOG);
		AccType = (Switch)findViewById(R.id.swiAccType);
		
		jangkaWaktuEff = (EditText)findViewById(R.id.txtEffDate);
		jangkaWaktuExp = (EditText)findViewById(R.id.txtExpDate);
		jangkaWaktuExp.setKeyListener(null);
		
		nilaiPertanggungan = (EditText)findViewById(R.id.txtNilaiPertanggungan);
		nilaiPerlengkapan = (EditText)findViewById(R.id.txtNilaiPerlengkapan);
		tjh = (EditText)findViewById(R.id.txtTJH);
		tjh.setText("20,000,000.00");
		
		rate = (EditText)findViewById(R.id.txtRate);
		premi = (EditText)findViewById(R.id.txtPremi);
		discpct = (EditText)findViewById(R.id.txtDiscountPct);
		disc = (EditText)findViewById(R.id.txtDiscount);
		materai = (EditText)findViewById(R.id.txtMaterai);
		polis = (EditText)findViewById(R.id.txtPolicyCost);
		total = (EditText)findViewById(R.id.txtTotal);
	}
	
	private void LoadDB(){

		DBA_PRODUCT_MAIN dbm = null;
		DBA_PRODUCT_ACA_MOBIL dba = null;
		
		Cursor cm = null;
		Cursor c = null;
		
		try{
			
			dbm = new DBA_PRODUCT_MAIN(getBaseContext());
			dbm.open();
			cm = dbm.getRow(SPPA_ID);
			cm.moveToFirst();
			
			dba = new DBA_PRODUCT_ACA_MOBIL(getBaseContext());
			dba.open();
			c = dba.getRow(SPPA_ID);
			c.moveToFirst();
			
			customerno.setText(cm.getString(1));
			customername.setText(cm.getString(2));
			

			carType = c.getString(3);
			flagLoad = 1;
			
			SpinnerGenericAdapter a = (SpinnerGenericAdapter)spMake.getAdapter();
			spMake.setSelection(a.getItemId(c.getString(2)));
		
			tahun.setText(c.getString(4));
			nopol1.setText(c.getString(5));
			nopol2.setText(c.getString(6));
			nopol3.setText(c.getString(7));
			
			color.setText(c.getString(8));
			chassisNo.setText(c.getString(9));
			machineNo.setText(c.getString(10));
			perlengkapan.setText(c.getString(11));
			
			seat.setText(String.valueOf(c.getInt(12)));
			jangkaWaktuEff.setText(String.valueOf(c.getString(13)));
			jangkaWaktuExp.setText(String.valueOf(c.getString(14)));
			nilaiPertanggungan.setText(nf.format(c.getDouble(15)));
			nilaiPerlengkapan.setText(nf.format(c.getDouble(16)));
			
			if(c.getString(17).equals("1"))
				AOG.setChecked(true);
			else
				AOG.setChecked(false);
			
			tjh.setText(nf.format(c.getDouble(18)));
			
			rate.setText(nf.format(c.getDouble(19)));
			premi.setText(nf.format(c.getDouble(20)));
			discpct.setText(nf.format(cm.getDouble(23)));
			disc.setText(nf.format(cm.getDouble(24)));
			polis.setText(nf.format(c.getDouble(21)));
			materai.setText(nf.format(c.getDouble(22)));
			total.setText(nf.format(c.getDouble(23)));
			
			model.setText(c.getString(26));
			
			if(c.getString(27).equals("1"))
				AccType.setChecked(true);
			else
				AccType.setChecked(false);
			
			if (cm.getString(20).equals("R")){
				model.setEnabled(false);
				spMake.setEnabled(false);
				spType.setEnabled(false);
				tahun.setEnabled(false);
				nopol1.setEnabled(false);
				nopol2.setEnabled(false);
				nopol3.setEnabled(false);
				color.setEnabled(false);
				chassisNo.setEnabled(false);
				machineNo.setEnabled(false);
				seat.setEnabled(false);
				jangkaWaktuEff.setEnabled(false);
				jangkaWaktuExp.setEnabled(false);

				maxTahun = 7;
				customerno.setEnabled(false);
				((Button)findViewById(R.id.btnChooseCustomer)).setEnabled(false);
			}
			
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
		getMenuInflater().inflate(R.menu.fill_acamobil, menu);
		return true;
	}
	
	public void btnHomeClick(View v){
		Intent i = new Intent(getBaseContext(),  FirstActivity.class);
		startActivity(i);
		this.finish();
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
	        
    
    
    
	private void Back(){
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
	
	public void btnBackClick(View v) {
		Back();
	}
	
	@Override
	public void onBackPressed() {
		Back();
	}
	
	private void validasiNext () throws ParseException{
		flag = 0;
		if	(customername.getText().toString().isEmpty()){
			flag++ ;
			customername.setHintTextColor(Color.RED);
			UIErrorMessage = "Pilih customer";
		}
		if	(customerno.getText().toString().isEmpty()){
			flag++ ;
			customerno.setHintTextColor(Color.RED);
			UIErrorMessage = "Pilih customer";
		}
		if (nopol1.getText().toString().isEmpty() ){
			flag++ ;
			nopol1.setHintTextColor(Color.RED);
			UIErrorMessage = "No Polis ke-1 harus diisi";
		}
		if (nopol2.getText().toString().isEmpty()){
			flag++ ;
			nopol2.setHintTextColor(Color.RED);
			UIErrorMessage = "No Polis ke-2 harus diisi";
		}
		
		/*
		if (nopol3.getText().toString().isEmpty() ){
			flag++ ;
			nopol3.setHintTextColor(Color.RED);
			UIErrorMessage = "No Polis ke-3 harus diisi";
		}
		*/
		
		if (model.getText().toString().isEmpty() ){
			flag++ ;
			model.setHintTextColor(Color.RED);
			UIErrorMessage = "Model kendaraan harus diisi";
		}
		if	(tahun.getText().toString().isEmpty() ){
			flag++ ;
			tahun.setHintTextColor(Color.RED);
			UIErrorMessage = "Tahun kendaraan harus diisi";
		}
		if	(perlengkapan.getText().toString().isEmpty()){
			flag++ ;
			perlengkapan.setHintTextColor(Color.RED);
			UIErrorMessage = "Perlengkapan harus diisi";
		}
		if	(chassisNo.getText().toString().isEmpty() ){
			flag++ ;
			chassisNo.setHintTextColor(Color.RED);
			UIErrorMessage = "No Rangka harus diisi";
		}
		if	(machineNo.getText().toString().isEmpty()){
			flag++ ;
			machineNo.setHintTextColor(Color.RED);
			UIErrorMessage = "No Mesin harus diisi";
		}
		if	(color.getText().toString().isEmpty() ){
			flag++ ;
			color.setHintTextColor(Color.RED);
			UIErrorMessage = "Warna harus diisi";
		}
		if	(jangkaWaktuEff.getText().toString().isEmpty() ){
			flag++ ;
			jangkaWaktuEff.setHintTextColor(Color.RED);
			UIErrorMessage = "Tanggal mulai harus diisi";
		}
		if	(jangkaWaktuExp.getText().toString().isEmpty()){
			flag++ ;
			jangkaWaktuExp.setHintTextColor(Color.RED);
			UIErrorMessage = "Tanggal berakhir harus diisi";
		}
		if	(rate.getText().toString().isEmpty()){
			flag++ ;
			rate.setHintTextColor(Color.RED);
			UIErrorMessage = "Rate tidak bisa dihitung. Hubungi IT ACA";
		}
		if	(premi.getText().toString().isEmpty()){
			flag++ ;
			premi.setHintTextColor(Color.RED);
			UIErrorMessage = "Premi tidak bisa dihitung. Hubungi IT ACA";
		}
		if	(discpct.getText().toString().isEmpty()){
			flag++ ;
			discpct.setHintTextColor(Color.RED);
		}
		if	(disc.getText().toString().isEmpty()){
			flag++ ;
			disc.setHintTextColor(Color.RED);
		}
		if	(nilaiPerlengkapan.getText().toString().isEmpty() ){
			flag++ ;
			nilaiPerlengkapan.setHintTextColor(Color.RED);
			UIErrorMessage = "Nilai Perlengkapan harus diisi, jika tidak ada masukkan angka 0";
		}
		if	(nilaiPertanggungan.getText().toString().isEmpty() ){
			flag++ ;
			nilaiPertanggungan.setHintTextColor(Color.RED);
			UIErrorMessage = "Nilai Pertanggungan harus diisi, jika tidak ada masukkan angka 0";
		}
		if	(tjh.getText().toString().isEmpty()){
			flag++ ;
			tjh.setHintTextColor(Color.RED);
			UIErrorMessage = "Nilai TJH harus diisi, jika tidak ada masukkan angka 0";
		}
		if	(getTJH() < 20000000.00){
			flag++;
			tjh.setTextColor(Color.RED);
			UIErrorMessage = "Nilai TJH minimum Rp. 20,000,000";
		}
		if (getTJH() > 70000000.00){
			flag++;
			tjh.setTextColor(Color.RED);
			UIErrorMessage = "Nilai TJH maksimum Rp. 70,000,000";
		}
		if (getNilaiPertanggungan() < 101000000.00) {
			flag++;
			nilaiPertanggungan.setTextColor(Color.RED);
			UIErrorMessage = "Nilai pertanggungan minimum Rp. 101,000,000";
		}
		if (getNilaiPertanggungan() > 1000000000.00) {
			flag++;
			nilaiPertanggungan.setTextColor(Color.RED);
			UIErrorMessage = "Nilai pertanggungan maksimum Rp. 1,000,000,000";
		}
		if (getNilaiPerlengkapan() > (0.1*getNilaiPertanggungan()) ){
			flag ++ ;
			nilaiPerlengkapan.setTextColor(Color.RED);
			UIErrorMessage = "Nilai perlengkapan maksimum 10% dari Nilai pertanggungan";
		}
		if (diffYear() > 7){
			flag++;
			tahun.setTextColor(Color.RED);
			UIErrorMessage = "Umur Kendaraan maksimal 7 tahun";
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
			seat.setText("1");
			
			validasiNext();
			validasiUmur();
			
			if(flag != 0)
				Toast.makeText(getBaseContext(), UIErrorMessage, Toast.LENGTH_SHORT).show();
			else{
				calculateAll();
				insertDB();
			}		
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private void getCustomerData () {
		RetrieveCustomer retrieveCustomer = new RetrieveCustomer(FillACAMobilActivity.this, customerno.getText().toString());
		retrieveCustomer.execute();
		retrieveCustomer.customerInterface = this;
		
	} 
	
	@Override
	public void getCustomerList(ArrayList<HashMap<String, String>> custList) {
		if (custList != null) {
			this.custList = custList;
		}
		initDiscount();
	}
	

	private void initDiscount () {
		MAX_DISCOUNT = Utility.getMaxDiscount(getBaseContext(), "31");
		
		if (Utility.getIsDiscountable(getBaseContext(), "16").equals("0"))
		{
			disc.setEnabled(false);
			discpct.setEnabled(false);
		}
	}
	
	
	
	public void btnChooseCustomerClick(View v) {
		
		if (customerno.getText().length() == 0) {
			Intent i = new Intent(getBaseContext(),  ChooseCustomerForBuyActivity.class);
			i.putExtras(b);
			startActivity(i);
			this.finish();
		}
//		else {
//			try {
//				getCustomerData();
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
	
	public void showDisc(View v) {
		TableRow trDiscPct = (TableRow)findViewById(R.id.trDiscPct);
		TableRow trDisc = (TableRow)findViewById(R.id.trDisc);
		trDiscPct.setVisibility(View.VISIBLE);
		trDisc.setVisibility(View.VISIBLE);
	}
	
	private double getTPL (double tjh) {
		double tpl = 0.0;
		
		if(tjh == 0)
			tpl = 0.0;
		else if (tjh <= 5000000)
			tpl = 50000;
		else if (tjh <= 10000000)
			tpl = 85000;
		else if (tjh <= 20000000)
			tpl = 127500;
		else if (tjh <= 30000000)
			tpl = 170000;
		else if (tjh <= 40000000)
			tpl = 212500;
		else if (tjh <= 50000000)
			tpl = 255000;
		
		return tpl;
	}
	
	private double getPremi () throws ParseException{
		double TSI = getTSI();
		double nilaitjh =  getTJH();
		
		if(AOG.isChecked())
			v_rate = v_rate + 0.45;
		
		double TPL = getTPL(nilaitjh - 20000000);
		double nilaiPremi = (TSI * v_rate / 100 ) + TPL ;
		
		return nilaiPremi;
	}
	
	private double getTJH () throws ParseException{
		double Tjh ;
		
		if(tjh.getText().toString().isEmpty())
			Tjh = 0;
		else 
			Tjh = nf.parse(tjh.getText().toString()).doubleValue();
		
		return Tjh;
	}
	
	private double getNilaiPertanggungan () throws ParseException {
		double pertanggungan ;
		
		if(nilaiPertanggungan.getText().toString().isEmpty())
			pertanggungan = 0;
		else 
			pertanggungan = nf.parse(nilaiPertanggungan.getText().toString()).doubleValue();

		return pertanggungan;
	}
	
	private double getNilaiPerlengkapan () throws ParseException{
		double perlengkapan ;
		
		if(nilaiPerlengkapan.getText().toString().isEmpty())
			perlengkapan = 0;
		else 
			perlengkapan =  nf.parse(nilaiPerlengkapan.getText().toString()).doubleValue();
	
		return perlengkapan;
	}
	
	private double getTSI() throws ParseException {
		double pertanggungan =  getNilaiPertanggungan();
		double perlengkapan = getNilaiPerlengkapan();
		double TSI = pertanggungan + perlengkapan;
	
		return TSI;
	}
	
	private double getLoadingRate (){
	
		double rate = 0.0;
		
		if (diffYear() == 6)
			rate += 0.15;
		
		if (diffYear() == 7)
			rate += 0.30;
			
		return rate;
	}
	
	private int diffYear(){
		Calendar c = Calendar.getInstance();
		
		int tahunKend = Integer.parseInt(tahun.getText().toString());
		int tahunToday = c.get(Calendar.YEAR);
		
		return tahunToday - tahunKend;
	}
	
	private double getRate () throws InterruptedException, ParseException{
		DBA_MASTER_ACA_MOBIL_RATE dba = null;
		Cursor c = null;
		double v = 0.0;
		
		try {
			dba = new DBA_MASTER_ACA_MOBIL_RATE(getBaseContext());
			dba.open();
			
			c = dba.getRate(String.valueOf(nf.format(v_tsi)));
			if (c.moveToFirst())
				v = c.getDouble(2) + getLoadingRate();
		}
		catch(Exception e) {
			e.printStackTrace();
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK)
			Back();
		
		return super.onKeyDown(keyCode, event);
	}
	 
	@Override
	protected Dialog onCreateDialog(int id) {
		
		c = Calendar.getInstance();
		
		int Y = c.get(Calendar.YEAR);
		int M = c.get(Calendar.MONTH);
		int D =  c.get(Calendar.DAY_OF_MONTH);
		
		switch (id) {
			case DATE_FROM_ID:
				return new DatePickerDialog(this, datePickerListener, Y, M, D);
			case DATE_TO_ID:
				return new DatePickerDialog(this, datePickerListenerNext, Y + 1, M, D);
		}
		return null;
	}
	
	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
			jangkaWaktuEff.setText(Utility.setUIDate(selectedYear, selectedMonth, selectedDay));
			jangkaWaktuExp.setText(
					Utility.getYesterdayString(
					Utility.setUIDate(selectedYear + 1, selectedMonth, selectedDay).toString()));
			
			validasiTanggal();
		}
	};
	
	private DatePickerDialog.OnDateSetListener datePickerListenerNext  = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
			jangkaWaktuExp.setText(Utility.setUIDate(selectedYear, selectedMonth, selectedDay));
		}
	};
	
	private void insertDB() throws ParseException{
		
		DBA_PRODUCT_ACA_MOBIL dba = null;
		DBA_PRODUCT_MAIN dba2 = null;

		try {
			
			Log.d("dd", PRODUCT_ACTION);
			
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
				
				dba = new DBA_PRODUCT_ACA_MOBIL(getBaseContext());
				dba2 = new DBA_PRODUCT_MAIN(getBaseContext());
				
				dba.open();
				dba2.open();
				
				String aog; 
				String acctype;
		
				if (AOG.isChecked()) aog = "1"; else aog = "0";
				
				if(AccType.isChecked()) acctype = "1"; else acctype = "0";
				
				if (PRODUCT_ACTION.equals("NEW")) {
					SPPA_ID = dba2.initialInsert(customerno.getText().toString(), 
							customername.getText().toString(), "ACAMOBIL",
								nf.parse(premi.getText().toString()).doubleValue(),
								nf.parse(materai.getText().toString()).doubleValue(),
								nf.parse(polis.getText().toString()).doubleValue(),
								nf.parse(total.getText().toString()).doubleValue(),		
								jangkaWaktuEff.getText().toString(), jangkaWaktuExp.getText().toString(),
								Utility.DateToString(Utility.GetToday(), "dd/MM/yyyy"),
								"","","","N","",
								nf.parse((discpct.getText().toString())).doubleValue(),
								nf.parse((disc.getText().toString())).doubleValue());
					
					 dba.initialInsert(SPPA_ID, 
							((SpinnerGenericItem)spMake.getSelectedItem()).getCode(),
							((SpinnerGenericItem)spType.getSelectedItem()).getCode(),
							model.getText().toString(),
							tahun.getText().toString(), 
							nopol1.getText().toString().toUpperCase(), 
							nopol2.getText().toString(), 
							nopol3.getText().toString().toUpperCase(), 
							color.getText().toString().toUpperCase(), 
							chassisNo.getText().toString().toUpperCase(), 
							machineNo.getText().toString().toUpperCase(), 
							acctype,
							perlengkapan.getText().toString().toUpperCase(), 
							1, 
							jangkaWaktuEff.getText().toString(), jangkaWaktuExp.getText().toString(), 
							nf.parse(nilaiPertanggungan.getText().toString()).doubleValue(),
							nf.parse(nilaiPerlengkapan.getText().toString()).doubleValue(),
							aog,
							nf.parse(tjh.getText().toString()).doubleValue(),
							nf.parse(rate.getText().toString()).doubleValue(),
							nf.parse(premi.getText().toString()).doubleValue(),
							nf.parse(materai.getText().toString()).doubleValue(),
							nf.parse(polis.getText().toString()).doubleValue(),
							nf.parse(total.getText().toString()).doubleValue(), 
							((SpinnerGenericItem)spMake.getSelectedItem()).getDesc(),
							((SpinnerGenericItem)spType.getSelectedItem()).getDesc(),
							"ACAMOBIL", customername.getText().toString());	
					 
				}
				else {
					dba2.nextInsert(SPPA_ID, customerno.getText().toString(), 
							customername.getText().toString(), 
							nf.parse(premi.getText().toString()).doubleValue(),
							nf.parse(materai.getText().toString()).doubleValue(),
							nf.parse(polis.getText().toString()).doubleValue(),
							nf.parse(total.getText().toString()).doubleValue(),
							jangkaWaktuEff.getText().toString(), jangkaWaktuExp.getText().toString(),
							Utility.DateToString(Utility.GetToday(), "dd/MM/yyyy"),
							nf.parse((discpct.getText().toString())).doubleValue(),
							nf.parse((disc.getText().toString())).doubleValue());
					
					dba.nextInsert(SPPA_ID,
							((SpinnerGenericItem)spMake.getSelectedItem()).getCode(),
							((SpinnerGenericItem)spType.getSelectedItem()).getCode(),
							model.getText().toString(),
							tahun.getText().toString(), 
							nopol1.getText().toString().toUpperCase(), 
							nopol2.getText().toString(), 
							nopol3.getText().toString().toUpperCase(), 
							color.getText().toString().toUpperCase(), 
							chassisNo.getText().toString().toUpperCase(), 
							machineNo.getText().toString().toUpperCase(), 
							acctype,
							perlengkapan.getText().toString().toUpperCase(), 
							1, 
							jangkaWaktuEff.getText().toString(), jangkaWaktuExp.getText().toString(), 
							nf.parse(nilaiPertanggungan.getText().toString()).doubleValue(),
							nf.parse(nilaiPerlengkapan.getText().toString()).doubleValue(),
							aog,
							nf.parse(tjh.getText().toString()).doubleValue(),
							nf.parse(rate.getText().toString()).doubleValue(),
							nf.parse(premi.getText().toString()).doubleValue(),
							nf.parse(materai.getText().toString()).doubleValue(),
							nf.parse(polis.getText().toString()).doubleValue(),
							nf.parse(total.getText().toString()).doubleValue(),
							((SpinnerGenericItem)spMake.getSelectedItem()).getDesc(),
							((SpinnerGenericItem)spType.getSelectedItem()).getDesc(),
							"ACAMOBIL", customername.getText().toString());	
				}	
				
				Intent i = new Intent(getBaseContext(),  ConfirmActivity.class);
				b.putLong("SPPA_ID",  SPPA_ID);
				i.putExtras(b);
				startActivity(i);
				FillACAMobilActivity.this.finish();
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
				DBA_PRODUCT_ACA_MOBIL dba2 = null;
				
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
					
					dba2 = new DBA_PRODUCT_ACA_MOBIL(context);
					dba2.open();
					
					//dapatin no SPPA
					c = dba.getRow(SPPA_ID);
					c.moveToNext();
					E_SPPA_NO = c.getString(5);
			            
					if (E_SPPA_NO == null || E_SPPA_NO.length() == 0 || !TextUtils.isDigitsOnly(E_SPPA_NO))
					{
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
	    	    		
	    	    		SoapObject result = (SoapObject)envelope.bodyIn;
	                	String response = result.getProperty(0).toString(); 

	                	flag = response.toString();
	                	
	                	if (flag.equals("0")) 
	                		flag = "0";
	                	else if (flag.equals("1"))
	                	{
	                		//hapus data di local db
							dba.delete(SPPA_ID);
							dba2.delete(SPPA_ID);
							
							
							//hapus directory
							Utility.DeleteDirectory(SPPA_ID);
	                	}
	                	else 
	                		flag = "2";
					}
             	
				}
				catch (Exception e) {
					e.printStackTrace();;
					error = true;
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
					if (flag == "2")
						Toast.makeText(getBaseContext(), "SPPA sudah dibayar dan menjadi POLIS", Toast.LENGTH_SHORT).show();
					else
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
