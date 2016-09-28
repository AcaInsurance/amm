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

import com.aca.database.DBA_PRODUCT_MEDISAFE;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class LoadProductMedisafe extends AsyncTask<String, Void, Void>{
	private ProgressDialog progressBar;
	private Context ctx;
	
	private boolean error = false;
	private String errorMessage = "";
	
	private SoapObject requestRetrieve = null;
    private SoapSerializationEnvelope envelope = null;
    private HttpTransportSE androidHttpTransport = null;
    
    private static String NAMESPACE = "http://tempuri.org/";     
	private static String URL = Utility.getURL(); 
    private static String SOAP_ACTION = "http://tempuri.org/LoadMedisafe";  //   
    private static String METHOD_NAME= "LoadMedisafe"; //
    
    private long SPPA_ID = 0;
    
    private NumberFormat nf;
    
    private String noPolis;
    
    private ArrayList<HashMap<String, String>> mediasafeList;
    private ArrayList<HashMap<String, String>> familyList;
    private ArrayList<HashMap<String, String>> familyList2;
    private ArrayList<HashMap<String, String>> familyList3;
    private ArrayList<HashMap<String, String>> familyList4;
    private ArrayList<HashMap<String, String>> familyList5;
    
    
    private ArrayList<HashMap<String, String>> polisList ;
    
    public LoadProductMedisafe (Context ctx, String policyNo, Long SPPA_ID, 
    		ArrayList<HashMap<String, String>> polis,
    		ArrayList<HashMap<String, String>> famlist,
    		ArrayList<HashMap<String, String>> famlist2,
    		ArrayList<HashMap<String, String>> famlist3,
    		ArrayList<HashMap<String, String>> famlist4,
    		ArrayList<HashMap<String, String>> famlist5
    		){
    	this.ctx = ctx;
    	noPolis = policyNo;
    	polisList = polis;
    	
    	familyList = famlist;
    	familyList2 = famlist2;
    	familyList3 = famlist3;
    	familyList4 = famlist4;
    	familyList5 = famlist5;
    	
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
            
            mediasafeList = new ArrayList<HashMap<String, String>>(); 
            
            int iTotalDataFromWebService = table.getPropertyCount();
            for(int i = 0; i < iTotalDataFromWebService; i++)
            {
            	tableRow = (SoapObject) table.getProperty(i);

	            HashMap<String, String> map = new HashMap<String, String>(); 
	            map.put("SPPA_NO", tableRow.getPropertySafelyAsString("SPPA_NO").toString()); 
	            
	            map.put("PRODUCT_PLAN_FLAG", tableRow.getPropertySafelyAsString("PRODUCT_PLAN_FLAG").toString()); 
	            map.put("PREMIUM_METHOD_FLAG", tableRow.getPropertySafelyAsString("PREMIUM_METHOD_FLAG").toString());
	            map.put("NCB_ACCOUNT_NO", tableRow.getPropertySafelyAsString("NCB_ACCOUNT_NO").toString());
	            map.put("NCB_ACCOUNT_BANK", tableRow.getPropertySafelyAsString("NCB_ACCOUNT_BANK").toString());
	            map.put("NCB_ACCOUNT_NAME", tableRow.getPropertySafelyAsString("NCB_ACCOUNT_NAME").toString());
	          
	            map.put("Q1_FLAG", tableRow.getPropertySafelyAsString("Q1_FLAG").toString());
	            map.put("Q1_NOTE", tableRow.getPropertySafelyAsString("Q1_NOTE").toString());
	            map.put("Q2_FLAG", tableRow.getPropertySafelyAsString("Q2_FLAG").toString());
	            map.put("Q2_NOTE", tableRow.getPropertySafelyAsString("Q2_NOTE").toString());
	         
	            mediasafeList.add(map);
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
		
		DBA_PRODUCT_MEDISAFE dba = null;
		
		try
		{
			progressBar.hide();
			progressBar.dismiss();
			
			dba = new DBA_PRODUCT_MEDISAFE(ctx);
			
			if (error)
				Utility.showCustomDialogInformation(ctx, "Informasi", 
						errorMessage);	
			else
			{
				dba.open();
				String sPasangan = "0";
				String sAnak1 = "0";
				String sAnak2 = "0";
				String sAnak3 = "0";

				if(!familyList2.get(0).get("NAME").toString().equals(" "))
					sPasangan = "1";
				if(!familyList3.get(0).get("NAME").toString().equals(" "))
					sAnak1 = "1";
				if(!familyList4.get(0).get("NAME").toString().equals(" "))
					sAnak2 = "1";
				if(!familyList5.get(0).get("NAME").toString().equals(" "))
					sAnak3 = "1";
				
				int q1 = 0, q2 = 0 ;
				if (mediasafeList.get(0).get("Q1_FLAG").toString().toLowerCase().contains("anytype"))
					q1 = 0;
				if (mediasafeList.get(0).get("Q2_FLAG").toString().toLowerCase().contains("anytype"))
					q2 = 0;
				
				dba.initialInsert(
						SPPA_ID,
						
						//anak1
						familyList2.get(0).get("NAME").toString(),
						familyList2.get(0).get("DOB").toString(),
						familyList2.get(0).get("ID_NO").toString(),
						
						//anak2
						familyList3.get(0).get("NAME").toString(),
						familyList3.get(0).get("DOB").toString(),
						familyList3.get(0).get("ID_NO").toString(),
						
						
						//anak3
						familyList4.get(0).get("NAME").toString(),
						familyList4.get(0).get("DOB").toString(),
						familyList4.get(0).get("ID_NO").toString(),
						
						
						//anak4
						familyList5.get(0).get("NAME").toString(),
						familyList5.get(0).get("DOB").toString(),
						familyList5.get(0).get("ID_NO").toString(),
						
						mediasafeList.get(0).get("NCB_ACCOUNT_NO").toString(),
						mediasafeList.get(0).get("NCB_ACCOUNT_BANK").toString(),
						mediasafeList.get(0).get("NCB_ACCOUNT_NAME").toString(),
						
						Integer.parseInt(mediasafeList.get(0).get("PRODUCT_PLAN_FLAG").toString()),
						
						Utility.getFormatDate(polisList.get(0).get("EFF_DATE").toString()),
						Utility.getFormatDate(polisList.get(0).get("EXP_DATE").toString()),
						
						nf.parse(polisList.get(0).get("PREMIUM").toString()).doubleValue(),
						nf.parse(polisList.get(0).get("CHARGE").toString()).doubleValue(),
						nf.parse(polisList.get(0).get("TOTAL_PREMIUM").toString()).doubleValue(),
				
						"MEDISAFE", 
						
						polisList.get(0).get("CUSTOMER_NAME").toString(),

						nf.parse(familyList2.get(0).get("PREMI").toString()).doubleValue(),
						nf.parse(familyList3.get(0).get("PREMI").toString()).doubleValue(),
						nf.parse(familyList4.get(0).get("PREMI").toString()).doubleValue(),
						nf.parse(familyList5.get(0).get("PREMI").toString()).doubleValue(),
						
						sPasangan,
						sAnak1,
						sAnak2,
						sAnak3,

						q1,q2,
						mediasafeList.get(0).get("Q1_NOTE").toString(),
						mediasafeList.get(0).get("Q2_NOTE").toString()
						
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
