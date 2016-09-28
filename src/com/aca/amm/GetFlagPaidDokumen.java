package com.aca.amm;

import java.net.SocketTimeoutException;

import org.apache.http.conn.ConnectTimeoutException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.AlarmManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class GetFlagPaidDokumen extends AsyncTask<String, Void, String> {
	
	private ProgressDialog progressBar;
	private Context ctx;
	
	private boolean error = false;
	private String errorMessage = "";
	
	private SoapObject requestRetrieve = null;
    private SoapSerializationEnvelope envelope = null;
    private HttpTransportSE androidHttpTransport = null;
    
    private static String NAMESPACE = "http://tempuri.org/";     
	private static String URL = Utility.getURL();    
    private static String SOAP_ACTION = "http://tempuri.org/GetSPPAStatus";     
    private static String METHOD_NAME= "GetSPPAStatus";
    
    private String sppaNO = "0";
    public InterfaceFlagPaid interfaceFlagPaid = null;
    
    
    public GetFlagPaidDokumen (Context ctx,  String sppa_no){
    	this.ctx = ctx;
    	this.sppaNO = sppa_no;
    	
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
		String flag ="0";
		
		try{
			SoapObject responseBody = null;					// Contains XML content of dataset
	        
	        requestRetrieve.addProperty(Utility.GetPropertyInfo("SPPANo", sppaNO, String.class));
	        
	    	envelope.setOutputSoapObject(requestRetrieve);
    		androidHttpTransport.call(SOAP_ACTION, envelope);  

            responseBody = (SoapObject) envelope.bodyIn;

            if (responseBody.getPropertyCount() != 0)
            	flag  = responseBody.getProperty(0).toString();
            else {
            	flag = "-1";
            	error = true;
            	errorMessage = "Gagal mendapatkan verifikasi";
            	return null;
			}
            
            Log.d("POL", flag);
		}
		catch (Exception e) {
			flag = "-1";
    		error = true;
    		e.printStackTrace();	        		
			errorMessage = new MasterExceptionClass(e).getException();
		}
		finally{
	        
		}
		return flag;
	}
	
	protected void onPostExecute(String flag) {
		
		super.onPostExecute(flag);
		
		progressBar.hide();
		progressBar.dismiss();
		
		try
		{
			if (error) {
				Utility.showCustomDialogInformation(ctx, "Informasi", 
						errorMessage);
				flag = "-1";
			}				
			else {
				interfaceFlagPaid.getFlagPaidApproval(flag);
			}
		}
		catch (Exception e) {
    		error = true;
    		e.printStackTrace();
    		errorMessage = new MasterExceptionClass(e).getException();
		}
	}
	
}
