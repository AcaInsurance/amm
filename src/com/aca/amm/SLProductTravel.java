package com.aca.amm;

import java.io.File;
import java.net.SocketTimeoutException;
import java.text.NumberFormat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.conn.ConnectTimeoutException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.aca.database.DBA_MASTER_AGENT;
import com.aca.database.DBA_PRODUCT_MAIN;
import com.aca.database.DBA_PRODUCT_TRAVEL_SAFE;


public class SLProductTravel extends AsyncTask<String, String, Void>{

	private ProgressDialog progressBar;
	private Context ctx;
	private SyncUnsyncActivity sua = null;
	private SyncUnpaidActivity sup = null;
	
	private DBA_PRODUCT_MAIN dbProductMain = null;
	private DBA_PRODUCT_TRAVEL_SAFE dbProductTravelInt = null;
	private DBA_MASTER_AGENT dbAgent = null;
	
	private boolean error = false;
	private String errorMessage = "";
	
	private SoapObject requestinsert = null;
    private SoapSerializationEnvelope envelope = null;
    private HttpTransportSE androidHttpTransport = null;
    
    private static String NAMESPACE = "http://tempuri.org/";     
	private static String URL = Utility.getURL(); 
    private static String SOAP_ACTION_INSERT = "http://tempuri.org/DoSaveTravelSafeInt";     
    private static String METHOD_NAME_INSERT = "DoSaveTravelSafeInt";
    
    private SoapObject requestuploadimg = null;
    private SoapSerializationEnvelope envelopeuploadimg = null;
    private static String URL_UPLOAD_IMAGE = Utility.getUrlImage();
    private static String SOAP_ACTION_UPLOAD_IMG = "http://tempuri.org/DoSaveImage";     
    private static String METHOD_NAME_UPLOAD_IMG = "DoSaveImage";
    
    private String E_SPPA_NO = "";
    private long SPPA_ID = 0;
    

