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
import org.kobjects.util.Util;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.aca.database.DBA_MASTER_AGENT;
import com.aca.database.DBA_PRODUCT_MAIN;
import com.aca.database.DBA_PRODUCT_OTOMATE;


public class SLProductOtomate extends AsyncTask<String, String, Void>{
	private static final String TAG = "com.aca.amm.SLProductOtomate";
	
	private ProgressDialog progressBar;
	private Context ctx;
	private SyncUnsyncActivity sua = null;
	private SyncUnpaidActivity sup = null;
	
	private DBA_PRODUCT_MAIN dbProductMain = null;
	private DBA_PRODUCT_OTOMATE dbProductOtomate = null;
	private DBA_MASTER_AGENT dbAgent = null;
	
	private boolean error = false;
	private String errorMessage = "";
	
    private HttpTransportSE androidHttpTransport = null;
    private static String NAMESPACE = "http://tempuri.org/";  
    
    private SoapObject requestinsert = null;
    private SoapSerializationEnvelope envelope = null;
	private static String URL = Utility.getURL(); 
    private static String SOAP_ACTION_INSERT = "http://tempuri.org/DoSaveOtomateNew";     
    private static String METHOD_NAME_INSERT = "DoSaveOtomateNew";
    
    private SoapObject requestuploadimg = null;
    private SoapSerializationEnvelope envelopeuploadimg = null;
    private static String URL_UPLOAD_IMAGE = Utility.getUrlImage();
    private static String SOAP_ACTION_UPLOAD_IMG = "http://tempuri.org/DoSaveImage";
    private static String METHOD_NAME_UPLOAD_IMG = "DoSaveImage";
    
    private String E_SPPA_NO = "";
    private long SPPA_ID = 0;

    private boolean isGetSPPA = false;
	private String flag = "FALSE";
	boolean isAddedPhoto = false;
    

	public SLProductOtomate(Context ctx, long SPPA_ID, Activity a)
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
		Cursor cProductOtomate = null;
		Cursor cAgent = null;
		
