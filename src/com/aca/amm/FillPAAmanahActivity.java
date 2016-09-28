package com.aca.amm;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.aca.amm.R;
import com.aca.amm.FillMedisafe.DeleteSPPA;
import com.aca.amm.R.id;
import com.aca.amm.R.layout;
import com.aca.amm.R.menu;
import com.aca.database.DBA_MASTER_PA_AMANAH_RATE;
import com.aca.database.DBA_MASTER_PRODUCT_SETTING;
import com.aca.database.DBA_PRODUCT_MAIN;
import com.aca.database.DBA_PRODUCT_PA_AMANAH;

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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class FillPAAmanahActivity extends ControlNormalActivity {

	private ArrayList<HashMap<String, String>> custList ;
	
	private Context context = null;
	
	private Bundle b;
	private NumberFormat nf;
	private String PRODUCT_ACTION;
	private long SPPA_ID;
	private int flag = 0;
	private Calendar c;

    private double v_premi = 0, v_materai = 0, v_polis = 0, v_total = 0, v_discpct = 0, v_disc = 0;
	private EditText etFromDate, etToDate;
	private static final int DATE_FROM_ID = 99;
	private static final int DATE_TO_ID = 100;
	private EditText namaahliwaris1, alamatahliwaris1, hubahliwaris1;
	private EditText namaahliwaris2, alamatahliwaris2, hubahliwaris2;
	private EditText namaahliwaris3, alamatahliwaris3, hubahliwaris3;
	private EditText nilaipertanggungan, premi, polis, materai, total, customerno, customername, discpct, disc;
	private RadioButton plan1, plan2, plan3;
	private RadioGroup rbgPlan;
	
	private String UIErrorMessage = "Lengkapi semua data";
	private double MAX_DISCOUNT = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_fill_paamanah);

		context = FillPAAmanahActivity.this;
		
		nf = NumberFormat.getInstance();
