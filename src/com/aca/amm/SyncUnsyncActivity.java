package com.aca.amm;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.aca.amm.CheckVehicleWS.CheckVehicle;
import com.aca.dal.Scalar;
import com.aca.database.DBA_MASTER_AGENT;
import com.aca.database.DBA_PRODUCT_MAIN;
import com.aca.database.DBA_PRODUCT_OTOMATE;
import com.aca.database.DBA_PRODUCT_OTOMATE_SYARIAH;
import com.aca.dbflow.VersionAndroid;
import com.aca.util.UtilDate;
import com.raizlabs.android.dbflow.sql.language.Select;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import org.joda.time.LocalDate;

public class SyncUnsyncActivity extends ControlListActivity implements InterfaceCustomer, CheckVehicle {

	private DBA_PRODUCT_MAIN db;
	private Cursor c;
	private int[] mWordListItems;
	private String[] mWordListColumns;
	private ListView lv;
	private NumberFormat nf;
	private long sppaID = 0;
	protected String kodeproduk;
    private Toast toast;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sync_unsync);
		
		nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		
		lv = getListView();
		
		int[] ListItems = {R.id.txtSyncSPPANo, R.id.txtSyncProductName, R.id.txtSyncCustomerCode, R.id.txtSyncCustomerName, R.id.txtSyncTotalAmount, R.id.btnSyncSPPA, R.id.btnPhotoTake, R.id.btnDeleteSync};
		mWordListItems = ListItems;
		
		String[] ListColumns = {db._id, db.PRODUCT_NAME, db.CUSTOMER_CODE, db.CUSTOMER_NAME, db.TOTAL, db.CUSTOMER_CODE, db._id, db._id};
		mWordListColumns = ListColumns;
		
		BindListView();
	}

	public void BindListView() {
		
		SimpleCursorAdapter mCursorAdapter = null;
		
		try {
			
			db = new DBA_PRODUCT_MAIN(this);
			db.open();
			
			c = db.getRowsUnSynch();
			c.moveToFirst();
			
			mCursorAdapter = new SimpleCursorAdapter(this, R.layout.list_item_sync_unsync, c, mWordListColumns, mWordListItems, 0){
				
				@SuppressLint("DefaultLocale")
				@Override
				public void setViewText(TextView v, String text) {

					switch (v.getId()) {
						case R.id.txtSyncProductName:
                            String productName = Scalar.getProdukName(SyncUnsyncActivity.this, c.getString(3), c.getLong(0)).toUpperCase();
                            super.setViewText(v, productName);
					        break;  
						case R.id.txtSyncCustomerCode:
							super.setViewText(v, c.getString(1).toUpperCase());
						    break;
						case R.id.txtSyncCustomerName:
							super.setViewText(v, c.getString(2).toUpperCase());
						    break;
						case R.id.txtSyncTotalAmount:
					        super.setViewText(v, nf.format((c.getDouble(9))));
					        break;        
						default : super.setViewText(v, text);
				    }
				}
				
				@Override
				public void setViewImage(ImageView i, String text) {
					
					String noPolis = c.getString(19) + c.getString(14) + c.getString(15) + c.getString(16);

					switch(i.getId()) {
						case R.id.btnSyncSPPA:
							i.setOnClickListener(syncSPPA(mContext, c.getLong(0), c.getString(3), c.getString(1)));
							break;
						case R.id.btnPhotoTake:
							i.setOnClickListener(uploadNewPhoto(mContext, c.getLong(0), c.getString(3)));
							break;
						case R.id.btnDeleteSync:
							i.setOnClickListener(deleteSync(mContext, c.getString(0), noPolis));
							break;
						default : 
							super.setViewImage(i, text);	
					}
				}
			}; 

			lv.setAdapter(mCursorAdapter);			
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

					DBA_PRODUCT_MAIN dba = null;
					Cursor cx = null;
					
					try{
						dba = new DBA_PRODUCT_MAIN(SyncUnsyncActivity.this);
						dba.open();
						
						cx = dba.getRow(arg3);
						cx.moveToFirst();
						
						startActivity(NextActivity(arg3, cx.getString(3), "VIEW",  cx.getString(1), cx.getString(2)));
						
					}catch(Exception ex){
						ex.printStackTrace();
					}finally{
						if (cx != null)
							cx.close();
						
						if (dba!= null)
							dba.close();
					}
				}
			});
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			if (db != null)
				db.close();
		}
	}
	
	private OnClickListener uploadNewPhoto(final Context ctx, final long id, final String pro_name) {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getBaseContext(),  UploadPhotoActivity.class);
				Bundle b = new Bundle();
				b.putString("PRODUCT_TYPE", pro_name);
				b.putLong("SPPA_ID", id);
				b.putString("PRODUCT_ACTION", "VIEW");
				
				i.putExtras(b);
				startActivity(i);
				SyncUnsyncActivity.this.finish();
			}
		};
	}
	
	private OnClickListener deleteSync(final Context ctx, final String rowId, final String noPolis) {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				showConfirmDelete(ctx, rowId, noPolis).show();
			}
		};
	}
	
	private OnClickListener syncSPPA(final Context ctx, final long i, final String pro_name, final String cust_no) {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				showConfirmSync(ctx, i, pro_name, cust_no).show();
			}
		};
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.sync_unsync, menu);
		return true;
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
	
	private AlertDialog showConfirmDelete(final Context ctx, final String rowId, final String noPolis) {
	    AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
	        .setTitle("Hapus") 
	        .setMessage("Hapus data ini sekarang?") 
	        .setIcon(R.drawable.delete)
	        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

	            public void onClick(DialogInterface dialog, int whichButton) { 
	            	
	            	boolean error = false;
					DBA_PRODUCT_MAIN dba = null;
					LockUnlockPolis lpol = null;
					
					try {
						lpol = new LockUnlockPolis(ctx, noPolis, "0");
						
						lpol.execute();
						lpol.get(10000, TimeUnit.MILLISECONDS);
						
						if (lpol != null && lpol.getResult().equals("OKE")); {
						    dba = new DBA_PRODUCT_MAIN(ctx);
							dba.open();
							dba.delete(Long.parseLong(rowId));
							Utility.DeleteDirectory(Long.parseLong(rowId));
						}
					}
					catch (ExecutionException e) {
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
					finally 
					{
						if (dba != null)
							dba.close();
						
						if(error)
							Utility.showCustomDialogInformation(SyncUnsyncActivity.this, "Informasi", 
									"Data gagal dihapus");
						else {
							Utility.showCustomDialogInformation(SyncUnsyncActivity.this, "Informasi", 
									"Data berhasil dihapus");
							BindListView();
						}
					}
					
	                dialog.dismiss();
	            }   
	        })
	        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	                dialog.dismiss();
	            }
	        })
	        .create();
	        return myQuittingDialogBox;	
	}
	
	private AlertDialog showConfirmSync(final Context ctx, final long i, final String pro_name, final String  customerNo) {
	    AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
	        .setTitle("Submit ke Server") 
	        .setMessage("Anda ingin submit data ini ke server sekarang? ") 
	        .setIcon(R.drawable.syncsppa)
	        .setPositiveButton("Synchronize", new DialogInterface.OnClickListener() {

	            @SuppressLint("DefaultLocale")
				public void onClick(DialogInterface dialog, int whichButton) {  
	            	 sppaID = i;
	            	 
	            	try {
		            	if(pro_name.toUpperCase().equals("ASRI")){
							SLProductASRI asri = new SLProductASRI(ctx, sppaID, SyncUnsyncActivity.this);
							asri.execute();
						}
						else if(pro_name.toUpperCase().equals("OTOMATE")) {
                            kodeproduk = Scalar.getProdukKode(SyncUnsyncActivity.this, sppaID);

                            if (!Scalar.isNewSppa(SyncUnsyncActivity.this, i)) {
                                toast = Toast.makeText(SyncUnsyncActivity.this, "Proses sinkronisasi tidak bisa diproses untuk sppa otomate yang dibuat pada versi aplikasi sebelumnya", Toast.LENGTH_SHORT);
                                toast.show();
                                return;
                            }

                            HashMap<String, String> map = getData(sppaID, kodeproduk, pro_name);
                            CheckVehicleWS ws = new CheckVehicleWS(ctx, map);
                            ws.mCallBack = SyncUnsyncActivity.this;
                            ws.execute();

                         /*   if (needSync()) {
                                Intent i = new Intent(SyncUnsyncActivity.this, FillOtomateActivity.class);
                                Bundle b = new Bundle();

                                b.putString("PRODUCT_ACTION", "SYNC");
                                b.putLong("SPPA_ID", sppaID);
                                i.putExtras(b);
                                startActivityForResult(i, 100);
                            }
                            else {
                                HashMap<String, String> map = getData(sppaID, kodeproduk);
                                CheckVehicleWS ws = new CheckVehicleWS(ctx, map);
                                ws.mCallBack = SyncUnsyncActivity.this;
                                ws.execute();
                            }
						}*/
                        }
						else if(pro_name.toUpperCase().equals("ASRISYARIAH")){
							SLProductASRISyariah asri_syariah = new SLProductASRISyariah(ctx,sppaID, SyncUnsyncActivity.this);
							asri_syariah.execute();
						}
						else if(pro_name.toUpperCase().equals("OTOMATESYARIAH")){
							kodeproduk = "09";

                            if (!Scalar.isNewSppa(SyncUnsyncActivity.this, i)) {
                                toast = Toast.makeText(SyncUnsyncActivity.this, "Proses sinkronisasi tidak bisa diproses untuk sppa otomate yang dibuat pada versi aplikasi sebelumnya", Toast.LENGTH_SHORT);
                                toast.show();
                                return;
                            }
							
							HashMap<String, String> map = getData(sppaID, kodeproduk, pro_name);
							CheckVehicleWS ws = new CheckVehicleWS(ctx, map);
							ws.mCallBack = SyncUnsyncActivity.this;
							ws.execute();
							
						
						}
						else if(pro_name.toUpperCase().equals("TRAVELSAFE")){
							SLProductTravel travel = new SLProductTravel(ctx, sppaID, SyncUnsyncActivity.this);
							travel.execute();
						}
						else if(pro_name.toUpperCase().equals("TRAVELDOM")){
							SLProductTravelDom traveldom = new SLProductTravelDom(ctx, sppaID, SyncUnsyncActivity.this);
							traveldom.execute();
						}
						else if(pro_name.toUpperCase().equals("MEDISAFE")){
							SLProductMediaSafe mediasafe = new SLProductMediaSafe(ctx,sppaID, SyncUnsyncActivity.this);
							mediasafe.execute();
						}
						else if(pro_name.toUpperCase().equals("EXECUTIVESAFE")){
							SLProductExecutive executive = new SLProductExecutive(ctx, sppaID, SyncUnsyncActivity.this);
							executive.execute();
						}
						else if(pro_name.toUpperCase().equals("TOKO")){
							SLProductToko toko = new SLProductToko(ctx,sppaID, SyncUnsyncActivity.this);
							toko.execute();
						}
		            	
						else if(pro_name.toUpperCase().equals("PAAMANAH")){
							SLProductPAAMANAH paamanah = new SLProductPAAMANAH(ctx, sppaID, SyncUnsyncActivity.this);
							paamanah.execute();
						}
						else if(pro_name.toUpperCase().equals("ACAMOBIL")){
							SLProductACAMobil acamobil = new SLProductACAMobil(ctx,sppaID, SyncUnsyncActivity.this);
							acamobil.execute();
						}
						else if(pro_name.toUpperCase().equals("CARGO")){
							SLProductCargo cargo = new SLProductCargo(ctx, sppaID, SyncUnsyncActivity.this);
							cargo.execute();
						}
						else if(pro_name.toUpperCase().equals("WELLWOMAN")){
							doSaveWellWoman(customerNo);						
						}
						else if(pro_name.toUpperCase().equals("DNO")){
							SLProductDNO dno = new SLProductDNO(ctx, sppaID, SyncUnsyncActivity.this);
							dno.execute();	
						}
						else if(pro_name.toUpperCase().equals("KONVENSIONAL")){
							SLProductKonvensional konve = new SLProductKonvensional(ctx, sppaID, SyncUnsyncActivity.this);
							konve.execute();	
						}
		            	
	            	}
	            	finally {
						BindListView();
		                dialog.dismiss();	            		
	            	}
					
	            }   

	        })
	        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	                dialog.dismiss();
	            }
	        })
	        .create();
	        return myQuittingDialogBox;	
	}

    private boolean needSync() {
        try {
            VersionAndroid versionAndroid = new Select().from(VersionAndroid.class).querySingle();

            DBA_PRODUCT_MAIN dbMain = new DBA_PRODUCT_MAIN(SyncUnsyncActivity.this);
            dbMain.open();
            Cursor cMain = dbMain.getRow(sppaID);
            if(cMain.moveToFirst()) {
                String entryDate = cMain.getString(cMain.getColumnIndex(DBA_PRODUCT_MAIN.ENTRY_DATE));
                if (TextUtils.isEmpty(entryDate)) {
                    return false;
                }
                LocalDate mEntryDate = UtilDate.toDate(entryDate, UtilDate.STANDARD_DATE);
                dbMain.close();
                if (versionAndroid.Version >= 36 && mEntryDate.isBefore(UtilDate.toDate(versionAndroid.DateTime)) ){
                    return true;
                }
            }
            dbMain.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK) {
            HashMap<String, String> map = getData(sppaID, kodeproduk, "OTOMATE");
            CheckVehicleWS ws = new CheckVehicleWS(SyncUnsyncActivity.this, map);
            ws.mCallBack = SyncUnsyncActivity.this;
            ws.execute();
        }
    }

    @SuppressWarnings("finally")
	protected HashMap<String, String> getData(long SPPA_ID, String kodeproduk, String productName) {
		
		DBA_PRODUCT_MAIN dbProductMain = null;
		DBA_PRODUCT_OTOMATE dbProductOtomate = null;
		DBA_PRODUCT_OTOMATE_SYARIAH dbProductOtomateSyariah = null;
		DBA_MASTER_AGENT dbAgent = null;
		
		Cursor cProductMain = null;
		Cursor cProductOtomate = null;
		Cursor cAgent 	= null;
		
		HashMap<String, String> map = null;
		
		try{
			
			dbProductMain = new DBA_PRODUCT_MAIN(this);
			dbProductMain.open();
			cProductMain = dbProductMain.getRow(SPPA_ID);
			cProductMain.moveToFirst();

			dbAgent = new DBA_MASTER_AGENT(this);
			dbAgent.open();
			cAgent = dbAgent.getRow();
			cAgent.moveToFirst();

            /*dbProductOtomate = new DBA_PRODUCT_OTOMATE(this);
            dbProductOtomate.open();
            cProductOtomate = dbProductOtomate.getRow(SPPA_ID);
            cProductOtomate.moveToFirst();
			*/
			if (productName.equalsIgnoreCase("OTOMATE")) {
				dbProductOtomate = new DBA_PRODUCT_OTOMATE(this);
				dbProductOtomate.open();	
				cProductOtomate = dbProductOtomate.getRow(SPPA_ID);
				cProductOtomate.moveToFirst();
			}
			else {
				dbProductOtomateSyariah = new DBA_PRODUCT_OTOMATE_SYARIAH(this);
				dbProductOtomateSyariah.open();
				cProductOtomate = dbProductOtomateSyariah.getRow(SPPA_ID);
				cProductOtomate.moveToFirst();
			}
				
			
			map = new HashMap<String, String>();
			map.put("prodId", kodeproduk);
			map.put("effDate", cProductMain.getString(12));
			map.put("ChassisNo", cProductOtomate.getString(9));
			map.put("EngineNo", cProductOtomate.getString(10));
			map.put("PlatNo1", cProductOtomate.getString(5));
			map.put("PlatNo2", cProductOtomate.getString(6));
			map.put("PlatNo3", cProductOtomate.getString(7));
			map.put("BranchNo", cAgent.getString(0));
			
			
			
			
		}
		catch (Exception ex) {
			ex.printStackTrace();
			map = null;
		}
		finally {
			dbProductMain.close();
			
			if (dbProductOtomate != null) {
				dbProductOtomate.close();
			}
			if (dbProductOtomateSyariah != null) {
				dbProductOtomateSyariah.close();
			}
			
			cProductMain.close();
			cProductOtomate.close();
			
			return map;
		}
	}

	private void doSaveWellWoman (String customerNo) {
		//get data customer for ktp
		
		RetrieveCustomer retrieveCustomer = new RetrieveCustomer(SyncUnsyncActivity.this, customerNo);
		retrieveCustomer.customerInterface = this;
		retrieveCustomer.execute();
	}
	

	@Override
	public void getCustomerList(ArrayList<HashMap<String, String>> custList) {
		String noktp = "";
		
		if (custList != null) {
			noktp = custList.get(0).get("CUSTOMER_ID").toString();
			CheckCustomerReject checkCustomerReject = new CheckCustomerReject(SyncUnsyncActivity.this, noktp);
			checkCustomerReject.execute();
		}
		else {
			Utility.showCustomDialogInformation(this, "Informasi", "Gagal mendapatkan data customer");
		}
	}
	
	
	protected void gotoSaveWellWoman () {
		SLProductWellWoman wellwoman = new SLProductWellWoman(getListView().getContext(), sppaID, SyncUnsyncActivity.this);
		wellwoman.execute();
	}
	
	
	private Intent NextActivity(long id, String act, String action, String cust_code, String cust_name)
	{
		Intent i = null;
		Bundle b = new Bundle();

		b.putString("PRODUCT_ACTION", action);
		b.putLong("SPPA_ID", id);
		b.putString("PRODUCT_TYPE", act);
		
		try{
			if(act.equalsIgnoreCase("OTOMATE")){		
				i = new Intent(getBaseContext(),  FillOtomateActivity.class);
				i.putExtras(b);
				this.finish();
			}
			else if(act.equalsIgnoreCase("TRAVELDOM")){
				i = new Intent(getBaseContext(),  FillTravelDomActivity.class);
				i.putExtras(b);
				this.finish();
			}
			else if(act.equalsIgnoreCase("TRAVELSAFE")){
				i = new Intent(getBaseContext(),  FillTravelActivity.class);
				i.putExtras(b);
				this.finish();
			}
			else if(act.equalsIgnoreCase("TOKO")){
				i = new Intent(getBaseContext(),  FillTokoActivity.class);
				i.putExtras(b);
				this.finish();
			}
			else if(act.equalsIgnoreCase("MEDISAFE")){
				i = new Intent(getBaseContext(),  FillMedisafe.class);
				i.putExtras(b);
				this.finish();
			}
			else if(act.equalsIgnoreCase("ASRI")){
				i = new Intent(getBaseContext(),  FillAsriActivity.class);
				i.putExtras(b);
				this.finish();
			}
			else if(act.equalsIgnoreCase("ASRISYARIAH")){
				i = new Intent(getBaseContext(),  FillAsriSyariahActivity.class);
				i.putExtras(b);
				this.finish();
			}
			else if(act.equalsIgnoreCase("OTOMATESYARIAH")){
				i = new Intent(getBaseContext(),  FillOtomateSyariahActivity.class);
				i.putExtras(b);
				this.finish();
			}
			else if(act.equalsIgnoreCase("EXECUTIVESAFE")){
				i = new Intent(getBaseContext(),  FillExecutiveActivity.class);
				i.putExtras(b);
				this.finish();
			}
			else if(act.equalsIgnoreCase("PAAMANAH")){
				i = new Intent(getBaseContext(),  FillPAAmanahActivity.class);
				i.putExtras(b);
				this.finish();
			}
			else if(act.equalsIgnoreCase("ACAMOBIL")){
				i = new Intent(getBaseContext(),  FillACAMobilActivity.class);
				i.putExtras(b);
				this.finish();
			}
			else if(act.equalsIgnoreCase("CARGO")){
				i = new Intent(getBaseContext(),  FillCargoActivity.class);
				i.putExtras(b);
				this.finish();
			}
			else if(act.equalsIgnoreCase("WELLWOMAN")){
				i = new Intent(getBaseContext(),  FillWellWomanActivity.class);
				i.putExtras(b);
				this.finish();
			}
			else if(act.equalsIgnoreCase("DNO")){
				i = new Intent(getBaseContext(),  FillDNOActivity.class);
				i.putExtras(b);
				this.finish();
			}
			else if(act.equalsIgnoreCase("KONVENSIONAL")){
				i = new Intent(getBaseContext(),  FillKonvensionalActivity.class);
				b.putString("TIPE_KONVENSIONAL", "");
				i.putExtras(b);
				this.finish();
			}
			
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		
		return i;
	}

	@Override
	public void CheckVehicleListener(Boolean result) {
		if (result) {
            SLProductOtomate otomate = new SLProductOtomate(this ,sppaID , SyncUnsyncActivity.this);
            otomate.execute();


            /*
			if (kodeproduk.equals("03")) {
				SLProductOtomate otomate = new SLProductOtomate(this ,sppaID , SyncUnsyncActivity.this);
				otomate.execute();
			}
			else {
				SLProductOtomateSyariah otomate_syariah = new SLProductOtomateSyariah(this, sppaID, SyncUnsyncActivity.this);
				otomate_syariah.execute();				
			}			*/
		}
		else {
			
		}
		
	}


}
