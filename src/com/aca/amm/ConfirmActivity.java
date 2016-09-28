package com.aca.amm;

import java.text.NumberFormat;

import com.aca.amm.R;
import com.aca.database.DBA_PRODUCT_CARGO;
import com.aca.database.DBA_PRODUCT_MAIN;
import com.aca.database.DBA_PRODUCT_TRAVEL_SAFE;

import android.opengl.Visibility;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ConfirmActivity extends ControlNormalActivity {

	private Bundle b;
	private String PRODUCT_TYPE;
	private String PRODUCT_ACTION;
	private long SPPA_ID;
	private int size = 20;
	private NumberFormat nf;
	
	private TextView nama, product, periode,periode2, premi, premiUS, diskon, material, total, catatan;
	private TextView premicargolbl, premicargotxt;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_confirm);
		
		nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		
		try{
			Intent i = getIntent();
			b = i.getExtras();
			PRODUCT_TYPE = b.getString("PRODUCT_TYPE");
			PRODUCT_ACTION = b.getString("PRODUCT_ACTION");
			SPPA_ID = b.getLong("SPPA_ID");
		}
		catch(Exception e){
		}
		
		InitControl();
		fillField();
	}
	
	private void InitControl(){
		nama = (TextView)findViewById(R.id.lblNamatxt);
		product = (TextView)findViewById(R.id.lblProducttxt);
		periode = (TextView)findViewById(R.id.lblPeriodetxt);
		periode2 = (TextView)findViewById(R.id.lblPeriodetxt2);
		premi = (TextView)findViewById(R.id.lblPremitxt);
		diskon = (TextView)findViewById(R.id.lblDiskontxt);
		material = (TextView)findViewById(R.id.lblMaterialtxt);
		total = (TextView)findViewById(R.id.lblTotaltxt);
		catatan = (TextView)findViewById(R.id.lblCatatantxt);
		premiUS = (TextView)findViewById(R.id.lblPremiDollartxt);
		premicargolbl = (TextView)findViewById(R.id.lblPremiNoIDR);
		premicargotxt = (TextView)findViewById(R.id.txtPremiNoIDR);
		
		
		((TextView)findViewById(R.id.lblNama)).setTextSize(size);
		((TextView)findViewById(R.id.lblProduct)).setTextSize(size);
		((TextView)findViewById(R.id.lblPeriode)).setTextSize(size);
		((TextView)findViewById(R.id.lblPeriode2)).setTextSize(size);
		((TextView)findViewById(R.id.lblPremi)).setTextSize(size);
		((TextView)findViewById(R.id.lblDiskon)).setTextSize(size);
		((TextView)findViewById(R.id.lblMaterial)).setTextSize(size);
		((TextView)findViewById(R.id.lblTotal)).setTextSize(size);
		((TextView)findViewById(R.id.lblCatatan)).setTextSize(size);
		((TextView)findViewById(R.id.lblPremiDollar)).setTextSize(size); 
		
		
		nama.setTextSize(size);
		product.setTextSize(size);
		periode.setTextSize(size);
		periode2.setTextSize(size);
		premi.setTextSize(size);
		diskon.setTextSize(size);
		material.setTextSize(size);
		total.setTextSize(size);
		catatan.setTextSize(size);
		premiUS.setTextSize(size);
		premicargolbl.setTextSize(size);
		premicargotxt.setTextSize(size);
		
		if (PRODUCT_TYPE.equals("TRAVELSAFE")) {
			((TableRow)findViewById(R.id.tRowPremiUS)).setVisibility(View.VISIBLE);
		}
		
		if (PRODUCT_TYPE.equals("CARGO")) {
			((TableRow)findViewById(R.id.tRowPremiNoIDR)).setVisibility(View.VISIBLE);
		}
	}
	
	private  void fillField() {
		
		DBA_PRODUCT_MAIN dba = null;
		Cursor c = null;
	
		DBA_PRODUCT_TRAVEL_SAFE dba2 = null;
		Cursor cm = null;
		
		try {
			
			dba = new DBA_PRODUCT_MAIN(getBaseContext());
			dba.open();
			
			c = dba.getRow(SPPA_ID);
			c.moveToFirst();

			
			
			nama.setText(c.getString(2));
			product.setText(c.getString(3));
			periode.setText(c.getString(12));
			periode2.setText(c.getString(13));
			premi.setText(nf.format(c.getDouble(6)));
			material.setText(nf.format(c.getDouble(7)));
			diskon.setText(nf.format(c.getDouble(24)));
			total.setText(nf.format(c.getDouble(9)));
			catatan.setText("");
			
		
			if (PRODUCT_TYPE.equals("TRAVELSAFE")) {
				dba2 = new DBA_PRODUCT_TRAVEL_SAFE(getBaseContext());
				dba2.open();
				
				cm = dba2.getRow(SPPA_ID);
				cm.moveToFirst();
				
				premiUS.setText(nf.format(cm.getDouble(26)));
			}
			
			
			if (c.getDouble(24) == 0)
			{
				TableRow trDisc = (TableRow)findViewById(R.id.trDisc);
				trDisc.setVisibility(View.GONE);
			}
			
			if (PRODUCT_TYPE.equals("CARGO")) {
				DBA_PRODUCT_CARGO dbacargo = new DBA_PRODUCT_CARGO(this);
				Cursor cargocursor = null;

				try {
					dbacargo.open();
					cargocursor = dbacargo.getRow(SPPA_ID);

					if (!cargocursor.moveToFirst())
						return;

					double exrate = cargocursor.getDouble(12);
					double cargoPremi = cargocursor.getDouble(25);
					int currency = cargocursor.getInt(11);
					
					premi.setText(nf.format(cargoPremi * exrate));  // premi jepang
					premicargotxt.setText(nf.format(cargoPremi)); 
					premicargolbl.setText("Premi(" + Utility.getcurrency(currency) + ")");
					
					

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (dbacargo != null)
						dbacargo.close();

					if (cargocursor != null)
						cargocursor.close();

				}
			}
		}
		catch(Exception ex) {
		
			
		}
		finally {
			if (c != null)
				c.close();
			
			if (dba != null)
				dba.close();
			
			if (cm != null)
				cm.close();
			
			if (dba2 != null)
				dba2.close();
			
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.confirm, menu);
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
	
	@Override
	public void onBackPressed() {
		Back();
	}
	
	private void Back() {
		Intent i = PrevActivity(PRODUCT_TYPE);
		i.putExtras(b);
		startActivity(i);
		this.finish();
	}

	public void btnNextClick(View v) {
		Intent i = new Intent(getBaseContext(),  UploadPhotoActivity.class);
		i.putExtras(b);
		startActivity(i);
		this.finish();
	}
	
	private Intent PrevActivity(String act) {
		Intent i = null;
		try{
			
			if (PRODUCT_ACTION.equals("NEW"))
				PRODUCT_ACTION = "EDIT";
			
			if(act.equalsIgnoreCase("OTOMATE")){		
				i = new Intent(getBaseContext(),  FillOtomateActivity.class);
				b.putString("PRODUCT_ACTION", PRODUCT_ACTION);
				i.putExtras(b);
			}
			else if(act.equalsIgnoreCase("TRAVELDOM")){
				i = new Intent(getBaseContext(),  FillTravelDomActivity.class);
				b.putString("PRODUCT_ACTION", PRODUCT_ACTION);
				i.putExtras(b);
			}
			else if(act.equalsIgnoreCase("TRAVELSAFE")){
				i = new Intent(getBaseContext(),  FillTravelActivity.class);
				b.putString("PRODUCT_ACTION", PRODUCT_ACTION);
				i.putExtras(b);
			}
			else if(act.equalsIgnoreCase("TOKO")){
				i = new Intent(getBaseContext(),  FillTokoActivity.class);
				b.putString("PRODUCT_ACTION", PRODUCT_ACTION);
				i.putExtras(b);
			}
			else if(act.equalsIgnoreCase("MEDISAFE")){
				i = new Intent(getBaseContext(),  FillMedisafe.class);
				b.putString("PRODUCT_ACTION", PRODUCT_ACTION);
				i.putExtras(b);
			}
			else if(act.equalsIgnoreCase("ASRI")){	
				i = new Intent(getBaseContext(),  FillAsriActivity.class);
				b.putString("PRODUCT_ACTION", PRODUCT_ACTION);
				i.putExtras(b);
			}
			else if(act.equalsIgnoreCase("ASRISYARIAH")){
				i = new Intent(getBaseContext(),  FillAsriSyariahActivity.class);
				b.putString("PRODUCT_ACTION", PRODUCT_ACTION);
				i.putExtras(b);
			}
			else if(act.equalsIgnoreCase("OTOMATESYARIAH")){				
				i = new Intent(getBaseContext(),  FillOtomateSyariahActivity.class);
				b.putString("PRODUCT_ACTION", PRODUCT_ACTION);
				i.putExtras(b);
			}
			else if(act.equalsIgnoreCase("EXECUTIVESAFE")){
				i = new Intent(getBaseContext(),  FillExecutiveActivity.class);
				b.putString("PRODUCT_ACTION", PRODUCT_ACTION);
				i.putExtras(b);
			}
			else if(act.equalsIgnoreCase("PAAMANAH")){
				i = new Intent(getBaseContext(),  FillPAAmanahActivity.class);
				b.putString("PRODUCT_ACTION", PRODUCT_ACTION);
				i.putExtras(b);
			}
			else if(act.equalsIgnoreCase("ACAMOBIL")){
				i = new Intent(getBaseContext(),  FillACAMobilActivity.class);
				b.putString("PRODUCT_ACTION", PRODUCT_ACTION);
				i.putExtras(b);
			}
			else if(act.equalsIgnoreCase("CARGO")){
				i = new Intent(getBaseContext(),  FillCargoActivity.class);
				b.putString("PRODUCT_ACTION", PRODUCT_ACTION);
				i.putExtras(b);
			}
			else if(act.equalsIgnoreCase("WELLWOMAN")){
				i = new Intent(getBaseContext(),  FillWellWomanActivity.class);
				b.putString("PRODUCT_ACTION", PRODUCT_ACTION);
				i.putExtras(b);
			}
			else if(act.equalsIgnoreCase("DNO")){
				i = new Intent(getBaseContext(),  FillDNOActivity.class);
				b.putString("PRODUCT_ACTION", PRODUCT_ACTION);
				i.putExtras(b);
			}
			else if(act.equalsIgnoreCase("KONVENSIONAL")){
				i = new Intent(getBaseContext(),  FillKonvensionalActivity.class);
				b.putString("PRODUCT_ACTION", PRODUCT_ACTION);
				i.putExtras(b);
			}
			
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		
		return i;
	}
}
