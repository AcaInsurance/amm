package com.aca.amm;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.apache.http.conn.ConnectTimeoutException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class GetFlagApprovalDokumen extends AsyncTask<String, Void, String>{
	private ProgressDialog progressBar;
	private Context ctx;
	
	private boolean error = false;
	private String errorMessage = "";
	
	private SoapObject requestRetrieve = null;
    private SoapSerializationEnvelope envelope = null;
    private HttpTransportSE androidHttpTransport = null;
    
    private static String NAMESPACE = "http://tempuri.org/";     
	private static String URL = Utility.getURL();    
    private static String SOAP_ACTION = "http://tempuri.org/GetFlagApprovalDokumen";     
    private static String METHOD_NAME= "GetFlagApprovalDokumen";
    
    private String sppaNO = "0";
    public InterfaceApproval interfaceApproval = null;
	@SuppressWarnings("unused")
	private static final String TAG = "com.aca.amm.GetFlagApprovalDokumen";
	
    public GetFlagApprovalDokumen (Context ctx,  String sppa_no){
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
		String flag = "0";
		try{
			SoapObject responseBody = null;					// Contains XML content of dataset
	        
	        requestRetrieve.addProperty(Utility.GetPropertyInfo("sppaNo", sppaNO, String.class));
	        
	    	envelope.setOutputSoapObject(requestRetrieve);
    		androidHttpTransport.call(SOAP_ACTION, envelope);  

            responseBody = (SoapObject) envelope.bodyIn;
            flag  = responseBody.getProperty(0).toString();
            Log.i(TAG, "::doInBackground:" + "flag approval = " + flag);
		}          
            
		catch (Exception e) {
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
//		
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
				interfaceApproval.getFlagApproval(flag);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
