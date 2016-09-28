package com.aca.amm;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.kobjects.util.Util;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.aca.amm.CheckCustomerIdWS.CallBackListener;
import com.aca.amm.R.color;
import com.aca.database.DBA_MASTER_AGENT;

import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.database.Cursor;
import android.gesture.GestureOverlayView;
import android.graphics.Color;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

public class CustomerActivity extends ControlNormalActivity implements CallBackListener, InterfaceCustomer {

	private ProgressDialog progressBar;
    private RetriveCustomerInfoWS scRetrive;
    private InsertCustomerInfoWS scInsert;
    private UpdateCustomerInfoWS scUpdate;
    
    private boolean error = false;
    private String errorMessage = "";
    
    private SoapObject requestretrive = null;
    private SoapObject requestinsert = null;
    private SoapObject requestupdate = null;
    
    private SoapSerializationEnvelope envelope = null;
    private HttpTransportSE androidHttpTransport = null;
    
    private ArrayList<HashMap<String, String>> customer; 
    
    private PropertyInfo customerNo;
    
    private int flag = 0;
    
    private PropertyInfo pbranchid = new PropertyInfo();
    private PropertyInfo puserid = new PropertyInfo();
    private PropertyInfo pfullname = new PropertyInfo();
    private PropertyInfo paddress = new PropertyInfo();
    private PropertyInfo prtno = new PropertyInfo();
    private PropertyInfo prwno = new PropertyInfo();
    private PropertyInfo pkelurahan = new PropertyInfo();
    private PropertyInfo pkecamatan = new PropertyInfo();
    private PropertyInfo pcity = new PropertyInfo();
    private PropertyInfo pcitycode = new PropertyInfo();
    private PropertyInfo ppostcode = new PropertyInfo();
    private PropertyInfo pphone = new PropertyInfo();
    private PropertyInfo pemail = new PropertyInfo();
    private PropertyInfo pdob = new PropertyInfo();
    private PropertyInfo poccupation = new PropertyInfo();
    private PropertyInfo pbussiness = new PropertyInfo();
    private PropertyInfo ppob = new PropertyInfo();
    private PropertyInfo pgender = new PropertyInfo();
    private PropertyInfo pcuststatus = new PropertyInfo();
    private PropertyInfo pcustidno = new PropertyInfo();
    private PropertyInfo pcustrange = new PropertyInfo();
    
	private static String NAMESPACE = "http://tempuri.org/";     
	private static String URL = Utility.getURL();    
	
	
    private static String SOAP_ACTION = "http://tempuri.org/GetCustomer";     
    private static String METHOD_NAME = "GetCustomer";
    
    private static String SOAP_ACTION_INSERT = "http://tempuri.org/InsertCustomer";     
    private static String METHOD_NAME_INSERT = "InsertCustomer";
    
    private static String SOAP_ACTION_UPDATE = "http://tempuri.org/UpdateCustomer";     
    private static String METHOD_NAME_UPDATE = "UpdateCustomer";
    
    
	private String cust_code, cust_action, city_name, city_id;
	private Bundle b;
	
	private Spinner spOccupation, spBusiness;
	private Calendar c;
	private static final int DATE_ID = 99;
	
	private EditText name, mobile, email, pob, ktp, alamat, kodepos, etTglLahir, cityName, cityID;
	private Switch gender;
	private RadioButton status1, status2, status3;
	private RadioButton income1, income2, income3, income4, income5;
	private Spinner phoneCodeSpn;
	
