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
import com.aca.database.DBA_PRODUCT_CARGO;
import com.aca.database.DBA_PRODUCT_MAIN;



public class SLProductCargo extends AsyncTask<String, String, Void>{

	private ProgressDialog progressBar;
	private Context ctx;
	private SyncUnsyncActivity sua = null;
	private SyncUnpaidActivity sup = null;
	
	private DBA_PRODUCT_MAIN dbProductMain = null;
	private DBA_PRODUCT_CARGO dbProductCargo = null;
	private DBA_MASTER_AGENT dbAgent = null;
	
	private boolean error = false;
	private String errorMessage = "";
	
    private HttpTransportSE androidHttpTransport = null;
    private static String NAMESPACE = "http://tempuri.org/";     

    private SoapObject requestinsert = null;
    private SoapSerializationEnvelope envelope = null;
    private static String URL = Utility.getURL();  
    private static String SOAP_ACTION_INSERT = "http://tempuri.org/DoSaveMarine";     
    private static String METHOD_NAME_INSERT = "DoSaveMarine";
    
    private SoapObject requestuploadimg = null;
    private SoapSerializationEnvelope envelopeuploadimg = null;
    private static String URL_UPLOAD_IMAGE = "http://www.aca-mobile.com/WsSaveImage.asmx";
    private static String SOAP_ACTION_UPLOAD_IMG = "http://tempuri.org/DoSaveImage";     
    private static String METHOD_NAME_UPLOAD_IMG = "DoSaveImage";
    
    private String E_SPPA_NO = "";
    private long SPPA_ID = 0;
    

    private boolean isGetSPPA = false;
	private String flag = "FALSE";
	@SuppressWarnings("unused")
	private static final String TAG = "com.aca.amm.SLProductCargo";
	
	public SLProductCargo(Context ctx, long SPPA_ID, Activity a)
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
		00 - PRODUCT_MAIN_ID,
		01 - _id,
		02 - ALAMAT_SAMA,
		03 - LUAS_BANGUNAN,
		04 - HARGA_BANGUNGAN,
		05 - HARGA_PERABOTAN,
		06 - TOTAL_HARGA,
		07 - ALAMAT_PERTANGGUNGAN,
		08 - RT,
		09 - RW,
		
		10 - KELURAHAN,
		11 - KECAMATAN,
		12 - KOTA_PROPINSI,
		13 - KODE_POS,
		14 - DINDING,
		15 - LANTAI,
		16 - ATAP,
		17 - KEBAKARAN_BANJIR,
		18 - PENCURIAN,
		19 - TANGGAL_MULAI,
		
		20 - TANGGAL_AKHIR,
		21 - RATE,
		22 - PREMI,
		24 - POLIS,
		24 - MATERAI,
		25 - TOTAL,
		26 - PRODUCT_NAME,
		27 - CUSTOMER_NAME 
		*/
		
