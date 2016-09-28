package com.aca.amm;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Calendar;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.Toast;

import com.aca.database.DBA_MASTER_AGENT;
import com.aca.database.DBA_PRODUCT_DNO;
import com.aca.database.DBA_PRODUCT_MAIN;

public class FillDNOActivity extends ControlNormalActivity{

	private Context context = null;
	
	private Bundle b;
	private NumberFormat nf;
	private String PRODUCT_ACTION;
	private long SPPA_ID;
	private int flag = 0;
	private Calendar c;
    
 	private double v_premi = 0, v_polis = 0, v_materai = 0, v_total = 0, v_discpct = 0, v_disc = 0;
	private EditText etFromDate, etToDate, etQ1Date;
	private Switch q1flag;
	private static final int DATE_FROM_ID = 99;
	private static final int DATE_TO_ID = 100;
	private static final int DATE_Q1_ID = 101;
	private EditText compname1, compname2, compname3, compname4, compname5, compname6, compname7, compname8, compname9, compname10;
	private Spinner spCompBusType1, spCompBusType2, spCompBusType3, spCompBusType4, spCompBusType5, spCompBusType6, spCompBusType7, spCompBusType8, spCompBusType9, spCompBusType10;
	private EditText premi, polis, total, materai, discpct, disc;
	private EditText customerno, customername;
	private EditText q1note;
	
	private TableRow row1, row2, row3, row4, row5, row6, row7, row8, row9, row10;
	private RadioButton plan1, plan2;
	private RadioGroup rbgPlan;

	private int rowFlag = 0;
	
