package com.aca.amm;

import java.net.SocketTimeoutException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.aca.database.DBA_PRODUCT_ASRI_SYARIAH;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class LOAD_ASRI_SYARIAH extends AsyncTask<String, Void, Void>{

	private ProgressDialog progressBar;
	private Context ctx;
	
	private boolean error = false;
	private String errorMessage = "";
	
	private SoapObject requestRetrieve = null;
    private SoapSerializationEnvelope envelope = null;
    private HttpTransportSE androidHttpTransport = null;
    
    private static String NAMESPACE = "http://tempuri.org/";     
	private static String URL = Utility.getURL();   
    private static String SOAP_ACTION = "http://tempuri.org/LoadASRI";     
    private static String METHOD_NAME= "LoadASRI";
    
    private long SPPA_ID = 0;
    
    private NumberFormat nf;
    
    private String noPolis;
    
    private ArrayList<HashMap<String, String>> asriList;
    private ArrayList<HashMap<String, String>> polisList ;
    
    public LOAD_ASRI_SYARIAH (Context ctx, String policyNo, Long SPPA_ID, ArrayList<HashMap<String, String>> polis){
    	this.ctx = ctx;
    	noPolis = policyNo;
    	polisList = polis;
    	this.SPPA_ID = SPPA_ID;
    	
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
            table = (SoapObject) responseBody.getProperty(0);
            
            asriList = new ArrayList<HashMap<String, String>>(); 
            
            int iTotalDataFromWebService = table.getPropertyCount();
            for(int i = 0; i < iTotalDataFromWebService; i++)
            {
            	tableRow = (SoapObject) table.getProperty(i);

	            HashMap<String, String> map = new HashMap<String, String>(); 
	            map.put("SPPA_NO", tableRow.getPropertySafelyAsString("SPPA_NO").toString()); 
	            
	            map.put("BUILDING_SIZE", tableRow.getPropertySafelyAsString("BUILDING_SIZE").toString()); 
	            map.put("BUILDING_SI", tableRow.getPropertySafelyAsString("BUILDING_SI").toString());
	            map.put("CONTENT_SI", tableRow.getPropertySafelyAsString("CONTENT_SI").toString());
	            
	         //   map.put("OTHER_1_DESC", tableRow.getPropertySafelyAsString("OTHER_1_DESC").toString());
	         //   map.put("OTHER_1_SI", tableRow.getPropertySafelyAsString("OTHER_1_SI").toString());
	           // map.put("OTHER_2_DESC", tableRow.getPropertySafelyAsString("OTHER_2_DESC").toString());
	          //  map.put("OTHER_2_SI", tableRow.getPropertySafelyAsString("OTHER_2_SI").toString());
	            
	            map.put("RISK_LOCATION_FLAG", tableRow.getPropertySafelyAsString("RISK_LOCATION_FLAG").toString());
	            map.put("RISK_ADDRESS", tableRow.getPropertySafelyAsString("RISK_ADDRESS").toString());
	            map.put("RISK_RT_NO", tableRow.getPropertySafelyAsString("RISK_RT_NO").toString());
	            map.put("RISK_RW_NO", tableRow.getPropertySafelyAsString("RISK_RW_NO").toString());
	            map.put("RISK_KELURAHAN", tableRow.getPropertySafelyAsString("RISK_KELURAHAN").toString());	            
	            map.put("RISK_KECAMATAN", tableRow.getPropertySafelyAsString("RISK_KECAMATAN").toString());
	            map.put("RISK_CITY", tableRow.getPropertySafelyAsString("RISK_CITY").toString()); 
	            map.put("RISK_POST_CODE", tableRow.getPropertySafelyAsString("RISK_POST_CODE").toString());
	            
	            map.put("WALL_FLAG", tableRow.getPropertySafelyAsString("WALL_FLAG").toString());
	            map.put("WALL_NOTE", tableRow.getPropertySafelyAsString("WALL_NOTE").toString());
	            map.put("FLOOR_FLAG", tableRow.getPropertySafelyAsString("FLOOR_FLAG").toString());
	            map.put("FLOOR_NOTE", tableRow.getPropertySafelyAsString("FLOOR_NOTE").toString());
	            map.put("CEILING_FLAG", tableRow.getPropertySafelyAsString("CEILING_FLAG").toString());
	            map.put("CEILING_NOTE", tableRow.getPropertySafelyAsString("CEILING_NOTE").toString());
	            
	            map.put("QUESTION_4A_FLAG", tableRow.getPropertySafelyAsString("QUESTION_4A_FLAG").toString());
	     //       map.put("QUESTION_4A_NOTE", tableRow.getPropertySafelyAsString("QUESTION_4A_NOTE").toString());
	            map.put("QUESTION_4B_FLAG", tableRow.getPropertySafelyAsString("QUESTION_4B_FLAG").toString());
	     //       map.put("QUESTION_4B_NOTE", tableRow.getPropertySafelyAsString("QUESTION_4B_NOTE").toString());
	            
	            map.put("LONGITUDE", tableRow.getPropertySafelyAsString("LONGITUDE").toString());
	            map.put("LATITUDE", tableRow.getPropertySafelyAsString("LATITUDE").toString()); 
	            
	            map.put("RATE", tableRow.getPropertySafelyAsString("RATE").toString()); 
	            
	            /* 		LOAD FAILED
	             * 
	            map.put("PAYMENT_METHOD_FLAG", tableRow.getPropertySafelyAsString("PAYMENT_METHOD_FLAG").toString());
	            map.put("PAYMENT_PROOF_NO", tableRow.getPropertySafelyAsString("PAYMENT_PROOF_NO").toString()); 
	            map.put("PAYMENT_AMOUNT", tableRow.getPropertySafelyAsString("PAYMENT_AMOUNT").toString()); 
	            
	            map.put("CC_NAME", tableRow.getPropertySafelyAsString("CC_NAME").toString());
	            map.put("CC_TYPE_FLAG", tableRow.getPropertySafelyAsString("CC_TYPE_FLAG").toString()); 
	            map.put("CC_EXPR_MONTH", tableRow.getPropertySafelyAsString("CC_EXPR_MONTH").toString()); 	            
	            map.put("CC_EXPR_YEAR", tableRow.getPropertySafelyAsString("CC_EXPR_YEAR").toString());
	            map.put("CC_NO", tableRow.getPropertySafelyAsString("CC_NO").toString());
	            map.put("CC_SECURITY_CODE", tableRow.getPropertySafelyAsString("CC_SECURITY_CODE").toString());
	            map.put("CC_SOLD_DATE", tableRow.getPropertySafelyAsString("CC_SOLD_DATE").toString());
	            map.put("CC_SETTLE_DATE", tableRow.getPropertySafelyAsString("CC_SETTLE_DATE").toString());
	            map.put("CRE_DATE", tableRow.getPropertySafelyAsString("CRE_DATE").toString());
	            map.put("CRE_BY", tableRow.getPropertySafelyAsString("CRE_BY").toString());
	            map.put("CRE_IP_ADDRESS", tableRow.getPropertySafelyAsString("CRE_IP_ADDRESS").toString());
	            map.put("MOD_DATE", tableRow.getPropertySafelyAsString("MOD_DATE").toString());
	            map.put("MOD_BY", tableRow.getPropertySafelyAsString("MOD_BY").toString());
	            map.put("MOD_IP_ADDRESS", tableRow.getPropertySafelyAsString("MOD_IP_ADDRESS").toString());
	            */
	            asriList.add(map);
            }				
		}catch(ArrayIndexOutOfBoundsException e){
			e.printStackTrace();
			error = true;
			errorMessage = e.getMessage();
		}
		catch(SocketTimeoutException e){
			e.printStackTrace();
			error = true;
			errorMessage = e.getMessage();
		}
		catch(Exception e){
			e.printStackTrace();
			error = true;
			errorMessage = e.getMessage();
		}finally{
			
		}
		
		return null;
	}
	
