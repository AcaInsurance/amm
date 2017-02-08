package com.aca.amm;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aca.HelperClass.WebServices;
import com.aca.Retrofit.TravelService;
import com.aca.database.DBA_MASTER_ACA_MOBIL_RATE;
import com.aca.database.DBA_MASTER_AGENT;
import com.aca.database.DBA_MASTER_ASRI_RATE;
import com.aca.database.DBA_MASTER_BUSINESS_TYPE;
import com.aca.database.DBA_MASTER_CAR_BRAND;
import com.aca.database.DBA_MASTER_CAR_BRAND_SYARIAH;
import com.aca.database.DBA_MASTER_CAR_BRAND_TRUK;
import com.aca.database.DBA_MASTER_CAR_TYPE;
import com.aca.database.DBA_MASTER_CAR_TYPE_SYARIAH;
import com.aca.database.DBA_MASTER_CITY_PROVINCE;
import com.aca.database.DBA_MASTER_COUNTRY;
import com.aca.database.DBA_MASTER_EXECUTIVE_SAFE_RATE;
import com.aca.database.DBA_MASTER_JENIS_TOKO;
import com.aca.database.DBA_MASTER_KONVE_RATE;
import com.aca.database.DBA_MASTER_LOB;
import com.aca.database.DBA_MASTER_MEDISAFE_RATE;
import com.aca.database.DBA_MASTER_OCCUPATION;
import com.aca.database.DBA_MASTER_OTOMATE_AOG;
import com.aca.database.DBA_MASTER_OTOMATE_PA;
import com.aca.database.DBA_MASTER_OTOMATE_RATE;
import com.aca.database.DBA_MASTER_OTOMATE_TPL;
import com.aca.database.DBA_MASTER_PA_AMANAH_RATE;
import com.aca.database.DBA_MASTER_PRODUCT_SETTING;
import com.aca.database.DBA_MASTER_TOKO_RATE;
import com.aca.database.DBA_MASTER_TRAVELSAFE_DOM_RATE;
import com.aca.database.DBA_MASTER_TRAVELSAFE_INT_RATE;
import com.aca.database.DBA_MASTER_VESSEL_DETAIL;
import com.aca.database.DBA_MASTER_VESSEL_TYPE;
import com.aca.database.DBA_MASTER_WELLWOMAN_RATE;
import com.aca.database.DBA_PRODUCT_MAIN;
import com.aca.database.DBA_TABLE_CREATE_ALL;
import com.aca.database.DBA_TABLE_CREATE_BIG;
import com.aca.database.DBA_TABLE_VERSION;
import com.aca.dbflow.GeneralSetting;
import com.aca.dbflow.GeneralSetting_Table;
import com.aca.dbflow.PaketOtomate;
import com.aca.dbflow.PerluasanPremi;
import com.aca.dbflow.SettingOtomate;
import com.aca.dbflow.StandardField;
import com.aca.dbflow.SubProduct;
import com.aca.dbflow.SubProductPlan;
import com.aca.dbflow.SubProductPlanAdd;
import com.aca.dbflow.SubProductPlanBDA;
import com.aca.dbflow.SubProductPlanBasic;
import com.aca.dbflow.VersionAndroid;
import com.aca.util.Var;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;

import org.apache.http.conn.ConnectTimeoutException;
import org.joda.time.LocalDateTime;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import rx.Observable;
import rx.Observer;
import rx.functions.Func5;
import rx.functions.Func6;
import rx.observers.Observers;
import rx.schedulers.Schedulers;

public class SplashActivity extends Activity implements RetriveLastVersionAppsListener {


    private boolean statusSync = false;
    private boolean statusFinished = false;

    private int iWaitedTime = 0;

    private Boolean bNeedSyncTokoRate = statusSync;
    private Boolean bNeedSyncTSDomRate = statusSync;
    private Boolean bNeedSyncTSIntRate = statusSync;
    private Boolean bNeedSyncCarBrand = statusSync;

    private Boolean bNeedSyncCarType = statusSync;
    private Boolean bNeedSyncCity = statusSync;
    private Boolean bNeedSyncCountry = statusSync;
    private Boolean bNeedSyncToko = statusSync;
    private Boolean bNeedSyncJob = statusSync;
    private Boolean bNeedSyncUsaha = statusSync;
    private Boolean bNeedSyncASRIRate = statusSync;
    private Boolean bNeedSyncOtomateRate = statusSync;
    private Boolean bNeedSyncMedisafeRate = statusSync;
    private Boolean bNeedSyncExecutiveRate = statusSync;
    private Boolean bNeedSyncPAAmanahRate = statusSync;
    private Boolean bNeedSyncProductSetting = statusSync;
    private Boolean bNeedSyncACAMobilRate = true;
    private Boolean bNeedSyncWellWomanRate = statusSync;
    private Boolean bNeedSyncDNOBusType = statusSync;
    private Boolean bNeedSyncVesselType = statusSync;
    private Boolean bNeedSyncVesselDetail = statusSync;
    private Boolean bNeedSyncOtomateTPL = statusSync;
    private Boolean bNeedSyncOtomatePA = statusSync;
    private Boolean bNeedSyncOtomateAOG = statusSync;
    private Boolean bNeedSyncKonveRate = statusSync;
    private Boolean bNeedSyncCarBrandTruk = statusSync;
    private Boolean bNeedSyncLoadPaketOtomate = statusSync;
    private Boolean bNeedSyncLoadPerluasan = statusSync;
    private Boolean bNeedSyncLoadOtomateSetting = statusSync;
    private Boolean bNeedSyncLoadStandardField = statusSync;

    private Boolean bNeedSyncLabbaikMaster = statusSync;

    private Boolean bFinishCarBrand = statusFinished;
    private Boolean bFinishCarType = statusFinished;
    private Boolean bFinishCity = statusFinished;
    private Boolean bFinishCountry = statusFinished;
    private Boolean bFinishToko = statusFinished;
    private Boolean bFinishJob = statusFinished;
    private Boolean bFinishUsaha = statusFinished;
    private Boolean bFinishASRIRate = statusFinished;
    private Boolean bFinishOtomateRate = statusFinished;
    private Boolean bFinishTokoRate = statusFinished;
    private Boolean bFinishTSDomRate = statusFinished;
    private Boolean bFinishTSIntRate = statusFinished;
    private Boolean bFinishMedisafeRate = statusFinished;
    private Boolean bFinishExecutiveRate = statusFinished;
    private Boolean bFinishPAAmanahRate = statusFinished;
    private Boolean bFinishProductSetting = statusFinished;
    private Boolean bFinishACAMobilRate = statusFinished;
    private Boolean bFinishWellWomanRate = statusFinished;
    private Boolean bFinishBusType = statusFinished;
    private Boolean bFinishVesselType = statusFinished;
    private Boolean bFinishVesselDetail = statusFinished;
    private Boolean bFinishCarBrandTruk = statusFinished;
    private Boolean bFinishOtomateTPL = statusFinished;
    private Boolean bFinishOtomatePA = statusFinished;
    private Boolean bFinishOtomateAOG = statusFinished;
    private Boolean bFinishKonveRate = statusFinished;
    private Boolean bFinishLoadOtomateSetting = statusFinished;
    private Boolean bFinishLoadPaketOtomatee = statusFinished;
    private Boolean bFinishLoadPerluasan = statusFinished;
    private Boolean bFinishLoadStandardField = statusFinished;

    private Boolean bFinishLoadLabbaikMaster = statusFinished;


    private Boolean bFailLaunch = false;

    private SoapObject requestretrive_latest_version = null;
    private SoapObject requestretrive_car_brand = null;
    private SoapObject requestretrive_car_type = null;
    private SoapObject requestretrive_city = null;
    private SoapObject requestretrive_country = null;
    private SoapObject requestretrive_country_non_asia = null;
    private SoapObject requestretrive_toko = null;
    private SoapObject requestretrive_job = null;
    private SoapObject requestretrive_usaha = null;
    private SoapObject requestretrive_bus_type = null;
    private SoapObject requestretrive_vessel_type = null;
    private SoapObject requestretrive_vessel_detail = null;
    private SoapObject requestretrive_car_brand_truk = null;

    private SoapObject requestretrive_Otomate_TPL = null;
    private SoapObject requestretrive_Otomate_PA = null;
    private SoapObject requestretrive_Otomate_AOG = null;
    private SoapObject requestretrive_Konve_Rate = null;


    private SoapObject requestretrive_asri_rate = null;
    private SoapObject requestretrive_otomate_rate = null;
    private SoapObject requestretrive_toko_rate = null;
    private SoapObject requestretrive_ts_dom_rate = null;
    private SoapObject requestretrive_ts_int_rate = null;
    private SoapObject requestretrive_medisafe_rate = null;
    private SoapObject requestretrive_executive_rate = null;
    private SoapObject requestretrive_pa_amanah_rate = null;
    private SoapObject requestretrive_product_setting = null;
    private SoapObject requestretrive_sppa_status = null;
    private SoapObject requestretrive_acamobil_rate = null;
    private SoapObject requestretrive_wellwoman_rate = null;

    private SoapSerializationEnvelope envelope_latest_version = null;
    private SoapSerializationEnvelope envelope_car_brand = null;
    private SoapSerializationEnvelope envelope_car_type = null;
    private SoapSerializationEnvelope envelope_city = null;
    private SoapSerializationEnvelope envelope_country = null;
    private SoapSerializationEnvelope envelope_country_non_asia = null;
    private SoapSerializationEnvelope envelope_toko = null;
    private SoapSerializationEnvelope envelope_job = null;
    private SoapSerializationEnvelope envelope_usaha = null;
    private SoapSerializationEnvelope envelope_bus_type = null;
    private SoapSerializationEnvelope envelope_vessel_type = null;
    private SoapSerializationEnvelope envelope_vessel_detail = null;
    private SoapSerializationEnvelope envelope_car_brand_truk = null;

    private SoapSerializationEnvelope envelope_otomate_tpl = null;
    private SoapSerializationEnvelope envelope_otomate_pa = null;
    private SoapSerializationEnvelope envelope_otomate_aog = null;
    private SoapSerializationEnvelope envelope_konve_rate = null;

    private SoapSerializationEnvelope envelope_asri_rate = null;
    private SoapSerializationEnvelope envelope_otomate_rate = null;
    private SoapSerializationEnvelope envelope_toko_rate = null;
    private SoapSerializationEnvelope envelope_ts_dom_rate = null;
    private SoapSerializationEnvelope envelope_ts_int_rate = null;
    private SoapSerializationEnvelope envelope_medisafe_rate = null;
    private SoapSerializationEnvelope envelope_executive_rate = null;
    private SoapSerializationEnvelope envelope_pa_amanah_rate = null;
    private SoapSerializationEnvelope envelope_product_setting = null;
    private SoapSerializationEnvelope envelope_sppa_status = null;
    private SoapSerializationEnvelope envelope_acamobil_rate = null;
    private SoapSerializationEnvelope envelope_wellwoman_rate = null;


    private HttpTransportSE androidHttpTransport = null;

    private static String NAMESPACE = "http://tempuri.org/";
    private static String URL = Utility.getURL();

    private static String SOAP_ACTION_LATEST_VERSION = "http://tempuri.org/GetLatestVersion";
    private static String METHOD_NAME_LATEST_VERSION = "GetLatestVersion";

    private static String SOAP_ACTION_CITY = "http://tempuri.org/GetListCity";
    private static String METHOD_NAME_CITY = "GetListCity";

    private static String SOAP_ACTION_COUNTRY = "http://tempuri.org/GetListCountry";
    private static String METHOD_NAME_COUNTRY = "GetListCountry";

    private static String SOAP_ACTION_JENIS_TOKO = "http://tempuri.org/GetListJenisToko";
    private static String METHOD_NAME_JENIS_TOKO = "GetListJenisToko";

    private static String SOAP_ACTION_JOB = "http://tempuri.org/GetListJob";
    private static String METHOD_NAME_JOB = "GetListJob";

    private static String SOAP_ACTION_USAHA = "http://tempuri.org/GetListUsaha";
    private static String METHOD_NAME_USAHA = "GetListUsaha";

    private static String SOAP_ACTION_CAR_BRAND = "http://tempuri.org/GetCarBrand";
    private static String METHOD_NAME_CAR_BRAND = "GetCarBrand";

    private static String SOAP_ACTION_CAR_TYPE = "http://tempuri.org/GetCarTypeAll";
    private static String METHOD_NAME_CAR_TYPE = "GetCarTypeAll";

    private static String SOAP_ACTION_ASRI_RATE = "http://tempuri.org/GetRateASRIAll";
    private static String METHOD_NAME_ASRI_RATE = "GetRateASRIAll";

    private static String SOAP_ACTION_OTOMATE_RATE = "http://tempuri.org/GetRateOtomateAll";
    private static String METHOD_NAME_OTOMATE_RATE = "GetRateOtomateAll";

    private static String SOAP_ACTION_TOKO_RATE = "http://tempuri.org/GetRateTokoAll";
    private static String METHOD_NAME_TOKO_RATE = "GetRateTokoAll";

    private static String SOAP_ACTION_TS_DOM_RATE = "http://tempuri.org/GetPremiTSDomestikAll";
    private static String METHOD_NAME_TS_DOM_RATE = "GetPremiTSDomestikAll";

    private static String SOAP_ACTION_TS_INT_RATE = "http://tempuri.org/GetPremiTSIntAll";
    private static String METHOD_NAME_TS_INT_RATE = "GetPremiTSIntAll";

    private static String SOAP_ACTION_MEDISAFE_RATE = "http://tempuri.org/GetPremiMedisafeAll";
    private static String METHOD_NAME_MEDISAFE_RATE = "GetPremiMedisafeAll";

    private static String SOAP_ACTION_EXECUTIVE_RATE = "http://tempuri.org/GetPremiExecutiveSafeAll";
    private static String METHOD_NAME_EXECUTIVE_RATE = "GetPremiExecutiveSafeAll";

    private static String SOAP_ACTION_PA_AMANAH_RATE = "http://tempuri.org/GetPremiPAAll";
    private static String METHOD_NAME_PA_AMANAH_RATE = "GetPremiPAAll";

    private static String SOAP_ACTION_PRODUCT_SETTING = "http://tempuri.org/GetProductSetting";
    private static String METHOD_NAME_PRODUCT_SETTING = "GetProductSetting";

    private static String SOAP_ACTION_SPPA_STATUS = "http://tempuri.org/GetSPPAStatus";
    private static String METHOD_NAME_SPPA_STATUS = "GetSPPAStatus";

    private static String SOAP_ACTION_ACAMOBIL_RATE = "http://tempuri.org/GetRateMobilAll";
    private static String METHOD_NAME_ACAMOBIL_RATE = "GetRateMobilAll";

    private static String SOAP_ACTION_WELLWOMAN_RATE = "http://tempuri.org/GetPremiWellWomanAll";
    private static String METHOD_NAME_WELLWOMAN_RATE = "GetPremiWellWomanAll";

    private static String SOAP_ACTION_BUS_TYPE = "http://tempuri.org/GetJenisUsahaLiability ";
    private static String METHOD_NAME_BUS_TYPE = "GetJenisUsahaLiability ";

    private static String SOAP_ACTION_VESSEL_TYPE = "http://tempuri.org/GetVesselType ";
    private static String METHOD_NAME_VESSEL_TYPE = "GetVesselType ";

    private static String SOAP_ACTION_VESSEL_DETAIL = "http://tempuri.org/GetVesselDetil ";
    private static String METHOD_NAME_VESSEL_DETAIL = "GetVesselDetil ";


    private static String SOAP_ACTION_OTOMATE_TPL = "http://tempuri.org/GetPremiTPLOtomate ";
    private static String METHOD_NAME_OTOMATE_TPL = "GetPremiTPLOtomate ";


    private static String SOAP_ACTION_OTOMATE_PA = "http://tempuri.org/GetPremiPAOtomate ";
    private static String METHOD_NAME_OTOMATE_PA = "GetPremiPAOtomate ";


    private static String SOAP_ACTION_OTOMATE_AOG = "http://tempuri.org/GetRateAOGOtomate ";
    private static String METHOD_NAME_OTOMATE_AOG = "GetRateAOGOtomate ";


    private static String SOAP_ACTION_KONVE_RATE = "http://tempuri.org/GetRateMotorCoven ";
    private static String METHOD_NAME_KONVE_RATE = "GetRateMotorCoven ";

    private static String SOAP_ACTION_CAR_BRAND_TRUK = "http://tempuri.org/GetCarBrandTruk";
    private static String METHOD_NAME_CAR_BRAND_TRUK = "GetCarBrandTruk";


    private CarBrandWS wsCarBrand;
    private CarTypeWS wsCarType;
    private CityWS wsCity;
    private CountryWS wsCountry;
    private TokoWS wsToko;
    private JobWS wsJob;
    private UsahaWS wsUsaha;
    private BusTypeWS wsBusType;
    private VesselTypeWS wsVesselType;
    private VesselDetailWS wsVesselDetail;
    private CarBrandTrukWS wsCarBrandTruk;


    private OtomateTplWS wsOtomateTPL;
    private OtomatePaWS wsOtomatePA;
    private OtomateAOGWS wsOtomateAOG;
    private KonveRateWS wsKonveRate;


