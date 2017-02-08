
package com.aca.amm;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aca.amm.CheckVehicleWS.CheckVehicle;
import com.aca.dal.Scalar;
import com.aca.database.DBA_MASTER_AGENT;
import com.aca.database.DBA_PRODUCT_ASRI;
import com.aca.database.DBA_PRODUCT_ASRI_SYARIAH;
import com.aca.database.DBA_PRODUCT_CARGO;
import com.aca.database.DBA_PRODUCT_CONVENSIONAL;
import com.aca.database.DBA_PRODUCT_DNO;
import com.aca.database.DBA_PRODUCT_EXECUTIVE_SAFE;
import com.aca.database.DBA_PRODUCT_MAIN;
import com.aca.database.DBA_PRODUCT_MEDISAFE;
import com.aca.database.DBA_PRODUCT_OTOMATE;
import com.aca.database.DBA_PRODUCT_OTOMATE_SYARIAH;
import com.aca.database.DBA_PRODUCT_PA_AMANAH;
import com.aca.database.DBA_PRODUCT_TOKO;
import com.aca.database.DBA_PRODUCT_TRAVEL_DOM;
import com.aca.database.DBA_PRODUCT_TRAVEL_SAFE;
import com.aca.database.DBA_PRODUCT_WELLWOMAN;
import com.aca.dbflow.MedisafeKuestioner;
import com.aca.dbflow.MedisafeKuestioner_Table;
import com.aca.util.Var;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;


public class SyncUnpaidActivity extends ControlListActivity implements InterfaceCustomer, InterfaceApproval, InterfaceFlagPaid, CheckVehicle {

    private ArrayList<HashMap<String, String>> custList = null;
    private DBA_PRODUCT_MAIN db;
    private Cursor c;

    private int[] mWordListItems;
    private String[] mWordListColumns;

    private ListView lv;

    private NumberFormat nf;

    private long pos;
    protected String tag = "Sync Unpaid";
    private Toast toast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_unpaid);

        nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);

        lv = getListView();

        int[] ListItems = {
                R.id.txtESPPANo,
                R.id.txtSyncProductName,
                R.id.txtSyncCustomerCode,
                R.id.txtSyncCustomerName,
                R.id.txtSyncTotalAmount,
                R.id.btnPaidSPPA,
                R.id.btnUnsyncPhoto};

        mWordListItems = ListItems;

        String[] ListColumns = {
                db.E_SPPA_NO,
                db.PRODUCT_NAME,
                db.CUSTOMER_CODE,
                db.CUSTOMER_NAME,
                db.TOTAL,
                db.CUSTOMER_CODE,
                db._id};

        mWordListColumns = ListColumns;