		try{
			Log.i(TAG, "::doInBackground:" + "sppaid: "+ SPPA_ID); 
			 
			
			dbProductMain = new DBA_PRODUCT_MAIN(ctx);
			dbProductOtomate = new DBA_PRODUCT_OTOMATE(ctx);			
			dbAgent = new DBA_MASTER_AGENT(ctx);
			dbProductMain.open();
			dbProductOtomate.open();
			dbAgent.open();
			

			isAddedPhoto = Utility.isAddedPhoto(SPPA_ID);
			if(!isAddedPhoto)
				return null;
			
			
			cProductMain = dbProductMain.getRow(SPPA_ID);
			cProductMain.moveToFirst();			 
			cProductOtomate = dbProductOtomate.getRow(SPPA_ID);
			cProductOtomate.moveToFirst();			
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
    		
    		requestinsert.addProperty(Utility.GetPropertyInfo("TSI",String.valueOf(cProductOtomate.getLong(15) + cProductOtomate.getLong(16)), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("DiscountPct", String.valueOf(cProductMain.getDouble(23)), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("Discount", String.valueOf(cProductMain.getDouble(24)), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("CommissionPct", "0", String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("Commission", "0", String.class));
    		
    		NumberFormat nf = NumberFormat.getInstance();
    		String thepremi = (nf.format(cProductMain.getDouble(6)));
    		Log.i(TAG, "::doInBackground:" + "premi:" +  thepremi );
    		
    		requestinsert.addProperty(Utility.GetPropertyInfo("Premi",thepremi + "", String.class));
    		
    		
    		requestinsert.addProperty(Utility.GetPropertyInfo("TotalPremi", nf.format(cProductMain.getDouble(9)), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("Stamp", cProductMain.getString(7), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("Charge", cProductMain.getString(8), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("EffDate", cProductMain.getString(12), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("ExpDate", cProductMain.getString(13), String.class));
    		
    		requestinsert.addProperty(Utility.GetPropertyInfo("Rate", cProductOtomate.getString(19), String.class));
    		
    		requestinsert.addProperty(Utility.GetPropertyInfo("SI", String.valueOf(cProductOtomate.getLong(15)), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("AddSI",String.valueOf(cProductOtomate.getLong(16)), String.class));
//    		requestinsert.addProperty(Utility.GetPropertyInfo("ActOfGod", cProductOtomate.getString(17), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("TPL", cProductOtomate.getString(18), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("PA",  String.format("%.0f",  cProductOtomate.getDouble(30)), String.class));

    		requestinsert.addProperty(Utility.GetPropertyInfo("Flood", cProductOtomate.getString(36), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("EQ", cProductOtomate.getString(37), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("SRCC", cProductOtomate.getString(38), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("TS", cProductOtomate.getString(39), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("KodeProduk", cProductOtomate.getString(40), String.class));
            requestinsert.addProperty(Utility.GetPropertyInfo("BengkelAuth", cProductOtomate.getString(41), String.class));

    		
    		requestinsert.addProperty(Utility.GetPropertyInfo("VehicleMerk", cProductOtomate.getString(2), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("VehicleMerkDesc",cProductOtomate.getString(28),String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("VehicleCategory", cProductOtomate.getString(3), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("VehicleCategoryDesc", cProductOtomate.getString(29), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("KetBrandType", cProductOtomate.getString(26), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("BuiltYear", cProductOtomate.getString(4), String.class));
    		
    		requestinsert.addProperty(Utility.GetPropertyInfo("Plat1", cProductOtomate.getString(5), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("Plat2", cProductOtomate.getString(6), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("Plat3", cProductOtomate.getString(7), String.class));
    		
    		requestinsert.addProperty(Utility.GetPropertyInfo("ChassisNo", cProductOtomate.getString(9), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("EngineNo", cProductOtomate.getString(10), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("AccType", cProductOtomate.getString(27), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("NonStdAccesories", cProductOtomate.getString(11), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("SeatingCap", cProductOtomate.getString(12), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("Color", cProductOtomate.getString(8), String.class));
    		

    		requestinsert.addProperty(Utility.GetPropertyInfo("PaymentMethod", "", String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("PaymentProofNo", "", String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("CCNo", "", String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("CCName", "", String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("CCMonth", "", String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("CCYear", "", String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("CCSecretCode", "", String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("CCType", "", String.class));
    		

    		requestinsert.addProperty(Utility.GetPropertyInfo("Penggunaan", cProductOtomate.getString(32), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("OwnRisk", cProductOtomate.getString(31), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("DamageFlag", cProductOtomate.getString(33), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("DamageRemark", cProductOtomate.getString(34), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("LoadingType", cProductOtomate.getString(35), String.class));

//    		DoSaveOtomate{BranchID=00; UserID=068899; SignPlace=JAKARTA; MKTCode=A1; CurrentIPAddress=127.0.0.2; OfficeID=3; Status=M; type=aca; CustomerNo=0015000120; PrevPolisBranch=; PrevPolisYear=; PrevPolisNo=; TSI=998999999; DiscountPct=0.0; Discount=0.0; CommissionPct=0; Commission=0; Premi=1.32168e+07; TotalPremi=1.32168e+07; Stamp=0; Charge=0; EffDate=23/04/2015; ExpDate=23/04/2016; Rate=1.323; SI=998999999; AddSI=0; ActOfGod=0; TPL=20000000; VehicleMerk=47; VehicleMerkDesc=CADILLAC; VehicleCategory=1; VehicleCategoryDesc=ESCALADE; KetBrandType=VTV; BuiltYear=2009; Plat1=B; Plat2=444; Plat3=; ChassisNo=TTT; EngineNo=RR; AccType=0; NonStdAccesories=RR; SeatingCap=1; Color=TTT; PaymentMethod=; PaymentProofNo=; CCNo=; CCName=; CCMonth=; CCYear=; CCSecretCode=; CCType=; }
    		envelope.setOutputSoapObject(requestinsert);
    		androidHttpTransport.call(SOAP_ACTION_INSERT, envelope);  
    		//SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
    		
    		SoapObject result = (SoapObject)envelope.bodyIn;
        	String response = result.getProperty(0).toString(); 

    		E_SPPA_NO = response.toString();
    		
    		Log.i(TAG, "::doInBackground:" + "e sppa no: " + E_SPPA_NO);
    		if (TextUtils.isDigitsOnly(E_SPPA_NO))
    			isGetSPPA = true;
    		else
    			isGetSPPA = false;
    		
    		if (!isGetSPPA) {
    			error = true;
    			return null;
    		}
    		 
			dbProductMain.updateESPPA(SPPA_ID, E_SPPA_NO);
			dbProductMain.updateSyncDate(SPPA_ID, Utility.DateToString(Utility.GetToday(), "dd/MM/yyyy"));

			
			Log.d("Nomor sppa", E_SPPA_NO);
			
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
			
			if (dbProductOtomate != null)
				dbProductOtomate.close();
			
			if (cAgent != null)
				cAgent.close();
			
			if (dbAgent != null)
				dbAgent.close();
			
			if (cProductMain != null)
				cProductMain.close();
			
			if (cProductOtomate != null)
				cProductOtomate.close();
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
			if (!isAddedPhoto)
			{
				Utility.showCustomDialogInformation(ctx, "Informasi", "Foto belum ada");
				return;
			}
			
			if (error) 
			{
                if (isGetSPPA && flag.equals("FALSE"))
                  	Utility.showCustomDialogInformation(ctx, "Sinkronisasi foto gagal", "Mohon lakukan sinkronisasi manual pada tabulasi belum dibayar");
                else if (!isGetSPPA && flag.equals("FALSE"))
                	Utility.showCustomDialogInformation(ctx, "Sinkronisasi SPPA dan foto gagal", errorMessage); 
            }
            else
			{
            	if (sua != null)
            		Utility.showCustomDialogInformation(ctx, "Sinkronisasi SPPA dan foto berhasil", "Silahkan cek ke tabulasi belum dibayar");
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
	
	}
}
