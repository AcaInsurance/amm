package com.aca.amm;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import org.kobjects.util.Util;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.aca.amm.R;
import com.aca.database.DBA_MASTER_ASRI_RATE;
import com.aca.database.DBA_MASTER_PRODUCT_SETTING;
import com.aca.database.DBA_PRODUCT_ASRI;
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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

@SuppressLint("DefaultLocale")
public class FillAsriActivity extends ControlNormalActivity implements InterfaceCustomer {

	ArrayList<HashMap<String, String>> custList ;

	private double maxTSIasri = 2000000;
	
	private Context context = null;
	
	private Bundle b;
	private NumberFormat nf, nf_asri;
	private String PRODUCT_ACTION;
	private long SPPA_ID;
	private int flag = 0;
	private Calendar c;

    private double v_rate = 0, v_tsi = 0, v_premi = 0, v_discpct = 0, v_disc = 0, v_polis = 0, v_materai = 0, v_total = 0;

	private static final int DATE_FROM_ID = 99;
	private static final int DATE_TO_ID = 100;
	private EditText etFromDate, etToDate, luas, hargabangunan, hargaperabotan, totalharga, alamat, kodepos, premi, polis, materai, total, rate, customerno, customername, discpct, disc;
	private Switch swiAlamat;

	private String  city_name, city_id;
	private EditText cityName;
	
