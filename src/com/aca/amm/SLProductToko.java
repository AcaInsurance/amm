package com.aca.amm;

import java.io.File;
import java.net.SocketTimeoutException;

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
import com.aca.database.DBA_PRODUCT_TOKO;


public class SLProductToko extends AsyncTask<String, String, Void>{

	private ProgressDialog progressBar;
	private Context ctx;
	private SyncUnsyncActivity sua = null;
	private SyncUnpaidActivity sup = null;
	
	private DBA_PRODUCT_MAIN dbProductMain = null;
	private DBA_PRODUCT_TOKO dbProductToko = null;
	private DBA_MASTER_AGENT dbAgent = null;
	
	private boolean error = false;
	private String errorMessage = "";
	
	private SoapObject requestinsert = null;
    private SoapSerializationEnvelope envelope = null;
    private HttpTransportSE androidHttpTransport = null;
    
    private static String NAMESPACE = "http://tempuri.org/";     
	private static String URL = Utility.getURL(); 
    private static String SOAP_ACTION_INSERT = "http://tempuri.org/DoSaveToko";     
    private static String METHOD_NAME_INSERT = "DoSaveToko";
    
    private SoapObject requestuploadimg = null;
    private SoapSerializationEnvelope envelopeuploadimg = null;
    private static String URL_UPLOAD_IMAGE = Utility.getUrlImage();
    private static String SOAP_ACTION_UPLOAD_IMG = "http://tempuri.org/DoSaveImage";     
    private static String METHOD_NAME_UPLOAD_IMG = "DoSaveImage";
    
    private String E_SPPA_NO = "";
    private long SPPA_ID = 0;
    

    private boolean isGetSPPA = false;
	private String flag = "FALSE";
    
	public SLProductToko(Context ctx, long SPPA_ID, Activity a)
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
		01 - PRODUCT_MAIN_ID,
		
		02 - LUAS_BANGUNAN,
		03 - HARGA_BANGUNAN,
		04 - HARGA_PERABOTAN,
		05 - HARGA_STOCK,
		06 - TOTAL_HARGA,
		
		07 - ALAMAT_SAMA,
		08 - ALAMAT_PERTANGGUNGAN,
		09 - RT,
		10 - RW,
		11 - KELURAHAN,
		12 - KECAMATAN,
		13 - KOTA_PROPINSI,
		14 - KODE_POS,
		
		15 - DINDING,
		16 - LANTAI,
		17 - ATAP,
		18 - KEBAKARAN_BANJIR,
		19 - PENCURIAN,
		
		20 - TANGGAL_MULAI,
		21 - TANGGAL_AKHIR,
		22 - RATE,
		23 - PREMI,
		24 - POLIS,
		25 - MATERAI,
		26 - TOTAL,
		
		27 - KODE_POS_LOKASI_USAHA,
		28 - SHOPPING_CENTRE,
		29 - KEPEMILIKAN_BANGUNAN,
		30 - JENIS_USAHA,
		31 - GROUP_FRANCHISE,
		32 - UMUR_BANGUNAN, 
		33 - PRODUCT_NAME, 
		34 - CUSTOMER_NAME,
		
		35 - JENIS_USAHA_DESC
		36 - ADA_SPRINKLER
		*/
		
		error = false;
		
		Cursor cProductMain = null;
		Cursor cProductToko = null;
		Cursor cAgent = null;
		
		try{
			
    		/* dapatin data di db product main */
			dbProductMain = new DBA_PRODUCT_MAIN(ctx);
			dbProductMain.open();
			cProductMain = dbProductMain.getRow(SPPA_ID);
			cProductMain.moveToFirst();
			
    		/* dapatin data di db db product toko */
			dbProductToko = new DBA_PRODUCT_TOKO(ctx);
			dbProductToko.open();
			cProductToko = dbProductToko.getRow(SPPA_ID);
			cProductToko.moveToFirst();
			
			dbAgent = new DBA_MASTER_AGENT(ctx);
			dbAgent.open();
			cAgent = dbAgent.getRow();
			cAgent.moveToFirst();
			
			String flag = "0";
			
			if( cProductToko.getString(7).equals("0"))
				flag = "1";
			else 
				flag = "0";
			
			String flagSewa = "0";
			
			if( cProductToko.getString(29).equals("0"))
				flagSewa = "1";
			else 
				flagSewa = "0";
			
			String flagFranchise = "0";
			
			if( cProductToko.getString(31).equals("0"))
				flagFranchise = "1";
			else 
				flagFranchise = "0";
			
			
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
    		
			requestinsert.addProperty(Utility.GetPropertyInfo("TSI",cProductToko.getString(6), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("DiscountPct", String.valueOf(cProductMain.getDouble(23)), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("Discount", String.valueOf(cProductMain.getDouble(24)), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("CommissionPct", "0", String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("Commission", "0", String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("Premi", cProductMain.getString(6), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("TotalPremi", cProductMain.getString(9), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("Stamp", cProductMain.getString(7), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("Charge", cProductMain.getString(8), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("EffDate", cProductMain.getString(12), String.class));
    		requestinsert.addProperty(Utility.GetPropertyInfo("ExpDate", cProductMain.getString(13), String.class));

			requestinsert.addProperty(Utility.GetPropertyInfo("KdPosToko", cProductToko.getString(27), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("Shopping", cProductToko.getString(28),  String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("Building", flagSewa, String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("JenisToko", cProductToko.getString(30), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("JenisTokoDesc", cProductToko.getString(35), String.class));

			requestinsert.addProperty(Utility.GetPropertyInfo("FranchiseFlag",flagFranchise, String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("BuildingSize", cProductToko.getString(2), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("BuildingSI", cProductToko.getString(3), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("ContentSI",  cProductToko.getString(4), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("Other1SI", cProductToko.getString(5), String.class));
			
			requestinsert.addProperty(Utility.GetPropertyInfo("RiskAddress", cProductToko.getString(8), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("RiskRTNo", cProductToko.getString(10), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("RiskRWNo",cProductToko.getString(10), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("RiskKelurahan",  cProductToko.getString(11), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("RiskKecamatan", cProductToko.getString(12), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("RiskLocationFlag", flag, String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("RiskCity", cProductToko.getString(9), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("RiskPostCode", cProductToko.getString(14),String.class));

			requestinsert.addProperty(Utility.GetPropertyInfo("Latitude",  "0", String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("Longitude",  "0", String.class));
			
			requestinsert.addProperty(Utility.GetPropertyInfo("WallFlag","1", String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("WallNote", "",String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("FloorFlag", "1", String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("FloorNote", "", String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("CeilingFlag","1", String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("CeilingNote", "", String.class));

			requestinsert.addProperty(Utility.GetPropertyInfo("Question4AFlag", "0", String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("Question4ANote", "",  String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("Question4BFlag", "0", String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("Question4BNote", "",  String.class));
			
			requestinsert.addProperty(Utility.GetPropertyInfo("FireSprinklerFlag", cProductToko.getString(36), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("BuildingAge",  cProductToko.getString(32), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("Rate",  cProductToko.getString(22), String.class));


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
			
			if (dbProductToko != null)
				dbProductToko.close();
			
			if (cAgent != null)
				cAgent.close();
			
			if (dbAgent != null)
				dbAgent.close();
			
			if (cProductMain != null)
				cProductMain.close();
			
			if (cProductToko != null)
				cProductToko.close();
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
