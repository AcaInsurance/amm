package com.aca.amm;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.Toast;

import com.aca.binding.EqBind;
import com.aca.binding.FloodBind;
import com.aca.binding.PABind;
import com.aca.binding.TPLBind;
import com.aca.dal.Scalar;
import com.aca.database.DBA_MASTER_KONVE_RATE;
import com.aca.database.DBA_MASTER_OTOMATE_AOG;
import com.aca.database.DBA_MASTER_OTOMATE_RATE;
import com.aca.database.DBA_MASTER_PRODUCT_SETTING;
import com.aca.database.DBA_PRODUCT_MAIN;
import com.aca.database.DBA_PRODUCT_OTOMATE;
import com.aca.dbflow.GeneralSetting;
import com.aca.dbflow.SettingOtomate;
import com.aca.dbflow.SettingOtomate_Table;
import com.aca.util.Var;
import com.raizlabs.android.dbflow.sql.language.Select;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

@SuppressLint("DefaultLocale")
public class FillOtomateActivity extends ControlNormalActivity {


    @Bind(R.id.spnFlood)
    Spinner spnFlood;
    @Bind(R.id.spnEq)
    Spinner spnEq;
    @Bind(R.id.swiSRCC)
    Switch swiSRCC;
    @Bind(R.id.swiTS)
    Switch swiTS;
    @Bind(R.id.lblTitle)
    CustomTextViewBold lblTitle;
    @Bind(R.id.swiBengkel)
    Switch swiBengkel;

    private Bundle b;
    private Calendar c;
    private Context context = null;
    private NumberFormat nf;


    private double v_rate = 0,
            v_tsi = 0,
            v_premi = 0,
            v_polis = 0,
            v_materai = 0,
            v_total = 0,
            v_discpct = 0,
            v_disc = 0,
            MAX_DISCOUNT = 0;


    private int flag = 0,
            maxTahun = 7;

    private final int DATE_FROM_ID = 99,
            DATE_TO_ID = 100;
    private long SPPA_ID;

    private String carBrand = "",
            carType = "",
            carIDBrand,
            carIDType,
            PRODUCT_ACTION,
            UIErrorMessage = "Lengkapi semua data";

    private EditText
            nopol1,
            nopol2,
            nopol3,
            model,
            tahun,
            chassisNo,
            machineNo,
            color,
            perlengkapan,
            seat,
            jangkaWaktuEff,
            jangkaWaktuExp,
            nilaiPertanggungan,
            nilaiPerlengkapan,
            risikoSendiri,
            keteranganKerusakan,
            rate,
            premi,
            polis,
            materai,
            total,
            customerno,
            customername,
            discpct,
            disc,
            tipeKendaraan,
            merkKendaraan;

    private LinearLayout layLoading5tahun;

    private RadioButton rbLoadingRate,
            rbLoadingResikoSendiri;


    private Switch AOG,
            AccType,
            swiKerusakan;

