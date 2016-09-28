package com.aca.amm;

import java.util.ArrayList;
import java.util.HashMap;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;


class RetrieveOtomateTPL extends AsyncTask<String, Void, Void>{
	private Activity ctx ;
	
	private boolean error = false;
	private String errorMessage = "";
	
	private SoapObject requestretrive = null;
    private SoapSerializationEnvelope envelope = null;
    private HttpTransportSE androidHttpTransport = null;
    
    private static String NAMESPACE = "http://tempuri.org/";     
	private static String URL = Utility.getURL(); 

    private static String SOAP_ACTION = "http://tempuri.org/GetPremiTPLOtomate";     
    private static String METHOD_NAME= "GetPremiTPLOtomate";
    
    private String idKtp;
    private ArrayList<HashMap<String, String>> arrList = null;
	private ProgressDialog progressBar;    
	public OtomateTPLListener mcallback = null;
	
	public RetrieveOtomateTPL (Activity ctx, String idKtp ){
		this.ctx = ctx;
		this.idKtp = idKtp;
		
	}
	 
	public interface OtomateTPLListener {
		void getOtomateTPL (ArrayList<HashMap<String, String>> arrList) ;		
		

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
		progressBar.show();
		
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
	        requestretrive.addProperty(Utility.GetPropertyInfo("param", idKtp, String.class));
	        
	    	envelope.setOutputSoapObject(requestretrive);
	    	envelope.bodyOut = requestretrive;
    		androidHttpTransport.call(SOAP_ACTION, envelope);  
    			
            responseBody = (SoapObject) envelope.getResponse();
            responseBody = (SoapObject) responseBody.getProperty(1);
            
            if(responseBody.getPropertyCount() > 0) {
            	table = (SoapObject) responseBody.getProperty(0);
            	tableRow = (SoapObject) table.getProperty(0);
            	arrList = new ArrayList<HashMap<String, String>>(); 
                 
                HashMap<String, String> map = new HashMap<String, String>(); 
                map.put("TPL", tableRow.getPropertySafelyAsString("TPL").toString()); 
	            map.put("Premi", tableRow.getPropertySafelyAsString("Premi").toString());   
                arrList.add(map);
        	}
            else {
            	error = true;
            	errorMessage = "Data TPL tidak ditemukan";
            			
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
			
			mcallback.getOtomateTPL(arrList);
		
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public ArrayList<HashMap<String, String>> getOtomateTPL (){
		return arrList;
	}
	
}