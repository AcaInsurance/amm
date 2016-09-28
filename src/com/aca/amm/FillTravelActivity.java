package com.aca.amm;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.kobjects.util.Util;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import com.aca.amm.R;
import com.aca.database.DBA_INSURED_GROUP;
import com.aca.database.DBA_MASTER_TRAVELSAFE_INT_RATE;
import com.aca.database.DBA_PRODUCT_MAIN;
import com.aca.database.DBA_PRODUCT_TRAVEL_SAFE;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window; 
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

public class FillTravelActivity extends ControlNormalActivity implements InterfaceCustomer{

	private ArrayList<HashMap<String, String>> custList;
	private Context context = null;
	private Bundle b;
	private NumberFormat nf;
	private String PRODUCT_ACTION, KODE_NEGARA, NAMA_NEGARA;
	private long SPPA_ID;
	private int flag = 0;
	private Calendar c;
	
	private ArrayList<HashMap<String, String>> premiResult; 
	
	private static String NAMESPACE = "http://tempuri.org/";     
    private static String URL = Utility.getURL();     
    private static String SOAP_ACTION_EXRATE = "http://tempuri.org/GetExchangeRateTS";     
    private static String METHOD_NAME_EXRATE = "GetExchangeRateTS";
    
    private ProgressDialog progressBar;
    private SoapObject requestretrive = null;
    private SoapSerializationEnvelope envelope = null;
    private HttpTransportSE androidHttpTransport = null;
    private boolean error = false;
    private String errorMessage = "";
    private RetrivePremi sc;
    
    private double  v_premi = 0,
		    		v_polis = 0,
		    		v_materai = 0,
		    		v_total = 0,
		    		v_discpct = 0, 
		    		v_disc = 0;
		 	
    private double v_exchange_rate = 1,
    		vMaxBenefit = 0,
    		vPremiDays = 0,
    		vPremiWeeks = 0, 
    		vLoadingPremi = 0;
 	
    private double  v_premi_pasangan = 0,
    		v_premi_anak_1 = 0, 
    		v_premi_anak_2 = 0,
    		v_premi_anak_3 = 0;
    
    private int vTotalDays,
    			vTotalWeeks;
    
    private String vCCOD = "",
    			   vDCOD = "0";
    
	private LinearLayout  famLayout, 
						  gruplayout;
	
	private LinearLayout insured1,
						insured2,
						insured3,
						insured4,
						insured5, 
						insured6,
						insured7,
						insured8,
						insured9,
						insured10,
						insured11,
						insured12,
						insured13,
						insured14,
						insured15;
	
	
	private ImageView   btnDelNegara1,
					 	btnDelNegara2,
					 	btnDelNegara3,
					 	btnDelNegara4,
					 	btnDelNegara5,
					 	btnDelNegara6;
	
	private Button btnAdd, btnRemove, btnHide;
	private Button btnInputInsuredGroup;
	private Spinner spnINSURED_COUNT;
	
	private EditText jumlahTertanggung, namaGrupTour;
 
	private static final int MAX_INSURED_GROUP = 10;
	private static long PRODUCT_MAIN_ID = -1;
	private static int INSURED_COUNT = 0;
	private double premi_tertanggung_grup = 0;
	
	private EditText namanegara1,
					namanegara2,
					namanegara3,
					namanegara4,
					namanegara5,
					namanegara6;
	
	private EditText hariKepergian, etFromDate, etToDate, etPasangan, etAnak1, etAnak2, etAnak3, customerno, customername, nopass;
	private Spinner spTujuan; 
	private Switch pasangan, anak1, anak2, anak3, annual;
	
	private static final int DATE_FROM_ID = 99;
	private static final int DATE_TO_ID = 100;
	private static final int DATE_LAHIR_PASANGAN = 101;
	private static final int DATE_LAHIR_ANAK1 = 102;
	private static final int DATE_LAHIR_ANAK2 = 103;
	private static final int DATE_LAHIR_ANAK3 = 104;
	
	private EditText namapasangan, nopasspasangan, namaanak1, nopassanak1, namaanak2, nopassanak2, namaanak3, nopassanak3;
	private EditText namaahliwaris, hub;
	private EditText jumlahhari, tambahan, loadingpremi, premi, polis, materai, total, discpct, disc;
	private RadioButton fam, individu, grup, asia, nonasia, vip, duluxe, superior, executive, schengen;
	private RadioGroup radioGroup, rbgPlan, rbgNegara;

	private String UIErrorMessage = "Lengkapi semua data";
	private double MAX_DISCOUNT = 0; 
	
	boolean exit = false, 
			fingerDown = false;
	
	ScrollView scrollView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_fill_travel);

		context = FillTravelActivity.this;
		
		famLayout = (LinearLayout)findViewById(R.id.layoutFamily); 
		gruplayout = (LinearLayout)findViewById(R.id.layoutGroup);
		
		nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);

	    radioGroup = (RadioGroup) findViewById(R.id.rbgIndividuOrFamily);        
	    rbgPlan = (RadioGroup) findViewById(R.id.rbgPlan);
	    rbgNegara = (RadioGroup) findViewById(R.id.rbgNegaraTujuan);
	    
	    spTujuan = (Spinner)findViewById(R.id.spinner1);
		Utility.BindTujuanPerjalanan(spTujuan, getBaseContext());
		
		
		scrollView = (ScrollView)findViewById(R.id.scrollView1);
		
		InitControls();
		RegisterListener();
		initColor();
		try{
			
			Intent i = getIntent();
			b = i.getExtras();
			
			PRODUCT_ACTION = b.getString("PRODUCT_ACTION");
		
			
			if (PRODUCT_ACTION.equals("EDIT") || PRODUCT_ACTION.equals("VIEW")) {
				SPPA_ID = b.getLong("SPPA_ID");
				LoadDB();
			}
//			else if (PRODUCT_ACTION.equals("SELECT_COUNTRY"))
//			{
//				//ambil data di Bundle
//				if (KODE_NEGARA != null && NAMA_NEGARA != null)
//				{
//					kodenegara.setText(KODE_NEGARA);
//					namanegara.setText(NAMA_NEGARA);
//				}
//			}
			else if (PRODUCT_ACTION.equals("VIEW.UNPAID")) {
				SPPA_ID = b.getLong("SPPA_ID");
				LoadDB();
				disableView();
			}
			
			else {
				findViewById(R.id.btnDelete)
				.setVisibility(View.GONE);
			}
			
			if (b.getString("CUSTOMER_NO") != null) {
				customerno.setText(b.getString("CUSTOMER_NO"));
				customername.setText(b.getString("CUSTOMER_NAME"));
				getCustomerData();
			}
			else if(!customerno.getText().toString().isEmpty())
				getCustomerData();
			
			MAX_DISCOUNT = Utility.getMaxDiscount(getBaseContext(), "29");
			
			if (Utility.getIsDiscountable(getBaseContext(), "07").equals("0"))
			{
				disc.setEnabled(false);
				discpct.setEnabled(false);
			}
			
		
		}
		catch(Exception e){	
			e.printStackTrace();
		}
		
