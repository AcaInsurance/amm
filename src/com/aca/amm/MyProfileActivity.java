package com.aca.amm;

import java.util.HashMap;
import java.util.logging.Logger;

import org.w3c.dom.Comment;

import com.aca.amm.ChangePasswordWS.ChangePassword;
import com.aca.amm.ChangeUsernameWS.ChangeUsername;
import com.aca.database.DBA_MASTER_ACA_MOBIL_RATE;
import com.aca.database.DBA_MASTER_AGENT;

import android.os.Bundle;
import android.R.integer;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.text.TextUtils;

public class MyProfileActivity extends ControlNormalActivity implements InterfaceLogin, ChangePassword, ChangeUsername  {
	EditText  namaEditText,
		 	  handphoneEditText,
		 	  emailEditText,
			  usernameEditText;
	
	protected EditText oldPasswordEditText;
	protected EditText newPasswordEditText;
	protected EditText confirmPasswordEditText;
	
	protected EditText currentUsername;
	protected EditText newUsername;
	protected EditText yourPassword;
	
	private Dialog dialog;
			

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my_profile);
		
		initVariable();
		loadDB();
	}

	private void loadDB() {
		DBA_MASTER_AGENT dbAgent = null;
		Cursor cursor = null;
		
		try {
			dbAgent = new DBA_MASTER_AGENT(this);
			dbAgent.open();
			
			cursor  = dbAgent.getRow();
			cursor.moveToFirst();
			
			namaEditText.setText(cursor.getString(13).toUpperCase());
			handphoneEditText.setText(cursor.getString(11));
			emailEditText.setText(cursor.getString(10).toUpperCase());
			usernameEditText.setText(cursor.getString(12).toUpperCase());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (dbAgent != null) dbAgent.close();
			if (cursor != null) cursor.close();
			
		}
		
	}

	private void initVariable() {
		 namaEditText 		= (EditText) findViewById(R.id.txtNama);
	 	 handphoneEditText	 = (EditText) findViewById(R.id.txtPhoneNumber);
	 	 emailEditText		 = (EditText) findViewById(R.id.txtEmail);
		 usernameEditText = (EditText) findViewById(R.id.txtUserName);
		 
		 namaEditText.setEnabled(false);
		 handphoneEditText.setEnabled(false);
		 emailEditText.setEnabled(false);
		 usernameEditText.setEnabled(false);
		 
		 namaEditText.setTextColor(Color.BLACK);
		 handphoneEditText.setTextColor(Color.BLACK);
		 emailEditText.setTextColor(Color.BLACK);
		 usernameEditText.setTextColor(Color.BLACK);
	}

	public void btnChangeUsernameClick (View v) {
		dialog = new Dialog(this);
		dialog.setContentView(R.layout.dialog_change_username);
		dialog.setTitle("Change Username");
		
		int textViewId = dialog.getContext().getResources().getIdentifier("android:id/title", null, null);
		TextView tv = (TextView) dialog.findViewById(textViewId);
		tv.setTextColor(getResources().getColor(R.color.Black));

 
		Button dialogOK = (Button) dialog.findViewById(R.id.dlgOK);
		Button dialogCancel = (Button) dialog.findViewById(R.id.dlgCancel);
		
		currentUsername = (EditText)dialog.findViewById(R.id.txtCurrentUserName);
		newUsername = (EditText)dialog.findViewById(R.id.txtNewUsername);
		yourPassword = (EditText)dialog.findViewById(R.id.txtYourPassword);
		
		currentUsername.setText(usernameEditText.getText().toString());
		currentUsername.setEnabled(false);
		
		
		dialogOK.setOnClickListener(new OnClickListener() {
		
		 	
			@Override
			public void onClick(View v) {
				if (!Utility.isEmptyField(newUsername))
					return;
				if (!Utility.isEmptyField(yourPassword))
					return;
				
				// 2nd validation - check password

				DBA_MASTER_AGENT dbAgent = new DBA_MASTER_AGENT(MyProfileActivity.this);
				dbAgent.open();
				int validLogin = dbAgent.login(
			   						  Utility.getUserID(MyProfileActivity.this)
									, Utility.md5(yourPassword.getText().toString()) );
				
				dbAgent.close();
				
				if (validLogin == 0) {
					yourPassword.requestFocus();
					yourPassword.setError("Your password is incorrect");
					return;
				}

				HashMap<String, String> map = new HashMap<String, String>();
				map.put("UserID",  Utility.getUserID(MyProfileActivity.this));
				map.put("UserName",newUsername.getText().toString().trim());
				
				ChangeUsernameWS ws = new ChangeUsernameWS(MyProfileActivity.this, map);
				ws.mCallBack = MyProfileActivity.this;
				ws.execute();
			}
		});
		
		
		dialogCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
 
		dialog.show();
	}
	
	@Override
	public void ChangeUsernameListener(Boolean result) {
		if (result) {
			DBA_MASTER_AGENT dbAgent = new DBA_MASTER_AGENT(this);
			try {
				dbAgent.open();
				dbAgent.updateUsername(newUsername.getText().toString());
				
				usernameEditText.setText(newUsername.getText().toString());
				
				dialog.dismiss();
			
			} catch (SQLException e) {
				e.printStackTrace();
			}
			finally {
				if (dbAgent != null) {
					dbAgent.close();					
				}
			}	
		}
	}
	

	public void btnChangePasswordClick (View v) {
		// custom dialog
		dialog = new Dialog(this);
		dialog.setContentView(R.layout.dialog_change_password);
		dialog.setTitle("Change Password");
		
		int textViewId = dialog.getContext().getResources().getIdentifier("android:id/title", null, null);
		TextView tv = (TextView) dialog.findViewById(textViewId);
		tv.setTextColor(getResources().getColor(R.color.Black));

 
		Button dialogReset = (Button) dialog.findViewById(R.id.dlgReset);
		Button dialogCancel = (Button) dialog.findViewById(R.id.dlgCancel);
		
		dialogReset.setOnClickListener(new OnClickListener() {
		
		 	
			@Override
			public void onClick(View v) {
				oldPasswordEditText = (EditText)dialog.findViewById(R.id.txtOldPassword);
				newPasswordEditText = (EditText)dialog.findViewById(R.id.txtNewPassword);
				confirmPasswordEditText = (EditText)dialog.findViewById(R.id.txtConfirmPassword);
				
				if (!Utility.isEmptyField(oldPasswordEditText))
					return;
				if (!Utility.isEmptyField(newPasswordEditText))
					return;
				if (!Utility.isEmptyField(confirmPasswordEditText))
					return;
				
				// 2nd validation

				
				if (newPasswordEditText.length() < 6) {
					newPasswordEditText.requestFocus();
					newPasswordEditText.setError("Password should be at least 6 characters");
					return;	
				}

				if (!Utility.hasSpecialChar(newPasswordEditText.getText().toString())){
					newPasswordEditText.requestFocus();
					newPasswordEditText.setError("Password should has at least 1 special character");
					return;
				}
				
				
				if (!newPasswordEditText.getText().toString()
						.equals(confirmPasswordEditText.getText().toString()) )
				{	
					confirmPasswordEditText.requestFocus();
					confirmPasswordEditText.setError("Password did not match");
					return;
				}
			
				// 2nd validation - check password

				DBA_MASTER_AGENT dbAgent = new DBA_MASTER_AGENT(MyProfileActivity.this);

				try {
					dbAgent.open();
					int validLogin = dbAgent.login(
	   						  Utility.getUserID(MyProfileActivity.this)
							, Utility.md5(oldPasswordEditText.getText().toString()) );

					if (validLogin == 0) {
						oldPasswordEditText.requestFocus();
						oldPasswordEditText.setError("Your password is incorrect");
						return;
					}
					
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("UserID", Utility.getUserID(MyProfileActivity.this));
					map.put("Password", newPasswordEditText.getText().toString());
					
					ChangePasswordWS ws = new ChangePasswordWS(MyProfileActivity.this, map);
					ws.mCallBack = MyProfileActivity.this;
					ws.execute();

					
				} catch (Exception e) {
					e.printStackTrace();
					return;
					
				} finally {
					if (dbAgent != null)
						dbAgent.close();
				}
				

				
				// call ws to match user id and old pass
//				
//				LoginUserWS ws = new LoginUserWS(
//											MyProfileActivity.this, 
//											Utility.getUserID(MyProfileActivity.this), 
//											oldPasswordEditText.getText().toString() );
//							
//				ws.mCallBack = MyProfileActivity.this;
//				ws.execute();
			}
		});
		
		
		dialogCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
 
		dialog.show();
	}
	

	@Override
	public void loginUser(boolean login) {
		if (login) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("UserID", Utility.getUserID(this));
			map.put("Password", newPasswordEditText.getText().toString());
			
			ChangePasswordWS ws = new ChangePasswordWS(this, map);
			ws.mCallBack = this;
			ws.execute();
			

			dialog.dismiss();
		}
		
	}


	@Override
	public void ChangePasswordListener(Boolean result) {
		if (result) {
			DBA_MASTER_AGENT dbAgent = new DBA_MASTER_AGENT(this);
			try {
				dbAgent.open();
				dbAgent.updatePassword(Utility.md5(newPasswordEditText.getText().toString()));
				Log.i(TAG, "::ChangePasswordListener:" + Utility.md5(newPasswordEditText.getText().toString()));
				
				dialog.dismiss();
			
			} catch (SQLException e) {
				e.printStackTrace();
			}
			finally {
				if (dbAgent != null) {
					dbAgent.close();					
				}
			}	
		}
		
	}	

	public void btnSignOutClick (View v) {
//	    Utility.ConfirmDialog(MyProfileActivity.this, "Konfirmasi", "Anda yakin ingin keluar dari aplikasi ini?", "Ya", "Tidak");

        DBA_MASTER_AGENT dba = new DBA_MASTER_AGENT(getBaseContext());
        try {
            dba.open();
            if (dba.getRow().getCount() != 0) {
//                dba.updateStatusLogout();
                dba.deleteAll();
                Toast.makeText(this, "Anda sudah logout", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (dba != null)
                dba.close();

            finish();
        }
    }
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_profile, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	
	
	private void Back(){
		try{
			this.finish();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Back();
	}
	

	public void btnHomeClick(View v) {
		Home();
	}
	
	private void Home(){
		this.finish();
	}

	
	


}