protected void onPostExecute(Void result) {
		
		super.onPostExecute(result);
		
		DBA_PRODUCT_ASRI_SYARIAH dba = new DBA_PRODUCT_ASRI_SYARIAH(ctx);
		
		progressBar.hide();
		progressBar.dismiss();
		
		try
		{
			if (error)
				Toast.makeText(ctx, "Cannot synchronize to server due to : " + errorMessage , Toast.LENGTH_LONG).show();	
			else
			{
				dba.open();
				dba.initialInsert(
						SPPA_ID,
						asriList.get(0).get("RISK_LOCATION_FLAG").toString(),
					
						Integer.parseInt(asriList.get(0).get("BUILDING_SIZE").toString()),
						
						nf.parse(asriList.get(0).get("BUILDING_SI").toString()).doubleValue(),
						nf.parse(asriList.get(0).get("CONTENT_SI").toString()).doubleValue(),
						
						nf.parse(polisList.get(0).get("TOTAL_SI").toString()).doubleValue(),
						
						asriList.get(0).get("RISK_ADDRESS").toString(),
						asriList.get(0).get("RISK_RT_NO").toString(),
						asriList.get(0).get("RISK_RW_NO").toString(),
						asriList.get(0).get("RISK_KELURAHAN").toString(),
						asriList.get(0).get("RISK_KECAMATAN").toString(),
						
						asriList.get(0).get("RISK_CITY").toString(),
						asriList.get(0).get("RISK_POST_CODE").toString(),
						asriList.get(0).get("WALL_FLAG").toString(),
						asriList.get(0).get("FLOOR_FLAG").toString(),
						asriList.get(0).get("CEILING_FLAG").toString(),
						asriList.get(0).get("QUESTION_4A_FLAG").toString(),
						asriList.get(0).get("QUESTION_4B_FLAG").toString(),
						
						Utility.getFormatDate(polisList.get(0).get("EFF_DATE").toString()),
						Utility.getFormatDate(polisList.get(0).get("EXP_DATE").toString()),
						
						nf.parse(asriList.get(0).get("RATE").toString()).doubleValue(),
						
						nf.parse(polisList.get(0).get("PREMIUM").toString()).doubleValue(),
						nf.parse(polisList.get(0).get("STAMP").toString()).doubleValue(),
						nf.parse(polisList.get(0).get("CHARGE").toString()).doubleValue(),
						nf.parse(polisList.get(0).get("TOTAL_PREMIUM").toString()).doubleValue(),
					
						"ASRI", 
						polisList.get(0).get("CUSTOMER_NAME").toString(), "");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (dba != null)
				dba.close();
		}
	}
	
}
