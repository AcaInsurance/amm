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
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.aca.database.DBA_MASTER_AGENT;
import com.aca.database.DBA_PRODUCT_EXECUTIVE_SAFE;
import com.aca.database.DBA_PRODUCT_MAIN;


public class SLProductExecutive extends AsyncTask<String, String, Void>{

	private ProgressDialog progressBar;
	private Context ctx;
	private SyncUnsyncActivity sua = null;
	private SyncUnpaidActivity sup = null;
	
	private DBA_PRODUCT_MAIN dbProductMain = null;
	private DBA_PRODUCT_EXECUTIVE_SAFE dbProductExecutive = null;
	private DBA_MASTER_AGENT dbAgent = null;
	
	private boolean error = false;
	private String errorMessage = "";
	
	private SoapObject requestinsert = null;
	private HttpTransportSE androidHttpTransport = null;
	 
    private SoapSerializationEnvelope envelope = null;
    private static String NAMESPACE = "http://tempuri.org/";     
	private static String URL = Utility.getURL();   
    private static String SOAP_ACTION_INSERT = "http://tempuri.org/DoSaveExecutiveSafe";     
    private static String METHOD_NAME_INSERT = "DoSaveExecutiveSafe";
    
    
    private SoapObject requestuploadimg = null;
    private SoapSerializationEnvelope envelopeuploadimg = null;
    private static String URL_UPLOAD_IMAGE = Utility.getUrlImage();
    private static String SOAP_ACTION_UPLOAD_IMG = "http://tempuri.org/DoSaveImage";     
    private static String METHOD_NAME_UPLOAD_IMG = "DoSaveImage";
    
    private String E_SPPA_NO = "";
    private long SPPA_ID = 0;

    private boolean isGetSPPA = false;
	private String flag = "FALSE";
    
