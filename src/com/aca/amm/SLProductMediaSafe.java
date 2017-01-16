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
import com.aca.database.DBA_PRODUCT_MEDISAFE;
import com.aca.dbflow.MedisafeKuestioner;
import com.aca.dbflow.MedisafeKuestioner_Table;
import com.aca.util.Var;
import com.raizlabs.android.dbflow.sql.language.Select;

import static android.os.Build.VERSION_CODES.M;
import static com.aca.database.DBA_PRODUCT_CARGO.TSI;
import static com.aca.dbflow.MedisafeKuestioner_Table.IsYesA1;
import static com.aca.dbflow.MedisafeKuestioner_Table.IsYesA2;
import static com.aca.dbflow.MedisafeKuestioner_Table.IsYesA3;
import static com.aca.dbflow.MedisafeKuestioner_Table.IsYesA4;
import static com.aca.dbflow.MedisafeKuestioner_Table.IsYesB1;
import static com.aca.dbflow.MedisafeKuestioner_Table.IsYesB2i;
import static com.aca.dbflow.MedisafeKuestioner_Table.IsYesB2ii;
import static com.aca.dbflow.MedisafeKuestioner_Table.IsYesB2iii;
import static com.aca.dbflow.MedisafeKuestioner_Table.IsYesB2iv;
import static com.aca.dbflow.MedisafeKuestioner_Table.IsYesB2v;
import static com.aca.dbflow.MedisafeKuestioner_Table.IsYesB2vi;
import static com.aca.dbflow.MedisafeKuestioner_Table.IsYesB3;
import static com.aca.dbflow.MedisafeKuestioner_Table.IsYesB4;
import static com.aca.dbflow.MedisafeKuestioner_Table.IsYesB5i;
import static com.aca.dbflow.MedisafeKuestioner_Table.IsYesB5ii;
import static com.aca.dbflow.MedisafeKuestioner_Table.KeteranganA2;
import static com.aca.dbflow.MedisafeKuestioner_Table.KeteranganA3;
import static com.aca.dbflow.MedisafeKuestioner_Table.KeteranganA4;
import static com.aca.dbflow.MedisafeKuestioner_Table.KeteranganB1;
import static com.aca.dbflow.MedisafeKuestioner_Table.KeteranganB2i;
import static com.aca.dbflow.MedisafeKuestioner_Table.KeteranganB2ii;
import static com.aca.dbflow.MedisafeKuestioner_Table.KeteranganB2iii;
import static com.aca.dbflow.MedisafeKuestioner_Table.KeteranganB2iv;
import static com.aca.dbflow.MedisafeKuestioner_Table.KeteranganB2v;
import static com.aca.dbflow.MedisafeKuestioner_Table.KeteranganB2vi;
import static com.aca.dbflow.MedisafeKuestioner_Table.KeteranganB31;
import static com.aca.dbflow.MedisafeKuestioner_Table.KeteranganB32;
import static com.aca.dbflow.MedisafeKuestioner_Table.KeteranganB33;
import static com.aca.dbflow.MedisafeKuestioner_Table.KeteranganB34;
import static com.aca.dbflow.MedisafeKuestioner_Table.KeteranganB4;
import static com.aca.dbflow.MedisafeKuestioner_Table.KeteranganB5i;
import static com.aca.dbflow.MedisafeKuestioner_Table.KeteranganB5ii;
import static com.aca.dbflow.MedisafeKuestioner_Table.NamaPerusahaan1;
import static com.aca.dbflow.MedisafeKuestioner_Table.NamaPerusahaan2;
import static com.aca.dbflow.MedisafeKuestioner_Table.NamaPerusahaan3;
import static com.aca.dbflow.MedisafeKuestioner_Table.NamaPerusahaan4;
import static com.aca.dbflow.MedisafeKuestioner_Table.NoPolis1;
import static com.aca.dbflow.MedisafeKuestioner_Table.NoPolis2;
import static com.aca.dbflow.MedisafeKuestioner_Table.NoPolis3;
import static com.aca.dbflow.MedisafeKuestioner_Table.NoPolis4;
import static com.aca.dbflow.MedisafeKuestioner_Table.SppaNo;
import static com.aca.dbflow.PerluasanPremi_Table.Premi;
import static com.aca.dbflow.StandardField_Table.IsActive;


