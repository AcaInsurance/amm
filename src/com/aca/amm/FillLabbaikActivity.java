package com.aca.amm;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.location.GpsStatus;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.aca.binding.LabbaikPlanBind;
import com.aca.binding.LabbaikTujuanBind;
import com.aca.dal.Premi;
import com.aca.dal.Scalar;
import com.aca.database.DBA_INSURED_GROUP;
import com.aca.database.DBA_MASTER_TRAVELSAFE_INT_RATE;
import com.aca.database.DBA_PRODUCT_MAIN;
import com.aca.database.DBA_PRODUCT_TRAVEL_SAFE;
import com.aca.dbflow.SppaMain;
import com.aca.dbflow.StandardField;
import com.aca.dbflow.SubProduct;
import com.aca.dbflow.SubProduct_Table;
import com.aca.holder.PremiHolder;
import com.aca.util.UtilDate;
import com.aca.util.UtilMath;
import com.aca.util.Var;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;

import org.joda.time.LocalDate;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import butterknife.Bind;
import butterknife.ButterKnife;


public class FillLabbaikActivity extends ControlNormalActivity implements InterfaceCustomer {

    private static final int DATE_FROM_ID = 99;
    private static final int DATE_TO_ID = 100;
    private static String URL = Utility.getURL();
    private static String SOAP_ACTION_EXRATE = "http://tempuri.org/GetExchangeRateLabbaik";
    private static long PRODUCT_MAIN_ID = -1;
    private static int INSURED_COUNT = 0;
    //Need handler for callbacks to the UI thread
    final Handler mHandler = new Handler();
    @Bind(R.id.lblJumlahHari)
    TextView lblJumlahHari;
    @Bind(R.id.lblJumlahMinggu)
    TextView lblJumlahMinggu;
    @Bind(R.id.spnPlan)
    Spinner spnPlan;
    @Bind(R.id.txtPremiAlokasi)
    EditText txtPremiAlokasi;
    @Bind(R.id.txtTotalIdr)
    EditText txtTotalIdr;
    boolean exit = false,
            fingerDown = false;
    ScrollView scrollView;
    final Runnable mUpdateResults = new Runnable() {

        public void run() {
            updateUI();
        }
    };
    int tempPlanSelected = -1;
    private ArrayList<HashMap<String, String>> custList;
    private Context context = null;
    private Bundle b;
    private NumberFormat nf;
    private String PRODUCT_ACTION, KODE_NEGARA, NAMA_NEGARA;
    private long SPPA_ID;
    private int flag = 0;
    private ProgressDialog progressBar;
    private SoapObject requestretrive = null;
    private SoapSerializationEnvelope envelope = null;
    private HttpTransportSE androidHttpTransport = null;
    private boolean error = false;
    private RetrivePremi sc;
    private double exchangeRate = 1;
    private ImageView btnDelNegara1,
            btnDelNegara2,
            btnDelNegara3,
            btnDelNegara4,
            btnDelNegara5,
            btnDelNegara6;
    private EditText jumlahTertanggung;
    private EditText namanegara1,
            namanegara2,
            namanegara3,
            namanegara4,
            namanegara5,
            namanegara6;
    private EditText  etFromDate, etToDate, customerno, customername, nopass;
    private Spinner spTujuan;
    private Switch annual;
    private EditText namaahliwaris, hub;
    private EditText jumlahhari, tambahan, loadingpremi, premi, polis, materai, total, discpct, disc;
    private String UIErrorMessage = "Lengkapi semua data";
    private double MAX_DISCOUNT = 0;
    //Create runnable for posting


    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_fill_labbaik);
        ButterKnife.bind(this);

        context = FillLabbaikActivity.this;


        nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);


        spTujuan = (Spinner) findViewById(R.id.spinner1);

        scrollView = (ScrollView) findViewById(R.id.scrollView1);

        InitControls();
        RegisterListener();
        initColor();
        try {

            Intent i = getIntent();
            b = i.getExtras();

            PRODUCT_ACTION = b.getString("PRODUCT_ACTION");

            if (PRODUCT_ACTION == null) {
                PRODUCT_ACTION = "NEW";
                Toast.makeText(this, "Terjadi Kesalahan pada program", Toast.LENGTH_SHORT).show();
                return;
            }

            if (PRODUCT_ACTION.equals("EDIT") || PRODUCT_ACTION.equals("VIEW")) {
                SPPA_ID = b.getLong("SPPA_ID");
                LoadDB();
            }
            else if (PRODUCT_ACTION.equals("VIEW.UNPAID")) {
                SPPA_ID = b.getLong("SPPA_ID");
                LoadDB();
                disableView();
            } else {
                findViewById(R.id.btnDelete)
                        .setVisibility(View.GONE);
            }

            if (b.getString("CUSTOMER_NO") != null) {
                customerno.setText(b.getString("CUSTOMER_NO"));
                customername.setText(b.getString("CUSTOMER_NAME"));
                getCustomerData();
            } else if (!customerno.getText().toString().isEmpty())
                getCustomerData();

            MAX_DISCOUNT = Utility.getMaxDiscount(getBaseContext(), "29");

            if (Utility.getIsDiscountable(getBaseContext(), "07").equals("0")) {
                disc.setEnabled(false);
                discpct.setEnabled(false);
            }

            sc = new RetrivePremi();
            sc.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }

