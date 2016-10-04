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
import com.aca.database.DBA_PRODUCT_TRAVEL_DOM;


public class SLProductTravelDom extends AsyncTask<String, String, Void>{

	@SuppressWarnings("unused")
	private static final String TAG = "com.aca.amm.SLProductTravelDom";
	private ProgressDialog progressBar;
	private Context ctx;
	private SyncUnsyncActivity sua = null;
	private SyncUnpaidActivity sup = null;
	
	private DBA_PRODUCT_MAIN dbProductMain = null;
	private DBA_PRODUCT_TRAVEL_DOM dbProductTravelDom = null;
	private DBA_MASTER_AGENT dbAgent = null;
	
	private boolean error = false;
	private String errorMessage = "";
	
	private SoapObject requestinsert = null;
    private SoapSerializationEnvelope envelope = null;
    private HttpTransportSE androidHttpTransport = null;
    
    private static String NAMESPACE = "http://tempuri.org/";     
	private static String URL = Utility.getURL();  
    private static String SOAP_ACTION_INSERT = "http://tempuri.org/DoSaveTravelSafeDom";     
    private static String METHOD_NAME_INSERT = "DoSaveTravelSafeDom";
    
    private SoapObject requestuploadimg = null;
    private SoapSerializationEnvelope envelopeuploadimg = null;
    private static String URL_UPLOAD_IMAGE = Utility.getUrlImage();
    private static String SOAP_ACTION_UPLOAD_IMG = "http://tempuri.org/DoSaveImage";     
    private static String METHOD_NAME_UPLOAD_IMG = "DoSaveImage";
    
    private String E_SPPA_NO = "";
    private long SPPA_ID = 0;
    

    private boolean isGetSPPA = false;
	private String flag = "FALSE";
    
	public SLProductTravelDom(Context ctx, long SPPA_ID, Activity a)
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
		/*
			00 - _id, 
    		01 - PRODUCT_MAIN_ID,
    		02 - TUJUAN_PERJALANAN,
    		03 - KOTA_TUJUAN,
    		04 - AHLI_WARIS,
    		05 - HUBUNGAN,
    		06 - TGL_KEBERANGKATAN,
    		07 - TGL_KEMBALI,
    		08 - PLAN,
    		09 - JUMLAH_HARI,
    		10 - TAMBAHAN_PER_MINGGU,
    		11 - PREMI,
    		12 - BIAYA_POLIS,
    		13 - TOTAL, 
    		14 - PRODUCT_NAME, 
    		15 - CUSTOMER_NAME,
    		16 - CCOD ,
        	17 - DCOD ,
        	18 - PREMIDAYS ,
        	19 - PREMIWEEKS,
        	20 - MAXBENEFIT,
        	21 - TOTALDAYS ,
        	22 - TOTALWEEKS
		*/
		
		error = false;
		
		Cursor cProductMain = null;
		Cursor cProductTravelDom = null;
		Cursor cAgent = null;
		
		NumberFormat nf = NumberFormat.getInstance();
		try{
			
    		/* dapatin data di db product main */
			dbProductMain = new DBA_PRODUCT_MAIN(ctx);
			dbProductMain.open();
			cProductMain = dbProductMain.getRow(SPPA_ID);
			cProductMain.moveToFirst();
			
    		/* dapatin data di db db product travel dom */
			dbProductTravelDom = new DBA_PRODUCT_TRAVEL_DOM(ctx);
			dbProductTravelDom.open();
			cProductTravelDom = dbProductTravelDom.getRow(SPPA_ID);
			cProductTravelDom.moveToFirst();
			
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
			requestinsert.addProperty(Utility.GetPropertyInfo("Premi", nf.format(cProductMain.getDouble(6)), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("TotalPremi", nf.format(cProductMain.getDouble(9)), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("Stamp", cProductMain.getString(7), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("Charge", cProductMain.getString(8), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("DepartureDate", cProductMain.getString(12), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("ArrivalDate", cProductMain.getString(13), String.class));

			requestinsert.addProperty(Utility.GetPropertyInfo("MaxBenefit",nf.format(cProductTravelDom.getDouble(20)), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("TravelNote", cProductTravelDom.getString(2),String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("TravelCityName", cProductTravelDom.getString(3), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("Plan1", cProductTravelDom.getString(8), String.class));

			requestinsert.addProperty(Utility.GetPropertyInfo("BeneName", cProductTravelDom.getString(4), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("BeneRelation", cProductTravelDom.getString(5), String.class));
			
			requestinsert.addProperty(Utility.GetPropertyInfo("TotalDays",cProductTravelDom.getString(21),String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("PremiDays",nf.format( cProductTravelDom.getDouble(18)), String.class));
			
			requestinsert.addProperty(Utility.GetPropertyInfo("TotalWeeks",cProductTravelDom.getString(22),String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("PremiWeeks",nf.format( cProductTravelDom.getDouble(19)), String.class));
			
			requestinsert.addProperty(Utility.GetPropertyInfo("CCOD",cProductTravelDom.getString(16), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("DCOD", cProductTravelDom.getString(17), String.class));
			
			requestinsert.addProperty(Utility.GetPropertyInfo("PaymentMethod", "", String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("PaymentProofNo", "", String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("CCNo", "", String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("CCName", "", String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("CCMonth", "", String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("CCYear", "", String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("CCSecretCode", "", String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("CCType", "", String.class));

			/*
			for(int ii = 0; ii < requestinsert.getPropertyCount(); ii++)
			{
				Log.d("PI", requestinsert.getProperty(ii).toString());
			}
			*/
			
    		envelope.setOutputSoapObject(requestinsert);
    		androidHttpTransport.call(SOAP_ACTION_INSERT, envelope);  
    		//SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
    		
    		SoapObject result = (SoapObject)envelope.bodyIn;
        	String response = result.getProperty(0).toString(); 

    		E_SPPA_NO = response.toString();
    		
    		Log.i(TAG, "::doInBackground:" + "E_SPPANO :" +  E_SPPA_NO);
    		
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
			
			if (cProductMain != null)
				cProductMain.close();
			
			if (cProductTravelDom != null)
				cProductTravelDom.close();
			
			if (cAgent != null)
				cAgent.close();
			
			if (dbAgent != null)
				dbAgent.close();
			
			if (dbProductMain != null)
				dbProductMain.close();
			
			if (dbProductTravelDom != null)
				dbProductTravelDom.close();
			
			
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