public class SLProductMediaSafe extends AsyncTask<String, String, Void>{

	private ProgressDialog progressBar;
	private Context ctx;
	private SyncUnsyncActivity sua = null;
	private SyncUnpaidActivity sup = null;
	
	private DBA_PRODUCT_MAIN dbProductMain = null;
	private DBA_PRODUCT_MEDISAFE dbProductMediaSafe = null;
	private DBA_MASTER_AGENT dbAgent = null;
	
	private boolean error = false;
	private String errorMessage = "";
	
	private SoapObject requestinsert = null;
    private SoapSerializationEnvelope envelope = null;
    private HttpTransportSE androidHttpTransport = null;
    
    private static String NAMESPACE = "http://tempuri.org/";     
	private static String URL = Utility.getURL(); 
    private static String SOAP_ACTION_INSERT = "http://tempuri.org/DoSaveMedisafe";     
    private static String METHOD_NAME_INSERT = "DoSaveMedisafe";
    
    private SoapObject requestuploadimg = null;
    private SoapSerializationEnvelope envelopeuploadimg = null;
    private static String URL_UPLOAD_IMAGE = Utility.getUrlImage();
    private static String SOAP_ACTION_UPLOAD_IMG = "http://tempuri.org/DoSaveImage";     
    private static String METHOD_NAME_UPLOAD_IMG = "DoSaveImage";
    
    private String E_SPPA_NO = "";
    private long SPPA_ID = 0;

    private boolean isGetSPPA = false;
	private String flag = "FALSE";
	
