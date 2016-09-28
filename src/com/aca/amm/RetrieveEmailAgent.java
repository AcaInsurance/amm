package com.aca.amm;

import java.net.SocketTimeoutException;

import org.apache.http.conn.ConnectTimeoutException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class RetrieveEmailAgent extends AsyncTask<String, Void, String>{
	private ProgressDialog progressBar;
	private Context ctx;
	
	private boolean error = false;
	private String errorMessage = "";
	
	private SoapObject requestRetrieve = null;
    private SoapSerializationEnvelope envelope = null;
    private HttpTransportSE androidHttpTransport = null;
    
    private static String NAMESPACE = "http://tempuri.org/";     
	private static String URL = Utility.getURL(); 
    private static String SOAP_ACTION = "http://tempuri.org/GetEmailAgent";     
    private static String METHOD_NAME= "GetEmailAgent";
    
    private String userID = "";
    public InterfaceApproval interfaceApproval = null;
    
    public RetrieveEmailAgent (Context ctx,  String userID){
    	this.ctx = ctx;
    	this.userID = userID;
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
	protected String doInBackground(String... params) {
		error = false;
		try{
			SoapObject responseBody = null;					// Contains XML content of dataset
	        
	        requestRetrieve.addProperty(Utility.GetPropertyInfo("UID", userID, String.class));
	        
	    	envelope.setOutputSoapObject(requestRetrieve);
    		androidHttpTransport.call(SOAP_ACTION, envelope);  

            responseBody = (SoapObject) envelope.bodyIn;
            return responseBody.getProperty(0).toString();
            
		}	
		catch (Exception e) {
    		error = true;
    		e.printStackTrace();	        		
			errorMessage = new MasterExceptionClass(e).getException();
		}
		return "";
		
	}
	
	protected void onPostExecute(String emailAgent) {
		
		super.onPostExecute(emailAgent);
		
		progressBar.hide();
		progressBar.dismiss();
		
		try
		{
			if (error) {
				Utility.showCustomDialogInformation(ctx, "Informasi", 
						errorMessage);
			}
			else {
				interfaceApproval.getFlagApproval(emailAgent);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
