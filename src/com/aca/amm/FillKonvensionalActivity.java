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
import com.aca.amm.RetrieveOtomatePA.OtomateTPLListener;
import com.aca.database.DBA_MASTER_KONVE_RATE;
import com.aca.database.DBA_MASTER_OTOMATE_RATE;
import com.aca.database.DBA_MASTER_PRODUCT_SETTING;
import com.aca.database.DBA_PRODUCT_CONVENSIONAL;
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
import android.widget.CompoundButton;
import android.widget.AdapterView.OnItemSelectedListener; 
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
 

@SuppressLint("DefaultLocale")
public class FillKonvensionalActivity extends ControlNormalActivity {


	private Bundle b;
	private Calendar c;
	private Context context = null;
	private NumberFormat nf;
	
	private boolean isTruk = false;

	
	private double  v_rate = 0,
			v_tsi = 0,
			v_premi = 0,
			v_polis = 0,
			v_materai = 0,
			v_total = 0,
			v_discpct = 0, 
			v_disc = 0,
			MAX_DISCOUNT = 0;
	
	
	private int flag = 0,
				maxTahun = 7,
				flagLoad = 0;

	private final int DATE_FROM_ID = 99,
					  DATE_TO_ID = 100;
	private long SPPA_ID;
 

	private String  carBrand = "", 
					car_seat = "1",
					carType = "",
					carIDBrand, 
					carIDType,
					PRODUCT_ACTION,
					vehicle_category  = "1",
					UIErrorMessage = "Lengkapi semua data",
					tipeKonvensional = "";
	
	private EditText 
			nopol1, 
			nopol2, 
			nopol3, 
			model, 
			tahun, 
			chassisNo, 
			machineNo, 
			color, 
			perlengkapan, 
			seat, 
			jangkaWaktuEff, 
			jangkaWaktuExp, 
			nilaiPertanggungan, 
			nilaiPerlengkapan, 
			risikoSendiri, 
			keteranganKerusakan,
			rate, 
			premi, 
			polis, 
			materai, 
			total, 
			customerno,
			customername,
			discpct, 
			disc,			
			tipeKendaraan, 
			merkKendaraan;
	
	private LinearLayout layLoading5tahun;
	
	private RadioButton rbLoadingRate,
						rbLoadingResikoSendiri,
						rbRateBawah, 
						rbRateTengah, 
						rbRateAtas,
						rbTruk,
						rbNonTruk;

	private RadioGroup rgRate,
						rgJenisKendaraan;
 

	private Switch  AOG, 
					AccType, 
					swiKerusakan, 
					EQ, 
					SRCC, 
					TS;

	private Spinner spnTpl, 
					spnPA, 
					spnPenggunaan,
					spnDNC;

	
	
