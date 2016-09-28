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
import com.aca.amm.FillTravelActivity.DeleteSPPA;
import com.aca.amm.R.id;
import com.aca.amm.R.layout;
import com.aca.amm.R.menu;
import com.aca.database.DBA_MASTER_TRAVELSAFE_DOM_RATE;
import com.aca.database.DBA_PRODUCT_MAIN;
import com.aca.database.DBA_PRODUCT_TRAVEL_DOM;

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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Toast;

public class FillTravelDomActivity extends ControlNormalActivity {

	private ArrayList<HashMap<String, String>> custList;
	private Context context = null;
	private Bundle b;
	private NumberFormat nf;
	private String PRODUCT_ACTION;
	private long SPPA_ID;
	private int flag = 0;
	private Calendar c;

  	private double v_premi = 0, v_polis = 0, v_materai = 0, v_total = 0, v_discpct = 0, v_disc = 0;
    private double vMaxBenefit, vPremiDays, vPremiWeeks, vCCOD, vDCOD;
    private int vTotalDays, vTotalWeeks;

	private int plan;
	private EditText etFromDate, etToDate;
	private Spinner spTujuan;
	private static final int DATE_FROM_ID = 99;
	private static final int DATE_TO_ID = 100;
	
	private EditText kota, ahliwaris, hub;
	private EditText total, premi, polis, materai, tambahan, jmlhari, customerno, customername, discpct, disc;
	private RadioButton plan1, plan2;
	private RadioGroup rbgPlan;

	private String UIErrorMessage = "Lengkapi semua data";
	private double MAX_DISCOUNT = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_fill_travel_dom);
		
		context = FillTravelDomActivity.this;
		
		nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		
		spTujuan = (Spinner)findViewById(R.id.spinner1);
		Utility.BindTujuanPerjalanan(spTujuan, getBaseContext());
		
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
			
			if (Utility.getIsDiscountable(getBaseContext(), "06").equals("0"))
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
		customerno.setEnabled(false);
		customername.setEnabled(false);
		
		etFromDate.setEnabled(false);
		etToDate.setEnabled(false);
		
		kota .setEnabled(false);
		ahliwaris.setEnabled(false);
		hub.setEnabled(false);
		
		premi.setEnabled(false);
		discpct.setEnabled(false);
		disc.setEnabled(false);
		polis.setEnabled(false);
		tambahan.setEnabled(false);
		jmlhari .setEnabled(false);
		materai.setEnabled(false);
		total.setEnabled(false);
		
		plan1.setEnabled(false);
		plan2.setEnabled(false);
	
		spTujuan.setEnabled(false);
		
		(findViewById(R.id.btnChooseCustomer)).setEnabled(false);
		