	public SLProductMediaSafe(Context ctx, long SPPA_ID, Activity a)
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
		androidHttpTransport = new HttpTransportSE(URL, Var.BASIC_TIMEOUT);
		requestinsert = new SoapObject(NAMESPACE, METHOD_NAME_INSERT);
	}
	
	@Override
	protected Void doInBackground(String... arg0) {
		error = false;
		
		Cursor cProductMain = null;
		Cursor cProductMediaSafe = null;
		Cursor cAgent = null;
		NumberFormat nf =NumberFormat.getInstance();
		try{
			
    		/* dapatin data di db product main */
			dbProductMain = new DBA_PRODUCT_MAIN(ctx);
			dbProductMain.open();
			cProductMain = dbProductMain.getRow(SPPA_ID);
			cProductMain.moveToFirst();
			
    		/* dapatin data di db db product MEDISAFE */
			dbProductMediaSafe = new DBA_PRODUCT_MEDISAFE(ctx);
			dbProductMediaSafe.open();
			cProductMediaSafe = dbProductMediaSafe.getRow(SPPA_ID);
			cProductMediaSafe.moveToFirst();
			
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
			requestinsert.addProperty(Utility.GetPropertyInfo("EffDate", cProductMain.getString(12), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("ExpDate", cProductMain.getString(13), String.class));

			requestinsert.addProperty(Utility.GetPropertyInfo("ProductPlanFlag", cProductMediaSafe.getString(19), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("NCBAccountNo",  cProductMediaSafe.getString(14), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("NCBAccountBank",  cProductMediaSafe.getString(15), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("NCBAccountName", cProductMediaSafe.getString(16), String.class));
			
			requestinsert.addProperty(Utility.GetPropertyInfo("Q1Flag", cProductMediaSafe.getString(33), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("Q1Note", cProductMediaSafe.getString(35), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("Q2Flag", cProductMediaSafe.getString(34),String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("Q2Note", cProductMediaSafe.getString(36), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("ApprovalRemarks", "",String.class));
			
			requestinsert.addProperty(Utility.GetPropertyInfo("Name1", cProductMediaSafe.getString(2), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("DOB1",cProductMediaSafe.getString(3), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("IDNo1", cProductMediaSafe.getString(4),String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("Premi1",nf.format(cProductMediaSafe.getDouble(25)), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("Gender1",cProductMediaSafe.getString(cProductMediaSafe.getColumnIndex(DBA_PRODUCT_MEDISAFE.GENDER_1)), String.class));

			requestinsert.addProperty(Utility.GetPropertyInfo("Name2", cProductMediaSafe.getString(5), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("DOB2", cProductMediaSafe.getString(6), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("IDNo2", cProductMediaSafe.getString(7), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("Premi2", nf.format(cProductMediaSafe.getDouble(26)), String.class));
            requestinsert.addProperty(Utility.GetPropertyInfo("Gender2",cProductMediaSafe.getString(cProductMediaSafe.getColumnIndex(DBA_PRODUCT_MEDISAFE.GENDER_2)), String.class));

			requestinsert.addProperty(Utility.GetPropertyInfo("Name3", cProductMediaSafe.getString(8), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("DOB3", cProductMediaSafe.getString(9), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("IDNo3", cProductMediaSafe.getString(10), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("Premi3",nf.format( cProductMediaSafe.getDouble(27)), String.class));
            requestinsert.addProperty(Utility.GetPropertyInfo("Gender3",cProductMediaSafe.getString(cProductMediaSafe.getColumnIndex(DBA_PRODUCT_MEDISAFE.GENDER_3)), String.class));

            requestinsert.addProperty(Utility.GetPropertyInfo("Name4", cProductMediaSafe.getString(11), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("DOB4", cProductMediaSafe.getString(12), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("IDNo4", cProductMediaSafe.getString(13), String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("Premi4",nf.format( cProductMediaSafe.getDouble(28)), String.class));
            requestinsert.addProperty(Utility.GetPropertyInfo("Gender4",cProductMediaSafe.getString(cProductMediaSafe.getColumnIndex(DBA_PRODUCT_MEDISAFE.GENDER_4)), String.class));

			requestinsert.addProperty(Utility.GetPropertyInfo("PaymentMethod", "", String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("PaymentProofNo", "", String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("CCNo", "", String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("CCName", "", String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("CCMonth", "", String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("CCYear", "", String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("CCSecretCode", "", String.class));
			requestinsert.addProperty(Utility.GetPropertyInfo("CCType", "", String.class));

            MedisafeKuestioner kuestioner = new Select()
                    .from(MedisafeKuestioner.class)
                    .where(SppaNo.eq(String.valueOf(SPPA_ID)))
                    .querySingle();

            requestinsert.addProperty(Utility.GetPropertyInfo("IsYesA1", String.valueOf(kuestioner.IsYesA1),String.class));
            requestinsert.addProperty(Utility.GetPropertyInfo("IsYesA2", String.valueOf(kuestioner.IsYesA2),String.class));
            requestinsert.addProperty(Utility.GetPropertyInfo("IsYesA3", String.valueOf(kuestioner.IsYesA3),String.class));
            requestinsert.addProperty(Utility.GetPropertyInfo("IsYesA4", String.valueOf(kuestioner.IsYesA4),String.class));
            requestinsert.addProperty(Utility.GetPropertyInfo("IsYesB1", String.valueOf(kuestioner.IsYesB1),String.class));
            requestinsert.addProperty(Utility.GetPropertyInfo("IsYesB2i", String.valueOf(kuestioner.IsYesB2i),String.class));
            requestinsert.addProperty(Utility.GetPropertyInfo("IsYesB2ii", String.valueOf(kuestioner.IsYesB2ii),String.class));
            requestinsert.addProperty(Utility.GetPropertyInfo("IsYesB2iii", String.valueOf(kuestioner.IsYesB2iii),String.class));
            requestinsert.addProperty(Utility.GetPropertyInfo("IsYesB2iv", String.valueOf(kuestioner.IsYesB2iv),String.class));
            requestinsert.addProperty(Utility.GetPropertyInfo("IsYesB2v", String.valueOf(kuestioner.IsYesB2v),String.class));
            requestinsert.addProperty(Utility.GetPropertyInfo("IsYesB2vi", String.valueOf(kuestioner.IsYesB2vi),String.class));
            requestinsert.addProperty(Utility.GetPropertyInfo("IsYesB3", String.valueOf(kuestioner.IsYesB3),String.class));
            requestinsert.addProperty(Utility.GetPropertyInfo("IsYesB4", String.valueOf(kuestioner.IsYesB4),String.class));
            requestinsert.addProperty(Utility.GetPropertyInfo("IsYesB5i", String.valueOf(kuestioner.IsYesB5i),String.class));
            requestinsert.addProperty(Utility.GetPropertyInfo("IsYesB5ii", String.valueOf(kuestioner.IsYesB5ii),String.class));
            requestinsert.addProperty(Utility.GetPropertyInfo("NamaPerusahaan1",kuestioner.NamaPerusahaan1,String.class));
            requestinsert.addProperty(Utility.GetPropertyInfo("NamaPerusahaan2",kuestioner.NamaPerusahaan2,String.class));
            requestinsert.addProperty(Utility.GetPropertyInfo("NamaPerusahaan3",kuestioner.NamaPerusahaan3,String.class));
            requestinsert.addProperty(Utility.GetPropertyInfo("NamaPerusahaan4",kuestioner.NamaPerusahaan4,String.class));
            requestinsert.addProperty(Utility.GetPropertyInfo("NoPolis1",kuestioner.NoPolis1,String.class));
            requestinsert.addProperty(Utility.GetPropertyInfo("NoPolis2",kuestioner.NoPolis2,String.class));
            requestinsert.addProperty(Utility.GetPropertyInfo("NoPolis3",kuestioner.NoPolis3,String.class));
            requestinsert.addProperty(Utility.GetPropertyInfo("NoPolis4",kuestioner.NoPolis4,String.class));
            requestinsert.addProperty(Utility.GetPropertyInfo("KeteranganA2",kuestioner.KeteranganA2,String.class));
            requestinsert.addProperty(Utility.GetPropertyInfo("KeteranganA3",kuestioner.KeteranganA3,String.class));
            requestinsert.addProperty(Utility.GetPropertyInfo("KeteranganA4",kuestioner.KeteranganA4,String.class));
            requestinsert.addProperty(Utility.GetPropertyInfo("KeteranganB1",kuestioner.KeteranganB1,String.class));
            requestinsert.addProperty(Utility.GetPropertyInfo("KeteranganB2i",kuestioner.KeteranganB2i,String.class));
            requestinsert.addProperty(Utility.GetPropertyInfo("KeteranganB2ii",kuestioner.KeteranganB2ii,String.class));
            requestinsert.addProperty(Utility.GetPropertyInfo("KeteranganB2iii",kuestioner.KeteranganB2iii,String.class));
            requestinsert.addProperty(Utility.GetPropertyInfo("KeteranganB2iv",kuestioner.KeteranganB2iv,String.class));
            requestinsert.addProperty(Utility.GetPropertyInfo("KeteranganB2v",kuestioner.KeteranganB2v,String.class));
            requestinsert.addProperty(Utility.GetPropertyInfo("KeteranganB2vi",kuestioner.KeteranganB2vi,String.class));
            requestinsert.addProperty(Utility.GetPropertyInfo("KeteranganB31",kuestioner.KeteranganB31,String.class));
            requestinsert.addProperty(Utility.GetPropertyInfo("KeteranganB32",kuestioner.KeteranganB32,String.class));
            requestinsert.addProperty(Utility.GetPropertyInfo("KeteranganB33",kuestioner.KeteranganB33,String.class));
            requestinsert.addProperty(Utility.GetPropertyInfo("KeteranganB34",kuestioner.KeteranganB34,String.class));
            requestinsert.addProperty(Utility.GetPropertyInfo("KeteranganB4",kuestioner.KeteranganB4,String.class));
            requestinsert.addProperty(Utility.GetPropertyInfo("KeteranganB5i",kuestioner.KeteranganB5i,String.class));
            requestinsert.addProperty(Utility.GetPropertyInfo("KeteranganB5ii",kuestioner.KeteranganB5ii,String.class));
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
		    		androidHttpTransport = new HttpTransportSE(URL_UPLOAD_IMAGE, Var.BASIC_TIMEOUT);
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
			
			if (dbProductMediaSafe != null)
				dbProductMediaSafe.close();
			
			if (cAgent != null)
				cAgent.close();
			
			if (dbAgent != null)
				dbAgent.close();
			
			if (cProductMain != null)
				cProductMain.close();
			
			if (cProductMediaSafe != null)
				cProductMediaSafe.close();
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