	ArrayList<HashMap<String, String>> custList; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_fill_konvensional);

		context = FillKonvensionalActivity.this;
		nf = NumberFormat.getInstance();

		InitEditText(); 
		RegisterListener(); 
		init(); 
	}

	private void getKonvensionalType() {
		if (	PRODUCT_ACTION.equals("EDIT") 
			|| PRODUCT_ACTION.equals("VIEW")
			|| PRODUCT_ACTION.equals("VIEW.UNPAID"))
		{
			SPPA_ID = b.getLong("SPPA_ID");
			
			DBA_PRODUCT_CONVENSIONAL dba = new DBA_PRODUCT_CONVENSIONAL(
					this);
			Cursor c = null;

			try {
				dba.open();
				c = dba.getRow(SPPA_ID);

				if (!c.moveToFirst())
					return;

//				tipeKonvensional = c.getStin(24);
				

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (dba != null)
					dba.close();

				if (c != null)
					c.close();

			}
		}
	}

	private void init() {
		try {

			Intent i = getIntent();
			b = i.getExtras();

			PRODUCT_ACTION = b.getString("PRODUCT_ACTION");
			tipeKonvensional = b.getString("TIPE_KONVENSIONAL");
			
			Log.i(TAG, "::onCreate:" + "tipe konvensional :  " + tipeKonvensional);
			
			if (tipeKonvensional.equalsIgnoreCase("COMPREHENSIVE")) {
				((TextView)findViewById(R.id.lblTitle)).setText("Asuransi Kendaraan Konvensional Comprehensive");
				maxTahun = 7;
			}
			else if (tipeKonvensional.equalsIgnoreCase("TLO")) {
				((TextView)findViewById(R.id.lblTitle)).setText("Asuransi Kendaraan Konvensional TLO");
				maxTahun = 15;
				disablePerluasanJaminan();
			}

			if (PRODUCT_ACTION.equals("EDIT") || PRODUCT_ACTION.equals("VIEW")) {
				SPPA_ID = b.getLong("SPPA_ID");
				LoadDB();
			} else if (PRODUCT_ACTION.equals("VIEW.UNPAID")) { 
				SPPA_ID = b.getLong("SPPA_ID");
				LoadDB();
				disableView();
			} else {
				findViewById(R.id.btnDelete).setVisibility(View.GONE);
			}

			if (b.getString("CUSTOMER_NO") != null) {
				customerno.setText(b.getString("CUSTOMER_NO"));
				customername.setText(b.getString("CUSTOMER_NAME"));
			}

			MAX_DISCOUNT = Utility.getMaxDiscount(getBaseContext(), "21");

			if (Utility.getIsDiscountable(getBaseContext(), "03").equals("0")) {
				disc.setEnabled(false);
				discpct.setEnabled(false);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private void disableView() {
		// TODO Auto-generated method stub
		nopol1.setEnabled(false);
		nopol2.setEnabled(false);
		nopol3.setEnabled(false);
		model.setEnabled(false);
		tahun.setEnabled(false);
		chassisNo.setEnabled(false);
		machineNo.setEnabled(false);
		color.setEnabled(false);
		perlengkapan.setEnabled(false);
		seat.setEnabled(false);
		jangkaWaktuEff.setEnabled(false);
		jangkaWaktuExp.setEnabled(false);
		nilaiPertanggungan.setEnabled(false);
		nilaiPerlengkapan.setEnabled(false);
		spnTpl.setEnabled(false);
		spnPA.setEnabled(false);
		rate.setEnabled(false);
		premi.setEnabled(false);
		polis.setEnabled(false);
		materai.setEnabled(false);
		total.setEnabled(false);
		customerno.setEnabled(false);
		customername.setEnabled(false);
		discpct.setEnabled(false);
		disc.setEnabled(false);
		tipeKendaraan.setEnabled(false);
		merkKendaraan.setEnabled(false);
		
		swiKerusakan.setEnabled(false);
		SRCC.setEnabled(false);
		TS.setEnabled(false);
		AOG.setEnabled(false);
		EQ.setEnabled(false);;
		rbRateAtas.setEnabled(false);
		rbRateBawah.setEnabled(false);
		rbRateTengah.setEnabled(false);
		
		spnDNC.setEnabled(false);
		spnPA.setEnabled(false);
		spnTpl.setEnabled(false);
		risikoSendiri.setEnabled(false);
		keteranganKerusakan.setEnabled(false);
		 
		nopol1.setTextColor(Color.BLACK);
		nopol2.setTextColor(Color.BLACK);
		nopol3.setTextColor(Color.BLACK);
		model.setTextColor(Color.BLACK);
		tahun.setTextColor(Color.BLACK);
		chassisNo.setTextColor(Color.BLACK);
		machineNo.setTextColor(Color.BLACK);
		color.setTextColor(Color.BLACK);
		perlengkapan.setTextColor(Color.BLACK);
		seat.setTextColor(Color.BLACK);
		jangkaWaktuEff.setTextColor(Color.BLACK);
		jangkaWaktuExp.setTextColor(Color.BLACK);
		nilaiPertanggungan.setTextColor(Color.BLACK);
		nilaiPerlengkapan.setTextColor(Color.BLACK); 
		rate.setTextColor(Color.BLACK);
		premi.setTextColor(Color.BLACK);
		polis.setTextColor(Color.BLACK);
		materai.setTextColor(Color.BLACK);
		total.setTextColor(Color.BLACK);
		customerno.setTextColor(Color.BLACK);
		customername.setTextColor(Color.BLACK);
		discpct.setTextColor(Color.BLACK);
		disc.setTextColor(Color.BLACK);
		tipeKendaraan.setTextColor(Color.BLACK);
		merkKendaraan.setTextColor(Color.BLACK);

		(findViewById(R.id.btnChooseCustomer)).setEnabled(false);
		(findViewById(R.id.swiAccType)).setEnabled(false);
		(findViewById(R.id.swiAOG)).setEnabled(false);
 

		swiKerusakan.setTextColor(Color.BLACK);
		SRCC.setTextColor(Color.BLACK);
		TS.setTextColor(Color.BLACK);
		AOG.setTextColor(Color.BLACK);
		EQ.setTextColor(Color.BLACK); 
		
		risikoSendiri.setTextColor(Color.BLACK);
		keteranganKerusakan.setTextColor(Color.BLACK);
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == 100 && requestCode == 200) {
			carBrand = data.getStringExtra("CAR_BRAND");
			carIDBrand = data.getStringExtra("CAR_ID");
			isTruk = data.getBooleanExtra("ISTRUK", false);

			if (carBrand != null && carIDBrand != null) {
				merkKendaraan.setText(carBrand);
				merkKendaraan.setTag(carIDBrand);
				tipeKendaraan.setText("");
				tipeKendaraan.setEnabled(true);
			}
		}

		else if (resultCode == 10 && requestCode == 20) {

			carType = data.getStringExtra("CAR_TYPE");
			carIDType = data.getStringExtra("CAR_ID");

			if (carType != null && carIDType != null) {
				tipeKendaraan.setText(carType);
				tipeKendaraan.setTag(carIDType);
				
				
			
//				blockLCGC(carType);
				setRateKendaraanKhusus(carType);
				 
				GetVehicleCategory ws = new GetVehicleCategory(FillKonvensionalActivity.this, 
						merkKendaraan.getTag().toString(), 
						tipeKendaraan.getTag().toString());
				
				ws.execute();
				
				GetCarSeat wsSeat = new GetCarSeat(FillKonvensionalActivity.this, 
						tipeKendaraan.getTag().toString(),
						merkKendaraan.getTag().toString());
				
				wsSeat.execute();
			}
		}
	}

	private void setRateKendaraanKhusus(String carType) { 
		Log.i(TAG, "::setRateKendaraanKhusus:" + "car type :" + carType);
		
		if (tipeKonvensional.equalsIgnoreCase("TLO"))
			return;
		
		if (carType.equalsIgnoreCase("HARRIER") ||
			carType.equalsIgnoreCase("ALPHARD") ||
			carType.equalsIgnoreCase("VELLFIRE")||
			carType.equalsIgnoreCase("ELGRAND")) {
				rbRateBawah.setEnabled(false);
				rbRateTengah.setEnabled(false);
				
				rbRateAtas.setChecked(true);
			}
			else {
				rbRateBawah.setEnabled(true);
				rbRateTengah.setEnabled(true); 
			}

	}

	private double getRatePerluasan () {
		String jaminan = getTipeKonven();
		String wilayah = getWilayah(nopol1.getText().toString().toUpperCase()); 
		String vehicle_category = "0"; 

		double rateFlood =  AOG.isChecked() ?  getRatePerluasan(jaminan, wilayah, vehicle_category, "1", "0") : 0;
		double rateEQ = 	EQ.isChecked() ?   getRatePerluasan(jaminan, wilayah, vehicle_category, "2", "0") : 0;
		double rateSRCC = 	SRCC.isChecked() ? getRatePerluasan(jaminan, "0", vehicle_category, "3", "0") : 0;
		double rateTS = 	TS.isChecked() ?   getRatePerluasan(jaminan, "0", vehicle_category, "4", "0") : 0;

		Log.i(TAG, "::getRatePerluasan:" + "rate flood : " + rateFlood);
		Log.i(TAG, "::getRatePerluasan:" + "rate eq : " + rateEQ);
		Log.i(TAG, "::getRatePerluasan:" + "rate srcc : " + rateSRCC);
		Log.i(TAG, "::getRatePerluasan:" + "rate ts : " + rateTS);
		
		
		return rateFlood + rateEQ + rateSRCC + rateTS;

	}
	private void calculateAll() throws ParseException, InterruptedException {
		v_tsi = getTSI();
		v_rate = getRate();
		v_rate = v_rate  + getRatePerluasan();
		v_premi = getPremi();
		v_discpct = Utility.parseDouble(discpct);
		v_disc = Utility.countDiscount(v_premi, v_discpct);
		v_polis = 0;
		v_materai = Utility.countMaterai(v_premi);
		v_total = Utility.countTotal(v_premi, v_polis, v_materai, v_disc);

		NumberFormat nfRate = NumberFormat.getInstance();
		nfRate.setMinimumFractionDigits(5);
		
		
		rate.setText(nfRate.format(v_rate));
		premi.setText(nf.format(v_premi));
		disc.setText(nf.format(v_disc));
		polis.setText(nf.format(v_polis));
		materai.setText(nf.format(v_materai));
		total.setText(nf.format(v_total));
	}

	private void RegisterListener() {
		rgJenisKendaraan.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (rbTruk.isChecked())
					isTruk = true;
				else 
					isTruk = false;
				
				merkKendaraan.setText("");			
				merkKendaraan.setTag("");
				tipeKendaraan.setText("");
					
			}
		}); 
		
		swiKerusakan.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					(findViewById(R.id.sectionKerusakan)).setVisibility(View.VISIBLE);
				}
				else {
					(findViewById(R.id.sectionKerusakan)).setVisibility(View.GONE);
				}

				keteranganKerusakan.setText("");
			}
		});



		rbLoadingResikoSendiri.setOnCheckedChangeListener(new OnCheckedChangeListener() { 
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked && layLoading5tahun.getVisibility() == View.VISIBLE) 
				{
					risikoSendiri.setText(nf.format(500000));
				}
				else
				{
					risikoSendiri.setText(nf.format(300000));					
				}
			}
		});
		
		rbLoadingRate.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				try {
					calculateAll();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		
		risikoSendiri.addTextChangedListener(new TextWatcher() {
			private String current = "";

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
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

				risikoSendiri.setTextColor(Color.BLACK);
				try {
					if (!s.toString().equals(current)) {
						risikoSendiri.removeTextChangedListener(this);

						String replaceable = String.format("[%s,.\\s]", "Rp. ");
						String cleanString = s.toString().replaceAll(
								replaceable, "");

						double parsed;
						try {
							parsed = Double.parseDouble(cleanString);
						} catch (NumberFormatException e) {
							parsed = 0.00;
						}
						String formatted = nf.format(parsed);

						current = formatted;
						risikoSendiri.setText(formatted);
						risikoSendiri.setSelection(formatted.length());
						risikoSendiri.addTextChangedListener(this);

//						calculateAll();
//						validasiNilaiTSI();

					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		
		nilaiPertanggungan.addTextChangedListener(new TextWatcher() {
			private String current = "";

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
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

				nilaiPertanggungan.setTextColor(Color.BLACK);
				try {
					if (!s.toString().equals(current)) {
						nilaiPertanggungan.removeTextChangedListener(this);

						String replaceable = String.format("[%s,.\\s]", "Rp. ");
						String cleanString = s.toString().replaceAll(
								replaceable, "");

						double parsed;
						try {
							parsed = Double.parseDouble(cleanString);
						} catch (NumberFormatException e) {
							parsed = 0.00;
						}
						String formatted = nf.format(parsed);

						current = formatted;
						nilaiPertanggungan.setText(formatted);
						nilaiPertanggungan.setSelection(formatted.length());
						nilaiPertanggungan.addTextChangedListener(this);

						calculateAll();
						// validasiNilaiTSI();

					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		nilaiPerlengkapan.addTextChangedListener(new TextWatcher() {
			private String current = "";

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
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

				nilaiPerlengkapan.setTextColor(Color.BLACK);
				try {
					if (!s.toString().equals(current)) {
						nilaiPerlengkapan.removeTextChangedListener(this);

						String replaceable = String.format("[%s,.\\s]", "Rp. ");
						String cleanString = s.toString().replaceAll(
								replaceable, "");

						double parsed;
						try {
							parsed = Double.parseDouble(cleanString);
						} catch (NumberFormatException e) {
							parsed = 0.00;
						}
						String formatted = nf.format(parsed);

						current = formatted;
						nilaiPerlengkapan.setText(formatted);
						nilaiPerlengkapan.setSelection(formatted.length());
						nilaiPerlengkapan.addTextChangedListener(this);

						calculateAll();
						// validasiNilaiTSI();

					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		
		spnTpl.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				try {
					if (!nilaiPertanggungan.getText().toString().isEmpty())
						calculateAll();

				} catch (ParseException e) { 
					e.printStackTrace();
				} catch (InterruptedException e) { 
					e.printStackTrace();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		
		spnPA.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				try {
					if (!nilaiPertanggungan.getText().toString().isEmpty())
						calculateAll();

				} catch (ParseException e) { 
					e.printStackTrace();
				} catch (InterruptedException e) { 
					e.printStackTrace();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		
		spnDNC.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				try {
					if (!nilaiPertanggungan.getText().toString().isEmpty())
						calculateAll();

				} catch (ParseException e) { 
					e.printStackTrace();
				} catch (InterruptedException e) { 
					e.printStackTrace();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		
		

		tahun.addTextChangedListener(new TextWatcher() {
			private String current = "";

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) { 

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) { 

			}

			@Override
			public void afterTextChanged(Editable s) {  
				tahun.setTextColor(Color.BLACK);
				try {
					if (!s.toString().equals(current)) {
						if (s.toString().length() == 4) 
						{
						 	if (isTruk) {
						 		if (diffYear() >= 10  && tipeKonvensional.equalsIgnoreCase("TLO")) {
						 			layLoading5tahun.setVisibility(View.VISIBLE);
									rbLoadingRate.setChecked(true);
						 		} 
						 	}
							else {
								if (diffYear() > 5 && tipeKonvensional.equalsIgnoreCase("COMPREHENSIVE")) 
								{
									layLoading5tahun.setVisibility(View.VISIBLE);
									rbLoadingRate.setChecked(true);
								}
								else
								{
									layLoading5tahun.setVisibility(View.GONE);
									risikoSendiri.setText(nf.format(300000));								
								}
							}
							
						} 

						calculateAll();
						// validasiNilaiTSI();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		nopol1.addTextChangedListener(new TextWatcher() {
			private String current = "";

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
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

				nopol1.setTextColor(Color.BLACK);
				try {
					if (!s.toString().equals(current)) {
						calculateAll();
						// validasiNilaiTSI();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		jangkaWaktuEff.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View arg0, boolean hasFocus) {
				if (hasFocus)
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
					if (!nilaiPertanggungan.getText().toString().isEmpty())
						calculateAll();
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		
		EQ.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				try {
					if (!nilaiPertanggungan.getText().toString().isEmpty())
						calculateAll();
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		
		SRCC.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				try {
					if (!SRCC.isChecked()) { 
						TS.setChecked(false);
					}
					if (!nilaiPertanggungan.getText().toString().isEmpty())
						calculateAll();
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		
		TS.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton cb, boolean checked) {
				try {
					if (checked && !SRCC.isChecked()) {
						SRCC.setChecked(true);
						Utility.showCustomDialogInformation(FillKonvensionalActivity.this, "Informasi", 
							"Huru Hara harus dipilih jika Terorisme dan Sabotase dipilih");						 
					}
					if (!nilaiPertanggungan.getText().toString().isEmpty())
						calculateAll();
				
					
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		});
		
		
	 
		
		rgRate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				try {
					if (!nilaiPertanggungan.getText().toString().isEmpty())
						calculateAll();
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		});
		 
		discpct.addTextChangedListener(new TextWatcher() { 

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) { 

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) { 
			}

			@Override
			public void afterTextChanged(Editable s) { 
				try {
					discpct.setTextColor(Color.BLACK);
					if (!nilaiPertanggungan.getText().toString().isEmpty() && !tahun.getText().toString().isEmpty())
						calculateAll();
				} catch (ParseException e) { 
					e.printStackTrace();
				} catch (InterruptedException e) { 
					e.printStackTrace();
				} 
			}
		});
		
 
		merkKendaraan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				showJenisKendaraan();
				
			}
		});

		merkKendaraan.setKeyListener(null);

		tipeKendaraan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getBaseContext(),
						ChooseCarTypeActivity.class);
				b.putString("BRAND_CODE", carIDBrand);
				i.putExtras(b);
				startActivityForResult(i, 20);
			}
		});

		tipeKendaraan.setKeyListener(null);
	}

	private void validasiTanggal() {
		((Button) findViewById(R.id.btnNext)).setEnabled(true);
		jangkaWaktuEff.setTextColor(Color.BLACK);
		boolean flag = Utility.validasiEffDate(jangkaWaktuEff.getText()
				.toString(),
				Utility.GetTomorrowString(Utility.GetTodayString()),
				getBaseContext());

		if (!flag) {
			jangkaWaktuEff.setTextColor(Color.RED);
			Utility.showCustomDialogInformation(FillKonvensionalActivity.this,
					"Warning", "Tanggal mulai minimal besok hari");
			((Button) findViewById(R.id.btnNext)).setEnabled(false);
		}
	}

	// private double validasiUmur() {
	// Calendar c = Calendar.getInstance();
	// int todayYear = c.get(Calendar.YEAR);
	//
	// if (tahun.getText().toString().isEmpty())
	// return 0.0;
	//
	// int vtahun = Integer.parseInt(tahun.getText().toString());
	// double loadingrate = 0.0 ;
	//
	// if (vtahun+ maxTahun < todayYear){
	// tahun.setTextColor(Color.RED);
	// flag ++ ;
	//
	// Utility.showCustomDialogInformation(FillConventionalActivity.this,
	// "Informasi",
	// "Umur kendaran maksimal 7 tahun");
	// }
	// else if ((todayYear-vtahun) == 6 ){
	// loadingrate = 5/100;
	// }
	// else if ((todayYear-vtahun) == 7 ){
	// loadingrate = 10/100;
	// }
	// return loadingrate;
	// }

	private void InitEditText() {

		customerno		= (EditText) findViewById(R.id.txtCustomerNo);
		customername	= (EditText) findViewById(R.id.txtCustomerName);
		model			= (EditText) findViewById(R.id.txtModel);
		tahun 			= (EditText) findViewById(R.id.txtYear);
		merkKendaraan 	= (EditText) findViewById(R.id.txtMake);
		tipeKendaraan 	= (EditText) findViewById(R.id.txtType);
		nopol1 			= (EditText) findViewById(R.id.txtNopol1);
		nopol2 			= (EditText) findViewById(R.id.txtNopol2);
		nopol3			= (EditText) findViewById(R.id.txtNopol3);
		chassisNo		= (EditText) findViewById(R.id.txtChassisNo);
		machineNo		= (EditText) findViewById(R.id.txtMachineNo);
		color 			= (EditText) findViewById(R.id.txtColor);
		perlengkapan 	= (EditText) findViewById(R.id.txtPerlengkapan);
		seat			= (EditText) findViewById(R.id.txtSeat);
		risikoSendiri 	= (EditText) findViewById(R.id.txtRisiko);
		risikoSendiri.setText(nf.format(300000));

		
		keteranganKerusakan = (EditText)findViewById(R.id.txtKerusakan);
		(findViewById(R.id.sectionKerusakan)).setVisibility(View.GONE);
		

		jangkaWaktuEff  = (EditText) findViewById(R.id.txtEffDate);
		jangkaWaktuExp  = (EditText) findViewById(R.id.txtExpDate);
		jangkaWaktuExp.setKeyListener(null);

		nilaiPertanggungan = (EditText) findViewById(R.id.txtNilaiPertanggungan);
		nilaiPerlengkapan  = (EditText) findViewById(R.id.txtNilaiPerlengkapan); 
		
		rate		= (EditText) findViewById(R.id.txtRate);
		premi		= (EditText) findViewById(R.id.txtPremi);
		discpct 	= (EditText) findViewById(R.id.txtDiscountPct); 
		disc 		= (EditText) findViewById(R.id.txtDiscount);
		materai 	= (EditText) findViewById(R.id.txtMaterai);
		polis		= (EditText) findViewById(R.id.txtPolicyCost);
		total 		= (EditText) findViewById(R.id.txtTotal);
		
		discpct.setText(nf.format(0.0));
		
		layLoading5tahun = (LinearLayout) findViewById(R.id.sectionLoading5tahun);
		
		
		rbRateAtas 				 = (RadioButton) findViewById(R.id.rbRateAtas);
		rbRateTengah			 = (RadioButton) findViewById(R.id.rbRateTengah);
		rbRateBawah				 = (RadioButton) findViewById(R.id.rbRateBawah);
		rbLoadingRate 		     = (RadioButton)findViewById(R.id.rbLoadingRate);
		rbLoadingResikoSendiri   = (RadioButton)findViewById(R.id.rbLoadingResikoSendiri);
		rbTruk					 = (RadioButton)findViewById(R.id.rbTruk);
		rbNonTruk			   	 = (RadioButton)findViewById(R.id.rbNonTruk);
		
		rgRate 			 = (RadioGroup)findViewById(R.id.rgRateChoice);
		rgJenisKendaraan = (RadioGroup)findViewById(R.id.rgJenisKendaraan);
		

		spnTpl 			   = (Spinner) findViewById(R.id.spnTPL);
		spnPA 			   = (Spinner) findViewById(R.id.spnPA);
		spnDNC 			   = (Spinner) findViewById(R.id.spnDNC);
		
		
		AOG 			= (Switch) findViewById(R.id.swiAOG);
		EQ 				= (Switch) findViewById(R.id.swiEQ);
		SRCC			= (Switch) findViewById(R.id.swiSRCC);
		TS 				= (Switch) findViewById(R.id.swiTS);
		swiKerusakan 	= (Switch) findViewById(R.id.swiKerusakan);
		AccType 		= (Switch) findViewById(R.id.swiAccType);

		Utility.BindTplKonve(spnTpl, FillKonvensionalActivity.this, FillKonvensionalActivity.this);
		Utility.BindPAKonve(spnPA, FillKonvensionalActivity.this, FillKonvensionalActivity.this);
		Utility.BindDNC(spnDNC, FillKonvensionalActivity.this, FillKonvensionalActivity.this);
		
	
	}

	private void LoadDB() {

		DBA_PRODUCT_MAIN dbm = null;
		DBA_PRODUCT_CONVENSIONAL dba = null;

		Cursor cm = null;
		Cursor c = null;

		try {

			dbm = new DBA_PRODUCT_MAIN(getBaseContext());
			dbm.open();
			cm = dbm.getRow(SPPA_ID);
			cm.moveToFirst();

			dba = new DBA_PRODUCT_CONVENSIONAL(getBaseContext());
			dba.open();
			c = dba.getRow(SPPA_ID);
			c.moveToFirst();

			customerno.setText(cm.getString(1));
			customername.setText(cm.getString(2));

			carIDBrand = c.getString(2);
			carIDType = c.getString(3);

			if (c.getString(24).equalsIgnoreCase("0"))
				tipeKonvensional = "COMPREHENSIVE";
			else
				tipeKonvensional = "TLO";
			
			if (tipeKonvensional.equalsIgnoreCase("COMPREHENSIVE")) {
				((TextView)findViewById(R.id.lblTitle)).setText("Konvensional Comprehensive");
				maxTahun = 7;
			}
			else if (tipeKonvensional.equalsIgnoreCase("TLO")){
				((TextView)findViewById(R.id.lblTitle)).setText("Konvensional TLO");
				disablePerluasanJaminan();
				maxTahun = 15;
			} 
			

			if (c.getInt(41) == 1)
				isTruk = true;
			else
				isTruk = false; 
			
			vehicle_category = c.getString(42);
			car_seat = c.getString(12);

			merkKendaraan.setTag(c.getString(2));
			merkKendaraan.setText(c.getString(34));
			tipeKendaraan.setTag(c.getString(3));
			tipeKendaraan.setText(c.getString(35));
			
			setRateKendaraanKhusus(c.getString(35));
			 

			flagLoad = 1;

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
			

			if (c.getString(17).equals("1"))
				AOG.setChecked(true);
			else
				AOG.setChecked(false);
			 
			if (c.getString(18).equals("1"))
				EQ.setChecked(true);
			else
				EQ.setChecked(false);
			 
			if (c.getString(19).equals("1"))
				SRCC.setChecked(true);
			else
				SRCC.setChecked(false);
			
			if (c.getString(20).equals("1"))
				TS.setChecked(true);
			else
				TS.setChecked(false);
			  
			
			SpinnerGenericAdapter a;
			
			a =  (SpinnerGenericAdapter)spnTpl.getAdapter();
			spnTpl.setSelection(a.getItemId(c.getString(21)));

			a =  (SpinnerGenericAdapter)spnPA.getAdapter();
			spnPA.setSelection(a.getItemId(c.getString(22)));

			if (c.getString(23).equals("3")) 
				rbRateAtas.setChecked(true);
			else if (c.getString(23).equals("2")) 
				rbRateTengah.setChecked(true);
			else if (c.getString(23).equals("1")) 
				rbRateBawah.setChecked(true);

		
			
			rate.setText(nf.format(c.getDouble(25)));
			premi.setText(nf.format(c.getDouble(26)));
			discpct.setText(nf.format(cm.getDouble(23)));
			disc.setText(nf.format(cm.getDouble(24)));
			polis.setText(nf.format(c.getDouble(27)));
			materai.setText(nf.format(c.getDouble(28)));
			total.setText(nf.format(c.getDouble(29)));

			model.setText(c.getString(32));

			if (c.getString(33).equals("1"))
				AccType.setChecked(true);
			else
				AccType.setChecked(false);
			
			a =  (SpinnerGenericAdapter)spnDNC.getAdapter();
			spnDNC.setSelection(a.getItemId(c.getString(36)));
			

			if (c.getString(38).equals("1"))
				 swiKerusakan.setChecked(true);
			else
				swiKerusakan.setChecked(false);
			
			risikoSendiri.setText(nf.format(c.getDouble(37)));
			keteranganKerusakan.setText(c.getString(39));
			
			if (c.getInt(40) != 1)
				rbLoadingRate.setChecked(true);
			else
				rbLoadingResikoSendiri.setChecked(true);
			
  
			
			if (cm.getString(20).equals("R")) {
				model.setEnabled(false);
				merkKendaraan.setEnabled(false);
				tipeKendaraan.setEnabled(false);
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

//				maxTahun = 7;
				customerno.setEnabled(false);
				((Button) findViewById(R.id.btnChooseCustomer))
						.setEnabled(false);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
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
		try {
			SPPA_ID = b.getLong("SPPA_ID");
			PRODUCT_ACTION = b.getString("PRODUCT_ACTION");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.fill_comprehensive, menu);
		return true;
	}

	public void btnHomeClick(View v) {
		Intent i = new Intent(getBaseContext(), FirstActivity.class);
		startActivity(i);
		this.finish();
	}

	private void Back() {
		try {

			Intent i = null;

			if (PRODUCT_ACTION.equals("NEW"))
				i = new Intent(getBaseContext(), ChooseProductActivity.class);
			else
				i = new Intent(getBaseContext(), SyncActivity.class);

			startActivity(i);
			this.finish();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void btnBackClick(View v) {
		Back();
	}

	public void btnDeleteClick(View v) {

		showConfirmDelete(getBaseContext(), SPPA_ID, "").show();
	}

	private AlertDialog showConfirmDelete(final Context ctx, final long rowId,
			final String noPolis) {
		AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
				.setTitle("Hapus")
				.setMessage("Hapus data ini sekarang?")
				.setIcon(R.drawable.delete)
				.setPositiveButton("Delete",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int whichButton) {

								new DeleteSPPA().execute();
								dialog.dismiss();
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						}).create();
		return myQuittingDialogBox;
	}

	@Override
	public void onBackPressed() {
		Back();
	}

	private void validasiNext() throws ParseException {
		flag = 0;

		if (customerno.getText().toString().isEmpty()) {
			flag++;
			customerno.setHintTextColor(Color.RED);
			UIErrorMessage = "Pilih customer";
			return;
		}
		if (customername.getText().toString().isEmpty()) {
			flag++;
			customername.setHintTextColor(Color.RED);
			UIErrorMessage = "Pilih customer";
			return;
		}
		if (merkKendaraan.getText().toString().isEmpty()) {
			flag++;
			merkKendaraan.setHintTextColor(Color.RED);
			UIErrorMessage = "Merk kendaraan harus dipilih";
			return;
		}
		if (tipeKendaraan.getText().toString().isEmpty()) {
			flag++;
			tipeKendaraan.setHintTextColor(Color.RED);
			UIErrorMessage = "Tipe kendaraan harus dipilih";
			return;
		}
		if (model.getText().toString().isEmpty()) {
			flag++;
			model.setHintTextColor(Color.RED);
			UIErrorMessage = "Model kendaraan harus diisi";
			return;
		}
		if (tahun.getText().toString().isEmpty()) {
			flag++;
			tahun.setHintTextColor(Color.RED);
			UIErrorMessage = "Tahun kendaraan harus diisi";
			return;
		}

		if (diffYear() < 0) {
			flag++;
			tahun.setTextColor(Color.RED);
			UIErrorMessage = "Tahun kendaraan tidak valid";
			return;			
		}
		

		if (isTruk) {
			if (diffYear() > 1 && tipeKonvensional.equalsIgnoreCase("COMPREHENSIVE")) 	{
				flag++;
				tahun.setTextColor(Color.RED);
				UIErrorMessage = "Umur Kendaraan maksimal 1 tahun";
				return;
			}
			else if (diffYear() > 15 && tipeKonvensional.equalsIgnoreCase("TLO")) 	{
				flag++;
				tahun.setTextColor(Color.RED);
				UIErrorMessage = "Umur Kendaraan maksimal 15 tahun";
				return;
			}
		}
		else {
			if (diffYear() > maxTahun) {
				flag++;
				tahun.setTextColor(Color.RED);
				UIErrorMessage = "Umur Kendaraan maksimal " +  maxTahun + "  tahun";
				return;
			}
			
		}
		
		
		
		
		

		if (nopol1.getText().toString().isEmpty()) {
			flag++;
			nopol1.setHintTextColor(Color.RED);
			UIErrorMessage = "No Polisi ke-1 harus diisi";
			return;
		}
		if (nopol2.getText().toString().isEmpty()) {
			flag++;
			nopol2.setHintTextColor(Color.RED);
			UIErrorMessage = "No Polisi ke-2 harus diisi";
			return;
		}

		String plat1 = nopol1.getText().toString().toUpperCase();

		if (!(plat1.equals("A")
				|| plat1.equals("B") 
				|| plat1.equals("C")
				|| plat1.equals("D") 
				|| plat1.equals("E") 
				|| plat1.equals("F")
				|| plat1.equals("T") 
				|| plat1.equals("Z")
				|| plat1.equals("G")
				|| plat1.equals("H")
				|| plat1.equals("K")
				|| plat1.equals("L")
				|| plat1.equals("M")
				|| plat1.equals("N")
				|| plat1.equals("P")
				|| plat1.equals("R")
				|| plat1.equals("S")
				|| plat1.equals("AA")
				|| plat1.equals("AB")
				|| plat1.equals("AD")
				|| plat1.equals("AE")
				|| plat1.equals("AG")
				|| plat1.equals("W")
				|| plat1.equals("DB")
				|| plat1.equals("DD")
				|| plat1.equals("DM")
				|| plat1.equals("DN")
				|| plat1.equals("DE")
				|| plat1.equals("DG")
				|| plat1.equals("DH")
				|| plat1.equals("DK")
				|| plat1.equals("DL")
				|| plat1.equals("DR")
				|| plat1.equals("DS")
				|| plat1.equals("EA")
				|| plat1.equals("EB")
				|| plat1.equals("ED")
				|| plat1.equals("DA") || plat1.equals("KB") || plat1.equals("KT")
				|| plat1.equals("BA") || plat1.equals("BB") || plat1.equals("BD")
				|| plat1.equals("BE") || plat1.equals("BG")
				|| plat1.equals("BH") || plat1.equals("BK")
				|| plat1.equals("BL") || plat1.equals("BM")
				|| plat1.equals("BP") || plat1.equals("BN")
				|| plat1.equals("CC") || plat1.equals("CD")
				)) {
			flag++;
			nopol1.setTextColor(Color.RED);
			UIErrorMessage = "Kode Plat No tidak valid untuk produk Otomate";
			return;
		}
 
		if (color.getText().toString().isEmpty()) {
			flag++;
			color.setHintTextColor(Color.RED);
			UIErrorMessage = "Warna harus diisi";
			return;
		}
		if (chassisNo.getText().toString().isEmpty()) {
			flag++;
			chassisNo.setHintTextColor(Color.RED);
			UIErrorMessage = "No Rangka harus diisi";
			return;
		}
		if (machineNo.getText().toString().isEmpty()) {
			flag++;
			machineNo.setHintTextColor(Color.RED);
			UIErrorMessage = "No Mesin harus diisi";
			return;
		}

		 if (perlengkapan.getText().toString().isEmpty()){
			 flag++ ;
			 perlengkapan.setHintTextColor(Color.RED);
			 UIErrorMessage = "Perlengkapan harus diisi";
			 return;
		 }

		if (jangkaWaktuEff.getText().toString().isEmpty()) {
			flag++;
			jangkaWaktuEff.setHintTextColor(Color.RED);
			UIErrorMessage = "Tanggal mulai harus diisi";
			return;
		}
		if (jangkaWaktuExp.getText().toString().isEmpty()) {
			flag++;
			jangkaWaktuExp.setHintTextColor(Color.RED);
			UIErrorMessage = "Tanggal berakhir harus diisi";
			return;
		}
		if (nilaiPertanggungan.getText().toString().isEmpty()) {
			flag++;
			nilaiPertanggungan.setHintTextColor(Color.RED);
			UIErrorMessage = "Nilai Pertanggungan harus diisi";
			return;
		}
		if (nilaiPerlengkapan.getText().toString().isEmpty()) {
			nilaiPerlengkapan.setText("0"); 
		} 
		
		if (risikoSendiri.getText().toString().isEmpty()) {
			flag++;
			risikoSendiri.setHintTextColor(Color.RED);
			UIErrorMessage = "Risiko harus diisi";
			return;
		}
		
		if (keteranganKerusakan.getText().toString().isEmpty() && swiKerusakan.isChecked()) {
			flag++;
			keteranganKerusakan.setHintTextColor(Color.RED);
			UIErrorMessage = "Keterangan kerusakan harus diisi";
			return;
		}
		
		
		
		DBA_MASTER_PRODUCT_SETTING dbSetting= new DBA_MASTER_PRODUCT_SETTING(this);
		Cursor cSetting = null;
		double maxTSI = 0;
		double minTSI = 0; 
		
		try {
			dbSetting.open();
			cSetting = dbSetting.getRow("02");

			if (!cSetting.moveToFirst())
				return;

			minTSI = nf.parse(cSetting.getString(3)).doubleValue();
			maxTSI = nf.parse(cSetting.getString(4)).doubleValue();
			

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dbSetting != null)
				dbSetting.close();

			if (cSetting != null)
				cSetting.close();

		}
		
		
		minTSI = 0;
		
		if (getNilaiPertanggungan() < minTSI) {
			flag++;
			nilaiPertanggungan.setTextColor(Color.RED);
			UIErrorMessage = "Nilai pertanggungan minimal " + nf.format(minTSI);
			return;
		}

		if (getNilaiPertanggungan() > maxTSI) {
			flag++;
			nilaiPertanggungan.setTextColor(Color.RED);
			UIErrorMessage = "Nilai pertanggungan maksimal " + nf.format(maxTSI);
			return;
 		}
		if (getNilaiPerlengkapan() > (0.1 * getNilaiPertanggungan())) {
			flag++;
			nilaiPerlengkapan.setTextColor(Color.RED);
			UIErrorMessage = "Nilai perlengkapan maksimal 10% nilai pertanggungan";
			return;
		}
		

		if (layLoading5tahun.getVisibility() == View.GONE)
		{
			if (nf.parse(risikoSendiri.getText().toString()).doubleValue() < 300000) {
				flag++;
				risikoSendiri.setTextColor(Color.RED);
				UIErrorMessage = "Risiko sendiri minimal 300 ribu";
				return;	
			}			
		}
		else 
		{
			if (rbLoadingResikoSendiri.isChecked() && layLoading5tahun.getVisibility() == View.VISIBLE) {
				if (nf.parse(risikoSendiri.getText().toString()).doubleValue() < 500000) {
					flag++;
					risikoSendiri.setTextColor(Color.RED);
					UIErrorMessage = "Risiko Sendiri minimal 500 ribu jika kendaraan lebih dari 5 tahun";
					return;
				}
			}	
			else {
				if (nf.parse(risikoSendiri.getText().toString()).doubleValue() < 300000) {
					flag++;
					risikoSendiri.setTextColor(Color.RED);
					UIErrorMessage = "Risiko sendiri minimal 300 ribu";
					return;
				}			
			}
		}

		if (rate.getText().toString().isEmpty()) {
			flag++;
			rate.setHintTextColor(Color.RED);
			UIErrorMessage = "Rate tidak bisa dihitung. Hubungi IT ACA";
			return;
		}
		if (premi.getText().toString().isEmpty()) {
			flag++;
			premi.setHintTextColor(Color.RED);
			UIErrorMessage = "Premi tidak bisa dihitung. Hubungi IT ACA";
			return;
		}
		if (discpct.getText().toString().isEmpty()) {
			flag++;
			discpct.setHintTextColor(Color.RED); 
			return;
		}
		if (disc.getText().toString().isEmpty()) {
			flag++;
			disc.setHintTextColor(Color.RED);
			return;
		} 

		if (Utility.parseDouble(discpct) > MAX_DISCOUNT) {
			flag++;
			discpct.setTextColor(Color.RED);
			UIErrorMessage = "Diskon terlalu besar, maksimum adalah "
					+ String.valueOf(MAX_DISCOUNT) + "%";
			return;
		}

	}

	public void btnNextClick(View v) {
		try {
			seat.setText("1");

			validasiNext(); 

			if (flag != 0)
				Utility.showCustomDialogInformation(FillKonvensionalActivity.this,
						"Warning", UIErrorMessage);
			else {
				calculateAll();
				insertDB();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void btnChooseCustomerClick(View v) {
		Intent i = new Intent(getBaseContext(),
				ChooseCustomerForBuyActivity.class);
		i.putExtras(b);
		startActivity(i);
		this.finish();
		//
		// if (customerno.getText().length() == 0) {
		// Intent i = new Intent(getBaseContext(),
		// ChooseCustomerForBuyActivity.class);
		// i.putExtras(b);
		// startActivity(i);
		// this.finish();
		// }
		// else {
		// try {
		// custList = Utility.getCustData(getBaseContext(),
		// customerno.getText().toString());
		// if(custList != null)
		// customername.setText(custList.get(0).get("CUSTOMER_NAME"));
		// else {
		// customerno.setTextColor(Color.RED);
		// Toast.makeText(getBaseContext(), "Nasabah tidak ditemukan",
		// Toast.LENGTH_SHORT).show();
		// customername.setText("");
		// }
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// }
	}
	public void showDisc(View v) {
		TableRow trDiscPct = (TableRow) findViewById(R.id.trDiscPct);
		TableRow trDisc = (TableRow) findViewById(R.id.trDisc);
		
		if (trDisc.getVisibility() == View.GONE) {
			trDiscPct.setVisibility(View.VISIBLE);
			trDisc.setVisibility(View.VISIBLE);			
		}
		else {
			trDiscPct.setVisibility(View.GONE);
			trDisc.setVisibility(View.GONE);
		}
	}
	
	
	private void disablePerluasanJaminan() {
		AOG.setEnabled(false);
		EQ.setEnabled(false);
		SRCC.setEnabled(false);	
		TS.setEnabled(false);
		spnTpl.setEnabled(false);
		spnPA.setEnabled(false);
	}

	private void showJenisKendaraan()
	{
		// custom dialog
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.dialog_konvensional_choose_truk);
		dialog.setTitle("Pilih Jenis Kendaraan");


		int textViewId = dialog.getContext().getResources().getIdentifier("android:id/title", null, null);
		TextView tv = (TextView) dialog.findViewById(textViewId);
		tv.setTextColor(getResources().getColor(R.color.Black));
		
 
		Button dialogTruk = (Button) dialog.findViewById(R.id.btnTruk);
		Button dialogNonTruk = (Button) dialog.findViewById(R.id.btnNonTruk);
		 
		dialogTruk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent i = new Intent(getBaseContext(),
						ChooseCarBrandActivity.class);
				b.putBoolean("ISTRUK", true);
				i.putExtras(b);
				startActivityForResult(i, 200);
				
				dialog.dismiss();
			}
		});
		
		
		dialogNonTruk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getBaseContext(),
						ChooseCarBrandActivity.class);
				b.putBoolean("ISTRUK", false);
				i.putExtras(b);
				startActivityForResult(i, 200);
				
				dialog.dismiss();
			}
		}); 
		dialog.show();
		 
//		else {
//			Intent i = new Intent(getBaseContext(),
//					ChooseCarBrandActivity.class);
//			b.putBoolean("ISTRUK", false);
//			i.putExtras(b);
//			startActivityForResult(i, 200);
//		}
	}
	
 


	private double getPremiPerluasan() throws ParseException {
		double Tjh = 0;
		double PA = 0;
		double premiPerluasan = 0;
		double premiTJH, premiPAP, premiPAD;
  
		String jaminan = "";
		String wilayah = "0"; 
		String vehicle_category = "0"; 
		
		Log.i(TAG, "::getPremiPerluasan:" + "vehicle category = " + vehicle_category);
		
		Tjh = nf.parse(((SpinnerGenericItem)spnTpl.getSelectedItem()).getCode()).doubleValue();
		PA = nf.parse(((SpinnerGenericItem)spnPA.getSelectedItem()).getCode()). doubleValue();

		double rateTJH = getRatePerluasan(jaminan, wilayah, vehicle_category, "5", String.valueOf(Tjh));
		double ratePAP = getRatePerluasan(jaminan, wilayah, vehicle_category, "6", "0");
		double ratePAD = getRatePerluasan(jaminan, wilayah, vehicle_category, "7", "0");
		
		premiTJH = Tjh *  rateTJH / 100 ;
		premiPAP = ratePAP * PA / 100;
		premiPAD = ratePAD * PA * (Integer.parseInt(car_seat) - 1) / 100;
		premiPerluasan = premiTJH + premiPAD + premiPAP;
		
		Log.i(TAG, "::getPremiPerluasan: premi tjh= " + premiTJH);
		Log.i(TAG, "::getPremiPerluasan: premi pap= " + premiPAP);
		Log.i(TAG, "::getPremiPerluasan: premi pad= " + premiPAD);
		
		Log.i(TAG, "::getPremiPerluasan:" + premiPerluasan);
		
		
		return premiPerluasan;
	}
	
	 
	
	private double getPremi() throws ParseException {
		double TSI = getTSI();
		double premiPerluasan = getPremiPerluasan();
		double nilaiPremi = (TSI * v_rate / 100) + premiPerluasan;

		return nilaiPremi;
	}

	private double getNilaiPertanggungan() throws ParseException {
		double pertanggungan;

		if (nilaiPertanggungan.getText().toString().isEmpty())
			pertanggungan = 0;
		else
			pertanggungan = nf.parse(nilaiPertanggungan.getText().toString())
					.doubleValue();

		return pertanggungan;
	}

	private double getNilaiPerlengkapan() throws ParseException {
		double perlengkapan;

		if (nilaiPerlengkapan.getText().toString().isEmpty())
			perlengkapan = 0;
		else
			perlengkapan = nf.parse(nilaiPerlengkapan.getText().toString())
					.doubleValue();

		return perlengkapan;
	}
 

	private double getTSI() throws ParseException {
		double pertanggungan = getNilaiPertanggungan();
		double perlengkapan = getNilaiPerlengkapan();

		double TSI = pertanggungan + perlengkapan;

		return TSI;
	}

	private double getLoadingRate() {
		double rate = 0.05;
		
		
		if (tipeKonvensional.equalsIgnoreCase("COMPREHENSIVE") && layLoading5tahun.getVisibility() == View.VISIBLE) {
			if (diffYear() > 5 && rbLoadingRate.isChecked())
				rate = rate * (diffYear() - 5) ;
			else
				rate = 0;
		}
		else if (tipeKonvensional.equalsIgnoreCase("TLO")) {
			if (diffYear() > 10)
				rate = rate * (diffYear() - 10) ;
			else
				rate = 0;
		}
		else
			rate = 0;
		
		return rate;
	}

	private int diffYear() {
		Calendar c = Calendar.getInstance();

		int tahunKend = Integer.parseInt(tahun.getText().toString());
		int tahunToday = c.get(Calendar.YEAR);

		return tahunToday - tahunKend;
	}
	private String getWilayah (String plat1) {
		String Wilayah = "3";
		
		if (plat1.equals("BA") || plat1.equals("BB") || plat1.equals("BD")
				|| plat1.equals("BE") || plat1.equals("BG")
				|| plat1.equals("BH") || plat1.equals("BK")
				|| plat1.equals("BL") || plat1.equals("BM")
				|| plat1.equals("BP") || plat1.equals("BN")
				|| plat1.equals("CC") || plat1.equals("CD"))
			Wilayah = "1";
		else if (plat1.equals("A") || plat1.equals("B")
				|| plat1.equals("C") || plat1.equals("D")
				|| plat1.equals("E") || plat1.equals("F")
				|| plat1.equals("T") || plat1.equals("Z"))
			Wilayah = "2";
		else if (plat1.equals("G") || plat1.equals("H")
				|| plat1.equals("K") || plat1.equals("L")
				|| plat1.equals("M") || plat1.equals("N")
				|| plat1.equals("P") || plat1.equals("R")
				|| plat1.equals("S") || plat1.equals("AA")
				|| plat1.equals("AB") || plat1.equals("AD")
				|| plat1.equals("AE") || plat1.equals("AG")
				|| plat1.equals("W") || plat1.equals("DB")
				|| plat1.equals("DD") || plat1.equals("DM")
				|| plat1.equals("DN") || plat1.equals("DE")
				|| plat1.equals("DG") || plat1.equals("DH")
				|| plat1.equals("DK") || plat1.equals("DL")
				|| plat1.equals("DR") || plat1.equals("DS")
				|| plat1.equals("EA") || plat1.equals("EB")
				|| plat1.equals("ED") || plat1.equals("DA")
				|| plat1.equals("KB") || plat1.equals("KT"))
			Wilayah = "3";

		return Wilayah;
	}
	
	private String getTipeKonven () {
		if (tipeKonvensional.equalsIgnoreCase("COMPREHENSIVE"))
			return "AR";
		else
			return "TL";
	}

	private double getRate() throws InterruptedException, ParseException {
		DBA_MASTER_KONVE_RATE dba = null;
		Cursor c = null;
		double v = 0.0;
		String Wilayah = "3";
		String jaminan;
		String exco;
		String vehicle_category;
		String plat1 = nopol1.getText().toString().toUpperCase();

		try {
			dba = new DBA_MASTER_KONVE_RATE(getBaseContext());
			dba.open();

			Wilayah = getWilayah(plat1);
			jaminan = getTipeKonven();	
			exco = "0";
			vehicle_category = FillKonvensionalActivity.this.vehicle_category;
			
			Log.d("vehicle category", vehicle_category);
			Log.d("plat1", plat1);
			Log.d("Wilayah", Wilayah);
			Log.d("Jaminan", jaminan);
 
			if (isTruk)
				c = dba.getRate(Wilayah, vehicle_category, exco, jaminan, "0");
			else
				c = dba.getRate(Wilayah, vehicle_category, exco, jaminan, String.valueOf(nf.format(v_tsi)));

			if (c.moveToFirst()) {
				
				double rateKomersil = ((SpinnerGenericItem)spnDNC.getSelectedItem()).getCode().equalsIgnoreCase("C") ? 1.1 : 1 ;
				Log.i(TAG, "::getRate:" + "rate komersil = "  +rateKomersil);
				
				if (rbRateAtas.isChecked()) {
					v = c.getDouble(2) ;				
				}
				else if (rbRateTengah.isChecked()) {
					v = c.getDouble(1) ; 			
				}
				else if (rbRateBawah.isChecked()) {
					v = c.getDouble(0) ; 			
				}
				
				v = v * rateKomersil * (1 + getLoadingRate());	
			}

			Log.i(TAG, "::getRate:" + "new rate : " + v);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null)
				c.close();

			if (dba != null)
				dba.close();
		}

		return v;
	}
	
	private double getRatePerluasan (String jaminan, String wilayah , String vehicle_category, String exco, String jnp) {
		DBA_MASTER_KONVE_RATE dba = null ;
		Cursor c = null;
 
		try {
		
			
			dba = new DBA_MASTER_KONVE_RATE(this);
			dba.open();
			if (isTruk)
				c = dba.getRate(wilayah, vehicle_category, exco, jaminan, "0");
			else
				c = dba.getRate(wilayah, vehicle_category, exco, jaminan, jnp);

			if (!c.moveToFirst())
				return 0;
			
			return c.getDouble(0);

			

		} catch (Exception e) {
			e.printStackTrace();
			
			return 0;
			
		} finally {
			if (dba != null)
				dba.close();

			if (c != null)
				c.close();

		} 
		 
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
		int D = c.get(Calendar.DAY_OF_MONTH);

		switch (id) {
		case DATE_FROM_ID:
			return new DatePickerDialog(this, datePickerListener, Y, M, D + 1);
		case DATE_TO_ID:
			return new DatePickerDialog(this, datePickerListenerNext, Y + 1, M,
					D);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			String theDate = Utility.setUIDate(selectedYear, selectedMonth,
					selectedDay);
			jangkaWaktuEff.setText(theDate);
			jangkaWaktuExp.setText(Utility.GetTodayNextYearString(theDate));
			validasiTanggal();
		}
	};

	private DatePickerDialog.OnDateSetListener datePickerListenerNext = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			jangkaWaktuExp.setText(Utility.setUIDate(selectedYear,
					selectedMonth, selectedDay));
		}
	};

	private void insertDB() throws ParseException {

		DBA_PRODUCT_CONVENSIONAL dba = null;
		DBA_PRODUCT_MAIN dba2 = null;

		try {

			if (PRODUCT_ACTION.equals("VIEW")) {
				Intent i = new Intent(getBaseContext(), ConfirmActivity.class);
				b.putLong("SPPA_ID", SPPA_ID);
				i.putExtras(b);
				startActivity(i);
				this.finish();
			} else {

				dba = new DBA_PRODUCT_CONVENSIONAL(getBaseContext());
				dba2 = new DBA_PRODUCT_MAIN(getBaseContext());

				dba.open();
				dba2.open();
 
				String acctype;
 

				if (AccType.isChecked())
					acctype = "1";
				else
					acctype = "0";

				if (PRODUCT_ACTION.equals("NEW")) {
					SPPA_ID = dba2
							.initialInsert(customerno.getText().toString(),
									customername.getText().toString(),
									"KONVENSIONAL",
									nf.parse(premi.getText().toString())
											.doubleValue(),
									nf.parse(materai.getText().toString())
											.doubleValue(),
									nf.parse(polis.getText().toString())
											.doubleValue(),
									nf.parse(total.getText().toString())
											.doubleValue(), jangkaWaktuEff
											.getText().toString(),
									jangkaWaktuExp.getText().toString(),
									Utility.DateToString(Utility.GetToday(),
											"dd/MM/yyyy"), "", "", "", "N", "",
									nf.parse((discpct.getText().toString()))
											.doubleValue(),
									nf.parse((disc.getText().toString()))
											.doubleValue());

					dba.initialInsert(SPPA_ID, merkKendaraan.getTag()
							.toString(), tipeKendaraan.getTag().toString(),
							model.getText().toString(), tahun.getText()
									.toString(), nopol1.getText().toString()
									.toUpperCase(),
							nopol2.getText().toString(), nopol3.getText()
									.toString().toUpperCase(), color.getText()
									.toString().toUpperCase(), chassisNo
									.getText().toString().toUpperCase(),
							machineNo.getText().toString().toUpperCase(),
							acctype, perlengkapan.getText().toString().toUpperCase(), 
							1, 
							((SpinnerGenericItem)spnDNC.getSelectedItem()).getCode(),
							jangkaWaktuEff.getText()
									.toString(), jangkaWaktuExp.getText()
									.toString(),
							nf.parse(nilaiPertanggungan.getText().toString())
									.doubleValue(),
							nf.parse(nilaiPerlengkapan.getText().toString()).doubleValue(), 
							
							AOG.isChecked() ? "1" : "0",
							EQ.isChecked() ? "1" : "0",
							SRCC.isChecked() ? "1" : "0",
							TS.isChecked() ? "1" : "0",
										
							((SpinnerGenericItem)spnTpl.getSelectedItem()).getCode(),
							((SpinnerGenericItem)spnPA.getSelectedItem()).getCode(),
							
							rbRateAtas.isChecked() ? "3" : (rbRateTengah.isChecked() ? "2" : rbRateBawah.isChecked() ? "1" : "0"),
							tipeKonvensional.equals("COMPREHENSIVE") ? "0"  : "1",		

							nf.parse(rate.getText().toString()).doubleValue(),
							nf.parse(premi.getText().toString()).doubleValue(),
							nf.parse(materai.getText().toString())
									.doubleValue(),
							nf.parse(polis.getText().toString()).doubleValue(),
							nf.parse(total.getText().toString()).doubleValue(),
							merkKendaraan.getText().toString(),
							tipeKendaraan.getText().toString(), 
							"KONVENSIONAL",
							customername.getText().toString(),
							nf.parse(risikoSendiri.getText().toString()).doubleValue(),
							swiKerusakan.isChecked() ?  "1" : "0",
							keteranganKerusakan.getText().toString(),
							rbLoadingRate.isChecked() ? 1 : 2,
							isTruk ? "1" : "0",
							vehicle_category,
							car_seat
							  );
						

				} else {
					dba2.nextInsert(SPPA_ID, customerno.getText().toString(),
							customername.getText().toString(),
							nf.parse(premi.getText().toString()).doubleValue(),
							nf.parse(materai.getText().toString())
									.doubleValue(),
							nf.parse(polis.getText().toString()).doubleValue(),
							nf.parse(total.getText().toString()).doubleValue(),
							jangkaWaktuEff.getText().toString(), jangkaWaktuExp
									.getText().toString(), Utility
									.DateToString(Utility.GetToday(),
											"dd/MM/yyyy"),
							nf.parse((discpct.getText().toString()))
									.doubleValue(),
							nf.parse((disc.getText().toString())).doubleValue());

					dba.nextInsert(SPPA_ID, merkKendaraan.getTag().toString(),
							tipeKendaraan.getTag().toString(), model.getText()
									.toString(), tahun.getText().toString(),
							nopol1.getText().toString().toUpperCase(), nopol2
									.getText().toString(), nopol3.getText()
									.toString().toUpperCase(), color.getText()
									.toString().toUpperCase(), chassisNo
									.getText().toString().toUpperCase(),
							machineNo.getText().toString().toUpperCase(),
							acctype, perlengkapan.getText().toString().toUpperCase(), 
							1,
							((SpinnerGenericItem)spnDNC.getSelectedItem()).getCode(),
							jangkaWaktuEff.getText().toString(), 
							jangkaWaktuExp.getText().toString(),
							nf.parse(nilaiPertanggungan.getText().toString()).doubleValue(),
							nf.parse(nilaiPerlengkapan.getText().toString()).doubleValue(), 
							
							AOG.isChecked() ? "1" : "0",
							EQ.isChecked() ? "1" : "0",
							SRCC.isChecked() ? "1" : "0",
							TS.isChecked() ? "1" : "0",
								

							((SpinnerGenericItem)spnTpl.getSelectedItem()).getCode(),
							((SpinnerGenericItem)spnPA.getSelectedItem()).getCode(),
							
							rbRateAtas.isChecked() ? "3" : (rbRateTengah.isChecked() ? "2" : rbRateBawah.isChecked() ? "1" : "0"),
							tipeKonvensional.equals("COMPREHENSIVE") ? "0"  : "1",		

					
							
							nf.parse(rate.getText().toString()).doubleValue(),
							nf.parse(premi.getText().toString()).doubleValue(),
							nf.parse(materai.getText().toString())
									.doubleValue(),
							nf.parse(polis.getText().toString()).doubleValue(),
							nf.parse(total.getText().toString()).doubleValue(),
							merkKendaraan.getText().toString(), tipeKendaraan
									.getText().toString(), "KONVENSIONAL",
							customername.getText().toString(),
							nf.parse(risikoSendiri.getText().toString()).doubleValue(),
							swiKerusakan.isChecked() ?  "1" : "0",
							keteranganKerusakan.getText().toString(),
							rbLoadingRate.isChecked() ? 1 : 2,
							isTruk ? "1" : "0",
							vehicle_category,
							car_seat
							);
				}

				Intent i = new Intent(getBaseContext(), ConfirmActivity.class);
				b.putLong("SPPA_ID", SPPA_ID);
				i.putExtras(b);
				startActivity(i);
				FillKonvensionalActivity.this.finish();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (dba != null)
				dba.close();

			if (dba2 != null)
				dba2.close();
		}
	}

	public class DeleteSPPA extends AsyncTask<Void, Void, Void> {
		private ProgressDialog progDialog = null;
		private boolean error = false;
		private String flag = "";

		protected void onPreExecute() {
			progDialog = ProgressDialog.show(context, "", "Processing...",
					false);
		}

		@Override
		protected Void doInBackground(Void... params) {

			DBA_PRODUCT_MAIN dba = null;
			DBA_PRODUCT_CONVENSIONAL dba2 = null;

			Cursor c = null;
			String E_SPPA_NO = "";

			HttpTransportSE androidHttpTransport = null;
			String NAMESPACE = "http://tempuri.org/";

			SoapObject request = null;
			SoapSerializationEnvelope envelope = null;
			String URL = Utility.getURL();
			String SOAP_ACTION = "http://tempuri.org/DeleteSPPA";
			String METHOD_NAME = "DeleteSPPA";

			try {
				dba = new DBA_PRODUCT_MAIN(context);
				dba.open();

				dba2 = new DBA_PRODUCT_CONVENSIONAL(context);
				dba2.open();

				// dapatin no SPPA
				c = dba.getRow(SPPA_ID);
				c.moveToNext();
				E_SPPA_NO = c.getString(5);

				Log.d("-->", E_SPPA_NO);

				if (E_SPPA_NO == null || E_SPPA_NO.length() == 0
						|| !TextUtils.isDigitsOnly(E_SPPA_NO)) {
					Log.d("-->", "EMPTY");

					// hapus data di local db
					dba.delete(SPPA_ID);
					dba2.delete(SPPA_ID);

					// hapus directory
					Utility.DeleteDirectory(SPPA_ID);
				} else {
					// coba hapus SPPA di server
					envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
					envelope.implicitTypes = true;
					envelope.dotNet = true; // used only if we use the
											// webservice from a dot net file
											// (asmx)
					androidHttpTransport = new HttpTransportSE(URL);
					request = new SoapObject(NAMESPACE, METHOD_NAME);

					request.addProperty(Utility.GetPropertyInfo("SPPANo",
							E_SPPA_NO, String.class));

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


					//copy in pimp testing ya. SAMUEKL 
					
					
					if (flag.equals("1")) {
						// hapus data di local db
						dba.delete(SPPA_ID);
						dba2.delete(SPPA_ID);

						// hapus directory
						Utility.DeleteDirectory(SPPA_ID);
					}
				}

			} catch (Exception e) {
				error = true;
				e.printStackTrace();
				// errorMessage = new MasterExceptionClass(e).getException();
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

		protected void onPostExecute(Void result) {
			try {
				progDialog.dismiss();
				progDialog = null;

				if (error)
					Toast.makeText(getBaseContext(), "Data gagal dihapus",
							Toast.LENGTH_SHORT).show();
				else {
					Toast.makeText(getBaseContext(), "Data berhasil dihapus",
							Toast.LENGTH_SHORT).show();

					Intent i = null;

					i = new Intent(getBaseContext(), SyncActivity.class);

					startActivity(i);
				}
			} catch (Exception ex) {
				//
			}
		}
	}
	


class GetVehicleCategory extends AsyncTask<String, Void, Void>{
	private Activity ctx ;
	
	private boolean error = false;
	private String errorMessage = "";
	
	private SoapObject requestretrive = null;
    private SoapSerializationEnvelope envelope = null;
    private HttpTransportSE androidHttpTransport = null;
    
    private final String NAMESPACE = "http://tempuri.org/";     
	private final String URL = Utility.getURL(); 

    private final String SOAP_ACTION = "http://tempuri.org/GetVehicleCategory";     
    private final String METHOD_NAME= "GetVehicleCategory";
    
    private String VehicleCategory,VehicleMerk ;
    private ArrayList<HashMap<String, String>> arrList = null;
	private ProgressDialog progressBar;

	private String responseString;     
	
	public GetVehicleCategory (Activity ctx, String VehicleMerk, String VehicleCategory ){
		this.ctx = ctx;
		this.VehicleMerk = VehicleMerk;
		this.VehicleCategory = VehicleCategory; 
	}
	  
	
	@Override
	protected void onPreExecute() {

		super.onPreExecute();
		 
		progressBar = new ProgressDialog(ctx);
		progressBar.setCancelable(false);
		progressBar.setMessage("Please wait ...");
		progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressBar.show();
		
		envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); 
		envelope.implicitTypes = true;
		envelope.dotNet = true;	//used only if we use the webservice from a dot net file (asmx)
		androidHttpTransport = new HttpTransportSE(URL);

	}
	
	@Override
	protected Void doInBackground(String... params) {
		
		error = false;
		
		try{ 

			requestretrive = new SoapObject(NAMESPACE, METHOD_NAME);
			
			Log.i(TAG, "::doInBackground:" + "vehicle merk = " + VehicleMerk);
			Log.i(TAG, "::doInBackground:" + "vehicle category = " + VehicleCategory);
			
	        requestretrive.addProperty(Utility.GetPropertyInfo("VehicleMerk", VehicleMerk, String.class));
	        requestretrive.addProperty(Utility.GetPropertyInfo("VehicleCategory", VehicleCategory, String.class));
	        
	    	envelope.setOutputSoapObject(requestretrive);
	    	envelope.bodyOut = requestretrive;
    		androidHttpTransport.call(SOAP_ACTION, envelope);  
    			 
			SoapObject result = (SoapObject)envelope.bodyIn;
    		responseString = result.getProperty(0).toString();
    		
    		Log.i(TAG, "::doInBackground:" + "vehicle category = " + responseString);
		}	
		catch (Exception e) {
    		error = true;
    		e.printStackTrace();
			errorMessage = new MasterExceptionClass(e).getException();
		}
		return null;
	}
	
	@Override

	protected void onPostExecute(Void result) {
		 
		super.onPostExecute(result);
		progressBar.hide();
		progressBar.dismiss();
		try{	
			if(error){
				Toast.makeText(ctx, errorMessage, Toast.LENGTH_SHORT).show();
				tipeKendaraan.setText("");
				tipeKendaraan.setTag("");
			}
			else {
				vehicle_category = responseString;
				
				if (!nilaiPertanggungan.getText().toString().isEmpty())
					calculateAll();
			}
			 
		
		}catch(Exception ex){
			ex.printStackTrace();
		}
	} 
	
}



class GetCarSeat extends AsyncTask<String, Void, Void>{
	private Activity ctx ;
	
	private boolean error = false;
	private String errorMessage = "";
	
	private SoapObject requestretrive = null;
    private SoapSerializationEnvelope envelope = null;
    private HttpTransportSE androidHttpTransport = null;
    
    private final String NAMESPACE = "http://tempuri.org/";     
	private final String URL = Utility.getURL(); 

    private final String SOAP_ACTION = "http://tempuri.org/GetCarSeat";     
    private final String METHOD_NAME= "GetCarSeat";
    
    private String VehicleBrand,VehicleType ;
    private ArrayList<HashMap<String, String>> arrList = null;
	private ProgressDialog progressBar;

	private String responseString;     
	
	public GetCarSeat (Activity ctx, String VehicleType, String VehicleBrand ){
		this.ctx = ctx;
		this.VehicleType = VehicleType;
		this.VehicleBrand = VehicleBrand; 
	}
	  
	
	@Override
	protected void onPreExecute() {

		super.onPreExecute();
		 
		progressBar = new ProgressDialog(ctx);
		progressBar.setCancelable(false);
		progressBar.setMessage("Please wait ...");
		progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressBar.show();
		
		envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); 
		envelope.implicitTypes = true;
		envelope.dotNet = true;	//used only if we use the webservice from a dot net file (asmx)
		androidHttpTransport = new HttpTransportSE(URL);

	}
	
	@Override
	protected Void doInBackground(String... params) {
		
		error = false;
		
		try{ 

			requestretrive = new SoapObject(NAMESPACE, METHOD_NAME);
			
			Log.i(TAG, "::doInBackground:" + "vehicle brand = " + VehicleBrand);
			Log.i(TAG, "::doInBackground:" + "vehicle type = " + VehicleType);
			
	        requestretrive.addProperty(Utility.GetPropertyInfo("VehicleType", VehicleType, String.class));
	        requestretrive.addProperty(Utility.GetPropertyInfo("VehicleBrand", VehicleBrand, String.class));
	        
	    	envelope.setOutputSoapObject(requestretrive);
	    	envelope.bodyOut = requestretrive;
    		androidHttpTransport.call(SOAP_ACTION, envelope);  
    			 
			SoapObject result = (SoapObject)envelope.bodyIn;
    		responseString = result.getProperty(0).toString();
    		
    		Log.i(TAG, "::doInBackground:" + "vehicle seat = " + responseString);
		}	
		catch (Exception e) {
    		error = true;
    		e.printStackTrace();
			errorMessage = new MasterExceptionClass(e).getException();
		}
		return null;
	}
	
	@Override

	protected void onPostExecute(Void result) {
		 
		super.onPostExecute(result);
		progressBar.hide();
		progressBar.dismiss();
		try{	
			if(error){
				Toast.makeText(ctx, errorMessage, Toast.LENGTH_SHORT).show();
				tipeKendaraan.setText("");
				tipeKendaraan.setTag("");
				
			}
			else {
				car_seat = responseString;
				
				if (!nilaiPertanggungan.getText().toString().isEmpty())
					calculateAll();
			}
			 
		
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
	
	
}
