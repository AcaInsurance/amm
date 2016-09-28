package com.aca.amm;

import java.io.File;
import java.net.SocketTimeoutException;
import java.util.HashMap;

import org.apache.http.conn.ConnectTimeoutException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

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
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


public class ChangeUsernameWS extends AsyncTask<String, Void, Void> {
	@SuppressWarnings("unused")
	private static final String TAG = "com.aca.amm.ChangeUsernameWS";
	private ProgressDialog progressBar;
	private Context ctx;
	
	private boolean error = false;
	private String errorMessage = "";
	
	private SoapObject requestinsert = null;
    private SoapSerializationEnvelope envelope = null;
    private HttpTransportSE androidHttpTransport = null;
    
    private static String NAMESPACE = "http://tempuri.org/";     
	private static String URL = Utility.getURL();    
    private static String SOAP_ACTION_INSERT = "http://tempuri.org/ChangeUserName";     
    private static String METHOD_NAME_INSERT = "ChangeUserName";
    
    private HashMap<String, String> map = null;
    public ChangeUsername mCallBack = null;

	private Boolean response = false;
	private String responseMessage = "";

	public interface ChangeUsername {
		public void ChangeUsernameListener (Boolean result);
	}
	
    
	public ChangeUsernameWS(Context ctx, HashMap<String, String> map)
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
		response = false;

		try{
			
			requestinsert.addProperty(Utility.GetPropertyInfo("UserID", map.get("UserID"), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("UserName", map.get("UserName"), String.class));
    	
    		Log.i(TAG, "::doInBackground:" + map.get("UserName"));
    		Log.i(TAG, "::doInBackground:" + map.get("UserID"));
    		
    		envelope.setOutputSoapObject(requestinsert);
    		androidHttpTransport.call(SOAP_ACTION_INSERT, envelope);  
    		SoapObject result = (SoapObject)envelope.bodyIn;
        	
    		responseMessage = result.getPropertySafelyAsString("ChangeUserNameResult").toLowerCase().replace(Utility.getAnytypeStr(), "");
    		
    		Log.i(TAG, "::doInBackground:" + responseMessage);
    		response = true;
    		
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
		
		Dialog dialog = null;
		
		try
		{
            if (error) {
                dialog = Utility.showCustomDialogInformation(ctx, "Informasi", 
                  		errorMessage);                
            }
            else
			{
            	if (TextUtils.isEmpty(responseMessage.trim())) {
            		dialog = Utility.showCustomDialogInformation(ctx, "Informasi", 
            				"Sukses ganti username");
            		response = true;
            	}
            	else {
            		dialog = Utility.showCustomDialogInformation(ctx, "Informasi", 
            				"Gagal ganti username");
            		response = false;
            	}
            	
			}
            dialog.setOnDismissListener(dialogListener());
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			
		}
	}

	private OnDismissListener dialogListener() {
		return new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				mCallBack.ChangeUsernameListener(response);
			}
		};
	}
}