	public SLProductExecutive(Context ctx, long SPPA_ID, Activity a)
	{
		this.SPPA_ID = SPPA_ID;
		this.ctx = ctx;
		this.sua = (SyncUnsyncActivity) a;
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
		01 - CUSTOMER_CODE,
		02 - CUSTOMER_NAME,
		03 - PRODUCT_NAME,
		04 - I_SPPA_NO,
		05 - E_SPPA_NO,
		06 - PREMI,
		07 - STAMP,
		08 - COST,
		09 - TOTAL,
		
		10 - SIGNATURE,
		11 - IS_PAID,
		12 - START_DATE,
		13 - END_DATE
		14 - ENTRY_DATE,
		15 - SYNC_DATE,
		16 - PAID_DATE
		*/
		
		/*
	 	00 - _id,
		01 - PRODUCT_MAIN_ID,
		
		02 - NAMA_PASANGAN,
		03 - TGL_LAHIR_PASANGAN,
		04 - NO_KTP_PASANGAN,
		
		05 - NAMA_ANAK_1,
		06 - TGL_LAHIR_ANAK_1,
		07 - NO_KTP_ANAK_1,
		
		08 - NAMA_ANAK_2,
		09 - TGL_LAHIR_ANAK_2,
		10 - NO_KTP_ANAK_2,
		
		11 - NAMA_ANAK_3,
		12 - TGL_LAHIR_ANAK_3,
		13 - NO_KTP_ANAK_3,
		
		14 - NAMA_AHLIWARIS_1,
		15 - HUB_AHLIWARIS_1,
		16 - ALAMAT_AHLIWARIS_1,
		
		17 - NAMA_AHLIWARIS_2,
		18 - HUB_AHLIWARIS_2,
		19 - ALAMAT_AHLIWARIS_2,
		
		20 - NAMA_AHLIWARIS_3,
		21 - HUB_AHLIWARIS_3,
		22 - ALAMAT_AHLIWARIS_3,
		
		23 - TGL_MULAI,
		24 - TGL_AKHIR,
		25 - PLAN,
		26 - PREMI,
		27 - BIAYA_POLIS,
		28 - TOTAL,
		
		29 - PRODUCT_NAME, 
		30 - CUSTOMER_NAME
		
		31 - PREMI_PASANGAN
		32 - PREMI_ANAK_1
		33 - PREMI_ANAK_2
		34 - PREMI_ANAK_3
		
		35 - IS_PASANGAN
		36 - IS_ANAK_1
		37 - IS_ANAK_2
		38 - IS_ANAK_3
		*/
		
		/*
		00 - _id,
		01 - PRODUCT_MAIN_ID,
		
		02 - NAMA_PASANGAN,
		03 - TGL_LAHIR_PASANGAN,
		04 - NO_KTP_PASANGAN,
		
		05 - NAMA_ANAK_1,
		06 - TGL_LAHIR_ANAK_1,
		07 - NO_KTP_ANAK_1,
		
		08 - NAMA_ANAK_2,
		09 - TGL_LAHIR_ANAK_2,
		10 - NO_KTP_ANAK_2,
		
		11 - NAMA_ANAK_3,
		12 - TGL_LAHIR_ANAK_3,
		13 - NO_KTP_ANAK_3,
		
		14 - NAMA_AHLIWARIS_1,
		15 - HUB_AHLIWARIS_1,
		16 - ALAMAT_AHLIWARIS_1,
		
		17 - NAMA_AHLIWARIS_2,
		18 - HUB_AHLIWARIS_2,
		19 - ALAMAT_AHLIWARIS_2,
		
		20 - NAMA_AHLIWARIS_3,
		21 - HUB_AHLIWARIS_3,
		22 - ALAMAT_AHLIWARIS_3,
		
		23 - TGL_MULAI,
		24 - TGL_AKHIR,
		25 - PLAN,
		26 - PREMI,
		27 - BIAYA_POLIS,
		28 - TOTAL,
		
		29 - PRODUCT_NAME, 
		30 - CUSTOMER_NAME,
		31 - PREMI_PASANGAN,
		32 - PREMI_ANAK_1,
		33 - PREMI_ANAK_2,
		34 - PREMI_ANAK_3,
		35 - IS_PASANGAN,
		36 - IS_ANAK_1,
		37 - IS_ANAK_2,
		38 - IS_ANAK_3 
		 
		 */
		
		Cursor cProductMain = null;
		Cursor cProductExecutive = null;
		Cursor cAgent = null;

		NumberFormat nf =NumberFormat.getInstance();
		try{
			
    		/* dapatin data di db product main */
			dbProductMain = new DBA_PRODUCT_MAIN(ctx);
			dbProductMain.open();
			cProductMain = dbProductMain.getRow(SPPA_ID);
			cProductMain.moveToFirst();
			
    		/* dapatin data di db db product EXECUTIVE SAFE */
			dbProductExecutive = new DBA_PRODUCT_EXECUTIVE_SAFE(ctx);
			dbProductExecutive.open();
			cProductExecutive = dbProductExecutive.getRow(SPPA_ID);
			cProductExecutive.moveToFirst();
			
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
			requestinsert.addProperty(Utility.GetPropertyInfo("Premi", nf.format(cProductMain.getDouble(6)),String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("TotalPremi", nf.format(cProductMain.getDouble(9)), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("Stamp", cProductMain.getString(7), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("Charge", cProductMain.getString(8), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("EffDate", cProductMain.getString(12), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("ExpDate", cProductMain.getString(13), String.class));
			
			requestinsert.addProperty(Utility.GetPropertyInfo("Plan", cProductExecutive.getString(25), String.class));

			requestinsert.addProperty(Utility.GetPropertyInfo("Name1", cProductExecutive.getString(2), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("DOB1", cProductExecutive.getString(3), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("IDNo1", cProductExecutive.getString(4), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("Premi1", nf.format(cProductExecutive.getDouble(31)), String.class));

			requestinsert.addProperty(Utility.GetPropertyInfo("Name2", cProductExecutive.getString(5), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("DOB2", cProductExecutive.getString(6), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("IDNo2", cProductExecutive.getString(7), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("Premi2", nf.format(cProductExecutive.getDouble(32)), String.class));

			requestinsert.addProperty(Utility.GetPropertyInfo("Name3", cProductExecutive.getString(8), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("DOB3", cProductExecutive.getString(9), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("IDNo3", cProductExecutive.getString(10), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("Premi3", nf.format(cProductExecutive.getDouble(33)), String.class));
			
			requestinsert.addProperty(Utility.GetPropertyInfo("Name4", cProductExecutive.getString(11), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("DOB4", cProductExecutive.getString(12), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("IDNo4", cProductExecutive.getString(13), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("Premi4", nf.format(cProductExecutive.getDouble(34)), String.class));
			
			requestinsert.addProperty(Utility.GetPropertyInfo("BeneName1", cProductExecutive.getString(14), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("BeneAddress1", cProductExecutive.getString(15), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("BeneRelation1", cProductExecutive.getString(16), String.class));
			
			requestinsert.addProperty(Utility.GetPropertyInfo("BeneName2", cProductExecutive.getString(17), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("BeneAddress2", cProductExecutive.getString(18), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("BeneRelation2", cProductExecutive.getString(19), String.class));
			
			requestinsert.addProperty(Utility.GetPropertyInfo("BeneName3", cProductExecutive.getString(20), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("BeneAddress3", cProductExecutive.getString(21), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("BeneRelation3", cProductExecutive.getString(22), String.class));

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
			
			if (dbProductExecutive != null)
				dbProductExecutive.close();
			
			if (cAgent != null)
				cAgent.close();
			
			if (dbAgent != null)
				dbAgent.close();
			
			if (cProductMain != null)
				cProductMain.close();
			
			if (cProductExecutive != null)
				cProductExecutive.close();
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
				Utility.showCustomDialogInformation(ctx, "Sinkronisasi SPPA dan foto berhasil", 
						"Silahkan cek ke tabulasi belum dibayar");
			}
			
			if (isGetSPPA)
				sua.BindListView();
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
