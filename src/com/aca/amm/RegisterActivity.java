package com.aca.amm;

import java.util.HashMap;
import java.util.Locale;
import java.util.jar.Attributes.Name;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.aca.amm.CheckUserNameWS.CheckUsernameCallback;
import com.aca.amm.RegisterUsernameWS.RegisterUsernameCallBack;

import android.os.Bundle;
import android.os.Message;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.res.Configuration;
import android.content.Intent;
import android.graphics.Color;
import android.renderscript.Type;
import android.support.v4.text.TextUtilsCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class RegisterActivity extends Activity 
							implements RegisterUsernameCallBack, CheckUsernameCallback{
	@SuppressWarnings("unused")
	private static final String TAG = "com.aca.amm.RegisterActivity";
	
	EditText nama;	
	EditText id;
	EditText handphone;
	EditText email;
	EditText confirmEmail;
	EditText username;
	EditText password;
	EditText confirmPassword; 
	EditText bankAccount;
	Spinner phoneCodeSpn, bankNameSpn;
	
	Button cancel,  register;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);
		
		initVariable();
		registerListener();
	}
	
	


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		Locale locale = new Locale("en");
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getBaseContext().getResources().updateConfiguration(config,
		      getBaseContext().getResources().getDisplayMetrics());
	}




	private void initVariable() {
		nama			 = (EditText)findViewById(R.id.txtNama);
		id 				 = (EditText)findViewById(R.id.txtID);
		handphone		 = (EditText)findViewById(R.id.txtPhoneNumber);
		email			 = (EditText)findViewById(R.id.txtEmail);
		confirmEmail	 = (EditText)findViewById(R.id.txtConfirmEmail);
		username		 = (EditText)findViewById(R.id.txtUserName);
		password 		 = (EditText)findViewById(R.id.txtPassword);
		confirmPassword  = (EditText)findViewById(R.id.txtConfirmPassword);
		phoneCodeSpn	 = (Spinner)findViewById(R.id.txtPhoneCode);
		bankNameSpn		 = (Spinner)findViewById(R.id.txtBankName);
		
		bankAccount	 	 = (EditText)findViewById(R.id.txtBankAccount);
		
		cancel = (Button)findViewById(R.id.btnCancel);
		register  = (Button)findViewById(R.id.btnRegister);
		
		Utility.BindPhoneCode(phoneCodeSpn, RegisterActivity.this, RegisterActivity.this);
		Utility.BindBankName(bankNameSpn, RegisterActivity.this, RegisterActivity.this);

	}
	
	private boolean validasiForm() {
		boolean valid = true;
		
		if (TextUtils.isEmpty(nama.getText().toString().trim())) {
			nama.requestFocus();
			nama.setError("Please enter your name");
			
			return false;
		}
		
		id.setText("111");
		
		if (TextUtils.isEmpty(id.getText().toString().trim())) {
			id.requestFocus();
			id.setError("Please enter your ID");
			
			return false;
		}
		if (TextUtils.isEmpty(handphone.getText().toString().trim())) {
			handphone.requestFocus();
			handphone.setError("Please enter your phone number");
			
			return false;
		}
		if (TextUtils.isEmpty(email.getText().toString().trim())) {
			email.requestFocus();
			email.setError("Please enter your email");
			
			return false;
		}
		if (TextUtils.isEmpty(confirmEmail.getText().toString().trim())) {
			confirmEmail.requestFocus();
			confirmEmail.setError("Please enter your email");
			
			return false;
		}
		
		if(!email.getText().toString().contains("@")	 ||
			email.getText().toString().startsWith("@")	 ||
			email.getText().toString().endsWith("@"))  {

			email.requestFocus();
			email.setError("Invalid email input");
			
			return false;
		}
		
		if (!email.getText().toString().trim()
				.equalsIgnoreCase(confirmEmail.getText().toString().trim()) ) {
			
			confirmEmail.requestFocus();
			confirmEmail.setError("Email did not match");
			
			return false;
		}
		
		if (TextUtils.isEmpty(username.getText().toString().trim())) {
			username.requestFocus();
			username.setError("Please enter your username");
			
			return false;
		}
		if (TextUtils.isEmpty(password.getText().toString().trim())) {
			password.requestFocus();
			password.setError("Please enter your password");
			
			return false;
		}
		if (TextUtils.isEmpty(confirmPassword.getText().toString().trim())) {
			confirmPassword.requestFocus();
			confirmPassword.setError("Please enter your password");
			
			return false;
		}
		
		
		if (password.getText().toString().trim().length() < 6) {
			password.requestFocus();
			password.setError("Password should be at least 6 characters");
			return false;	
		}

		if (!Utility.hasSpecialChar(password.getText().toString())) {
			password.requestFocus();
			password.setError("Password should has at least 1 special character");
			return false;
		}
		
		
		if (!TextUtils.equals(password.getText().toString().trim(), 
						confirmPassword.getText().toString().trim())) {
			
			confirmPassword.requestFocus();
			confirmPassword.setError("Password did not match");
			
			return false;
		}
		
		if (TextUtils.isEmpty(bankAccount.getText().toString().trim())) {
			bankAccount.requestFocus();
			bankAccount.setError("Please enter your Bank Account");
			
			return false;
		}
		
		return valid;
		
	}
	
	private void registerListener() {
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Back();
//				password.setInputType(InputType.TYPE_NULL);
//				confirmPassword.setInputType(InputType.TYPE_NULL);
//				confirmPassword.setText(Utility.md5(password.getText().toString())); 
				
			}
		});
		
		phoneCodeSpn.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int pos, long arg3) {
					Log.i(TAG, "::onItemSelected:" + "phone code :" + phoneCodeSpn.getSelectedItem().toString());
					Log.i(TAG, "::onItemSelected:" + "phone code :" + ((SpinnerGenericItem)phoneCodeSpn.getSelectedItem()).getDesc());
					
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				boolean valid = validasiForm();
				
				if (valid) {
//					validasiUserName();

					
					 HashMap<String, String> map = new HashMap<String, String>();
					 map.put("ChannelCode", "A");
					 map.put("Nama", nama.getText().toString().trim());
					 map.put("HPNo", ((SpinnerGenericItem)phoneCodeSpn.getSelectedItem()).getDesc() + handphone.getText().toString().trim());
					 map.put("EmailAddress", email.getText().toString().trim());
					 map.put("UserName", username.getText().toString().trim());
					 map.put("Password", password.getText().toString().trim());
					 map.put("BankAccount", bankAccount.getText().toString().trim());
					 map.put("BankName", ((SpinnerGenericItem)bankNameSpn.getSelectedItem()).getDesc());

//					 map.put("NoID", id.getText().toString().trim());
					 
					RegisterUsernameWS ws = new RegisterUsernameWS(RegisterActivity.this, map);
					ws.mCallBackListener = RegisterActivity.this;
					ws.execute();					
			
					
				}
				else {
					Utility.toastMakeTextShort(RegisterActivity.this, "Please complete the form");
					
				}
			}
		});
	}


	@Override
	public void RegisterUsernameListener(boolean result, String message) {
		Log.i(TAG, "::RegisterUsernameListener:" + message);
		Log.i(TAG, "::RegisterUsernameListener:" + result);
		

		username.setError(null);
		email.setError(null);
	
		if (result && TextUtils.isEmpty(message)) {
			Dialog dialog = Utility.showCustomDialogInformation(this, "Informasi", 
					"Proses Registrasi selesai silahkan cek Email/SMS anda.");
			dialog.setOnDismissListener(new OnDismissListener() {
				
				@Override
				public void onDismiss(DialogInterface dialog) {
					startActivity(new Intent(RegisterActivity.this, FirstActivity.class));
					RegisterActivity.this.finish();
				}
			});
			dialog.show();
			
		}
		else if (result && !TextUtils.isEmpty(message)) {
			
			if (message.toLowerCase().contains("mail")) {
				email.requestFocus();
				email.setError(message);				
			}
			
			else if (message.toLowerCase().contains("user") && 
				message.toLowerCase().contains("name") ) 
			{
				username.requestFocus();
				username.setError(message);
			}
			else {
				Utility.showCustomDialogInformation(this, "Informasi", message);
			}
		}
		
	}

	
	protected void validasiUserName() {
		HashMap<String, String> map = new  HashMap<String, String>();
		map.put("UserName", username.getText().toString().trim());
		
		CheckUserNameWS ws = new CheckUserNameWS(this, map);
		ws.mCallBack = this;
		ws.execute();
		
		
	}
	

	@Override
	public void checkUsernameListener(boolean result) {
		if (!result) {
			username.requestFocus();
			username.setError("Username is already exist");
		}
		else {
			
			Log.i(TAG, "::checkUsernameListener:" + ((SpinnerGenericItem)phoneCodeSpn.getSelectedItem()).getDesc() + handphone.getText().toString().trim());
		
			 HashMap<String, String> map = new HashMap<String, String>();
			 map.put("ChannelCode", "A");
			 map.put("Nama", nama.getText().toString().trim());
			 map.put("NoID", id.getText().toString().trim());
			 map.put("HPNo", ((SpinnerGenericItem)phoneCodeSpn.getSelectedItem()).getDesc() + handphone.getText().toString().trim());
			 map.put("EmailAddress", email.getText().toString().trim());
			 map.put("UserName", username.getText().toString().trim());
			 map.put("Password", password.getText().toString().trim());
			 
			 
			RegisterUsernameWS ws = new RegisterUsernameWS(RegisterActivity.this, map);
			ws.mCallBackListener = RegisterActivity.this;
			ws.execute();					
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
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
			Intent i = new Intent(getBaseContext(),  RegisterOrLoginActivity.class);
			
			startActivity(i);
			this.finish();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}


	@Override
	public void RegisterUsernameListener(boolean result) {
		// TODO Auto-generated method stub
		
	}
	

	public void btnHomeClick(View v) {
		Intent i = new Intent(getBaseContext(),  FirstActivity.class);
		startActivity(i);
		this.finish();
	}
	


	
	
	
	
}
