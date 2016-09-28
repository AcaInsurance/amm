package com.aca.amm;

import java.text.NumberFormat;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.aca.amm.R;
import com.aca.database.DBA_PRODUCT_MAIN;

import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SyncIncompleteActivity extends ControlListActivity {

	private DBA_PRODUCT_MAIN db;
	private Cursor c;
	
	private int[] mWordListItems;
	private String[] mWordListColumns;
	
	private ListView lv;
	
	private NumberFormat nf;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sync_incomplete);
		
		nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		
		lv = getListView();
		
		int[] ListItems = {R.id.txtSyncSPPANo, R.id.txtSyncProductName, R.id.txtSyncCustomerCode,  R.id.txtSyncCustomerName, R.id.txtSyncTotalAmount, R.id.btnDeleteIncomplete};
		mWordListItems = ListItems;
		
		String[] ListColumns = {db._id, db.PRODUCT_NAME, db.CUSTOMER_CODE,  db.CUSTOMER_NAME, db.TOTAL, db._id};
		mWordListColumns = ListColumns;
		
	
		BindListView();
	}
	
	
	private void BindListView() {
		
		SimpleCursorAdapter mCursorAdapter = null;
		
		try {
			
			db = new DBA_PRODUCT_MAIN(this);
			db.open();
			c = db.getRowsIncomplete();
			c.moveToFirst();
	
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			if (db != null)
				db.close();
		}
		
		mCursorAdapter = new SimpleCursorAdapter(this, R.layout.list_item_sync_incomplete, c, mWordListColumns, mWordListItems, 0) {
			
			@Override
			public void setViewText(TextView v, String text) {
				switch (v.getId()) {
					case R.id.txtSyncProductName:
				        super.setViewText(v, c.getString(3).toUpperCase());
				        break;  
					case R.id.txtSyncCustomerCode:
						super.setViewText(v, c.getString(1).toUpperCase());
					    break;
					case R.id.txtSyncCustomerName:
						super.setViewText(v, c.getString(2));
					    break;
					case R.id.txtSyncTotalAmount:
				        super.setViewText(v, nf.format(c.getDouble(9)));
				        break;        
					default : super.setViewText(v, text);
			    }
			}
			
			@Override
			public void setViewImage(ImageView i, String text) {
				String noPolis = c.getString(19) + c.getString(14) + c.getString(15) + c.getString(16);
				switch(i.getId()) {
					case R.id.btnDeleteIncomplete:
						i.setOnClickListener(deleteIncomplete(mContext, c.getString(0), noPolis));
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
					dba = new DBA_PRODUCT_MAIN(SyncIncompleteActivity.this);
					dba.open();
					
					cx = dba.getRow(arg3);
					cx.moveToFirst();
					
					startActivity(NextActivity(arg3, cx.getString(3), "EDIT",  cx.getString(1), cx.getString(2)));
					
				}catch(Exception ex){
					ex.printStackTrace();
				}finally{
					if (cx != null)
						cx.close();
					
					if (dba != null)
						dba.close();
				}
			}
		});
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
					try 
					{
						lpol = new LockUnlockPolis(ctx, noPolis, "0");
						
						lpol.execute();
						lpol.get(10000, TimeUnit.MILLISECONDS);
						
						if (lpol.getResult().equals("OKE") && lpol != null)
						{
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
					finally {
						if (dba != null)
							dba.close();
						
						if(error)
							Utility.showCustomDialogInformation(ctx, "Informasi", 
									"Data gagal dihapus");
						else{
							Utility.showCustomDialogInformation(ctx, "Informasi", 
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
	
	private OnClickListener deleteIncomplete(final Context ctx, final String rowId, final String noPolis)
	{
		return new OnClickListener() {

			@Override
			public void onClick(View v) {
				showConfirmDelete(ctx, rowId, noPolis).show();
			}
		};
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.sync_incomplete, menu);
		return true;
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
	public void onBackPressed() {
		Back();
	}
	
	private void Back(){
		Intent i = new Intent(getBaseContext(),  FirstActivity.class);
		startActivity(i);
		this.finish();
	}
}
