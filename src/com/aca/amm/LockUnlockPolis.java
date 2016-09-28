package com.aca.amm;

import java.net.SocketTimeoutException;
import org.apache.http.conn.ConnectTimeoutException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class LockUnlockPolis  extends AsyncTask<String, Void, Void>{
	private Context ctx ;
	
	private boolean error = false;
	private String errorMessage = "";
	
	private SoapObject requestretrive = null;
    private SoapSerializationEnvelope envelope = null;
    private HttpTransportSE androidHttpTransport = null;
    
    private static String NAMESPACE = "http://tempuri.org/";     
	private static String URL = Utility.getURL();   

    private static String SOAP_ACTION = "http://tempuri.org/LockUnlockDb";     
    private static String METHOD_NAME= "LockUnlockDb";
    
    private ProgressDialog progressBar = null;
    private String noPolis;
    private String flag ;
    private String resultString = "";
    
	public LockUnlockPolis (Context ctx, String noPolis, String lockOrUnlock){
		this.ctx = ctx;
		this.noPolis = noPolis;
		flag = lockOrUnlock;
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
		envelope.dotNet = true;	//used only if we use the webservice from a dot net file (asmx)
		androidHttpTransport = new HttpTransportSE(URL);

	}
	
	@Override
	protected Void doInBackground(String... params) {
		error = false;
		
		try{
			requestretrive = new SoapObject(NAMESPACE, METHOD_NAME);
	        requestretrive.addProperty(Utility.GetPropertyInfo("functionType", flag, String.class));
	        requestretrive.addProperty(Utility.GetPropertyInfo("functionCode", "A", String.class));
	        requestretrive.addProperty(Utility.GetPropertyInfo("polisNo", noPolis, String.class));
	        
	    	envelope.setOutputSoapObject(requestretrive);
	    	envelope.bodyOut = requestretrive;
    		androidHttpTransport.call(SOAP_ACTION, envelope);  
    		
    		SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
    		
    		resultString = response.toString();
        	
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
		
		try{	
			
			progressBar.hide();
			progressBar.dismiss();
			
			if(error){
				Utility.showCustomDialogInformation(ctx, "Informasi", 
						errorMessage);				
			}
			else{
			}			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public String getResult () {
		return resultString;
	}
}
