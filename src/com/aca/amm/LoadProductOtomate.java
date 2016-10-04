package com.aca.amm;

import java.net.SocketTimeoutException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.conn.ConnectTimeoutException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.aca.database.DBA_PRODUCT_OTOMATE;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class LoadProductOtomate extends AsyncTask<String, Void, Void>{
	private ProgressDialog progressBar;
	private Context ctx;
	
	private boolean error = false;
	private String errorMessage = "";
	
	private SoapObject requestRetrieve = null;
    private SoapSerializationEnvelope envelope = null;
    private HttpTransportSE androidHttpTransport = null;
    
    private static String NAMESPACE = "http://tempuri.org/";     
	private static String URL = Utility.getURL(); 
    private static String SOAP_ACTION = "http://tempuri.org/LoadOtomate";     
    private static String METHOD_NAME= "LoadOtomate";
    
    private long SPPA_ID = 0;
    
    private NumberFormat nf;
    
    private String noPolis;
    
    private ArrayList<HashMap<String, String>> otomateList;
    private ArrayList<HashMap<String, String>> polisList ;
    
    public LoadProductOtomate (Context ctx, String policyNo, Long SPPA_ID, ArrayList<HashMap<String, String>> polis){
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
            
            if (responseBody.getPropertyCount() == 0) {
            	error = true;
            	errorMessage = "Data tidak dapat ditemukan";
            	return null;
            			
            }
            
            table = (SoapObject) responseBody.getProperty(0);
            
            otomateList = new ArrayList<HashMap<String, String>>(); 
            
            int iTotalDataFromWebService = table.getPropertyCount();
            for(int i = 0; i < iTotalDataFromWebService; i++)
            {
            	tableRow = (SoapObject) table.getProperty(i);

	            HashMap<String, String> map = new HashMap<String, String>(); 
//	            map.put("SPPA_NO", tableRow.getPropertySafelyAsString("SPPA_NO").toString()); 
	            
	            map.put("VEHICLE_MERK", tableRow.getPropertySafelyAsString("VEHICLE_MERK").toString());
	            map.put("VEHICLE_CATEGORY", tableRow.getPropertySafelyAsString("VEHICLE_CATEGORY").toString());
	            map.put("VEHICLE_DESC", "");
	            map.put("BUILT_YEAR", tableRow.getPropertySafelyAsString("BUILT_YEAR").toString());
	            
	            map.put("PLAT_NO_1", tableRow.getPropertySafelyAsString("PLAT_NO_1").toString());
	            map.put("PLAT_NO_2", tableRow.getPropertySafelyAsString("PLAT_NO_2").toString());
	            map.put("PLAT_NO_3", tableRow.getPropertySafelyAsString("PLAT_NO_3").toString());
	            
	            map.put("CHASSIS_NO", tableRow.getPropertySafelyAsString("CHASSIS_NO").toString());
	            map.put("ENGINE_NO", tableRow.getPropertySafelyAsString("ENGINE_NO").toString());
	            map.put("COLOUR", tableRow.getPropertySafelyAsString("COLOUR").toString());
	            
	            map.put("NON_STANDARD_ACCESORIES", tableRow.getPropertySafelyAsString("NON_STANDARD_ACCESORIES").toString()); 
	            if(map.get("NON_STANDARD_ACCESORIES").isEmpty())
		            map.put("ACCESORIES_FLAG", "0");
	            else
		            map.put("ACCESORIES_FLAG", "1"); 

	            	
	            map.put("SEATING_CAP", tableRow.getPropertySafelyAsString("SEATING_CAP").toString());
	            
	            map.put("SI", tableRow.getPropertySafelyAsString("TOTAL_SI").toString());
	            map.put("ADD_SI", "0.00");
	            map.put("ACT_OF_GOD", "0");
	            map.put("TPL", "20000000.00");
	            map.put("RATE", "0.00");
	          
	            /*
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
	            otomateList.add(map);
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
		
		DBA_PRODUCT_OTOMATE dba = null;
		
		try
		{
			progressBar.hide();
			progressBar.dismiss();
			
			dba = new DBA_PRODUCT_OTOMATE(ctx);
			
			if (error)
				Utility.showCustomDialogInformation(ctx, "Informasi", 
						errorMessage);	
			else
			{
				
				dba.open();
//				dba.initialInsert(
//						SPPA_ID,
//						otomateList.get(0).get("VEHICLE_MERK").toString(),
//						otomateList.get(0).get("VEHICLE_CATEGORY").toString(),
//						otomateList.get(0).get("VEHICLE_DESC").toString(),
//						otomateList.get(0).get("BUILT_YEAR").toString(),
//						otomateList.get(0).get("PLAT_NO_1").toString(),
//						otomateList.get(0).get("PLAT_NO_2").toString(),
//						otomateList.get(0).get("PLAT_NO_3").toString(),
//						otomateList.get(0).get("COLOUR").toString(),
//						otomateList.get(0).get("CHASSIS_NO").toString(),
//						otomateList.get(0).get("ENGINE_NO").toString(),
//						otomateList.get(0).get("ACCESORIES_FLAG").toString(),
//						otomateList.get(0).get("NON_STANDARD_ACCESORIES").toString(),
//						Integer.parseInt(otomateList.get(0).get("SEATING_CAP").toString()),
//						Utility.getFormatDate(polisList.get(0).get("EFF_DATE").toString()),
//						Utility.getFormatDate(polisList.get(0).get("EXP_DATE").toString()),
//						nf.parse(otomateList.get(0).get("SI").toString()).doubleValue(),
//						nf.parse(otomateList.get(0).get("ADD_SI").toString()).doubleValue(),
//						otomateList.get(0).get("ACT_OF_GOD").toString(),
//						otomateList.get(0).get("TPL").toString(),
//						nf.parse(otomateList.get(0).get("RATE").toString()).doubleValue(),
//						nf.parse(polisList.get(0).get("PREMIUM").toString()).doubleValue(),
//						nf.parse(polisList.get(0).get("STAMP").toString()).doubleValue(),
//						nf.parse(polisList.get(0).get("CHARGE").toString()).doubleValue(),
//						nf.parse(polisList.get(0).get("TOTAL_PREMIUM").toString()).doubleValue(),
//
//						"",
//						"",
//
//						"OTOMATE",
//						polisList.get(0).get("CUSTOMER_NAME").toString()
//						,""
//						,300000
//						,""
//						,""
//						,""
//						,1
//						);
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