//		startScrollingThread();
		
	}
	
	//Need handler for callbacks to the UI thread
	final Handler mHandler = new Handler();
	//Create runnable for posting
	
	final Runnable mUpdateResults = new Runnable() {
	
		public void run() {
		updateUI();
		}
	};
	
	private void updateUI() {
		if(!exit && !fingerDown) {
	
			
			if(scrollView.getScrollY() >= (scrollView.getChildAt(0).getHeight()-scrollView.getMeasuredHeight())) {
					scrollView.smoothScrollTo(0,0);
			}else {
					scrollView.smoothScrollTo(0,scrollView.getScrollY()+1);
			}
		}
	}
	protected void startScrollingThread() {
		// Fire off a thread to do some work that we shouldn't do directly in the UI thread
		Thread t = new Thread() {
			public void run() {
				while(!exit) {
				try {
					Thread.sleep(30);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				mHandler.post(mUpdateResults);
				}
			}
		};
		t.start();
	}
	
	//===========================
	
	private void disableView() {
		// TODO Auto-generated method stub
		nopass.setEnabled(false);
		
	    namapasangan.setEnabled(false);
	    namaanak1.setEnabled(false);
	    namaanak2.setEnabled(false);
	    namaanak3.setEnabled(false);
	    nopasspasangan.setEnabled(false);
	    nopassanak1.setEnabled(false);
	    nopassanak2.setEnabled(false);
	    nopassanak3.setEnabled(false);
	    
	    namaahliwaris.setEnabled(false);
	    hub.setEnabled(false);
	    
	    namanegara1.setEnabled(false); 

	    jumlahhari.setEnabled(false);
	    tambahan.setEnabled(false);
	    loadingpremi.setEnabled(false);
	    premi.setEnabled(false);
	    materai.setEnabled(false);
	    polis.setEnabled(false);
	    total.setEnabled(false);
	    discpct.setEnabled(false);
	    disc.setEnabled(false);
	    
	    fam.setEnabled(false);
	    
	    individu.setEnabled(false);
	    asia.setEnabled(false);
	    nonasia.setEnabled(false);
	    schengen.setEnabled(false);

	    vip.setEnabled(false);	    
	    executive.setEnabled(false);	    
	    duluxe.setEnabled(false);	    
	    superior.setEnabled(false);
	    
	    pasangan.setEnabled(false);
	    customerno.setEnabled(false);
	    customername.setEnabled(false);

	    etFromDate.setEnabled(false);
	    etToDate .setEnabled(false);
	    etPasangan.setEnabled(false);	    
	    etAnak1.setEnabled(false);	    
	    etAnak2.setEnabled(false);	    
	    etAnak3.setEnabled(false);
	    
	    spTujuan.setEnabled(false);
	    annual.setEnabled(false);
	    
	    (findViewById(R.id.btnChooseCustomer)).setEnabled(false);
	    
//		change text color 
	    
	    
	    
	    namapasangan.setTextColor(Color.BLACK);
	    namaanak1.setTextColor(Color.BLACK);
	    namaanak2.setTextColor(Color.BLACK);
	    namaanak3.setTextColor(Color.BLACK);
	    nopasspasangan.setTextColor(Color.BLACK);
	    nopassanak1.setTextColor(Color.BLACK);
	    nopassanak2.setTextColor(Color.BLACK);
	    nopassanak3.setTextColor(Color.BLACK);
	    
	    namaahliwaris.setTextColor(Color.BLACK);
	    hub.setTextColor(Color.BLACK);
	    
	    namanegara1.setTextColor(Color.BLACK);

	    jumlahhari.setTextColor(Color.BLACK);
	    tambahan.setTextColor(Color.BLACK);
	    loadingpremi.setTextColor(Color.BLACK);
	    premi.setTextColor(Color.BLACK);
	    materai.setTextColor(Color.BLACK);
	    polis.setTextColor(Color.BLACK);
	    total.setTextColor(Color.BLACK);
	    discpct.setTextColor(Color.BLACK);
	    disc.setTextColor(Color.BLACK);
	    

	    pasangan.setTextColor(Color.BLACK);
	    customerno.setTextColor(Color.BLACK);
	    customername.setTextColor(Color.BLACK);

	    etFromDate.setTextColor(Color.BLACK);
	    etToDate .setTextColor(Color.BLACK);
	    etPasangan.setTextColor(Color.BLACK);	    
	    etAnak1.setTextColor(Color.BLACK);	    
	    etAnak2.setTextColor(Color.BLACK);	    
	    etAnak3.setTextColor(Color.BLACK);
	    
		
	}

	private void getCustomerData () {
		RetrieveCustomer retrieveCustomer = new RetrieveCustomer(FillTravelActivity.this, customerno.getText().toString());
		retrieveCustomer.execute();
		retrieveCustomer.customerInterface = this;
		
	} 
	
	@Override
	public void getCustomerList(ArrayList<HashMap<String, String>> custList) {
		if (custList != null) {
			this.custList = custList;
			
			sc = new RetrivePremi();
			sc.execute();
		}
		else {
			Dialog dialog = Utility.showCustomDialogInformation(this, "Informasi", "Gagal mendapatkan data customer");
			dialog.setOnDismissListener(new OnDismissListener() {
				
				@Override
				public void onDismiss(DialogInterface dialog) {
					if (PRODUCT_ACTION.equals("NEW"))
						startActivity(new Intent(FillTravelActivity.this, ChooseProductActivity.class));
					else if (PRODUCT_ACTION.equals("EDIT") || 
							PRODUCT_ACTION.equals("VIEW") || 
							PRODUCT_ACTION.equals("VIEW.UNPAID"))
						startActivity(new Intent(FillTravelActivity.this, SyncActivity.class));
					
					FillTravelActivity.this.finish();
				}
			});
		}
	}
	
	private void InitControls() {
		namapasangan = (EditText)findViewById(R.id.txtnama1);
		namaanak1 = (EditText)findViewById(R.id.txtnama2);
		namaanak2 = (EditText)findViewById(R.id.txtnama3);
		namaanak3 = (EditText)findViewById(R.id.txtnama4);
		
		nopasspasangan = (EditText)findViewById(R.id.txtnopass1);
		nopassanak1 = (EditText)findViewById(R.id.txtnopass2);
		nopassanak2 = (EditText)findViewById(R.id.txtnopass3);
		nopassanak3 = (EditText)findViewById(R.id.txtnopass4);
		
		namaahliwaris = (EditText)findViewById(R.id.txtAhliWaris);
		hub = (EditText)findViewById(R.id.txtHubungan);
		 
		namanegara1 = (EditText)findViewById(R.id.txtNamaNegara1);
		namanegara2 = (EditText)findViewById(R.id.txtNamaNegara2);
		namanegara3 = (EditText)findViewById(R.id.txtNamaNegara3);
		namanegara4 = (EditText)findViewById(R.id.txtNamaNegara4);
		namanegara5 = (EditText)findViewById(R.id.txtNamaNegara5);
		namanegara6 = (EditText)findViewById(R.id.txtNamaNegara6);
		
		namanegara1.setTag("");
		namanegara2.setTag("");
		namanegara3.setTag("");
		namanegara4.setTag("");
		namanegara5.setTag("");
		namanegara6.setTag("");
		 
		
		jumlahhari = (EditText)findViewById(R.id.txtJmlHariDipertanggungkan);
		tambahan = (EditText)findViewById(R.id.txtTambahanPerMinggu);
		loadingpremi = (EditText)findViewById(R.id.txtLoadingPremi);
		premi = (EditText)findViewById(R.id.txtPremi);
		materai = (EditText)findViewById(R.id.txtMaterai);
		polis = (EditText)findViewById(R.id.txtBiayaPolis);
		polis.setText("1.50");
		total = (EditText)findViewById(R.id.txtTotal);
		discpct = (EditText)findViewById(R.id.txtDiscountPct);
		disc = (EditText)findViewById(R.id.txtDiscount);
		
		fam = (RadioButton)findViewById(R.id.rboFamily);
    	individu = (RadioButton)findViewById(R.id.rboIndividu);
    	grup = (RadioButton)findViewById(R.id.rboGroup);
    	
    	asia = (RadioButton)findViewById(R.id.rboAsia);
    	nonasia = (RadioButton)findViewById(R.id.rboNonAsia);
    	schengen = (RadioButton)findViewById(R.id.rboSchengen);
    	schengen.setVisibility(View.GONE);
    	
    	vip = (RadioButton)findViewById(R.id.rboPlan1);
    	executive = (RadioButton)findViewById(R.id.rboPlan2);
    	duluxe = (RadioButton)findViewById(R.id.rboPlan3);
    	superior = (RadioButton)findViewById(R.id.rboPlan4);
    	
    	pasangan = (Switch)findViewById(R.id.swiPasangan);
		anak1 = (Switch)findViewById(R.id.swiAnak1);
		anak2 = (Switch)findViewById(R.id.swiAnak2);
		anak3 = (Switch)findViewById(R.id.swiAnak3);
		
		annual = (Switch)findViewById(R.id.swiAnnual);
		
		nopass = (EditText)findViewById(R.id.txtNoPass);
		
		customerno = (EditText)findViewById(R.id.txtCustomerNo);
		customername = (EditText)findViewById(R.id.txtCustomerName);
		
		hariKepergian = (EditText)findViewById(R.id.txtHariKepergian);
		
		etFromDate = (EditText)findViewById(R.id.txtTglBerangkat);
		etToDate  = (EditText)findViewById(R.id.txtTglKembali);
		etPasangan = (EditText)findViewById(R.id.txttgllahir1);
		etAnak1 = (EditText)findViewById(R.id.txttgllahir2);
		etAnak2 = (EditText)findViewById(R.id.txttgllahir3);
		etAnak3 = (EditText)findViewById(R.id.txttgllahir4);
		
		
		insured1 = (LinearLayout)findViewById(R.id.sectionInsured1);
		insured2 = (LinearLayout)findViewById(R.id.sectionInsured2);
		insured3 = (LinearLayout)findViewById(R.id.sectionInsured3);
		insured4 = (LinearLayout)findViewById(R.id.sectionInsured4);
		insured5 = (LinearLayout)findViewById(R.id.sectionInsured5); 
		insured6 = (LinearLayout)findViewById(R.id.sectionInsured6);
		insured7 = (LinearLayout)findViewById(R.id.sectionInsured7);
		insured8 = (LinearLayout)findViewById(R.id.sectionInsured8);
		insured9 = (LinearLayout)findViewById(R.id.sectionInsured9);
		insured10 = (LinearLayout)findViewById(R.id.sectionInsured10);
		insured11 = (LinearLayout)findViewById(R.id.sectionInsured11);
		insured12 = (LinearLayout)findViewById(R.id.sectionInsured12);
		insured13 = (LinearLayout)findViewById(R.id.sectionInsured13);
		insured14 = (LinearLayout)findViewById(R.id.sectionInsured14);
		insured15 = (LinearLayout)findViewById(R.id.sectionInsured15);
		  
		insured1.setVisibility(View.GONE);
		insured2.setVisibility(View.GONE);
		insured3.setVisibility(View.GONE);
		insured4.setVisibility(View.GONE);
		insured5.setVisibility(View.GONE);
		insured6.setVisibility(View.GONE);
		insured7.setVisibility(View.GONE);
		insured8.setVisibility(View.GONE);
		insured9.setVisibility(View.GONE);
		insured10.setVisibility(View.GONE);
		insured11.setVisibility(View.GONE);
		insured12.setVisibility(View.GONE);
		insured13.setVisibility(View.GONE);
		insured14.setVisibility(View.GONE);
		insured15.setVisibility(View.GONE);
		 
		btnDelNegara1 = (ImageView) findViewById(R.id.btnDeleteNegara);
		btnDelNegara2 = (ImageView) findViewById(R.id.btnDeleteNegara2);
		btnDelNegara3 = (ImageView) findViewById(R.id.btnDeleteNegara3);
		btnDelNegara4 = (ImageView) findViewById(R.id.btnDeleteNegara4);
		btnDelNegara5 = (ImageView) findViewById(R.id.btnDeleteNegara5);
		btnDelNegara6 = (ImageView) findViewById(R.id.btnDeleteNegara6);

		btnAdd = (Button)findViewById(R.id.btnAdd);
		btnRemove = (Button)findViewById(R.id.btnRemove);
		btnHide = (Button)findViewById(R.id.btnHide);
		btnInputInsuredGroup = (Button)findViewById(R.id.btnInputInsuredGroup);
		
		jumlahTertanggung = (EditText) findViewById(R.id.txtInsuredCount);
		namaGrupTour = (EditText) findViewById(R.id.txtNamaGrupTur);
		
//		spnINSURED_COUNT = (Spinner)findViewById(R.id.spnINSURED_COUNT);
//		Utility.BindINSURED_COUNT(spnINSURED_COUNT, FillTravelActivity.this, FillTravelActivity.this);
		
		
		
	}
	
	private void initColor () {
		namapasangan.setEnabled(false);
		etPasangan.setEnabled(false);
		nopasspasangan.setEnabled(false);
		
		namapasangan.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
		etPasangan.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
		nopasspasangan.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));	
		
		
		namaanak1.setEnabled(false);
		etAnak1.setEnabled(false);
		nopassanak1.setEnabled(false);
		
		namaanak1.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
		etAnak1.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
		nopassanak1.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));	
		
		namaanak2.setEnabled(false);
		etAnak2.setEnabled(false);
		nopassanak2.setEnabled(false);
		
		namaanak2.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
		etAnak2.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
		nopassanak2.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));		
		
		
		namaanak3.setEnabled(false);
		etAnak3.setEnabled(false);
		nopassanak3.setEnabled(false);
		
		namaanak3.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
		etAnak3.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
		nopassanak3.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));		
		
		
		
	}
	

	private void LoadDB() {
		
		DBA_PRODUCT_MAIN dbm = null;
		DBA_PRODUCT_TRAVEL_SAFE dba = null;
		
		Cursor cm = null;
		Cursor c = null;
		
		try{
			
			dbm = new DBA_PRODUCT_MAIN(getBaseContext());
			dba = new DBA_PRODUCT_TRAVEL_SAFE(getBaseContext());
			
			dbm.open();
			cm = dbm.getRow(SPPA_ID);
			cm.moveToFirst();
			
			dba.open();
			c = dba.getRow(SPPA_ID);
			c.moveToFirst();
			
			customerno.setText(cm.getString(1));
			customername.setText(cm.getString(2));
			
			// jika bukan grup
			
			PRODUCT_MAIN_ID = c.getInt(93);
			namaGrupTour.setText(c.getString(94));
			jumlahTertanggung.setText(c.getString(95));
		
			
			for (int i = 0; i < spTujuan.getCount(); i++) {               
				String item = spTujuan.getItemAtPosition(i).toString(); 
	            if (item.equalsIgnoreCase(c.getString(15))) {
	            	  spTujuan.setSelection(i);
	               break;
	              }
			}
			
			
			if (c.getLong(1) == 1) 
				fam.setChecked(true);
			else if (c.getLong(1) == 0)
				individu.setChecked(true);
			else 
				grup.setChecked(true);
			
			if (c.getLong(16) == 1) 
				asia.setChecked(true); 
			else 
				nonasia.setChecked(true);
			
			if (c.getLong(1) != 2) 
			{
				if(c.getLong(35) == 1) pasangan.setChecked(true);
				namapasangan.setText(c.getString(3));
				etPasangan.setText(c.getString(4));
				nopasspasangan.setText(c.getString(5));
				
				if(c.getLong(36) == 1) anak1.setChecked(true);
				namaanak1.setText(c.getString(6));
				etAnak1.setText(c.getString(7));
				nopassanak1.setText(c.getString(8));
				
				if(c.getLong(37) == 1) anak2.setChecked(true);
				namaanak2.setText(c.getString(9));
				etAnak2.setText(c.getString(10));
				nopassanak2.setText(c.getString(11));
				
				if(c.getLong(38) == 1) anak3.setChecked(true);
				namaanak3.setText(c.getString(12));
				etAnak3.setText(c.getString(13));
				nopassanak3.setText(c.getString(14));
			}
			
//			schengne
				
			namanegara1.setText(c.getString(17));
			namanegara2.setText(c.getString(83));	
			namanegara3.setText(c.getString(84));
			namanegara4.setText(c.getString(85));
			namanegara5.setText(c.getString(86));
			namanegara6.setText(c.getString(87));
			
			if (TextUtils.isEmpty(c.getString(39))) 
				namanegara1.setTag("");
			else
				namanegara1.setTag(c.getString(39));				
			
			if (TextUtils.isEmpty(c.getString(88)))
				namanegara2.setTag("");
			else
				namanegara2.setTag(c.getString(88));				
			 
			if (TextUtils.isEmpty(c.getString(89)))
				namanegara3.setTag("");
			else
				namanegara3.setTag(c.getString(89));
 
			if (TextUtils.isEmpty(c.getString(90)))
				namanegara4.setTag("");
			else
				namanegara4.setTag(c.getString(90));
			 
			if (TextUtils.isEmpty(c.getString(91)))
				namanegara5.setTag("");
			else
				namanegara5.setTag(c.getString(91)); 

			if (TextUtils.isEmpty(c.getString(92)))
				namanegara6.setTag("");
			else
				namanegara6.setTag(c.getString(93));
			 
			
			//ambil data di Bundle
			if (KODE_NEGARA != null && NAMA_NEGARA != null)
			{
				namanegara1.setTag(KODE_NEGARA);
				namanegara1.setText(NAMA_NEGARA);
			}
				
			
				
			namaahliwaris.setText(c.getString(18));
			hub.setText(c.getString(19));
			
			etFromDate.setText(c.getString(20));
			etToDate.setText(c.getString(21));
			
			if(c.getLong(22) == 0)
				vip.setChecked(true);
			else if(c.getLong(22) == 1)
				executive.setChecked(true);
			else if(c.getLong(22) == 2)
				duluxe.setChecked(true);
			else if(c.getLong(22) == 3)
				superior.setChecked(true);
			
			if(c.getLong(40) == 1) annual.setChecked(true); else annual.setChecked(false);
			
			jumlahhari.setText(nf.format(c.getDouble(23)));
			tambahan.setText(nf.format(c.getDouble(24)));
			loadingpremi.setText(nf.format(c.getDouble(25)));
			premi.setText(nf.format(c.getDouble(26)));
			discpct.setText(nf.format(cm.getDouble(23)));
			disc.setText(nf.format(cm.getDouble(24)));
			polis.setText(nf.format(c.getDouble(27)));
			total.setText(nf.format(c.getDouble(28)));
			
			nopass.setText(c.getString(51));
			
			if (cm.getString(20).equals("R")){
				etFromDate.setEnabled(false);
				etToDate.setEnabled(false);
				customerno.setEnabled(false);
				((Button)findViewById(R.id.btnChooseCustomer)).setEnabled(false);
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			if (cm != null)
				cm.close();
			
			if (c != null)
				c.close();
			
			if (dbm != null)
				dbm.close();
			
			if (dba != null)
				dba.close();
		}
	}
	
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode == 100 ){
			KODE_NEGARA = data.getStringExtra("KODE_NEGARA");
			NAMA_NEGARA = data.getStringExtra("NAMA_NEGARA");
			
			if (KODE_NEGARA != null && NAMA_NEGARA != null)
			{
				switch (requestCode) {
				case 200:
					namanegara1.setTag(KODE_NEGARA);
					namanegara1.setText(NAMA_NEGARA);
					break;
				case 201:
					namanegara2.setTag(KODE_NEGARA);
					namanegara2.setText(NAMA_NEGARA);
					break;
				case 202:
					namanegara3.setTag(KODE_NEGARA);
					namanegara3.setText(NAMA_NEGARA);
					break;
				case 203:
					namanegara4.setTag(KODE_NEGARA);
					namanegara4.setText(NAMA_NEGARA);
					break;
				case 204:
					namanegara5.setTag(KODE_NEGARA);
					namanegara5.setText(NAMA_NEGARA);
					break;
				case 205:
					namanegara6.setTag(KODE_NEGARA);
					namanegara6.setText(NAMA_NEGARA);
					break;

				default:
					break;
				}
				
			}	
		}
		else if (resultCode == 101 && requestCode == 200) {
			PRODUCT_MAIN_ID = data.getExtras().getLong(FillInsuredGroupActivity.TAG_BUNDLE_PRODUCT_MAIN_ID);
			INSURED_COUNT = data.getExtras().getInt(FillInsuredGroupActivity.TAG_BUNDLE_INSURED_COUNT);
			
			Log.i(TAG, "::onActivityResult:" + "product main id = " + PRODUCT_MAIN_ID);
			
			jumlahTertanggung.setText(INSURED_COUNT + "");
			try 
			{
				calculateAll();
			} catch (ParseException e) 
			{ 
				e.printStackTrace();
			}
		}
	}

	
	private void RegisterListener(){
		btnDelNegara1.setOnClickListener(btnDelNegaraOnClickListener(namanegara1));
		btnDelNegara2.setOnClickListener(btnDelNegaraOnClickListener(namanegara2));
		btnDelNegara3.setOnClickListener(btnDelNegaraOnClickListener(namanegara3));
		btnDelNegara4.setOnClickListener(btnDelNegaraOnClickListener(namanegara4));
		btnDelNegara5.setOnClickListener(btnDelNegaraOnClickListener(namanegara5));
		btnDelNegara6.setOnClickListener(btnDelNegaraOnClickListener(namanegara6));
		
		
		btnInputInsuredGroup.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) { 
				Bundle b = new Bundle();
				b.putLong(FillInsuredGroupActivity.TAG_BUNDLE_PRODUCT_MAIN_ID, PRODUCT_MAIN_ID);
				Log.i(TAG, "::onClick:" + "product main id = " + PRODUCT_MAIN_ID);
				
				
				Intent intent = new Intent(FillTravelActivity.this, FillInsuredGroupActivity.class);
				intent.putExtras(b);
				startActivityForResult(intent, 200);
				
				
			}
		}); 
		btnHide.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				View view = (View) findViewById(R.id.sectionDataTertanggung);
				 
				if (view.getVisibility() == View.VISIBLE){
					view.setVisibility(View.GONE);
					btnHide.setText("Show");
				}
				else {
					view.setVisibility(View.VISIBLE);
					btnHide.setText("Hide");
				}
			}
		});
