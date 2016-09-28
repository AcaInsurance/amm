package com.aca.amm;

import java.text.ParseException;
import java.util.Calendar;

import com.aca.database.DBA_INSURED_GROUP;

import android.os.Bundle;
import android.R.integer;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;

public class FillInsuredGroupActivity extends Activity {

	private EditText namaTertanggung1,
					namaTertanggung2,
					namaTertanggung3,
					namaTertanggung4,
					namaTertanggung5,
					namaTertanggung6,
					namaTertanggung7,
					namaTertanggung8,
					namaTertanggung9,
					namaTertanggung10,
					namaTertanggung11,
					namaTertanggung12,
					namaTertanggung13,
					namaTertanggung14,
					namaTertanggung15;
	
	private EditText dob1,
					dob2,
					dob3,
					dob4,
					dob5,
					dob6,
					dob7,
					dob8,
					dob9,
					dob10,
					dob11,
					dob12,
					dob13,
					dob14,
					dob15;
	
	private EditText passport1,
					passport2,
					passport3,
					passport4,
					passport5,
					passport6,
					passport7,
					passport8,
					passport9,
					passport10,
					passport11,
					passport12,
					passport13,
					passport14,
					passport15;
	
	private Switch swInsured1,
					swInsured2,
					swInsured3,
					swInsured4,
					swInsured5,
					swInsured6,
					swInsured7,
					swInsured8,
					swInsured9,
					swInsured10;
	
	Button btnBack;
	long productMainId;
	int insuredCount;

