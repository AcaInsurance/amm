package com.aca.amm;

import java.net.SocketTimeoutException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.http.conn.ConnectTimeoutException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.aca.amm.R;
import com.aca.amm.R.layout;
import com.aca.database.DBA_MASTER_AGENT;
import com.aca.database.DBA_PRODUCT_MAIN;

@SuppressLint("DefaultLocale")
public class SPPAActivity extends ControlListActivity {

	private static String NAMESPACE = "http://tempuri.org/";     
    private static String URL = Utility.getURL();      
    private static String SOAP_ACTION = "http://tempuri.org/GetListProductionAgent";     
    private static String METHOD_NAME = "GetListProductionAgent";
    
    private static String SOAP_ACTION_POLICY = "http://tempuri.org/GetPolicy";     
    private static String METHOD_NAME_POLICY = "GetPolicy";
    
    private boolean error = false;
    private String errorMessage = "";
    //private ListView lv;
    //private Bundle b;
    int count = 0;
    private SoapObject requestretrive = null;
    private SoapSerializationEnvelope envelope = null;
    private HttpTransportSE androidHttpTransport = null;

    private SoapObject requestretrivePolis = null;
    private SoapSerializationEnvelope envelopePolis = null;
    private HttpTransportSE androidHttpTransportPolis = null;
    private NumberFormat nf;
    
    private ProgressDialog progressBar;
    private ProgressDialog progressBarPolis;
    
    RetriveSPPAWS sc;
    RetrivePolicy pol;
	ArrayList<HashMap<String, String>> productionList; 
	ArrayList<HashMap<String, String>> polisList; 
	
