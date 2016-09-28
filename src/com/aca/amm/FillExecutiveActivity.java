package com.aca.amm;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.aca.amm.R;
import com.aca.amm.FillAsriActivity.DeleteSPPA;
import com.aca.amm.R.id;
import com.aca.amm.R.layout;
import com.aca.amm.R.menu;
import com.aca.database.DBA_MASTER_EXECUTIVE_SAFE_RATE;
import com.aca.database.DBA_PRODUCT_EXECUTIVE_SAFE;
import com.aca.database.DBA_PRODUCT_MAIN;

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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class FillExecutiveActivity extends ControlNormalActivity {

	ArrayList<HashMap<String, String>> custList ;
	
	private Context context = null;
	
	private Bundle b;
	private NumberFormat nf;
	private String PRODUCT_ACTION;
	private long SPPA_ID;
	private int flag = 0;
	private Calendar c;
	
 	private double v_premi, v_polis, v_materai, v_total, v_discpct, v_disc;
 	private double v_premi_pasangan = 0, v_premi_anak_1 = 0, v_premi_anak_2 = 0, v_premi_anak_3 = 0;
	private EditText etFromDate, etToDate;
	private EditText etPasangan, etAnak1, etAnak2, etAnak3;
	private Switch pasangan, anak1, anak2, anak3;
	private static final int DATE_FROM_ID = 99;
	private static final int DATE_TO_ID = 100;
	private static final int DATE_LAHIR_PASANGAN = 101;
	private static final int DATE_LAHIR_ANAK1 = 102;
	private static final int DATE_LAHIR_ANAK2 = 103;
	private static final int DATE_LAHIR_ANAK3 = 104;
	private EditText namapasangan, noktppasangan, namaanak1, noktpanak1, namaanak2, noktpanak2, namaanak3, noktpanak3;
	private EditText namaahliwaris1, alamatahliwaris1, hubahliwaris1, namaahliwaris2, alamatahliwaris2, hubahliwaris2, namaahliwaris3, alamatahliwaris3, hubahliwaris3;
	private EditText premi, polis, materai, total, customerno, customername, discpct, disc;
	private RadioButton plan1, plan2, plan3, plan4;
	private RadioGroup rbgPlan;
	
	private String UIErrorMessage = "Lengkapi semua data";
	private double MAX_DISCOUNT = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_fill_executive);
		
		context = FillExecutiveActivity.this;
		
		nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		
		InitControls();
		RegisterListener();
		initColor();
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
			
			/*
			if (b.getString("CUSTOMER_NO") != null) {
				customerno.setText(b.getString("CUSTOMER_NO"));
				customername.setText(b.getString("CUSTOMER_NAME"));
				custList = Utility.getCustData(getBaseContext(), customerno.getText().toString());
			}
			else if(!customerno.getText().toString().isEmpty())
				custList = Utility.getCustData(getBaseContext(), customerno.getText().toString());
			*/
			
			
			MAX_DISCOUNT = Utility.getMaxDiscount(getBaseContext(), "29");
			
			
			if (Utility.getIsDiscountable(getBaseContext(), "05").equals("0"))
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
		plan1.setEnabled(false);
		plan2.setEnabled(false);
		plan3.setEnabled(false);
		plan4.setEnabled(false);
		
		namapasangan.setEnabled(false);
		namaanak1.setEnabled(false);
		namaanak2.setEnabled(false);
		namaanak3.setEnabled(false);
		
		noktppasangan.setEnabled(false);
		noktpanak1.setEnabled(false);
		noktpanak2.setEnabled(false);
		noktpanak3.setEnabled(false);
		
		namaahliwaris1.setEnabled(false);
		namaahliwaris2 .setEnabled(false);
		namaahliwaris3 .setEnabled(false);
		
		alamatahliwaris1.setEnabled(false);
		alamatahliwaris2 .setEnabled(false);
		alamatahliwaris3 .setEnabled(false);
		
		hubahliwaris1.setEnabled(false);
		hubahliwaris2.setEnabled(false);
		hubahliwaris3.setEnabled(false);
	
		etFromDate.setEnabled(false);
		etToDate .setEnabled(false);
		etPasangan .setEnabled(false);
		etAnak1.setEnabled(false);
		etAnak2 .setEnabled(false);
		etAnak3 .setEnabled(false);
		
		pasangan .setEnabled(false);
		anak1.setEnabled(false);
		anak2.setEnabled(false);
		anak3.setEnabled(false);
		
		customerno.setEnabled(false);
		customername.setEnabled(false);
		
		(findViewById(R.id.btnChooseCustomer)).setEnabled(false);
		
