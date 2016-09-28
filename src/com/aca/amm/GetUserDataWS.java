package com.aca.amm;

import java.util.ArrayList;
import java.util.HashMap;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.Toast;

public   class GetUserDataWS extends AsyncTask<String, Void, Void>{
	private static String NAMESPACE = "http://tempuri.org/";     
    private static String URL = Utility.getURL(); 
    
    private static String SOAP_ACTION = "http://tempuri.org/GetAgentInfo";     
    private static String METHOD_NAME = "GetAgentInfo";
    
    
    private ProgressDialog progressBar;    
    private PropertyInfo id, pass;
    private HttpTransportSE androidHttpTransport = null;
    
    private SoapObject requestretrive = null;
    private SoapSerializationEnvelope envelope = null;
	private Context context;

    private boolean error;
    private String errorMessage = ""; 
    public GetuserdataListener mCallBack = null;
    private HashMap<String, String> hmap = null;

	
	@SuppressWarnings("unused")
	private static final String TAG = "com.aca.amm.GetUserDataWS";
	

	
	public GetUserDataWS (Context context, HashMap<String, String> map ) {
		this.context  = context;
		this.hmap = map;
		
	}
	public interface GetuserdataListener{
		public void getuserdataListener (HashMap<String, String> map) ;
	}

	
		@Override
		protected void onPreExecute() {

			super.onPreExecute();

			progressBar = new ProgressDialog(context);
			progressBar.setCancelable(false);
			progressBar.setMessage("Please wait ...");
			progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressBar.show();
			
			requestretrive = new SoapObject(NAMESPACE, METHOD_NAME);

			id = new PropertyInfo();
			pass = new PropertyInfo();
			
			envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); 
			envelope.implicitTypes = true;
	    	envelope.dotNet = true;	//used only if we use the webservice from a dot net file (asmx)
	    				
			androidHttpTransport = new HttpTransportSE(URL);
		}
		
		@Override
		protected Void doInBackground(String... params) {

    		error = false; 
			SoapObject table = null;                        // Contains table of dataset that returned through SoapObject
	        SoapObject tableRow = null;                     // Contains row of table
	        SoapObject responseBody = null;					// Contains XML content of dataset
			
			try{
				
				id.setName("AgentCode");
	    		id.setValue(hmap.get("UserID"));
	    		id.setType(String.class);
	    		requestretrive.addProperty(id);
	    		
	    	  
	    		envelope.setOutputSoapObject(requestretrive);
	    		envelope.bodyOut = requestretrive;
	    		androidHttpTransport.call(SOAP_ACTION, envelope);  
	    		
	    	    responseBody = (SoapObject) envelope.getResponse();		            
		        responseBody = (SoapObject) responseBody.getProperty(1);
		        
		        if (responseBody.getPropertyCount() == 0) {
		        	error = true; 
		        	errorMessage = "Tidak dapat mengambil data agent. Pastikan user id anda benar dan koneksi tidak terputus";
		        	return null;
		        }

	    	    table = (SoapObject) responseBody.getProperty(0);
		        tableRow = (SoapObject) table.getProperty(0);
		        
//		        hmap = new HashMap<String, String>();
		        hmap.put("PHONE_NO", tableRow.getPropertySafelyAsString("PHONE_NO").toString());
		        hmap.put("EMAIL_ADDRESS", tableRow.getPropertySafelyAsString("EMAIL_ADDRESS").toString()); 
		        
		        Log.i(TAG, "::doInBackground:" + "phone no :" + tableRow.getPropertySafelyAsString("PHONE_NO").toString() + "aaa");
		        Log.i(TAG, "::doInBackground:" + "email :" + tableRow.getPropertySafelyAsString("EMAIL_ADDRESS").toString() + "aaa");
		        
		     
	        	
			}	
			catch (Exception e) {
				hmap = null;
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
				if (error) {
					Utility.showCustomDialogInformation(context, "Informasi" ,errorMessage);
					hmap = null;
				}
				else {
					mCallBack.getuserdataListener(hmap);
				}
				
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		 
}

