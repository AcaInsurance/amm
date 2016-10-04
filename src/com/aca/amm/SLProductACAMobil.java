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
import com.aca.database.DBA_PRODUCT_ACA_MOBIL;
import com.aca.database.DBA_PRODUCT_MAIN;


public class SLProductACAMobil extends AsyncTask<String, String, Void>{

	private ProgressDialog progressBar;
	private Context ctx;
	private SyncUnsyncActivity sua;
	
	private DBA_PRODUCT_MAIN dbProductMain = null;
	private DBA_PRODUCT_ACA_MOBIL dbProductACAMobil = null;
	private DBA_MASTER_AGENT dbAgent = null;
	
	private boolean error = false;
	private String errorMessage = "";
	
    private HttpTransportSE androidHttpTransport = null;
    private static String NAMESPACE = "http://tempuri.org/";  
    
    private SoapObject requestinsert = null;
    private SoapSerializationEnvelope envelope = null;
	private static String URL = Utility.getURL();   
    private static String SOAP_ACTION_INSERT = "http://tempuri.org/DoSaveMobil";     
    private static String METHOD_NAME_INSERT = "DoSaveMobil";
    
    private SoapObject requestuploadimg = null;
    private SoapSerializationEnvelope envelopeuploadimg = null;
    private static String URL_UPLOAD_IMAGE = Utility.getUrlImage();
    private static String SOAP_ACTION_UPLOAD_IMG = "http://tempuri.org/DoSaveImage";     
    private static String METHOD_NAME_UPLOAD_IMG = "DoSaveImage";
    
    private String E_SPPA_NO = "";
    private long SPPA_ID = 0;

    private boolean isGetSPPA = false;
	private String flag = "FALSE";
    
	public SLProductACAMobil(Context ctx, long SPPA_ID, Activity a)
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
		00 - PRODUCT_MAIN_ID , 
		01 - _id, 
		02 - PRODUCT_MAKE , 
		03 - PRODUCT_TYPE , 
		04 - YEAR , 
		05 - NOPOL_1 , 
		06 - NOPOL_2 , 
		07 - NOPOL_3 , 
		08 - COLOR , 
		09 - CHASSIS_NO , 
		
		10 - MACHINE_NO , 
		11 - PERLENGKAPAN ,
		12 - SEAT_NUMBER , 
		13 - JANGKA_WAKTU_EFF ,
		14 - JANGKA_WAKTU_EXP,
		15 - NILAI_PERTANGGUNGAN ,
		16 - NILAI_PERTANGGUNGAN_PERLENGKAPAN ,
		17 - ACT_OF_GOD ,
		18 - TJH_PIHAK_KETIGA , 
		19 - RATE , 
		
		20 - PREMI ,
		21 - POLIS ,
		22 - MATERAI ,
		23 - TOTAL, 
		24 - PRODUCT_NAME, 
		25 - CUSTOMER_NAME,
		26 - PRODUCT_MODEL,
		27 - PERLENGKAPAN_TYPE,
		28 - PRODUCT_MAKE_DESC,
		29 - PRODUCT_TYEP_DESC
		 */
		
		
		error = false;
		Cursor cProductMain = null;
		Cursor cProductACAMobil = null;
		Cursor cAgent = null;

		String flag = "FALSE";
		NumberFormat nf = NumberFormat.getInstance();
		
		try{
			
    		/* dapatin data di db product main */
			dbProductMain = new DBA_PRODUCT_MAIN(ctx);
			dbProductMain.open();
			cProductMain = dbProductMain.getRow(SPPA_ID);
			cProductMain.moveToFirst();
			
    		/* dapatin data di db db product asri */
			dbProductACAMobil = new DBA_PRODUCT_ACA_MOBIL(ctx);
			dbProductACAMobil.open();
			cProductACAMobil = dbProductACAMobil.getRow(SPPA_ID);
			cProductACAMobil.moveToFirst();
			
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
    		requestinsert.addProperty(Utility.GetPropertyInfo("type", "aca", String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("CustomerNo", cProductMain.getString(1), String.class));
    		
    		requestinsert.addProperty(Utility.GetPropertyInfo("PrevPolisBranch", cProductMain.getString(17), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("PrevPolisYear", cProductMain.getString(18), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("PrevPolisNo", cProductMain.getString(19), String.class));
    		
    		requestinsert.addProperty(Utility.GetPropertyInfo("TSI",String.valueOf(cProductACAMobil.getLong(15) + cProductACAMobil.getLong(16)), String.class));
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
    		
    		requestinsert.addProperty(Utility.GetPropertyInfo("Rate", cProductACAMobil.getString(19), String.class));
    		
    		requestinsert.addProperty(Utility.GetPropertyInfo("SI", String.valueOf(cProductACAMobil.getLong(15)), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("AddSI",String.valueOf(cProductACAMobil.getLong(16)), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("ActOfGod", cProductACAMobil.getString(17), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("TPL", String.valueOf(cProductACAMobil.getString(18)), String.class));
    		
    		requestinsert.addProperty(Utility.GetPropertyInfo("VehicleMerk", cProductACAMobil.getString(2), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("VehicleMerkDesc",cProductACAMobil.getString(28),String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("VehicleCategory", cProductACAMobil.getString(3), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("VehicleCategoryDesc", cProductACAMobil.getString(29), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("KetBrandType", cProductACAMobil.getString(26), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("BuiltYear", cProductACAMobil.getString(4), String.class));
    		
    		requestinsert.addProperty(Utility.GetPropertyInfo("Plat1", cProductACAMobil.getString(5), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("Plat2", cProductACAMobil.getString(6), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("Plat3", cProductACAMobil.getString(7), String.class));
    		
    		requestinsert.addProperty(Utility.GetPropertyInfo("ChassisNo", cProductACAMobil.getString(9), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("EngineNo", cProductACAMobil.getString(10), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("AccType", cProductACAMobil.getString(27), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("NonStdAccesories", cProductACAMobil.getString(11), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("SeatingCap", cProductACAMobil.getString(12), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("Color", cProductACAMobil.getString(8), String.class));
    		

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
			
			if (dbProductACAMobil != null)
				dbProductACAMobil.close();
			
			if (cAgent != null)
				cAgent.close();
			
			if (dbAgent != null)
				dbAgent.close();
			
			if (cProductMain != null)
				cProductMain.close();
			
			if (cProductACAMobil != null)
				cProductACAMobil.close();
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
