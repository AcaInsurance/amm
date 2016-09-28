package com.aca.amm;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.aca.amm.GetExchangeRate.GetexchangerateInterface;
import com.aca.amm.R;
import com.aca.database.DBA_MASTER_PRODUCT_SETTING;
import com.aca.database.DBA_MASTER_VESSEL_DETAIL;
import com.aca.database.DBA_PRODUCT_CARGO;
import com.aca.database.DBA_PRODUCT_MAIN;

import android.R.anim;
import android.R.layout;
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
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.SumPathEffect;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("DefaultLocale")
public class FillCargoActivity extends ControlNormalActivity implements GetexchangerateInterface {

ArrayList<HashMap<String, String>> custList ;
	
	private Context context = null;
	
	private Bundle b;
	private NumberFormat nf, nf_asri;
	private String PRODUCT_ACTION;
	private long SPPA_ID;
	private int flag = 0;
	private Calendar c;

    private double v_rate = 0, v_tsi = 0, v_premi = 0, v_discpct = 0, v_disc = 0, v_polis = 0, v_materai = 0, v_total = 0;
	private Spinner spPolicyType, spCurrency;
	private static final int DATE_FROM_ID = 99;
	
	private EditText  vesselweight, vesselyear, printedvesselname, printedvesselweight, printedvesselyear;
	private EditText voyageno, blno, lcno, interestdetails, voyagefrom, voyageto, transhipment;
	private EditText etd, si, premi, polis, materai, total, rate, customerno, customername, discpct, disc;
	private EditText exchangePremi;
	private TextView exchangeRate;
	
	private AutoCompleteTextView vesselName;
	private Spinner vesselType;
	
	private String UIErrorMessage = "Lengkapi semua data.";
	private double MAX_DISCOUNT = 0;
	private boolean enableEdit = true;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_fill_cargo);
		
		context = FillCargoActivity.this;
		
		nf = NumberFormat.getInstance();
