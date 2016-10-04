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
import com.aca.database.DBA_PRODUCT_PA_AMANAH;


public class SLProductPAAMANAH extends AsyncTask<String, String, Void>{

	@SuppressWarnings("unused")
	private static final String TAG = "com.aca.amm.SLProductPAAMANAH";

	private ProgressDialog progressBar;
	private Context ctx;
	private SyncUnsyncActivity sua = null;
	private SyncUnpaidActivity sup = null;
	
	private DBA_PRODUCT_MAIN dbProductMain = null;
	private DBA_PRODUCT_PA_AMANAH dbProductAmanah = null;
	private DBA_MASTER_AGENT dbAgent = null;
	
	private boolean error = false;
	private String errorMessage = "";
	
	private SoapObject requestinsert = null;
    private SoapSerializationEnvelope envelope = null;
    private HttpTransportSE androidHttpTransport = null;
    
    private static String NAMESPACE = "http://tempuri.org/";     
	private static String URL = Utility.getURL(); 
    private static String SOAP_ACTION_INSERT = "http://tempuri.org/DoSavePA";     
    private static String METHOD_NAME_INSERT = "DoSavePA";
    
    private SoapObject requestuploadimg = null;
    private SoapSerializationEnvelope envelopeuploadimg = null;
    private static String URL_UPLOAD_IMAGE = Utility.getUrlImage();
    private static String SOAP_ACTION_UPLOAD_IMG = "http://tempuri.org/DoSaveImage";     
    private static String METHOD_NAME_UPLOAD_IMG = "DoSaveImage";
    
    private String E_SPPA_NO = "";
    private long SPPA_ID = 0;
    
    private boolean isGetSPPA = false;
	private String flag = "FALSE";
    
    
	public SLProductPAAMANAH(Context ctx, long SPPA_ID, Activity a)
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
		02 - NILAI_PERTANGGUNGAN,
		
		03 - NAMA_AHLIWARIS_1,
		04 - HUB_AHLIWARIS_1,
		05 - ALAMAT_AHLIWARIS_1,
		
		06 - NAMA_AHLIWARIS_2,
		07 - HUB_AHLIWARIS_2,
		08 - ALAMAT_AHLIWARIS_2,
		
		09 - NAMA_AHLIWARIS_3,
		10 - HUB_AHLIWARIS_3,
		11 - ALAMAT_AHLIWARIS_3,
		
		12 - TGL_MULAI,
		13 - TGL_AKHIR,
		14 - PLAN,
		15 - PREMI,
		16 - BIAYA_POLIS,
		17 - TOTAL,
		
		18 - PRODUCT_NAME, 
		19 - CUSTOMER_NAME 
		*/
		
		error = false;
		
		Cursor cProductMain = null;
		Cursor cProductPAamanah = null;
		Cursor cAgent = null;
		
		NumberFormat nf = NumberFormat.getInstance();
		try{
			
    		/* dapatin data di db product main */
			dbProductMain = new DBA_PRODUCT_MAIN(ctx);
			dbProductMain.open();
			cProductMain = dbProductMain.getRow(SPPA_ID);
			cProductMain.moveToFirst();
			
    		/* dapatin data di db db product pa amanah */
			dbProductAmanah = new DBA_PRODUCT_PA_AMANAH(ctx);
			dbProductAmanah.open();
			cProductPAamanah = dbProductAmanah.getRow(SPPA_ID);
			cProductPAamanah.moveToFirst();
			
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
    		
    		requestinsert.addProperty(Utility.GetPropertyInfo("SI",nf.format(cProductPAamanah.getDouble(2)), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("DiscountPct", String.valueOf(cProductMain.getDouble(23)), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("Discount", String.valueOf(cProductMain.getDouble(24)), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("CommissionPct", "0", String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("Commission", "0", String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("Premi", nf.format(cProductMain.getDouble(6)), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("TotalPremi", nf.format(cProductMain.getDouble(9)), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("Stamp", cProductMain.getString(7), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("Charge", cProductMain.getString(8), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("EffDate", cProductMain.getString(12), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("ExpDate", cProductMain.getString(13), String.class));
    		
    		
    		requestinsert.addProperty(Utility.GetPropertyInfo("Plan", cProductPAamanah.getString(14), String.class));
    		
    		requestinsert.addProperty(Utility.GetPropertyInfo("BeneName1", cProductPAamanah.getString(3), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("BeneRelation1", cProductPAamanah.getString(4), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("BeneAddress1", cProductPAamanah.getString(5), String.class));
    		
    		requestinsert.addProperty(Utility.GetPropertyInfo("BeneName2", cProductPAamanah.getString(6), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("BeneRelation2", cProductPAamanah.getString(7), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("BeneAddress2", cProductPAamanah.getString(8), String.class));
    		
    		requestinsert.addProperty(Utility.GetPropertyInfo("BeneName3", cProductPAamanah.getString(9), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("BeneRelation3", cProductPAamanah.getString(10), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("BeneAddress3", cProductPAamanah.getString(11), String.class));
    		
    		requestinsert.addProperty(Utility.GetPropertyInfo("PaymentMethod", "", String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("PaymentProofNo", "", String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("CCNo", "", String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("CCName", "", String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("CCMonth", "", String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("CCYear", "", String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("CCSecretCode", "", String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("CCType", "", String.class));
//    		
//    		DoSavePA{BranchID=00; UserID=068899; SignPlace=JAKARTA; MKTCode=A1; CurrentIPAddress=127.0.0.2; OfficeID=3; Status=M; CustomerNo=0014000161; 
//    		PrevPolisBranch=; PrevPolisYear=; PrevPolisNo=; SI=90,000,000; DiscountPct=0.0; Discount=0.0; CommissionPct=0; Commission=0; Premi=261,000; 
//    		TotalPremi=261,000; Stamp=0; Charge=0; EffDate=23/04/2015; ExpDate=23/04/2016; Plan=2; BeneName1=ADAS; BeneRelation1=DADAS; BeneAddress1=DASD; BeneName2=; BeneRelation2=; BeneAddress2=; BeneName3=; BeneRelation3=; BeneAddress3=; PaymentMethod=; PaymentProofNo=; CCNo=; CCName=; CCMonth=; CCYear=; CCSecretCode=; CCType=; }

    		envelope.setOutputSoapObject(requestinsert);
    		androidHttpTransport.call(SOAP_ACTION_INSERT, envelope);  
    		//SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
    		
    		SoapObject result = (SoapObject)envelope.bodyIn;
        	String response = result.getProperty(0).toString(); 

    		E_SPPA_NO = response.toString();

    		Log.i(TAG, "::doInBackground:" + "E_SPPA_NO:" + E_SPPA_NO);
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
			
			if (dbProductAmanah != null)
				dbProductAmanah.close();
			
			if (cAgent != null)
				cAgent.close();
			
			if (dbAgent != null)
				dbAgent.close();
			
			if (cProductMain != null)
				cProductMain.close();
			
			if (cProductPAamanah != null)
				cProductPAamanah.close();
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
