package com.aca.amm;

import java.util.HashMap;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;


public class CheckVehicleWS extends AsyncTask<String, Void, Void> {
	@SuppressWarnings("unused")
	private static final String TAG = "com.aca.amm.CheckVehicleWS";
	private ProgressDialog progressBar;
	private Context ctx;
	
	
	private boolean error = false;
	private String errorMessage = "";
	
	private SoapObject requestinsert = null;
    private SoapSerializationEnvelope envelope = null;
    private HttpTransportSE androidHttpTransport = null;
    
    private static String NAMESPACE = "http://tempuri.org/";     
	private static String URL = Utility.getURL();    
    private static String SOAP_ACTION_INSERT = "http://tempuri.org/CheckVehicle";     
    private static String METHOD_NAME_INSERT = "CheckVehicle";
    
    private HashMap<String, String> map = null;
    private long SPPAID = 0;
    
    public CheckVehicle mCallBack = null;

	private Boolean response = false;
	private String responseMessage = "";
	
	public interface CheckVehicle {
		public void CheckVehicleListener (Boolean result);
	}
	
    
	public CheckVehicleWS(Context ctx, HashMap<String, String> map)
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
	
	@Override
	protected Void doInBackground(String... params) {
		error = false;		
				
		try{
		
			if (cekPlat(map.get("PlatNo2"), map.get("PlatNo3"))) {
    	
				requestinsert.addProperty(Utility.GetPropertyInfo("prodId", map.get("prodId"), String.class));
	    		requestinsert.addProperty(Utility.GetPropertyInfo("effDate", map.get("effDate"), String.class));
				requestinsert.addProperty(Utility.GetPropertyInfo("ChassisNo", map.get("ChassisNo"), String.class));
	    		requestinsert.addProperty(Utility.GetPropertyInfo("EngineNo", map.get("EngineNo"), String.class));
				requestinsert.addProperty(Utility.GetPropertyInfo("PlatNo1", map.get("PlatNo1"), String.class));
	    		requestinsert.addProperty(Utility.GetPropertyInfo("PlatNo2", map.get("PlatNo2"), String.class));
				requestinsert.addProperty(Utility.GetPropertyInfo("PlatNo3", map.get("PlatNo3"), String.class));
				requestinsert.addProperty(Utility.GetPropertyInfo("BranchNo", map.get("BranchNo"), String.class));
	    		
				 
						
	    		envelope.setOutputSoapObject(requestinsert);
	    		androidHttpTransport.call(SOAP_ACTION_INSERT, envelope);  
	    		SoapObject result = (SoapObject)envelope.bodyIn;
	        	
	    		responseMessage = result.getPropertySafelyAsString("CheckVehicleResult");
	    	
	    		if (responseMessage.toLowerCase().contains("anytype"))
	    			responseMessage = "";
	    		
	    		Log.i(TAG, "::doInBackground:" + responseMessage);
	    		response = true;
			}
			else {
				responseMessage = "";  
	    		response = true;
			}
		}
		catch (Exception e) {
    		error = true;
    		e.printStackTrace();	        		
			errorMessage = new MasterExceptionClass(e).getException();
		}finally{
		}
		return null;
		
	}
	
	private boolean cekPlat(String plat2, String plat3) { 
		 
		 if ((plat2.equalsIgnoreCase("999")  || 
			 plat2.equalsIgnoreCase("9999")  ||
			 plat2.equalsIgnoreCase("99999")) && 
			 (plat3.equalsIgnoreCase("XX") ||
			 plat3.equalsIgnoreCase("XXX") )) 
		 {
			 return false;
		 }
		 else {
			return true;
					
		}
		 
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
                
                response = false;

                dialog.setOnDismissListener(dialogListener());
            }
            else
			{
            	if (TextUtils.isEmpty(responseMessage.trim())) {
            		response = true;
            		mCallBack.CheckVehicleListener(response);
            	}     	
            	
            	else {
            		dialog = Utility.showCustomDialogInformation(ctx, "Informasi", responseMessage);
            		response = false;

                    dialog.setOnDismissListener(dialogListener());
            	}		
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

	private OnDismissListener dialogListener() {
		return new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				mCallBack.CheckVehicleListener(response);
			}
		};
	}
}