	int TAG_DOB1;
	int TAG_DOB2;
	static int TAG_DOB3;
	static int TAG_DOB4;
	static int TAG_DOB5;
	static int TAG_DOB6;
	static int TAG_DOB7;
	static int TAG_DOB8;
	static int TAG_DOB9;
	static int TAG_DOB10;
	
	
	private static final String TAG = "com.aca.amm.FillInsuredGroupActivity";
	public static final String TAG_BUNDLE_PRODUCT_MAIN_ID = "TAG_BUNDLE_PRODUCT_MAIN_ID";
	public static final String TAG_BUNDLE_INSURED_COUNT = "TAG_BUNDLE_INSURED_COUNT";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fill_insured_group);
		
		initControl();
		initVariable();
		registerListener(); 
		loadDB();
	}
	
	

	private void initVariable() {
		productMainId = -1;
		insuredCount = 0;
		TAG_DOB1 = 10;
		TAG_DOB2 = 20;
		TAG_DOB3 = 30;
		TAG_DOB4 = 40;
		TAG_DOB5 = 50;
		TAG_DOB6 = 60;
		TAG_DOB7 = 70;
		TAG_DOB8 = 80;
		TAG_DOB9 = 90;
		TAG_DOB10 = 100;
		
	}
	
	private int validate(Switch swiInsured, EditText nama, EditText dob, EditText passport, int count){
		if (swiInsured.isChecked()) {
			if (nama.getText().toString().isEmpty() ||  dob.getText().toString().isEmpty()  || passport.getText().toString().isEmpty()) 
			{
				Utility.showCustomDialogInformation(FillInsuredGroupActivity.this, "Informasi", "Lengkapi Data " + count);
				return 1;
			}
			return 0;
		}
		return 0;
	}
	
	private boolean validasi () {
		int flag = 0;
		
		flag += validate(swInsured1, namaTertanggung1, dob1, passport1, 1);
		flag += validate(swInsured2, namaTertanggung2, dob2, passport2, 2);
		flag += validate(swInsured3, namaTertanggung3, dob3, passport3, 3);
		flag += validate(swInsured4, namaTertanggung4, dob4, passport4, 4);
		flag += validate(swInsured5, namaTertanggung5, dob5, passport5, 5);
		flag += validate(swInsured6, namaTertanggung6, dob6, passport6, 6);
		flag += validate(swInsured7, namaTertanggung7, dob7, passport7, 7);
		flag += validate(swInsured8, namaTertanggung8, dob8, passport8, 8);
		flag += validate(swInsured9, namaTertanggung9, dob9, passport9, 9);
		flag += validate(swInsured10, namaTertanggung10, dob10, passport10, 10);
		
		if (flag > 0) 
			return false;
		else
			return true;
		
	}



	private void loadDB() {
		productMainId = getIntent().getExtras().getLong(TAG_BUNDLE_PRODUCT_MAIN_ID);
		if (productMainId == -1)
			return;
		
		 
		DBA_INSURED_GROUP dba = new DBA_INSURED_GROUP(this);
		Cursor c = null;

		try {
			dba.open();
			c = dba.getRow(productMainId);

			if (!c.moveToFirst())
				return;

			
			namaTertanggung1.setText(c.getString(2)); dob1.setText(c.getString(3)); passport1.setText(c.getString(4));
			namaTertanggung2.setText(c.getString(5)); dob2.setText(c.getString(6)); passport2.setText(c.getString(7));
			namaTertanggung3.setText(c.getString(8)); dob3.setText(c.getString(9)); passport3.setText(c.getString(10));
			namaTertanggung4.setText(c.getString(11)); dob4.setText(c.getString(12)); passport4.setText(c.getString(13));
			namaTertanggung5.setText(c.getString(14)); dob5.setText(c.getString(15)); passport5.setText(c.getString(16));
			namaTertanggung6.setText(c.getString(17)); dob6.setText(c.getString(18)); passport6.setText(c.getString(19));
			namaTertanggung7.setText(c.getString(20)); dob7.setText(c.getString(21)); passport7.setText(c.getString(22));
			namaTertanggung8.setText(c.getString(23)); dob8.setText(c.getString(24)); passport8.setText(c.getString(25));
			namaTertanggung9.setText(c.getString(26)); dob9.setText(c.getString(27)); passport9.setText(c.getString(28));
			namaTertanggung10.setText(c.getString(29)); dob10.setText(c.getString(30)); passport10.setText(c.getString(31));
 
			if (c.getInt(34) == 1)  swInsured1.setChecked(true) ;
			if (c.getInt(35) == 1)  swInsured2.setChecked(true) ;
			if (c.getInt(36) == 1)  swInsured3.setChecked(true) ;
			if (c.getInt(37) == 1)  swInsured4.setChecked(true) ;
			if (c.getInt(38) == 1)  swInsured5.setChecked(true) ;
			if (c.getInt(39) == 1)  swInsured6.setChecked(true) ;
			if (c.getInt(40) == 1)  swInsured7.setChecked(true) ;
			if (c.getInt(41) == 1)  swInsured8.setChecked(true) ;
			if (c.getInt(42) == 1)  swInsured9.setChecked(true) ;
			if (c.getInt(43) == 1)  swInsured10.setChecked(true) ;
			
			insuredCount = c.getInt(44);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dba != null)
				dba.close();

			if (c != null)
				c.close();

		}
	}



	private void registerListener() {
		btnBack.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				
				if (!validasi()) return;
				
				
				DBA_INSURED_GROUP dba = new DBA_INSURED_GROUP(FillInsuredGroupActivity.this);
				try {
					dba.open();
					
					if (productMainId == -1) {
						productMainId = dba.initialInsert( 
								namaTertanggung1.getText().toString(), dob1.getText().toString(), passport1.getText().toString(), 
								namaTertanggung2.getText().toString(), dob2.getText().toString(), passport2.getText().toString(), 
								namaTertanggung3.getText().toString(), dob3.getText().toString(), passport3.getText().toString(), 
								namaTertanggung4.getText().toString(), dob4.getText().toString(), passport4.getText().toString(), 
								namaTertanggung5.getText().toString(), dob5.getText().toString(), passport5.getText().toString(), 
								namaTertanggung6.getText().toString(), dob6.getText().toString(), passport6.getText().toString(), 
								namaTertanggung7.getText().toString(), dob7.getText().toString(), passport7.getText().toString(), 
								namaTertanggung8.getText().toString(), dob8.getText().toString(), passport8.getText().toString(), 
								namaTertanggung9.getText().toString(), dob9.getText().toString(), passport9.getText().toString(), 
								namaTertanggung10.getText().toString(), dob10.getText().toString(), passport10.getText().toString(),
								swInsured1.isChecked() ? 1 : 0,
								swInsured2.isChecked() ? 1 : 0,
								swInsured3.isChecked() ? 1 : 0,
								swInsured4.isChecked() ? 1 : 0,
								swInsured5.isChecked() ? 1 : 0,
								swInsured6.isChecked() ? 1 : 0,
								swInsured7.isChecked() ? 1 : 0,
								swInsured8.isChecked() ? 1 : 0,
								swInsured9.isChecked() ? 1 : 0,
								swInsured10.isChecked() ? 1 : 0,
								insuredCount
								);
						Log.i(TAG, "::onClick: product ID = " + productMainId); 
					}
					else {
						 dba.nextInsert(productMainId,
								    namaTertanggung1.getText().toString(), dob1.getText().toString(), passport1.getText().toString(), 
									namaTertanggung2.getText().toString(), dob2.getText().toString(), passport2.getText().toString(), 
									namaTertanggung3.getText().toString(), dob3.getText().toString(), passport3.getText().toString(), 
									namaTertanggung4.getText().toString(), dob4.getText().toString(), passport4.getText().toString(), 
									namaTertanggung5.getText().toString(), dob5.getText().toString(), passport5.getText().toString(), 
									namaTertanggung6.getText().toString(), dob6.getText().toString(), passport6.getText().toString(), 
									namaTertanggung7.getText().toString(), dob7.getText().toString(), passport7.getText().toString(), 
									namaTertanggung8.getText().toString(), dob8.getText().toString(), passport8.getText().toString(), 
									namaTertanggung9.getText().toString(), dob9.getText().toString(), passport9.getText().toString(), 
									namaTertanggung10.getText().toString(), dob10.getText().toString(), passport10.getText().toString(),
									swInsured1.isChecked() ? 1 : 0,
									swInsured2.isChecked() ? 1 : 0,
									swInsured3.isChecked() ? 1 : 0,
									swInsured4.isChecked() ? 1 : 0,
									swInsured5.isChecked() ? 1 : 0,
									swInsured6.isChecked() ? 1 : 0,
									swInsured7.isChecked() ? 1 : 0,
									swInsured8.isChecked() ? 1 : 0,
									swInsured9.isChecked() ? 1 : 0,
									swInsured10.isChecked() ? 1 : 0,
									insuredCount
									);
						 }
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (dba != null)
						dba.close();
				}
				
				
				Intent i = new Intent(FillInsuredGroupActivity.this, FillTravelActivity.class);
				Bundle b = new Bundle();
				b.putLong(TAG_BUNDLE_PRODUCT_MAIN_ID, productMainId);
				b.putInt(TAG_BUNDLE_INSURED_COUNT, insuredCount);
				i.putExtras(b);
				setResult(101, i);
				FillInsuredGroupActivity.this.finish();
			}
		});
		dob1.setOnClickListener(dobOnclickListener(dob1)); 
		dob2.setOnClickListener(dobOnclickListener(dob2));
		dob3.setOnClickListener(dobOnclickListener(dob3));
		dob4.setOnClickListener(dobOnclickListener(dob4));
		dob5.setOnClickListener(dobOnclickListener(dob5));
		dob6.setOnClickListener(dobOnclickListener(dob6));
		dob7.setOnClickListener(dobOnclickListener(dob7));
		dob8.setOnClickListener(dobOnclickListener(dob8));
		dob9.setOnClickListener(dobOnclickListener(dob9));
		dob10.setOnClickListener(dobOnclickListener(dob10));
		
		
	}

	private OnClickListener dobOnclickListener(final EditText dob) {
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Calendar c  = Calendar.getInstance();
				
				int Y = c.get(Calendar.YEAR);
				int M = c.get(Calendar.MONTH);
				int D = c.get(Calendar.DAY_OF_MONTH);
				
				Dialog dialog = myDatePickerDialog(dob, Y-20, M, D); 
				dialog.show();
			}
		};
	}
 

	private Dialog myDatePickerDialog(final EditText dob, int year, int month, int day) {
		
		DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
			public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

				String myDate = Utility.setUIDate(selectedYear, selectedMonth, selectedDay);
				boolean flag = Utility.validasiEffDate(Utility.GetTodayString(), myDate, FillInsuredGroupActivity.this);
				
				
				if (flag)
					dob.setText(myDate);
				else
					Utility.showCustomDialogInformation(FillInsuredGroupActivity.this, "Informasi", "Tanggal maksimal hari ini");
					
			}
		};
		
		return new DatePickerDialog(FillInsuredGroupActivity.this, datePickerListener, year, month, day);
	}


	private void initControl() {

		namaTertanggung1 = (EditText)findViewById(R.id.txtNamaTertanggung1);
		namaTertanggung2 = (EditText)findViewById(R.id.txtNamaTertanggung2);
		namaTertanggung3 = (EditText)findViewById(R.id.txtNamaTertanggung3);
		namaTertanggung4 = (EditText)findViewById(R.id.txtNamaTertanggung4);
		namaTertanggung5 = (EditText)findViewById(R.id.txtNamaTertanggung5);
		namaTertanggung6 = (EditText)findViewById(R.id.txtNamaTertanggung6);
		namaTertanggung7 = (EditText)findViewById(R.id.txtNamaTertanggung7);
		namaTertanggung8 = (EditText)findViewById(R.id.txtNamaTertanggung8);
		namaTertanggung9 = (EditText)findViewById(R.id.txtNamaTertanggung9);
		namaTertanggung10 = (EditText)findViewById(R.id.txtNamaTertanggung10);
		namaTertanggung11 = (EditText)findViewById(R.id.txtNamaTertanggung11);
		namaTertanggung12 = (EditText)findViewById(R.id.txtNamaTertanggung12);
		namaTertanggung13 = (EditText)findViewById(R.id.txtNamaTertanggung13);
		namaTertanggung14 = (EditText)findViewById(R.id.txtNamaTertanggung14);
		namaTertanggung15 = (EditText)findViewById(R.id.txtNamaTertanggung15);
		
		dob1 = (EditText)findViewById(R.id.txtDOB1);
		dob2 = (EditText)findViewById(R.id.txtDOB2);
		dob3 = (EditText)findViewById(R.id.txtDOB3);
		dob4 = (EditText)findViewById(R.id.txtDOB4);
		dob5 = (EditText)findViewById(R.id.txtDOB5);
		dob6 = (EditText)findViewById(R.id.txtDOB6);
		dob7 = (EditText)findViewById(R.id.txtDOB7);
		dob8 = (EditText)findViewById(R.id.txtDOB8);
		dob9 = (EditText)findViewById(R.id.txtDOB9);
		dob10 = (EditText)findViewById(R.id.txtDOB10);
		dob11 = (EditText)findViewById(R.id.txtDOB11);
		dob12 = (EditText)findViewById(R.id.txtDOB12);
		dob13 = (EditText)findViewById(R.id.txtDOB13);
		dob14 = (EditText)findViewById(R.id.txtDOB14);
		dob15 = (EditText)findViewById(R.id.txtDOB15);
		
		dob1.setFocusable(false);
		dob2.setFocusable(false);
		dob3.setFocusable(false);
		
		passport1 = (EditText)findViewById(R.id.txtPassport1);
		passport2 = (EditText)findViewById(R.id.txtPassport2);
		passport3 = (EditText)findViewById(R.id.txtPassport3);
		passport4 = (EditText)findViewById(R.id.txtPassport4);
		passport5 = (EditText)findViewById(R.id.txtPassport5);
		passport6 = (EditText)findViewById(R.id.txtPassport6);
		passport7 = (EditText)findViewById(R.id.txtPassport7);
		passport8 = (EditText)findViewById(R.id.txtPassport8);
		passport9 = (EditText)findViewById(R.id.txtPassport9);
		passport10 = (EditText)findViewById(R.id.txtPassport10);
		passport11 = (EditText)findViewById(R.id.txtPassport11);
		passport12 = (EditText)findViewById(R.id.txtPassport12);
		passport13 = (EditText)findViewById(R.id.txtPassport13);
		passport14 = (EditText)findViewById(R.id.txtPassport14);
		passport15 = (EditText)findViewById(R.id.txtPassport15);
		 
		
		swInsured1 = (Switch)findViewById(R.id.swInsured1);
		swInsured2 = (Switch)findViewById(R.id.swInsured2);
		swInsured3 = (Switch)findViewById(R.id.swInsured3);
		swInsured4 = (Switch)findViewById(R.id.swInsured4);
		swInsured5 = (Switch)findViewById(R.id.swInsured5);
		swInsured6 = (Switch)findViewById(R.id.swInsured6);
		swInsured7 = (Switch)findViewById(R.id.swInsured7);
		swInsured8 = (Switch)findViewById(R.id.swInsured8);
		swInsured9 = (Switch)findViewById(R.id.swInsured9);
		swInsured10 = (Switch)findViewById(R.id.swInsured10);
		
		swInsured1.setChecked(false);
		swInsured2.setChecked(false);
		swInsured3.setChecked(false);
		swInsured4.setChecked(false);
		swInsured5.setChecked(false);
		swInsured6.setChecked(false);
		swInsured7.setChecked(false);
		swInsured8.setChecked(false);
		swInsured9.setChecked(false);
		swInsured10.setChecked(false);
		
		namaTertanggung1.setEnabled(false);
		namaTertanggung2.setEnabled(false);
		namaTertanggung3.setEnabled(false);
		namaTertanggung4.setEnabled(false);
		namaTertanggung5.setEnabled(false);
		namaTertanggung6.setEnabled(false);
		namaTertanggung7.setEnabled(false);
		namaTertanggung8.setEnabled(false);
		namaTertanggung9.setEnabled(false);
		namaTertanggung10.setEnabled(false);
		namaTertanggung11.setEnabled(false);
		namaTertanggung12.setEnabled(false);
		namaTertanggung13.setEnabled(false);
		namaTertanggung14.setEnabled(false);
		namaTertanggung15.setEnabled(false);

	    dob1.setEnabled(false);
		dob2.setEnabled(false);
		dob3.setEnabled(false);
		dob4.setEnabled(false);
		dob5.setEnabled(false);
		dob6.setEnabled(false);
		dob7.setEnabled(false);
		dob8.setEnabled(false);
		dob9.setEnabled(false);
		dob10.setEnabled(false);
		dob11.setEnabled(false);
		dob12.setEnabled(false);
		dob13.setEnabled(false);
		dob14.setEnabled(false);
		dob15.setEnabled(false);

        passport1.setEnabled(false);
		passport2.setEnabled(false);
		passport3.setEnabled(false);
		passport4.setEnabled(false);
		passport5.setEnabled(false);
		passport6.setEnabled(false);
		passport7.setEnabled(false);
		passport8.setEnabled(false);
		passport9.setEnabled(false);
		passport10.setEnabled(false);
		passport11.setEnabled(false);
		passport12.setEnabled(false);
		passport13.setEnabled(false);
		passport14.setEnabled(false);
		passport15.setEnabled(false);

		
		swInsured1.setOnCheckedChangeListener(coverCheckedChange(namaTertanggung1, dob1, passport1));
		swInsured2.setOnCheckedChangeListener(coverCheckedChange(namaTertanggung2, dob2, passport2));
		swInsured3.setOnCheckedChangeListener(coverCheckedChange(namaTertanggung3, dob3, passport3));
		swInsured4.setOnCheckedChangeListener(coverCheckedChange(namaTertanggung4, dob4, passport4));
		swInsured5.setOnCheckedChangeListener(coverCheckedChange(namaTertanggung5, dob5, passport5));
		swInsured6.setOnCheckedChangeListener(coverCheckedChange(namaTertanggung6, dob6, passport6));
		swInsured7.setOnCheckedChangeListener(coverCheckedChange(namaTertanggung7, dob7, passport7));
		swInsured8.setOnCheckedChangeListener(coverCheckedChange(namaTertanggung8, dob8, passport8));
		swInsured9.setOnCheckedChangeListener(coverCheckedChange(namaTertanggung9, dob9, passport9));
		swInsured10.setOnCheckedChangeListener(coverCheckedChange(namaTertanggung10, dob10, passport10));
		
		
	    btnBack = (Button)findViewById(R.id.btnBack);
	}

	private OnCheckedChangeListener coverCheckedChange(final EditText nama,final EditText dob, final EditText passport) {
		return new OnCheckedChangeListener() { 
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (!isChecked) {
					nama.setEnabled(false);
					dob.setEnabled(false);
					passport.setEnabled(false);

					insuredCount--;
					
					nama.setText("");
					dob.setText("");
					passport.setText(""); 
				}
				else {
					insuredCount++;
					
					nama.setEnabled(true);
					dob.setEnabled(true);
					passport.setEnabled(true);					
				}
			}
		};
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) { 
		getMenuInflater().inflate(R.menu.fill_insured_group, menu);
		return true;
	}
	

	public void btnHomeClick(View v) {
		Intent i = new Intent(getBaseContext(), FirstActivity.class);
		startActivity(i);
		this.finish();
	}

}
