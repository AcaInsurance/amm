package com.aca.amm;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.WeakHashMap;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.aca.amm.R;
import com.aca.database.DBA_MASTER_WELLWOMAN_RATE;
import com.aca.database.DBA_PRODUCT_MAIN;
import com.aca.database.DBA_PRODUCT_WELLWOMAN;

import android.os.AsyncTask;
import android.os.Bundle;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class FillWellWomanActivity extends ControlNormalActivity implements InterfaceCustomer{

private ArrayList<HashMap<String, String>> custList ;
	
	private Context context = null;
	
	private Bundle b;
	private NumberFormat nf;
	private String PRODUCT_ACTION;
	private long SPPA_ID;
	private int flag = 0;
	private Calendar c;
    
 	private double v_premi = 0, v_polis = 0, v_materai = 0, v_total = 0, v_discpct = 0, v_disc = 0;
	private EditText etFromDate, etToDate;
	private Switch q1flag, q2flag, q3flag, q4flag;
	private static final int DATE_FROM_ID = 99;
	private static final int DATE_TO_ID = 100;
	private EditText benename1, benerelation1;
	private EditText premi, polis, total, materai, discpct, disc;
	private EditText customerno, customername;
	private EditText q1note, q2note, q3note, q4note;
	
	private RadioButton plan1, plan2, plan3;
	private RadioGroup rbgPlan;
	private Button btnNext;

	private String UIErrorMessage = "Lengkapi semua data";
	private double MAX_DISCOUNT = 0;
	private double premiReas = 0;
	private String benefit = "0";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_fill_well_woman);
				
		context = FillWellWomanActivity.this;
		
		InitControls();
		RegisterListener();
		initColor();
		
		nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		
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
				getCustomerData();	
			}
			else if(!customerno.getText().toString().isEmpty())
				getCustomerData();
			

			initDiscount();
		
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
		
		benename1.setEnabled(false);
		benerelation1.setEnabled(false);
		
		premi.setEnabled(false);
		discpct.setEnabled(false);
		disc.setEnabled(false);
		polis.setEnabled(false);
		materai.setEnabled(false);
		total.setEnabled(false);
	
		q1flag.setEnabled(false);
		q2flag.setEnabled(false);
		q3flag.setEnabled(false);
		q4flag.setEnabled(false);
		
		customerno.setEnabled(false);
		customername.setEnabled(false);
		
		
		q1note.setEnabled(false);
		q2note.setEnabled(false);
		q3note.setEnabled(false);;
		q4note.setEnabled(false);
		
		etFromDate.setEnabled(false); 
		etToDate.setEnabled(false);
		
		(findViewById(R.id.btnChooseCustomer)).setEnabled(false);
		
		
