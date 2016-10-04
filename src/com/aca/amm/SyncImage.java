package com.aca.amm;
import java.io.File;
import java.net.SocketTimeoutException;

import org.apache.http.conn.ConnectTimeoutException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.aca.database.DBA_PRODUCT_MAIN;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;


public class SyncImage extends AsyncTask<Void, String, Void> 
{

    private HttpTransportSE androidHttpTransport = null;
    private static String NAMESPACE = "http://tempuri.org/";  
    
    private SoapObject requestinsert = null;
    private SoapSerializationEnvelope envelope = null;
	private static String URL = Utility.getURL();    
    private static String SOAP_ACTION_INSERT = "http://tempuri.org/CheckImage";     
    private static String METHOD_NAME_INSERT = "CheckImage";
    
    private SoapObject requestuploadimg = null;
    private SoapSerializationEnvelope envelopeuploadimg = null;
    private static String URL_UPLOAD_IMAGE = Utility.getUrlImage();
    private static String SOAP_ACTION_UPLOAD_IMG = "http://tempuri.org/DoSaveImage";     
    private static String METHOD_NAME_UPLOAD_IMG = "DoSaveImage";
    

    private boolean error = false;
	private String errorMessage = "";

	private ProgressDialog progDialog = null;
	private String flag = "";
	private String flag_image = "FALSE";
	
	private String E_SPPA_NO = "";
	private long SPPA_ID;
	private Context context;
	private SyncUnpaidActivity sua;
	private boolean backgroundService = false;
	
	public SyncImage(Context context, long SPPA_ID, SyncUnpaidActivity sua) {
		super();
		this.context = context;
		this.SPPA_ID = SPPA_ID;
		this.backgroundService = false;
		this.sua = sua;
	}
	
	public SyncImage(Context context, boolean backgroundService, long SPPA_ID) {
		this.context = context;
		this.backgroundService = backgroundService;
		this.SPPA_ID = SPPA_ID;
		this.sua = null;
		
	}



	protected void onPreExecute()  {
		if(!backgroundService) {
			progDialog = new ProgressDialog(context);
			progDialog.setCancelable(false);
			progDialog.setMessage("Please wait ...");
			progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progDialog.show();
		}
	}

	@Override
	protected Void doInBackground(Void... params) {

		DBA_PRODUCT_MAIN db = null;
		Cursor c = null;

		try{

			db = new DBA_PRODUCT_MAIN(context);
			db.open();


			c = db.getRow(SPPA_ID);

			c.moveToNext();

			E_SPPA_NO = c.getString(5);

			if (E_SPPA_NO != null && E_SPPA_NO.length() > 0)
			{

				Log.d("sppa id = ", SPPA_ID + "");
				/* Sync Images */
				String folder = Environment.getExternalStorageDirectory() +"/LoadImg/" + String.valueOf(SPPA_ID);
				File f = new File(folder);
				if(f.isDirectory())
				{
					File[] files=f.listFiles();
					for(int i=0;i<files.length;i++)
					{

						envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); 
						envelope.implicitTypes = true;
						envelope.dotNet = true;	//used only if we use the webservice from a dot net file (asmx)
						androidHttpTransport = new HttpTransportSE(URL);
						requestinsert = new SoapObject(NAMESPACE, METHOD_NAME_INSERT);

						requestinsert.addProperty(Utility.GetPropertyInfo("SPPANo", E_SPPA_NO, String.class));
						requestinsert.addProperty(Utility.GetPropertyInfo("filename", files[i].getName().toString().trim(), String.class));


						envelope.setOutputSoapObject(requestinsert);
						androidHttpTransport.call(SOAP_ACTION_INSERT, envelope);  
						//SoapPrimitive response = (SoapPrimitive)envelope.getResponse();

						SoapObject result = (SoapObject)envelope.bodyIn;
						String response = result.getProperty(0).toString(); 

						flag = response.toString();

						Log.d("file-->", files[i].getName().toString().trim());
						Log.d("flag-->", flag);

						if (flag.equals("0"))
						{
							SoapObject responseBody = null;
							
							flag_image = "FALSE";
							envelopeuploadimg = new SoapSerializationEnvelope(SoapEnvelope.VER11); 
							envelopeuploadimg.implicitTypes = true;
							envelopeuploadimg.dotNet = true;	//used only if we use the webservice from a dot net file (asmx)
							androidHttpTransport = new HttpTransportSE(URL_UPLOAD_IMAGE);
							requestuploadimg = new SoapObject(NAMESPACE, METHOD_NAME_UPLOAD_IMG);

							String fpath = folder + File.separator + files[i].getName().toString().trim();
							String file = Utility.ImageToString(fpath);

							// put publish prorgres here ]

							if (!backgroundService)
								publishProgress("Start uploading image : " + files[i].getName().toString().trim());
							
							requestuploadimg.addProperty(Utility.GetPropertyInfo("sppano", E_SPPA_NO, String.class));
							requestuploadimg.addProperty(Utility.GetPropertyInfo("filename", files[i].getName().toString().trim(), String.class));
							requestuploadimg.addProperty(Utility.GetPropertyInfo("picbyte", file, String.class));

							envelopeuploadimg.setOutputSoapObject(requestuploadimg);
							androidHttpTransport.call(SOAP_ACTION_UPLOAD_IMG, envelopeuploadimg);   
							
							responseBody = (SoapObject) envelopeuploadimg.bodyIn;
							flag_image  = responseBody.getProperty(0).toString();

		    	            Log.d("flag image", flag_image);
		    	            
							 if (flag_image.toUpperCase().equals("FALSE")) {
			    	            	break;
			    	            }
						}
						else {
							flag_image = "TRUE";
						}
					}
				}  
			}
		}
	
		catch (Exception e) {
    		error = true;
    		e.printStackTrace();	        		
			errorMessage = new MasterExceptionClass(e).getException();
		}
		finally
		{
			if (c != null)
				c.close();

			if (db != null)
				db.close();
		}

		return null;

	}    

	protected void onProgressUpdate(String... values) {
		progDialog.setMessage(values[0]);
	}

	protected void onPostExecute(Void result) 
	{
		DBA_PRODUCT_MAIN dba = null;
	
		try
		{
			if (!backgroundService) {

				progDialog.dismiss();
				progDialog = null;
				
				if(error)
					Toast.makeText(context, "Foto gagal di-sinkron", Toast.LENGTH_SHORT).show();
				else	
					Toast.makeText(context, "Foto berhasil di-sinkron", Toast.LENGTH_SHORT).show();
								
				dba = new DBA_PRODUCT_MAIN(context);		
				dba.open();
				dba.updateCompletePhoto(SPPA_ID, flag_image.toUpperCase());
				
				if (sua != null) {
					sua.BindListView();		
					Log.i("sync image", "bind list view");
				}
			}
			else {
				dba = new DBA_PRODUCT_MAIN(context);		
				dba.open();
				dba.updateCompletePhoto(SPPA_ID, flag_image.toUpperCase());
			}
			
		}
		catch (Exception e) {
    		error = true;
    		e.printStackTrace();	        		
			errorMessage = new MasterExceptionClass(e).getException();
		}
		
		finally {
			if (dba != null)
				dba.close();
		}
	}
}