    private Spinner spnTpl,
            spnPA,
            spnPenggunaan;
    private String kodeProduk;
    private boolean isNeedApprove;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_fill_otomate);
        ButterKnife.bind(this);

        context = FillOtomateActivity.this;
        nf = NumberFormat.getInstance();


        getProductAction();
        getKodeProduk();
        initLayout();
        InitEditText();
        RegisterListener();
        init();

    }

    private void getKodeProduk() {
        try {
            Cursor cursor;

            DBA_PRODUCT_OTOMATE dba = new DBA_PRODUCT_OTOMATE(this);
            dba.open();

            if (SPPA_ID != 0L) {
                cursor = dba.getRow(SPPA_ID);
                cursor.moveToFirst();
                kodeProduk = cursor.getString(cursor.getColumnIndex(DBA_PRODUCT_OTOMATE.KODE_PRODUK));
            } else {
                kodeProduk = GeneralSetting.getParameterValue(Var.GN_OTOMATE_PICKED);
            }

            kodeProduk = TextUtils.isEmpty(kodeProduk) ? Var.OTOMATE : kodeProduk;

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initLayout() {
        try {
            String namaProduk = Scalar.getProdukName(this, kodeProduk);
            lblTitle.setText(namaProduk);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getProductAction() {
        try {
            Intent i = getIntent();
            b = i.getExtras();

            PRODUCT_ACTION = b.getString("PRODUCT_ACTION");
            SPPA_ID = b.getLong("SPPA_ID");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void init() {
        try {

            if (PRODUCT_ACTION.equals("EDIT") || PRODUCT_ACTION.equals("VIEW")) {
                SPPA_ID = b.getLong("SPPA_ID");
                LoadDB();
            } else if (PRODUCT_ACTION.equals("VIEW.UNPAID")) {
                SPPA_ID = b.getLong("SPPA_ID");
                LoadDB();
                disableView();
            }
            else if (PRODUCT_ACTION.equalsIgnoreCase("SYNC")) {
                SPPA_ID = b.getLong("SPPA_ID");
                LoadDB();
                insertDB();
                setResult(RESULT_OK);
                this.finish();
            }
            else {
                findViewById(R.id.btnDelete).setVisibility(View.GONE);
            }

            if (b.getString("CUSTOMER_NO") != null) {
                customerno.setText(b.getString("CUSTOMER_NO"));
                customername.setText(b.getString("CUSTOMER_NAME"));
            }

            MAX_DISCOUNT = Utility.getMaxDiscount(getBaseContext(), "21");

            if (Utility.getIsDiscountable(getBaseContext(), "03").equals("0")) {
                disc.setEnabled(false);
                discpct.setEnabled(false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void disableView() {
        nopol1.setEnabled(false);
        nopol2.setEnabled(false);
        nopol3.setEnabled(false);
        model.setEnabled(false);
        tahun.setEnabled(false);
        chassisNo.setEnabled(false);
        machineNo.setEnabled(false);
        color.setEnabled(false);
        perlengkapan.setEnabled(false);
        seat.setEnabled(false);
        jangkaWaktuEff.setEnabled(false);
        jangkaWaktuExp.setEnabled(false);
        nilaiPertanggungan.setEnabled(false);
        nilaiPerlengkapan.setEnabled(false);
        spnTpl.setEnabled(false);
        spnPA.setEnabled(false);
        rate.setEnabled(false);
        premi.setEnabled(false);
        polis.setEnabled(false);
        materai.setEnabled(false);
        total.setEnabled(false);
        customerno.setEnabled(false);
        customername.setEnabled(false);
        discpct.setEnabled(false);
        disc.setEnabled(false);
        tipeKendaraan.setEnabled(false);
        merkKendaraan.setEnabled(false);
        spnPA.setEnabled(false);
        spnPenggunaan.setEnabled(false);
        spnTpl.setEnabled(false);
        risikoSendiri.setEnabled(false);
        swiKerusakan.setEnabled(false);
        keteranganKerusakan.setEnabled(false);
        spnFlood.setEnabled(false);
        spnEq.setEnabled(false);
        swiSRCC.setEnabled(false);
        swiTS.setEnabled(false);
        swiBengkel.setEnabled(false);


        nopol1.setTextColor(Color.BLACK);
        nopol2.setTextColor(Color.BLACK);
        nopol3.setTextColor(Color.BLACK);
        model.setTextColor(Color.BLACK);
        tahun.setTextColor(Color.BLACK);
        chassisNo.setTextColor(Color.BLACK);
        machineNo.setTextColor(Color.BLACK);
        color.setTextColor(Color.BLACK);
        perlengkapan.setTextColor(Color.BLACK);
        seat.setTextColor(Color.BLACK);
        jangkaWaktuEff.setTextColor(Color.BLACK);
        jangkaWaktuExp.setTextColor(Color.BLACK);
        nilaiPertanggungan.setTextColor(Color.BLACK);
        nilaiPerlengkapan.setTextColor(Color.BLACK);
        rate.setTextColor(Color.BLACK);
        premi.setTextColor(Color.BLACK);
        polis.setTextColor(Color.BLACK);
        materai.setTextColor(Color.BLACK);
        total.setTextColor(Color.BLACK);
        customerno.setTextColor(Color.BLACK);
        customername.setTextColor(Color.BLACK);
        discpct.setTextColor(Color.BLACK);
        disc.setTextColor(Color.BLACK);
        tipeKendaraan.setTextColor(Color.BLACK);
        merkKendaraan.setTextColor(Color.BLACK);

        risikoSendiri.setTextColor(Color.BLACK);
        keteranganKerusakan.setTextColor(Color.BLACK);

        (findViewById(R.id.btnChooseCustomer)).setEnabled(false);
        (findViewById(R.id.swiAccType)).setEnabled(false);
        (findViewById(R.id.swiAOG)).setEnabled(false);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 100 && requestCode == 200) {
            carBrand = data.getStringExtra("CAR_BRAND");
            carIDBrand = data.getStringExtra("CAR_ID");

            if (carBrand != null && carIDBrand != null) {
                merkKendaraan.setText(carBrand);
                merkKendaraan.setTag(carIDBrand);
                tipeKendaraan.setText("");
                tipeKendaraan.setEnabled(true);
            }
        } else if (resultCode == 10 && requestCode == 20) {

            carType = data.getStringExtra("CAR_TYPE");
            carIDType = data.getStringExtra("CAR_ID");

            if (carType != null && carIDType != null) {
                tipeKendaraan.setText(carType);
                tipeKendaraan.setTag(carIDType);
            }
        }
    }

    private void blockLCGC(String carType) {
        Log.i(TAG, "::setRateKendaraanKhusus:" + "car type :" + carType);

        if (carType.equalsIgnoreCase("AGYA") ||
                carType.equalsIgnoreCase("AYLA") ||
                carType.equalsIgnoreCase("BRIO SATYA") ||
                carType.equalsIgnoreCase("WAGON-R")) {
        }
    }


    private void calculateAll() throws ParseException, InterruptedException {
        v_tsi = getTSI();
        v_rate = getRate() + getRatePerluasan();
        v_premi = getPremi();
        v_discpct = Utility.parseDouble(discpct);
        v_disc = Utility.countDiscount(v_premi, v_discpct);
        v_polis = 0;
        v_materai = Utility.countMaterai(v_premi);
        v_total = Utility.countTotal(v_premi, v_polis, v_materai, v_disc);

        NumberFormat nfRate = NumberFormat.getInstance();
        nfRate.setMinimumFractionDigits(5);

        rate.setText(nfRate.format(v_rate));
        premi.setText(nf.format(v_premi));
        disc.setText(nf.format(v_disc));
        polis.setText(nf.format(v_polis));
        materai.setText(nf.format(v_materai));
        total.setText(nf.format(v_total));
    }

    private void RegisterListener() {
        swiKerusakan.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                keteranganKerusakan.setText("");

                if (isChecked)
                    (findViewById(R.id.sectionKerusakan)).setVisibility(View.VISIBLE);
                else
                    (findViewById(R.id.sectionKerusakan)).setVisibility(View.GONE);
            }
        });

        rbLoadingResikoSendiri.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    risikoSendiri.setText(nf.format(500000));
                } else {
                    risikoSendiri.setText(nf.format(300000));
                }
            }
        });

        rbLoadingRate.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    calculateAll();
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        risikoSendiri.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub


                risikoSendiri.setTextColor(Color.BLACK);
                try {
                    if (!s.toString().equals(current)) {
                        risikoSendiri.removeTextChangedListener(this);

                        String replaceable = String.format("[%s,.\\s]", "Rp. ");
                        String cleanString = s.toString().replaceAll(
                                replaceable, "");

                        double parsed;
                        try {
                            parsed = Double.parseDouble(cleanString);
                        } catch (NumberFormatException e) {
                            parsed = 0.00;
                        }
                        String formatted = nf.format(parsed);

                        current = formatted;
                        risikoSendiri.setText(formatted);
                        risikoSendiri.setSelection(formatted.length());
                        risikoSendiri.addTextChangedListener(this);


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        nilaiPertanggungan.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

                nilaiPertanggungan.setTextColor(Color.BLACK);
                try {
                    if (!s.toString().equals(current)) {
                        nilaiPertanggungan.removeTextChangedListener(this);

                        String replaceable = String.format("[%s,.\\s]", "Rp. ");
                        String cleanString = s.toString().replaceAll(
                                replaceable, "");

                        double parsed;
                        try {
                            parsed = Double.parseDouble(cleanString);
                        } catch (NumberFormatException e) {
                            parsed = 0.00;
                        }
                        String formatted = nf.format(parsed);

                        current = formatted;
                        nilaiPertanggungan.setText(formatted);
                        nilaiPertanggungan.setSelection(formatted.length());
                        nilaiPertanggungan.addTextChangedListener(this);

                        calculateAll();
                        // validasiNilaiTSI();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        nilaiPerlengkapan.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

                nilaiPerlengkapan.setTextColor(Color.BLACK);
                try {
                    if (!s.toString().equals(current)) {
                        nilaiPerlengkapan.removeTextChangedListener(this);

                        String replaceable = String.format("[%s,.\\s]", "Rp. ");
                        String cleanString = s.toString().replaceAll(
                                replaceable, "");

                        double parsed;
                        try {
                            parsed = Double.parseDouble(cleanString);
                        } catch (NumberFormatException e) {
                            parsed = 0.00;
                        }
                        String formatted = nf.format(parsed);

                        current = formatted;
                        nilaiPerlengkapan.setText(formatted);
                        nilaiPerlengkapan.setSelection(formatted.length());
                        nilaiPerlengkapan.addTextChangedListener(this);

                        calculateAll();
                        // validasiNilaiTSI();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        spnTpl.setOnItemSelectedListener(calculateAllListener());
        spnPA.setOnItemSelectedListener(calculateAllListener());

        spnFlood.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {

                    SettingOtomate settingOtomate = new Select()
                            .from(SettingOtomate.class)
                            .where(SettingOtomate_Table.KodeProduct.eq(kodeProduk))
                            .querySingle();
                    boolean isPaket = Boolean.parseBoolean(settingOtomate.IsPaket);

                    if (isPaket) {
                        String idFlood = FloodBind.getID(spnFlood);
                        EqBind.select(spnEq, idFlood);
                    }

                    if (!nilaiPertanggungan.getText().toString().isEmpty())
                        calculateAll();

                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnEq.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {

                    SettingOtomate settingOtomate = new Select()
                            .from(SettingOtomate.class)
                            .where(SettingOtomate_Table.KodeProduct.eq(kodeProduk))
                            .querySingle();
                    boolean isPaket = Boolean.parseBoolean(settingOtomate.IsPaket);

                    if (isPaket) {
                        String idEq = EqBind.getID(spnEq);
                        FloodBind.select(spnFlood, idEq);
                    }

                    if (!nilaiPertanggungan.getText().toString().isEmpty())
                        calculateAll();

                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        swiSRCC.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    if (!swiSRCC.isChecked()) {
                        swiTS.setChecked(false);
                    }

                    if (!nilaiPertanggungan.getText().toString().isEmpty())
                        calculateAll();

                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        swiTS.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    if (isChecked && !swiSRCC.isChecked()) {
                        swiSRCC.setChecked(true);
                        Utility.showCustomDialogInformation(FillOtomateActivity.this, "Informasi",
                                "Huru Hara harus dipilih jika Terorisme dan Sabotase dipilih");
                    }
                    if (!nilaiPertanggungan.getText().toString().isEmpty())
                        calculateAll();

                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        swiBengkel.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    if (!nilaiPertanggungan.getText().toString().isEmpty()) {
                        calculateAll();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        tahun.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

                tahun.setTextColor(Color.BLACK);
                try {
                    if (!s.toString().equals(current)) {
                        if (s.toString().length() == 4) {
                            if (diffYear() > 5) {
                                layLoading5tahun.setVisibility(View.VISIBLE);
                                rbLoadingRate.setChecked(true);
                            } else {
                                layLoading5tahun.setVisibility(View.GONE);
                                risikoSendiri.setText(nf.format(300000));
                            }
                        }

                        // if (diffYear() > 7 ) {
                        // tahun.setTextColor(Color.RED);
                        // Utility.showCustomDialogInformation(FillOtomateActivity.this,
                        // "Informasi", "Umur kendaaraan maksimal 7 tahun");
                        // }
                        // else if (diffYear() < 0) {
                        // tahun.setTextColor(Color.RED);
                        // Utility.showCustomDialogInformation(FillOtomateActivity.this,
                        // "Informasi", "Input tahun tidak valid");
                        // }
                        // else

                        calculateAll();
                        // validasiNilaiTSI();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        nopol1.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

                nopol1.setTextColor(Color.BLACK);
                try {
                    if (!s.toString().equals(current)) {
                        calculateAll();
                        // validasiNilaiTSI();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        jangkaWaktuEff.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean hasFocus) {
                if (hasFocus)
                    showDialog(DATE_FROM_ID);
                else {
                }
            }
        });
        jangkaWaktuEff.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showDialog(DATE_FROM_ID);
            }
        });

        AOG.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                try {
                    calculateAll();
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


        discpct.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    discpct.setTextColor(Color.BLACK);
                    if (!nilaiPertanggungan.getText().toString().isEmpty() && !tahun.getText().toString().isEmpty())
                        calculateAll();
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


        disc.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
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

        merkKendaraan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),
                        ChooseCarBrandActivity.class);
                i.putExtras(b);
                startActivityForResult(i, 200);
            }
        });


        merkKendaraan.setKeyListener(null);

        tipeKendaraan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),
                        ChooseCarTypeActivity.class);
                b.putString("BRAND_CODE", carIDBrand);
                i.putExtras(b);
                startActivityForResult(i, 20);
            }
        });

        tipeKendaraan.setKeyListener(null);
    }


    private OnItemSelectedListener calculateAllListener() {
        return new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                try {


                    if (!nilaiPertanggungan.getText().toString().isEmpty())
                        calculateAll();

                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        };
    }

    private void validasiTanggal() {
        ((Button) findViewById(R.id.btnNext)).setEnabled(true);
        jangkaWaktuEff.setTextColor(Color.BLACK);
        boolean flag = Utility.validasiEffDate(jangkaWaktuEff.getText()
                        .toString(),
                Utility.GetTomorrowString(Utility.GetTodayString()),
                getBaseContext());

        if (!flag) {
            jangkaWaktuEff.setTextColor(Color.RED);
            Utility.showCustomDialogInformation(FillOtomateActivity.this,
                    "Warning", "Tanggal mulai minimal besok hari");
            ((Button) findViewById(R.id.btnNext)).setEnabled(false);
        }
    }

    // private double validasiUmur() {
    // Calendar c = Calendar.getInstance();
    // int todayYear = c.get(Calendar.YEAR);
    //
    // if (tahun.getText().toString().isEmpty())
    // return 0.0;
    //
    // int vtahun = Integer.parseInt(tahun.getText().toString());
    // double loadingrate = 0.0 ;
    //
    // if (vtahun+ maxTahun < todayYear){
    // tahun.setTextColor(Color.RED);
    // flag ++ ;
    //
    // Utility.showCustomDialogInformation(FillOtomateActivity.this,
    // "Informasi",
    // "Umur kendaran maksimal 7 tahun");
    // }
    // else if ((todayYear-vtahun) == 6 ){
    // loadingrate = 5/100;
    // }
    // else if ((todayYear-vtahun) == 7 ){
    // loadingrate = 10/100;
    // }
    // return loadingrate;
    // }

    private void InitEditText() {

        customerno = (EditText) findViewById(R.id.txtCustomerNo);
        customername = (EditText) findViewById(R.id.txtCustomerName);

        model = (EditText) findViewById(R.id.txtModel);
        tahun = (EditText) findViewById(R.id.txtYear);

        merkKendaraan = (EditText) findViewById(R.id.txtMake);
        tipeKendaraan = (EditText) findViewById(R.id.txtType);

        nopol1 = (EditText) findViewById(R.id.txtNopol1);
        nopol2 = (EditText) findViewById(R.id.txtNopol2);
        nopol3 = (EditText) findViewById(R.id.txtNopol3);

        chassisNo = (EditText) findViewById(R.id.txtChassisNo);
        machineNo = (EditText) findViewById(R.id.txtMachineNo);
        color = (EditText) findViewById(R.id.txtColor);
        perlengkapan = (EditText) findViewById(R.id.txtPerlengkapan);
        seat = (EditText) findViewById(R.id.txtSeat);


        keteranganKerusakan = (EditText) findViewById(R.id.txtKerusakan);
        jangkaWaktuEff = (EditText) findViewById(R.id.txtEffDate);
        jangkaWaktuExp = (EditText) findViewById(R.id.txtExpDate);
        jangkaWaktuExp.setKeyListener(null);


        nilaiPertanggungan = (EditText) findViewById(R.id.txtNilaiPertanggungan);
        nilaiPerlengkapan = (EditText) findViewById(R.id.txtNilaiPerlengkapan);
        risikoSendiri = (EditText) findViewById(R.id.txtRisiko);
        risikoSendiri.setText(nf.format(300000));


        rate = (EditText) findViewById(R.id.txtRate);
        premi = (EditText) findViewById(R.id.txtPremi);
        discpct = (EditText) findViewById(R.id.txtDiscountPct);
        disc = (EditText) findViewById(R.id.txtDiscount);
        materai = (EditText) findViewById(R.id.txtMaterai);
        polis = (EditText) findViewById(R.id.txtPolicyCost);
        total = (EditText) findViewById(R.id.txtTotal);

        discpct.setText(nf.format(0.0));

        layLoading5tahun = (LinearLayout) findViewById(R.id.sectionLoading5tahun);


        AOG = (Switch) findViewById(R.id.swiAOG);
        AccType = (Switch) findViewById(R.id.swiAccType);
        swiKerusakan = (Switch) findViewById(R.id.swiKerusakan);


        spnTpl = (Spinner) findViewById(R.id.spnTPL);
        spnPA = (Spinner) findViewById(R.id.spnPA);
        spnPenggunaan = (Spinner) findViewById(R.id.spnPenggunaan);


        rbLoadingRate = (RadioButton) findViewById(R.id.rbLoadingRate);
        rbLoadingResikoSendiri = (RadioButton) findViewById(R.id.rbLoadingResikoSendiri);


        (findViewById(R.id.sectionKerusakan)).setVisibility(View.GONE);


        FloodBind.bind(this, spnFlood, kodeProduk);
        EqBind.bind(this, spnEq, kodeProduk);
        TPLBind.bind(this, spnTpl, kodeProduk);
        PABind.bind(this, spnPA, kodeProduk);
        bindPerluasan();

//        Utility.BindTpl(spnTpl, FillOtomateActivity.this, FillOtomateActivity.this);
//        Utility.BindPA(spnPA, FillOtomateActivity.this, FillOtomateActivity.this);
        Utility.BindPenggunaan(spnPenggunaan, FillOtomateActivity.this, FillOtomateActivity.this);

    }

    private void bindPerluasan() {

        try {
            SettingOtomate settingOtomate = SettingOtomate.get(kodeProduk);
            boolean srcc = Boolean.parseBoolean(settingOtomate.SRCCDefault);
            boolean ts = Boolean.parseBoolean(settingOtomate.TSDefault);
            boolean isChangeAble = Boolean.parseBoolean(settingOtomate.IsChangeable);
            boolean bengkel = Boolean.parseBoolean(settingOtomate.BengkelDefault);
            boolean isChangeAbleBengkel = Boolean.parseBoolean(settingOtomate.IsChangeableBengkel);

            swiSRCC.setChecked(srcc);
            swiTS.setChecked(ts);
            swiBengkel.setChecked(bengkel);

            swiSRCC.setEnabled(isChangeAble);
            swiTS.setEnabled(isChangeAble);
            swiBengkel.setEnabled(isChangeAbleBengkel);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void LoadDB() {

        DBA_PRODUCT_MAIN dbm = null;
        DBA_PRODUCT_OTOMATE dba = null;

        Cursor cm = null;
        Cursor c = null;

        try {

            dbm = new DBA_PRODUCT_MAIN(getBaseContext());
            dbm.open();
            cm = dbm.getRow(SPPA_ID);
            cm.moveToFirst();

            dba = new DBA_PRODUCT_OTOMATE(getBaseContext());
            dba.open();
            c = dba.getRow(SPPA_ID);
            c.moveToFirst();

            customerno.setText(cm.getString(1));
            customername.setText(cm.getString(2));

            carIDBrand = c.getString(2);
            carIDType = c.getString(3);

            merkKendaraan.setTag(c.getString(2));
            merkKendaraan.setText(c.getString(28));
            tipeKendaraan.setTag(c.getString(3));
            tipeKendaraan.setText(c.getString(29));


            tahun.setText(c.getString(4));
            nopol1.setText(c.getString(5));
            nopol2.setText(c.getString(6));
            nopol3.setText(c.getString(7));

            color.setText(c.getString(8));
            chassisNo.setText(c.getString(9));
            machineNo.setText(c.getString(10));
            perlengkapan.setText(c.getString(11));

            seat.setText(String.valueOf(c.getInt(12)));
            jangkaWaktuEff.setText(String.valueOf(c.getString(13)));
            jangkaWaktuExp.setText(String.valueOf(c.getString(14)));
            nilaiPertanggungan.setText(nf.format(c.getDouble(15)));
            nilaiPerlengkapan.setText(nf.format(c.getDouble(16)));

            if (c.getString(17).equals("1"))
                AOG.setChecked(true);
            else
                AOG.setChecked(false);

            FloodBind.select(spnFlood, c.getString(c.getColumnIndex(DBA_PRODUCT_OTOMATE.FLOOD)));
            EqBind.select(spnEq, c.getString(c.getColumnIndex(DBA_PRODUCT_OTOMATE.EQ)));

            if (!TextUtils.isEmpty(c.getString(c.getColumnIndex(DBA_PRODUCT_OTOMATE.SRCC)))) {
                swiSRCC.setChecked(c.getString(c.getColumnIndex(DBA_PRODUCT_OTOMATE.SRCC)).equalsIgnoreCase("1") ? true : false);
            }
            if (!TextUtils.isEmpty(c.getString(c.getColumnIndex(DBA_PRODUCT_OTOMATE.TS)))) {
                swiTS.setChecked(c.getString(c.getColumnIndex(DBA_PRODUCT_OTOMATE.TS)).equalsIgnoreCase("1") ? true : false);
            }
            if (!TextUtils.isEmpty(c.getString(c.getColumnIndex(DBA_PRODUCT_OTOMATE.TS)))) {
                swiBengkel.setChecked(c.getString(c.getColumnIndex(DBA_PRODUCT_OTOMATE.BENGKEL)).equalsIgnoreCase("1") ? true : false);
            }
            TPLBind.select(spnTpl, String.valueOf(c.getDouble(18)));
            PABind.select(spnPA, c.getString(30));
            SpinnerGenericAdapter a;
            a = (SpinnerGenericAdapter) spnPenggunaan.getAdapter();
            spnPenggunaan.setSelection(a.getItemId(c.getString(32)));

            risikoSendiri.setText(nf.format(c.getDouble(31)));
            if (c.getInt(33) == 1)
                swiKerusakan.setChecked(true);
            else
                swiKerusakan.setChecked(false);

            keteranganKerusakan.setText(c.getString(34));

            if (c.getInt(35) != 2)
                rbLoadingRate.setChecked(true);
            else
                rbLoadingResikoSendiri.setChecked(true);

            rate.setText(nf.format(c.getDouble(19)));
            premi.setText(nf.format(c.getDouble(20)));
            discpct.setText(nf.format(cm.getDouble(23)));
            disc.setText(nf.format(cm.getDouble(24)));
            polis.setText(nf.format(c.getDouble(21)));
            materai.setText(nf.format(c.getDouble(22)));
            total.setText(nf.format(c.getDouble(23)));

            model.setText(c.getString(26));

            if (c.getString(27).equals("1"))
                AccType.setChecked(true);
            else
                AccType.setChecked(false);

            if (cm.getString(20).equals("R")) {
                model.setEnabled(false);
                merkKendaraan.setEnabled(false);
                tipeKendaraan.setEnabled(false);
                tahun.setEnabled(false);
                nopol1.setEnabled(false);
                nopol2.setEnabled(false);
                nopol3.setEnabled(false);
                color.setEnabled(false);
                chassisNo.setEnabled(false);
                machineNo.setEnabled(false);
                seat.setEnabled(false);
                jangkaWaktuEff.setEnabled(false);
                jangkaWaktuExp.setEnabled(false);

                maxTahun = 7;
                customerno.setEnabled(false);
                ((Button) findViewById(R.id.btnChooseCustomer))
                        .setEnabled(false);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putAll(b);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fill_otomate, menu);
        return true;
    }

    public void btnHomeClick(View v) {
        Intent i = new Intent(getBaseContext(), FirstActivity.class);
        startActivity(i);
        this.finish();
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

    public void btnBackClick(View v) {
        Back();
    }

    public void btnDeleteClick(View v) {

        showConfirmDelete(getBaseContext(), SPPA_ID, "").show();
    }

    private AlertDialog showConfirmDelete(final Context ctx, final long rowId,
                                          final String noPolis) {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                .setTitle("Hapus")
                .setMessage("Hapus data ini sekarang?")
                .setIcon(R.drawable.delete)
                .setPositiveButton("Delete",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int whichButton) {

                                new DeleteSPPA().execute();
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        }).create();
        return myQuittingDialogBox;
    }

    @Override
    public void onBackPressed() {
        Back();
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
        if (merkKendaraan.getText().toString().isEmpty()) {
            flag++;
            merkKendaraan.setHintTextColor(Color.RED);
            UIErrorMessage = "Merk kendaraan harus dipilih";
            return;
        }
        if (tipeKendaraan.getText().toString().isEmpty()) {
            flag++;
            tipeKendaraan.setHintTextColor(Color.RED);
            UIErrorMessage = "Tipe kendaraan harus dipilih";
            return;
        }
        if (model.getText().toString().isEmpty()) {
            flag++;
            model.setHintTextColor(Color.RED);
            UIErrorMessage = "Model kendaraan harus diisi";
            return;
        }
        if (tahun.getText().toString().isEmpty()) {
            flag++;
            tahun.setHintTextColor(Color.RED);
            UIErrorMessage = "Tahun kendaraan harus diisi";
            return;
        }

        if (diffYear() < 0) {
            flag++;
            tahun.setTextColor(Color.RED);
            UIErrorMessage = "Tahun kendaraan tidak valid";
            return;
        }

        if (diffYear() > maxTahun) {
            flag++;
            tahun.setTextColor(Color.RED);
            UIErrorMessage = "Umur Kendaraan maksimal 7 tahun";
            return;
        }

        if (nopol1.getText().toString().isEmpty()) {
            flag++;
            nopol1.setHintTextColor(Color.RED);
            UIErrorMessage = "No Polisi ke-1 harus diisi";
            return;
        }
        if (nopol2.getText().toString().isEmpty()) {
            flag++;
            nopol2.setHintTextColor(Color.RED);
            UIErrorMessage = "No Polisi ke-2 harus diisi";
            return;
        }

        String plat1 = nopol1.getText().toString().toUpperCase();

        if (!(plat1.equals("A") || plat1.equals("B") || plat1.equals("C")
                || plat1.equals("D") || plat1.equals("E") || plat1.equals("F")
                || plat1.equals("T") || plat1.equals("Z")
                || plat1.equals("G")
                || plat1.equals("H")
                || plat1.equals("K")
                || plat1.equals("L")
                || plat1.equals("M")
                || plat1.equals("N")
                || plat1.equals("P")
                || plat1.equals("R")
                || plat1.equals("S")
                || plat1.equals("AA")
                || plat1.equals("AB")
                || plat1.equals("AD")
                || plat1.equals("AE")
                || plat1.equals("AG")
                || plat1.equals("W")
                || plat1.equals("DB")
                || plat1.equals("DD")
                || plat1.equals("DM")
                || plat1.equals("DN")
                || plat1.equals("DE")
                || plat1.equals("DG")
                || plat1.equals("DH")
                || plat1.equals("DK")
                || plat1.equals("DL")
                || plat1.equals("DR")
                || plat1.equals("DS")
                || plat1.equals("EA")
                || plat1.equals("EB")
                || plat1.equals("ED")
                || plat1.equals("DA") || plat1.equals("KB") || plat1.equals("KT")
                || plat1.equals("BA") || plat1.equals("BB") || plat1.equals("BD")
                || plat1.equals("BE") || plat1.equals("BG")
                || plat1.equals("BH") || plat1.equals("BK")
                || plat1.equals("BL") || plat1.equals("BM")
                || plat1.equals("BP") || plat1.equals("BN")
                || plat1.equals("CC") || plat1.equals("CD")
        )) {
            flag++;
            nopol1.setTextColor(Color.RED);
            UIErrorMessage = "Kode Plat No tidak valid untuk produk Otomate";
            return;
        }
        if (color.getText().toString().isEmpty()) {
            flag++;
            color.setHintTextColor(Color.RED);
            UIErrorMessage = "Warna harus diisi";
            return;
        }
        if (chassisNo.getText().toString().isEmpty()) {
            flag++;
            chassisNo.setHintTextColor(Color.RED);
            UIErrorMessage = "No Rangka harus diisi";
            return;
        }
        if (machineNo.getText().toString().isEmpty()) {
            flag++;
            machineNo.setHintTextColor(Color.RED);
            UIErrorMessage = "No Mesin harus diisi";
            return;
        }

        if (perlengkapan.getText().toString().isEmpty()) {
            flag++;
            perlengkapan.setHintTextColor(Color.RED);
            UIErrorMessage = "Perlengkapan harus diisi";
            return;
        }

        if (jangkaWaktuEff.getText().toString().isEmpty()) {
            flag++;
            jangkaWaktuEff.setHintTextColor(Color.RED);
            UIErrorMessage = "Tanggal mulai harus diisi";
            return;
        }
        if (jangkaWaktuExp.getText().toString().isEmpty()) {
            flag++;
            jangkaWaktuExp.setHintTextColor(Color.RED);
            UIErrorMessage = "Tanggal berakhir harus diisi";
            return;
        }
        if (nilaiPertanggungan.getText().toString().isEmpty()) {
            flag++;
            nilaiPertanggungan.setHintTextColor(Color.RED);
            UIErrorMessage = "Nilai Pertanggungan harus diisi";
            return;
        }
        if (nilaiPerlengkapan.getText().toString().isEmpty()) {
            nilaiPerlengkapan.setText("0");

        }
        if (risikoSendiri.getText().toString().isEmpty()) {
            flag++;
            risikoSendiri.setHintTextColor(Color.RED);
            UIErrorMessage = "Risiko sendiri harus diisi";
            return;
        }


        if (swiKerusakan.isChecked() && keteranganKerusakan.getText().toString().isEmpty()) {
            flag++;
            keteranganKerusakan.setHintTextColor(Color.RED);
            UIErrorMessage = "Keterangan kerusakan harus diisi";
            return;
        }


        DBA_MASTER_PRODUCT_SETTING dbSetting = new DBA_MASTER_PRODUCT_SETTING(this);
        Cursor cSetting = null;
        double maxTSI = 0;
        double minTSI = 0;

        try {
            dbSetting.open();
            cSetting = dbSetting.getRow("03");

            if (!cSetting.moveToFirst())
                return;

            minTSI = nf.parse(cSetting.getString(3)).doubleValue();
            maxTSI = nf.parse(cSetting.getString(4)).doubleValue();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dbSetting != null)
                dbSetting.close();

            if (cSetting != null)
                cSetting.close();

        }


        if (getNilaiPertanggungan() < minTSI) {
            flag++;
            nilaiPertanggungan.setTextColor(Color.RED);
            UIErrorMessage = "Nilai pertanggungan minimal " + nf.format(minTSI);
            return;
        }

        if (getNilaiPertanggungan() > maxTSI) {
            flag++;
            nilaiPertanggungan.setTextColor(Color.RED);
            UIErrorMessage = "Nilai pertanggungan maksimal " + nf.format(maxTSI);
            return;
        }
        if (getNilaiPerlengkapan() > (0.1 * getNilaiPertanggungan())) {
            flag++;
            nilaiPerlengkapan.setTextColor(Color.RED);
            UIErrorMessage = "Nilai perlengkapan maksimal 10% nilai pertanggungan";
            return;
        }

        if (layLoading5tahun.getVisibility() == View.GONE) {
            if (nf.parse(risikoSendiri.getText().toString()).doubleValue() < 300000) {
                flag++;
                risikoSendiri.setTextColor(Color.RED);
                UIErrorMessage = "Risiko sendiri minimal 300 ribu";
                return;
            }
        } else {
            if (rbLoadingResikoSendiri.isChecked()) {
                if (nf.parse(risikoSendiri.getText().toString()).doubleValue() < 500000) {
                    flag++;
                    risikoSendiri.setTextColor(Color.RED);
                    UIErrorMessage = "Risiko Sendiri minimal 500 ribu jika kendaraan lebih dari 5 tahun";
                    return;
                }
            } else {
                if (nf.parse(risikoSendiri.getText().toString()).doubleValue() < 300000) {
                    flag++;
                    risikoSendiri.setTextColor(Color.RED);
                    UIErrorMessage = "Risiko sendiri minimal 300 ribu";
                    return;
                }
            }
        }


        if (rate.getText().toString().isEmpty()) {
            flag++;
            rate.setHintTextColor(Color.RED);
            UIErrorMessage = "Rate tidak bisa dihitung. Hubungi IT ACA";
            return;
        }
        if (premi.getText().toString().isEmpty()) {
            flag++;
            premi.setHintTextColor(Color.RED);
            UIErrorMessage = "Premi tidak bisa dihitung. Hubungi IT ACA";
            return;
        }
        if (discpct.getText().toString().isEmpty()) {
            flag++;
            discpct.setHintTextColor(Color.RED);
            return;
        }
        if (disc.getText().toString().isEmpty()) {
            flag++;
            disc.setHintTextColor(Color.RED);
            return;
        }

        if (Utility.parseDouble(discpct) > MAX_DISCOUNT) {
            flag++;
            discpct.setTextColor(Color.RED);
            UIErrorMessage = "Diskon terlalu besar, maksimum adalah "
                    + String.valueOf(MAX_DISCOUNT) + "%";
            return;
        }

    }

    public void btnNextClick(View v) {
        try {
            seat.setText("1");

            validasiNext();
            // validasiUmur();

            if (flag != 0)
                Utility.showCustomDialogInformation(FillOtomateActivity.this,
                        "Warning", UIErrorMessage);
            else {
                calculateAll();
                insertDB();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void btnChooseCustomerClick(View v) {
        Intent i = new Intent(getBaseContext(),
                ChooseCustomerForBuyActivity.class);
        i.putExtras(b);
        startActivity(i);
        this.finish();
        //
        // if (customerno.getText().length() == 0) {
        // Intent i = new Intent(getBaseContext(),
        // ChooseCustomerForBuyActivity.class);
        // i.putExtras(b);
        // startActivity(i);
        // this.finish();
        // }
        // else {
        // try {
        // custList = Utility.getCustData(getBaseContext(),
        // customerno.getText().toString());
        // if(custList != null)
        // customername.setText(custList.get(0).get("CUSTOMER_NAME"));
        // else {
        // customerno.setTextColor(Color.RED);
        // Toast.makeText(getBaseContext(), "Nasabah tidak ditemukan",
        // Toast.LENGTH_SHORT).show();
        // customername.setText("");
        // }
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }
        // }
    }

    public void showDisc(View v) {
        TableRow trDiscPct = (TableRow) findViewById(R.id.trDiscPct);
        TableRow trDisc = (TableRow) findViewById(R.id.trDisc);

        if (trDisc.getVisibility() == View.GONE) {
            trDiscPct.setVisibility(View.VISIBLE);
            trDisc.setVisibility(View.VISIBLE);
        } else {
            trDiscPct.setVisibility(View.GONE);
            trDisc.setVisibility(View.GONE);
        }
    }

    private double getTPL() throws ParseException {
        return nf.parse(((SpinnerGenericItem) spnTpl.getSelectedItem()).getCode()).doubleValue();
    }

    private double getPA() throws ParseException {
        return nf.parse(((SpinnerGenericItem) spnPA.getSelectedItem()).getCode()).doubleValue();
    }

    private double getAOG(String wilayah) {
        return 0;
//
//        if (AOG.isChecked()) {
//            DBA_MASTER_OTOMATE_AOG dba = new DBA_MASTER_OTOMATE_AOG(this);
//            Cursor c = null;
//
//            try {
//                dba.open();
////                c = dba.getByWilayah(wilayah);
//
//                if (!c.moveToFirst()) return 0;
//
//                Log.i(TAG, "::getAOG:" + "rate aog = " + c.getDouble(1));
//
//                return c.getDouble(1);
//            } catch (Exception e) {
//                e.printStackTrace();
//                return 0;
//            } finally {
//                if (dba != null)
//                    dba.close();
//
//                if (c != null)
//                    c.close();
//            }
//        } else
//            return 0;
    }

    private double getPremi() throws ParseException {
        double TSI = getTSI();
     /*   double TPL = getTPL();
        double PA = getPA();*/
        double TPL = Double.parseDouble(TPLBind.getPremi(spnTpl));
        double PA = Double.parseDouble(PABind.getPremi(spnPA));

        double nilaiPremi = (TSI * v_rate / 100) + TPL + PA;

        return nilaiPremi;
    }

    private double getTJH() throws ParseException {
        return nf.parse(((SpinnerGenericItem) spnTpl.getSelectedItem()).getCode()).doubleValue();
    }

    private double getNilaiPertanggungan() throws ParseException {
        double pertanggungan;

        if (nilaiPertanggungan.getText().toString().isEmpty())
            pertanggungan = 0;
        else
            pertanggungan = nf.parse(nilaiPertanggungan.getText().toString())
                    .doubleValue();

        return pertanggungan;
    }

    private double getNilaiPerlengkapan() throws ParseException {
        double perlengkapan;

        if (nilaiPerlengkapan.getText().toString().isEmpty())
            perlengkapan = 0;
        else
            perlengkapan = nf.parse(nilaiPerlengkapan.getText().toString())
                    .doubleValue();

        return perlengkapan;
    }

    private double getRatePerluasan() {
        try {
            String jaminan = "AR";
            String wilayah = cekPlat(nopol1.getText().toString().toUpperCase());
            String vehicle_category = "0";
            String floodTipe = FloodBind.getID(spnFlood);
            String eqTipe = EqBind.getID(spnEq);
            SettingOtomate settingOtomate = SettingOtomate.get(kodeProduk);

            boolean isChangeAble = Boolean.parseBoolean(settingOtomate.IsChangeable);
            double rateFlood = getFloodEq(wilayah, floodTipe, 1);
            double rateEQ = getFloodEq(wilayah, eqTipe, 2);
            double rateSRCC = 0;
            double rateTS = 0;
            double rateBengkel;

            rateBengkel = swiBengkel.isChecked() ? rateBengkel = getFloodEq(wilayah, "3", 10) : 0;

            if (isChangeAble) {
                rateSRCC = swiSRCC.isChecked() ? getRatePerluasan(jaminan, "0", vehicle_category, "3", "0") : 0;
                rateTS = swiTS.isChecked() ? getRatePerluasan(jaminan, "0", vehicle_category, "4", "0") : 0;
            }

            Log.i(TAG, "::getRatePerluasan:" + "rate flood : " + rateFlood);
            Log.i(TAG, "::getRatePerluasan:" + "rate eq : " + rateEQ);
            Log.i(TAG, "::getRatePerluasan:" + "rate srcc : " + rateSRCC);
            Log.i(TAG, "::getRatePerluasan:" + "rate ts : " + rateTS);


            return rateFlood + rateEQ + rateSRCC + rateTS + rateBengkel;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;

    }


    private double getRatePerluasan(String jaminan, String wilayah, String vehicle_category, String exco, String jnp) {
        DBA_MASTER_KONVE_RATE dba = null;
        Cursor c = null;

        try {
            dba = new DBA_MASTER_KONVE_RATE(this);
            dba.open();
            c = dba.getRate(wilayah, vehicle_category, exco, jaminan, jnp);

            if (!c.moveToFirst())
                return 0;

            return c.getDouble(0);

        } catch (Exception e) {
            e.printStackTrace();

            return 0;

        } finally {
            if (dba != null)
                dba.close();

            if (c != null)
                c.close();
        }

    }


    private void validasiNilaiTSI() {
        // ((Button)findViewById(R.id.btnNext)).setEnabled(true);
        // nilaiPertanggungan.setTextColor(Color.BLACK);
        // nilaiPerlengkapan.setTextColor(Color.BLACK);
        // tjh.setTextColor(Color.BLACK);

        DBA_MASTER_PRODUCT_SETTING dbSetting = new DBA_MASTER_PRODUCT_SETTING(this);
        Cursor cSetting = null;
        double maxTSI = 0;
        double minTSI = 0;

        try {
            dbSetting.open();
            cSetting = dbSetting.getRow("03");

            if (!cSetting.moveToFirst())
                return;

            minTSI = cSetting.getDouble(3);
            maxTSI = cSetting.getDouble(4);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dbSetting != null)
                dbSetting.close();

            if (cSetting != null)
                cSetting.close();

        }


        boolean flag = true;
        try {
            if (getNilaiPertanggungan() < minTSI) {
                flag = false;
                nilaiPertanggungan.setTextColor(Color.RED);
                UIErrorMessage = "Nilai pertanggungan minimal " + minTSI;
                return;
            }

            if (getNilaiPertanggungan() > maxTSI) {
                flag = false;
                nilaiPertanggungan.setTextColor(Color.RED);
                UIErrorMessage = "Nilai pertanggungan maksimal " + maxTSI;
                return;
            }
            if (getNilaiPerlengkapan() > (0.1 * getNilaiPertanggungan())) {
                flag = false;
                nilaiPerlengkapan.setTextColor(Color.RED);
                UIErrorMessage = "Nilai perlengkapan maksimal 10% nilai pertanggungan";
                return;
            }

//			if (getTJH() < 20000000.00) {
//				flag = false;
//				tjh.setTextColor(Color.RED);
//				UIErrorMessage = "Nilai TJH minimal 20 juta";
//				return;
//			}
//			if (getTJH() > 70000000.00) {
//				flag = false;
//				tjh.setTextColor(Color.RED);
//				UIErrorMessage = "Nilai TJH maksimal 70 juta";
//				return;
//			}

            Log.i("nilai pertanggungan", getNilaiPertanggungan() + "");
            Log.i("nilai perlengkapan", getNilaiPerlengkapan() + "");
            Log.i("tjh", getTJH() + "");

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (!flag) {
            // ((Button)findViewById(R.id.btnNext)).setEnabled(false);
        }

    }

    private double getTSI() throws ParseException {
        double pertanggungan = getNilaiPertanggungan();
        double perlengkapan = getNilaiPerlengkapan();

        double TSI = pertanggungan + perlengkapan;

        return TSI;
    }

    private double getLoadingRate() {
        double rate = 0.05;

        if (diffYear() > 5)
            rate = rate * (diffYear() - 5);
        else
            rate = 0;

        return rate;
    }

    private int diffYear() {
        Calendar c = Calendar.getInstance();

        int tahunKend = Integer.parseInt(tahun.getText().toString());
        int tahunToday = c.get(Calendar.YEAR);

        return tahunToday - tahunKend;
    }

    private double getRate() throws InterruptedException, ParseException {
        DBA_MASTER_OTOMATE_RATE dba = null;
        Cursor c = null;
        double v = 0.0;

        String Wilayah,
                plat1,
                floodTipe,
                eqTipe;

        try {

            plat1 = nopol1.getText().toString().toUpperCase();
            Wilayah = "3";
            Wilayah = cekPlat(plat1);
//            floodTipe = FloodBind.getID(spnFlood);
//            eqTipe = EqBind.getID(spnEq);

            dba = new DBA_MASTER_OTOMATE_RATE(getBaseContext());
            dba.open();
            c = dba.getRate(String.valueOf(nf.format(v_tsi)), Wilayah, kodeProduk);


 /*           if (c.moveToFirst()) {
                if (rbLoadingRate.isChecked()) {
                    v = (c.getDouble(2) * (1 + getLoadingRate()))
                            + getAOG(Wilayah)
                            + getFloodEq(Wilayah, floodTipe, 1)
                            + getFloodEq(Wilayah, eqTipe, 2);
                } else {
                    v = c.getDouble(2)
                            + getAOG(Wilayah)
                            + getFloodEq(Wilayah, floodTipe, 1)
                            + getFloodEq(Wilayah, eqTipe, 2);
                }
            }
*/
            if (c.moveToFirst()) {
                if (rbLoadingRate.isChecked()) {
                    v = (c.getDouble(2) * (1 + getLoadingRate()));
                } else {
                    v = c.getDouble(2);
                }
            }

            Log.i(TAG, "::getRate:" + "new rate : " + v);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null)
                c.close();

            if (dba != null)
                dba.close();
        }

        return v;
    }

    private double getFloodEq(String wilayah, String tipe, int exco) {
        DBA_MASTER_OTOMATE_AOG dba = null;
        Cursor cursor = null;
        try {
            dba = new DBA_MASTER_OTOMATE_AOG(this);
            dba.open();
            cursor = dba.getByWilayah(wilayah, kodeProduk, tipe, String.valueOf(exco));

            if (!cursor.moveToFirst())
                return 0;

            double rate = cursor.getDouble(cursor.getColumnIndex(Var.RATE));
            return rate;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dba != null) dba.close();
            if (cursor != null) cursor.close();
        }
        return 0;
    }

    private String cekPlat(String plat1) {
        if (plat1.equals("BA") || plat1.equals("BB") || plat1.equals("BD")
                || plat1.equals("BE") || plat1.equals("BG")
                || plat1.equals("BH") || plat1.equals("BK")
                || plat1.equals("BL") || plat1.equals("BM")
                || plat1.equals("BP") || plat1.equals("BN")
                || plat1.equals("CC") || plat1.equals("CD"))
            return "1";
        else if (plat1.equals("A") || plat1.equals("B")
                || plat1.equals("C") || plat1.equals("D")
                || plat1.equals("E") || plat1.equals("F")
                || plat1.equals("T") || plat1.equals("Z"))
            return "2";
        else if (plat1.equals("G") || plat1.equals("H")
                || plat1.equals("K") || plat1.equals("L")
                || plat1.equals("M") || plat1.equals("N")
                || plat1.equals("P") || plat1.equals("R")
                || plat1.equals("S") || plat1.equals("AA")
                || plat1.equals("AB") || plat1.equals("AD")
                || plat1.equals("AE") || plat1.equals("AG")
                || plat1.equals("W") || plat1.equals("DB")
                || plat1.equals("DD") || plat1.equals("DM")
                || plat1.equals("DN") || plat1.equals("DE")
                || plat1.equals("DG") || plat1.equals("DH")
                || plat1.equals("DK") || plat1.equals("DL")
                || plat1.equals("DR") || plat1.equals("DS")
                || plat1.equals("EA") || plat1.equals("EB")
                || plat1.equals("ED") || plat1.equals("DA")
                || plat1.equals("KB") || plat1.equals("KT"))
            return "3";

        return "2";
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            Back();

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        c = Calendar.getInstance();

        int Y = c.get(Calendar.YEAR);
        int M = c.get(Calendar.MONTH);
        int D = c.get(Calendar.DAY_OF_MONTH);

        switch (id) {
            case DATE_FROM_ID:
                return new DatePickerDialog(this, datePickerListener, Y, M, D + 1);
            case DATE_TO_ID:
                return new DatePickerDialog(this, datePickerListenerNext, Y + 1, M,
                        D);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            String theDate = Utility.setUIDate(selectedYear, selectedMonth,
                    selectedDay);
            jangkaWaktuEff.setText(theDate);
            jangkaWaktuExp.setText(Utility.GetTodayNextYearString(theDate));
            validasiTanggal();
        }
    };

    private DatePickerDialog.OnDateSetListener datePickerListenerNext = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            jangkaWaktuExp.setText(Utility.setUIDate(selectedYear,
                    selectedMonth, selectedDay));
        }
    };

    private void insertDB() throws ParseException {

        DBA_PRODUCT_OTOMATE dba = null;
        DBA_PRODUCT_MAIN dba2 = null;

        try {

            if (PRODUCT_ACTION.equals("VIEW")) {
                Intent i = new Intent(getBaseContext(), ConfirmActivity.class);
                b.putLong("SPPA_ID", SPPA_ID);
                i.putExtras(b);
                startActivity(i);
                this.finish();
            } else {

                dba = new DBA_PRODUCT_OTOMATE(getBaseContext());
                dba2 = new DBA_PRODUCT_MAIN(getBaseContext());

                dba.open();
                dba2.open();

                String aog;
                String acctype;

                if (AOG.isChecked())
                    aog = "1";
                else
                    aog = "0";

                if (AccType.isChecked())
                    acctype = "1";
                else
                    acctype = "0";

                if (PRODUCT_ACTION.equals("NEW")) {
                    SPPA_ID = dba2
                            .initialInsert(customerno.getText().toString(),
                                    customername.getText().toString(),
                                    "OTOMATE",
                                    nf.parse(premi.getText().toString())
                                            .doubleValue(),
                                    nf.parse(materai.getText().toString())
                                            .doubleValue(),
                                    nf.parse(polis.getText().toString())
                                            .doubleValue(),
                                    nf.parse(total.getText().toString())
                                            .doubleValue(), jangkaWaktuEff
                                            .getText().toString(),
                                    jangkaWaktuExp.getText().toString(),
                                    Utility.DateToString(Utility.GetToday(),
                                            "dd/MM/yyyy"), "", "", "", "N", "",
                                    nf.parse((discpct.getText().toString()))
                                            .doubleValue(),
                                    nf.parse((disc.getText().toString()))
                                            .doubleValue());

                    dba.initialInsert(SPPA_ID, merkKendaraan.getTag()
                                    .toString(), tipeKendaraan.getTag().toString(),
                            model.getText().toString(), tahun.getText()
                                    .toString(), nopol1.getText().toString()
                                    .toUpperCase(),
                            nopol2.getText().toString(), nopol3.getText()
                                    .toString().toUpperCase(), color.getText()
                                    .toString().toUpperCase(), chassisNo
                                    .getText().toString().toUpperCase(),
                            machineNo.getText().toString().toUpperCase(),
                            acctype, perlengkapan.getText().toString()
                                    .toUpperCase(), 1, jangkaWaktuEff.getText()
                                    .toString(), jangkaWaktuExp.getText()
                                    .toString(),
                            nf.parse(nilaiPertanggungan.getText().toString())
                                    .doubleValue(),
                            nf.parse(nilaiPerlengkapan.getText().toString())
                                    .doubleValue(), aog,
                            TPLBind.getID(spnTpl),
                            nf.parse(rate.getText().toString()).doubleValue(),
                            nf.parse(premi.getText().toString()).doubleValue(),
                            nf.parse(materai.getText().toString())
                                    .doubleValue(),
                            nf.parse(polis.getText().toString()).doubleValue(),
                            nf.parse(total.getText().toString()).doubleValue(),
                            merkKendaraan.getText().toString(),
                            tipeKendaraan.getText().toString(),
                            "OTOMATE",
                            customername.getText().toString(),
                            PABind.getID(spnPA),
                            nf.parse(risikoSendiri.getText().toString()).doubleValue(),
                            ((SpinnerGenericItem) spnPenggunaan.getSelectedItem()).getCode(),
                            swiKerusakan.isChecked() ? "1" : "0",
                            keteranganKerusakan.getText().toString(),
                            rbLoadingRate.isChecked() ? 1 : 2,

                            FloodBind.getID(spnFlood),
                            EqBind.getID(spnEq),
                            swiSRCC.isChecked() ? "1" : "0",
                            swiTS.isChecked() ? "1" : "0",
                            kodeProduk,
                            swiBengkel.isChecked() ? "1" : "0"
                    );

                } else {
                    dba2.nextInsert(SPPA_ID, customerno.getText().toString(),
                            customername.getText().toString(),
                            nf.parse(premi.getText().toString()).doubleValue(),
                            nf.parse(materai.getText().toString())
                                    .doubleValue(),
                            nf.parse(polis.getText().toString()).doubleValue(),
                            nf.parse(total.getText().toString()).doubleValue(),
                            jangkaWaktuEff.getText().toString(), jangkaWaktuExp
                                    .getText().toString(), Utility
                                    .DateToString(Utility.GetToday(),
                                            "dd/MM/yyyy"),
                            nf.parse((discpct.getText().toString()))
                                    .doubleValue(),
                            nf.parse((disc.getText().toString())).doubleValue());

                    dba.nextInsert(
                            SPPA_ID,

                            merkKendaraan.getTag().toString(),
                            tipeKendaraan.getTag().toString(),
                            model.getText().toString(),
                            tahun.getText().toString(),

                            nopol1.getText().toString().toUpperCase(),
                            nopol2.getText().toString(),
                            nopol3.getText().toString().toUpperCase(),

                            color.getText().toString().toUpperCase(),
                            chassisNo.getText().toString().toUpperCase(),
                            machineNo.getText().toString().toUpperCase(),
                            acctype,
                            perlengkapan.getText().toString().toUpperCase(),
                            1,

                            jangkaWaktuEff.getText().toString(),
                            jangkaWaktuExp.getText().toString(),

                            nf.parse(nilaiPertanggungan.getText().toString()).doubleValue(),
                            nf.parse(nilaiPerlengkapan.getText().toString()).doubleValue(),

                            aog,

                            TPLBind.getID(spnTpl),

                            nf.parse(rate.getText().toString()).doubleValue(),
                            nf.parse(premi.getText().toString()).doubleValue(),
                            nf.parse(materai.getText().toString()).doubleValue(),
                            nf.parse(polis.getText().toString()).doubleValue(),
                            nf.parse(total.getText().toString()).doubleValue(),

                            merkKendaraan.getText().toString(),
                            tipeKendaraan.getText().toString(),

                            "OTOMATE",
                            customername.getText().toString(),

                            PABind.getID(spnPA),
                            nf.parse(risikoSendiri.getText().toString()).doubleValue(),
                            ((SpinnerGenericItem) spnPenggunaan.getSelectedItem()).getCode(),

                            swiKerusakan.isChecked() ? "1" : "0",
                            keteranganKerusakan.getText().toString(),
                            rbLoadingRate.isChecked() ? 1 : 2,

                            FloodBind.getID(spnFlood),
                            EqBind.getID(spnEq),
                            swiSRCC.isChecked() ? "1" : "0",
                            swiTS.isChecked() ? "1" : "0",
                            kodeProduk,
                            swiBengkel.isChecked() ? "1" : "0"
                    );
                }

                if (!PRODUCT_ACTION.equalsIgnoreCase("SYNC")) {
                    Intent i = new Intent(getBaseContext(), ConfirmActivity.class);
                    b.putLong("SPPA_ID", SPPA_ID);
                    i.putExtras(b);
                    startActivity(i);
                    FillOtomateActivity.this.finish();
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (dba != null)
                dba.close();

            if (dba2 != null)
                dba2.close();
        }
    }

    public class DeleteSPPA extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progDialog = null;
        private boolean error = false;
        private String flag = "";

        protected void onPreExecute() {
            progDialog = ProgressDialog.show(FillOtomateActivity.this, "", "Processing...", false);
        }

        @Override
        protected Void doInBackground(Void... params) {

            DBA_PRODUCT_MAIN dba = null;
            DBA_PRODUCT_OTOMATE dba2 = null;


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

                dba2 = new DBA_PRODUCT_OTOMATE(context);
                dba2.open();

                // dapatin no SPPA
                c = dba.getRow(SPPA_ID);
                c.moveToNext();
                E_SPPA_NO = c.getString(5);

                Log.d("-->", E_SPPA_NO);

                if (E_SPPA_NO == null || E_SPPA_NO.length() == 0
                        || !TextUtils.isDigitsOnly(E_SPPA_NO)) {
                    Log.d("-->", "EMPTY");

                    // hapus data di local db
                    dba.delete(SPPA_ID);
                    dba2.delete(SPPA_ID);

                    // hapus directory
                    Utility.DeleteDirectory(SPPA_ID);
                } else {
                    // coba hapus SPPA di server
                    envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.implicitTypes = true;
                    envelope.dotNet = true; // used only if we use the
                    // webservice from a dot net file
                    // (asmx)
                    androidHttpTransport = new HttpTransportSE(URL);
                    request = new SoapObject(NAMESPACE, METHOD_NAME);

                    request.addProperty(Utility.GetPropertyInfo("SPPANo",
                            E_SPPA_NO, String.class));

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
                // errorMessage = new MasterExceptionClass(e).getException();
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

        protected void onPostExecute(Void result) {
            try {
                progDialog.dismiss();
                progDialog = null;

                if (error)
                    Toast.makeText(getBaseContext(), "Data gagal dihapus",
                            Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(getBaseContext(), "Data berhasil dihapus",
                            Toast.LENGTH_SHORT).show();

                    Intent i = null;
                    i = new Intent(getBaseContext(), SyncActivity.class);
                    startActivity(i);
                }
            } catch (Exception ex) {
                //
            }
        }
    }


}
