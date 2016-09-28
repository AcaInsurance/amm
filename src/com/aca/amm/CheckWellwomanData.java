package com.aca.amm;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.Toast;

@SuppressLint("DefaultLocale")
public class CheckWellwomanData extends AsyncTask<String, Void, Boolean> {

	private ProgressDialog progressBar;
	private SyncUnpaidActivity ctx;
	

	private boolean error = false;
	private String errorMessage = "";
	
	private SoapObject requestinsert = null;
    private SoapSerializationEnvelope envelope = null;
    private HttpTransportSE androidHttpTransport = null;
    
    private static String NAMESPACE = "http://tempuri.org/";     
	private static String URL = Utility.getURL();    
    private static String SOAP_ACTION_INSERT = "http://tempuri.org/CheckWellwomanData";     
    private static String METHOD_NAME_INSERT = "CheckWellwomanData";
    
    private String noktp = "";
    private boolean flag = false;
    
    
    public CheckWellwomanData (SyncUnpaidActivity ctx, String noktp) {
    	this.noktp = noktp;
    	this.ctx = ctx;
   
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
		requestinsert = new SoapObject(NAMESPACE, METHOD_NAME_INSERT);
	}
	
	@SuppressLint("DefaultLocale")
	@Override
	protected Boolean doInBackground(String... params) {
		// TODO Auto-generated method stub
		error = false;
			
		try {
			requestinsert.addProperty(Utility.GetPropertyInfo("IdNo", noktp, String.class));
			envelope.setOutputSoapObject(requestinsert);
			androidHttpTransport.call(SOAP_ACTION_INSERT, envelope);
			
			SoapObject result = (SoapObject)envelope.bodyIn;
	    	String response = result.getProperty(0).toString(); 
	    	
	    	publishProgress("Synchronizing SPPA is successfull");
	    	
	    	if (response.toLowerCase().equals("true"))
	    		flag = true;
	    	else {
				flag = false;
			}
		} 
		catch (Exception e) {
    		error = true;
    		e.printStackTrace();	        		
			errorMessage = new MasterExceptionClass(e).getException();
		}
		
		   	
		return flag;
	}
	

		
	private void publishProgress(String string) {
		// TODO Auto-generated method stub
		
	}


	protected void onPostExecute(Boolean result) {
		
		super.onPostExecute(result);
		
		
		progressBar.hide();
		progressBar.dismiss();
		try
		{
			if (error)
				Utility.showCustomDialogInformation(ctx, "Informasi", 
						errorMessage);
			else {
				if (!result) {
					String contentString = "No KTP sudah pernah terdaftar dalam produk Well Woman";
					Utility.showCustomDialogInformation(ctx, "Warning", contentString);
				}
				else {
					ctx.validasiApproval();
				}
			}
				
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
		}
	}
	
	

}