//		TXT COLOR BLACK
		
		
		namapasangan.setTextColor(Color.BLACK);
		namaanak1.setTextColor(Color.BLACK);
		namaanak2.setTextColor(Color.BLACK);
		namaanak3.setTextColor(Color.BLACK);
		
		noktppasangan.setTextColor(Color.BLACK);
		noktpanak1.setTextColor(Color.BLACK);
		noktpanak2.setTextColor(Color.BLACK);
		noktpanak3.setTextColor(Color.BLACK);
		
		namaahliwaris1.setTextColor(Color.BLACK);
		namaahliwaris2 .setTextColor(Color.BLACK);
		namaahliwaris3 .setTextColor(Color.BLACK);
		
		alamatahliwaris1.setTextColor(Color.BLACK);
		alamatahliwaris2 .setTextColor(Color.BLACK);
		alamatahliwaris3 .setTextColor(Color.BLACK);
		
		hubahliwaris1.setTextColor(Color.BLACK);
		hubahliwaris2.setTextColor(Color.BLACK);
		hubahliwaris3.setTextColor(Color.BLACK);
	
		etFromDate.setTextColor(Color.BLACK);
		etToDate .setTextColor(Color.BLACK);
		etPasangan .setTextColor(Color.BLACK);
		etAnak1.setTextColor(Color.BLACK);
		etAnak2 .setTextColor(Color.BLACK);
		etAnak3 .setTextColor(Color.BLACK);
		
		customerno.setTextColor(Color.BLACK);
		customername.setTextColor(Color.BLACK);
		
	}

	private void InitControls() {

		plan1 = (RadioButton)findViewById(R.id.rboPlan1);
		plan2 = (RadioButton)findViewById(R.id.rboPlan2);
		plan3 = (RadioButton)findViewById(R.id.rboPlan3);
		plan4 = (RadioButton)findViewById(R.id.rboPlan4);
		
		namapasangan = (EditText)findViewById(R.id.txtnama1);
		namaanak1 = (EditText)findViewById(R.id.txtnama2);
		namaanak2 = (EditText)findViewById(R.id.txtnama3);
		namaanak3 = (EditText)findViewById(R.id.txtnama4);
		
		noktppasangan = (EditText)findViewById(R.id.txtnoktp1);
		noktpanak1 = (EditText)findViewById(R.id.txtnoktp2);
		noktpanak2 = (EditText)findViewById(R.id.txtnoktp3);
		noktpanak3 = (EditText)findViewById(R.id.txtnoktp4);
		
		namaahliwaris1 = (EditText)findViewById(R.id.txtnamaahliwaris1);
		namaahliwaris2 = (EditText)findViewById(R.id.txtnamaahliwaris2);
		namaahliwaris3 = (EditText)findViewById(R.id.txtnamaahliwaris3);
		
		alamatahliwaris1 = (EditText)findViewById(R.id.txtalamat1);
		alamatahliwaris2 = (EditText)findViewById(R.id.txtalamat2);
		alamatahliwaris3 = (EditText)findViewById(R.id.txtalamat3);
		
		hubahliwaris1 = (EditText)findViewById(R.id.txthubungan1);
		hubahliwaris2 = (EditText)findViewById(R.id.txthubungan2);
		hubahliwaris3 = (EditText)findViewById(R.id.txthubungan3);
		
		premi = (EditText)findViewById(R.id.txtPremi);
		discpct = (EditText)findViewById(R.id.txtDiscountPct);
		disc = (EditText)findViewById(R.id.txtDiscount);
		polis = (EditText)findViewById(R.id.txtBiayaPolis);
		materai = (EditText)findViewById(R.id.txtMaterai);
		total = (EditText)findViewById(R.id.txtTotal);
		
		etFromDate = (EditText)findViewById(R.id.txtfromdate);
		etToDate  = (EditText)findViewById(R.id.txttodate);
		etPasangan = (EditText)findViewById(R.id.txttgllahir1);
		etAnak1 = (EditText)findViewById(R.id.txttgllahir2);
		etAnak2 = (EditText)findViewById(R.id.txttgllahir3);
		etAnak3 = (EditText)findViewById(R.id.txttgllahir4);
		
		pasangan = (Switch)findViewById(R.id.swiPasangan);
		anak1 = (Switch)findViewById(R.id.swiAnak1);
		anak2 = (Switch)findViewById(R.id.swiAnak2);
		anak3 = (Switch)findViewById(R.id.swiAnak3);
		
		customerno = (EditText)findViewById(R.id.txtCustomerNo);
		customername = (EditText)findViewById(R.id.txtCustomerName);
		
		rbgPlan = (RadioGroup)findViewById(R.id.rbgPlan);
	}

	private void initColor () {
		namapasangan.setEnabled(false);
		etPasangan.setEnabled(false);
		noktppasangan.setEnabled(false);
		
		namapasangan.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
		etPasangan.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
		noktppasangan.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));	
		
		
		namaanak1.setEnabled(false);
		etAnak1.setEnabled(false);
		noktpanak1.setEnabled(false);
		
		namaanak1.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
		etAnak1.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
		noktpanak1.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));	
		
		namaanak2.setEnabled(false);
		etAnak2.setEnabled(false);
		noktpanak2.setEnabled(false);
		
		namaanak2.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
		etAnak2.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
		noktpanak2.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));		
		
		
		namaanak3.setEnabled(false);
		etAnak3.setEnabled(false);
		noktpanak3.setEnabled(false);
		
		namaanak3.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
		etAnak3.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
		noktpanak3.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));		
		
		
	}
	
	private void LoadDB() {

		DBA_PRODUCT_MAIN dbm = null;
		DBA_PRODUCT_EXECUTIVE_SAFE dba = null;
		
		Cursor cm = null;
		Cursor c = null;
		
		try{
			
			dbm = new DBA_PRODUCT_MAIN(getBaseContext());
			dba = new DBA_PRODUCT_EXECUTIVE_SAFE(getBaseContext());
			
			dbm.open();
			cm = dbm.getRow(SPPA_ID);
			cm.moveToFirst();
			
			dba.open();
			c = dba.getRow(SPPA_ID);
			c.moveToFirst();
			
			customerno.setText(cm.getString(1));
			customername.setText(cm.getString(2));

			namapasangan.setText(c.getString(2));
			etPasangan.setText(c.getString(3));
			noktppasangan.setText(c.getString(4));
			
			namaanak1.setText(c.getString(5));
			etAnak1.setText(c.getString(6));
			noktpanak1.setText(c.getString(7));
			
			namaanak2.setText(c.getString(8));
			etAnak2.setText(c.getString(9));
			noktpanak2.setText(c.getString(10));
			
			namaanak3.setText(c.getString(11));
			etAnak3.setText(c.getString(12));
			noktpanak3.setText(c.getString(13));
			
			namaahliwaris1.setText(c.getString(14));
			hubahliwaris1.setText(c.getString(15));
			alamatahliwaris1.setText(c.getString(16));
			
			namaahliwaris2.setText(c.getString(17));
			hubahliwaris2.setText(c.getString(18));
			alamatahliwaris2.setText(c.getString(19));
			
			namaahliwaris3.setText(c.getString(20));
			hubahliwaris3.setText(c.getString(21));
			alamatahliwaris3.setText(c.getString(22));
			
			etFromDate.setText(c.getString(23));
			etToDate.setText(c.getString(24));
			
			if(c.getLong(35) == 1)
				pasangan.setChecked(true);
			if(c.getLong(36) == 1)
				anak1.setChecked(true);
			if(c.getLong(37) == 1)
				anak2.setChecked(true);
			if(c.getLong(38) == 1)
				anak3.setChecked(true);
			
			if(c.getLong(25) == 1)
				plan1.setChecked(true);
			else if(c.getLong(25) == 2)
				plan2.setChecked(true);
			else if(c.getLong(25) == 3)
				plan3.setChecked(true);
			else if(c.getLong(25) == 4)
				plan4.setChecked(true);
			
			premi.setText(nf.format(c.getDouble(26)));
			discpct.setText(nf.format(cm.getDouble(23)));
			disc.setText(nf.format(cm.getDouble(24)));
			polis.setText(nf.format(c.getDouble(27)));
			total.setText(nf.format(c.getDouble(28)));
			
			if (cm.getString(20).equals("R")){
				etFromDate.setEnabled(false);
				etToDate.setEnabled(false);
				
				namaanak1.setEnabled(false);
				namaanak2.setEnabled(false);
				namaanak3.setEnabled(false);
				namapasangan.setEnabled(false);
				
				noktpanak1.setEnabled(false);
				noktpanak2.setEnabled(false);
				noktpanak3.setEnabled(false);
				noktppasangan.setEnabled(false);
				
				etAnak1.setEnabled(false);
				etAnak2.setEnabled(false);
				etAnak3.setEnabled(false);
				etPasangan.setEnabled(false);
				
				anak1.setEnabled(false);
				anak2.setEnabled(false);
				anak3.setEnabled(false);
				pasangan.setEnabled(false);
				
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
	
	private void RegisterListener(){
		etPasangan.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus)
					showDialog(DATE_LAHIR_PASANGAN);
			}
		});
		
		etPasangan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DATE_LAHIR_PASANGAN);
			}
		});
		
		etAnak1.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus)
					showDialog(DATE_LAHIR_ANAK1);
			}
		});
		
		etAnak2.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus)
					showDialog(DATE_LAHIR_ANAK2);
			}
		});
		
		etAnak2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DATE_LAHIR_ANAK2);
			}
		});
		
		etAnak1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DATE_LAHIR_ANAK1);
			}
		});
		
		etAnak3.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus)
					showDialog(DATE_LAHIR_ANAK3);
			}
		});
		
		etAnak3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DATE_LAHIR_ANAK3);
			}
		});

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
						Utility.showCustomDialogInformation(FillExecutiveActivity.this, "Warning", "Tanggal mulai minimal besok hari");
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
	        		calculateAll();
	        }
	    });
		

		pasangan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if(!pasangan.isChecked())
				{
					namapasangan.setEnabled(false);
					etPasangan.setEnabled(false);
					noktppasangan.setEnabled(false);
					
					namapasangan.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
					etPasangan.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
					noktppasangan.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
					
					namapasangan.setText("");
					etPasangan.setText("");
					noktppasangan.setText("");

				}
				else {
					namapasangan.setEnabled(true);
					etPasangan.setEnabled(true);
					noktppasangan.setEnabled(true);
					
					namapasangan.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext));					
					etPasangan.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext));					
					noktppasangan.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext));					
					
				}
				
				namapasangan.setHintTextColor(Color.GRAY);				
				etPasangan.setHintTextColor(Color.GRAY);
				noktppasangan.setHintTextColor(Color.GRAY);
				
				
				calculateAll();
