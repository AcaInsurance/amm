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
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.wifi.WifiConfiguration;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


public class RegisterUsernameWS extends AsyncTask<String, Void, Void> {
	@SuppressWarnings("unused")
	private static final String TAG = "com.aca.amm.RegisterUsernameWS";
	
	private ProgressDialog progressBar;
	private Context ctx;
	
	private boolean error = false;
	private String errorMessage = "";
	
	private SoapObject requestinsert = null;
    private SoapSerializationEnvelope envelope = null;
    private HttpTransportSE androidHttpTransport = null;
    
    private static String NAMESPACE = "http://tempuri.org/";     
	private static String URL = Utility.getURL();    
    private static String SOAP_ACTION_INSERT = "http://tempuri.org/DoSaveCustomerRegist";     
    private static String METHOD_NAME_INSERT = "DoSaveCustomerRegist";
    
    private HashMap<String, String> mapCustomer = null;
    public RegisterUsernameCallBack mCallBackListener;
	private String response;
    
    
    public interface RegisterUsernameCallBack {
    	public void RegisterUsernameListener (boolean result) ;
    	public void RegisterUsernameListener (boolean result, String message) ;
    	
    }
    
	public RegisterUsernameWS(Context ctx, HashMap<String, String> mapCustomer)
	{
		this.ctx = ctx;
		this.mapCustomer = mapCustomer;
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
	
	@Override
	protected Void doInBackground(String... params) {
		error = false;		
		
		try{
			
			requestinsert.addProperty(Utility.GetPropertyInfo("ChannelCode", mapCustomer.get("ChannelCode"), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("Nama", mapCustomer.get("Nama"), String.class)); 
    		requestinsert.addProperty(Utility.GetPropertyInfo("HPNo", mapCustomer.get("HPNo"), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("EmailAddress", mapCustomer.get("EmailAddress"), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("UserName", mapCustomer.get("UserName"), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("Password", mapCustomer.get("Password"), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("BankAccount", mapCustomer.get("BankAccount"), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("BankName", mapCustomer.get("BankName"), String.class));
    		
    		
    		envelope.setOutputSoapObject(requestinsert);
    		androidHttpTransport.call(SOAP_ACTION_INSERT, envelope);  
    		SoapObject result = (SoapObject)envelope.bodyIn;
        	response = result.getPropertySafelyAsString("DoSaveCustomerRegistResult");
        	
        	if (response.toLowerCase().contains("anytype"))
        		response = "";
        			
        	
        	Log.i(TAG, "::doInBackground:" + response);
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
		
//		WifiConfiguration : erromessage -> koneksi ke sever putus
//							reponses -> null
		
//		3G 				:errormessage ->
//						response -> user name already exist
		try
		{
//			Utility.showCustomDialogInformation(ctx, "Informasi", "DEBUG:: errormessage -> " + errorMessage + " response -> "+ response);
			
            if (error) {
                Utility.showCustomDialogInformation(ctx, "Informasi", 
                  		errorMessage);                

                mCallBackListener.RegisterUsernameListener(false);
            }
            else
			{
            	mCallBackListener.RegisterUsernameListener(true, response);
			}
            
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			
		}
	}
}