//		nf.setMaximumFractionDigits(2);
//		nf.setMinimumFractionDigits(2);
		
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
			
			MAX_DISCOUNT = Utility.getMaxDiscount(getBaseContext(), "29");
			
			if (Utility.getIsDiscountable(getBaseContext(), "10").equals("0"))
			{
				disc.setEnabled(false);
				discpct.setEnabled(false);
			}
		}
		catch(Exception e){	
			e.printStackTrace();
		}	
	}
	
	private void disableView() {
		// TODO Auto-generated method stub
			
		namaahliwaris1.setEnabled(false);
		namaahliwaris2.setEnabled(false);
		namaahliwaris3.setEnabled(false);
		
		alamatahliwaris1.setEnabled(false);
		alamatahliwaris2.setEnabled(false);
		alamatahliwaris3.setEnabled(false);
		
		hubahliwaris1.setEnabled(false);
		hubahliwaris2.setEnabled(false);
		hubahliwaris3.setEnabled(false);
		
		etToDate.setEnabled(false);
		etFromDate.setEnabled(false);
		
		nilaipertanggungan.setEnabled(false);
		premi.setEnabled(false);
		discpct.setEnabled(false);
		disc.setEnabled(false);
		polis.setEnabled(false);
		materai.setEnabled(false);
		total.setEnabled(false);
		
		customerno.setEnabled(false);
		customername.setEnabled(false);

		rbgPlan.setEnabled(false);
		plan1.setEnabled(false);
		plan2.setEnabled(false);
		plan3.setEnabled(false);
		
		(findViewById(R.id.btnChooseCustomer)).setEnabled(false);
		
		/////////////////
		
		namaahliwaris1.setTextColor(Color.BLACK);
		namaahliwaris2.setTextColor(Color.BLACK);
		namaahliwaris3.setTextColor(Color.BLACK);
		
		alamatahliwaris1.setTextColor(Color.BLACK);
		alamatahliwaris2.setTextColor(Color.BLACK);
		alamatahliwaris3.setTextColor(Color.BLACK);
		
		hubahliwaris1.setTextColor(Color.BLACK);
		hubahliwaris2.setTextColor(Color.BLACK);
		hubahliwaris3.setTextColor(Color.BLACK);
		
		etToDate.setTextColor(Color.BLACK);
		etFromDate.setTextColor(Color.BLACK);
		
		nilaipertanggungan.setTextColor(Color.BLACK);
		premi.setTextColor(Color.BLACK);
		discpct.setTextColor(Color.BLACK);
		disc.setTextColor(Color.BLACK);
		polis.setTextColor(Color.BLACK);
		materai.setTextColor(Color.BLACK);
		total.setTextColor(Color.BLACK);
		
		customerno.setTextColor(Color.BLACK);
		customername.setTextColor(Color.BLACK);

	}

	private void InitControls() {

		plan1 = (RadioButton)findViewById(R.id.rboPlan1);
		plan2 = (RadioButton)findViewById(R.id.rboPlan2);
		plan3 = (RadioButton)findViewById(R.id.rboPlan3);
		
		namaahliwaris1 = (EditText)findViewById(R.id.txtnama1);
		namaahliwaris2 = (EditText)findViewById(R.id.txtnama2);
		namaahliwaris3 = (EditText)findViewById(R.id.txtnama3);
		
		alamatahliwaris1 = (EditText)findViewById(R.id.txtalamat1);
		alamatahliwaris2 = (EditText)findViewById(R.id.txtalamat2);
		alamatahliwaris3 = (EditText)findViewById(R.id.txtalamat3);
		
		hubahliwaris1 = (EditText)findViewById(R.id.txthubungan1);
		hubahliwaris2 = (EditText)findViewById(R.id.txthubungan2);
		hubahliwaris3 = (EditText)findViewById(R.id.txthubungan3);
		
		etToDate = (EditText)findViewById(R.id.txttodate);
		etFromDate = (EditText)findViewById(R.id.txtfromdate);
		
		nilaipertanggungan = (EditText)findViewById(R.id.txtNilaiPertanggungan);
		premi = (EditText)findViewById(R.id.txtPremi);
		discpct = (EditText)findViewById(R.id.txtDiscountPct);
		disc = (EditText)findViewById(R.id.txtDiscount);
		polis = (EditText)findViewById(R.id.txtBiayaPolis);
		materai = (EditText)findViewById(R.id.txtMaterai);
		total = (EditText)findViewById(R.id.txtTotal);
		
		customerno = (EditText)findViewById(R.id.txtCustomerNo);
		customername = (EditText)findViewById(R.id.txtCustomerName);

		rbgPlan = (RadioGroup)findViewById(R.id.rbgPlan);
	}

	private void LoadDB() {
	
		DBA_PRODUCT_MAIN dbm = null;
		DBA_PRODUCT_PA_AMANAH dba  = null;
		
		Cursor cm = null;
		Cursor c = null;
		try{
			
			dbm = new DBA_PRODUCT_MAIN(getBaseContext());
			dba = new DBA_PRODUCT_PA_AMANAH(getBaseContext());
			
			dbm.open();
			cm = dbm.getRow(SPPA_ID);
			cm.moveToFirst();
			
			dba.open();
			c = dba.getRow(SPPA_ID);
			c.moveToFirst();
			
			customerno.setText(cm.getString(1));
			customername.setText(cm.getString(2));
			
			namaahliwaris1.setText(c.getString(3));
			hubahliwaris1.setText(c.getString(4));
			alamatahliwaris1.setText(c.getString(5));
			
			namaahliwaris2.setText(c.getString(6));
			hubahliwaris2.setText(c.getString(7));
			alamatahliwaris2.setText(c.getString(8));
			
			namaahliwaris3.setText(c.getString(9));
			hubahliwaris3.setText(c.getString(10));
			alamatahliwaris3.setText(c.getString(11));
			
			nilaipertanggungan.setText(nf.format(c.getDouble(2)));
			
			etFromDate.setText(c.getString(12));
			etToDate.setText(c.getString(13));
			
			if(c.getLong(14) == 1)
				plan1.setChecked(true);
			else if(c.getLong(14) == 2)
				plan2.setChecked(true);
			else if(c.getLong(14) == 3)
				plan3.setChecked(true);

			premi.setText(nf.format(c.getDouble(15)));
			discpct.setText(nf.format(cm.getDouble(23)));
			disc.setText(nf.format(cm.getDouble(24)));
			polis.setText(nf.format(c.getDouble(16)));
			total.setText(nf.format(c.getDouble(17)));
			
			if (cm.getString(20).equals("R")){
				etFromDate.setEnabled(false);
				etToDate.setEnabled(false);
				
				namaahliwaris1.setEnabled(false);
				namaahliwaris2.setEnabled(false);
				namaahliwaris3.setEnabled(false);
				
				hubahliwaris1.setEnabled(false);
				hubahliwaris2.setEnabled(false);
				hubahliwaris3.setEnabled(false);
				
				alamatahliwaris1.setEnabled(false);
				alamatahliwaris2.setEnabled(false);
				alamatahliwaris3.setEnabled(false);

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
						Utility.showCustomDialogInformation(FillPAAmanahActivity.this, "Warning", "Tanggal mulai minimal besok hari");
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
		
		rbgPlan.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	        public void onCheckedChanged(RadioGroup group, int checkedId) {
	        	
        		try {
        			if (nilaipertanggungan.getText().toString().isEmpty())
						return ;
					
					calculateAll();
					
				} catch (ParseException e) {
					e.printStackTrace();
				}						
	        	
	        }
	    });
		
		nilaipertanggungan.addTextChangedListener(new TextWatcher() {
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
				nilaipertanggungan.setTextColor(Color.BLACK);
				try {
				   if (!s.toString().equals(current)) {
					   nilaipertanggungan.removeTextChangedListener(this);     
		                
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
		                nilaipertanggungan.setText(formatted);
		                nilaipertanggungan.setSelection(formatted.length());
		                nilaipertanggungan.addTextChangedListener(this);
		                
		                calculateAll();
//		                validasiNilaiTSI();
		                
		            }                           
				} catch (Exception e) {
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
	}
	private void validasiNilaiTSI () {
		((Button)findViewById(R.id.btnNext)).setEnabled(true);
		nilaipertanggungan.setTextColor(Color.BLACK);
		
		boolean flag = true;
		

		DBA_MASTER_PRODUCT_SETTING dbSetting= new DBA_MASTER_PRODUCT_SETTING(this);
		Cursor cSetting = null;
		double maxTSI = 0;
		double minTSI = 0; 
		
		try {
			dbSetting.open();
			cSetting = dbSetting.getRow("10");

			if (!cSetting.moveToFirst())
				return;

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
			if (getTSI() > maxTSI) {
				flag = false;
				nilaipertanggungan.setTextColor(Color.RED);
			}
			if (getTSI() % 10000000 != 0) {
				flag = false;
				nilaipertanggungan.setTextColor(Color.RED);
			}
		} 
		catch (ParseException e) {
			e.printStackTrace();
		}
		
		if (!flag) {
			((Button)findViewById(R.id.btnNext)).setEnabled(false);
		}
	
	}
	
	private void calculateAll() throws ParseException{
		v_premi = getPremi() * getTSI() / 10000000;			
		v_discpct = Utility.parseDouble(discpct);
		v_disc = Utility.countDiscount(v_premi, v_discpct);
		v_materai = Utility.countMaterai(v_premi);
		v_polis = 0;
		v_total = Utility.countTotal(v_premi, v_polis, v_materai, v_disc);
		 
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
		getMenuInflater().inflate(R.menu.fill_paamanah, menu);
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
	
		
		if (namaahliwaris1.getText().toString().isEmpty()) {
			flag++;
			namaahliwaris1.setHintTextColor(Color.RED);
			UIErrorMessage = "Nama ahli waris ke-1 harus diisi";
			return;
		}	
		if (hubahliwaris1.getText().toString().isEmpty()) {
			flag++;
			hubahliwaris1.setHintTextColor(Color.RED);
			UIErrorMessage = "Hubungan ahli waris ke-1 harus diisi";
			return;
		}
		if (alamatahliwaris1.getText().toString().isEmpty()) {
			flag++;
			alamatahliwaris1.setHintTextColor(Color.RED);
			UIErrorMessage = "Alamat ahli waris ke-1 harus diisi";
			return;
		}
		
		
		if (etFromDate.getText().toString().isEmpty() ){
			flag++ ;
			etFromDate.setHintTextColor(Color.RED);
			UIErrorMessage = "Tanggal mulai harus diisi";
			return;
		}
		if (etToDate.getText().toString().isEmpty() ){
			flag++ ;
			etToDate.setHintTextColor(Color.RED);
			UIErrorMessage = "Tanggal berakhir harus diisi";
			return;
		}
		if( nilaipertanggungan.getText().toString().isEmpty() ){
			flag++ ;
			nilaipertanggungan.setHintTextColor(Color.RED);
			UIErrorMessage = "Nilai pertanggungan harus diisi";
			return;
		}
		
		DBA_MASTER_PRODUCT_SETTING dbSetting= new DBA_MASTER_PRODUCT_SETTING(this);
		Cursor cSetting = null;
		double maxTSI = 0;
		double minTSI = 0; 
		
		try {
			dbSetting.open();
			cSetting = dbSetting.getRow("10");

			if (!cSetting.moveToFirst())
				return;

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
		
		
		
		if (getTSI() > maxTSI) {
			flag++;
			nilaipertanggungan.setTextColor(Color.RED);
			UIErrorMessage = "Nilai pertanggungan maksimal " + nf.format(maxTSI);
			return;
		}
		
		if (getTSI() < minTSI) {
			flag++;
			nilaipertanggungan.setTextColor(Color.RED);
			UIErrorMessage = "Nilai pertanggungan minimal " + nf.format(minTSI);
			return;
		}
		
		
		if (getTSI() % 10000000 != 0) {
			flag++;
			UIErrorMessage = "Nilai pertanggungan harus kelipatan 10 juta";
			nilaipertanggungan.setTextColor(Color.RED);
			return;
		}
	
		if (Utility.parseDouble(discpct) > MAX_DISCOUNT){
			flag++;
			discpct.setTextColor(Color.RED);
			UIErrorMessage = "% diskon terlalu besar, maksimum adalah " + String.valueOf(MAX_DISCOUNT) + "%";
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
	
		
	}

	public void btnNextClick(View v) {
	    try{
	    	validasiNext();
	    	
			if(flag != 0)
				Utility.showCustomDialogInformation(FillPAAmanahActivity.this, "Warning", 
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
		DBA_PRODUCT_PA_AMANAH dba = null;
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
				dba = new DBA_PRODUCT_PA_AMANAH(getBaseContext());
				dba2 = new DBA_PRODUCT_MAIN(getBaseContext());
				
				dba.open();
				dba2.open();
				
				int plan = 0;
				
				if(plan1.isChecked())
					plan = 1;
				else if(plan2.isChecked())
					plan = 2;
				else if(plan3.isChecked())
					plan = 3;
				
				if (PRODUCT_ACTION.equals("NEW")) {
					SPPA_ID = dba2.initialInsert(customerno.getText().toString(), customername.getText().toString(), "PAAMANAH",
							nf.parse(premi.getText().toString()).doubleValue(),	
							0,
							nf.parse(polis.getText().toString()).doubleValue(),
							nf.parse(total.getText().toString()).doubleValue(),
							etFromDate.getText().toString(), 
							etToDate.getText().toString(),
							Utility.DateToString(Utility.GetToday(), "dd/MM/yyyy"),
							"","","","N","",
							nf.parse((discpct.getText().toString())).doubleValue(),
							nf.parse((disc.getText().toString())).doubleValue());
					
					dba.initialInsert(SPPA_ID,
						nf.parse(nilaipertanggungan.getText().toString()).doubleValue(),
						namaahliwaris1.getText().toString().toUpperCase(), hubahliwaris1.getText().toString().toUpperCase(), alamatahliwaris1.getText().toString().toUpperCase(),
						namaahliwaris2.getText().toString().toUpperCase(), hubahliwaris2.getText().toString().toUpperCase(), alamatahliwaris2.getText().toString().toUpperCase(),
						namaahliwaris3.getText().toString().toUpperCase(), hubahliwaris3.getText().toString().toUpperCase(), alamatahliwaris3.getText().toString().toUpperCase(),
						plan, 
						etFromDate.getText().toString(), etToDate.getText().toString(),
						v_premi, v_polis, v_total, 
						"PAAMANAH",  customername.getText().toString());
				}
				else{
					 
					dba2.nextInsert(SPPA_ID, customerno.getText().toString(), customername.getText().toString(),
							nf.parse(premi.getText().toString()).doubleValue(),
							v_materai,
							nf.parse(polis.getText().toString()).doubleValue(),
							nf.parse(total.getText().toString()).doubleValue(),
							etFromDate.getText().toString(), 
							etToDate.getText().toString(),
							Utility.DateToString(Utility.GetToday(), "dd/MM/yyyy"),
							nf.parse((discpct.getText().toString())).doubleValue(),
							nf.parse((disc.getText().toString())).doubleValue());
					
					dba.nextInsert(SPPA_ID,
							nf.parse(nilaipertanggungan.getText().toString()).doubleValue(),
							namaahliwaris1.getText().toString().toUpperCase(), hubahliwaris1.getText().toString().toUpperCase(), alamatahliwaris1.getText().toString().toUpperCase(),
							namaahliwaris2.getText().toString().toUpperCase(), hubahliwaris2.getText().toString().toUpperCase(), alamatahliwaris2.getText().toString().toUpperCase(),
							namaahliwaris3.getText().toString().toUpperCase(), hubahliwaris3.getText().toString().toUpperCase(), alamatahliwaris3.getText().toString().toUpperCase(),
							plan, 
							etFromDate.getText().toString(), etToDate.getText().toString(),
							v_premi, v_polis, v_total,
							"PAAMANAH", customername.getText().toString());
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
	
	private double getTSI () throws ParseException{
		double tsi ;
		
		if(nilaipertanggungan.getText().toString().isEmpty())
			tsi = 0;
		else 
			tsi = nf.parse(nilaipertanggungan.getText().toString()).doubleValue();
		
		return tsi;
	}
	
	
	private double getPremi (){
		String pplan = "0";
		String type, age;
		
		if(plan1.isChecked())
			pplan = "1";
		else if(plan2.isChecked())
			pplan = "2";
		else if(plan3.isChecked())
			pplan = "3";
		
		DBA_MASTER_PA_AMANAH_RATE dba = null;
		Cursor c = null;
		double v = 0.0;
		
		try {
			dba = new DBA_MASTER_PA_AMANAH_RATE(getBaseContext());
			dba.open();
			
			type = "4";
			age = "0";
			
			c = dba.getRate(type, pplan, age);
			
			if (c.moveToFirst())
				v = c.getDouble(3);
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
				return new DatePickerDialog(this, datePickerListenerNext, Y+1, M, D);
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
			Utility.showCustomDialogInformation(FillPAAmanahActivity.this, "Warning", "Tanggal mulai minimal besok hari");
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
				DBA_PRODUCT_PA_AMANAH dba2 = null;
				
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
					
					dba2 = new DBA_PRODUCT_PA_AMANAH(context);
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