	private String UIErrorMessage = "Lengkapi semua data.";
	private double MAX_DISCOUNT = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_fill_asri);
	
		context = FillAsriActivity.this;
		
		nf = NumberFormat.getInstance();
		
		nf_asri = NumberFormat.getInstance();
		nf_asri.setMaximumFractionDigits(3);
		nf_asri.setMinimumFractionDigits(3);
		
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
			else if (PRODUCT_ACTION.equals("VIEW.UNPAID")) {
				SPPA_ID = b.getLong("SPPA_ID");
				LoadDB();
				disableView();
			}
			
			else {
				findViewById(R.id.btnDelete)
				.setVisibility(View.GONE);
			}
			
			if (b.getString("CUSTOMER_NO") != null) {
				customerno.setText(b.getString("CUSTOMER_NO"));
				customername.setText(b.getString("CUSTOMER_NAME"));
			}
			

			initDiscount();
			
		}
		catch(Exception e){	
			e.printStackTrace();
		}
	}
	
	private void disableView() {
		// TODO Auto-generated method stub
		customerno.setEnabled(false);
		customername.setEnabled(false); 

		etFromDate.setEnabled(false);
		etToDate.setEnabled(false); 
		luas.setEnabled(false); 
		hargabangunan.setEnabled(false); 
		hargaperabotan.setEnabled(false); 
		totalharga.setEnabled(false); 
		alamat.setEnabled(false); 
		cityName.setEnabled(false); 
		kodepos.setEnabled(false); 
		premi.setEnabled(false); 
		polis.setEnabled(false);
		materai.setEnabled(false); 
		total.setEnabled(false); 
		rate.setEnabled(false);
		
		swiAlamat.setEnabled(false);
		
		(findViewById(R.id.btnChooseCustomer)).setEnabled(false);
		
		customerno.setTextColor(Color.BLACK);
		customername.setTextColor(Color.BLACK);

		etFromDate.setTextColor(Color.BLACK);
		etToDate.setTextColor(Color.BLACK);
		luas.setTextColor(Color.BLACK);
		hargabangunan.setTextColor(Color.BLACK);
		hargaperabotan.setTextColor(Color.BLACK);
		totalharga.setTextColor(Color.BLACK);
		alamat.setTextColor(Color.BLACK);
		cityName.setTextColor(Color.BLACK);
		kodepos.setTextColor(Color.BLACK);
		premi.setTextColor(Color.BLACK);
		polis.setTextColor(Color.BLACK);
		materai.setTextColor(Color.BLACK);
		total.setTextColor(Color.BLACK);
		rate.setTextColor(Color.BLACK);
		
		swiAlamat.setTextColor(Color.BLACK);
		
		
	}

	private void initDiscount () {
		MAX_DISCOUNT = Utility.getMaxDiscount(getBaseContext(), "31");
		
		if (Utility.getIsDiscountable(getBaseContext(), "16").equals("0"))
		{
			disc.setEnabled(false);
			discpct.setEnabled(false);
		}
	}
	
	private void getCustomerData () {
		RetrieveCustomer retrieveCustomer = new RetrieveCustomer(FillAsriActivity.this, customerno.getText().toString());
		retrieveCustomer.execute();
		retrieveCustomer.customerInterface = this;
		
	} 
	
	@Override
	public void getCustomerList(ArrayList<HashMap<String, String>> custList) {
		if (custList != null) {
			this.custList = custList;
			alamat.setText(custList.get(0).get("CUSTOMER_ADDRESS").toString());
			kodepos.setText(custList.get(0).get("CUSTOMER_KODE_POS").toString());							
			city_name = custList.get(0).get("CUSTOMER_CITY").toString();
			cityName.setText(city_name);
			city_id = custList.get(0).get("CUSTOMER_CITY_CODE").toString();
			cityName.setTag(city_id);
		}	
		else
			Utility.showCustomDialogInformation(FillAsriActivity.this, "Informasi", "Gagal ambil data alamat nasabah. Mohon alamat diketik saja");
			
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == 100 && requestCode == 200){
			city_name = data.getStringExtra("CITY_NAME");
			city_id = data.getStringExtra("CITY_ID");
			
			if (city_name != null && city_id != null)
			{
				cityName.setText(city_name);
				cityName.setTag(city_id);
				Log.d("id city", cityName.getTag().toString());
			}	
		}	
	}


	private void LoadDB() {

		DBA_PRODUCT_MAIN dbm = null;
		DBA_PRODUCT_ASRI dba = null;
		
		Cursor cm = null;
		Cursor c = null;
		
		try{
			
			dbm = new DBA_PRODUCT_MAIN(getBaseContext());
			dba = new DBA_PRODUCT_ASRI(getBaseContext());
			
			dbm.open();
			cm = dbm.getRow(SPPA_ID);
			cm.moveToFirst();
			
			dba.open();
			c = dba.getRow(SPPA_ID);
			c.moveToFirst();
			
			//
			customerno.setText(cm.getString(1));
			customername.setText(cm.getString(2));
			
			//
			luas.setText(String.valueOf(c.getInt(3)));
			hargabangunan.setText(nf.format(c.getDouble(4)));
			hargaperabotan.setText(nf.format(c.getDouble(5)));
			totalharga.setText(nf.format(c.getDouble(6)));
			
			//
			if (c.getString(2).equals("1")) swiAlamat.setChecked(true); else swiAlamat.setChecked(false);
			alamat.setText(c.getString(7));
			
			cityName.setText(c.getString(8));
			cityName.setTag(c.getString(12));
			
			kodepos.setText(c.getString(13));
			etFromDate.setText(c.getString(19));
			etToDate.setText(c.getString(20));
			
			//
			rate.setText(nf_asri.format(c.getDouble(21)));
			premi.setText(nf.format(c.getDouble(22)));
			discpct.setText(nf.format(cm.getDouble(23)));
			disc.setText(nf.format(cm.getDouble(24)));
			polis.setText(nf.format(c.getDouble(23)));
			materai.setText(nf.format(c.getDouble(24)));
			total.setText(nf.format(c.getDouble(25)));
			
			if(cm.getString(20).equals("R")){
				
				swiAlamat.setEnabled(false);
				alamat.setEnabled(false);
				cityName.setEnabled(false);
				kodepos.setEnabled(false);
				etFromDate.setEnabled(false);
				etToDate.setEnabled(false);
				
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

	private void InitControls() {
		
		customerno = (EditText)findViewById(R.id.txtCustomerNo);
		customername = (EditText)findViewById(R.id.txtCustomerName);

		luas = (EditText)findViewById(R.id.txtLuasBangunan);
		hargabangunan = (EditText)findViewById(R.id.txtHargaBangunan);
		hargaperabotan = (EditText)findViewById(R.id.txtPerabotan);
		totalharga = (EditText)findViewById(R.id.txtTotalNilaiHarta);

		swiAlamat = (Switch)findViewById(R.id.swiAlamatPertanggungan);
		alamat = (EditText)findViewById(R.id.txtalamat);
		kodepos = (EditText)findViewById(R.id.txtKodePos2);

		cityName = (EditText)findViewById(R.id.txtProvinsi);
		
		etFromDate = (EditText)findViewById(R.id.txtfromdate);
		etToDate  = (EditText)findViewById(R.id.txttodate);
		
		premi = (EditText)findViewById(R.id.txtPremi);
		discpct = (EditText)findViewById(R.id.txtDiscountPct);
		disc = (EditText)findViewById(R.id.txtDiscount);
		rate = (EditText)findViewById(R.id.txtRate);
		materai = (EditText)findViewById(R.id.txtMaterai);
		polis = (EditText)findViewById(R.id.txtPolicyCost);
		total = (EditText)findViewById(R.id.txtTotal);
	}

	private void RegisterListener(){
		etToDate.setKeyListener(null);
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
						Utility.showCustomDialogInformation(FillAsriActivity.this, "Warning", "Tanggal mulai minimal besok hari");
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
		
		swiAlamat.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if(swiAlamat.isChecked() && !customerno.getText().toString().isEmpty()){
					getCustomerData();
//					custList = Utility.getCustData(FillAsriActivity.this, customerno.getText().toString());
				}
				else {
					alamat.setText("");
					kodepos.setText("");
					cityName.setText("");
					cityName.setTag("");
				}
			}
		});
		
		luas.addTextChangedListener(new TextWatcher() {
			
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
				luas.setTextColor(Color.BLACK);				
			}
		});
		
		hargabangunan.addTextChangedListener(new TextWatcher() {
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
			
				hargabangunan.setTextColor(Color.BLACK);
				hargaperabotan.setTextColor(Color.BLACK);
				try {
				   if (!s.toString().equals(current)) {
					   hargabangunan.removeTextChangedListener(this);     
		                
		                String replaceable = String.format("[%s,.\\s]", "Rp. ");
		                String cleanString = s.toString().replaceAll(replaceable, "");  
		            		                
		                double parsed;
		                try {
		                    parsed = Double.parseDouble(cleanString);
		                } catch (NumberFormatException e) {
		                    parsed = 0.00;
		                }        
		                String formatted = nf.format(parsed);
		                 
		                current = formatted;
		                hargabangunan.setText(formatted);
		                hargabangunan.setSelection(formatted.length());
		                hargabangunan.addTextChangedListener(this);
		                
		                calculateAll();
		                
		            }                           
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		
		hargaperabotan.addTextChangedListener(new TextWatcher() {
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

				hargaperabotan.setTextColor(Color.BLACK);
				hargabangunan.setTextColor(Color.BLACK);
				try {
				   if (!s.toString().equals(current)) {
					   hargaperabotan.removeTextChangedListener(this);     
		                
		                String replaceable = String.format("[%s,.\\s]", "Rp. ");
		                String cleanString = s.toString().replaceAll(replaceable, "");  
		            		                
		                double parsed;
		                try {
		                    parsed = Double.parseDouble(cleanString);
		                } catch (NumberFormatException e) {
		                    parsed = 0.00;
		                }
		                String formatted = nf.format(parsed);
		                
		                current = formatted;
		                hargaperabotan.setText(formatted);
		                hargaperabotan.setSelection(formatted.length());
		                hargaperabotan.addTextChangedListener(this);
		                

						calculateAll(); 
				   }
				} catch (ParseException e) {
					e.printStackTrace();
				}
	                                       
			}
		});
		 
		totalharga.addTextChangedListener(new TextWatcher() {
			
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

				totalharga.setTextColor(Color.BLACK);
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
						
					} catch (ParseException e) {
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
						
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		cityName.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getBaseContext(), ChooseProvinceActivity.class);
				i.putExtras(b);
				startActivityForResult(i, 200);
			}
		});
		
		cityName.setKeyListener(null);
	}
	
	private void calculateAll() throws ParseException {
		v_tsi = getTSI();
		v_rate = getRate();
		v_premi = Utility.countPremi(v_rate, v_tsi, 100000);
		v_discpct = Utility.parseDouble(discpct);
		v_disc = Utility.countDiscount(v_premi, v_discpct);
		v_polis = 0;
		v_materai = Utility.countMaterai(v_premi);
		v_total = Utility.countTotal(v_premi, v_polis, v_materai, v_disc);
		
		totalharga.setText(nf.format(v_tsi));
		rate.setText(nf_asri.format(v_rate));
	    premi.setText(nf.format(v_premi));
	    disc.setText(nf.format(v_disc));
	    polis.setText(nf.format(v_polis));
		materai.setText(nf.format(v_materai));
		total.setText(nf.format(v_total));
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
		getMenuInflater().inflate(R.menu.fill_asri, menu);
		return true;
	}

	public void btnHomeClick(View v) {
		Intent i = new Intent(getBaseContext(),  FirstActivity.class);
		startActivity(i);
		this.finish();
	}
	
	public void btnBackClick(View v) {
		Back();
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
	
	private void validasiNext () throws ParseException{

		flag = 0;
		
		if( customerno.getText().toString().isEmpty()){
			flag++ ;
			customerno.setHintTextColor(Color.RED);
			UIErrorMessage = "Pilih customer";
			return;
		}
		if( customername.getText().toString().isEmpty()){
			flag++ ;
			customername.setHintTextColor(Color.RED);
			UIErrorMessage = "Pilih customer";
			return;
		}
		if (luas.getText().toString().isEmpty()){
			flag++ ;
			luas.setHintTextColor(Color.RED);
			UIErrorMessage = "Luas bangunan harus diisi";
			return;
		}
		if (Utility.parseDouble(luas) == 0 ){
			flag++ ;
			luas.setTextColor(Color.RED);
			UIErrorMessage = "Luas bangunan harus lebih besar dari 0";
			return;
		}
		
		if (hargabangunan.getText().toString().isEmpty()){
			flag++ ;
			hargabangunan.setHintTextColor(Color.RED);
			UIErrorMessage = "Harga bangunan harus diisi";
			return;
		}
		if (Utility.parseDouble(hargabangunan)== 0 ){
			flag++ ;
			hargabangunan.setTextColor(Color.RED);
			UIErrorMessage = "Harga bangunan harus lebih besar dari 0";
			return;
		}
		if (hargaperabotan.getText().toString().isEmpty() ){
			hargaperabotan.setText("0");
		}
		if (totalharga.getText().toString().isEmpty() ){
			flag++ ;
			totalharga.setHintTextColor(Color.RED);
			UIErrorMessage = "Total harga tidak bisa dihitung. Hubungi IT ACA";
			return;
		}
		

        if (!validasiNilaiTSI()) {
        	flag ++;

        	return;
        }
        
		if( alamat.getText().toString().isEmpty() ){
			flag++ ;
			alamat.setHintTextColor(Color.RED);
			UIErrorMessage = "Alamat pertanggungan harus diisi";
			return;
		}
		if( cityName.getText().toString().isEmpty() ){
			flag++ ;
			cityName.setHintTextColor(Color.RED);
			UIErrorMessage = "Provinsi harus dipilih";
			return;
		}
		
		if( kodepos.getText().toString().isEmpty() ){
			flag++ ;
			kodepos.setHintTextColor(Color.RED);
			UIErrorMessage = "Kode Pos pertanggungan harus diisi";
			return;
		}
		if (etFromDate.getText().toString().isEmpty() ){
			flag++ ;
			etFromDate.setHintTextColor(Color.RED);
			UIErrorMessage = "Tanggal mulai asuransi harus diisi";
			return;
		}
		if (etToDate.getText().toString().isEmpty() ){
			flag++ ;
			etToDate.setHintTextColor(Color.RED);
			UIErrorMessage = "Tanggal berakhir asuransi harus diisi";
			return;
		}
		if( rate.getText().toString().isEmpty()){
			flag++ ;
			rate.setHintTextColor(Color.RED);
			UIErrorMessage = "Rate tidak ada. Hubungi IT ACA.";
			return;
		}
		if( premi.getText().toString().isEmpty()){
			flag++ ;
			premi.setHintTextColor(Color.RED);
			UIErrorMessage = "Premi tidak bisa dihitung. Hubungi IT ACA";
			return;
		}
		if( discpct.getText().toString().isEmpty()){
			flag++ ;
			discpct.setHintTextColor(Color.RED);
			UIErrorMessage = "Diskon % harus diisi, jika tidak ada masukkan angka 0";
			return;
		}
		if( disc.getText().toString().isEmpty()){
			flag++ ;
			disc.setHintTextColor(Color.RED);
			UIErrorMessage = "Diskon harus diisi, jika tidak ada masukkan angka 0";
			return;
		}
		
		if (Utility.parseDouble(discpct) > MAX_DISCOUNT){
			flag++;
			discpct.setTextColor(Color.RED);
			UIErrorMessage = "% diskon terlalu besar, maksimum adalah " + String.valueOf(MAX_DISCOUNT) + "%";
			return;
		}
			
	}

	public void btnNextClick(View v)
	{
		try{
			validasiNext();
			if(flag != 0)
				Utility.showCustomDialogInformation(FillAsriActivity.this, "Warning", UIErrorMessage);

			else {
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
//				if(custList != null) {
//					customername.setText(custList.get(0).get("CUSTOMER_NAME"));
//					customerno.setTextColor(Color.BLACK);
//				}
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
	
	private double getHargabangunan () throws ParseException  {
		return Utility.parseDouble(hargabangunan);
	}
	
	private double getHargaperabotan () throws ParseException {
		return Utility.parseDouble(hargaperabotan);
	}
	
	private double getTSI () throws ParseException{
		double hargaBangunan = getHargabangunan();
		double hargaPerabotan = getHargaperabotan();
		
		double TSI = hargaBangunan + hargaPerabotan;
	 				
		return TSI;
	}
	
	private double getRate (){
		DBA_MASTER_ASRI_RATE dba = null;
		Cursor c = null;
		double v = 0.0;
		
		try {
			
			dba = new DBA_MASTER_ASRI_RATE(getBaseContext());
			dba.open();
			
			c = dba.getRate(getTSI());
			
			if (c.moveToFirst())
				v = c.getDouble(1);
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
	
	@SuppressLint("DefaultLocale")
	public void insertDB()
	{
		DBA_PRODUCT_ASRI dba = null;
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
				dba = new DBA_PRODUCT_ASRI(getBaseContext());
				dba2 = new DBA_PRODUCT_MAIN(getBaseContext());
				
				dba.open();
				dba2.open();
				
				String sama;
				
				if (swiAlamat.isChecked()) sama = "1"; else sama = "0";
	
				if (PRODUCT_ACTION.equals("NEW")) {
					SPPA_ID = dba2.initialInsert(
							customerno.getText().toString(), 
							customername.getText().toString(), 
							"ASRI",
							nf.parse((premi.getText().toString())).doubleValue(),
							nf.parse((materai.getText().toString())).doubleValue(),
							nf.parse((polis.getText().toString())).doubleValue(),
							nf.parse((total.getText().toString())).doubleValue(),
							etFromDate.getText().toString(), etToDate.getText().toString(),
							Utility.DateToString(Utility.GetToday(), "dd/MM/yyyy"),
							"","","","N","", 
							nf.parse((discpct.getText().toString())).doubleValue(),
							nf.parse((disc.getText().toString())).doubleValue());
					
					dba.delete(SPPA_ID);
					dba.initialInsert(SPPA_ID, 
							sama, 
							Integer.parseInt(luas.getText().toString()),
							nf.parse(hargabangunan.getText().toString()).doubleValue(), 
							nf.parse(hargaperabotan.getText().toString()).doubleValue(),
							nf.parse(totalharga.getText().toString()).doubleValue(), 
							alamat.getText().toString().toUpperCase(Locale.getDefault()),
							"", "", "", "", 
							cityName.getTag().toString(), 
							kodepos.getText().toString().toUpperCase(Locale.getDefault()), 
							"", "", "", "", "",
							etFromDate.getText().toString(), etToDate.getText().toString(), 
							nf_asri.parse(rate.getText().toString()).doubleValue(),
							nf.parse(premi.getText().toString()).doubleValue(),
							nf.parse(materai.getText().toString()).doubleValue(), 
							nf.parse(polis.getText().toString()).doubleValue(),
							nf.parse(total.getText().toString()).doubleValue(), 
							"ASRI", 
							customername.getText().toString(),
							cityName.getText().toString()
							);
				}
				else {
					dba2.nextInsert(
							SPPA_ID, 
							customerno.getText().toString(), 
							customername.getText().toString(),
							nf.parse(premi.getText().toString()).doubleValue(),
							nf.parse(materai.getText().toString()).doubleValue(),
							nf.parse(polis.getText().toString()).doubleValue(),
							nf.parse(total.getText().toString()).doubleValue(),
							etFromDate.getText().toString(), etToDate.getText().toString(),
							Utility.DateToString(Utility.GetToday(), "dd/MM/yyyy"),
							nf.parse((discpct.getText().toString())).doubleValue(),
							nf.parse((disc.getText().toString())).doubleValue());
					
					dba.nextInsert(
							SPPA_ID, 
							sama, 
							Integer.parseInt(luas.getText().toString()), 
							nf.parse(hargabangunan.getText().toString()).doubleValue(), 
							nf.parse(hargaperabotan.getText().toString()).doubleValue(), 
							nf.parse(totalharga.getText().toString()).doubleValue(),
							alamat.getText().toString().toUpperCase(Locale.getDefault()), 
							"", "", "", "", 
							cityName.getTag().toString(), 
							kodepos.getText().toString().toUpperCase(), 
							"", "", "", "", "", 
							etFromDate.getText().toString(), etToDate.getText().toString(),
							nf_asri.parse(rate.getText().toString()).doubleValue(),
							nf.parse(premi.getText().toString()).doubleValue(),
							nf.parse(materai.getText().toString()).doubleValue(), 
							nf.parse(polis.getText().toString()).doubleValue(),
							nf.parse(total.getText().toString()).doubleValue(), 
							"ASRI", 
							customername.getText().toString(),
							cityName.getText().toString());
				}
				
				
				Intent i = new Intent(getBaseContext(),  ConfirmActivity.class);
				b.putLong("SPPA_ID", SPPA_ID);
				i.putExtras(b);
				startActivity(i);
				this.finish();
			}
				
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			
			Log.d("AMM", "finally");
			
			if (dba != null)
				dba.close();
			
			if (dba2 != null)
				dba2.close();
		}
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
				return new DatePickerDialog(this, datePickerListenerNext, Y + 1, M, D + 1);
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

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK)
			Back();
		
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onBackPressed() {
		Back();
	}
	
	private void validasiTanggal () {
		((Button)findViewById(R.id.btnNext)).setEnabled(true);
		etFromDate.setTextColor(Color.BLACK);
		boolean flag = Utility.validasiEffDate(etFromDate.getText().toString(), 
							Utility.GetTomorrowString(Utility.GetTodayString()), getBaseContext());
		
		if (!flag) {
			etFromDate.setTextColor(Color.RED);
			Utility.showCustomDialogInformation(FillAsriActivity.this, "Warning", "Tanggal mulai minimal besok hari");
			((Button)findViewById(R.id.btnNext)).setEnabled(false);
		}
	}
	
	private boolean validasiNilaiTSI () {
		
		boolean flag = true; 
		
		DBA_MASTER_PRODUCT_SETTING dbSetting= new DBA_MASTER_PRODUCT_SETTING(this);
		Cursor cSetting = null;
		double maxTSI = 0;
		double minTSI = 0; 
		
		try {
			dbSetting.open();
			cSetting = dbSetting.getRow("01");

			if (!cSetting.moveToFirst())
				return false;

			minTSI = cSetting.getDouble(3);
			maxTSI = cSetting.getDouble(4);
			

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dbSetting != null)
				dbSetting.close();

			if (cSetting != null)
				cSetting.close();

		}
				
		try {
			if (nf.parse(totalharga.getText().toString()).doubleValue() > maxTSI )
			{
				flag = false;  
				
				hargabangunan.setTextColor(Color.RED);
				hargaperabotan.setTextColor(Color.RED);
				totalharga.setTextColor(Color.RED);
				
				Utility.showCustomDialogInformation(this, "Informasi", "Maksimal TSI " + nf.format(maxTSI)); 
				
			}
			else {
				hargabangunan.setTextColor(Color.BLACK);
				hargaperabotan.setTextColor(Color.BLACK);
				totalharga.setTextColor(Color.BLACK);
				
			}
				
		} catch (ParseException e1) { 
			e1.printStackTrace();
		}
 
		
	 
		return flag;
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
				DBA_PRODUCT_ASRI dba2 = null;
				
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
					
					dba2 = new DBA_PRODUCT_ASRI(context);
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
