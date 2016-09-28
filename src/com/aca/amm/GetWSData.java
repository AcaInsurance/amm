package com.aca.amm;

import java.util.ArrayList;
import java.util.HashMap; 
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.aca.database.DBA_MASTER_KONVE_RATE;
 
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class GetWSData {
	private Context ctx;
	
	public GetWSData (Context ctx) {
		this.ctx = ctx;
	}
	
	public void run () {
		RetrieveKonveRate  ws = new RetrieveKonveRate(ctx);
		ws.execute();
	}

	static class RetrieveKonveRate extends AsyncTask<String, Void, Void>{
		private Context ctx ;
		
		private boolean error = false;
		private String errorMessage = "";
		
		private SoapObject requestretrive = null;
	    private SoapSerializationEnvelope envelope = null;
	    private HttpTransportSE androidHttpTransport = null;
	    
	    private static String NAMESPACE = "http://tempuri.org/";     
		private static String URL = Utility.getURL(); 
	
	    private static String SOAP_ACTION = "http://tempuri.org/GetRateMotorCoven";     
	    private static String METHOD_NAME= "GetRateMotorCoven";
	    
	    private String idKtp;
	    
	    private ArrayList<HashMap<String, String>> custList;
	
		private ProgressDialog progressBar;
	    
		public InterfaceCustomer customerInterface = null;
		
		public RetrieveKonveRate (Context ctx){
			this.ctx = ctx; 
		}
		
		@Override
		protected void onPreExecute() {
	
			super.onPreExecute();
			
	//		Log.d("context", );
	//
			progressBar = new ProgressDialog(ctx);
			progressBar.setCancelable(false);
			progressBar.setMessage("Please wait ...");
			progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//			progressBar.show();
			
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
				
	
				requestretrive = new SoapObject(NAMESPACE, METHOD_NAME); 
		        
		    	envelope.setOutputSoapObject(requestretrive);
		    	envelope.bodyOut = requestretrive;
	    		androidHttpTransport.call(SOAP_ACTION, envelope);  
	    			
	            responseBody = (SoapObject) envelope.getResponse();
	            responseBody = (SoapObject) responseBody.getProperty(1);
	            
	            if(responseBody.getPropertyCount() > 0) {
	            	table = (SoapObject) responseBody.getProperty(0);
	            	custList = new ArrayList<HashMap<String, String>>(); 
	                 
	                HashMap<String, String> map;
	                
	                for (int i = 0 ;i < table.getPropertyCount(); i ++) {

		            	tableRow = (SoapObject) table.getProperty(i);
		                map = new HashMap<String, String>();  
		                
		                map.put("JNP", tableRow.getPropertySafelyAsString("JNP").toString()); 
		                map.put("WILAYAH", tableRow.getPropertySafelyAsString("Wilayah").toString());  
		                map.put("VEHICLE_CATEGORY", tableRow.getPropertySafelyAsString("Vehicle_Category").toString());  
		                map.put("EXCO", tableRow.getPropertySafelyAsString("Exco").toString());  
		                map.put("RATE_BAWAH", tableRow.getPropertySafelyAsString("Rate_Bawah").toString());  
		                map.put("RATE_TENGAH", tableRow.getPropertySafelyAsString("Rate_Tengah").toString());  
		                map.put("RATE_ATAS", tableRow.getPropertySafelyAsString("Rate_Atas").toString());  
		                map.put("JAMINAN", tableRow.getPropertySafelyAsString("Jaminan").toString());  
		                  
		                
		                custList.add(map);
	                }
	        	}
	            else {
	            	error = true;
	            	errorMessage = "Rate tidak ditemukan";
	            			
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
			progressBar.hide();
			progressBar.dismiss();
			try{	
				if(error){
					
					Toast.makeText(ctx, errorMessage, Toast.LENGTH_SHORT).show();
				}
				
				DBA_MASTER_KONVE_RATE dba = new DBA_MASTER_KONVE_RATE(ctx);

				try {
					HashMap<String, String> map;
					dba.open();
					
					for (int i = 0 ; i < custList.size(); i ++) {
						map = custList.get(i);
						
						dba.insert(Utility.ifAnytype(map.get("JNP")), 
								Utility.ifAnytype(map.get("WILAYAH")),
								Utility.ifAnytype(map.get("VEHICLE_CATEGORY")),
								Utility.ifAnytype(map.get("EXCO")),
								Utility.ifAnytype(map.get("RATE_BAWAH")),
								Utility.ifAnytype(map.get("RATE_TENGAH")),
								Utility.ifAnytype(map.get("RATE_ATAS")),
								Utility.ifAnytype(map.get("JAMINAN")));
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (dba != null)
						dba.close();
				}
				customerInterface.getCustomerList(custList);
			
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		public ArrayList<HashMap<String, String>> getCustomerData (){
			return custList;
		}
		
	}
		
}