    private boolean isGetSPPA = false;
	private String flag = "FALSE";
    
    
	public SLProductTravel(Context ctx, long SPPA_ID, Activity a)
	{
		this.SPPA_ID = SPPA_ID;
		this.ctx = ctx;
		if (a instanceof SyncUnsyncActivity) {
			this.sua = (SyncUnsyncActivity) a;			
		}
		else if (a instanceof SyncUnpaidActivity) {
			this.sup = (SyncUnpaidActivity) a;
		}
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
	
	@Override
	protected Void doInBackground(String... arg0) {

		error = false;
		
		Cursor cProductMain = null;
		Cursor cProductTravelInt = null;
		Cursor cAgent = null;
		
		NumberFormat nf = NumberFormat.getInstance();
		try{
			
    		/* dapatin data di db product main */
			dbProductMain = new DBA_PRODUCT_MAIN(ctx);
			dbProductMain.open();
			cProductMain = dbProductMain.getRow(SPPA_ID);
			cProductMain.moveToFirst();
			
    		/* dapatin data di db db product travel int */
			dbProductTravelInt = new DBA_PRODUCT_TRAVEL_SAFE(ctx);
			dbProductTravelInt.open();
			cProductTravelInt = dbProductTravelInt.getRow(SPPA_ID);
			cProductTravelInt.moveToFirst();
			
			dbAgent = new DBA_MASTER_AGENT(ctx);
			dbAgent.open();
			cAgent = dbAgent.getRow();
			cAgent.moveToFirst();
			
			requestinsert.addProperty(Utility.GetPropertyInfo("BranchID", cAgent.getString(0), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("UserID", cAgent.getString(1), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("SignPlace", cAgent.getString(2), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("MKTCode", cAgent.getString(3), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("CurrentIPAddress", "127.0.0.2", String.class));
    		
    		requestinsert.addProperty(Utility.GetPropertyInfo("OfficeID", cAgent.getString(4), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("Status", "M", String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("CustomerNo", cProductMain.getString(1), String.class));

			requestinsert.addProperty(Utility.GetPropertyInfo("PrevPolisBranch", cProductMain.getString(17), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("PrevPolisYear", cProductMain.getString(18), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("PrevPolisNo", cProductMain.getString(19), String.class));
    		
			requestinsert.addProperty(Utility.GetPropertyInfo("TSI", "0", String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("DiscountPct", String.valueOf(cProductMain.getDouble(23)), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("Discount", String.valueOf(cProductMain.getDouble(24)), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("CommissionPct", "0", String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("Commission", "0", String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("Premi",  nf.format(cProductTravelInt.getDouble(26)), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("TotalPremi",  nf.format(cProductTravelInt.getDouble(26)), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("Stamp", cProductMain.getString(7), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("Charge", cProductMain.getString(8), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("DepartureDate", cProductMain.getString(12), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("ArrivalDate", cProductMain.getString(13), String.class));

			requestinsert.addProperty(Utility.GetPropertyInfo("TravelAlokasi", cProductTravelInt.getString(50), String.class));
			
			requestinsert.addProperty(Utility.GetPropertyInfo("MaxBenefit", nf.format(cProductTravelInt.getDouble(47)), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("TravelNote", cProductTravelInt.getString(15), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("TravelCountryFlag", cProductTravelInt.getString(16), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("TravelPassportNo", cProductTravelInt.getString(51), String.class));
			
			requestinsert.addProperty(Utility.GetPropertyInfo("TravelCountryName", cProductTravelInt.getString(17), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("TravelCountryCode",  cProductTravelInt.getString(39), String.class));

			requestinsert.addProperty(Utility.GetPropertyInfo("TravelCountryName2", cProductTravelInt.getString(83), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("TravelCountryCode2",  cProductTravelInt.getString(88), String.class));

			requestinsert.addProperty(Utility.GetPropertyInfo("TravelCountryName3", cProductTravelInt.getString(84), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("TravelCountryCode3",  cProductTravelInt.getString(89), String.class));

			
			requestinsert.addProperty(Utility.GetPropertyInfo("TravelCountryName4", cProductTravelInt.getString(85), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("TravelCountryCode4",  cProductTravelInt.getString(90), String.class));

			
			requestinsert.addProperty(Utility.GetPropertyInfo("TravelCountryName5", cProductTravelInt.getString(86), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("TravelCountryCode5",  cProductTravelInt.getString(91), String.class));

			
			requestinsert.addProperty(Utility.GetPropertyInfo("TravelCountryName6", cProductTravelInt.getString(87), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("TravelCountryCode6",  cProductTravelInt.getString(92), String.class));

			
			requestinsert.addProperty(Utility.GetPropertyInfo("AnnualFlag", cProductTravelInt.getString(40), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("ExchangeRate", cProductTravelInt.getString(41),String.class));
			
			requestinsert.addProperty(Utility.GetPropertyInfo("Name2", cProductTravelInt.getString(3), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("DOB2",  cProductTravelInt.getString(4), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("IDNo2", cProductTravelInt.getString(5), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("Premi2", nf.format(cProductTravelInt.getDouble(31)), String.class));

			requestinsert.addProperty(Utility.GetPropertyInfo("Name3", cProductTravelInt.getString(6), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("DOB3",  cProductTravelInt.getString(7), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("IDNo3", cProductTravelInt.getString(8), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("Premi3", nf.format(cProductTravelInt.getDouble(32)), String.class));

			requestinsert.addProperty(Utility.GetPropertyInfo("Name4", cProductTravelInt.getString(9), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("DOB4",  cProductTravelInt.getString(10), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("IDNo4", cProductTravelInt.getString(11), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("Premi4", nf.format(cProductTravelInt.getDouble(33)), String.class));

			requestinsert.addProperty(Utility.GetPropertyInfo("Name5", cProductTravelInt.getString(12), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("DOB5", cProductTravelInt.getString(13), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("IDNo5", cProductTravelInt.getString(14), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("Premi5", nf.format(cProductTravelInt.getDouble(34)), String.class));

			requestinsert.addProperty(Utility.GetPropertyInfo("Name6", cProductTravelInt.getString(12), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("DOB6", cProductTravelInt.getString(13), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("IDNo6", cProductTravelInt.getString(14), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("Premi6", nf.format(cProductTravelInt.getDouble(34)), String.class));
			

			requestinsert.addProperty(Utility.GetPropertyInfo("Name7", cProductTravelInt.getString(12), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("DOB7", cProductTravelInt.getString(13), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("IDNo7", cProductTravelInt.getString(14), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("Premi7", nf.format(cProductTravelInt.getDouble(34)), String.class));
			

			requestinsert.addProperty(Utility.GetPropertyInfo("Name8", cProductTravelInt.getString(12), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("DOB8", cProductTravelInt.getString(13), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("IDNo8", cProductTravelInt.getString(14), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("Premi8", nf.format(cProductTravelInt.getDouble(34)), String.class));
			

			requestinsert.addProperty(Utility.GetPropertyInfo("Name9", cProductTravelInt.getString(12), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("DOB9", cProductTravelInt.getString(13), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("IDNo9", cProductTravelInt.getString(14), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("Premi9", nf.format(cProductTravelInt.getDouble(34)), String.class));
			

			requestinsert.addProperty(Utility.GetPropertyInfo("Name10", cProductTravelInt.getString(12), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("DOB10", cProductTravelInt.getString(13), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("IDNo10", cProductTravelInt.getString(14), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("Premi10", nf.format(cProductTravelInt.getDouble(34)), String.class));


			requestinsert.addProperty(Utility.GetPropertyInfo("Name11", cProductTravelInt.getString(12), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("DOB11", cProductTravelInt.getString(13), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("IDNo11", cProductTravelInt.getString(14), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("Premi11", nf.format(cProductTravelInt.getDouble(34)), String.class));

			requestinsert.addProperty(Utility.GetPropertyInfo("Plan1", cProductTravelInt.getString(22), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("Plan2", cProductTravelInt.getString(1), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("BeneName", cProductTravelInt.getString(18), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("BeneRelation", cProductTravelInt.getString(19), String.class));


			requestinsert.addProperty(Utility.GetPropertyInfo("TotalDays", cProductTravelInt.getString(48), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("PremiDays",nf.format(cProductTravelInt.getDouble(45)), String.class));
			
			requestinsert.addProperty(Utility.GetPropertyInfo("TotalWeeks",cProductTravelInt.getString(49), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("PremiWeeks", nf.format(cProductTravelInt.getDouble(46)), String.class));
			
			requestinsert.addProperty(Utility.GetPropertyInfo("CCOD", cProductTravelInt.getString(43), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("DCOD", cProductTravelInt.getString(44),String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("ACOD", cProductTravelInt.getString(42),String.class));

			requestinsert.addProperty(Utility.GetPropertyInfo("PaymentMethod", "", String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("PaymentProofNo", "", String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("CCNo", "", String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("CCName", "", String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("CCMonth", "", String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("CCYear", "", String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("CCSecretCode", "", String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("CCType", "", String.class));


    		envelope.setOutputSoapObject(requestinsert);
    		androidHttpTransport.call(SOAP_ACTION_INSERT, envelope);  
    		//SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
    		
    		SoapObject result = (SoapObject)envelope.bodyIn;
        	String response = result.getProperty(0).toString(); 

    		E_SPPA_NO = response.toString();
    		
    		if (TextUtils.isDigitsOnly(E_SPPA_NO))
    			isGetSPPA = true;
    		else
    			isGetSPPA = false;
    		
    		if (!isGetSPPA) {
    			error = true;
    			return null;
    		}
    		    	
    		dbProductMain.open();
			dbProductMain.updateESPPA(SPPA_ID, E_SPPA_NO);
			dbProductMain.updateSyncDate(SPPA_ID, Utility.DateToString(Utility.GetToday(), "dd/MM/yyyy"));
			

			
			Log.d("Nomor sppa", E_SPPA_NO);
			isGetSPPA = true;
			
    		/* Sync Images */
    		String folder = Environment.getExternalStorageDirectory() +"/LoadImg/" + String.valueOf(SPPA_ID);
            File f = new File(folder);
            if(f.isDirectory())
            {
                File[] files=f.listFiles();
                for(int i=0;i<files.length;i++)
                {
                	SoapObject responseBody = null;
                	flag = "FALSE";
                	
		    		envelopeuploadimg = new SoapSerializationEnvelope(SoapEnvelope.VER11); 
		    		envelopeuploadimg.implicitTypes = true;
		        	envelopeuploadimg.dotNet = true;	//used only if we use the webservice from a dot net file (asmx)
		    		androidHttpTransport = new HttpTransportSE(URL_UPLOAD_IMAGE);
		    		requestuploadimg = new SoapObject(NAMESPACE, METHOD_NAME_UPLOAD_IMG);
		    	
		    		String fpath = folder + File.separator + files[i].getName().toString().trim();
                	String file = Utility.ImageToString(fpath);
                	publishProgress("Start uploading image : " + files[i].getName().toString().trim());

                	requestuploadimg.addProperty(Utility.GetPropertyInfo("sppano", E_SPPA_NO, String.class));
                	requestuploadimg.addProperty(Utility.GetPropertyInfo("filename", files[i].getName().toString().trim(), String.class));
                	requestuploadimg.addProperty(Utility.GetPropertyInfo("picbyte", file, String.class));
    				
                	envelopeuploadimg.setOutputSoapObject(requestuploadimg);
    	    		androidHttpTransport.call(SOAP_ACTION_UPLOAD_IMG, envelopeuploadimg);   

    	    		responseBody = (SoapObject) envelopeuploadimg.bodyIn;
    	            flag  = responseBody.getProperty(0).toString();

    	            Log.d("do save dno", flag + " " + i + " sukses");
    	            
    	            if (flag.toUpperCase().equals("FALSE")) {
    	            	break;
    	            }

                }
            }   

		}
		catch (Exception e) {
    		error = true;
    		e.printStackTrace();	        		
			errorMessage = new MasterExceptionClass(e).getException();
		}finally{
			
			 dbProductMain.updateCompletePhoto(SPPA_ID, flag.toUpperCase());
			
			if (dbProductMain != null)
				dbProductMain.close();
			
			if (dbProductTravelInt != null)
				dbProductTravelInt.close();
			
			if (cAgent != null)
				cAgent.close();
			
			if (dbAgent != null)
				dbAgent.close();
			
			if (cProductMain != null)
				cProductMain.close();
			
			if (cProductTravelInt != null)
				cProductTravelInt.close();
		}
		
		return null;
	}

	@Override
	protected void onProgressUpdate(String... values) {
		progressBar.setMessage(values[0]);
    }


	protected void onPostExecute(Void result) {
		
		super.onPostExecute(result);
		
		progressBar.hide();
		progressBar.dismiss();
		
		try
		{
			if (error) {
                if (isGetSPPA && flag.equals("FALSE"))
                  	Utility.showCustomDialogInformation(ctx, "Sinkronisasi foto gagal", 
                  			"Mohon lakukan sinkronisasi manual pada tabulasi belum dibayar");
                else if (!isGetSPPA && flag.equals("FALSE"))
                	Utility.showCustomDialogInformation(ctx, "Sinkronisasi SPPA dan foto gagal", 
						errorMessage);
            }
            else
			{
            	if (sua != null)
            		Utility.showCustomDialogInformation(ctx, "Sinkronisasi SPPA dan foto berhasil", 
						"Silahkan cek ke tabulasi belum dibayar");
			}
			

			if (isGetSPPA) {
				if (sua != null)
					sua.BindListView();
				else if (sup != null) {
					sup.BindListView();
				    sup.showReSync(E_SPPA_NO);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (dbProductMain != null)
				dbProductMain.close();
		}
	}
}
