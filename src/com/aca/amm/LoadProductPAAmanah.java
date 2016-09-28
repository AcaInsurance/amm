package com.aca.amm;

import java.net.SocketTimeoutException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.aca.database.DBA_PRODUCT_PA_AMANAH;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class LoadProductPAAmanah extends AsyncTask<String, Void, Void>{
	private ProgressDialog progressBar;
	private Context ctx;
	
	private boolean error = false;
	private String errorMessage = "";
	
	private SoapObject requestRetrieve = null;
    private SoapSerializationEnvelope envelope = null;
    private HttpTransportSE androidHttpTransport = null;
    
    private static String NAMESPACE = "http://tempuri.org/";     
	private static String URL = Utility.getURL(); 
    private static String SOAP_ACTION = "http://tempuri.org/LoadPA";     //
    private static String METHOD_NAME= "LoadPA";  //
    
    private long SPPA_ID = 0;
    
    private NumberFormat nf;
    
    private String noPolis;

    private ArrayList<HashMap<String, String>> beneList;
    private ArrayList<HashMap<String, String>> beneList2;
    private ArrayList<HashMap<String, String>> beneList3;
    
    private ArrayList<HashMap<String, String>> paList;
    private ArrayList<HashMap<String, String>> polisList ;
    
    public LoadProductPAAmanah (Context ctx, String policyNo, Long SPPA_ID, 
    		ArrayList<HashMap<String, String>> polis,
    		ArrayList<HashMap<String, String>> Benelist,
    		ArrayList<HashMap<String, String>> Benelist2,
    		ArrayList<HashMap<String, String>> Benelist3
    		){
    	this.ctx = ctx;
    	this.SPPA_ID = SPPA_ID;
    	
    	noPolis = policyNo;
    	polisList = polis;
    	
    	beneList = Benelist;
    	beneList2 = Benelist2;
    	beneList3 = Benelist3;
    	
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
            
            paList = new ArrayList<HashMap<String, String>>(); 
            
            int iTotalDataFromWebService = table.getPropertyCount();
            for(int i = 0; i < iTotalDataFromWebService; i++)
            {
            	tableRow = (SoapObject) table.getProperty(i);

	            HashMap<String, String> map = new HashMap<String, String>(); 
	            map.put("SPPA_NO", tableRow.getPropertySafelyAsString("SPPA_NO").toString()); 
	            
	            map.put("PRODUCT_PLAN_FLAG", tableRow.getPropertySafelyAsString("PRODUCT_PLAN_FLAG").toString()); 
	          
	            map.put("TSI_1", tableRow.getPropertySafelyAsString("TSI_1").toString()); 
	            map.put("TSI_2", tableRow.getPropertySafelyAsString("TSI_2").toString()); 
	            map.put("TSI_3", tableRow.getPropertySafelyAsString("TSI_3").toString()); 
		  
	            paList.add(map);
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
		
		DBA_PRODUCT_PA_AMANAH dba = null;
		
		try
		{
			
			dba = new DBA_PRODUCT_PA_AMANAH(ctx);
			
			progressBar.hide();
			progressBar.dismiss();
			
			if (error)
				Utility.showCustomDialogInformation(ctx, "Informasi", 
						errorMessage);	
			else
			{
				dba.open();
				
				 dba.initialInsert(
					SPPA_ID,
					
					//TSI 2 DAN TSI 3 ??
					nf.parse(paList.get(0).get("TSI_1").toString()).doubleValue(),
					
					//bene 1
					beneList.get(0).get("NAME").toString().toUpperCase(),
					beneList.get(0).get("RELATION").toString().toUpperCase(),
					beneList.get(0).get("ADDRESS").toString().toUpperCase(),
					
					//bene 2
					beneList2.get(0).get("NAME").toString().toUpperCase(),
					beneList2.get(0).get("RELATION").toString().toUpperCase(),
					beneList2.get(0).get("ADDRESS").toString().toUpperCase(),
					
					//bene 3
					beneList3.get(0).get("NAME").toString().toUpperCase(),
					beneList3.get(0).get("RELATION").toString().toUpperCase(),
					beneList3.get(0).get("ADDRESS").toString().toUpperCase(),
					
					Integer.parseInt(paList.get(0).get("PRODUCT_PLAN_FLAG").toString()),
					
					Utility.getFormatDate(polisList.get(0).get("EFF_DATE").toString()),
					Utility.getFormatDate(polisList.get(0).get("EXP_DATE").toString()),
					
					nf.parse(polisList.get(0).get("PREMIUM").toString()).doubleValue(),
					nf.parse(polisList.get(0).get("CHARGE").toString()).doubleValue(),
					nf.parse(polisList.get(0).get("TOTAL_PREMIUM").toString()).doubleValue(),
					
					"PAAMANAH", 
					polisList.get(0).get("CUSTOMER_NAME").toString()
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
