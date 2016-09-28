package com.aca.amm;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


public  class GetExchangeRate extends AsyncTask<String, Void, Void>{
		private ProgressDialog progressBar;
		private Context ctx;
		
		private boolean error = false;
		private String errorMessage = "";
		
		private SoapObject requestRetrieve = null;
	    private SoapSerializationEnvelope envelope = null;
	    private HttpTransportSE androidHttpTransport = null;
	    
	    private static String NAMESPACE = "http://tempuri.org/";     
		private static String URL = Utility.getURL();    
	    private static String SOAP_ACTION = "http://tempuri.org/GetExchangeRate";     
	    private static String METHOD_NAME= "GetExchangeRate";
	    
	    private String currency;
	    private String exchangeRate = "";
		private boolean response = false;
		public GetexchangerateInterface mcallback = null;
		
		@SuppressWarnings("unused")
		private static final String TAG = "com.aca.amm.GetExchangeRate";
		
	    public GetExchangeRate (Context ctx, String currency) {
	    	this.currency = currency;
	    	this.ctx = ctx;
	    }
	    
	    public interface GetexchangerateInterface {
	    	public void GetexchangerateListener (String exRate) ;
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
	    	envelope.dotNet = true;
	    	androidHttpTransport = new HttpTransportSE(URL);

			requestRetrieve = new SoapObject(NAMESPACE, METHOD_NAME);
		}
		
		@Override
		protected Void doInBackground(String... params) {
	
			error = false;
			response = false;
			
			try{ 

				
				
				Log.i(TAG, "::doInBackground:" + "currency : " + currency);
		        requestRetrieve.addProperty(Utility.GetPropertyInfo("curr", currency, String.class));
		    	 
	    		envelope.setOutputSoapObject(requestRetrieve);
	    		androidHttpTransport.call(SOAP_ACTION, envelope);  
	    		SoapObject result = (SoapObject)envelope.bodyIn;
	        	
	    		exchangeRate = result.getPropertySafelyAsString("GetExchangeRateResult");
	      
	    		Log.i(TAG, "::doInBackground: exchange rate " + exchangeRate);
	    		response = true;
			}
			catch (Exception e) {
				exchangeRate = "0";
				
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
				if(error) {
					Toast.makeText(ctx, "Gagal mendapatkan exchange rate", Toast.LENGTH_SHORT).show();
				}
				mcallback.GetexchangerateListener(exchangeRate);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
}
