package com.aca.amm;

import java.net.SocketTimeoutException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.aca.database.DBA_PRODUCT_TOKO;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class LoadProductToko extends AsyncTask<String, Void, Void>{
	private ProgressDialog progressBar;
	private Context ctx;
	
	private boolean error = false;
	private String errorMessage = "";
	
	private SoapObject requestRetrieve = null;
    private SoapSerializationEnvelope envelope = null;
    private HttpTransportSE androidHttpTransport = null;
    
    private static String NAMESPACE = "http://tempuri.org/";     
	private static String URL = Utility.getURL(); 
    private static String SOAP_ACTION = "http://tempuri.org/LoadToko";     //
    private static String METHOD_NAME= "LoadToko";  //
    
    private long SPPA_ID = 0;
    
    private NumberFormat nf;
    
    private String noPolis;
    
    private ArrayList<HashMap<String, String>> tokoList;
    private ArrayList<HashMap<String, String>> polisList ;
    
    public LoadProductToko (Context ctx, String policyNo, Long SPPA_ID, 
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
            
            tokoList = new ArrayList<HashMap<String, String>>(); 
            
            int iTotalDataFromWebService = table.getPropertyCount();
            for(int i = 0; i < iTotalDataFromWebService; i++)
            {
            	tableRow = (SoapObject) table.getProperty(i);

	            HashMap<String, String> map = new HashMap<String, String>(); 
	            map.put("SPPA_NO", tableRow.getPropertySafelyAsString("SPPA_NO").toString()); 
	            
	            map.put("KODE_POS_LOKASI", tableRow.getPropertySafelyAsString("KODE_POS_LOKASI").toString()); 
	            map.put("SHOPPING_CENTRE_FLAG", tableRow.getPropertySafelyAsString("SHOPPING_CENTRE_FLAG").toString());
	           
	            String flag = "0";
	            if (tableRow.getPropertySafelyAsString("TIPE_BANGUNAN_FLAG").toString().equals("0"))
	            	flag = "1";
	            else 
	            	flag = "0";
	            
	            map.put("TIPE_BANGUNAN_FLAG", flag);
		        
	            map.put("JENIS_TOKO", tableRow.getPropertySafelyAsString("JENIS_TOKO").toString()); 
	            map.put("JENIS_TOKO_DESC", tableRow.getPropertySafelyAsString("JENIS_TOKO_DESC").toString());
	            
	            flag = "0";
	            if (tableRow.getPropertySafelyAsString("FRANCHISE_FLAG").toString().equals("0"))
	            	flag = "1";
	            else 
	            	flag = "0";
	            
	            map.put("FRANCHISE_FLAG", flag); 
	            
	            map.put("BUILDING_SIZE", tableRow.getPropertySafelyAsString("BUILDING_SIZE").toString());
	            map.put("BUILDING_SI", tableRow.getPropertySafelyAsString("BUILDING_SI").toString());
	            map.put("CONTENT_SI", tableRow.getPropertySafelyAsString("CONTENT_SI").toString()); 
	            map.put("OTHER_1_SI", tableRow.getPropertySafelyAsString("OTHER_1_SI").toString());
	            
	            flag = "0";
	            if (tableRow.getPropertySafelyAsString("RISK_LOCATION_FLAG").toString().equals("0"))
	            	flag = "1";
	            else 
	            	flag = "0";
	            
	            map.put("RISK_LOCATION_FLAG", flag); 
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
	            map.put("QUESTION_4A_NOTE", tableRow.getPropertySafelyAsString("QUESTION_4A_NOTE").toString());
	            map.put("QUESTION_4B_FLAG", tableRow.getPropertySafelyAsString("QUESTION_4B_FLAG").toString());
	            map.put("QUESTION_4B_NOTE", tableRow.getPropertySafelyAsString("QUESTION_4B_NOTE").toString());
		        
	            map.put("FIRE_SPRINKLER_FLAG", tableRow.getPropertySafelyAsString("FIRE_SPRINKLER_FLAG").toString());
	            map.put("BUILDING_AGE", tableRow.getPropertySafelyAsString("BUILDING_AGE").toString());
		        
	            map.put("RATE", "0.00");

	            tokoList.add(map);
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
		
		DBA_PRODUCT_TOKO dba = null;
		
		try
		{
			progressBar.hide();
			progressBar.dismiss();
			
			dba = new DBA_PRODUCT_TOKO(ctx);
			
			if (error)
				Utility.showCustomDialogInformation(ctx, "Informasi", 
						errorMessage);	
			else
			{
				dba.open();
				
				dba.initialInsert(
					SPPA_ID,
					
					tokoList.get(0).get("KODE_POS_LOKASI").toString().toUpperCase(),
					Integer.parseInt(tokoList.get(0).get("SHOPPING_CENTRE_FLAG").toString()),
					Integer.parseInt(tokoList.get(0).get("TIPE_BANGUNAN_FLAG").toString()),
					tokoList.get(0).get("JENIS_TOKO").toString(),
					Integer.parseInt(tokoList.get(0).get("FRANCHISE_FLAG").toString()),
					Integer.parseInt(tokoList.get(0).get("RISK_LOCATION_FLAG").toString()),
					
					Integer.parseInt(tokoList.get(0).get("BUILDING_SIZE").toString()),
					nf.parse(tokoList.get(0).get("BUILDING_SI").toString()).doubleValue(),
					nf.parse(tokoList.get(0).get("CONTENT_SI").toString()).doubleValue(),
					0,//total
					
					tokoList.get(0).get("RISK_ADDRESS").toString(),
					tokoList.get(0).get("RISK_RT_NO").toString(),
					tokoList.get(0).get("RISK_RW_NO").toString(),
					tokoList.get(0).get("RISK_KELURAHAN").toString(),
					tokoList.get(0).get("RISK_KECAMATAN").toString(),
					tokoList.get(0).get("RISK_CITY").toString(),

					tokoList.get(0).get("RISK_POST_CODE").toString(),
					
					tokoList.get(0).get("WALL_FLAG").toString(),
					tokoList.get(0).get("FLOOR_FLAG").toString(),
					tokoList.get(0).get("CEILING_FLAG").toString(),
					tokoList.get(0).get("QUESTION_4A_FLAG").toString(),
					tokoList.get(0).get("QUESTION_4B_FLAG").toString(),
					
					Integer.parseInt(tokoList.get(0).get("BUILDING_AGE").toString()), //umur
					
					Utility.getFormatDate(polisList.get(0).get("EFF_DATE").toString()),
					Utility.getFormatDate(polisList.get(0).get("EXP_DATE").toString()),
					
					nf.parse(tokoList.get(0).get("RATE").toString()).doubleValue(),
					nf.parse(polisList.get(0).get("PREMIUM").toString()).doubleValue(),
					nf.parse(polisList.get(0).get("STAMP").toString()).doubleValue(),
					nf.parse(polisList.get(0).get("CHARGE").toString()).doubleValue(),
					nf.parse(polisList.get(0).get("TOTAL_PREMIUM").toString()).doubleValue(),
					
					"TOKO", 
					polisList.get(0).get("CUSTOMER_NAME").toString(),

					tokoList.get(0).get("JENIS_TOKO_DESC").toString(),
					nf.parse(tokoList.get(0).get("OTHER_1_SI").toString()).doubleValue(),
					Integer.parseInt(tokoList.get(0).get("FIRE_SPRINKLER_FLAG").toString())
					,"");
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