//				if(!customerno.getText().toString().isEmpty() && !etFromDate.getText().toString().isEmpty() && !etToDate.getText().toString().isEmpty()){
//					if (!etPasangan.getText().toString().isEmpty()){
//					}
//				}
			}
		});
		
		anak1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if(!anak1.isChecked())
				{
					namaanak1.setEnabled(false);
					etAnak1.setEnabled(false);
					noktpanak1.setEnabled(false);
					
					namaanak1.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
					etAnak1.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
					noktpanak1.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					

					namaanak1.setText("");
					etAnak1.setText("");
					noktpanak1.setText("");
				}
				else {
					namaanak1.setEnabled(true);
					etAnak1.setEnabled(true);
					noktpanak1.setEnabled(true);
					
					namaanak1.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext));					
					etAnak1.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext));					
					noktpanak1.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext));						
				}
				namaanak1.setHintTextColor(Color.GRAY);				
				etAnak1.setHintTextColor(Color.GRAY);
				noktpanak1.setHintTextColor(Color.GRAY);
				
				calculateAll();
			}
		});
		
		anak2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if(!anak2.isChecked())
				{
					namaanak2.setEnabled(false);
					etAnak2.setEnabled(false);
					noktpanak2.setEnabled(false);
					
					namaanak2.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
					etAnak2.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
					noktpanak2.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					

					namaanak2.setText("");
					etAnak2.setText("");
					noktpanak2.setText("");
				}
				else {
					namaanak2.setEnabled(true);
					etAnak2.setEnabled(true);
					noktpanak2.setEnabled(true);
					
					namaanak2.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext));					
					etAnak2.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext));					
					noktpanak2.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext));					
					
				}

				namaanak2.setHintTextColor(Color.GRAY);				
				etAnak2.setHintTextColor(Color.GRAY);
				noktpanak2.setHintTextColor(Color.GRAY);
				
				calculateAll();
				
			}
		});
	
		anak3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if(!anak3.isChecked())
				{
					namaanak3.setEnabled(false);
					etAnak3.setEnabled(false);
					noktpanak3.setEnabled(false);
					
					namaanak3.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
					etAnak3.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
					noktpanak3.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					

					namaanak3.setText("");
					etAnak3.setText("");
					noktpanak3.setText("");
				}
				else {
					namaanak3.setEnabled(true);
					etAnak3.setEnabled(true);
					noktpanak3.setEnabled(true);
					
					namaanak3.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext));					
					etAnak3.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext));					
					noktpanak3.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext));					
					
				}

				namaanak3.setHintTextColor(Color.GRAY);				
				etAnak3.setHintTextColor(Color.GRAY);
				noktpanak3.setHintTextColor(Color.GRAY);
				
				calculateAll();
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

	
	private void calculateAll() {
		try
		{
			getPremi();
			getDisc();
			getPolis();
			getMaterai();
			getTotal();
			
			premi.setText(nf.format(v_premi + v_premi_pasangan + v_premi_anak_1 + v_premi_anak_2 + v_premi_anak_3));
			disc.setText(nf.format(v_disc));
			materai.setText(nf.format(v_materai));
			total.setText(nf.format(v_total));
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.fill_executive, menu);
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
	
	/******* PASANGAN **********/
		
		if (pasangan.isChecked() &&
				(namapasangan.getText().toString().isEmpty() || 
				 etPasangan.getText().toString().isEmpty())) {
			flag++;
			namapasangan.setHintTextColor(Color.RED);
			etPasangan.setHintTextColor(Color.RED);
			
			UIErrorMessage = "Data pasangan harus dilengkapi";
			return;
		}
		/******* ANAK 1**********/
		if (anak1.isChecked() && 
				(namaanak1.getText().toString().isEmpty() ||
				 etAnak1.getText().toString().isEmpty())) {
			flag++;
			namaanak1.setHintTextColor(Color.RED);
			etAnak1.setHintTextColor(Color.RED);
				
			UIErrorMessage = "Data anak pertama harus dilengkapi";
			return;
		}	
		/******* ANAK 2**********/
		if (anak2.isChecked() &&
				(namaanak2.getText().toString().isEmpty() || 
				 etAnak2.getText().toString().isEmpty())) {
			flag++;
			namaanak2.setHintTextColor(Color.RED);
			etAnak2.setHintTextColor(Color.RED);
			
			UIErrorMessage = "Data anak kedua harus dilengkapi";
			return;
		}		

		/******* ANAK 3**********/
		if (anak3.isChecked() &&
				(namaanak3.getText().toString().isEmpty() ||
				etAnak3.getText().toString().isEmpty())) {
			flag++;
			namaanak3.setHintTextColor(Color.RED);
			etAnak3.setHintTextColor(Color.RED);
			
			UIErrorMessage = "Data anak ketiga harus dilengkapi";
			return;
		}		
		/*********** AHLI WARIS 1 ***********/
		
		if (namaahliwaris1.getText().toString().isEmpty() ||
			hubahliwaris1.getText().toString().isEmpty() ||
			alamatahliwaris1.getText().toString().isEmpty() ) {
			flag++;
			
			namaahliwaris1.setHintTextColor(Color.RED);
			hubahliwaris1.setHintTextColor(Color.RED);
			alamatahliwaris1.setHintTextColor(Color.RED);
			
			UIErrorMessage = "Data ahli waris ke-1 harus dilengkapi";
			
			return;
		}	
//		
//		/*********** AHLI WARIS 2 ***********/
//		
//		if (namaahliwaris2.getText().toString().isEmpty() ||
//			hubahliwaris2.getText().toString().isEmpty() ||
//			alamatahliwaris2.getText().toString().isEmpty() ) {
//			flag++;
//			
//			namaahliwaris2.setHintTextColor(Color.RED);
//			hubahliwaris2.setHintTextColor(Color.RED);
//			alamatahliwaris2.setHintTextColor(Color.RED);
//			
//			UIErrorMessage = "Data ahli waris ke-2 harus dilengkapi";
//			
//			return;
//		}	
//		
//		/*********** AHLI WARIS 3 ***********/
//		
//		if (namaahliwaris3.getText().toString().isEmpty() ||
//			hubahliwaris3.getText().toString().isEmpty() ||
//			alamatahliwaris3.getText().toString().isEmpty() ) {
//			flag++;
//			
//			namaahliwaris3.setHintTextColor(Color.RED);
//			hubahliwaris3.setHintTextColor(Color.RED);
//			alamatahliwaris3.setHintTextColor(Color.RED);
//			
//			UIErrorMessage = "Data ahli waris ke-3 harus dilengkapi";
//			
//			return;
//		}	
		
		if (etFromDate.getText().toString().isEmpty() ){
			flag++ ;
			etFromDate.setHintTextColor(Color.RED);
			UIErrorMessage = "Tanggal mulai harus diisi";
			return;
		}
		if (etToDate.getText().toString().isEmpty()){
			flag++ ;
			etToDate.setHintTextColor(Color.RED);
			UIErrorMessage = "Tanggal berakhir harus diisi";
			return;
		}
		if (!plan1.isChecked() && 
				!plan2.isChecked() &&
				!plan3.isChecked() && 
				!plan4.isChecked() ) {
			flag++;
			UIErrorMessage = "Pilih plan";
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

	public void btnNextClick(View v) {
		try{
			
			validasiNext();
			
			if(flag != 0)
				Utility.showCustomDialogInformation(FillExecutiveActivity.this, "Warning", 
						UIErrorMessage);		
			else{
				calculateAll();
				insertDB();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private void getPremi (){

		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String formattedDate = df.format(c.getTime());
		
		String plan;
		
		if(plan1.isChecked())
			plan = "1";
		else if(plan2.isChecked())
			plan = "2";
		else if(plan3.isChecked())
			plan = "3";
		else
			plan = "4";
		
		v_premi = getPremi("0", plan, "2");
		
		v_premi_pasangan = 0.00;
		v_premi_anak_1 = 0.00;
		v_premi_anak_2 = 0.00;
		v_premi_anak_3 = 0.00;

    	if (pasangan.isChecked()){
    		int umur2 = 0;
    		v_premi_pasangan = getPremi(String.valueOf(umur2), plan, "2");
    	}
    	
    	if (anak1.isChecked()){
    		int umur3 = 0;
    		v_premi_anak_1 = getPremi(String.valueOf(umur3), plan, "2");
    	}
    	
    	if (anak2.isChecked()){
    		int umur4 = 0;
    		v_premi_anak_2 = getPremi(String.valueOf(umur4), plan, "2");
    	}
    	
    	if (anak3.isChecked()){
    		int umur5 = 0;
    		v_premi_anak_3 = getPremi(String.valueOf(umur5), plan, "2");
    	}
	}
	
	private double getPremi(String pAge, String pPlan, String pType) {
		DBA_MASTER_EXECUTIVE_SAFE_RATE dba = null;
		Cursor c = null;
		double v = 0.0;
		
		try {
			dba = new DBA_MASTER_EXECUTIVE_SAFE_RATE(getBaseContext());
			dba.open();
			
			pType = "2";
			pAge  = "0";
			
			c = dba.getRate(pType, pPlan, pAge);
			
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
	
	private void getDisc() throws ParseException {
		v_discpct = Utility.parseDouble(discpct);
		v_disc = Utility.countDiscount(v_premi + v_premi_pasangan + v_premi_anak_1 + v_premi_anak_2 + v_premi_anak_3, v_discpct);
	}
	
	private void getPolis() {
		v_polis = 0;
	}
	
	private void getMaterai() {
		v_materai = Utility.countMaterai(v_premi + v_premi_pasangan + v_premi_anak_1 + v_premi_anak_2 + v_premi_anak_3);
	}
	
	private void getTotal() {
		v_total = Utility.countTotal(v_premi + v_premi_pasangan + v_premi_anak_1 + v_premi_anak_2 + v_premi_anak_3, v_polis, v_materai, v_disc);
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
	
	public void insertDB() {
		DBA_PRODUCT_EXECUTIVE_SAFE dba = null;
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
				dba = new DBA_PRODUCT_EXECUTIVE_SAFE(getBaseContext());
				dba2 = new DBA_PRODUCT_MAIN(getBaseContext());
				
				dba.open();
				dba2.open();
		
				String plan;
				String sPasangan = "0";
				String sAnak1 = "0";
				String sAnak2 = "0";
				String sAnak3 = "0";
				
				if(plan1.isChecked())
					plan = "1";
				else if(plan2.isChecked())
					plan = "2";
				else if(plan3.isChecked())
					plan = "3";
				else
					plan = "4";
				
				if (pasangan.isChecked())
					sPasangan = "1";
				if (anak1.isChecked())
					sAnak1 = "1";
				if (anak2.isChecked())
					sAnak2 = "1";
				if (anak3.isChecked())
					sAnak3 = "1";
			
				if (PRODUCT_ACTION.equals("NEW")) {	
					SPPA_ID = dba2.initialInsert(customerno.getText().toString(), 
							customername.getText().toString(), "EXECUTIVESAFE",
							nf.parse(premi.getText().toString()).doubleValue(),
							v_materai,
							nf.parse(polis.getText().toString()).doubleValue(),
							nf.parse(total.getText().toString()).doubleValue(),
							etFromDate.getText().toString(), 
							etToDate.getText().toString(),
							Utility.DateToString(Utility.GetToday(), "dd/MM/yyyy"),
							"","","","N","",
							nf.parse((discpct.getText().toString())).doubleValue(),
							nf.parse((disc.getText().toString())).doubleValue());
					
					 dba.initialInsert(SPPA_ID,
						namapasangan.getText().toString().toUpperCase(), etPasangan.getText().toString(), noktppasangan.getText().toString(),
						namaanak1.getText().toString().toUpperCase(), etAnak1.getText().toString(), noktpanak1.getText().toString().toUpperCase(),
						namaanak2.getText().toString().toUpperCase(), etAnak2.getText().toString(), noktpanak2.getText().toString().toUpperCase(),
						namaanak3.getText().toString().toUpperCase(), etAnak3.getText().toString(),noktpanak3.getText().toString().toUpperCase(),
						namaahliwaris1.getText().toString().toUpperCase(),  hubahliwaris1.getText().toString().toUpperCase(), alamatahliwaris1.getText().toString().toUpperCase(),
						namaahliwaris2.getText().toString().toUpperCase(),  hubahliwaris2.getText().toString().toUpperCase(), alamatahliwaris2.getText().toString().toUpperCase(),
						namaahliwaris3.getText().toString().toUpperCase(), hubahliwaris3.getText().toString().toUpperCase(), alamatahliwaris3.getText().toString().toUpperCase(),
						Integer.parseInt(plan), 
						etFromDate.getText().toString(),  etToDate.getText().toString(),
						nf.parse(premi.getText().toString()).doubleValue(), 
						nf.parse(polis.getText().toString()).doubleValue(), 
						nf.parse(total.getText().toString()).doubleValue(), 
						v_premi_pasangan, v_premi_anak_1, v_premi_anak_2, v_premi_anak_3,
						sPasangan, sAnak1, sAnak2, sAnak3,
						"EXECUTIVESAFE", customername.getText().toString());
				}
				else {
					dba2.nextInsert(SPPA_ID, customerno.getText().toString(), 
							customername.getText().toString(),
							nf.parse(premi.getText().toString()).doubleValue(),
							v_materai,
							nf.parse(polis.getText().toString()).doubleValue(),
							nf.parse(total.getText().toString()).doubleValue(),
							etFromDate.getText().toString(), etToDate.getText().toString(),
							Utility.DateToString(Utility.GetToday(), "dd/MM/yyyy"),
							nf.parse((discpct.getText().toString())).doubleValue(),
							nf.parse((disc.getText().toString())).doubleValue());
					
					dba.nextInsert(SPPA_ID,
							namapasangan.getText().toString().toUpperCase(), etPasangan.getText().toString(), noktppasangan.getText().toString(),
							namaanak1.getText().toString().toUpperCase(), etAnak1.getText().toString(), noktpanak1.getText().toString().toUpperCase(),
							namaanak2.getText().toString().toUpperCase(), etAnak2.getText().toString(), noktpanak2.getText().toString().toUpperCase(),
							namaanak3.getText().toString().toUpperCase(), etAnak3.getText().toString(),noktpanak3.getText().toString().toUpperCase(),
							namaahliwaris1.getText().toString().toUpperCase(),  hubahliwaris1.getText().toString().toUpperCase(), alamatahliwaris1.getText().toString().toUpperCase(),
							namaahliwaris2.getText().toString().toUpperCase(),  hubahliwaris2.getText().toString().toUpperCase(), alamatahliwaris2.getText().toString().toUpperCase(),
							namaahliwaris3.getText().toString().toUpperCase(), hubahliwaris3.getText().toString().toUpperCase(), alamatahliwaris3.getText().toString().toUpperCase(),
							Integer.parseInt(plan), 
							etFromDate.getText().toString(), etToDate.getText().toString(),
							nf.parse(premi.getText().toString()).doubleValue(), 
							nf.parse(polis.getText().toString()).doubleValue(), 
							nf.parse(total.getText().toString()).doubleValue(),
							v_premi_pasangan, v_premi_anak_1, v_premi_anak_2, v_premi_anak_3,
							sPasangan, sAnak1, sAnak2, sAnak3,
							"EXECUTIVESAFE", customername.getText().toString());
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
			case DATE_LAHIR_PASANGAN:
				return new DatePickerDialog(this, datePickerListenerPasangan, Y-20, M, D);
			case DATE_LAHIR_ANAK1:
				return new DatePickerDialog(this, datePickerListenerAnak1, Y-10, M, D);
			case DATE_LAHIR_ANAK2:
				return new DatePickerDialog(this, datePickerListenerAnak2, Y-10, M, D);
			case DATE_LAHIR_ANAK3:
				return new DatePickerDialog(this, datePickerListenerAnak3, Y-10, M, D);
		}
		return null;
	}
	
	private DatePickerDialog.OnDateSetListener datePickerListener  = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
			String theDate = Utility.setUIDate(selectedYear, selectedMonth, selectedDay);
			etFromDate.setText(theDate);
			etToDate.setText(Utility.GetTodayNextYearString(theDate));
			validasiTanggal();
		}
	};
	
	private DatePickerDialog.OnDateSetListener datePickerListenerNext = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
			etToDate.setText(Utility.setUIDate(selectedYear + 1, selectedMonth, selectedDay));
		}
	};
	
	private DatePickerDialog.OnDateSetListener datePickerListenerPasangan = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
		    etPasangan.setText(Utility.setUIDate(selectedYear, selectedMonth, selectedDay));
		    calculateAll();
		}
	};
	
	private DatePickerDialog.OnDateSetListener datePickerListenerAnak1 = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
		    etAnak1.setText(Utility.setUIDate(selectedYear, selectedMonth, selectedDay));
		}
	};
	
	private DatePickerDialog.OnDateSetListener datePickerListenerAnak2 = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
		    etAnak2.setText(Utility.setUIDate(selectedYear, selectedMonth, selectedDay));
		}
	};
	
	private DatePickerDialog.OnDateSetListener datePickerListenerAnak3 = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
		    etAnak3.setText(Utility.setUIDate(selectedYear, selectedMonth, selectedDay));
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
			Utility.showCustomDialogInformation(FillExecutiveActivity.this, "Warning", "Tanggal mulai minimal besok hari");
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
				DBA_PRODUCT_EXECUTIVE_SAFE dba2 = null;
				
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

					dba2 = new DBA_PRODUCT_EXECUTIVE_SAFE(context);
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