    private ASRIRateWS wsASRIRate;
    private OtomateRateWS wsOtomateRate;
    private MedisafeRateWS wsMedisafeRate;
    private ExecutiveSafeRateWS wsExecutiveSafeRate;
    private PAAmanahRateWS wsPAAmanahRate;
    private TravelSafeDomRateWS wsTSDomRate;
    private TravelSafeIntRateWS wsTSIntRate;
    private TokoRateWS wsTokoRate;
    private ACAMobilRateWS wsACAMobilRate;
    private WellWomanRateWS wsWellWomanRate;


    private ProductSettingWS wsProductSetting;
    private SPPAStatusWS wsSPPAStatus;

    private ProgressBar pb;
    private boolean errorSync = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);


        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        Utility.setAutoDateTime(this);


        pb = (ProgressBar) findViewById(R.id.progressBar1);

        wsCarBrand = new CarBrandWS();
        wsCarType = new CarTypeWS();
        wsCity = new CityWS();
        wsCountry = new CountryWS();
        wsToko = new TokoWS();
        wsJob = new JobWS();
        wsUsaha = new UsahaWS();
        wsBusType = new BusTypeWS();
        wsVesselType = new VesselTypeWS();
        wsVesselDetail = new VesselDetailWS();
        wsCarBrandTruk = new CarBrandTrukWS();

        wsASRIRate = new ASRIRateWS();
        wsOtomateRate = new OtomateRateWS();
        wsMedisafeRate = new MedisafeRateWS();
        wsExecutiveSafeRate = new ExecutiveSafeRateWS();
        wsPAAmanahRate = new PAAmanahRateWS();
        wsTSDomRate = new TravelSafeDomRateWS();
        wsTSIntRate = new TravelSafeIntRateWS();
        wsTokoRate = new TokoRateWS();
        wsACAMobilRate = new ACAMobilRateWS();
        wsWellWomanRate = new WellWomanRateWS();

        wsOtomateTPL = new OtomateTplWS();
        wsOtomatePA = new OtomatePaWS();
        wsOtomateAOG = new OtomateAOGWS();
        wsKonveRate = new KonveRateWS();

        wsProductSetting = new ProductSettingWS();
        wsSPPAStatus = new SPPAStatusWS();

        setMessageSync();
        checkLatestVersion();


    }

    private void setMessageSync() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.aca.amm", Context.MODE_PRIVATE);
        String isSuccessSync = sharedPreferences.getString("SUCCESS_SYNC", "FALSE");

        if (isSuccessSync.equals("TRUE")) {
            String message = getResources().getString(R.string.sync_message_not_init);

            ((TextView) findViewById(R.id.textView1))
                    .setText(message);
        } else {
            String message = getResources().getString(R.string.sync_message_init);

            ((TextView) findViewById(R.id.textView1))
                    .setText(message);

        }


    }

    private void checkLatestVersion() {
        boolean isConnect = Utility.cekInternetConnection(this);

        if (!isConnect)
            this.finish();

        RetrieveLatestVersionApps ws = new RetrieveLatestVersionApps(this);
        ws.mCallbackListener = this;
        ws.execute();

    }

    @Override
    public void getLastVersionApps(String versionCode, String tanggal, String maintenance) {


        if (TextUtils.isEmpty(versionCode) || TextUtils.isEmpty(tanggal)) {
            Toast.makeText(this, "Gagal mendapatkan versi terakhir", Toast.LENGTH_SHORT).show();
            SplashActivity.this.finish();
        }

        if (maintenance.equals("1")) {
            Dialog dialog = Utility.showCustomDialogInformation(SplashActivity.this, "Informasi", "Aplikasi sedang dalam perbaikan");
            dialog.setOnDismissListener(new OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    SplashActivity.this.finish();
                }
            });
        } else {
            try {
                if (verifyVersion(tanggal, Integer.parseInt(versionCode)))
                    startProgress();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    public boolean verifyVersion(String waktuUpdate, int version) {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            int currentVersion = pInfo.versionCode;


            LocalDateTime dateTime = Utility.parseUTC(waktuUpdate);
            int newVersion = version;

            LocalDateTime now = Utility.getDateTime();

            if (currentVersion < newVersion) {
                if (now.isAfter(dateTime)) {
                    popupUpdate();
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


    public void popupUpdate() {
        try {
            Dialog dialog = Utility.showCustomDialogInformation(SplashActivity.this, "Versi baru sudah tersedia", "Silahkan download melalui playstore");
            dialog.setOnDismissListener(new OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    final String appPackageName = getPackageName();
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));

                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                    SplashActivity.this.finish();

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isClose = false;

    public void startProgress() {
        Runnable runnable = new Runnable() {

            @Override
            public void run() {

                Looper.prepare();

                PrepareDB();
                PrepareDBLocal();
                SyncData();
                EmptySharedPreferences();

                do {
                    try {

                        Thread.sleep(1000);
                        iWaitedTime += 1000;

                        pb.post(new Runnable() {

                            @Override
                            public void run() {
                                pb.setProgress(iWaitedTime / 1000);

                                if (errorSync) {
                                    if (isClose)
                                        return;

                                    isClose = true;
                                    startActivity(new Intent(SplashActivity.this, FirstActivity.class)
                                            .putExtra("FAIL", "TRUE"));
                                    SplashActivity.this.finish();

                                }

                            }
                        });


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (iWaitedTime > 1000 * 60 * 10) {
                        bFailLaunch = true;
                        break;
                    }

                } while (!bFinishCarBrand || !bFinishCarType
                        || !bFinishCity || !bFinishCountry
                        || !bFinishToko || !bFinishJob
                        || !bFinishUsaha || !bFinishASRIRate
                        || !bFinishOtomateRate || !bFinishACAMobilRate
                        || !bFinishMedisafeRate || !bFinishExecutiveRate
                        || !bFinishPAAmanahRate || !bFinishTSDomRate
                        || !bFinishTSIntRate || !bFinishTokoRate
                        || !bFinishProductSetting || !bFinishWellWomanRate
                        || !bFinishBusType || !bFinishVesselType
                        || !bFinishVesselDetail || !bFinishOtomateTPL
                        || !bFinishOtomatePA || !bFinishOtomateAOG
                        || !bFinishKonveRate || !bFinishCarBrandTruk
                        || !bFinishLoadStandardField || !bFinishLoadPaketOtomatee
                        || !bFinishLoadPerluasan || !bFinishLoadOtomateSetting
                        || !bFinishLoadLabbaikMaster

                        );


                if (!bFailLaunch) {
                    finish();
                    DBA_MASTER_AGENT dba = new DBA_MASTER_AGENT(getBaseContext());
                    dba.open();
                    if (dba.getRow().getCount() != 0)
                        dba.updateStatusLogout();
                    dba.close();


                    Intent i = new Intent(getBaseContext(), FirstActivity.class);
                    i.putExtra("FAIL", "FALSE");
                    startActivity(i);
//					SplashActivity.this.finish();
                } else {
                    finish();
                }
            }
        };

        new Thread(runnable).start();
    }

    protected void EmptySharedPreferences() {
        SharedPreferences sharedPreferences = SplashActivity.this.getSharedPreferences("com.aca.amm", Context.MODE_PRIVATE);
        sharedPreferences
                .edit()
                .putLong("SESSION", System.currentTimeMillis())
                .apply();

    }

    // tanpa copy sqlite external dari assets
    private void PrepareDBLocal() {
        DBA_TABLE_CREATE_ALL dba_create = new DBA_TABLE_CREATE_ALL(SplashActivity.this);
        DBA_TABLE_CREATE_BIG dba_big = new DBA_TABLE_CREATE_BIG(SplashActivity.this);

        try {
            dba_create.open();
            dba_big.open();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dba_create.close();
            dba_big.close();
        }
    }


    private void PrepareDB() {

        Boolean error = false;
        String errorMessage = "";

        try {
            Utility.CreateDatabaseDir(getBaseContext());

            String destPath = "/data/data/" + getPackageName() + "/databases/AMM_VERSION_BIG";
            File f = new File(destPath);

//			Utility.CopyDB( getBaseContext().getAssets().open("AMM_VERSION_BIG"), new FileOutputStream(destPath));

            if (!f.exists()) {
                Utility.CopyDB(getBaseContext().getAssets().open("AMM_VERSION_BIG"), new FileOutputStream(destPath));


                //artinya ini aplikasi baru, sehingga folder LoadImg dihapus
//				CleanUpDirectory();
            }

        } catch (FileNotFoundException e) {
            error = true;
            errorMessage = e.getMessage();
            e.printStackTrace();
        } catch (IOException e) {
            error = true;
            errorMessage = e.getMessage();
            e.printStackTrace();
        } catch (Exception e) {
            error = true;
            errorMessage = e.getMessage();
            e.printStackTrace();
        }
    }

    private void CleanUpDirectory() {
        File directory = new File(Environment.getExternalStorageDirectory() + "/LoadImg");
        if (directory.exists()) {
            try {
                DeleteDirectoryFile(directory);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void DeleteDirectoryFile(File file) throws IOException {
        if (file.isDirectory()) {

            //directory is empty, then delete it
            if (file.list().length == 0) {
                file.delete();
            } else {

                //list all the directory contents
                String files[] = file.list();

                for (String temp : files) {
                    //construct the file structure
                    File fileDelete = new File(file, temp);

                    //recursive delete
                    DeleteDirectoryFile(fileDelete);
                }

                //check the directory again, if empty then delete it
                if (file.list().length == 0) {
                    file.delete();
                }
            }

        } else {
            //if file, then delete it
            file.delete();
        }
    }


    private void setForceSync() {
        try {
            VersionAndroid versionAndroid = VersionAndroid.get();
            String functionVersion = Var.GN_IS_SYNC_NEW_VERSION + "_" + versionAndroid.Version;

            GeneralSetting generalSetting = new Select()
                    .from(GeneralSetting.class)
                    .where(GeneralSetting_Table.ParameterCode.eq(functionVersion))
                    .querySingle();

            if (generalSetting == null) {
                GeneralSetting.insert(functionVersion, String.valueOf(Boolean.TRUE));

                int arrayFunction = getArrayFunction(versionAndroid.Version);
                DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(this);
                dbVersion.open();
                String[] functionName = getResources().getStringArray(arrayFunction);

                for (String f: functionName) {
                    dbVersion.setNeedSync(f);
                }
                dbVersion.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private int getArrayFunction(int version) {
        switch (version) {
            case 39: return R.array.function_39;
        }
        return 0;
    }

    private void SyncData() {

        requestretrive_latest_version = new SoapObject(NAMESPACE, METHOD_NAME_LATEST_VERSION);
        envelope_latest_version = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope_latest_version.implicitTypes = true;
        envelope_latest_version.dotNet = true;
        androidHttpTransport = new HttpTransportSE(URL);

        Boolean error = false;
        String errorMessage = "";

        error = false;
        DBA_TABLE_VERSION db = null;
        Cursor c = null;

        try {

            SoapObject table = null;
            SoapObject tableRow = null;
            SoapObject responseBody = null;


            envelope_latest_version.setOutputSoapObject(requestretrive_latest_version);
            envelope_latest_version.bodyOut = requestretrive_latest_version;
            androidHttpTransport.call(SOAP_ACTION_LATEST_VERSION, envelope_latest_version);

            responseBody = (SoapObject) envelope_latest_version.getResponse();
            responseBody = (SoapObject) responseBody.getProperty(1);

            if (responseBody.getPropertyCount() == 0) {
                error = true;
            }
            table = (SoapObject) responseBody.getProperty(0);


            db = new DBA_TABLE_VERSION(getBaseContext());
            db.open();

            setForceSync();
            int wsVersion = 0;
            int localVersion = 0;
            String wsFunctionName = "";
            String localFunctionName = "";
            String isSuccessSync = "";
            boolean bFound = false;

            int iTotalDataFromWebService = table.getPropertyCount();
            for (int i = 0; i < iTotalDataFromWebService; i++) {
                bFound = false;

                tableRow = (SoapObject) table.getProperty(i);
                Log.d("version", tableRow.getPropertySafelyAsString("Version"));
                Log.d("function_name", tableRow.getPropertySafelyAsString("Function_Name"));

                wsVersion = Integer.parseInt(tableRow.getPropertySafelyAsString("Version"));
                wsFunctionName = tableRow.getPropertySafelyAsString("Function_Name");

                c = db.getAll();

                if (c.moveToFirst()) {
                    do {
                        localVersion = c.getInt(0);
                        localFunctionName = c.getString(1);
                        isSuccessSync = c.getString(2);

                        if (wsFunctionName.equalsIgnoreCase(localFunctionName) && wsVersion > localVersion) {
                            db.update(wsVersion, wsFunctionName);
                            bFound = true;

                            SetNeedSyncFlag(wsFunctionName);

                        } else if (wsFunctionName.equalsIgnoreCase(localFunctionName) && wsVersion == localVersion
                                && TextUtils.isEmpty(isSuccessSync)) {
                            bFound = true;
                            SetNeedSyncFlag(wsFunctionName);
                        } else if (wsFunctionName.equalsIgnoreCase(localFunctionName) && wsVersion == localVersion
                                && isSuccessSync.equals("YES")) {
                            bFound = true;
                        } else if (wsFunctionName.equalsIgnoreCase(localFunctionName) && wsVersion == localVersion
                                && isSuccessSync.equals("NO")) {
                            bFound = true;

                            SetNeedSyncFlag(wsFunctionName);
                        }

	            		/*
	            		if (wsFunctionName.equals("PremiTSDomestik"))
	            			SetNeedSyncFlag(wsFunctionName);
	            		*/
                    } while (c.moveToNext());
                }
                if (!bFound) {
                    db.insert(wsVersion, wsFunctionName);
                    SetNeedSyncFlag(wsFunctionName);
                }
            }

        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            error = true;
            errorMessage = e.getMessage();
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            error = true;
            errorMessage = "SocketTimeoutException " + e.getMessage();
        } catch (ConnectTimeoutException e) {
            e.printStackTrace();
            error = true;
            errorMessage = "ConnectTimeoutException " + e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            error = true;
            errorMessage = e.getMessage();
        } finally {

            if (c != null)
                c.close();

            if (db != null)
                db.close();
        }

        try {
            if (error) {
                Toast.makeText(getBaseContext(), "Cannot retrive table version from server due to : " + errorMessage, Toast.LENGTH_LONG).show();
                Log.d("Sync. Table Version", "Fail");

                errorSync = true;

            } else {

                if (bNeedSyncCarBrand) {
                    requestretrive_car_brand = new SoapObject(NAMESPACE, METHOD_NAME_CAR_BRAND);
                    wsCarBrand.execute("");
                } else
                    bFinishCarBrand = true;


                //---------------------------


                if (bNeedSyncCarType) {
                    requestretrive_car_type = new SoapObject(NAMESPACE, METHOD_NAME_CAR_TYPE);
                    wsCarType.execute("");
                } else
                    bFinishCarType = true;


                //---------------------------
                if (bNeedSyncCity) {
                    requestretrive_city = new SoapObject(NAMESPACE, METHOD_NAME_CITY);
                    wsCity.execute("");
                } else
                    bFinishCity = true;

                //---------------------------
                if (bNeedSyncCountry) {
                    requestretrive_country = new SoapObject(NAMESPACE, METHOD_NAME_COUNTRY);
                    requestretrive_country_non_asia = new SoapObject(NAMESPACE, METHOD_NAME_COUNTRY);
                    wsCountry.execute("");
                } else
                    bFinishCountry = true;

                //---------------------------
                if (bNeedSyncToko) {
                    requestretrive_toko = new SoapObject(NAMESPACE, METHOD_NAME_JENIS_TOKO);
                    wsToko.execute("");
                } else
                    bFinishToko = true;

                //---------------------------
                if (bNeedSyncJob) {
                    requestretrive_job = new SoapObject(NAMESPACE, METHOD_NAME_JOB);
                    wsJob.execute("");
                } else
                    bFinishJob = true;

                //---------------------------
                if (bNeedSyncUsaha) {
                    requestretrive_usaha = new SoapObject(NAMESPACE, METHOD_NAME_USAHA);
                    wsUsaha.execute("");
                } else
                    bFinishUsaha = true;

                //---------------------------
                if (bNeedSyncASRIRate) {
                    requestretrive_asri_rate = new SoapObject(NAMESPACE, METHOD_NAME_ASRI_RATE);
                    wsASRIRate.execute("");
                } else
                    bFinishASRIRate = true;

                //---------------------------
                if (bNeedSyncOtomateRate) {
                    requestretrive_otomate_rate = new SoapObject(NAMESPACE, METHOD_NAME_OTOMATE_RATE);
                    wsOtomateRate.execute("");
                } else
                    bFinishOtomateRate = true;


                //---------------------------
                if (bNeedSyncACAMobilRate) {
                    requestretrive_acamobil_rate = new SoapObject(NAMESPACE, METHOD_NAME_ACAMOBIL_RATE);
                    wsACAMobilRate.execute("");
                } else
                    bFinishACAMobilRate = true;

                //---------------------------
                if (bNeedSyncMedisafeRate) {
                    requestretrive_medisafe_rate = new SoapObject(NAMESPACE, METHOD_NAME_MEDISAFE_RATE);
                    wsMedisafeRate.execute("");
                } else
                    bFinishMedisafeRate = true;

                //---------------------------
                if (bNeedSyncExecutiveRate) {
                    requestretrive_executive_rate = new SoapObject(NAMESPACE, METHOD_NAME_EXECUTIVE_RATE);
                    wsExecutiveSafeRate.execute("");
                } else
                    bFinishExecutiveRate = true;

                //---------------------------
                if (bNeedSyncPAAmanahRate) {
                    requestretrive_pa_amanah_rate = new SoapObject(NAMESPACE, METHOD_NAME_PA_AMANAH_RATE);
                    wsPAAmanahRate.execute("");
                } else
                    bFinishPAAmanahRate = true;

                //---------------------------
                if (bNeedSyncTSDomRate) {
                    requestretrive_ts_dom_rate = new SoapObject(NAMESPACE, METHOD_NAME_TS_DOM_RATE);
                    wsTSDomRate.execute("");
                } else
                    bFinishTSDomRate = true;


                //---------------------------
                if (bNeedSyncTSIntRate) {
                    requestretrive_ts_int_rate = new SoapObject(NAMESPACE, METHOD_NAME_TS_INT_RATE);
                    wsTSIntRate.execute("");
                } else
                    bFinishTSIntRate = true;


                //---------------------------
                if (bNeedSyncTokoRate) {
                    requestretrive_toko_rate = new SoapObject(NAMESPACE, METHOD_NAME_TOKO_RATE);
                    wsTokoRate.execute("");
                } else
                    bFinishTokoRate = true;

                //---------------------------
                if (bNeedSyncWellWomanRate) {
                    requestretrive_wellwoman_rate = new SoapObject(NAMESPACE, METHOD_NAME_WELLWOMAN_RATE);
                    wsWellWomanRate.execute("");
                } else
                    bFinishWellWomanRate = true;

                //*****************************
                if (bNeedSyncDNOBusType) {
                    requestretrive_bus_type = new SoapObject(NAMESPACE, METHOD_NAME_BUS_TYPE);
                    wsBusType.execute("");
                } else
                    bFinishBusType = true;

                //*****************************
                if (bNeedSyncVesselType) {
                    requestretrive_vessel_type = new SoapObject(NAMESPACE, METHOD_NAME_VESSEL_TYPE);
                    wsVesselType.execute("");
                } else
                    bFinishVesselType = true;

                //*****************************
                if (bNeedSyncVesselDetail) {
                    requestretrive_vessel_detail = new SoapObject(NAMESPACE, METHOD_NAME_VESSEL_DETAIL);
                    wsVesselDetail.execute("");
                } else
                    bFinishVesselDetail = true;

                if (bNeedSyncOtomateTPL) {
                    requestretrive_Otomate_TPL = new SoapObject(NAMESPACE, METHOD_NAME_OTOMATE_TPL);
                    wsOtomateTPL.execute("");
                } else
                    bFinishOtomateTPL = true;

                if (bNeedSyncOtomatePA) {
                    requestretrive_Otomate_PA = new SoapObject(NAMESPACE, METHOD_NAME_OTOMATE_PA);
                    wsOtomatePA.execute("");
                } else
                    bFinishOtomatePA = true;


                if (bNeedSyncOtomateAOG) {
                    requestretrive_Otomate_AOG = new SoapObject(NAMESPACE, METHOD_NAME_OTOMATE_AOG);
                    wsOtomateAOG.execute("");
                } else
                    bFinishOtomateAOG = true;


                if (bNeedSyncKonveRate) {
                    requestretrive_Konve_Rate = new SoapObject(NAMESPACE, METHOD_NAME_KONVE_RATE);
                    wsKonveRate.execute("");
                } else
                    bFinishKonveRate = true;

                if (bNeedSyncCarBrandTruk) {
                    requestretrive_car_brand_truk = new SoapObject(NAMESPACE, METHOD_NAME_CAR_BRAND_TRUK);
                    wsCarBrandTruk.execute("");
                } else
                    bFinishCarBrandTruk = true;


                //---------------------------
                if (bNeedSyncProductSetting) {
                    requestretrive_product_setting = new SoapObject(NAMESPACE, METHOD_NAME_PRODUCT_SETTING);
                    wsProductSetting.execute("");
                } else
                    bFinishProductSetting = true;

                if (bNeedSyncLoadOtomateSetting) {
                    fetchOtomateSetting();
                } else
                    bFinishLoadOtomateSetting = true;

                if (bNeedSyncLoadPaketOtomate) {
                    fetchPaketOtomate();
                } else
                    bFinishLoadPaketOtomatee = true;


                if (bNeedSyncLoadPerluasan) {
                    fetchPerluasan();
                } else
                    bFinishLoadPerluasan = true;


                if (bNeedSyncLoadStandardField) {
                    fetchStandardField();
                } else
                    bFinishLoadStandardField = true;

                if (bNeedSyncLabbaikMaster) {
                    fetchLabbaikMaster();
                } else
                    bFinishLoadLabbaikMaster = true;

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void fetchLabbaikMaster(){
        Observable.zip(
                TravelService.createMasterService(null).SubProduct().subscribeOn(Schedulers.newThread()),
                TravelService.createMasterService(null).SubProductPlan().subscribeOn(Schedulers.newThread()),
                TravelService.createMasterService(null).SubProductPlanAdd().subscribeOn(Schedulers.newThread()),
                TravelService.createMasterService(null).SubProductPlanBasic().subscribeOn(Schedulers.newThread()),
                TravelService.createMasterService(null).SubProductPlanBDA().subscribeOn(Schedulers.newThread()),
                new Func5<List<SubProduct>, List<SubProductPlan>, List<SubProductPlanAdd>, List<SubProductPlanBasic>, List<SubProductPlanBDA>, Object>() {
                    @Override
                    public Object call(List<SubProduct> subProducts,
                                       List<SubProductPlan> subProductPlen, List<SubProductPlanAdd> subProductPlanAdds,
                                       List<SubProductPlanBasic> subProductPlanBasics, List<SubProductPlanBDA> subProductPlanBDAs) {
                        insertSubProcuct(subProducts);
                        insertSubProductPlan(subProductPlen);
                        insertSubProductPlanAdd(subProductPlanAdds);
                        insertSubProductPlanBasic(subProductPlanBasics);
                        insertSubProductPlanBDA(subProductPlanBDAs);

                        bFinishLoadLabbaikMaster = true;
                        onFinishFetch(!bFinishLoadLabbaikMaster, "LabbaikMaster");

                        return null;
                    }
                }
        ).subscribe(
                new Observer<Object>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        bFinishLoadLabbaikMaster = false;
                        onFinishFetch(!bFinishLoadLabbaikMaster, "LabbaikMaster");
                    }

                    @Override
                    public void onNext(Object o) {

                    }
                }

        );
    }


    private void insertSubProductPlanBDA(List<SubProductPlanBDA> subProductPlanBDAs) {
        Delete.table(SubProductPlanBDA.class);

        for (SubProductPlanBDA spp : subProductPlanBDAs) {
            spp.save();
        }
    }


    private void insertSubProductPlanBasic(List<SubProductPlanBasic> subProductPlanBasics) {
        Delete.table(SubProductPlanBasic.class);

        for (SubProductPlanBasic spp : subProductPlanBasics) {
            spp.save();
        }
    }

    private void insertSubProductPlanAdd(List<SubProductPlanAdd> subProductPlanAdds) {
        Delete.table(SubProductPlanAdd.class);

        for (SubProductPlanAdd spp : subProductPlanAdds) {
            spp.save();
        }

    }

    private void insertSubProductPlan(List<SubProductPlan> subProductPlen) {
        Delete.table(SubProductPlan.class);

        for (SubProductPlan spp : subProductPlen) {
            spp.save();
        }
    }

    private void insertSubProcuct(List<SubProduct> subProducts) {
        Delete.table(SubProduct.class);

        for (SubProduct sp : subProducts) {
            sp.save();
        }
    }

    private void fetchStandardField() {

        WebServices ws = new WebServices(
                SplashActivity.this,
                "LoadStandardField",
                new HashMap<String, String>(),
                SplashActivity.this.getResources().getStringArray(R.array.LoadStandardField_post),
                SplashActivity.this.getResources().getStringArray(R.array.LoadStandardField_get)
        ) {
            @Override
            protected void onSuccess(@NonNull ArrayList<HashMap<String, String>> arrList) {
                try {
                    if (arrList != null) {
                        Delete.table(StandardField.class);
                        StandardField standardField;
                        for (HashMap<String, String> map : arrList) {
                            standardField = new StandardField();
                            standardField.FieldCode = map.get("FieldCode");
                            standardField.FieldCodeDt = map.get("FieldCodeDt");
                            standardField.FieldNameDt = map.get("FieldNameDt");
                            standardField.Value = map.get("Value");
                            standardField.Description = map.get("Description");
                            standardField.IsActive = map.get("IsActive");
                            standardField.save();
                        }
                    } else {
                        Toast.makeText(SplashActivity.this, "", Toast.LENGTH_SHORT).show();
                    }
                    bFinishLoadStandardField = true;
                    onFinishFetch(!bFinishLoadStandardField, "StandardField");
                } catch (Exception e) {
                    e.printStackTrace();
                    bFinishLoadStandardField = false;
                    onFinishFetch(!bFinishLoadStandardField, "StandardField");
                }
            }

            @Override
            protected void onFailed(String message) {
                try {
                    Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
                    bFinishLoadStandardField = false;
                    onFinishFetch(!bFinishLoadStandardField, "StandardField");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onCancel() {

            }
        };
        ws.execute();
    }

    private void fetchPerluasan() {

        WebServices ws = new WebServices(
                SplashActivity.this,
                "LoadPerluasan",
                new HashMap<String, String>(),
                SplashActivity.this.getResources().getStringArray(R.array.LoadPerluasan_post),
                SplashActivity.this.getResources().getStringArray(R.array.LoadPerluasan_get)
        ) {
            @Override
            protected void onSuccess(@NonNull ArrayList<HashMap<String, String>> arrList) {
                try {
                    if (arrList != null) {
                        Delete.table(PerluasanPremi.class);
                        PerluasanPremi perluasanPremi;
                        for (HashMap<String, String> map : arrList) {
                            perluasanPremi = new PerluasanPremi();
                            perluasanPremi.Tipe = map.get("Tipe");
                            perluasanPremi.Amount = Double.parseDouble(map.get("Amount"));
                            perluasanPremi.Amount_Text = map.get("Amount_Text");
                            perluasanPremi.Premi = Double.parseDouble(map.get("Premi"));
                            perluasanPremi.Kode_Produk = map.get("Kode_Produk");
                            perluasanPremi.save();
                        }
                    } else {
                        Toast.makeText(SplashActivity.this, "", Toast.LENGTH_SHORT).show();
                    }
                    bFinishLoadPerluasan = true;
                    onFinishFetch(!bFinishLoadPerluasan, "MsPremiPerluasan");
                } catch (Exception e) {
                    e.printStackTrace();
                    bFinishLoadPerluasan = false;
                    onFinishFetch(!bFinishLoadPerluasan, "MsPremiPerluasan");
                }
            }

            @Override
            protected void onFailed(String message) {
                try {
                    Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
                    bFinishLoadPerluasan = false;
                    onFinishFetch(!bFinishLoadPerluasan, "MsPremiPerluasan");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onCancel() {

            }
        };
        ws.execute();
    }

    private void fetchPaketOtomate() {

        WebServices ws = new WebServices(
                SplashActivity.this,
                "LoadPaketOtomate",
                new HashMap<String, String>(),
                SplashActivity.this.getResources().getStringArray(R.array.LoadPaketOtomate_post),
                SplashActivity.this.getResources().getStringArray(R.array.LoadPaketOtomate_get)
        ) {
            @Override
            protected void onSuccess(@NonNull ArrayList<HashMap<String, String>> arrList) {
                try {
                    Delete.table(PaketOtomate.class);
                    PaketOtomate paketOtomate;
                    if (arrList != null) {
                        for (HashMap<String, String> map : arrList) {
                            paketOtomate = new PaketOtomate();
                            paketOtomate.KodeProduct = map.get("KodeProduct");
                            paketOtomate.Flood = map.get("Flood");
                            paketOtomate.Eq = map.get("Eq");
                            paketOtomate.save();
                        }
                    } else {
                        Toast.makeText(SplashActivity.this, "", Toast.LENGTH_SHORT).show();
                    }

                    bFinishLoadPaketOtomatee = true;
                    onFinishFetch(!bFinishLoadPaketOtomatee, "MsPaketOtomate");
                } catch (Exception e) {
                    e.printStackTrace();
                    bFinishLoadPaketOtomatee = false;
                    onFinishFetch(!bFinishLoadPaketOtomatee, "MsPaketOtomate");
                }
            }

            @Override
            protected void onFailed(String message) {
                try {
                    Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
                    bFinishLoadPaketOtomatee = false;
                    onFinishFetch(!bFinishLoadPaketOtomatee, "MsPaketOtomate");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onCancel() {

            }
        };
        ws.execute();
    }

    private void fetchOtomateSetting() {
        WebServices ws = new WebServices(
                SplashActivity.this,
                "LoadOtomateSetting",
                new HashMap<String, String>(),
                getResources().getStringArray(R.array.LoadOtomateSetting_post),
                getResources().getStringArray(R.array.LoadOtomateSetting_get)
        ) {
            @Override
            protected void onSuccess(@NonNull ArrayList<HashMap<String, String>> arrList) {
                try {
                    if (arrList != null) {
                        Delete.table(SettingOtomate.class);
                        SettingOtomate settingOtomate;
                        for (HashMap<String, String> map : arrList) {
                            settingOtomate = new SettingOtomate();
                            settingOtomate.KodeProduct = map.get("KodeProduct");
                            settingOtomate.FloodDefault = map.get("FloodDefault");
                            settingOtomate.EqDefault = map.get("EqDefault");
                            settingOtomate.IsPaket = map.get("IsPaket");
                            settingOtomate.SRCCDefault = map.get("SRCCDefault");
                            settingOtomate.TSDefault = map.get("TSDefault");
                            settingOtomate.IsChangeable = map.get("IsChangeable");
                            settingOtomate.BengkelDefault = map.get("BengkelDefault");
                            settingOtomate.IsChangeableBengkel = map.get("IsChangeableBengkel");
                            settingOtomate.LimitTPL = Double.parseDouble(map.get("LimitTPL"));
                            settingOtomate.LimitPA = Double.parseDouble(map.get("LimitPA"));
                            settingOtomate.save();

                        }

                    } else {
                        Toast.makeText(SplashActivity.this, "", Toast.LENGTH_SHORT).show();
                    }
                    bFinishLoadOtomateSetting = true;
                    onFinishFetch(!bFinishLoadOtomateSetting, "MsSettingOtomate");
                } catch (Exception e) {
                    e.printStackTrace();
                    bFinishLoadOtomateSetting = false;
                    onFinishFetch(!bFinishLoadOtomateSetting, "MsSettingOtomate");

                }
            }

            @Override
            protected void onFailed(String message) {
                try {
                    Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
                    bFinishLoadOtomateSetting = false;
                    onFinishFetch(!bFinishLoadOtomateSetting, "MsSettingOtomate");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onCancel() {

            }
        };
        ws.execute();
    }

    private void onFinishFetch(boolean error, String functionName) {
        try {
            if (error) {
                DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                dbVersion.open();
                dbVersion.setNeedSync(functionName);
                dbVersion.close();
                Log.d("Sync. " + functionName, "Fail");
                errorSync = true;
            } else {
                DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                dbVersion.open();
                dbVersion.setSuccessSync(functionName);
                dbVersion.close();
                Log.d("Sync. " + functionName, "Success");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.splash, menu);
        return true;
    }


    public void SetNeedSyncFlag(String s) {
        if (s.equals("CarBrand"))
            bNeedSyncCarBrand = true;
        else if (s.equals("CarType"))
            bNeedSyncCarType = true;
        else if (s.equals("ListCity"))
            bNeedSyncCity = true;
        else if (s.equals("ListCountry"))
            bNeedSyncCountry = true;
        else if (s.equals("ListJenisToko"))
            bNeedSyncToko = true;
        else if (s.equals("ListJob"))
            bNeedSyncJob = true;
        else if (s.equals("ListUsaha"))
            bNeedSyncUsaha = true;
        else if (s.equals("RateASRI"))
            bNeedSyncASRIRate = true;
        else if (s.equals("RateOtomate"))
            bNeedSyncOtomateRate = true;
        else if (s.equals("PremiMedisafe"))
            bNeedSyncMedisafeRate = true;
        else if (s.equals("PremiExecutiveSafe"))
            bNeedSyncExecutiveRate = true;
        else if (s.equals("PremiPA"))
            bNeedSyncPAAmanahRate = true;
        else if (s.equals("PremiTSDomestik"))
            bNeedSyncTSDomRate = true;
        else if (s.equals("PremiTSInt"))
            bNeedSyncTSIntRate = true;
        else if (s.equals("RateToko"))
            bNeedSyncTokoRate = true;
        else if (s.equals("PremiWellWoman"))
            bNeedSyncWellWomanRate = true;
        else if (s.equals("JenisUsahaLiability"))
            bNeedSyncDNOBusType = true;
        else if (s.equals("VesselType"))
            bNeedSyncVesselType = true;
        else if (s.equals("VesselName"))
            bNeedSyncVesselDetail = true;

        else if (s.equals("PremiTPLOtomate"))
            bNeedSyncOtomateTPL = true;
        else if (s.equals("PremiPAOtomate"))
            bNeedSyncOtomatePA = true;
        else if (s.equals("PremiAOGOtomate"))
            bNeedSyncOtomateAOG = true;
        else if (s.equals("PremiRateKonve"))
            bNeedSyncKonveRate = true;
        else if (s.equals("CarBrandTruk"))
            bNeedSyncCarBrandTruk = true;
        else if (s.equals("ProductSetting"))
            bNeedSyncProductSetting = true;


        else if (s.equals("MsSettingOtomate"))
            bNeedSyncLoadOtomateSetting = true;
        else if (s.equals("MsPaketOtomate"))
            bNeedSyncLoadPaketOtomate = true;
        else if (s.equals("MsPremiPerluasan"))
            bNeedSyncLoadPerluasan = true;
        else if (s.equals("StandardField"))
            bNeedSyncLoadStandardField = true;
        else if (s.equalsIgnoreCase("LabbaikMaster"))
            bNeedSyncLabbaikMaster = true;
    }

    private class CarBrandWS extends AsyncTask<String, Void, Void> {

        private Boolean error = false;
        private String errorMessage = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            envelope_car_brand = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope_car_brand.implicitTypes = true;
            envelope_car_brand.dotNet = true;
            androidHttpTransport = new HttpTransportSE(URL);
        }

        @Override
        protected Void doInBackground(String... params) {

            error = false;
            DBA_MASTER_CAR_BRAND db = null;
            DBA_MASTER_CAR_BRAND_SYARIAH dbSyariah = null;

            try {

                SoapObject table = null;                        // Contains table of dataset that returned through SoapObject
                SoapObject tableRow = null;                     // Contains row of table
                SoapObject responseBody = null;                    // Contains XML content of dataset

                PropertyInfo param;

                param = new PropertyInfo();
                param.setName("conn");
                param.setValue("1");
                param.setType(String.class);

                requestretrive_car_brand.addProperty(param);

                envelope_car_brand.setOutputSoapObject(requestretrive_car_brand);
                envelope_car_brand.bodyOut = requestretrive_car_brand;
                androidHttpTransport.call(SOAP_ACTION_CAR_BRAND, envelope_car_brand);

                responseBody = (SoapObject) envelope_car_brand.getResponse();
                responseBody = (SoapObject) responseBody.getProperty(1);

                if (responseBody.getPropertyCount() == 0) {
                    error = true;
                }
                table = (SoapObject) responseBody.getProperty(0);

                db = new DBA_MASTER_CAR_BRAND(getBaseContext());
                db.open();
                db.deleteAll();

                int iTotalDataFromWebService = table.getPropertyCount();
                for (int i = 0; i < iTotalDataFromWebService; i++) {
                    tableRow = (SoapObject) table.getProperty(i);
                    Log.d("CARBRD", tableRow.getPropertySafelyAsString("CABRCDBRD").toString());
                    db.insert(tableRow.getPropertySafelyAsString("CABRCDBRD").toString(), tableRow.getPropertySafelyAsString("CABRDESC1").toString());
                }


                /**GET CAR BRAND SYARIAH**/

                envelope_car_brand = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope_car_brand.implicitTypes = true;
                envelope_car_brand.dotNet = true;
                androidHttpTransport = new HttpTransportSE(URL);


                param = new PropertyInfo();
                param.setName("conn");
                param.setValue("0");
                param.setType(String.class);

                requestretrive_car_brand = new SoapObject(NAMESPACE, METHOD_NAME_CAR_BRAND);
                requestretrive_car_brand.addProperty(param);

                envelope_car_brand.setOutputSoapObject(requestretrive_car_brand);
                envelope_car_brand.bodyOut = requestretrive_car_brand;
                androidHttpTransport.call(SOAP_ACTION_CAR_BRAND, envelope_car_brand);

                responseBody = (SoapObject) envelope_car_brand.getResponse();
                responseBody = (SoapObject) responseBody.getProperty(1);

                if (responseBody.getPropertyCount() == 0) {
                    error = true;
                }
                table = (SoapObject) responseBody.getProperty(0);

                dbSyariah = new DBA_MASTER_CAR_BRAND_SYARIAH(getBaseContext());
                dbSyariah.open();
                dbSyariah.deleteAll();

                iTotalDataFromWebService = table.getPropertyCount();
                for (int i = 0; i < iTotalDataFromWebService; i++) {
                    tableRow = (SoapObject) table.getProperty(i);

                    Log.d("CARBRD", tableRow.getPropertySafelyAsString("CABRCDBRD").toString());

                    dbSyariah.insert(
                            tableRow.getPropertySafelyAsString("CABRCDBRD").toString(),
                            tableRow.getPropertySafelyAsString("CABRDESC1").toString());
                }


            } catch (ArrayIndexOutOfBoundsException ex) {
                ex.printStackTrace();
                error = true;
            } catch (SocketTimeoutException ex) {
                ex.printStackTrace();
                error = true;
            } catch (Exception e) {
                e.printStackTrace();
                error = true;
            } finally {
                if (db != null)
                    db.close();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);
            DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);

            try {
                if (error) {
//					Toast.makeText(getBaseContext(), "Retrive car brand failed", Toast.LENGTH_SHORT).show();
                    bFinishCarBrand = false;

                    dbVersion.open();
                    dbVersion.setNeedSync("CarBrand");
                    dbVersion.close();

                    Log.d("Sync. Car Brand", "Fail");
//					SplashActivity.this.finish();
                    errorSync = true;
                } else {
                    //pb.setProgress(pb.getProgress() + 10);
                    dbVersion.open();
                    dbVersion.setSuccessSync("CarBrand");
                    dbVersion.close();

                    bFinishCarBrand = true;
                    Log.d("Sync. Car Brand", "Success");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private class CarTypeWS extends AsyncTask<String, Void, Void> {

        private Boolean error = false;
        private String errorMessage = "";

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            envelope_car_type = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope_car_type.implicitTypes = true;
            envelope_car_type.dotNet = true;    //used only if we use the webservice from a dot net file (asmx)

            androidHttpTransport = new HttpTransportSE(URL);
        }

        @Override
        protected Void doInBackground(String... params) {

            error = false;
            DBA_MASTER_CAR_TYPE db = null;
            DBA_MASTER_CAR_TYPE_SYARIAH dbSyariah = null;

            try {

                SoapObject table = null;                        // Contains table of dataset that returned through SoapObject
                SoapObject tableRow = null;                     // Contains row of table
                SoapObject responseBody = null;                    // Contains XML content of dataset

                PropertyInfo param;

                param = new PropertyInfo();
                param.setName("conn");
                param.setValue("1");
                param.setType(String.class);

                requestretrive_car_type.addProperty(param);

                envelope_car_type.setOutputSoapObject(requestretrive_car_type);
                envelope_car_type.bodyOut = requestretrive_car_type;
                androidHttpTransport.call(SOAP_ACTION_CAR_TYPE, envelope_car_type);

                // This step: get file XML
                responseBody = (SoapObject) envelope_car_type.getResponse();
                // remove information XML,only retrieved results that returned
                responseBody = (SoapObject) responseBody.getProperty(1);
                // get information XMl of tables that is returned
                if (responseBody.getPropertyCount() == 0) {
                    error = true;
                }
                table = (SoapObject) responseBody.getProperty(0);
                //Get information each row in table,0 is first row

                db = new DBA_MASTER_CAR_TYPE(getBaseContext());
                db.open();
                db.deleteAll();

                int iTotalDataFromWebService = table.getPropertyCount();

                for (int i = 0; i < iTotalDataFromWebService; i++) {
                    tableRow = (SoapObject) table.getProperty(i);
                    //Log.d("CARTYP", tableRow.getPropertySafelyAsString("CABTCDTYP").toString());
                    db.insert(tableRow.getPropertySafelyAsString("CABTCDTYP").toString(), tableRow.getPropertySafelyAsString("CABTDESC").toString(), tableRow.getPropertySafelyAsString("CABTCDBRD").toString());
                }


                /**GET CAR TYPE SYARIAH**/


                envelope_car_type = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope_car_type.implicitTypes = true;
                envelope_car_type.dotNet = true;    //used only if we use the webservice from a dot net file (asmx)

                androidHttpTransport = new HttpTransportSE(URL);


                param = new PropertyInfo();
                param.setName("conn");
                param.setValue("0");
                param.setType(String.class);

                requestretrive_car_type = new SoapObject(NAMESPACE, METHOD_NAME_CAR_TYPE);
                requestretrive_car_type.addProperty(param);

                envelope_car_type.setOutputSoapObject(requestretrive_car_type);
                envelope_car_type.bodyOut = requestretrive_car_type;
                androidHttpTransport.call(SOAP_ACTION_CAR_TYPE, envelope_car_type);

                // This step: get file XML
                responseBody = (SoapObject) envelope_car_type.getResponse();
                // remove information XML,only retrieved results that returned
                responseBody = (SoapObject) responseBody.getProperty(1);
                // get information XMl of tables that is returned
                if (responseBody.getPropertyCount() == 0) {
                    error = true;
                }
                table = (SoapObject) responseBody.getProperty(0);
                //Get information each row in table,0 is first row

                dbSyariah = new DBA_MASTER_CAR_TYPE_SYARIAH(getBaseContext());
                dbSyariah.open();
                dbSyariah.deleteAll();

                iTotalDataFromWebService = table.getPropertyCount();

                for (int i = 0; i < iTotalDataFromWebService; i++) {
                    tableRow = (SoapObject) table.getProperty(i);
                    //Log.d("CARTYP", tableRow.getPropertySafelyAsString("CABTCDTYP").toString());

                    if (tableRow.getPropertySafelyAsString("CABTDESC").toString().equals("BRV")) {
                        String mobil = tableRow.getPropertySafelyAsString("CABTDESC").toString();
                        mobil = mobil + "";
                    }

                    dbSyariah.insert(
                            tableRow.getPropertySafelyAsString("CABTCDTYP").toString(),
                            tableRow.getPropertySafelyAsString("CABTDESC").toString(),
                            tableRow.getPropertySafelyAsString("CABTCDBRD").toString());
                }


            } catch (ArrayIndexOutOfBoundsException ex) {
                ex.printStackTrace();
                error = true;
            } catch (SocketTimeoutException ex) {
                ex.printStackTrace();
                error = true;
            } catch (Exception e) {
                e.printStackTrace();
                error = true;
            } finally {
                if (db != null)
                    db.close();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            try {
                if (error) {
//					Toast.makeText(getBaseContext(), "Retrive car type failed", Toast.LENGTH_SHORT).show();

                    bFinishCarType = true;
                    DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                    dbVersion.open();
                    dbVersion.setNeedSync("CarType");
                    dbVersion.close();
                    Log.d("Sync. Car Type", "Fail");
                    errorSync = true;
                } else {
                    //pb.setProgress(pb.getProgress() + 10);

                    bFinishCarType = true;
                    DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                    dbVersion.open();
                    dbVersion.setSuccessSync("CarType");
                    dbVersion.close();
                    Log.d("Sync. Car Type", "Success");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    private class CityWS extends AsyncTask<String, Void, Void> {

        private Boolean error = false;
        private String errorMessage = "";

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            envelope_city = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope_city.implicitTypes = true;
            envelope_city.dotNet = true;    //used only if we use the webservice from a dot net file (asmx)
            androidHttpTransport = new HttpTransportSE(URL);
        }

        @Override
        protected Void doInBackground(String... params) {

            error = false;
            DBA_MASTER_CITY_PROVINCE db = null;

            try {

                SoapObject table = null;                        // Contains table of dataset that returned through SoapObject
                SoapObject tableRow = null;                     // Contains row of table
                SoapObject responseBody = null;                    // Contains XML content of dataset

                envelope_city.setOutputSoapObject(requestretrive_city);
                envelope_city.bodyOut = requestretrive_city;
                androidHttpTransport.call(SOAP_ACTION_CITY, envelope_city);

                // This step: get file XML
                responseBody = (SoapObject) envelope_city.getResponse();
                // remove information XML,only retrieved results that returned
                responseBody = (SoapObject) responseBody.getProperty(1);
                // get information XMl of tables that is returned
                if (responseBody.getPropertyCount() == 0) {
                    error = true;
                }
                table = (SoapObject) responseBody.getProperty(0);
                //Get information each row in table,0 is first row

                db = new DBA_MASTER_CITY_PROVINCE(getBaseContext());
                db.open();
                db.deleteAll();

                int iTotalDataFromWebService = table.getPropertyCount();
                for (int i = 0; i < iTotalDataFromWebService; i++) {

                    tableRow = (SoapObject) table.getProperty(i);
                    //Log.d("KOTA", tableRow.getPropertySafelyAsString("KOTA").toString());
                    db.insert(tableRow.getPropertySafelyAsString("NO").toString(),
                            tableRow.getPropertySafelyAsString("KOTA").toString(),
                            tableRow.getPropertySafelyAsString("PROPINSI").toString(),
                            tableRow.getPropertySafelyAsString("KABUPATEN").toString(),
//	            			tableRow.getPropertySafelyAsString("ZONE").toString()
                            ""
                    );

                }

            } catch (ArrayIndexOutOfBoundsException ex) {
                ex.printStackTrace();
                error = true;
            } catch (SocketTimeoutException ex) {
                ex.printStackTrace();
                error = true;
            } catch (Exception e) {
                e.printStackTrace();
                error = true;

            } finally {
                if (db != null)
                    db.close();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            try {
                if (error) {
                    //Toast.makeText(getBaseContext(), "Retrive city failed", Toast.LENGTH_SHORT).show();

                    bFinishCity = false;
                    DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                    dbVersion.open();
                    dbVersion.setNeedSync("ListCity");
                    dbVersion.close();
                    Log.d("Sync. City", "Fail");
                    errorSync = true;
                } else {
                    //pb.setProgress(pb.getProgress() + 10);

                    bFinishCity = true;
                    DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                    dbVersion.open();
                    dbVersion.setSuccessSync("ListCity");
                    dbVersion.close();
                    Log.d("Sync. City", "Success");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private class CountryWS extends AsyncTask<String, Void, Void> {

        private Boolean error = false;
        private String errorMessage = "";

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            envelope_country = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope_country.implicitTypes = true;
            envelope_country.dotNet = true;    //used only if we use the webservice from a dot net file (asmx)

            envelope_country_non_asia = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope_country_non_asia.implicitTypes = true;
            envelope_country_non_asia.dotNet = true;    //used only if we use the webservice from a dot net file (asmx)
            androidHttpTransport = new HttpTransportSE(URL);
        }

        @Override
        protected Void doInBackground(String... params) {

            error = false;
            DBA_MASTER_COUNTRY db = null;

            try {

                SoapObject table = null;                        // Contains table of dataset that returned through SoapObject
                SoapObject tableRow = null;                     // Contains row of table
                SoapObject responseBody = null;                    // Contains XML content of dataset

		        /* get country asia */
                PropertyInfo param = new PropertyInfo();
                param.setName("CountryCode");
                param.setValue("1");
                param.setType(String.class);
                requestretrive_country.addProperty(param);

                envelope_country.setOutputSoapObject(requestretrive_country);
                envelope_country.bodyOut = requestretrive_country;
                androidHttpTransport.call(SOAP_ACTION_COUNTRY, envelope_country);

                // This step: get file XML
                responseBody = (SoapObject) envelope_country.getResponse();
                // remove information XML,only retrieved results that returned
                responseBody = (SoapObject) responseBody.getProperty(1);
                // get information XMl of tables that is returned

                if (responseBody.getPropertyCount() == 0) {
                    error = true;
                }
                table = (SoapObject) responseBody.getProperty(0);
                //Get information each row in table,0 is first row

                db = new DBA_MASTER_COUNTRY(getBaseContext());
                db.open();
                db.deleteAll();

                int iTotalDataFromWebService = table.getPropertyCount();

                for (int i = 0; i < iTotalDataFromWebService; i++) {

                    tableRow = (SoapObject) table.getProperty(i);
                    //Log.d("TPCVDESC1", tableRow.getPropertySafelyAsString("TPCVDESC1").toString());
                    db.insert(tableRow.getPropertySafelyAsString("TPCVCODE1").toString(), tableRow.getPropertySafelyAsString("TPCVDESC1").toString(), "1");
                }


	            /* get country non-asia */
                param = new PropertyInfo();
                param.setName("CountryCode");
                param.setValue("2");
                param.setType(String.class);
                requestretrive_country_non_asia.addProperty(param);

                envelope_country_non_asia.setOutputSoapObject(requestretrive_country_non_asia);
                envelope_country_non_asia.bodyOut = requestretrive_country_non_asia;
                androidHttpTransport.call(SOAP_ACTION_COUNTRY, envelope_country_non_asia);

                // This step: get file XML
                responseBody = (SoapObject) envelope_country_non_asia.getResponse();
                // remove information XML,only retrieved results that returned
                responseBody = (SoapObject) responseBody.getProperty(1);
                // get information XMl of tables that is returned


                if (responseBody.getPropertyCount() == 0) {
                    error = true;
                }
                table = (SoapObject) responseBody.getProperty(0);
                //Get information each row in table,0 is first row

                iTotalDataFromWebService = table.getPropertyCount();

                for (int i = 0; i < iTotalDataFromWebService; i++) {
                    tableRow = (SoapObject) table.getProperty(i);
                    //Log.d("TPCVDESC1", tableRow.getPropertySafelyAsString("TPCVDESC1").toString());
                    db.insert(tableRow.getPropertySafelyAsString("TPCVCODE1").toString(), tableRow.getPropertySafelyAsString("TPCVDESC1").toString(), "2");
                }

                db.insertSchengen();


            } catch (ArrayIndexOutOfBoundsException ex) {
                ex.printStackTrace();
                error = true;
            } catch (SocketTimeoutException ex) {
                ex.printStackTrace();
                error = true;
            } catch (Exception e) {
                e.printStackTrace();
                error = true;

            } finally {
                if (db != null)
                    db.close();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            try {
                if (error) {
//					Toast.makeText(getBaseContext(), "Retrive country failed", Toast.LENGTH_SHORT).show();

                    bFinishCountry = true;
                    DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                    dbVersion.open();
                    dbVersion.setNeedSync("ListCountry");
                    dbVersion.close();
                    Log.d("Sync. Country", "Fail");
                    errorSync = true;
                } else {
                    //pb.setProgress(pb.getProgress() + 10);

                    bFinishCountry = true;
                    DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                    dbVersion.open();
                    dbVersion.setSuccessSync("ListCountry");
                    dbVersion.close();
                    Log.d("Sync. Country", "Success");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private class TokoWS extends AsyncTask<String, Void, Void> {

        private Boolean error = false;
        private String errorMessage = "";

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            envelope_toko = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope_toko.implicitTypes = true;
            envelope_toko.dotNet = true;    //used only if we use the webservice from a dot net file (asmx)
            androidHttpTransport = new HttpTransportSE(URL);
        }

        @Override
        protected Void doInBackground(String... params) {

            error = false;
            DBA_MASTER_JENIS_TOKO db = null;

            try {

                SoapObject table = null;                        // Contains table of dataset that returned through SoapObject
                SoapObject tableRow = null;                     // Contains row of table
                SoapObject responseBody = null;                    // Contains XML content of dataset

                envelope_toko.setOutputSoapObject(requestretrive_toko);
                envelope_toko.bodyOut = requestretrive_toko;
                androidHttpTransport.call(SOAP_ACTION_JENIS_TOKO, envelope_toko);

                // This step: get file XML
                responseBody = (SoapObject) envelope_toko.getResponse();
                // remove information XML,only retrieved results that returned
                responseBody = (SoapObject) responseBody.getProperty(1);
                // get information XMl of tables that is returned

                if (responseBody.getPropertyCount() == 0) {
                    error = true;
                }
                table = (SoapObject) responseBody.getProperty(0);
                //Get information each row in table,0 is first row

                db = new DBA_MASTER_JENIS_TOKO(getBaseContext());
                db.open();
                db.deleteAll();

                int iTotalDataFromWebService = table.getPropertyCount();
                for (int i = 0; i < iTotalDataFromWebService; i++) {
                    tableRow = (SoapObject) table.getProperty(i);
                    //Log.d("OKUPASI_DESCRIPTION", tableRow.getPropertySafelyAsString("OKUPASI_DESCRIPTION").toString());
                    db.insert(tableRow.getPropertySafelyAsString("ID").toString(), tableRow.getPropertySafelyAsString("OKUPASI_DESCRIPTION").toString(), Double.parseDouble(tableRow.getPropertySafelyAsString("RATE").toString()));
                }


            } catch (ArrayIndexOutOfBoundsException ex) {
                ex.printStackTrace();
                error = true;
            } catch (SocketTimeoutException ex) {
                ex.printStackTrace();
                error = true;
            } catch (Exception e) {
                e.printStackTrace();
                error = true;
            } finally {
                if (db != null)
                    db.close();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            try {
                if (error) {
//					Toast.makeText(getBaseContext(), "Retrive toko failed", Toast.LENGTH_SHORT).show();

                    bFinishToko = false;
                    DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                    dbVersion.open();
                    dbVersion.setNeedSync("ListJenisToko");
                    dbVersion.close();
                    Log.d("Sync. Toko", "Fail");
                    errorSync = true;
                } else {
                    //pb.setProgress(pb.getProgress() + 10);

                    bFinishToko = true;
                    DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                    dbVersion.open();
                    dbVersion.setSuccessSync("ListJenisToko");
                    dbVersion.close();
                    Log.d("Sync. Toko", "Success");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private class JobWS extends AsyncTask<String, Void, Void> {

        private Boolean error = false;
        private String errorMessage = "";

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            envelope_job = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope_job.implicitTypes = true;
            envelope_job.dotNet = true;    //used only if we use the webservice from a dot net file (asmx)
            androidHttpTransport = new HttpTransportSE(URL);
        }

        @Override
        protected Void doInBackground(String... params) {

            error = false;
            DBA_MASTER_OCCUPATION db = null;

            try {

                SoapObject table = null;                        // Contains table of dataset that returned through SoapObject
                SoapObject tableRow = null;                     // Contains row of table
                SoapObject responseBody = null;                    // Contains XML content of dataset

                envelope_job.setOutputSoapObject(requestretrive_job);
                envelope_job.bodyOut = requestretrive_job;
                androidHttpTransport.call(SOAP_ACTION_JOB, envelope_job);

                // This step: get file XML
                responseBody = (SoapObject) envelope_job.getResponse();
                // remove information XML,only retrieved results that returned
                responseBody = (SoapObject) responseBody.getProperty(1);
                // get information XMl of tables that is returned

                if (responseBody.getPropertyCount() == 0) {
                    error = true;
                }
                table = (SoapObject) responseBody.getProperty(0);
                //Get information each row in table,0 is first row

                db = new DBA_MASTER_OCCUPATION(getBaseContext());
                db.open();
                db.deleteAll();

                int iTotalDataFromWebService = table.getPropertyCount();

                for (int i = 0; i < iTotalDataFromWebService; i++) {
                    tableRow = (SoapObject) table.getProperty(i);
                    //Log.d("JOB", tableRow.getPropertySafelyAsString("GECURFDS").toString());
                    db.insert(tableRow.getPropertySafelyAsString("GECURFYY").toString(), tableRow.getPropertySafelyAsString("GECURFDS").toString());
                }

            } catch (ArrayIndexOutOfBoundsException ex) {
                ex.printStackTrace();
                error = true;
            } catch (SocketTimeoutException ex) {
                ex.printStackTrace();
                error = true;
            } catch (Exception e) {
                e.printStackTrace();
                error = true;
            } finally {
                if (db != null)
                    db.close();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            try {
                if (error) {
//					Toast.makeText(getBaseContext(), "Retrive job failed", Toast.LENGTH_SHORT).show();

                    bFinishJob = false;
                    DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                    dbVersion.open();
                    dbVersion.setNeedSync("ListJob");
                    dbVersion.close();
                    Log.d("Sync. Job", "Fail");
                    errorSync = true;
                } else {
                    //pb.setProgress(pb.getProgress() + 10);

                    bFinishJob = true;
                    DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                    dbVersion.open();
                    dbVersion.setSuccessSync("ListJob");
                    dbVersion.close();
                    Log.d("Sync. Job", "Success");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private class UsahaWS extends AsyncTask<String, Void, Void> {

        private Boolean error = false;
        private String errorMessage = "";

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            envelope_usaha = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope_usaha.implicitTypes = true;
            envelope_usaha.dotNet = true;    //used only if we use the webservice from a dot net file (asmx)
            androidHttpTransport = new HttpTransportSE(URL);
        }

        @Override
        protected Void doInBackground(String... params) {

            error = false;
            DBA_MASTER_LOB db = null;

            try {

                SoapObject table = null;                        // Contains table of dataset that returned through SoapObject
                SoapObject tableRow = null;                     // Contains row of table
                SoapObject responseBody = null;                    // Contains XML content of dataset

                envelope_usaha.setOutputSoapObject(requestretrive_usaha);
                envelope_usaha.bodyOut = requestretrive_usaha;
                androidHttpTransport.call(SOAP_ACTION_USAHA, envelope_usaha);

                // This step: get file XML
                responseBody = (SoapObject) envelope_usaha.getResponse();
                // remove information XML,only retrieved results that returned
                responseBody = (SoapObject) responseBody.getProperty(1);
                // get information XMl of tables that is returned

                if (responseBody.getPropertyCount() == 0) {
                    error = true;
                }
                table = (SoapObject) responseBody.getProperty(0);
                //Get information each row in table,0 is first row

                db = new DBA_MASTER_LOB(getBaseContext());
                db.open();
                db.deleteAll();

                int iTotalDataFromWebService = table.getPropertyCount();

                for (int i = 0; i < iTotalDataFromWebService; i++) {
                    tableRow = (SoapObject) table.getProperty(i);
                    //Log.d("USAHA", tableRow.getPropertySafelyAsString("GECURFDS").toString());
                    db.insert(tableRow.getPropertySafelyAsString("GECURFYY").toString(), tableRow.getPropertySafelyAsString("GECURFDS").toString());
                }

            } catch (ArrayIndexOutOfBoundsException ex) {
                ex.printStackTrace();
                error = true;
            } catch (SocketTimeoutException ex) {
                ex.printStackTrace();
                error = true;
            } catch (Exception e) {
                e.printStackTrace();
                error = true;
            } finally {
                if (db != null)
                    db.close();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            try {
                if (error) {
//					Toast.makeText(getBaseContext(), "Retrive usaha failed", Toast.LENGTH_SHORT).show();
                    bFinishUsaha = false;
                    DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                    dbVersion.open();
                    dbVersion.setNeedSync("ListUsaha");
                    dbVersion.close();
                    Log.d("Sync. Usaha", "Fail");
                    errorSync = true;
                } else {
                    bFinishUsaha = true;
                    DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                    dbVersion.open();
                    dbVersion.setSuccessSync("ListUsaha");
                    dbVersion.close();
                    Log.d("Sync. Usaha", "Success");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /****************************
     * DNO BUSINESS TYPE	*
     * *
     * *
     **************************/

    private class BusTypeWS extends AsyncTask<String, Void, Void> {

        private Boolean error = false;
        private String errorMessage = "";

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            envelope_bus_type = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope_bus_type.implicitTypes = true;
            envelope_bus_type.dotNet = true;    //used only if we use the webservice from a dot net file (asmx)
            androidHttpTransport = new HttpTransportSE(URL);
        }

        @Override
        protected Void doInBackground(String... params) {

            error = false;
            DBA_MASTER_BUSINESS_TYPE db = null;

            try {

                SoapObject table = null;                        // Contains table of dataset that returned through SoapObject
                SoapObject tableRow = null;                     // Contains row of table
                SoapObject responseBody = null;                    // Contains XML content of dataset

                envelope_bus_type.setOutputSoapObject(requestretrive_bus_type);
                envelope_bus_type.bodyOut = requestretrive_bus_type;
                androidHttpTransport.call(SOAP_ACTION_BUS_TYPE, envelope_bus_type);

                responseBody = (SoapObject) envelope_bus_type.getResponse();
                responseBody = (SoapObject) responseBody.getProperty(1);

                if (responseBody.getPropertyCount() == 0) {
                    error = true;
                }
                table = (SoapObject) responseBody.getProperty(0);

                db = new DBA_MASTER_BUSINESS_TYPE(getBaseContext());
                db.open();
                db.deleteAll();

                int iTotalDataFromWebService = table.getPropertyCount();

                for (int i = 0; i < iTotalDataFromWebService; i++) {
                    tableRow = (SoapObject) table.getProperty(i);
                    Log.d("JENIS USAHA", tableRow.getPropertySafelyAsString("JENIS_USAHA").toString());
                    db.insert(tableRow.getPropertySafelyAsString("JENIS_USAHA").toString());
                }

            } catch (ArrayIndexOutOfBoundsException ex) {
                ex.printStackTrace();
                error = true;
            } catch (SocketTimeoutException ex) {
                ex.printStackTrace();
                error = true;
            } catch (Exception e) {
                e.printStackTrace();
                error = true;
            } finally {
                if (db != null)
                    db.close();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            try {
                if (error) {
//					Toast.makeText(getBaseContext(), "Retrive jenis usaha failed", Toast.LENGTH_SHORT).show();
                    bFinishBusType = false;
                    DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                    dbVersion.open();
                    dbVersion.setNeedSync("JenisUsahaLiability");
                    dbVersion.close();
                    Log.d("Sync. Jenis Usaha", "Fail");
                    errorSync = true;
                } else {
                    bFinishBusType = true;
                    DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                    dbVersion.open();
                    dbVersion.setSuccessSync("JenisUsahaLiability");
                    dbVersion.close();
                    Log.d("Sync. Jenis Usaha", "Success");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    /****************************
     * VESSEL TYPE			*
     * *
     * *
     **************************/

    private class VesselTypeWS extends AsyncTask<String, Void, Void> {

        private Boolean error = false;
        private String errorMessage = "";

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            envelope_vessel_type = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope_vessel_type.implicitTypes = true;
            envelope_vessel_type.dotNet = true;    //used only if we use the webservice from a dot net file (asmx)
            androidHttpTransport = new HttpTransportSE(URL);
        }

        @Override
        protected Void doInBackground(String... params) {

            error = false;
            DBA_MASTER_VESSEL_TYPE db = null;

            try {

                SoapObject table = null;                        // Contains table of dataset that returned through SoapObject
                SoapObject tableRow = null;                     // Contains row of table
                SoapObject responseBody = null;                    // Contains XML content of dataset

                envelope_vessel_type.setOutputSoapObject(requestretrive_vessel_type);
                envelope_vessel_type.bodyOut = requestretrive_vessel_type;
                androidHttpTransport.call(SOAP_ACTION_VESSEL_TYPE, envelope_vessel_type);

                responseBody = (SoapObject) envelope_vessel_type.getResponse();
                responseBody = (SoapObject) responseBody.getProperty(1);

                if (responseBody.getPropertyCount() == 0) {
                    error = true;
                }
                table = (SoapObject) responseBody.getProperty(0);

                db = new DBA_MASTER_VESSEL_TYPE(getBaseContext());
                db.open();
                db.deleteAll();

                int iTotalDataFromWebService = table.getPropertyCount();

                for (int i = 0; i < iTotalDataFromWebService; i++) {
                    tableRow = (SoapObject) table.getProperty(i);

                    Log.d("Desc1", tableRow.getPropertySafelyAsString("Desc1").toString());

                    db.insert(

                            tableRow.getPropertySafelyAsString("Status").toString()
                            , tableRow.getPropertySafelyAsString("Code").toString()
                            , tableRow.getPropertySafelyAsString("Type").toString()
                            , tableRow.getPropertySafelyAsString("Abbrv").toString()
                            , tableRow.getPropertySafelyAsString("Desc1").toString()
                            , tableRow.getPropertySafelyAsString("Desc2").toString()

                    );
                }

            } catch (ArrayIndexOutOfBoundsException ex) {
                ex.printStackTrace();
                error = true;
            } catch (SocketTimeoutException ex) {
                ex.printStackTrace();
                error = true;
            } catch (Exception e) {
                e.printStackTrace();
                error = true;
            } finally {
                if (db != null)
                    db.close();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            try {
                if (error) {

                    bFinishVesselType = false;

                    DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                    dbVersion.open();
                    dbVersion.setNeedSync("VesselType");
                    dbVersion.close();

                    Log.d("Sync. Vessel type", "Fail");
                    errorSync = true;
                } else {
                    bFinishVesselType = true;

                    DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                    dbVersion.open();
                    dbVersion.setSuccessSync("VesselType");
                    dbVersion.close();

                    Log.d("Sync. Vessel type", "Success");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    /****************************
     * VESSEL DETAIL 		*
     * *
     * *
     **************************/

    private class VesselDetailWS extends AsyncTask<String, Void, Void> {

        private Boolean error = false;
        private String errorMessage = "";

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            envelope_vessel_detail = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope_vessel_detail.implicitTypes = true;
            envelope_vessel_detail.dotNet = true;    //used only if we use the webservice from a dot net file (asmx)
            androidHttpTransport = new HttpTransportSE(URL);
        }

        @Override
        protected Void doInBackground(String... params) {

            error = false;
            DBA_MASTER_VESSEL_DETAIL db = null;

            try {

                SoapObject table = null;                        // Contains table of dataset that returned through SoapObject
                SoapObject tableRow = null;                     // Contains row of table
                SoapObject responseBody = null;                    // Contains XML content of dataset

                envelope_vessel_detail.setOutputSoapObject(requestretrive_vessel_detail);
                envelope_vessel_detail.bodyOut = requestretrive_vessel_detail;
                androidHttpTransport.call(SOAP_ACTION_VESSEL_DETAIL, envelope_vessel_detail);

                responseBody = (SoapObject) envelope_vessel_detail.getResponse();
                responseBody = (SoapObject) responseBody.getProperty(1);

                if (responseBody.getPropertyCount() == 0) {
                    error = true;
                }
                table = (SoapObject) responseBody.getProperty(0);

                db = new DBA_MASTER_VESSEL_DETAIL(getBaseContext());
                db.open();
                db.deleteAll();

                int iTotalDataFromWebService = table.getPropertyCount();

                for (int i = 0; i < iTotalDataFromWebService; i++) {
                    tableRow = (SoapObject) table.getProperty(i);

//	            	Log.d("VesselName", tableRow.getPropertySafelyAsString("VesselName").toString());

                    db.insert(
                            tableRow.getPropertySafelyAsString("Status").toString()
                            , tableRow.getPropertySafelyAsString("VesselCode").toString()
                            , tableRow.getPropertySafelyAsString("VesselType").toString()
                            , tableRow.getPropertySafelyAsString("VesselName").toString()
                            , tableRow.getPropertySafelyAsString("VesselType2").toString()
                            , tableRow.getPropertySafelyAsString("Weight").toString()
                            , tableRow.getPropertySafelyAsString("Weight2").toString()
                            , tableRow.getPropertySafelyAsString("Year").toString()
                            , tableRow.getPropertySafelyAsString("Month").toString()
                            , tableRow.getPropertySafelyAsString("Length").toString()
                            , tableRow.getPropertySafelyAsString("Width").toString()
                            , tableRow.getPropertySafelyAsString("Height").toString()
                            , tableRow.getPropertySafelyAsString("TotalEngine").toString()
                            , tableRow.getPropertySafelyAsString("Engine").toString()
                            , tableRow.getPropertySafelyAsString("Brand").toString()
                            , tableRow.getPropertySafelyAsString("ExName").toString()
                            , tableRow.getPropertySafelyAsString("YOH").toString()
                            , tableRow.getPropertySafelyAsString("Registration").toString()
                            , tableRow.getPropertySafelyAsString("Material ").toString()
                            , tableRow.getPropertySafelyAsString("Flag").toString()
                            , tableRow.getPropertySafelyAsString("BLNo").toString()
                            , tableRow.getPropertySafelyAsString("Filler").toString()

                    );
                }

            } catch (ArrayIndexOutOfBoundsException ex) {
                ex.printStackTrace();
                error = true;
            } catch (SocketTimeoutException ex) {
                ex.printStackTrace();
                error = true;
            } catch (Exception e) {
                e.printStackTrace();
                error = true;
            } finally {
                if (db != null)
                    db.close();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            try {
                if (error) {

                    bFinishVesselDetail = false;

                    DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                    dbVersion.open();
                    dbVersion.setNeedSync("VesselName");
                    dbVersion.close();

                    Log.d("Sync. Vessel detail", "Fail");
                    errorSync = true;
                } else {
                    bFinishVesselDetail = true;

                    DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                    dbVersion.open();
                    dbVersion.setSuccessSync("VesselName");
                    dbVersion.close();

                    Log.d("Sync. Vessel detail", "Success");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    /****************
     * ENTER RATE WS
     *******************/


    private class ASRIRateWS extends AsyncTask<String, Void, Void> {

        private Boolean error = false;
        private String errorMessage = "";

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            envelope_asri_rate = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope_asri_rate.implicitTypes = true;
            envelope_asri_rate.dotNet = true;    //used only if we use the webservice from a dot net file (asmx)
            androidHttpTransport = new HttpTransportSE(URL);
        }

        @Override
        protected Void doInBackground(String... params) {

            error = false;
            DBA_MASTER_ASRI_RATE db = null;

            try {

                SoapObject table = null;                        // Contains table of dataset that returned through SoapObject
                SoapObject tableRow = null;                     // Contains row of table
                SoapObject responseBody = null;                    // Contains XML content of dataset

                envelope_asri_rate.setOutputSoapObject(requestretrive_asri_rate);
                envelope_asri_rate.bodyOut = requestretrive_asri_rate;
                androidHttpTransport.call(SOAP_ACTION_ASRI_RATE, envelope_asri_rate);

                responseBody = (SoapObject) envelope_asri_rate.getResponse();
                responseBody = (SoapObject) responseBody.getProperty(1);

                if (responseBody.getPropertyCount() == 0) {
                    error = true;
                }
                table = (SoapObject) responseBody.getProperty(0);

                db = new DBA_MASTER_ASRI_RATE(getBaseContext());
                db.open();
                db.deleteAll();

                int iTotalDataFromWebService = table.getPropertyCount();

                for (int i = 0; i < iTotalDataFromWebService; i++) {
                    tableRow = (SoapObject) table.getProperty(i);
                    db.insert(tableRow.getPropertySafelyAsString("ZONE").toString(), Double.parseDouble(tableRow.getPropertySafelyAsString("RATE").toString()), tableRow.getPropertySafelyAsString("KETERANGAN").toString());
                }

            } catch (ArrayIndexOutOfBoundsException ex) {
                ex.printStackTrace();
                error = true;
            } catch (SocketTimeoutException ex) {
                ex.printStackTrace();
                error = true;
            } catch (Exception e) {
                e.printStackTrace();
                error = true;
            } finally {
                if (db != null)
                    db.close();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            try {
                if (error) {
//					Toast.makeText(getBaseContext(), "Retrive ASRI Rate failed", Toast.LENGTH_SHORT).show();
                    bFinishASRIRate = false;
                    DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                    dbVersion.open();
                    dbVersion.setNeedSync("RateASRI");
                    dbVersion.close();
                    Log.d("Sync. ASRI Rate", "Fail");
                    errorSync = true;
                } else {
                    bFinishASRIRate = true;
                    DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                    dbVersion.open();
                    dbVersion.setSuccessSync("RateASRI");
                    dbVersion.close();
                    Log.d("Sync. ASRI Rate", "Success");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    private class OtomateRateWS extends AsyncTask<String, Void, Void> {

        private Boolean error = false;
        private String errorMessage = "";

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            envelope_otomate_rate = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope_otomate_rate.implicitTypes = true;
            envelope_otomate_rate.dotNet = true;
            androidHttpTransport = new HttpTransportSE(URL);
        }

        @Override
        protected Void doInBackground(String... params) {

            error = false;
            DBA_MASTER_OTOMATE_RATE db = null;

            try {

                SoapObject table = null;
                SoapObject tableRow = null;
                SoapObject responseBody = null;

                envelope_otomate_rate.setOutputSoapObject(requestretrive_otomate_rate);
                envelope_otomate_rate.bodyOut = requestretrive_otomate_rate;
                androidHttpTransport.call(SOAP_ACTION_OTOMATE_RATE, envelope_otomate_rate);

                responseBody = (SoapObject) envelope_otomate_rate.getResponse();
                responseBody = (SoapObject) responseBody.getProperty(1);

                if (responseBody.getPropertyCount() == 0) {
                    error = true;
                }
                table = (SoapObject) responseBody.getProperty(0);

                db = new DBA_MASTER_OTOMATE_RATE(getBaseContext());
                db.open();
                db.deleteAll();

                int iTotalDataFromWebService = table.getPropertyCount();
                Log.d("otomate rate", String.valueOf(iTotalDataFromWebService));

                for (int i = 0; i < iTotalDataFromWebService; i++) {
                    tableRow = (SoapObject) table.getProperty(i);
                    Log.d("jnp", tableRow.getPropertySafelyAsString("JNP").toString());
                    db.insert(tableRow.getPropertySafelyAsString("Kategori").toString(),
                            tableRow.getPropertySafelyAsString("JNP").toString(),
                            tableRow.getPropertySafelyAsString("Wilayah").toString(),
                            Double.parseDouble(tableRow.getPropertySafelyAsString("Rate").toString()),
                            tableRow.getPropertySafelyAsString("Kode_Produk").toString()
                    );
                }
            } catch (ArrayIndexOutOfBoundsException ex) {
                ex.printStackTrace();
                error = true;
            } catch (SocketTimeoutException ex) {
                ex.printStackTrace();
                error = true;
            } catch (Exception e) {
                e.printStackTrace();
                error = true;
            } finally {
                if (db != null)
                    db.close();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            try {
                if (error) {
//					Toast.makeText(getBaseContext(), "Retrive Otomate Rate failed", Toast.LENGTH_SHORT).show();
                    bFinishOtomateRate = false;
                    DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                    dbVersion.open();
                    dbVersion.setNeedSync("RateOtomate");
                    dbVersion.close();
                    Log.d("Sync. Otomate Rate", "Fail");
                    errorSync = true;
                } else {
                    bFinishOtomateRate = true;
                    DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                    dbVersion.open();
                    dbVersion.setSuccessSync("RateOtomate");
                    dbVersion.close();
                    Log.d("Sync. Otomate Rate", "Success");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    private class ACAMobilRateWS extends AsyncTask<String, Void, Void> {

        private Boolean error = false;
        private String errorMessage = "";

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            envelope_acamobil_rate = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope_acamobil_rate.implicitTypes = true;
            envelope_acamobil_rate.dotNet = true;
            androidHttpTransport = new HttpTransportSE(URL);
        }

        @Override
        protected Void doInBackground(String... params) {

            error = false;
            DBA_MASTER_ACA_MOBIL_RATE db = null;

            try {

                SoapObject table = null;
                SoapObject tableRow = null;
                SoapObject responseBody = null;

                envelope_acamobil_rate.setOutputSoapObject(requestretrive_acamobil_rate);
                envelope_acamobil_rate.bodyOut = requestretrive_acamobil_rate;
                androidHttpTransport.call(SOAP_ACTION_ACAMOBIL_RATE, envelope_acamobil_rate);

                responseBody = (SoapObject) envelope_acamobil_rate.getResponse();
                responseBody = (SoapObject) responseBody.getProperty(1);

                if (responseBody.getPropertyCount() == 0) {
                    error = true;
                }
                table = (SoapObject) responseBody.getProperty(0);

                db = new DBA_MASTER_ACA_MOBIL_RATE(getBaseContext());
                db.open();
                db.deleteAll();

                int iTotalDataFromWebService = table.getPropertyCount();
                Log.d("aca mobil rate", String.valueOf(iTotalDataFromWebService));

                for (int i = 0; i < iTotalDataFromWebService; i++) {
                    tableRow = (SoapObject) table.getProperty(i);
                    db.insert("1", tableRow.getPropertySafelyAsString("TSI").toString(), Double.parseDouble(tableRow.getPropertySafelyAsString("RATE").toString()));
                }
            } catch (ArrayIndexOutOfBoundsException ex) {
                ex.printStackTrace();
                error = true;
            } catch (SocketTimeoutException ex) {
                ex.printStackTrace();
                error = true;
            } catch (Exception e) {
                e.printStackTrace();
                error = true;
            } finally {
                if (db != null)
                    db.close();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            try {
                if (error) {
//					Toast.makeText(getBaseContext(), "Retrive ACA Mobil Rate failed", Toast.LENGTH_SHORT).show();
                    bFinishACAMobilRate = false;
                    DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                    dbVersion.open();
                    dbVersion.setNeedSync("RateMobil");
                    dbVersion.close();
                    Log.d("Sync. ACA Mobil Rate", "Fail");
                    errorSync = true;
                } else {
                    bFinishACAMobilRate = true;
                    DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                    dbVersion.open();
                    dbVersion.setSuccessSync("RateMobil");
                    dbVersion.close();
                    Log.d("Sync. ACA Mobil Rate", "Success");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private class MedisafeRateWS extends AsyncTask<String, Void, Void> {

        private Boolean error = false;
        private String errorMessage = "";

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            envelope_medisafe_rate = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope_medisafe_rate.implicitTypes = true;
            envelope_medisafe_rate.dotNet = true;
            androidHttpTransport = new HttpTransportSE(URL);
        }

        @Override
        protected Void doInBackground(String... params) {

            error = false;
            DBA_MASTER_MEDISAFE_RATE db = null;

            try {

                SoapObject table = null;
                SoapObject tableRow = null;
                SoapObject responseBody = null;

                envelope_medisafe_rate.setOutputSoapObject(requestretrive_medisafe_rate);
                envelope_medisafe_rate.bodyOut = requestretrive_medisafe_rate;
                androidHttpTransport.call(SOAP_ACTION_MEDISAFE_RATE, envelope_medisafe_rate);

                responseBody = (SoapObject) envelope_medisafe_rate.getResponse();
                responseBody = (SoapObject) responseBody.getProperty(1);

                if (responseBody.getPropertyCount() == 0) {
                    error = true;
                }
                table = (SoapObject) responseBody.getProperty(0);

                db = new DBA_MASTER_MEDISAFE_RATE(getBaseContext());
                db.open();
                db.deleteAll();

                int iTotalDataFromWebService = table.getPropertyCount();

                for (int i = 0; i < iTotalDataFromWebService; i++) {
                    tableRow = (SoapObject) table.getProperty(i);
                    db.insert(Integer.parseInt(tableRow.getPropertySafelyAsString("Type").toString()), Integer.parseInt(tableRow.getPropertySafelyAsString("Plan").toString()), Integer.parseInt(tableRow.getPropertySafelyAsString("Age").toString()), Double.parseDouble(tableRow.getPropertySafelyAsString("Premi").toString()));
                }

            } catch (ArrayIndexOutOfBoundsException ex) {
                ex.printStackTrace();
                error = true;
            } catch (SocketTimeoutException ex) {
                ex.printStackTrace();
                error = true;
            } catch (Exception e) {
                e.printStackTrace();
                error = true;
            } finally {
                if (db != null)
                    db.close();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            try {
                if (error) {
//					Toast.makeText(getBaseContext(), "Retrive Medisafe Rate failed", Toast.LENGTH_SHORT).show();
                    bFinishMedisafeRate = false;
                    DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                    dbVersion.open();
                    dbVersion.setNeedSync("PremiMedisafe");
                    dbVersion.close();
                    Log.d("Sync. Medisafe Rate", "Fail");
                    errorSync = true;
                } else {
                    bFinishMedisafeRate = true;
                    DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                    dbVersion.open();
                    dbVersion.setSuccessSync("PremiMedisafe");
                    dbVersion.close();
                    Log.d("Sync. Medisafe Rate", "Success");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    private class ExecutiveSafeRateWS extends AsyncTask<String, Void, Void> {

        private Boolean error = false;
        private String errorMessage = "";

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            envelope_executive_rate = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope_executive_rate.implicitTypes = true;
            envelope_executive_rate.dotNet = true;
            androidHttpTransport = new HttpTransportSE(URL);
        }

        @Override
        protected Void doInBackground(String... params) {

            error = false;
            DBA_MASTER_EXECUTIVE_SAFE_RATE db = null;

            try {

                SoapObject table = null;
                SoapObject tableRow = null;
                SoapObject responseBody = null;

                envelope_executive_rate.setOutputSoapObject(requestretrive_executive_rate);
                envelope_executive_rate.bodyOut = requestretrive_executive_rate;
                androidHttpTransport.call(SOAP_ACTION_EXECUTIVE_RATE, envelope_executive_rate);

                responseBody = (SoapObject) envelope_executive_rate.getResponse();
                responseBody = (SoapObject) responseBody.getProperty(1);


                if (responseBody.getPropertyCount() == 0) {
                    error = true;
                }
                table = (SoapObject) responseBody.getProperty(0);

                db = new DBA_MASTER_EXECUTIVE_SAFE_RATE(getBaseContext());
                db.open();
                db.deleteAll();

                int iTotalDataFromWebService = table.getPropertyCount();

                for (int i = 0; i < iTotalDataFromWebService; i++) {
                    tableRow = (SoapObject) table.getProperty(i);
                    db.insert(Integer.parseInt(tableRow.getPropertySafelyAsString("Type").toString()), Integer.parseInt(tableRow.getPropertySafelyAsString("Plan").toString()), Integer.parseInt(tableRow.getPropertySafelyAsString("Age").toString()), Double.parseDouble(tableRow.getPropertySafelyAsString("Premi").toString()));
                }

            } catch (ArrayIndexOutOfBoundsException ex) {
                ex.printStackTrace();
                error = true;
            } catch (SocketTimeoutException ex) {
                ex.printStackTrace();
                error = true;
            } catch (Exception e) {
                e.printStackTrace();
                error = true;
            } finally {
                if (db != null)
                    db.close();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            try {
                if (error) {
//					Toast.makeText(getBaseContext(), "Retrive Executive Safe Rate failed", Toast.LENGTH_SHORT).show();
                    bFinishExecutiveRate = false;
                    DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                    dbVersion.open();
                    dbVersion.setNeedSync("PremiExecutiveSafe");
                    dbVersion.close();
                    Log.d("Sync. Executive Safe Rate", "Fail");
                    errorSync = true;
                } else {
                    bFinishExecutiveRate = true;
                    DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                    dbVersion.open();
                    dbVersion.setSuccessSync("PremiExecutiveSafe");
                    dbVersion.close();
                    Log.d("Sync. Executive Safe Rate", "Success");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    private class PAAmanahRateWS extends AsyncTask<String, Void, Void> {

        private Boolean error = false;
        private String errorMessage = "";

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            envelope_pa_amanah_rate = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope_pa_amanah_rate.implicitTypes = true;
            envelope_pa_amanah_rate.dotNet = true;
            androidHttpTransport = new HttpTransportSE(URL);
        }

        @Override
        protected Void doInBackground(String... params) {

            error = false;
            DBA_MASTER_PA_AMANAH_RATE db = null;

            try {

                SoapObject table = null;
                SoapObject tableRow = null;
                SoapObject responseBody = null;

                envelope_pa_amanah_rate.setOutputSoapObject(requestretrive_pa_amanah_rate);
                envelope_pa_amanah_rate.bodyOut = requestretrive_pa_amanah_rate;
                androidHttpTransport.call(SOAP_ACTION_PA_AMANAH_RATE, envelope_pa_amanah_rate);

                responseBody = (SoapObject) envelope_pa_amanah_rate.getResponse();
                responseBody = (SoapObject) responseBody.getProperty(1);

                if (responseBody.getPropertyCount() == 0) {
                    error = true;
                }
                table = (SoapObject) responseBody.getProperty(0);

                db = new DBA_MASTER_PA_AMANAH_RATE(getBaseContext());
                db.open();
                db.deleteAll();

                int iTotalDataFromWebService = table.getPropertyCount();

                for (int i = 0; i < iTotalDataFromWebService; i++) {
                    tableRow = (SoapObject) table.getProperty(i);
                    db.insert(Integer.parseInt(tableRow.getPropertySafelyAsString("Type").toString()), Integer.parseInt(tableRow.getPropertySafelyAsString("Plan").toString()), Integer.parseInt(tableRow.getPropertySafelyAsString("Age").toString()), Double.parseDouble(tableRow.getPropertySafelyAsString("Premi").toString()));
                }

            } catch (ArrayIndexOutOfBoundsException ex) {
                ex.printStackTrace();
                error = true;
            } catch (SocketTimeoutException ex) {
                ex.printStackTrace();
                error = true;
            } catch (Exception e) {
                e.printStackTrace();
                error = true;
            } finally {
                if (db != null)
                    db.close();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            try {
                if (error) {
//					Toast.makeText(getBaseContext(), "Retrive PA Amanah Rate failed", Toast.LENGTH_SHORT).show();
                    bFinishPAAmanahRate = false;
                    DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                    dbVersion.open();
                    dbVersion.setNeedSync("PremiPA");
                    dbVersion.close();
                    Log.d("Sync. PA Amanah Rate", "Fail");
                    errorSync = true;
                } else {
                    bFinishPAAmanahRate = true;
                    DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                    dbVersion.open();
                    dbVersion.setSuccessSync("PremiPA");
                    dbVersion.close();
                    Log.d("Sync. PA Amanah Rate", "Success");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private class TravelSafeDomRateWS extends AsyncTask<String, Void, Void> {

        private Boolean error = false;
        private String errorMessage = "";

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            envelope_ts_dom_rate = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope_ts_dom_rate.implicitTypes = true;
            envelope_ts_dom_rate.dotNet = true;
            androidHttpTransport = new HttpTransportSE(URL);
        }

        @Override
        protected Void doInBackground(String... params) {

            error = false;
            DBA_MASTER_TRAVELSAFE_DOM_RATE db = null;

            try {

                SoapObject table = null;
                SoapObject tableRow = null;
                SoapObject responseBody = null;

                envelope_ts_dom_rate.setOutputSoapObject(requestretrive_ts_dom_rate);
                envelope_ts_dom_rate.bodyOut = requestretrive_ts_dom_rate;
                androidHttpTransport.call(SOAP_ACTION_TS_DOM_RATE, envelope_ts_dom_rate);

                responseBody = (SoapObject) envelope_ts_dom_rate.getResponse();
                responseBody = (SoapObject) responseBody.getProperty(1);

                if (responseBody.getPropertyCount() == 0) {
                    error = true;
                }
                table = (SoapObject) responseBody.getProperty(0);

                db = new DBA_MASTER_TRAVELSAFE_DOM_RATE(getBaseContext());
                db.open();
                db.deleteAll();

                int iTotalDataFromWebService = table.getPropertyCount();

                for (int i = 0; i < iTotalDataFromWebService; i++) {
                    tableRow = (SoapObject) table.getProperty(i);
                    db.insert(tableRow.getPropertySafelyAsString("Area").toString(), tableRow.getPropertySafelyAsString("Coverage").toString(), tableRow.getPropertySafelyAsString("Duration_Code").toString(), Double.parseDouble(tableRow.getPropertySafelyAsString("Premi").toString()), Double.parseDouble(tableRow.getPropertySafelyAsString("Max_Benefit").toString()));
                }

            } catch (ArrayIndexOutOfBoundsException ex) {
                ex.printStackTrace();
                error = true;
            } catch (SocketTimeoutException ex) {
                ex.printStackTrace();
                error = true;
            } catch (Exception e) {
                e.printStackTrace();
                error = true;
            } finally {
                if (db != null)
                    db.close();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            try {
                if (error) {
//					Toast.makeText(getBaseContext(), "Retrive Travel Safe Dom Rate failed", Toast.LENGTH_SHORT).show();
                    bFinishTSDomRate = false;
                    DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                    dbVersion.open();
                    dbVersion.setNeedSync("PremiTSDomestik");
                    dbVersion.close();
                    Log.d("Sync. Travel Safe Dom Rate", "Fail");
                    errorSync = true;
                } else {
                    bFinishTSDomRate = true;
                    DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                    dbVersion.open();
                    dbVersion.setSuccessSync("PremiTSDomestik");
                    dbVersion.close();
                    Log.d("Sync. Travel Safe Dom Rate", "Success");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    private class TravelSafeIntRateWS extends AsyncTask<String, Void, Void> {

        private Boolean error = false;
        private String errorMessage = "";

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            envelope_ts_int_rate = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope_ts_int_rate.implicitTypes = true;
            envelope_ts_int_rate.dotNet = true;
            androidHttpTransport = new HttpTransportSE(URL);
        }

        @Override
        protected Void doInBackground(String... params) {

            error = false;
            DBA_MASTER_TRAVELSAFE_INT_RATE db = null;

            try {

                SoapObject table = null;
                SoapObject tableRow = null;
                SoapObject responseBody = null;

                envelope_ts_int_rate.setOutputSoapObject(requestretrive_ts_int_rate);
                envelope_ts_int_rate.bodyOut = requestretrive_ts_int_rate;
                androidHttpTransport.call(SOAP_ACTION_TS_INT_RATE, envelope_ts_int_rate);

                responseBody = (SoapObject) envelope_ts_int_rate.getResponse();
                responseBody = (SoapObject) responseBody.getProperty(1);

                if (responseBody.getPropertyCount() == 0) {
                    error = true;
                }
                table = (SoapObject) responseBody.getProperty(0);

                db = new DBA_MASTER_TRAVELSAFE_INT_RATE(getBaseContext());
                db.open();
                db.deleteAll();

                int iTotalDataFromWebService = table.getPropertyCount();

                for (int i = 0; i < iTotalDataFromWebService; i++) {
                    tableRow = (SoapObject) table.getProperty(i);
                    db.insert(tableRow.getPropertySafelyAsString("Area").toString(), tableRow.getPropertySafelyAsString("Coverage").toString(), tableRow.getPropertySafelyAsString("Duration_Code").toString(), Double.parseDouble(tableRow.getPropertySafelyAsString("Premi").toString()), Double.parseDouble(tableRow.getPropertySafelyAsString("Max_Benefit").toString()));
                }

            } catch (ArrayIndexOutOfBoundsException ex) {
                ex.printStackTrace();
                error = true;
            } catch (SocketTimeoutException ex) {
                ex.printStackTrace();
                error = true;
            } catch (Exception e) {
                e.printStackTrace();
                error = true;
            } finally {
                if (db != null)
                    db.close();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            try {
                if (error) {
//					Toast.makeText(getBaseContext(), "Retrive Travel Safe Int. Rate failed", Toast.LENGTH_SHORT).show();
                    bFinishTSIntRate = false;
                    DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                    dbVersion.open();
                    dbVersion.setNeedSync("PremiTSInt");
                    dbVersion.close();
                    Log.d("Sync. Travel Safe Int. Rate", "Fail");
                    errorSync = true;
                } else {
                    bFinishTSIntRate = true;
                    DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                    dbVersion.open();
                    dbVersion.setSuccessSync("PremiTSInt");
                    dbVersion.close();
                    Log.d("Sync. Travel Safe Int. Rate", "Success");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    private class TokoRateWS extends AsyncTask<String, Void, Void> {

        private Boolean error = false;
        private String errorMessage = "";

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            envelope_toko_rate = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope_toko_rate.implicitTypes = true;
            envelope_toko_rate.dotNet = true;
            androidHttpTransport = new HttpTransportSE(URL);
        }

        @Override
        protected Void doInBackground(String... params) {

            error = false;
            DBA_MASTER_TOKO_RATE db = null;

            try {

                SoapObject table = null;
                SoapObject tableRow = null;
                SoapObject responseBody = null;

                envelope_toko_rate.setOutputSoapObject(requestretrive_toko_rate);
                envelope_toko_rate.bodyOut = requestretrive_toko_rate;
                androidHttpTransport.call(SOAP_ACTION_TOKO_RATE, envelope_toko_rate);

                responseBody = (SoapObject) envelope_toko_rate.getResponse();
                responseBody = (SoapObject) responseBody.getProperty(1);

                if (responseBody.getPropertyCount() == 0) {
                    error = true;
                }
                table = (SoapObject) responseBody.getProperty(0);

                db = new DBA_MASTER_TOKO_RATE(getBaseContext());
                db.open();
                db.deleteAll();

                int iTotalDataFromWebService = table.getPropertyCount();

                for (int i = 0; i < iTotalDataFromWebService; i++) {
                    tableRow = (SoapObject) table.getProperty(i);
                    db.insert(Integer.parseInt(tableRow.getPropertySafelyAsString("ID").toString()), tableRow.getPropertySafelyAsString("OKUPASI_DESCRIPTION").toString(), Double.parseDouble(tableRow.getPropertySafelyAsString("RATE").toString()));
                }

            } catch (ArrayIndexOutOfBoundsException ex) {
                ex.printStackTrace();
                error = true;
            } catch (SocketTimeoutException ex) {
                ex.printStackTrace();
                error = true;
            } catch (Exception e) {
                e.printStackTrace();
                error = true;
            } finally {
                if (db != null)
                    db.close();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            try {
                if (error) {
//					Toast.makeText(getBaseContext(), "Retrive Toko Rate failed", Toast.LENGTH_SHORT).show();
                    bFinishTokoRate = false;
                    DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                    dbVersion.open();
                    dbVersion.setNeedSync("RateToko");
                    dbVersion.close();
                    Log.d("Sync. Toko Rate", "Fail");
                    errorSync = true;
                } else {
                    bFinishTokoRate = true;
                    DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                    dbVersion.open();
                    dbVersion.setSuccessSync("RateToko");
                    dbVersion.close();
                    Log.d("Sync. Toko Rate", "Success");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private class WellWomanRateWS extends AsyncTask<String, Void, Void> {

        private Boolean error = false;
        private String errorMessage = "";

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            envelope_wellwoman_rate = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope_wellwoman_rate.implicitTypes = true;
            envelope_wellwoman_rate.dotNet = true;
            androidHttpTransport = new HttpTransportSE(URL);
        }

        @Override
        protected Void doInBackground(String... params) {

            error = false;
            DBA_MASTER_WELLWOMAN_RATE db = null;

            try {

                SoapObject table = null;
                SoapObject tableRow = null;
                SoapObject responseBody = null;

                envelope_wellwoman_rate.setOutputSoapObject(requestretrive_wellwoman_rate);
                envelope_wellwoman_rate.bodyOut = requestretrive_wellwoman_rate;
                androidHttpTransport.call(SOAP_ACTION_WELLWOMAN_RATE, envelope_wellwoman_rate);

                responseBody = (SoapObject) envelope_wellwoman_rate.getResponse();
                responseBody = (SoapObject) responseBody.getProperty(1);

                if (responseBody.getPropertyCount() == 0) {
                    error = true;
                }
                table = (SoapObject) responseBody.getProperty(0);

                db = new DBA_MASTER_WELLWOMAN_RATE(getBaseContext());
                db.open();
                db.deleteAll();

                int iTotalDataFromWebService = table.getPropertyCount();

                for (int i = 0; i < iTotalDataFromWebService; i++) {
                    tableRow = (SoapObject) table.getProperty(i);

                    Log.d("Plan", tableRow.getPropertySafelyAsString("Plan").toString());
                    Log.d("Benefit", tableRow.getPropertySafelyAsString("Benefit").toString());
                    Log.d("Age", tableRow.getPropertySafelyAsString("Age").toString());
                    Log.d("Premi", tableRow.getPropertySafelyAsString("Premi").toString());
                    Log.d("PremiReas", tableRow.getPropertySafelyAsString("PremiReas").toString());


                    db.insert(tableRow.getPropertySafelyAsString("Plan").toString(),
                            tableRow.getPropertySafelyAsString("Benefit").toString(),
                            tableRow.getPropertySafelyAsString("Age").toString(),
                            Double.parseDouble(tableRow.getPropertySafelyAsString("Premi").toString()),
                            Double.parseDouble(tableRow.getPropertySafelyAsString("PremiReas").toString()));
                }

            } catch (ArrayIndexOutOfBoundsException ex) {
                ex.printStackTrace();
                error = true;
            } catch (SocketTimeoutException ex) {
                ex.printStackTrace();
                error = true;
            } catch (Exception e) {
                e.printStackTrace();
                error = true;
            } finally {
                if (db != null)
                    db.close();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            try {
                if (error) {
//					Toast.makeText(getBaseContext(), "Retrive WellWoman Rate failed", Toast.LENGTH_SHORT).show();
                    bFinishWellWomanRate = false;
                    DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                    dbVersion.open();
                    dbVersion.setNeedSync("PremiWellWoman");
                    dbVersion.close();
                    //Log.d("Sync. Well Woman Rate", "Fail");
                    errorSync = true;
                } else {
                    bFinishWellWomanRate = true;
                    DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                    dbVersion.open();
                    dbVersion.setSuccessSync("PremiWellWoman");
                    dbVersion.close();
                    //Log.d("Sync. Well Woman Rate", "Success");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    private class OtomateTplWS extends AsyncTask<String, Void, Void> {

        private Boolean error = false;
        private String errorMessage = "";

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            envelope_otomate_tpl = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope_otomate_tpl.implicitTypes = true;
            envelope_otomate_tpl.dotNet = true;
            androidHttpTransport = new HttpTransportSE(URL);
        }

        @Override
        protected Void doInBackground(String... params) {

            error = false;
            DBA_MASTER_OTOMATE_TPL db = null;

            try {

                SoapObject table = null;
                SoapObject tableRow = null;
                SoapObject responseBody = null;

                envelope_otomate_tpl.setOutputSoapObject(requestretrive_Otomate_TPL);
                envelope_otomate_tpl.bodyOut = requestretrive_Otomate_TPL;
                androidHttpTransport.call(SOAP_ACTION_OTOMATE_TPL, envelope_otomate_tpl);

                responseBody = (SoapObject) envelope_otomate_tpl.getResponse();
                responseBody = (SoapObject) responseBody.getProperty(1);

                if (responseBody.getPropertyCount() == 0) {
                    error = true;
                }
                table = (SoapObject) responseBody.getProperty(0);

                db = new DBA_MASTER_OTOMATE_TPL(getBaseContext());
                db.open();
                db.deleteAll();

                int iTotalDataFromWebService = table.getPropertyCount();

                for (int i = 0; i < iTotalDataFromWebService; i++) {
                    tableRow = (SoapObject) table.getProperty(i);

                    Log.d("TPL", tableRow.getPropertySafelyAsString("TPL").toString());
                    Log.d("Premi", tableRow.getPropertySafelyAsString("Premi").toString());


                    db.insert(tableRow.getPropertySafelyAsString("TPL").toString(),
                            tableRow.getPropertySafelyAsString("Premi").toString());
                }

            } catch (ArrayIndexOutOfBoundsException ex) {
                ex.printStackTrace();
                error = true;
            } catch (SocketTimeoutException ex) {
                ex.printStackTrace();
                error = true;
            } catch (Exception e) {
                e.printStackTrace();
                error = true;
            } finally {
                if (db != null)
                    db.close();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            try {
                if (error) {
//					Toast.makeText(getBaseContext(), "Retrive WellWoman Rate failed", Toast.LENGTH_SHORT).show();
                    bFinishOtomateTPL = false;
                    DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                    dbVersion.open();
                    dbVersion.setNeedSync("PremiTPLOtomate");
                    dbVersion.close();
                    //Log.d("Sync. Well Woman Rate", "Fail");
                    errorSync = true;
                } else {
                    bFinishOtomateTPL = true;
                    DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                    dbVersion.open();
                    dbVersion.setSuccessSync("PremiTPLOtomate");
                    dbVersion.close();
                    //Log.d("Sync. Well Woman Rate", "Success");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    private class OtomatePaWS extends AsyncTask<String, Void, Void> {

        private Boolean error = false;
        private String errorMessage = "";

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            envelope_otomate_pa = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope_otomate_pa.implicitTypes = true;
            envelope_otomate_pa.dotNet = true;
            androidHttpTransport = new HttpTransportSE(URL);
        }

        @Override
        protected Void doInBackground(String... params) {

            error = false;
            DBA_MASTER_OTOMATE_PA db = null;

            try {

                SoapObject table = null;
                SoapObject tableRow = null;
                SoapObject responseBody = null;

                envelope_otomate_pa.setOutputSoapObject(requestretrive_Otomate_PA);
                envelope_otomate_pa.bodyOut = requestretrive_Otomate_PA;
                androidHttpTransport.call(SOAP_ACTION_OTOMATE_PA, envelope_otomate_pa);

                responseBody = (SoapObject) envelope_otomate_pa.getResponse();
                responseBody = (SoapObject) responseBody.getProperty(1);

                if (responseBody.getPropertyCount() == 0) {
                    error = true;
                }
                table = (SoapObject) responseBody.getProperty(0);

                db = new DBA_MASTER_OTOMATE_PA(getBaseContext());
                db.open();
                db.deleteAll();

                int iTotalDataFromWebService = table.getPropertyCount();

                for (int i = 0; i < iTotalDataFromWebService; i++) {
                    tableRow = (SoapObject) table.getProperty(i);

                    Log.d("PA", tableRow.getPropertySafelyAsString("PA").toString());
                    Log.d("Premi", tableRow.getPropertySafelyAsString("Premi").toString());


                    db.insert(tableRow.getPropertySafelyAsString("PA").toString(),
                            tableRow.getPropertySafelyAsString("Premi").toString());
                }

            } catch (ArrayIndexOutOfBoundsException ex) {
                ex.printStackTrace();
                error = true;
            } catch (SocketTimeoutException ex) {
                ex.printStackTrace();
                error = true;
            } catch (Exception e) {
                e.printStackTrace();
                error = true;
            } finally {
                if (db != null)
                    db.close();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            try {
                if (error) {
//					Toast.makeText(getBaseContext(), "Retrive WellWoman Rate failed", Toast.LENGTH_SHORT).show();
                    bFinishOtomatePA = false;
                    DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                    dbVersion.open();
                    dbVersion.setNeedSync("PremiPAOtomate");
                    dbVersion.close();
                    //Log.d("Sync. Well Woman Rate", "Fail");
                    errorSync = true;
                } else {
                    bFinishOtomatePA = true;
                    DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                    dbVersion.open();
                    dbVersion.setSuccessSync("PremiPAOtomate");
                    dbVersion.close();
                    //Log.d("Sync. Well Woman Rate", "Success");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    private class OtomateAOGWS extends AsyncTask<String, Void, Void> {

        private Boolean error = false;
        private String errorMessage = "";

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            envelope_otomate_aog = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope_otomate_aog.implicitTypes = true;
            envelope_otomate_aog.dotNet = true;
            androidHttpTransport = new HttpTransportSE(URL);
        }

        @Override
        protected Void doInBackground(String... params) {

            error = false;
            DBA_MASTER_OTOMATE_AOG db = null;

            try {

                SoapObject table = null;
                SoapObject tableRow = null;
                SoapObject responseBody = null;

                envelope_otomate_aog.setOutputSoapObject(requestretrive_Otomate_AOG);
                envelope_otomate_aog.bodyOut = requestretrive_Otomate_AOG;
                androidHttpTransport.call(SOAP_ACTION_OTOMATE_AOG, envelope_otomate_aog);

                responseBody = (SoapObject) envelope_otomate_aog.getResponse();
                responseBody = (SoapObject) responseBody.getProperty(1);

                if (responseBody.getPropertyCount() == 0) {
                    error = true;
                }
                table = (SoapObject) responseBody.getProperty(0);

                db = new DBA_MASTER_OTOMATE_AOG(getBaseContext());
                db.open();
                db.deleteAll();

                int iTotalDataFromWebService = table.getPropertyCount();

                for (int i = 0; i < iTotalDataFromWebService; i++) {
                    tableRow = (SoapObject) table.getProperty(i);

                    Log.d("Wilayah", tableRow.getPropertySafelyAsString("Wilayah").toString());
                    Log.d("Rate", tableRow.getPropertySafelyAsString("Rate").toString());


                    db.insert(tableRow.getPropertySafelyAsString("Wilayah").toString(),
                            tableRow.getPropertySafelyAsString("Rate").toString(),
                            tableRow.getPropertySafelyAsString("Kode_Produk").toString(),
                            tableRow.getPropertySafelyAsString("Tipe").toString(),
                            tableRow.getPropertySafelyAsString("Exco").toString()
                    );
                }

            } catch (ArrayIndexOutOfBoundsException ex) {
                ex.printStackTrace();
                error = true;
            } catch (SocketTimeoutException ex) {
                ex.printStackTrace();
                error = true;
            } catch (Exception e) {
                e.printStackTrace();
                error = true;
            } finally {
                if (db != null)
                    db.close();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            try {
                if (error) {
//					Toast.makeText(getBaseContext(), "Retrive WellWoman Rate failed", Toast.LENGTH_SHORT).show();
                    bFinishOtomateAOG = false;
                    DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                    dbVersion.open();
                    dbVersion.setNeedSync("PremiAOGOtomate");
                    dbVersion.close();
                    //Log.d("Sync. Well Woman Rate", "Fail");
                    errorSync = true;
                } else {
                    bFinishOtomateAOG = true;
                    DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                    dbVersion.open();
                    dbVersion.setSuccessSync("PremiAOGOtomate");
                    dbVersion.close();
                    //Log.d("Sync. Well Woman Rate", "Success");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    private class KonveRateWS extends AsyncTask<String, Void, Void> {

        private Boolean error = false;
        private String errorMessage = "";

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            envelope_konve_rate = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope_konve_rate.implicitTypes = true;
            envelope_konve_rate.dotNet = true;
            androidHttpTransport = new HttpTransportSE(URL);
        }

        @Override
        protected Void doInBackground(String... params) {

            error = false;
            DBA_MASTER_KONVE_RATE db = null;

            try {

                SoapObject table = null;
                SoapObject tableRow = null;
                SoapObject responseBody = null;

                envelope_konve_rate.setOutputSoapObject(requestretrive_Konve_Rate);
                envelope_konve_rate.bodyOut = requestretrive_Konve_Rate;
                androidHttpTransport.call(SOAP_ACTION_KONVE_RATE, envelope_konve_rate);

                responseBody = (SoapObject) envelope_konve_rate.getResponse();
                responseBody = (SoapObject) responseBody.getProperty(1);

                if (responseBody.getPropertyCount() == 0) {
                    error = true;
                }
                table = (SoapObject) responseBody.getProperty(0);

                db = new DBA_MASTER_KONVE_RATE(getBaseContext());
                db.open();
                db.deleteAll();

                int iTotalDataFromWebService = table.getPropertyCount();

                for (int i = 0; i < iTotalDataFromWebService; i++) {
                    tableRow = (SoapObject) table.getProperty(i);

                    Log.d("JNP", tableRow.getPropertySafelyAsString("JNP").toString());
                    Log.d("Rate_Bawah", tableRow.getPropertySafelyAsString("Rate_Bawah").toString());


                    db.insert(Utility.ifAnytype(tableRow.getPropertySafelyAsString("JNP").toString()),
                            Utility.ifAnytype(tableRow.getPropertySafelyAsString("Wilayah").toString()),
                            Utility.ifAnytype(tableRow.getPropertySafelyAsString("Vehicle_Category").toString()),
                            Utility.ifAnytype(tableRow.getPropertySafelyAsString("Exco").toString()),
                            Utility.ifAnytype(tableRow.getPropertySafelyAsString("Rate_Bawah").toString()),
                            Utility.ifAnytype(tableRow.getPropertySafelyAsString("Rate_Tengah").toString()),
                            Utility.ifAnytype(tableRow.getPropertySafelyAsString("Rate_Atas").toString()),
                            Utility.ifAnytype(tableRow.getPropertySafelyAsString("Jaminan").toString()));
                }

            } catch (ArrayIndexOutOfBoundsException ex) {
                ex.printStackTrace();
                error = true;
            } catch (SocketTimeoutException ex) {
                ex.printStackTrace();
                error = true;
            } catch (Exception e) {
                e.printStackTrace();
                error = true;
            } finally {
                if (db != null)
                    db.close();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            try {
                if (error) {
//					Toast.makeText(getBaseContext(), "Retrive WellWoman Rate failed", Toast.LENGTH_SHORT).show();
                    bFinishKonveRate = false;
                    DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                    dbVersion.open();
                    dbVersion.setNeedSync("PremiRateKonve");
                    dbVersion.close();
                    //Log.d("Sync. Well Woman Rate", "Fail");
                    errorSync = true;
                } else {
                    bFinishKonveRate = true;
                    DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                    dbVersion.open();
                    dbVersion.setSuccessSync("PremiRateKonve");
                    dbVersion.close();
                    //Log.d("Sync. Well Woman Rate", "Success");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private class CarBrandTrukWS extends AsyncTask<String, Void, Void> {

        private Boolean error = false;
        private String errorMessage = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            envelope_car_brand_truk = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope_car_brand_truk.implicitTypes = true;
            envelope_car_brand_truk.dotNet = true;
            androidHttpTransport = new HttpTransportSE(URL);
        }

        @Override
        protected Void doInBackground(String... params) {

            error = false;
            DBA_MASTER_CAR_BRAND_TRUK db = null;

            try {

                SoapObject table = null;                        // Contains table of dataset that returned through SoapObject
                SoapObject tableRow = null;                     // Contains row of table
                SoapObject responseBody = null;                    // Contains XML content of dataset

                PropertyInfo param = new PropertyInfo();
                param.setName("conn");
                param.setValue("1");
                param.setType(String.class);

                requestretrive_car_brand_truk.addProperty(param);

                envelope_car_brand_truk.setOutputSoapObject(requestretrive_car_brand_truk);
                envelope_car_brand_truk.bodyOut = requestretrive_car_brand_truk;
                androidHttpTransport.call(SOAP_ACTION_CAR_BRAND_TRUK, envelope_car_brand_truk);

                // This step: get file XML
                responseBody = (SoapObject) envelope_car_brand_truk.getResponse();
                // remove information XML,only retrieved results that returned
                responseBody = (SoapObject) responseBody.getProperty(1);
                // get information XMl of tables that is returned

                if (responseBody.getPropertyCount() == 0) {
                    error = true;
                }
                table = (SoapObject) responseBody.getProperty(0);
                //Get information each row in table,0 is first row

                db = new DBA_MASTER_CAR_BRAND_TRUK(getBaseContext());
                db.open();
                db.deleteAll();

                int iTotalDataFromWebService = table.getPropertyCount();
                for (int i = 0; i < iTotalDataFromWebService; i++) {
                    tableRow = (SoapObject) table.getProperty(i);
                    Log.d("CARBRD", tableRow.getPropertySafelyAsString("CABRCDBRD").toString());
                    db.insert(tableRow.getPropertySafelyAsString("CABRCDBRD").toString(), tableRow.getPropertySafelyAsString("CABRDESC1").toString());
                }

            } catch (ArrayIndexOutOfBoundsException ex) {
                ex.printStackTrace();
                error = true;
            } catch (SocketTimeoutException ex) {
                ex.printStackTrace();
                error = true;
            } catch (Exception e) {
                e.printStackTrace();
                error = true;
            } finally {
                if (db != null)
                    db.close();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);
            DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);

            try {
                if (error) {
//					Toast.makeText(getBaseContext(), "Retrive car brand failed", Toast.LENGTH_SHORT).show();
                    bFinishCarBrandTruk = false;

                    dbVersion.open();
                    dbVersion.setNeedSync("CarBrandTruk");
                    dbVersion.close();

                    Log.d("Sync. Car Brand Truk", "Fail");
//					SplashActivity.this.finish();
                    errorSync = true;
                } else {
                    //pb.setProgress(pb.getProgress() + 10);
                    dbVersion.open();
                    dbVersion.setSuccessSync("CarBrandTruk");
                    dbVersion.close();

                    bFinishCarBrandTruk = true;
                    Log.d("Sync. Car Brand Truk", "Success");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    private class ProductSettingWS extends AsyncTask<String, Void, Void> {

        private Boolean error = false;
        private String errorMessage = "";

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            envelope_product_setting = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope_product_setting.implicitTypes = true;
            envelope_product_setting.dotNet = true;
            androidHttpTransport = new HttpTransportSE(URL);
        }

        @Override
        protected Void doInBackground(String... params) {

            error = false;
            DBA_MASTER_PRODUCT_SETTING db = null;

            try {

                SoapObject table = null;
                SoapObject tableRow = null;
                SoapObject responseBody = null;

                envelope_product_setting.setOutputSoapObject(requestretrive_product_setting);
                envelope_product_setting.bodyOut = requestretrive_product_setting;
                androidHttpTransport.call(SOAP_ACTION_PRODUCT_SETTING, envelope_product_setting);

                responseBody = (SoapObject) envelope_product_setting.getResponse();
                responseBody = (SoapObject) responseBody.getProperty(1);

                if (responseBody.getPropertyCount() == 0) {
                    error = true;
                }

                table = (SoapObject) responseBody.getProperty(0);

                db = new DBA_MASTER_PRODUCT_SETTING(getBaseContext());
                db.open();
                db.deleteAll();

                int iTotalDataFromWebService = table.getPropertyCount();

                for (int i = 0; i < iTotalDataFromWebService; i++) {
                    tableRow = (SoapObject) table.getProperty(i);
                    db.insert(tableRow.getPropertySafelyAsString("LOB").toString(),
                            tableRow.getPropertySafelyAsString("PRODUCT_NAME").toString(),
                            tableRow.getPropertySafelyAsString("IS_RENEWABLE").toString(),
                            Double.parseDouble(tableRow.getPropertySafelyAsString("MIN_TSI").toString()),
                            Double.parseDouble(tableRow.getPropertySafelyAsString("MAX_TSI").toString()),
                            Double.parseDouble(tableRow.getPropertySafelyAsString("MIN_PREMI").toString()),
                            Double.parseDouble(tableRow.getPropertySafelyAsString("MAX_PREMI").toString()),
                            tableRow.getPropertySafelyAsString("IS_DISCOUNTABLE").toString());
                }

            } catch (ArrayIndexOutOfBoundsException ex) {
                ex.printStackTrace();
                error = true;
            } catch (SocketTimeoutException ex) {
                ex.printStackTrace();
                error = true;
            } catch (Exception e) {
                e.printStackTrace();
                error = true;
            } finally {
                if (db != null)
                    db.close();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            try {
                if (error) {
//					Toast.makeText(getBaseContext(), "Retrive Product Setting failed", Toast.LENGTH_SHORT).show();
                    bFinishProductSetting = false;
                    DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                    dbVersion.open();
                    dbVersion.setNeedSync("ProductSetting");
                    dbVersion.close();
                    Log.d("Sync. Product Setting", "Fail");
                    errorSync = true;
                } else {
                    bFinishProductSetting = true;
                    DBA_TABLE_VERSION dbVersion = new DBA_TABLE_VERSION(SplashActivity.this);
                    dbVersion.open();
                    dbVersion.setSuccessSync("ProductSetting");
                    dbVersion.close();
                    Log.d("Sync. Product Setting", "Success");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    private class SPPAStatusWS extends AsyncTask<String, Void, Void> {

        private Boolean error = false;
        private String errorMessage = "";

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            envelope_sppa_status = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope_sppa_status.implicitTypes = true;
            envelope_sppa_status.dotNet = true;
            androidHttpTransport = new HttpTransportSE(URL);
        }

        @Override
        protected Void doInBackground(String... params) {

            error = false;
            DBA_PRODUCT_MAIN db = null;
            Cursor c = null;

            try {

                db = new DBA_PRODUCT_MAIN(getBaseContext());
                db.open();

                c = db.getRowsUnPaid();

                while (c.moveToNext()) {
                    long rowID = c.getLong(0);
                    String sppaNo = c.getString(5);

                    SoapObject responseBody = null;

                    Log.d("SPPA Status", sppaNo);

                    requestretrive_sppa_status.addProperty(Utility.GetPropertyInfo("SPPANo", sppaNo, String.class));
                    envelope_sppa_status.setOutputSoapObject(requestretrive_sppa_status);
                    androidHttpTransport.call(SOAP_ACTION_SPPA_STATUS, envelope_sppa_status);

                    responseBody = (SoapObject) envelope_sppa_status.bodyIn;
                    String flag = responseBody.getProperty(0).toString();

                    if (flag.equals("1")) {
                        db.delete(rowID);
                        Utility.DeleteDirectory(rowID);
                    }
                }
            } catch (ArrayIndexOutOfBoundsException ex) {
                ex.printStackTrace();
                error = true;
            } catch (SocketTimeoutException ex) {
                ex.printStackTrace();
                error = true;
            } catch (Exception e) {
                e.printStackTrace();
                error = true;
            } finally {
                if (c != null)
                    c.close();

                if (db != null)
                    db.close();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            try {
                if (error) {
                    Toast.makeText(getBaseContext(), "Sync. SPPA Paid failed", Toast.LENGTH_SHORT).show();
                    Log.d("Sync. SPPA Paid", "Fail");
                } else {
                    Log.d("Sync. SPPA Paid", "Success");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


}