//		BindListView();

    }


    private void toast(String string) {
        // TODO Auto-generated method stub
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();

    }

    public static void newInstance() {
        new SyncUnpaidActivity();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sync_unpaid, menu);
        return true;
    }


    @Override
    public void getCustomerList(ArrayList<HashMap<String, String>> custList) {
        if (custList != null) {
            String noKTPString = custList.get(0).get("CUSTOMER_ID").toString();
            CheckWellwomanData cWellwomanData = new CheckWellwomanData(SyncUnpaidActivity.this, noKTPString);
            cWellwomanData.execute();
        } else {
            Utility.showCustomDialogInformation(this, "Informasi", "Gagal mendapatkan data customer");
        }
    }


    private void getCustomerData(String customerNo) {
        RetrieveCustomer retrieveCustomer = new RetrieveCustomer(SyncUnpaidActivity.this, customerNo);
        retrieveCustomer.execute();
        retrieveCustomer.customerInterface = this;
    }


    private void validasiKTPWellWoman(String customer_no) throws InterruptedException, ExecutionException, TimeoutException {
        getCustomerData(customer_no);
    }

    public static class approval {
        static String sppano;
        static long i;
        static double total;
        static String action;
        static Bundle bundle;
        static String product;
        static String customerNo;

    }

    @Override
    public void getFlagApproval(String flag) {


        if (approval.action.equals("VIEW.UNPAID")) {
            String statusString = "";

            if (flag.equals("1"))
                statusString = "APPROVED";
            else {
                statusString = "NOTAPPROVED";
            }
            startActivity(NextActivity(approval.bundle.getLong("BUNDLE1"),
                    approval.bundle.getString("BUNDLE2"),
                    approval.bundle.getString("BUNDLE3"),
                    approval.bundle.getString("BUNDLE4"),
                    approval.bundle.getString("BUNDLE5"),
                    statusString));
        } else {
            if (flag.equals("0"))
                Utility.showCustomDialogInformation(SyncUnpaidActivity.this, "Informasi", "SPPA ini belum disetujui");
            else if (flag.equals("2"))
                Utility.showCustomDialogInformation(SyncUnpaidActivity.this, "Informasi", "SPPA ini ditolak");
            else if (flag.equals("-1"))
                Utility.showCustomDialogInformation(SyncUnpaidActivity.this, "Informasi", "Checking validasi gagal. Hubungi IT ACA");
            else if (flag.equals("3")) {
//				 reSync(approval.i, approval.customerNo); // sppa id
                Utility.showCustomDialogInformation(SyncUnpaidActivity.this, "Informasi", "Dokumen belum lengkap. Hubungi IT ACA");
            } else if (flag.equals("4")) {
                Utility.showCustomDialogInformation(SyncUnpaidActivity.this, "Informasi", "SPPA butuh approval. Hubungi Agensi Mitraca");
            } else if (flag.equals("1")) {
                validasiFlagPaid();
            }
        }
    }

    private void reSync(long sppaID, String customerNo) {
        String pro_name = approval.product;
        String kodeproduk;
        try {
            if (pro_name.toUpperCase().equals("ASRI")) {
                SLProductASRI asri = new SLProductASRI(SyncUnpaidActivity.this, sppaID, SyncUnpaidActivity.this);
                asri.execute();
            } else if (pro_name.toUpperCase().equals("OTOMATE")) {
                kodeproduk = "03";

                HashMap<String, String> map = getData(sppaID, kodeproduk);
                CheckVehicleWS ws = new CheckVehicleWS(SyncUnpaidActivity.this, map);
                ws.mCallBack = SyncUnpaidActivity.this;
                ws.execute();
            } else if (pro_name.toUpperCase().equals("ASRISYARIAH")) {
                SLProductASRISyariah asri_syariah = new SLProductASRISyariah(SyncUnpaidActivity.this, sppaID, SyncUnpaidActivity.this);
                asri_syariah.execute();
            } else if (pro_name.toUpperCase().equals("OTOMATESYARIAH")) {
                kodeproduk = "09";

                HashMap<String, String> map = getData(sppaID, kodeproduk);
                CheckVehicleWS ws = new CheckVehicleWS(SyncUnpaidActivity.this, map);
                ws.mCallBack = SyncUnpaidActivity.this;
                ws.execute();
            } else if (pro_name.toUpperCase().equals("TRAVELSAFE")) {
                SLProductTravel travel = new SLProductTravel(SyncUnpaidActivity.this, sppaID, SyncUnpaidActivity.this);
                travel.execute();
            } else if (pro_name.toUpperCase().equals("TRAVELDOM")) {
                SLProductTravelDom traveldom = new SLProductTravelDom(SyncUnpaidActivity.this, sppaID, SyncUnpaidActivity.this);
                traveldom.execute();
            } else if (pro_name.toUpperCase().equals("MEDISAFE")) {
                SLProductMediaSafe mediasafe = new SLProductMediaSafe(SyncUnpaidActivity.this, sppaID, SyncUnpaidActivity.this);
                mediasafe.execute();
            } else if (pro_name.toUpperCase().equals("EXECUTIVESAFE")) {
                SLProductExecutive executive = new SLProductExecutive(SyncUnpaidActivity.this, sppaID, SyncUnpaidActivity.this);
                executive.execute();
            } else if (pro_name.toUpperCase().equals("TOKO")) {
                SLProductToko toko = new SLProductToko(SyncUnpaidActivity.this, sppaID, SyncUnpaidActivity.this);
                toko.execute();
            } else if (pro_name.toUpperCase().equals("PAAMANAH")) {
                SLProductPAAMANAH paamanah = new SLProductPAAMANAH(SyncUnpaidActivity.this, sppaID, SyncUnpaidActivity.this);
                paamanah.execute();
            } else if (pro_name.toUpperCase().equals("ACAMOBIL")) {
                SLProductACAMobil acamobil = new SLProductACAMobil(SyncUnpaidActivity.this, sppaID, SyncUnpaidActivity.this);
                acamobil.execute();
            } else if (pro_name.toUpperCase().equals("CARGO")) {
                SLProductCargo cargo = new SLProductCargo(SyncUnpaidActivity.this, sppaID, SyncUnpaidActivity.this);
                cargo.execute();
            } else if (pro_name.toUpperCase().equals("WELLWOMAN")) {
                SLProductWellWoman wellWoman = new SLProductWellWoman(SyncUnpaidActivity.this, sppaID, SyncUnpaidActivity.this);
                wellWoman.execute();
            } else if (pro_name.toUpperCase().equals("DNO")) {
                SLProductDNO dno = new SLProductDNO(SyncUnpaidActivity.this, sppaID, SyncUnpaidActivity.this);
                dno.execute();
            } else if (pro_name.toUpperCase().equals("KONVENSIONAL")) {
                SLProductKonvensional konve = new SLProductKonvensional(SyncUnpaidActivity.this, sppaID, SyncUnpaidActivity.this);
                konve.execute();
            }
        } finally {
            BindListView();
        }
    }


    @SuppressWarnings("finally")
    protected HashMap<String, String> getData(long SPPA_ID, String kodeproduk) {

        DBA_PRODUCT_MAIN dbProductMain = null;
        DBA_PRODUCT_OTOMATE dbProductOtomate = null;
        DBA_PRODUCT_OTOMATE_SYARIAH dbProductOtomateSyariah = null;
        DBA_MASTER_AGENT dbAgent = null;

        Cursor cProductMain = null;
        Cursor cProductOtomate = null;
        Cursor cAgent = null;

        HashMap<String, String> map = null;

        try {

            dbProductMain = new DBA_PRODUCT_MAIN(this);
            dbProductMain.open();
            cProductMain = dbProductMain.getRow(SPPA_ID);
            cProductMain.moveToFirst();

            dbAgent = new DBA_MASTER_AGENT(this);
            dbAgent.open();
            cAgent = dbAgent.getRow();
            cAgent.moveToFirst();

            dbProductOtomate = new DBA_PRODUCT_OTOMATE(this);
            dbProductOtomate.open();
            cProductOtomate = dbProductOtomate.getRow(SPPA_ID);
            cProductOtomate.moveToFirst();

            /*
            if (kodeproduk.equalsIgnoreCase("03")) {
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
			*/

            map = new HashMap<String, String>();
            map.put("prodId", kodeproduk);
            map.put("effDate", cProductMain.getString(12));
            map.put("ChassisNo", cProductOtomate.getString(9));
            map.put("EngineNo", cProductOtomate.getString(10));
            map.put("PlatNo1", cProductOtomate.getString(5));
            map.put("PlatNo2", cProductOtomate.getString(6));
            map.put("PlatNo3", cProductOtomate.getString(7));
            map.put("BranchNo", cAgent.getString(0));

        } catch (Exception ex) {
            ex.printStackTrace();
            map = null;
        } finally {
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


    public void validasiApproval() {
        GetFlagApprovalDokumen app = new GetFlagApprovalDokumen(SyncUnpaidActivity.this, approval.sppano);
        app.execute();
        app.interfaceApproval = this;
    }

    @SuppressWarnings("finally")
    private boolean cekTheFlag(long rowID) {
        DBA_PRODUCT_WELLWOMAN dba = null;
        boolean needApproved = false;

        try {
            dba = new DBA_PRODUCT_WELLWOMAN(SyncUnpaidActivity.this);
            dba.open();
            Cursor cursor = dba.getRow(rowID);

            if (cursor.moveToFirst()) {
                if (cursor.getString(7).equals("1") ||
                        cursor.getString(8).equals("1") ||
                        cursor.getString(9).equals("1") ||
                        cursor.getString(10).equals("1")) {
                    needApproved = true;
                } else {
                    needApproved = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dba != null)
                dba.close();

            return needApproved;
        }

    }


    @Override
    public void getFlagPaidApproval(String flag) {
//		starting #1 
/*

        if (BuildConfig.DEBUG)
            flag = "1";
*/

        if (flag.equals("1")) {
            Utility.showCustomDialogInformation(SyncUnpaidActivity.this, "Informasi", "SPPA ini sudah dibayar");

            DBA_PRODUCT_MAIN dba = null;
            try {

                Log.i("tag", "::getFlagPaidApproval:" + "sppano " + approval.sppano);

                String pro_name = approval.product;
                pos = approval.i;

                if (pro_name.toUpperCase().equals("ASRI")) {
                    DBA_PRODUCT_ASRI dbProduct = new DBA_PRODUCT_ASRI(getListView().getContext());
                    dbProduct.open();
                    dbProduct.delete(pos);
                    dbProduct.close();
                } else if (pro_name.toUpperCase().equals("OTOMATE")) {
                    DBA_PRODUCT_OTOMATE dbProduct = new DBA_PRODUCT_OTOMATE(getListView().getContext());
                    dbProduct.open();
                    dbProduct.delete(pos);
                    dbProduct.close();
                } else if (pro_name.toUpperCase().equals("ASRISYARIAH")) {
                    DBA_PRODUCT_ASRI_SYARIAH dbProduct = new DBA_PRODUCT_ASRI_SYARIAH(getListView().getContext());
                    dbProduct.open();
                    dbProduct.delete(pos);
                    dbProduct.close();
                } else if (pro_name.toUpperCase().equals("OTOMATESYARIAH")) {
                    DBA_PRODUCT_OTOMATE_SYARIAH dbProduct = new DBA_PRODUCT_OTOMATE_SYARIAH(getListView().getContext());
                    dbProduct.open();
                    dbProduct.delete(pos);
                    dbProduct.close();
                } else if (pro_name.toUpperCase().equals("TRAVELSAFE")) {
                    DBA_PRODUCT_TRAVEL_SAFE dbProduct = new DBA_PRODUCT_TRAVEL_SAFE(getListView().getContext());
                    dbProduct.open();
                    dbProduct.delete(pos);
                    dbProduct.close();
                } else if (pro_name.toUpperCase().equals("TRAVELDOM")) {
                    DBA_PRODUCT_TRAVEL_DOM dbProduct = new DBA_PRODUCT_TRAVEL_DOM(getListView().getContext());
                    dbProduct.open();
                    dbProduct.delete(pos);
                    dbProduct.close();
                } else if (pro_name.toUpperCase().equals("MEDISAFE")) {
                    DBA_PRODUCT_MEDISAFE dbProduct = new DBA_PRODUCT_MEDISAFE(getListView().getContext());
                    dbProduct.open();
                    dbProduct.delete(pos);
                    dbProduct.close();

                    new Select().from(MedisafeKuestioner.class)
                            .where(MedisafeKuestioner_Table.SppaNo.eq(pos+""))
                            .querySingle()
                            .delete();
                } else if (pro_name.toUpperCase().equals("EXECUTIVESAFE")) {
                    DBA_PRODUCT_EXECUTIVE_SAFE dbProduct = new DBA_PRODUCT_EXECUTIVE_SAFE(getListView().getContext());
                    dbProduct.open();
                    dbProduct.delete(pos);
                    dbProduct.close();
                } else if (pro_name.toUpperCase().equals("TOKO")) {
                    DBA_PRODUCT_TOKO dbProduct = new DBA_PRODUCT_TOKO(getListView().getContext());
                    dbProduct.open();
                    dbProduct.delete(pos);
                    dbProduct.close();
                } else if (pro_name.toUpperCase().equals("PAAMANAH")) {
                    DBA_PRODUCT_PA_AMANAH dbProduct = new DBA_PRODUCT_PA_AMANAH(getListView().getContext());
                    dbProduct.open();
                    dbProduct.delete(pos);
                    dbProduct.close();
                } else if (pro_name.toUpperCase().equals("CARGO")) {
                    DBA_PRODUCT_CARGO dbProduct = new DBA_PRODUCT_CARGO(getListView().getContext());
                    dbProduct.open();
                    dbProduct.delete(pos);
                    dbProduct.close();
                } else if (pro_name.toUpperCase().equals("WELLWOMAN")) {
                    DBA_PRODUCT_WELLWOMAN dbProduct = new DBA_PRODUCT_WELLWOMAN(getListView().getContext());
                    dbProduct.open();
                    dbProduct.delete(pos);
                    dbProduct.close();
                } else if (pro_name.toUpperCase().equals("DNO")) {
                    DBA_PRODUCT_DNO dbProduct = new DBA_PRODUCT_DNO(getListView().getContext());
                    dbProduct.open();
                    dbProduct.delete(pos);
                    dbProduct.close();
                } else if (pro_name.toUpperCase().equals("KONVENSIONAL")) {
                    DBA_PRODUCT_CONVENSIONAL dbProduct = new DBA_PRODUCT_CONVENSIONAL(getListView().getContext());
                    dbProduct.open();
                    dbProduct.delete(pos);
                    dbProduct.close();
                }
                else if (pro_name.toUpperCase().equalsIgnoreCase(Var.LABBAIK)) {
                    DBA_PRODUCT_TRAVEL_SAFE dbProduct = new DBA_PRODUCT_TRAVEL_SAFE(getListView().getContext());
                    dbProduct.open();
                    dbProduct.delete(pos);
                    dbProduct.close();
                }


                dba = new DBA_PRODUCT_MAIN(getListView().getContext());
                dba.open();
                dba.delete(pos);

                Utility.DeleteDirectory(pos);
                BindListView();
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (dba != null)
                    dba.close();
            }
        } else {
            Intent i = new Intent(getBaseContext(), PaidSPPAActivity.class);
            Bundle b = new Bundle();
            b.putString("sppano", approval.sppano);
            b.putDouble("amount", approval.total);
            i.putExtras(b);
            startActivity(i);
            SyncUnpaidActivity.this.finish();
        }

    }


    private void validasiFlagPaid() {
        GetFlagPaidDokumen app;

        app = new GetFlagPaidDokumen(SyncUnpaidActivity.this, approval.sppano);
        app.interfaceFlagPaid = this;
        app.execute();

    }

    private OnClickListener PaidSPPA(final long i, final String sppano, final double total, final String product_name, final String customer_no, final String customer_name) {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Scalar.isOtomateSppa(SyncUnpaidActivity.this, i)) {
                    if (!Scalar.isNewSppa(SyncUnpaidActivity.this, i)) {
                        toast = Toast.makeText(SyncUnpaidActivity.this, "Proses pembayaran tidak bisa diproses untuk sppa otomate yang dibuat pada versi aplikasi sebelumnya", Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }
                }
                boolean error = false;
                String errorMessage = "";

                DBA_MASTER_AGENT dbAgent = new DBA_MASTER_AGENT(SyncUnpaidActivity.this);
                Cursor c = null;

                try {
                    Log.d("id row", "id = " + i);


                    dbAgent.open();
                    c = dbAgent.getRow();

                    if (!c.moveToFirst())
                        return;

//						Testing 2, live 1

                    if (c.getString(8).equals("2")) {
                        Utility.showCustomDialogInformation(SyncUnpaidActivity.this, "Informasi", "User testing tidak memiliki akses untuk proses bayar");
                        return;
                    }

                    approval.sppano = sppano;
                    approval.i = i;
                    approval.total = total;
                    approval.action = "PAY";
                    approval.product = product_name;
                    approval.customerNo = customer_no;

                    if (product_name.equals("WELLWOMAN")) {
                        validasiKTPWellWoman(customer_no);
                    } else {
                        validasiApproval();
                    }
                } catch (Exception e) {
                    error = true;
                    e.printStackTrace();
                    errorMessage = new MasterExceptionClass(e).getException();
                } finally {
                    if (error)
                        Utility.showCustomDialogInformation(getListView().getContext(), "Warning",
                                errorMessage);
                    if (dbAgent != null)
                        dbAgent.close();

                    if (c != null)
                        c.close();

                }
            }
        };
    }


    @Override
    public void onBackPressed() {
        Back();
    }

    private void Back() {
        Intent i = new Intent(getBaseContext(), FirstActivity.class);
        startActivity(i);
        this.finish();
    }


    private Intent NextActivity(long id, String act, String action, String cust_code, String cust_name, String status) {
        Intent i = null;
        Bundle b = new Bundle();

        b.putString("PRODUCT_ACTION", action);
        b.putLong("SPPA_ID", id);
        b.putString("PRODUCT_TYPE", act);
        b.putString("STATUS", status);

        Log.d("product action ", action);
        Log.d("PRODUCT_TYPE ", act);
        Log.d("product STATUS ", status);

        try {
            if (act.equalsIgnoreCase("OTOMATE")) {
                i = new Intent(getBaseContext(), FillOtomateActivity.class);
                i.putExtras(b);
                this.finish();
            } else if (act.equalsIgnoreCase("TRAVELDOM")) {
                i = new Intent(getBaseContext(), FillTravelDomActivity.class);
                i.putExtras(b);
                this.finish();
            } else if (act.equalsIgnoreCase("TRAVELSAFE")) {
                i = new Intent(getBaseContext(), FillTravelActivity.class);
                i.putExtras(b);
                this.finish();
            } else if (act.equalsIgnoreCase("TOKO")) {
                i = new Intent(getBaseContext(), FillTokoActivity.class);
                i.putExtras(b);
                this.finish();
            } else if (act.equalsIgnoreCase("MEDISAFE")) {
                i = new Intent(getBaseContext(), FillMedisafe.class);
                i.putExtras(b);
                this.finish();
            } else if (act.equalsIgnoreCase("ASRI")) {
                i = new Intent(getBaseContext(), FillAsriActivity.class);
                i.putExtras(b);
                this.finish();
            } else if (act.equalsIgnoreCase("ASRISYARIAH")) {
                i = new Intent(getBaseContext(), FillAsriSyariahActivity.class);
                i.putExtras(b);
                this.finish();
            } else if (act.equalsIgnoreCase("OTOMATESYARIAH")) {
                i = new Intent(getBaseContext(), FillOtomateSyariahActivity.class);
                i.putExtras(b);
                this.finish();
            } else if (act.equalsIgnoreCase("EXECUTIVESAFE")) {
                i = new Intent(getBaseContext(), FillExecutiveActivity.class);
                i.putExtras(b);
                this.finish();
            } else if (act.equalsIgnoreCase("PAAMANAH")) {
                i = new Intent(getBaseContext(), FillPAAmanahActivity.class);
                i.putExtras(b);
                this.finish();
            } else if (act.equalsIgnoreCase("ACAMOBIL")) {
                i = new Intent(getBaseContext(), FillACAMobilActivity.class);
                i.putExtras(b);
                this.finish();
            } else if (act.equalsIgnoreCase("CARGO")) {
                i = new Intent(getBaseContext(), FillCargoActivity.class);
                i.putExtras(b);
                this.finish();
            } else if (act.equalsIgnoreCase("WELLWOMAN")) {
                i = new Intent(getBaseContext(), FillWellWomanActivity.class);
                i.putExtras(b);
                this.finish();
            } else if (act.equalsIgnoreCase("DNO")) {
                i = new Intent(getBaseContext(), FillDNOActivity.class);
                i.putExtras(b);
                this.finish();
            } else if (act.equalsIgnoreCase("KONVENSIONAL")) {
                i = new Intent(getBaseContext(), FillKonvensionalActivity.class);
                b.putString("TIPE_KONVENSIONAL", "");
                i.putExtras(b);
                this.finish();
            }else if (act.equalsIgnoreCase(Var.LABBAIK)) {
                i = new Intent(getBaseContext(), FillLabbaikActivity.class);
                i.putExtras(b);
                this.finish();
            }

        } catch (Exception ex) {
            ex.printStackTrace();

        }

        return i;
    }


    protected OnClickListener syncPhoto(final long sppaID) {
        // TODO Auto-generated method stub
        return new OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("id row", "id = " + sppaID);
                new SyncImage(SyncUnpaidActivity.this, sppaID, SyncUnpaidActivity.this).execute();


            }
        };
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        Log.i("sync unpaid", "on resume");
        BindListView();

    }

    public void showReSync(String newSPPA) {
        Utility.showCustomDialogInformation(this, "Silahkan tap tombol BAYAR kembali", "Nomor SPPA anda menjadi " + newSPPA);
    }


    public void BindListView() {
        SimpleCursorAdapter mCursorAdapter = null;

        Log.i("sync unpaid", "bind list view");
        try {
            db = new DBA_PRODUCT_MAIN(this);
            db.open();

            c = db.getRowsUnPaid();
            c.moveToFirst();

            mCursorAdapter = new SimpleCursorAdapter(
                    this,
                    R.layout.list_item_sync_unpaid,
                    c,
                    mWordListColumns,
                    mWordListItems,
                    0) {
                @Override
                public void setViewText(TextView v, String text) {
                    switch (v.getId()) {
                        case R.id.txtESPPANo:
                            super.setViewText(v, c.getString(5).toUpperCase());
                            break;
                        case R.id.txtSyncProductName:
                            String productName = Scalar.getProdukName(SyncUnpaidActivity.this, c.getString(3), c.getLong(0)).toUpperCase();
                            super.setViewText(v, productName);
                            break;
                        case R.id.txtSyncCustomerName:
                            super.setViewText(v, c.getString(2).toUpperCase());
                            break;
                        case R.id.txtSyncTotalAmount:
                            super.setViewText(v, nf.format((c.getDouble(9))));
                            break;
                        default:
                            super.setViewText(v, text);
                    }
                }

                @Override
                public void setViewImage(ImageView i, String text) {
                    Log.d("id row", "id = " + c.getLong(0));
                    switch (i.getId()) {
                        case R.id.btnPaidSPPA:
                            i.setOnClickListener(PaidSPPA(c.getLong(0), c.getString(5), c.getDouble(9), c.getString(3), c.getString(1), c.getString(2)));

                            if (!TextUtils.isEmpty(c.getString(23))) {
                                if (c.getString(23).equals("FALSE")) {
                                    i.setEnabled(false);
                                } else {
                                    i.setEnabled(true);
                                }
                            }
                            break;

                        case R.id.btnUnsyncPhoto:
                            i.setOnClickListener(syncPhoto(c.getLong(0)));

                            if (!TextUtils.isEmpty(c.getString(23)))
                                if (c.getString(23).equals("FALSE")) {
                                    i.setVisibility(View.VISIBLE);
                                } else {
                                    i.setVisibility(View.GONE);
                                }

                            break;

                        default:
                            super.setViewImage(i, text);
                    }
                }
            };

            lv.setAdapter(mCursorAdapter);

            lv.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long pos) {

                    DBA_PRODUCT_MAIN dba = null;
                    Cursor cx = null;

                    try {
                        dba = new DBA_PRODUCT_MAIN(SyncUnpaidActivity.this);
                        dba.open();

                        cx = dba.getRow(pos);
                        cx.moveToFirst();

                        String productNameString = cx.getString(3);
                        Log.d("product name", productNameString);

                        approval.bundle = new Bundle();
                        approval.bundle.putLong("BUNDLE1", pos);
                        approval.bundle.putString("BUNDLE2", cx.getString(3));
                        approval.bundle.putString("BUNDLE3", "VIEW.UNPAID");
                        approval.bundle.putString("BUNDLE4", cx.getString(1));
                        approval.bundle.putString("BUNDLE5", cx.getString(2));

                        approval.action = "VIEW.UNPAID";
                        approval.sppano = cx.getString(5);

                        if (productNameString.equals("OTOMATE") ||
                            productNameString.equals("OTOMATESYARIAH") ||
                            productNameString.equals("DNO")) {
                            validasiApproval();
                        } else if (productNameString.equals("WELLWOMAN")) {
                            boolean needApprove = cekTheFlag(pos);

                            if (needApprove)
                                validasiApproval();
                            else {
                                startActivity(NextActivity(approval.bundle.getLong("BUNDLE1"),
                                        approval.bundle.getString("BUNDLE2"),
                                        approval.bundle.getString("BUNDLE3"),
                                        approval.bundle.getString("BUNDLE4"),
                                        approval.bundle.getString("BUNDLE5"),
                                        "NOTAPPROVED"));
                            }
                        } else {
                            startActivity(NextActivity(
                                    approval.bundle.getLong("BUNDLE1"),
                                    approval.bundle.getString("BUNDLE2"),
                                    approval.bundle.getString("BUNDLE3"),
                                    approval.bundle.getString("BUNDLE4"),
                                    approval.bundle.getString("BUNDLE5"),
                                    "NOTAPPROVED"));
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } finally {
                        if (cx != null)
                            cx.close();

                        if (dba != null)
                            dba.close();
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (db != null)
                db.close();
        }
    }


    @Override
    public void CheckVehicleListener(Boolean result) {
        if (result) {
            SLProductOtomate otomate = new SLProductOtomate(this, approval.i, SyncUnpaidActivity.this);
            otomate.execute();
/*

			if (approval.product.equalsIgnoreCase("OTOMATE")) {

				SLProductOtomate otomate = new SLProductOtomate(this ,approval.i , SyncUnpaidActivity.this);
				otomate.execute();
			}
			else {
				SLProductOtomateSyariah otomate_syariah = new SLProductOtomateSyariah(this, approval.i, SyncUnpaidActivity.this);
				otomate_syariah.execute();				
			}			
*/
        } else {

        }

    }


}
