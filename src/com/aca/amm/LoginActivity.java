package com.aca.amm;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.http.conn.ConnectTimeoutException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.aca.amm.GetUserDataWS.GetuserdataListener; 
import com.aca.amm.R;
import com.aca.amm.ResetPasswordWS.ResetPassword;
import com.aca.database.DBA_MASTER_AGENT;
import com.aca.database.DBA_MASTER_DISC_COMM;
import com.aca.database.DBA_TABLE_CREATE_ALL;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.Message;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.attr.id;
import static android.provider.ContactsContract.CommonDataKinds.Identity.NAMESPACE;
import static com.aca.amm.R.id.progressBar;

public class LoginActivity extends Activity implements InterfaceLogin, ResetPassword, GetuserdataListener{



    private boolean error;
    private String errorMessage = "";
    private boolean removeOldID = false;
    
    private ArrayList<HashMap<String, String>> newsList = null;
    
    private RetrieveAccount sc;
     
    private EditText agentcode, password;
	
	private String previousActivity = "";
	protected String mode;
	private String pesan;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		
		/*************************************
		
		startActivity(new Intent(getBaseContext(), FirstActivity.class));
		this.finish();
		******/
		Intent i = getIntent();

		getIntentData();
		initControl();
	}

	private void initControl(){
		agentcode = (EditText)findViewById(R.id.txtUserName);
		password = (EditText)findViewById(R.id.txtPassword);

        if (BuildConfig.DEBUG) {
            agentcode.setText("068899");
            password.setText("aca32@@");
        }
	} 

	@Override
	protected void onResume() {
		super.onResume();
		
		Locale locale = new Locale("en");
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getBaseContext().getResources().updateConfiguration(config,
		      getBaseContext().getResources().getDisplayMetrics());
	}

	private void getIntentData() {
		if  (!TextUtils.isEmpty(getIntent().getStringExtra("PREVIOUS"))) {
			previousActivity = getIntent().getStringExtra("PREVIOUS");
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	public void btnForgotClick (View v) {
		final Dialog dialog = new Dialog(LoginActivity.this);
		dialog.setContentView(R.layout.dialog_forgot_password);
		dialog.setTitle("Forgot Password");


		int textViewId = dialog.getContext().getResources().getIdentifier("android:id/title", null, null);
		TextView tv = (TextView) dialog.findViewById(textViewId);
		tv.setTextColor(getResources().getColor(R.color.Black));
		
 
		Button dialogButtonReset = (Button) dialog.findViewById(R.id.dlgReset);
		Button dialogButtonCancel = (Button) dialog.findViewById(R.id.dlgCancel);

				
		dialogButtonReset.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText userIDEditText = (EditText)dialog.findViewById(R.id.txtUserID);
				
				RadioButton smsRadio = (RadioButton)dialog.findViewById(R.id.rgbSMS), 
							emailRadio = (RadioButton)dialog.findViewById(R.id.rgbEmail);
				
				String userid = userIDEditText.getText().toString().trim();
				
				//=======================================
				
				
				if (userid.isEmpty()) 
				{
					userIDEditText.requestFocus();
					userIDEditText.setError("Please fill the User ID / Username / Email");
					return;
				}
				
				if (!smsRadio.isChecked() && !emailRadio.isChecked()) 
				{
					smsRadio.requestFocus();
					smsRadio.setError("Choose 1");
					emailRadio.setError("Choose 1");
					
					return;
				}
				
				//=======================================
				 
				if (smsRadio.isChecked())
					mode = "2";
				else if (emailRadio.isChecked())
					mode =  "1";
				
				//=======================================
				
				
				final HashMap<String, String> map = new HashMap<String, String>();
				map.put("UserID", userid);
				map.put("mode", mode);				
				
				   
	    		GetUserDataWS ws = new GetUserDataWS(LoginActivity.this, map) ;
				ws.mCallBack = LoginActivity.this;
				ws.execute();	
			 
				dialog.dismiss();

			}
		});

		dialogButtonCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				
			}
		});

		dialog.show();
	}
	
	@Override
	public void getuserdataListener(HashMap<String, String> list) { 
		if (list != null) { 		  
			boolean lanjut = false;
			try {
			 
				String email = list.get("EMAIL_ADDRESS").trim();
				String phone = list.get("PHONE_NO").trim(); 
				pesan = "";  
				  
				if (mode.equals("1")) 
				{
					if (email.trim().isEmpty() || email.toLowerCase().contains("anytype")){ 
						pesan = "Tidak ada email terdaftar. Silahkan hubungi IT-ACA";
						lanjut = false;
					}
					else 
					{
						lanjut = true;
						email = "***" + email.substring(3, email.length());
						pesan = "Password yang baru akan dikirimkan ke email " + email;
					}
				}
				else 
				{
					if (phone.trim().isEmpty() || phone.toLowerCase().contains("anytype")){
						pesan = "Tidak ada No.HP terdaftar. Silahkan hubungi IT-ACA";
						lanjut = false;
					}	
					else {
						lanjut = true;
						phone = phone.substring(0, phone.length()-3) + "***";
						pesan = "Password yang baru akan dikirimkan ke No.hp " + phone;
					}
				}
			} catch (Exception e) { 
				e.printStackTrace();
			}  
			finally { 

				if (lanjut) {
					ResetPasswordWS ws = new ResetPasswordWS(LoginActivity.this, list);
					ws.mCallBack = LoginActivity.this;
					ws.execute();
				}
				else {
					Utility.showCustomDialogInformation(LoginActivity.this, "Informasi", pesan);
				}
				
			}
		}
		else {
			Utility.showCustomDialogInformation(this, "Informasi", "User id is invalid");
		}
	}
	

	@Override
	public void ResetPasswordListener(String result) {
		Utility.showCustomDialogInformation(LoginActivity.this, "Informasi", pesan);
	}
	

	
	public void btnLoginClick(View v) {

		boolean bError = false;
		String sError = "";
		
		if (agentcode.getText().length() == 0)
		{
			bError = true;
			sError = "User ID harus diisi";
			
		}
		else if (password.getText().length() == 0)
		{
			bError = true;
			sError = "Password harus diisi";
		}
		
		if (bError)
		{
			Utility.showCustomDialogInformation(LoginActivity.this, "Informasi", 
					sError);
		}
		else
		{

			sc = new RetrieveAccount(this, agentcode.getText().toString(), password.getText().toString());
            sc.interfaceLogin = this;
			sc.execute();
		}
	}
	/*

	private class RetrieveAccount extends AsyncTask<String, Void, Void>{


		@Override
		protected void onPreExecute() {

			super.onPreExecute();

			progressBar = new ProgressDialog(LoginActivity.this);
			progressBar.setCancelable(false);
			progressBar.setMessage("Please wait ...");
			progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressBar.show();
			
			envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); 
			envelope.implicitTypes = true;
	    	envelope.dotNet = true;	//used only if we use the webservice from a dot net file (asmx)
	    	
	    	envelope2 = new SoapSerializationEnvelope(SoapEnvelope.VER11); 
			envelope2.implicitTypes = true;
			envelope2.dotNet = true;	//used only if we use the webservice from a dot net file (asmx)
			
			envelope3 = new SoapSerializationEnvelope(SoapEnvelope.VER11); 
			envelope3.implicitTypes = true;
			envelope3.dotNet = true;	//used only if we use the webservice from a dot net file (asmx)
			
			
			androidHttpTransport = new HttpTransportSE(URL);
		}
		
		@Override
		protected Void doInBackground(String... params) {

    		
			DBA_MASTER_AGENT dba = null;
			DBA_MASTER_DISC_COMM dbaDC = null;
			Cursor c = null;
			
			error = false;
			removeOldID = false;
			
			try{
				
				id.setName("AgentCode");
	    		id.setValue(agentcode.getText().toString().trim());
	    		id.setType(String.class);
	    		requestretrive.addProperty(id);
	    		
	    		pass.setName("Password");
	    		pass.setValue(password.getText().toString());
	    		pass.setType(String.class);
	    		requestretrive.addProperty(pass);
	    		
	    		envelope.setOutputSoapObject(requestretrive);
	    		envelope.bodyOut = requestretrive;
	    		androidHttpTransport.call(SOAP_ACTION, envelope);  

            	SoapObject result = (SoapObject)envelope.bodyIn;
            	
            	String loginResult = result.getPropertySafelyAsString("ValidateLoginAgentNewResult");
            	
            	if (loginResult.toLowerCase().contains("anytype"))
            		loginResult = "";
            	
	        	if(!TextUtils.isEmpty(loginResult)) {
	        		error = true;
	        		errorMessage = loginResult;
	        	
	        		return null;
	        	}
	        	else
	        	{   
	        		
					dba = new DBA_MASTER_AGENT(getBaseContext());
		            dba.open();
		            
		          
		            
	        		SoapObject table = null;                        // Contains table of dataset that returned through SoapObject
			        SoapObject tableRow = null;                     // Contains row of table
			        SoapObject responseBody = null;					// Contains XML content of dataset
			    
		    		requestretrive2.addProperty(id);
		    		envelope2.setOutputSoapObject(requestretrive2);
		    		envelope2.bodyOut = requestretrive2;
		    		androidHttpTransport.call(SOAP_ACTION_DETAIL, envelope2);  
		    				    		
		            responseBody = (SoapObject) envelope2.getResponse();		            
		            responseBody = (SoapObject) responseBody.getProperty(1);
		            
		            if (responseBody.getPropertyCount() == 0) {
		            	error = true;
		            	errorMessage = "Data agent kosong";
		            	return null;
		            }
		            
		            table = (SoapObject) responseBody.getProperty(0);
		            tableRow = (SoapObject) table.getProperty(0);
	            
		            if (!error)
		            {
		            	
		            	c = dba.getRow();
				            
			            //cek apakah sudah ada kode agent di DB
			            if (c.getCount() > 0)
			            {
			            	c.moveToFirst();
			            	//jika sudah ada, apakah sama?, jika tidak sama maka flag error
			            	//1 device harus 1 agent code, tidak bisa di-sharing
			            	if (!c.getString(1).equalsIgnoreCase(tableRow.getPropertySafelyAsString("U_ID"))) 
			            	{
			            		error = true;
			            		removeOldID = true;
			            		return null;
			            	}
			            }
			            else 
			            {
		            		File fileOrDirectory = new File(Environment.getExternalStorageDirectory() + "/LoadImg/");
							Utility.DeleteRecursive(fileOrDirectory);
						}
				            

		            	
		            	id.setValue(tableRow.getPropertySafelyAsString("U_ID").toString());
		            	
			            requestretrive3.addProperty(id);
			    		envelope3.setOutputSoapObject(requestretrive3);
			    		envelope3.bodyOut = requestretrive3;
			    		androidHttpTransport.call(SOAP_ACTION_DISC_COMM, envelope3);  
			            
			    		responseBody = (SoapObject) envelope3.getResponse();		            
			            responseBody = (SoapObject) responseBody.getProperty(1);
			            
			            if (responseBody.getPropertyCount() == 0) {
			            	error = true;
			            	errorMessage = "Diskon komisi belum ada";
			            	return null;
			            	
			            }
			            
			            dba.deleteAll();
			            dba.insert(
			            			  tableRow.getPropertySafelyAsString("BRANCH_ID").toString()
				            		, tableRow.getPropertySafelyAsString("U_ID").toString()
				            		, tableRow.getPropertySafelyAsString("SIGN_PLACE").toString()
				            		, tableRow.getPropertySafelyAsString("MKT_CODE").toString()
				            		, tableRow.getPropertySafelyAsString("OFFICE_ID").toString()
				            		, tableRow.getPropertySafelyAsString("MAX_ROW").toString()
		            				
				            		, tableRow.getPropertySafelyAsString("STATUS_USER").toString()
		            				, tableRow.getPropertySafelyAsString("USER_EXP_DATE").toString()
		            				, tableRow.getPropertySafelyAsString("EMAIL_ADDRESS").toString()
		            				, tableRow.getPropertySafelyAsString("PHONE_NO").toString()
		            				, tableRow.getPropertySafelyAsString("USER_NAME").toString()
		            				, tableRow.getPropertySafelyAsString("U_NAME").toString()
		            				, Utility.md5(tableRow.getPropertySafelyAsString("U_PASS").toString())
			            		);

//			            
			            table = (SoapObject) responseBody.getProperty(0);
			            tableRow = (SoapObject) table.getProperty(0);


			            dbaDC = new DBA_MASTER_DISC_COMM(getBaseContext());
			            dbaDC.open();
			            dbaDC.deleteAll();
			            
			            int iTotalDataFromWebService = table.getPropertyCount();
			            
			            for(int i = 0; i < iTotalDataFromWebService; i++)
			            {
			            	tableRow = (SoapObject) table.getProperty(i);
				            dbaDC.insert(tableRow.getPropertySafelyAsString("AGENT_CODE").toString()
				            		, tableRow.getPropertySafelyAsString("LOB").toString()
				            		, tableRow.getPropertySafelyAsString("BRANCH_CODE").toString()
				            		, Double.parseDouble(tableRow.getPropertySafelyAsString("DISC_COMM").toString())
				            		, Double.parseDouble(tableRow.getPropertySafelyAsString("DISCOUNT").toString())
				            		, Double.parseDouble(tableRow.getPropertySafelyAsString("COMMISION").toString()));
			            }	
			            error = false;

		            }		            
	        	
	        	}}
				catch (Exception e) {
	        		error = true;
	        		e.printStackTrace();	        		
					errorMessage = new MasterExceptionClass(e).getException();
				}
	        	finally{
				if(dba != null)
					dba.close();
				
				if(dbaDC != null)
					dbaDC.close();
			}
			
			return null;
		}
		
		@Override
	
		protected void onPostExecute(Void result) {
			
			super.onPostExecute(result);
			
			progressBar.hide();
			progressBar.dismiss();
			
			try{	
				if (removeOldID) {
					Utility.showCustomDialogInformation(LoginActivity.this, "Informasi", 
							"User ID lain telah terdaftar pada device ini");
//					ShowDialog(R.layout.dialog_remove_userid, "Konfirmasi", R.id.dlgOK, R.id.dlgCancel);
				}
				else {
					if(error){
						Utility.showCustomDialogInformation(LoginActivity.this, "Informasi", errorMessage);
					}
					else{
						SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences("com.aca.amm", Context.MODE_PRIVATE);
						sharedPreferences
							.edit()
							.putLong("SESSION", System.currentTimeMillis())
							.apply();
						
						Intent i = new Intent(getBaseContext(),  FirstActivity.class);
						startActivity(i);
						LoginActivity.this.finish();	
					}			
					
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
			finally {
				removeOldID = false;
				error = false;
				errorMessage = "";
				
			}
		}
	}*/

	public ArrayList<HashMap<String, String>> getCurrentNews (Context ctx) throws InterruptedException {
	
		CurrentNews news = new CurrentNews(ctx);
		boolean error = false;
		try {
			news.execute();
			news.get(10000, TimeUnit.MILLISECONDS);
		} catch (ExecutionException e) {
			e.printStackTrace();
			error = true;
		} catch (TimeoutException e) {
			e.printStackTrace();
			error = true;
		}
	    finally {
	    }

		if (error)
			return null;
		else
			return news.getCurrentNews();
	}
	
	public void ChooseCustomerClick()
	{
		Bundle b = new Bundle();
		b.putString("FLAG", "CUSTOMER");
		b.putString("ACTION", "NEW");
		
		Intent i = new Intent(getBaseContext(),  ChooseCustomerActivity.class);
		i.putExtras(b);
		startActivity(i);
		this.finish();
	}
	
	public void SPPAClick()
	{
		Intent i = new Intent(getBaseContext(),  SPPAActivity.class);
		startActivity(i);
		this.finish();
	}
	
	public void SyncClick()
	{
		Intent i = new Intent(getBaseContext(),  SyncActivity.class);
		startActivity(i);
		this.finish();
	}
	
	public void ProductClick () 
	{
		Intent i = new Intent(getBaseContext(),  ChooseProductActivity.class);
		startActivity(i);
		this.finish();
	}
	
	private void ShowDialog(final int layoutID, String title, int buttonOKId, int buttonCancelId)
	{
		// custom dialog
		final Dialog dialog = new Dialog(LoginActivity.this);
		dialog.setContentView(layoutID);
		dialog.setTitle(title);


		int textViewId = dialog.getContext().getResources().getIdentifier("android:id/title", null, null);
		TextView tv = (TextView) dialog.findViewById(textViewId);
		tv.setTextColor(getResources().getColor(R.color.Black));


		Button dialogButtonOK = (Button) dialog.findViewById(buttonOKId);
		Button dialogButtonCancel = (Button) dialog.findViewById(buttonCancelId);

		dialogButtonOK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (layoutID == R.layout.dialog_remove_userid) {
					
					ShowDialog(R.layout.dialog_remove_userid_confirmation, "Konfirmasi", R.id.dlgOK, R.id.dlgCancel);
					dialog.dismiss();
				}
				else if (layoutID == R.layout.dialog_remove_userid_confirmation) {
					EditText currentPassword = (EditText)dialog.findViewById(R.id.edCurrentPassword);
					
					
					DBA_MASTER_AGENT dbAgent = new DBA_MASTER_AGENT(LoginActivity.this);
					dbAgent.open();
					
					
				 	Cursor c = dbAgent.getRow();
					c.moveToFirst();
					
					String userID = c.getString(1);
					
					dbAgent.close();
					
					if (TextUtils.isEmpty(currentPassword.getText())) {
						Utility.showCustomDialogInformation(LoginActivity.this, "Informasi", "Password harus diisi");
					}
					else {
						Log.d("user id ", userID);
						Log.d("password", currentPassword.getText().toString().trim());
						
//						LoginUserWS ws = new LoginUserWS(LoginActivity.this, 
//								userID,
//								currentPassword.getText().toString().trim());
//						ws.mCallBack = LoginActivity.this;
//						ws.execute();

//						dialog.dismiss();
					}
				}
				
			}
		});

		dialogButtonCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	protected void dropDatabase() {
		DBA_TABLE_CREATE_ALL dba = new DBA_TABLE_CREATE_ALL(this);

		try {
			dba.open();
			dba.drop();
			
			Toast.makeText(this, "Data lama sukses dihapus", Toast.LENGTH_SHORT).show();
						
		}catch (Exception ex) {
			ex.printStackTrace();
			Toast.makeText(this, "Data lama gagal dihapus", Toast.LENGTH_SHORT).show();
		}
		finally { 
			dba.close();
		}
	}

	@Override
	public void loginUser(boolean login) {
		if(login) {
/*			dropDatabase();
			startActivity(new Intent(LoginActivity.this, SplashActivity.class));
			LoginActivity.this.finish();*/

            Intent i = new Intent(LoginActivity.this,  FirstActivity.class);
            startActivity(i);
            finish();
		}
		
	}

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
	
	private void Back(){
		try{
			if (previousActivity.equals("REGISTERORLOGIN"))
			{
				Intent i = new Intent(getBaseContext(),  RegisterOrLoginActivity.class);
				
				startActivity(i);
				this.finish();
			}
			else {
				Intent i = new Intent(getBaseContext(),  FirstActivity.class);
				
				startActivity(i);
				this.finish();
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

	
	
}