//		change text color to black
		
		customerno.setTextColor(Color.BLACK);
		customername.setTextColor(Color.BLACK);
		
		etFromDate.setTextColor(Color.BLACK);
		etToDate.setTextColor(Color.BLACK);
		
		kota .setTextColor(Color.BLACK);
		ahliwaris.setTextColor(Color.BLACK);
		hub.setTextColor(Color.BLACK);
		
		premi.setTextColor(Color.BLACK);
		discpct.setTextColor(Color.BLACK);
		disc.setTextColor(Color.BLACK);
		polis.setTextColor(Color.BLACK);
		tambahan.setTextColor(Color.BLACK);
		jmlhari .setTextColor(Color.BLACK);
		materai.setTextColor(Color.BLACK);
		total.setTextColor(Color.BLACK);
		
		
	}

	private void LoadDB() {

		DBA_PRODUCT_MAIN dbm = null;
		DBA_PRODUCT_TRAVEL_DOM dba = null;
		
		Cursor cm  = null;
		Cursor c = null;
		
		try{
			
			dbm = new DBA_PRODUCT_MAIN(getBaseContext());
			dba = new DBA_PRODUCT_TRAVEL_DOM(getBaseContext());
			
			dbm.open();
			cm = dbm.getRow(SPPA_ID);
			cm.moveToFirst();
			
			dba.open();
			c = dba.getRow(SPPA_ID);
			c.moveToFirst();
			
			customerno.setText(cm.getString(1));
			customername.setText(cm.getString(2));
			
		
			for (int i = 0; i < spTujuan.getCount(); i++) {              
//	              long itemIdAtPosition1 = spTujuan.getItemIdAtPosition(i);
				String item = spTujuan.getItemAtPosition(i).toString();
				
				if (item.equalsIgnoreCase(c.getString(2))) {
	            	  spTujuan.setSelection(i);
	               break;
	              }
			}
			
			kota.setText(c.getString(3));
			ahliwaris.setText(c.getString(4));
			hub.setText(c.getString(5));
			etFromDate.setText(c.getString(6));
			etToDate.setText(c.getString(7));
			
			if(c.getInt(8) == 0)
				plan1.setChecked(true);
			else
				plan2.setChecked(true);
			
			jmlhari.setText(nf.format(c.getDouble(9)));
			tambahan.setText(nf.format(c.getDouble(10)));
			premi.setText(nf.format(c.getDouble(11)));
			discpct.setText(nf.format(cm.getDouble(23)));
			disc.setText(nf.format(cm.getDouble(24)));
			polis.setText(nf.format(c.getDouble(12)));
			total.setText(nf.format(c.getDouble(13)));
			
			if (cm.getString(20).equals("R")){
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
		
		etFromDate = (EditText)findViewById(R.id.txtTglBerangkat);
		etToDate  = (EditText)findViewById(R.id.txtTglKembali);
		
		kota = (EditText)findViewById(R.id.txtKotaTujuan);
		ahliwaris = (EditText)findViewById(R.id.txtAhliWaris);
		hub = (EditText)findViewById(R.id.txtHubungan);
		
		premi = (EditText)findViewById(R.id.txtPremi);
		discpct = (EditText)findViewById(R.id.txtDiscountPct);
		disc = (EditText)findViewById(R.id.txtDiscount);
		polis = (EditText)findViewById(R.id.txtBiayaPolis);
		tambahan = (EditText)findViewById(R.id.txtTambahanPerMinggu);
		jmlhari = (EditText)findViewById(R.id.txtJmlHariDipertanggungkan);
		materai = (EditText)findViewById(R.id.txtMaterai);
		materai.setText("3,000.00");
		total = (EditText)findViewById(R.id.txtTotal);
		
		plan1 = (RadioButton)findViewById(R.id.rboPlan1);
		plan2 = (RadioButton)findViewById(R.id.rboPlan2);
	
		rbgPlan = (RadioGroup)findViewById(R.id.rbgPlan);
	}

	private void RegisterListener(){
		etToDate.setKeyListener(null);
		etToDate.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus)
					showDialog(DATE_TO_ID);
			}
		});
		etToDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DATE_TO_ID);
			}
		});
		
		etFromDate.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View arg0, boolean hasFocus) {
				if(hasFocus)
					showDialog(DATE_FROM_ID);
//				else { 
//					((Button)findViewById(R.id.btnNext)).setEnabled(true);
//					etFromDate.setTextColor(Color.BLACK);
//					boolean flag = Utility.validasiEffDate(etFromDate.getText().toString(), 
//										Utility.GetTodayString(), getBaseContext());
//					
//					if (!flag) {
//						etFromDate.setTextColor(Color.RED);
//						Utility.showCustomDialogInformation(FillTravelDomActivity.this, "Warning", "Tanggal minimal hari ini");
//						((Button)findViewById(R.id.btnNext)).setEnabled(false);
//					}
//				}
			}
		});
		etFromDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showDialog(DATE_FROM_ID);
			}
		});
		
		rbgPlan.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				if(!customerno.getText().toString().isEmpty() && !etFromDate.getText().toString().isEmpty() && !etToDate.getText().toString().isEmpty()){
					try {
						calculateAll();
					}
					catch (Exception e) {
						e.printStackTrace();
					}
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
		getMenuInflater().inflate(R.menu.fill_travel_dom, menu);
		return true;
	}
	
	public void btnHomeClick(View v){
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
		
		if (customerno.getText().toString().isEmpty()){
			flag++ ;
			UIErrorMessage = "Pilih customer";
			return;
		}
		
		if (customername.getText().toString().isEmpty()){
			flag++ ;
			customername.setHintTextColor(Color.RED);
			UIErrorMessage = "Pilih customer";
			return;
		}
	
		if (kota.getText().toString().isEmpty() ){
			flag++ ;
			kota.setHintTextColor(Color.RED);
			UIErrorMessage = "Kota tujuan harus diisi";
			return;
		}
		if (ahliwaris.getText().toString().isEmpty() ){
			flag++ ;
			ahliwaris.setHintTextColor(Color.RED);
			UIErrorMessage = "Nama ahli waris harus diisi";
			return;
		}
		if (hub.getText().toString().isEmpty() ){
			flag++ ;
			hub.setHintTextColor(Color.RED);
			UIErrorMessage = "Hubungan tertanggung dengan ahli waris harus diisi";
			return;
		}
	
		if (jmlhari.getText().toString().isEmpty() ){
			flag++ ;
			jmlhari.setHintTextColor(Color.RED);
			UIErrorMessage = "Jumlah hari tidak bisa dihitung. Hubungi IT ACA";
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
		if (premi.getText().toString().isEmpty()){
			flag++ ;
			premi.setHintTextColor(Color.RED);
			UIErrorMessage = "Premi tidak bisa dihitung. Hubungi IT ACA";
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
				Utility.showCustomDialogInformation(FillTravelDomActivity.this, "Warning", 
						UIErrorMessage);	
			else{
				calculateAll();
				insertDB();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private void calculateAll() throws ParseException {
		getPremi();
		getDiscount();
		getPolis();
		getMaterai();
		getTotal();
	
		tambahan.setText(nf.format(vPremiWeeks));
        jmlhari.setText(nf.format(vPremiDays));
	
        materai.setText(nf.format(v_materai));
        disc.setText(nf.format(v_disc));
		polis.setText(nf.format(v_polis));
        premi.setText(nf.format(v_premi));
		total.setText(nf.format(v_total));
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
		DBA_PRODUCT_TRAVEL_DOM dba = new DBA_PRODUCT_TRAVEL_DOM(getBaseContext());
		DBA_PRODUCT_MAIN dba2 = new DBA_PRODUCT_MAIN(getBaseContext());
		
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
				dba = new DBA_PRODUCT_TRAVEL_DOM(getBaseContext());
				dba2 = new DBA_PRODUCT_MAIN(getBaseContext());
				
				dba.open();
				dba2.open();
				
				if(plan1.isChecked()) plan = 0;
				else plan = 1;
				
				if (PRODUCT_ACTION.equals("NEW"))
				{
					SPPA_ID  = dba2.initialInsert(customerno.getText().toString(), 
							customername.getText().toString(), "TRAVELDOM",
							nf.parse(premi.getText().toString()).doubleValue(),
							v_materai,
							nf.parse(polis.getText().toString()).doubleValue(),
							nf.parse(total.getText().toString()).doubleValue(),
							etFromDate.getText().toString(), etToDate.getText().toString(),
							Utility.DateToString(Utility.GetToday(), "dd/MM/yyyy"),
							"","","","N","",
							nf.parse((discpct.getText().toString())).doubleValue(),
							nf.parse((disc.getText().toString())).doubleValue());
					
					dba.initialInsert(SPPA_ID, 
						(spTujuan.getSelectedItem().toString()), 
						kota.getText().toString().toUpperCase(), 
						plan, ahliwaris.getText().toString().toUpperCase(), hub.getText().toString().toUpperCase(), 
						etFromDate.getText().toString().toUpperCase(), etToDate.getText().toString().toUpperCase(), 
						nf.parse(jmlhari.getText().toString()).doubleValue(),
						nf.parse(tambahan.getText().toString()).doubleValue(), 
						nf.parse(premi.getText().toString()).doubleValue(),
						nf.parse(polis.getText().toString()).doubleValue(), 
						nf.parse(total.getText().toString()).doubleValue(),
						"TRAVELDOM", customername.getText().toString(),
						vCCOD, vDCOD, vPremiDays, vPremiWeeks, vMaxBenefit, vTotalDays, vTotalWeeks);		
				}
				else{
					
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
							(spTujuan.getSelectedItem().toString()), 
							kota.getText().toString().toUpperCase(), 
							plan, ahliwaris.getText().toString().toUpperCase(), hub.getText().toString().toUpperCase(), 
							etFromDate.getText().toString().toUpperCase(), etToDate.getText().toString().toUpperCase(), 
							nf.parse(jmlhari.getText().toString()).doubleValue(),
							nf.parse(tambahan.getText().toString()).doubleValue(), 
							nf.parse(premi.getText().toString()).doubleValue(),
							nf.parse(polis.getText().toString()).doubleValue(), 
							nf.parse(total.getText().toString()).doubleValue(),
							"TRAVELDOM", customername.getText().toString(),
							vCCOD, vDCOD, vPremiDays, vPremiWeeks, vMaxBenefit, vTotalDays, vTotalWeeks);		
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
	
	private void getTotal() {
		v_total = Utility.countTotal(v_premi, v_polis, v_materai, v_disc);
	}

	private void getDiscount() throws ParseException {
		v_discpct = Utility.parseDouble(discpct);
		v_disc = v_premi * v_discpct / 100;
	}
	
	private void getMaterai() {
//		v_materai = 3000.00;
		v_materai = 0.00;
	}

	private void getPolis() {
		v_polis =  0;
	}
	
	private void getPremi () {
		
		DBA_MASTER_TRAVELSAFE_DOM_RATE dba = null;
		
		Cursor c = null;
		Cursor c_week = null;
		
		String area;
		
	    String Coverage;
	    String CCOD ;
	    String DCOD ;
	    String Plan ;
	    
	    int Duration ;
	    int DurationCode;
	    double Week ;
	    int Counter;
	
	    Week = 0;
	    Duration = 0;
	    Counter = 1;

	    try {

	        Duration = Utility.DATEDIFF(etFromDate.getText().toString(), etToDate.getText().toString()) ;
	        //Duration += 1;
        
	        if(plan1.isChecked()) Plan = "0"; else Plan = "1";
        
	        if (Plan.equals("0")) {
	        	Coverage = "481" ;
	        	CCOD = "481" ;
	        }
	        else {
	        	Coverage = "482";
	        	CCOD = "482";
	        }
        
	        if (Duration <= 4 ){
	        	DurationCode = 1;
	        	DCOD = "1";
	        }
	        else if (Duration <= 11 ){
	        	DurationCode = 2;
	        	DCOD = "2" ;
	        }
	        else if (Duration <= 20){ 
	        	DurationCode = 3;
	        	DCOD = "3";
	        }
	        else if (Duration <= 31 ){
	        	DurationCode = 4;
	        	DCOD = "4";
	        }
	        else {
	        	DurationCode = 4 ;
	        	DCOD = "5";
	        	
	        	Week = (Double)((Duration - 31) / 7.0) ;
	        	Week = Math.ceil(Week);
	        	Duration = 31 ;
	        }
        
	        try
	        {
	        	dba = new DBA_MASTER_TRAVELSAFE_DOM_RATE(getBaseContext());
	        	dba.open();
			
	        	area = "4";
			
	        	c = dba.getRate(area, Coverage, String.valueOf(DurationCode));
			
	        	if (Week > 0) {
	        		c_week = dba.getRate(area, Coverage, "5");
					   
		            if (!c_week.moveToFirst()) 
		                vPremiWeeks = 0;
		            else
		                vPremiWeeks = c_week.getDouble(3) * Week * Counter;
	        	}
	        	else
	        		vPremiWeeks = 0;
		
	        	if (!c.moveToFirst()){
				    vPremiDays = 0;
		            vMaxBenefit = 0.00;
		        }
		        else{
		            vPremiDays = c.getDouble(3) * Counter;
		            vMaxBenefit = c.getDouble(4);
		        }
	        	
		        vTotalDays = Duration;
		        vTotalWeeks = (int) Week;
		        v_premi = (vPremiDays + vPremiWeeks) / Counter;
		        
		        vDCOD = Double.parseDouble(DCOD);
		        vCCOD = Double.parseDouble(CCOD);
	       
	        }
	        catch (Exception ex) {
	        	ex.printStackTrace();
	        }
	        finally{
	        	
	        	if (c != null)
	        		c.close();
	        	
	        	if (c_week != null)
	        		c_week.close();
	        	
	        	if (dba != null)
	        		dba.close();
	        }
        
	    }
	    catch (Exception ex) {
	    	ex.printStackTrace();
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
				return new DatePickerDialog(this, datePickerListener, Y, M, D);
			case DATE_TO_ID:
				return new DatePickerDialog(this, datePickerListenerNext, Y, M, D + 1);
		}
		return null;
	}
	
	private DatePickerDialog.OnDateSetListener datePickerListener  = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
			String theDate = Utility.setUIDate(selectedYear, selectedMonth, selectedDay);
			etFromDate.setText(theDate);
			etToDate.setText(Utility.GetTomorrowString(theDate));
			
			
			validasiTanggalAwal();
			
			
			try {
				if (!TextUtils.isEmpty(etFromDate.getText()) && !TextUtils.isEmpty(etToDate.getText()))
					calculateAll();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
	
	private DatePickerDialog.OnDateSetListener datePickerListenerNext = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
			etToDate.setText(Utility.setUIDate(selectedYear, selectedMonth, selectedDay));
			
			validasiTanggalAkhir();
			
			try {
				if (!TextUtils.isEmpty(etFromDate.getText()) && !TextUtils.isEmpty(etToDate.getText()))
					calculateAll();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Back();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onBackPressed() {
		Back();
	}
	
	private void validasiTanggalAwal () {
		((Button)findViewById(R.id.btnNext)).setEnabled(true);
		etFromDate.setTextColor(Color.BLACK);
		etToDate.setTextColor(Color.BLACK);
		boolean flag = Utility.validasiEffDate(etFromDate.getText().toString(), 
							Utility.GetTodayString(), getBaseContext());
		
		if (!flag) {
			etFromDate.setTextColor(Color.RED);
			Utility.showCustomDialogInformation(FillTravelDomActivity.this, "Warning", "Tanggal mulai minimal hari hari");
			((Button)findViewById(R.id.btnNext)).setEnabled(false);
		}
	}
	
	private void validasiTanggalAkhir () {
		((Button)findViewById(R.id.btnNext)).setEnabled(true);
		etToDate.setTextColor(Color.BLACK);
		boolean flag = Utility.validasiEffDate(etToDate.getText().toString(),
												etFromDate.getText().toString(),
												getBaseContext());
												
		if (!flag) {
			etToDate.setTextColor(Color.RED);
			Utility.showCustomDialogInformation(FillTravelDomActivity.this, "Informasi",
					"Tanggal kembali tidak sesuai");
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
				DBA_PRODUCT_TRAVEL_DOM dba2 = null;
				
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
					
					dba2 = new DBA_PRODUCT_TRAVEL_DOM(context);
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