//		
//		btnAdd.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				INSURED_COUNT = (INSURED_COUNT+1) > MAX_INSURED_GROUP ? MAX_INSURED_GROUP : INSURED_COUNT+1; 
//				Log.i(TAG, "::onClick:" + "insured count = " + INSURED_COUNT);
//				
//				if (INSURED_COUNT == MAX_INSURED_GROUP) 
//					jumlahTertanggung.setText(INSURED_COUNT  + " (maks)");
//				else
//					jumlahTertanggung.setText(INSURED_COUNT  + "");
//				
//				
//				switch (INSURED_COUNT) {
//					case 1: insured1.setVisibility(View.VISIBLE); break;
//					case 2: insured2.setVisibility(View.VISIBLE); break;
//					case 3: insured3.setVisibility(View.VISIBLE); break;
//					case 4: insured4.setVisibility(View.VISIBLE); break;
//					case 5: insured5.setVisibility(View.VISIBLE); break;
//					case 6: insured6.setVisibility(View.VISIBLE); break;
//					case 7: insured7.setVisibility(View.VISIBLE); break;
//					case 8: insured8.setVisibility(View.VISIBLE); break;
//					case 9: insured9.setVisibility(View.VISIBLE); break;
//					case 10: insured10.setVisibility(View.VISIBLE); break;
////					case 11: insured11.setVisibility(View.VISIBLE); break;
////					case 12: insured12.setVisibility(View.VISIBLE); break;
////					case 13: insured13.setVisibility(View.VISIBLE); break;
////					case 14: insured14.setVisibility(View.VISIBLE); break;
////					case 15: insured15.setVisibility(View.VISIBLE); break; 
//	
//					default:
//						break;
//				}
//			}
//		});
//		
//		btnRemove.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				switch (INSURED_COUNT) {
////				case 15: insured15.setVisibility(View.GONE);
////						 initInsured(namaTertanggung15, dob15, passport15);
////						 break;
////
////				case 14: insured14.setVisibility(View.GONE);
////						 initInsured(namaTertanggung14, dob14, passport14);
////						 break;
////
////				case 13: insured13.setVisibility(View.GONE);
////						 initInsured(namaTertanggung13, dob13, passport13);
////						 break;
////
////				case 12: insured12.setVisibility(View.GONE);
////						 initInsured(namaTertanggung12, dob12, passport12);
////						 break;
////
////				case 11: insured11.setVisibility(View.GONE);
////						 initInsured(namaTertanggung11, dob11, passport11);
////						 break;
//
//				case 10: insured10.setVisibility(View.GONE);
//						 initInsured(namaTertanggung10, dob10, passport10);
//						 break;
//
//				case 9: insured9.setVisibility(View.GONE);
//						 initInsured(namaTertanggung9, dob9, passport9);
//						 break;
//
//				case 8: insured8.setVisibility(View.GONE);
//						 initInsured(namaTertanggung8, dob8, passport8);
//						 break;
//
//				case 7: insured7.setVisibility(View.GONE);
//						 initInsured(namaTertanggung7, dob7, passport7);
//						 break;
//
//				case 6: insured6.setVisibility(View.GONE);
//						 initInsured(namaTertanggung6, dob6, passport6);
//						 break;
//
//				case 5: insured5.setVisibility(View.GONE);
//						 initInsured(namaTertanggung5, dob5, passport5);
//						 break;
//
//				case 4: insured4.setVisibility(View.GONE);
//						 initInsured(namaTertanggung4, dob4, passport4);
//						 break;
//
//				case 3: insured3.setVisibility(View.GONE);
//						 initInsured(namaTertanggung3, dob3, passport3);
//						 break;
//
//				case 2: insured2.setVisibility(View.GONE);
//						 initInsured(namaTertanggung2, dob2, passport2);
//						 break;
//
//				case 1: insured1.setVisibility(View.GONE);
//						 initInsured(namaTertanggung1, dob1, passport1);
//						 break;
//
//				default:
//					break;
//				}
//
//				INSURED_COUNT = (INSURED_COUNT-1) < 0 ? 0 : INSURED_COUNT-1;
//				Log.i(TAG, "::onClick:" + "insured count = " + INSURED_COUNT);
//				jumlahTertanggung.setText(INSURED_COUNT  + "");
//				
//			}
//
//			private void initInsured(EditText namaTertanggung, EditText dob,
//					EditText passport) {
//				namaTertanggung.setText("");
//				dob.setText("");
//				passport.setText("");
//
//			}
//		});
		
		etAnak3.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus)
					showDialog(DATE_LAHIR_ANAK3);
			}
		});
		etAnak3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DATE_LAHIR_ANAK3);
			}
		});
		
		etAnak2.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus)
					showDialog(DATE_LAHIR_ANAK2);
			}
		});
		etAnak2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DATE_LAHIR_ANAK2);
			}
		});
		
		etAnak1.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus)
					showDialog(DATE_LAHIR_ANAK1);
			}
		});
		etAnak1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DATE_LAHIR_ANAK1);
			}
		});
		
		etPasangan.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus)
					showDialog(DATE_LAHIR_PASANGAN);
			}
		});
		etPasangan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DATE_LAHIR_PASANGAN);
			}
		});
		  
		etToDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) { 
				etToDate.setTextColor(Color.BLACK);
				
				SimpleDateFormat dateFormat = new  SimpleDateFormat("dd/MM/yyyy");
				Calendar c  = Calendar.getInstance();
				
				int Y, M, D;
				
				
				if (!etToDate.getText().toString().isEmpty()) 
				{
					try {
						c.setTime(dateFormat.parse(etToDate.getText().toString()));
					} catch (ParseException e) { 
						e.printStackTrace();
					}
					
					Y = c.get(Calendar.YEAR);
					M = c.get(Calendar.MONTH);
					D = c.get(Calendar.DAY_OF_MONTH);
					
				}
				else
				{
					Y = c.get(Calendar.YEAR);
					M = c.get(Calendar.MONTH);
					D = c.get(Calendar.DAY_OF_MONTH);
					
				}
				
				
				DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
					public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) { 
						etToDate.setText(Utility.setUIDate(selectedYear, selectedMonth, selectedDay)); 
						try {
							calculateAll();
						} catch (ParseException e) { 
							e.printStackTrace();
						} 
					}
				};
				
				Dialog dialog = new DatePickerDialog(FillTravelActivity.this, datePickerListener, Y, M, D); 
				dialog.show();
				  
				try 
				{
					long maxDate = 0;
					
					if (annual.isChecked()) 
					{
						maxDate= dateFormat.parse(Utility.getAnnualDate(etFromDate.getText().toString(), etToDate.getText().toString())).getTime();
						((DatePickerDialog) dialog).getDatePicker().setMaxDate(maxDate);
						Log.i(TAG, "::onCreateDialog:" + " max date annual year");
					}
					else if ( grup.isChecked())
					{
						maxDate= dateFormat.parse(Utility.getAnnualMonth(etFromDate.getText().toString())).getTime();
						((DatePickerDialog) dialog).getDatePicker().setMaxDate(maxDate);	
						Log.i(TAG, "::onCreateDialog:" + " max date annual month");
					}
					else
					{ 
						Date from = dateFormat.parse(etFromDate.getText().toString());

						Calendar cal = Calendar.getInstance();
				        cal.setTime(from);
				        cal.add(Calendar.YEAR, 5); 
						((DatePickerDialog) dialog).getDatePicker().setMaxDate(cal.getTimeInMillis());	
						Log.i(TAG, "::onCreateDialog:" + " max date normal");	
					}
					
					
				} catch (ParseException e) 
				{ 
					e.printStackTrace();
				}
				
				
			}
		}); 
		
		etFromDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) { 
				etFromDate.setTextColor(Color.BLACK);
				SimpleDateFormat dateFormat = new  SimpleDateFormat("dd/MM/yyyy");
				Calendar c  = Calendar.getInstance();
				
				int Y, M, D;
				
				
				if (!etFromDate.getText().toString().isEmpty()) 
				{
					try {
						c.setTime(dateFormat.parse(etFromDate.getText().toString()));
					} catch (ParseException e) { 
						e.printStackTrace();
					}
					
					Y = c.get(Calendar.YEAR);
					M = c.get(Calendar.MONTH);
					D = c.get(Calendar.DAY_OF_MONTH);
					
				}
				else
				{
					Y = c.get(Calendar.YEAR);
					M = c.get(Calendar.MONTH);
					D = c.get(Calendar.DAY_OF_MONTH);
					
				}
				
				
				DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
					public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) { 
						etFromDate.setText(Utility.setUIDate(selectedYear, selectedMonth, selectedDay)); 
						try {
							if (annual.isChecked())
								etToDate.setText(Utility.getAnnualDate(etFromDate.getText().toString(), ""));
							else
								etToDate.setText(Utility.GetTomorrowString(etFromDate.getText().toString()));

							calculateAll();
						} catch (ParseException e) { 
							e.printStackTrace();
						}
					 
							
					}
				};
				
				Dialog dialog = new DatePickerDialog(FillTravelActivity.this, datePickerListener, Y, M, D); 
				dialog.show(); 
			}
		});
		
		rbgNegara.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				namanegara1.setText("");
				namanegara2.setText("");
				namanegara3.setText("");
				namanegara4.setText("");
				namanegara5.setText("");
				namanegara6.setText("");
				
				namanegara1.setTag("");
				namanegara2.setTag("");
				namanegara3.setTag("");
				namanegara4.setTag("");
				namanegara5.setTag("");
				namanegara6.setTag("");
				
				
				
				if (schengen.isChecked()) {
					duluxe.setEnabled(false);
					superior.setEnabled(false);
					
					vip.setChecked(false);
					executive.setChecked(false);
					duluxe.setChecked(false);
					superior.setChecked(false);  
				}
				else {
					vip.setEnabled(true);
					executive.setEnabled(true);
					duluxe.setEnabled(true);
					superior.setEnabled(true);   
				}
				
				try{
					calculateAll();
				}
				catch(ParseException e) {
					e.printStackTrace();				
				}
			}
		});
		
		annual.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				String mulai = "",
					   akhir = "";
				
				try { 
					if (etFromDate.getText().toString().isEmpty())
					{
						mulai = Utility.GetTodayString();
						etFromDate.setText(mulai);					
					}
					else 
					{					
						mulai = etFromDate.getText().toString();
					}
						 
					
					if (isChecked) 
					{ 
						  akhir = Utility.getAnnualDate(mulai, akhir); 
					} 
					else
					{
						  akhir = Utility.GetTomorrowString(mulai);
					}
					
					etFromDate.setTextColor(Color.BLACK);
					etToDate.setTextColor(Color.BLACK);
					
					etToDate.setText(akhir);			
					calculateAll();
				}
				catch(Exception e) {
					e.printStackTrace();
				}   
			}
		});
		
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	        public void onCheckedChanged(RadioGroup group, int checkedId) {
	        	RadioButton rbFam = (RadioButton)findViewById(R.id.rboFamily);
	        	RadioButton rbIndividu = (RadioButton)findViewById(R.id.rboIndividu); 
	        	
	        	if(rbFam.isChecked()) {
	        		famLayout.setVisibility(View.VISIBLE);	 
	        		gruplayout.setVisibility(View.GONE);	
	        		
	        		etToDate.setText("");
	        		disableGrup();
	        	}
	        	else if (rbIndividu.isChecked()) {
	        		famLayout.setVisibility(View.GONE);	 
	        		gruplayout.setVisibility(View.GONE);
	        		

	        		etToDate.setText("");
	        		disableFam();
	        		disableGrup();
	        	}
	        	else {
	        		famLayout.setVisibility(View.GONE);	 
	        		gruplayout.setVisibility(View.VISIBLE);
	        		
	        		vip.setEnabled(false);
					executive.setEnabled(false);
					duluxe.setEnabled(false);
					superior.setEnabled(false);   
					
					annual.setEnabled(false);
					annual.setChecked(false);

	        		etToDate.setText("");
	        		disableFam();	
	        	}
	        	
	        	if(!customerno.getText().toString().isEmpty() && !etFromDate.getText().toString().isEmpty() && !etToDate.getText().toString().isEmpty()){
	        		try {
						calculateAll();
					} catch (ParseException e) {
						e.printStackTrace();
					}
	        	}
	        }
	    });
		
		namanegara1.setOnClickListener(namaNegaraOnClickListener(200));
		namanegara2.setOnClickListener(namaNegaraOnClickListener(201));
		namanegara3.setOnClickListener(namaNegaraOnClickListener(202));
		namanegara4.setOnClickListener(namaNegaraOnClickListener(203));
		namanegara5.setOnClickListener(namaNegaraOnClickListener(204));
		namanegara6.setOnClickListener(namaNegaraOnClickListener(205));
		 
		namanegara1.setKeyListener(null);
		namanegara2.setKeyListener(null);
		namanegara3.setKeyListener(null);
		namanegara4.setKeyListener(null);
		namanegara5.setKeyListener(null);
		namanegara6.setKeyListener(null);
		
		//TODO: FIXED THIS!
		
		/*
		spNegara.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				String[] namaNegara = new String [] {"austria",
	        			"belgium",
	        			"czech Rep.",
	        			"denmark",
	        			"estonia",
	        			"finland",
	        			"france",
	        			"germany",
	        			"greece",
	        			"hungary",
	        			"iceland",
	        			"italy",
	        			"latvia",
	        			"lithuania",
	        			"luxembourg",
	        			"malta",
	        			"netherlands",
	        			"norway",
	        			"poland",
	        			"portugal",
	        			"slovakia",
	        			"slovenia",
	        			"spain",
	        			"sweden",
	        			"switzerland"
	        			};
				
				int schengen = 0;
        		for (int i = 0; i<25; i++) {
        			if(((SpinnerGenericItem)spNegara.getSelectedItem()).getDesc().toLowerCase().contains(namaNegara[i])){
        				schengen = 1;
        				break;
        			}
        		}
        		if(schengen == 1) {
        			duluxe.setEnabled(false);
        			superior.setEnabled(false);
        		}
        		else {
        			duluxe.setEnabled(true);
        			superior.setEnabled(true);
        		}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		*/
		
		
		rbgPlan.setOnCheckedChangeListener(new OnCheckedChangeListener()  {
	        public void onCheckedChanged(RadioGroup group, int checkedId) {
	        	if(!customerno.getText().toString().isEmpty() && !etFromDate.getText().toString().isEmpty() && !etToDate.getText().toString().isEmpty()){
	        		try {
						calculateAll();
					} catch (ParseException e) {
						e.printStackTrace();
					}
	        	}
	        }
	    });
		
		
		
		discpct.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus) {
					discpct.setText(Utility.removeComma(discpct.getText().toString()));
				}
				else {
					try {
						if (discpct.getText().toString().isEmpty())
							return ;
						
						discpct.setText(nf.format(nf.parse(discpct.getText().toString())));
						calculateAll();
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		disc.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus) {
					disc.setText(Utility.removeComma(disc.getText().toString()));
				}
				else {
					try {
						if (disc.getText().toString().isEmpty())
							return ;
						
						disc.setText(nf.format(nf.parse(disc.getText().toString()))); 
						calculateAll();
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		pasangan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if(!pasangan.isChecked())
				{
					namapasangan.setEnabled(false);
					etPasangan.setEnabled(false);
					nopasspasangan.setEnabled(false);
					
					namapasangan.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
					etPasangan.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
					nopasspasangan.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
					
					namapasangan.setText("");
					etPasangan.setText("");
					nopasspasangan.setText("");
				}
				else {
					namapasangan.setEnabled(true);
					etPasangan.setEnabled(true);
					nopasspasangan.setEnabled(true);
					
					namapasangan.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext));					
					etPasangan.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext));					
					nopasspasangan.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext));					
					
				}
				namapasangan.setHintTextColor(Color.GRAY);
				etPasangan.setHintTextColor(Color.GRAY);
				nopasspasangan.setHintTextColor(Color.GRAY);
			}
		});
		
		anak1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if(!anak1.isChecked())
				{
					namaanak1.setEnabled(false);
					etAnak1.setEnabled(false);
					nopassanak1.setEnabled(false);
					
					namaanak1.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
					etAnak1.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
					nopassanak1.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					

					namaanak1.setText("");
					etAnak1.setText("");
					nopassanak1.setText("");
				}
				else {
					namaanak1.setEnabled(true);
					etAnak1.setEnabled(true);
					nopassanak1.setEnabled(true);
					
					namaanak1.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext));					
					etAnak1.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext));					
					nopassanak1.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext));					
					
				}

				namaanak1.setHintTextColor(Color.GRAY);
				etAnak1.setHintTextColor(Color.GRAY);
				nopassanak1.setHintTextColor(Color.GRAY);
			}
		});
		
		anak2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if(!anak2.isChecked())
				{
					namaanak2.setEnabled(false);
					etAnak2.setEnabled(false);
					nopassanak2.setEnabled(false);
					
					namaanak2.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
					etAnak2.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
					nopassanak2.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					

					namaanak2.setText("");
					etAnak2.setText("");
					nopassanak2.setText("");
				}
				else {
					namaanak2.setEnabled(true);
					etAnak2.setEnabled(true);
					nopassanak2.setEnabled(true);
					
					namaanak2.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext));					
					etAnak2.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext));					
					nopassanak2.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext));					
					
				}

				namaanak2.setHintTextColor(Color.GRAY);
				etAnak2.setHintTextColor(Color.GRAY);
				nopassanak2.setHintTextColor(Color.GRAY);
			}
		});
	
		anak3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if(!anak3.isChecked())
				{
					namaanak3.setEnabled(false);
					etAnak3.setEnabled(false);
					nopassanak3.setEnabled(false);
					
					namaanak3.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
					etAnak3.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
					nopassanak3.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					

					namaanak3.setText("");
					etAnak3.setText("");
					nopassanak3.setText("");
				}
				else {
					namaanak3.setEnabled(true);
					etAnak3.setEnabled(true);
					nopassanak3.setEnabled(true);
					
					namaanak3.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext));					
					etAnak3.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext));					
					nopassanak3.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext));					
					
				}

				namaanak3.setHintTextColor(Color.GRAY);
				etAnak3.setHintTextColor(Color.GRAY);
				nopassanak3.setHintTextColor(Color.GRAY);
			}
		});
	}
	
	private OnClickListener btnDelNegaraOnClickListener(final EditText namaNegara)
	{
		return new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				namaNegara.setText("");
				namaNegara.setTag("");				
			}
		};
	}
	private OnClickListener namaNegaraOnClickListener(final int requestCode) {
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (asia.isChecked())
					b.putInt("NEGARA_TUJUAN", 1);
				else if (nonasia.isChecked())
					b.putInt("NEGARA_TUJUAN", 2);
				else
					b.putInt("NEGARA_TUJUAN", 3);
				
				Intent i = new Intent(getBaseContext(), ChooseCountryActivity.class);
				i.putExtras(b);
				startActivityForResult(i, requestCode);
			}
			
		};
	}
	private void disableGrup () {
		
		DBA_INSURED_GROUP dba = new DBA_INSURED_GROUP(this);
		Cursor c = null;
		Boolean result = false;

		try {
			dba.open();
			result = dba.delete(PRODUCT_MAIN_ID);
			
			PRODUCT_MAIN_ID = -1;
			INSURED_COUNT = 0;

			namaGrupTour.setText("");
			jumlahTertanggung.setText("");

			vip.setEnabled(true);
			executive.setEnabled(true);
			duluxe.setEnabled(true);
			superior.setEnabled(true);   			
			annual.setEnabled(true); 
			
			Log.i(TAG, "::disableGrup:" + "delete " + result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dba != null)
				dba.close();

		}
	}
	
	private void disableFam () {
		
		pasangan.setChecked(false);
		anak1.setChecked(false);
		anak2.setChecked(false);
		anak3.setChecked(false);
		
		
		
		namapasangan.setEnabled(false);
		etPasangan.setEnabled(false);
		nopasspasangan.setEnabled(false);
		
		namapasangan.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
		etPasangan.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
		nopasspasangan.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
		
		namapasangan.setText("");
		etPasangan.setText("");
		nopasspasangan.setText("");
		
		namaanak1.setEnabled(false);
		etAnak1.setEnabled(false);
		nopassanak1.setEnabled(false);
		
		namaanak1.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
		etAnak1.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
		nopassanak1.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					

		namaanak1.setText("");
		etAnak1.setText("");
		nopassanak1.setText("");
		
		
		namaanak2.setEnabled(false);
		etAnak2.setEnabled(false);
		nopassanak2.setEnabled(false);
		
		namaanak2.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
		etAnak2.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
		nopassanak2.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					

		namaanak2.setText("");
		etAnak2.setText("");
		nopassanak2.setText("");
		
		namaanak3.setEnabled(false);
		etAnak3.setEnabled(false);
		nopassanak3.setEnabled(false);
		
		namaanak3.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
		etAnak3.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					
		nopassanak3.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextdisabled));					

		namaanak3.setText("");
		etAnak3.setText("");
		nopassanak3.setText("");
		
		
	}
	
	private void calculateAll() throws ParseException {
		if (grup.isChecked())
			getPremiGrup();
		else
			getPremi();
		getDiscount();
	 	getPolis();
	 	getMaterai();
		getTotal();
		
		jumlahhari.setText(nf.format(vPremiDays));
        tambahan.setText(nf.format(vPremiWeeks));
        loadingpremi.setText(nf.format(vLoadingPremi));
		premi.setText(nf.format(v_premi));
		disc.setText(nf.format(v_disc));
		polis.setText(nf.format(v_polis));
		materai.setText(nf.format(v_materai));
		total.setText(nf.format(v_total));
	}
	
	 
	
	private void getPremiGrup() {
		
		DBA_MASTER_TRAVELSAFE_INT_RATE dba = null;		
		DBA_INSURED_GROUP dbInsured = null;
		
		Cursor c = null,
			   c_week = null,
			   cInsured = null;		
		
		String  Area, 
				Coverage,
				CCOD = "", 
				DCOD = null,
				TotalDays, 
				TotalWeeks;
		
		double  MaxBenefit, 
				Week,
				PremiDays, 
				PremiWeeks,
				LoadingPremi,
				Premi,
				Premi0;	
		
		int Duration,
				DurationCode = 0,
				Age;
		
		int[] ages = new int[10];
		
	    Week = 0;
	    Duration = 0;
	    Coverage = "";


    	if(custList == null)
    		return;  
    	
	    try{ 
	  
	    	Duration = Utility.DATEDIFF(etFromDate.getText().toString(), etToDate.getText().toString()) + 1;    		    	 
	    	Calendar cal = Calendar.getInstance();
	    	SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	    	String formattedDate = df.format(cal.getTime());
	    	
	    	
//	    	Age = Utility.getAge(Utility.getFormatDate(custList.get(0).get("CUSTOMER_DOB").toString()), Utility.GetTodayString());
//
//	    	Log.d ("Age", String.valueOf(Age));
//	    	Log.d ("Duration", String.valueOf(Duration));
//	    	
//	    	dbInsured = new DBA_INSURED_GROUP(FillTravelActivity.this);
//	    	dbInsured.open();
//	    	cInsured = dbInsured.getRow(PRODUCT_MAIN_ID);
//	    	Log.i(TAG, "::getPremiGrup:" + "product maind id = " + PRODUCT_MAIN_ID);
//	    	
//	    	if (cInsured.moveToFirst()) {
//	    		Log.i(TAG, "::getPremiGrup:" + "insured count :" + INSURED_COUNT);
//	    		
//	    		for (int i = 0; i < INSURED_COUNT; i++) {
//	    			String dob = cInsured.getString((i + 1) * 3);
//	    			String today = Utility.GetTodayString();
//			    	ages[i] = Utility.getAge(dob, today);	   	    			
//			    	Log.i(TAG, "::getPremiGrup:" + "age  " + ages[0]);
//	    		}
//	    	}
         	 
	    	
	    	
    		String lfam = null, choosenPlan, tujuan, flagAnnual;
		
    		if (fam.isChecked()) 
    			lfam = "1"; 
    		else if (individu.isChecked())
    			lfam = "0";
    		else if (grup.isChecked())
    			lfam = "2";
		
    		if (asia.isChecked()) tujuan = "1"; else tujuan = "2"; //schengen juga 2
    		if (annual.isChecked()) flagAnnual = "1"; else flagAnnual = "0";
		
			if(vip.isChecked()) 			choosenPlan = "0";
			else if(executive.isChecked())  choosenPlan = "1";
			else if(duluxe.isChecked()) 	choosenPlan = "2";
			else		   					choosenPlan = "3";
		  
			 
			if (tujuan.equals("1")) 
			{
				Coverage = "491";
				CCOD =  Coverage;
			}
			else if (tujuan.equals("2")) 
			{
				Coverage = "492";
				CCOD = Coverage;
			}
		
		 
		
			if (Duration <= 5) 
			{
				DurationCode = 1;
				DCOD = DurationCode + "";
			}
			else if (Duration <= 8)
			{
				DurationCode = 2;
				DCOD = DurationCode + "";
			}
			else if (Duration <= 15) 
			{
				DurationCode = 3;
				DCOD = DurationCode + "";
			}
			else if (Duration <= 22)
			{
				DurationCode = 4;
				DCOD = DurationCode + "";
			}
			else if (Duration <= 30)
			{
				DurationCode = 5;
				DCOD = DurationCode + "";
			} 
			 
			

			Log.d ("Area", tujuan);
			Log.d ("Coverage", Coverage);
			Log.d ("DurationCode", String.valueOf(DurationCode));
			Log.d ("Week", String.valueOf(Week));

			
			
	        Area = tujuan;	        
			dba = new DBA_MASTER_TRAVELSAFE_INT_RATE(getBaseContext());
			dba.open();
			c = dba.getRate(Area, Coverage, String.valueOf(DurationCode));
			 
	

//			if (Week > 0)
//			{
//				c_week = dba.getRate(Area, Coverage, "9");
//            	
//            	if(!c_week.moveToFirst())            		
//                    PremiWeeks = 0.0;
//                else
//                    PremiWeeks = c_week.getDouble(3) * Week;
//			}
//            else
//                PremiWeeks = 0.0;
            
            if (!c.moveToFirst())
            {
                PremiDays = 0.0;
                MaxBenefit = 0.00;
            }
            else
            { 
                PremiDays = c.getDouble(3);
                
                String DestinationCountry = namanegara1.getTag().toString();  
                
                if (Area.equals("1")) 
                {
                	if (DestinationCountry.equals("HK") ||
            			DestinationCountry.equals("BN") || 
            			DestinationCountry.equals("MY") || 
            			DestinationCountry.equals("SG") || 
            			DestinationCountry.equals("JP") || 
            			DestinationCountry.equals("KR") || 
            			DestinationCountry.equals("KP") )
                        MaxBenefit = c.getDouble(4);
                    else
                        MaxBenefit = c.getDouble(4) * 0.75;
                }
                else
                    MaxBenefit = c.getDouble(4);
            }
            PremiWeeks = 0.0;
            TotalDays  = String.valueOf(Duration);
            TotalWeeks = String.valueOf(Week); 
            Premi0 	   = PremiDays + PremiWeeks;
            Premi0	   = INSURED_COUNT * Premi0; // jumlah tertanggung * premi
            
            Log.i(TAG, "::getPremiGrup:" + "jumlah tertanggung = " + INSURED_COUNT);
            Log.i(TAG, "::getPremiGrup:" + "premi total 	   = " + Premi0);
            
 
//            if (Age >= 70) 
//                LoadingPremi = (0.5 * (Premi0));
//            else
//                LoadingPremi = 0.0;
//            
            LoadingPremi = 0.0;
            Premi 		 = Premi0 + LoadingPremi;
             
            
            v_premi = Premi;            
            vLoadingPremi = LoadingPremi;
            vPremiDays = PremiDays;
            vPremiWeeks = PremiWeeks;
            
            
            vTotalDays = Integer.parseInt(TotalDays);
            vTotalWeeks =  (int) Double.parseDouble(TotalWeeks);
            vMaxBenefit = MaxBenefit;
            vCCOD = CCOD;
            vDCOD = DCOD;
            
            Log.i(TAG, "::getPremi:" + "ccod = " + vCCOD);
            Log.i(TAG, "::getPremi:" + "totalweek = " + TotalWeeks);
            Log.i(TAG, "::getPremi:" + "dcod = " + vDCOD);
            
	    }
	    catch (Exception ex){
	    	ex.printStackTrace();
	    }
	    finally{
	    	if (c!=null)
	    		c.close();
	    	
	    	if (c_week!=null)
	    		c_week.close();
	    	
	    	if(dba != null)
	    		dba.close();
	    	
	    	if(dbInsured != null)
	    		dbInsured.close();
	    	
	    	
	    }
		
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putAll(b);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		
		b = savedInstanceState;
		
		try{
			SPPA_ID = b.getLong("SPPA_ID");
			PRODUCT_ACTION = b.getString("PRODUCT_ACTION");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.fill_travel, menu);
		return true;
	}
	
	public void btnHomeClick(View v) {
		Intent i = new Intent(getBaseContext(),  FirstActivity.class);
		startActivity(i);
		this.finish();
	}
	
	public void btnBackClick(View v) {
		Back();
	}
	
	