//		nf.setMaximumFractionDigits(2);
//		nf.setMinimumFractionDigits(2);
//		
		nf_asri = NumberFormat.getInstance();
		nf_asri.setMaximumFractionDigits(3);
		nf_asri.setMinimumFractionDigits(3);
		
		
		InitControls();
		
		try{
			
			Intent i = getIntent();
			b = i.getExtras();
			
			PRODUCT_ACTION = b.getString("PRODUCT_ACTION");
			
			if (PRODUCT_ACTION.equals("EDIT") || PRODUCT_ACTION.equals("VIEW")) {
				SPPA_ID = b.getLong("SPPA_ID");
				enableEdit = false;
				RegisterListener();
				LoadDB();
			}
			else if (PRODUCT_ACTION.equals("VIEW.UNPAID")) {
				SPPA_ID = b.getLong("SPPA_ID");
				enableEdit = false;
			
				LoadDB();
				disableView();
			}
			else {
				findViewById(R.id.btnDelete)
				.setVisibility(View.GONE);
				RegisterListener();
			}
			
			if (b.getString("CUSTOMER_NO") != null) {
				customerno.setText(b.getString("CUSTOMER_NO"));
				customername.setText(b.getString("CUSTOMER_NAME"));
			}
			 
			MAX_DISCOUNT = Utility.getMaxDiscount(getBaseContext(), "11");
			
			if (Utility.getIsDiscountable(getBaseContext(), "13").equals("0"))
			{
				disc.setEnabled(false);
				discpct.setEnabled(false);
			}
		}
		catch(Exception e){	
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fill_cargo, menu);
		return true;
	}
	
	private void LoadDB() {

				
		DBA_PRODUCT_MAIN dbm = null;
		DBA_PRODUCT_CARGO dba = null;
		
		Cursor cm = null;
		Cursor c = null;
		
		try{
			
			dbm = new DBA_PRODUCT_MAIN(getBaseContext());
			dba = new DBA_PRODUCT_CARGO(getBaseContext());
			
			dbm.open();
			cm = dbm.getRow(SPPA_ID);
			cm.moveToFirst();
			
			dba.open();
			c = dba.getRow(SPPA_ID);
			c.moveToFirst();
			
			//
			customerno.setText(cm.getString(1));
			customername.setText(cm.getString(2));
			
			SpinnerGenericAdapter a;
			
			Log.i(TAG, "::LoadDB:" + "load db");
			a = (SpinnerGenericAdapter)vesselType.getAdapter();
			vesselType.setSelection(a.getItemId(c.getString(30)));
			

			Log.i(TAG, "::LoadDB:" + "vessel code : " + c.getString(30));
			Log.i(TAG, "::LoadDB:" + "vessel type id : " + a.getItemId(c.getString(30)));
			
			Log.i(TAG, "::LoadDB:" + " vessel name :" + c.getString(8));
			vesselName.setText(c.getString(8));
			
			vesselName.setTag(R.string.vessel_code, c.getString(7));
//			vesselName.setTag(R.string.vessel_name, c.getString(8));
			
			vesselweight.setText(c.getString(13));
			vesselyear.setText(c.getString(14));  
			
			voyageno.setText(c.getString(21)); 
			
			etd.setText(c.getString(22));
			
			blno.setText(c.getString(5));
			lcno.setText(c.getString(6)); 
			interestdetails.setText(c.getString(3)); 
			voyagefrom.setText(c.getString(17)); 
			voyageto.setText(c.getString(18)); 
			transhipment.setText(c.getString(19));

			a = (SpinnerGenericAdapter)spPolicyType.getAdapter();
			spPolicyType.setSelection(a.getItemId(c.getString(20)));
			
			
			a = (SpinnerGenericAdapter)spCurrency.getAdapter();
			spCurrency.setSelection(a.getItemId(c.getString(11)));

			exchangeRate.setText(nf.format(c.getDouble(12)));
			si.setText(nf.format(c.getDouble(2)));
			rate.setText(nf_asri.format(c.getDouble(24)));
			premi.setText(nf.format(c.getDouble(25)));
			exchangePremi.setText(nf.format(c.getDouble(25) * c.getDouble(12)));
			discpct.setText(nf.format(cm.getDouble(23)));
			disc.setText(nf.format(cm.getDouble(24)));
			polis.setText(nf.format(c.getDouble(26)));
			materai.setText(nf.format(0));
			total.setText(nf.format(c.getDouble(27)));
			
			
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
	
		vesselType  = (Spinner)findViewById(R.id.spinVesselType);		
		vesselName = (AutoCompleteTextView)findViewById(R.id.txtVesselName);		

		spCurrency = (Spinner)findViewById(R.id.spinnerCurrency);
		spPolicyType = (Spinner)findViewById(R.id.spinnerPolicyType);
		
		Utility.BindCargoPolicyType(spPolicyType, getBaseContext(), this);
		Utility.BindVesselType(vesselType, getBaseContext(), this);		
		Utility.BindCargoCurrency(spCurrency, getBaseContext(), this);
						
		
		vesselweight = (EditText)findViewById(R.id.txtVesselWeight);
		vesselyear = (EditText)findViewById(R.id.txtVesselYear);
		
		voyageno = (EditText)findViewById(R.id.txtVoyageNo);
		blno = (EditText)findViewById(R.id.txtBLNo);
		lcno = (EditText)findViewById(R.id.txtLCNo);
		interestdetails = (EditText)findViewById(R.id.txtInterestDetails);
		voyagefrom = (EditText)findViewById(R.id.txtVoyageFrom);
		voyageto = (EditText)findViewById(R.id.txtVoyageTo);
		transhipment = (EditText)findViewById(R.id.txtTranshipment);
		voyageno = (EditText)findViewById(R.id.txtVoyageNo);
		
		etd = (EditText)findViewById(R.id.txtfromdate);
		
		si = (EditText)findViewById(R.id.txtSI);
		premi = (EditText)findViewById(R.id.txtPremi);
		discpct = (EditText)findViewById(R.id.txtDiscountPct);
		disc = (EditText)findViewById(R.id.txtDiscount);
		rate = (EditText)findViewById(R.id.txtRate);
		materai = (EditText)findViewById(R.id.txtMaterai);
		polis = (EditText)findViewById(R.id.txtPolicyCost);
		total = (EditText)findViewById(R.id.txtTotal);
		exchangeRate = (TextView)findViewById(R.id.txtExchangeRate);
		exchangePremi = (EditText)findViewById(R.id.txtExchangePremi);
		
		
		vesselweight.setEnabled(false);
		vesselyear.setEnabled(false);
	}

	private void disableView () {
		customerno.setEnabled(false);
		customername .setEnabled(false);
	
		vesselType.setEnabled(false);
		vesselName .setEnabled(false);		
		vesselweight .setEnabled(false);
		vesselyear .setEnabled(false);
		
		spCurrency.setEnabled(false);
		spPolicyType.setEnabled(false);
		
		voyageno .setEnabled(false);
		blno.setEnabled(false);
		lcno.setEnabled(false);
		interestdetails.setEnabled(false);
		voyagefrom.setEnabled(false);
		voyageto .setEnabled(false);
		transhipment.setEnabled(false);
		voyageno.setEnabled(false);		
		etd .setEnabled(false);		
		si.setEnabled(false);
		rate.setEnabled(false);

		(findViewById(R.id.btnChooseCustomer)).setEnabled(false);
		
		customerno.setTextColor(Color.BLACK);
		customername .setTextColor(Color.BLACK);
	
//		vesselType.setTextColor(Color.BLACK);
		vesselName .setTextColor(Color.BLACK);
		
		vesselweight .setTextColor(Color.BLACK);
		vesselyear .setTextColor(Color.BLACK);
		
		voyageno .setTextColor(Color.BLACK);
		blno.setTextColor(Color.BLACK);
		lcno.setTextColor(Color.BLACK);
		interestdetails.setTextColor(Color.BLACK);
		voyagefrom.setTextColor(Color.BLACK);
		voyageto .setTextColor(Color.BLACK);
		transhipment.setTextColor(Color.BLACK);
		voyageno.setTextColor(Color.BLACK);		
		etd .setTextColor(Color.BLACK);		
		si.setTextColor(Color.BLACK);
		

	}
	
	@Override
	public void GetexchangerateListener(String exRate) {
		Log.i(TAG, "::GetexchangerateListener: exchange rate : " + exRate);
		if (exRate.equals("0"))
			exRate = "1";
		
		exchangeRate.setText(exRate);
	}

	
	
	private void RegisterListener(){
 
		
		exchangeRate.addTextChangedListener(new TextWatcher() {
			
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
				 
				try { 
					 if (!rate.getText().toString().isEmpty() && !si.getText().toString().isEmpty())
						 calculateAll();
					 
					 si.setTextColor(Color.BLACK);
				} catch (ParseException e) { 
					exchangePremi.setText("0");
					e.printStackTrace();
				}
			}
		});
		
		spCurrency.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapter, View view,
					int position, long arg3) {
				String currency = ((SpinnerGenericItem)spCurrency.getSelectedItem()).getCode();
				Log.i(TAG, "::onItemSelected:" + "currency : " + currency);
				
				
				GetExchangeRate ws = new GetExchangeRate(FillCargoActivity.this, currency);
				ws.mcallback = FillCargoActivity.this;
				ws.execute();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) { 
				
			}
		});
		
		
		vesselType.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				enableEdit = true;
				return false;
			}
		});
		
		vesselType.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapter, View view,
					int position, long id) {
				
				String code = ((SpinnerGenericItem)vesselType.getSelectedItem()).getCode();
				
				Log.i(TAG, "::onItemSelected:" + "vessel type code " + code);
				
				BindListView(code);
				
				if (code.equalsIgnoreCase("TRK")) {
					((TextView) findViewById(R.id.vesselType)).setText("Conveyance Type");
					((TextView) findViewById(R.id.vesselName)).setText("Plat No.");
					
					
			
						blno.setText(" ");
						voyageno.setText(" ");					
				
					
					blno.setEnabled(false);
					blno.setBackground(getResources().getDrawable(R.drawable.edittextdisabled));
					voyageno.setEnabled(false);			
					voyageno.setBackground(getResources().getDrawable(R.drawable.edittextdisabled));
					
					
//					3 = LAND IN TRANSIT
					spPolicyType.setSelection(3); 
					spPolicyType.setEnabled(false);
					spPolicyType.setBackground(getResources().getDrawable(R.drawable.edittextdisabled));
				}
				else if (code.equalsIgnoreCase("TRN")){
					((TextView) findViewById(R.id.vesselType)).setText("Conveyance Type");
					((TextView) findViewById(R.id.vesselName)).setText("Train Name & No.");
				

					
						blno.setText(" ");
						voyageno.setText(" ");					
				
					
					blno.setEnabled(false);
					blno.setBackground(getResources().getDrawable(R.drawable.edittextdisabled));
					
					voyageno.setEnabled(false);		
					voyageno.setBackground(getResources().getDrawable(R.drawable.edittextdisabled));
					

					spPolicyType.setEnabled(true);
					spPolicyType.setBackground(getResources().getDrawable(R.drawable.edittext));
				}
				else {
					((TextView) findViewById(R.id.vesselType)).setText("Vessel Type");				
					((TextView) findViewById(R.id.vesselName)).setText("Vessel Name");
					

					if (enableEdit) {
						blno.setText("");
						voyageno.setText("");					
					}
					
					blno.setEnabled(true);
					blno.setBackground(getResources().getDrawable(R.drawable.edittext));
					
					voyageno.setEnabled(true);
					voyageno.setBackground(getResources().getDrawable(R.drawable.edittext));
					
					spPolicyType.setEnabled(true);
					spPolicyType.setBackground(getResources().getDrawable(R.drawable.edittext));
					
				}
				

				if (enableEdit) {
					vesselName.setText("");
					vesselweight.setText("");
					vesselyear.setText("");
				}
			
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		vesselName.addTextChangedListener(new TextWatcher() {
			
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
				vesselName.setTextColor(Color.BLACK);			
			}
		});
		
		vesselName.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long id) {
				vesselName.setTextColor(Color.BLACK);			
				
				DBA_MASTER_VESSEL_DETAIL dba = new DBA_MASTER_VESSEL_DETAIL(FillCargoActivity.this);
				Cursor c = null;
				try {
					String name = (String) adapter.getItemAtPosition(position);
					
					Log.i(TAG, "::onItemSelected:" + "view: " + name);
					
					dba.open();
					c = dba.getByVesselName(name);
					c.moveToFirst();

//					vesselName.setTag(c.getString(3)); // VESSEL TYPE
					vesselName.setTag(R.string.vessel_code, c.getString(3));  //vessel type
//					vesselName.setTag(R.string.vessel_name, name);
					
					vesselweight.setText(c.getString(6));
					vesselyear.setText(c.getString(8));
				

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (dba != null)
						dba.close();

					if (c != null)
						c.close();

				}
				
			}});
	
				
		etd.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View arg0, boolean hasFocus) {
				if(hasFocus)
					showDialog(DATE_FROM_ID);
				else { 
//					((Button)findViewById(R.id.btnNext)).setEnabled(true);
//					etd.setTextColor(Color.BLACK);
//					boolean flag = Utility.validasiEffDate(etd.getText().toString(), 
//										Utility.GetTomorrowString(Utility.GetTodayString()), getBaseContext());
//					
//					if (!flag) {
//						etd.setTextColor(Color.RED);
//						Utility.showCustomDialogInformation(FillCargoActivity.this, "Warning", "Tanggal mulai minimal besok hari");
//						((Button)findViewById(R.id.btnNext)).setEnabled(false);
//					}
				}
			}
		});
		
		etd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showDialog(DATE_FROM_ID);
			}
		});
		 
		si.addTextChangedListener(new TextWatcher() {
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
			
				si.setTextColor(Color.BLACK);
				try {
				   if (!s.toString().equals(current)) {
					   si.removeTextChangedListener(this);     
		                
		                String replaceable = String.format("[%s,.\\s]", "Rp. ");
		                String cleanString = s.toString().replaceAll(replaceable, "");  
		            		                
		                double parsed;
		                try {
		                    parsed = Double.parseDouble(cleanString);
		                } catch (NumberFormatException e) {
		                    parsed = 0.00;
		                }        
		                String formatted = nf.format(parsed);
		                 
		                Log.i(TAG, "::afterTextChanged:" + "length : " + formatted.length());
		                
		                current = formatted;
		                si.setText(formatted);
		                si.setSelection(formatted.length());
		                si.addTextChangedListener(this);
		                
		                if (!rate.getText().toString().isEmpty())
		                	calculateAll();

		                //validasiNilaiTSI();
		                
		            }                           
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		

//		si.addTextChangedListener(new TextWatcher() {
//			private String current = "";
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before, int count) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void afterTextChanged(Editable s) {
//				// TODO Auto-generated method stub
//			
//				si.setTextColor(Color.BLACK);
//				try {
//				   if (!s.toString().equals(current)) {
//					   si.removeTextChangedListener(this);     
//		                
//		                String replaceable = String.format("[%s,.\\s]", "Rp. ");
//		                String cleanString = s.toString().replaceAll(replaceable, "");  
//		            		                
//		                double parsed;
//		                try {
//		                    parsed = Double.parseDouble(cleanString);
//		                } catch (NumberFormatException e) {
//		                    parsed = 0.00;
//		                }        
//		                String formatted = nf.format(parsed);
//		                 
//		                current = formatted;
//		                si.setText(formatted);
//		                si.setSelection(formatted.length());
//		                si.addTextChangedListener(this);
//		                
//		                calculateAll();
////		                validasiNilaiTSI();
//		                
//		            }                           
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//
//			}
//		});
		
		rate.addTextChangedListener(new TextWatcher() {
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
			
//				rate.setTextColor(Color.BLACK);
				try {
				   if (!s.toString().equals(current)) {
//					   rate.removeTextChangedListener(this);     
//		                
//		                String replaceable = String.format("[%s,.\\s]", "Rp. ");
//		                String cleanString = s.toString().replaceAll(replaceable, "");  
//		            		                
//		                double parsed;
//		                try {
//		                    parsed = Double.parseDouble(cleanString);
//		                } catch (NumberFormatException e) {
//		                    parsed = 0.00;
//		                }        
//		                String formatted = nf.format(parsed);
//		                 
//		                current = formatted;
//		                rate.setText(formatted);
//		                rate.setSelection(formatted.length());
//		                rate.addTextChangedListener(this);
		                
		                if (!rate.getText().toString().isEmpty() && !si.getText().toString().isEmpty())
		                	calculateAll();

		                //validasiNilaiTSI();
		                
		            }                           
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		
		
		
//		rate.setOnFocusChangeListener(new OnFocusChangeListener() {
//			@Override
//			public void onFocusChange(View v, boolean hasFocus) {
//				if(hasFocus) {
////					rate.setText(Utility.removeComma(rate.getText().toString()));
//				}
//				else {
//					try {
//						if (rate.getText().toString().isEmpty())
//							return ;
////						
////						rate.setText(nf.format(nf.parse(rate.getText().toString())));
//						calculateAll();
//						
//					} catch (ParseException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		});
		
		
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
	

	private void BindListView(String code) {

		DBA_MASTER_VESSEL_DETAIL db = null;
		Cursor c = null;
		
		
		try {
			
			db = new DBA_MASTER_VESSEL_DETAIL(this);
			db.open();
			c = db.getByVesselCode(code);
			c.moveToFirst();
	
			ArrayList<String> arrList = new ArrayList<String>();
			
		    do{
		    	arrList.add(c.getString(4));
		    }while(c.moveToNext()) ;
		    
		    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, 
		    		android.R.layout.simple_dropdown_item_1line, 
		    		arrList);
		    
//		    arrayAdapter.setDropDownViewResource(R.layout.row_item);		    
//		    TextView tView = (TextView)LayoutInflater.from(getBaseContext()).inflate(android.R.layout.simple_dropdown_item_1line, false);

		    vesselName.setAdapter( arrayAdapter);
		    
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			if (db != null)
				db.close();
			
			if (c != null)
				c.close();
		}
		
	
	                        
	
		
	}
	
	private double getexchangeratepremi () { 
		try {
			if (premi.getText().toString().isEmpty())
				return 0;
			
			double apremi = nf.parse(premi.getText().toString()).doubleValue();
			double arate = nf.parse(exchangeRate.getText().toString()).doubleValue();
			double totalpremi = apremi * arate;
			
			return totalpremi;
					
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	
	private void calculateAll() throws ParseException {
		v_tsi = Utility.parseDouble(si);
		v_rate = Utility.parseDouble(rate);
		Log.i(TAG, "::calculateAll:" + "rate = " + v_rate );

		v_premi = Utility.countPremi(v_rate, v_tsi);
		v_discpct = Utility.parseDouble(discpct);
		v_disc = Utility.countDiscount(v_premi, v_discpct);
		v_polis = 0;
		v_materai = Utility.countMaterai(v_premi);
		
		premi.setText(nf.format(v_premi));
	    disc.setText(nf.format(v_disc));
	    polis.setText(nf.format(v_polis));
		materai.setText(nf.format(v_materai));
			
		double totalpremi = getexchangeratepremi();
		exchangePremi.setText(nf.format(totalpremi));
	
		v_total = Utility.countTotal(totalpremi, v_polis, v_materai, v_disc);
		total.setText(nf.format(v_total));
		
//		si.setText(nf.format(v_tsi));
//		rate.setText(nf.format(v_rate));
	   
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
		
		if(vesselName.getText().toString().isEmpty()){
			flag++ ;
			vesselName.setHintTextColor(Color.RED);
			UIErrorMessage = "Vessel/conveyance name harus diisi";
			
			return;      			
		}
		
		DBA_MASTER_VESSEL_DETAIL dba = new DBA_MASTER_VESSEL_DETAIL(this);
		Cursor c = null;

		try {
			dba.open();
			c = dba.getByVesselName(vesselName.getText().toString());
		
			if (c.getCount() == 0)
			{
				flag++ ;
				vesselName.setTextColor(Color.RED);
				UIErrorMessage = "Vessel/conveyance tidak valid";
				return ;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			

			if (dba != null)
				dba.close();

			if (c != null)
				c.close();

		} 
		
//		if(vesselName.getTag(R.string.vessel_name).toString() != vesselName.getText().toString()){
//			flag++ ;
//			vesselName.setTextColor(Color.RED);
//			UIErrorMessage = "Vessel/conveyance name tidak valid";
//
//			return;
//		}
//		
		
		String code = ((SpinnerGenericItem)vesselType.getSelectedItem()).getCode();
		
		Calendar calendar = Calendar.getInstance();
		int todayYear = calendar.get(Calendar.YEAR);
		
		if (   code.equalsIgnoreCase("AIR") 
				|| code.equalsIgnoreCase("TRK")
				|| code.equalsIgnoreCase("TRN")) 
		{
			
			int taun  = Integer.parseInt(vesselyear.getText().toString()) == 0 
					? todayYear 
					: Integer.parseInt(vesselyear.getText().toString());
			
			Log.i(TAG, "::validasiNext:" + "tahun : " + taun);
			
			if ( (todayYear -  taun) > 35 ) {
				flag ++;
				UIErrorMessage = "Usia Kapal Maksimum 35 tahun. Harap lapor ke Underwritter";
				
				return;
			}
			
		} 
		
		
//		if( rate.getText().toString().isEmpty()){
//			flag++ ;
//			rate.setHintTextColor(Color.RED);
//			UIErrorMessage = "Rate tidak ada. Hubungi IT ACA.";
//			
//			return;
//		}
		 
		if( etd.getText().toString().isEmpty()){
			flag++ ;
			etd.setHintTextColor(Color.RED);
			UIErrorMessage = "Est. Time Dept harus diisi";
			
			return;      			
		}
		
		
		
		DBA_MASTER_PRODUCT_SETTING dbSetting= new DBA_MASTER_PRODUCT_SETTING(this);
		Cursor cSetting = null;
		double maxTSI = 0;
		double minTSI = 0; 
		
		try {
			dbSetting.open();
			cSetting = dbSetting.getRow("13");

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

		if (si.getText().toString().isEmpty()) {
			flag++ ;
			si.setHintTextColor(Color.RED);
			UIErrorMessage = "Sum Insured harus diisi";
			
			return;
		}
		
		if( si.getText().toString().isEmpty() ||
			nf.parse(si.getText().toString()).doubleValue() == minTSI ){
			
			flag++ ;
			si.setTextColor(Color.RED);
			UIErrorMessage = "Sum Insured harus diisi";
			
			return;
		}
		
		double suminsured = nf.parse(si.getText().toString()).doubleValue() * nf. parse(exchangeRate.getText().toString()).doubleValue();
		
		if( si.getText().toString().isEmpty() || suminsured > maxTSI ){
			
			flag++ ;
			si.setTextColor(Color.RED);
			UIErrorMessage = "Sum Insured harus diisi dengan nilai maksimal " + nf.format(maxTSI) + " rupiah";
			
			return;
		}
		if(    rate.getText().toString().isEmpty()
			|| nf.parse(rate.getText().toString()).doubleValue() < 0.08 )
		{
			
			flag++ ;
			rate.setHintTextColor(Color.RED);
			UIErrorMessage = "Rate harus diisi minimal 0.08%";
			
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
				Toast.makeText(getBaseContext(), UIErrorMessage, Toast.LENGTH_SHORT).show();
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
//	
//	public void btnChooseVesselType(View v) {
//		Intent i = new Intent(getBaseContext(),  ChooseVesselTypeActivity.class);
//		i.putExtras(b);
//		i.putExtra("VesselFlag", "NORMAL");
//		startActivity(i);
//		this.finish();
//	}

//
//	public void btnChoosePrintedVesselType(View v) {
//		
//		
//		Intent i = new Intent(getBaseContext(),  ChooseVesselTypeActivity.class);
//		i.putExtras(b);
//		i.putExtra("VesselFlag", "PRINTED");
//		startActivity(i);
//		this.finish();
//	}
	
//	public void btnChooseVesselName(View v) {
//		if (vesseltype.getText().length() > 0)
//		{
//			Intent i = new Intent(getBaseContext(),  ChooseVesselNameActivity.class);
//			i.putExtras(b);
//			i.putExtra("VesselFlag", "NORMAL");
//			i.putExtra("VesselType", vesseltype.getText().toString());
//			startActivity(i);
//			this.finish();
//		}
//	}
//
//
//	public void btnChoosePrintedVesselName(View v) {
//		
//		if (printedvesseltype.getText().length() > 0)
//		{
//			Intent i = new Intent(getBaseContext(),  ChooseVesselNameActivity.class);
//			i.putExtras(b);
//			i.putExtra("VesselFlag", "PRINTED");
//			i.putExtra("VesselType", printedvesseltype.getText().toString());
//			startActivity(i);
//			this.finish();
//		}
//	}
	
	
	
	
	
	@SuppressLint("DefaultLocale")
	public void insertDB()
	{
		DBA_PRODUCT_CARGO dba = null;
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
				dba = new DBA_PRODUCT_CARGO(getBaseContext());
				dba2 = new DBA_PRODUCT_MAIN(getBaseContext());
				
				dba.open();
				dba2.open();
				Log.i(TAG, "::insertDB:" + "vessel code : " + ((SpinnerGenericItem)vesselType.getSelectedItem()).getCode());
				
				String codeCurrency = ((SpinnerGenericItem)spCurrency.getSelectedItem()).getCode();
				String descCurrency = ((SpinnerGenericItem)spCurrency.getSelectedItem()).getDesc();
				String policyType = ((SpinnerGenericItem)spPolicyType.getSelectedItem()).getCode();
				
				if (PRODUCT_ACTION.equals("NEW")) {
					SPPA_ID = dba2.initialInsert(
							customerno.getText().toString(), 
							customername.getText().toString(), 
							"CARGO",
							nf.parse((premi.getText().toString())).doubleValue(),
							nf.parse((materai.getText().toString())).doubleValue(),
							nf.parse((polis.getText().toString())).doubleValue(),
							nf.parse((total.getText().toString())).doubleValue(),
							etd.getText().toString(), "",
							Utility.DateToString(Utility.GetToday(), "dd/MM/yyyy"),
							"","","","N","", 
							nf.parse((discpct.getText().toString())).doubleValue(),
							nf.parse((disc.getText().toString())).doubleValue());
					
					
					
					dba.initialInsert( 
							SPPA_ID, 
							nf.parse(si.getText().toString()).doubleValue(), 
							
							interestdetails.getText().toString().toUpperCase(Locale.getDefault()), 
							"",
							blno.getText().toString().toUpperCase(Locale.getDefault()),
							lcno.getText().toString().toUpperCase(Locale.getDefault()),
							
							vesselName.getTag(R.string.vessel_code).toString(),
							((SpinnerGenericItem)vesselType.getSelectedItem()).getCode(),
							vesselName.getText().toString().toUpperCase(),
							"","",
							
//							((SpinnerGenericItem)spCurrency.getSelectedItem()).getCode(), 
							codeCurrency,
							nf.parse(exchangeRate.getText().toString()).doubleValue(),
							
							vesselweight.getText().toString().toUpperCase(Locale.getDefault()),
							vesselyear.getText().toString().toUpperCase(Locale.getDefault()),
							"",""	,
							
							voyagefrom.getText().toString().toUpperCase(), 
							voyageto.getText().toString().toUpperCase(), 
							transhipment.getText().toString().toUpperCase(), 
							policyType,
							voyageno.getText().toString().toUpperCase(),
							
							nf_asri.parse(rate.getText().toString()).doubleValue(),
							etd.getText().toString(),
							"",
							nf.parse(premi.getText().toString()).doubleValue(),
							nf.parse(polis.getText().toString()).doubleValue(),
							nf.parse(total.getText().toString()).doubleValue(), 
							"CARGO", 
							customername.getText().toString());
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
							etd.getText().toString(), "",
							Utility.DateToString(Utility.GetToday(), "dd/MM/yyyy"),
							nf.parse((discpct.getText().toString())).doubleValue(),
							nf.parse((disc.getText().toString())).doubleValue());
					
					dba.nextInsert(
							SPPA_ID, 
							nf.parse(si.getText().toString()).doubleValue(), 
							
							interestdetails.getText().toString().toUpperCase(Locale.getDefault()), 
							"",
							blno.getText().toString().toUpperCase(Locale.getDefault()),
							lcno.getText().toString().toUpperCase(Locale.getDefault()),
							
							vesselName.getTag(R.string.vessel_code).toString(),
							((SpinnerGenericItem)vesselType.getSelectedItem()).getCode(),
							vesselName.getText().toString().toUpperCase(Locale.getDefault()),
							"","",
							
							codeCurrency,
							nf.parse(exchangeRate.getText().toString()).doubleValue(),
							
							vesselweight.getText().toString().toUpperCase(Locale.getDefault()),
							vesselyear.getText().toString().toUpperCase(Locale.getDefault()),
							"","",
							
							
							voyagefrom.getText().toString().toUpperCase(), 
							voyageto.getText().toString().toUpperCase(), 
							transhipment.getText().toString().toUpperCase(), 
							policyType,
							voyageno.getText().toString().toUpperCase(),
							
							nf_asri.parse(rate.getText().toString()).doubleValue(),
							etd.getText().toString(),
							"",
							nf.parse(premi.getText().toString()).doubleValue(),
							nf.parse(polis.getText().toString()).doubleValue(),
							nf.parse(total.getText().toString()).doubleValue(), 
							"CARGO", 
							customername.getText().toString());
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
				return new DatePickerDialog(this, datePickerListener, Y, M, D);
		}
		return null;
	}
	
	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
			etd.setText(Utility.setUIDate(selectedYear, selectedMonth, selectedDay));
//			validasiTanggal();
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
		etd.setTextColor(Color.BLACK);
		boolean flag = Utility.validasiEffDate(etd.getText().toString(), 
							Utility.GetTomorrowString(Utility.GetTodayString()), getBaseContext());
		
		if (!flag) {
			etd.setTextColor(Color.RED);
			Utility.showCustomDialogInformation(FillCargoActivity.this, "Warning", "Tanggal mulai minimal besok hari");
			((Button)findViewById(R.id.btnNext)).setEnabled(false);
		}
	}
	
	public class DeleteSPPA extends AsyncTask<Void, Void, Void> 
    {
		private ProgressDialog progDialog = null;
		private boolean error = false;
		private String flag = "";
		
		protected void onPreExecute()  {
            progDialog = ProgressDialog.show(context, "", "Processing...",true);
        }
		
		 @Override
		 protected Void doInBackground(Void... params) {
			 
			 
				DBA_PRODUCT_MAIN dba = null;
				DBA_PRODUCT_CARGO dba2 = null;
				
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
					
					dba2 = new DBA_PRODUCT_CARGO(context);
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
