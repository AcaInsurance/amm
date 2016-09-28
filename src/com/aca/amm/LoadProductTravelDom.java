package com.aca.amm;

import java.net.SocketTimeoutException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.aca.database.DBA_PRODUCT_TRAVEL_DOM;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class LoadProductTravelDom extends AsyncTask<String, Void, Void>{
	private ProgressDialog progressBar;
	private Context ctx;
	
	private boolean error = false;
	private String errorMessage = "";
	
	private SoapObject requestRetrieve = null;
    private SoapSerializationEnvelope envelope = null;
    private HttpTransportSE androidHttpTransport = null;
    
    private static String NAMESPACE = "http://tempuri.org/";     
	private static String URL = Utility.getURL(); 
    private static String SOAP_ACTION = "http://tempuri.org/LoadTravelSafe";     //
    private static String METHOD_NAME= "LoadTravelSafe";  //
    
    private long SPPA_ID = 0;
    
    private NumberFormat nf;
    
    private String noPolis;

    private ArrayList<HashMap<String, String>> travelList;
    private ArrayList<HashMap<String, String>> polisList ;
    
    public LoadProductTravelDom (Context ctx, String policyNo, Long SPPA_ID, 
    		ArrayList<HashMap<String, String>> polis){
    	this.ctx = ctx;
    	this.SPPA_ID = SPPA_ID;
    	
    	noPolis = policyNo;
    	polisList = polis;
    	
    	nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
    }
    
    
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
		requestRetrieve = new SoapObject(NAMESPACE, METHOD_NAME);
	}
	
	@Override
	protected Void doInBackground(String... params) {
		error = false;
		try{
			
			SoapObject table = null;                        // Contains table of dataset that returned through SoapObject
	        SoapObject tableRow = null;                     // Contains row of table
	        SoapObject responseBody = null;					// Contains XML content of dataset
	        
	        requestRetrieve.addProperty(Utility.GetPropertyInfo("PolicyNo", noPolis, String.class));
	        
	    	envelope.setOutputSoapObject(requestRetrieve);
    		envelope.bodyOut = requestRetrieve;
    		androidHttpTransport.call(SOAP_ACTION, envelope);  

    		
            responseBody = (SoapObject) envelope.getResponse();
            responseBody = (SoapObject) responseBody.getProperty(1);
            
            if (responseBody.getPropertyCount() == 0) {
            	error = true;
            	errorMessage = "Data tidak dapat ditemukan";
            	return null;
            			
            }
            
            table = (SoapObject) responseBody.getProperty(0);
            
            travelList = new ArrayList<HashMap<String, String>>(); 
            
            int iTotalDataFromWebService = table.getPropertyCount();
            for(int i = 0; i < iTotalDataFromWebService; i++)
            {
            	tableRow = (SoapObject) table.getProperty(i);

	            HashMap<String, String> map = new HashMap<String, String>(); 
	            map.put("SPPA_NO", tableRow.getPropertySafelyAsString("SPPA_NO").toString()); 
	            
	            map.put("TRAVEL_TYPE", tableRow.getPropertySafelyAsString("TRAVEL_TYPE").toString()); 
	            map.put("TRAVEL_NOTE", tableRow.getPropertySafelyAsString("TRAVEL_NOTE").toString());
	            map.put("TRAVEL_CITY_NAME", tableRow.getPropertySafelyAsString("TRAVEL_CITY_NAME").toString()); 
	   
	            map.put("BENEFICIARY_NAME", tableRow.getPropertySafelyAsString("BENEFICIARY_NAME").toString()); 
	            map.put("BENEFICIARY_RELATION", tableRow.getPropertySafelyAsString("BENEFICIARY_RELATION").toString());
	          
	            map.put("DEPARTURE_DATE", tableRow.getPropertySafelyAsString("DEPARTURE_DATE").toString()); 
	            map.put("ARRIVAL_DATE", tableRow.getPropertySafelyAsString("ARRIVAL_DATE").toString());
	          
	            map.put("PRODUCT_PLAN_1_FLAG", tableRow.getPropertySafelyAsString("PRODUCT_PLAN_1_FLAG").toString()); 
	          
	            map.put("TOTAL_DAYS", tableRow.getPropertySafelyAsString("TOTAL_DAYS").toString());
	            map.put("PREMI_DAYS", tableRow.getPropertySafelyAsString("PREMI_DAYS").toString()); 
	            map.put("TOTAL_WEEKS", tableRow.getPropertySafelyAsString("TOTAL_WEEKS").toString());
	            map.put("PREMI_WEEKS", tableRow.getPropertySafelyAsString("PREMI_WEEKS").toString()); 
	            map.put("PREMI_ALOKASI", tableRow.getPropertySafelyAsString("PREMI_ALOKASI").toString());
	           
	            map.put("CCOD", tableRow.getPropertySafelyAsString("CCOD").toString()); 
	            map.put("DCOD", tableRow.getPropertySafelyAsString("DCOD").toString());
	          
	            map.put("MAX_BENEFIT", tableRow.getPropertySafelyAsString("MAX_BENEFIT").toString()); 

	            travelList.add(map);
            }				
		}	
		catch (Exception e) {
    		error = true;
    		e.printStackTrace();	        		
			errorMessage = new MasterExceptionClass(e).getException();
		}
		return null;
	}
	
	protected void onPostExecute(Void result) {
		
		super.onPostExecute(result);
		
		DBA_PRODUCT_TRAVEL_DOM dba = null;
		
		try
		{
			progressBar.hide();
			progressBar.dismiss();
			
			dba = new DBA_PRODUCT_TRAVEL_DOM(ctx);
			
			if (error)
				Utility.showCustomDialogInformation(ctx, "Informasi", 
						errorMessage);	
			else
			{
				dba.open();
				
				 dba.initialInsert(
					SPPA_ID,
					
					travelList.get(0).get("TRAVEL_TYPE").toString(),
					travelList.get(0).get("TRAVEL_CITY_NAME").toString(),
					Integer.parseInt(travelList.get(0).get("PRODUCT_PLAN_1_FLAG").toString()),
					
					travelList.get(0).get("BENEFICIARY_NAME").toString(),
					travelList.get(0).get("BENEFICIARY_RELATION").toString(),
					
					Utility.getFormatDate(travelList.get(0).get("DEPARTURE_DATE").toString()),
					Utility.getFormatDate(travelList.get(0).get("ARRIVAL_DATE").toString()),
					
					nf.parse(travelList.get(0).get("TOTAL_DAYS").toString()).doubleValue(),
					nf.parse("0.00").doubleValue(), //tambahan per migngu
					
					nf.parse(polisList.get(0).get("PREMIUM").toString()).doubleValue(),
					nf.parse(polisList.get(0).get("CHARGE").toString()).doubleValue(),
					nf.parse(polisList.get(0).get("TOTAL_PREMIUM").toString()).doubleValue(),
					
					"TRAVELDOM",  
					polisList.get(0).get("CUSTOMER_NAME").toString(),
					
					nf.parse(travelList.get(0).get("CCOD").toString()).doubleValue(),
					nf.parse(travelList.get(0).get("DCOD").toString()).doubleValue(),
					
					nf.parse(travelList.get(0).get("PREMI_DAYS").toString()).doubleValue(),
					nf.parse(travelList.get(0).get("PREMI_WEEKS").toString()).doubleValue(),
					
					nf.parse(travelList.get(0).get("MAX_BENEFIT").toString()).doubleValue(),

					Integer.parseInt(travelList.get(0).get("TOTAL_DAYS").toString()),
					Integer.parseInt(travelList.get(0).get("TOTAL_WEEKS").toString())
					);
			}
			
		}
		catch(Exception e)
		{
			Utility.showCustomDialogInformation(ctx, "Informasi", 
					"Gagal menyimpan renewal");	
		}
		finally
		{
			if (dba != null)
				dba.close();
		}
	}
}