	private Long SPPA_ID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sppa);
		
		nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		
		//b = new Bundle();
		
		DBA_MASTER_AGENT dba = null;
		Cursor c = null;
		
		try {
			dba = new DBA_MASTER_AGENT(getBaseContext());
			dba.open();
			c = dba.getRow();
			c.moveToFirst();
			
			sc = new RetriveSPPAWS();
	    	sc.execute(c.getString(1));	
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		finally{
			
			if (c!=null)
				c.close();
			
			if (dba!=null)
				dba.close();
		}
	}
	
	@Override
	public void onBackPressed() {
		Back();
	}
	
	private void Back(){
		Intent i = new Intent(getBaseContext(),  FirstActivity.class);
		startActivity(i);
		this.finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.sppa, menu);
		return true;
	}


	private class RetriveSPPAWS extends AsyncTask<String, Void, Void>{

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			progressBar = new ProgressDialog(SPPAActivity.this);
			progressBar.setCancelable(false);
			progressBar.setMessage("Please wait ...");
			progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressBar.show();
			
			envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); 
			envelope.implicitTypes = true;
	    	envelope.dotNet = true;
			androidHttpTransport = new HttpTransportSE(URL);
		}
		
		@Override
		protected Void doInBackground(String... params) {

			error = false;
			try{
				
				SoapObject table = null;                        // Contains table of dataset that returned through SoapObject
		        SoapObject tableRow = null;                     // Contains row of table
		        SoapObject responseBody = null;					// Contains XML content of dataset
				
				requestretrive = new SoapObject(NAMESPACE, METHOD_NAME);
		        requestretrive.addProperty(Utility.GetPropertyInfo("AgentCode", params[0], String.class));
		        requestretrive.addProperty(Utility.GetPropertyInfo("param", "", String.class));
		        
		    	envelope.setOutputSoapObject(requestretrive);
	    		envelope.bodyOut = requestretrive;
	    		
	    		androidHttpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
	    		
	    		androidHttpTransport.call(SOAP_ACTION, envelope);  
	            responseBody = (SoapObject) envelope.getResponse();
	            responseBody = (SoapObject) responseBody.getProperty(1);
	            
	            if (responseBody.getPropertyCount() == 0 ){
	            	error = true;
	            	errorMessage = "Data produksi tidak ditemukan";
	            	return null;
	            }
	            table = (SoapObject) responseBody.getProperty(0);
	            
	            productionList = new ArrayList<HashMap<String, String>>(); 
	            
	            int iTotalDataFromWebService = table.getPropertyCount();
	            for(int i = 0; i < iTotalDataFromWebService; i++)
	            {
	            	tableRow = (SoapObject) table.getProperty(i);
	            	Log.d("SPPA_NO", tableRow.getPropertySafelyAsString("SPPANo").toString()); 
	            	
	            	
		            HashMap<String, String> map = new HashMap<String, String>(); 
		            map.put("POLICY_NO", tableRow.getPropertySafelyAsString("PolicyNo").toString()); 
		            map.put("CUSTOMER_NAME", tableRow.getPropertySafelyAsString("CustomerName").toString()); 
		            map.put("SPPA_PRODUCT_ID", Utility.getProductNameBySPPAno(tableRow.getPropertySafelyAsString("SPPANo").toString())); 
		            map.put("EXP_DATE",  Utility.getFormatDateForSPPA(tableRow.getPropertySafelyAsString("ExpDate").toString())); 
		            
		            map.put("PREV_POLICY", tableRow.getPropertySafelyAsString("PrevPolicy").toString()); 
		            map.put("STATUS", tableRow.getPropertySafelyAsString("Status").toString()); 

		            productionList.add(map);
	            }
			}
			catch (Exception e) {
        		error = true;
        		e.printStackTrace();	        		
				errorMessage = new MasterExceptionClass(e).getException();
			}
			finally{
		        
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			try{				
				progressBar.hide();
				progressBar.dismiss();
				
				if(error){
					Utility.showCustomDialogInformation(SPPAActivity.this, "Informasi", 
							errorMessage);
				}else{
					ListAdapter adapter = new SimpleAdapter(SPPAActivity.this
	            			,productionList
	                        ,R.layout.list_item_production
	                        ,new String[] { "POLICY_NO", "CUSTOMER_NAME", "SPPA_PRODUCT_ID", 
							"EXP_DATE", "POLICY_NO"}
	            			,new int[] { R.id.txtProductionPolicyNo,
										R.id.txtProductionCustomerName ,
										R.id.txtProductionProduct ,
										R.id.txtProductionExpDate,
										R.id.btnSyncPolis})
					{
						@Override
						public void setViewText(TextView v, String text) {
							count ++ ;
							switch (v.getId()) {
								case R.id.txtProductionPolicyNo:
									super.setViewText(v, text.toUpperCase());
									break;
								default : super.setViewText(v, text);
							}
						
						}
						@Override
						public void setViewImage(ImageView i, String text) {
							switch(i.getId()) {
								case R.id.btnSyncPolis:	
									i.setOnClickListener(insertDB(text));
									break;
								default : 
									super.setViewImage(i, text);	
							}
						}
					};    
		            setListAdapter(adapter);
				}
				
			}catch (Exception e) {
        		error = true;
        		e.printStackTrace();	        		
				errorMessage = new MasterExceptionClass(e).getException();
			}
		}
	}
	
	private OnClickListener insertDB (final String noPolis){
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int pos = 0;
				
				for (int i = 0; i < getListAdapter().getCount(); i++)
                {	
                    if (productionList.get(i).get("POLICY_NO").equals(noPolis))
                    {
                    	pos = i ;
                    	break;
                    }
                }
				
				String prevPolisNo = productionList.get(pos).get("PREV_POLICY");
				String produkNama = productionList.get(pos).get("SPPA_PRODUCT_ID");
				String status = productionList.get(pos).get("STATUS");
				String dateExp = productionList.get(pos).get("EXP_DATE");
				String today = Utility.DateToString(Utility.GetToday(), "dd/MM/yyyy");
				
				
				Log.d("today", today);
				Log.d("date exp", dateExp);
				
				int resultExp = 0;
				
				resultExp = Utility.isExpired(dateExp, today);
				Log.d ("expired ga", String.valueOf(resultExp));
				
				Log.d("prev polis no", prevPolisNo);
				Log.d ("produk", produkNama);
				Log.d ("no polis", noPolis);
				
				if(produkNama.toLowerCase().contains("travel"))
				{
					Utility.showCustomDialogInformation(SPPAActivity.this, "Informasi", 
							"Produk travel tidak bisa di-renew");
					return ;
				}
				if (resultExp < 0)
				{
					Utility.showCustomDialogInformation(SPPAActivity.this, "Informasi", 
							"Polis sudah expired dan tidak bisa di-renew");
					return ;
				}
				if (status.toLowerCase().equals("a")) {
					if(prevPolisNo.equals("0000000000")) {
						
						DBA_PRODUCT_MAIN dba = null;
						Cursor c = null;
						try {
							Utility.PolisNumber(noPolis);
							
							dba = new DBA_PRODUCT_MAIN(getListView().getContext());
							dba.open();
							
							c = dba.getRowByPolicyNo(Utility.prevPolisNo);
							c.moveToFirst();
							
							if (c.getCount() == 0) {
								
								LockUnlockPolis lpol = null;
								boolean error = false;
								
								try {
									lpol =  new LockUnlockPolis(getListView().getContext(), noPolis, "1");
									lpol.execute();
									lpol.get(10000, TimeUnit.MILLISECONDS);
								} catch (ExecutionException e) {
									e.printStackTrace();
									error = true;
								} catch (TimeoutException e) {
									e.printStackTrace();
									error = true;
								}
								catch (Exception e) {
									e.printStackTrace();;
									error = true;
								}
							    finally {
								    if (!error && lpol.getResult().equals("OKE") && lpol != null)
									{
										pol = new RetrivePolicy();
										pol.execute(noPolis);	
									}
								    else {
								    	Utility.showCustomDialogInformation(SPPAActivity.this, "Informasi", 
												"Proses renewal gagal. Mohon informasikan ke IT ACA");
									}
								}							
							}
							else {
								Utility.showCustomDialogInformation(SPPAActivity.this, "Informasi", 
										"Polis ini sudah pernah direnew atau dalam proses renewal");
							}
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
						finally{
							
							if (c != null)
								c.close();
							
							if(dba != null)
								dba.close();
						}
					}
					else {
						Utility.showCustomDialogInformation(SPPAActivity.this, "Informasi", 
								"Polis expired atau tidak bisa direnewal");
					}
				}
				else {
					Utility.showCustomDialogInformation(SPPAActivity.this, "Informasi", 
							"Polis expired atau tidak bisa direnewal");				}
				
			}
		};
}

	private class RetrivePolicy extends AsyncTask<String, Void, Void>{
		
		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();
			progressBarPolis = new ProgressDialog(SPPAActivity.this);
			progressBarPolis.setCancelable(false);
			progressBarPolis.setMessage("Please wait ...");
			progressBarPolis.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressBarPolis.show();
			
			envelopePolis = new SoapSerializationEnvelope(SoapEnvelope.VER11); 
			envelopePolis.implicitTypes = true;
	    	envelopePolis.dotNet = true;	//used only if we use the webservic e from a dot net file (asmx)
			androidHttpTransportPolis = new HttpTransportSE(URL);
		}
		
		@Override
		protected Void doInBackground(String... params) {

			error = false;
			try{
				
				SoapObject table = null;                        // Contains table of dataset that returned through SoapObject
		        SoapObject tableRow = null;                     // Contains row of table
		        SoapObject responseBody = null;					// Contains XML content of dataset
				
		        Log.d("no polis", params[0]);

				requestretrivePolis = new SoapObject(NAMESPACE, METHOD_NAME_POLICY);
		        requestretrivePolis.addProperty(Utility.GetPropertyInfo("PolicyNo", params[0], String.class));
	    		
		    	envelopePolis.setOutputSoapObject(requestretrivePolis);
	    		envelopePolis.bodyOut = requestretrivePolis;

	    		androidHttpTransportPolis.call(SOAP_ACTION_POLICY, envelopePolis);  

	            responseBody = (SoapObject) envelopePolis.getResponse();
	            responseBody = (SoapObject) responseBody.getProperty(1);
	            
	            if( responseBody.getPropertyCount() == 0 ) {
	            	error = true;
	            	errorMessage = "Data produksi tidak ditemukan";
	            	return null;
	            	 
	            }

	            table = (SoapObject) responseBody.getProperty(0);
	            
	            polisList = new ArrayList<HashMap<String, String>>(); 
	            
	            for(int i = 0; i < 1; i++)
	            {
	            	tableRow = (SoapObject) table.getProperty(i);
	            	Log.d("WS", tableRow.getPropertySafelyAsString("SPPA_NO").toString()); 
	            	
		            HashMap<String, String> map = new HashMap<String, String>(); 
		        
		            map.put("SPPA_NO", tableRow.getPropertySafelyAsString("SPPA_NO").toString()); 
		            map.put("SPPA_AGENT_ID", tableRow.getPropertySafelyAsString("SPPA_AGENT_ID").toString()); 
		            map.put("SPPA_OFFICE_ID", tableRow.getPropertySafelyAsString("SPPA_OFFICE_ID").toString()); 
		            map.put("SPPA_PRODUCT_ID", tableRow.getPropertySafelyAsString("SPPA_PRODUCT_ID").toString()); 
		            map.put("SPPA_YEAR", tableRow.getPropertySafelyAsString("SPPA_YEAR").toString()); 
		            
		            map.put("SPPA_SEQUENCE", tableRow.getPropertySafelyAsString("SPPA_SEQUENCE").toString()); 
		            map.put("POLICY_NO", tableRow.getPropertySafelyAsString("POLICY_NO").toString()); 
		            map.put("PREV_POLICY", tableRow.getPropertySafelyAsString("PREV_POLICY").toString()); 
		            map.put("CUSTOMER_NO", tableRow.getPropertySafelyAsString("CUSTOMER_NO").toString()); 
		            map.put("CUSTOMER_NAME", tableRow.getPropertySafelyAsString("CUSTOMER_NAME").toString()); 
		         
		            map.put("TOTAL_SI", tableRow.getPropertySafelyAsString("TOTAL_SI").toString()); 
		            map.put("PREMIUM", "0.00"); 
		            map.put("CHARGE", "0.00"); 
		            map.put("STAMP", "0.00"); 
		            map.put("TOTAL_PREMIUM","0.00"); 
		            
		            map.put("EFF_DATE", tableRow.getPropertySafelyAsString("EXP_DATE").toString()); 
		            map.put("EXP_DATE", Utility.AddExpiredDate(tableRow.getPropertySafelyAsString("EXP_DATE").toString())); 
		      
		            polisList.add(map);
	            }
				

			}
			catch (Exception e) {
        		error = true;
        		e.printStackTrace();	        		
				errorMessage = new MasterExceptionClass(e).getException();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			try{
				progressBarPolis.hide();
				progressBarPolis.dismiss();
				
				if(error){
					Utility.showCustomDialogInformation(SPPAActivity.this, "Informasi", 
							errorMessage);	
				}else{
					savePolis();
				}
				
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
	
	@SuppressLint("DefaultLocale")
	private void savePolis () throws InterruptedException{
		String pro_name = Utility.getProductNameBySPPAno(polisList.get(0).get("SPPA_NO"));
		String nopolis = polisList.get(0).get("POLICY_NO").toString();
		
		Utility.PolisNumber(nopolis);
		
		try {
			saveDBAMain(pro_name.toUpperCase());
			
			if(pro_name.toUpperCase().equals("OTOMATE")) {
				LoadProductOtomate otomate = new LoadProductOtomate(getListView().getContext(), nopolis, SPPA_ID,polisList);
				otomate.execute();
			}		
			else if (pro_name.toUpperCase().equals("ASRI")){
				LoadProductAsri asri = new LoadProductAsri(getListView().getContext(), nopolis, SPPA_ID, polisList);
				asri.execute();
			}
			else if (pro_name.toUpperCase().equals("OTOMATESYARIAH")){
				LoadProductOtomateSyariah otomateSyariah = new LoadProductOtomateSyariah(getListView().getContext(), nopolis, SPPA_ID, polisList);
				otomateSyariah.execute();
			}
			else if (pro_name.toUpperCase().equals("ASRISYARIAH")){
				LoadProductAsriSyariah asriSyariah = new LoadProductAsriSyariah(getListView().getContext(), nopolis, SPPA_ID, polisList);
				asriSyariah.execute();
			}
			else if (pro_name.toUpperCase().equals("MEDISAFE")){
				ArrayList<HashMap<String, String>> familyList = new ArrayList<HashMap<String,String>>();
				ArrayList<HashMap<String, String>> familyList2 = new ArrayList<HashMap<String,String>>();
				ArrayList<HashMap<String, String>> familyList3 = new ArrayList<HashMap<String,String>>();
				ArrayList<HashMap<String, String>> familyList4 = new ArrayList<HashMap<String,String>>();
				ArrayList<HashMap<String, String>> familyList5 = new ArrayList<HashMap<String,String>>();
				
				familyList = getFamily(polisList.get(0).get("SPPA_NO"), "0");
				familyList2 = getFamily(polisList.get(0).get("SPPA_NO"), "1");
				familyList3 = getFamily(polisList.get(0).get("SPPA_NO"), "2");
				familyList4 = getFamily(polisList.get(0).get("SPPA_NO"), "3");
				familyList5 = getFamily(polisList.get(0).get("SPPA_NO"), "4");
				
						
				LoadProductMedisafe medisafe = new LoadProductMedisafe(getListView().getContext(), nopolis, SPPA_ID, polisList,
								familyList,
								familyList2,
								familyList3,
								familyList4,
								familyList5);
				
				medisafe.execute();
			}
			else if (pro_name.toUpperCase().equals("EXECUTIVESAFE")){
				ArrayList<HashMap<String, String>> beneList = new ArrayList<HashMap<String,String>>();
				ArrayList<HashMap<String, String>> beneList2 = new ArrayList<HashMap<String,String>>();
				ArrayList<HashMap<String, String>> beneList3 = new ArrayList<HashMap<String,String>>();
				
				beneList = getBene(polisList.get(0).get("SPPA_NO"), "1");
				beneList2 = getBene(polisList.get(0).get("SPPA_NO"), "2");
				beneList3 = getBene(polisList.get(0).get("SPPA_NO"), "3");
				
				ArrayList<HashMap<String, String>> familyList = new ArrayList<HashMap<String,String>>();
				ArrayList<HashMap<String, String>> familyList2 = new ArrayList<HashMap<String,String>>();
				ArrayList<HashMap<String, String>> familyList3 = new ArrayList<HashMap<String,String>>();
				ArrayList<HashMap<String, String>> familyList4 = new ArrayList<HashMap<String,String>>();
				ArrayList<HashMap<String, String>> familyList5 = new ArrayList<HashMap<String,String>>();
				
				familyList = getFamily(polisList.get(0).get("SPPA_NO"), "0");
				familyList2 = getFamily(polisList.get(0).get("SPPA_NO"), "1");
				familyList3 = getFamily(polisList.get(0).get("SPPA_NO"), "2");
				familyList4 = getFamily(polisList.get(0).get("SPPA_NO"), "3");
				familyList5 = getFamily(polisList.get(0).get("SPPA_NO"), "4");
				
				
						
				LoadProductExecutive executive = new LoadProductExecutive(getListView().getContext(), nopolis, SPPA_ID, polisList,
						beneList,
						beneList2,
						beneList3,
						
						familyList,
						familyList2,
						familyList3,
						familyList4,
						familyList5);
				
				executive.execute();
			}
			
			else if (pro_name.toUpperCase().equals("PAAMANAH")){
				ArrayList<HashMap<String, String>> beneList = new ArrayList<HashMap<String,String>>();
				ArrayList<HashMap<String, String>> beneList2 = new ArrayList<HashMap<String,String>>();
				ArrayList<HashMap<String, String>> beneList3 = new ArrayList<HashMap<String,String>>();
				
				beneList = getBene(polisList.get(0).get("SPPA_NO"), "1");
				beneList2 = getBene(polisList.get(0).get("SPPA_NO"), "2");
				beneList3 = getBene(polisList.get(0).get("SPPA_NO"), "3");
				
						
				LoadProductPAAmanah pa = new LoadProductPAAmanah(getListView().getContext(), nopolis, SPPA_ID, polisList,
												beneList,
												beneList2,
												beneList3);
				
				pa.execute();
			}
			else if (pro_name.toUpperCase().equals("TOKO")){
				LoadProductToko toko = new LoadProductToko(getListView().getContext(), nopolis, SPPA_ID, polisList);
				toko.execute();
			}
			else if (pro_name.toUpperCase().equals("TRAVELSAFE")){
				
				ArrayList<HashMap<String, String>> familyList2 = new ArrayList<HashMap<String,String>>();
				ArrayList<HashMap<String, String>> familyList3 = new ArrayList<HashMap<String,String>>();
				ArrayList<HashMap<String, String>> familyList4 = new ArrayList<HashMap<String,String>>();
				ArrayList<HashMap<String, String>> familyList5 = new ArrayList<HashMap<String,String>>();
				
				familyList2 = getFamily(polisList.get(0).get("SPPA_NO"), "1");
				familyList3 = getFamily(polisList.get(0).get("SPPA_NO"), "2");
				familyList4 = getFamily(polisList.get(0).get("SPPA_NO"), "3");
				familyList5 = getFamily(polisList.get(0).get("SPPA_NO"), "4");
				
				LoadProductTravelInt travelInt = new LoadProductTravelInt(getListView().getContext(), nopolis, SPPA_ID, 
						polisList,
						familyList2,
						familyList3,
						familyList4,
						familyList5);
				travelInt.execute();
			}
			else if (pro_name.toUpperCase().equals("TRAVELDOM")){
				LoadProductTravelDom travelDom = new LoadProductTravelDom(getListView().getContext(), nopolis, SPPA_ID,  polisList);
				travelDom.execute();
			}
		} catch (Exception e) {
    		error = true;
    		e.printStackTrace();	        		
			errorMessage = new MasterExceptionClass(e).getException();
		}
		finally 
		{
			if(error) {
				Toast.makeText(SPPAActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
				
				DBA_PRODUCT_MAIN dba = new DBA_PRODUCT_MAIN(getListView().getContext());
				dba.open();
				dba.delete(SPPA_ID);
				dba.close();
			}
			else
				Utility.showCustomDialogInformation(SPPAActivity.this, "Informasi", 
						"Tersimpan di bucket Incomplete");
			}
	
	}
	
	private ArrayList<HashMap<String, String>> getFamily (String sppa_no, String famkey) throws InterruptedException{
		
		LoadFamily fam = new LoadFamily(getListView().getContext(), sppa_no, famkey);
        fam.execute();
        
        try {
			fam.get(10000, TimeUnit.MILLISECONDS);
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
    	
    	return fam.getFamily();

	}
	private ArrayList<HashMap<String, String>> getBene (String sppa_no, String benekey) throws InterruptedException{
		
		LoadBene bene = new LoadBene(getListView().getContext(), sppa_no, benekey);
		bene.execute();
        
        try {
        	bene.get(10000, TimeUnit.MILLISECONDS);
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
    	
    	return bene.getBeneficiary();

	}
	
	
	private void saveDBAMain(String product_name){
		DBA_PRODUCT_MAIN dba = null;
		
		String today = Utility.DateToString(Utility.GetToday(), "dd/MM/yyyy");
		Log.w ("today date", today);
		try {

			dba = new DBA_PRODUCT_MAIN(getBaseContext());
			dba.open();
			
			SPPA_ID =  dba.initialInsert(
					polisList.get(0).get("CUSTOMER_NO").toString(), 
					polisList.get(0).get("CUSTOMER_NAME").toString(), 
					product_name,
					nf.parse(polisList.get(0).get("PREMIUM").toString()).doubleValue(),
					nf.parse(polisList.get(0).get("STAMP").toString()).doubleValue(),
					nf.parse(polisList.get(0).get("CHARGE").toString()).doubleValue(),
					nf.parse(polisList.get(0).get("TOTAL_PREMIUM").toString()).doubleValue(),
					Utility.getFormatDate(polisList.get(0).get("EFF_DATE").toString()),
					Utility.getFormatDate(polisList.get(0).get("EXP_DATE").toString()),
					today,
					Utility.prevPolisBranch,
					Utility.prevPolisYear,
					Utility.prevPolisNo,
					"R",
					Utility.prevPolisLOB, 0, 0
					);

			Log.d("SPPA ID", String.valueOf(SPPA_ID));
			Log.d("prev polis no final", Utility.prevPolisNo);
			
		}catch(Exception ex){
			ex.printStackTrace();
			
		}finally{
			if(dba != null)
				dba.close();
		}
	}
	
	
	
	public void btnHomeClick(View v)
	{
		Home();
	}
	
	private void Home(){
		Intent i = new Intent(getBaseContext(),  FirstActivity.class);
		startActivity(i);
		this.finish();
	}
}