public void btnDeleteClick(View v){
		
		showConfirmDelete(getBaseContext(), SPPA_ID, "").show();
	}
	
    private AlertDialog showConfirmDelete(final Context ctx, final long rowId, final String noPolis) {
	    AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
	        .setTitle("Hapus") 
	        .setMessage("Hapus data ini sekarang?") 
	        .setIcon(R.drawable.delete)
	        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

	        	public void onClick(DialogInterface dialog, int whichButton) { 
    				new DeleteSPPA().execute();
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
	
	private void Back(){
		try{
			
			Intent i = null;
			
			if (PRODUCT_ACTION.equals("NEW"))
				i = new Intent(getBaseContext(),  ChooseProductActivity.class);
			else
				i = new Intent(getBaseContext(),  SyncActivity.class);
			
			startActivity(i);
			this.finish();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private void validasiNext () throws ParseException{
		flag = 0;
		
		if( customerno.getText().toString().isEmpty()){
			flag++ ;
			customerno.setHintTextColor(Color.RED);
			UIErrorMessage = "Pilih customer";
			return;
		}
		
		if( customername.getText().toString().isEmpty()){
			flag++ ;
			customername.setHintTextColor(Color.RED);
			UIErrorMessage = "Pilih customer";
			return;
		}
		
		
		if( nopass.getText().toString().isEmpty()){
			flag++ ;
			nopass.setHintTextColor(Color.RED);
			UIErrorMessage = "No passport tertanggung harus diisi";
			return;
		}
		
		/************** pasangan ************/
		if (pasangan.isChecked() &&
				(namapasangan.getText().toString().isEmpty() || 
				 etPasangan.getText().toString().isEmpty() || 
				 nopasspasangan.getText().toString().isEmpty()
				 )) {
			flag++;
			namapasangan.setHintTextColor(Color.RED);
			etPasangan.setHintTextColor(Color.RED);
			nopasspasangan.setHintTextColor(Color.RED);
			
			UIErrorMessage = "Data pasangan harus diisi";
			return;
		}
		/************** anak 1 ************/
		if (anak1.isChecked() && 
				(namaanak1.getText().toString().isEmpty() || 
				 etAnak1.getText().toString().isEmpty() ||
				 nopassanak1.getText().toString().isEmpty() )) {
			flag++;
			
			namaanak1.setHintTextColor(Color.RED);
			etAnak1.setHintTextColor(Color.RED);
			nopassanak1.setHintTextColor(Color.RED);
			
			UIErrorMessage = "Data anak pertama harus diisi";
			return;
		}	

		/************** anak 2 ************/
		
		if (anak2.isChecked() && 
				(namaanak2.getText().toString().isEmpty() || 
				 etAnak2.getText().toString().isEmpty() ||
				 nopassanak2.getText().toString().isEmpty() )) {
			flag++;
			
			namaanak2.setHintTextColor(Color.RED);
			etAnak2.setHintTextColor(Color.RED);
			nopassanak2.setHintTextColor(Color.RED);
			
			UIErrorMessage = "Data anak kedua harus diisi";
			return;
		}		
		/************** anak 3 ************/
		
		if (anak3.isChecked() && 
				(namaanak3.getText().toString().isEmpty() || 
				 etAnak3.getText().toString().isEmpty() ||
				 nopassanak3.getText().toString().isEmpty() )) {
			flag++;
			
			namaanak3.setHintTextColor(Color.RED);
			etAnak3.setHintTextColor(Color.RED);
			nopassanak3.setHintTextColor(Color.RED);
			
			UIErrorMessage = "Data anak ketiga harus diisi";
			return;
		}		
		/********************************/
		
		if (grup.isChecked() && namaGrupTour.getText().toString().isEmpty()) {
			flag++ ;
			
			namaGrupTour.setHintTextColor(Color.RED);			
			UIErrorMessage = "Nama grup tur harus diisi";
			return;
			
		}
		if (grup.isChecked() && (jumlahTertanggung.getText().toString().isEmpty() || jumlahTertanggung.getText().toString().equals("0"))) {
			flag++ ;
			
			UIErrorMessage = "Harap masukkan data tertanggung";
			return;
		}
		
		if( namanegara1.getText().toString().isEmpty()){
			flag++ ;
			
			namanegara1.setHintTextColor(Color.RED);			
			UIErrorMessage = "Nama Negara harus dipilih";
			return;
		}
		
		if (namaahliwaris.getText().toString().isEmpty() ){
			flag++ ;
			namaahliwaris.setHintTextColor(Color.RED);
			UIErrorMessage = "Nama ahli waris harus diisi";
			return;
		}
		if (hub.getText().toString().isEmpty() ){
			flag++ ;
			hub.setHintTextColor(Color.RED);
			UIErrorMessage = "Hubungan tertanggung dengan ahli waris harus diisi";
			return;
		}
		
		if (etFromDate.getText().toString().isEmpty() ){
			flag++ ;
			etFromDate.setHintTextColor(Color.RED);
			UIErrorMessage = "Tanggal mulai harus diisi";
			return;
		}
		if( etToDate.getText().toString().isEmpty() ){
			flag++ ;
			etToDate.setHintTextColor(Color.RED);
			UIErrorMessage = "Tanggal berakhir harus diisi";
			return;
		}
		
		if (!Utility.validasiEffDate(etFromDate.getText().toString(), Utility.GetTodayString(), getBaseContext()))
		{
			flag++ ;
			etFromDate.setTextColor(Color.RED);
			UIErrorMessage = "Tanggal mulai minimal hari hari";
			return;
		} 
		if (!Utility.validasiEffDate(etToDate.getText().toString(), etFromDate.getText().toString(), getBaseContext()))
		{
			flag++ ;
			etToDate.setTextColor(Color.RED);
			UIErrorMessage = "Tanggal kembali tidak sesuai";
			return;
		} 
		  
	
		if (jumlahhari.getText().toString().isEmpty() ){
			flag++ ;
			jumlahhari.setHintTextColor(Color.RED);
			UIErrorMessage = "Jumlah hari tidak bisa dihitung. Hubungi IT ACA";
			return;
		}
		
		
		if( premi.getText().toString().isEmpty()){
			flag++ ;
			premi.setHintTextColor(Color.RED);
			UIErrorMessage = "Premi tidak bisa dihitung. Hubungi IT ACA";
			return;
		}
		if (Utility.parseDouble(discpct) > MAX_DISCOUNT){
			flag++;
			discpct.setTextColor(Color.RED);
			UIErrorMessage = "% diskon terlalu besar, maksimum adalah " + String.valueOf(MAX_DISCOUNT) + "%";
			return;
		}
		
		
		
	}

	public void btnNextClick(View v) {
		try{
			validasiNext();
			
			if(flag != 0)
				Utility.showCustomDialogInformation(FillTravelActivity.this, "Warning", 
						UIErrorMessage);	
			else{
				calculateAll();
				insertDB();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private void getDiscount() throws ParseException {
		v_discpct = Utility.parseDouble(discpct);
		v_disc = v_premi * v_discpct / 100;
	}
	
	private void getMaterai() {
		v_materai = 0; //1.50;
	}

	private void getPolis() {
		v_polis =  0;
	}
	
	private void getTotal() {
		v_total = Utility.countTotal(v_premi, v_polis, v_materai, v_disc);
	}

	
	public void btnChooseCustomerClick(View v) {
		Intent i = new Intent(getBaseContext(),  ChooseCustomerForBuyActivity.class);
		i.putExtras(b);
		startActivity(i);
		this.finish();
//		
//		if (customerno.getText().length() == 0) {
//			Intent i = new Intent(getBaseContext(),  ChooseCustomerForBuyActivity.class);
//			i.putExtras(b);
//			startActivity(i);
//			this.finish();
//		}
//		else {
//			try {
//				custList = Utility.getCustData(getBaseContext(), customerno.getText().toString());
//				if(custList != null)
//					customername.setText(custList.get(0).get("CUSTOMER_NAME"));
//				else {
//					customerno.setTextColor(Color.RED);
//					Toast.makeText(getBaseContext(), "Nasabah tidak ditemukan", Toast.LENGTH_SHORT).show();
//					customername.setText("");
//				}
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
	}
		
	private void getPremi(){
			
		DBA_MASTER_TRAVELSAFE_INT_RATE dba = null;
		
		Cursor c = null, c_week = null;
		
		String Area = null, Coverage, CCOD = "", DCOD = null, TotalDays, TotalWeeks;
		double MaxBenefit, Week, PremiDays, PremiWeeks, LoadingPremi, Premi, Premi0;	
		Integer Duration, DurationCode = null, Age;
		
		Double premiBda = 0.0;
		
	    Week = 0;
	    Duration = 0;
	    Coverage = "";

	    try{
      
	    	Duration = Utility.DATEDIFF(etFromDate.getText().toString(), etToDate.getText().toString()) + 1;
    	
	    	Log.d ("Duration", String.valueOf(Duration));
	    	 
	    	Calendar cal = Calendar.getInstance();
	    	SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	    	String formattedDate = df.format(cal.getTime());
		
	    	if(custList == null)
	    		return;
    	
	    	Age = Utility.getAge(Utility.getFormatDate(custList.get(0).get("CUSTOMER_DOB").toString()), Utility.GetTodayString());
	    	Log.d ("Age", String.valueOf(Age));
	    	
	    	
    		String lfam = null, choosenPlan, tujuan, flagAnnual;
		
    		if (fam.isChecked()) 
    			lfam = "1"; 
    		else if (individu.isChecked())
    			lfam = "0";
    		else if (grup.isChecked())
    			lfam = "2";
		
    		if (asia.isChecked())
    			tujuan = "1"; 
    		else 
    			tujuan = "2";
//    		schengen juga 2

	        Area = tujuan;
	        
    		if (annual.isChecked()) flagAnnual = "1"; else flagAnnual = "0";
		
			if(vip.isChecked())
				choosenPlan = "0";
			else if(executive.isChecked())
				choosenPlan = "1";
			else if(duluxe.isChecked())
				choosenPlan = "2";
			else
				choosenPlan = "3";
		  
			if (lfam.equals("2")) {
				if (tujuan.equals("1")) {
					Coverage = "491";
					CCOD =  Coverage;
				}
				else if (tujuan.equals("2")) {
					Coverage = "492";
					CCOD = Coverage;
				}
			}
			else {  
		        if (choosenPlan.equals("0")) {
		            if (lfam.equals("0")){
		                Coverage = "49V";
		                CCOD = "49V";
		            }
		            else if (lfam.equals("1")){
		                Coverage = "95V";
		                CCOD = "49V";
		            }
		            else if (lfam.equals("2")){
		                Coverage = "491";
		                CCOD = "491";
		            }
		            
		        }
		        else if (choosenPlan.equals("1")) {
		            if (lfam.equals("0")) {
		                Coverage = "49E";
		                CCOD = "49E";
		            }
		            else if (lfam.equals("1")){
		                Coverage = "95E";
		                CCOD = "49E";
		            }
		        }
		        else if (choosenPlan.equals("2")){
		            if (lfam.equals("0")){
		                Coverage = "49D";
		                CCOD = "49D";
		            }
		            else if (lfam.equals("1")){
		                Coverage = "95D";
		                CCOD = "49D";
		            }
		        }
		        else if (choosenPlan.equals("3")){
		        	if (lfam.equals("0")){
		                Coverage = "49S";
		                CCOD = "49S";
		        	}
		            else if (lfam.equals("1")){
		                Coverage = "95S";
		                CCOD = "49S";
		            }
		        }
			}
			
			if (lfam.equals("2")) {
				if (Duration <= 5) {
					DurationCode = 1;
					DCOD = DurationCode + "";
				}
				else if (Duration <= 8) {
					DurationCode = 2;
					DCOD = DurationCode + "";
				}
				else if (Duration <= 15) {
					DurationCode = 3;
					DCOD = DurationCode + "";
				}
				else if (Duration <= 22) {
					DurationCode = 4;
					DCOD = DurationCode + "";
				}
				else if (Duration <= 30) {
					DurationCode = 5;
					DCOD = DurationCode + "";
				}
//				annual sama loading weeks group ?
				
//				 else {
//			            if (flagAnnual.equals("1")){
//			                DurationCode = 0;
//			                DCOD = "0";
//			            }
//			            else{
//			                DurationCode = 8;
//			                Week = (Duration - 31) / 7.0;
//			                Week = Math.ceil(Week);
//			                Log.d ("week", nf.format(Week));
//			                Duration = 31;
//			                DCOD = "9";
//			            }
//			        }

			}
				else {
	       
		        if (Duration <= 4 ){
		            DurationCode = 1;
		            DCOD = "1";
		        }
		        else if (Duration <= 6) {
		            DurationCode = 2;
		            DCOD = "2";
		        }
		        else if (Duration <= 8) {
		            DurationCode = 3;
		            DCOD = "3";
		        }
		        else if (Duration <= 10) {
		            DurationCode = 4;
		            DCOD = "4";
		        }
		        else if (Duration <= 15) {
		            DurationCode = 5;
		            DCOD = "5";
		        }
		        else if (Duration <= 20) {
		            DurationCode = 6;
		            DCOD = "6";
		        }
		        else if (Duration <= 25) {
		            DurationCode = 7;
		            DCOD = "7";
		        }
		        else if (Duration <= 31) {
		            DurationCode = 8;
		            DCOD = "8";
		        }
		        else {
		            if (flagAnnual.equals("1")){
		                DurationCode = 0;
		                DCOD = "0";
		            }
		            else{
	            	  if (Duration > 94) {
		                	double weekBda = Math.ceil((Duration - 94) / 7.0);
		                	premiBda =  weekBda *  getPremiBDA(Area, Coverage);
		                	Log.i(TAG, "::getPremiBDA:" + premiBda);
		                }
		            	  
		                DurationCode = 8;
		                Week = (Duration - 31) / 7.0;
		                Week = Math.ceil(Week);
		                Log.d ("week", nf.format(Week));
		                Duration = 31;
		                DCOD = "9";
		                
		              
		            }
		        }
        
			}
	        
			dba = new DBA_MASTER_TRAVELSAFE_INT_RATE(getBaseContext());
			dba.open();
	
			Log.d ("Area", Area);
			Log.d ("Coverage", Coverage);
			Log.d ("DurationCode", String.valueOf(DurationCode));
			Log.d ("Week", String.valueOf(Week));


			c = dba.getRate(Area, Coverage, String.valueOf(DurationCode));
		 
			if (Week > 0){
				c_week = dba.getRate(Area, Coverage, "9");
            	
            	if(!c_week.moveToFirst())            		
                    PremiWeeks = 0.0;
                else
                    PremiWeeks = c_week.getDouble(3) * Week;
			}
            else
                PremiWeeks = 0.0;
			 
            
            if (!c.moveToFirst()){
                PremiDays = 0.0;
                MaxBenefit = 0.00;
            }
            else {
            	
            	
                PremiDays = c.getDouble(3);
                
                String DestinationCountry = namanegara1.getTag().toString();  
                
                if (Area.equals("1")) {
                	if (DestinationCountry.equals("HK") ||
            			DestinationCountry.equals("BN") || 
            			DestinationCountry.equals("MY") || 
            			DestinationCountry.equals("SG") || 
            			DestinationCountry.equals("JP") || 
            			DestinationCountry.equals("KR") || 
            			DestinationCountry.equals("KP") )
                        MaxBenefit = c.getDouble(4);
                    else
                        MaxBenefit = c.getDouble(4) * 0.75;
                }
                else
                    MaxBenefit = c.getDouble(4);
            }
 
            TotalDays = String.valueOf(Duration);
            TotalWeeks = String.valueOf(Week);
 
            Premi0 = PremiDays + PremiWeeks + premiBda;
 
            if (Age >= 70) 
                LoadingPremi = (0.5 * (Premi0));
            else
                LoadingPremi = 0.0;
            
            Premi = Premi0 + LoadingPremi;
            
            
            v_premi = Premi;            
            vLoadingPremi = LoadingPremi;
            vPremiDays = PremiDays;
            vPremiWeeks = PremiWeeks + premiBda;
            
            
            vTotalDays = Integer.parseInt(TotalDays);
            vTotalWeeks =  (int) Double.parseDouble(TotalWeeks);
            vMaxBenefit = MaxBenefit;
            vCCOD = CCOD;
            vDCOD = DCOD;
            
            Log.i(TAG, "::getPremi:" + "ccod = " + vCCOD);
            Log.i(TAG, "::getPremi:" + "totalweek = " + TotalWeeks);
            Log.i(TAG, "::getPremi:" + "dcod = " + vDCOD);
            
	    }
	    catch (Exception ex){
	    	ex.printStackTrace();
	    }
	    finally{
	    	if (c!=null)
	    		c.close();
	    	
	    	if (c_week!=null)
	    		c_week.close();
	    	
	    	if(dba != null)
	    		dba.close();
	    }
	}
	

	private Double getPremiBDA(String area, String coverage) {
		 
		DBA_MASTER_TRAVELSAFE_INT_RATE dba = new DBA_MASTER_TRAVELSAFE_INT_RATE(this);
		double premiBda = 0.0;
		Cursor cursor = null;
		try {
			dba.open();
			cursor = dba.getRate(area, coverage, "A");
			cursor.moveToFirst();
			premiBda = cursor.getDouble(3);
			return premiBda;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dba != null)
				dba.close();
		}
		return 0.0;
	 
	}
	private Double GetPremiumAlokasi() {
		DBA_MASTER_TRAVELSAFE_INT_RATE dba = null;
		Cursor c = null;
		Cursor c_week = null;
		
		String Area ;
		
		Integer Duration ; 
		Integer DurationCode ; 
		Integer Week ; 
		
		String Coverage ; 
		
		Integer Age ; 
		
		Double premi = 0.00 ; 
		Double premiWeeks ; 
		Double LPremi ;
	
	    Week = 0 ;
	    Duration = 0; 
	    Coverage = "";

    try{

    	Duration = Utility.DATEDIFF(etFromDate.getText().toString(), etToDate.getText().toString()) ;

    	Calendar cal = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String formattedDate = df.format(cal.getTime());
		
		if(custList == null)
			return 0.0;
    	
		Age = Utility.getAge(Utility.getFormatDate(custList.get(0).get("CUSTOMER_DOB").toString()), Utility.GetTodayString());
		String lfam, choosenPlan, tujuan, flagAnnual;
		
		if(fam.isChecked())
			lfam = "1";
		else
			lfam = "0";
		
		
		if(asia.isChecked())
			tujuan = "A";
		else
			tujuan = "B";
		
		if (annual.isChecked())
			flagAnnual = "1";
		else 
			flagAnnual = "0";
		
		if(vip.isChecked())
			choosenPlan = "0";
		else if(executive.isChecked())
			choosenPlan = "1";
		else if(duluxe.isChecked())
			choosenPlan = "2";
		else
			choosenPlan = "3";
		   
		Area = tujuan;
		
		String namaNegara = namanegara1.getText().toString().toLowerCase();
		
		if(namaNegara.contains("spain")) {
			if (Duration < 30)
				Duration = 30;
		}
		else if (namaNegara.contains("norway")){
			if(Duration < 8)
				Duration = 8;
			Duration += 15;
		}

        if (choosenPlan.equals("0")) {
            if (lfam.equals("0")){
                Coverage = "49V";
            }
            else if (lfam.equals("1")){
                Coverage = "95V";
            }
        }

        else if (choosenPlan.equals("1")) {
            if (lfam.equals("0")) {
                Coverage = "49E";
            }
            else if (lfam.equals("1")){
                Coverage = "95E";
            }
        }
        else if (choosenPlan.equals("2")){
            if (lfam.equals("0")){
                Coverage = "49D";
            }
            else if (lfam.equals("1")){
                Coverage = "95D";
            }
        }
        else if (choosenPlan.equals("3")){
        	if (lfam.equals("0")){
                Coverage = "49S";
        	}
            else if (lfam.equals("1")){
                Coverage = "95S";
            }
        }

        if (Duration <= 4)
            DurationCode = 1;
        else if (Duration <= 6)
            DurationCode = 2;
        else if (Duration <= 8)
            DurationCode = 3;
        else if (Duration <= 10)
            DurationCode = 4;
        else if (Duration <= 15)
            DurationCode = 5;
        else if (Duration <= 20)
            DurationCode = 6;
        else if (Duration <= 25)
            DurationCode = 7;
        else if (Duration <= 31)
            DurationCode = 8;
        else {
            if (flagAnnual.equals("1")){
                DurationCode = 0;
            }
            else{
                DurationCode = 8;
                Week = (Duration - 31) / 7;
                Duration = 31;
            }
        }

    	dba = new DBA_MASTER_TRAVELSAFE_INT_RATE(getBaseContext());
		dba.open();

		c = dba.getRate(Area, Coverage, String.valueOf(DurationCode));
		
		if (Week > 0){
	        	
	    	c_week = dba.getRate(Area, Coverage, "9");
	        	
	    	if(!c_week.moveToFirst())            		
	    		premiWeeks = 0.0;
	        else
	        	premiWeeks = c_week.getDouble(3) * Week;
		}
        else
        	premiWeeks = 0.0;

          
		if (!c.moveToFirst())
			premi = 0.0;
		else
    	  premi = c.getDouble(3) + premiWeeks;

        if (Age >= 70) 
            LPremi = (0.5 * premi);
        else
            LPremi = 0.00;
        
        premi = premi + LPremi;

    }
    catch (Exception ex) {
    	
    }
    finally{
    	
    	if (c != null)
    		c.close();
    	
    	if (c_week != null)
    		c_week.close();
    	
    	if(dba != null)
    		dba.close();
    }
    
    return premi ;
}
	
    
	public void insertDB()
	{
		DBA_PRODUCT_TRAVEL_SAFE dba = null;
		DBA_PRODUCT_MAIN dba2  = null;
		DBA_INSURED_GROUP dbInsured = null;
		Cursor cInsured = null;
		
		try{
			
			if (PRODUCT_ACTION.equals("VIEW"))
			{
				Intent i = new Intent(getBaseContext(),  ConfirmActivity.class);
				b.putLong("SPPA_ID", SPPA_ID);
				i.putExtras(b);
				startActivity(i);
				this.finish();
			}
			else
			{
				dba = new DBA_PRODUCT_TRAVEL_SAFE(getBaseContext());
				dba2 = new DBA_PRODUCT_MAIN(getBaseContext());
				dbInsured = new DBA_INSURED_GROUP(getBaseContext());
				
				dba.open();
				dba2.open();
				dbInsured.open();
				
				cInsured = dbInsured.getRow(PRODUCT_MAIN_ID);
				if (grup.isChecked())
					cInsured.moveToFirst();
				
				
				String  lfam = null, 
						choosenPlan, 
						tujuan, 
						annual;
				
				String sPasangan = "0";
				String sAnak1 = "0";
				String sAnak2 = "0";
				String sAnak3 = "0";
				
				if (this.annual.isChecked()) annual = "1" ; else annual = "0";
				
				if (fam.isChecked()) 
	    			lfam = "1"; 
	    		else if (individu.isChecked())
	    			lfam = "0";
	    		else if (grup.isChecked())
	    			lfam = "2";
				 
		
				if(asia.isChecked()) tujuan = "1"; else tujuan = "2";
//				schengen juga 2
				
				if(vip.isChecked())
					choosenPlan = "0";
				else if(executive.isChecked())
					choosenPlan = "1";
				else if(duluxe.isChecked())
					choosenPlan = "2";
				else
					choosenPlan = "3";
				
				if (pasangan.isChecked())
					sPasangan = "1";
				if (anak1.isChecked())
					sAnak1 = "1";
				if (anak2.isChecked())
					sAnak2 = "1";
				if (anak3.isChecked())
					sAnak3 = "1";
				
				if (PRODUCT_ACTION.equals("NEW")) {
					SPPA_ID = dba2.initialInsert(customerno.getText().toString(), 
							customername.getText().toString(), "TRAVELSAFE",
							nf.parse(premi.getText().toString()).doubleValue() * v_exchange_rate,
							v_materai  * v_exchange_rate,
							nf.parse(polis.getText().toString()).doubleValue()  * v_exchange_rate,
							nf.parse(total.getText().toString()).doubleValue()  * v_exchange_rate,
							etFromDate.getText().toString(), 
							etToDate.getText().toString(),
							Utility.DateToString(Utility.GetToday(), "dd/MM/yyyy"),
							"","","","N","",
							nf.parse((discpct.getText().toString())).doubleValue(),
							nf.parse((disc.getText().toString())).doubleValue());
					
					if (grup.isChecked()) {
						dba.initialInsert(SPPA_ID, 
								cInsured.getString(2), cInsured.getString(3), cInsured.getString(4),
								cInsured.getString(5), cInsured.getString(6), cInsured.getString(7),
								cInsured.getString(8), cInsured.getString(9), cInsured.getString(10),
								cInsured.getString(11), cInsured.getString(12), cInsured.getString(13),
								
								(spTujuan.getSelectedItem().toString()), 
								tujuan, 
								namanegara1.getText().toString(),
								choosenPlan, 
								namaahliwaris.getText().toString().toUpperCase(), 
								hub.getText().toString().toUpperCase(), 
								etFromDate.getText().toString(),
								etToDate.getText().toString(), 
								nf.parse(jumlahhari.getText().toString()).doubleValue(),
								nf.parse(tambahan.getText().toString()).doubleValue(), 
								nf.parse(loadingpremi.getText().toString()).doubleValue(),
								lfam,
								nf.parse(premi.getText().toString()).doubleValue(), 
								nf.parse(polis.getText().toString()).doubleValue(), 
								nf.parse(total.getText().toString()).doubleValue(), 
								
								"TRAVELSAFE", 
								customername.getText().toString(),
								
								premi_tertanggung_grup, 
								premi_tertanggung_grup, 
								premi_tertanggung_grup, 
								premi_tertanggung_grup, 
								
								cInsured.getString(34),
								cInsured.getString(35),
								cInsured.getString(36),
								cInsured.getString(37),
								
								
								namanegara1.getTag().toString(),
								
								annual,
								v_exchange_rate,
								tujuan,
								vCCOD, 
								vDCOD, 
								vPremiDays,
								vPremiWeeks, 
								vMaxBenefit, 
								vTotalDays,
								vTotalWeeks, 
								
								GetPremiumAlokasi(),
								nopass.getText().toString(),
								
							    cInsured.getString(14), cInsured.getString(15), cInsured.getString(16),
							    cInsured.getString(17), cInsured.getString(18), cInsured.getString(19),
							    cInsured.getString(20), cInsured.getString(21), cInsured.getString(22),
							    cInsured.getString(23), cInsured.getString(24), cInsured.getString(25),
							    cInsured.getString(26), cInsured.getString(27), cInsured.getString(28),
							    cInsured.getString(29), cInsured.getString(30), cInsured.getString(31),
							    
							    cInsured.getInt(38), 
							    cInsured.getInt(39), 
							    cInsured.getInt(40), 
							    cInsured.getInt(41), 
							    cInsured.getInt(42), 
							    cInsured.getInt(43), 
							    
							    premi_tertanggung_grup,
							    premi_tertanggung_grup,
							    premi_tertanggung_grup,
							    premi_tertanggung_grup,
							    premi_tertanggung_grup,
							    premi_tertanggung_grup,

							    
								namanegara2.getText().toString(), namanegara2.getTag().toString(),
								namanegara3.getText().toString(), namanegara3.getTag().toString(),
								namanegara4.getText().toString(), namanegara4.getTag().toString(),
								namanegara5.getText().toString(), namanegara5.getTag().toString(),
								namanegara6.getText().toString(), namanegara6.getTag().toString(),
								
								PRODUCT_MAIN_ID,
								namaGrupTour.getText().toString(),
								INSURED_COUNT
								);
					}
					else {
						dba.initialInsert(SPPA_ID, 
							namapasangan.getText().toString().toUpperCase(), etPasangan.getText().toString(), nopasspasangan.getText().toString().toUpperCase(), 
							namaanak1.getText().toString().toUpperCase(), etAnak1.getText().toString(), nopassanak1.getText().toString().toUpperCase(), 
							namaanak2.getText().toString().toUpperCase(), etAnak2.getText().toString(), nopassanak2.getText().toString().toUpperCase(),
							namaanak3.getText().toString().toUpperCase(), etAnak3.getText().toString(), nopassanak3.getText().toString().toUpperCase(), 
							(spTujuan.getSelectedItem().toString()), 
							tujuan, 
							namanegara1.getText().toString(),
							choosenPlan, 
							namaahliwaris.getText().toString().toUpperCase(), 
							hub.getText().toString().toUpperCase(), 
							etFromDate.getText().toString(),
							etToDate.getText().toString(), 
							nf.parse(jumlahhari.getText().toString()).doubleValue(),
							nf.parse(tambahan.getText().toString()).doubleValue(), 
							nf.parse(loadingpremi.getText().toString()).doubleValue(),
							lfam,
							nf.parse(premi.getText().toString()).doubleValue(), 
							nf.parse(polis.getText().toString()).doubleValue(), 
							nf.parse(total.getText().toString()).doubleValue(), 
							"TRAVELSAFE", 
							customername.getText().toString(),
							v_premi_pasangan, v_premi_anak_1, v_premi_anak_2, v_premi_anak_3,
							sPasangan, sAnak1, sAnak2, sAnak3,
							namanegara1.getTag().toString(),
							annual,
							v_exchange_rate,
							tujuan,
							vCCOD, vDCOD, vPremiDays, vPremiWeeks, vMaxBenefit, vTotalDays,vTotalWeeks, GetPremiumAlokasi(),
							nopass.getText().toString(),
							
							"", "", "",
							"", "", "",
							"", "", "",
							"", "", "",
							"", "", "",
							"", "", "",
						    
							0,
							0,
							0,
							0,
						    0,
						    0,
						    
						    0.0,
						    0.0,
						    0.0,
						    0.0,
						    0.0,
						    0.0,
						    
							namanegara2.getText().toString(), namanegara2.getTag().toString(),
							namanegara3.getText().toString(), namanegara3.getTag().toString(),
							namanegara4.getText().toString(), namanegara4.getTag().toString(),
							namanegara5.getText().toString(), namanegara5.getTag().toString(),
							namanegara6.getText().toString(), namanegara6.getTag().toString(),

							PRODUCT_MAIN_ID,
							namaGrupTour.getText().toString(),
							INSURED_COUNT
							
							);
					}
				}
				else{
					dba2.nextInsert(SPPA_ID, customerno.getText().toString(), 
							customername.getText().toString(), 
							nf.parse(premi.getText().toString()).doubleValue()  * v_exchange_rate,
							v_materai  * v_exchange_rate,
							nf.parse(polis.getText().toString()).doubleValue()  * v_exchange_rate,
							nf.parse(total.getText().toString()).doubleValue()  * v_exchange_rate,
							etFromDate.getText().toString(), 
							etToDate.getText().toString(),
							Utility.DateToString(Utility.GetToday(), "dd/MM/yyyy"),
							nf.parse((discpct.getText().toString())).doubleValue(),
							nf.parse((disc.getText().toString())).doubleValue());
					
					if (grup.isChecked()) {
						dba.nextInsert(SPPA_ID, 
								cInsured.getString(2), cInsured.getString(3), cInsured.getString(4),
								cInsured.getString(5), cInsured.getString(6), cInsured.getString(7),
								cInsured.getString(8), cInsured.getString(9), cInsured.getString(10),
								cInsured.getString(11), cInsured.getString(12), cInsured.getString(13),
								
								(spTujuan.getSelectedItem().toString()), 
								tujuan, 
								namanegara1.getText().toString(),
								choosenPlan, 					
								namaahliwaris.getText().toString().toUpperCase(), 
								hub.getText().toString().toUpperCase(), 
								etFromDate.getText().toString(),
								etToDate.getText().toString(), 
								nf.parse(jumlahhari.getText().toString()).doubleValue(), 
								nf.parse(tambahan.getText().toString()).doubleValue(), 
								nf.parse(loadingpremi.getText().toString()).doubleValue(),
								lfam,
								nf.parse(premi.getText().toString()).doubleValue(), 
								nf.parse(polis.getText().toString()).doubleValue(), 
								nf.parse(total.getText().toString()).doubleValue(), 
								"TRAVELSAFE", 
								customername.getText().toString(),
								
								premi_tertanggung_grup, 
								premi_tertanggung_grup, 
								premi_tertanggung_grup, 
								premi_tertanggung_grup, 
								
								cInsured.getString(34),
								cInsured.getString(35),
								cInsured.getString(36),
								cInsured.getString(37),
								
								namanegara1.getTag().toString(),
								annual,
								v_exchange_rate,
								tujuan,
								vCCOD, vDCOD, vPremiDays, vPremiWeeks, vMaxBenefit, vTotalDays,vTotalWeeks, GetPremiumAlokasi(),
								nopass.getText().toString(),
								
							    cInsured.getString(14), cInsured.getString(15), cInsured.getString(16),
							    cInsured.getString(17), cInsured.getString(18), cInsured.getString(19),
							    cInsured.getString(20), cInsured.getString(21), cInsured.getString(22),
							    cInsured.getString(23), cInsured.getString(24), cInsured.getString(25),
							    cInsured.getString(26), cInsured.getString(27), cInsured.getString(28),
							    cInsured.getString(29), cInsured.getString(30), cInsured.getString(31),
							    
							    cInsured.getInt(38), 
							    cInsured.getInt(39), 
							    cInsured.getInt(40), 
							    cInsured.getInt(41), 
							    cInsured.getInt(42), 
							    cInsured.getInt(43), 
							    
							    premi_tertanggung_grup,
							    premi_tertanggung_grup,
							    premi_tertanggung_grup,
							    premi_tertanggung_grup,
							    premi_tertanggung_grup,
							    premi_tertanggung_grup,
							    
 
							    
								namanegara2.getText().toString(), namanegara2.getTag().toString(),
								namanegara3.getText().toString(), namanegara3.getTag().toString(),
								namanegara4.getText().toString(), namanegara4.getTag().toString(),
								namanegara5.getText().toString(), namanegara5.getTag().toString(),
								namanegara6.getText().toString(), namanegara6.getTag().toString(),

								PRODUCT_MAIN_ID,
								namaGrupTour.getText().toString(),
								INSURED_COUNT
								
								);
					}
					else {
						dba.nextInsert(SPPA_ID, 
								namapasangan.getText().toString().toUpperCase(), etPasangan.getText().toString(), nopasspasangan.getText().toString().toUpperCase(), 
								namaanak1.getText().toString().toUpperCase(), etAnak1.getText().toString(), nopassanak1.getText().toString().toUpperCase(), 
								namaanak2.getText().toString().toUpperCase(), etAnak2.getText().toString(), nopassanak2.getText().toString().toUpperCase(),
								namaanak3.getText().toString().toUpperCase(), etAnak3.getText().toString(), nopassanak3.getText().toString().toUpperCase(), 
								(spTujuan.getSelectedItem().toString()), 
								tujuan, 
								namanegara1.getText().toString(),
								choosenPlan, 					
								namaahliwaris.getText().toString().toUpperCase(), 
								hub.getText().toString().toUpperCase(), 
								etFromDate.getText().toString(),
								etToDate.getText().toString(), 
								nf.parse(jumlahhari.getText().toString()).doubleValue(), 
								nf.parse(tambahan.getText().toString()).doubleValue(), 
								nf.parse(loadingpremi.getText().toString()).doubleValue(),
								lfam,
								nf.parse(premi.getText().toString()).doubleValue(), 
								nf.parse(polis.getText().toString()).doubleValue(), 
								nf.parse(total.getText().toString()).doubleValue(), 
								"TRAVELSAFE", 
								customername.getText().toString(),
								v_premi_pasangan, v_premi_anak_1, v_premi_anak_2, v_premi_anak_3,
								sPasangan, sAnak1, sAnak2, sAnak3,
								namanegara1.getTag().toString(),
								annual,
								v_exchange_rate,
								tujuan,
								vCCOD, vDCOD, vPremiDays, vPremiWeeks, vMaxBenefit, vTotalDays,vTotalWeeks, GetPremiumAlokasi(),
								nopass.getText().toString(),
								
//							    cInsured.getString(14), cInsured.getString(15), cInsured.getString(16),
//							    cInsured.getString(17), cInsured.getString(18), cInsured.getString(19),
//							    cInsured.getString(20), cInsured.getString(21), cInsured.getString(22),
//							    cInsured.getString(23), cInsured.getString(24), cInsured.getString(25),
//							    cInsured.getString(26), cInsured.getString(27), cInsured.getString(28),
//							    cInsured.getString(29), cInsured.getString(30), cInsured.getString(31),
//							    
//							    cInsured.getInt(38), 
//							    cInsured.getInt(39), 
//							    cInsured.getInt(40), 
//							    cInsured.getInt(41), 
//							    cInsured.getInt(42), 
//							    cInsured.getInt(43), 
//							    
//							    premi_tertanggung_grup,
//							    premi_tertanggung_grup,
//							    premi_tertanggung_grup,
//							    premi_tertanggung_grup,
//							    premi_tertanggung_grup,
//							    premi_tertanggung_grup,
								
								"", "", "",
								"", "", "",
								"", "", "",
								"", "", "",
								"", "", "",
								"", "", "",
							    
								0,
								0,
								0,
								0,
							    0,
							    0,
							    
							    0.0,
							    0.0,
							    0.0,
							    0.0,
							    0.0,
							    0.0,

							    
								namanegara2.getText().toString(), namanegara2.getTag().toString(),
								namanegara3.getText().toString(), namanegara3.getTag().toString(),
								namanegara4.getText().toString(), namanegara4.getTag().toString(),
								namanegara5.getText().toString(), namanegara5.getTag().toString(),
								namanegara6.getText().toString(), namanegara6.getTag().toString(),

								PRODUCT_MAIN_ID,
								namaGrupTour.getText().toString(),
								INSURED_COUNT
								);
					}
				
				}
				
				Intent i = new Intent(getBaseContext(),  ConfirmActivity.class);
				b.putLong("SPPA_ID",  SPPA_ID);
				i.putExtras(b);
				startActivity(i);
				this.finish();
			}
		
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			if (dba != null)
				dba.close();
			
			if (dba2 != null)
				dba2.close();
			

			if (dbInsured != null)
				dbInsured.close();
			
			if (cInsured != null)
				cInsured.close();
		}
	}

	private class RetrivePremi extends AsyncTask<String, Void, Void>{

		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();

			progressBar = new ProgressDialog(FillTravelActivity.this);
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
			
				envelope.setOutputSoapObject(requestretrive);
	    		envelope.bodyOut = requestretrive;
	    		androidHttpTransport.call(SOAP_ACTION_EXRATE, envelope);  

	    		SoapObject result = (SoapObject)envelope.bodyIn;
	        	String exrate = result.getProperty(0).toString(); 
	        	v_exchange_rate = Double.parseDouble(exrate);
	        	Log.d("er", String.valueOf(v_exchange_rate));
			}
			catch (Exception e) {
        		error = true;
        		e.printStackTrace();	        		
//				errorMessage = new MasterExceptionClass(e).getException();
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {

			super.onPostExecute(result);
			
			progressBar.hide();
			progressBar.dismiss();
			
			try{	
				if(error) {
					Toast.makeText(FillTravelActivity.this,
							"Gagal mendapatkan rate premi",
							Toast.LENGTH_SHORT).show();
					
					startActivity(new Intent(FillTravelActivity.this, ChooseProductActivity.class));
					FillTravelActivity.this.finish();
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		DatePickerDialog dp = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		c = Calendar.getInstance();
	
		
		int Y = c.get(Calendar.YEAR);
		int M = c.get(Calendar.MONTH);
		int D =  c.get(Calendar.DAY_OF_MONTH);
		
		switch (id) {
		/*
			case DATE_FROM_ID:
				dp =  new DatePickerDialog(this, datePickerListener, Y, M, D); 
				return dp;
				
			case DATE_TO_ID:

				long maxDate = 0;
				
				try 
				{	
					if (!etToDate.getText().toString().isEmpty())
					{
						c.setTime(dateFormat.parse(etToDate.getText().toString()));

						Y = c.get(Calendar.YEAR);
						M = c.get(Calendar.MONTH);
						D = c.get(Calendar.DAY_OF_MONTH);	
					}
										
					dp = new DatePickerDialog(this, datePickerListenerNext, Y , M, D + 1);
					
					
					if (annual.isChecked()) 
					{
						maxDate= dateFormat.parse(Utility.getAnnualDate(etFromDate.getText().toString(), etToDate.getText().toString())).getTime();
						dp.getDatePicker().setMaxDate(maxDate);
						Log.i(TAG, "::onCreateDialog:" + " max date annual year");
					}
					else if ( grup.isChecked())
					{
						maxDate= dateFormat.parse(Utility.getAnnualMonth(etFromDate.getText().toString())).getTime();
						dp.getDatePicker().setMaxDate(maxDate);	
						Log.i(TAG, "::onCreateDialog:" + " max date annual month");
					}
					else
					{

						Log.i(TAG, "::onCreateDialog:" + " max date annual year");	
					}
					
					
				} catch (ParseException e) 
				{ 
					e.printStackTrace();
				}
				
				return dp;
				*/
			case DATE_LAHIR_PASANGAN:
				return new DatePickerDialog(this, datePickerListenerPasangan, Y-20, M, D);
			case DATE_LAHIR_ANAK1:
				return new DatePickerDialog(this, datePickerListenerAnak1, Y-10, M, D);
			case DATE_LAHIR_ANAK2:
				return new DatePickerDialog(this, datePickerListenerAnak2, Y-10, M, D);
			case DATE_LAHIR_ANAK3:
				return new DatePickerDialog(this, datePickerListenerAnak3, Y-10, M, D);
				
		}
		return null;
	}
	
	
	
	@Override
	@Deprecated
	protected void onPrepareDialog(int id, Dialog dialog) {  
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date dateFrom = null; 
//		 
		switch (id) {
			
				
			case DATE_TO_ID:
				try 
				{
					long maxDate = 0;
					
					if (annual.isChecked()) 
					{
						maxDate= dateFormat.parse(Utility.getAnnualDate(etFromDate.getText().toString(), etToDate.getText().toString())).getTime();
						((DatePickerDialog) dialog).getDatePicker().setMaxDate(maxDate);
						Log.i(TAG, "::onCreateDialog:" + " max date annual year");
					}
					else if ( grup.isChecked())
					{
						maxDate= dateFormat.parse(Utility.getAnnualMonth(etFromDate.getText().toString())).getTime();
						((DatePickerDialog) dialog).getDatePicker().setMaxDate(maxDate);	
						Log.i(TAG, "::onCreateDialog:" + " max date annual month");
					}
					else
					{ 
						Date from = dateFormat.parse(etFromDate.getText().toString());

						Calendar cal = Calendar.getInstance();
				        cal.setTime(from);
				        cal.add(Calendar.YEAR, 5); 
						((DatePickerDialog) dialog).getDatePicker().setMaxDate(cal.getTimeInMillis());	
						Log.i(TAG, "::onCreateDialog:" + " max date normal");	
					}
					
					
				} catch (ParseException e) 
				{ 
					e.printStackTrace();
				}
		}
	}
	
 
	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
			String theDate = Utility.setUIDate(selectedYear, selectedMonth, selectedDay);
			etFromDate.setText(theDate);
			etToDate.setText(Utility.GetTomorrowString(theDate));
			
			validasiTanggalAwal();
			
			
			try {
				calculateAll();
			} catch (ParseException e) { 
				e.printStackTrace();
			}
			
		}
	}; 
	private DatePickerDialog.OnDateSetListener datePickerListenerPasangan = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

			String myDate = Utility.setUIDate(selectedYear, selectedMonth, selectedDay);
			boolean flag = Utility.validasiEffDate(Utility.GetTodayString(), myDate, FillTravelActivity.this);
			
			if (flag)
				etPasangan.setText(myDate);
			else {
				Utility.showCustomDialogInformation(FillTravelActivity.this, "Informasi", "Tanggal tidak valid"); 
				etPasangan.setText("");
			}
		}
	};
	
	private DatePickerDialog.OnDateSetListener datePickerListenerAnak1 = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
			String myDate = Utility.setUIDate(selectedYear, selectedMonth, selectedDay);
			boolean flag = Utility.validasiEffDate(Utility.GetTodayString(), myDate, FillTravelActivity.this);
			
			if (flag)
				etAnak1.setText(myDate);
			else {
				Utility.showCustomDialogInformation(FillTravelActivity.this, "Informasi", "Tanggal tidak valid"); 
				etAnak1.setText("");
			}
		}
	};
	
	private DatePickerDialog.OnDateSetListener datePickerListenerAnak2 = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
			String myDate = Utility.setUIDate(selectedYear, selectedMonth, selectedDay);
			boolean flag = Utility.validasiEffDate(Utility.GetTodayString(), myDate, FillTravelActivity.this);
			
			if (flag)
				etAnak2.setText(myDate);
			else {
				Utility.showCustomDialogInformation(FillTravelActivity.this, "Informasi", "Tanggal tidak valid"); 
				etAnak2.setText(""); 
			}
			
		}
	};
	
	private DatePickerDialog.OnDateSetListener datePickerListenerAnak3 = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) { 
		    
			String myDate = Utility.setUIDate(selectedYear, selectedMonth, selectedDay);
			boolean flag = Utility.validasiEffDate(Utility.GetTodayString(), myDate, FillTravelActivity.this);
			
			if (flag)
				etAnak3.setText(myDate);
			else {
				Utility.showCustomDialogInformation(FillTravelActivity.this, "Informasi", "Tanggal tidak valid"); 
				etAnak3.setText("");
			}
			
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK)
			Back();
		
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onBackPressed() {
		Back();
	}
	
	
	private void validasiTanggalAwal () {
		((Button)findViewById(R.id.btnNext)).setEnabled(true);
		etFromDate.setTextColor(Color.BLACK);
		etToDate.setTextColor(Color.BLACK);
		boolean flag = Utility.validasiEffDate(
							etFromDate.getText().toString(), 
							Utility.GetTodayString(), getBaseContext());
		
		if (!flag) {
			etFromDate.setTextColor(Color.RED);
			Utility.showCustomDialogInformation(FillTravelActivity.this, "Warning", "Tanggal mulai minimal hari hari");
			((Button)findViewById(R.id.btnNext)).setEnabled(false);
		}
	}
	
	
	
	private void validasiTanggalAkhir () {
		((Button)findViewById(R.id.btnNext)).setEnabled(true);
		etToDate.setTextColor(Color.BLACK);
		boolean flag = Utility.validasiEffDate(etToDate.getText().toString(),
												etFromDate.getText().toString(),
												getBaseContext());
												
		if (!flag) {
			etToDate.setTextColor(Color.RED);
			Utility.showCustomDialogInformation(FillTravelActivity.this, "Informasi",
					"Tanggal kembali tidak sesuai");
			((Button)findViewById(R.id.btnNext)).setEnabled(false);
		}
	}
	
	public class DeleteSPPA extends AsyncTask<Void, Void, Void> 
    {
		private ProgressDialog progDialog = null;
		private boolean error = false;
		private String flag = "";
		
		protected void onPreExecute()  {
            progDialog = ProgressDialog.show(context, "", "Processing...",false);
        }
		
		 @Override
		 protected Void doInBackground(Void... params) {
			 
			 
				DBA_PRODUCT_MAIN dba = null;
				DBA_PRODUCT_TRAVEL_SAFE dba2 = null;
				
				Cursor c = null;
				String E_SPPA_NO = "";
				
				HttpTransportSE androidHttpTransport = null;
				String NAMESPACE = "http://tempuri.org/";  
				    
				SoapObject request = null;
			    SoapSerializationEnvelope envelope = null;
			    String URL = Utility.getURL();  
			    String SOAP_ACTION = "http://tempuri.org/DeleteSPPA";     
			    String METHOD_NAME = "DeleteSPPA";
			    
			   
			    
				try 
				{
				    dba = new DBA_PRODUCT_MAIN(context);
					dba.open();
					
					dba2 = new DBA_PRODUCT_TRAVEL_SAFE(context);
					dba2.open();

					
					//dapatin no SPPA
					c = dba.getRow(SPPA_ID);
					c.moveToNext();
					E_SPPA_NO = c.getString(5);
			            
					Log.d("-->", E_SPPA_NO);
					
					if (E_SPPA_NO == null || E_SPPA_NO.length() == 0 || !TextUtils.isDigitsOnly(E_SPPA_NO))
					{
						Log.d("-->", "EMPTY");
						
						//hapus data di local db
						dba.delete(SPPA_ID);
						dba2.delete(SPPA_ID);
						
						//hapus directory
						Utility.DeleteDirectory(SPPA_ID);
					}
					else
					{
						//coba hapus SPPA di server
						envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); 
						envelope.implicitTypes = true;
						envelope.dotNet = true;	//used only if we use the webservice from a dot net file (asmx)
			    		androidHttpTransport = new HttpTransportSE(URL);
			    		request = new SoapObject(NAMESPACE, METHOD_NAME);
			    	
			    		request.addProperty(Utility.GetPropertyInfo("SPPANo", E_SPPA_NO, String.class));
	    				
			    		envelope.setOutputSoapObject(request);
	    	    		androidHttpTransport.call(SOAP_ACTION, envelope);  
	    	    		
	    	    		SoapObject result = (SoapObject) envelope.bodyIn;					
						String response = "";
		
						if (result.getPropertyCount() == 0) {
							flag = "1";
						}
						else
						{
							response = result.getPropertySafelyAsString("DeleteSPPAResult");						
							flag = response.toString();
						}


						if (flag.equals("1")) {
							// hapus data di local db
							dba.delete(SPPA_ID);
							dba2.delete(SPPA_ID);

							// hapus directory
							Utility.DeleteDirectory(SPPA_ID);
						}
					}
             	
				}
				catch (Exception e) {
	        		error = true;
	        		e.printStackTrace();	        		
//					errorMessage = new MasterExceptionClass(e).getException();
				}
				finally {
					
					if (c != null)
						c.close();
					
					if (dba != null)
						dba.close();
					
					if (dba2 != null)
						dba2.close();
				}
				
				return null;
		 }
		 

		 protected void onPostExecute(Void result) 
		 {
			 try {
				 progDialog.dismiss();
				 progDialog = null;
				 
				 if(error)
					Toast.makeText(getBaseContext(), "Data gagal dihapus", Toast.LENGTH_SHORT).show();
				 else{	
					Toast.makeText(getBaseContext(), "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
					 
					 Intent i = null;
					
					i = new Intent(getBaseContext(),  SyncActivity.class);
					
					startActivity(i);
				}
				 
			 }	
			 catch(Exception ex) {
				 ex.printStackTrace();
			 }
		 }
    }
	

}