	private String UIErrorMessage = "Lengkapi semua data";
	private double MAX_DISCOUNT = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_fill_dno);
		
		context = FillDNOActivity.this;
		
		spCompBusType1 = (Spinner)findViewById(R.id.spinnerCompBusType1);
		spCompBusType2 = (Spinner)findViewById(R.id.spinnerCompBusType2);
		spCompBusType3 = (Spinner)findViewById(R.id.spinnerCompBusType3);
		spCompBusType4 = (Spinner)findViewById(R.id.spinnerCompBusType4);
		spCompBusType5 = (Spinner)findViewById(R.id.spinnerCompBusType5);
		spCompBusType6 = (Spinner)findViewById(R.id.spinnerCompBusType6);
		spCompBusType7 = (Spinner)findViewById(R.id.spinnerCompBusType7);
		spCompBusType8 = (Spinner)findViewById(R.id.spinnerCompBusType8);
		spCompBusType9 = (Spinner)findViewById(R.id.spinnerCompBusType9);
		spCompBusType10 = (Spinner)findViewById(R.id.spinnerCompBusType10);
		
		Utility.BindJenisUsahaLiability(spCompBusType1, getBaseContext(), this);
		Utility.BindJenisUsahaLiability(spCompBusType2, getBaseContext(), this);
		Utility.BindJenisUsahaLiability(spCompBusType3, getBaseContext(), this);
		Utility.BindJenisUsahaLiability(spCompBusType4, getBaseContext(), this);
		Utility.BindJenisUsahaLiability(spCompBusType5, getBaseContext(), this);
		Utility.BindJenisUsahaLiability(spCompBusType6, getBaseContext(), this);
		Utility.BindJenisUsahaLiability(spCompBusType7, getBaseContext(), this);
		Utility.BindJenisUsahaLiability(spCompBusType8, getBaseContext(), this);
		Utility.BindJenisUsahaLiability(spCompBusType9, getBaseContext(), this);
		Utility.BindJenisUsahaLiability(spCompBusType10, getBaseContext(), this);
		
		InitControls();
		RegisterListener();
		initColor();
		
		rowFlag = 1;
		
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
			}
			
			
			
			MAX_DISCOUNT = Utility.getMaxDiscount(getBaseContext(), "29");
			
			
			if (Utility.getIsDiscountable(getBaseContext(), "17").equals("0"))
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
		
		compname1.setEnabled(false);
		compname2.setEnabled(false);
		compname3.setEnabled(false);
		compname4.setEnabled(false);
		compname5.setEnabled(false);
		compname6.setEnabled(false);
		compname7.setEnabled(false);
		compname8.setEnabled(false);
		compname9.setEnabled(false);
		compname10.setEnabled(false);
		
		row1.setEnabled(false);
		row2.setEnabled(false);
		row3.setEnabled(false);
		row4.setEnabled(false);
		row5.setEnabled(false);
		row6.setEnabled(false);
		row7.setEnabled(false);
		row8.setEnabled(false);
		row9.setEnabled(false);
		row10.setEnabled(false);
		

		rbgPlan.setEnabled(false);
		
		premi.setEnabled(false);
		discpct.setEnabled(false);
		disc.setEnabled(false);
		polis.setEnabled(false);
		materai.setEnabled(false);
		total.setEnabled(false);
	
		q1flag.setEnabled(false);
		q1note.setEnabled(false);
		
		customerno.setEnabled(false);
		customername.setEnabled(false);
		
		plan1.setEnabled(false);
		plan2.setEnabled(false);
		
		etFromDate.setEnabled(false);
		etToDate.setEnabled(false);
		etQ1Date.setEnabled(false);
		
		spCompBusType1.setEnabled(false);
		spCompBusType2.setEnabled(false);
		spCompBusType3.setEnabled(false);
		spCompBusType4.setEnabled(false);
		spCompBusType5.setEnabled(false);
		spCompBusType6.setEnabled(false);
		spCompBusType7.setEnabled(false);
		spCompBusType8.setEnabled(false);
		spCompBusType9.setEnabled(false);
		spCompBusType10.setEnabled(false);
		
		(findViewById(R.id.btnAddMore)).setEnabled(false);
		(findViewById(R.id.btnRemoveMore)).setEnabled(false);
		(findViewById(R.id.btnChooseCustomer)).setEnabled(false);
		
		
		////////////////////////
		
		etFromDate.setTextColor(Color.BLACK);
		etToDate.setTextColor(Color.BLACK);
		etQ1Date.setTextColor(Color.BLACK);
		
		
		compname1.setTextColor(Color.BLACK);
		compname2.setTextColor(Color.BLACK);
		compname3.setTextColor(Color.BLACK);
		compname4.setTextColor(Color.BLACK);
		compname5.setTextColor(Color.BLACK);
		compname6.setTextColor(Color.BLACK);
		compname7.setTextColor(Color.BLACK);
		compname8.setTextColor(Color.BLACK);
		compname9.setTextColor(Color.BLACK);
		compname10.setTextColor(Color.BLACK);
		
		premi.setTextColor(Color.BLACK);
		discpct.setTextColor(Color.BLACK);
		disc.setTextColor(Color.BLACK);
		polis.setTextColor(Color.BLACK);
		materai.setTextColor(Color.BLACK);
		total.setTextColor(Color.BLACK);
	
		q1flag.setTextColor(Color.BLACK);
		q1note.setTextColor(Color.BLACK);
		
		customerno.setTextColor(Color.BLACK);
		customername.setTextColor(Color.BLACK);
		
		
	}


	private void initColor () {
		q1note.setEnabled(false);
		q1note.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));		
		
		etQ1Date.setEnabled(false);
		etQ1Date.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));				
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fill_dno, menu);
		return true;
	}
	
	private void InitControls() {

		plan1 = (RadioButton)findViewById(R.id.rboPlan1);
		plan2 = (RadioButton)findViewById(R.id.rboPlan2);	
		
		compname1 = (EditText)findViewById(R.id.txtCompName1);
		compname2 = (EditText)findViewById(R.id.txtCompName2);
		compname3 = (EditText)findViewById(R.id.txtCompName3);
		compname4 = (EditText)findViewById(R.id.txtCompName4);
		compname5 = (EditText)findViewById(R.id.txtCompName5);
		compname6 = (EditText)findViewById(R.id.txtCompName6);
		compname7 = (EditText)findViewById(R.id.txtCompName7);
		compname8 = (EditText)findViewById(R.id.txtCompName8);
		compname9 = (EditText)findViewById(R.id.txtCompName9);
		compname10 = (EditText)findViewById(R.id.txtCompName10);
		
		row1 = (TableRow)findViewById(R.id.trRowPerusahaan1);
		row2 = (TableRow)findViewById(R.id.trRowPerusahaan2);
		row3 = (TableRow)findViewById(R.id.trRowPerusahaan3);
		row4 = (TableRow)findViewById(R.id.trRowPerusahaan4);
		row5 = (TableRow)findViewById(R.id.trRowPerusahaan5);
		row6 = (TableRow)findViewById(R.id.trRowPerusahaan6);
		row7 = (TableRow)findViewById(R.id.trRowPerusahaan7);
		row8 = (TableRow)findViewById(R.id.trRowPerusahaan8);
		row9 = (TableRow)findViewById(R.id.trRowPerusahaan9);
		row10 = (TableRow)findViewById(R.id.trRowPerusahaan10);
		
		
		
		premi = (EditText)findViewById(R.id.txtPremi);
		discpct = (EditText)findViewById(R.id.txtDiscountPct);
		disc = (EditText)findViewById(R.id.txtDiscount);
		polis = (EditText)findViewById(R.id.txtBiayaPolis);
		materai = (EditText)findViewById(R.id.txtMaterai);
		total = (EditText)findViewById(R.id.txtTotal);
	
		q1flag = (Switch)findViewById(R.id.swiQ1Flag);
		
		customerno = (EditText)findViewById(R.id.txtCustomerNo);
		customername = (EditText)findViewById(R.id.txtCustomerName);
		
		rbgPlan = (RadioGroup)findViewById(R.id.rbgPlan);
		
		q1note = (EditText)findViewById(R.id.txtQ1Note);
		
	}
	
	public void expandViewPerusahaan (TableRow tr, EditText namaPerusahaan) {
		if (!namaPerusahaan.getText().toString().isEmpty()) {
			tr.setVisibility(View.VISIBLE);
			rowFlag ++ ;
			
		}
	}

	private void LoadDB() {
		
		DBA_PRODUCT_MAIN dbm = null;
		DBA_PRODUCT_DNO dba = null;
		
		Cursor cm = null;
		Cursor c = null;
		
		try{
			
			dbm = new DBA_PRODUCT_MAIN(getBaseContext());
			dba = new DBA_PRODUCT_DNO(getBaseContext());
			
			dbm.open();
			cm = dbm.getRow(SPPA_ID);
			cm.moveToFirst();
			
			dba.open();
			c = dba.getRow(SPPA_ID);
			c.moveToFirst();
			
			customerno.setText(cm.getString(1));
			customername.setText(cm.getString(2));
			
			
			compname1.setText(c.getString(13));
			compname2.setText(c.getString(15));
			compname3.setText(c.getString(17));
			compname4.setText(c.getString(19));
			compname5.setText(c.getString(21));
			compname6.setText(c.getString(23));
			compname7.setText(c.getString(25));
			compname8.setText(c.getString(27));
			compname9.setText(c.getString(29));
			compname10.setText(c.getString(31));
			
			expandViewPerusahaan(row2, compname2);
			expandViewPerusahaan(row3, compname3);
			expandViewPerusahaan(row4, compname4);
			expandViewPerusahaan(row5, compname5);
			expandViewPerusahaan(row6, compname6);
			expandViewPerusahaan(row7, compname7);
			expandViewPerusahaan(row8, compname8);
			expandViewPerusahaan(row9, compname9);
			expandViewPerusahaan(row10, compname10);
			
				
			SpinnerGenericAdapter a;
			
			a = (SpinnerGenericAdapter)spCompBusType1.getAdapter();
			spCompBusType1.setSelection(a.getItemId(c.getString(14)));

			a = (SpinnerGenericAdapter)spCompBusType2.getAdapter();
			spCompBusType2.setSelection(a.getItemId(c.getString(16)));
			
			a = (SpinnerGenericAdapter)spCompBusType3.getAdapter();
			spCompBusType3.setSelection(a.getItemId(c.getString(18)));
			
			a = (SpinnerGenericAdapter)spCompBusType4.getAdapter();
			spCompBusType4.setSelection(a.getItemId(c.getString(20)));
			
			a = (SpinnerGenericAdapter)spCompBusType5.getAdapter();
			spCompBusType5.setSelection(a.getItemId(c.getString(22)));
			
			a = (SpinnerGenericAdapter)spCompBusType6.getAdapter();
			spCompBusType6.setSelection(a.getItemId(c.getString(24)));
			
			a = (SpinnerGenericAdapter)spCompBusType7.getAdapter();
			spCompBusType7.setSelection(a.getItemId(c.getString(26)));
			
			a = (SpinnerGenericAdapter)spCompBusType8.getAdapter();
			spCompBusType8.setSelection(a.getItemId(c.getString(28)));
			
			a = (SpinnerGenericAdapter)spCompBusType9.getAdapter();
			spCompBusType9.setSelection(a.getItemId(c.getString(30)));
			
			a = (SpinnerGenericAdapter)spCompBusType10.getAdapter();
			spCompBusType10.setSelection(a.getItemId(c.getString(32)));
			
			q1note.setText(c.getString(6));

			if(c.getString(5).equals("1"))
				q1flag.setChecked(true);
			
			etQ1Date.setText(c.getString(7));
			etFromDate.setText(c.getString(3));
			etToDate.setText(c.getString(4));
			
			if(c.getString(2).equals("1")){
				plan1.setChecked(true);
			}else if(c.getString(2).equals("2")){
				plan2.setChecked(true);
			}
			
			premi.setText(nf.format(c.getDouble(8)));
			discpct.setText(nf.format(cm.getDouble(23)));
			disc.setText(nf.format(cm.getDouble(24)));
			polis.setText(nf.format(c.getDouble(9)));
			total.setText(nf.format(c.getDouble(10)));
			
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
	
	public void btnAddMoreClick(View view) {
		rowFlag ++ ;
		switch (rowFlag) {
			case 2: row2.setVisibility(View.VISIBLE); break;
			case 3: row3.setVisibility(View.VISIBLE); break;
			case 4: row4.setVisibility(View.VISIBLE); break;
			case 5: row5.setVisibility(View.VISIBLE); break;
			case 6: row6.setVisibility(View.VISIBLE); break;
			case 7: row7.setVisibility(View.VISIBLE); break;
			case 8: row8.setVisibility(View.VISIBLE); break;
			case 9: row9.setVisibility(View.VISIBLE); break;
			case 10: row10.setVisibility(View.VISIBLE); break;
			case 11: rowFlag = 10;
		default:
			break;
		}
		((EditText)findViewById(R.id.txtFlagRow)).setText(rowFlag + "");
	}
	
	public void btnRemoveMoreClick (View view) {
		switch (rowFlag) {
			case 2: row2.setVisibility(View.GONE); 
					compname2.setText("");
					spCompBusType2.setSelection(0);break;
			case 3: row3.setVisibility(View.GONE); 
					compname3.setText("");
					spCompBusType3.setSelection(0);break;
			case 4: row4.setVisibility(View.GONE); 
					compname4.setText("");
					spCompBusType4.setSelection(0);break;
			case 5: row5.setVisibility(View.GONE); 
					compname5.setText("");
					spCompBusType5.setSelection(0);break;
			case 6: row6.setVisibility(View.GONE); 
					compname6.setText("");
					spCompBusType6.setSelection(0);break;
			case 7: row7.setVisibility(View.GONE); 
					compname7.setText("");
					spCompBusType7.setSelection(0);break;
			case 8: row8.setVisibility(View.GONE); 
					compname8.setText("");
					spCompBusType8.setSelection(0);break;
			case 9: row9.setVisibility(View.GONE);  
					compname9.setText("");
					spCompBusType9.setSelection(0);break;
			case 10: row10.setVisibility(View.GONE); 
					compname10.setText("");
					spCompBusType10.setSelection(0);break;
				
		default:
			break;
		}
		rowFlag--;
		if (rowFlag < 1) rowFlag = 1;
		

		((EditText)findViewById(R.id.txtFlagRow)).setText(rowFlag + "");
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
					validasiTanggal();
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

		etQ1Date = (EditText)findViewById(R.id.txtQ1Date);
		etQ1Date.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View arg0, boolean hasFocus) {
				if(hasFocus)
					showDialog(DATE_Q1_ID);
				else { 
					((Button)findViewById(R.id.btnNext)).setEnabled(true);
					etQ1Date.setTextColor(Color.BLACK);
				}
			}
		});
		etQ1Date.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showDialog(DATE_Q1_ID);
			}
		});
		
		
		q1flag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(!q1flag.isChecked())
				{
					q1note.setEnabled(false);		
					etQ1Date.setEnabled(false); 

					q1note.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
					etQ1Date.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
					
					q1note.setText("");
					etQ1Date.setText("");
					

					((Button)findViewById(R.id.btnNext)).setEnabled(true);
					
				}
				else {
					q1note.setEnabled(true);				
					q1note.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext));		
					
					etQ1Date.setEnabled(true);				
					etQ1Date.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext));		
					
					
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
		if (rowFlag > 0)
		if (compname1.getText().toString().isEmpty() ||
			((SpinnerGenericItem)spCompBusType1.getSelectedItem()).getCode().equals("0")) {
			flag++;
			compname1.setHintTextColor(Color.RED);
			UIErrorMessage = "Data Perusahaan harus diisi minimal 1";
			return;
		}
		if (rowFlag > 1)
			if (compname2.getText().toString().isEmpty() ||
					((SpinnerGenericItem)spCompBusType2.getSelectedItem()).getCode().equals("0")) {
				flag++;
				compname2.setHintTextColor(Color.RED);
				UIErrorMessage = "Data Perusahaan harus dilengkapi";
				return;
			}
			
		if (rowFlag > 2)
		if (compname3.getText().toString().isEmpty() ||
				((SpinnerGenericItem)spCompBusType3.getSelectedItem()).getCode().equals("0")) {
				flag++;
				compname3.setHintTextColor(Color.RED);
				UIErrorMessage = "Data Perusahaan harus dilengkapi";
				return;
			}
		if (rowFlag > 3)
			if (compname4.getText().toString().isEmpty() ||
					((SpinnerGenericItem)spCompBusType4.getSelectedItem()).getCode().equals("0")) {
					flag++;
					compname4.setHintTextColor(Color.RED);
					UIErrorMessage = "Data Perusahaan harus dilengkapi";
					return;
				}
		if (rowFlag > 4)
			if (compname5.getText().toString().isEmpty() ||
					((SpinnerGenericItem)spCompBusType5.getSelectedItem()).getCode().equals("0")) {
					flag++;
					compname5.setHintTextColor(Color.RED);
					UIErrorMessage = "Data Perusahaan harus dilengkapi";
					return;
				}
		if (rowFlag > 5)
			if (compname6.getText().toString().isEmpty() ||
					((SpinnerGenericItem)spCompBusType6.getSelectedItem()).getCode().equals("0")) {
					flag++;
					compname6.setHintTextColor(Color.RED);
					UIErrorMessage = "Data Perusahaan harus dilengkapi";
					return;
				}
		if (rowFlag > 6)
			if (compname7.getText().toString().isEmpty() ||
					((SpinnerGenericItem)spCompBusType7.getSelectedItem()).getCode().equals("0")) {
					flag++;
					compname7.setHintTextColor(Color.RED);
					UIErrorMessage = "Data Perusahaan harus dilengkapi";
					return;
				}
		if (rowFlag > 7)
			if (compname8.getText().toString().isEmpty() ||
					((SpinnerGenericItem)spCompBusType8.getSelectedItem()).getCode().equals("0")) {
					flag++;
					compname8.setHintTextColor(Color.RED);
					UIErrorMessage = "Data Perusahaan harus dilengkapi";
					return;
				}
		if (rowFlag > 8)
			if (compname9.getText().toString().isEmpty() ||
					((SpinnerGenericItem)spCompBusType9.getSelectedItem()).getCode().equals("0")) {
					flag++;
					compname9.setHintTextColor(Color.RED);
					UIErrorMessage = "Data Perusahaan harus dilengkapi";
					return;
				}
		if (rowFlag > 9)
			if (compname10.getText().toString().isEmpty() ||
					((SpinnerGenericItem)spCompBusType10.getSelectedItem()).getCode().equals("0")) {
					flag++;
					compname10.setHintTextColor(Color.RED);
					UIErrorMessage = "Data Perusahaan harus dilengkapi";
					return;
				}
		
		
		if (q1flag.isChecked() && (q1note.getText().toString().isEmpty() || etQ1Date.getText().toString().isEmpty())) {
			flag ++ ;
			
			q1note.setHintTextColor(Color.RED);
			etQ1Date.setHintTextColor(Color.RED);
			
			UIErrorMessage = "Penjelasan harus diisi";
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
				Utility.showCustomDialogInformation(FillDNOActivity.this, "Warning", 
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
	
	private void getPremi(){

		if(plan1.isChecked())
		{
			v_premi = 5000000.00;
		}
		else if(plan2.isChecked())
		{
			v_premi = 10000000.00;
		}
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
		
		DBA_PRODUCT_DNO dba = null;
		DBA_PRODUCT_MAIN dba2 = null;
		DBA_MASTER_AGENT dba_MASTER_AGENT = null;
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
				dba = new DBA_PRODUCT_DNO(getBaseContext());
				dba2 = new DBA_PRODUCT_MAIN(getBaseContext());
				dba_MASTER_AGENT = new DBA_MASTER_AGENT(getBaseContext());
				
				dba.open();
				dba2.open();
				dba_MASTER_AGENT.open();
				
				String plan = "1";
				String q01flag = "0";
				
				if(plan1.isChecked())
					plan = "1";
				else if(plan2.isChecked())
					plan = "2";

				
				if (q1flag.isChecked())
					q01flag = "1";
				
				if (PRODUCT_ACTION.equals("NEW")) {
					SPPA_ID = dba2.initialInsert(customerno.getText().toString(), 
							customername.getText().toString(), "DNO",
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
						etFromDate.getText().toString(), 
						etToDate.getText().toString(),
						nf.parse(premi.getText().toString()).doubleValue(),
						v_polis,
						v_total,
						"DNO",
						customername.getText().toString(),
						q01flag, 
						q1note.getText().toString(),
						etQ1Date.getText().toString(),
						compname1.getText().toString().toUpperCase(),
						((SpinnerGenericItem)spCompBusType1.getSelectedItem()).getCode(),
						compname2.getText().toString().toUpperCase(),
						((SpinnerGenericItem)spCompBusType2.getSelectedItem()).getCode(),
						compname3.getText().toString().toUpperCase(),
						((SpinnerGenericItem)spCompBusType3.getSelectedItem()).getCode(),
						compname4.getText().toString().toUpperCase(),
						((SpinnerGenericItem)spCompBusType4.getSelectedItem()).getCode(),
						compname5.getText().toString().toUpperCase(),
						((SpinnerGenericItem)spCompBusType5.getSelectedItem()).getCode(),
						compname6.getText().toString().toUpperCase(),
						((SpinnerGenericItem)spCompBusType6.getSelectedItem()).getCode(),
						compname7.getText().toString().toUpperCase(),
						((SpinnerGenericItem)spCompBusType7.getSelectedItem()).getCode(),
						compname8.getText().toString().toUpperCase(),
						((SpinnerGenericItem)spCompBusType8.getSelectedItem()).getCode(),
						compname9.getText().toString().toUpperCase(),
						((SpinnerGenericItem)spCompBusType9.getSelectedItem()).getCode(),
						compname10.getText().toString().toUpperCase(),
						((SpinnerGenericItem)spCompBusType10.getSelectedItem()).getCode()						
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
							etFromDate.getText().toString(), 
							etToDate.getText().toString(),
							nf.parse(premi.getText().toString()).doubleValue(),
							v_polis,
							v_total,
							"DNO",
							customername.getText().toString(),
							q01flag, 
							q1note.getText().toString(),
							etQ1Date.getText().toString(),
							compname1.getText().toString().toUpperCase(),
							((SpinnerGenericItem)spCompBusType1.getSelectedItem()).getCode(),
							compname2.getText().toString().toUpperCase(),
							((SpinnerGenericItem)spCompBusType2.getSelectedItem()).getCode(),
							compname3.getText().toString().toUpperCase(),
							((SpinnerGenericItem)spCompBusType3.getSelectedItem()).getCode(),
							compname4.getText().toString().toUpperCase(),
							((SpinnerGenericItem)spCompBusType4.getSelectedItem()).getCode(),
							compname5.getText().toString().toUpperCase(),
							((SpinnerGenericItem)spCompBusType5.getSelectedItem()).getCode(),
							compname6.getText().toString().toUpperCase(),
							((SpinnerGenericItem)spCompBusType6.getSelectedItem()).getCode(),
							compname7.getText().toString().toUpperCase(),
							((SpinnerGenericItem)spCompBusType7.getSelectedItem()).getCode(),
							compname8.getText().toString().toUpperCase(),
							((SpinnerGenericItem)spCompBusType8.getSelectedItem()).getCode(),
							compname9.getText().toString().toUpperCase(),
							((SpinnerGenericItem)spCompBusType9.getSelectedItem()).getCode(),
							compname10.getText().toString().toUpperCase(),
							((SpinnerGenericItem)spCompBusType10.getSelectedItem()).getCode()
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
			
			if (dba_MASTER_AGENT != null) 
				dba_MASTER_AGENT.close();
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
			case DATE_Q1_ID:
				return new DatePickerDialog(this, datePickerListenerQ1, Y, M, D);
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
			calculateAll();
		}
	};
	
	private DatePickerDialog.OnDateSetListener datePickerListenerNext = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
			String theDate = Utility.setUIDate(selectedYear, selectedMonth, selectedDay);
			etToDate.setText(Utility.GetTodayNextYearString(theDate));
		}
	};
	
	private DatePickerDialog.OnDateSetListener datePickerListenerQ1 = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
			String theDate = Utility.setUIDate(selectedYear, selectedMonth, selectedDay);
			etQ1Date.setText(theDate);
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
			Utility.showCustomDialogInformation(FillDNOActivity.this, "Warning", "Tanggal mulai minimal besok hari");
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
				DBA_PRODUCT_DNO dba2 = null;
				
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
					
					dba2 = new DBA_PRODUCT_DNO(context);
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