		error = false;
		Cursor cProductMain = null;
		Cursor cProductCargo = null;
		Cursor cAgent = null;
		
		
		NumberFormat nf = NumberFormat.getInstance();
		try{
			
			/* dapatin data di db product main */
			dbProductMain = new DBA_PRODUCT_MAIN(ctx);
			dbProductMain.open();
			cProductMain = dbProductMain.getRow(SPPA_ID);
			cProductMain.moveToFirst();

			
    		/* dapatin data di db db product CARGO/MARINE */
			dbProductCargo = new DBA_PRODUCT_CARGO(ctx);
			dbProductCargo.open();
			cProductCargo= dbProductCargo.getRow(SPPA_ID);
			cProductCargo.moveToFirst();
			
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
    		
    		
    		requestinsert.addProperty(Utility.GetPropertyInfo("TSI", String.valueOf(cProductCargo.getLong(2)), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("InterestDetail", cProductCargo.getString(3), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("ConditionWarranties", cProductCargo.getString(4), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("BLNo", cProductCargo.getString(5), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("LCNo", cProductCargo.getString(6), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("ConveyanceType", cProductCargo.getString(7), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("ConveyanceName", cProductCargo.getString(8), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("ConveyanceCode", cProductCargo.getString(30), String.class));

    		
    		requestinsert.addProperty(Utility.GetPropertyInfo("Currency", cProductCargo.getString(11), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("ExchangeRate", String.valueOf(cProductCargo.getDouble(12)), String.class));
    		
    		requestinsert.addProperty(Utility.GetPropertyInfo("VesselWeight", cProductCargo.getString(13), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("VesselYear", cProductCargo.getString(14), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("VoyageFr", cProductCargo.getString(17), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("VoyageTo", cProductCargo.getString(18), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("VoyageTrans", cProductCargo.getString(19), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("PolicyType", cProductCargo.getString(20), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("VoyageNo", cProductCargo.getString(21), String.class));

    		requestinsert.addProperty(Utility.GetPropertyInfo("DiscountPct", String.valueOf(cProductMain.getDouble(23)), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("Discount", String.valueOf(cProductMain.getDouble(24)), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("CommissionPct", "0", String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("Commission", "0", String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("Premi", nf.format(cProductMain.getDouble(6)), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("TotalPremi", nf.format(cProductMain.getDouble(9)), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("Stamp", cProductMain.getString(7), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("Charge", cProductMain.getString(8), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("EffDate", cProductMain.getString(12), String.class));
    		
    		requestinsert.addProperty(Utility.GetPropertyInfo("Rate", String.valueOf(cProductCargo.getDouble(24)), String.class));
    		
    		requestinsert.addProperty(Utility.GetPropertyInfo("PaymentMethod", "", String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("PaymentProofNo", "", String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("CCNo", "", String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("CCName", "", String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("CCMonth", "", String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("CCYear", "", String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("CCSecretCode", "", String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("CCType", "", String.class));
    		
    		
//    		DoSaveMarine{
//    			BranchID=00;
//    			UserID=068899; 
//    			SignPlace=JAKARTA; 
//    			MKTCode=A1;
//    			CurrentIPAddress=127.0.0.2;
//    			OfficeID=3;
//    			Status=M;
//    			CustomerNo=0014000161; 
//    			TSI=2.0E14; 
//    			InterestDetail=STHM; 
//    			ConditionWarranties=;
//    			BLNo=;
//    			LCNo=STNYTTSHSHT; 
//    			ConveyanceType=MV; 
//    			
//    			ConveyanceName=;
//    			Currency=3;
//    			ExchangeRate=1.0;
//    			VesselWeight=; 
//    			VesselYear=;
//    			VoyageFr=RHSMSRHNSRY;
//    			VoyageTo=SRHNRS;
//    			VoyageTrans=SRHN;
//    			PolicyType=2;
//    			VoyageNo=;
//    			DiscountPct=0.0;
//    			Discount=0.0;
//    			CommissionPct=0; 
//    			Commission=0; 
//    			Premi=172000000000; 
//    			TotalPremi=172000000000;
//    			Stamp=0; Charge=0; 
//    			EffDate=; 
//    			Rate=0.086;
//    			PaymentMethod=;
//    			PaymentProofNo=; 
//    			CCNo=; CCName=; CCMonth=; CCYear=; CCSecretCode=; CCType=; 
//    			}
//    		
    		envelope.setOutputSoapObject(requestinsert);
    		
//    		publishProgress("Start synchronizing SPPA");
    		androidHttpTransport.call(SOAP_ACTION_INSERT, envelope);  

    		SoapObject result = (SoapObject)envelope.bodyIn;
        	String response = result.getProperty(0).toString(); 

    		E_SPPA_NO = response.toString();
    		
    		
    		Log.i(TAG, "::doInBackground:" + "e_SPPANO : " +   E_SPPA_NO);
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
			
//    		publishProgress("Synchronizing SPPA is successfull (" + E_SPPA_NO + ")");

			
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

    	            Log.d("do save cargo", flag + " " + i + " sukses");
    	            
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
			
			if (dbProductCargo != null)
				dbProductCargo.close();
			
			if (cProductMain != null)
				cProductMain.close();
			
			if (cProductCargo != null)
				cProductCargo.close();
			
			if (dbAgent != null)
				dbAgent.close();
			
			if (cAgent != null)
				cAgent.close();
		}
		
		return null;
	}
	
	@Override
	protected void onProgressUpdate(String... values) {
		progressBar.setMessage(values[0]);
    }

	@Override
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
