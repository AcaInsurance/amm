package com.aca.amm;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.conn.ConnectTimeoutException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.aca.amm.ChangePasswordWS.ChangePassword;
import com.aca.database.DBA_MASTER_AGENT;
import com.aca.database.DBA_PRODUCT_DNO;
import com.aca.database.DBA_PRODUCT_MAIN;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


public class ResetPasswordWS extends AsyncTask<String, Void, Void> {

	private ProgressDialog progressBar;
	private Context ctx;
	
	private boolean error = false;
	private String errorMessage = "";
	
	private SoapObject requestinsert = null;
    private SoapSerializationEnvelope envelope = null;
    private HttpTransportSE androidHttpTransport = null;
    
    private static String NAMESPACE = "http://tempuri.org/";     
	private static String URL = Utility.getURL();    
    private static String SOAP_ACTION_INSERT = "http://tempuri.org/ResetPwd";     
    private static String METHOD_NAME_INSERT = "ResetPwd";
    
    private HashMap<String, String> map = null; 
    public ResetPassword mCallBack = null;
    private boolean response = false;
	private String responseString;

	private String pesan = "";
	
	public interface ResetPassword {
		public void ResetPasswordListener (String result);
	}
    
    
	public ResetPasswordWS(Context ctx, HashMap<String, String> map)
	{
		this.ctx = ctx;
		this.map = map;
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
		
		requestinsert = new SoapObject(NAMESPACE, METHOD_NAME_INSERT);
	}
	
	@SuppressWarnings("finally")
	@Override
	protected Void doInBackground(String... params) {
		error = false;		
		responseString = "";
		 
		try{
			
			requestinsert.addProperty(Utility.GetPropertyInfo("UserID", map.get("UserID"), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("mode", map.get("mode"), String.class));
    		 
    		
    		envelope.setOutputSoapObject(requestinsert);
    		androidHttpTransport.call(SOAP_ACTION_INSERT, envelope);
			SoapObject result = (SoapObject)envelope.bodyIn;
    		responseString = result.getProperty(0).toString();
    		
		}
		catch (Exception e) {
    		error = true;
    		e.printStackTrace();	        		
			errorMessage = new MasterExceptionClass(e).getException();
		}finally{
		}
		return null;
		
	}
	
	protected void onPostExecute(Void result) {
		
		super.onPostExecute(result);
		
		progressBar.hide();
		progressBar.dismiss();
		
		try
		{
            if (error) {
            	 Dialog dialog = null;
                 dialog = Utility.showCustomDialogInformation(ctx, "Informasi", 
                  		errorMessage);                
                 dialog.setOnDismissListener(dialogListener());
            }
            else
			{
            	Dialog dialog = null;
            	
            	if(!responseString.trim().toLowerCase().contains("anytype")) {
            		dialog = Utility.showCustomDialogInformation(ctx, "Informasi", responseString); 
                	dialog.setOnDismissListener(dialogListener());
            		
            	}
            	else {
            		mCallBack.ResetPasswordListener(responseString);
            	}
			} 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		} 
	}

	private OnDismissListener dialogListener() {
		return new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				mCallBack.ResetPasswordListener(responseString);
				
			}
		};
	}
}
