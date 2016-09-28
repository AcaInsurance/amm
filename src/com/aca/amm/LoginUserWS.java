package com.aca.amm;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class LoginUserWS extends AsyncTask<String, Void, Void>{
	private static String NAMESPACE = "http://tempuri.org/";     
    private static String URL = Utility.getURL(); 
    
    private static String SOAP_ACTION = "http://tempuri.org/ValidateLoginAgent";     
    private static String METHOD_NAME = "ValidateLoginAgent";
    
    
    private ProgressDialog progressBar;    
    private PropertyInfo id, pass;
    private HttpTransportSE androidHttpTransport = null;
    
    private SoapObject requestretrive = null;
    private SoapSerializationEnvelope envelope = null;
	private Context context;

    private boolean error;
    private String errorMessage = "";
    private boolean login;
    private String userID, password;
    public InterfaceLogin mCallBack = null;
    
	public LoginUserWS (Context context, String userID, String password) {
		this.context  = context;
		this.userID = userID;
		this.password = password;
		
	}
		@Override
		protected void onPreExecute() {

			super.onPreExecute();

			progressBar = new ProgressDialog(context);
			progressBar.setCancelable(false);
			progressBar.setMessage("Please wait ...");
			progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressBar.show();
			
			requestretrive = new SoapObject(NAMESPACE, METHOD_NAME);

			id = new PropertyInfo();
			pass = new PropertyInfo();
			
			envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); 
			envelope.implicitTypes = true;
	    	envelope.dotNet = true;	//used only if we use the webservice from a dot net file (asmx)
	    				
			androidHttpTransport = new HttpTransportSE(URL);
		}
		
		@Override
		protected Void doInBackground(String... params) {

    		error = false;
			login = false;
			
			try{
				
				id.setName("AgentCode");
	    		id.setValue(userID.trim());
	    		id.setType(String.class);
	    		requestretrive.addProperty(id);
	    		
	    		pass.setName("Password");
	    		pass.setValue(password.toString());
	    		pass.setType(String.class);
	    		requestretrive.addProperty(pass);
	    		
	    		envelope.setOutputSoapObject(requestretrive);
	    		envelope.bodyOut = requestretrive;
	    		androidHttpTransport.call(SOAP_ACTION, envelope);  

            	SoapObject result = (SoapObject)envelope.bodyIn;
            	String loginResult = result.getProperty(0).toString(); 
	        	login = Boolean.parseBoolean(loginResult);
	        	
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
				if (error) {
					Utility.showCustomDialogInformation(context, "Informasi" ,errorMessage);
				}else {
					if (!login)
						Utility.showCustomDialogInformation(context, "Informasi", "Password tidak cocok dengan current User ID");
				}
				mCallBack.loginUser(login);
				
				
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
}