	private Button btnPrev, btnNext, btnCheckCustomer;
	private View sectionMiddle; 

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_customer);
		
		customerNo = new PropertyInfo();
		
		try
		{
			Intent i = getIntent();
			b = i.getExtras();
			cust_action = b.getString("CUST_ACTION");
			cust_code = b.getString("CUST_CODE");
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		btnPrev = (Button)findViewById(R.id.btnPrev);
		btnNext = (Button)findViewById(R.id.btnNext);
		
		spOccupation = (Spinner)findViewById(R.id.spinnerOccupation);
		Utility.BindOccupation(spOccupation, getBaseContext(), this);
		
		spBusiness = (Spinner)findViewById(R.id.spinnerBusiness);
		Utility.BindBusiness(spBusiness, getBaseContext(), this);
		
		InitEditText();
		RegisterListener();
		
		if(cust_action.equals("EDIT")){
			LoadCustomer();
			((com.aca.amm.CustomTextViewBold)findViewById(R.id.lblTitle)).setText("Edit Nasabah");
			
			btnPrev.setText("Batal");
			btnNext.setText("Update");
			
			btnCheckCustomer.setVisibility(View.GONE);
			sectionMiddle.setVisibility(View.VISIBLE);
		}
		else {
			btnPrev.setText("Batal");
			btnNext.setText("Simpan");	
			
			sectionMiddle.setVisibility(View.GONE);
		}	
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
				cityID.setText(city_id);
			}	
		}	
	}

	private void LoadCustomer() {
		requestretrive = new SoapObject(NAMESPACE, METHOD_NAME);
		scRetrive = new RetriveCustomerInfoWS();
		scRetrive.execute(cust_code);
	}

	private void InitEditText() {

		name = (EditText)findViewById(R.id.txtName);
		mobile = (EditText)findViewById(R.id.txtMobile);
		email = (EditText)findViewById(R.id.txtEmail);
		pob = (EditText)findViewById(R.id.txtPOB);
		ktp = (EditText)findViewById(R.id.txtIDNo);
		alamat = (EditText)findViewById(R.id.txtAddress);
		kodepos = (EditText)findViewById(R.id.txtPostalCode);
		etTglLahir = (EditText)findViewById(R.id.txtDOB);
		cityName = (EditText)findViewById(R.id.txtCityName);
		cityID = (EditText)findViewById(R.id.txtCityID);
		
		gender = (Switch)findViewById(R.id.swiGender);
		
		status1 = (RadioButton)findViewById(R.id.rboStatus1);
		status2 = (RadioButton)findViewById(R.id.rboStatus2);
		status3 = (RadioButton)findViewById(R.id.rboStatus3);
		
		income1 = (RadioButton)findViewById(R.id.rboIncome1);
		income2 = (RadioButton)findViewById(R.id.rboIncome2);
		income3 = (RadioButton)findViewById(R.id.rboIncome3);
		income4 = (RadioButton)findViewById(R.id.rboIncome4);
		income5 = (RadioButton)findViewById(R.id.rboIncome5);
		phoneCodeSpn = (Spinner)findViewById(R.id.txtPhoneCode); 
		btnCheckCustomer = (Button)findViewById(R.id.btnCheckCustomer);
		sectionMiddle = (View)findViewById(R.id.sectionMiddle);
		
		Utility.BindPhoneCode(phoneCodeSpn, this, CustomerActivity.this); 
	}
	
	private void emptyForm () {
		name.setText("");
		mobile .setText(""); 
		pob.setText(""); 
		alamat .setText("");
		kodepos.setText(""); 
		cityName.setText("");
		cityID.setText("");		
		gender.setText("");
		
		status1.setChecked(true);
		income1 .setChecked(true);
		
		phoneCodeSpn.setSelection(0);
		sectionMiddle.setVisibility(View.GONE);
	}

	private void RegisterListener(){
		btnCheckCustomer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int flag = 0;
				if (ktp.getText().toString().isEmpty()) {
					ktp.setError("Data harus diisi");
					flag ++;
				}
				if (email.getText().toString().isEmpty()) {
					email.setError("Data harus diisi");
					flag ++;
				}
				if (etTglLahir.getText().toString().isEmpty()) {
					etTglLahir.setError("Data harus diisi");
					flag ++;
				} 
				if (flag == 0) {
					HashMap<String, String> map = new HashMap<String, String>();

					map.put("IDNo", ktp.getText().toString().trim());
					map.put("DOB", etTglLahir.getText().toString().trim());
					map.put("Email", email.getText().toString().trim());
					map.put("AgentCode", Utility.getUserID(CustomerActivity.this).trim());
					
					CheckCustomerIdWS ws = new CheckCustomerIdWS(CustomerActivity.this, map);
					ws.mCallBack = CustomerActivity.this;
					ws.execute();
					
				}
				else {
					Utility.showCustomDialogInformation(CustomerActivity.this, "Informasi", "Isi semua data");
				}
			}
		});
		
		etTglLahir.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus)
					showDialog(DATE_ID);
			}
		});
		etTglLahir.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialog(DATE_ID);
				
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
	
	private void onCheckCustomerListener (HashMap<String, String> map) {

//		Jika ID sudah pernah ada IF....
		if (map != null) {
			
			if (!map.get("CRE_BY").toString().equalsIgnoreCase(Utility.getUserID(this))) {
				if (map.get("CUSTOMER_DOB").equalsIgnoreCase(etTglLahir.getText().toString()) &&
					map.get("CUSTOMER_EMAIL").equalsIgnoreCase(email.getText().toString()) ) {
//					loadcustomer

					sectionMiddle.setVisibility(View.VISIBLE);
				}
				else {
					Utility.showCustomDialogInformation(CustomerActivity.this, "ID sudah terdaftar dengan identitas berbeda",
							"Agent dapat menghubungi admin support untuk mendapat penjelasan jika ID, Email, Tanggal Lahir yang dimasukkan sudah benar");

					sectionMiddle.setVisibility(View.GONE);
				}
			}
			else {
				sectionMiddle.setVisibility(View.VISIBLE);
//				LOAD CUSTOMER
			}
		}
		else {
			sectionMiddle.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_ID:
		   // set date picker as current date
			c = Calendar.getInstance();
			Utility.year = c.get(Calendar.YEAR);
			Utility.month = c.get(Calendar.MONTH);
			Utility.day = c.get(Calendar.DAY_OF_MONTH);

			
		   return new DatePickerDialog(this, datePickerListener, 
				   Utility.year - 20, Utility.month, Utility.day);
		}
		return null;
	}
	
	private DatePickerDialog.OnDateSetListener datePickerListener 
    = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,
			int selectedMonth, int selectedDay) {
			Utility.year = selectedYear;
			Utility.month = selectedMonth;
			Utility.day = selectedDay;
			
			String day = "", month = "";
			
			if(Utility.month + 1 < 10){

				month = "0" + (Utility.month + 1);
		    }
			else {
				month = String.valueOf(Utility.month + 1); // penambahan coding
			}
			
		    if(Utility.day < 10){

		    	day  = "0" + Utility.day ;
		    }else{
		    	day = String.valueOf(Utility.day);
		    }
		
			// set selected date into textview
			etTglLahir.setText(new StringBuilder().append(day)
					   .append("/").append(month).append("/").append(Utility.year)
					   .append(" "));
			
			etTglLahir.setError(null);
			
			
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.customer, menu);
		return true;
	}
	
	public void btnHomeClick(View v)
	{
		Intent i = new Intent(getBaseContext(),  FirstActivity.class);
		startActivity(i);
		this.finish();
	}
	
	public void btnBackClick(View v)
	{
		Back();
	}
	

	private void validasiNext (){
		flag = 0;
		
		if (name.getText().toString().isEmpty() ){
			flag++ ;
			name.setHintTextColor(Color.RED);
		}
		if (mobile.getText().toString().isEmpty() ){
			flag++ ;
			mobile.setHintTextColor(Color.RED);
		}
		if (email.getText().toString().isEmpty() ){
			flag++ ;
			email.setHintTextColor(Color.RED);
		}
		if (pob.getText().toString().isEmpty() ){
			flag++ ;
			pob.setHintTextColor(Color.RED);
		}
		if (ktp.getText().toString().isEmpty() ){
			flag++ ;
			ktp.setHintTextColor(Color.RED);
		}
		if( alamat.getText().toString().isEmpty() ){
			flag++ ;
			alamat.setHintTextColor(Color.RED);
		}
		
		if( cityName.getText().toString().isEmpty() ){
			flag++ ;
			cityName.setHintTextColor(Color.RED);
		}
		
	
		if( kodepos.getText().toString().isEmpty()){
			flag++ ;
			kodepos.setHintTextColor(Color.RED);
		}
		if( etTglLahir.getText().toString().isEmpty()){
			flag++ ;
			etTglLahir.setHintTextColor(Color.RED);
		}
		
	}

	
	public void btnNextClick(View v)
	{
		try {
			validasiNext ();
			
			if(flag > 0)
			{
				Utility.showCustomDialogInformation(CustomerActivity.this, "Informasi", "Semua data harus dilengkapi");
			}
			else {
				if(cust_action.equals("NEW")){
					requestinsert = new SoapObject(NAMESPACE, METHOD_NAME_INSERT);
					scInsert = new InsertCustomerInfoWS();
					scInsert.execute();
				}
				else {
					requestupdate = new SoapObject(NAMESPACE, METHOD_NAME_UPDATE);
					scUpdate = new UpdateCustomerInfoWS();
					scUpdate.execute();
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	@Override
	public void onBackPressed() {
		Back();
	}
	
	private void Back(){
		Intent i = new Intent(getBaseContext(), ChooseCustomerActivity.class);
		i.putExtras(b);
		startActivity(i);
		this.finish();
	}
	
	private class InsertCustomerInfoWS extends AsyncTask<String, Void, Void>{

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			
			progressBar = new ProgressDialog(CustomerActivity.this);
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
			DBA_MASTER_AGENT dba = null;
			Cursor c = null;
			error = false;
			try{
				dba = new DBA_MASTER_AGENT(getBaseContext());
				dba.open();
				
				c = dba.getRow();
				c.moveToFirst();
				
		        pbranchid.setName("BranchID");         
		        pbranchid.setValue(c.getString(0));         
		        pbranchid.setType(String.class);         
		        requestinsert.addProperty(pbranchid);
		        
		        puserid.setName("UserID");         
		        puserid.setValue(c.getString(1));         
		        puserid.setType(String.class);         
		        requestinsert.addProperty(puserid);
		        
		        pfullname.setName("FullName");         
		        pfullname.setValue(name.getText().toString());         
		        pfullname.setType(String.class);         
		        requestinsert.addProperty(pfullname);
		        
		        paddress.setName("Address");         
		        paddress.setValue(alamat.getText().toString());         
		        paddress.setType(String.class);         
		        requestinsert.addProperty(paddress);
		        
		        prtno.setName("RtNo");         
		        prtno.setValue("");         
		        prtno.setType(String.class);         
		        requestinsert.addProperty(prtno);
		        	
		        prwno.setName("RwNo");         
		        prwno.setValue("");         
		        prwno.setType(String.class);         
		        requestinsert.addProperty(prwno);
		        
		        pkelurahan.setName("Kelurahan");         
		        pkelurahan.setValue("");         
		        pkelurahan.setType(String.class);         
		        requestinsert.addProperty(pkelurahan);
		        
		        pkecamatan.setName("Kecamatan");         
		        pkecamatan.setValue("");         
		        pkecamatan.setType(String.class);         
		        requestinsert.addProperty(pkecamatan);

		        pcity.setName("City");         
		        pcity.setValue(cityName.getText().toString());         
		        pcity.setType(String.class);         
		        requestinsert.addProperty(pcity);
		        
		        pcitycode.setName("CityCode");         
		        pcitycode.setValue(cityID.getText().toString());         
		        pcitycode.setType(String.class);         
		        requestinsert.addProperty(pcitycode);
		        
		        ppostcode.setName("PostCode");         
		        ppostcode.setValue(kodepos.getText().toString());         
		        ppostcode.setType(String.class);         
		        requestinsert.addProperty(ppostcode);

		        pphone.setName("Phone");         
		        pphone.setValue(((SpinnerGenericItem)phoneCodeSpn.getSelectedItem()).getDesc() +  mobile.getText().toString());         
		        pphone.setType(String.class);         
		        requestinsert.addProperty(pphone);
		        
		        pemail.setName("Email");         
		        pemail.setValue(email.getText().toString());         
		        pemail.setType(String.class);         
		        requestinsert.addProperty(pemail);
		        
		        pdob.setName("DOB");         
		        pdob.setValue(etTglLahir.getText().toString());         
		        pdob.setType(String.class);         
		        requestinsert.addProperty(pdob);
		        
		        poccupation.setName("Occupation");         
		        poccupation.setValue(((SpinnerGenericItem)spOccupation.getSelectedItem()).getCode());         
		        poccupation.setType(String.class);         
		        requestinsert.addProperty(poccupation);
		        
		        pbussiness.setName("Business");         
		        pbussiness.setValue(((SpinnerGenericItem)spBusiness.getSelectedItem()).getCode());         
		        pbussiness.setType(String.class);         
		        requestinsert.addProperty(pbussiness);
		        
		        ppob.setName("POB");         
		        ppob.setValue(pob.getText().toString());         
		        ppob.setType(String.class);         
		        requestinsert.addProperty(ppob);

		        pgender.setName("Gender");   
		        if (gender.isChecked())
		        	pgender.setValue("M"); 
		        else
		        	pgender.setValue("F"); 
		        pgender.setType(String.class);         
		        requestinsert.addProperty(pgender);
		        
		        pcuststatus.setName("CustStatus"); 
		        if (status1.isChecked())
		        	pcuststatus.setValue("S");    
		        else if (status2.isChecked())
		        	pcuststatus.setValue("M");   
		        else
		        	pcuststatus.setValue("W");
		        
		        pcuststatus.setType(String.class);         
		        requestinsert.addProperty(pcuststatus);
		        
		        
		        pcustidno.setName("CustIDNo");         
		        pcustidno.setValue(ktp.getText().toString());         
		        pcustidno.setType(String.class);         
		        requestinsert.addProperty(pcustidno);
		        
		        
		        pcustrange.setName("CustRange"); 
		        if (income1.isChecked())
		        	pcustrange.setValue("1");    
		        else if (income2.isChecked())
		        	pcustrange.setValue("2");
		        else if (income3.isChecked())
		        	pcustrange.setValue("3");   
		        else if (income4.isChecked())
		        	pcustrange.setValue("4");   
		        else
		        	pcustrange.setValue("5");
		        
		        pcustrange.setType(String.class);         
		        requestinsert.addProperty(pcustrange); 
		        

		    	envelope.setOutputSoapObject(requestinsert);
	    		androidHttpTransport.call(SOAP_ACTION_INSERT, envelope);  
	    		SoapPrimitive responseSoap = (SoapPrimitive)envelope.getResponse();  
	    		String response = responseSoap.toString();
	    	
	     		Log.i(TAG, "::doInBackground:" + "" + response);
	    				
	    		if (TextUtils.isEmpty(response) || !TextUtils.isDigitsOnly(response)) {
	    			error = true;
	    			errorMessage = "Gagal membuat customer. Silahkan coba lagi";
	    		}
	    		
			}catch (Exception e) {
        		error = true;
        		e.printStackTrace();	        		
				errorMessage = new MasterExceptionClass(e).getException();
			}finally{
				
				if (c != null)
					c.close();
				
				if(dba != null)
					dba.close();
			}
			
			return null;
		}
		
		@Override
	
		protected void onPostExecute(Void result) {

			super.onPostExecute(result);
			
			try{
				progressBar.hide();
				progressBar.dismiss();
				
				if(error){
					Utility.showCustomDialogInformation(CustomerActivity.this, "Informasi", 
							errorMessage);
				}else{
					//Intent i = new Intent(getBaseContext(),  ChooseCustomerActivity.class);
					Intent i = new Intent(getBaseContext(),  ChooseProductActivity.class);
					startActivity(i);
					CustomerActivity.this.finish();
					
					Toast.makeText(CustomerActivity.this, "Customer baru berhasil dibuat", Toast.LENGTH_SHORT).show();
				}
				
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
	

	private class UpdateCustomerInfoWS extends AsyncTask<String, Void, Void>{

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			
			progressBar = new ProgressDialog(CustomerActivity.this);
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
			DBA_MASTER_AGENT dba = null;
			Cursor c = null;
			error = false;
			try{
				dba = new DBA_MASTER_AGENT(getBaseContext());
				dba.open();
				
				c = dba.getRow();
				c.moveToFirst();
				
		        pbranchid.setName("CustomerNo");         
		        pbranchid.setValue(cust_code);         
		        pbranchid.setType(String.class);         
		        requestupdate.addProperty(pbranchid);
		        
		        puserid.setName("UserID");         
		        puserid.setValue(c.getString(1));         
		        puserid.setType(String.class);         
		        requestupdate.addProperty(puserid);
		        
		        pfullname.setName("FullName");         
		        pfullname.setValue(name.getText().toString());         
		        pfullname.setType(String.class);         
		        requestupdate.addProperty(pfullname);
		        
		        paddress.setName("Address");         
		        paddress.setValue(alamat.getText().toString());         
		        paddress.setType(String.class);         
		        requestupdate.addProperty(paddress);
		        
		        prtno.setName("RtNo");         
		        prtno.setValue("");         
		        prtno.setType(String.class);         
		        requestupdate.addProperty(prtno);
		        	
		        prwno.setName("RwNo");         
		        prwno.setValue("");         
		        prwno.setType(String.class);         
		        requestupdate.addProperty(prwno);
		        
		        pkelurahan.setName("Kelurahan");         
		        pkelurahan.setValue("");         
		        pkelurahan.setType(String.class);         
		        requestupdate.addProperty(pkelurahan);
		        
		        pkecamatan.setName("Kecamatan");         
		        pkecamatan.setValue("");         
		        pkecamatan.setType(String.class);         
		        requestupdate.addProperty(pkecamatan);

		        pcity.setName("City");         
		        pcity.setValue(cityName.getText().toString());         
		        pcity.setType(String.class);         
		        requestupdate.addProperty(pcity);
		        
		        pcitycode.setName("CityCode");         
		        pcitycode.setValue(cityID.getText().toString());         
		        pcitycode.setType(String.class);         
		        requestupdate.addProperty(pcitycode);
		        
		        ppostcode.setName("PostCode");         
		        ppostcode.setValue(kodepos.getText().toString());         
		        ppostcode.setType(String.class);         
		        requestupdate.addProperty(ppostcode);

		        pphone.setName("Phone");         
		        pphone.setValue(((SpinnerGenericItem)phoneCodeSpn.getSelectedItem()).getDesc() +  mobile.getText().toString());         
		        pphone.setType(String.class);         
		        requestupdate.addProperty(pphone);
		        
		        pemail.setName("Email");         
		        pemail.setValue(email.getText().toString());         
		        pemail.setType(String.class);         
		        requestupdate.addProperty(pemail);
		        
		        pdob.setName("DOB");         
		        pdob.setValue(etTglLahir.getText().toString());         
		        pdob.setType(String.class);         
		        requestupdate.addProperty(pdob);
		        
		        poccupation.setName("Occupation");         
		        poccupation.setValue(((SpinnerGenericItem)spOccupation.getSelectedItem()).getCode());         
		        poccupation.setType(String.class);         
		        requestupdate.addProperty(poccupation);
		        
		        pbussiness.setName("Business");         
		        pbussiness.setValue(((SpinnerGenericItem)spBusiness.getSelectedItem()).getCode());         
		        pbussiness.setType(String.class);         
		        requestupdate.addProperty(pbussiness);
		        
		        ppob.setName("POB");         
		        ppob.setValue(pob.getText().toString());         
		        ppob.setType(String.class);         
		        requestupdate.addProperty(ppob);

		        pgender.setName("Gender");   
		        if (gender.isChecked())
		        	pgender.setValue("M"); 
		        else
		        	pgender.setValue("F"); 
		        pgender.setType(String.class);         
		        requestupdate.addProperty(pgender);
		        
		        pcuststatus.setName("CustStatus"); 
		        if (status1.isChecked())
		        	pcuststatus.setValue("S");    
		        else if (status2.isChecked())
		        	pcuststatus.setValue("M");   
		        else
		        	pcuststatus.setValue("W");
		        
		        pcuststatus.setType(String.class);         
		        requestupdate.addProperty(pcuststatus);
		        
		        
		        pcustidno.setName("CustIDNo");         
		        pcustidno.setValue(ktp.getText().toString());         
		        pcustidno.setType(String.class);         
		        requestupdate.addProperty(pcustidno);
		        
		        
		        pcustrange.setName("CustRange"); 
		        if (income1.isChecked())
		        	pcustrange.setValue("1");    
		        else if (income2.isChecked())
		        	pcustrange.setValue("2");
		        else if (income3.isChecked())
		        	pcustrange.setValue("3");   
		        else if (income4.isChecked())
		        	pcustrange.setValue("4");   
		        else
		        	pcustrange.setValue("5");
		        
		        pcustrange.setType(String.class);         
		        requestupdate.addProperty(pcustrange);
		        
		        

		    	envelope.setOutputSoapObject(requestupdate);
	    		androidHttpTransport.call(SOAP_ACTION_UPDATE, envelope);  
	    		
	    		SoapPrimitive responseSoap = (SoapPrimitive)envelope.getResponse();  
	    		String response = responseSoap.toString();
	    		
	    		Log.i(TAG, "::doInBackground:" + "" + response);
	    				
	    		if (TextUtils.isEmpty(response) || !TextUtils.isDigitsOnly(response)) {
	    			error = true;
	    			errorMessage = "Gagal memperbarui customer. Silahkan coba lagi";
	    		}
	    				
			} catch (Exception e) {
        		error = true;
        		e.printStackTrace();	        		
				errorMessage = new MasterExceptionClass(e).getException();
			}finally{
				
				if (c != null)
					c.close();
				
				if(dba != null)
					dba.close();
			}
			
			return null;
		}
		
		@Override
	
		protected void onPostExecute(Void result) {

			super.onPostExecute(result);
			
			try{
				progressBar.hide();
				progressBar.dismiss();
				
				if(error){
					Utility.showCustomDialogInformation(CustomerActivity.this, "Informasi", 
							errorMessage);	
				}else{
					//Intent i = new Intent(getBaseContext(),  ChooseCustomerActivity.class);
					Intent i = new Intent(getBaseContext(),  ChooseProductActivity.class);
					startActivity(i);
					CustomerActivity.this.finish();
					
					Toast.makeText(CustomerActivity.this, "Data customer berhasil di-update", Toast.LENGTH_SHORT).show();
				}
				
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
	
	private class RetriveCustomerInfoWS extends AsyncTask<String, Void, Void>{

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			progressBar = new ProgressDialog(CustomerActivity.this);
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
				
				SoapObject table = null;                        // Contains table of dataset that returned through SoapObject
		        SoapObject tableRow = null;                     // Contains row of table
		        SoapObject responseBody = null;					// Contains XML content of dataset
				
		        customerNo.setName("CustomerNo");         
		        customerNo.setValue(params[0]);         
		        customerNo.setType(String.class);         
		        requestretrive.addProperty(customerNo);
		        
		    	envelope.setOutputSoapObject(requestretrive);
	    		envelope.bodyOut = requestretrive;
	    		androidHttpTransport.call(SOAP_ACTION, envelope);  

	    		
	            responseBody = (SoapObject) envelope.getResponse();
	            responseBody = (SoapObject) responseBody.getProperty(1);
	            
	            if (responseBody.getPropertyCount() == 0) {
	            	error = true;
	            	errorMessage = "Data customer tidak ditemukan";
	            	return null;
	            }
	            table = (SoapObject) responseBody.getProperty(0);
	            
	            customer = new ArrayList<HashMap<String, String>>(); 
	            
	            int iTotalDataFromWebService = table.getPropertyCount();
	            for(int i = 0; i < iTotalDataFromWebService; i++)
	            {
	            	tableRow = (SoapObject) table.getProperty(i);

		            HashMap<String, String> map = new HashMap<String, String>(); 
		            map.put("CUSTOMER_NO", tableRow.getPropertySafelyAsString("CUSTOMER_NO").toString()); 
		            map.put("CUSTOMER_NAME", tableRow.getPropertySafelyAsString("CUSTOMER_NAME").toString()); 
		            map.put("CUSTOMER_GENDER", tableRow.getPropertySafelyAsString("CUSTOMER_GENDER").toString());
		            map.put("CUSTOMER_STATUS", tableRow.getPropertySafelyAsString("CUSTOMER_STATUS").toString());
		            map.put("CUSTOMER_PHONE_NO", tableRow.getPropertySafelyAsString("CUSTOMER_PHONE_NO").toString());
		            map.put("CUSTOMER_EMAIL", tableRow.getPropertySafelyAsString("CUSTOMER_EMAIL").toString());
		           
		            //map.put("CUSTOMER_DOB", tableRow.getPropertySafelyAsString("CUSTOMER_DOB").toString());
		            Utility.mapData("CUSTOMER_DOB", map, tableRow);
		            map.put("CUSTOMER_POB", tableRow.getPropertySafelyAsString("CUSTOMER_POB").toString());
		            
		            Utility.mapDataString("CUSTOMER_ID", map, tableRow);
		            
		            map.put("CUSTOMER_ADDRESS", tableRow.getPropertySafelyAsString("CUSTOMER_ADDRESS").toString());
		            map.put("CUSTOMER_CITY", tableRow.getPropertySafelyAsString("CUSTOMER_CITY").toString());
		            map.put("CUSTOMER_CITY_CODE", tableRow.getPropertySafelyAsString("CUSTOMER_CITY_CODE").toString());
		            map.put("CUSTOMER_KODE_POS", tableRow.getPropertySafelyAsString("CUSTOMER_KODE_POS").toString());
		            
		            map.put("CUSTOMER_OCCUPATION", tableRow.getPropertySafelyAsString("CUSTOMER_OCCUPATION").toString());
		            map.put("CUSTOMER_BUSINESS", tableRow.getPropertySafelyAsString("CUSTOMER_BUSINESS").toString());
		            
		            Utility.mapDataString("CUSTOMER_RANGE", map, tableRow);
		            map.put("CRE_BY", tableRow.getPropertySafelyAsString("CRE_BY").toString());
		            
		            customer.add(map);
	            }	
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
			
			try{

				
				progressBar.hide();
				progressBar.dismiss();
				
				if(error){
					Utility.showCustomDialogInformation(CustomerActivity.this, "Informasi", 
							errorMessage);	
					btnNext.setVisibility(View.GONE);
				}else{
					
					HashMap<String, String> map = customer.get(0);
					
					fillCustomer (map);
					
				}
				
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
	
	private String convertToDate(String dateobj)
	{
		try
		{
			String tanggal = dateobj.substring(8,10);
			String bulan = dateobj.substring(5,7);
			String tahun = dateobj.substring(0,4);
			
			return (new StringBuilder().append(tanggal).append("/").append(bulan).append("/").append(tahun)).toString();
		}
		catch(Exception e)
		{
			return "";
		}
	}



	public void fillCustomer(HashMap<String, String> map) {
		// TODO Auto-generated method stub
		name.setText(map.get("CUSTOMER_NAME").toString());
		
		if (map.get("CUSTOMER_GENDER").toString().equals("M"))
			gender.setChecked(true);
		else
			gender.setChecked(false);
		
		if (map.get("CUSTOMER_STATUS").toString().equals("S"))
			status1.setChecked(true);
		else if (map.get("CUSTOMER_STATUS").toString().equals("M"))
			status2.setChecked(true);
		else
			status3.setChecked(true);
		
//		misal 0811551151
//		tanpa 0 -> 815151
//		dengan 62 -> 628151515
		
//		jadi 81115155511
//		kalo ga ada 0 tetep 815151
//		dengan 62 -> 62626252
		String phoneNumber = map.get("CUSTOMER_PHONE_NO").toString();
		
		Log.i(TAG, "::onPostExecute:" + "after remove 0:" + phoneNumber);
		
		if (phoneNumber.substring(0, 2).equals("62") ||
			phoneNumber.substring(0, 2).equals("65")) {
			
			SpinnerGenericAdapter a = (SpinnerGenericAdapter)phoneCodeSpn.getAdapter();
			phoneCodeSpn.setSelection(a.getItemIdByDesc(phoneNumber.substring(0, 2)));						
			mobile.setText(phoneNumber.substring(2));
		}
		else if (phoneNumber.startsWith("0")) {
			mobile.setText(phoneNumber.substring(1));
		}
		else 
			mobile.setText(phoneNumber);
			

		email.setText(map.get("CUSTOMER_EMAIL").toString());
		etTglLahir.setText(convertToDate (map.get("CUSTOMER_DOB").toString()));
		pob.setText(map.get("CUSTOMER_POB").toString());
		ktp.setText(map.get("CUSTOMER_ID").toString());
		alamat.setText(map.get("CUSTOMER_ADDRESS").toString());
		kodepos.setText(map.get("CUSTOMER_KODE_POS").toString());		
		
		//SpinnerGenericAdapter a = (SpinnerGenericAdapter)spCity.getAdapter();
		//spCity.setSelection(a.getItemId(map.get("CUSTOMER_CITY_CODE").toString()));
		
		cityName.setText(map.get("CUSTOMER_CITY").toString());
		cityID.setText(map.get("CUSTOMER_CITY_CODE").toString());
		
		
		//ambil data di Bundle
		if (city_name != null && city_id != null)
		{
			cityName.setText(city_name);
			cityID.setText(city_id);
		}
		
		
		SpinnerGenericAdapter a = (SpinnerGenericAdapter)spOccupation.getAdapter();
		spOccupation.setSelection(a.getItemId(map.get("CUSTOMER_OCCUPATION").toString()));
		
		a = (SpinnerGenericAdapter)spBusiness.getAdapter();
		spBusiness.setSelection(a.getItemId(map.get("CUSTOMER_BUSINESS").toString()));
		
		
		if (map.get("CUSTOMER_RANGE").toString().equals("1"))
			income1.setChecked(true);
		else if (map.get("CUSTOMER_RANGE").toString().equals("2"))
			income2.setChecked(true);
		else if (map.get("CUSTOMER_RANGE").toString().equals("3"))
			income3.setChecked(true);
		else if (map.get("CUSTOMER_RANGE").toString().equals("4"))
			income4.setChecked(true);
		else
			income5.setChecked(true);
	}



	@Override
	public void CheckCustomerIDListener(String result) { 
		RetrieveCustomerById ws;
		

		sectionMiddle.setVisibility(View.GONE);
		switch (Integer.parseInt(result)) {
		case 1:
			ws = new RetrieveCustomerById(CustomerActivity.this, ktp.getText().toString());
			ws.customerInterface= CustomerActivity.this;
			ws.execute();
			

			btnPrev.setText("Batal");
			btnNext.setText("Update");
			
			cust_action = "EDIT";
			
			break;

		case 2:
			emptyForm();
			Utility.showCustomDialogInformation(CustomerActivity.this, 
					"ID sudah terdaftar dengan identitas berbeda", 
					"Agent dapat menghubungi admin support untuk mendapat penjelasan jika ID, email, tanggal lahir yang dimasukkan sudah benar");
			break;
			
		case 3:

			ws = new RetrieveCustomerById(CustomerActivity.this, ktp.getText().toString());
			ws.customerInterface= CustomerActivity.this;
			ws.execute();
			
			
			btnPrev.setText("Batal");
			btnNext.setText("Update");
			
			cust_action = "EDIT";
			
			break;
			
		case 4:
			emptyForm();
			

			btnPrev.setText("Batal");
			btnNext.setText("Simpan");
			
			cust_action = "NEW";
			
			sectionMiddle.setVisibility(View.VISIBLE);
			
			break;
		default:
			emptyForm();
			break;
		}
	}



	@Override
	public void getCustomerList(ArrayList<HashMap<String, String>> custList) {
		if (custList != null) {
			sectionMiddle.setVisibility(View.VISIBLE);
			fillCustomer(custList.get(0));
		}
		else 
			emptyForm();
	}
	
	
}