//		startScrollingThread();

    }

    private void updateUI() {
        if (!exit && !fingerDown) {


            if (scrollView.getScrollY() >= (scrollView.getChildAt(0).getHeight() - scrollView.getMeasuredHeight())) {
                scrollView.smoothScrollTo(0, 0);
            } else {
                scrollView.smoothScrollTo(0, scrollView.getScrollY() + 1);
            }
        }
    }

    //===========================

    protected void startScrollingThread() {
        // Fire off a thread to do some work that we shouldn't do directly in the UI thread
        Thread t = new Thread() {
            public void run() {
                while (!exit) {
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

    private void disableView() {
        // TODO Auto-generated method stub
        nopass.setEnabled(false);


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


        customerno.setEnabled(false);
        customername.setEnabled(false);

        etFromDate.setEnabled(false);
        etToDate.setEnabled(false);

        spTujuan.setEnabled(false);
        annual.setEnabled(false);

        (findViewById(R.id.btnChooseCustomer)).setEnabled(false);

//		change text color


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


        customerno.setTextColor(Color.BLACK);
        customername.setTextColor(Color.BLACK);

        etFromDate.setTextColor(Color.BLACK);
        etToDate.setTextColor(Color.BLACK);


    }

    private void getCustomerData() {
        RetrieveCustomer retrieveCustomer = new RetrieveCustomer(FillLabbaikActivity.this, customerno.getText().toString());
        retrieveCustomer.execute();
        retrieveCustomer.customerInterface = this;

    }

    @Override
    public void getCustomerList( ArrayList<HashMap<String, String>> custList ) {
        if (custList != null) {
            this.custList = custList;

        } else {
            Dialog dialog = Utility.showCustomDialogInformation(this, "Informasi", "Gagal mendapatkan data customer");
            dialog.setOnDismissListener(new OnDismissListener() {

                @Override
                public void onDismiss( DialogInterface dialog ) {
                    if (PRODUCT_ACTION.equals("NEW"))
                        startActivity(new Intent(FillLabbaikActivity.this, ChooseProductActivity.class));
                    else if (PRODUCT_ACTION.equals("EDIT") ||
                            PRODUCT_ACTION.equals("VIEW") ||
                            PRODUCT_ACTION.equals("VIEW.UNPAID"))
                        startActivity(new Intent(FillLabbaikActivity.this, SyncActivity.class));

                    FillLabbaikActivity.this.finish();
                }
            });
        }
    }

    private void InitControls() {

        annual = (Switch) findViewById(R.id.swiAnnual);

        namaahliwaris = (EditText) findViewById(R.id.txtAhliWaris);
        hub = (EditText) findViewById(R.id.txtHubungan);

        namanegara1 = (EditText) findViewById(R.id.txtNamaNegara1);
        namanegara2 = (EditText) findViewById(R.id.txtNamaNegara2);
        namanegara3 = (EditText) findViewById(R.id.txtNamaNegara3);
        namanegara4 = (EditText) findViewById(R.id.txtNamaNegara4);
        namanegara5 = (EditText) findViewById(R.id.txtNamaNegara5);
        namanegara6 = (EditText) findViewById(R.id.txtNamaNegara6);

        namanegara1.setTag("");
        namanegara2.setTag("");
        namanegara3.setTag("");
        namanegara4.setTag("");
        namanegara5.setTag("");
        namanegara6.setTag("");


        jumlahhari = (EditText) findViewById(R.id.txtJmlHariDipertanggungkan);
        tambahan = (EditText) findViewById(R.id.txtTambahanPerMinggu);
        loadingpremi = (EditText) findViewById(R.id.txtLoadingPremi);
        premi = (EditText) findViewById(R.id.txtPremi);
        materai = (EditText) findViewById(R.id.txtMaterai);
        polis = (EditText) findViewById(R.id.txtBiayaPolis);
        polis.setText("1.50");
        total = (EditText) findViewById(R.id.txtTotal);
        discpct = (EditText) findViewById(R.id.txtDiscountPct);
        disc = (EditText) findViewById(R.id.txtDiscount);


        nopass = (EditText) findViewById(R.id.txtNoPass);

        customerno = (EditText) findViewById(R.id.txtCustomerNo);
        customername = (EditText) findViewById(R.id.txtCustomerName);


        etFromDate = (EditText) findViewById(R.id.txtTglBerangkat);
        etToDate = (EditText) findViewById(R.id.txtTglKembali);


        btnDelNegara1 = (ImageView) findViewById(R.id.btnDeleteNegara);
        btnDelNegara2 = (ImageView) findViewById(R.id.btnDeleteNegara2);
        btnDelNegara3 = (ImageView) findViewById(R.id.btnDeleteNegara3);
        btnDelNegara4 = (ImageView) findViewById(R.id.btnDeleteNegara4);
        btnDelNegara5 = (ImageView) findViewById(R.id.btnDeleteNegara5);
        btnDelNegara6 = (ImageView) findViewById(R.id.btnDeleteNegara6);


        jumlahTertanggung = (EditText) findViewById(R.id.txtInsuredCount);

        namaahliwaris.setText(Var.LEGAL_HEIRS);
        hub.setText(Var.LEGAL_HEIRS);


        LabbaikTujuanBind.bind(this, spTujuan);
        LabbaikPlanBind.bind(this, spnPlan, spTujuan);

    }

    private void initColor() {
    }

    private void LoadDB() {

        DBA_PRODUCT_MAIN dbm = null;
        DBA_PRODUCT_TRAVEL_SAFE dba = null;

        Cursor cm = null;
        Cursor c = null;

        try {

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

            PRODUCT_MAIN_ID = c.getInt(93);

            StandardField sf = StandardField.getStandardFieldByDescription(c.getString(15));
            LabbaikTujuanBind.select(spTujuan, sf.FieldNameDt);

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


            if (KODE_NEGARA != null && NAMA_NEGARA != null) {
                namanegara1.setTag(KODE_NEGARA);
                namanegara1.setText(NAMA_NEGARA);
            }


            namaahliwaris.setText(c.getString(18));
            hub.setText(c.getString(19));
            etFromDate.setText(c.getString(20));
            etToDate.setText(c.getString(21));

            int plan = c.getInt(22) - 1;
            spnPlan.setSelection(plan);
            tempPlanSelected = plan;


            jumlahhari.setText(nf.format(c.getDouble(23)));
            tambahan.setText(nf.format(c.getDouble(24)));
            loadingpremi.setText(nf.format(c.getDouble(25)));
            premi.setText(nf.format(c.getDouble(26)));
            discpct.setText(nf.format(cm.getDouble(23)));
            disc.setText(nf.format(cm.getDouble(24)));
            polis.setText(nf.format(c.getDouble(27)));
            total.setText(nf.format(c.getDouble(28)));

            nopass.setText(c.getString(51));

            if (cm.getString(20).equals("R")) {
                etFromDate.setEnabled(false);
                etToDate.setEnabled(false);
                customerno.setEnabled(false);
                ((Button) findViewById(R.id.btnChooseCustomer)).setEnabled(false);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
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

    private void setDefaultNegara() {
        namanegara1.setText(Var.LABBAIK_DEFAULT_ARAB_DESCRIPTION);
        namanegara1.setTag(Var.LABBAIK_DEFAULT_ARAB_KODE);
        namanegara1.setEnabled(false);
        btnDelNegara1.setVisibility(View.GONE);

        if (spTujuan.getSelectedItemPosition() == 0) {
            namanegara2.setEnabled(false);
            namanegara3.setEnabled(false);
            namanegara4.setEnabled(false);
            namanegara5.setEnabled(false);
            namanegara6.setEnabled(false);

            namanegara2.setText("");
            namanegara3.setText("");
            namanegara4.setText("");
            namanegara5.setText("");
            namanegara6.setText("");

            namanegara2.setTag("");
            namanegara3.setTag("");
            namanegara4.setTag("");
            namanegara5.setTag("");
            namanegara6.setTag("");


        } else {
            namanegara2.setEnabled(true);
            namanegara3.setEnabled(true);
            namanegara4.setEnabled(true);
            namanegara5.setEnabled(true);
            namanegara6.setEnabled(true);
        }
    }

/*    @OnItemSelected (R.id.spinner1)
    public void onItemSelectedSpTujuan() {
        setDefaultNegara();
        LabbaikPlanBind.bind(FillLabbaikActivity.this, spnPlan, spTujuan);
        calculateAll();
    }

    @OnItemSelected (R.id.spnPlan)
    public void onItemSelectedSpnPlan() {
        calculateAll();
    }*/

    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 100) {
            KODE_NEGARA = data.getStringExtra("KODE_NEGARA");
            NAMA_NEGARA = data.getStringExtra("NAMA_NEGARA");

            if (KODE_NEGARA != null && NAMA_NEGARA != null) {
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
        } else if (resultCode == 101 && requestCode == 200) {
            PRODUCT_MAIN_ID = data.getExtras().getLong(FillInsuredGroupActivity.TAG_BUNDLE_PRODUCT_MAIN_ID);
            INSURED_COUNT = data.getExtras().getInt(FillInsuredGroupActivity.TAG_BUNDLE_INSURED_COUNT);

            Log.i(TAG, "::onActivityResult:" + "product main id = " + PRODUCT_MAIN_ID);

            jumlahTertanggung.setText(INSURED_COUNT + "");
            try {
                calculateAll();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void RegisterListener() {
        spTujuan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected( AdapterView<?> parent, View view, int position, long id ) {
                setDefaultNegara();
                LabbaikPlanBind.bind(FillLabbaikActivity.this, spnPlan, spTujuan);
                if (tempPlanSelected != -1) {
                    spnPlan.setSelection(tempPlanSelected);
                    tempPlanSelected = -1;
                }

                calculateAll();
            }

            @Override
            public void onNothingSelected( AdapterView<?> parent ) {

            }
        });

        spnPlan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected( AdapterView<?> parent, View view, int position, long id ) {
                calculateAll();
            }

            @Override
            public void onNothingSelected( AdapterView<?> parent ) {

            }
        });

        btnDelNegara2.setOnClickListener(btnDelNegaraOnClickListener(namanegara2));
        btnDelNegara3.setOnClickListener(btnDelNegaraOnClickListener(namanegara3));
        btnDelNegara4.setOnClickListener(btnDelNegaraOnClickListener(namanegara4));
        btnDelNegara5.setOnClickListener(btnDelNegaraOnClickListener(namanegara5));
        btnDelNegara6.setOnClickListener(btnDelNegaraOnClickListener(namanegara6));


        etToDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick( View v ) {
                etToDate.setTextColor(Color.BLACK);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Calendar c = Calendar.getInstance();

                int Y, M, D;


                if (!etToDate.getText().toString().isEmpty()) {
                    try {
                        c.setTime(dateFormat.parse(etToDate.getText().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Y = c.get(Calendar.YEAR);
                    M = c.get(Calendar.MONTH);
                    D = c.get(Calendar.DAY_OF_MONTH);

                } else {
                    Y = c.get(Calendar.YEAR);
                    M = c.get(Calendar.MONTH);
                    D = c.get(Calendar.DAY_OF_MONTH);

                }


                DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet( DatePicker view, int selectedYear, int selectedMonth, int selectedDay ) {
                        etToDate.setText(Utility.setUIDate(selectedYear, selectedMonth, selectedDay));
                        try {
                            calculateAll();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                Dialog dialog = new DatePickerDialog(FillLabbaikActivity.this, datePickerListener, Y, M, D);
                dialog.show();

                try {
                    long maxDate;

                    if (annual.isChecked()) {
                        maxDate = dateFormat.parse(Utility.getAnnualDate(etFromDate.getText().toString(), etToDate.getText().toString())).getTime();
                        ((DatePickerDialog) dialog).getDatePicker().setMaxDate(maxDate);
                        Log.i(TAG, "::onCreateDialog:" + " max date annual year");
                    } else {
                        Date from = dateFormat.parse(etFromDate.getText().toString());

                        Calendar cal = Calendar.getInstance();
                        cal.setTime(from);
                        cal.add(Calendar.YEAR, 5);
                        ((DatePickerDialog) dialog).getDatePicker().setMaxDate(cal.getTimeInMillis());
                        Log.i(TAG, "::onCreateDialog:" + " max date normal");
                    }


                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        });

        etFromDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick( View arg0 ) {
                etFromDate.setTextColor(Color.BLACK);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Calendar c = Calendar.getInstance();

                int Y, M, D;


                if (!etFromDate.getText().toString().isEmpty()) {
                    try {
                        c.setTime(dateFormat.parse(etFromDate.getText().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Y = c.get(Calendar.YEAR);
                    M = c.get(Calendar.MONTH);
                    D = c.get(Calendar.DAY_OF_MONTH);

                } else {
                    Y = c.get(Calendar.YEAR);
                    M = c.get(Calendar.MONTH);
                    D = c.get(Calendar.DAY_OF_MONTH);

                }


                DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet( DatePicker view, int selectedYear, int selectedMonth, int selectedDay ) {
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

                Dialog dialog = new DatePickerDialog(FillLabbaikActivity.this, datePickerListener, Y, M, D);
                dialog.show();
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


        discpct.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange( View v, boolean hasFocus ) {
                if (hasFocus) {
                    discpct.setText(Utility.removeComma(discpct.getText().toString()));
                } else {
                    try {
                        if (discpct.getText().toString().isEmpty())
                            return;

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
            public void onFocusChange( View v, boolean hasFocus ) {
                if (hasFocus) {
                    disc.setText(Utility.removeComma(disc.getText().toString()));
                } else {
                    try {
                        if (disc.getText().toString().isEmpty())
                            return;

                        disc.setText(nf.format(nf.parse(disc.getText().toString())));
                        calculateAll();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private OnClickListener btnDelNegaraOnClickListener( final EditText namaNegara ) {
        return new OnClickListener() {
            @Override
            public void onClick( View v ) {
                namaNegara.setText("");
                namaNegara.setTag("");
            }
        };
    }

    private OnClickListener namaNegaraOnClickListener( final int requestCode ) {
        return new OnClickListener() {

            @Override
            public void onClick( View v ) {
                b.putInt("NEGARA_TUJUAN", Var.LABBAIK_DEFAULT_NEGARA_TUJUAN);

                Intent i = new Intent(getBaseContext(), ChooseCountryActivity.class);
                i.putExtras(b);
                startActivityForResult(i, requestCode);
            }

        };
    }

    private PremiHolder calculateAll() {
        try {
            fillSppaMain();

            Premi premi = new Premi();
            PremiHolder premiHolder = premi.countPremi();
            setPremi(premiHolder);
            return premiHolder;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setPremi( PremiHolder premiHolder ) {
        int daysPeriode = premiHolder.daysPeriode;
        int weeksPeriode = premiHolder.weeksPeriode;

        double basicPremi = premiHolder.premiBasic;
        double addPremi = premiHolder.getAddPremi();
        double loadingPremi = premiHolder.premiLoading;
        double charge = premiHolder.getTotalCharge();
        double totalPremi = premiHolder.getTotal();
        double totalPremiAlokasi = premiHolder.getPremiAlokasiUmroh();

        double totalPremiInIdr = premiHolder.getTotalInIdr();
/*
        double totalDiskon = premiHolder.getDiskonAmount();
        double persenDiskon = premiHolder.getDiskon();
*/


        lblJumlahHari.setText(String.format(getString(R.string.label_jumlah_hari_yang_dipertanggungkan), daysPeriode));
        lblJumlahMinggu.setText(String.format(getString(R.string.label_tambahan_per_minggu), weeksPeriode));

        jumlahhari.setText(Var.USD + " " + UtilMath.toString(basicPremi));
        tambahan.setText(Var.USD + " " + UtilMath.toString(addPremi));
        loadingpremi.setText(Var.USD + " " + UtilMath.toString(loadingPremi));
        polis.setText(Var.USD + " " + UtilMath.toString(charge));
        premi.setText(Var.USD + " " + UtilMath.toString(totalPremi));
        txtPremiAlokasi.setText(Var.USD + " " + UtilMath.toString(totalPremiAlokasi));
        total.setText(Var.USD + " " + UtilMath.toString(totalPremi));
        txtTotalIdr.setText(Var.IDR + " " + UtilMath.toString(totalPremiInIdr));

    }

    @Override
    protected void onSaveInstanceState( Bundle outState ) {
        super.onSaveInstanceState(outState);
        outState.putAll(b);
    }

    @Override
    protected void onRestoreInstanceState( Bundle savedInstanceState ) {
        super.onRestoreInstanceState(savedInstanceState);

        b = savedInstanceState;

        try {
            SPPA_ID = b.getLong("SPPA_ID");
            PRODUCT_ACTION = b.getString("PRODUCT_ACTION");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        getMenuInflater().inflate(R.menu.fill_travel, menu);
        return true;
    }

    public void btnHomeClick( View v ) {
        Intent i = new Intent(getBaseContext(), FirstActivity.class);
        startActivity(i);
        this.finish();
    }

    public void btnBackClick( View v ) {
        Back();
    }

    public void btnDeleteClick( View v ) {

        showConfirmDelete(getBaseContext(), SPPA_ID, "").show();
    }

    private AlertDialog showConfirmDelete( final Context ctx, final long rowId, final String noPolis ) {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                .setTitle("Hapus")
                .setMessage("Hapus data ini sekarang?")
                .setIcon(R.drawable.delete)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick( DialogInterface dialog, int whichButton ) {
                        new DeleteSPPA().execute();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick( DialogInterface dialog, int which ) {
                        dialog.dismiss();
                    }
                })
                .create();
        return myQuittingDialogBox;
    }

    private void Back() {
        try {

            Intent i = null;

            if (PRODUCT_ACTION.equals("NEW"))
                i = new Intent(getBaseContext(), ChooseProductActivity.class);
            else
                i = new Intent(getBaseContext(), SyncActivity.class);

            startActivity(i);
            this.finish();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void validasiNext() throws ParseException {
        flag = 0;

        if (customerno.getText().toString().isEmpty()) {
            flag++;
            customerno.setHintTextColor(Color.RED);
            UIErrorMessage = "Pilih customer";
            return;
        }

        if (customername.getText().toString().isEmpty()) {
            flag++;
            customername.setHintTextColor(Color.RED);
            UIErrorMessage = "Pilih customer";
            return;
        }


        if (nopass.getText().toString().isEmpty()) {
            flag++;
            nopass.setHintTextColor(Color.RED);
            UIErrorMessage = "No passport tertanggung harus diisi";
            return;
        }


        if (namanegara1.getText().toString().isEmpty()) {
            flag++;

            namanegara1.setHintTextColor(Color.RED);
            UIErrorMessage = "Nama Negara harus dipilih";
            return;
        }

        if (namaahliwaris.getText().toString().isEmpty()) {
            flag++;
            namaahliwaris.setHintTextColor(Color.RED);
            UIErrorMessage = "Nama ahli waris harus diisi";
            return;
        }
        if (hub.getText().toString().isEmpty()) {
            flag++;
            hub.setHintTextColor(Color.RED);
            UIErrorMessage = "Hubungan tertanggung dengan ahli waris harus diisi";
            return;
        }

        if (etFromDate.getText().toString().isEmpty()) {
            flag++;
            etFromDate.setHintTextColor(Color.RED);
            UIErrorMessage = "Tanggal mulai harus diisi";
            return;
        }
        if (etToDate.getText().toString().isEmpty()) {
            flag++;
            etToDate.setHintTextColor(Color.RED);
            UIErrorMessage = "Tanggal berakhir harus diisi";
            return;
        }

        if (!Utility.validasiEffDate(etFromDate.getText().toString(), Utility.GetTodayString(), getBaseContext())) {
            flag++;
            etFromDate.setTextColor(Color.RED);
            UIErrorMessage = "Tanggal mulai minimal hari hari";
            return;
        }
        if (!Utility.validasiEffDate(etToDate.getText().toString(), etFromDate.getText().toString(), getBaseContext())) {
            flag++;
            etToDate.setTextColor(Color.RED);
            UIErrorMessage = "Tanggal kembali tidak sesuai";
            return;
        }

        if (!validateMaxDay() || !validateAge()) {
            UIErrorMessage = "";
            flag++;
            return;
        }

        if (jumlahhari.getText().toString().isEmpty()) {
            flag++;
            jumlahhari.setHintTextColor(Color.RED);
            UIErrorMessage = "Jumlah hari tidak bisa dihitung. Hubungi IT ACA";
            return;
        }


        if (premi.getText().toString().isEmpty()) {
            flag++;
            premi.setHintTextColor(Color.RED);
            UIErrorMessage = "Premi tidak bisa dihitung. Hubungi IT ACA";
            return;
        }
        if (Utility.parseDouble(discpct) > MAX_DISCOUNT) {
            flag++;
            discpct.setTextColor(Color.RED);
            UIErrorMessage = "% diskon terlalu besar, maksimum adalah " + String.valueOf(MAX_DISCOUNT) + "%";
            return;
        }


    }

    private SubProduct getSubProduct() {
        String subProductCode = LabbaikTujuanBind.getID(spTujuan);
        SubProduct subProduct = new Select()
                .from(SubProduct.class)
                .where(SubProduct_Table.SubProductCode.eq(subProductCode))
                .querySingle();

        return subProduct;
    }

    private boolean validateAge() {
        SubProduct subProduct;
        int maxAgeFrom, maxAgeTo, minAge, age;
        String expDate;
        String dob;
        String subProductCode;
        String message;

        try {
            subProductCode = Scalar.getSubProductCode();

            if (subProductCode.isEmpty())
                return true;

            subProduct = new Select().from(SubProduct.class)
                    .where(SubProduct_Table.SubProductCode.eq(subProductCode))
                    .querySingle();


            expDate = Scalar.getExpiredDate();
            age = getAge();
            maxAgeFrom = Integer.parseInt(subProduct.MaxAgeFrom);
            maxAgeTo = Integer.parseInt(subProduct.MaxAgeTo);
            minAge = Integer.parseInt(subProduct.MinAge);
            dob = UtilDate.parseUTC(custList.get(0).get("CUSTOMER_DOB")).toString(UtilDate.STANDARD_DATE);

            message = String.format(context.getString(R.string.message_validate_umur), minAge, maxAgeTo);

            if (age == 0) {
                int ageInMonth = UtilDate.monthDiffInPeriode(UtilDate.toDate(dob), UtilDate.toDate(expDate));
                if (ageInMonth >= minAge)
                    return true;
            }

            if ((double) age >= (double) minAge / 12 && age < maxAgeTo) {
                return true;
            } else if (age == maxAgeTo) {
                LocalDate minusOneDayDate = UtilDate.toDate(expDate).minusDays(1);
                int newAge = Scalar.getAge(minusOneDayDate.toString(UtilDate.STANDARD_DATE), dob, UtilDate.STANDARD_DATE);

                if (newAge < maxAgeTo)
                    return true;
                else {
                    Utility.showCustomDialogInformation(this, "Information", message);
                    return false;
                }
            } else {
                Utility.showCustomDialogInformation(this, "Information", message);
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean validateMaxDay() {
        try {
            SubProduct subProduct = getSubProduct();
            int maxDayTravel = Integer.parseInt(subProduct.MaxDayTravel);
            int daysPeriode = Scalar.getDaysPeriode();

            if (daysPeriode > maxDayTravel) {
                Utility.showCustomDialogInformation(this, "Information", String.format(getString(R.string.message_validate_max_day), maxDayTravel));
                return false;
            }

            return true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void btnNextClick( View v ) {
        try {
            validasiNext();

            if (flag != 0) {
                if (!UIErrorMessage.isEmpty())
                    Utility.showCustomDialogInformation(FillLabbaikActivity.this, "Warning", UIErrorMessage);
            } else {
                PremiHolder premiHolder = calculateAll();
                insertDB(premiHolder);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void btnChooseCustomerClick( View v ) {
        Intent i = new Intent(getBaseContext(), ChooseCustomerForBuyActivity.class);
        i.putExtras(b);
        startActivity(i);
        this.finish();
    }

    private void fillSppaMain() {
        Delete.table(SppaMain.class);
        SppaMain sppaMain = new SppaMain();
        sppaMain.SubProductCode = LabbaikTujuanBind.getID(spTujuan);
        sppaMain.ProductCode = Var.INT;
        sppaMain.ZoneId = LabbaikTujuanBind.getValue(spTujuan);
        sppaMain.PlanCode = LabbaikPlanBind.getID(spnPlan);
        sppaMain.Age = String.valueOf(getAge());
        sppaMain.EffectiveDate = etFromDate.getText().toString();
        sppaMain.ExpireDate = etToDate.getText().toString();
        sppaMain.ExchangeRate = String.valueOf(exchangeRate);
        sppaMain.save();
    }

    private int getAge() {
        if (custList == null || TextUtils.isEmpty(etToDate.getText().toString()))
            return 0;

        String pattern = UtilDate.STANDARD_DATE;
        String expiredDate = etToDate.getText().toString();
        String dob = UtilDate.parseUTC(custList.get(0).get("CUSTOMER_DOB")).toString(pattern);

        return Scalar.getAge(expiredDate, dob, pattern);

/*
        return Utility.getAge(
                Utility.getFormatDate(custList.get(0).get("CUSTOMER_DOB").toString()),
                Utility.GetTodayString());
*/
    }

    private Double getPremiBDA( String area, String coverage ) {

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
            dba.close();
        }
        return 0.0;

    }

    public void insertDB( PremiHolder premiHolder ) {
        DBA_PRODUCT_TRAVEL_SAFE dba = null;
        DBA_PRODUCT_MAIN dba2 = null;
        DBA_INSURED_GROUP dbInsured = null;
        Cursor cInsured = null;

        try {

            if (PRODUCT_ACTION.equals("VIEW")) {
                Intent i = new Intent(getBaseContext(), ConfirmActivity.class);
                b.putLong("SPPA_ID", SPPA_ID);
                i.putExtras(b);
                startActivity(i);
                this.finish();
            } else {
                dba = new DBA_PRODUCT_TRAVEL_SAFE(getBaseContext());
                dba2 = new DBA_PRODUCT_MAIN(getBaseContext());
                dbInsured = new DBA_INSURED_GROUP(getBaseContext());

                dba.open();
                dba2.open();
                dbInsured.open();

                cInsured = dbInsured.getRow(PRODUCT_MAIN_ID);

                String isFamily,
                        isAsia,
                        isAnnual;

                String isPasangan = "0";
                String isAnak1 = "0";
                String isAnak2 = "0";
                String isAnak3 = "0";

                double premiPasangan = 0;
                double premiAnak1 = 0;
                double premiAnak2 = 0;
                double premiAnak3 = 0;

                isAnnual = "0";
                isFamily = "0";
                isAsia = "2";

                List<Pair<String, String>> listNegara = getListNegara();

                if (PRODUCT_ACTION.equals("NEW")) {
                    SPPA_ID = dba2.initialInsert(
                            customerno.getText().toString(),
                            customername.getText().toString(),
                            Var.LABBAIK,
                            premiHolder.getTotalInIdr(),
                            premiHolder.getTotalCharge() * exchangeRate,
                            premiHolder.getTotalCharge() * exchangeRate,
                            premiHolder.getTotalInIdr(),
                            etFromDate.getText().toString(),
                            etToDate.getText().toString(),
                            Utility.DateToString(Utility.GetToday(), "dd/MM/yyyy"),
                            "", "", "", "N", "",
                            nf.parse((discpct.getText().toString())).doubleValue(),
                            nf.parse((disc.getText().toString())).doubleValue());

                    dba.initialInsert(
                            SPPA_ID,
                            "", "", "",
                            "", "", "",
                            "", "", "",
                            "", "", "",
                            spTujuan.getSelectedItem().toString(),
                            isAsia,
                            namanegara1.getText().toString(),
                            LabbaikPlanBind.getOrder(spnPlan),
                            namaahliwaris.getText().toString().toUpperCase(),
                            hub.getText().toString().toUpperCase(),
                            etFromDate.getText().toString(),
                            etToDate.getText().toString(),

                            premiHolder.premiBasic,
                            premiHolder.getAddPremi(),
                            premiHolder.premiLoading,

                            isFamily,
                            premiHolder.getTotalPremi(),
                            premiHolder.getTotalCharge(),
                            premiHolder.getTotal(),
                            Var.LABBAIK,
                            customername.getText().toString(),

                            premiPasangan,
                            premiAnak1,
                            premiAnak2,
                            premiAnak3,
                            isPasangan,
                            isAnak1,
                            isAnak2,
                            isAnak3,

                            namanegara1.getTag().toString(),
                            isAnnual,
                            exchangeRate,
                            premiHolder.acod,
                            premiHolder.ccod,
                            premiHolder.dcod,
                            premiHolder.premiBasic,
                            premiHolder.premiAdd,
                            premiHolder.maxBenefit,
                            premiHolder.daysPeriode,
                            premiHolder.weeksPeriode,
                            premiHolder.getPremiAlokasiUmroh(),
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

                            listNegara.get(0).second.toString(), listNegara.get(0).first.toString(),
                            listNegara.get(1).second.toString(), listNegara.get(1).first.toString(),
                            listNegara.get(2).second.toString(), listNegara.get(2).first.toString(),
                            listNegara.get(3).second.toString(), listNegara.get(3).first.toString(),
                            listNegara.get(4).second.toString(), listNegara.get(4).first.toString(),

                            PRODUCT_MAIN_ID,
                            "",
                            INSURED_COUNT
                    );

                } else {
                    dba2.nextInsert(SPPA_ID,
                            customerno.getText().toString(),
                            customername.getText().toString(),
                            premiHolder.getTotalInIdr(),
                            premiHolder.getTotalCharge() * exchangeRate,
                            premiHolder.getTotalCharge() * exchangeRate,
                            premiHolder.getTotalInIdr(),
                            etFromDate.getText().toString(),
                            etToDate.getText().toString(),
                            Utility.DateToString(Utility.GetToday(), "dd/MM/yyyy"),
                            nf.parse((discpct.getText().toString())).doubleValue(),
                            nf.parse((disc.getText().toString())).doubleValue());


                    dba.nextInsert(
                            SPPA_ID,
                            "", "", "",
                            "", "", "",
                            "", "", "",
                            "", "", "",
                            spTujuan.getSelectedItem().toString(),
                            isAsia,
                            namanegara1.getText().toString(),
                            LabbaikPlanBind.getOrder(spnPlan),
                            namaahliwaris.getText().toString().toUpperCase(),
                            hub.getText().toString().toUpperCase(),
                            etFromDate.getText().toString(),
                            etToDate.getText().toString(),

                            premiHolder.premiBasic,
                            premiHolder.getAddPremi(),
                            premiHolder.premiLoading,

                            isFamily,
                            premiHolder.getTotalPremi(),
                            premiHolder.getTotalCharge(),
                            premiHolder.getTotal(),
                            Var.LABBAIK,
                            customername.getText().toString(),

                            premiPasangan,
                            premiAnak1,
                            premiAnak2,
                            premiAnak3,
                            isPasangan,
                            isAnak1,
                            isAnak2,
                            isAnak3,

                            namanegara1.getTag().toString(),
                            isAnnual,
                            exchangeRate,
                            premiHolder.acod,
                            premiHolder.ccod,
                            premiHolder.dcod,
                            premiHolder.premiBasic,
                            premiHolder.getAddPremi(),
                            premiHolder.maxBenefit,
                            premiHolder.daysPeriode,
                            premiHolder.weeksPeriode,
                            premiHolder.getPremiAlokasiUmroh(),
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

                            listNegara.get(0).second.toString(), listNegara.get(0).first.toString(),
                            listNegara.get(1).second.toString(), listNegara.get(1).first.toString(),
                            listNegara.get(2).second.toString(), listNegara.get(2).first.toString(),
                            listNegara.get(3).second.toString(), listNegara.get(3).first.toString(),
                            listNegara.get(4).second.toString(), listNegara.get(4).first.toString(),

                            PRODUCT_MAIN_ID,
                            "",
                            INSURED_COUNT
                    );

                }

                Intent i = new Intent(getBaseContext(), ConfirmActivity.class);
                b.putLong("SPPA_ID", SPPA_ID);
                i.putExtras(b);
                startActivity(i);
                this.finish();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
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

    private List<Pair<String, String>> getListNegara() {
        List<Pair<String, String>> negaraList = new ArrayList<>();

     /*   if (!namanegara1.getText().toString().isEmpty()) {
            negaraList.add(new Pair<>(namanegara1.getTag().toString(), namanegara1.getText().toString()));
        }*/
        negaraList.add(new Pair<>(namanegara2.getTag().toString(), namanegara2.getText().toString()));
        negaraList.add(new Pair<>(namanegara3.getTag().toString(), namanegara3.getText().toString()));
        negaraList.add(new Pair<>(namanegara4.getTag().toString(), namanegara4.getText().toString()));
        negaraList.add(new Pair<>(namanegara5.getTag().toString(), namanegara5.getText().toString()));
        negaraList.add(new Pair<>(namanegara6.getTag().toString(), namanegara6.getText().toString()));

 /*       if (!namanegara2.getText().toString().isEmpty()) {
            negaraList.add(new Pair<>(namanegara2.getTag().toString(), namanegara2.getText().toString()));
        }

        if (!namanegara3.getText().toString().isEmpty()) {
            negaraList.add(new Pair<>(namanegara3.getTag().toString(), namanegara3.getText().toString()));
        }

        if (!namanegara4.getText().toString().isEmpty()) {
            negaraList.add(new Pair<>(namanegara4.getTag().toString(), namanegara4.getText().toString()));
        }

        if (!namanegara5.getText().toString().isEmpty()) {
            negaraList.add(new Pair<>(namanegara5.getTag().toString(), namanegara5.getText().toString()));
        }

        if (!namanegara6.getText().toString().isEmpty()) {
            negaraList.add(new Pair<>(namanegara6.getTag().toString(), namanegara6.getText().toString()));
        }*/


        Set<Pair<String, String>> set = new HashSet<>();
        set.addAll(negaraList);
        negaraList.clear();
        negaraList.addAll(set);
        negaraList.remove(new Pair<>("",""));

        for (int i = negaraList.size(); i<5; i++) {
            negaraList.add(new Pair<>("",""));
        }
        return negaraList;
    }

    @Override
    @Deprecated
    protected void onPrepareDialog( int id, Dialog dialog ) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date dateFrom = null;
//
        switch (id) {


            case DATE_TO_ID:
                try {
                    long maxDate = 0;

                    if (annual.isChecked()) {
                        maxDate = dateFormat.parse(Utility.getAnnualDate(etFromDate.getText().toString(), etToDate.getText().toString())).getTime();
                        ((DatePickerDialog) dialog).getDatePicker().setMaxDate(maxDate);
                        Log.i(TAG, "::onCreateDialog:" + " max date annual year");
                    } else {
                        Date from = dateFormat.parse(etFromDate.getText().toString());

                        Calendar cal = Calendar.getInstance();
                        cal.setTime(from);
                        cal.add(Calendar.YEAR, 5);
                        ((DatePickerDialog) dialog).getDatePicker().setMaxDate(cal.getTimeInMillis());
                        Log.i(TAG, "::onCreateDialog:" + " max date normal");
                    }


                } catch (ParseException e) {
                    e.printStackTrace();
                }
        }
    }

    @Override
    public boolean onKeyDown( int keyCode, KeyEvent event ) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            Back();

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        Back();
    }


    private class RetrivePremi extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            progressBar = new ProgressDialog(FillLabbaikActivity.this);
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
        protected Void doInBackground( String... params ) {

            error = false;

            try {

                envelope.setOutputSoapObject(requestretrive);
                envelope.bodyOut = requestretrive;
                androidHttpTransport.call(SOAP_ACTION_EXRATE, envelope);

                SoapObject result = (SoapObject) envelope.bodyIn;
                String exrate = result.getProperty(0).toString();
                exchangeRate = Double.parseDouble(exrate);
                Log.d("er", String.valueOf(exchangeRate));
            } catch (Exception e) {
                error = true;
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute( Void result ) {

            super.onPostExecute(result);

            progressBar.hide();
            progressBar.dismiss();

            try {
                if (error) {
                    Toast.makeText(FillLabbaikActivity.this,
                            "Gagal mendapatkan rate premi",
                            Toast.LENGTH_SHORT).show();

                     startActivity(new Intent(FillLabbaikActivity.this, ChooseProductActivity.class));
                    FillLabbaikActivity.this.finish();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public class DeleteSPPA extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progDialog = null;
        private boolean error = false;
        private String flag = "";

        protected void onPreExecute() {
            progDialog = ProgressDialog.show(context, "", "Processing...", false);
        }

        @Override
        protected Void doInBackground( Void... params ) {


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


            try {
                dba = new DBA_PRODUCT_MAIN(context);
                dba.open();

                dba2 = new DBA_PRODUCT_TRAVEL_SAFE(context);
                dba2.open();


                //dapatin no SPPA
                c = dba.getRow(SPPA_ID);
                c.moveToNext();
                E_SPPA_NO = c.getString(5);

                Log.d("-->", E_SPPA_NO);

                if (E_SPPA_NO == null || E_SPPA_NO.length() == 0 || !TextUtils.isDigitsOnly(E_SPPA_NO)) {
                    Log.d("-->", "EMPTY");

                    //hapus data di local db
                    dba.delete(SPPA_ID);
                    dba2.delete(SPPA_ID);

                    //hapus directory
                    Utility.DeleteDirectory(SPPA_ID);
                } else {
                    //coba hapus SPPA di server
                    envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.implicitTypes = true;
                    envelope.dotNet = true;    //used only if we use the webservice from a dot net file (asmx)
                    androidHttpTransport = new HttpTransportSE(URL);
                    request = new SoapObject(NAMESPACE, METHOD_NAME);

                    request.addProperty(Utility.GetPropertyInfo("SPPANo", E_SPPA_NO, String.class));

                    envelope.setOutputSoapObject(request);
                    androidHttpTransport.call(SOAP_ACTION, envelope);

                    SoapObject result = (SoapObject) envelope.bodyIn;
                    String response = "";

                    if (result.getPropertyCount() == 0) {
                        flag = "1";
                    } else {
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

            } catch (Exception e) {
                error = true;
                e.printStackTrace();
//					errorMessage = new MasterExceptionClass(e).getException();
            } finally {

                if (c != null)
                    c.close();

                if (dba != null)
                    dba.close();

                if (dba2 != null)
                    dba2.close();
            }

            return null;
        }


        protected void onPostExecute( Void result ) {
            try {
                progDialog.dismiss();
                progDialog = null;

                if (error)
                    Toast.makeText(getBaseContext(), "Data gagal dihapus", Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(getBaseContext(), "Data berhasil dihapus", Toast.LENGTH_SHORT).show();

                    Intent i = null;

                    i = new Intent(getBaseContext(), SyncActivity.class);

                    startActivity(i);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


}