//		TEXT COLOR DISABLED
		
		
			
		benename1.setTextColor(Color.BLACK);
		benerelation1.setTextColor(Color.BLACK);
		
		premi.setTextColor(Color.BLACK);
		discpct.setTextColor(Color.BLACK);
		disc.setTextColor(Color.BLACK);
		polis.setTextColor(Color.BLACK);
		materai.setTextColor(Color.BLACK);
		total.setTextColor(Color.BLACK);
			
		customerno.setTextColor(Color.BLACK);
		customername.setTextColor(Color.BLACK);
				
		q1note.setTextColor(Color.BLACK);
		q2note.setTextColor(Color.BLACK);
		q3note.setTextColor(Color.BLACK);;
		q4note.setTextColor(Color.BLACK);
		
		etFromDate.setTextColor(Color.BLACK); 
		etToDate.setTextColor(Color.BLACK);
		
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
		RetrieveCustomer retrieveCustomer = new RetrieveCustomer(FillWellWomanActivity.this, customerno.getText().toString());
		retrieveCustomer.execute();
		retrieveCustomer.customerInterface = this;
		
	} 
	
	@Override
	public void getCustomerList(ArrayList<HashMap<String, String>> custList) {
		if (custList != null) {
			this.custList = custList;
			getAge();
			getGender();
		}
		else {
			Dialog dialog = Utility.showCustomDialogInformation(this, "Informasi", "Gagal mendapatkan data customer");
			dialog.setOnDismissListener(new OnDismissListener() {
				
				@Override
				public void onDismiss(DialogInterface dialog) {
					if (PRODUCT_ACTION.equals("NEW"))
						startActivity(new Intent(FillWellWomanActivity.this, ChooseProductActivity.class));
					else if (PRODUCT_ACTION.equals("EDIT") || 
							PRODUCT_ACTION.equals("VIEW") || 
							PRODUCT_ACTION.equals("VIEW.UNPAID"))
						startActivity(new Intent(FillWellWomanActivity.this, SyncActivity.class));
					
					FillWellWomanActivity.this.finish();
				}
			});
		}
	}


	private void InitControls() {

		plan1 = (RadioButton)findViewById(R.id.rboPlan1);
		plan2 = (RadioButton)findViewById(R.id.rboPlan2);
		plan3 = (RadioButton)findViewById(R.id.rboPlan3);
		
		benename1 = (EditText)findViewById(R.id.txtBeneName1);
		benerelation1 = (EditText)findViewById(R.id.txtBeneRelation1);
		
		premi = (EditText)findViewById(R.id.txtPremi);
		discpct = (EditText)findViewById(R.id.txtDiscountPct);
		disc = (EditText)findViewById(R.id.txtDiscount);
		polis = (EditText)findViewById(R.id.txtBiayaPolis);
		materai = (EditText)findViewById(R.id.txtMaterai);
		total = (EditText)findViewById(R.id.txtTotal);
	
		q1flag = (Switch)findViewById(R.id.swiQ1Flag);
		q2flag = (Switch)findViewById(R.id.swiQ2Flag);
		q3flag = (Switch)findViewById(R.id.swiQ3Flag);
		q4flag = (Switch)findViewById(R.id.swiQ4Flag);
		
		customerno = (EditText)findViewById(R.id.txtCustomerNo);
		customername = (EditText)findViewById(R.id.txtCustomerName);
		
		rbgPlan = (RadioGroup)findViewById(R.id.rbgPlan);
		
		q1note = (EditText)findViewById(R.id.txtQ1Note);
		q2note = (EditText)findViewById(R.id.txtQ2Note);
		q3note = (EditText)findViewById(R.id.txtQ3Note);
		q4note = (EditText)findViewById(R.id.txtQ4Note);
		
		btnNext = (Button)findViewById(R.id.btnNext);
		
		
	}
	
	private void initColor () {
		q1note.setEnabled(false);
		q1note.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));		
		
		q2note.setEnabled(false);
		q2note.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));	
		
		q3note.setEnabled(false);
		q3note.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));	
		
		q4note.setEnabled(false);
		q4note.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));	
		
		
		
	}
	

	private void LoadDB() {
		
		DBA_PRODUCT_MAIN dbm = null;
		DBA_PRODUCT_WELLWOMAN dba = null;
		
		Cursor cm = null;
		Cursor c = null;
		
		try{
			
			dbm = new DBA_PRODUCT_MAIN(getBaseContext());
			dba = new DBA_PRODUCT_WELLWOMAN(getBaseContext());
			
			dbm.open();
			cm = dbm.getRow(SPPA_ID);
			cm.moveToFirst();
			
			dba.open();
			c = dba.getRow(SPPA_ID);
			c.moveToFirst();
			
			customerno.setText(cm.getString(1));
			customername.setText(cm.getString(2));
			
			benename1.setText(c.getString(3));
			benerelation1.setText(c.getString(4));
			
			q1note.setText(c.getString(11));
			q2note.setText(c.getString(12));
			q3note.setText(c.getString(13));
			q4note.setText(c.getString(14));
			

			if(c.getString(7).equals("1"))
				q1flag.setChecked(true);
			
			if(c.getString(8).equals("1"))
				q2flag.setChecked(true);
			
			if(c.getString(9).equals("1"))
				q3flag.setChecked(true);
			
			if(c.getString(10).equals("1"))
				q4flag.setChecked(true);
			
			
			etFromDate.setText(c.getString(5));
			etToDate.setText(c.getString(6));
			
			if(c.getString(2).equals("1")){
				plan1.setChecked(true);
			}else if(c.getString(2).equals("2")){
				plan2.setChecked(true);
			}else if(c.getString(2).equals("3")){
				plan3.setChecked(true);
			}
			
			premi.setText(nf.format(c.getDouble(15)));
			
			premiReas = c.getDouble(16);
			benefit = c.getString(21);
			
			discpct.setText(nf.format(cm.getDouble(23)));
			disc.setText(nf.format(cm.getDouble(24)));
			polis.setText(nf.format(c.getDouble(17)));
			total.setText(nf.format(c.getDouble(18)));
			
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
										(Utility.GetTodayString()), getBaseContext());
					
					if (!flag) {
						etFromDate.setTextColor(Color.RED);
						Utility.showCustomDialogInformation(FillWellWomanActivity.this, "Warning", "Tanggal mulai minimal hari ini");
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
	        	if(!customerno.getText().toString().isEmpty() && !etFromDate.getText().toString().isEmpty() && !etToDate.getText().toString().isEmpty()){
	        		calculateAll();
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
		
		q1flag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(!q1flag.isChecked())
				{
					q1note.setEnabled(false);				
					q1note.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
					q1note.setText("");
				}
				else {
					q1note.setEnabled(true);				
					q1note.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext));								
				}
				
			}
		});
		
		q2flag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						// TODO Auto-generated method stub
						if(!q2flag.isChecked())
						{
							q2note.setEnabled(false);				
							q2note.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
							q2note.setText("");
						}
						else {
							q2note.setEnabled(true);				
							q2note.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext));								
						}
						
					}
				});

		q3flag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(!q3flag.isChecked())
				{
					q3note.setEnabled(false);				
					q3note.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
					q3note.setText("");
				}
				else {
					q3note.setEnabled(true);				
					q3note.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext));								
				}
				
			}
		});

		q4flag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(!q4flag.isChecked())
				{
					q4note.setEnabled(false);				
					q4note.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
					q4note.setText("");
				}
				else {
					q4note.setEnabled(true);				
					q4note.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext));								
				}
				
			}
		});
	}
	
	private void calculateAll() {
		try {
			getPremi();
			getDisc();
			getPolis();
			getMaterai();
			getTotal();
			
			premi.setText(nf.format(v_premi));
			disc.setText(nf.format(v_disc));
			materai.setText(nf.format(v_materai));
			total.setText(nf.format(v_total));
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.fill_medisafe, menu);
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
		
		if (!plan1.isChecked() && 
			!plan2.isChecked() && 
			!plan3.isChecked() )
		{
			flag++;
			UIErrorMessage = "Pilih plan";
			return;
			
		}
		if( benename1.getText().toString().isEmpty()){
			flag++ ;
			benename1.setHintTextColor(Color.RED);
			UIErrorMessage = "Ahli Waris harus diisi";
			return;
		}
		if( benerelation1.getText().toString().isEmpty()){
			flag++ ;
			benerelation1.setHintTextColor(Color.RED);
			UIErrorMessage = "Hubungan Ahli Waris harus diisi";
			return;
		}
		if (  q1flag.isChecked() && q1note.getText().toString().isEmpty() ||
				  q2flag.isChecked() && q2note.getText().toString().isEmpty() ||
				  q3flag.isChecked() && q3note.getText().toString().isEmpty() ||
				  q4flag.isChecked() && q4note.getText().toString().isEmpty()
				) {
				flag++;
				UIErrorMessage = "Semua penjelasan harus diisi";
				return;
			}
		
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
				Utility.showCustomDialogInformation(FillWellWomanActivity.this, "Warning", 
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
	
	private void getGender() {
		if (custList != null) {
			String gender = custList.get(0).get("CUSTOMER_GENDER").toString();
			if (gender.equals("M")) {
				Utility.showCustomDialogInformation(FillWellWomanActivity.this, "Informasi", 
						"Tertanggung hanya diperbolehkan wanita");
				btnNext.setVisibility(View.GONE);
			}
		}
	}
	
	private int getAge () {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String formattedDate = df.format(c.getTime());
		
	
		int umur = Utility.getAge(Utility.getFormatDate(custList.get(0).get("CUSTOMER_DOB").toString()), formattedDate);
		Log.d("umur", umur + "");
		
		if (umur < 18) {
			Utility.showCustomDialogInformation(FillWellWomanActivity.this, "Informasi",
					"Minimal umur 18");
			btnNext.setVisibility(View.GONE);
		}
		else if (umur > 64) {
			Utility.showCustomDialogInformation(FillWellWomanActivity.this, "Informasi",
					"Maksimal umur 64");			

			btnNext.setVisibility(View.GONE);
		}
		
		return umur;
				
	}
	private void getPremi(){	
		
		String plan = "1";
		
		if(plan1.isChecked())
			plan = "1";
		else if(plan2.isChecked())
			plan = "2";
		else if(plan3.isChecked())
			plan = "3";
		
		v_premi = 0.00;

		
		if(custList == null)
			return;
		
		int umur = getAge();
		if (umur < 18 && umur > 64)
			v_premi = 0.0;
		else 
			v_premi = getPremi(String.valueOf(umur), plan);
    	
	}
	
	private double getPremi(String pAge, String pPlan) {
		DBA_MASTER_WELLWOMAN_RATE dba = null;
		Cursor c = null;
		Cursor cAllCursor = null;
		
		double v = 0.0;
		
		
		try {
			dba = new DBA_MASTER_WELLWOMAN_RATE(getBaseContext());
			dba.open();
			
			c = dba.getRate(pPlan, pAge);
			cAllCursor = dba.getAll();
			
			Log.d("Jumlah rate", String.valueOf(cAllCursor.getCount()));
			if (c.moveToFirst())
			{
				v = c.getDouble(3);
				premiReas = c.getDouble(4);
				benefit = c.getString(1);
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		finally {
			if (c != null)
				c.close();
			
			if (cAllCursor != null)
				cAllCursor.close();
			
			if (dba != null)
				dba.close();
		}
		
		return v ;
	}
	
	private void getDisc() throws ParseException {
		v_discpct = Utility.parseDouble(discpct);
		v_disc = Utility.countDiscount(v_premi, v_discpct);
	}
	
	private void getMaterai() {
		v_materai = Utility.countMaterai(v_premi);
	}

	private void getPolis() {
		v_polis = 0;
	}
	
	private void getTotal() {
		v_total = Utility.countTotal(v_premi, v_polis, v_materai, v_disc);
	}
	
	public void insertDB() {
		
		DBA_PRODUCT_WELLWOMAN dba = null;
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
				dba = new DBA_PRODUCT_WELLWOMAN(getBaseContext());
				dba2 = new DBA_PRODUCT_MAIN(getBaseContext());
				
				dba.open();
				dba2.open();
				
				String plan = "1";
				String q01flag = "0", q02flag = "0", q03flag = "0", q04flag = "0";
				
				if(plan1.isChecked())
					plan = "1";
				else if(plan2.isChecked())
					plan = "2";
				else if(plan3.isChecked())
					plan = "3";
				
				
				if (q1flag.isChecked())
					q01flag = "1";
				
				if (q2flag.isChecked())
					q02flag = "1";
				
				if (q3flag.isChecked())
					q03flag = "1";
				
				if (q4flag.isChecked())
					q04flag = "1";

				if (PRODUCT_ACTION.equals("NEW")) {
					SPPA_ID = dba2.initialInsert(customerno.getText().toString(), 
							customername.getText().toString(), "WELLWOMAN",
							nf.parse(premi.getText().toString()).doubleValue(),
							v_materai,
							v_polis,
							v_total,
							etFromDate.getText().toString(), 
							etToDate.getText().toString(), 
							Utility.DateToString(Utility.GetToday(), "dd/MM/yyyy"),
							"","","","N","",
							nf.parse((discpct.getText().toString())).doubleValue(),
							nf.parse((disc.getText().toString())).doubleValue());
					
					dba.initialInsert(
						SPPA_ID,
						plan,
						benename1.getText().toString().toUpperCase(),
						benerelation1.getText().toString().toUpperCase(),
						etFromDate.getText().toString(), 
						etToDate.getText().toString(),
						nf.parse(premi.getText().toString()).doubleValue(),
						premiReas,
						v_polis,
						v_total,
						"WELLWOMAN",
						customername.getText().toString(),
						q01flag, 
						q02flag,
						q03flag,
						q04flag,
						q1note.getText().toString(),
						q2note.getText().toString(), 
						q3note.getText().toString(),
						q4note.getText().toString(),
						benefit
						);
	
				}
				else 
				{
					dba2.nextInsert(SPPA_ID, customerno.getText().toString(), 
							customername.getText().toString(),
							nf.parse(premi.getText().toString()).doubleValue(),
							v_materai,
							v_polis,
							v_total,
							etFromDate.getText().toString(), 
							etToDate.getText().toString(),
							Utility.DateToString(Utility.GetToday(), "dd/MM/yyyy"),
							nf.parse((discpct.getText().toString())).doubleValue(),
							nf.parse((disc.getText().toString())).doubleValue());
					
					dba.nextInsert(
							SPPA_ID,
							plan,
							benename1.getText().toString().toUpperCase(),
							benerelation1.getText().toString().toUpperCase(),
							etFromDate.getText().toString(), 
							etToDate.getText().toString(),
							nf.parse(premi.getText().toString()).doubleValue(),
							premiReas,
							v_polis,
							v_total,
							"WELLWOMAN",
							customername.getText().toString(),
							q01flag, 
							q02flag,
							q03flag,
							q04flag,
							q1note.getText().toString(),
							q2note.getText().toString(), 
							q3note.getText().toString(),
							q4note.getText().toString(),
							benefit
							);
						
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
	
	/**** BERUBAH *****/
	
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
			
			String theDate = Utility.setUIDate(selectedYear, selectedMonth, selectedDay);
			etFromDate.setText(theDate);
			etToDate.setText(
					Utility.getYesterdayString (
					Utility.GetTodayNextYearString(theDate)));
			
			validasiTanggal();
			calculateAll();
		}
	};
	
	private DatePickerDialog.OnDateSetListener datePickerListenerNext = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
//			etToDate.setText(Utility.setUIDate(selectedYear, selectedMonth, selectedDay));
			
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
	/********* BERUBAH ************/ 
	
	private void validasiTanggal () {
		((Button)findViewById(R.id.btnNext)).setEnabled(true);
		etFromDate.setTextColor(Color.BLACK);
		boolean flag = Utility.validasiEffDate(etFromDate.getText().toString(), 
							Utility.GetTodayString(), getBaseContext());
		
		if (!flag) {
			etFromDate.setTextColor(Color.RED);
			Utility.showCustomDialogInformation(FillWellWomanActivity.this, "Warning", "Tanggal mulai minimal hari hari");
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
				DBA_PRODUCT_WELLWOMAN dbaWM = null;
				
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
					
					dbaWM = new DBA_PRODUCT_WELLWOMAN(context);
					dbaWM.open();
					
					//dapatin no SPPA
					c = dba.getRow(SPPA_ID);
					c.moveToNext();
					E_SPPA_NO = c.getString(5);
			            
					Log.d("-->", E_SPPA_NO);
					Log.d("SPPA ID", SPPA_ID + "");
					
					
					if (E_SPPA_NO == null || E_SPPA_NO.length() == 0 || !TextUtils.isDigitsOnly(E_SPPA_NO))
					{
						Log.d("-->", "EMPTY");
						
						//hapus data di local db
						dba.delete(SPPA_ID);
						dbaWM.delete(SPPA_ID);
						
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
							dbaWM.delete(SPPA_ID);

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
					

					if (dbaWM != null)
						dbaWM.close();
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
